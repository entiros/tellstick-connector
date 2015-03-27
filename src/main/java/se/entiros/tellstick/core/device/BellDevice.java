package se.entiros.tellstick.core.device;

/**
 * Bell Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface BellDevice extends Device {
    /**
     * Bell
     *
     * @throws DeviceException
     */
    void bell() throws DeviceException;
}
