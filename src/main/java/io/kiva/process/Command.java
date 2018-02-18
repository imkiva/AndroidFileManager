package io.kiva.process;

/**
 * @author kiva
 * @date 2018/2/17
 */
public class Command {
    // cat /dev/urandom | strings
    private static final String SIGNAL_PREFIX = "vVlvefVrfQJ7Z1@";

    private String mCommand;
    private String mSignal;
    private String mUserData;

    public Command(String command) {
        this(command, null, null);
    }

    public Command(String mCommand, String mSignal, String mUserData) {
        this.mCommand = mCommand;
        this.mSignal = mSignal;
        this.mUserData = mUserData;
    }

    public String getCommand() {
        return mCommand;
    }

    public String getUserData() {
        return mUserData;
    }

    public String getSignal() {
        return mSignal;
    }

    public static String makeSignal(String text) {
        return SIGNAL_PREFIX + text;
    }

    public static boolean isSignal(String line) {
        return line.startsWith(SIGNAL_PREFIX);
    }

    @Override
    public String toString() {
        return getCommand();
    }
}
