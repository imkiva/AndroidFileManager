package io.kiva.process;

/**
 * @author kiva
 * @date 2018/2/14
 */
@FunctionalInterface
public interface IOutputListener {
    void onNewOutput(ProcessOutput output);
}
