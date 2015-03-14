package com.alstermark.tellstick.core.device.impl;

import com.alstermark.tellstick.core.TellstickCoreLibrary;
import com.alstermark.tellstick.core.device.AbstractDevice;
import com.alstermark.tellstick.core.device.DeviceException;
import com.alstermark.tellstick.core.device.DeviceHandler;
import com.alstermark.tellstick.core.device.SceneDevice;


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
