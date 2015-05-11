/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device;

public class TestUpDownDevice extends AbstractTestDevice implements UpDownDevice {
    public TestUpDownDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public void up() throws DeviceException {
        counter++;
    }

    @Override
    public void down() throws DeviceException {
        counter--;
    }

    @Override
    public void stop() throws DeviceException {
        counter = 0;
    }
}
