package io.kiva.android.file.core.process;

import io.kiva.android.file.core.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author kiva
 * @date 2018/2/14
 */
final class ReaderThread extends OperationThread {
    private OutputListener mListener;
    private InputStream mInputStream;

    ReaderThread(InputStream inputStream, OutputListener mListener) {
        this.mInputStream = inputStream;
        this.mListener = mListener;
        this.setName("ReaderThread");
    }

    @Override
    public void run() {
        if (mListener == null) {
            return;
        }

        String line;
        ProcessOutput output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream))) {
            while (isRunning()) {
                try {
                    while ((line = reader.readLine()) != null) {
                        output = new ProcessOutput(line);
                        mListener.onNewOutput(output);
                    }
                } catch (IOException ignore) {
                }
            }
        } catch (IOException ignore) {
        }
    }

    @Override
    public synchronized void start() {
        setRunning(true);
        super.start();
    }
}
