/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.proxy;

import org.apache.log4j.Logger;
import se.entiros.tellstick.core.device.Device;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Device Proxy to add "redelivery" to all methods that return void
 *
 * @author Petter Alstermark, Entiros AB
 */
public class DeviceProxy {
    private static final Logger logger = Logger.getLogger(DeviceProxy.class);

    private final Map<Integer, DeviceMethodCall> deviceMethodCallsMap = new LinkedHashMap<Integer, DeviceMethodCall>();
    private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();

    private int tries = 3;
    private long callDelay = 500;

    private DeviceMethodCaller thread;

    public DeviceProxy() {
        start();
    }

    /**
     * Clear and start
     */
    public void start() {
        // Already started
        if (thread != null)
            return;

        // Clear
        clear();

        // Start
        thread = new DeviceMethodCaller();
        thread.start();
    }

    /**
     * Stop and clear
     */
    public void stop() {
        // Stop thread
        if (thread != null) {
            DeviceMethodCaller currentThread = thread;
            thread = null;
            currentThread.interrupt();
        }

        // Clear
        clear();
    }

    /**
     * Clear
     */
    public void clear() {
        synchronized (deviceMethodCallsMap) {
            deviceMethodCallsMap.clear();
        }

        queue.clear();
    }

    /**
     * @return number of tries for each call
     */
    public int getTries() {
        return tries;
    }

    /**
     * @param tries number of tries for each call
     */
    public void setTries(int tries) {
        this.tries = tries;
    }

    /**
     * @return delay between calls
     */
    public long getCallDelay() {
        return callDelay;
    }

    /**
     * @param callDelay delay between calls
     */
    public void setCallDelay(long callDelay) {
        this.callDelay = callDelay;
    }

    /**
     * @param deviceId device ID
     * @return DeviceMethodCall
     */
    private DeviceMethodCall getDeviceMethodCall(int deviceId) {
        synchronized (deviceMethodCallsMap) {
            return deviceMethodCallsMap.remove(deviceId);
        }
    }

    /**
     * @param deviceMethodCall method call
     * @param override         if true, override any existing DeviceMethodCall with the same device ID
     */
    private void queue(DeviceMethodCall deviceMethodCall, boolean override) {
        synchronized (deviceMethodCallsMap) {
            // Add Device Method Call
            if (override || !deviceMethodCallsMap.containsKey(deviceMethodCall.getDeviceId())) {
                deviceMethodCallsMap.put(deviceMethodCall.getDeviceId(), deviceMethodCall);
            }
        }

        // Queue for run
        if (!queue.contains(deviceMethodCall.getDeviceId())) {
            queue.offer(deviceMethodCall.getDeviceId());
        }
    }

    /**
     * @param device device
     * @param <T>    device type
     * @return proxied device
     */
    @SuppressWarnings("unchecked")
    public <T extends Device> T proxy(final T device, Class<T> targetClass) {
        return (T) Proxy.newProxyInstance(device.getClass().getClassLoader(), new Class<?>[]{targetClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // Do tries
                if (method.getReturnType().equals(Void.TYPE)) {
                    queue(new DeviceMethodCall(device, method, args), true);
                    return Void.TYPE;
                }
                // Call once and return result
                else {
                    return method.invoke(device, args);
                }
            }
        });
    }

    /**
     * Thread to do the device method calls
     */
    private class DeviceMethodCaller extends Thread {
        @Override
        public void run() {
            while (thread == this) {
                try {
                    Integer deviceId = queue.take();

                    // Skipp null
                    if (deviceId == null)
                        continue;

                    // Get Device Method Call
                    DeviceMethodCall deviceMethodCall = getDeviceMethodCall(deviceId);

                    // Skipp null
                    if (deviceMethodCall == null)
                        continue;

                    // Call device method
                    deviceMethodCall.call();

                    // Add for new call try
                    if (deviceMethodCall.getTries() < tries) {
                        queue(deviceMethodCall, false);
                    }

                    if (logger.isTraceEnabled())
                        logger.trace(String.format("Delaying %d milliseconds", getCallDelay()));

                    // Delay
                    Thread.sleep(getCallDelay());
                } catch (IllegalAccessException e) {
                    logger.error(e);
                } catch (InvocationTargetException e) {
                    logger.error(e);
                } catch (InterruptedException e) {
                    logger.debug("Interrupted");
                }
            }
        }
    }

    /**
     * Device Cethod Call
     */
    private class DeviceMethodCall {
        private int tries;
        private Device device;
        private Method method;
        private Object[] args;

        /**
         * @param device device
         * @param method method
         * @param args   arguments
         */
        private DeviceMethodCall(Device device, Method method, Object[] args) {
            this.device = device;
            this.method = method;
            this.args = args;

            this.tries = 0;
        }

        /**
         * Call
         *
         * @throws java.lang.reflect.InvocationTargetException
         * @throws IllegalAccessException
         */
        public void call() throws InvocationTargetException, IllegalAccessException {
            tries++;

            if (logger.isDebugEnabled())
                logger.debug(String.format("%d call to method '%s' on device '%s' (#%d)", tries, method.getName(), device.getName(), device.getDeviceId()));

            method.invoke(device, args);
        }

        /**
         * @return device ID
         */
        public int getDeviceId() {
            return device.getDeviceId();
        }

        /**
         * @return number of tries performed
         */
        public int getTries() {
            return tries;
        }
    }

}
