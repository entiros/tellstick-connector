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
