/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

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
