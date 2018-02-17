package io.kiva.file.ui;

import io.kiva.file.core.DirectoryNavigator;
import io.kiva.file.core.FileManager;
import io.kiva.file.core.OnCacheUpdatedListener;
import io.kiva.file.core.model.FileModel;
import io.kiva.file.core.utils.FileHelper;
import io.kiva.file.core.utils.Log;
import io.kiva.file.ui.cell.FileCell;
import io.kiva.file.ui.model.FileViewModel;
import io.kiva.file.ui.model.TopDirModel;
import io.kiva.fx.FxApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class GuiApp extends FxApplication implements OnCacheUpdatedListener, EventHandler<MouseEvent> {
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

        MenuBar menuBar = (MenuBar) mRootLayout.getTop();
        menuBar.setUseSystemMenuBar(true);
        initMenu(menuBar);

        initData();
    }

    private void initMenu(MenuBar menuBar) {
        ObservableList<Menu> menus = menuBar.getMenus();
        Menu refresh = new Menu("Refresh");
        refresh.addEventHandler(EventType.ROOT, event -> {
            mNavigator.navigate(mNavigator.getCurrentPath());
        });

        Menu navigateTo = new Menu("Goto");
        navigateTo.addEventHandler(EventType.ROOT, event -> {
            // TODO: Navigate to someplace
        });
        menus.add(refresh);
        menus.add(navigateTo);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        mManager.dispose();
    }

    @Override
    public void onCacheUpdated(String path, List<FileModel> newCache) {
        Platform.runLater(() -> {
            List<FileViewModel> models = mapToViewModel(path, newCache);
            synchronized (this) {
                mListView.setItems(FXCollections.observableArrayList(models));
            }
        });
    }

    private List<FileViewModel> mapToViewModel(String path, List<FileModel> newCache) {
        ArrayList<FileViewModel> models = new ArrayList<>();
        if (!FileHelper.isRootDirectory(path)) {
            models.add(new TopDirModel());
        }
        models.addAll(newCache.stream()
                .map(FileViewModel::new)
                .collect(Collectors.toList()));
        return models;
    }

    private ListCell<FileViewModel> onCreateListCell(ListView<FileViewModel> listView) {
        FileCell fileCell = new FileCell(this);
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

        // Apply caches first.
        // Replace them in onCacheUpdated()
        synchronized (this) {
            String cachedPath = null;
            if (model instanceof TopDirModel) {
                cachedPath = mNavigator.getParentPath();
            } else if (model.getFileModel().isDirectory()) {
                cachedPath = mNavigator.getCurrentPath()
                        + model.getFileModel().getName()
                        + File.separator;
            }
            if (cachedPath != null) {
                List<FileModel> cachedModels = mManager.getCachedFileList(cachedPath);
                if (cachedModels.size() > 0) {
                    Log.d("Hit cache: " + cachedPath);
                }
                List<FileViewModel> cached = mapToViewModel(cachedPath, cachedModels);
                mListView.setItems(FXCollections.observableArrayList(cached));
                mListView.scrollTo(0);
            }
        }

        if (model instanceof TopDirModel) {
            mNavigator.navigateTop();

        } else if (model.getFileModel().isDirectory()) {
            mNavigator.navigateInto(model.getFileModel().getName());
        }
    }
}
