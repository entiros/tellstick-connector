/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.device.AbstractDevice;
import se.entiros.tellstick.core.device.DeviceHandler;

/**
 * Unsupported Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public class UnsupportedDevice extends AbstractDevice {
    public UnsupportedDevice(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }
}
