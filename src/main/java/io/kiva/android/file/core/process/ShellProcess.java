package io.kiva.android.file.core.process;

import java.io.IOException;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ShellProcess implements AutoCloseable, OutputListener {
    private Process mProcess;
    private ReaderThread mReaderThread;

    private ShellProcess(Process process) {
        this.mProcess = process;
        this.mReaderThread = new ReaderThread(process.getInputStream(), this);
        this.mReaderThread.start();
    }

    @Override
    public void close() {
        if (mReaderThread.isAlive()) {
            mReaderThread.setRunning(false);
            try {
                mReaderThread.join();
            } catch (InterruptedException ignore) {
            }
        }
        if (mProcess != null) {
            mProcess.destroy();
        }
    }

    @Override
    public void onNewOutput(ProcessOutput output) {

    }

    public static ShellProcess open() {
        String shell = ShellHelper.detectShell();
        if (shell == null) {
            throw new UnsupportedOperationException("Failed to detect shell");
        }

        ProcessBuilder builder = new ProcessBuilder(shell);
        builder.redirectErrorStream(true);
        try {
            return new ShellProcess(builder.start());

        } catch (IOException e) {
            throw new RuntimeException("Failed to start shell process", e);
        }
    }
}
