/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.device;

public class TestBellDevice extends AbstractTestDevice implements BellDevice {

    public TestBellDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public void bell() throws DeviceException {
        counter++;
    }
}
