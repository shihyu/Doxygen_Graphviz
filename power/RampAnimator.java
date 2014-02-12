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

import android.animation.ValueAnimator;
import android.util.IntProperty;
import android.view.Choreographer;

// ++ [framework]
import android.util.Slog;
import com.htc.htcjavaflag.HtcBuildFlag;
// -- [framwork]

/**
 * A custom animator that progressively updates a property value at
 * a given variable rate until it reaches a particular target value.
 */
final class RampAnimator<T> {
    // ++ [framework] Add master debug control.
    private final String TAG = "XAN-RA";
    private final boolean DEBUG_ON = HtcBuildFlag.Htc_DEBUG_flag;
    // -- [framework] Add master debug control.

    //[++framework] Add mode control mechanism
    private static final int BRIGHTNESS_NONE_MODE = 0;
    private static final int BRIGHTNESS_CAMERA_LEAVE_MODE = 1;
    //[--framework] Add mode control mechanism

    private final T mObject;
    private final IntProperty<T> mProperty;
    private final Choreographer mChoreographer;

    // ++ [framework] Add support for customized duration for backlight transition.
    private long mStartTimeNanos = -1;
    private int mStartValue;
    private int mDuration = -1;
    // -- [framework] Add support for customized duration for backlight transition.

    static int mCurrentValue;
    private int mTargetValue;
    private int mRate;

    // ++ [framework] Add button light control mechanism
    static int mCurrentButtonValue;
    static int mTargetButtonValue;
    // -- [framework] Add button light control mechanism

    private boolean mAnimating;
    private float mAnimatedValue; // higher precision copy of mCurrentValue
    private long mLastFrameTimeNanos;

    private boolean mFirstTime = true;

    //[++framework] Add mode control mechanism
    private boolean mCameraAnimating = false;
    //[--framework] Add mode control mechanism

    public RampAnimator(T object, IntProperty<T> property) {
        mObject = object;
        mProperty = property;
        mChoreographer = Choreographer.getInstance();
    }

    //[++framework] Add mode control mechanism
    public boolean animateTo(int target, int rate, int buttonValue, int duration) {
        return animateTo(target, rate, buttonValue, duration, BRIGHTNESS_NONE_MODE);
    }
    //[--framework] Add mode control mechanism

    /**
     * Starts animating towards the specified value.
     *
     * If this is the first time the property is being set, the value jumps
     * directly to the target.
     *
     * @param target The target value.
     * @param rate The convergence rate, in units per second.
     * @return True if the target differs from the previous target.
     */
    // ++ [framework] Add button light control mechanism
    public boolean animateTo(int target, int rate, int buttonValue, int duration, int mode) {
    // -- [framework] Add button light control mechanism
        // Immediately jump to the target the first time.
        if (mFirstTime) {
            mFirstTime = false;
            // ++ [framework] Add button light control mechanism
            mTargetButtonValue = buttonValue;
            // -- [framework] Add button light control mechanism

            mProperty.setValue(mObject, target);

            // ++ [framework] Add button light control mechanism
            mCurrentButtonValue = mTargetButtonValue;
            // -- [framework] Add button light control mechanism
            mCurrentValue = target;
            return true;
        }

        //[++framework] Add mode control mechanism
        if (mode == BRIGHTNESS_CAMERA_LEAVE_MODE) {
            mCameraAnimating = true;
        }
        //[--framework] Add mode control mechanism

        // ++ [framework] Add support for customized duration for backlight transition.
        int old_rate = mRate;
        int old_duration = mDuration;
        // -- [framework] Add support for customized duration for backlight transition.

        // Adjust the rate based on the closest target.
        // If a faster rate is specified, then use the new rate so that we converge
        // more rapidly based on the new request.
        // If a slower rate is specified, then use the new rate only if the current
        // value is somewhere in between the new and the old target meaning that
        // we will be ramping in a different direction to get there.
        // Otherwise, continue at the previous rate.

        // ++ [framework] Add support for customized duration for backlight transition.
        if (duration < 0
        // -- [framework] Add support for customized duration for backlight transition.
            && (!mAnimating
                || rate > mRate
                || (target <= mCurrentValue && mCurrentValue <= mTargetValue)
                || (mTargetValue <= mCurrentValue && mCurrentValue <= target))) {
            mRate = rate;
            //[++framework] Add mode control mechanism
            if (!mCameraAnimating) {
                mDuration = -1;
            }
            //[--framework] Add mode control mechanism
        // ++ [framework] Add support for customized duration for backlight transition.
        } else if (duration >= 0) {
            mRate = -1;
            mStartTimeNanos = -1;
            mStartValue = mCurrentValue;
            mDuration = duration;
        }
        // -- [framework] Add support for customized duration for backlight transition.

        // ++ [framework] Add debug message.
        if (DEBUG_ON &&
                ((!mAnimating && target != mCurrentValue) || (buttonValue >= 0 && buttonValue != mCurrentButtonValue))) {
            Slog.d(TAG, "animateTo(" + target + ", " + rate + ", " + buttonValue + ", " + duration + ")"
                    + ", target(" + mCurrentValue + ")=" + mTargetValue + "->" + target
                    + ", bValue(" + mCurrentButtonValue + ")=" + mTargetButtonValue + "->" + buttonValue
                    + ", rate=" + old_rate + "->" + mRate
                    + ", duration=" + old_duration + "->" + mDuration
                    + ", mAnimating=" + mAnimating
                    + ", mAnimatedValue=" + mAnimatedValue);
        }
        // -- [framework] Add debug message.

        // ++ [framework] Add button light control mechanism
        final boolean changed = (mTargetValue != target) || (buttonValue >= 0 && mTargetButtonValue != buttonValue);
        // -- [framework] Add button light control mechanism

        mTargetValue = target;

        // ++ [framework] Add button light control mechanism
        mTargetButtonValue = buttonValue;
        // -- [framework] Add button light control mechanism

        // Start animating.
        // ++ [framework] Add button light control mechanism
        if (!mAnimating && (target != mCurrentValue || (buttonValue >= 0 && buttonValue != mCurrentButtonValue))) {
        // -- [framework] Add button light control mechanism
            mAnimating = true;
            mAnimatedValue = mCurrentValue;
            mLastFrameTimeNanos = System.nanoTime();
            postCallback();
        }

        return changed;
    }

    // ++ [framework] For HtcAutoBrightnessCtrl, to avoid incorrect current lcd value being read when launch Camera during animating.
    int getCurrentBrightness() {
        return mCurrentValue;
    }

    int getTargetBrightness() {
        return mTargetValue;
    }

    void cancelAnimation() {
        animateTo(mTargetValue, 0, mTargetButtonValue, -1);
    }
    // -- [framework] For HtcAutoBrightnessCtrl, to avoid incorrect current lcd value being read when launch Camera during animating.

    private void postCallback() {
        mChoreographer.postCallback(Choreographer.CALLBACK_ANIMATION, mCallback, null);
    }

    private final Runnable mCallback = new Runnable() {
        @Override // Choreographer callback
        public void run() {
            // ++ [framework] Add support for customized duration for backlight transition.
            if (mDuration > 0 && mStartTimeNanos < 0) {
                mStartTimeNanos = mChoreographer.getFrameTimeNanos();
            }
            // -- [framework] Add support for customized duration for backlight transition.

            final long frameTimeNanos = mChoreographer.getFrameTimeNanos();
            final float timeDelta = (frameTimeNanos - mLastFrameTimeNanos)
                    * 0.000000001f;
            mLastFrameTimeNanos = frameTimeNanos;

            // Advance the animated value towards the target at the specified rate
            // and clamp to the target. This gives us the new current value but
            // we keep the animated value around to allow for fractional increments
            // towards the target.
            final float scale = ValueAnimator.getDurationScale();
            // ++ [framework] Add support for customized duration for backlight transition.
            if (scale == 0 || mRate == 0 || mDuration == 0 || (mRate <= 0 && mDuration <= 0)) {
            // -- [framework] Add support for customized duration for backlight transition.
                // ++ [framework] Add debug message.
                if (DEBUG_ON) {
                    Slog.d(TAG, "mCb:Anim off."
                            + "(" + mCurrentValue
                            + "," + mAnimatedValue
                            + "," + mTargetValue
                            + "," + scale
                            + "," + mRate
                            + "," + mDuration + ")");
                }
                // -- [framework] Add debug message.
                // Animation off.
                mAnimatedValue = mTargetValue;
            // ++ [framework] Add support for customized duration for backlight transition.
            } else if (mDuration > 0) {
                final int DELTA = mTargetValue - mStartValue;
                final float ELAPSED = frameTimeNanos - mStartTimeNanos;
                final float AMOUNT = DELTA * (ELAPSED / ((float) mDuration * 1000000));

                mAnimatedValue = mStartValue + AMOUNT;

                // Case: Decrease
                if (mStartValue > mTargetValue) {
                    mAnimatedValue = Math.max(mTargetValue, Math.round(mAnimatedValue));
                // Case: Increase
                } else {
                    mAnimatedValue = Math.min(mTargetValue, Math.round(mAnimatedValue));
                }

                // ++ [framework] Add debug message.
                if (DEBUG_ON && false) {
                    Slog.d(TAG, "mCallback:"
                            + " lvalue=" + mCurrentValue + "->" + mAnimatedValue
                            + ", mStartValue=" + mStartValue
                            + ", mDuration=" + mDuration
                            + ", AMOUNT=" + AMOUNT
                            + ", DELTA=" + DELTA
                            + ", ELAPSED=" + ELAPSED
                            + ", frameTimeNanos=" + frameTimeNanos
                            + ", mStartTimeNanos=" + mStartTimeNanos);
                }
                // -- [framework] Add debug message.
            // -- [framework] Add support for customized duration for backlight transition.
            } else {
                final float amount = timeDelta * mRate / scale;
                if (mTargetValue > mCurrentValue) {
                    mAnimatedValue = Math.min(mAnimatedValue + amount, mTargetValue);
                } else {
                    mAnimatedValue = Math.max(mAnimatedValue - amount, mTargetValue);
                }
                // ++ [framework] Add debug message.
                if (DEBUG_ON && false) {
                    Slog.d(TAG, "mCallback:"
                            + " lvalue=" + mCurrentValue + "->" + mAnimatedValue
                            + ", amount=" + amount
                            + ", scale=" + scale
                            + ", mRate=" + mRate
                            + ", timeDelta=" + timeDelta
                            + ", frameTimeNanos=" + frameTimeNanos
                            + ", mLastFrameTimeNanos=" + mLastFrameTimeNanos);
                }
                // -- [framework] Add debug message.
            }
            final int oldCurrentValue = mCurrentValue;
            mCurrentValue = Math.round(mAnimatedValue);

            // ++ [framework] Add debug message.
            if (DEBUG_ON && false) {
                Slog.d(TAG, "mCallback: setValue mCurrentValue=" + mCurrentValue);
            }
            if (DEBUG_ON) {
                if (scale == 0 || mRate == 0 || mDuration == 0 || (mRate <= 0 && mDuration <= 0)) {
                    Slog.d(TAG, "mCb:setValue (" + oldCurrentValue + "," + mCurrentValue + ")");
                }
            }
            // -- [framework] Add debug message.

            // ++ [framework] Add button light control mechanism
            if (oldCurrentValue != mCurrentValue || mTargetButtonValue != mCurrentButtonValue) {
                if (DEBUG_ON) {
                    if (scale == 0 || mRate == 0 || mDuration == 0 || (mRate <= 0 && mDuration <= 0)) {
                        Slog.d(TAG, "mCb:SV S");
                    }
                }
                mProperty.setValue(mObject, mCurrentValue);
                if (DEBUG_ON) {
                    if (scale == 0 || mRate == 0 || mDuration == 0 || (mRate <= 0 && mDuration <= 0)) {
                        Slog.d(TAG, "mCb:SV E");
                    }
                }
                // [framework] Button value will be set when 'mProperty.setValue(mObject, mCurrentValue)' is called and returned.
                mCurrentButtonValue = mTargetButtonValue;
            }
            // -- [framework] Add button light control mechanism

            if (mTargetValue != mCurrentValue) {
                postCallback();
            } else {
                // ++ [framework] Add debug message.
                if (DEBUG_ON) {
                    Slog.d(TAG, "mCb:FV (" + mTargetValue + ")");
                }
                // -- [framework] Add debug message.
                // ++ [framework] Add support for customized duration for backlight transition.
                mStartTimeNanos = -1;
                mDuration = -1;
                // -- [framework] Add support for customized duration for backlight transition.
                //[++framework] Add mode control mechanism
                mCameraAnimating = false;
                //[--framework] Add mode control mechanism
                mAnimating = false;
            }
        }
    };
}
