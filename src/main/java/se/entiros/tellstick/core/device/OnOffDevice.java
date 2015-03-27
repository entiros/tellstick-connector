package se.entiros.tellstick.core.device;

/**
 * On/Off Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface OnOffDevice extends Device {
    /**
     * Turn on
     *
     * @throws DeviceException
     */
    void on() throws DeviceException;

    /**
     * Turn Off
     *
     * @throws DeviceException
     */
    void off() throws DeviceException;

    /**
     * @return true if device is on
     */
    boolean isOn();
}
