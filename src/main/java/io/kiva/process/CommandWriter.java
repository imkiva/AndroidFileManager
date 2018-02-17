package io.kiva.process;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author kiva
 * @date 2018/2/14
 */
final class CommandWriter {
    private static final String SEPARATOR = System.getProperty("line.separator");

    private OutputStream mOutputStream;

    CommandWriter(OutputStream mOutputStream) {
        this.mOutputStream = mOutputStream;
    }

    public void addCommand(Command command) {
        try {
            String s = command.getCommand()
                    + ";"
                    + finishCommand(command.getSignal(), command.getUserData());
            System.out.println(s);
            mOutputStream.write(s.getBytes());
            mOutputStream.write(SEPARATOR.getBytes());
            mOutputStream.flush();
        } catch (IOException ignore) {
        }
    }

    private static String finishCommand(String signal, String data) {
        if (signal == null) {
            return "";
        }
        return " echo " + ShellHelper.escapeParameter(signal + data);
    }
}
