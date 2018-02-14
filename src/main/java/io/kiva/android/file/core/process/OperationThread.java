package io.kiva.android.file.core.process;

import io.kiva.android.file.core.utils.Log;

/**
 * @author kiva
 * @date 2018/2/14
 */
class OperationThread extends Thread {
    private boolean isRunning;

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isRunning() {
        return isRunning;
    }
    
    public void stopSelf() {
        if (isAlive()) {
            setRunning(false);
            try {
                join(ShellProcess.WAIT_TIMEOUT);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
