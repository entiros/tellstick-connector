package se.entiros.tellstick.core.device;

import se.entiros.tellstick.core.TellstickException;

/**
 * Device Exception
 */
public class DeviceException extends TellstickException {
    public DeviceException(Device device, int errorNo) {
        super(device.getName(), device.getLibrary(), errorNo);
    }
}
