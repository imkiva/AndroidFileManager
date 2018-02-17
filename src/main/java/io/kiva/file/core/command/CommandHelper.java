package io.kiva.file.core.command;

import io.kiva.process.ShellHelper;

/**
 * @author kiva
 * @date 2018/2/17
 */
final class CommandHelper {
    private static final String ADB_SHELL_PREFIX = "adb shell";
    public static String listCommand(String path) {
        return ADB_SHELL_PREFIX + " ls -al " + ShellHelper.escapeParameter(path);
    }

    public static String createDirectoryCommand(String path) {
        return ADB_SHELL_PREFIX + " mkdir " + ShellHelper.escapeParameter(path);
    }

    public static String deleteCommand(String path) {
        return ADB_SHELL_PREFIX + " rm -rf " + ShellHelper.escapeParameter(path);
    }
}
