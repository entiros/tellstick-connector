package com.alstermark.tellstick.core.device;

/**
 * Bell Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public interface BellDevice extends Device {
    /**
     * Bell
     *
     * @throws com.alstermark.tellstick.core.device.DeviceException
     */
    void bell() throws DeviceException;
}
