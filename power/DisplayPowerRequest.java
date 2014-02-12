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

import android.os.PowerManager;

/**
 * Describes the requested power state of the display.
 *
 * This object is intended to describe the general characteristics of the
 * power state, such as whether the screen should be on or off and the current
 * brightness controls leaving the {@link DisplayPowerController} to manage the
 * details of how the transitions between states should occur.  The goal is for
 * the {@link PowerManagerService} to focus on the global power state and not
 * have to micro-manage screen off animations, auto-brightness and other effects.
 */
final class DisplayPowerRequest {
    public static final int SCREEN_STATE_OFF = 0;
    public static final int SCREEN_STATE_DIM = 1;
    public static final int SCREEN_STATE_BRIGHT = 2;

    // The requested minimum screen power state: off, dim or bright.
    public int screenState;

    // If true, the proximity sensor overrides the screen state when an object is
    // nearby, turning it off temporarily until the object is moved away.
    public boolean useProximitySensor;

    // The desired screen brightness in the range 0 (minimum / off) to 255 (brightest).
    // The display power controller may choose to clamp the brightness.
    // When auto-brightness is enabled, this field should specify a nominal default
    // value to use while waiting for the light sensor to report enough data.
    public int screenBrightness;

    // The screen auto-brightness adjustment factor in the range -1 (dimmer) to 1 (brighter).
    public float screenAutoBrightnessAdjustment;

    // If true, enables automatic brightness control.
    public boolean useAutoBrightness;

    // ++ [framework] To determine whether the screen brightness came from override
    public boolean useOverrideBrightness;
    // -- [framework] To determine whether the screen brightness came from override

    // ++ [framework] To determine user had pressed power key to force screen on.
    public boolean forceScreenOn;
    // -- [framework] To determine user had pressed power key to force screen on.

    // ++ [framework] Workaround for M7 p-sensor problem after ending call
    public boolean delayScreenOn;
    // -- [framework] Workaround for M7 p-sensor problem after ending call

    // ++ [framework] Workaround for turning on screen when screen is off.
    public boolean turnOnScreenOnce;
    // -- [framework] Workaround for turning on screen when screen is off.

    // If true, prevents the screen from completely turning on if it is currently off.
    // The display does not enter a "ready" state if this flag is true and screen on is
    // blocked.  The window manager policy blocks screen on while it prepares the keyguard to
    // prevent the user from seeing intermediate updates.
    //
    // Technically, we may not block the screen itself from turning on (because that introduces
    // extra unnecessary latency) but we do prevent content on screen from becoming
    // visible to the user.
    public boolean blockScreenOn;

    //++[framework] For max auto brightness feature
    public int screenAutoBrightnessPercentage;
    //--[framework] For max auto brightness feature

    public DisplayPowerRequest() {
        screenState = SCREEN_STATE_BRIGHT;
        useProximitySensor = false;
        screenBrightness = PowerManager.BRIGHTNESS_ON;
        screenAutoBrightnessAdjustment = 0.0f;
        useAutoBrightness = false;

        // ++ [framework] To determine whether the screen brightness came from override
        useOverrideBrightness = false;
        // -- [framework] To determine whether the screen brightness came from override

        // ++ [framework] To determine user had pressed power key to force screen on.
        forceScreenOn = false;
        // -- [framework] To determine user had pressed power key to force screen on.

        //++[framework] For max auto brightness feature
        screenAutoBrightnessPercentage = PowerManager.SCREEN_AUTO_BRIGHTNESS_SETTING_DEFAULT_PERCENTAGE_VALUE;
        //++[framework] For max auto brightness feature

        // ++ [framework] Workaround for M7 p-sensor problem after ending call
        delayScreenOn = false;
        // -- [framework] Workaround for M7 p-sensor problem after ending call

        // ++ [framework] Workaround for turning on screen when screen is off.
        turnOnScreenOnce = false;
        // -- [framework] Workaround for turning on screen when screen is off.

        blockScreenOn = false;
    }

    public DisplayPowerRequest(DisplayPowerRequest other) {
        copyFrom(other);
    }

    public void copyFrom(DisplayPowerRequest other) {
        screenState = other.screenState;
        useProximitySensor = other.useProximitySensor;
        screenBrightness = other.screenBrightness;
        screenAutoBrightnessAdjustment = other.screenAutoBrightnessAdjustment;
        useAutoBrightness = other.useAutoBrightness;

        // ++ [framework] To determine whether the screen brightness came from override
        useOverrideBrightness = other.useOverrideBrightness;
        // -- [framework] To determine whether the screen brightness came from override

        // ++ [framework] To determine user had pressed power key to force screen on.
        forceScreenOn = other.forceScreenOn;
        // -- [framework] To determine user had pressed power key to force screen on.

        //++[framework] For max auto brightness feature
        screenAutoBrightnessPercentage = other.screenAutoBrightnessPercentage;
        //--[framework] For max auto brightness feature

        // ++ [framework] Workaround for M7 p-sensor problem after ending call
        delayScreenOn = other.delayScreenOn;
        // -- [framework] Workaround for M7 p-sensor problem after ending call

        // ++ [framework] Workaround for turning on screen when screen is off.
        turnOnScreenOnce = other.turnOnScreenOnce;
        // -- [framework] Workaround for turning on screen when screen is off.

        blockScreenOn = other.blockScreenOn;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DisplayPowerRequest
                && equals((DisplayPowerRequest)o);
    }

    public boolean equals(DisplayPowerRequest other) {
        return other != null
                && screenState == other.screenState
                && useProximitySensor == other.useProximitySensor
                && screenBrightness == other.screenBrightness
                && screenAutoBrightnessAdjustment == other.screenAutoBrightnessAdjustment
                && useAutoBrightness == other.useAutoBrightness
                // ++ [framework] To determine whether the screen brightness came from override
                && useOverrideBrightness == other.useOverrideBrightness
                // -- [framework] To determine whether the screen brightness came from override
                // ++ [framework] To determine user had pressed power key to force screen on.
                && forceScreenOn == other.forceScreenOn
                // -- [framework] To determine user had pressed power key to force screen on.
                //++[framework] For max auto brightness feature
                && screenAutoBrightnessPercentage == other.screenAutoBrightnessPercentage
                //--[framework] For max auto brightness feature
                // ++ [framework] Workaround for M7 p-sensor problem after ending call
                && delayScreenOn == other.delayScreenOn
                // -- [framework] Workaround for M7 p-sensor problem after ending call
                // ++ [framework] Workaround for turning on screen when screen is off.
                && turnOnScreenOnce == other.turnOnScreenOnce
                // -- [framework] Workaround for turning on screen when screen is off.
                && blockScreenOn == other.blockScreenOn;
    }

    @Override
    public int hashCode() {
        return 0; // don't care
    }

    @Override
    public String toString() {
        return "screenState=" + screenState
                + ", useProximitySensor=" + useProximitySensor
                + ", screenBrightness=" + screenBrightness
                + ", screenAutoBrightnessAdjustment=" + screenAutoBrightnessAdjustment
                + ", useAutoBrightness=" + useAutoBrightness
                // ++ [framework] To determine whether the screen brightness came from override
                + ", useOverrideBrightness=" + useOverrideBrightness
                // -- [framework] To determine whether the screen brightness came from override
                // ++ [framework] To determine user had pressed power key to force screen on.
                + ", forceScreenOn=" + forceScreenOn
                // -- [framework] To determine user had pressed power key to force screen on.
                //++[framework] For max auto brightness feature
                + ", screenAutoBrightnessPercentage=" + screenAutoBrightnessPercentage
                //--[framework] For max auto brightness feature
                // ++ [framework] Workaround for M7 p-sensor problem after ending call
                + ", delayScreenOn=" + delayScreenOn
                // -- [framework] Workaround for M7 p-sensor problem after ending call
                // ++ [framework] Workaround for turning on screen when screen is off.
                + ", turnOnScreenOnce=" + turnOnScreenOnce
                // -- [framework] Workaround for turning on screen when screen is off.
                + ", blockScreenOn=" + blockScreenOn;
    }
}
