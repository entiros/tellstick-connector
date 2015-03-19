package se.entiros.tellstick.core.device.impl;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.device.*;

import java.util.ArrayList;
import java.util.List;


public class GroupDeviceImpl extends AbstractDevice implements GroupDevice {
    private final List<Device> devices = new ArrayList<Device>();

    public GroupDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);

        if (logger.isTraceEnabled())
            logger.trace("Get methods " + deviceId);

        int methods = getLibrary().tdMethods(deviceId, deviceHandler.getSupportedMethods());

        // Bell
        if ((methods & TellstickCoreLibrary.TELLSTICK_BELL) > 0)
            devices.add(new BellDeviceImpl(deviceHandler, deviceId));

        // Dim
        if ((methods & TellstickCoreLibrary.TELLSTICK_DIM) > 0)
            devices.add(new DimmableDeviceImpl(deviceHandler, deviceId));

            // On/Off
        else if ((methods & TellstickCoreLibrary.TELLSTICK_TURNON) > 0 && (methods & TellstickCoreLibrary.TELLSTICK_TURNOFF) > 0)
            devices.add(new OnOffDeviceImpl(deviceHandler, deviceId));

        // Up/Down
        if ((methods & TellstickCoreLibrary.TELLSTICK_UP) > 0 && (methods & TellstickCoreLibrary.TELLSTICK_DOWN) > 0 && (methods & TellstickCoreLibrary.TELLSTICK_STOP) > 0)
            devices.add(new UpDownDeviceImpl(deviceHandler, deviceId));

        // Scene
        if ((methods & TellstickCoreLibrary.TELLSTICK_EXECUTE) > 0)
            devices.add(new SceneDeviceImpl(deviceHandler, deviceId));
    }

    @Override
    public List<Device> getDevices() {
        return devices;
    }

    @Override
    public void on() {
        if (logger.isDebugEnabled())
            logger.debug("ON " + toString());

        for (Device device : getDevices()) {
            try {
                // On/off device
                if (device instanceof OnOffDevice) {
                    ((OnOffDevice) device).on();
                }
                // Dimmable device
                else if (device instanceof DimmableDevice) {
                    ((DimmableDevice) device).on();
                }
            } catch (DeviceException e) {
                logger.error("Failed to turn on device #" + device.getDeviceId());
            }
        }
    }

    @Override
    public void off() {
        if (logger.isDebugEnabled())
            logger.debug("ON " + toString());

        for (Device device : getDevices()) {
            try {
                // On/off device
                if (device instanceof OnOffDevice) {
                    ((OnOffDevice) device).off();
                }
                // Dimmable device
                else if (device instanceof DimmableDevice) {
                    ((DimmableDevice) device).off();
                }
            } catch (DeviceException e) {
                logger.error("Failed to turn on device #" + device.getDeviceId());
            }
        }
    }

    @Override
    public boolean isOn() {
        throw new RuntimeException("Not implemented for GroupDevice");
    }
}
