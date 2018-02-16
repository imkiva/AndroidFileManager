package io.kiva.file.ui;

import io.kiva.fx.FxApplication;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-gui")) {
                FxApplication.launch(FxMain.class, "/storage/sdcard0");
                return;
            }
        }
        CommandLine commandLine = new CommandLine("/storage/sdcard0");
        commandLine.main();
    }
}
