package io.kiva.file.ui;

import io.kiva.fx.FxApplication;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class FxMain extends FxApplication {
    private BorderPane mRootLayout;

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
        setTitle("Android File Manager");
        setContentView("main.fxml");
        addIcon("icon.png");
        mRootLayout = getRootLayout();
    }
}
