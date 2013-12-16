/*
 * Copyright (C) 2007 The Android Open Source Project
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

import com.android.internal.app.IBatteryStats;
import com.android.server.BatteryService;
import com.android.server.EventLogTags;
import com.android.server.LightsService;
import com.android.server.TwilightService;
import com.android.server.Watchdog;
import com.android.server.am.ActivityManagerService;
import com.android.server.display.DisplayManagerService;
import com.android.server.dreams.DreamManagerService;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.SensorManager;
import android.hardware.SystemSensorManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IPowerManager;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemService;
import android.os.UserHandle;
import android.os.WorkSource;
import android.provider.Settings;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.util.TimeUtils;
import android.view.WindowManagerPolicy;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import libcore.util.Objects;

// [framework]
import com.htc.htcjavaflag.HtcBuildFlag;
// [framework]

//+ Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.
import android.app.UiModeManager;
//- Greg Tsai (2011/06/21).

//+ [framework] for DimDelay to 7 sec, ScreenOffDely to 15 sec when phone off hook, menghui_wu
import android.os.SystemProperties;
//- [framework] for DimDelay to 7 sec, ScreenOffDely to 15 sec when phone off hook, menghui_wu

import com.htc.content.Intents; // Import this to use HTC defined intents. Charles Tsai, 2012.08.31.

// ++ [framework] To support log reduction
import java.util.Formatter;
// -- [framework] To support log reduction

// ++ [framework] acquireWakeLock NPE happened before PowerManagerService init. Rhed_Jao, 20130603.
//                This will make IBinder which pass to acquireWakeLock was GC immediately sometimes, and it cannot add ref to kernel.
//                Device got hang later, when surface flinger try to binder transaction.
import java.util.HashSet;
// -- [framework] acquireWakeLock NPE happened before PowerManagerService init. Rhed_Jao, 20130603

//++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
//--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower

/**
 * The power manager service is responsible for coordinating power management
 * functions on the device.
 */
public final class PowerManagerService extends IPowerManager.Stub
        implements Watchdog.Monitor {
    private static final String TAG = "PowerManagerService";

    // ++ [framework] Add master debug control.
    private static final boolean DEBUG_ON = HtcBuildFlag.Htc_DEBUG_flag;
    // -- [framework] Add master debug control.

    private static final boolean DEBUG = false;
    private static final boolean DEBUG_SPEW = DEBUG && true;

    // Message: Sent when a user activity timeout occurs to update the power state.
    private static final int MSG_USER_ACTIVITY_TIMEOUT = 1;
    // Message: Sent when the device enters or exits a napping or dreaming state.
    private static final int MSG_SANDMAN = 2;
    // Message: Sent when the screen on blocker is released.
    private static final int MSG_SCREEN_ON_BLOCKER_RELEASED = 3;
    // Message: Sent to poll whether the boot animation has terminated.
    private static final int MSG_CHECK_IF_BOOT_ANIMATION_FINISHED = 4;

    //[++framework] need to post ClearScreenTimeoutTask to clear once flag
    // Message: Sent to post ClearScreenTimeoutTask to clear once flag.
    private static final int MSG_SEND_CLEAR_SCREEN_TIMEOUT_TASK = 8;
    //[--framework] need to post ClearScreenTimeoutTask to clear once flag
    //++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
    private static final int MSG_CHECK_IF_OOBE_STATUS_FINISHED = 10;
    private static final int MSG_CHECK_IF_OOBE_STATUS_FINISHED_TIMEOUT = 11;
    //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower

    // Dirty bit: mWakeLocks changed
    private static final int DIRTY_WAKE_LOCKS = 1 << 0;
    // Dirty bit: mWakefulness changed
    private static final int DIRTY_WAKEFULNESS = 1 << 1;
    // Dirty bit: user activity was poked or may have timed out
    private static final int DIRTY_USER_ACTIVITY = 1 << 2;
    // Dirty bit: actual display power state was updated asynchronously
    private static final int DIRTY_ACTUAL_DISPLAY_POWER_STATE_UPDATED = 1 << 3;
    // Dirty bit: mBootCompleted changed
    private static final int DIRTY_BOOT_COMPLETED = 1 << 4;
    // Dirty bit: settings changed
    private static final int DIRTY_SETTINGS = 1 << 5;
    // Dirty bit: mIsPowered changed
    private static final int DIRTY_IS_POWERED = 1 << 6;
    // Dirty bit: mStayOn changed
    private static final int DIRTY_STAY_ON = 1 << 7;
    // Dirty bit: battery state changed
    private static final int DIRTY_BATTERY_STATE = 1 << 8;
    // Dirty bit: proximity state changed
    private static final int DIRTY_PROXIMITY_POSITIVE = 1 << 9;
    // Dirty bit: screen on blocker state became held or unheld
    private static final int DIRTY_SCREEN_ON_BLOCKER_RELEASED = 1 << 10;
    // Dirty bit: dock state changed
    private static final int DIRTY_DOCK_STATE = 1 << 11;

    // Wakefulness: The device is asleep and can only be awoken by a call to wakeUp().
    // The screen should be off or in the process of being turned off by the display controller.
    private static final int WAKEFULNESS_ASLEEP = 0;
    // Wakefulness: The device is fully awake.  It can be put to sleep by a call to goToSleep().
    // When the user activity timeout expires, the device may start napping or go to sleep.
    private static final int WAKEFULNESS_AWAKE = 1;
    // Wakefulness: The device is napping.  It is deciding whether to dream or go to sleep
    // but hasn't gotten around to it yet.  It can be awoken by a call to wakeUp(), which
    // ends the nap. User activity may brighten the screen but does not end the nap.
    private static final int WAKEFULNESS_NAPPING = 2;
    // Wakefulness: The device is dreaming.  It can be awoken by a call to wakeUp(),
    // which ends the dream.  The device goes to sleep when goToSleep() is called, when
    // the dream ends or when unplugged.
    // User activity may brighten the screen but does not end the dream.
    private static final int WAKEFULNESS_DREAMING = 3;

    // Summarizes the state of all active wakelocks.
    private static final int WAKE_LOCK_CPU = 1 << 0;
    private static final int WAKE_LOCK_SCREEN_BRIGHT = 1 << 1;
    private static final int WAKE_LOCK_SCREEN_DIM = 1 << 2;
    private static final int WAKE_LOCK_BUTTON_BRIGHT = 1 << 3;
    private static final int WAKE_LOCK_PROXIMITY_SCREEN_OFF = 1 << 4;
    private static final int WAKE_LOCK_STAY_AWAKE = 1 << 5; // only set if already awake

    // Summarizes the user activity state.
    private static final int USER_ACTIVITY_SCREEN_BRIGHT = 1 << 0;
    private static final int USER_ACTIVITY_SCREEN_DIM = 1 << 1;

    // Default and minimum screen off timeout in milliseconds.
    private static final int DEFAULT_SCREEN_OFF_TIMEOUT = 15 * 1000;
    private static final int MINIMUM_SCREEN_OFF_TIMEOUT = 10 * 1000;

    // The screen dim duration, in milliseconds.
    // This is subtracted from the end of the screen off timeout so the
    // minimum screen off timeout should be longer than this.
    private static final int SCREEN_DIM_DURATION = 7 * 1000;

    // The maximum screen dim time expressed as a ratio relative to the screen
    // off timeout.  If the screen off timeout is very short then we want the
    // dim timeout to also be quite short so that most of the time is spent on.
    // Otherwise the user won't get much screen on time before dimming occurs.
    private static final float MAXIMUM_SCREEN_DIM_RATIO = 0.2f;

    // The name of the boot animation service in init.rc.
    private static final String BOOT_ANIMATION_SERVICE = "bootanim";

    // Poll interval in milliseconds for watching boot animation finished.
    private static final int BOOT_ANIMATION_POLL_INTERVAL = 200;

    // If the battery level drops by this percentage and the user activity timeout
    // has expired, then assume the device is receiving insufficient current to charge
    // effectively and terminate the dream.
    private static final int DREAM_BATTERY_LEVEL_DRAIN_CUTOFF = 5;

    // ++ [framework] Add screen dim mechanism when dongle mode is turned on
    private static final int SCREEN_ENTER_DIM_IN_DONGLE_MODE = 13 * 1000;
    // -- [framework] Add screen dim mechanism when dongle mode is turned on

    // Message: Sent to post ClearScreenTimeoutTask to clear once flag.
    private static final int SEND_CLEAR_SCREEN_TIMEOUT_DELAY_VALUE = 0; // 0ms
    //[--framework] need to post ClearScreenTimeoutTask to clear once flag

    private Context mContext;
    private LightsService mLightsService;
    private BatteryService mBatteryService;
    private DisplayManagerService mDisplayManagerService;
    private IBatteryStats mBatteryStats;
    private HandlerThread mHandlerThread;
    private PowerManagerHandler mHandler;

    private WindowManagerPolicy mPolicy;
    private Notifier mNotifier;
    private DisplayPowerController mDisplayPowerController;
    private WirelessChargerDetector mWirelessChargerDetector;
    private SettingsObserver mSettingsObserver;
    private DreamManagerService mDreamManager;
    private LightsService.Light mAttentionLight;

    private final Object mLock = new Object();

    // A bitfield that indicates what parts of the power state have
    // changed and need to be recalculated.
    private int mDirty;

    // Indicates whether the device is awake or asleep or somewhere in between.
    // This is distinct from the screen power state, which is managed separately.
    private int mWakefulness;

    // True if MSG_SANDMAN has been scheduled.
    private boolean mSandmanScheduled;

    // Table of all suspend blockers.
    // There should only be a few of these.
    private final ArrayList<SuspendBlocker> mSuspendBlockers = new ArrayList<SuspendBlocker>();

    // Table of all wake locks acquired by applications.
    private final ArrayList<WakeLock> mWakeLocks = new ArrayList<WakeLock>();

    // ++ [framework] For CPU performance controlling mechanism.
    private final ArrayList<HtcCpuCtrl> mHtcCpuCtrls = new ArrayList<HtcCpuCtrl>();
    // -- [framework] For CPU performance controlling mechanism.

    // A bitfield that summarizes the state of all active wakelocks.
    private int mWakeLockSummary;

    // If true, instructs the display controller to wait for the proximity sensor to
    // go negative before turning the screen on.
    private boolean mRequestWaitForNegativeProximity;

    // Timestamp of the last time the device was awoken or put to sleep.
    private long mLastWakeTime;
    private long mLastSleepTime;

    // True if we need to send a wake up or go to sleep finished notification
    // when the display is ready.
    private boolean mSendWakeUpFinishedNotificationWhenReady;
    private boolean mSendGoToSleepFinishedNotificationWhenReady;

    // Timestamp of the last call to user activity.
    private long mLastUserActivityTime;
    private long mLastUserActivityTimeNoChangeLights;

    // A bitfield that summarizes the effect of the user activity timer.
    // A zero value indicates that the user activity timer has expired.
    private int mUserActivitySummary;

    // The desired display power state.  The actual state may lag behind the
    // requested because it is updated asynchronously by the display power controller.
    private final DisplayPowerRequest mDisplayPowerRequest = new DisplayPowerRequest();

    // The time the screen was last turned off, in elapsedRealtime() timebase.
    private long mLastScreenOffEventElapsedRealTime;

    // True if the display power state has been fully applied, which means the display
    // is actually on or actually off or whatever was requested.
    private boolean mDisplayReady;

    // True if holding a wake-lock to block suspend of the CPU.
    private boolean mHoldingWakeLockSuspendBlocker;

    // The suspend blocker used to keep the CPU alive when wake locks have been acquired.
    private final SuspendBlocker mWakeLockSuspendBlocker;

    // The screen on blocker used to keep the screen from turning on while the lock
    // screen is coming up.
    private final ScreenOnBlockerImpl mScreenOnBlocker;

    // The display blanker used to turn the screen on or off.
    private final DisplayBlankerImpl mDisplayBlanker;

    // True if systemReady() has been called.
    private boolean mSystemReady;

    // True if boot completed occurred.  We keep the screen on until this happens.
    private boolean mBootCompleted;

    // True if the device is plugged into a power source.
    private boolean mIsPowered;

    // The current plug type, such as BatteryManager.BATTERY_PLUGGED_WIRELESS.
    private int mPlugType;

    // The current battery level percentage.
    private int mBatteryLevel;

    // The battery level percentage at the time the dream started.
    // This is used to terminate a dream and go to sleep if the battery is
    // draining faster than it is charging and the user activity timeout has expired.
    private int mBatteryLevelWhenDreamStarted;

    // The current dock state.
    private int mDockState = Intent.EXTRA_DOCK_STATE_UNDOCKED;

    // True if the device should wake up when plugged or unplugged.
    private boolean mWakeUpWhenPluggedOrUnpluggedConfig;

    // True if dreams are supported on this device.
    private boolean mDreamsSupportedConfig;

    // Default value for dreams enabled
    private boolean mDreamsEnabledByDefaultConfig;

    // Default value for dreams activate-on-sleep
    private boolean mDreamsActivatedOnSleepByDefaultConfig;

    // Default value for dreams activate-on-dock
    private boolean mDreamsActivatedOnDockByDefaultConfig;

    // True if dreams are enabled by the user.
    private boolean mDreamsEnabledSetting;

    // True if dreams should be activated on sleep.
    private boolean mDreamsActivateOnSleepSetting;

    // True if dreams should be activated on dock.
    private boolean mDreamsActivateOnDockSetting;

    // The screen off timeout setting value in milliseconds.
    private int mScreenOffTimeoutSetting;

    // The maximum allowable screen off timeout according to the device
    // administration policy.  Overrides other settings.
    private int mMaximumScreenOffTimeoutFromDeviceAdmin = Integer.MAX_VALUE;

    // The stay on while plugged in setting.
    // A bitfield of battery conditions under which to make the screen stay on.
    private int mStayOnWhilePluggedInSetting;

    // True if the device should stay on.
    private boolean mStayOn;

    // True if the proximity sensor reads a positive result.
    private boolean mProximityPositive;

    // ++ [framework] Workaround for M7 p-sensor problem.
    private boolean mADTalkingDetected;
    // -- [framework] Workaround for M7 p-sensor problem.

    // Screen brightness setting limits.
    private int mScreenBrightnessSettingMinimum;
    private int mScreenBrightnessSettingMaximum;
    private int mScreenBrightnessSettingDefault;

    // The screen brightness setting, from 0 to 255.
    // Use -1 if no value has been set.
    private int mScreenBrightnessSetting;

    // The screen auto-brightness adjustment setting, from -1 to 1.
    // Use 0 if there is no adjustment.
    private float mScreenAutoBrightnessAdjustmentSetting;

    // The screen brightness mode.
    // One of the Settings.System.SCREEN_BRIGHTNESS_MODE_* constants.
    private int mScreenBrightnessModeSetting;

    // The screen brightness setting override from the window manager
    // to allow the current foreground activity to override the brightness.
    // Use -1 to disable.
    private int mScreenBrightnessOverrideFromWindowManager = -1;

    // The user activity timeout override from the window manager
    // to allow the current foreground activity to override the user activity timeout.
    // Use -1 to disable.
    private long mUserActivityTimeoutOverrideFromWindowManager = -1;

    // The screen brightness setting override from the settings application
    // to temporarily adjust the brightness until next updated,
    // Use -1 to disable.
    private int mTemporaryScreenBrightnessSettingOverride = -1;

    // The screen brightness adjustment setting override from the settings
    // application to temporarily adjust the auto-brightness adjustment factor
    // until next updated, in the range -1..1.
    // Use NaN to disable.
    private float mTemporaryScreenAutoBrightnessAdjustmentSettingOverride = Float.NaN;

    // Time when we last logged a warning about calling userActivity() without permission.
    private long mLastWarningAboutUserActivityPermission = Long.MIN_VALUE;

    private native void nativeInit();
    private static native void nativeShutdown();
    private static native void nativeReboot(String reason) throws IOException;

    private static native void nativeSetPowerState(boolean screenOn, boolean screenBright);
    private static native void nativeAcquireSuspendBlocker(String name);
    private static native void nativeReleaseSuspendBlocker(String name);
    private static native void nativeSetInteractive(boolean enable);
    private static native void nativeSetAutoSuspend(boolean enable);

    // ++ [framework] add efs sync for shutdown, add wait method prevent modem crash, menghui_wu, 2012/03/06
    private native void nativeShutdownEFSSync();
    private native void nativeShutdownEFSSync_wait(int msTimeout);
    // -- [framework] add efs sync for shutdown, add wait method prevent modem crash, menghui_wu, 2012/03/06

    // ++ [framework] Add system wipe API
    private native void nativeWipeSystem(byte[] imei, byte[] encMsg);
    // -- [framework] Add system wipe APi

    // ++ [framework] JB porting, for CPU perf wake lock
    public static native void nativeAcquireCpuPerfWakeLock();
    public static native void nativeReleaseCpuPerfWakeLock();
    // -- [framework] JB porting, for CPU perf wake lock

    // ++ [framework] JB porting, for DVCS wakelock, menghui_wu, 2011/09/27
    private static native void nativeAcquireCpuApDvcsWakeLock();
    private static native void nativeReleaseCpuApDvcsWakeLock();
    // -- [framework] JB porting, for DVCS wakelock, menghui_wu, 2011/09/27

    // ++ [framework] JB porting, for CPU single core
    private static native void nativeAcquireCpuSingleCoreWakeLock();
    private static native void nativeReleaseCpuSingleCoreWakeLock();
    // -- [framework] JB porting, for CPU single core

    // ++ [framework] Add for CPU Control feature.
    private static native void nativeAcquireCpuFreqMinWakeLock(int level);
    private static native void nativeReleaseCpuFreqMinWakeLock();
    private static native void nativeAcquireCpuFreqMaxWakeLock(int level);
    private static native void nativeReleaseCpuFreqMaxWakeLock();
    private static native void nativeAcquireCpuNumMinWakeLock(int level);
    private static native void nativeReleaseCpuNumMinWakeLock();
    private static native void nativeAcquireCpuNumMaxWakeLock(int level);
    private static native void nativeReleaseCpuNumMaxWakeLock();

    //[++framework] To control screen off by Proximity Sensor, peter li , 20130418
    private static native void nativeSetProximityStateWithScreenOff(boolean value);
    //[--framework] To control screen off by Proximity Sensor, peter li , 20130418

    private static final int CPU_LOCK_MASK = PowerManager.CPU_MAX_FREQ
                                           | PowerManager.CPU_MIN_FREQ
                                           | PowerManager.CPU_MAX_NUM
                                           | PowerManager.CPU_MIN_NUM;
    private int mCpuFreqMinCount = 0;
    private int mCpuFreqMaxCount = 0;
    private int mCpuNumMinCount = 0;
    private int mCpuNumMaxCount = 0;

    private int mCpuPerfCount = 0;
    // -- [framework] Add for CPU Control feature.

    // ++ [framework] To determine actual screen state.
    private boolean mActualScreenOn = true;
    // -- [framework] To determine actual screen state.

    //+ Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.
    // define the first sense version to enable this new behavior
    private static final float SENSE_VERSION_FOR_DESKDOCK_DIM_BEHAVIOR = 3.5f;
    private static boolean mbEnableDeskDockBehavior = false;	// Flag to turn on/off this new behavior
    // default setting of dim timeout setting for desk dock mode
    private static final int DEFAULT_DESKDOCK_DIM_SCREEN_VALUE = 10;	// Default is 10, unit is "minute" 
    private static final int DESKDOCK_MILLISECOND_PER_UNIT = 60000;	// constant for calcuating timeout value to ms.
    // key for retrieving timeout value from Setting DB
    private static final String DESKDOCK_DIM_SCREEN = "deskdock_dim_screen"; 
    private int mScreenOffDelayForDeskDock;	// variable to keep the dim timeout value
    private boolean mDeskModeEnabled = false;	// variable to record if it is Desk Dock mode currently.
    //- Greg Tsai (2011/06/21).

    // ++ [framework] PMS internal API for Power related modules only
    private PMSInternalAPI mPMSInternalAPI;
    // -- [framework] PMS internal API for Power related modules only

    // ++ [framework] Add screen timeout mechanism when dongle mode is turned on
    private HtcDongleMode mHtcDongleMode;
    // -- [framework] Add screen timeout mechanism when dongle mode is turned on

    // ++ [framework] Collect wake lock record, dump record, and scan bad behavior wake lock.
    private HtcWakeLockMonitor mHtcWakeLockMonitor;
    // ++ [framework] Collect wake lock record, dump record, and scan bad behavior wake lock.

    // ++ [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.
    private final int DEFAULT_SYSTEM_TIMEOUT = 60000; // in ms
//    private long mTimeoutTaskAssignedTime = -1;
    private int mScreenTimeout_Once = -1;
    private int mScreenTimeout_Always = -1;
    private int mScreenTimeout_System = DEFAULT_SYSTEM_TIMEOUT;
    // -- [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.

    //+[framework] Recude log, menghui_wu, 2012/02/03
    private static HtcPMSExtension mHtcPMSExtension;
    //-[framework] Recude log, menghui_wu, 2012/02/03

    //[++framework] Add new API for HtcShutdownThread, peter_li 20130426 
    private static int WAIT_FOR_SUSPEND_TO_COMPLETE = 5000;  // Milliseconds.
    private static ConditionVariable mCondition = new ConditionVariable();
    private final Object mShutdownLock = new Object();
    private static boolean startGoToSleepByShutdown = false;
    private static boolean doneNativeSetAutoSuspend = false;
    //[--framework] Add new API for HtcShutdownThread, peter_li 20130426 

    //+[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02
    private Handler mPowerSaverHandler;
    private HtcPowerSaver mHtcPowerSaver;
    private Runnable runHtcPowerSaverCheck = new Runnable() {
        public void run() {
            Slog.d(TAG, "runHtcPowerSaverCheck ======================================");     
            synchronized (this) {          
                mHtcPowerSaver.check();
            }
        }
    };
    //-[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02

    // ++ [framework] acquireWakeLock happened before PowerManagerService init.
    // True if init() has done.
    private boolean mInitReady = false;
    // Table of all wake locks acquired before PowerManagerService init.
    private final ArrayList<WakeLock> mTmpWakeLockPool = new ArrayList<WakeLock>();
    private final Object mInitLock = new Object();
    // -- [framework] acquireWakeLock happened before PowerManagerService init.

    //++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
    private final SuspendBlocker mOOBESuspendBlocker;

    private boolean mHoldingOOBESuspendBlocker = false;

    private boolean mHasSetTimeout = false;

    private static final int OOBE_DEFAULT = -1;
    private static final int OOBE_UNFINISH = 0;
    private static final int OOBE_FINISH = 1;

    private static final int OOBE_PROP_UNFINISH = 0;
    private static final int OOBE_PROP_FINISH = 1;

    private static final String ACTION_OOBE_TIMEOUT = "com.htc.oobe.OOBE_TIMEOUT";

    private static final String SYS_PROP_OOBE_DONE     = "persist.sys.pms.oobe.done";

    private static final String ATT_OOBE_R2G_PACKAGE_NAME = "com.synchronoss.dcs.att.r2g";
    private static final String ATT_OOBE_R2G_HOME_ACTIVITY = "com.synchronoss.dcs.att.r2g.DCSMainActivity";

    private static final String HTC_OOBE_USER_OPERATED = "OOBE_USER_OPERATED";
    private static final String HTC_OOBE_PACKAGE_NAME = "com.htc.android.htcsetupwizard";
    private static final String HTC_OOBE_HOME_ACTIVITY = "com.htc.android.htcsetupwizard.MainActivity";

    private final int DEFAULT_OOBE_MONITOR_TIMEOUT = 60*60*1000; // 1 hr
    //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower

    public PowerManagerService() {
        synchronized (mLock) {
            mWakeLockSuspendBlocker = createSuspendBlockerLocked("PowerManagerService");
            //++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            mOOBESuspendBlocker = createSuspendBlockerLocked("PowerManagerService.OOBE");
            //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            mWakeLockSuspendBlocker.acquire();
            mScreenOnBlocker = new ScreenOnBlockerImpl();
            mDisplayBlanker = new DisplayBlankerImpl();
            mHoldingWakeLockSuspendBlocker = true;
            mWakefulness = WAKEFULNESS_AWAKE;
        }

        nativeInit();
        nativeSetPowerState(true, true);
    }

    /**
     * Initialize the power manager.
     * Must be called before any other functions within the power manager are called.
     */
    public void init(Context context, LightsService ls,
            ActivityManagerService am, BatteryService bs, IBatteryStats bss,
            DisplayManagerService dm) {
        mContext = context;
        mLightsService = ls;
        mBatteryService = bs;
        mBatteryStats = bss;
        mDisplayManagerService = dm;
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mHandler = new PowerManagerHandler(mHandlerThread.getLooper());

        Watchdog.getInstance().addMonitor(this);

        // Forcibly turn the screen on at boot so that it is in a known power state.
        // We do this in init() rather than in the constructor because setting the
        // screen state requires a call into surface flinger which then needs to call back
        // into the activity manager to check permissions.  Unfortunately the
        // activity manager is not running when the constructor is called, so we
        // have to defer setting the screen state until this point.
        mDisplayBlanker.unblankAllDisplays();

        // ++ [framework] init PMSInternalAPI.
        mPMSInternalAPI = new PMSInternalAPI();
        // -- [framework] init PMSInternalAPI.

        //+[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02
        // init HtcPowerSaver
        mHtcPowerSaver = new HtcPowerSaver(mContext, mPMSInternalAPI);
        mPowerSaverHandler = new Handler();
        //-[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02

        // ++ [framework] Add screen timeout mechanism when dongle mode is turned on
        mHtcDongleMode = new HtcDongleMode(mContext, mPMSInternalAPI);
        // -- [framework] Add screen timeout mechanism when dongle mode is turned on

        // ++ [framework] Collect wake lock record, dump record, and scan bad behavior wake lock.
        mHtcWakeLockMonitor = new HtcWakeLockMonitor(mContext, mPMSInternalAPI, Process.myUid());
        // -- [framework] Collect wake lock record, dump record, and scan bad behavior wake lock.

        //+[framework] Recude log, menghui_wu, 2012/02/03
        mHtcPMSExtension = new HtcPMSExtension(mContext, mLightsService);
        //-[framework] Recude log, menghui_wu, 2012/02/03

        // ++ [framework] Write screen state to file
        mHtcPMSExtension.writeScreenState(true);
        // -- [framework] Write screen state to file

        // ++ [framework] acquireWakeLock NPE happened before PowerManagerService init. Rhed_Jao, 20130603.
        // This will make IBinder which pass to acquireWakeLock was GC immediately sometimes, and it cannot add ref to kernel.
        // Device got hang later, when surface flinger try to binder transaction.
        synchronized (mTmpBinderPool) {
            mTmpBinderPool.clear();
        }
        // -- [framework] acquireWakeLock NPE happened before PowerManagerService init. Rhed_Jao, 20130603.

        // ++ [framework] acquireWakeLock happened before PowerManagerService init.
        synchronized (mInitLock) {
            mInitReady = true;
            handleWakeLockFromQueue();
        }
        // -- [framework] acquireWakeLock happened before PowerManagerService init.

    }

    public void setPolicy(WindowManagerPolicy policy) {
        synchronized (mLock) {
            mPolicy = policy;
        }
    }

    public void systemReady(TwilightService twilight, DreamManagerService dreamManager) {
        synchronized (mLock) {
            mSystemReady = true;
            mDreamManager = dreamManager;

            PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
            mScreenBrightnessSettingMinimum = pm.getMinimumScreenBrightnessSetting();
            mScreenBrightnessSettingMaximum = pm.getMaximumScreenBrightnessSetting();
            mScreenBrightnessSettingDefault = pm.getDefaultScreenBrightnessSetting();

            SensorManager sensorManager = new SystemSensorManager(mHandler.getLooper());

            // The notifier runs on the system server's main looper so as not to interfere
            // with the animations and other critical functions of the power manager.
            mNotifier = new Notifier(Looper.getMainLooper(), mContext, mBatteryStats,
                    createSuspendBlockerLocked("PowerManagerService.Broadcasts"),
                    // ++ [framework] Add HtcPMSExtension
                    mScreenOnBlocker, mPolicy, mHtcPMSExtension);
                    // -- [framework] Add HtcPMSExtension

            // The display power controller runs on the power manager service's
            // own handler thread to ensure timely operation.
            mDisplayPowerController = new DisplayPowerController(mHandler.getLooper(),
                    mContext, mNotifier, mLightsService, twilight, sensorManager,
                    mDisplayManagerService, mDisplayBlanker,
                    mDisplayPowerControllerCallbacks, mHandler,
                    // ++ [framework] Add PMSInternalAPI
                    mPMSInternalAPI);
                    // -- [framework] Add PMSInternalAPI

            //+ Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.
            // get current sense version and make sure this new behavior only applies to Sense 3.5 and later projects.
            float fSenseVersion = 1.0f;
            try {
                fSenseVersion = Float.parseFloat(HtcPMSExtension.getHtcCustomizationReader("Android_Core_Framework").readString("sense_version", "5.0"));
            } catch (Exception e) {
                if (DEBUG_ON) {
                    Slog.d(TAG, "systemReady: Exception caught. " + e);
                }
            }
            // In case the sense version of some future projects is valid, but didn't phase in Desk Dock AP.
            mbEnableDeskDockBehavior = (fSenseVersion >= SENSE_VERSION_FOR_DESKDOCK_DIM_BEHAVIOR);
            //- Greg Tsai (2011/06/21).

            mWirelessChargerDetector = new WirelessChargerDetector(sensorManager,
                    createSuspendBlockerLocked("PowerManagerService.WirelessChargerDetector"));
            mSettingsObserver = new SettingsObserver(mHandler);
            mAttentionLight = mLightsService.getLight(LightsService.LIGHT_ID_ATTENTION);

            // Register for broadcasts from other components of the system.
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
            mContext.registerReceiver(new BatteryReceiver(), filter, null, mHandler);

            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_BOOT_COMPLETED);
            //+[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02
            filter.addAction(Intents.ACTION_QUICKBOOT_POWERON);
            //-[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02
            mContext.registerReceiver(new BootCompletedReceiver(), filter, null, mHandler);

            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_DREAMING_STARTED);
            filter.addAction(Intent.ACTION_DREAMING_STOPPED);
            mContext.registerReceiver(new DreamReceiver(), filter, null, mHandler);

            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_USER_SWITCHED);
            mContext.registerReceiver(new UserSwitchedReceiver(), filter, null, mHandler);

            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_DOCK_EVENT);
            //+ Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.
            if (mbEnableDeskDockBehavior) {
                // listen new intent for desk dock mode in case user not enters desk dock mode by selecting AP directly.
                filter.addAction(UiModeManager.ACTION_EXIT_DESK_MODE);
                filter.addAction(UiModeManager.ACTION_ENTER_DESK_MODE);
            }
            //- Greg Tsai (2011/06/21).
            mContext.registerReceiver(new DockReceiver(), filter, null, mHandler);

            //++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            filter = new IntentFilter();
            filter.addAction(ACTION_OOBE_TIMEOUT);
            mContext.registerReceiver(new OOBETimeoutReceiver(), filter, null, mHandler);
            //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower

            // Register for settings changes.
            final ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.Secure.getUriFor(
                    Settings.Secure.SCREENSAVER_ENABLED),
                    false, mSettingsObserver, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.Secure.getUriFor(
                    Settings.Secure.SCREENSAVER_ACTIVATE_ON_SLEEP),
                    false, mSettingsObserver, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.Secure.getUriFor(
                    Settings.Secure.SCREENSAVER_ACTIVATE_ON_DOCK),
                    false, mSettingsObserver, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.SCREEN_OFF_TIMEOUT),
                    false, mSettingsObserver, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.Global.getUriFor(
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN),
                    false, mSettingsObserver, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.SCREEN_BRIGHTNESS),
                    false, mSettingsObserver, UserHandle.USER_ALL);
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.SCREEN_BRIGHTNESS_MODE),
                    false, mSettingsObserver, UserHandle.USER_ALL);
            //+ Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.
            resolver.registerContentObserver(Settings.System.getUriFor(
                    DESKDOCK_DIM_SCREEN),
                    false, mSettingsObserver, UserHandle.USER_ALL);
            //- Greg Tsai (2011/06/21).

            // Go.
            readConfigurationLocked();
            updateSettingsLocked();
            mDirty |= DIRTY_BATTERY_STATE;
            updatePowerStateLocked();
        }
    }

    private void readConfigurationLocked() {
        final Resources resources = mContext.getResources();

        mWakeUpWhenPluggedOrUnpluggedConfig = resources.getBoolean(
                com.android.internal.R.bool.config_unplugTurnsOnScreen);
        mDreamsSupportedConfig = resources.getBoolean(
                com.android.internal.R.bool.config_dreamsSupported);
        mDreamsEnabledByDefaultConfig = resources.getBoolean(
                com.android.internal.R.bool.config_dreamsEnabledByDefault);
        mDreamsActivatedOnSleepByDefaultConfig = resources.getBoolean(
                com.android.internal.R.bool.config_dreamsActivatedOnSleepByDefault);
        mDreamsActivatedOnDockByDefaultConfig = resources.getBoolean(
                com.android.internal.R.bool.config_dreamsActivatedOnDockByDefault);
    }

    private void updateSettingsLocked() {
        final ContentResolver resolver = mContext.getContentResolver();

        mDreamsEnabledSetting = (Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SCREENSAVER_ENABLED,
                mDreamsEnabledByDefaultConfig ? 1 : 0,
                UserHandle.USER_CURRENT) != 0);
        mDreamsActivateOnSleepSetting = (Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SCREENSAVER_ACTIVATE_ON_SLEEP,
                mDreamsActivatedOnSleepByDefaultConfig ? 1 : 0,
                UserHandle.USER_CURRENT) != 0);
        mDreamsActivateOnDockSetting = (Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SCREENSAVER_ACTIVATE_ON_DOCK,
                mDreamsActivatedOnDockByDefaultConfig ? 1 : 0,
                UserHandle.USER_CURRENT) != 0);
        mScreenOffTimeoutSetting = Settings.System.getIntForUser(resolver,
                Settings.System.SCREEN_OFF_TIMEOUT, DEFAULT_SCREEN_OFF_TIMEOUT,
                UserHandle.USER_CURRENT);
        mStayOnWhilePluggedInSetting = Settings.Global.getInt(resolver,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN, BatteryManager.BATTERY_PLUGGED_AC);

        final int oldScreenBrightnessSetting = mScreenBrightnessSetting;
        mScreenBrightnessSetting = Settings.System.getIntForUser(resolver,
                Settings.System.SCREEN_BRIGHTNESS, mScreenBrightnessSettingDefault,
                UserHandle.USER_CURRENT);
        if (oldScreenBrightnessSetting != mScreenBrightnessSetting) {
            mTemporaryScreenBrightnessSettingOverride = -1;
        }

        final float oldScreenAutoBrightnessAdjustmentSetting =
                mScreenAutoBrightnessAdjustmentSetting;
        mScreenAutoBrightnessAdjustmentSetting = Settings.System.getFloatForUser(resolver,
                Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ, 0.0f,
                UserHandle.USER_CURRENT);
        if (oldScreenAutoBrightnessAdjustmentSetting != mScreenAutoBrightnessAdjustmentSetting) {
            mTemporaryScreenAutoBrightnessAdjustmentSettingOverride = Float.NaN;
        }

        mScreenBrightnessModeSetting = Settings.System.getIntForUser(resolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL, UserHandle.USER_CURRENT);

        // ++ [framework] JB MR1 4.2 porting from JB 4.1.1
        mScreenOffDelayForDeskDock = Settings.System.getIntForUser(resolver,
                DESKDOCK_DIM_SCREEN,
                DEFAULT_DESKDOCK_DIM_SCREEN_VALUE, UserHandle.USER_CURRENT);
        if (mScreenOffDelayForDeskDock != -1) {
            mScreenOffDelayForDeskDock = mScreenOffDelayForDeskDock * DESKDOCK_MILLISECOND_PER_UNIT;
        }
        // -- [framework] JB MR1 4.2 porting from JB 4.1.1

        if (DEBUG_ON) {
            Slog.d(TAG, "SettingsObserver:"
                    + " mDreamsEnabledSetting=" + mDreamsEnabledSetting
                    + ", mDreamsActivateOnSleepSetting=" + mDreamsActivateOnSleepSetting
                    + ", mDreamsActivateOnDockSetting=" + mDreamsActivateOnDockSetting
                    + ", mScreenOffTimeoutSetting=" + mScreenOffTimeoutSetting
                    + ", mStayOnWhilePluggedInSetting=" + mStayOnWhilePluggedInSetting
                    + ", mScreenBrightnessSetting=" + oldScreenBrightnessSetting + "->" + mScreenBrightnessSetting
                    + ", mScreenAutoBrightnessAdjustmentSetting=" + oldScreenAutoBrightnessAdjustmentSetting + "->" + mScreenAutoBrightnessAdjustmentSetting
                    + ", mScreenBrightnessModeSetting=" + mScreenBrightnessModeSetting
                    + ", mScreenOffDelayForDeskDock=" + mScreenOffDelayForDeskDock);
        }

        mDirty |= DIRTY_SETTINGS;
    }

    private void handleSettingsChangedLocked() {
        updateSettingsLocked();
        updatePowerStateLocked();
    }

    // ++ [framework] acquireWakeLock happened before PowerManagerService init.
    private int findWakeLockIndexLockedFromQueue(IBinder lock) {
        final int count = mTmpWakeLockPool.size();
        if (DEBUG_ON) {
            Slog.d(TAG, "findWakeLockIndexLockedFromQueue count=" + count);
        }
        for (int i = 0; i < count; i++) {
            if (mTmpWakeLockPool.get(i).mLock == lock) {
                return i;
            }
        }
        return -1;
    }

    private void addWakeLockToQueue(IBinder lock, int flags, String tag, WorkSource ws, int uid, int pid) {
        WakeLock wakeLock;
        int index = findWakeLockIndexLockedFromQueue(lock);
        if (DEBUG_ON) {
            Slog.d(TAG, "addWakeLockToQueue index=" + index);
        }
        if (index >= 0) {
            wakeLock = mTmpWakeLockPool.get(index);
            if (!wakeLock.hasSameProperties(flags, tag, ws, uid, pid)) {
                wakeLock.updateProperties(flags, tag, ws, uid, pid);
            }
        } else {
            wakeLock = new WakeLock(lock, flags, tag, ws, uid, pid);
            mTmpWakeLockPool.add(wakeLock);
        }
    }

    private void removeWakeLockFromQueue(IBinder lock, int flags) {
        int index = findWakeLockIndexLockedFromQueue(lock);
        if (DEBUG_ON) {
            Slog.d(TAG, "removeWakeLockFromQueue index=" + index);
        }
        if (index < 0) {
            return;
        }
        mTmpWakeLockPool.remove(index);
    }

    private void handleWakeLockFromQueue() {
        final int count = mTmpWakeLockPool.size();
        if (DEBUG_ON) {
            Slog.d(TAG, "handleWakeLockFromQueue count=" + count);
        }
        if (count <= 0) {
            return;
        }
        WakeLock wakeLock;
        for (int i = 0; i < count; i++) {
            wakeLock = mTmpWakeLockPool.get(i);
            final long ident = Binder.clearCallingIdentity();
            try {
                acquireWakeLockInternal(wakeLock.mLock, wakeLock.mFlags, wakeLock.mTag, wakeLock.mWorkSource, wakeLock.mOwnerUid, wakeLock.mOwnerPid);
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }
        mTmpWakeLockPool.clear();
        if (DEBUG_ON) {
            Slog.d(TAG, "handleWakeLockFromQueue final size=" + mTmpWakeLockPool.size());
        }
    }
    // -- [framework] acquireWakeLock happened before PowerManagerService init.

    // ++ [framework] acquireWakeLock NPE happened before PowerManagerService init. Rhed_Jao, 20130603.
    // This will make IBinder which pass to acquireWakeLock was GC immediately sometimes, and it cannot add ref to kernel.
    // Device got hang later, when surface flinger try to binder transaction.
    private final HashSet<IBinder> mTmpBinderPool = new HashSet<IBinder>();
    // -- [framework] acquireWakeLock NPE happened before PowerManagerService init. Rhed_Jao, 20130603.

    @Override // Binder call
    public void acquireWakeLock(IBinder lock, int flags, String tag, WorkSource ws) {
        if (lock == null) {
            throw new IllegalArgumentException("lock must not be null");
        }
        PowerManager.validateWakeLockParameters(flags, tag);

        // ++ [framework] acquireWakeLock NPE happened before PowerManagerService init. Rhed_Jao, 20130603.
        // This will make IBinder which pass to acquireWakeLock was GC immediately sometimes, and it cannot add ref to kernel.
        // Device got hang later, when surface flinger try to binder transaction.
        if (null == mContext) {
            synchronized (mTmpBinderPool) {
                if (null == mContext) {
                    mTmpBinderPool.add(lock);
                }
            }
        }
        // -- [framework] acquireWakeLock NPE happened before PowerManagerService init. Rhed_Jao, 20130603.

        // ++ [framework] acquireWakeLock happened before PowerManagerService init.
        synchronized (mInitLock) {
            if (mInitReady == false) {
                addWakeLockToQueue(lock, flags, tag, ws, Binder.getCallingUid(), Binder.getCallingPid());
                return;
            }
        }
        // -- [framework] acquireWakeLock happened before PowerManagerService init.

        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.WAKE_LOCK, null);
        if (ws != null && ws.size() != 0) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.UPDATE_DEVICE_STATS, null);
        } else {
            ws = null;
        }

        // ++ [framework] Disable GC when the wake lock type is performance related.
        if (mHtcPMSExtension != null && ((flags & PowerManager.CPU_PERF_WAKE_LOCK) != 0)) {
            mHtcPMSExtension.disableGC(true);
        }
        // -- [framework] Disable GC when the wake lock type is performance related.

        final int uid = Binder.getCallingUid();
        final int pid = Binder.getCallingPid();
        final long ident = Binder.clearCallingIdentity();
        try {
            acquireWakeLockInternal(lock, flags, tag, ws, uid, pid);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }

        // ++ [framework] Enable GC when the wake lock type is performance related.
        if (mHtcPMSExtension != null && ((flags & PowerManager.CPU_PERF_WAKE_LOCK) != 0)) {
            mHtcPMSExtension.disableGC(false);
        }
        // -- [framework] Enable GC when the wake lock type is performance related
    }

    private void acquireWakeLockInternal(IBinder lock, int flags, String tag, WorkSource ws,
            int uid, int pid) {
        synchronized (mLock) {
            if (DEBUG_SPEW) {
                Slog.d(TAG, "acquireWakeLockInternal: lock=" + Objects.hashCode(lock)
                        + ", flags=0x" + Integer.toHexString(flags)
                        + ", tag=\"" + tag + "\", ws=" + ws + ", uid=" + uid + ", pid=" + pid);
            }

            WakeLock wakeLock;
            int index = findWakeLockIndexLocked(lock);
            if (index >= 0) {
                wakeLock = mWakeLocks.get(index);
                if (!wakeLock.hasSameProperties(flags, tag, ws, uid, pid)) {
                    // Update existing wake lock.  This shouldn't happen but is harmless.
                    notifyWakeLockReleasedLocked(wakeLock);
                    wakeLock.updateProperties(flags, tag, ws, uid, pid);
                    notifyWakeLockAcquiredLocked(wakeLock);
                }
            } else {
                wakeLock = new WakeLock(lock, flags, tag, ws, uid, pid);
                try {
                    lock.linkToDeath(wakeLock, 0);
                } catch (RemoteException ex) {
                    throw new IllegalArgumentException("Wake lock is already dead.");
                }
                notifyWakeLockAcquiredLocked(wakeLock);
                mWakeLocks.add(wakeLock);
            }

            applyWakeLockFlagsOnAcquireLocked(wakeLock);

            // ++ [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl
            if ((flags & PowerManager.WAKE_LOCK_LEVEL_MASK) == PowerManager.CPU_PERF_WAKE_LOCK) {
                mCpuPerfCount++;
                // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
//                if (mCpuPerfCount == 1)
//                    nativeAcquireCpuPerfWakeLock();
                updateHtcCpuCtrlLevel(PowerManager.CPU_PERF_WAKE_LOCK);
                // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
            }
            // -- [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl

            // ++ [framework] Monitor wake lock behaviors
            if (mHtcWakeLockMonitor != null) {
                mHtcWakeLockMonitor.addRecord(wakeLock.mFlags, wakeLock.mTag, wakeLock.mOwnerUid, wakeLock.mOwnerPid, wakeLock.mLock);
            }
            // -- [framework] Monitor wake lock behaviors

            // ++ [framework] Add WakeLock related logs to determine issue root cause more easily.
            if (DEBUG_ON || true) {
                // ++ [framework] Encrypt tag if neccessary in release rom.
                String new_tag = tag;
                if (!HtcBuildFlag.Htc_SECURITY_DEBUG_flag) {
                    if (new_tag.contains("@")) {
                        new_tag = "encrypted_tag"; // To prevent leak user account into debug logs.
                    }
                }
                // -- [framework] Encrypt tag if neccessary in release rom.

                String typeInfo = HtcPMSExtension.getWakeLockTypeInfo(flags);
                String flagInfo = wakeLock.getLockFlagsString();
                Formatter f;
                if (!wakeLock.hasSameProperties(flags, tag, ws, uid, pid)) {
                    f = new Formatter();
                    f.format("releaseWakeLock(%x): %s %s, tag=%s, flags=0x%x", 
                            wakeLock.hashCode(), typeInfo, flagInfo, new_tag, flags);
                    Slog.d(TAG, f.toString());
                }
                f = new Formatter();
                f.format("acquireWakeLock(%x): %s %s, tag=%s, flags=0x%x, pid=%d, uid=%d",
                        wakeLock.hashCode(), typeInfo, flagInfo, new_tag, flags, pid, uid);
                Slog.d(TAG, f.toString());
            }
            // -- [framework] Add WakeLock related logs to determine issue root cause more easily.

            mDirty |= DIRTY_WAKE_LOCKS;
            updatePowerStateLocked();
        }
    }

    // ++ [framework] For CPU performance controlling mechanism.
    @Override
    public void acquireHtcCpuCtrl(int flags, IBinder lock, String tag, WorkSource ws, int level) {
        if (lock == null) {
            throw new IllegalArgumentException("lock must not be null");
        }
        PowerManager.validateHtcCpuCtrlParameters(flags, tag);

        if (ws != null && ws.size() != 0) {
            // Do nothing.
        } else {
            ws = null;
        }

        // ++ [framework] Disable GC when the wake lock type is performance related.
        if (mHtcPMSExtension != null) {
            mHtcPMSExtension.disableGC(true);
        }
        // -- [framework] Disable GC when the wake lock type is performance related.

        final int uid = Binder.getCallingUid();
        final int pid = Binder.getCallingPid();
        final long ident = Binder.clearCallingIdentity();
        try {
            acquireHtcCpuCtrlInternal(lock, flags, tag, ws, uid, pid, level);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }

        // ++ [framework] Enable GC when the wake lock type is performance related.
        if (mHtcPMSExtension != null) {
            mHtcPMSExtension.disableGC(false);
        }
        // -- [framework] Enable GC when the wake lock type is performance related
    }

    private void acquireHtcCpuCtrlInternal(IBinder lock, int flags, String tag, WorkSource ws,
            int uid, int pid, int level) {
        synchronized (mLock) {
            HtcCpuCtrl htcCpuCtrl;
            int index = findHtcCpuCtrlIndexLocked(lock);
            if (index >= 0) {
                htcCpuCtrl = mHtcCpuCtrls.get(index);
                if (!htcCpuCtrl.hasSameProperties(flags, tag, ws, uid, pid)) {
                    // Update existing wake lock.  This shouldn't happen but is harmless.
                    notifyHtcCpuCtrlReleasedLocked(htcCpuCtrl);
                    htcCpuCtrl.updateProperties(flags, tag, ws, uid, pid);
                    notifyHtcCpuCtrlAcquiredLocked(htcCpuCtrl);
                }
            } else {
                htcCpuCtrl = new HtcCpuCtrl(lock, flags, tag, ws, uid, pid, level);
                try {
                    lock.linkToDeath(htcCpuCtrl, 0);
                } catch (RemoteException ex) {
                    throw new IllegalArgumentException("HtcCpuCtrl is already dead.");
                }
                notifyHtcCpuCtrlAcquiredLocked(htcCpuCtrl);
                mHtcCpuCtrls.add(htcCpuCtrl);
            }

            // ++ [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl
            if ((flags & PowerManager.WAKE_LOCK_LEVEL_MASK) == PowerManager.CPU_PERF_WAKE_LOCK) {
                mCpuPerfCount++;
                // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
//                if (mCpuPerfCount == 1)
//                    nativeAcquireCpuPerfWakeLock();
                updateHtcCpuCtrlLevel(PowerManager.CPU_PERF_WAKE_LOCK);
                // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
            } else if ((flags & CPU_LOCK_MASK) == PowerManager.CPU_MAX_FREQ) {
                mCpuFreqMaxCount++;
                updateHtcCpuCtrlLevel(PowerManager.CPU_MAX_FREQ);
            } else if ((flags & CPU_LOCK_MASK) == PowerManager.CPU_MIN_FREQ) {
                mCpuFreqMinCount++;
                updateHtcCpuCtrlLevel(PowerManager.CPU_MIN_FREQ);
            } else if ((flags & CPU_LOCK_MASK) == PowerManager.CPU_MAX_NUM) {
                mCpuNumMaxCount++;
                updateHtcCpuCtrlLevel(PowerManager.CPU_MAX_NUM);
            } else if ((flags & CPU_LOCK_MASK) == PowerManager.CPU_MIN_NUM) {
                mCpuNumMinCount++;
                updateHtcCpuCtrlLevel(PowerManager.CPU_MIN_NUM);
            }
            // -- [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl

            // ++ [framework] Monitor wake lock behaviors
            if (mHtcWakeLockMonitor != null) {
                mHtcWakeLockMonitor.addRecord(htcCpuCtrl.mFlags, htcCpuCtrl.mTag, htcCpuCtrl.mOwnerUid, htcCpuCtrl.mOwnerPid, htcCpuCtrl.mLock);
            }
            // -- [framework] Monitor wake lock behaviors

            // ++ [framework] Add WakeLock related logs to determine issue root cause more easily.
            if (DEBUG_ON || true) {
                // ++ [framework] Encrypt tag if neccessary in release rom.
                String new_tag = tag;
                if (!HtcBuildFlag.Htc_SECURITY_DEBUG_flag) {
                    if (new_tag.contains("@")) {
                        new_tag = "encrypted_tag"; // To prevent leak user account into debug logs.
                    }
                }
                // -- [framework] Encrypt tag if neccessary in release rom.

                String typeInfo = HtcPMSExtension.getWakeLockTypeInfo(flags);
                Formatter f;
                if (!htcCpuCtrl.hasSameProperties(flags, tag, ws, uid, pid)) {
                    f = new Formatter();
                    f.format("releaseHtcCpuCtrl(%x): %s, tag=%s, flags=0x%x", 
                            htcCpuCtrl.hashCode(), typeInfo, new_tag, flags);
                    Slog.d(TAG, f.toString());
                }
                f = new Formatter();
                f.format("acquireHtcCpuCtrl(%x): %s, tag=%s, flags=0x%x, pid=%d, uid=%d",
                        htcCpuCtrl.hashCode(), typeInfo, new_tag, flags, pid, uid);
                Slog.d(TAG, f.toString());
            }
            // -- [framework] Add WakeLock related logs to determine issue root cause more easily.

            mDirty |= DIRTY_WAKE_LOCKS;
            updatePowerStateLocked();
        }
    }
    // -- [framework] For CPU performance controlling mechanism.

    private static boolean isScreenLock(final WakeLock wakeLock) {
        switch (wakeLock.mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) {
            case PowerManager.FULL_WAKE_LOCK:
            case PowerManager.SCREEN_BRIGHT_WAKE_LOCK:
            case PowerManager.SCREEN_DIM_WAKE_LOCK:
                return true;
        }
        return false;
    }

    private void applyWakeLockFlagsOnAcquireLocked(WakeLock wakeLock) {
        if ((wakeLock.mFlags & PowerManager.ACQUIRE_CAUSES_WAKEUP) != 0 &&
                isScreenLock(wakeLock)) {
            // ++ [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
            if ((mProximityPositive || mADTalkingDetected) && wakeLock.mOwnerUid == Process.SYSTEM_UID && mDisplayPowerRequest != null && "keyguard".equals(wakeLock.mTag)) {
                if (DEBUG_ON) {
                    Slog.d(TAG, "Wake up requested from keyguard while p-sensor NEAR or AD talking detected.");
                }
                mDisplayPowerRequest.forceScreenOn = true;
            }
            // -- [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
            wakeUpNoUpdateLocked(SystemClock.uptimeMillis());
        }
    }

    // ++ [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl
    private void updateHtcCpuCtrlLevel(int flags) {
        int level = 0;
        int freq_min_level = 0;
        int freq_max_level = 0;
        int num_min_level = 0;
        int num_max_level = 0;

        // Search the lock list to find final level.
        final int M = mWakeLocks.size();
        for (int i = 0 ; i < M ; i++) {
            WakeLock wl = mWakeLocks.get(i);
            // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
            if ((wl.mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) == PowerManager.CPU_PERF_WAKE_LOCK) {
                if (PowerManager.CPU_FREQ_HIGHEST > freq_min_level) {
                    freq_min_level = PowerManager.CPU_FREQ_HIGHEST;
                }
                if (PowerManager.CPU_NUM_DUAL > num_min_level) {
                    num_min_level = PowerManager.CPU_NUM_DUAL;
                }
            }
            // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
        }

        final int N = mHtcCpuCtrls.size();
        for (int i = 0 ; i < N ; i++) {
            HtcCpuCtrl hcc = mHtcCpuCtrls.get(i);
            if ((hcc.mFlags & CPU_LOCK_MASK) == 0) {
                // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
                if ((hcc.mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) == PowerManager.CPU_PERF_WAKE_LOCK) {
                    if (PowerManager.CPU_FREQ_HIGHEST > freq_min_level) {
                        freq_min_level = PowerManager.CPU_FREQ_HIGHEST;
                    }
                    if (PowerManager.CPU_NUM_DUAL > num_min_level) {
                        num_min_level = PowerManager.CPU_NUM_DUAL;
                    }
                }
                // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
                continue;
            }
            try {
                level = (int) hcc.getLevel();
            } catch (Exception e) {
                Slog.d(TAG, "updateHtcCpuCtrlLevel: Skip. hcc.mFlags=" + hcc.mFlags + ", hcc.mTag=" + hcc.mTag, e);
                continue;
            }
            switch (hcc.mFlags & CPU_LOCK_MASK) {
                case PowerManager.CPU_MAX_FREQ:
                    if (freq_max_level == 0 || level < freq_max_level)
                        freq_max_level = level;
                    break;
                case PowerManager.CPU_MIN_FREQ:
                    if (freq_min_level == 0 || level > freq_min_level)
                        freq_min_level = level;
                    break;
                case PowerManager.CPU_MAX_NUM:
                    if (num_max_level == 0 || level < num_max_level)
                        num_max_level = level;
                    break;
                case PowerManager.CPU_MIN_NUM:
                    if (num_min_level == 0 || level > num_min_level)
                        num_min_level = level;
                    break;
            }
        }
        // Final update: make sure min/max will not conflict.
        switch (flags & PowerManager.WAKE_LOCK_LEVEL_MASK) {
            // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
            case PowerManager.CPU_PERF_WAKE_LOCK:
                if (freq_min_level != 0) {
                    if (freq_max_level != 0 && freq_max_level < freq_min_level) {
                        Slog.w(TAG, "Found cpu freq lock conflict in CPU_PERF:"
                                + " max=" + freq_max_level
                                + ", min=" + freq_min_level);
                        freq_min_level = freq_max_level;
                    }
                    nativeAcquireCpuFreqMinWakeLock(freq_min_level);
                }
                if (num_min_level != 0) {
                    if (num_max_level != 0 && num_max_level < num_min_level) {
                        Slog.w(TAG, "Found cpu number lock conflict in CPU_PERF:"
                                + " max=" + num_max_level
                                + ", min=" + num_min_level);
                        num_min_level = num_max_level;
                    }
                    nativeAcquireCpuNumMinWakeLock(num_min_level);
                }
                break;
            // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
            case PowerManager.CPU_MIN_FREQ:
                if (freq_min_level == 0) {
                    Slog.d(TAG, "There is no CPU_MIN_FREQ to update");
                    return;
                }
                if (freq_max_level != 0 && freq_max_level < freq_min_level) {
                    Slog.w(TAG, "Found cpu freq lock conflict in CPU_MIN_FREQ:"
                            + " max=" + freq_max_level
                            + ", min=" + freq_min_level);
                    freq_min_level = freq_max_level;
                }
                nativeAcquireCpuFreqMinWakeLock(freq_min_level);
                break;
            case PowerManager.CPU_MAX_FREQ:
                if (freq_max_level == 0) {
                    if (freq_min_level != 0) {
                        // If we meet conflict before, we need to set back
                        // cpufreq_min when cpufreq_max released.
                        nativeAcquireCpuFreqMinWakeLock(freq_min_level);
                    } else {
                        Slog.d(TAG, "There is no CPU_MAX_FREQ to update");
                    }
                    return;
                }
                if (freq_min_level != 0 && freq_max_level < freq_min_level) {
                    Slog.w(TAG, "Found cpu freq lock conflict in CPU_MAX_FREQ:"
                            + " max=" + freq_max_level
                            + ", min=" + freq_min_level);
                    freq_min_level = freq_max_level;
                    nativeAcquireCpuFreqMinWakeLock(freq_min_level);
                }
                nativeAcquireCpuFreqMaxWakeLock(freq_max_level);
                break;
            case PowerManager.CPU_MIN_NUM:
                if (num_min_level == 0) {
                    Slog.d(TAG, "There is no CPU_MIN_NUM to update");
                    return;
                }
                if (num_max_level != 0 && num_max_level < num_min_level) {
                    Slog.w(TAG, "Found cpu number lock conflict in CPU_MIN_NUM:"
                            + " max=" + num_max_level
                            + ", min=" + num_min_level);
                    num_min_level = num_max_level;
                }
                nativeAcquireCpuNumMinWakeLock(num_min_level);
                break;
            case PowerManager.CPU_MAX_NUM:
                if (num_max_level == 0) {
                    if (num_min_level != 0) {
                        /* If we meet conflict before, we need to set back
                         * cpunum_min when cpunum_max released. */
                        nativeAcquireCpuNumMinWakeLock(num_min_level);
                    } else {
                        Slog.d(TAG, "There is no CPU_MAX_NUM to update");
                    }
                    return;
                }
                if (num_min_level != 0 && num_max_level < num_min_level) {
                    Slog.w(TAG, "Found cpu number lock conflict in CPU_MAX_NUM:"
                            + " max=" + num_max_level
                            + ", min=" + num_min_level);
                    num_min_level = num_max_level;
                    nativeAcquireCpuNumMinWakeLock(num_min_level);
                }
                nativeAcquireCpuNumMaxWakeLock(num_max_level);
                break;
        }
    }
    // -- [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl

    @Override // Binder call
    public void releaseWakeLock(IBinder lock, int flags) {
        if (lock == null) {
            throw new IllegalArgumentException("lock must not be null");
        }

        // ++ [framework] acquireWakeLock happened before PowerManagerService init.
        synchronized (mInitLock) {
            if (mInitReady == false) {
                removeWakeLockFromQueue(lock, flags);
                return;
            }
        }
        // -- [framework] acquireWakeLock happened before PowerManagerService init.

        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.WAKE_LOCK, null);

        final long ident = Binder.clearCallingIdentity();
        try {
            releaseWakeLockInternal(lock, flags);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void releaseWakeLockInternal(IBinder lock, int flags) {
        synchronized (mLock) {
            if (DEBUG_SPEW) {
                Slog.d(TAG, "releaseWakeLockInternal: lock=" + Objects.hashCode(lock)
                        + ", flags=0x" + Integer.toHexString(flags));
            }

            int index = findWakeLockIndexLocked(lock);
            if (index < 0) {
                // ++ [framework] Monitor wake lock behaviors
                // No such wakelock, stop dead record here. GB30 Menghui Wu
                if (mHtcWakeLockMonitor != null) {
                    mHtcWakeLockMonitor.stopDeadRecord(lock);
                }
                // -- [framework] Monitor wake lock behaviors
                return;
            }

            WakeLock wakeLock = mWakeLocks.get(index);
            mWakeLocks.remove(index);

            // ++ [framework] Add WakeLock related logs to determine issue root cause more easily.
            if (DEBUG_ON || true) { 
                // ++ [framework] Encrypt tag if neccessary in release rom.
                String new_tag = wakeLock.mTag;
                if (!HtcBuildFlag.Htc_SECURITY_DEBUG_flag) {
                    if (new_tag.contains("@")) {
                        new_tag = "encrypted_tag"; // To prevent leak user account into debug logs.
                    }
                }
                // -- [framework] Encrypt tag if neccessary in release rom.

                String typeInfo = HtcPMSExtension.getWakeLockTypeInfo(wakeLock.mFlags);
                String flagInfo = wakeLock.getLockFlagsString();
                Formatter f = new Formatter();
                f.format("releaseWakeLock(%x): %s %s, tag=%s, flags=0x%x", wakeLock.hashCode(), typeInfo, flagInfo, new_tag, wakeLock.mFlags);
                Slog.d(TAG, f.toString());
            }
            // -- [framework] Add WakeLock related logs to determine issue root cause more easily.

            notifyWakeLockReleasedLocked(wakeLock);
            wakeLock.mLock.unlinkToDeath(wakeLock, 0);

            if ((flags & PowerManager.WAIT_FOR_PROXIMITY_NEGATIVE) != 0) {
                mRequestWaitForNegativeProximity = true;
            }

            // ++ [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
            if ((wakeLock.mFlags & PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK) != 0) {
                if (DEBUG_ON) {
                    Slog.d(TAG, "releaseWakeLock: disable force screen on.");
                }
                mADTalkingDetected = false;

                if (mDisplayPowerRequest != null) {
                    mDisplayPowerRequest.forceScreenOn = false;
                }
            }
            // -- [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.

            applyWakeLockFlagsOnReleaseLocked(wakeLock);

            // ++ [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl
            if ((wakeLock.mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) == PowerManager.CPU_PERF_WAKE_LOCK) {
                mCpuPerfCount--;
                // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
//                if (mCpuPerfCount == 0) {
//                    nativeReleaseCpuPerfWakeLock();
//                }
                if (mCpuPerfCount == 0) {
                    if (mCpuFreqMinCount == 0) {
                        nativeReleaseCpuFreqMinWakeLock();
                    }
                    if (mCpuNumMinCount == 0) {
                        nativeReleaseCpuNumMinWakeLock();
                    }
                    if (mCpuFreqMinCount != 0 || mCpuNumMinCount != 0) {
                        updateHtcCpuCtrlLevel(PowerManager.CPU_PERF_WAKE_LOCK);
                    }
                } else {
                    updateHtcCpuCtrlLevel(PowerManager.CPU_PERF_WAKE_LOCK);
                }
                // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
            }
            // -- [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl

            // ++ [framework] Monitor wake lock behaviors
            if (mHtcWakeLockMonitor != null) {
                mHtcWakeLockMonitor.stopRecord(wakeLock.mTag, wakeLock.mLock);
            }
            // -- [framework] Monitor wake lock behaviors

            mDirty |= DIRTY_WAKE_LOCKS;
            updatePowerStateLocked();
        }
    }

    // ++ [framework] For CPU performance controlling mechanism.
    @Override
    public void releaseHtcCpuCtrl(IBinder lock, int flags) {
        if (lock == null) {
            throw new IllegalArgumentException("lock must not be null");
        }

        final long ident = Binder.clearCallingIdentity();
        try {
            releaseHtcCpuCtrlInternal(lock, flags);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void releaseHtcCpuCtrlInternal(IBinder lock, int flags) {
        synchronized (mLock) {
            int index = findHtcCpuCtrlIndexLocked(lock);
            if (index < 0) {
                // ++ [framework] Monitor wake lock behaviors
                // No such wakelock, stop dead record here. GB30 Menghui Wu
                if (mHtcWakeLockMonitor != null) {
                    mHtcWakeLockMonitor.stopDeadRecord(lock);
                }
                // -- [framework] Monitor wake lock behaviors
                return;
            }

            HtcCpuCtrl htcCpuCtrl = mHtcCpuCtrls.get(index);
            mHtcCpuCtrls.remove(index);

            // ++ [framework] Add WakeLock related logs to determine issue root cause more easily.
            if (DEBUG_ON || true) { 
                // ++ [framework] Encrypt tag if neccessary in release rom.
                String new_tag = htcCpuCtrl.mTag;
                if (!HtcBuildFlag.Htc_SECURITY_DEBUG_flag) {
                    if (new_tag.contains("@")) {
                        new_tag = "encrypted_tag"; // To prevent leak user account into debug logs.
                    }
                }
                // -- [framework] Encrypt tag if neccessary in release rom.

                String typeInfo = HtcPMSExtension.getWakeLockTypeInfo(htcCpuCtrl.mFlags);
                Formatter f = new Formatter();
                f.format("releaseHtcCpuCtrl(%x): %s, tag=%s, flags=0x%x", htcCpuCtrl.hashCode(), typeInfo, new_tag, htcCpuCtrl.mFlags);
                Slog.d(TAG, f.toString());
            }
            // -- [framework] Add WakeLock related logs to determine issue root cause more easily.

            notifyHtcCpuCtrlReleasedLocked(htcCpuCtrl);
            htcCpuCtrl.mLock.unlinkToDeath(htcCpuCtrl, 0);

            // ++ [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl
            if ((htcCpuCtrl.mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) == PowerManager.CPU_PERF_WAKE_LOCK) {
                mCpuPerfCount--;
                // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
//                if (mCpuPerfCount == 0) {
//                    nativeReleaseCpuPerfWakeLock();
//                }
                if (mCpuPerfCount == 0) {
                    if (mCpuFreqMinCount == 0) {
                        nativeReleaseCpuFreqMinWakeLock();
                    }
                    if (mCpuNumMinCount == 0) {
                        nativeReleaseCpuNumMinWakeLock();
                    }
                    if (mCpuFreqMinCount != 0 || mCpuNumMinCount != 0) {
                        updateHtcCpuCtrlLevel(PowerManager.CPU_PERF_WAKE_LOCK);
                    }
                } else {
                    updateHtcCpuCtrlLevel(PowerManager.CPU_PERF_WAKE_LOCK);
                }
                // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
            } else if ((htcCpuCtrl.mFlags & CPU_LOCK_MASK) == PowerManager.CPU_MAX_FREQ) {
                mCpuFreqMaxCount--;
                if (mCpuFreqMaxCount == 0) {
                    updateHtcCpuCtrlLevel(PowerManager.CPU_MAX_FREQ);
                    nativeReleaseCpuFreqMaxWakeLock();
                } else {
                    updateHtcCpuCtrlLevel(PowerManager.CPU_MAX_FREQ);
                }
            } else if ((htcCpuCtrl.mFlags & CPU_LOCK_MASK) == PowerManager.CPU_MIN_FREQ) {
                mCpuFreqMinCount--;
                // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
                if (mCpuFreqMinCount == 0 && mCpuPerfCount == 0) {
                // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
                    nativeReleaseCpuFreqMinWakeLock();
                } else {
                    updateHtcCpuCtrlLevel(PowerManager.CPU_MIN_FREQ);
                }
            } else if ((htcCpuCtrl.mFlags & CPU_LOCK_MASK) == PowerManager.CPU_MAX_NUM) {
                mCpuNumMaxCount--;
                if (mCpuNumMaxCount == 0) {
                    updateHtcCpuCtrlLevel(PowerManager.CPU_MAX_NUM);
                    nativeReleaseCpuNumMaxWakeLock();
                } else {
                    updateHtcCpuCtrlLevel(PowerManager.CPU_MAX_NUM);
                }
            } else if ((htcCpuCtrl.mFlags & CPU_LOCK_MASK) == PowerManager.CPU_MIN_NUM) {
                mCpuNumMinCount--;
                // ++ [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
                if (mCpuNumMinCount == 0 && mCpuPerfCount == 0) {
                // -- [framework] Rework CPU_PERF_WAKE_LOCK to use new kernel API
                    nativeReleaseCpuNumMinWakeLock();
                } else {
                    updateHtcCpuCtrlLevel(PowerManager.CPU_MIN_NUM);
                }
            }
            // -- [framework] JB MR1 4.2 porting from JB 4.1.1, for HtcCpuCtrl

            // ++ [framework] Monitor wake lock behaviors
            if (mHtcWakeLockMonitor != null) {
                mHtcWakeLockMonitor.stopRecord(htcCpuCtrl.mTag, htcCpuCtrl.mLock);
            }
            // -- [framework] Monitor wake lock behaviors

            mDirty |= DIRTY_WAKE_LOCKS;
            updatePowerStateLocked();
        }
    }
    // -- [framework] For CPU performance controlling mechanism.

    private void handleWakeLockDeath(WakeLock wakeLock) {
        synchronized (mLock) {
            if (DEBUG_SPEW) {
                Slog.d(TAG, "handleWakeLockDeath: lock=" + Objects.hashCode(wakeLock.mLock));
            }

            int index = mWakeLocks.indexOf(wakeLock);
            if (index < 0) {
                return;
            }

            mWakeLocks.remove(index);
            notifyWakeLockReleasedLocked(wakeLock);

            applyWakeLockFlagsOnReleaseLocked(wakeLock);
            mDirty |= DIRTY_WAKE_LOCKS;
            updatePowerStateLocked();
        }
    }

    // ++ [framework] For CPU performance controlling mechanism.
    private void handleHtcCpuCtrlDeath(HtcCpuCtrl htcCpuCtrl) {
        synchronized (mLock) {
            int index = mHtcCpuCtrls.indexOf(htcCpuCtrl);
            if (index < 0) {
                return;
            }

            mHtcCpuCtrls.remove(index);
            notifyHtcCpuCtrlReleasedLocked(htcCpuCtrl);

            mDirty |= DIRTY_WAKE_LOCKS;
            updatePowerStateLocked();
        }
    }
    // -- [framework] For CPU performance controlling mechanism.

    private void applyWakeLockFlagsOnReleaseLocked(WakeLock wakeLock) {
        if ((wakeLock.mFlags & PowerManager.ON_AFTER_RELEASE) != 0) {
            userActivityNoUpdateLocked(SystemClock.uptimeMillis(),
                    PowerManager.USER_ACTIVITY_EVENT_OTHER,
                    PowerManager.USER_ACTIVITY_FLAG_NO_CHANGE_LIGHTS,
                    wakeLock.mOwnerUid);
        }
    }

    @Override // Binder call
    public void updateWakeLockWorkSource(IBinder lock, WorkSource ws) {
        if (lock == null) {
            throw new IllegalArgumentException("lock must not be null");
        }

        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.WAKE_LOCK, null);
        if (ws != null && ws.size() != 0) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.UPDATE_DEVICE_STATS, null);
        } else {
            ws = null;
        }

        final long ident = Binder.clearCallingIdentity();
        try {
            updateWakeLockWorkSourceInternal(lock, ws);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void updateWakeLockWorkSourceInternal(IBinder lock, WorkSource ws) {
        synchronized (mLock) {
            int index = findWakeLockIndexLocked(lock);
            if (index < 0) {
                throw new IllegalArgumentException("Wake lock not active");
            }

            WakeLock wakeLock = mWakeLocks.get(index);
            if (!wakeLock.hasSameWorkSource(ws)) {
                notifyWakeLockReleasedLocked(wakeLock);
                wakeLock.updateWorkSource(ws);
                notifyWakeLockAcquiredLocked(wakeLock);
            }
        }
    }

    // ++ [framework] For CPU performance controlling mechanism.
    @Override
    public void updateHtcCpuCtrlWorkSource(IBinder lock, WorkSource ws) {
        if (lock == null) {
            throw new IllegalArgumentException("lock must not be null");
        }

        if (ws != null && ws.size() != 0) {
            // Do nothing
        } else {
            ws = null;
        }

        final long ident = Binder.clearCallingIdentity();
        try {
            updateHtcCpuCtrlWorkSourceInternal(lock, ws);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void updateHtcCpuCtrlWorkSourceInternal(IBinder lock, WorkSource ws) {
        synchronized (mLock) {
            int index = findHtcCpuCtrlIndexLocked(lock);
            if (index < 0) {
                throw new IllegalArgumentException("HtcCpuCtrl not active");
            }

            HtcCpuCtrl htcCpuCtrl = mHtcCpuCtrls.get(index);
            if (!htcCpuCtrl.hasSameWorkSource(ws)) {
                notifyHtcCpuCtrlReleasedLocked(htcCpuCtrl);
                htcCpuCtrl.updateWorkSource(ws);
                notifyHtcCpuCtrlAcquiredLocked(htcCpuCtrl);
            }
        }
    }
    // -- [framework] For CPU performance controlling mechanism.

    private int findWakeLockIndexLocked(IBinder lock) {
        final int count = mWakeLocks.size();
        for (int i = 0; i < count; i++) {
            if (mWakeLocks.get(i).mLock == lock) {
                return i;
            }
        }
        return -1;
    }

    // ++ [framework] For CPU performance controlling mechanism.
    private int findHtcCpuCtrlIndexLocked(IBinder lock) {
        final int count = mHtcCpuCtrls.size();
        for (int i = 0; i < count; i++) {
            if (mHtcCpuCtrls.get(i).mLock == lock) {
                return i;
            }
        }
        return -1;
    }
    // -- [framework] For CPU performance controlling mechanism.

    private void notifyWakeLockAcquiredLocked(WakeLock wakeLock) {
        if (mSystemReady) {
            mNotifier.onWakeLockAcquired(wakeLock.mFlags, wakeLock.mTag,
                    wakeLock.mOwnerUid, wakeLock.mOwnerPid, wakeLock.mWorkSource);
        }
    }

    private void notifyWakeLockReleasedLocked(WakeLock wakeLock) {
        if (mSystemReady) {
            mNotifier.onWakeLockReleased(wakeLock.mFlags, wakeLock.mTag,
                    wakeLock.mOwnerUid, wakeLock.mOwnerPid, wakeLock.mWorkSource);
        }
    }

    // ++ [framework] For CPU performance controlling mechanism.
    private void notifyHtcCpuCtrlAcquiredLocked(HtcCpuCtrl htcCpuCtrl) {
        if (mSystemReady) {
            mNotifier.onWakeLockAcquired(PowerManager.PARTIAL_WAKE_LOCK, htcCpuCtrl.mTag,
                    htcCpuCtrl.mOwnerUid, htcCpuCtrl.mOwnerPid, htcCpuCtrl.mWorkSource);
        }
    }

    private void notifyHtcCpuCtrlReleasedLocked(HtcCpuCtrl htcCpuCtrl) {
        if (mSystemReady) {
            mNotifier.onWakeLockReleased(PowerManager.PARTIAL_WAKE_LOCK, htcCpuCtrl.mTag,
                    htcCpuCtrl.mOwnerUid, htcCpuCtrl.mOwnerPid, htcCpuCtrl.mWorkSource);
        }
    }
    // -- [framework] For CPU performance controlling mechanism.

    @Override // Binder call
    public boolean isWakeLockLevelSupported(int level) {
        final long ident = Binder.clearCallingIdentity();
        try {
            return isWakeLockLevelSupportedInternal(level);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private boolean isWakeLockLevelSupportedInternal(int level) {
        synchronized (mLock) {
            switch (level) {
                case PowerManager.PARTIAL_WAKE_LOCK:
                case PowerManager.SCREEN_DIM_WAKE_LOCK:
                case PowerManager.SCREEN_BRIGHT_WAKE_LOCK:
                case PowerManager.FULL_WAKE_LOCK:
                // ++ [framework] to support HtcCpuCtrl
                case PowerManager.CPU_PERF_WAKE_LOCK:
                case PowerManager.CPU_MAX_FREQ:
                case PowerManager.CPU_MIN_FREQ:
                case PowerManager.CPU_MAX_NUM:
                case PowerManager.CPU_MIN_NUM:
                // -- [framework[ to support HtcCpuCtrl
                    return true;

                case PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK:
                    return mSystemReady && mDisplayPowerController.isProximitySensorAvailable();

                default:
                    return false;
            }
        }
    }

    @Override // Binder call
    public void userActivity(long eventTime, int event, int flags) {
        final long now = SystemClock.uptimeMillis();
        if (mContext.checkCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER)
                != PackageManager.PERMISSION_GRANTED) {
            // Once upon a time applications could call userActivity().
            // Now we require the DEVICE_POWER permission.  Log a warning and ignore the
            // request instead of throwing a SecurityException so we don't break old apps.
            synchronized (mLock) {
                if (now >= mLastWarningAboutUserActivityPermission + (5 * 60 * 1000)) {
                    mLastWarningAboutUserActivityPermission = now;
                    Slog.w(TAG, "Ignoring call to PowerManager.userActivity() because the "
                            + "caller does not have DEVICE_POWER permission.  "
                            + "Please fix your app!  "
                            + " pid=" + Binder.getCallingPid()
                            + " uid=" + Binder.getCallingUid());
                }
            }
            return;
        }

        if (eventTime > SystemClock.uptimeMillis()) {
            throw new IllegalArgumentException("event time must not be in the future");
        }

        final int uid = Binder.getCallingUid();
        final long ident = Binder.clearCallingIdentity();
        try {
            userActivityInternal(eventTime, event, flags, uid);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    // Called from native code.
    // ++ [framework] Add 'force' variable
    private void userActivityFromNative(long eventTime, int event, int flags, boolean force) {
    // -- [framework] Add 'force' variable
        // ++ [framework] Add 'force' variable
        userActivityInternal(eventTime, event, flags, Process.SYSTEM_UID, force);
        // -- [framework] Add 'force' variable
    }

    private void userActivityInternal(long eventTime, int event, int flags, int uid) {
        // ++ [framework] Overload function with 'force' variable
        userActivityInternal(eventTime, event, flags, uid, false);
        // -- [framework] overload function with 'force' variable
    }

    // ++ [framework] Overload function with 'force' variable
    private void userActivityInternal(long eventTime, int event, int flags, int uid, boolean force) {
    // -- [framework] Overload function with 'force' variable
        synchronized (mLock) {
            // ++ [framework] Add 'force' variable
            if (userActivityNoUpdateLocked(eventTime, event, flags, uid, force)) {
            // -- [framework] Add 'force' variable
                updatePowerStateLocked();
            }
        }
    }

    private boolean userActivityNoUpdateLocked(long eventTime, int event, int flags, int uid) {
        // ++ [framework] Overload function with 'force' variable
        return userActivityNoUpdateLocked(eventTime, event, flags, uid, false);
        // -- [framework] Overload function with 'force' variable
    }

    // ++ [framework] Overload function with 'force' variable
    private boolean userActivityNoUpdateLocked(long eventTime, int event, int flags, int uid, boolean force) {
    // -- [framework] Overload function with 'force' variable

        // ++ [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.
        if (mScreenTimeout_Once > 0) {
            if (DEBUG_ON) {
                Slog.d(TAG, "restore:"
                        + " mScreenTimeout_Once=" + mScreenTimeout_Once
                        + ", force reset screen timeout.");
            }
            mHandler.removeMessages(MSG_SEND_CLEAR_SCREEN_TIMEOUT_TASK);
            mHandler.removeCallbacks(mClearScreenTimeoutTask);
            mScreenTimeout_Once = -1;
            recalculateScreenTimeout();
        }
        // -- [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.

        //++ U3400 User Behavior Logging ++
//        if (!com.htc.profileflag.ProfileConfig.getProfileForceDisableUlog()) {
            com.htc.server.HtcDeviceInfoManager deviceManager =
                com.htc.server.HtcDeviceInfoManager.getInstance();
            if (deviceManager != null) {
                deviceManager.userActivity(eventTime);
            } else {
                Slog.w(TAG, "userActivity, deviceInfoManager not ready!");
            }
//        }
        //-- U3400 User Behavior Logging --

        if (DEBUG_SPEW) {
            Slog.d(TAG, "userActivityNoUpdateLocked: eventTime=" + eventTime
                    + ", event=" + event + ", flags=0x" + Integer.toHexString(flags)
                    + ", uid=" + uid);
        }

        // ++ [framework] Add 'force' variable
        if (!force && (eventTime < mLastSleepTime || eventTime < mLastWakeTime
                || mWakefulness == WAKEFULNESS_ASLEEP || !mBootCompleted || !mSystemReady)) {
//        if (eventTime < mLastSleepTime || eventTime < mLastWakeTime
//                || mWakefulness == WAKEFULNESS_ASLEEP || !mBootCompleted || !mSystemReady) {
        // -- [framework] Add 'force' variable
            return false;
        }

        mNotifier.onUserActivity(event, uid);

        if ((flags & PowerManager.USER_ACTIVITY_FLAG_NO_CHANGE_LIGHTS) != 0) {
            if (eventTime > mLastUserActivityTimeNoChangeLights
                    // ++ [framework] Add 'force' variable
                    && eventTime > mLastUserActivityTime || force) {
//                    && eventTime > mLastUserActivityTime) {
                    // -- [framework] Add 'force' variable
                mLastUserActivityTimeNoChangeLights = eventTime;
                mDirty |= DIRTY_USER_ACTIVITY;
                return true;
            }
        } else {
            // ++ [framework] Add 'force' variable
            if (eventTime > mLastUserActivityTime || force) {
//            if (eventTime > mLastUserActivityTime) {
            // -- [framework] Add 'force' variable
                mLastUserActivityTime = eventTime;
                mDirty |= DIRTY_USER_ACTIVITY;
                return true;
            }
        }
        return false;
    }

    @Override // Binder call
    public void wakeUp(long eventTime) {
        if (eventTime > SystemClock.uptimeMillis()) {
            throw new IllegalArgumentException("event time must not be in the future");
        }

        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        // ++ [framework] Add debug info.
        if (DEBUG_ON) {
            Slog.d(TAG, "wakeUp(" + eventTime + ")"
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid()
                    + ", stack=" + android.os.Debug.getCallers(3));
        }
        // -- [framework] Add debug info.

        final long ident = Binder.clearCallingIdentity();
        try {
            wakeUpInternal(eventTime);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    // Called from native code.
    private void wakeUpFromNative(long eventTime) {
        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "wakeUpFromNative(" + eventTime + ")"
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.

        // ++ [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
        if (mDisplayPowerRequest != null && (mProximityPositive || mADTalkingDetected)) {
            if (DEBUG_ON) {
                Slog.d(TAG, "Wake up requested from native while p-sensor NEAR or AD talking detected.");
            }
            mDisplayPowerRequest.forceScreenOn = true;
        }
        // -- [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.

        wakeUpInternal(eventTime);
    }

    private void wakeUpInternal(long eventTime) {
        synchronized (mLock) {
            if (wakeUpNoUpdateLocked(eventTime)) {
                updatePowerStateLocked();
            }
        }
    }

    private boolean wakeUpNoUpdateLocked(long eventTime) {
        if (DEBUG_SPEW) {
            Slog.d(TAG, "wakeUpNoUpdateLocked: eventTime=" + eventTime);
        }

        // ++ [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
        if ((eventTime < mLastSleepTime || mWakefulness == WAKEFULNESS_AWAKE
                || !mBootCompleted || !mSystemReady) && !mDisplayPowerRequest.forceScreenOn) {
//        if (eventTime < mLastSleepTime || mWakefulness == WAKEFULNESS_AWAKE
//                || !mBootCompleted || !mSystemReady) {
        // -- [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
            return false;
        }

        switch (mWakefulness) {
            case WAKEFULNESS_ASLEEP:
                Slog.i(TAG, "Waking up from sleep...");
                sendPendingNotificationsLocked();
                mNotifier.onWakeUpStarted();
                mSendWakeUpFinishedNotificationWhenReady = true;
                break;
            case WAKEFULNESS_DREAMING:
                Slog.i(TAG, "Waking up from dream...");
                break;
            case WAKEFULNESS_NAPPING:
                Slog.i(TAG, "Waking up from nap...");
                break;
        }

        mLastWakeTime = eventTime;
        mWakefulness = WAKEFULNESS_AWAKE;
        mDirty |= DIRTY_WAKEFULNESS;

        userActivityNoUpdateLocked(
                eventTime, PowerManager.USER_ACTIVITY_EVENT_OTHER, 0, Process.SYSTEM_UID);
        return true;
    }

    //[++framework] Add new API for HtcShutdownThread, peter_li 20130426 
    public void goToSleepForShutdown(long time, int reason) {

        if (DEBUG_ON) {
            Slog.d(TAG, "goToSleepForShutdown(" + time + ", " + reason + ")");
        }

        synchronized (mShutdownLock) {
            startGoToSleepByShutdown = true;
            doneNativeSetAutoSuspend = false;
        }
        goToSleep(time,reason);
    }

    public boolean getNativeSetAutoSuspendDone() {

        if (DEBUG_ON) {
            Slog.d(TAG, "getNativeSetAutoSuspendDone(" + doneNativeSetAutoSuspend + ")");
        }

        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        synchronized (mShutdownLock) {
            if (startGoToSleepByShutdown) {
                if (doneNativeSetAutoSuspend) {
                    if (DEBUG_ON) {
                        Slog.d(TAG, "Auto Suspend Done");
                    }
                    startGoToSleepByShutdown = false;
                    doneNativeSetAutoSuspend = false;
                    return true;
                }
            } else {
                if (DEBUG_ON) {
                    Slog.d(TAG, "Not call by Shutdown Thread");
                }
                startGoToSleepByShutdown = false;
                doneNativeSetAutoSuspend = false;
                return false;
            }
        }

        if (mCondition != null) {
            if (DEBUG_ON) {
                Slog.d(TAG, "trigger condition block");
            }
            if (!mCondition.block(WAIT_FOR_SUSPEND_TO_COMPLETE)) {
                Slog.d(TAG, "getNativeSetAutoSuspendDone: timeout");
            }
            if (DEBUG_ON) {
                Slog.d(TAG, "close condition block");
            }
            mCondition.close();
        }

        synchronized (mShutdownLock) {
            boolean tmpValue = false;
            tmpValue = doneNativeSetAutoSuspend;
            startGoToSleepByShutdown = false;
            doneNativeSetAutoSuspend = false;
            return tmpValue;
        }

    }
    //[--framework] Add new API for HtcShutdownThread, peter_li 20130426 

    @Override // Binder call
    public void goToSleep(long eventTime, int reason) {
        if (eventTime > SystemClock.uptimeMillis()) {
            throw new IllegalArgumentException("event time must not be in the future");
        }

        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "goToSleep(" + eventTime + ", " + reason + ")"
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid()
                    + ", stack=" + android.os.Debug.getCallers(3));
        }
        // -- [framework] Add debug message.

        final long ident = Binder.clearCallingIdentity();
        try {
            goToSleepInternal(eventTime, reason);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    // Called from native code.
    private void goToSleepFromNative(long eventTime, int reason) {
        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "goToSleepFromNative(" + eventTime + ", " + reason + ")"
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.
        goToSleepInternal(eventTime, reason);
    }

    private void goToSleepInternal(long eventTime, int reason) {
        synchronized (mLock) {
            if (goToSleepNoUpdateLocked(eventTime, reason)) {
                updatePowerStateLocked();
            }
        }
    }

    private boolean goToSleepNoUpdateLocked(long eventTime, int reason) {
        if (DEBUG_SPEW) {
            Slog.d(TAG, "goToSleepNoUpdateLocked: eventTime=" + eventTime + ", reason=" + reason);
        }

        if (eventTime < mLastWakeTime || mWakefulness == WAKEFULNESS_ASLEEP
                || !mBootCompleted || !mSystemReady) {
            return false;
        }

        switch (reason) {
            case PowerManager.GO_TO_SLEEP_REASON_DEVICE_ADMIN:
                Slog.i(TAG, "Going to sleep due to device administration policy...");
                break;
            case PowerManager.GO_TO_SLEEP_REASON_TIMEOUT:
                Slog.i(TAG, "Going to sleep due to screen timeout...");
                // ++ [framework] Workaround for M7 p-sensor problem.
                if (mDisplayPowerController != null) {
                    mDisplayPowerController.notifyScreenTimeout(true);
                }
                // -- [framework] Workaround for M7 p-sensor problem.
                break;
            default:
                Slog.i(TAG, "Going to sleep by user request...");
                reason = PowerManager.GO_TO_SLEEP_REASON_USER;
                break;
        }

        sendPendingNotificationsLocked();
        mNotifier.onGoToSleepStarted(reason);
        mSendGoToSleepFinishedNotificationWhenReady = true;

        mLastSleepTime = eventTime;
        mDirty |= DIRTY_WAKEFULNESS;
        mWakefulness = WAKEFULNESS_ASLEEP;

        // Report the number of wake locks that will be cleared by going to sleep.
        int numWakeLocksCleared = 0;
        final int numWakeLocks = mWakeLocks.size();
        for (int i = 0; i < numWakeLocks; i++) {
            final WakeLock wakeLock = mWakeLocks.get(i);
            switch (wakeLock.mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) {
                case PowerManager.FULL_WAKE_LOCK:
                case PowerManager.SCREEN_BRIGHT_WAKE_LOCK:
                case PowerManager.SCREEN_DIM_WAKE_LOCK:
                    numWakeLocksCleared += 1;
                    break;
            }
        }
        EventLog.writeEvent(EventLogTags.POWER_SLEEP_REQUESTED, numWakeLocksCleared);
        return true;
    }

    @Override // Binder call
    public void nap(long eventTime) {
        if (eventTime > SystemClock.uptimeMillis()) {
            throw new IllegalArgumentException("event time must not be in the future");
        }

        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        final long ident = Binder.clearCallingIdentity();
        try {
            napInternal(eventTime);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void napInternal(long eventTime) {
        synchronized (mLock) {
            if (napNoUpdateLocked(eventTime)) {
                updatePowerStateLocked();
            }
        }
    }

    private boolean napNoUpdateLocked(long eventTime) {
        if (DEBUG_SPEW) {
            Slog.d(TAG, "napNoUpdateLocked: eventTime=" + eventTime);
        }

        if (eventTime < mLastWakeTime || mWakefulness != WAKEFULNESS_AWAKE
                || !mBootCompleted || !mSystemReady) {
            return false;
        }

        Slog.i(TAG, "Nap time...");

        mDirty |= DIRTY_WAKEFULNESS;
        mWakefulness = WAKEFULNESS_NAPPING;
        return true;
    }

    /**
     * Updates the global power state based on dirty bits recorded in mDirty.
     *
     * This is the main function that performs power state transitions.
     * We centralize them here so that we can recompute the power state completely
     * each time something important changes, and ensure that we do it the same
     * way each time.  The point is to gather all of the transition logic here.
     */
    private void updatePowerStateLocked() {
        // ++ [framework] Add debug log.
        if (DEBUG_ON && mHtcPMSExtension != null) {
            Formatter f = new Formatter();
            try {
                final String logKey = "udpatePowerStateLocked-arg_check";
                f.format("updatePowerStateLocked: mDirty=0x%x, mSystemReady=%b", mDirty, mSystemReady);

                mHtcPMSExtension.reduceLog(logKey, TAG, f.toString());
            } catch (Exception e) {
                if (f != null) {
                    Slog.d(TAG, " - " + f.toString());
                }
            }
        }
        // -- [framework] Add debug log.

        if (!mSystemReady || mDirty == 0) {
            return;
        }

        // Phase 0: Basic state updates.
        updateIsPoweredLocked(mDirty);
        updateStayOnLocked(mDirty);

        // Phase 1: Update wakefulness.
        // Loop because the wake lock and user activity computations are influenced
        // by changes in wakefulness.
        final long now = SystemClock.uptimeMillis();
        int dirtyPhase2 = 0;
        for (;;) {
            int dirtyPhase1 = mDirty;
            dirtyPhase2 |= dirtyPhase1;
            mDirty = 0;

            updateWakeLockSummaryLocked(dirtyPhase1);
            updateUserActivitySummaryLocked(now, dirtyPhase1);
            if (!updateWakefulnessLocked(dirtyPhase1)) {
                break;
            }
        }

        // Phase 2: Update dreams and display power state.
        updateDreamLocked(dirtyPhase2);
        updateDisplayPowerStateLocked(dirtyPhase2);

        // Phase 3: Send notifications, if needed.
        if (mDisplayReady) {
            sendPendingNotificationsLocked();
        }

        // Phase 4: Update suspend blocker.
        // Because we might release the last suspend blocker here, we need to make sure
        // we finished everything else first!
        updateSuspendBlockerLocked();
    }

    private void sendPendingNotificationsLocked() {
        if (mSendWakeUpFinishedNotificationWhenReady) {
            mSendWakeUpFinishedNotificationWhenReady = false;
            mNotifier.onWakeUpFinished();
        }
        if (mSendGoToSleepFinishedNotificationWhenReady) {
            mSendGoToSleepFinishedNotificationWhenReady = false;
            mNotifier.onGoToSleepFinished();
        }
    }

    /**
     * Updates the value of mIsPowered.
     * Sets DIRTY_IS_POWERED if a change occurred.
     */
    private void updateIsPoweredLocked(int dirty) {
        if ((dirty & DIRTY_BATTERY_STATE) != 0) {
            final boolean wasPowered = mIsPowered;
            final int oldPlugType = mPlugType;
            mIsPowered = mBatteryService.isPowered(BatteryManager.BATTERY_PLUGGED_ANY);
            mPlugType = mBatteryService.getPlugType();
            mBatteryLevel = mBatteryService.getBatteryLevel();

            // ++ [framework] Add DEBUG_ON.
            if (DEBUG || DEBUG_ON) {
            // -- [framework] Add DEBUG_ON.
                Slog.d(TAG, "updateIsPoweredLocked: wasPowered=" + wasPowered
                        + ", mIsPowered=" + mIsPowered
                        + ", oldPlugType=" + oldPlugType
                        + ", mPlugType=" + mPlugType
                        + ", mBatteryLevel=" + mBatteryLevel);
            }

            if (wasPowered != mIsPowered || oldPlugType != mPlugType) {
                mDirty |= DIRTY_IS_POWERED;

                // Update wireless dock detection state.
                final boolean dockedOnWirelessCharger = mWirelessChargerDetector.update(
                        mIsPowered, mPlugType, mBatteryLevel);

                // Treat plugging and unplugging the devices as a user activity.
                // Users find it disconcerting when they plug or unplug the device
                // and it shuts off right away.
                // Some devices also wake the device when plugged or unplugged because
                // they don't have a charging LED.
                final long now = SystemClock.uptimeMillis();
                if (shouldWakeUpWhenPluggedOrUnpluggedLocked(wasPowered, oldPlugType,
                        dockedOnWirelessCharger)) {
                    wakeUpNoUpdateLocked(now);
                }
                userActivityNoUpdateLocked(
                        now, PowerManager.USER_ACTIVITY_EVENT_OTHER, 0, Process.SYSTEM_UID);

                // Tell the notifier whether wireless charging has started so that
                // it can provide feedback to the user.
                if (dockedOnWirelessCharger) {
                    mNotifier.onWirelessChargingStarted();
                }

                //+ Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.
                if (mbEnableDeskDockBehavior && mDeskModeEnabled) {
                    // If this feature is enable and desk dock is on,
                    // recalculate everything and reset timer
                    recalculateScreenTimeout();
                    userActivity(SystemClock.uptimeMillis(), PowerManager.USER_ACTIVITY_EVENT_OTHER, 0);
                }
                //- Greg Tsai (2011/06/21).
            }
        }
    }

    private boolean shouldWakeUpWhenPluggedOrUnpluggedLocked(
            boolean wasPowered, int oldPlugType, boolean dockedOnWirelessCharger) {
        // Don't wake when powered unless configured to do so.
        if (!mWakeUpWhenPluggedOrUnpluggedConfig) {
            return false;
        }

        // Don't wake when undocked from wireless charger.
        // See WirelessChargerDetector for justification.
        if (wasPowered && !mIsPowered
                && oldPlugType == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
            return false;
        }

        // Don't wake when docked on wireless charger unless we are certain of it.
        // See WirelessChargerDetector for justification.
        if (!wasPowered && mIsPowered
                && mPlugType == BatteryManager.BATTERY_PLUGGED_WIRELESS
                && !dockedOnWirelessCharger) {
            return false;
        }

        // If already dreaming and becoming powered, then don't wake.
        if (mIsPowered && (mWakefulness == WAKEFULNESS_NAPPING
                || mWakefulness == WAKEFULNESS_DREAMING)) {
            return false;
        }

        // Otherwise wake up!
        return true;
    }

    /**
     * Updates the value of mStayOn.
     * Sets DIRTY_STAY_ON if a change occurred.
     */
    private void updateStayOnLocked(int dirty) {
        if ((dirty & (DIRTY_BATTERY_STATE | DIRTY_SETTINGS)) != 0) {
            final boolean wasStayOn = mStayOn;
            if (mStayOnWhilePluggedInSetting != 0
                    && !isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked()) {
                mStayOn = mBatteryService.isPowered(mStayOnWhilePluggedInSetting);
            } else {
                mStayOn = false;
            }

            if (mStayOn != wasStayOn) {
                mDirty |= DIRTY_STAY_ON;
            }
        }
    }

    /**
     * Updates the value of mWakeLockSummary to summarize the state of all active wake locks.
     * Note that most wake-locks are ignored when the system is asleep.
     *
     * This function must have no other side-effects.
     */
    private void updateWakeLockSummaryLocked(int dirty) {
        if ((dirty & (DIRTY_WAKE_LOCKS | DIRTY_WAKEFULNESS)) != 0) {
            mWakeLockSummary = 0;

            final int numWakeLocks = mWakeLocks.size();
            for (int i = 0; i < numWakeLocks; i++) {
                final WakeLock wakeLock = mWakeLocks.get(i);
                switch (wakeLock.mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) {
                    case PowerManager.PARTIAL_WAKE_LOCK:
                        mWakeLockSummary |= WAKE_LOCK_CPU;
                        break;
                    case PowerManager.FULL_WAKE_LOCK:
                        if (mWakefulness != WAKEFULNESS_ASLEEP) {
                            mWakeLockSummary |= WAKE_LOCK_CPU
                                    | WAKE_LOCK_SCREEN_BRIGHT | WAKE_LOCK_BUTTON_BRIGHT;
                            if (mWakefulness == WAKEFULNESS_AWAKE) {
                                mWakeLockSummary |= WAKE_LOCK_STAY_AWAKE;
                            }
                        }
                        break;
                    case PowerManager.SCREEN_BRIGHT_WAKE_LOCK:
                        if (mWakefulness != WAKEFULNESS_ASLEEP) {
                            mWakeLockSummary |= WAKE_LOCK_CPU | WAKE_LOCK_SCREEN_BRIGHT;
                            if (mWakefulness == WAKEFULNESS_AWAKE) {
                                mWakeLockSummary |= WAKE_LOCK_STAY_AWAKE;
                            }
                        }
                        break;
                    case PowerManager.SCREEN_DIM_WAKE_LOCK:
                        if (mWakefulness != WAKEFULNESS_ASLEEP) {
                            mWakeLockSummary |= WAKE_LOCK_CPU | WAKE_LOCK_SCREEN_DIM;
                            if (mWakefulness == WAKEFULNESS_AWAKE) {
                                mWakeLockSummary |= WAKE_LOCK_STAY_AWAKE;
                            }
                        }
                        break;
                    case PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK:
                        // ++ [framework] Fix Google issue, monitor p-sensor event even screen is off.
//                        if (mWakefulness != WAKEFULNESS_ASLEEP) {
                        // -- [framework] Fix Google issue, monitor p-sensor event even screen is off.
                        // ++ [framework] Fix Google issue, p-sensor shouldn't keep CPU awake.
                            mWakeLockSummary |= WAKE_LOCK_PROXIMITY_SCREEN_OFF;
//                            mWakeLockSummary |= WAKE_LOCK_CPU | WAKE_LOCK_PROXIMITY_SCREEN_OFF;
                        // -- [framework] Fix Google issue, p-sensor shouldn't keep CPU awake.
                        // ++ [framework] Fix Google issue, monitor p-sensor event even screen is off.
//                        }
                        // -- [framework] Fix Google issue, monitor p-sensor event even screen is off.
                        break;
                }
            }

            // ++ [framework] Add screen timeout mechanism when dongle mode is turned on
            if (mHtcDongleMode != null) {
                mWakeLockSummary = mHtcDongleMode.getCtrlWakeLockState("updateWakeLockSummaryLocked", mWakeLockSummary,
                        WAKE_LOCK_SCREEN_BRIGHT | WAKE_LOCK_BUTTON_BRIGHT);
            }
            // -- [framework] Add screen timeout mechanism when dongle mode is turned on

            // ++ [framework] Add DEBUG_ON flag.
            if (DEBUG_SPEW || DEBUG_ON) {
            // -- [framework] Add DEBUG_ON flag.
                Slog.d(TAG, "updateWakeLockSummaryLocked:"
                        + " mWakefulness=" + wakefulnessToString(mWakefulness)
                        + ", mWakeLockSummary=0x" + Integer.toHexString(mWakeLockSummary));
            }
        }
    }

    /**
     * Updates the value of mUserActivitySummary to summarize the user requested
     * state of the system such as whether the screen should be bright or dim.
     * Note that user activity is ignored when the system is asleep.
     *
     * This function must have no other side-effects.
     */
    private void updateUserActivitySummaryLocked(long now, int dirty) {
        // Update the status of the user activity timeout timer.
        if ((dirty & (DIRTY_USER_ACTIVITY | DIRTY_WAKEFULNESS | DIRTY_SETTINGS)) != 0) {
            mHandler.removeMessages(MSG_USER_ACTIVITY_TIMEOUT);

            // ++ [framework] Move variable out, so that it can be accessed in debug msg.
            int screenOffTimeout = 0;
            int screenDimDuration = 0;
            // -- [framework] Move variable out, so that it can be accessed in debug msg.
            long nextTimeout = 0;
            if (mWakefulness != WAKEFULNESS_ASLEEP) {
                // ++ [framework] Move variable out, so that it can be accessed in debug msg.
                screenOffTimeout = getScreenOffTimeoutLocked();
                screenDimDuration = getScreenDimDurationLocked(screenOffTimeout);
                // -- [framework] Move variable out, so that it can be accessed in debug msg.

                mUserActivitySummary = 0;
                if (mLastUserActivityTime >= mLastWakeTime) {
                    nextTimeout = mLastUserActivityTime
                            + screenOffTimeout - screenDimDuration;
                    if (now < nextTimeout) {
                        mUserActivitySummary |= USER_ACTIVITY_SCREEN_BRIGHT;
                    } else {
                        nextTimeout = mLastUserActivityTime + screenOffTimeout;
                        if (now < nextTimeout) {
                            mUserActivitySummary |= USER_ACTIVITY_SCREEN_DIM;
                        }
                    }
                }
                if (mUserActivitySummary == 0
                        && mLastUserActivityTimeNoChangeLights >= mLastWakeTime) {
                    nextTimeout = mLastUserActivityTimeNoChangeLights + screenOffTimeout;
                    if (now < nextTimeout
                            && mDisplayPowerRequest.screenState
                                    != DisplayPowerRequest.SCREEN_STATE_OFF) {
                        mUserActivitySummary = mDisplayPowerRequest.screenState
                                == DisplayPowerRequest.SCREEN_STATE_BRIGHT ?
                                USER_ACTIVITY_SCREEN_BRIGHT : USER_ACTIVITY_SCREEN_DIM;
                    }
                }
                if (mUserActivitySummary != 0) {
                    Message msg = mHandler.obtainMessage(MSG_USER_ACTIVITY_TIMEOUT);
                    msg.setAsynchronous(true);
                    mHandler.sendMessageAtTime(msg, nextTimeout);
                }
            } else {
                mUserActivitySummary = 0;
            }

            // ++ [framework] Add DEBUG_ON flag
            if (DEBUG_SPEW || DEBUG_ON) {
            // -- [framework] Add DEBUG_ON flag
                Slog.d(TAG, "updateUserActivitySummaryLocked: mWakefulness=" + wakefulnessToString(mWakefulness)
                        + ", mUserActivitySummary=0x" + Integer.toHexString(mUserActivitySummary)
                        // ++ [framework] Add debug message.
                        + ", now=" + now
                        + ", dirty=" + dirty
                        + ", screenDimDuration=" + screenDimDuration
                        + ", screenOffTimeout=" + screenOffTimeout
                        // -- [framework] Add debug message.
                        + ", nextTimeout=" + TimeUtils.formatUptime(nextTimeout));
            }
        }
    }

    /**
     * Called when a user activity timeout has occurred.
     * Simply indicates that something about user activity has changed so that the new
     * state can be recomputed when the power state is updated.
     *
     * This function must have no other side-effects besides setting the dirty
     * bit and calling update power state.  Wakefulness transitions are handled elsewhere.
     */
    private void handleUserActivityTimeout() { // runs on handler thread
        synchronized (mLock) {
            if (DEBUG_SPEW) {
                Slog.d(TAG, "handleUserActivityTimeout");
            }

            mDirty |= DIRTY_USER_ACTIVITY;
            updatePowerStateLocked();
        }
    }

    // ++ [framework] Check if the dongle mode is turned on
    private boolean isDongleModeTurnedOn() {
        if (mHtcDongleMode != null && mHtcDongleMode.isDongleMode()) {
            return true;
        }
        return false;
    }
    // -- [framework] Check if the dongle mode is turned on

    private int getScreenOffTimeoutLocked() {
        int timeout = mScreenOffTimeoutSetting;

        // ++ [framework] refine adjust HTC screen delay
        timeout = adjustHtcScreenOffDelay(timeout);
        // -- [framework] refine adjust HTC screen delay

        if (isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked()) {
            timeout = Math.min(timeout, mMaximumScreenOffTimeoutFromDeviceAdmin);
        }
        if (mUserActivityTimeoutOverrideFromWindowManager >= 0) {
            timeout = (int)Math.min(timeout, mUserActivityTimeoutOverrideFromWindowManager);
        }
        return Math.max(timeout, MINIMUM_SCREEN_OFF_TIMEOUT);
    }

    private int getScreenDimDurationLocked(int screenOffTimeout) {
        // ++ [framework] refine adjust HTC screen delay
        int adjDimDelay = adjustHtcScreenDimDuration(screenOffTimeout);
        // -- [framework] refine adjust HTC screen delay

        if ((-1) == adjDimDelay) {
            return Math.min(SCREEN_DIM_DURATION,
                    (int)(screenOffTimeout * MAXIMUM_SCREEN_DIM_RATIO));
        } else {
            return Math.min(screenOffTimeout, adjDimDelay);
        }
    }

    // ++ [framework] refine adjust HTC screen delay
    private int adjustHtcScreenOffDelay(int totalDelay) {
        int adjTotalDelay = totalDelay;

        if ((-1) == mScreenOffTimeoutSetting) {
            adjTotalDelay = mMaximumScreenOffTimeoutFromDeviceAdmin;
            if (DEBUG_ON) {
                Slog.d(TAG, "adjustHtcScreenOffDelay[NTF]: adjTotalDelay=" + adjTotalDelay);
            }
        }

        // ++ [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.
        if (mScreenTimeout_Once > 0) {
            adjTotalDelay = mScreenTimeout_Once;
            if (DEBUG_ON) {
                Slog.d(TAG, "adjustHtcScreenOffDelay[Once]: adjTotalDelay=" + adjTotalDelay);
            }
        } else if (mScreenTimeout_Always > 0) {
            adjTotalDelay = mScreenTimeout_Always;
            if (DEBUG_ON) {
                Slog.d(TAG, "adjustHtcScreenOffDelay[Always]: adjTotalDelay=" + adjTotalDelay);
            }
        } else if ((-2) == mScreenOffTimeoutSetting && mScreenTimeout_System > 0) {
            adjTotalDelay = mScreenTimeout_System;
            if (DEBUG_ON) {
                Slog.d(TAG, "adjustHtcScreenOffDelay[System]: adjTotalDelay=" + adjTotalDelay);
            }
        }
        // -- [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.

        // ++ [framework] To prevent "Never turn off" screen time out setting bypass the 3LM device policy, Menghui Wu, 2011/08/19
        try {
            if (HtcPMSExtension.getHtcCustomizationReader("Android_Core_Framework").readBoolean("support_3lm", false) &&
                    ((-1) == mScreenOffTimeoutSetting)) { // -1 is screen never timeout
                adjTotalDelay = Integer.MAX_VALUE;
                if (DEBUG_ON) {
                    Slog.d(TAG, "adjustHtcScreenOffDelay[3LM]: adjTotalDelay=" + adjTotalDelay);
                }
            }
        } catch (Exception e) {
            if (DEBUG_ON) {
                Slog.d(TAG, "adjustHtcScreenOffDelay[3LM]: Exception caught. " + e);
            }
        }
        // -- [framework] To prevent "Never turn off" screen time out setting bypass the 3LM device policy, Menghui Wu, 2011/08/19

        // ++ Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.
        if (mbEnableDeskDockBehavior) {
            // only use setting of dim timeour for desk dock mode 
            // when device is in the desk dock mode, charger is pulgged, its value is not -1.
            if (mDeskModeEnabled && mIsPowered && mScreenOffDelayForDeskDock != -1) {
                // since the value is for dim delay, add LONG_DIM_TIME back.
                adjTotalDelay = mScreenOffDelayForDeskDock + SCREEN_DIM_DURATION;
                if (DEBUG_ON) {
                    Slog.d(TAG, "adjustHtcScreenOffDelay[DeskDock]: adjTotalDelay=" + adjTotalDelay);
                }
            }
        }
        // -- Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.

        // ++ [framework] Add screen timeout mechanism when dongle mode is turned on
        if (isDongleModeTurnedOn()) {
            adjTotalDelay = mMaximumScreenOffTimeoutFromDeviceAdmin;
            if (DEBUG_ON) {
                Slog.d(TAG, "adjustHtcScreenOffDelay[DongleMode]: adjTotalDelay=" + adjTotalDelay);
            }
        }
        // -- [framework] Add screen timeout mechanism when dongle mode is turned on

        return adjTotalDelay;
    }

    private int adjustHtcScreenDimDuration(final int screenOffTimeout) {
        int adjDimDuration = -1;

        if (mHtcPMSExtension != null && mHtcPMSExtension.isAMOLED()) {
            if ((-1) == mScreenOffTimeoutSetting) {
                adjDimDuration = screenOffTimeout - (30 * 60 * 1000);   // 30 mins
                if (DEBUG_ON) {
                    Slog.d(TAG, "adjustHtcScreenDimDuration[AMOLED]: adjDimDuration=" + adjDimDuration);
                }
            }
        }

        // ++ [framework] Add screen dim mechanism when dongle mode is turned on
        if (isDongleModeTurnedOn()) {
            adjDimDuration = screenOffTimeout - SCREEN_ENTER_DIM_IN_DONGLE_MODE;
            if (DEBUG_ON) {
                Slog.d(TAG, "adjustHtcScreenDimDuration[DongleMode]: adjDimDuration=" + adjDimDuration);
            }
        }
        // -- [framework] Add screen dim mechanism when dongle mode is turned on

        return adjDimDuration;
    }
    // -- [framework] refine adjust HTC screen delay

    // ++ [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.
    private Runnable mClearScreenTimeoutTask = new Runnable() {
        public void run() {
            if (DEBUG_ON) {
                Slog.d(TAG, "Clear Once ScreenTimeout");
            }
            synchronized (this) {
                mHandler.removeMessages(MSG_SEND_CLEAR_SCREEN_TIMEOUT_TASK);
                mHandler.removeCallbacks(this);
                mScreenTimeout_Once = -1;
                //recalculateScreenTimeout();
            }
        }
    };

    public void userActivityWithScreenDelay(long when, boolean noChangeLights, final int keylightDelay, final int dimDelay, final int screenOffDelay) {
        if (DEBUG_ON) {
            Slog.d(TAG, "userActivityWithScreenDelay(" + when + ", " + noChangeLights + ", " + keylightDelay + ", " + dimDelay + ", " + screenOffDelay + ")"
                + ", called by pid=" + Binder.getCallingPid()
                + ", uid=" + Binder.getCallingUid());
        }
        customizeScreenOffDelay_Once(keylightDelay + dimDelay + screenOffDelay, false);
    }

    public void customizeScreenOffDelay_Once(final int screenOffDelay, final boolean calcRemaining) {
        if (DEBUG_ON) {
            Slog.d(TAG, "customizeScreenOffDelay_Once(" + screenOffDelay + ", " + calcRemaining + ")"
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        synchronized (mLock) {
            customizeScreenOffDelayLocked(PowerManager.SCREEN_DELAY_TYPE_ONCE, screenOffDelay, calcRemaining);
        }
    }

    public void customizeScreenOffDelay_Always(final int screenOffDelay, final boolean calcRemaining) {
        if (DEBUG_ON) {
            Slog.d(TAG, "customizeScreenOffDelay_Always(" + screenOffDelay + ", " + calcRemaining + ")"
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        synchronized (mLock) {
            customizeScreenOffDelayLocked(PowerManager.SCREEN_DELAY_TYPE_ALWAYS, screenOffDelay, calcRemaining);
        }
    }

    public void customizeScreenOffDelay_System(final int screenOffDelay, final boolean calcRemaining) {
        if (DEBUG_ON) {
            Slog.d(TAG, "customizeScreenOffDelay_System(" + screenOffDelay + ", " + calcRemaining + ")"
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        synchronized (mLock) {
            customizeScreenOffDelayLocked(PowerManager.SCREEN_DELAY_TYPE_SYSTEM, screenOffDelay, calcRemaining);
        }
    }

    private void customizeScreenOffDelayLocked(final int flag, final int screenOffDelay, final boolean calcRemaining) {
        // Case: Unknown flag given
        switch (flag & PowerManager.SCREEN_DELAY_TYPE_MASK) {
            case PowerManager.SCREEN_DELAY_TYPE_ONCE:
            case PowerManager.SCREEN_DELAY_TYPE_ALWAYS:
            case PowerManager.SCREEN_DELAY_TYPE_SYSTEM:
                break;
            default:
                if (DEBUG_ON) {
                    Slog.d(TAG, "customizeScreenOffDelayLocked: Unknown flag found. Skip."
                            + " flag=0x" + Integer.toHexString(flag)
                            + ", screenOffDelay=" + screenOffDelay
                            + ", calcRemaining=" + calcRemaining);
                }
                return;
        }

        // Case: Restore timeouts when 'screenOffDelay' is negative
        if (screenOffDelay < 0) {
            switch (flag & PowerManager.SCREEN_DELAY_TYPE_MASK) {
                case PowerManager.SCREEN_DELAY_TYPE_ONCE:
                    // Disable 'once' mode
                    mHandler.removeMessages(MSG_SEND_CLEAR_SCREEN_TIMEOUT_TASK);
                    mHandler.removeCallbacks(mClearScreenTimeoutTask);
                    mScreenTimeout_Once = -1;
                    break;
                case PowerManager.SCREEN_DELAY_TYPE_ALWAYS:
                    mScreenTimeout_Always = -1;
                    break;
                case PowerManager.SCREEN_DELAY_TYPE_SYSTEM:
                    mScreenTimeout_System = DEFAULT_SYSTEM_TIMEOUT;
                    break;
            }

            if (DEBUG_ON) {
                Slog.d(TAG, "customizeScreenOffDelayLocked: reset."
                        + " flag=0x" + Integer.toHexString(flag)
                        + " (Once=" + mScreenTimeout_Once
                        + ", Always=" + mScreenTimeout_Always
                        + ", System=" + mScreenTimeout_System + ")");
            }

            recalculateScreenTimeout();
            return;
        }

        // Case: Calculate remaining timeout
        if (calcRemaining) {
            final int CURRENT_TIMEOUT = mScreenTimeout_Once > 0 ? mScreenTimeout_Once :
                                       (mScreenTimeout_Always > 0 ? mScreenTimeout_Always : mScreenTimeout_System);

            if (CURRENT_TIMEOUT != screenOffDelay) {
                final long NOW = SystemClock.uptimeMillis();
//                long elapsed = NOW - mTimeoutTaskAssignedTime;
                long elapsed = NOW - mLastUserActivityTime;
                long remaining = CURRENT_TIMEOUT - elapsed;

                // Case: Previous Timeout < New Timeout
                if (CURRENT_TIMEOUT < screenOffDelay) {
                    mScreenTimeout_Once = (int) (screenOffDelay - elapsed);
                // Case: Previous Timeout > New Timeout
                } else if (CURRENT_TIMEOUT > screenOffDelay) {
                    mScreenTimeout_Once = (int) Math.min(remaining, screenOffDelay);
                }

                // Case: remaining must be at least greater than 7 secs
                mScreenTimeout_Once = Math.max(mScreenTimeout_Once, SCREEN_DIM_DURATION);

                recalculateScreenTimeout();

                // Case: Reschedule timeout task only when device is not in DIM or OFF mode
                if (isScreenBright() && mWakefulness != WAKEFULNESS_ASLEEP) {
//                    setTimeoutLocked(SystemClock.uptimeMillis(), SCREEN_BRIGHT);
                    mLastUserActivityTime = NOW;
                    mDirty |= DIRTY_USER_ACTIVITY;
                    updateUserActivitySummaryLocked(NOW, DIRTY_SETTINGS);
                    //[++framework] need to post ClearScreenTimeoutTask to clear once flag
                    handleClearScreenTimeoutTask(mScreenTimeout_Once, 0);
                    //[--framework] need to post ClearScreenTimeoutTask to clear once flag
                }
            // Case: Previous Timeout = New Timeout
            } else {
                if (DEBUG_ON) {
                    Slog.d(TAG, "customizeScreenOffDelayLocked: Same. Skip remaining calculation.");
                }
            }
        }

        int prev_once = mScreenTimeout_Once;
        int prev_always = mScreenTimeout_Always;
        int prev_system = mScreenTimeout_System;
        switch (flag & PowerManager.SCREEN_DELAY_TYPE_MASK) {
            case PowerManager.SCREEN_DELAY_TYPE_ONCE:
                if (!calcRemaining) {
                    mScreenTimeout_Once = screenOffDelay;
                    //[++framework] need to post ClearScreenTimeoutTask to clear once flag
                    handleClearScreenTimeoutTask(mScreenTimeout_Once, 1);
                    //[--framework] need to post ClearScreenTimeoutTask to clear once flag
                }
                break;
            case PowerManager.SCREEN_DELAY_TYPE_ALWAYS:
                mScreenTimeout_Always = screenOffDelay;
                break;
            case PowerManager.SCREEN_DELAY_TYPE_SYSTEM:
                mScreenTimeout_System = screenOffDelay;
                break;
        }

        if (DEBUG_ON) {
            Slog.d(TAG, "customizeScreenOffDelayLocked:"
                    + " flag=0x" + Integer.toHexString(flag)
                    + ", screenOffDelay=" + screenOffDelay
                    + ", calcRemaining=" + calcRemaining
                    + " (Once=" + prev_once + "->" + mScreenTimeout_Once
                    + ", Always=" + prev_always + "->" + mScreenTimeout_Always
                    + ", System=" + prev_system + "->" + mScreenTimeout_System + ")"
                    + ", mWakefulness=" + wakefulnessToString(mWakefulness));
        }

        recalculateScreenTimeout();

        // Case: Apply new timeout immidiately when calcRemaining is false
        if (!calcRemaining && isScreenBright() && mWakefulness != WAKEFULNESS_ASLEEP) {
//            setTimeoutLocked(SystemClock.uptimeMillis(), SCREEN_BRIGHT);
            final long NOW = SystemClock.uptimeMillis();
            mLastUserActivityTime = NOW;
            mDirty |= DIRTY_USER_ACTIVITY;
            updateUserActivitySummaryLocked(NOW, DIRTY_SETTINGS);
            //[++framework] need to post ClearScreenTimeoutTask to clear once flag
            if (flag == PowerManager.SCREEN_DELAY_TYPE_ONCE) {
                handleClearScreenTimeoutTask(mScreenTimeout_Once, 1);
            }
            //[--framework] need to post ClearScreenTimeoutTask to clear once flag
        }
    }
    // -- [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.

    /**
     * Updates the wakefulness of the device.
     *
     * This is the function that decides whether the device should start napping
     * based on the current wake locks and user activity state.  It may modify mDirty
     * if the wakefulness changes.
     *
     * Returns true if the wakefulness changed and we need to restart power state calculation.
     */
    private boolean updateWakefulnessLocked(int dirty) {
        boolean changed = false;
        if ((dirty & (DIRTY_WAKE_LOCKS | DIRTY_USER_ACTIVITY | DIRTY_BOOT_COMPLETED
                | DIRTY_WAKEFULNESS | DIRTY_STAY_ON | DIRTY_PROXIMITY_POSITIVE
                | DIRTY_DOCK_STATE)) != 0) {
            if (mWakefulness == WAKEFULNESS_AWAKE && isItBedTimeYetLocked()) {
                if (DEBUG_SPEW) {
                    Slog.d(TAG, "updateWakefulnessLocked: Bed time...");
                }
                final long time = SystemClock.uptimeMillis();
                if (shouldNapAtBedTimeLocked()) {
                    changed = napNoUpdateLocked(time);
                } else {
                    changed = goToSleepNoUpdateLocked(time,
                            PowerManager.GO_TO_SLEEP_REASON_TIMEOUT);
                }
            }
        }
        return changed;
    }

    /**
     * Returns true if the device should automatically nap and start dreaming when the user
     * activity timeout has expired and it's bedtime.
     */
    private boolean shouldNapAtBedTimeLocked() {
        return mDreamsActivateOnSleepSetting
                || (mDreamsActivateOnDockSetting
                        && mDockState != Intent.EXTRA_DOCK_STATE_UNDOCKED);
    }

    /**
     * Returns true if the device should go to sleep now.
     * Also used when exiting a dream to determine whether we should go back
     * to being fully awake or else go to sleep for good.
     */
    private boolean isItBedTimeYetLocked() {
        return mBootCompleted && !isBeingKeptAwakeLocked();
    }

    /**
     * Returns true if the device is being kept awake by a wake lock, user activity
     * or the stay on while powered setting.
     */
    private boolean isBeingKeptAwakeLocked() {
        return mStayOn
                || mProximityPositive
                || (mWakeLockSummary & WAKE_LOCK_STAY_AWAKE) != 0
                || (mUserActivitySummary & (USER_ACTIVITY_SCREEN_BRIGHT
                        | USER_ACTIVITY_SCREEN_DIM)) != 0;
    }

    /**
     * Determines whether to post a message to the sandman to update the dream state.
     */
    private void updateDreamLocked(int dirty) {
        if ((dirty & (DIRTY_WAKEFULNESS
                | DIRTY_USER_ACTIVITY
                | DIRTY_WAKE_LOCKS
                | DIRTY_BOOT_COMPLETED
                | DIRTY_SETTINGS
                | DIRTY_IS_POWERED
                | DIRTY_STAY_ON
                | DIRTY_PROXIMITY_POSITIVE
                | DIRTY_BATTERY_STATE)) != 0) {
            scheduleSandmanLocked();
        }
    }

    private void scheduleSandmanLocked() {
        if (!mSandmanScheduled) {
            mSandmanScheduled = true;
            Message msg = mHandler.obtainMessage(MSG_SANDMAN);
            msg.setAsynchronous(true);
            mHandler.sendMessage(msg);
        }
    }

    /**
     * Called when the device enters or exits a napping or dreaming state.
     *
     * We do this asynchronously because we must call out of the power manager to start
     * the dream and we don't want to hold our lock while doing so.  There is a risk that
     * the device will wake or go to sleep in the meantime so we have to handle that case.
     */
    private void handleSandman() { // runs on handler thread
        // Handle preconditions.
        boolean startDreaming = false;
        synchronized (mLock) {
            mSandmanScheduled = false;
            boolean canDream = canDreamLocked();
            if (DEBUG_SPEW) {
                Slog.d(TAG, "handleSandman: canDream=" + canDream
                        + ", mWakefulness=" + wakefulnessToString(mWakefulness));
            }

            if (canDream && mWakefulness == WAKEFULNESS_NAPPING) {
                startDreaming = true;
            }
        }

        // Start dreaming if needed.
        // We only control the dream on the handler thread, so we don't need to worry about
        // concurrent attempts to start or stop the dream.
        boolean isDreaming = false;
        if (mDreamManager != null) {
            if (startDreaming) {
                mDreamManager.startDream();
            }
            isDreaming = mDreamManager.isDreaming();
        }

        // Update dream state.
        // We might need to stop the dream again if the preconditions changed.
        boolean continueDreaming = false;
        synchronized (mLock) {
            if (isDreaming && canDreamLocked()) {
                if (mWakefulness == WAKEFULNESS_NAPPING) {
                    mWakefulness = WAKEFULNESS_DREAMING;
                    mDirty |= DIRTY_WAKEFULNESS;
                    mBatteryLevelWhenDreamStarted = mBatteryLevel;
                    updatePowerStateLocked();
                    continueDreaming = true;
                } else if (mWakefulness == WAKEFULNESS_DREAMING) {
                    if (!isBeingKeptAwakeLocked()
                            && mBatteryLevel < mBatteryLevelWhenDreamStarted
                                    - DREAM_BATTERY_LEVEL_DRAIN_CUTOFF) {
                        // If the user activity timeout expired and the battery appears
                        // to be draining faster than it is charging then stop dreaming
                        // and go to sleep.
                        Slog.i(TAG, "Stopping dream because the battery appears to "
                                + "be draining faster than it is charging.  "
                                + "Battery level when dream started: "
                                + mBatteryLevelWhenDreamStarted + "%.  "
                                + "Battery level now: " + mBatteryLevel + "%.");
                    } else {
                        continueDreaming = true;
                    }
                }
            }
            if (!continueDreaming) {
                handleDreamFinishedLocked();
            }
        }

        // Stop dreaming if needed.
        // It's possible that something else changed to make us need to start the dream again.
        // If so, then the power manager will have posted another message to the handler
        // to take care of it later.
        if (mDreamManager != null) {
            if (!continueDreaming) {
                mDreamManager.stopDream();
            }
        }
    }

    /**
     * Returns true if the device is allowed to dream in its current state
     * assuming that it is currently napping or dreaming.
     */
    private boolean canDreamLocked() {
        return mDreamsSupportedConfig
                && mDreamsEnabledSetting
                && mDisplayPowerRequest.screenState != DisplayPowerRequest.SCREEN_STATE_OFF
                && mBootCompleted
                && (mIsPowered || isBeingKeptAwakeLocked());
    }

    /**
     * Called when a dream is ending to figure out what to do next.
     */
    private void handleDreamFinishedLocked() {
        if (mWakefulness == WAKEFULNESS_NAPPING
                || mWakefulness == WAKEFULNESS_DREAMING) {
            if (isItBedTimeYetLocked()) {
                goToSleepNoUpdateLocked(SystemClock.uptimeMillis(),
                        PowerManager.GO_TO_SLEEP_REASON_TIMEOUT);
                updatePowerStateLocked();
            } else {
                wakeUpNoUpdateLocked(SystemClock.uptimeMillis());
                updatePowerStateLocked();
            }
        }
    }

    private void handleScreenOnBlockerReleased() {
        synchronized (mLock) {
            mDirty |= DIRTY_SCREEN_ON_BLOCKER_RELEASED;
            updatePowerStateLocked();
        }
    }

    /**
     * Updates the display power state asynchronously.
     * When the update is finished, mDisplayReady will be set to true.  The display
     * controller posts a message to tell us when the actual display power state
     * has been updated so we come back here to double-check and finish up.
     *
     * This function recalculates the display power state each time.
     */
    private void updateDisplayPowerStateLocked(int dirty) {
        if ((dirty & (DIRTY_WAKE_LOCKS | DIRTY_USER_ACTIVITY | DIRTY_WAKEFULNESS
                | DIRTY_ACTUAL_DISPLAY_POWER_STATE_UPDATED | DIRTY_BOOT_COMPLETED
                | DIRTY_SETTINGS | DIRTY_SCREEN_ON_BLOCKER_RELEASED)) != 0) {
            int newScreenState = getDesiredScreenPowerStateLocked();
            if (newScreenState != mDisplayPowerRequest.screenState) {
                if (newScreenState == DisplayPowerRequest.SCREEN_STATE_OFF
                        && mDisplayPowerRequest.screenState
                                != DisplayPowerRequest.SCREEN_STATE_OFF) {
                    mLastScreenOffEventElapsedRealTime = SystemClock.elapsedRealtime();
                }

                mDisplayPowerRequest.screenState = newScreenState;
                nativeSetPowerState(
                        newScreenState != DisplayPowerRequest.SCREEN_STATE_OFF,
                        newScreenState == DisplayPowerRequest.SCREEN_STATE_BRIGHT);
            }

            int screenBrightness = mScreenBrightnessSettingDefault;
            float screenAutoBrightnessAdjustment = 0.0f;
            boolean autoBrightness = (mScreenBrightnessModeSetting ==
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            // ++ [framework] To determine whether the screen brightness came from override
            boolean overrideBrightness = false;
            // -- [framework] To determine whether the screen brightness came from override
            if (isValidBrightness(mScreenBrightnessOverrideFromWindowManager)) {
                screenBrightness = mScreenBrightnessOverrideFromWindowManager;
                autoBrightness = false;
                // ++ [framework] to determine whether the screen brightness came from override
                overrideBrightness = true;
                // -- [framework] to determine whether the screen brightness came from override
            } else if (isValidBrightness(mTemporaryScreenBrightnessSettingOverride)) {
                screenBrightness = mTemporaryScreenBrightnessSettingOverride;
                // ++ [framework] to determine whether the screen brightness came from override
                overrideBrightness = true;
                // -- [framework] to determine whether the screen brightness came from override
            } else if (isValidBrightness(mScreenBrightnessSetting)) {
                screenBrightness = mScreenBrightnessSetting;
            }
            if (autoBrightness) {
                screenBrightness = mScreenBrightnessSettingDefault;
                if (isValidAutoBrightnessAdjustment(
                        mTemporaryScreenAutoBrightnessAdjustmentSettingOverride)) {
                    screenAutoBrightnessAdjustment =
                            mTemporaryScreenAutoBrightnessAdjustmentSettingOverride;
                } else if (isValidAutoBrightnessAdjustment(
                        mScreenAutoBrightnessAdjustmentSetting)) {
                    screenAutoBrightnessAdjustment = mScreenAutoBrightnessAdjustmentSetting;
                }
            }
            screenBrightness = Math.max(Math.min(screenBrightness,
                    mScreenBrightnessSettingMaximum), mScreenBrightnessSettingMinimum);
            screenAutoBrightnessAdjustment = Math.max(Math.min(
                    screenAutoBrightnessAdjustment, 1.0f), -1.0f);
            mDisplayPowerRequest.screenBrightness = screenBrightness;
            mDisplayPowerRequest.screenAutoBrightnessAdjustment =
                    screenAutoBrightnessAdjustment;
            mDisplayPowerRequest.useAutoBrightness = autoBrightness;
            // ++ [framework] to determine whether the screen brightness came from override
            mDisplayPowerRequest.useOverrideBrightness = overrideBrightness;
            // -- [framework] to determine whether the screen brightness came from override

            mDisplayPowerRequest.useProximitySensor = shouldUseProximitySensorLocked();

            mDisplayPowerRequest.blockScreenOn = mScreenOnBlocker.isHeld();

            mDisplayReady = mDisplayPowerController.requestPowerState(mDisplayPowerRequest,
                    mRequestWaitForNegativeProximity);
            mRequestWaitForNegativeProximity = false;

            if (DEBUG_SPEW) {
                Slog.d(TAG, "updateScreenStateLocked: mDisplayReady=" + mDisplayReady
                        + ", newScreenState=" + newScreenState
                        + ", mWakefulness=" + wakefulnessToString(mWakefulness)
                        + ", mWakeLockSummary=0x" + Integer.toHexString(mWakeLockSummary)
                        + ", mUserActivitySummary=0x" + Integer.toHexString(mUserActivitySummary)
                        + ", mBootCompleted=" + mBootCompleted);
            }
        }
    }

    private static boolean isValidBrightness(int value) {
        return value >= 0 && value <= 255;
    }

    private static boolean isValidAutoBrightnessAdjustment(float value) {
        // Handles NaN by always returning false.
        return value >= -1.0f && value <= 1.0f;
    }

    private int getDesiredScreenPowerStateLocked() {
        if (mWakefulness == WAKEFULNESS_ASLEEP) {
            return DisplayPowerRequest.SCREEN_STATE_OFF;
        }

        if ((mWakeLockSummary & WAKE_LOCK_SCREEN_BRIGHT) != 0
                || (mUserActivitySummary & USER_ACTIVITY_SCREEN_BRIGHT) != 0
                || !mBootCompleted) {
            return DisplayPowerRequest.SCREEN_STATE_BRIGHT;
        }

        return DisplayPowerRequest.SCREEN_STATE_DIM;
    }

    private final DisplayPowerController.Callbacks mDisplayPowerControllerCallbacks =
            new DisplayPowerController.Callbacks() {
        @Override
        public void onStateChanged() {
            // ++ [framework] Fix Google issue, synchronize lock required to call locked function.
            synchronized (mLock) {
            // -- [framework] Fix Google issue, synchronize lock required to call locked function.
                mDirty |= DIRTY_ACTUAL_DISPLAY_POWER_STATE_UPDATED;
                updatePowerStateLocked();
            }
        }

        @Override
        public void onProximityPositive() {
            // ++ [framework] Fix Google issue, synchronize lock required to call locked function.
            synchronized (mLock) {
            // -- [framework] Fix Google issue, synchronize lock required to call locked function.
                mProximityPositive = true;
                mDirty |= DIRTY_PROXIMITY_POSITIVE;
                updatePowerStateLocked();
            }
        }

        @Override
        public void onProximityNegative() {
            // ++ [framework] Fix Google issue, synchronize lock required to call locked function.
            synchronized (mLock) {
            // -- [framework] Fix Google issue, synchronize lock required to call locked function.
                mProximityPositive = false;
                mDirty |= DIRTY_PROXIMITY_POSITIVE;

                // ++ [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
                if (mDisplayPowerRequest != null && mDisplayPowerRequest.forceScreenOn) {
                    if (DEBUG_ON) {
                        Slog.d(TAG, "onProximityNegative: disable force screen on.");
                    }
                    mDisplayPowerRequest.forceScreenOn = false;
                }
                // ++ [framework] Workaround for M7 p-sensor problem.
                if (mWakefulness == WAKEFULNESS_ASLEEP) {
                    wakeUpNoUpdateLocked(SystemClock.uptimeMillis());
                } else {
                    userActivityNoUpdateLocked(SystemClock.uptimeMillis(),
                            PowerManager.USER_ACTIVITY_EVENT_OTHER, 0, Process.SYSTEM_UID);
                }
//                userActivityNoUpdateLocked(SystemClock.uptimeMillis(),
//                        PowerManager.USER_ACTIVITY_EVENT_OTHER, 0, Process.SYSTEM_UID);
                // -- [framework] Workaround for M7 p-sensor problem.
                // -- [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.

                updatePowerStateLocked();
            }
        }

        // ++ [framework] Workaround for M7 p-sensor problem.
        @Override
        public void onAngleDetectorTalkingDetected() {
            synchronized (mLock) {
                mADTalkingDetected = true;
            }
        }

        @Override
        public void onAngleDetectorReadingDetected() {
            synchronized (mLock) {
                mADTalkingDetected = false;
                // ++ [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
                if (mDisplayPowerRequest != null && mDisplayPowerRequest.forceScreenOn) {
                    if (DEBUG_ON) {
                        Slog.d(TAG, "onAngleDetectorReadingDetected: disable force screen on.");
                    }
                    mDisplayPowerRequest.forceScreenOn = false;
                }
                // -- [framework] Fix Google issue, allow screen on when p-sensor is always NEAR.
                if (mWakefulness == WAKEFULNESS_ASLEEP) {
                    wakeUpNoUpdateLocked(SystemClock.uptimeMillis());
                } else {
                    userActivityNoUpdateLocked(SystemClock.uptimeMillis(),
                            PowerManager.USER_ACTIVITY_EVENT_OTHER, 0, Process.SYSTEM_UID);
                }

                updatePowerStateLocked();
            }
        }
        // -- [framework] Workaround for M7 p-sensor problem.
    };

    private boolean shouldUseProximitySensorLocked() {
        return (mWakeLockSummary & WAKE_LOCK_PROXIMITY_SCREEN_OFF) != 0;
    }

    /**
     * Updates the suspend blocker that keeps the CPU alive.
     *
     * This function must have no other side-effects.
     */
    private void updateSuspendBlockerLocked() {
        boolean wantCpu = isCpuNeededLocked();
        if (wantCpu != mHoldingWakeLockSuspendBlocker) {
            mHoldingWakeLockSuspendBlocker = wantCpu;
            if (wantCpu) {
                if (DEBUG) {
                    Slog.d(TAG, "updateSuspendBlockerLocked: Acquiring suspend blocker.");
                }
                mWakeLockSuspendBlocker.acquire();
            } else {
                if (DEBUG) {
                    Slog.d(TAG, "updateSuspendBlockerLocked: Releasing suspend blocker.");
                }
                mWakeLockSuspendBlocker.release();
            }
        }
    }

    private boolean isCpuNeededLocked() {
        return !mBootCompleted
                // ++ [framework] Fix Google issue, p-sensor shouldn't keep CPU awake.
                || ((mWakeLockSummary & (~WAKE_LOCK_PROXIMITY_SCREEN_OFF)) != 0)
                || (!mProximityPositive && (mUserActivitySummary != 0))
                || (!mProximityPositive && (mDisplayPowerRequest.screenState != DisplayPowerRequest.SCREEN_STATE_OFF))
//                || mWakeLockSummary != 0
//                || mUserActivitySummary != 0
//                || mDisplayPowerRequest.screenState != DisplayPowerRequest.SCREEN_STATE_OFF
                // -- [framework] Fix Google issue, p-sensor shouldn't keep CPU awake.
                || !mDisplayReady;
    }

    // ++ [framework] Add helper method to check current state
    private boolean isScreenBright() {
        synchronized (mLock) {
            return !mSystemReady || mDisplayPowerRequest.screenState == DisplayPowerRequest.SCREEN_STATE_BRIGHT;
        }
    }
    // -- [framework] Add helper method to check current state

    @Override // Binder call
    public boolean isScreenOn() {
        final long ident = Binder.clearCallingIdentity();
        try {
            return isScreenOnInternal();
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    // ++ [framework] To determine actual screen state.
    @Override
    public boolean isActualScreenOn() {
        final long ident = Binder.clearCallingIdentity();
        try {
            return mActualScreenOn;
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }
    // -- [framework] To determine actual screen state.

    private boolean isScreenOnInternal() {
        synchronized (mLock) {
            return !mSystemReady
                    || mDisplayPowerRequest.screenState != DisplayPowerRequest.SCREEN_STATE_OFF;
        }
    }

    private void handleBatteryStateChangedLocked() {
        mDirty |= DIRTY_BATTERY_STATE;
        updatePowerStateLocked();
    }

    private void startWatchingForBootAnimationFinished() {
        mHandler.sendEmptyMessage(MSG_CHECK_IF_BOOT_ANIMATION_FINISHED);
    }

    private void checkIfBootAnimationFinished() {
        if (DEBUG) {
            Slog.d(TAG, "Check if boot animation finished...");
        }

        if (SystemService.isRunning(BOOT_ANIMATION_SERVICE)) {
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_IF_BOOT_ANIMATION_FINISHED,
                    BOOT_ANIMATION_POLL_INTERVAL);
            return;
        }

        synchronized (mLock) {
            if (!mBootCompleted) {
                Slog.i(TAG, "Boot animation finished.");
                handleBootCompletedLocked();
            }
        }

        //+[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02
        if (mHtcPowerSaver != null) {
            mHtcPowerSaver.bootCompletedNotify();
        }
        //-[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02

        // ++ [framework] Monitor wake lock behaviors
        // Start to dump wake lock records after booting is completed. Added by Charles Tsai
        if (mHtcWakeLockMonitor != null) {
            mHtcWakeLockMonitor.bootCompleted();
        }
        // -- [framework] Monitor wake lock behaviors
    }

    //[++framework] need to post ClearScreenTimeoutTask to clear once flag
    //[Important] Don't call this function in the other places.
    //Otherwise the screen will not be turn off by the timeout.
    private void handleClearScreenTimeoutTask(int timeout, int calcRemaining) {
        if (DEBUG_ON) {
            Slog.d(TAG, "handleClearScreenTimeoutTask ( " + timeout + " , " + calcRemaining + " )");
        }
        mHandler.removeCallbacks(mClearScreenTimeoutTask);
        mHandler.removeMessages(MSG_SEND_CLEAR_SCREEN_TIMEOUT_TASK);
        Message msg = mHandler.obtainMessage(MSG_SEND_CLEAR_SCREEN_TIMEOUT_TASK, timeout, calcRemaining);
        mHandler.sendMessageDelayed(msg, timeout);
        if (DEBUG_ON) {
            Slog.d(TAG, "handleClearScreenTimeoutTask end");
        }
    }

    //[Important] Please make sure the screen off timeout will happen earier than mClearScreenTimeoutTask.
    private void sendClearScreenTimeoutTask(int timeout, int calcRemaining) {
        if (DEBUG_ON) {
            Slog.d(TAG, "sendClearScreenTimeoutTask ( " + timeout + " , " + calcRemaining + " )");
            Slog.d(TAG, "Post Task Time at ( " + mLastUserActivityTime + " , " + SystemClock.uptimeMillis() + " )");
        }
        mHandler.removeCallbacks(mClearScreenTimeoutTask);
        mHandler.postDelayed(mClearScreenTimeoutTask, SEND_CLEAR_SCREEN_TIMEOUT_DELAY_VALUE);
        if (DEBUG_ON) {
            Slog.d(TAG, "sendClearScreenTimeoutTask end");
        }
    }
    //[--framework] need to post ClearScreenTimeoutTask to clear once flag

    private void handleBootCompletedLocked() {
        final long now = SystemClock.uptimeMillis();
        mBootCompleted = true;
        mDirty |= DIRTY_BOOT_COMPLETED;
        userActivityNoUpdateLocked(
                now, PowerManager.USER_ACTIVITY_EVENT_OTHER, 0, Process.SYSTEM_UID);
        updatePowerStateLocked();
    }

    /**
     * Reboots the device.
     *
     * @param confirm If true, shows a reboot confirmation dialog.
     * @param reason The reason for the reboot, or null if none.
     * @param wait If true, this call waits for the reboot to complete and does not return.
     */
    @Override // Binder call
    public void reboot(boolean confirm, String reason, boolean wait) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.REBOOT, null);

        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "reboot:"
                    + " confirm=" + confirm
                    + ", reason=" + reason
                    + ", wait=" + wait
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.

        final long ident = Binder.clearCallingIdentity();
        try {
            shutdownOrRebootInternal(false, confirm, reason, wait);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    /**
     * Shuts down the device.
     *
     * @param confirm If true, shows a shutdown confirmation dialog.
     * @param wait If true, this call waits for the shutdown to complete and does not return.
     */
    @Override // Binder call
    public void shutdown(boolean confirm, boolean wait) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.REBOOT, null);

        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "shutdown:"
                    + " confirm=" + confirm
                    + ", wait=" + wait
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.

        final long ident = Binder.clearCallingIdentity();
        try {
            shutdownOrRebootInternal(true, confirm, null, wait);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void shutdownOrRebootInternal(final boolean shutdown, final boolean confirm,
            final String reason, boolean wait) {
        if (mHandler == null || !mSystemReady) {
            throw new IllegalStateException("Too early to call shutdown() or reboot()");
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // ++ [framework] Add debug message.
                Slog.d(TAG, "shutdownOrRebootInternal: calling ShutdownThread."
                        + " confirm=" + confirm
                        + ", reason=" + reason);
                // -- [framework] Add debug message.
                synchronized (this) {
                    if (shutdown) {
                        ShutdownThread.shutdown(mContext, confirm);
                    } else {
                        ShutdownThread.reboot(mContext, reason, confirm);
                    }
                }
            }
        };

        //+ [framework] workaround for google potential issue: It might be deadlock in DevicePolicyManager when wipeDate called. Menghui, 2011/08/18
        HandlerThread rebootThread = new HandlerThread("PowerManagerService.reboot");
        rebootThread.start();
        Handler rebootHandler = new Handler(rebootThread.getLooper());
        //- [framework] workaround for google potential issue: It might be deadlock in DevicePolicyManager when wipeDate called. Menghui, 2011/08/18

        // ShutdownThread must run on a looper capable of displaying the UI.

        //+ [framework] workaround for google potential issue: It might be deadlock in DevicePolicyManager when wipeDate called. Menghui, 2011/08/18
        Message msg = Message.obtain(rebootHandler, runnable);
//        Message msg = Message.obtain(mHandler, runnable);
        //- [framework] workaround for google potential issue: It might be deadlock in DevicePolicyManager when wipeDate called. Menghui, 2011/08/18

        msg.setAsynchronous(true);

        //+ [framework] workaround for google potential issue: It might be deadlock in DevicePolicyManager when wipeDate called. Menghui, 2011/08/18
        rebootHandler.sendMessage(msg);
        Slog.d(TAG, "rebootHandler msg sent.");
//        mHandler.sendMessage(msg);
        //- [framework] workaround for google potential issue: It might be deadlock in DevicePolicyManager when wipeDate called. Menghui, 2011/08/18

        // PowerManager.reboot() is documented not to return so just wait for the inevitable.
        if (wait) {
            synchronized (runnable) {
                while (true) {
                    try {
                        // ++ [framework] Add debug message.
                        Slog.d(TAG, "runnable.wait()");
                        // -- [framework] Add debug message.
                        runnable.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    /**
     * Crash the runtime (causing a complete restart of the Android framework).
     * Requires REBOOT permission.  Mostly for testing.  Should not return.
     */
    @Override // Binder call
    public void crash(String message) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.REBOOT, null);

        final long ident = Binder.clearCallingIdentity();
        try {
            crashInternal(message);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void crashInternal(final String message) {
        Thread t = new Thread("PowerManagerService.crash()") {
            @Override
            public void run() {
                throw new RuntimeException(message);
            }
        };
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            Log.wtf(TAG, e);
        }
    }

    /**
     * Set the setting that determines whether the device stays on when plugged in.
     * The argument is a bit string, with each bit specifying a power source that,
     * when the device is connected to that source, causes the device to stay on.
     * See {@link android.os.BatteryManager} for the list of power sources that
     * can be specified. Current values include {@link android.os.BatteryManager#BATTERY_PLUGGED_AC}
     * and {@link android.os.BatteryManager#BATTERY_PLUGGED_USB}
     *
     * Used by "adb shell svc power stayon ..."
     *
     * @param val an {@code int} containing the bits that specify which power sources
     * should cause the device to stay on.
     */
    @Override // Binder call
    public void setStayOnSetting(int val) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.WRITE_SETTINGS, null);

        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "setStayOnSetting:"
                    + " val=" + val
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.

        final long ident = Binder.clearCallingIdentity();
        try {
            setStayOnSettingInternal(val);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void setStayOnSettingInternal(int val) {
        Settings.Global.putInt(mContext.getContentResolver(),
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN, val);
    }

    /**
     * Used by device administration to set the maximum screen off timeout.
     *
     * This method must only be called by the device administration policy manager.
     */
    @Override // Binder call
    public void setMaximumScreenOffTimeoutFromDeviceAdmin(int timeMs) {
        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "setMaximumScreenOffTimeoutFromDeviceAdmin:"
                    + " timeMs=" + timeMs
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.
        final long ident = Binder.clearCallingIdentity();
        try {
            setMaximumScreenOffTimeoutFromDeviceAdminInternal(timeMs);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void setMaximumScreenOffTimeoutFromDeviceAdminInternal(int timeMs) {
        synchronized (mLock) {
            mMaximumScreenOffTimeoutFromDeviceAdmin = timeMs;
            mDirty |= DIRTY_SETTINGS;
            updatePowerStateLocked();
        }
    }

    private boolean isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked() {
        return mMaximumScreenOffTimeoutFromDeviceAdmin >= 0
                && mMaximumScreenOffTimeoutFromDeviceAdmin < Integer.MAX_VALUE;
    }

    /**
     * Used by the phone application to make the attention LED flash when ringing.
     */
    @Override // Binder call
    public void setAttentionLight(boolean on, int color) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        // ++ [framework] Add Htc Log.
        if (DEBUG_ON) {
            Slog.d(TAG, "setAttentionLight:"
                    + " on=" + on
                    + ", color=" + color
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add Htc Log.

        final long ident = Binder.clearCallingIdentity();
        try {
            setAttentionLightInternal(on, color);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void setAttentionLightInternal(boolean on, int color) {
        LightsService.Light light;
        synchronized (mLock) {
            if (!mSystemReady) {
                return;
            }
            light = mAttentionLight;
        }

        // Control light outside of lock.
        light.setFlashing(color, LightsService.LIGHT_FLASH_HARDWARE, (on ? 3 : 0), 0);
    }

    /**
     * Used by the Watchdog.
     */
    public long timeSinceScreenWasLastOn() {
        synchronized (mLock) {
            if (mDisplayPowerRequest.screenState != DisplayPowerRequest.SCREEN_STATE_OFF) {
                return 0;
            }
            return SystemClock.elapsedRealtime() - mLastScreenOffEventElapsedRealTime;
        }
    }

    /**
     * Used by the window manager to override the screen brightness based on the
     * current foreground activity.
     *
     * This method must only be called by the window manager.
     *
     * @param brightness The overridden brightness, or -1 to disable the override.
     */
    public void setScreenBrightnessOverrideFromWindowManager(int brightness) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        // ++ [framework] Add debug logs to avoid other module use this API incorrectly
        if (DEBUG_ON) {
            Slog.d(TAG, "setScreenBrightnessOverrideFromWindowManager:"
                    + " brightness=" + brightness
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug logs to avoid other module use this API incorrectly

        final long ident = Binder.clearCallingIdentity();
        try {
            setScreenBrightnessOverrideFromWindowManagerInternal(brightness);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void setScreenBrightnessOverrideFromWindowManagerInternal(int brightness) {
        synchronized (mLock) {
            if (mScreenBrightnessOverrideFromWindowManager != brightness) {
                mScreenBrightnessOverrideFromWindowManager = brightness;
                mDirty |= DIRTY_SETTINGS;
                updatePowerStateLocked();
            }
        }
    }

    /**
     * Used by the window manager to override the button brightness based on the
     * current foreground activity.
     *
     * This method must only be called by the window manager.
     *
     * @param brightness The overridden brightness, or -1 to disable the override.
     */
    public void setButtonBrightnessOverrideFromWindowManager(int brightness) {
        // Do nothing.
        // Button lights are not currently supported in the new implementation.
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        // ++ [framework] Add debug logs to avoid other module use this API incorrectly
//        if (DEBUG_ON) {
//            Slog.d(TAG, "setButtonBrightnessOverrideFromWindowManager:"
//                    + " brightness=" + brightness
//                    + ", called by pid=" + Binder.getCallingPid()
//                    + ", uid=" + Binder.getCallingUid());
//        }
        // -- [framework] Add debug logs to avoid other module use this API incorrectly
    }

    /**
     * Used by the window manager to override the user activity timeout based on the
     * current foreground activity.  It can only be used to make the timeout shorter
     * than usual, not longer.
     *
     * This method must only be called by the window manager.
     *
     * @param timeoutMillis The overridden timeout, or -1 to disable the override.
     */
    public void setUserActivityTimeoutOverrideFromWindowManager(long timeoutMillis) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "setUserActivityTimeoutOverrideFromWindowManager:"
                    + " timeoutMillis=" + timeoutMillis
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.
        final long ident = Binder.clearCallingIdentity();
        try {
            setUserActivityTimeoutOverrideFromWindowManagerInternal(timeoutMillis);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void setUserActivityTimeoutOverrideFromWindowManagerInternal(long timeoutMillis) {
        synchronized (mLock) {
            if (mUserActivityTimeoutOverrideFromWindowManager != timeoutMillis) {
                mUserActivityTimeoutOverrideFromWindowManager = timeoutMillis;
                mDirty |= DIRTY_SETTINGS;
                updatePowerStateLocked();
            }
        }
    }

    /**
     * Used by the settings application and brightness control widgets to
     * temporarily override the current screen brightness setting so that the
     * user can observe the effect of an intended settings change without applying
     * it immediately.
     *
     * The override will be canceled when the setting value is next updated.
     *
     * @param brightness The overridden brightness.
     *
     * @see Settings.System#SCREEN_BRIGHTNESS
     */
    @Override // Binder call
    public void setTemporaryScreenBrightnessSettingOverride(int brightness) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "setTemporaryScreenBrightnessSettingOverrideInternal: API being called with brightness=" + brightness
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.

        final long ident = Binder.clearCallingIdentity();
        try {
            setTemporaryScreenBrightnessSettingOverrideInternal(brightness);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void setTemporaryScreenBrightnessSettingOverrideInternal(int brightness) {
        synchronized (mLock) {
            // ++ [framework] Google code logic refine. API may be called while screen is off, then virtual key will be turned on.
            // Skip if current state is off mode, or dim mode.
            final boolean isOffMode = mDisplayPowerRequest.screenState == DisplayPowerRequest.SCREEN_STATE_OFF;
            final boolean isDimMode = mDisplayPowerRequest.screenState == DisplayPowerRequest.SCREEN_STATE_DIM;
            if (isOffMode || isDimMode) {
//                Slog.d(TAG, "setBacklightBrightness: screen is off. Skip.");
                return;
            }
            // -- [framework] Google code logic refine. API may be called while screen is off, then virtual key will be turned on.

            if (mTemporaryScreenBrightnessSettingOverride != brightness) {
                mTemporaryScreenBrightnessSettingOverride = brightness;
                mDirty |= DIRTY_SETTINGS;
                updatePowerStateLocked();
            }
        }
    }

    /**
     * Used by the settings application and brightness control widgets to
     * temporarily override the current screen auto-brightness adjustment setting so that the
     * user can observe the effect of an intended settings change without applying
     * it immediately.
     *
     * The override will be canceled when the setting value is next updated.
     *
     * @param adj The overridden brightness, or Float.NaN to disable the override.
     *
     * @see Settings.System#SCREEN_AUTO_BRIGHTNESS_ADJ
     */
    @Override // Binder call
    public void setTemporaryScreenAutoBrightnessAdjustmentSettingOverride(float adj) {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        // ++ [framework] Add debug message.
        if (DEBUG_ON) {
            Slog.d(TAG, "setTemporaryScreenAutoBrightnessAdjustmentSettingOverride: API being called with adj=" + adj
                    + ", called by pid=" + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
        }
        // -- [framework] Add debug message.

        final long ident = Binder.clearCallingIdentity();
        try {
            setTemporaryScreenAutoBrightnessAdjustmentSettingOverrideInternal(adj);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    private void setTemporaryScreenAutoBrightnessAdjustmentSettingOverrideInternal(float adj) {
        synchronized (mLock) {
            // Note: This condition handles NaN because NaN is not equal to any other
            // value, including itself.
            if (mTemporaryScreenAutoBrightnessAdjustmentSettingOverride != adj) {
                mTemporaryScreenAutoBrightnessAdjustmentSettingOverride = adj;
                mDirty |= DIRTY_SETTINGS;
                updatePowerStateLocked();
            }
        }
    }

    /**
     * Low-level function turn the device off immediately, without trying
     * to be clean.  Most people should use {@link ShutdownThread} for a clean shutdown.
     */
    public static void lowLevelShutdown() {
        nativeShutdown();
    }

    /**
     * Low-level function to reboot the device.
     *
     * @param reason code to pass to the kernel (e.g. "recovery"), or null.
     * @throws IOException if reboot fails for some reason (eg, lack of
     *         permission)
     */
    public static void lowLevelReboot(String reason) throws IOException {
        nativeReboot(reason);
    }

    // ++ [framework] overload method to IPowerManager for HtcShutdownThread
    public void lowLevelShutdown_system() {
        int uid = Binder.getCallingUid();
        if (uid != Process.SYSTEM_UID) {
            Slog.d(TAG, "lowLevelShutdown_system: No one should call this API except HtcShutdownThread");
            return;
        }
        nativeShutdown();
    }

    public void lowLevelReboot_system(String reason) {
        int uid = Binder.getCallingUid();
        if (uid != Process.SYSTEM_UID) {
            Slog.d(TAG, "lowLevelReboot_system: No one should call this API except HtcShutdownThread");
            return;
        }
        try {
            nativeReboot(reason);
        } catch (Exception e) {}
    }
    // -- [framework] overload method to IPowerManager for HtcShutdownThread

    // ++ [framework] add efs sync for shutdown, add wait method prevent modem crash, menghui_wu, 2012/03/06
    public void lowLevelShutdownEFSSync() {
        int uid = Binder.getCallingUid();
        if (uid != Process.SYSTEM_UID) {
            Slog.d(TAG, "lowLevelShutdownEFSSync: No one should call this API except HtcShutdownThread");
            return;
        }
        nativeShutdownEFSSync();
    }

    public void lowLevelShutdownEFSSync_wait(int msTimeout) {
        int uid = Binder.getCallingUid();
        if (uid != Process.SYSTEM_UID) {
            Slog.d(TAG, "lowLevelShutdownEFSSync_wait: No one should call this API except HtcShutdownThread");
            return;
        }
        nativeShutdownEFSSync_wait(msTimeout);
    }
    // -- [framework] add efs sync for shutdown, add wait method prevent modem crash, menghui_wu, 2012/03/06

    // ++ [framework] Add system wipe API
    public boolean wipeSystem(byte[] imei, byte[] encMsg) {
        try {
            final int DEFAULT_S_ON = 1;
            final boolean IS_S_ON = (SystemProperties.getInt("ro.sf", DEFAULT_S_ON) != 0);

            if (IS_S_ON) {
                Slog.d(TAG, "wipeSystem: S_ON. Skip request.");
                return false;
            }

            if (imei == null || encMsg == null) {
                if (DEBUG_ON) {
                    Slog.d(TAG, "wipeSystem: Null found. imei=" + imei + ", encMsg=" + encMsg);
                }
                return false;
            }

            if (HtcBuildFlag.Htc_SECURITY_DEBUG_flag) {
                Slog.d(TAG, "wipeSystem: imei=" + new String(imei) + ", encMsg=" + new String(encMsg)
                        + ", called by pid=" + Binder.getCallingPid()
                        + ", uid=" + Binder.getCallingUid());
            }

            nativeWipeSystem(imei, encMsg);

            return true;
        } catch (Exception e) {
            if (DEBUG_ON) {
                Slog.d(TAG, "wipeSystem: imei=" + imei + ", encMsg=" + encMsg + ", Exception caught: " + e);
            }
        }
        return false;
    }
    // -- [framework] Add system wipe API

    @Override // Watchdog.Monitor implementation
    public void monitor() {
        // Grab and release lock for watchdog monitor to detect deadlocks.
        synchronized (mLock) {
        }
    }

    @Override // Binder call
    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (mContext.checkCallingOrSelfPermission(Manifest.permission.DUMP)
                != PackageManager.PERMISSION_GRANTED) {
            pw.println("Permission Denial: can't dump PowerManager from from pid="
                    + Binder.getCallingPid()
                    + ", uid=" + Binder.getCallingUid());
            return;
        }

        pw.println("POWER MANAGER (dumpsys power)\n");

        final DisplayPowerController dpc;
        final WirelessChargerDetector wcd;
        synchronized (mLock) {
            pw.println("Power Manager State:");
            pw.println("  mDirty=0x" + Integer.toHexString(mDirty));
            pw.println("  mWakefulness=" + wakefulnessToString(mWakefulness));
            pw.println("  mIsPowered=" + mIsPowered);
            pw.println("  mPlugType=" + mPlugType);
            pw.println("  mBatteryLevel=" + mBatteryLevel);
            pw.println("  mBatteryLevelWhenDreamStarted=" + mBatteryLevelWhenDreamStarted);
            pw.println("  mDockState=" + mDockState);
            pw.println("  mStayOn=" + mStayOn);
            pw.println("  mProximityPositive=" + mProximityPositive);
            pw.println("  mBootCompleted=" + mBootCompleted);
            pw.println("  mSystemReady=" + mSystemReady);
            pw.println("  mWakeLockSummary=0x" + Integer.toHexString(mWakeLockSummary));
            pw.println("  mUserActivitySummary=0x" + Integer.toHexString(mUserActivitySummary));
            pw.println("  mRequestWaitForNegativeProximity=" + mRequestWaitForNegativeProximity);
            pw.println("  mSandmanScheduled=" + mSandmanScheduled);
            pw.println("  mLastWakeTime=" + TimeUtils.formatUptime(mLastWakeTime));
            pw.println("  mLastSleepTime=" + TimeUtils.formatUptime(mLastSleepTime));
            pw.println("  mSendWakeUpFinishedNotificationWhenReady="
                    + mSendWakeUpFinishedNotificationWhenReady);
            pw.println("  mSendGoToSleepFinishedNotificationWhenReady="
                    + mSendGoToSleepFinishedNotificationWhenReady);
            pw.println("  mLastUserActivityTime=" + TimeUtils.formatUptime(mLastUserActivityTime));
            pw.println("  mLastUserActivityTimeNoChangeLights="
                    + TimeUtils.formatUptime(mLastUserActivityTimeNoChangeLights));
            pw.println("  mDisplayReady=" + mDisplayReady);
            pw.println("  mHoldingWakeLockSuspendBlocker=" + mHoldingWakeLockSuspendBlocker);
            //++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            pw.println("  mHoldingOOBESuspendBlocker=" + mHoldingOOBESuspendBlocker);
            //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower

            pw.println();
            pw.println("Settings and Configuration:");
            pw.println("  mDreamsSupportedConfig=" + mDreamsSupportedConfig);
            pw.println("  mDreamsEnabledSetting=" + mDreamsEnabledSetting);
            pw.println("  mDreamsActivateOnSleepSetting=" + mDreamsActivateOnSleepSetting);
            pw.println("  mDreamsActivateOnDockSetting=" + mDreamsActivateOnDockSetting);
            pw.println("  mScreenOffTimeoutSetting=" + mScreenOffTimeoutSetting);
            pw.println("  mMaximumScreenOffTimeoutFromDeviceAdmin="
                    + mMaximumScreenOffTimeoutFromDeviceAdmin + " (enforced="
                    + isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked() + ")");
            pw.println("  mStayOnWhilePluggedInSetting=" + mStayOnWhilePluggedInSetting);
            pw.println("  mScreenBrightnessSetting=" + mScreenBrightnessSetting);
            pw.println("  mScreenAutoBrightnessAdjustmentSetting="
                    + mScreenAutoBrightnessAdjustmentSetting);
            pw.println("  mScreenBrightnessModeSetting=" + mScreenBrightnessModeSetting);
            pw.println("  mScreenBrightnessOverrideFromWindowManager="
                    + mScreenBrightnessOverrideFromWindowManager);
            pw.println("  mUserActivityTimeoutOverrideFromWindowManager="
                    + mUserActivityTimeoutOverrideFromWindowManager);
            // ++ [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.
            pw.println("  mScreenTimeout_Once=" + mScreenTimeout_Once
                    + " mScreenTimeout_Always=" + mScreenTimeout_Always
                    + " mScreenTimeout_System=" + mScreenTimeout_System);
            // -- [framework] for customize screen timeout, including phone off hook, keyguard at hide, smart dim.
            pw.println("  mTemporaryScreenBrightnessSettingOverride="
                    + mTemporaryScreenBrightnessSettingOverride);
            pw.println("  mTemporaryScreenAutoBrightnessAdjustmentSettingOverride="
                    + mTemporaryScreenAutoBrightnessAdjustmentSettingOverride);
            pw.println("  mScreenBrightnessSettingMinimum=" + mScreenBrightnessSettingMinimum);
            pw.println("  mScreenBrightnessSettingMaximum=" + mScreenBrightnessSettingMaximum);
            pw.println("  mScreenBrightnessSettingDefault=" + mScreenBrightnessSettingDefault);

            final int screenOffTimeout = getScreenOffTimeoutLocked();
            final int screenDimDuration = getScreenDimDurationLocked(screenOffTimeout);
            pw.println();
            pw.println("Screen off timeout: " + screenOffTimeout + " ms");
            pw.println("Screen dim duration: " + screenDimDuration + " ms");

            pw.println();
            pw.println("Wake Locks: size=" + mWakeLocks.size());
            for (WakeLock wl : mWakeLocks) {
                pw.println("  " + wl);
            }

            // HTC start
            pw.println();
            pw.println("HtcCpuCtrls: size=" + mHtcCpuCtrls.size());
            pw.println("  mCpuFreqMinCount=" + mCpuFreqMinCount);
            pw.println("  mCpuFreqMaxCount=" + mCpuFreqMaxCount);
            pw.println("  mCpuNumMinCount=" + mCpuNumMinCount);
            pw.println("  mCpuNumMaxCount=" + mCpuNumMaxCount);
            for (HtcCpuCtrl hcc : mHtcCpuCtrls) {
                pw.println("  " + hcc);
            }
            // HTC end

            pw.println();
            pw.println("Suspend Blockers: size=" + mSuspendBlockers.size());
            for (SuspendBlocker sb : mSuspendBlockers) {
                pw.println("  " + sb);
            }

            pw.println();
            pw.println("Screen On Blocker: " + mScreenOnBlocker);

            pw.println();
            pw.println("Display Blanker: " + mDisplayBlanker);

            dpc = mDisplayPowerController;
            wcd = mWirelessChargerDetector;
        }

        if (dpc != null) {
            dpc.dump(pw);
        }

        if (wcd != null) {
            wcd.dump(pw);
        }
    }

    private SuspendBlocker createSuspendBlockerLocked(String name) {
        SuspendBlocker suspendBlocker = new SuspendBlockerImpl(name);
        mSuspendBlockers.add(suspendBlocker);
        return suspendBlocker;
    }

    private static String wakefulnessToString(int wakefulness) {
        switch (wakefulness) {
            case WAKEFULNESS_ASLEEP:
                return "Asleep";
            case WAKEFULNESS_AWAKE:
                return "Awake";
            case WAKEFULNESS_DREAMING:
                return "Dreaming";
            case WAKEFULNESS_NAPPING:
                return "Napping";
            default:
                return Integer.toString(wakefulness);
        }
    }

    private static WorkSource copyWorkSource(WorkSource workSource) {
        return workSource != null ? new WorkSource(workSource) : null;
    }

    private final class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (mLock) {
                handleBatteryStateChangedLocked();

                //+[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02
                if (mHtcPowerSaver != null) {
                    mHtcPowerSaver.updateBatteryInfo(intent);
                    mPowerSaverHandler.removeCallbacks(runHtcPowerSaverCheck);
                    mPowerSaverHandler.post(runHtcPowerSaverCheck);
                }
                //-[framework] PowerSaver 3.0 v0.76 for Sense 3.5, menghui_wu, 2011/12/02
            }
        }
    }

    private final class BootCompletedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // This is our early signal that the system thinks it has finished booting.
            // However, the boot animation may still be running for a few more seconds
            // since it is ultimately in charge of when it terminates.
            // Defer transitioning into the boot completed state until the animation exits.
            // We do this so that the screen does not start to dim prematurely before
            // the user has actually had a chance to interact with the device.
            //++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            startWatchingForOOBEStatusFinished();
            //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            startWatchingForBootAnimationFinished();
        }
    }

    private final class DreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (mLock) {
                scheduleSandmanLocked();
            }
        }
    }

    private final class UserSwitchedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (mLock) {
                handleSettingsChangedLocked();
            }
        }
    }

    private final class DockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (mLock) {
                int dockState = intent.getIntExtra(Intent.EXTRA_DOCK_STATE,
                        Intent.EXTRA_DOCK_STATE_UNDOCKED);
                if (mDockState != dockState) {
                    mDockState = dockState;
                    mDirty |= DIRTY_DOCK_STATE;
                    updatePowerStateLocked();
                }
                //+ Greg Tsai (2011/06/21), support new desk dock behaviors: keep screen on when desk mode is on with charging.
                if (mbEnableDeskDockBehavior) {
                    boolean wasDeskModeEnabled = mDeskModeEnabled;
                    // if receiving the dock intent from H/W or the launching intent for desk dock AP (enter desk dock mode by tapping AP directly),
                    // record that device is in the desk dock mode.
                    mDeskModeEnabled = (dockState == (Intent.EXTRA_DOCK_STATE_DESK) ||
            		        (intent.getAction().compareTo(UiModeManager.ACTION_ENTER_DESK_MODE)==0));
                    if (mDeskModeEnabled != wasDeskModeEnabled) {
                        // When the status of desk dock mode is changed, recalculate everything and reset timer
                        recalculateScreenTimeout();
                        userActivity(SystemClock.uptimeMillis(), PowerManager.USER_ACTIVITY_EVENT_OTHER, 0);
                    }
                }
                //- Greg Tsai (2011/06/21).
            }
        }
    }

    private final class SettingsObserver extends ContentObserver {
        public SettingsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            synchronized (mLock) {
                handleSettingsChangedLocked();
            }
        }
    }

    /**
     * Handler for asynchronous operations performed by the power manager.
     */
    private final class PowerManagerHandler extends Handler {
        public PowerManagerHandler(Looper looper) {
            super(looper, null, true /*async*/);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_USER_ACTIVITY_TIMEOUT:
                    handleUserActivityTimeout();
                    break;
                case MSG_SANDMAN:
                    handleSandman();
                    break;
                case MSG_SCREEN_ON_BLOCKER_RELEASED:
                    handleScreenOnBlockerReleased();
                    break;
                case MSG_CHECK_IF_BOOT_ANIMATION_FINISHED:
                    checkIfBootAnimationFinished();
                    break;
                //[++framework] need to post ClearScreenTimeoutTask to clear once flag
                case MSG_SEND_CLEAR_SCREEN_TIMEOUT_TASK:
                    sendClearScreenTimeoutTask(msg.arg1 ,msg.arg2);
                    break;
                //[--framework] need to post ClearScreenTimeoutTask to clear once flag
                //++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
                case MSG_CHECK_IF_OOBE_STATUS_FINISHED:
                    checkIfOOBEStatusFinished(false);
                    break;
                case MSG_CHECK_IF_OOBE_STATUS_FINISHED_TIMEOUT:
                    if (checkIfOOBEStatusFinished(true) == false) {
                        startShutdownDevice();
                    }
                    if (mHoldingOOBESuspendBlocker) {
                        mOOBESuspendBlocker.release();
                        mHoldingOOBESuspendBlocker = false;
                    }
                    break;
                //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            }
        }
    }

    // HTC start
    private boolean isPerfLevelLock(int flags) {
        int n = flags & CPU_LOCK_MASK;
        return n == PowerManager.CPU_MAX_FREQ
            || n == PowerManager.CPU_MIN_FREQ
            || n == PowerManager.CPU_MAX_NUM
            || n == PowerManager.CPU_MIN_NUM;
    }
    // HTC end

    /**
     * Represents a wake lock that has been acquired by an application.
     */
    private final class WakeLock implements IBinder.DeathRecipient {
        public final IBinder mLock;
        public int mFlags;
        public String mTag;
        public WorkSource mWorkSource;
        public int mOwnerUid;
        public int mOwnerPid;

        public WakeLock(IBinder lock, int flags, String tag, WorkSource workSource,
                int ownerUid, int ownerPid) {
            mLock = lock;
            mFlags = flags;
            mTag = tag;
            mWorkSource = copyWorkSource(workSource);
            mOwnerUid = ownerUid;
            mOwnerPid = ownerPid;
        }

        @Override
        public void binderDied() {
            PowerManagerService.this.handleWakeLockDeath(this);
        }

        public boolean hasSameProperties(int flags, String tag, WorkSource workSource,
                int ownerUid, int ownerPid) {
            return mFlags == flags
                    && mTag.equals(tag)
                    && hasSameWorkSource(workSource)
                    && mOwnerUid == ownerUid
                    && mOwnerPid == ownerPid;
        }

        public void updateProperties(int flags, String tag, WorkSource workSource,
                int ownerUid, int ownerPid) {
            mFlags = flags;
            mTag = tag;
            updateWorkSource(workSource);
            mOwnerUid = ownerUid;
            mOwnerPid = ownerPid;
        }

        public boolean hasSameWorkSource(WorkSource workSource) {
            return Objects.equal(mWorkSource, workSource);
        }

        public void updateWorkSource(WorkSource workSource) {
            mWorkSource = copyWorkSource(workSource);
        }

        @Override
        public String toString() {
            return getLockLevelString()
                    + " '" + mTag + "'" + getLockFlagsString()
                    + " (uid=" + mOwnerUid + ", pid=" + mOwnerPid + ", ws=" + mWorkSource + ")";
        }

        private String getLockLevelString() {
            switch (mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) {
                case PowerManager.FULL_WAKE_LOCK:
                    return "FULL_WAKE_LOCK                ";
                case PowerManager.SCREEN_BRIGHT_WAKE_LOCK:
                    return "SCREEN_BRIGHT_WAKE_LOCK       ";
                case PowerManager.SCREEN_DIM_WAKE_LOCK:
                    return "SCREEN_DIM_WAKE_LOCK          ";
                case PowerManager.PARTIAL_WAKE_LOCK:
                    return "PARTIAL_WAKE_LOCK             ";
                case PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK:
                    return "PROXIMITY_SCREEN_OFF_WAKE_LOCK";
                default:
                    // ++ [framework] Show flags
//                    return "???                           ";
                    return "mFlags=" + Integer.toHexString(mFlags) + "               ";
                    // -- [framework] Show flags
            }
        }

        private String getLockFlagsString() {
            String result = "";
            if ((mFlags & PowerManager.ACQUIRE_CAUSES_WAKEUP) != 0) {
                result += " ACQUIRE_CAUSES_WAKEUP";
            }
            if ((mFlags & PowerManager.ON_AFTER_RELEASE) != 0) {
                result += " ON_AFTER_RELEASE";
            }
            return result;
        }
    }

    // ++ [framework] For CPU performance controlling mechanism.
    private class HtcCpuCtrl implements IBinder.DeathRecipient {
        public final IBinder mLock;
        public int mFlags;
        public String mTag;
        public WorkSource mWorkSource;
        public int mOwnerUid;
        public int mOwnerPid;

        private int mLevel;

        public HtcCpuCtrl(IBinder lock, int flags, String tag, WorkSource workSource, int ownerUid, int ownerPid, int level) {
            mLock = lock;
            mFlags = flags;
            mTag = tag;
            mWorkSource = copyWorkSource(workSource);
            mOwnerUid = ownerUid;
            mOwnerPid = ownerPid;

            mLevel = level;
        }

        public int getLevel() {
            return mLevel;
        }

        @Override
        public void binderDied() {
            PowerManagerService.this.handleHtcCpuCtrlDeath(this);
        }

        public boolean hasSameProperties(int flags, String tag, WorkSource workSource,
                int ownerUid, int ownerPid) {
            return mFlags == flags
                    && mTag.equals(tag)
                    && hasSameWorkSource(workSource)
                    && mOwnerUid == ownerUid
                    && mOwnerPid == ownerPid;
        }

        public void updateProperties(int flags, String tag, WorkSource workSource,
                int ownerUid, int ownerPid) {
            mFlags = flags;
            mTag = tag;
            updateWorkSource(workSource);
            mOwnerUid = ownerUid;
            mOwnerPid = ownerPid;
        }

        public boolean hasSameWorkSource(WorkSource workSource) {
            return Objects.equal(mWorkSource, workSource);
        }

        public void updateWorkSource(WorkSource workSource) {
            mWorkSource = copyWorkSource(workSource);
        }

        @Override
        public String toString() {
            return getLockLevelString()
                    // ++ [framework] Add dumpLevelIfExist() to show HtcCpuCtrl levels
                    + " '" + mTag + "'" + dumpLevelIfExist()
                    // -- [framework] Add dumpLevelIfExist() to show HtcCpuCtrl levels
                    + " (uid=" + mOwnerUid + ", pid=" + mOwnerPid + ", ws=" + mWorkSource + ")";
        }

        private String getLockLevelString() {
            switch (mFlags & PowerManager.WAKE_LOCK_LEVEL_MASK) {
                case PowerManager.CPU_PERF_WAKE_LOCK:
                    return "CPU_PERF_WAKE_LOCK            ";
                case PowerManager.CPU_MAX_FREQ:
                    return "CPU_MAX_FREQ                  ";
                case PowerManager.CPU_MIN_FREQ:
                    return "CPU_MIN_FREQ                  ";
                case PowerManager.CPU_MAX_NUM:
                    return "CPU_MAX_NUM                   ";
                case PowerManager.CPU_MIN_NUM:
                    return "CPU_MIN_NUM                   ";
                default:
                    return "mFlags=" + Integer.toHexString(mFlags) + "               ";
            }
        }

        private String dumpLevelIfExist() {
            if ((mFlags & CPU_LOCK_MASK) != 0) {
                try {
                    switch (mFlags & CPU_LOCK_MASK) {
                        case PowerManager.CPU_MAX_FREQ:
                        case PowerManager.CPU_MIN_FREQ:
                            return ", Level=" + cpuFreqLevelToString();
                        case PowerManager.CPU_MAX_NUM:
                        case PowerManager.CPU_MIN_NUM:
                            return ", Level=" + cpuNumLevelToString();
                    }
                } catch (Exception e) {
                    Slog.d(TAG, "dumpLevelIfExist: Skip. mFlags=" + mFlags + ", mTag=" + mTag, e);
                    return "";
                }
            }
            return "";
        }

        public String cpuFreqLevelToString() {
            switch (mLevel) {
                case PowerManager.CPU_FREQ_LOWEST:
                    return "CPU_FREQ_LOWEST";
                case PowerManager.CPU_FREQ_LOW:
                    return "CPU_FREQ_LOW";
                case PowerManager.CPU_FREQ_MEDIUM:
                    return "CPU_FREQ_MEDIUM";
                case PowerManager.CPU_FREQ_HIGH:
                    return "CPU_FREQ_HIGH";
                case PowerManager.CPU_FREQ_HIGHEST:
                    return "CPU_FREQ_HIGHEST";
                default:
                    break;
            }
            return "Unknown cpu freq level";
        }

        public String cpuNumLevelToString() {
            switch (mLevel) {
                case PowerManager.CPU_NUM_SINGLE:
                    return "CPU_NUM_SINGLE";
                case PowerManager.CPU_NUM_DUAL:
                    return "CPU_NUM_DUAL";
                case PowerManager.CPU_NUM_TRIPLE:
                    return "CPU_NUM_TRIPLE";
                case PowerManager.CPU_NUM_QUAD:
                    return "CPU_NUM_QUAD";
                default:
                    break;
            }
            return "Unknown cpu number level";
        }
    }
    // -- [framework] For CPU performance controlling mechanism.

    private final class SuspendBlockerImpl implements SuspendBlocker {
        private final String mName;
        private int mReferenceCount;

        public SuspendBlockerImpl(String name) {
            mName = name;
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                if (mReferenceCount != 0) {
                    Log.wtf(TAG, "Suspend blocker \"" + mName
                            + "\" was finalized without being released!");
                    mReferenceCount = 0;
                    nativeReleaseSuspendBlocker(mName);
                }
            } finally {
                super.finalize();
            }
        }

        @Override
        public void acquire() {
            synchronized (this) {
                mReferenceCount += 1;
                if (mReferenceCount == 1) {
                    nativeAcquireSuspendBlocker(mName);
                }
            }
        }

        @Override
        public void release() {
            synchronized (this) {
                mReferenceCount -= 1;
                if (mReferenceCount == 0) {
                    nativeReleaseSuspendBlocker(mName);
                } else if (mReferenceCount < 0) {
                    Log.wtf(TAG, "Suspend blocker \"" + mName
                            + "\" was released without being acquired!", new Throwable());
                    mReferenceCount = 0;
                }
            }
        }

        @Override
        public String toString() {
            synchronized (this) {
                return mName + ": ref count=" + mReferenceCount;
            }
        }
    }

    private final class ScreenOnBlockerImpl implements ScreenOnBlocker {
        private int mNestCount;

        public boolean isHeld() {
            synchronized (this) {
                return mNestCount != 0;
            }
        }

        @Override
        public void acquire() {
            synchronized (this) {
                mNestCount += 1;
                if (DEBUG) {
                    Slog.d(TAG, "Screen on blocked: mNestCount=" + mNestCount);
                }
            }
        }

        @Override
        public void release() {
            synchronized (this) {
                mNestCount -= 1;
                if (mNestCount < 0) {
                    Log.wtf(TAG, "Screen on blocker was released without being acquired!",
                            new Throwable());
                    mNestCount = 0;
                }
                if (mNestCount == 0) {
                    mHandler.sendEmptyMessage(MSG_SCREEN_ON_BLOCKER_RELEASED);
                }
                if (DEBUG) {
                    Slog.d(TAG, "Screen on unblocked: mNestCount=" + mNestCount);
                }
            }
        }

        @Override
        public String toString() {
            synchronized (this) {
                return "held=" + (mNestCount != 0) + ", mNestCount=" + mNestCount;
            }
        }
    }

    private final class DisplayBlankerImpl implements DisplayBlanker {
        private boolean mBlanked;

        @Override
        public void blankAllDisplays() {
            synchronized (this) {
                // ++ [framework] To determine actual screen state.
                mActualScreenOn = false;
                // -- [framework] To determine actual screen state.

                mBlanked = true;
                mDisplayManagerService.blankAllDisplaysFromPowerManager();

                // ++ [framework] Monitor nativeSetInteractive
                Slog.d(TAG, "nativeSetInteractive: false...");
                // -- [framework] Monitor nativeSetInteractive
                nativeSetInteractive(false);
                // ++ [framework] Monitor nativeSetInteractive
                Slog.d(TAG, "nativeSetInteractive: false done.");
                // -- [framework] Monitor nativeSetInteractive

                // ++ [framework] Monitor nativeSetAutoSuspend
                Slog.d(TAG, "nativeSetAutoSuspend: true...");
                // -- [framework] Monitor nativeSetAutoSuspend
                nativeSetAutoSuspend(true);
                // ++ [framework] Monitor nativeSetAutoSuspend
                Slog.d(TAG, "nativeSetAutoSuspend: true done");
                // -- [framework] Monitor nativeSetAutoSuspend

            }
            // ++ [framework] Helper function to collect all htc modules which require screen state notify.
            screenStateNotifier(false);
            // -- [framework] Helper function to collect all htc modules which require screen state notify.

            //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            if (checkIfOOBEStatusFinished(false) == false) {
                startMonitorOOBEStatus();
            } else {
                clearMonitorOOBEStatus();
            }
            //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower

            //[++framework] Add new API for HtcShutdownThread, peter_li 20130426 
            synchronized (mShutdownLock) {

                if (startGoToSleepByShutdown) {
                    doneNativeSetAutoSuspend = true;

                    if (mCondition != null) {
                        if (DEBUG_ON) {
                            Slog.d(TAG, "open condition block");
                        }
                        mCondition.open();
                    }

                }
            }
            //[--framework] Add new API for HtcShutdownThread, peter_li 20130426 

        }

        @Override
        public void unblankAllDisplays() {
            synchronized (this) {

                // ++ [framework] Monitor nativeSetAutoSuspend
                Slog.d(TAG, "nativeSetAutoSuspend: false...");
                // -- [framework] Monitor nativeSetAutoSuspend
                nativeSetAutoSuspend(false);
                // ++ [framework] Monitor nativeSetAutoSuspend
                Slog.d(TAG, "nativeSetAutoSuspend: false done");
                // -- [framework] Monitor nativeSetAutoSuspend

                // ++ [framework] Monitor nativeSetInteractive
                Slog.d(TAG, "nativeSetInteractive: true ...");
                // -- [framework] Monitor nativeSetInteractive
                nativeSetInteractive(true);
                // ++ [framework] Monitor nativeSetInteractive
                Slog.d(TAG, "nativeSetInteractive: true done.");
                // -- [framework] Monitor nativeSetInteractive

                mDisplayManagerService.unblankAllDisplaysFromPowerManager();
                mBlanked = false;

                // ++ [framework] To determine actual screen state.
                mActualScreenOn = true;
                // -- [framework] To determine actual screen state.
            }
            // ++ [framework] Helper function to collect all htc modules which require screen state notify.
            screenStateNotifier(true);
            // -- [framework] Helper function to collect all htc modules which require screen state notify.

            //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
            clearMonitorOOBEStatus();
            //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower

        }

        @Override
        public String toString() {
            synchronized (this) {
                return "blanked=" + mBlanked;
            }
        }

        // ++ [framework] Helper function to collect all htc modules which require screen state notify.
        private void screenStateNotifier(boolean on) {
            // ++ [framework] Notify actual screen state.
            if (mHtcPMSExtension != null) {
                mHtcPMSExtension.notifyActualScreenState(on);
            }
            // -- [framework] Notify actual screen state.

            // ++ [framework] Write screen state to file.
            if (mHtcPMSExtension != null) {
                mHtcPMSExtension.writeScreenState(on);
            }
            // -- [framework] Write screen state to file.

            // ++ [framework] Monitor wake lock behavior
            if (mHtcWakeLockMonitor != null) {
                mHtcWakeLockMonitor.screenState(on);
            }
            // -- [framework] Monitor wake lock behavior

            // ++ [framework] Notify HtcAutoBrightnessCtrl screen is turned off.
            if (mDisplayPowerController != null) {
                HtcAutoBrightnessCtrl habc = mDisplayPowerController.getHtcAutoBrightnessCtrl();
                if (habc != null) {
                    habc.screenState(on);
                }
            }
            // -- [framework] Notify HtcAutoBrightnessCtrl screen is turned off.
        }
        // -- [framework] Helper function to collect all htc modules which require screen state notify.
    }

    // HTC UI - BEGIN
    //HTC: Sync CL113909 ++
    // For NightClock AP, Eric.SP Huang 2010/01/06, HTC: Sync CL113924
/*
    private boolean mNightMode = false;

    public void setNightMode(int nightmode) {
        Slog.d(TAG, "setNightMode:"
                + " nightmode=" + nightmode
                + ", pid=" + Binder.getCallingPid()
                + ", uid=" + Binder.getCallingUid());
        mNightMode = (nightmode == 1) ? true : false;
        //HTC: Sync CL114011 ++
        int value;
        if (mNightMode) {
            int brightnessMode = (mUseSoftwareAutoBrightness
                    ? LightsService.BRIGHTNESS_MODE_SENSOR
                    : LightsService.BRIGHTNESS_MODE_USER);
            mButtonLight.setBrightness(0, brightnessMode);
            // Device flags removed(ChaCha & Icon#G), assign false for new projects
            if (false) {
                mSharekeyBacklight.setBrightness(0, brightnessMode);
            }
        } else {
            // force recompute of backlight values
            if (mLightSensorValue >= 0) {
                value = (int) mLightSensorValue;
                mLightSensorValue = -1;
                lightSensorChangedLocked(value, false);
            }
        }
        //HTC: Sync CL114011 --
    }
*/
    //HTC: Sync CL113909 --
    // HTC UI - END

    /// HTC: trigger watchdog timeout
    /**
     * Trigger watchdog timeout.
     * Requires REBOOT permission.  For testing purpose only.
     *
     * @hide
     */
    public void watchdog() {
        mContext.enforceCallingOrSelfPermission(android.Manifest.permission.REBOOT, null);
        new Thread("PowerManagerService.watchdog()") {
            public void run() {
                Log.d(TAG, "PowerManagerService.watchdog()");
                synchronized (mLock) {
                    try {
                        Thread.sleep(5 * 60 * 1000);
                    } catch (InterruptedException e) {
                        Log.wtf(TAG, e);
                    }
                }
            }
        }.start();
    }

// Should not be needed by anyone now.  No result found when search entire code base, and sense 412 HtcIfPowerManager is removed.
    // ++ HTC:
    // Ported from Gingerbread: enable this method in Honeycomb.
    // Added by Charles Tsai, 2011.04.21.
//    public void setAutoBrightnessOffset(int offset, String appName) {
//        Slog.d(TAG, "Brightness offset is set to " + offset + " by " + appName);
//        mAutoBrightnessOffset = offset;
//        lightSensorChangedLocked(previous_lsvalue, false, true, -1);
//    }
    // -- HTC

    // ++ [framework] For timeout setting
    private void recalculateScreenTimeout() {
        synchronized (mLock) {
            if (isScreenBright() && mWakefulness != WAKEFULNESS_ASLEEP) {
                mDirty |= DIRTY_SETTINGS;
                updatePowerStateLocked();
            }
        }
    }
    // -- [framework] For timeout setting

    // ++ [framework] called by HtcShutdownThread
    public void setAblActive(int on) {
        try {
            LightsService.Light backlight = mLightsService.getLight(LightsService.LIGHT_ID_BACKLIGHT);
            backlight.setAblActive(on);
        } catch (Exception e) {
            if (DEBUG_ON) {
                Slog.d(TAG, "setAblActive: Exception caught. " + e);
            }
        }
    }
    // -- [framework] called by HtcShutdownThread

    //++[framework] add API to get Current Brightness Value, peter li 20120419
    public int fetchCurrentBrightnessValue() {
        if (mDisplayPowerController != null) {
            return mDisplayPowerController.fetchCurrentBrightnessValue();
        }
        return 255;
    }
    //--[framework] add API to get Current Brightness Value, peter li 20120419

    // ++ [HTC_PHONE] e: htc shawn
    public boolean getProximitySensorActive() {
        if (mDisplayPowerController != null) {
            return mDisplayPowerController.getProximitySensorActive();
        }
        return false;
    }
    // -- [HTC_PHONE] e: htc shawn

    // ++ [framework] for Sense 5 Car App
    public int[] getLightSensorTableValues() {
        if (mDisplayPowerController != null) {
            return mDisplayPowerController.getLightSensorTableValues();
        }
        return null;
    }
    // -- [framework] for Sense 5 Car App

    // HTC UI - BEGIN, called by WindowManagerService.
    //+htc: Set button backlight orientation
    public static final int PEN_BUTTON_AMBER = 0x1;
    public static final int PEN_BUTTON_GREEN = 0x2;
    public static final int PEN_BUTTON_BLUE = 0x4;

    public void setPenMenuButtonLed(int penButtonLed) {
        if (mHtcPMSExtension != null && mDisplayPowerController != null) {
            HtcAutoBrightnessCtrl habc = mDisplayPowerController.getHtcAutoBrightnessCtrl();

            int buttonValue = 0;
            if (habc != null) {
                final int LUX = Math.round(mDisplayPowerController.getAmbientLux());
                buttonValue = habc.getButtonValue(LUX);
            }

            mHtcPMSExtension.updatePenMenuButtonLed(penButtonLed, buttonValue, isScreenOn());
        }
    }

    public void setOrientation(int orientation) {
        if (mHtcPMSExtension != null && mDisplayPowerController != null) {
            HtcAutoBrightnessCtrl habc = mDisplayPowerController.getHtcAutoBrightnessCtrl();

            int buttonValue = 0;
            if (habc != null) {
                final int LUX = Math.round(mDisplayPowerController.getAmbientLux());
                buttonValue = habc.getButtonValue(LUX);
            }

            mHtcPMSExtension.setOrientation(orientation, buttonValue, isScreenOn());
        }
    }
    //-htc: Set button backlight orientation
    // HTC UI - END

/*
    //+[framework] for DVCS wakelock, menghui_wu, 2011/09/27
    private int acquireHtcDVCSWakeLockLocked(String tag) {
        if (tag.startsWith(PowerManager.HTC_DVCS_PREFIX)) {
            htcDVCSWakeLockCount++;
            if (htcDVCSWakeLockCount == 1) {
                nativeAcquireCpuApDvcsWakeLock();
                Slog.d(TAG, "acquireHtcDVCSWakeLockLocked: nativeAcquireCpuApDvcsWakeLock called");
            }
            Slog.d(TAG, "acquireHtcDVCSWakeLockLocked: htcDVCSWakeLockCount=" + htcDVCSWakeLockCount);
        }
        return htcDVCSWakeLockCount;
    }

    private int releaseHtcDVCSWakeLockLocked(String tag) {
        if (tag.startsWith(PowerManager.HTC_DVCS_PREFIX)) {
            htcDVCSWakeLockCount--;
            if (htcDVCSWakeLockCount > 0) {
                Slog.d(TAG, "releaseHtcDVCSWakeLockLocked: htcDVCSWakeLockCount=" + htcDVCSWakeLockCount);
            } else if (htcDVCSWakeLockCount == 0) {
                nativeReleaseCpuApDvcsWakeLock();
                Slog.d(TAG, "releaseHtcDVCSWakeLockLocked: nativeReleaseCpuApDvcsWakeLock called");
            } else {
                Slog.d(TAG, "Wrong DVCS count: htcDVCSWakeLockCount=" + htcDVCSWakeLockCount);
                // restore, force to release DVCS
                nativeReleaseCpuApDvcsWakeLock();
                htcDVCSWakeLockCount = 0;
            }
        }
        return htcDVCSWakeLockCount;
    }
    //-[framework] for DVCS wakelock, menghui_wu, 2011/09/27

    // ++ [framework] API for HtcPowerSaver-ReduceCPU to fix DVCS wake lock causes CPU can not suspend issue.
    private void enableCpuApDvcs(boolean enable) {
        synchronized (mLocks) {
            Slog.d(TAG, "enableCpuApDvcs: enable=" + enable + ", htcDVCSWakeLockCount=" + htcDVCSWakeLockCount);
            if (enable) {
                htcDVCSWakeLockCount++;
                if (htcDVCSWakeLockCount == 1) {
                    nativeAcquireCpuApDvcsWakeLock();
                    Slog.d(TAG, "enableCpuApDvcs: nativeAcquireCpuApDvcsWakeLock() is called");
                }
            } else {
                htcDVCSWakeLockCount--;
                if (htcDVCSWakeLockCount > 0) {
                    // do nothing
                } else if (htcDVCSWakeLockCount == 0) {
                    nativeReleaseCpuApDvcsWakeLock();
                    Slog.d(TAG, "enableCpuApDvcs: nativeReleaseCpuApDvcsWakeLock() is called");
                } else {
                    Slog.d(TAG, "enableCpuApDvcs: wrong DVCS count, htcDVCSWakeLockCount=" + htcDVCSWakeLockCount);
                    // restore, force to release DVCS
                    nativeReleaseCpuApDvcsWakeLock();
                    Slog.d(TAG, "enableCpuApDvcs: nativeReleaseCpuApDvcsWakeLock() is called by force!");
                    htcDVCSWakeLockCount = 0;
                }
            }
        }
    }
*/
    // -- [framework] API for HtcPowerSaver-ReduceCPU to fix DVCS wake lock causes CPU can not suspend issue.

    protected final class PMSInternalAPI {
        // ++ [framework] For HtcPowerSaver
        protected void nativeAcquireCpuApDvcsWakeLock() {
            PowerManagerService.this.nativeAcquireCpuApDvcsWakeLock();
        }

        protected void nativeReleaseCpuApDvcsWakeLock() {
            PowerManagerService.this.nativeReleaseCpuApDvcsWakeLock();
        }

//        protected void enableCpuApDvcs(boolean enable) {
//            PowerManagerService.this.enableCpuApDvcs(enable);
//        }
        // -- [framework] For HtcPowerSaver

        // ++ [framework] For HtcAutoBrightnessCtrl
        protected void setScreenBrightnessMode(int mode) {
            synchronized (mLock) {
                mScreenBrightnessModeSetting = mode;
                mDirty |= DIRTY_SETTINGS;
                updatePowerStateLocked();
            }
        }
        // -- [framework] For HtcAutoBrightnessCtrl

        // ++ [framework] Add screen timeout mechanism when dongle mode is turned on
        // this method should be called only when there is status change, enable=true->false, or enable=false->true.
        // NEVER call this method when there is no status change, enable=true->true, to avoid userActivity is being called again!!
        protected void enableDongleMode(boolean enable) {
            // recalculate everything and reset timer.
            recalculateScreenTimeout();

            // Skip the rest when screen is off.
            if (!isScreenOn()) {
                return;
            }

            synchronized (mLock) {
                if (mHtcDongleMode != null && enable) {
                    mWakeLockSummary = mHtcDongleMode.getCtrlWakeLockState("enableDongleMode", mWakeLockSummary,
                            WAKE_LOCK_SCREEN_BRIGHT | WAKE_LOCK_BUTTON_BRIGHT);
                }
            }

            // poke user activity
            PowerManagerService.this.userActivity(SystemClock.uptimeMillis(), PowerManager.USER_ACTIVITY_EVENT_OTHER, 0);
        }
        // -- [framework] Add screen timeout mechanism when dongle mode is turned on

        protected String lockType(int flags) {
            if (mHtcPMSExtension != null) {
                return mHtcPMSExtension.getWakeLockTypeInfo(flags);
            }
            return "???";
        }

        //[++framework] To control screen off by Proximity Sensor, peter li , 20130418
        protected void setProximityStateWithScreenOff(boolean value) {
            if (DEBUG_ON) {
                Slog.d(TAG, "Proximity With ScreenOff = " + value);
            }
            PowerManagerService.this.nativeSetProximityStateWithScreenOff(value);
        }
        //[--framework] To control screen off by Proximity Sensor, peter li , 20130418

        private PMSInternalAPI() {
        }
    }

    //++[framework] If the OOBE still keep in 1st page, then shutdown the devicePower
    private final class OOBETimeoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (DEBUG_ON) {
                Slog.d(TAG, "OOBETimeoutReceiver onReceive");
            }

            if (intent == null) {
                Slog.w(TAG, "OOBETimeoutReceiver intent is null");
                return;
            }

            String action = intent.getAction();

            if (action == null) {
                Slog.w(TAG, "action is null");
                return;
            }

            if (action.equals(ACTION_OOBE_TIMEOUT)) {

                try {

                    if (mHandler != null) {
                        mHandler.removeMessages(MSG_CHECK_IF_OOBE_STATUS_FINISHED_TIMEOUT);
                    }

                    if (!mHoldingOOBESuspendBlocker) {
                        mOOBESuspendBlocker.acquire();
                        mHoldingOOBESuspendBlocker = true;
                    }

                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(MSG_CHECK_IF_OOBE_STATUS_FINISHED_TIMEOUT);
                    }

                } catch (Exception e) {
                    Slog.w(TAG, "OOBETimeoutReceiver e=" + e);
                }

                if (DEBUG_ON) {
                    Slog.d(TAG, "OOBETimeoutReceiver 1");
                }

            } 

        }
    }

    @Override // Binder call
    public void informOOBEStatusChange(boolean status) {
        if (DEBUG_ON) {
            Slog.d(TAG, "OOBE change=" + status);
        }

        if (status) {

            try {

                if (mHandler != null) {
                    mHandler.removeMessages(MSG_CHECK_IF_OOBE_STATUS_FINISHED_TIMEOUT);
                }

                //set system property
                SystemProperties.set(SYS_PROP_OOBE_DONE, Integer.toString(OOBE_PROP_FINISH));

                clearMonitorOOBEStatus();

            } catch (Exception e) {
                Slog.w(TAG, "OOBE change e=" + e);
            }
        }

    }

    private void startWatchingForOOBEStatusFinished() {
        if (DEBUG_ON) {
            Slog.d(TAG, "OOBE s watch");
        }

        if (!mSystemReady) {
            Slog.d(TAG, "OOBE s watch 11");
            return;
        }

        try {
            if (mHandler != null) {
                mHandler.removeMessages(MSG_CHECK_IF_OOBE_STATUS_FINISHED);
                mHandler.sendEmptyMessage(MSG_CHECK_IF_OOBE_STATUS_FINISHED);
            }
        } catch (Exception e) {
            Slog.w(TAG, "OOBE s watch e=" + e);
        }

    }

    private boolean checkIfOOBEStatusFinished(boolean isTimeout) {
        if (DEBUG_ON) {
            Slog.d(TAG, "OOBE check");
        }

        if (!mSystemReady) {
            Slog.d(TAG, "OOBE check 11");
            return false;
        }

        //check system property
        int oobeDone = SystemProperties.getInt(SYS_PROP_OOBE_DONE, OOBE_PROP_UNFINISH);

        if (oobeDone == OOBE_PROP_FINISH) {
            if (DEBUG_ON) {
                Slog.d(TAG, "OOBE c done1");
            }
            return true;
        }

        //check HtcOOBE flag
        final ContentResolver cr = mContext.getContentResolver();
        int oobeFlag = Settings.System.getInt(cr, HTC_OOBE_USER_OPERATED, OOBE_DEFAULT);

        if (oobeFlag == OOBE_FINISH) {
            if (DEBUG_ON) {
                Slog.d(TAG, "OOBE c done2");
            }
            //set system property
            SystemProperties.set(SYS_PROP_OOBE_DONE, Integer.toString(OOBE_PROP_FINISH));
            return true;
        }

        if (isTimeout && (oobeFlag == OOBE_DEFAULT)) {
            if (DEBUG_ON) {
                Slog.d(TAG, "OOBE c done2-1");
            }
            //set system property
            SystemProperties.set(SYS_PROP_OOBE_DONE, Integer.toString(OOBE_PROP_FINISH));
            return true;
        }

        //check ATT OOBE Home Activity first
        try {

            PackageManager pm = mContext.getPackageManager();

            if (pm == null) {
                Slog.w(TAG, "pm is null");
                return false;
            }

            try {
                PackageInfo piATT = mContext.getPackageManager().getPackageInfo(ATT_OOBE_R2G_PACKAGE_NAME, 0);

                ComponentName cnATT= new ComponentName(ATT_OOBE_R2G_PACKAGE_NAME, ATT_OOBE_R2G_HOME_ACTIVITY);
                int statusATT = mContext.getPackageManager().getComponentEnabledSetting(cnATT);

                if (DEBUG_ON) {
                    Slog.d(TAG, "R2G status =" + statusATT);
                }

                if (statusATT == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                    if (DEBUG_ON) {
                        Slog.d(TAG, "OOBE c done3");
                    }
                    //set system property
                    SystemProperties.set(SYS_PROP_OOBE_DONE, Integer.toString(OOBE_PROP_FINISH));
                    return true;
                }

            } catch (NameNotFoundException ex) {

                if (DEBUG_ON) {
                    Slog.d(TAG, "Not find R2G");
                }

                //Check Our OOBE
                try {
                    PackageInfo piHTC = mContext.getPackageManager().getPackageInfo(HTC_OOBE_PACKAGE_NAME, 0);

                    ComponentName cnHTC= new ComponentName(HTC_OOBE_PACKAGE_NAME, HTC_OOBE_HOME_ACTIVITY);
                    int statusHTC = mContext.getPackageManager().getComponentEnabledSetting(cnHTC);

                    if (DEBUG_ON) {
                        Slog.d(TAG, "Our status =" + statusHTC);
                    }

                    if (statusHTC == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                        if (DEBUG_ON) {
                            Slog.d(TAG, "OOBE c done3");
                        }
                        //set system property
                        SystemProperties.set(SYS_PROP_OOBE_DONE, Integer.toString(OOBE_PROP_FINISH));
                        return true;
                    }
                } catch (NameNotFoundException ex1) {
                    Slog.w(TAG, "ex1=" + ex1);
                }

            }

        } catch (Exception e) {
            Slog.w(TAG, "e=" + e);
        }

        return false;

    }

    private void startMonitorOOBEStatus() {
        if (DEBUG_ON) {
            Slog.d(TAG, "OOBE s monitor");
        }

        if (!mSystemReady) {
            Slog.d(TAG, "OOBE s monitor 11");
            return;
        }

        try {

            AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

            if(mAlarmManager != null)
            {
                Intent intentOnAlarm = new Intent(ACTION_OOBE_TIMEOUT);
                PendingIntent mAlarmIntent = PendingIntent.getBroadcast(mContext, 0, intentOnAlarm, 0);

                if (mHasSetTimeout) {
                    mAlarmManager.cancel(mAlarmIntent);
                    mHasSetTimeout = false;
                }

                long timeToAlarm = SystemClock.elapsedRealtime() + DEFAULT_OOBE_MONITOR_TIMEOUT;

                if (DEBUG_ON) {
                    Slog.d(TAG, "OOBE s monitor time=" + timeToAlarm);
                }

                mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeToAlarm, mAlarmIntent);
                mHasSetTimeout = true;
            }

        } catch (Exception e) {
            Slog.w(TAG, "s monitor e=" + e);
        }

    }

    private void clearMonitorOOBEStatus() {
        if (DEBUG_ON) {
            Slog.d(TAG, "OOBE c monitor");
        }

        if ((!mSystemReady) || (!mHasSetTimeout)) {
            Slog.d(TAG, "OOBE c monitor 11");
            return;
        }

        try {
            AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

            if(mAlarmManager != null)
            {
                Intent intentOnAlarm = new Intent(ACTION_OOBE_TIMEOUT);
                PendingIntent mAlarmIntent = PendingIntent.getBroadcast(mContext, 0, intentOnAlarm, 0);

                if (mHasSetTimeout) {
                    if (DEBUG_ON) {
                        Slog.d(TAG, "OOBE c monitor 22");
                    }
                    mAlarmManager.cancel(mAlarmIntent);
                    mHasSetTimeout = false;
                }
            }

        } catch (Exception e) {
            Slog.d(TAG, "OOBE c monitor e=" +e);
        }
    }

    private void startShutdownDevice() {
        if (DEBUG_ON) {
            Slog.d(TAG, "OOBE s shutdown");
        }

        try {

            if (mHandler != null) {
                mHandler.removeMessages(MSG_CHECK_IF_OOBE_STATUS_FINISHED_TIMEOUT);
            }

            nativeShutdown();

        } catch (Exception e) {
            Slog.w(TAG, "OOBE s shutdown e=" + e);
        }

    }
    //--[framework] If the OOBE still keep in 1st page, then shutdown the devicePower

}
