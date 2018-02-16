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

    public void addCommand(String command) {
        try {
            mOutputStream.write(command.getBytes());
            mOutputStream.write(SEPARATOR.getBytes());
            mOutputStream.flush();
        } catch (IOException ignore) {
        }
    }
}
