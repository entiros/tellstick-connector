/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.device.AbstractDevice;
import se.entiros.tellstick.core.device.DeviceException;
import se.entiros.tellstick.core.device.DeviceHandler;
import se.entiros.tellstick.core.device.SceneDevice;

/**
 * Scene device implementation
 *
 * @author Petter Alstermark, Entiros AB
 */
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
