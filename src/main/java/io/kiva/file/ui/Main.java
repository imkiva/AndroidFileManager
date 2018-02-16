package io.kiva.file.ui;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class Main {
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine("/storage/sdcard0");
        commandLine.main();
    }
}
