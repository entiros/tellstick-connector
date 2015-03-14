package com.alstermark.tellstick.core.sensor;

import com.alstermark.tellstick.core.TellstickCoreLibrary;

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
