package io.kiva.android.file.core.process;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ProcessOutput {
    String mLine;

    public ProcessOutput(String line) {
        this.mLine = line;
    }

    public String getLine() {
        return mLine;
    }

    @Override
    public String toString() {
        return "OOOO -> " + getLine();
    }
}
