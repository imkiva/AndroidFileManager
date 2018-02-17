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

        throw new UnsupportedOperationException(osName + " is not a supported OperatingSystem platform.");
    }
}