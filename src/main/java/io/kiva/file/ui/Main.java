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
                FxApplication.launch(GuiApp.class, "/storage/sdcard0");
                return;
            }
        }
        ConsoleApp consoleApp = new ConsoleApp("/storage/sdcard0");
        consoleApp.main();
    }
}
