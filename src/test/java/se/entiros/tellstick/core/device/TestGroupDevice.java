package se.entiros.tellstick.core.device;

import java.util.List;

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
