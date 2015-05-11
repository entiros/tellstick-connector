/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.rawdevice;

import se.entiros.tellstick.core.Tellstick;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

@Ignore
public class RawDeviceHandlerTest {
    private static Tellstick tellstick;

    @BeforeClass
    public static void beforeClass() {
        tellstick = new Tellstick();
        tellstick.start();
    }

    @AfterClass
    public static void afterClass() {
        if (tellstick != null) {
            tellstick.stop();
            tellstick = null;
        }
    }

    @Test
    public void testHandleEvent() throws Exception {
        tellstick.getRawDeviceHandler().addRawDeviceEventListener(new RawDeviceEventListener() {
            @Override
            public void rawDeviceEvent(Map<String, String> parameters) {
                System.out.println(parameters);
            }
        });

        Thread.sleep(30000);
    }
}