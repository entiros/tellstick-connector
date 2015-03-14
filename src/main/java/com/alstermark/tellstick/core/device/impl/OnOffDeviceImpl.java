package com.alstermark.tellstick.core.device.impl;

import com.alstermark.tellstick.core.TellstickCoreLibrary;
import com.alstermark.tellstick.core.device.AbstractDevice;
import com.alstermark.tellstick.core.device.DeviceException;
import com.alstermark.tellstick.core.device.DeviceHandler;
import com.alstermark.tellstick.core.device.OnOffDevice;

public class OnOffDeviceImpl extends AbstractDevice implements OnOffDevice {

    public OnOffDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    @Override
    public void on() throws DeviceException {
        if (logger.isDebugEnabled())
            logger.debug("ON " + toString());

        int status = getLibrary().tdTurnOn(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    @Override
    public void off() throws DeviceException {
        if (logger.isDebugEnabled())
            logger.debug("OFF " + toString());

        int status = getLibrary().tdTurnOff(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    @Override
    public boolean isOn() {
        return (getStatus() & TellstickCoreLibrary.TELLSTICK_TURNON) > 0;
    }

}
