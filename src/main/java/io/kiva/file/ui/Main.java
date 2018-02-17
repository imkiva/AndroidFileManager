package io.kiva.file.ui;

import io.kiva.fx.FxApplication;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class Main {
    public static void main(String[] args) {
        FxApplication.launch(GuiApp.class, args.length == 0
                ? new String[]{"/storage/sdcard0"}
                : args);
    }
}
