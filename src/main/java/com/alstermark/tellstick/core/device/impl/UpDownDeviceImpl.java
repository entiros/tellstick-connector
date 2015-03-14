package com.alstermark.tellstick.core.device.impl;

import com.alstermark.tellstick.core.TellstickCoreLibrary;
import com.alstermark.tellstick.core.device.AbstractDevice;
import com.alstermark.tellstick.core.device.DeviceException;
import com.alstermark.tellstick.core.device.DeviceHandler;
import com.alstermark.tellstick.core.device.UpDownDevice;

public class UpDownDeviceImpl extends AbstractDevice implements UpDownDevice {

    public UpDownDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    @Override
    public void up() throws DeviceException {
        if (logger.isDebugEnabled())
            logger.debug("UP " + toString());

        int status = getLibrary().tdUp(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    @Override
    public void down() throws DeviceException {
        if (logger.isDebugEnabled())
            logger.debug("DOWN " + toString());

        int status = getLibrary().tdDown(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    @Override
    public void stop() throws DeviceException {
        if (logger.isDebugEnabled())
            logger.debug("STOP " + toString());

        int status = getLibrary().tdStop(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

}
