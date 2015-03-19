package se.entiros.tellstick.core.device;

/**
 * Up/Down Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
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
