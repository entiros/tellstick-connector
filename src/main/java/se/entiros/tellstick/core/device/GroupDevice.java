package se.entiros.tellstick.core.device;

import java.util.List;

/**
 * Group Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public interface GroupDevice extends OnOffDevice {
    /**
     * Get Devices
     *
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
