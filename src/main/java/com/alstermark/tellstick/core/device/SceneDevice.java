package com.alstermark.tellstick.core.device;

/**
 * Scene Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public interface SceneDevice extends Device {
    /**
     * Execute
     *
     * @throws com.alstermark.tellstick.core.device.DeviceException
     */
    void execute() throws DeviceException;
}