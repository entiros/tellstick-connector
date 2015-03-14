/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.alstermark.modules.tellstick;

import com.alstermark.tellstick.core.Tellstick;
import com.alstermark.tellstick.core.TellstickException;
import com.alstermark.tellstick.core.device.*;
import com.alstermark.tellstick.core.rawdevice.RawDeviceEventListener;
import com.alstermark.tellstick.core.sensor.Sensor;
import com.alstermark.tellstick.core.sensor.SensorEventListener;
import org.apache.log4j.Logger;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.lifecycle.Stop;
import org.mule.api.annotations.param.Optional;
import org.mule.api.callback.SourceCallback;
import org.mule.api.callback.StopSourceCallback;

import java.util.List;
import java.util.Map;

/**
 * Tellstick Anypoint Connector
 *
 * @author Petter Alstermark
 */
@Connector(name = "tellstick", friendlyName = "Tellstick", schemaVersion = "current")
public class TellstickConnector {
    private static final Logger logger = Logger.getLogger(TellstickConnector.class);

    private Tellstick tellstick;


    @Start
    public void start() {
        tellstick = new Tellstick();
        tellstick.start();
    }

    @Stop
    public void stop() {
        if (this.tellstick != null) {
            tellstick.stop();
            tellstick = null;
        }
    }

    /**
     * Get available devices
     *
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:get-devices}
     *
     * @return device list
     */
    @Processor
    public List<Device> getDevices() {
        return tellstick.getDeviceHandler().getDevices();
    }

    @Processor
    public Device getDevice(int deviceId) throws DeviceNotSupportedException {
        return tellstick.getDeviceHandler().getDevice(deviceId);
    }

    @Processor
    public Device getDeviceByName(String deviceName) {
        for (Device device : tellstick.getDeviceHandler().getDevices()) {
            if (deviceName.equalsIgnoreCase(device.getName()))
                return device;
        }
        return null;
    }

    @Processor
    public Device createDevice(String name, String model, String protocol, Map<String, String> parameters) throws TellstickException, DeviceNotSupportedException {
        return tellstick.getDeviceHandler().createDevice(name, model, protocol, parameters);
    }

    @Processor
    public boolean removeDevice(int deviceId) {
        return tellstick.getDeviceHandler().removeDevice(deviceId);
    }

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

    @Processor
    public void deviceOn(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        ((OnOffDevice) getDevice(device, deviceId, deviceName)).on();
    }

    @Processor
    public void deviceOff(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        ((OnOffDevice) getDevice(device, deviceId, deviceName)).off();
    }

    @Processor
    public void deviceUp(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        ((UpDownDevice) getDevice(device, deviceId, deviceName)).up();
    }

    @Processor
    public void deviceDown(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        ((UpDownDevice) getDevice(device, deviceId, deviceName)).down();
    }

    @Processor
    public void deviceStop(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        ((UpDownDevice) getDevice(device, deviceId, deviceName)).stop();
    }

    @Processor
    public void deviceBell(@Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        ((BellDevice) getDevice(device, deviceId, deviceName)).bell();
    }

    @Processor
    public void deviceDim(Integer level, @Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        ((DimmableDevice) getDevice(device, deviceId, deviceName)).dim(level);
    }

    @Processor
    public void deviceExecute(Integer level, @Optional Device device, @Optional Integer deviceId, @Optional String deviceName) throws DeviceNotSupportedException, DeviceException {
        ((SceneDevice) getDevice(device, deviceId, deviceName)).execute();
    }

    @Source
    public StopSourceCallback rawEvent(final SourceCallback callback) {
        // Device Listener
        final RawDeviceEventListener listener = new RawDeviceEventListener() {
            @Override
            public void rawDeviceEvent(Map<String, String> parameters) {
                try {
                    callback.process(parameters);
                } catch (Exception e) {
                    logger.error(String.format("Error when processing raw event '%s'", parameters));
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

    @Processor
    public List<Sensor> getSensors() {
        return tellstick.getSensorHandler().getSensors();
    }

    @Source
    public StopSourceCallback sensorEvent(final SourceCallback callback) {
        // Device Listener
        final SensorEventListener listener = new SensorEventListener() {

            @Override
            public void sensorEvent(Sensor sensor) {
                try {
                    callback.process(sensor);
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
     * @param device     device
     * @param deviceId   device ID
     * @param deviceName device name
     * @return device based on ID, name or provided device
     * @throws DeviceNotSupportedException
     */
    @SuppressWarnings("unchecked")
    protected <T extends Device> T getDevice(Device device, Integer deviceId, String deviceName) throws DeviceNotSupportedException {
        if (deviceId == null && deviceName == null && device == null)
            throw new RuntimeException("One of deviceId, deviceName or device have to be specified");

        // Return device
        if (device != null) {
            return (T) device;
        }
        // By Device ID
        else if (deviceId != null) {
            return (T) tellstick.getDeviceHandler().getDevice(deviceId);
        }
        // By Device Name
        else {
            return (T) getDeviceByName(deviceName);
        }
    }

}