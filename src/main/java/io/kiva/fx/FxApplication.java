package io.kiva.fx;

import io.kiva.file.ui.FxMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class FxApplication extends Application {
    private static final String LAYOUT_PREFIX = "/res/layout/";
    private static final String DRAWABLE_PREFIX = "/res/drawable/";

    private Stage mPrimaryStage;
    private Pane mRootLayout;
    private FXMLLoader mRootLayoutLoader;

    @Override
    public void start(Stage primaryStage) {
        this.mPrimaryStage = primaryStage;
    }

    @SuppressWarnings("unchecked")
    public <T extends Pane> T getRootLayout() {
        ensureStage();
        return (T) mRootLayout;
    }

    public Image loadImage(String resName) {
        return new Image(DRAWABLE_PREFIX + resName);
    }

    public FXMLLoader getRootLayoutLoader() {
        return mRootLayoutLoader;
    }

    protected void addIcon(String resName) {
        getPrimaryStage().getIcons().add(loadImage(resName));
    }

    protected void setTitle(String title) {
        getPrimaryStage().setTitle(title);
    }

    protected void setContentView(String resName) {
        try {
            mRootLayoutLoader = new FXMLLoader();
            mRootLayoutLoader.setLocation(FxMain.class.getResource(LAYOUT_PREFIX + resName));
            mRootLayout = mRootLayoutLoader.load();

            Scene scene = new Scene(mRootLayout);
            getPrimaryStage().setScene(scene);
            getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        ensureStage();
        return mPrimaryStage;
    }

    private void ensureStage() {
        if (mPrimaryStage == null) {
            throw new NullPointerException("Child didn't onCreateListCell super.start(Stage)");
        }
    }
}
