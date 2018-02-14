package io.kiva.android.file.core.process;

import java.security.AccessController;
import java.security.PrivilegedAction;

public enum Platform {
    LINUX,

    BSD,

    SOLARIS,

    AIX,

    WINDOWS;

    static Platform get() {
        String osName = AccessController.doPrivileged(
                (PrivilegedAction<String>) () -> System.getProperty("os.name")
        );

        if (osName.equals("Linux")) {
            return LINUX;
        }
        if (osName.contains("OS X")) {
            return BSD;
        }
        if (osName.equals("SunOS")) {
            return SOLARIS;
        }
        if (osName.equals("AIX")) {
            return AIX;
        }
        if (osName.equals("Windows")) {
            return WINDOWS;
        }

        throw new UnsupportedOperationException(osName + " is not a supported OS platform.");
    }
}