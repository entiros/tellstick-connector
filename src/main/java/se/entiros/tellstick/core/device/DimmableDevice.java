package se.entiros.tellstick.core.device;

/**
 * Dimmable Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface DimmableDevice extends OnOffDevice {
    void dim(int level) throws DeviceException;

    /**
     * On (max level)
     *
     * @throws DeviceException
     */
    void on() throws DeviceException;

    /**
     * Off (min level)
     *
     * @throws DeviceException
     */
    void off() throws DeviceException;

    /**
     * @return true if device is on or dim level is greater than zero
     */
    boolean isOn();
}
