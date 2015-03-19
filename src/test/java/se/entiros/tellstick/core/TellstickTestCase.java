package se.entiros.tellstick.core;

import se.entiros.tellstick.core.device.Device;
import se.entiros.tellstick.core.device.DeviceHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

@Ignore
public class TellstickTestCase {
    @Test
    public void testGetDevices() {
        Tellstick tellstick = new Tellstick();
        try {
            tellstick.start();

            DeviceHandler deviceHandler = tellstick.getDeviceHandler();
            List<Device> devices = deviceHandler.getDevices();

            for (Device device : devices) {
                System.out.println(device);
            }
        } finally {
            tellstick.stop();
        }
    }

}
