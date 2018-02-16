package io.kiva.process;

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
    
    public void stopSelf(long waitTimeout) {
        if (isAlive()) {
            setRunning(false);
            try {
                join(waitTimeout);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
