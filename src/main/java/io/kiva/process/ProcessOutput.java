package io.kiva.process;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ProcessOutput {
    private String mLine;

    public ProcessOutput(String line) {
        this.mLine = line;
    }

    public String getLine() {
        return mLine;
    }

    @Override
    public String toString() {
        return getLine();
    }
}
