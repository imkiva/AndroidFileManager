package io.kiva.android.file.core.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ReaderThread extends Thread {
    private OutputListener mListener;
    private InputStream mInputStream;
    private boolean isRunning;

    ReaderThread(InputStream inputStream, OutputListener mListener) {
        this.mInputStream = inputStream;
        this.mListener = mListener;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        if (mListener == null) {
            return;
        }

        String line;
        ProcessOutput output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream))) {
            while (isRunning) {
                try {
                    while ((line = reader.readLine()) != null) {
                        output = new ProcessOutput(line);
                        mListener.onNewOutput(output);
                    }
                } catch (IOException ignore) {
                    ignore.printStackTrace();
                }
            }
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public synchronized void start() {
        setRunning(true);
        super.start();
    }
}
