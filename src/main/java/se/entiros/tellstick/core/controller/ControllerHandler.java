/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core.controller;

import se.entiros.tellstick.core.TellstickCoreLibrary;
import se.entiros.tellstick.core.util.Runner;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Controller Handler
 *
 * @author Petter Alstermark, Entiros AB
 */
public class ControllerHandler {
    private static final Logger logger = Logger.getLogger(ControllerHandler.class);

    private final Set<ControllerEventListener> controllerEventListeners = new CopyOnWriteArraySet<ControllerEventListener>();

    private final TellstickCoreLibrary library;

    private final Runner eventRunner;

    private int controllerEventCallbackId = -1;

    public ControllerHandler(TellstickCoreLibrary library) {
        this.library = library;
        this.eventRunner = new Runner();
    }

    /**
     * @param listener controller event listener to add
     * @return true if added
     */
    public boolean addDeviceEventListener(ControllerEventListener listener) {
        return controllerEventListeners.add(listener);
    }

    /**
     * @param listener controller event listener to be removed
     * @return true if removed
     */
    public boolean removeDeviceEventListener(ControllerEventListener listener) {
        return controllerEventListeners.remove(listener);
    }

    /**
     * Start
     */
    public void start() {
        // Controller Event Listener
        logger.debug("Starting Controller Event Listener");
        TDControllerEventListener controllerEventListener = new TDControllerEventListener();
        controllerEventCallbackId = library.tdRegisterControllerEvent(controllerEventListener, null);

        // Start Event Runner
        eventRunner.start();
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop Event Runner
        eventRunner.stop();

        // Stop Controller Event Listener
        if (controllerEventCallbackId != -1) {
            logger.debug("Stopping Controller Event Listener");
            library.tdUnregisterCallback(controllerEventCallbackId);
            controllerEventCallbackId = -1;
        }
    }

    /**
     * Sensor Event Listener
     */
    private class TDControllerEventListener implements TellstickCoreLibrary.TDControllerEvent {
        public void event(int controllerId, int changeEvent, int changeType, String newValue, int callbackId, Pointer context) {
            String string = "";
            string += "controllerId: " + controllerId + ", ";
            string += "changeEvent: " + changeEvent + ", ";
            string += "newValue: " + newValue + ", ";
            string += "callbackId: " + callbackId;

            eventRunner.offer(new Runnable() {
                @Override
                public void run() {
                    for (ControllerEventListener listener : controllerEventListeners) {
                        // TODO
                    }
                }
            });

            logger.info(string);
        }
    }
}
