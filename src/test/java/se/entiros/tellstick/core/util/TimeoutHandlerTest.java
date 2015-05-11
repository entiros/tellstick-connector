/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeoutHandlerTest {
    public static final String MY_VALUE = "myValue";

    @Test
    public void testIsReady() throws InterruptedException {
        TimeoutHandler<String> timeoutHandler = new TimeoutHandler<String>(500);

        assertTrue(timeoutHandler.isReady(MY_VALUE));
        assertFalse(timeoutHandler.isReady(MY_VALUE));

        Thread.sleep(750);

        assertTrue(timeoutHandler.isReady(MY_VALUE));
        assertFalse(timeoutHandler.isReady(MY_VALUE));
    }

    @Test
    public void testClean() throws InterruptedException {
        TimeoutHandler<String> timeoutHandler = new TimeoutHandler<String>(2);

        assertTrue(timeoutHandler.isReady(MY_VALUE));

        Thread.sleep(750);

        assertTrue(timeoutHandler.isReady(MY_VALUE));
    }
}