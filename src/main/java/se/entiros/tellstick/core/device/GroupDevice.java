/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

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
