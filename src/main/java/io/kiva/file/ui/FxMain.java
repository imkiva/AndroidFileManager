package io.kiva.file.ui;

import io.kiva.file.core.DirectoryNavigator;
import io.kiva.file.core.FileManager;
import io.kiva.file.core.OnCacheUpdatedListener;
import io.kiva.file.core.model.FileModel;
import io.kiva.fx.FxApplication;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class FxMain extends FxApplication implements OnCacheUpdatedListener {
    private BorderPane mRootLayout;
    private ListView<FileModel> mListView;

    private FileManager mManager;
    private DirectoryNavigator mNavigator;

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
        setTitle("Android File Manager");
        setContentView("main.fxml");
        addIcon("icon.png");
        mRootLayout = getRootLayout();
        mListView = (ListView<FileModel>) mRootLayout.getCenter();
        mListView.setCellFactory(FileCell::new);
        initData();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        mManager.dispose();
    }

    private void initData() {
        mManager = new FileManager();
        mManager.addOnCacheUpdatedListener(this);
        mNavigator = mManager.getNavigator();

        List<String> args = getParameters().getRaw();
        mNavigator.navigate(args.isEmpty() ? "/" : args.get(0));
    }

    @Override
    public void onCacheUpdated(String path, List<FileModel> newCache) {
        mListView.setItems(FXCollections.observableArrayList(newCache));
    }

    public class FileCell extends ListCell<FileModel> {
        @SuppressWarnings("unused")
        FileCell(ListView<FileModel> listView) {
        }

        @Override
        protected void updateItem(FileModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null) {
                setGraphic(null);
                return;
            }
            Label label = new Label();
            label.setText(item.getName() + (item.isDirectory() ? "/" : ""));
            this.setGraphic(label);
        }
    }
}
