/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.modules.tellstick;

import org.junit.Before;
import org.junit.Ignore;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import se.entiros.tellstick.core.device.Device;
import org.mule.modules.tests.ConnectorTestCase;

import org.junit.Test;
import se.entiros.tellstick.core.device.DeviceException;
import se.entiros.tellstick.core.device.OnOffDevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;

public class TellstickConnectorTest extends ConnectorTestCase {
    private ReceiveThread deviceAddedThread = new ReceiveThread("vm://device-added");
    private ReceiveThread deviceChangedThread = new ReceiveThread("vm://device-changed");
    private ReceiveThread deviceRemovedThread = new ReceiveThread("vm://device-removed");

    @Before
    public void before() {
        deviceAddedThread.reset();
        deviceChangedThread.reset();
        deviceRemovedThread.reset();
    }

    @Override
    protected String getConfigResources() {
        return "tellstick-config.xml";
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetDevices() throws Exception {
        // Get devices
        List<Device> devices = (List<Device>) runFlow("get-devices").getMessage().getPayload();
        assertNotNull(devices);
        assertTrue("Expecting at least one device", devices.size() > 0);

        int deviceId = devices.get(0).getDeviceId();
        String deviceName = devices.get(0).getName();
        Device device;

        // Get device by ID
        device = (Device) runFlow("get-device", deviceId).getMessage().getPayload();
        assertNotNull(device);
        assertEquals(deviceName, device.getName());

        // Get device by name
        device = (Device) runFlow("get-device-by-name", deviceName).getMessage().getPayload();
        assertNotNull(device);
        assertEquals(deviceId, device.getDeviceId());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreate() throws Exception {
        int devicesBefore = ((List<Device>) runFlow("get-devices").getMessage().getPayload()).size();

        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("deviceName", "test-device");
        payload.put("model", "codeswitch");
        payload.put("protocol", "arctech");

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("house", "B");
        parameters.put("unit", "2");
        payload.put("parameters", parameters);

        // Create device
        Device device = (Device) runFlow("create-device", payload).getMessage().getPayload();
        assertNotNull(device);
        assertTrue(device.getDeviceId() > 0);

        try {
            // Expect device add and multiple changed events
            deviceAddedThread.waitUntil(1);
            deviceChangedThread.waitUntilAtLeast(1);

            // Number of devices is before + 1
            assertEquals(devicesBefore + 1, ((List<Device>) runFlow("get-devices").getMessage().getPayload()).size());
        } finally {
            // Remove device (returns true if removed)
            assertTrue((Boolean) runFlow("remove-device", device.getDeviceId()).getMessage().getPayload());
        }

        // Expect device removed event
        deviceRemovedThread.waitUntil(1);

        int devicesAfter = ((List<Device>) runFlow("get-devices").getMessage().getPayload()).size();
        assertEquals(devicesBefore, devicesAfter);
    }

    @Test
    @Ignore // requires Tellstick
    public void testOnOff() throws Exception {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("deviceName", "test-device");
        payload.put("model", "codeswitch");
        payload.put("protocol", "arctech");

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("house", "B");
        parameters.put("unit", "2");
        payload.put("parameters", parameters);

        // Create device
        Device device = (Device) runFlow("create-device", payload).getMessage().getPayload();
        assertNotNull(device);
        assertTrue(device.getDeviceId() > 0);

        try {
            runFlow("off", device);
            assertFalse(((OnOffDevice) runFlow("get-device", device.getDeviceId()).getMessage().getPayload()).isOn());

            runFlow("on", device.getDeviceId());
            assertTrue(((OnOffDevice) runFlow("get-device", device.getDeviceId()).getMessage().getPayload()).isOn());

            runFlow("off", device);
            assertFalse(((OnOffDevice) runFlow("get-device", device.getDeviceId()).getMessage().getPayload()).isOn());
        } finally {
            // Remove device (returns true if removed)
            assertTrue((Boolean) runFlow("remove-device", device.getDeviceId()).getMessage().getPayload());
        }
    }

    @Test
    @Ignore // requires Tellstick - I don't own a "up/down" device have been unable to test this
    public void testUpDown() throws Exception {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("deviceName", "test-device");
        payload.put("model", "codeswitch:roxcore");
        payload.put("protocol", "brateck");

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("house", "B");
        parameters.put("unit", "2");
        payload.put("parameters", parameters);

        // Create device
        Device device = (Device) runFlow("create-device", payload).getMessage().getPayload();
        assertNotNull(device);
        assertTrue(device.getDeviceId() > 0);

        try {
            runFlow("up", device);
            runFlow("down", device.getName());
        } finally {
            // Remove device (returns true if removed)
            assertTrue((Boolean) runFlow("remove-device", device.getDeviceId()).getMessage().getPayload());
        }
    }

    @Test
    @Ignore // requires Tellstick - I don't own a "bell" device have been unable to test this
    public void testBell() throws Exception {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("deviceName", "test-device");
        payload.put("model", "codeswitch");
        payload.put("protocol", "arctech");

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("house", "B");
        parameters.put("unit", "2");
        payload.put("parameters", parameters);

        // Create device
        Device device = (Device) runFlow("create-device", payload).getMessage().getPayload();
        assertNotNull(device);
        assertTrue(device.getDeviceId() > 0);

        try {
            runFlow("bell", device.getName());
        } finally {
            // Remove device (returns true if removed)
            assertTrue((Boolean) runFlow("remove-device", device.getDeviceId()).getMessage().getPayload());
        }
    }

    @Test
    @Ignore // requires Tellstick - I don't own a "dim" device have been unable to test this
    public void testDim() throws Exception {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("deviceName", "test-device");
        payload.put("model", "codeswitch");
        payload.put("protocol", "arctech");

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("house", "B");
        parameters.put("unit", "2");
        payload.put("parameters", parameters);

        // Create device
        Device device = (Device) runFlow("create-device", payload).getMessage().getPayload();
        assertNotNull(device);
        assertTrue(device.getDeviceId() > 0);

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("deviceName", device.getName());
            params.put("level", 125);
            runFlow("dim", params);
        } finally {
            // Remove device (returns true if removed)
            assertTrue((Boolean) runFlow("remove-device", device.getDeviceId()).getMessage().getPayload());
        }
    }


    @Test
    @Ignore // requires Tellstick - I don't own a "execute" device have been unable to test this
    public void testExecute() throws Exception {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("deviceName", "test-device");
        payload.put("model", "codeswitch");
        payload.put("protocol", "arctech");

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("house", "B");
        parameters.put("unit", "2");
        payload.put("parameters", parameters);

        // Create device
        Device device = (Device) runFlow("create-device", payload).getMessage().getPayload();
        assertNotNull(device);
        assertTrue(device.getDeviceId() > 0);

        try {
            runFlow("execute", device.getName());
        } finally {
            // Remove device (returns true if removed)
            assertTrue((Boolean) runFlow("remove-device", device.getDeviceId()).getMessage().getPayload());
        }
    }

    /**
     * Helper class to receive from Mule
     */
    private class ReceiveThread extends Thread {
        private List<MuleMessage> received = new CopyOnWriteArrayList<MuleMessage>();
        private String address;
        private boolean running = false;

        public ReceiveThread(String address) {
            this.address = address;
        }

        public void reset() {
            // Clear received
            received.clear();

            // Already running
            if (running) {
                return;
            }
            // Start
            else {
                running = true;
                start();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    received.add(muleContext.getClient().request(address, RECEIVE_TIMEOUT));
                } catch (MuleException e) {
                    logger.warn("Error while receiving from " + address, e);
                    break;
                }
            }
        }

        public List<MuleMessage> getReceived() {
            return received;
        }

        public void waitUntilAtLeast(int atLeastCount) {
            await().until(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return received.size();
                }
            }, greaterThanOrEqualTo(atLeastCount));
        }

        public void waitUntil(int exactCount) {
            await().until(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return received.size();
                }
            }, equalTo(exactCount));
        }
    }
}
