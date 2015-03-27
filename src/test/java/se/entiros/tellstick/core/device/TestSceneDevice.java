package se.entiros.tellstick.core.device;

public class TestSceneDevice extends AbstractTestDevice implements SceneDevice {
    public TestSceneDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public void execute() throws DeviceException {
        counter++;
    }
}
