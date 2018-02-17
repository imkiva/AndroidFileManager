package io.kiva.file.core.utils;

import io.kiva.process.ShellHelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author kiva
 * @date 2018/2/14
 */
public final class FileHelper {
    private static final String ADB_SHELL_PREFIX = "adb shell";

    public static List<String> splitPath(String path) {
        return Arrays.asList(path.split(File.separator));
    }

    public static String buildAndroidListCommand(String path) {
        return ADB_SHELL_PREFIX + " ls -al " + ShellHelper.escapeParameter(path);
    }

    public static String dirName(String fileName) {
        return new File(fileName).getParent();
    }

    public static String baseName(String fileName) {
        return new File(fileName).getName();
    }

    public static boolean isRootDirectory(String dir) {
        return dir.trim().equals(File.separator);
    }
}
