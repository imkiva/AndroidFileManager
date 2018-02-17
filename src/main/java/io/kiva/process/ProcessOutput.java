package io.kiva.process;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ProcessOutput {
    static final String END_COMMAND_FLAG = "---@@@qaz-COMMAND-END-plm@@@---";

    private String mLine;

    public ProcessOutput(String line) {
        this.mLine = line;
    }

    public String getLine() {
        return mLine;
    }

    public boolean isFinishFlag() {
        return getLine().trim().equals(END_COMMAND_FLAG);
    }

    @Override
    public String toString() {
        return getLine();
    }
}
