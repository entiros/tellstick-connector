package com.alstermark.tellstick.core.device;

import java.util.List;

/**
 * Test Group Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-07.
 */
public class TestGroupDevice extends AbstractTestDevice implements GroupDevice {
    public TestGroupDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public List<Device> getDevices() {
        return null;
    }

    @Override
    public void on() {
        counter++;
    }

    @Override
    public void off() {
        counter--;
    }

    @Override
    public boolean isOn() {
        throw new RuntimeException("Not implemented for GroupDevice");
    }
}
