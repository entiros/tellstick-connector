package se.entiros.tellstick.core.device;

import java.util.List;

/**
 * Group Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface GroupDevice extends OnOffDevice {
    /**
     * @return devices
     */
    List<Device> getDevices();

    /**
     * Turn on
     */
    void on();

    /**
     * Turn off
     */
    void off();
}
