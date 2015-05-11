/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.device.AbstractDevice;
import se.entiros.tellstick.core.device.DeviceException;
import se.entiros.tellstick.core.device.DeviceHandler;
import se.entiros.tellstick.core.device.UpDownDevice;

/**
 * Up/Down device implementation
 *
 * @author Petter Alstermark, Entiros AB
 */
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
