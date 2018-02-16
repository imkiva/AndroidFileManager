package io.kiva.file.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class FxMain extends Application {
    private BorderPane mRootLayout;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Android File Manager");
        setupView(primaryStage);
    }

    private void setupView(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FxMain.class.getResource("io/kiva/file/ui/layout/main.fxml"));
            mRootLayout = loader.load();

            Scene scene = new Scene(mRootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
