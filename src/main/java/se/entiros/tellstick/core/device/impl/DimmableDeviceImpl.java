package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.device.AbstractDevice;
import se.entiros.tellstick.core.device.DeviceException;
import se.entiros.tellstick.core.device.DeviceHandler;
import se.entiros.tellstick.core.device.DimmableDevice;

public class DimmableDeviceImpl extends AbstractDevice implements DimmableDevice {

    public DimmableDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    @Override
    public void dim(int level) throws DeviceException {
        if (logger.isDebugEnabled())
            logger.debug("DIM " + level + " " + toString());

        if (level < 0 || level > 255)
            throw new IllegalArgumentException("Dim level must be between 0 and 255.");

        int status = getLibrary().tdDim(getDeviceId(), level);
        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    @Override
    public void on() throws DeviceException {
        dim(255);
    }

    @Override
    public void off() throws DeviceException {
        dim(0);
    }

    @Override
    public boolean isOn() {
        boolean isOn = (getStatus() & TellstickCoreLibrary.TELLSTICK_TURNON) > 0;
        return isOn || ((TellstickCoreLibrary.TELLSTICK_DIM & getStatus()) > 0);
    }

}
