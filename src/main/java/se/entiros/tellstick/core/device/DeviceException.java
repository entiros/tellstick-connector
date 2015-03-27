package se.entiros.tellstick.core.device;

import se.entiros.tellstick.core.TellstickException;

/**
 * Device Exception
 *
 * @author Petter Alstermark, Entiros AB
 */
public class DeviceException extends TellstickException {
    /**
     * @param device  device
     * @param errorNo Tellstick error number
     */
    public DeviceException(Device device, int errorNo) {
        super(device.getName(), device.getLibrary(), errorNo);
    }
}
