package io.kiva.process;

import io.kiva.file.core.utils.Platform;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ShellHelper {
    private static final String CHARS_ESCAPE = "\"\\$`!";

    public static String escapeParameter(String parameter) {
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

    public static String buildFinishCommand(String signal) {
        return "echo \"" + escapeParameter(signal) + "\"";
    }

    static String detectShell() {
        Platform platform = Platform.get();
        switch (platform) {
            case LINUX:
            case AIX:
            case BSD:
            case SOLARIS:
                return "sh";
            case WINDOWS:
                return "cmd.exe";
        }
        return null;
    }
}
