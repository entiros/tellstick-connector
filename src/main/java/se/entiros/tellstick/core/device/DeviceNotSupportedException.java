package se.entiros.tellstick.core.device;

/**
 * Device not supported exception
 *
 * @author Petter Alstermark, Entiros AB
 */
public class DeviceNotSupportedException extends Exception {
    /**
     * @param message exception message
     */
    public DeviceNotSupportedException(String message) {
        super(message);
    }
}
