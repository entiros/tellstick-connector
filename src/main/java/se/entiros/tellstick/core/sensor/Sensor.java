package se.entiros.tellstick.core.sensor;

/**
 * Sensor
 *
 * @author Petter Alstermark, Entiros AB
 */
public class Sensor {
    private final String protocol;
    private final String model;
    private final int id;
    private final int dataType;
    private final String value;
    private final long timestamp;

    /**
     * @param id        ID
     * @param protocol  protocol
     * @param model     model
     * @param dataType  data type
     * @param value     value
     * @param timestamp timestamp
     */
    public Sensor(int id, String protocol, String model, int dataType, String value, long timestamp) {
        this.protocol = protocol;
        this.model = model;
        this.id = id;
        this.dataType = dataType;
        this.value = value;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Sensor [protocol=" + protocol + ", model=" + model + ", id="
                + id + ", dataType=" + dataType + ", value=" + value + "]";
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the dataType
     */
    public int getDataType() {
        return dataType;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sensor sensor = (Sensor) o;

        return id == sensor.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
