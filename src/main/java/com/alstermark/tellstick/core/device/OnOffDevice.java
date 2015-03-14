package com.alstermark.tellstick.core.device;

/**
 * On/Off Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public interface OnOffDevice extends Device {
    /**
     * Turn on
     *
     * @throws com.alstermark.tellstick.core.device.DeviceException
     */
    void on() throws DeviceException;

    /**
     * Turn Off
     *
     * @throws com.alstermark.tellstick.core.device.DeviceException
     */
    void off() throws DeviceException;

    /**
     * @return true if device is on
     */
    boolean isOn();
}
