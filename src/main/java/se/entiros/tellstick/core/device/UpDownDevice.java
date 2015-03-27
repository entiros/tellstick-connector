package se.entiros.tellstick.core.device;

/**
 * Up/Down Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface UpDownDevice extends Device {
    /**
     * Up
     *
     * @throws DeviceException
     */
    void up() throws DeviceException;

    /**
     * Down
     *
     * @throws DeviceException
     */
    void down() throws DeviceException;

    /**
     * Stop
     *
     * @throws DeviceException
     */
    void stop() throws DeviceException;
}
