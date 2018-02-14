package io.kiva.android.file.core.process;

import io.kiva.android.file.core.utils.Log;

import java.io.IOException;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ShellProcess implements AutoCloseable, OutputListener {
    public static final int WAIT_TIMEOUT = 160;
    private static final int KILLED_BY_SIGNAL_START = 128 + 1;
    private static final int KILLED_BY_SIGNAL_END = 128 + 64;

    private Process mProcess;
    private ReaderThread mReaderThread;
    private CommandWriter mCommandWriter;
    private OutputListener mListener;
    private boolean mClosed;
    private int mExitCode;
    private boolean mSafeExit;

    private ShellProcess(Process process) {
        this.mProcess = process;
        this.mReaderThread = new ReaderThread(process.getInputStream(), this);
        this.mReaderThread.start();
        this.mCommandWriter = new CommandWriter(process.getOutputStream());
        this.mClosed = false;
        this.mSafeExit = false;

        Log.d("Shell started ---- waiting for commands");
    }

    public void writeCommand(String command) {
        if (mClosed) {
            throw new ShellClosedException();
        }
        mCommandWriter.addCommand(command);
    }

    private void doSafeExit() {
        mCommandWriter.addCommand("exit 0");
        mReaderThread.stopSelf();
    }

    private void waitForExit() {
        try {
            // Let shell exit
            Thread.sleep(WAIT_TIMEOUT);
        } catch (InterruptedException ignore) {
        }
    }

    public boolean isKilledBySignal() {
        int code = getExitCode();
        return code >= KILLED_BY_SIGNAL_START && code <= KILLED_BY_SIGNAL_END;
    }

    public int getKillingSignal() {
        if (isKilledBySignal()) {
            return getExitCode() - 128;
        }
        return -1;
    }

    @Override
    public void close() {
        if (!mClosed) {
            mClosed = true;
            if (mSafeExit && mProcess.isAlive()) {
                doSafeExit();
            }

            mProcess.destroy();
            mReaderThread.stopSelf();

            if (mProcess.isAlive()) {
                waitForExit();
            }
            mExitCode = mProcess.exitValue();
            Log.d("Shell closed ---- exited with " + mExitCode
                    + (isKilledBySignal() ? ", killed by signal " + getKillingSignal() : ""));
        }
    }

    public int getExitCode() {
        if (!mClosed) {
            throw new IllegalStateException("Shell hasn't exited!");
        }
        return mExitCode;
    }

    public void setSafeExit(boolean mSafeExit) {
        this.mSafeExit = mSafeExit;
    }

    public void setOutputListener(OutputListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onNewOutput(ProcessOutput output) {
        if (mListener != null) {
            mListener.onNewOutput(output);
        }
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
