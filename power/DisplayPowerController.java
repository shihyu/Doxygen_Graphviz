/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.power;

import com.android.server.LightsService;
import com.android.server.TwilightService;
import com.android.server.TwilightService.TwilightState;
import com.android.server.display.DisplayManagerService;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.FloatMath;
import android.util.Slog;
import android.util.Spline;
import android.util.TimeUtils;

import java.io.PrintWriter;

// ++ [framework]
import com.htc.htcjavaflag.HtcBuildFlag;
// -- [framwork]

// ++ [framework] Workaround for M7 p-sensor problem after ending call
import com.htc.customization.HtcCustomizationManager;
import com.htc.customization.HtcCustomizationReader;
// -- [framework] Workaround for M7 p-sensor problem after ending call

/**
 * Controls the power state of the display.
 *
 * Handles the proximity sensor, light sensor, and animations between states
 * including the screen off animation.
 *
 * This component acts independently of the rest of the power manager service.
 * In particular, it does not share any state and it only communicates
 * via asynchronous callbacks to inform the power manager that something has
 * changed.
 *
 * Everything this class does internally is serialized on its handler although
 * it may be accessed by other threads from the outside.
 *
 * Note that the power manager service guarantees that it will hold a suspend
 * blocker as long as the display is not ready.  So most of the work done here
 * does not need to worry about holding a suspend blocker unless it happens
 * independently of the display ready signal.
 *
 * For debugging, you can make the electron beam and brightness animations run
 * slower by changing the "animator duration scale" option in Development Settings.
 */
final class DisplayPowerController {
    private static final String TAG = "XAN-DPC";

    // ++ [framework] Add master debug control.
    private static final boolean DEBUG_ON = HtcBuildFlag.Htc_DEBUG_flag;
    // -- [framework] Add master debug control.

    private static boolean DEBUG = false;
    private static final boolean DEBUG_PRETEND_PROXIMITY_SENSOR_ABSENT = false;
    private static final boolean DEBUG_PRETEND_LIGHT_SENSOR_ABSENT = false;

    // If true, uses the electron beam on animation.
    // We might want to turn this off if we cannot get a guarantee that the screen
    // actually turns on and starts showing new content after the call to set the
    // screen state returns.  Playing the animation can also be somewhat slow.
    private static final boolean USE_ELECTRON_BEAM_ON_ANIMATION = false;

    // ++ [framework] Disable electron beam off animation
    private static final boolean USE_ELECTRON_BEAM_OFF_ANIMATION = false;
    // -- [framework] Disable electron beam off animation

    // If true, enables the use of the screen auto-brightness adjustment setting.
    private static final boolean USE_SCREEN_AUTO_BRIGHTNESS_ADJUSTMENT =
            PowerManager.useScreenAutoBrightnessAdjustmentFeature();

    // The maximum range of gamma adjustment possible using the screen
    // auto-brightness adjustment setting.
    private static final float SCREEN_AUTO_BRIGHTNESS_ADJUSTMENT_MAX_GAMMA = 3.0f;

    // The minimum reduction in brightness when dimmed.
    private static final int SCREEN_DIM_MINIMUM_REDUCTION = 10;

    // If true, enables the use of the current time as an auto-brightness adjustment.
    // The basic idea here is to expand the dynamic range of auto-brightness
    // when it is especially dark outside.  The light sensor tends to perform
    // poorly at low light levels so we compensate for it by making an
    // assumption about the environment.
    private static final boolean USE_TWILIGHT_ADJUSTMENT =
            PowerManager.useTwilightAdjustmentFeature();

    // Specifies the maximum magnitude of the time of day adjustment.
    private static final float TWILIGHT_ADJUSTMENT_MAX_GAMMA = 1.5f;

    // The amount of time after or before sunrise over which to start adjusting
    // the gamma.  We want the change to happen gradually so that it is below the
    // threshold of perceptibility and so that the adjustment has maximum effect
    // well after dusk.
    private static final long TWILIGHT_ADJUSTMENT_TIME = DateUtils.HOUR_IN_MILLIS * 2;

    private static final int ELECTRON_BEAM_ON_ANIMATION_DURATION_MILLIS = 250;
    private static final int ELECTRON_BEAM_OFF_ANIMATION_DURATION_MILLIS = 400;

    private static final int MSG_UPDATE_POWER_STATE = 1;
    private static final int MSG_PROXIMITY_SENSOR_DEBOUNCED = 2;
    private static final int MSG_LIGHT_SENSOR_DEBOUNCED = 3;
    //[++framework] Modify the brightness flow for setSmoothBacklight function
    private static final int MSG_UPDATE_BRIGHTNESS_STATE = 4;
    private static final int MSG_UPDATE_BRIGHTNESS_STATE_FOR_CAMERA = 5;
    //[--framework] Modify the brightness flow for setSmoothBacklight function
    //[++framework] Rework Button light mechanism
    private static final int MSG_BUTTON_LIGHT_DEBOUNCED = 6;
    //[--framework] Rework Button light mechanism
    // ++ [framework] Workaround for M7 p-sensor problem after ending call
    private static final int MSG_DELAY_SCREEN_ON_DURATION = 7;
    // -- [framework] Workaround for M7 p-sensor problem after ending call

    //[++framework] Add mode control mechanism
    private static final int BRIGHTNESS_ANIMATION_CAMERA_LEAVE_MODE = 1;
    //[--framework] Add mode control mechanism

    private static final int PROXIMITY_UNKNOWN = -1;
    private static final int PROXIMITY_NEGATIVE = 0;
    private static final int PROXIMITY_POSITIVE = 1;

    // Proximity sensor debounce delay in milliseconds for positive or negative transitions.
    private static int PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY = 0;
    private static int PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY = 250;

    // Trigger proximity if distance is less than 5 cm.
    private static final float TYPICAL_PROXIMITY_THRESHOLD = 5.0f;

    // Light sensor event rate in milliseconds.
    private static final int LIGHT_SENSOR_RATE_MILLIS = 1000;

    // A rate for generating synthetic light sensor events in the case where the light
    // sensor hasn't reported any new data in a while and we need it to update the
    // debounce filter.  We only synthesize light sensor measurements when needed.
    private static final int SYNTHETIC_LIGHT_SENSOR_RATE_MILLIS =
            LIGHT_SENSOR_RATE_MILLIS * 2;

    // Brightness animation ramp rate in brightness units per second.
    private static final int BRIGHTNESS_RAMP_RATE_FAST = 200;
    private static final int BRIGHTNESS_RAMP_RATE_SLOW = 40;

    // ++ [framework] To support customized animation delay
    private static final int ANIM_MODE_GOOGLE = 0;
    private static final int ANIM_MODE_HTC    = 1;

    private static final String SYS_PROP_ANIMATION_MODE     = "persist.htc.pms.anim.mode";

    private static final String SYS_PROP_BRIGHTEN_DEBOUNCE  = "persist.htc.pms.brighten.ls";
    private static final String SYS_PROP_DARKEN_DEBOUNCE    = "persist.htc.pms.darken.ls";
    private static final String SYS_PROP_BRIGHTEN_DURATION  = "persist.htc.pms.brighten.dur";
    private static final String SYS_PROP_DARKEN_DURATION    = "persist.htc.pms.darken.dur";

    private static int ANIMATION_MODE = ANIM_MODE_HTC;
    private static int AUTOBRIGHTNESS_ANIM_DURATION = 500;
    private static int AUTODIMNESS_ANIM_DURATION = 500;
    // -- [framework] To support customized animation delay

    // IIR filter time constants in milliseconds for computing two moving averages of
    // the light samples.  One is a long-term average and the other is a short-term average.
    // We can use these filters to assess trends in ambient brightness.
    // The short term average gives us a filtered but relatively low latency measurement.
    // The long term average informs us about the overall trend.
    private static final long SHORT_TERM_AVERAGE_LIGHT_TIME_CONSTANT = 1000;
    private static final long LONG_TERM_AVERAGE_LIGHT_TIME_CONSTANT = 5000;

    // Stability requirements in milliseconds for accepting a new brightness
    // level.  This is used for debouncing the light sensor.  Different constants
    // are used to debounce the light sensor when adapting to brighter or darker environments.
    // This parameter controls how quickly brightness changes occur in response to
    // an observed change in light level that exceeds the hysteresis threshold.
    private static long BRIGHTENING_LIGHT_DEBOUNCE = 2000;
    private static long DARKENING_LIGHT_DEBOUNCE = 8000;
    //[++framework] Rework Button light mechanism
    private static long DARKENING_BUTTON_LIGHT_DEBOUNCE = 2000;
    //[--framework] Rework Button light mechanism


    // Hysteresis constraints for brightening or darkening.
    // The recent lux must have changed by at least this fraction relative to the
    // current ambient lux before a change will be considered.
    private static final float BRIGHTENING_LIGHT_HYSTERESIS = 0.10f;
    private static final float DARKENING_LIGHT_HYSTERESIS = 0.20f;

    private final Object mLock = new Object();

    // Notifier for sending asynchronous notifications.
    private final Notifier mNotifier;

    // The display suspend blocker.
    // Held while there are pending state change notifications.
    private final SuspendBlocker mDisplaySuspendBlocker;

    // The display blanker.
    private final DisplayBlanker mDisplayBlanker;

    // Our handler.
    private final DisplayControllerHandler mHandler;

    // Asynchronous callbacks into the power manager service.
    // Only invoked from the handler thread while no locks are held.
    private final Callbacks mCallbacks;
    private Handler mCallbackHandler;

    // The lights service.
    private final LightsService mLights;

    // The twilight service.
    private final TwilightService mTwilight;

    // The display manager.
    private final DisplayManagerService mDisplayManager;

    // The sensor manager.
    private final SensorManager mSensorManager;

    // The proximity sensor, or null if not available or needed.
    private Sensor mProximitySensor;

    // The light sensor, or null if not available or needed.
    private Sensor mLightSensor;

    // The dim screen brightness.
    private final int mScreenBrightnessDimConfig;

    // The minimum allowed brightness.
    private final int mScreenBrightnessRangeMinimum;

    // The maximum allowed brightness.
    private final int mScreenBrightnessRangeMaximum;

    // True if auto-brightness should be used.
    private boolean mUseSoftwareAutoBrightnessConfig;

    // The auto-brightness spline adjustment.
    // The brightness values have been scaled to a range of 0..1.
    private Spline mScreenAutoBrightnessSpline;

    // Amount of time to delay auto-brightness after screen on while waiting for
    // the light sensor to warm-up in milliseconds.
    // May be 0 if no warm-up is required.
    private int mLightSensorWarmUpTimeConfig;

    // True if we should fade the screen while turning it off, false if we should play
    // a stylish electron beam animation instead.
    private boolean mElectronBeamFadesConfig;

    // The pending power request.
    // Initially null until the first call to requestPowerState.
    // Guarded by mLock.
    private DisplayPowerRequest mPendingRequestLocked;

    // True if a request has been made to wait for the proximity sensor to go negative.
    // Guarded by mLock.
    private boolean mPendingWaitForNegativeProximityLocked;

    // True if the pending power request or wait for negative proximity flag
    // has been changed since the last update occurred.
    // Guarded by mLock.
    private boolean mPendingRequestChangedLocked;

    // Set to true when the important parts of the pending power request have been applied.
    // The important parts are mainly the screen state.  Brightness changes may occur
    // concurrently.
    // Guarded by mLock.
    private boolean mDisplayReadyLocked;

    // Set to true if a power state update is required.
    // Guarded by mLock.
    private boolean mPendingUpdatePowerStateLocked;

    /* The following state must only be accessed by the handler thread. */

    // The currently requested power state.
    // The power controller will progressively update its internal state to match
    // the requested power state.  Initially null until the first update.
    private DisplayPowerRequest mPowerRequest;

    // The current power state.
    // Must only be accessed on the handler thread.
    private DisplayPowerState mPowerState;

    // True if the device should wait for negative proximity sensor before
    // waking up the screen.  This is set to false as soon as a negative
    // proximity sensor measurement is observed or when the device is forced to
    // go to sleep by the user.  While true, the screen remains off.
    private boolean mWaitingForNegativeProximity;

    // The actual proximity sensor threshold value.
    private float mProximityThreshold;

    // Set to true if the proximity sensor listener has been registered
    // with the sensor manager.
    private boolean mProximitySensorEnabled;

    // The debounced proximity sensor state.
    private int mProximity = PROXIMITY_UNKNOWN;

    // The raw non-debounced proximity sensor state.
    private int mPendingProximity = PROXIMITY_UNKNOWN;
    private long mPendingProximityDebounceTime = -1; // -1 if fully debounced

    // True if the screen was turned off because of the proximity sensor.
    // When the screen turns on again, we report user activity to the power manager.
    private boolean mScreenOffBecauseOfProximity;

    // True if the screen on is being blocked.
    private boolean mScreenOnWasBlocked;

    // The elapsed real time when the screen on was blocked.
    private long mScreenOnBlockStartRealTime;

    // Set to true if the light sensor is enabled.
    private boolean mLightSensorEnabled;

    // The time when the light sensor was enabled.
    private long mLightSensorEnableTime;

    // The currently accepted nominal ambient light level.
    private float mAmbientLux;

    // True if mAmbientLux holds a valid value.
    private boolean mAmbientLuxValid;

    // The ambient light level threshold at which to brighten or darken the screen.
    private float mBrighteningLuxThreshold;
    private float mDarkeningLuxThreshold;

    // The most recent light sample.
    private float mLastObservedLux;

    // The time of the most light recent sample.
    private long mLastObservedLuxTime;

    // The number of light samples collected since the light sensor was enabled.
    private int mRecentLightSamples;

    // The long-term and short-term filtered light measurements.
    private float mRecentShortTermAverageLux;
    private float mRecentLongTermAverageLux;

    // The direction in which the average lux is moving relative to the current ambient lux.
    //    0 if not changing or within hysteresis threshold.
    //    1 if brightening beyond hysteresis threshold.
    //   -1 if darkening beyond hysteresis threshold.
    private int mDebounceLuxDirection;

    // The time when the average lux last changed direction.
    private long mDebounceLuxTime;

    // The screen brightness level that has been chosen by the auto-brightness
    // algorithm.  The actual brightness should ramp towards this value.
    // We preserve this value even when we stop using the light sensor so
    // that we can quickly revert to the previous auto-brightness level
    // while the light sensor warms up.
    // Use -1 if there is no current auto-brightness value available.
    private int mScreenAutoBrightness = -1;

    // The last screen auto-brightness gamma.  (For printing in dump() only.)
    private float mLastScreenAutoBrightnessGamma = 1.0f;

    // True if the screen auto-brightness value is actually being used to
    // set the display brightness.
    private boolean mUsingScreenAutoBrightness;

    // Animators.
    private ObjectAnimator mElectronBeamOnAnimator;
    private ObjectAnimator mElectronBeamOffAnimator;
    private RampAnimator<DisplayPowerState> mScreenBrightnessRampAnimator;

    // Twilight changed.  We might recalculate auto-brightness values.
    private boolean mTwilightChanged;

    // ++ [framework] JB MR1 4.2 porting from JB 4.1.1

    // ++ [framework] API collector for HtcAutoBrightnessCtrl
    PowerManagerService.PMSInternalAPI mPMSInternalAPI;
    DPCInternalAPI mDPCInternalAPI;
    // -- [framework] API collector for HtcAutoBrightnessCtrl

    // ++ [framework] Condition controlled auto brightness feature, 2012/01/04
    private HtcAutoBrightnessCtrl mHtcAutoBrightnessCtrl;
    // -- [framework] Condition controlled auto brightness feature, 2012/01/04

    // ++ [framework] Workaround for M7 p-sensor problem.
    private HtcAngleDetector mHtcAngleDetector;
    // -- [fraemwork] Workaround for M7 p-sensor problem.

    // ++ [framework] To support customized animation delay
    private int mCustomizeDuration = -1;
    // -- [framework] To support customized animation delay

    // ++ [framework] Keep lux in global variable to reuse it
    private int[] mLux;
    // -- [framework] Keep lux in global variable to reuse it

    // ++ [framework] Add button light control mechanism
    private int mButtonValue = 0;
    // -- [framework] Add button light control mechanism

    // -- [framework] JB MR1 4.2 porting from JB 4.1.1

    // ++ [framework] If AngleDetector is enable, use the new debounce value for positive or negative transitions
    private static boolean IS_ANGLE_DETECTOR_SUPPORT = false;
    // AngleDetector proximity sensor debounce delay in milliseconds for positive or negative transitions.
    private static final int ANGLEDETECTOR_PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY = 0;
    private static final int ANGLEDETECTOR_PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY = 0;
    // -- [framework] If AngleDetector is enable, use the new debounce value for positive or negative transitions

    // ++ [framework] Workaround for M7 p-sensor problem after ending call
    private static int DELAY_SCREEN_ON_FROM_NEAR_DURATION = 30000; // unit ms
    private static int DELAY_SCREEN_ON_FROM_FAR_ANGLE_DURATION = 2000; // unit ms
    private static int DELAY_SCREEN_ON_FROM_FAR_TOTAL_DURATION = 30000; // unit ms

    private static final int DELAY_SCREEN_UNKNOWN = -1;
    private static final int DELAY_SCREEN_FAR = 0;
    private static final int DELAY_SCREEN_NEAR = 1;

    private int mDelayScreenOnReason = DELAY_SCREEN_UNKNOWN;
    private int mProximityAfterEndCall = PROXIMITY_UNKNOWN;

    private HtcCustomizationReader mHtcCustomizationReader = null;

    private boolean checkHtcCustomizationReader() {
        if (mHtcCustomizationReader == null) {
            try {
                HtcCustomizationManager customizationManger = HtcCustomizationManager.getInstance();
                if (customizationManger == null) {
                    Slog.d(TAG, "customizationManger = null");
                    return false;
                }

                mHtcCustomizationReader = customizationManger.getCustomizationReader("Android_Core_Framework",
                    HtcCustomizationManager.READER_TYPE_XML, false);

                if (mHtcCustomizationReader == null) {
                    Slog.d(TAG, "mHtcCustomizationReader = null");
                    return false;
                }

            }
            catch (Exception e) {
                Slog.e(TAG, "Cannot get HtcCustomization, reason = " + e.toString());
                return false;
            }
        }

        return true;
    }
    // -- [framework] Workaround for M7 p-sensor problem after ending call

    /**
     * Creates the display power controller.
     */
    public DisplayPowerController(Looper looper, Context context, Notifier notifier,
            LightsService lights, TwilightService twilight, SensorManager sensorManager,
            DisplayManagerService displayManager,
            SuspendBlocker displaySuspendBlocker, DisplayBlanker displayBlanker,
            // ++ [framework] Add PMSInternalAPI
            Callbacks callbacks, Handler callbackHandler, PowerManagerService.PMSInternalAPI pmsInternalAPI) {
            // -- [framework] Add PMSInternalAPI
        mHandler = new DisplayControllerHandler(looper);
        mNotifier = notifier;
        mDisplaySuspendBlocker = displaySuspendBlocker;
        mDisplayBlanker = displayBlanker;
        mCallbacks = callbacks;
        mCallbackHandler = callbackHandler;

        // ++ [framework] Add PMSInternalAPI
        mPMSInternalAPI = pmsInternalAPI;
        // -- [framework] Add PMSInternalAPI

        mLights = lights;
        mTwilight = twilight;
        mSensorManager = sensorManager;
        mDisplayManager = displayManager;

        final Resources resources = context.getResources();

        mScreenBrightnessDimConfig = clampAbsoluteBrightness(resources.getInteger(
                com.android.internal.R.integer.config_screenBrightnessDim));

        int screenBrightnessMinimum = Math.min(resources.getInteger(
                com.android.internal.R.integer.config_screenBrightnessSettingMinimum),
                mScreenBrightnessDimConfig);

        mUseSoftwareAutoBrightnessConfig = resources.getBoolean(
                com.android.internal.R.bool.config_automatic_brightness_available);
        // ++ [framework] Change scope of 'screenBrightness', so that it can be used to initialize HtcAutoBrightnessCtrl
        int[] screenBrightness = null;
        // -- [framework] Change scope of 'screenBrightness', so that it can be used to initialize HtcAutoBrightnessCtrl
        if (mUseSoftwareAutoBrightnessConfig) {
            // ++ [framework] Change scope of 'screenBrightness', so that it can be used to initialize HtcAutoBrightnessCtrl
            mLux = resources.getIntArray(
                    com.android.internal.R.array.config_autoBrightnessLevels);
            screenBrightness = resources.getIntArray(
                    com.android.internal.R.array.config_autoBrightnessLcdBacklightValues);
            // -- [framework] Change scope of 'screenBrightness', so that it can be used to initialize HtcAutoBrightnessCtrl

            mScreenAutoBrightnessSpline = createAutoBrightnessSpline(mLux, screenBrightness);
            if (mScreenAutoBrightnessSpline == null) {
                Slog.e(TAG, "Error in config.xml.  config_autoBrightnessLcdBacklightValues "
                        + "(size " + screenBrightness.length + ") "
                        + "must be monotic and have exactly one more entry than "
                        + "config_autoBrightnessLevels (size " + mLux.length + ") "
                        + "which must be strictly increasing.  "
                        + "Auto-brightness will be disabled.");
                mUseSoftwareAutoBrightnessConfig = false;
            } else {
                if (screenBrightness[0] < screenBrightnessMinimum) {
                    screenBrightnessMinimum = screenBrightness[0];
                }
            }

            mLightSensorWarmUpTimeConfig = resources.getInteger(
                    com.android.internal.R.integer.config_lightSensorWarmupTime);
        }

        // ++ [framework] init DPCInternalAPI
        mDPCInternalAPI = new DPCInternalAPI();
        // -- [framework] init DPCInternalAPI

        // ++ [framework] Condition controlled auto brightness feature, 2012/01/04
        mHtcAutoBrightnessCtrl = new HtcAutoBrightnessCtrl(context, mDPCInternalAPI, mLux, screenBrightness);
        // -- [framework] Condition controlled auto brightness feature, 2012/01/04

        // ++ [framework] Workaround for M7 p-sensor problem.
        mHtcAngleDetector = new HtcAngleDetector(context, mDPCInternalAPI, mSensorManager);
        // -- [framework] Workaround for M7 p-sensor problem.

        mScreenBrightnessRangeMinimum = clampAbsoluteBrightness(screenBrightnessMinimum);
        mScreenBrightnessRangeMaximum = PowerManager.BRIGHTNESS_ON;

        mElectronBeamFadesConfig = resources.getBoolean(
                com.android.internal.R.bool.config_animateScreenLights);

        if (!DEBUG_PRETEND_PROXIMITY_SENSOR_ABSENT) {
            mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            if (mProximitySensor != null) {
                mProximityThreshold = Math.min(mProximitySensor.getMaximumRange(),
                        TYPICAL_PROXIMITY_THRESHOLD);
            }
        }

        if (mUseSoftwareAutoBrightnessConfig
                && !DEBUG_PRETEND_LIGHT_SENSOR_ABSENT) {
            mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }

        if (mUseSoftwareAutoBrightnessConfig && USE_TWILIGHT_ADJUSTMENT) {
            mTwilight.registerListener(mTwilightListener, mHandler);
        }
    }

    private static Spline createAutoBrightnessSpline(int[] lux, int[] brightness) {
        try {
            final int n = brightness.length;
            float[] x = new float[n];
            float[] y = new float[n];
            y[0] = normalizeAbsoluteBrightness(brightness[0]);
            for (int i = 1; i < n; i++) {
                x[i] = lux[i - 1];
                y[i] = normalizeAbsoluteBrightness(brightness[i]);
            }

            Spline spline = Spline.createMonotoneCubicSpline(x, y);
            if (DEBUG) {
                Slog.d(TAG, "Auto-brightness spline: " + spline);
                for (float v = 1f; v < lux[lux.length - 1] * 1.25f; v *= 1.25f) {
                    Slog.d(TAG, String.format("  %7.1f: %7.1f", v, spline.interpolate(v)));
                }
            }
            return spline;
        } catch (IllegalArgumentException ex) {
            Slog.e(TAG, "Could not create auto-brightness spline.", ex);
            return null;
        }
    }

    /**
     * Returns true if the proximity sensor screen-off function is available.
     */
    public boolean isProximitySensorAvailable() {
        return mProximitySensor != null;
    }

    /**
     * Requests a new power state.
     * The controller makes a copy of the provided object and then
     * begins adjusting the power state to match what was requested.
     *
     * @param request The requested power state.
     * @param waitForNegativeProximity If true, issues a request to wait for
     * negative proximity before turning the screen back on, assuming the screen
     * was turned off by the proximity sensor.
     * @return True if display is ready, false if there are important changes that must
     * be made asynchronously (such as turning the screen on), in which case the caller
     * should grab a wake lock, watch for {@link Callbacks#onStateChanged()} then try
     * the request again later until the state converges.
     */
    public boolean requestPowerState(DisplayPowerRequest request,
            boolean waitForNegativeProximity) {
        if (DEBUG) {
            Slog.d(TAG, "requestPowerState: "
                    + request + ", waitForNegativeProximity=" + waitForNegativeProximity);
        }

        synchronized (mLock) {
            boolean changed = false;

            if (waitForNegativeProximity
                    && !mPendingWaitForNegativeProximityLocked) {
                mPendingWaitForNegativeProximityLocked = true;
                changed = true;
            }

            if (mPendingRequestLocked == null) {
                mPendingRequestLocked = new DisplayPowerRequest(request);
                changed = true;
            } else if (!mPendingRequestLocked.equals(request)) {
                mPendingRequestLocked.copyFrom(request);
                changed = true;
            }

            if (changed) {
                mDisplayReadyLocked = false;
            }

            if (changed && !mPendingRequestChangedLocked) {
                mPendingRequestChangedLocked = true;
                sendUpdatePowerStateLocked();
            }

            return mDisplayReadyLocked;
        }
    }

    // ++ [framework] To support customized animation delay
    private void sendUpdatePowerState() {
        sendUpdatePowerState(-1);
    }
    // -- [framework] To support customized animation delay

    // ++ [framework] To support customized animation delay
    private void sendUpdatePowerState(final int DURATION) {
    // -- [framework] To support customized animation delay
        synchronized (mLock) {
            // ++ [framework] To support customized animation delay
            if (DEBUG_ON) {
                Slog.d(TAG, "sendUPS:Duration=" + mCustomizeDuration + "->" + DURATION);
            }
            mCustomizeDuration = DURATION;
            // -- [framework] To support customized animation delay
            sendUpdatePowerStateLocked();
        }
    }

    private void sendUpdatePowerStateLocked() {
        if (!mPendingUpdatePowerStateLocked) {
            mPendingUpdatePowerStateLocked = true;
            Message msg = mHandler.obtainMessage(MSG_UPDATE_POWER_STATE);
            msg.setAsynchronous(true);
            mHandler.sendMessage(msg);
        }
    }

    private void initialize() {
        if (DEBUG_ON) {
            Slog.d(TAG, "initialize...");
        }
        mPowerState = new DisplayPowerState(
                new ElectronBeam(mDisplayManager), mDisplayBlanker,
                mLights.getLight(LightsService.LIGHT_ID_BACKLIGHT),
                // ++ [framework] Add button light control mechanism
                mLights.getLight(LightsService.LIGHT_ID_BUTTONS));
                // -- [framework] Add button light control mechanism

        mElectronBeamOnAnimator = ObjectAnimator.ofFloat(
                mPowerState, DisplayPowerState.ELECTRON_BEAM_LEVEL, 0.0f, 1.0f);
        mElectronBeamOnAnimator.setDuration(ELECTRON_BEAM_ON_ANIMATION_DURATION_MILLIS);
        mElectronBeamOnAnimator.addListener(mAnimatorListener);

        mElectronBeamOffAnimator = ObjectAnimator.ofFloat(
                mPowerState, DisplayPowerState.ELECTRON_BEAM_LEVEL, 1.0f, 0.0f);
        mElectronBeamOffAnimator.setDuration(ELECTRON_BEAM_OFF_ANIMATION_DURATION_MILLIS);
        mElectronBeamOffAnimator.addListener(mAnimatorListener);

        mScreenBrightnessRampAnimator = new RampAnimator<DisplayPowerState>(
                mPowerState, DisplayPowerState.SCREEN_BRIGHTNESS);

        // ++ [framework] To support customized animation delay
        final int old_animation_mode = ANIMATION_MODE;
        final long old_brighten_ls_debounce = BRIGHTENING_LIGHT_DEBOUNCE;
        final long old_darken_ls_debounce = DARKENING_LIGHT_DEBOUNCE;
        final int old_brighten_duration = AUTOBRIGHTNESS_ANIM_DURATION;
        final int old_darken_duration = AUTODIMNESS_ANIM_DURATION;

        try {
            ANIMATION_MODE = android.os.SystemProperties.getInt(SYS_PROP_ANIMATION_MODE, ANIMATION_MODE);

            BRIGHTENING_LIGHT_DEBOUNCE = android.os.SystemProperties.getLong(SYS_PROP_BRIGHTEN_DEBOUNCE, BRIGHTENING_LIGHT_DEBOUNCE);
            DARKENING_LIGHT_DEBOUNCE = android.os.SystemProperties.getLong(SYS_PROP_DARKEN_DEBOUNCE, DARKENING_LIGHT_DEBOUNCE);
            AUTOBRIGHTNESS_ANIM_DURATION = android.os.SystemProperties.getInt(SYS_PROP_BRIGHTEN_DURATION, AUTOBRIGHTNESS_ANIM_DURATION);
            AUTODIMNESS_ANIM_DURATION = android.os.SystemProperties.getInt(SYS_PROP_DARKEN_DURATION, AUTODIMNESS_ANIM_DURATION);
        } catch (Exception e) {
            if (DEBUG_ON) {
                Slog.d(TAG, "initialize: Unable to get anim durations from SystemProperties. " + e);
            }
        }

        // ++ [framework] Workaround for M7 p-sensor problem after ending call
        final int old_delay_near = DELAY_SCREEN_ON_FROM_NEAR_DURATION;
        final int old_delay_far_angle = DELAY_SCREEN_ON_FROM_FAR_ANGLE_DURATION;
        final int old_delay_far_total = DELAY_SCREEN_ON_FROM_FAR_TOTAL_DURATION;

        // ++ [framework] If AngleDetector is enable, use the new debounce value for positive or negative transitions
        final int old_debounce_near = PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY;
        final int old_debounce_far = PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY;
        // -- [framework] If AngleDetector is enable, use the new debounce value for positive or negative transitions

        try {
            if (checkHtcCustomizationReader()) {
                DELAY_SCREEN_ON_FROM_NEAR_DURATION = mHtcCustomizationReader.readInteger(
                        "powermanager_delay_screen_on_from_near_duration",
                        DELAY_SCREEN_ON_FROM_NEAR_DURATION);
                DELAY_SCREEN_ON_FROM_FAR_ANGLE_DURATION = mHtcCustomizationReader.readInteger(
                        "powermanager_delay_screen_on_from_far_angle_duration",
                        DELAY_SCREEN_ON_FROM_FAR_ANGLE_DURATION);
                DELAY_SCREEN_ON_FROM_FAR_TOTAL_DURATION = mHtcCustomizationReader.readInteger(
                        "powermanager_delay_screen_on_from_far_total_duration",
                        DELAY_SCREEN_ON_FROM_FAR_TOTAL_DURATION);
                // ++ [framework] If AngleDetector is enable, use the new debounce value for positive or negative transitions
                IS_ANGLE_DETECTOR_SUPPORT = mHtcCustomizationReader.readBoolean(
                        "htcangledetector_is_angle_detector_enable",
                        IS_ANGLE_DETECTOR_SUPPORT);
                if (IS_ANGLE_DETECTOR_SUPPORT) {
                    PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY = ANGLEDETECTOR_PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY;
                    PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY = ANGLEDETECTOR_PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY;
                }
                // -- [framework] If AngleDetector is enable, use the new debounce value for positive or negative transitions
            }
        } catch (Exception e) {
            if (DEBUG_ON) {
                Slog.w(TAG, "initialize: Unable to get durations from CustomizationReader. " + e);
            }
        }
        // -- [framework] Workaround for M7 p-sensor problem after ending call

        if (DEBUG_ON) {
            Slog.d(TAG, "initialize:"
                    + " mode=" + old_animation_mode + "->" + ANIMATION_MODE
                    + ", angle detector mode=" + IS_ANGLE_DETECTOR_SUPPORT
                    + ", brighten_ls_debounce=" + old_brighten_ls_debounce + "->" + BRIGHTENING_LIGHT_DEBOUNCE
                    + ", darken_ls_debounce=" + old_darken_ls_debounce + "->" + DARKENING_LIGHT_DEBOUNCE
                    + ", brighten_duration=" + old_brighten_duration + "->" + AUTOBRIGHTNESS_ANIM_DURATION
                    + ", darken_duration=" + old_darken_duration + "->" + AUTODIMNESS_ANIM_DURATION
                    + ", delay_near=" + old_delay_near + "->" + DELAY_SCREEN_ON_FROM_NEAR_DURATION
                    + ", delay_far_angle=" + old_delay_far_angle + "->" + DELAY_SCREEN_ON_FROM_FAR_ANGLE_DURATION
                    + ", delay_far_total=" + old_delay_far_total + "->" + DELAY_SCREEN_ON_FROM_FAR_TOTAL_DURATION
                    + ", debounce_near=" + old_debounce_near + "->" + PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY
                    + ", debounce_far=" + old_debounce_far + "->" + PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY);
        }
        // -- [framework] To support customized animation delay
    }

    private final Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            sendUpdatePowerState();
        }
        @Override
        public void onAnimationRepeat(Animator animation) {
        }
        @Override
        public void onAnimationCancel(Animator animation) {
        }
    };

    private void updatePowerState() {
        // Update the power state request.
        final boolean mustNotify;
        boolean mustInitialize = false;
        boolean updateAutoBrightness = mTwilightChanged;
        boolean wasDim = false;
        mTwilightChanged = false;

        synchronized (mLock) {
            mPendingUpdatePowerStateLocked = false;
            if (mPendingRequestLocked == null) {
                return; // wait until first actual power request
            }

            if (mPowerRequest == null) {
                mPowerRequest = new DisplayPowerRequest(mPendingRequestLocked);
                mWaitingForNegativeProximity = mPendingWaitForNegativeProximityLocked;
                mPendingWaitForNegativeProximityLocked = false;
                mPendingRequestChangedLocked = false;
                mustInitialize = true;
            } else if (mPendingRequestChangedLocked) {
                if (mPowerRequest.screenAutoBrightnessAdjustment
                        != mPendingRequestLocked.screenAutoBrightnessAdjustment) {
                    updateAutoBrightness = true;
                }
                //++[framework] For max auto brightness feature
                if (mPowerRequest.screenAutoBrightnessPercentage
                        != mPendingRequestLocked.screenAutoBrightnessPercentage) {
                    updateAutoBrightness = true;
                }
                //--[framework] For max auto brightness feature
                wasDim = (mPowerRequest.screenState == DisplayPowerRequest.SCREEN_STATE_DIM);
                mPowerRequest.copyFrom(mPendingRequestLocked);
                mWaitingForNegativeProximity |= mPendingWaitForNegativeProximityLocked;
                mPendingWaitForNegativeProximityLocked = false;
                mPendingRequestChangedLocked = false;
                mDisplayReadyLocked = false;
            }

            mustNotify = !mDisplayReadyLocked;
        }

        // Initialize things the first time the power state is changed.
        if (mustInitialize) {
            initialize();
        }

        // Apply the proximity sensor.
        if (mProximitySensor != null) {
            // ++ [framework] Workaround for M7 p-sensor problem.
            if (mPowerRequest.useProximitySensor && mHtcAngleDetector != null) {
                mHtcAngleDetector.proximitySensorStatus(mProximity);
            }
            // -- [framework] Workaround for M7 p-sensor problem.
            if (mPowerRequest.useProximitySensor
                    // ++ [framework] Workaround for M7 p-sensor problem.
                    && (mPowerRequest.screenState != DisplayPowerRequest.SCREEN_STATE_OFF ||
                        (mHtcAngleDetector != null && mHtcAngleDetector.isScreenTimeout()))) {
//                    && mPowerRequest.screenState != DisplayPowerRequest.SCREEN_STATE_OFF) {
                    // -- [framework] Workaround for M7 p-sensor problem.
                setProximitySensorEnabled(true);
                if (!mScreenOffBecauseOfProximity
                        && mProximity == PROXIMITY_POSITIVE) {
                    mScreenOffBecauseOfProximity = true;
                    sendOnProximityPositiveWithWakelock();
                    //[++framework] To control screen off by Proximity Sensor, peter li , 20130418
                    if (mPMSInternalAPI != null) {
                        mPMSInternalAPI.setProximityStateWithScreenOff(true);
                    }
                    //[--framework] To control screen off by Proximity Sensor, peter li , 20130418
                    setScreenOn(false);
                }
            } else if (mWaitingForNegativeProximity
                    && mScreenOffBecauseOfProximity
                    && mProximity == PROXIMITY_POSITIVE
                    && mPowerRequest.screenState != DisplayPowerRequest.SCREEN_STATE_OFF) {
                setProximitySensorEnabled(true);
            // ++ [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
            } else if (mPowerRequest.forceScreenOn) {
                if (mProximity == PROXIMITY_NEGATIVE) {
                    mPowerRequest.forceScreenOn = false;
                }
            // -- [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
            // ++ [framework] Workaround for M7 p-sensor problem after ending call
            } else if (mPowerRequest.delayScreenOn) {
            // -- [framework] Workaround for M7 p-sensor problem after ending call
            } else {
                setProximitySensorEnabled(false);
                mWaitingForNegativeProximity = false;
            }
            if (mScreenOffBecauseOfProximity
                    && mProximity != PROXIMITY_POSITIVE) {
                mScreenOffBecauseOfProximity = false;
                sendOnProximityNegativeWithWakelock();
            }
            // ++ [framework] Workaround for M7 p-sensor problem.
            if (mPowerRequest.useProximitySensor && mProximity != PROXIMITY_POSITIVE && mHtcAngleDetector != null) {
                if (mHtcAngleDetector.isTalkingDetected()) {
                    if (!mPowerRequest.forceScreenOn) {
                        if (DEBUG_ON) {
                            Slog.d(TAG, "Talking.turning off.");
                        }
                        sendOnAngleDetectorTalkingDetected();
                        setScreenOn(false);
                    } else {
                        if (DEBUG_ON) {
                            Slog.d(TAG, "Force on is set. disable detector.");
                        }
                        mHtcAngleDetector.disableDetector_Ext();
                        sendOnAngleDetectorReadingDetected();
                    }
                } else if (mHtcAngleDetector.isTurnOnScreenOnce()) {
                    if (DEBUG_ON) {
                        Slog.d(TAG, "Reading.turning on.");
                    }
                    if (mPowerRequest.forceScreenOn) {
                        mPowerRequest.forceScreenOn = false;
                    }
                    sendOnAngleDetectorReadingDetected();
                }
            }
            // -- [framework] Workaround for M7 p-sensor problem.
        } else {
            mWaitingForNegativeProximity = false;
        }

        // Set the screen brightness.
        if (wantScreenOn(mPowerRequest.screenState)) {
            int target;
            boolean slow;
            // ++ [framework] Fix Google issue, check auto brightness mode to prevent auto brightness is used when manual brightness is selected.
            if (mPowerRequest.useAutoBrightness && mScreenAutoBrightness >= 0 && mLightSensorEnabled) {
            // -- [framework] Fix Google issue, check auto brightness mode to prevent auto brightness is used when manual brightness is selected.
                // Use current auto-brightness value.
                target = mScreenAutoBrightness;
                slow = mUsingScreenAutoBrightness;
                mUsingScreenAutoBrightness = true;
            } else {
                // Light sensor is disabled or not ready yet.
                // Use the current brightness setting from the request, which is expected
                // provide a nominal default value for the case where auto-brightness
                // is not ready yet.
                target = mPowerRequest.screenBrightness;
                slow = false;
                mUsingScreenAutoBrightness = false;
            }
            if (mPowerRequest.screenState == DisplayPowerRequest.SCREEN_STATE_DIM) {
                // Dim quickly by at least some minimum amount.
                target = Math.min(target - SCREEN_DIM_MINIMUM_REDUCTION,
                        mScreenBrightnessDimConfig);
                slow = false;
            } else if (wasDim) {
                // Brighten quickly.
                slow = false;
            }

            // ++ [framework] Add debug log
            if (DEBUG_ON && false) {
                Slog.d(TAG, "updatePowerState:"
                        + " target=" + target
                        + ", mCustomizeDuration=" + mCustomizeDuration
                        + ", mPR.sState=" + mPowerRequest.screenState
                        + ", mPR.auto=" + mPowerRequest.useAutoBrightness
                        + ", mPR.sBrightness=" + mPowerRequest.screenBrightness
                        + ", mScreenAutoBrightness=" + mScreenAutoBrightness);
            }
            // -- [framework] Add debug log

            if (mHtcAutoBrightnessCtrl != null) {
                //++[framework] For max auto brightness feature
                //target = mHtcAutoBrightnessCtrl.getCtrlBrightnessValue(target);
                target = mHtcAutoBrightnessCtrl.getCtrlBrightnessValue(target, mPowerRequest.screenAutoBrightnessPercentage);
                //--[framework] For max auto brightness feature
            }

            // ++ [framework] To support customized animation delay
            int curValue = mScreenBrightnessRampAnimator.getCurrentBrightness();
            int newTarget = clampScreenBrightness(target);

            if (curValue == 0) {
                animateScreenBrightness(newTarget, 0, mButtonValue, -1);
            } else if (mCustomizeDuration < 0 &&
                    mPowerRequest.useAutoBrightness &&
                    (mPowerRequest.screenState == DisplayPowerRequest.SCREEN_STATE_BRIGHT) &&
                    ((ANIMATION_MODE & ANIM_MODE_HTC) != 0) &&
                    !mPowerRequest.useOverrideBrightness) {
                if (curValue <= newTarget) {
                    animateScreenBrightness(newTarget, -1, mButtonValue, AUTOBRIGHTNESS_ANIM_DURATION);
                } else {
                    animateScreenBrightness(newTarget, -1, mButtonValue, AUTODIMNESS_ANIM_DURATION);
                }
            } else if (mCustomizeDuration < 0) {
                animateScreenBrightness(newTarget, slow ? BRIGHTNESS_RAMP_RATE_SLOW : BRIGHTNESS_RAMP_RATE_FAST, mButtonValue);
            } else {
                animateScreenBrightness(newTarget, -1, mButtonValue, mCustomizeDuration);
            }
            // -- [framework] To support customized animation delay
        } else {
            // Screen is off.  Don't bother changing the brightness.
            mUsingScreenAutoBrightness = false;
        }

        // ++ [framework] To support customized animation delay
        mCustomizeDuration = -1;
        // -- [framework] To support customized animation delay

        // Animate the screen on or off.
        // ++ [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
        //htc+- SW_ARCH, 20130709, use HtcAngleDetector to turn sceen on when p-sensor is FAR
        if ((!mScreenOffBecauseOfProximity && (mHtcAngleDetector != null && !mHtcAngleDetector.isTalkingDetected() && !mHtcAngleDetector.isPsensorNear()) && !mPowerRequest.delayScreenOn) || mPowerRequest.forceScreenOn || mPowerRequest.turnOnScreenOnce) {
//        if (!mScreenOffBecauseOfProximity) {
        // -- [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
            if (wantScreenOn(mPowerRequest.screenState)) {
                // Want screen on.
                // Wait for previous off animation to complete beforehand.
                // It is relatively short but if we cancel it and switch to the
                // on animation immediately then the results are pretty ugly.
                if (!mElectronBeamOffAnimator.isStarted()) {
                    // Turn the screen on.  The contents of the screen may not yet
                    // be visible if the electron beam has not been dismissed because
                    // its last frame of animation is solid black.
                    setScreenOn(true);

                    if (mPowerRequest.blockScreenOn
                            && mPowerState.getElectronBeamLevel() == 0.0f) {
                        blockScreenOn();
                    } else {
                        unblockScreenOn();
                        if (USE_ELECTRON_BEAM_ON_ANIMATION) {
                            if (!mElectronBeamOnAnimator.isStarted()) {
                                if (mPowerState.getElectronBeamLevel() == 1.0f) {
                                    mPowerState.dismissElectronBeam();
                                } else if (mPowerState.prepareElectronBeam(
                                        mElectronBeamFadesConfig ?
                                                ElectronBeam.MODE_FADE :
                                                        ElectronBeam.MODE_WARM_UP)) {
                                    mElectronBeamOnAnimator.start();
                                } else {
                                    mElectronBeamOnAnimator.end();
                                }
                            }
                        } else {
                            mPowerState.setElectronBeamLevel(1.0f);
                            mPowerState.dismissElectronBeam();
                        }
                    }
                }
            } else {
                // Want screen off.
                // Wait for previous on animation to complete beforehand.
                if (!mElectronBeamOnAnimator.isStarted()) {
                    // ++ [framework] Disable electron beam off animation
                    if (USE_ELECTRON_BEAM_OFF_ANIMATION) {
                    // -- [framework] Disable electron beam off animation
                        if (!mElectronBeamOffAnimator.isStarted()) {
                            if (mPowerState.getElectronBeamLevel() == 0.0f) {
                                setScreenOn(false);
                            } else if (mPowerState.prepareElectronBeam(
                                        mElectronBeamFadesConfig ?
                                        ElectronBeam.MODE_FADE :
                                        ElectronBeam.MODE_COOL_DOWN)
                                    && mPowerState.isScreenOn()) {
                                mElectronBeamOffAnimator.start();
                            } else {
                                mElectronBeamOffAnimator.end();
                            }
                        }
                    // ++ [framework] Disable electron beam off animation
                    } else {
                        // ++ [framework] Fix Google issue, screen off value was not synced to RampAnimator
                        animateScreenBrightness(0, 0, 0, -1);
                        // -- [framework] Fix Google issue, screen off value was not synced to RampAnimator
                        mPowerState.setElectronBeamLevel(0.0f);
                        setScreenOn(false);
                    }
                    // -- [framework] Disable electron beam off animation
                }
            }
        }

        // ++ [framework] Fix Google issue, register light sensor after screen is on.
        // Turn on the light sensor if needed.
        if (mLightSensor != null) {
            // ++ [framework] Fix Google issue, disable light sensor when screen is off
            // ++ Remove 'mPowerRequest.useAutoBrightness' by HTC Andy Chuang for enabling lightsensor when system ready
            setLightSensorEnabled(wantScreenOn(mPowerRequest.screenState) && !mScreenOffBecauseOfProximity, updateAutoBrightness);
//            setLightSensorEnabled(mPowerRequest.useAutoBrightness
//                    && wantScreenOn(mPowerRequest.screenState), updateAutoBrightness);
            // -- Remove 'mPowerRequest.useAutoBrightness' by HTC Andy Chuang for enabling lightsensor when system ready
            // -- [framework] Fix Google issue, disable light sensor when screen is off
        }
        // -- [framework] Fix Google issue, register light sensor after screen is on.

        // Report whether the display is ready for use.
        // We mostly care about the screen state here, ignoring brightness changes
        // which will be handled asynchronously.
        if (mustNotify
                && !mScreenOnWasBlocked
                && !mElectronBeamOnAnimator.isStarted()
                && !mElectronBeamOffAnimator.isStarted()
                && mPowerState.waitUntilClean(mCleanListener)) {
            synchronized (mLock) {
                if (!mPendingRequestChangedLocked) {
                    mDisplayReadyLocked = true;

                    if (DEBUG_ON) {
                        Slog.d(TAG, "Display ready!");
                    }
                }
            }
            sendOnStateChangedWithWakelock();
        }
    }

    private void blockScreenOn() {
        if (!mScreenOnWasBlocked) {
            mScreenOnWasBlocked = true;
            if (DEBUG) {
                Slog.d(TAG, "Blocked screen on.");
                mScreenOnBlockStartRealTime = SystemClock.elapsedRealtime();
            }
        }
    }

    private void unblockScreenOn() {
        if (mScreenOnWasBlocked) {
            mScreenOnWasBlocked = false;
            if (DEBUG) {
                Slog.d(TAG, "Unblocked screen on after " +
                        (SystemClock.elapsedRealtime() - mScreenOnBlockStartRealTime) + " ms");
            }
        }
    }

    private void setScreenOn(boolean on) {
        if (!mPowerState.isScreenOn() == on) {

            //[++framework] To control screen off by Proximity Sensor, peter li , 20130418
            if (on && (mPMSInternalAPI != null)) {
                mPMSInternalAPI.setProximityStateWithScreenOff(false);
            }
            //[--framework] To control screen off by Proximity Sensor, peter li , 20130418

            // ++ [framework] Workaround for turning on screen when screen is off.
            if (on && mPowerRequest != null && mPowerRequest.turnOnScreenOnce) {
                if (DEBUG_ON) {
                    Slog.w(TAG, "clear turnOnScreenOnce flag");
                }
                mPowerRequest.turnOnScreenOnce = false;
            }
            // -- [framework] Workaround for turning on screen when screen is off.

            mPowerState.setScreenOn(on);

            // ++ [framework] Workaround for M7 p-sensor problem.
            if (mHtcAngleDetector != null && on) {
                mHtcAngleDetector.notifyScreenTimeout(false);
            }
            // -- [framework] Workaround for M7 p-sensor problem.

            if (on) {
                mNotifier.onScreenOn();
            } else {
                mNotifier.onScreenOff();
            }
        }
    }

    private int clampScreenBrightness(int value) {
        return clamp(value, mScreenBrightnessRangeMinimum, mScreenBrightnessRangeMaximum);
    }

    private static int clampAbsoluteBrightness(int value) {
        return clamp(value, PowerManager.BRIGHTNESS_OFF, PowerManager.BRIGHTNESS_ON);
    }

    private static int clamp(int value, int min, int max) {
        if (value <= min) {
            return min;
        }
        if (value >= max) {
            return max;
        }
        return value;
    }

    private static float normalizeAbsoluteBrightness(int value) {
        return (float)clampAbsoluteBrightness(value) / PowerManager.BRIGHTNESS_ON;
    }

    // ++ [framework] Add button light control mechanism
    private void animateScreenBrightness(int target, int rate, int buttonValue) {
    // -- [framework] Add button light control mechanism
        animateScreenBrightness(target, rate, buttonValue, -1);
    }

    // ++ [framework] To support customized animation delay
    private void animateScreenBrightness(int target, int rate, int buttonValue, int duration) {
    // -- [framework] To support customized animation delay
        // ++ [framework] Add button light control mechanism
        if (mScreenBrightnessRampAnimator.animateTo(target, rate, buttonValue, duration)) {
        // -- [framework] Add button light control mechanism
            mNotifier.onScreenBrightness(target);
        }
    }

    private final Runnable mCleanListener = new Runnable() {
        @Override
        public void run() {
            sendUpdatePowerState();
        }
    };

    private void setProximitySensorEnabled(boolean enable) {
        if (enable) {
            if (!mProximitySensorEnabled) {
                // Register the listener.
                // Proximity sensor state already cleared initially.
                mProximitySensorEnabled = true;
                if (DEBUG_ON) {
                    Slog.d(TAG, "Reg P Sensor");
                }
                mSensorManager.registerListener(mProximitySensorListener, mProximitySensor,
                        SensorManager.SENSOR_DELAY_NORMAL, mHandler);
            }
        } else {
            if (mProximitySensorEnabled) {
                // Unregister the listener.
                // Clear the proximity sensor state for next time.
                mProximitySensorEnabled = false;
                mProximity = PROXIMITY_UNKNOWN;
                mPendingProximity = PROXIMITY_UNKNOWN;
                mHandler.removeMessages(MSG_PROXIMITY_SENSOR_DEBOUNCED);
                if (DEBUG_ON) {
                    Slog.d(TAG, "Unreg P Sensor");
                }
                mSensorManager.unregisterListener(mProximitySensorListener);
                clearPendingProximityDebounceTime(); // release wake lock (must be last)
            }
        }
        // ++ [framework] Workaround for M7 p-sensor problem.
        if (mHtcAngleDetector != null) {
            mHtcAngleDetector.proximitySensorOn(mProximitySensorEnabled);
        }
        // -- [framework] Workaround for M7 p-sensor problem.
    }

    private void handleProximitySensorEvent(long time, boolean positive) {
        if (mProximitySensorEnabled) {
            if (mPendingProximity == PROXIMITY_NEGATIVE && !positive) {
                return; // no change
            }
            if (mPendingProximity == PROXIMITY_POSITIVE && positive) {
                return; // no change
            }

            // Only accept a proximity sensor reading if it remains
            // stable for the entire debounce delay.  We hold a wake lock while
            // debouncing the sensor.
            mHandler.removeMessages(MSG_PROXIMITY_SENSOR_DEBOUNCED);
            if (positive) {
                mPendingProximity = PROXIMITY_POSITIVE;
                setPendingProximityDebounceTime(
                        time + PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY); // acquire wake lock
            } else {
                mPendingProximity = PROXIMITY_NEGATIVE;
                setPendingProximityDebounceTime(
                        time + PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY); // acquire wake lock
            }

            // Debounce the new sensor reading.
            debounceProximitySensor();
        }
    }

    private void debounceProximitySensor() {
        if (mProximitySensorEnabled
                && mPendingProximity != PROXIMITY_UNKNOWN
                && mPendingProximityDebounceTime >= 0) {
            final long now = SystemClock.uptimeMillis();
            if (mPendingProximityDebounceTime <= now) {
                // Sensor reading accepted.  Apply the change then release the wake lock.
                mProximity = mPendingProximity;
                // ++ [framework] Workaround for M7 p-sensor problem after ending call
                handleProximitySensorAfterEndCall(mProximity);
                // -- [framework] Workaround for M7 p-sensor problem after ending call
                updatePowerState();
                clearPendingProximityDebounceTime(); // release wake lock (must be last)
            } else {
                // Need to wait a little longer.
                // Debounce again later.  We continue holding a wake lock while waiting.
                Message msg = mHandler.obtainMessage(MSG_PROXIMITY_SENSOR_DEBOUNCED);
                msg.setAsynchronous(true);
                mHandler.sendMessageAtTime(msg, mPendingProximityDebounceTime);
            }
        }
    }

    private void clearPendingProximityDebounceTime() {
        if (mPendingProximityDebounceTime >= 0) {
            mPendingProximityDebounceTime = -1;
            mDisplaySuspendBlocker.release(); // release wake lock
        }
    }

    private void setPendingProximityDebounceTime(long debounceTime) {
        if (mPendingProximityDebounceTime < 0) {
            mDisplaySuspendBlocker.acquire(); // acquire wake lock
        }
        mPendingProximityDebounceTime = debounceTime;
    }

    private void setLightSensorEnabled(boolean enable, boolean updateAutoBrightness) {
        if (enable) {
            if (!mLightSensorEnabled) {
                updateAutoBrightness = true;
                mLightSensorEnabled = true;
                mLightSensorEnableTime = SystemClock.uptimeMillis();
                if (DEBUG_ON) {
                    Slog.d(TAG, "Reg L Sensor");
                }
                mSensorManager.registerListener(mLightSensorListener, mLightSensor,
                        LIGHT_SENSOR_RATE_MILLIS * 1000, mHandler);
            }
        } else {
            if (mLightSensorEnabled) {
                mLightSensorEnabled = false;
                mAmbientLuxValid = false;
                mRecentLightSamples = 0;
                //[++framework] Rework Button light mechanism
                mHandler.removeMessages(MSG_BUTTON_LIGHT_DEBOUNCED);
                //[--framework] Rework Button light mechanism
                mHandler.removeMessages(MSG_LIGHT_SENSOR_DEBOUNCED);
                mSensorManager.unregisterListener(mLightSensorListener);
                //++[framework] For Car mode, peter li 20121031
                if (DEBUG_ON) {
                    Slog.d(TAG, "Unreg L Sensor");
                }
                if (mHtcAutoBrightnessCtrl != null) {
                    mHtcAutoBrightnessCtrl.setLightSensorLevel(-1, false);
                }
                //--[framework] For Car mode, peter li 20121031
            }
        }
        if (updateAutoBrightness) {
            updateAutoBrightness(false);
        }
    }

    private void handleLightSensorEvent(long time, float lux) {
        //[++framework] Rework Button light mechanism
        mHandler.removeMessages(MSG_BUTTON_LIGHT_DEBOUNCED);
        //[--framework] Rework Button light mechanism
        mHandler.removeMessages(MSG_LIGHT_SENSOR_DEBOUNCED);

        applyLightSensorMeasurement(time, lux);
        updateAmbientLux(time);
    }

    private void applyLightSensorMeasurement(long time, float lux) {
        // Update our filters.
        // ++ [framework] Disable buggy Google mechanism for lux tuning.
        mRecentLightSamples = 1;
//        mRecentLightSamples += 1;
        // -- [framework] Disable buggy Google mechanism for lux tuning.
        if (mRecentLightSamples == 1) {
            mRecentShortTermAverageLux = lux;
            mRecentLongTermAverageLux = lux;
        } else {
            final long timeDelta = time - mLastObservedLuxTime;
            mRecentShortTermAverageLux += (lux - mRecentShortTermAverageLux)
                    * timeDelta / (SHORT_TERM_AVERAGE_LIGHT_TIME_CONSTANT + timeDelta);
            mRecentLongTermAverageLux += (lux - mRecentLongTermAverageLux)
                    * timeDelta / (LONG_TERM_AVERAGE_LIGHT_TIME_CONSTANT + timeDelta);
        }

        // Remember this sample value.
        mLastObservedLux = lux;
        mLastObservedLuxTime = time;
    }

    private void setAmbientLux(float lux) {
        mAmbientLux = lux;
        mBrighteningLuxThreshold = mAmbientLux * (1.0f + BRIGHTENING_LIGHT_HYSTERESIS);
        mDarkeningLuxThreshold = mAmbientLux * (1.0f - DARKENING_LIGHT_HYSTERESIS);
    }

    private void updateAmbientLux(long time) {
        // If the light sensor was just turned on then immediately update our initial
        // estimate of the current ambient light level.
        if (!mAmbientLuxValid) {
            final long timeWhenSensorWarmedUp =
                mLightSensorWarmUpTimeConfig + mLightSensorEnableTime;
            if (time < timeWhenSensorWarmedUp) {
                mHandler.sendEmptyMessageAtTime(MSG_LIGHT_SENSOR_DEBOUNCED,
                        timeWhenSensorWarmedUp);
                return;
            }
            setAmbientLux(mRecentShortTermAverageLux);
            mAmbientLuxValid = true;
            mDebounceLuxDirection = 0;
            mDebounceLuxTime = time;
            if (DEBUG) {
                Slog.d(TAG, "updateAmbientLux: Initializing: "
                        + ", mRecentShortTermAverageLux=" + mRecentShortTermAverageLux
                        + ", mRecentLongTermAverageLux=" + mRecentLongTermAverageLux
                        + ", mAmbientLux=" + mAmbientLux);
            }
            updateAutoBrightness(true);
        } else if (mRecentShortTermAverageLux > mBrighteningLuxThreshold
                && mRecentLongTermAverageLux > mBrighteningLuxThreshold) {
            // The ambient environment appears to be brightening.
            if (mDebounceLuxDirection <= 0) {
                mDebounceLuxDirection = 1;
                mDebounceLuxTime = time;
                if (DEBUG) {
                    Slog.d(TAG, "updateAmbientLux: Possibly brightened, waiting for "
                            + BRIGHTENING_LIGHT_DEBOUNCE + " ms: "
                            + "mBrighteningLuxThreshold=" + mBrighteningLuxThreshold
                            + ", mRecentShortTermAverageLux=" + mRecentShortTermAverageLux
                            + ", mRecentLongTermAverageLux=" + mRecentLongTermAverageLux
                            + ", mAmbientLux=" + mAmbientLux);
                }
            }
            long debounceTime = mDebounceLuxTime + BRIGHTENING_LIGHT_DEBOUNCE;
            if (time < debounceTime) {
                mHandler.sendEmptyMessageAtTime(MSG_LIGHT_SENSOR_DEBOUNCED, debounceTime);
                return;
            }
            setAmbientLux(mRecentShortTermAverageLux);
            if (DEBUG) {
                Slog.d(TAG, "updateAmbientLux: Brightened: "
                        + "mBrighteningLuxThreshold=" + mBrighteningLuxThreshold
                        + ", mRecentShortTermAverageLux=" + mRecentShortTermAverageLux
                        + ", mRecentLongTermAverageLux=" + mRecentLongTermAverageLux
                        + ", mAmbientLux=" + mAmbientLux);
            }
            updateAutoBrightness(true);
        } else if (mRecentShortTermAverageLux < mDarkeningLuxThreshold
                && mRecentLongTermAverageLux < mDarkeningLuxThreshold) {
            // The ambient environment appears to be darkening.
            if (mDebounceLuxDirection >= 0) {
                mDebounceLuxDirection = -1;
                mDebounceLuxTime = time;
                if (DEBUG) {
                    Slog.d(TAG, "updateAmbientLux: Possibly darkened, waiting for "
                            + DARKENING_LIGHT_DEBOUNCE + " ms: "
                            + "mDarkeningLuxThreshold=" + mDarkeningLuxThreshold
                            + ", mRecentShortTermAverageLux=" + mRecentShortTermAverageLux
                            + ", mRecentLongTermAverageLux=" + mRecentLongTermAverageLux
                            + ", mAmbientLux=" + mAmbientLux);
                }
            }
            long debounceTime = mDebounceLuxTime + DARKENING_LIGHT_DEBOUNCE;
            //[++framework] Rework Button light mechanism
            long debounceButtonTime = mDebounceLuxTime + DARKENING_BUTTON_LIGHT_DEBOUNCE;
            //[--framework] Rework Button light mechanism
            if (time < debounceTime) {
                //[++framework] Rework Button light mechanism
                if (time <= debounceButtonTime) {
                    mHandler.sendEmptyMessageAtTime(MSG_BUTTON_LIGHT_DEBOUNCED, debounceButtonTime);
                }
                //[--framework] Rework Button light mechanism
                mHandler.sendEmptyMessageAtTime(MSG_LIGHT_SENSOR_DEBOUNCED, debounceTime);
                return;
            }
            // Be conservative about reducing the brightness, only reduce it a little bit
            // at a time to avoid having to bump it up again soon.
            setAmbientLux(Math.max(mRecentShortTermAverageLux, mRecentLongTermAverageLux));
            if (DEBUG) {
                Slog.d(TAG, "updateAmbientLux: Darkened: "
                        + "mDarkeningLuxThreshold=" + mDarkeningLuxThreshold
                        + ", mRecentShortTermAverageLux=" + mRecentShortTermAverageLux
                        + ", mRecentLongTermAverageLux=" + mRecentLongTermAverageLux
                        + ", mAmbientLux=" + mAmbientLux);
            }
            updateAutoBrightness(true);
        } else if (mDebounceLuxDirection != 0) {
            // No change or change is within the hysteresis thresholds.
            mDebounceLuxDirection = 0;
            mDebounceLuxTime = time;
            //[++framework] Rework Button light mechanism
            mHandler.sendEmptyMessage(MSG_BUTTON_LIGHT_DEBOUNCED);
            //[--framework] Rework Button light mechanism

            if (DEBUG) {
                Slog.d(TAG, "updateAmbientLux: Canceled debounce: "
                        + "mBrighteningLuxThreshold=" + mBrighteningLuxThreshold
                        + ", mDarkeningLuxThreshold=" + mDarkeningLuxThreshold
                        + ", mRecentShortTermAverageLux=" + mRecentShortTermAverageLux
                        + ", mRecentLongTermAverageLux=" + mRecentLongTermAverageLux
                        + ", mAmbientLux=" + mAmbientLux);
            }
        }

        // Now that we've done all of that, we haven't yet posted a debounce
        // message. So consider the case where current lux is beyond the
        // threshold. It's possible that the light sensor may not report values
        // if the light level does not change, so we need to occasionally
        // synthesize sensor readings in order to make sure the brightness is
        // adjusted accordingly. Note these thresholds may have changed since
        // we entered the function because we called setAmbientLux and
        // updateAutoBrightness along the way.
        if (mLastObservedLux > mBrighteningLuxThreshold
                || mLastObservedLux < mDarkeningLuxThreshold) {
            mHandler.sendEmptyMessageAtTime(MSG_LIGHT_SENSOR_DEBOUNCED,
                    time + SYNTHETIC_LIGHT_SENSOR_RATE_MILLIS);
        }
    }

    private void debounceLightSensor() {
        if (mLightSensorEnabled) {
            long time = SystemClock.uptimeMillis();
            if (time >= mLastObservedLuxTime + SYNTHETIC_LIGHT_SENSOR_RATE_MILLIS) {
                if (DEBUG) {
                    Slog.d(TAG, "debounceLightSensor: Synthesizing light sensor measurement "
                            + "after " + (time - mLastObservedLuxTime) + " ms.");
                }
                applyLightSensorMeasurement(time, mLastObservedLux);
            }
            updateAmbientLux(time);
        }
    }

    //[++framework] Rework Button light mechanism
    private void debounceButtonLight() {
        if (DEBUG_ON) {
            Slog.d(TAG, "d-B-L=" + mLightSensorEnabled);
        }

        if (mLightSensorEnabled) {

            if (mPendingRequestLocked == null) {
                if (DEBUG_ON) {
                    Slog.w(TAG, "debounceButtonLight mPendingRequestLocked = null");
                }
                return;
            }

            if (mPowerRequest == null) {
                if (DEBUG_ON) {
                    Slog.w(TAG, "debounceButtonLight mPowerRequest = null");
                }
                return;
            }

            if (mPowerState == null) {
                if (DEBUG_ON) {
                    Slog.w(TAG, "debounceButtonLight mPowerState = null");
                }
                return;
            }

            if (wantScreenOn(mPowerRequest.screenState)) {

                if (mHtcAutoBrightnessCtrl != null) {
                    if (DEBUG_ON) {
                        Slog.d(TAG, "Average=(" + mRecentShortTermAverageLux + "," + mRecentLongTermAverageLux +")");
                    }
                    final int LUX = Math.round(Math.max(mRecentShortTermAverageLux, mRecentLongTermAverageLux));
                    mButtonValue = mHtcAutoBrightnessCtrl.getButtonValue(LUX);
                    mPowerState.setButtonBrightness(mButtonValue);
                }

            }

        }

    }
    //[--framework] Rework Button light mechanism

    // ++ [framework] To support customized animation delay
    private void updateAutoBrightness(boolean sendUpdate) {
        updateAutoBrightness(sendUpdate, -1);
    }
    // -- [framework] To support customized animation delay

    private void updateAutoBrightness(boolean sendUpdate, final int DURATION) {
        if (!mAmbientLuxValid) {
            return;
        }

        float value = mScreenAutoBrightnessSpline.interpolate(mAmbientLux);
        float gamma = 1.0f;

        if (USE_SCREEN_AUTO_BRIGHTNESS_ADJUSTMENT
                && mPowerRequest.screenAutoBrightnessAdjustment != 0.0f) {
            final float adjGamma = FloatMath.pow(SCREEN_AUTO_BRIGHTNESS_ADJUSTMENT_MAX_GAMMA,
                    Math.min(1.0f, Math.max(-1.0f,
                            -mPowerRequest.screenAutoBrightnessAdjustment)));
            gamma *= adjGamma;
            // ++ [framework] Add DEBUG_ON flag, and add debug message.
            if (DEBUG || DEBUG_ON) {
                Slog.d(TAG, "updateAB1:(" + adjGamma + "," + gamma + "," + mPowerRequest.screenAutoBrightnessAdjustment + ")");
            }
            // -- [framework] Add DEBUG_ON flag, and add debug message.
        }

        if (USE_TWILIGHT_ADJUSTMENT) {
            TwilightState state = mTwilight.getCurrentState();
            if (state != null && state.isNight()) {
                final long now = System.currentTimeMillis();
                final float earlyGamma =
                        getTwilightGamma(now, state.getYesterdaySunset(), state.getTodaySunrise());
                final float lateGamma =
                        getTwilightGamma(now, state.getTodaySunset(), state.getTomorrowSunrise());
                gamma *= earlyGamma * lateGamma;
                // ++ [framework] Add DEBUG_ON flag, and add debug message.
                if (DEBUG || DEBUG_ON) {
                    Slog.d(TAG, "updateAB2:(" + earlyGamma + "," + lateGamma + "," + gamma +")");
                }
                // -- [framework] Add DEBUG_ON flag, and add debug message.
            }
        }

        // ++ [framework] JB MR1 4.2 porting from JB 4.1.1
        if (mHtcAutoBrightnessCtrl != null) {
            final int LUX = Math.round(mAmbientLux);
            value = mHtcAutoBrightnessCtrl.getBrightnessBaseOnAlgorithm(LUX, mScreenAutoBrightnessSpline);
        }
        // -- [framework] JB MR1 4.2 porting from JB 4.1.1

        if (gamma != 1.0f) {
            final float in = value;
            value = FloatMath.pow(value, gamma);
            // ++ [framework] Add DEBUG_ON flag, and add debug message.
            if (DEBUG || DEBUG_ON) {
            // -- [framework] Add DEBUG_ON flag, and add debug message.
                Slog.d(TAG, "updateAB3:(" + gamma + "," + in + "," + value +")");
            }
        }

        int newScreenAutoBrightness = clampScreenBrightness(
                Math.round(value * PowerManager.BRIGHTNESS_ON));
        // ++ [framework] Add button light control mechanism
        if (mHtcAutoBrightnessCtrl != null) {
            final int LUX = Math.round(mAmbientLux);
            mButtonValue = mHtcAutoBrightnessCtrl.getButtonValue(LUX);
        }
        // -- [framework] Add button light control mechanism
        if (mScreenAutoBrightness != newScreenAutoBrightness || mScreenBrightnessRampAnimator.getCurrentBrightness() != newScreenAutoBrightness) {
            // ++ [framework] Add DEBUG_ON flag, and add debug message.
            if (DEBUG || DEBUG_ON) {
                Slog.d(TAG, "updateAB4:"
                        + "(" + mScreenAutoBrightness + "->" + newScreenAutoBrightness
                        + "," + value
                        + "," + gamma + ")");
            }
            // -- [framework] Add DEBUG_ON flag, and add debug message.

            mScreenAutoBrightness = newScreenAutoBrightness;
            mLastScreenAutoBrightnessGamma = gamma;
            if (sendUpdate) {
                sendUpdatePowerState(DURATION);
            }
        }
    }

    private static float getTwilightGamma(long now, long lastSunset, long nextSunrise) {
        if (lastSunset < 0 || nextSunrise < 0
                || now < lastSunset || now > nextSunrise) {
            return 1.0f;
        }

        if (now < lastSunset + TWILIGHT_ADJUSTMENT_TIME) {
            return lerp(1.0f, TWILIGHT_ADJUSTMENT_MAX_GAMMA,
                    (float)(now - lastSunset) / TWILIGHT_ADJUSTMENT_TIME);
        }

        if (now > nextSunrise - TWILIGHT_ADJUSTMENT_TIME) {
            return lerp(1.0f, TWILIGHT_ADJUSTMENT_MAX_GAMMA,
                    (float)(nextSunrise - now) / TWILIGHT_ADJUSTMENT_TIME);
        }

        return TWILIGHT_ADJUSTMENT_MAX_GAMMA;
    }

    private static float lerp(float x, float y, float alpha) {
        return x + (y - x) * alpha;
    }

    private void sendOnStateChangedWithWakelock() {
        mDisplaySuspendBlocker.acquire();
        mCallbackHandler.post(mOnStateChangedRunnable);
    }

    private final Runnable mOnStateChangedRunnable = new Runnable() {
        @Override
        public void run() {
            mCallbacks.onStateChanged();
            mDisplaySuspendBlocker.release();
        }
    };

    private void sendOnProximityPositiveWithWakelock() {
        mDisplaySuspendBlocker.acquire();
        mCallbackHandler.post(mOnProximityPositiveRunnable);
    }

    private final Runnable mOnProximityPositiveRunnable = new Runnable() {
        @Override
        public void run() {
            mCallbacks.onProximityPositive();
            mDisplaySuspendBlocker.release();
        }
    };

    private void sendOnProximityNegativeWithWakelock() {
        mDisplaySuspendBlocker.acquire();
        mCallbackHandler.post(mOnProximityNegativeRunnable);
    }

    private final Runnable mOnProximityNegativeRunnable = new Runnable() {
        @Override
        public void run() {
            mCallbacks.onProximityNegative();
            mDisplaySuspendBlocker.release();
        }
    };

    // ++ [framework] Workaround for M7 p-sensor problem.
    private void sendOnAngleDetectorTalkingDetected() {
        mCallbackHandler.post(mOnAngleDetectorTalkingDetectedRunnable);
    }

    private final Runnable mOnAngleDetectorTalkingDetectedRunnable = new Runnable() {
        @Override
        public void run() {
            mCallbacks.onAngleDetectorTalkingDetected();
        }
    };

    private void sendOnAngleDetectorReadingDetected() {
        mCallbackHandler.post(mOnAngleDetectorReadingDetectedRunnable);
    }

    private final Runnable mOnAngleDetectorReadingDetectedRunnable = new Runnable() {
        @Override
        public void run() {
            mCallbacks.onAngleDetectorReadingDetected();
        }
    };
    // -- [framework] Workaround for M7 p-sensor problem.

    public void dump(final PrintWriter pw) {
        synchronized (mLock) {
            pw.println();
            pw.println("Display Controller Locked State:");
            pw.println("  mDisplayReadyLocked=" + mDisplayReadyLocked);
            pw.println("  mPendingRequestLocked=" + mPendingRequestLocked);
            pw.println("  mPendingRequestChangedLocked=" + mPendingRequestChangedLocked);
            pw.println("  mPendingWaitForNegativeProximityLocked="
                    + mPendingWaitForNegativeProximityLocked);
            pw.println("  mPendingUpdatePowerStateLocked=" + mPendingUpdatePowerStateLocked);
        }

        pw.println();
        pw.println("Display Controller Configuration:");
        pw.println("  mScreenBrightnessDimConfig=" + mScreenBrightnessDimConfig);
        pw.println("  mScreenBrightnessRangeMinimum=" + mScreenBrightnessRangeMinimum);
        pw.println("  mScreenBrightnessRangeMaximum=" + mScreenBrightnessRangeMaximum);
        pw.println("  mUseSoftwareAutoBrightnessConfig="
                + mUseSoftwareAutoBrightnessConfig);
        pw.println("  mScreenAutoBrightnessSpline=" + mScreenAutoBrightnessSpline);
        pw.println("  mLightSensorWarmUpTimeConfig=" + mLightSensorWarmUpTimeConfig);

        mHandler.runWithScissors(new Runnable() {
            @Override
            public void run() {
                dumpLocal(pw);
            }
        }, 1000);
    }

    private void dumpLocal(PrintWriter pw) {
        pw.println();
        pw.println("Display Controller Thread State:");
        pw.println("  mPowerRequest=" + mPowerRequest);
        pw.println("  mWaitingForNegativeProximity=" + mWaitingForNegativeProximity);

        pw.println("  mProximitySensor=" + mProximitySensor);
        pw.println("  mProximitySensorEnabled=" + mProximitySensorEnabled);
        pw.println("  mProximityThreshold=" + mProximityThreshold);
        pw.println("  mProximity=" + proximityToString(mProximity));
        pw.println("  mPendingProximity=" + proximityToString(mPendingProximity));
        pw.println("  mPendingProximityDebounceTime="
                + TimeUtils.formatUptime(mPendingProximityDebounceTime));
        pw.println("  mScreenOffBecauseOfProximity=" + mScreenOffBecauseOfProximity);

        pw.println("  mLightSensor=" + mLightSensor);
        pw.println("  mLightSensorEnabled=" + mLightSensorEnabled);
        pw.println("  mLightSensorEnableTime="
                + TimeUtils.formatUptime(mLightSensorEnableTime));
        pw.println("  mAmbientLux=" + mAmbientLux);
        pw.println("  mAmbientLuxValid=" + mAmbientLuxValid);
        pw.println("  mLastObservedLux=" + mLastObservedLux);
        pw.println("  mLastObservedLuxTime="
                + TimeUtils.formatUptime(mLastObservedLuxTime));
        pw.println("  mRecentLightSamples=" + mRecentLightSamples);
        pw.println("  mRecentShortTermAverageLux=" + mRecentShortTermAverageLux);
        pw.println("  mRecentLongTermAverageLux=" + mRecentLongTermAverageLux);
        pw.println("  mDebounceLuxDirection=" + mDebounceLuxDirection);
        pw.println("  mDebounceLuxTime=" + TimeUtils.formatUptime(mDebounceLuxTime));
        pw.println("  mScreenAutoBrightness=" + mScreenAutoBrightness);
        pw.println("  mUsingScreenAutoBrightness=" + mUsingScreenAutoBrightness);
        pw.println("  mLastScreenAutoBrightnessGamma=" + mLastScreenAutoBrightnessGamma);
        pw.println("  mTwilight.getCurrentState()=" + mTwilight.getCurrentState());

        if (mElectronBeamOnAnimator != null) {
            pw.println("  mElectronBeamOnAnimator.isStarted()=" +
                    mElectronBeamOnAnimator.isStarted());
        }
        if (mElectronBeamOffAnimator != null) {
            pw.println("  mElectronBeamOffAnimator.isStarted()=" +
                    mElectronBeamOffAnimator.isStarted());
        }

        if (mPowerState != null) {
            mPowerState.dump(pw);
        }
    }

    private static String proximityToString(int state) {
        switch (state) {
            case PROXIMITY_UNKNOWN:
                return "Unknown";
            case PROXIMITY_NEGATIVE:
                return "Negative";
            case PROXIMITY_POSITIVE:
                return "Positive";
            default:
                return Integer.toString(state);
        }
    }

    private static boolean wantScreenOn(int state) {
        switch (state) {
            case DisplayPowerRequest.SCREEN_STATE_BRIGHT:
            case DisplayPowerRequest.SCREEN_STATE_DIM:
                return true;
        }
        return false;
    }

    /**
     * Asynchronous callbacks from the power controller to the power manager service.
     */
    public interface Callbacks {
        void onStateChanged();
        void onProximityPositive();
        void onProximityNegative();

        // ++ [framework] Workaround for M7 p-sensor problem.
        void onAngleDetectorTalkingDetected();
        void onAngleDetectorReadingDetected();
        // -- [framework] Workaround for M7 p-sensor problem.
    }

    private final class DisplayControllerHandler extends Handler {
        public DisplayControllerHandler(Looper looper) {
            super(looper, null, true /*async*/);
        }

        @Override
        public void handleMessage(Message msg) {
            if (DEBUG_ON) {
                Slog.d(TAG, "msg=" + msg.what);
            }
            switch (msg.what) {
                case MSG_UPDATE_POWER_STATE:
                    updatePowerState();
                    break;

                case MSG_PROXIMITY_SENSOR_DEBOUNCED:
                    debounceProximitySensor();
                    break;

                case MSG_LIGHT_SENSOR_DEBOUNCED:
                    //[++framework] Rework Button light mechanism
                    debounceButtonLight();
                    //[--framework] Rework Button light mechanism
                    debounceLightSensor();
                    break;

                //[++framework] Modify the brightness flow for setSmoothBacklight function
                case MSG_UPDATE_BRIGHTNESS_STATE:
                    if (mScreenBrightnessRampAnimator != null) {
                        mScreenBrightnessRampAnimator.animateTo(msg.arg1, -1, -1, msg.arg2);
                    }
                    break;

                case MSG_UPDATE_BRIGHTNESS_STATE_FOR_CAMERA:
                    if (mScreenBrightnessRampAnimator != null) {
                        mScreenBrightnessRampAnimator.animateTo(msg.arg1, -1, -1, msg.arg2, BRIGHTNESS_ANIMATION_CAMERA_LEAVE_MODE);
                    }
                    break;
                //[--framework] Modify the brightness flow for setSmoothBacklight function

                //[++framework] Rework Button light mechanism
                case MSG_BUTTON_LIGHT_DEBOUNCED:
                    debounceButtonLight();
                    break;
                //[--framework] Rework Button light mechanism

                // ++ [framework] Workaround for M7 p-sensor problem after ending call
                case MSG_DELAY_SCREEN_ON_DURATION:
                    handleDelayScreenOnTimeoutAfterEndCall();
                    break;
                // -- [framework] Workaround for M7 p-sensor problem after ending call
            }
        }
    }

    private final SensorEventListener mProximitySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mProximitySensorEnabled) {
                final long time = SystemClock.uptimeMillis();
                final float distance = event.values[0];
                boolean positive = distance >= 0.0f && distance < mProximityThreshold;
                // ++ [framework] Add debug message
                Slog.d(TAG, "P.onSensorChanged: " + (positive ? "NEAR" : "FAR"));
                // -- [framework] Add debug message
                handleProximitySensorEvent(time, positive);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not used.
        }
    };

    private final SensorEventListener mLightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mLightSensorEnabled) {

                //++[framework] For Car mode, peter li 20121031
                if (mHtcAutoBrightnessCtrl != null) {
                    mHtcAutoBrightnessCtrl.setLightSensorLevel((int)event.values[0], true);
                }
                //--[framework] For Car mode, peter li 20121031

                final long time = SystemClock.uptimeMillis();
                final float lux = event.values[0];
                handleLightSensorEvent(time, lux);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not used.
        }
    };

    private final TwilightService.TwilightListener mTwilightListener =
            new TwilightService.TwilightListener() {
        @Override
        public void onTwilightStateChanged() {
            mTwilightChanged = true;
            updatePowerState();
        }
    };

    // ++ [framework] For PMS

    //++[framework] add API to get Current Brightness Value, peter li 20120419
    protected int fetchCurrentBrightnessValue() {
        int lcdValue = mScreenBrightnessRampAnimator.getCurrentBrightness();

        if (DEBUG_ON) {
            Slog.d(TAG, "fetchCurrentBrightnessValue: lcdValue=" + lcdValue);
        }

        return lcdValue;
    }
    //--[framework] add API to get Current Brightness Value, peter li 20120419

    // ++ [HTC_PHONE] s: htc shawn
    protected boolean getProximitySensorActive() {
        // ++ [framework] Add Htc Log.
        Slog.d(TAG, "getPSensorActive=" + mProximity);
        // -- [framework] Add Htc Log.
        return (mProximity == PROXIMITY_POSITIVE);
    }
    // -- [HTC_PHONE] e: htc shawn

    protected float getAmbientLux() {
        return mAmbientLux;
    }

    protected HtcAutoBrightnessCtrl getHtcAutoBrightnessCtrl() {
        if (DEBUG_ON && mHtcAutoBrightnessCtrl == null) {
            Slog.d(TAG, "getHtcAutoBrightnessCtrl: null found.");
        }
        return mHtcAutoBrightnessCtrl;
    }

    // ++ [framework] Workaround for M7 p-sensor problem.
    protected void notifyScreenTimeout(boolean isTimeout) {
        if (mHtcAngleDetector != null) {
            if (mPowerRequest != null && mPowerRequest.forceScreenOn) {
                mPowerRequest.forceScreenOn = false;
            }
            mHtcAngleDetector.notifyScreenTimeout(isTimeout);
        }

        // ++ [framework] Workaround for turning on screen when screen is off.
        if (mPowerRequest != null && mPowerRequest.turnOnScreenOnce) {
            mPowerRequest.turnOnScreenOnce = false;
        }
        // -- [framework] Workaround for turning on screen when screen is off.

    }
    // -- [framework] Workaround for M7 p-sensor problem.
    // -- [framework] For PMS

    protected final class DPCInternalAPI {
        // ++ [framework] Condition controlled auto brightness feature, 2012/01/04
        protected Spline createAutoBrightnessSpline(int[] lux, int[] brightness) {
            return DisplayPowerController.this.createAutoBrightnessSpline(lux, brightness);
        }

        protected void setScreenBrightnessMode(int mode) {
            if (mPMSInternalAPI != null) {
                mPMSInternalAPI.setScreenBrightnessMode(mode);
            }
        }

        protected void setSmoothBacklight(int targetLcdValue, final int DURATION) {
            // Skip the rest when screen is off.
            if (!mPowerState.isScreenOn()) {
                return;
            }

            synchronized (mLock) {
                if (targetLcdValue != mScreenBrightnessRampAnimator.getCurrentBrightness()) {
                    //[++framework] Modify the brightness flow for setSmoothBacklight function
                    Message msg = mHandler.obtainMessage(MSG_UPDATE_BRIGHTNESS_STATE, targetLcdValue, DURATION);
                    msg.setAsynchronous(true);
                    mHandler.sendMessage(msg);
                    //[--framework] Modify the brightness flow for setSmoothBacklight function
                }
            }
        }

        //[++framework] Modify the brightness flow for setSmoothBacklight function
        protected void setSmoothBacklightForCamera(int targetLcdValue, final int DURATION) {
            // Skip the rest when screen is off.
            if (!mPowerState.isScreenOn()) {
                return;
            }

            synchronized (mLock) {
                if (targetLcdValue != mScreenBrightnessRampAnimator.getCurrentBrightness()) {
                    Message msg = mHandler.obtainMessage(MSG_UPDATE_BRIGHTNESS_STATE_FOR_CAMERA, targetLcdValue, DURATION);
                    msg.setAsynchronous(true);
                    mHandler.sendMessage(msg);
                }
            }
        }
        //[--framework] Modify the brightness flow for setSmoothBacklight function

        protected int getCurrentBrightness() {
            return mScreenBrightnessRampAnimator.getCurrentBrightness();
        }

        protected int getTargetBrightness() {
            return mScreenBrightnessRampAnimator.getTargetBrightness();
        }

        protected void cancelAnimation() {
            // Skip the rest when screen is off.
            if (!mPowerState.isScreenOn()) {
                return;
            }

            mScreenBrightnessRampAnimator.cancelAnimation();
        }

        protected void forceBrightnessUpdate(final int DURATION) {
            // Skip the rest when screen is off.
            if (!mPowerState.isScreenOn()) {
                return;
            }

            if (mUseSoftwareAutoBrightnessConfig && mPowerRequest.useAutoBrightness) {
                updateAutoBrightness(true, DURATION);
            }
        }

        // ++ [framework] Workaround for M7 p-sensor problem.
        protected void sendUpdatePowerState() {
            if (DEBUG_ON) {
                Slog.d(TAG, "DPCAPI: sendUPS");
            }
            DisplayPowerController.this.sendUpdatePowerState();
        }
        // -- [framework] Workaround for M7 p-sensor problem.

        // -- [framework] Condition controlled auto brightness feature, 2012/01/04

        // ++ [framework] Workaround for M7 p-sensor problem after ending call
        protected void sendDelayScreenOnStateAfterEndCall(int reason) {
            if (DEBUG_ON) {
                Slog.d(TAG, "DPCAPI: sDSOSAEC");
            }
            DisplayPowerController.this.sendDelayScreenOnStateAfterEndCall(reason);
        }
        // -- [framework] Workaround for M7 p-sensor problem after ending call

        private DPCInternalAPI() {
        }
    }

    // ++ [framework] Workaround for M7 p-sensor problem after ending call
    protected boolean inEndCallMode() {
        if (mDelayScreenOnReason != DELAY_SCREEN_UNKNOWN) {
            if (DEBUG_ON) {
                Slog.d(TAG, "iECM");
            }
            return true;
        }
        return false;
    }

    protected void clearEndCallMode() {

        if (DEBUG_ON) {
            Slog.d(TAG, "cECM");
        }

        if (mHandler != null) {
            mHandler.removeMessages(MSG_DELAY_SCREEN_ON_DURATION);
        }

        //clear delayscreenon flag and mProximityPositive
        if (mPMSInternalAPI != null) {
            mPMSInternalAPI.clearDelayScreenOn();
        }

        if (mPowerRequest != null) {
            mPowerRequest.delayScreenOn = false;
        }

        mDelayScreenOnReason = DELAY_SCREEN_UNKNOWN;
        mProximityAfterEndCall = PROXIMITY_UNKNOWN;

        //disable P-sensor
        disableProximitySensorAfterEndCall();
        sendOnProximityNegativeWithWakelock();
        sendUpdatePowerStateLocked();
    }

    protected boolean needDelayScreenOnAfterEndCall() {

        if (mDelayScreenOnReason != DELAY_SCREEN_UNKNOWN) {
            if (DEBUG_ON) {
                Slog.d(TAG, "delay runing=" + mDelayScreenOnReason);
            }
            return false;
        }

        //Check if end call during Near status
        if (mProximity == PROXIMITY_POSITIVE) {
            if (DEBUG_ON) {
                Slog.d(TAG, "delay on near");
            }

            if (mHandler != null) {
                mHandler.removeMessages(MSG_DELAY_SCREEN_ON_DURATION);

                Message msg = mHandler.obtainMessage(MSG_DELAY_SCREEN_ON_DURATION);
                msg.setAsynchronous(true);
                mHandler.sendMessageDelayed(msg, DELAY_SCREEN_ON_FROM_NEAR_DURATION);
                if (DEBUG_ON) {
                    Slog.d(TAG, "s dt=" + DELAY_SCREEN_ON_FROM_NEAR_DURATION);
                }
                mDelayScreenOnReason = DELAY_SCREEN_NEAR;
                return false;
            }
        }

        //Check if end call during Far status and screen is off
        if ((mProximity == PROXIMITY_NEGATIVE) && (mPMSInternalAPI != null) && (!mPMSInternalAPI.isActualScreenOn())) {

            if (DEBUG_ON) {
                Slog.d(TAG, "delay on far");
            }

            if (mHandler != null) {
                mHandler.removeMessages(MSG_DELAY_SCREEN_ON_DURATION);

                Message msg = mHandler.obtainMessage(MSG_DELAY_SCREEN_ON_DURATION);
                msg.setAsynchronous(true);
                mHandler.sendMessageDelayed(msg, DELAY_SCREEN_ON_FROM_FAR_TOTAL_DURATION);
                if (DEBUG_ON) {
                    Slog.d(TAG, "s dt=" + DELAY_SCREEN_ON_FROM_FAR_TOTAL_DURATION);
                }
                mDelayScreenOnReason = DELAY_SCREEN_FAR;
                mProximityAfterEndCall = mProximity;

                notifyReleaseProximityWakeLock(true, DELAY_SCREEN_ON_FROM_FAR_ANGLE_DURATION);

                return true;
            }

        }

        return false;
    }

    private void handleDelayScreenOnTimeoutAfterEndCall() {
        if (DEBUG_ON) {
            Slog.d(TAG, "d o Timeout");
        }

        if (mPMSInternalAPI != null) {
            mPMSInternalAPI.clearDelayScreenOn();
        }

        if (mPowerRequest != null) {
            mPowerRequest.delayScreenOn = false;
        }

        mDelayScreenOnReason = DELAY_SCREEN_UNKNOWN;
        mProximityAfterEndCall = PROXIMITY_UNKNOWN;

        //disable P-sensor
        disableProximitySensorAfterEndCall();

        if (mPMSInternalAPI != null) {
            mPMSInternalAPI.forceEnterSleepMode();
        }

    }

    private void handleProximitySensorAfterEndCall(int proximity) {

        if (mDelayScreenOnReason == DELAY_SCREEN_NEAR) {

            if (proximity == PROXIMITY_NEGATIVE) {

                if (DEBUG_ON) {
                    Slog.d(TAG, "r dso 1");
                }

                if (mHandler != null) {
                    mHandler.removeMessages(MSG_DELAY_SCREEN_ON_DURATION);
                }

                if (mPowerRequest != null) {
                    mPowerRequest.delayScreenOn = false;
                }

                mDelayScreenOnReason = DELAY_SCREEN_UNKNOWN;
                mProximityAfterEndCall = PROXIMITY_UNKNOWN;
            }

        } else if (mDelayScreenOnReason == DELAY_SCREEN_FAR) {

            if (proximity == PROXIMITY_NEGATIVE) {
                if (mProximityAfterEndCall == PROXIMITY_POSITIVE) {

                    if (DEBUG_ON) {
                        Slog.d(TAG, "r dso 2");
                    }

                    if (mHandler != null) {
                        mHandler.removeMessages(MSG_DELAY_SCREEN_ON_DURATION);
                    }

                    if (mPMSInternalAPI != null) {
                        mPMSInternalAPI.clearDelayScreenOn();
                    }

                    if (mPowerRequest != null) {
                        mPowerRequest.delayScreenOn = false;
                    }

                    mDelayScreenOnReason = DELAY_SCREEN_UNKNOWN;
                    mProximityAfterEndCall = PROXIMITY_UNKNOWN;

                    //disable P-sensor
                    disableProximitySensorAfterEndCall();
                    sendOnProximityNegativeWithWakelock();
                    sendUpdatePowerStateLocked();
                }
            } else if (proximity == PROXIMITY_POSITIVE) {
                mProximityAfterEndCall = PROXIMITY_POSITIVE;
            }

        }

    }

    private void sendDelayScreenOnStateAfterEndCall(int reason) {
        if (DEBUG_ON) {
            Slog.d(TAG, "sDSOSAEC="+reason);
        }
        boolean turnOnScreen = false;

        if (mHtcAngleDetector != null) {
            mHtcAngleDetector.disableDetector_Ext();
        }

        if (mProximityAfterEndCall == PROXIMITY_NEGATIVE) {
            if (reason == PowerManager.DELAY_SCREEN_ON_ANGLE_DETECTOR_REASON_POSE_CHANGE) {
                turnOnScreen = true;
            } else if (reason == PowerManager.DELAY_SCREEN_ON_ANGLE_DETECTOR_REASON_TIMEOUT) {
                turnOnScreen = true;
            }
        }

        if (turnOnScreen) {

            if (mHandler != null) {
                mHandler.removeMessages(MSG_DELAY_SCREEN_ON_DURATION);
            }

            if (mPMSInternalAPI != null) {
                mPMSInternalAPI.clearDelayScreenOn();
            }

            if (mPowerRequest != null) {
                mPowerRequest.delayScreenOn = false;
            }

            mDelayScreenOnReason = DELAY_SCREEN_UNKNOWN;
            mProximityAfterEndCall = PROXIMITY_UNKNOWN;

            //disable P-sensor
            disableProximitySensorAfterEndCall();
            sendOnProximityNegativeWithWakelock();
            sendUpdatePowerStateLocked();
        }

    }

    protected void notifyReleaseProximityWakeLock(boolean status, int timeout) {
        if (DEBUG_ON) {
            Slog.d(TAG, "nRPWL(" + status + "," + timeout + ")");
        }
        if (mHtcAngleDetector != null) {
            mHtcAngleDetector.notifyReleaseProximityWakeLock(status, timeout);
        }
    }

    private void disableProximitySensorAfterEndCall() {
        setProximitySensorEnabled(false);
        mWaitingForNegativeProximity = false;
        mScreenOffBecauseOfProximity = false;
    }
    // -- [framework] Workaround for M7 p-sensor problem after ending call

}
