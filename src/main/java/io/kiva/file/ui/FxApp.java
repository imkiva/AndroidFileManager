package io.kiva.file.ui;

import io.kiva.file.core.DirectoryNavigator;
import io.kiva.file.core.FileManager;
import io.kiva.file.core.OnCacheUpdatedListener;
import io.kiva.file.core.model.FileModel;
import io.kiva.file.core.utils.FileHelper;
import io.kiva.file.ui.model.FileViewModel;
import io.kiva.file.ui.model.TopDirModel;
import io.kiva.fx.FxApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class FxApp extends FxApplication implements OnCacheUpdatedListener, EventHandler<MouseEvent> {
    private ListView<FileViewModel> mListView;

    private FileManager mManager;
    private DirectoryNavigator mNavigator;

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
        setTitle("Android File Manager");
        setContentView("main.fxml");
        addIcon("icon.png");
        BorderPane mRootLayout = getRootLayout();
        mListView = (ListView<FileViewModel>) mRootLayout.getCenter();
        mListView.setCellFactory(this::onCreateListCell);
        mListView.setFixedCellSize(50);
        initData();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        mManager.dispose();
    }

    @Override
    public void onCacheUpdated(String path, List<FileModel> newCache) {
        Platform.runLater(() -> {
            ArrayList<FileViewModel> models = new ArrayList<>();
            if (!FileHelper.isRootDirectory(path)) {
                models.add(new TopDirModel());
            }
            models.addAll(newCache.stream()
                    .map(FileViewModel::new)
                    .collect(Collectors.toList()));
            mListView.setItems(null);
            mListView.setItems(FXCollections.observableArrayList(models));
            mListView.scrollTo(0);
        });
    }

    private ListCell<FileViewModel> onCreateListCell(ListView<FileViewModel> listView) {
        FileCell fileCell = new FileCell();
        fileCell.setOnMouseClicked(this);
        return fileCell;
    }

    private void initData() {
        mManager = new FileManager();
        mManager.addOnCacheUpdatedListener(this);
        mNavigator = mManager.getNavigator();

        List<String> args = getParameters().getRaw();
        mNavigator.navigate(args.isEmpty() ? "/" : args.get(0));
    }

    @Override
    public void handle(MouseEvent event) {
        FileViewModel model = mListView.getSelectionModel().getSelectedItem();
        if (model == null) {
            return;
        }

        if (model instanceof TopDirModel) {
            mNavigator.navigateTop();

        } else if (model.getFileModel().isDirectory()) {
            mNavigator.navigateInto(model.getFileModel().getName());
        }
    }

    public class FileCell extends ListCell<FileViewModel> {
        FileCell() {
        }

        @Override
        protected void updateItem(FileViewModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null) {
                setGraphic(null);
                return;
            }
            FileModel model = item.getFileModel();
            BorderPane pane = new BorderPane();
            Text text = new Text();
            text.setTextAlignment(TextAlignment.LEFT);
            text.setText(model.getName());

            Image image = model.isDirectory()
                    ? loadImage("file_type_dir.png")
                    : loadImage("file_type_unknown.png");

            ImageView imageView = new ImageView(image);

            pane.setLeft(imageView);
            pane.setCenter(text);
            this.setGraphic(pane);
        }
    }
}
