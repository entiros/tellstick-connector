/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device;

import se.entiros.tellstick.core.TellstickCoreLibrary;

public abstract class AbstractTestDevice implements Device {
    private int deviceId;
    private String name;

    protected int counter;

    public AbstractTestDevice(int deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
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
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public int getDeviceType() {
        return 0;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public TellstickCoreLibrary getLibrary() {
        return null;
    }

    @Override
    public DeviceHandler getDeviceHandler() {
        return null;
    }

    @Override
    public int compareTo(Device o) {
        return 0;
    }

    /**
     * @return counter value
     */
    public int getCounter() {
        return counter;
    }

}
