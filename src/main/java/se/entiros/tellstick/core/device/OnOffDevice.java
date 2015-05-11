/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device;

/**
 * On/Off Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public interface OnOffDevice extends Device {
    /**
     * Turn on
     *
     * @throws DeviceException
     */
    void on() throws DeviceException;

    /**
     * Turn Off
     *
     * @throws DeviceException
     */
    void off() throws DeviceException;

    /**
     * @return true if device is on
     */
    boolean isOn();
}
