package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.device.AbstractDevice;
import se.entiros.tellstick.core.device.DeviceException;
import se.entiros.tellstick.core.device.DeviceHandler;
import se.entiros.tellstick.core.device.OnOffDevice;

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
