package io.kiva.file.core.command;

import io.kiva.process.ShellHelper;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2018/2/17
 */
public final class CommandHelper {
    private static String ADB_PATH;
    private static String ADB_SHELL_PREFIX;

    static {
        setAdbPath("adb");
    }

    public static void setAdbPath(String adbPath) {
        ADB_PATH = adbPath;
        ADB_SHELL_PREFIX = ADB_PATH + " shell ";
    }

    public static String pushCommand(String targetDirectory, List<File> files) {
        StringBuilder builder = new StringBuilder(ADB_PATH).append(" push ");
        String fileList = files.stream()
                .map(File::getAbsolutePath)
                .map(ShellHelper::escapeParameter)
                .collect(Collectors.joining(" "));
        builder.append(fileList).append(" ")
                .append(ShellHelper.escapeParameter(targetDirectory));
        return builder.toString();
    }

    public static String listCommand(String path) {
        return ADB_SHELL_PREFIX + "ls -al " + ShellHelper.escapeParameter(path);
    }

    public static String pushCommand(String path) {
        return ADB_SHELL_PREFIX + "ls -al " + ShellHelper.escapeParameter(path);
    }

    public static String createDirectoryCommand(String path) {
        return ADB_SHELL_PREFIX + "mkdir " + ShellHelper.escapeParameter(path);
    }

    public static String deleteCommand(String path) {
        return ADB_SHELL_PREFIX + "rm -rf " + ShellHelper.escapeParameter(path);
    }
}
