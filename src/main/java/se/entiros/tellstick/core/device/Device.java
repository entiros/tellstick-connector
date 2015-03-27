package se.entiros.tellstick.core.device;

import se.entiros.tellstick.core.TellstickCoreLibrary;

/**
 * Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface Device extends Comparable<Device> {
    /**
     * @return the deviceId
     */
    int getDeviceId();

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the model
     */
    String getModel();

    /**
     * @return the protocol
     */
    String getProtocol();

    /**
     * @return the deviceType
     */
    int getDeviceType();

    /**
     * @return status
     */
    int getStatus();

    /**
     * @return library
     */
    TellstickCoreLibrary getLibrary();

    /**
     * @return device handler
     */
    DeviceHandler getDeviceHandler();
}
