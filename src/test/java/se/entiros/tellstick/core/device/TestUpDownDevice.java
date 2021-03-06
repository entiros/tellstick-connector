package se.entiros.tellstick.core.device;

/**
 * Test Up Down Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-07.
 */
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
