package se.entiros.tellstick.core.rawdevice;

import java.util.Map;

/**
 * Raw Device Event Listener
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface RawDeviceEventListener {
    /**
     * Raw Device Event
     *
     * @param parameters raw device parameters
     */
    void rawDeviceEvent(Map<String, String> parameters);
}
