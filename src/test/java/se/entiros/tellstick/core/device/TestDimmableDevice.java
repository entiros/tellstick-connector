/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device;

public class TestDimmableDevice extends AbstractTestDevice implements DimmableDevice {
    public TestDimmableDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public void dim(int level) throws DeviceException {
        counter = level;
    }

    @Override
    public void on() throws DeviceException {
        counter++;
    }

    @Override
    public void off() throws DeviceException {
        counter--;
    }

    @Override
    public boolean isOn() {
        return false;
    }
}
