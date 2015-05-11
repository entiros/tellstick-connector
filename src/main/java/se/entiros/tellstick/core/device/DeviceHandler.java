/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.TellstickException;
import se.entiros.tellstick.core.device.impl.*;
import se.entiros.tellstick.core.util.Runner;
import se.entiros.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Device Handler
 *
 * @author Petter Alstermark, Entiros AB
 */
public class DeviceHandler {
    private static final Logger logger = Logger.getLogger(DeviceHandler.class);

    private final Set<DeviceEventListener> deviceEventListeners = new CopyOnWriteArraySet<DeviceEventListener>();

    private final TellstickCoreLibrary library;
    private final int supportedMethods;

    private final Runner eventRunner;

    private int deviceEventCallbackId = -1;
    private int deviceChangeEventCallbackId = -1;

    @SuppressWarnings("FieldCanBeLocal")
    private TDDeviceEventListener deviceEventListener;
    @SuppressWarnings("FieldCanBeLocal")
    private TDDeviceChangeEventListener deviceChangeEventListener;

    /**
     * @param library          tellstick core library
     * @param supportedMethods supported methods
     */
    public DeviceHandler(TellstickCoreLibrary library, int supportedMethods) {
        this.library = library;
        this.supportedMethods = supportedMethods;
        this.eventRunner = new Runner();
    }

    /**
     * @param listener device event listener to add
     * @return true if added
     */
    public boolean addDeviceEventListener(DeviceEventListener listener) {
        return deviceEventListeners.add(listener);
    }

    /**
     * @param listener device event listener to be removed
     * @return true if removed
     */
    public boolean removeDeviceEventListener(DeviceEventListener listener) {
        return deviceEventListeners.remove(listener);
    }

    /**
     * Start
     */
    public void start() {
        // Device Event Listener
        logger.debug("Starting Device Event Listener");
        deviceEventListener = new TDDeviceEventListener();
        deviceEventCallbackId = library.tdRegisterDeviceEvent(deviceEventListener, null);

        // Device Change Event Listener
        logger.debug("Starting Device Change Event Listener");
        deviceChangeEventListener = new TDDeviceChangeEventListener();
        deviceChangeEventCallbackId = library.tdRegisterDeviceChangeEvent(deviceChangeEventListener, null);

        // Start event runner
        eventRunner.start();
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop event runner
        eventRunner.stop();

        // Stop Device Event Listener
        if (deviceEventCallbackId != -1) {
            logger.debug("Stopping Device Event Listener");
            library.tdUnregisterCallback(deviceEventCallbackId);
            deviceEventCallbackId = -1;
        }

        // Stop Device Change Event Listener
        if (deviceChangeEventCallbackId != -1) {
            logger.debug("Stopping Device Change Event Listener");
            library.tdUnregisterCallback(deviceChangeEventCallbackId);
            deviceChangeEventCallbackId = -1;
        }
    }

    /**
     * @return available devices
     */
    public List<Device> getDevices() {
        logger.trace("Get devices");

        List<Device> devices = new ArrayList<Device>();

        logger.trace("Get number of devices");
        int numDevices = library.tdGetNumberOfDevices();
        for (int i = 0; i < numDevices; i++) {
            logger.trace("Get device id " + i);

            int deviceId = library.tdGetDeviceId(i);

            try {
                devices.add(getDevice(deviceId));
            } catch (DeviceNotSupportedException e) {
                logger.warn("Device #" + deviceId + " is not supported.");
            }
        }

        return devices;
    }

    /**
     * @param deviceId device ID
     * @return device for device ID
     * @throws DeviceNotSupportedException
     */
    public Device getDevice(int deviceId) throws DeviceNotSupportedException {
        return createDevice(deviceId);
    }

    /**
     * Create a Java Device class (not new device in Tellstick)
     *
     * @param deviceId device ID
     * @return device
     * @throws DeviceNotSupportedException
     */
    protected Device createDevice(int deviceId) throws DeviceNotSupportedException {
        logger.trace("Create device " + deviceId);
        int methods = library.tdMethods(deviceId, getSupportedMethods());

        logger.trace("Get device type " + deviceId);
        int type = library.tdGetDeviceType(deviceId);

        // Group Device
        if (type == TellstickCoreLibrary.TELLSTICK_TYPE_GROUP) {
            return new GroupDeviceImpl(this, deviceId);
        }

        // Scene Device
        else if (type == TellstickCoreLibrary.TELLSTICK_TYPE_SCENE) {
            return new SceneDeviceImpl(this, deviceId);
        }

        // Bell Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_BELL) > 0) {
            return new BellDeviceImpl(this, deviceId);
        }

        // Dimmable Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_DIM) > 0) {
            return new DimmableDeviceImpl(this, deviceId);
        }

        // Up / Down Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_UP) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_DOWN) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_STOP) > 0) {
            return new UpDownDeviceImpl(this, deviceId);
        }

        // On / Off Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_TURNON) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_TURNOFF) > 0) {
            return new OnOffDeviceImpl(this, deviceId);
        }

        // Scene Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_EXECUTE) > 0) {
            return new SceneDeviceImpl(this, deviceId);
        }

        // Not supported
        else {
            return new UnsupportedDevice(this, deviceId);
        }
    }

    /**
     * @param name       name
     * @param model      model
     * @param protocol   protocol
     * @param parameters parameters
     * @return created device
     * @throws TellstickException
     * @throws DeviceNotSupportedException
     */
    public Device createDevice(String name, String model, String protocol, Map<String, String> parameters) throws TellstickException, DeviceNotSupportedException {
        logger.trace("Create add device");

        int deviceId = library.tdAddDevice();

        // Unable to create device
        if (deviceId <= 0)
            throw new TellstickException("Unable to create device,", library, deviceId);

        // Set Name
        if (!library.tdSetName(deviceId, name))
            logger.error("Unable to set device name");

        // Set Model
        if (!library.tdSetModel(deviceId, model))
            logger.error("Unable to set device model");

        // Set Protocol
        if (!library.tdSetProtocol(deviceId, protocol))
            logger.error("Unable to set device protocol");

        // Set Parameters
        for (Entry<String, String> entry : parameters.entrySet()) {
            // Set parameter
            if (!library.tdSetDeviceParameter(deviceId, entry.getKey(), entry.getValue()))
                logger.error("Unable to set parameter '" + entry.getKey() + "' to '" + entry.getValue() + "'");
        }

        return getDevice(deviceId);
    }

    /**
     * @param deviceId device ID for device to be removed
     * @return true if device was removed
     */
    public boolean removeDevice(int deviceId) {
        return library.tdRemoveDevice(deviceId);
    }

    /**
     * @param device device to be removed
     * @return true if device was removed
     */
    public boolean removeDevice(Device device) {
        return removeDevice(device.getDeviceId());
    }

    /**
     * @return the supportedMethods
     */
    public int getSupportedMethods() {
        return supportedMethods;
    }

    /**
     * @return the library
     */
    public TellstickCoreLibrary getLibrary() {
        return library;
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     * @param device   device
     */
    private void fireDeviceChanged(final int deviceId, final Device device) {
        eventRunner.offer(new Runnable() {
            @Override
            public void run() {
                for (DeviceEventListener listener : deviceEventListeners) {
                    listener.deviceChanged(deviceId, device);
                }
            }
        });
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     * @param device   device
     */
    private void fireDeviceAdded(final int deviceId, final Device device) {
        eventRunner.offer(new Runnable() {
            @Override
            public void run() {
                for (DeviceEventListener listener : deviceEventListeners) {
                    listener.deviceAdded(deviceId, device);
                }
            }
        });
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     */
    private void fireDeviceRemoved(final int deviceId) {
        eventRunner.offer(new Runnable() {
            @Override
            public void run() {
                for (DeviceEventListener listener : deviceEventListeners) {
                    listener.deviceRemoved(deviceId);
                }
            }
        });
    }

    /**
     * Device Event Listener
     */
    private class TDDeviceEventListener implements TellstickCoreLibrary.TDDeviceEvent {
        @Override
        public void event(int deviceId, int method, String data, int callbackId, Pointer context) {
            if (logger.isTraceEnabled())
                logger.trace(String.format("Event: %d, %d", deviceId, method));

            // Debug log
            if (logger.isDebugEnabled())
                logger.debug(String.format("DeviceId=%d, method=%d, data=%s", deviceId, method, data));

            try {
                fireDeviceChanged(deviceId, getDevice(deviceId));
            } catch (DeviceNotSupportedException e) {
                logger.warn(String.format("Device #%d is not supported.", deviceId));
            }
        }
    }

    /**
     * Device Change Event Listener
     */
    private class TDDeviceChangeEventListener implements TellstickCoreLibrary.TDDeviceChangeEvent {
        @Override
        public void event(int deviceId, int changeEvent, int changeType, int callbackId, Pointer context) {
            if (logger.isTraceEnabled())
                logger.trace(String.format("Event: %d", deviceId));

            // Debug log
            if (logger.isDebugEnabled())
                logger.debug(String.format("DeviceId=%d, changeEvent=%d, changeType=%d", deviceId, changeEvent, changeType));

            // Device Changed
            if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_CHANGED || changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_STATE_CHANGED) {
                try {
                    fireDeviceChanged(deviceId, getDevice(deviceId));
                } catch (DeviceNotSupportedException e) {
                    logger.warn(String.format("Device #%d is not supported.", deviceId));
                }
            }
            // Device Added
            else if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_ADDED) {
                try {
                    fireDeviceAdded(deviceId, getDevice(deviceId));
                } catch (DeviceNotSupportedException e) {
                    logger.warn(String.format("Device #%d is not supported.", deviceId));
                }
            }
            // Device Removed
            else if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_REMOVED) {
                fireDeviceRemoved(deviceId);
            }
            // Unhandled event
            else {
                logger.error(String.format("Unhandled Device Change Event '%d'", changeEvent));
            }
        }
    }
}
