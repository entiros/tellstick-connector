package com.alstermark.tellstick.core.device.impl;

import com.alstermark.tellstick.core.TellstickCoreLibrary;
import com.alstermark.tellstick.core.device.AbstractDevice;
import com.alstermark.tellstick.core.device.BellDevice;
import com.alstermark.tellstick.core.device.DeviceException;
import com.alstermark.tellstick.core.device.DeviceHandler;

public class BellDeviceImpl extends AbstractDevice implements BellDevice {

    public BellDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    @Override
    public void bell() throws DeviceException {
        if (logger.isDebugEnabled())
            logger.debug("BELL " + toString());

        int status = getLibrary().tdBell(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

}
