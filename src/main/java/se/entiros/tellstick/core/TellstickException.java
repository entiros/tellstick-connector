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
