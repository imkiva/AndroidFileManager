package io.kiva.file.core.utils;

/**
 * @author kiva
 * @date 2018/2/14
 */
public enum OperatingSystem {
    LINUX,

    BSD,

    SOLARIS,

    AIX,

    WINDOWS;

    public static OperatingSystem get() {
        String osName = SystemProperty.get("os.name");

        if (osName.contains("Linux")) {
            return LINUX;
        }
        if (osName.contains("OS X")) {
            return BSD;
        }
        if (osName.contains("SunOS")) {
            return SOLARIS;
        }
        if (osName.contains("AIX")) {
            return AIX;
        }
        if (osName.contains("Windows")) {
            return WINDOWS;
        }

        throw new UnsupportedOperationException(osName + " is not a supported OperatingSystem platform.");
    }
}