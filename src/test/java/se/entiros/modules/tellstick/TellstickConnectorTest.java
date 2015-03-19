/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.modules.tellstick;

import se.entiros.tellstick.core.device.Device;
import org.mule.modules.tests.ConnectorTestCase;

import org.junit.Test;

import java.util.List;

public class TellstickConnectorTest extends ConnectorTestCase {
    
    @Override
    protected String getConfigResources() {
        return "tellstick-config.xml";
    }

    @Test
    public void testGetDevices() throws Exception {
        List<Device> devices = (List<Device>) runFlow("testGetDevices").getMessage().getPayload();

        System.out.println(devices);
    }
}
