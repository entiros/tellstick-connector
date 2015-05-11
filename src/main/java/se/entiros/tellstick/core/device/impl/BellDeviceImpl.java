/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.device.AbstractDevice;
import se.entiros.tellstick.core.device.BellDevice;
import se.entiros.tellstick.core.device.DeviceException;
import se.entiros.tellstick.core.device.DeviceHandler;

/**
 * Bell device implementation
 *
 * @author Petter Alstermark, Entiros AB
 */
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
