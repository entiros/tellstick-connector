/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.sensor;

import se.entiros.tellstick.core.TellstickCoreLibrary;

/**
 * Temperature Sensor
 *
 * @author Petter Alstermark, Entiros AB
 */
public class TemperatureSensor extends Sensor {

    /**
     * @param id        id
     * @param protocol  protocol
     * @param model     model
     * @param value     value
     * @param timestamp timestamp
     */
    public TemperatureSensor(int id, String protocol, String model, String value, long timestamp) {
        super(id, protocol, model, TellstickCoreLibrary.TELLSTICK_TEMPERATURE, value, timestamp);
    }

    /**
     * Get Temperature
     *
     * @return temperature
     */
    public double getTemperature() {
        return Double.parseDouble(getValue());
    }
}
