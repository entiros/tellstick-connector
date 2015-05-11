/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.modules.tellstick;

import org.apache.log4j.Logger;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.lifecycle.Stop;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.callback.SourceCallback;
import org.mule.api.callback.StopSourceCallback;
import se.entiros.tellstick.core.Tellstick;
import se.entiros.tellstick.core.TellstickException;
import se.entiros.tellstick.core.device.*;
import se.entiros.tellstick.core.proxy.DeviceProxy;
import se.entiros.tellstick.core.rawdevice.RawDeviceEventListener;
import se.entiros.tellstick.core.sensor.Sensor;
import se.entiros.tellstick.core.sensor.SensorEventListener;

import java.util.List;
import java.util.Map;

/**
 * Tellstick Anypoint Connector
 *
 * @author Petter Alstermark, Entiros AB
 */
@Module(name = "tellstick", friendlyName = "Tellstick", schemaVersion = "current", minMuleVersion = "3.5.0")
public class TellstickConnector {
    private static final Logger logger = Logger.getLogger(TellstickConnector.class);

    private Tellstick tellstick;
    private DeviceProxy proxy = new DeviceProxy();

    /**
     * A built in queue will trigger device commands (on,  off, ...) giver number of times.
     * Some signals may be missed by receiving devices (interference by other devices etc), so repeating the command is
     * often helpful.
     */
    @Configurable
    @Default("1")
    private Integer tries;

    /**
     * Delay in milliseconds between device calls
     */
    @Configurable
    @Default("500")
    private Integer delay;

    /**
     * @Initialise is missing i Devkit 3.6.0, doing it in constructor
     */
    public TellstickConnector() {
        tellstick = new Tellstick();
        tellstick.start();
    }

    /**
     * @return number of tries for device commands
     */
    public Integer getTries() {
        return tries;
    }

    /**
     * @param tries number of tries for device commands
     */
    public void setTries(Integer tries) {
        this.tries = tries;

        proxy.setTries(tries);
    }

    /**
     * @return delay in milliseconds between device calls
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * @param delay delay in milliseconds between device calls
     */
    public void setDelay(Integer delay) {
        this.delay = delay;

        proxy.setCallDelay(delay);
    }

    @Stop
    public void doStop() {
        if (this.tellstick != null) {
            tellstick.stop();
            tellstick = null;
        }
    }

    /**
     * Get available devices
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:get-devices}
     *
     * @return device list
     */
    @Processor
    public List<Device> getDevices() {
        return tellstick.getDeviceHandler().getDevices();
    }

    /**
     * Get device by ID
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:get-device}
     *
     * @param deviceId device ID
     * @return device
     * @throws se.entiros.tellstick.core.device.DeviceNotSupportedException if the device type is not supported by the connector
     */
    @Processor
    public Device getDevice(int deviceId) throws DeviceNotSupportedException {
        return tellstick.getDeviceHandler().getDevice(deviceId);
    }

    /**
     * Get device by name
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:get-device-by-name}
     *
     * @param deviceName device name
     * @return device
     */
    @Processor
    public Device getDeviceByName(String deviceName) {
        for (Device device : tellstick.getDeviceHandler().getDevices()) {
            if (deviceName.equalsIgnoreCase(device.getName()))
                return device;
        }
        return null;
    }

    /**
     * Create device
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:create-device}
     *
     * @param deviceName device name
     * @param model      device model
     * @param protocol   device protocol
     * @param parameters device parameters
     * @return created device
     * @throws TellstickException          if device could not be created
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     */
    @Processor
    public Device createDevice(String deviceName, String model, String protocol, Map<String, String> parameters) throws TellstickException, DeviceNotSupportedException {
        return tellstick.getDeviceHandler().createDevice(deviceName, model, protocol, parameters);
    }

    /**
     * Remove device
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:remove-device}
     *
     * @param deviceId device ID
     * @return true if device was removed, false otherwise
     */
    @Processor
    public boolean removeDevice(int deviceId) {
        return tellstick.getDeviceHandler().removeDevice(deviceId);
    }

    /**
     * Device changed source
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:device-changed}
     *
     * @param callback the callback where the updated device will be processed
     * @return stop source callback
     */
    @Source
    public StopSourceCallback deviceChanged(final SourceCallback callback) {
        // Device Listener
        final DeviceEventListener listener = new DeviceEventListener() {
            @Override
            public void deviceChanged(int deviceId, Device device) {
                try {
                    callback.process(device);
                } catch (Exception e) {
                    logger.error(String.format("Error when processing device changed event for device #%d", deviceId));
                }
            }

            @Override
            public void deviceAdded(int deviceId, Device device) {
            }

            @Override
            public void deviceRemoved(int deviceId) {
            }
        };

        // Start listener
        tellstick.getDeviceHandler().addDeviceEventListener(listener);

        // Stop listener
        return new StopSourceCallback() {
            @Override
            public void stop() throws Exception {
                tellstick.getDeviceHandler().removeDeviceEventListener(listener);
            }
        };
    }

    /**
     * Device added source
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:device-added}
     *
     * @param callback the callback where the added device will be processed
     * @return stop source callback
     */
    @Source
    public StopSourceCallback deviceAdded(final SourceCallback callback) {
        // Device Listener
        final DeviceEventListener listener = new DeviceEventListener() {
            @Override
            public void deviceChanged(int deviceId, Device device) {
            }

            @Override
            public void deviceAdded(int deviceId, Device device) {
                try {
                    callback.process(device);
                } catch (Exception e) {
                    logger.error(String.format("Error when processing device added event for device #%d", deviceId));
                }
            }

            @Override
            public void deviceRemoved(int deviceId) {
            }
        };

        // Start listener
        tellstick.getDeviceHandler().addDeviceEventListener(listener);

        // Stop listener
        return new StopSourceCallback() {
            @Override
            public void stop() throws Exception {
                tellstick.getDeviceHandler().removeDeviceEventListener(listener);
            }
        };
    }

    /**
     * Device removed source
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:device-removed}
     *
     * @param callback the callback where the ID for the removed device will be processed
     * @return stop source callback
     */
    @Source
    public StopSourceCallback deviceRemoved(final SourceCallback callback) {
        // Device Listener
        final DeviceEventListener listener = new DeviceEventListener() {
            @Override
            public void deviceChanged(int deviceId, Device device) {
            }

            @Override
            public void deviceAdded(int deviceId, Device device) {
            }

            @Override
            public void deviceRemoved(int deviceId) {
                try {
                    callback.process(deviceId);
                } catch (Exception e) {
                    logger.error(String.format("Error when processing device removed event for device #%d", deviceId));
                }
            }
        };

        // Start listener
        tellstick.getDeviceHandler().addDeviceEventListener(listener);

        // Stop listener
        return new StopSourceCallback() {
            @Override
            public void stop() throws Exception {
                tellstick.getDeviceHandler().removeDeviceEventListener(listener);
            }
        };
    }

    /**
     * Turn device on. One of device, deviceId or deviceName is required
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:on}
     *
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     * @throws DeviceException             if unable to perform device action
     */
    @Processor(name = "on")
    public void deviceOn(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        getDevice(device, deviceId, deviceName, OnOffDevice.class).on();
    }

    /**
     * Turn device off. One of device, deviceId or deviceName is required
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:off}
     *
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     * @throws DeviceException             if unable to perform device action
     */
    @Processor(name = "off")
    public void deviceOff(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        getDevice(device, deviceId, deviceName, OnOffDevice.class).off();
    }

    /**
     * Issue device up command. One of device, deviceId or deviceName is required
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:up}
     *
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     * @throws DeviceException             if unable to perform device action
     */
    @Processor(name = "up")
    public void deviceUp(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        getDevice(device, deviceId, deviceName, UpDownDevice.class).up();
    }

    /**
     * Issue device down command. One of device, deviceId or deviceName is required
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:down}
     *
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     * @throws DeviceException             if unable to perform device action
     */
    @Processor(name = "down")
    public void deviceDown(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        getDevice(device, deviceId, deviceName, UpDownDevice.class).down();
    }

    /**
     * Issue device stop command. One of device, deviceId or deviceName is required
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:stop}
     *
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     * @throws DeviceException             if unable to perform device action
     */
    @Processor(name = "stop")
    public void deviceStop(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        getDevice(device, deviceId, deviceName, UpDownDevice.class).stop();
    }

    /**
     * Issue device bell command. One of device, deviceId or deviceName is required
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:bell}
     *
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     * @throws DeviceException             if unable to perform device action
     */
    @Processor(name = "bell")
    public void deviceBell(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        getDevice(device, deviceId, deviceName, BellDevice.class).bell();
    }

    /**
     * Set device dim level. One of device, deviceId or deviceName is required
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:dim}
     *
     * @param level      dim level (0-255)
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     * @throws DeviceException             if unable to perform device action
     */
    @Processor(name = "dim")
    public void deviceDim(Integer level, @Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        getDevice(device, deviceId, deviceName, DimmableDevice.class).dim(level);
    }

    /**
     * Issue device execute command. One of device, deviceId or deviceName is required
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:execute}
     *
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @throws DeviceNotSupportedException if the device type is not supported by the connector
     * @throws DeviceException             if unable to perform device action
     */
    @Processor(name = "execute")
    public void deviceExecute(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        getDevice(device, deviceId, deviceName, SceneDevice.class).execute();
    }

    /**
     * Raw event source
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:raw-event}
     *
     * @param callback   the callback where raw event will be processed as parameter {@link java.util.Map}
     * @param parameters expected parameters for a event to trigger (null or empty for all events)
     * @return stop source callback
     */
    @Source
    public StopSourceCallback rawEvent(final SourceCallback callback, @Optional final Map<String, String> parameters) {
        // Device Listener
        final RawDeviceEventListener listener = new RawDeviceEventListener() {
            @Override
            public void rawDeviceEvent(Map<String, String> params) {
                try {
                    // Process if expected parameters matches
                    if (matches(parameters, params)) {
                        callback.process(params);
                    }
                    // Debug log if filtered
                    else if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Filtered raw event: %s", params));
                    }
                } catch (Exception e) {
                    logger.error(String.format("Error when processing raw event '%s'", params));
                }
            }
        };

        // Start listener
        tellstick.getRawDeviceHandler().addRawDeviceEventListener(listener);

        // Stop listener
        return new StopSourceCallback() {
            @Override
            public void stop() throws Exception {
                tellstick.getRawDeviceHandler().removeRawDeviceEventListener(listener);
            }
        };
    }

    /**
     * Get sensors
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:get-sensors}
     *
     * @return sensors
     */
    @Processor
    public List<Sensor> getSensors() {
        return tellstick.getSensorHandler().getSensors();
    }

    /**
     * Sensor event source
     * <p/>
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:sensor-event}
     *
     * @param callback the callback where sensor event will be processed as {@link se.entiros.tellstick.core.sensor.Sensor}
     * @param id       expected ID for event to trigger
     * @param protocol expected protocol for event to trigger
     * @param model    expected model for event to trigger
     * @param dataType expected dataType for event to trigger
     * @param value    expected value for event to trigger
     * @return stop source callback
     */
    @Source
    public StopSourceCallback sensorEvent(final SourceCallback callback,
                                          @Optional final Integer id,
                                          @Optional final String protocol,
                                          @Optional final String model,
                                          @Optional final Integer dataType,
                                          @Optional final String value) {
        // Device Listener
        final SensorEventListener listener = new SensorEventListener() {
            @Override
            public void sensorEvent(Sensor sensor) {
                try {
                    // Process if expected parameters matches
                    if (matches(id, sensor.getId()) &&
                            matches(protocol, sensor.getProtocol()) &&
                            matches(model, sensor.getModel()) &&
                            matches(dataType, sensor.getDataType()) &&
                            matches(value, sensor.getValue())) {
                        callback.process(sensor);
                    }
                    // Debug log if filtered
                    else if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Filtered sensor event: %s", sensor));
                    }
                } catch (Exception e) {
                    logger.error(String.format("Error when processing sensor event '%s'", sensor));
                }
            }
        };

        // Start listener
        tellstick.getSensorHandler().addSensorEventListener(listener);

        // Stop listener
        return new StopSourceCallback() {
            @Override
            public void stop() throws Exception {
                tellstick.getSensorHandler().removeSensorEventListener(listener);
            }
        };
    }

    /**
     * @param expected   expected parameters
     * @param parameters parameters
     * @return true if all expected parameters matches in parameters, or if expected is null
     */
    protected static boolean matches(Map<String, String> expected, Map<String, String> parameters) {
        if (expected == null)
            return true;

        for (Map.Entry<String, String> entry : expected.entrySet()) {
            if (!entry.getValue().equals(parameters.get(entry.getKey())))
                return false;
        }

        return true;
    }

    /**
     * @param expected expected value
     * @param value    value
     * @return true if expected value equals value or null
     */
    protected static boolean matches(Integer expected, Integer value) {
        return expected == null || expected.equals(value);
    }

    /**
     * @param expected expected value
     * @param value    value
     * @return true if expected value equals value or null
     */
    protected static boolean matches(String expected, String value) {
        return expected == null || expected.equals(value);
    }

    /**
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @return device based on ID, name or provided device
     * @throws DeviceNotSupportedException
     */
    @SuppressWarnings("unchecked")
    protected <T extends Device> T getDevice(Device device, Integer deviceId, String deviceName, Class<T> deviceClass) throws DeviceNotSupportedException {
        if (deviceId == null && deviceName == null && device == null)
            throw new RuntimeException("One of deviceId, deviceName or device have to be specified");

        T d;

        // Return device
        if (device != null) {
            d = (T) device;
        }
        // By Device ID
        else if (deviceId != null) {
            d = (T) getDevice(deviceId);
        }
        // By Device Name
        else {
            d = (T) getDeviceByName(deviceName);
        }

        return proxy.proxy(d, deviceClass);
    }

}