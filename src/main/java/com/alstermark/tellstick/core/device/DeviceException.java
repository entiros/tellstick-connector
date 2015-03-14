package com.alstermark.tellstick.core.device;

import com.alstermark.tellstick.core.TellstickException;

/**
 * Device Exception
 */
public class DeviceException extends TellstickException {
    public DeviceException(Device device, int errorNo) {
        super(device.getName(), device.getLibrary(), errorNo);
    }
}
