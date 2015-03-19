package se.entiros.tellstick.core.device;

import se.entiros.tellstick.core.TellstickCoreLibrary;

/**
 * Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
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
     * Get Library
     *
     * @return library
     */
    TellstickCoreLibrary getLibrary();

    /**
     * Get Device Handler
     *
     * @return device handler
     */
    DeviceHandler getDeviceHandler();
}
