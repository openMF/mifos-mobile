package org.mifos.selfserviceapp.utils;

import android.util.Log;

/**
 * Created by dilpreet on 11/7/17.
 */

public class EncryptionUtil {

    static {
        try {
            System.loadLibrary("encryption");
        } catch (UnsatisfiedLinkError e) {
            Log.e("LoadJniLib", "Error: Could not load native library: " + e.getMessage());
        }
    }

    private static final native String getPassCodeHash(String passcode);

    public static String getHash(String passCode) {
        return getPassCodeHash(passCode);
    }
}
