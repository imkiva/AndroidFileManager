package io.kiva.android.file.core.process;

/**
 * @author kiva
 * @date 2018/2/14
 */
final class ShellHelper {
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
