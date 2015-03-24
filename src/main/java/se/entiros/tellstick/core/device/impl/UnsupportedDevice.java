package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.device.AbstractDevice;
import se.entiros.tellstick.core.device.DeviceHandler;

/**
 * Unsupported Device
 * <p/>
 * Created by Petter Alstermark on 2015-03-24.
 */
public class UnsupportedDevice extends AbstractDevice {
    public UnsupportedDevice(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }
}
