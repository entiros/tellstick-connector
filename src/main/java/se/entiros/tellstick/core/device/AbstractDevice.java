package se.entiros.tellstick.core.device;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

/**
 * Abstract Device
 *
 * @author Petter Alstermark, Entiros AB
 */
public abstract class AbstractDevice implements Device {
    protected static final Logger logger = Logger.getLogger(AbstractDevice.class);

    private final DeviceHandler deviceHandler;
    private final TellstickCoreLibrary library;

    private final int deviceId;

    private final String name;
    private final String model;
    private final String protocol;
    private final int deviceType;

    /**
     * @param deviceHandler device handler
     * @param deviceId      device ID
     */
    public AbstractDevice(DeviceHandler deviceHandler, int deviceId) {
        this.deviceHandler = deviceHandler;
        this.deviceId = deviceId;

        library = deviceHandler.getLibrary();

        // Name
        {
            if (logger.isTraceEnabled())
                logger.trace("Get device name " + deviceId);
            Pointer namePointer = library.tdGetName(deviceId);
            name = namePointer.getString(0);

            if (logger.isTraceEnabled())
                logger.trace("Release name " + deviceId);
            library.tdReleaseString(namePointer);
        }

        // Model
        {
            if (logger.isTraceEnabled())
                logger.trace("Get model " + deviceId);
            Pointer modelPointer = library.tdGetModel(deviceId);
            model = modelPointer.getString(0);

            if (logger.isTraceEnabled())
                logger.trace("Release model " + deviceId);
            library.tdReleaseString(modelPointer);
        }

        // Protocol
        {
            if (logger.isTraceEnabled())
                logger.trace("Get protocol " + deviceId);
            Pointer protocolPointer = library.tdGetProtocol(deviceId);
            protocol = protocolPointer.getString(0);

            if (logger.isTraceEnabled())
                logger.trace("Release protocol " + deviceId);
            library.tdReleaseString(protocolPointer);
        }

        deviceType = library.tdGetDeviceType(deviceId);
    }

    @Override
    public int getDeviceId() {
        return deviceId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public int getDeviceType() {
        return deviceType;
    }

    @Override
    public int getStatus() {
        if (logger.isTraceEnabled())
            logger.trace("Get status " + deviceId);

        return library.tdLastSentCommand(deviceId, deviceHandler.getSupportedMethods());
    }

    /**
     * @return library
     */
    public TellstickCoreLibrary getLibrary() {
        return library;
    }

    /**
     * @return device handler
     */
    public DeviceHandler getDeviceHandler() {
        return deviceHandler;
    }

    @Override
    public String toString() {
        return "Device [deviceId=" + deviceId + ", name=" + name + ", model="
                + model + ", protocol=" + protocol + ", deviceType="
                + deviceType + ", getClass()=" + getClass().getSimpleName() + "]";
    }

    @Override
    public int compareTo(Device device) {
        // Device or device name is null
        if (device == null || device.getName() == null) {
            return getName().compareTo("");
        }
        // Compare device names
        else {
            return getName().compareTo(device.getName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return deviceId == device.getDeviceId();
    }

    @Override
    public int hashCode() {
        return deviceId;
    }
}
