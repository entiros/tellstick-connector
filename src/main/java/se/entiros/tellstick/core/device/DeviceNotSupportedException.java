package se.entiros.tellstick.core.device;

/**
 * Device not supported exception
 */
public class DeviceNotSupportedException extends Exception {
    public DeviceNotSupportedException(String message) {
        super(message);
    }
}
