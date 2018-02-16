package io.kiva.file.core.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author kiva
 * @date 2018/2/14
 */
public final class FileHelper {
     private static final String ADB_SHELL_PREFIX = "adb shell";

    private static final String CHARS_ESCAPE = "\"\\$`!";

    public static List<String> splitPath(String path) {
        return Arrays.asList(path.split(File.separator));
    }

    public static String buildAndroidListCommand(String path) {
        return ADB_SHELL_PREFIX + " ls -al " + escapeParameter(path);
    }

    public static String buildAndroidFinishCommand(String signal) {
        return ADB_SHELL_PREFIX + " echo " + escapeParameter(signal);
    }

    private static String escapeParameter(String parameter) {
        StringBuilder builder = new StringBuilder("\"");
        for (char c : parameter.toCharArray()) {
            if (CHARS_ESCAPE.indexOf(c) >= 0) {
                builder.append('\\');
            }
            builder.append(c);
        }
        builder.append('"');
        return builder.toString();
    }
}
