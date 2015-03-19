package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.device.AbstractDevice;
import se.entiros.tellstick.core.device.DeviceException;
import se.entiros.tellstick.core.device.DeviceHandler;
import se.entiros.tellstick.core.device.SceneDevice;


public class SceneDeviceImpl extends AbstractDevice implements SceneDevice {

    public SceneDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    @Override
    public void execute() throws DeviceException {
        if (logger.isDebugEnabled())
            logger.debug("EXECUTE " + toString());

        int status = getLibrary().tdExecute(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

}
