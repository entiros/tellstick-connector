/**
 * (c) 2011-2015 Entiros AB The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package se.entiros.tellstick.core;

import com.sun.jna.Pointer;

/**
 * Tellstick Exception
 *
 * @author Petter Alstermark, Entiros AB
 */
public class TellstickException extends Exception {
    public TellstickException(TellstickCoreLibrary library, int errorNo) {
        super(getErrorString(library, errorNo));
    }

    /**
     * @param message message
     * @param library Tellstick core library
     * @param errorNo Tellstick error number
     */
    public TellstickException(String message, TellstickCoreLibrary library, int errorNo) {
        super(message + ", error: " + getErrorString(library, errorNo));
    }

    /**
     * @param library Tellstick core library
     * @param errorNo Tellstick error number
     * @return error string for error number
     */
    protected static String getErrorString(TellstickCoreLibrary library, int errorNo) {
        Pointer errorStringPointer = library.tdGetErrorString(errorNo);
        String errorString = errorStringPointer.getString(0);
        library.tdReleaseString(errorStringPointer);

        return errorString;
    }

}
