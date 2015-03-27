package se.entiros.tellstick.core.device;

/**
 * Scene Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface SceneDevice extends Device {
    /**
     * Execute
     *
     * @throws DeviceException
     */
    void execute() throws DeviceException;
}
