package io.kiva.file.ui;

import io.kiva.file.core.DirectoryNavigator;
import io.kiva.file.core.FileManager;
import io.kiva.file.core.FileManagerCallback;
import io.kiva.file.core.command.CommandHelper;
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
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class GuiApp extends FxApplication implements FileManagerCallback {
    private ListView<FileViewModel> mListView;

    private FileManager mManager;
    private DirectoryNavigator mNavigator;
    private ContextMenu mItemContextMenu;
    private UserConfig mUserConfig;

    public GuiApp() {
        mUserConfig = new UserConfig(GuiApp.class);
    }

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
        mListView.setFixedCellSize(35);

        MenuBar menuBar = (MenuBar) mRootLayout.getTop();
        menuBar.setUseSystemMenuBar(true);
        initMenu(menuBar);

        initData();
    }

    private void initMenu(MenuBar menuBar) {
        ObservableList<Menu> menus = menuBar.getMenus();
        Menu fileMenu = new Menu("File");
        Menu adbMenu = new Menu("ADB");

        MenuItem gotoItem = new MenuItem("_Goto");
        MenuItem refreshItem = new MenuItem("_Refresh");
        MenuItem newItem = new MenuItem("_New Directory");

        newItem.setMnemonicParsing(true);
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        newItem.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog("New Directory");
            dialog.setTitle("New Directory");
            dialog.setHeaderText("Input name of the directory");
            dialog.showAndWait().ifPresent(mManager::createDirectory);
        });

        gotoItem.setMnemonicParsing(true);
        gotoItem.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN));
        gotoItem.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog(mNavigator.getCurrentPath());
            dialog.setTitle("Goto");
            dialog.setHeaderText("Where you want to go?");
            dialog.setContentText("Absolute Path: ");
            dialog.showAndWait().ifPresent(mNavigator::navigate);
        });

        refreshItem.setMnemonicParsing(true);
        refreshItem.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        refreshItem.setOnAction(event -> refresh());

        fileMenu.getItems().addAll(newItem, new SeparatorMenuItem(), refreshItem, gotoItem);

        MenuItem setAdbPath = new MenuItem("Select ADB Executable");
        setAdbPath.setOnAction(event -> handleSelectAdb());
        adbMenu.getItems().addAll(setAdbPath);

        menus.addAll(fileMenu, adbMenu);

        MenuItem delete = new MenuItem("_Delete");
        delete.setMnemonicParsing(true);
        delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN));
        delete.setOnAction(event -> handleDelete(mListView.getSelectionModel().getSelectedItem()));
        mItemContextMenu = new ContextMenu(newItem, delete, refreshItem, new SeparatorMenuItem(), gotoItem);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        mManager.dispose();
    }

    @Override
    public void onCacheUpdated(String path, List<FileModel> newCache) {
        if (!mNavigator.getCurrentPath().equals(path)) {
            Log.d("Cache for" + path + " updated, but current path is " + mNavigator.getCurrentPath());
            return;
        }

        mUserConfig.setLastPath(path);
        Platform.runLater(() -> {
            List<FileViewModel> models = mapToViewModel(path, newCache);
            synchronized (this) {
                setTitle(path);
                mListView.setItems(FXCollections.observableArrayList(models));
            }
        });
    }

    @Override
    public void onDirectoryCreated(String dir) {
        Log.d("Directory created: " + dir);
        refresh();
    }

    @Override
    public void onFileDeleted(String path) {
        Log.d("File deleted: " + path);
        refresh();
    }

    private void refresh() {
        mNavigator.navigate(mNavigator.getCurrentPath());
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
        fileCell.setContextMenu(mItemContextMenu);
        fileCell.setOnMouseClicked(this::handleMouseClick);
        fileCell.setOnDragOver(this::handleDragOver);
        fileCell.setOnDragDropped(this::handleDragDropped);
        return fileCell;
    }

    private void initData() {
        mManager = new FileManager();
        mManager.addOnCacheUpdatedListener(this);
        mNavigator = mManager.getNavigator();

        mUserConfig.getAdbPath().ifPresent(this::applyAdbPath);
        List<String> args = getParameters().getRaw();
        mNavigator.navigate(args.isEmpty() ? mUserConfig.getLastPath().orElse("/") : args.get(0));
    }

    private void applyAdbPath(String adbPath) {
        Log.d("Apply ADB: " + adbPath);
        mUserConfig.setAdbPath(adbPath);
        CommandHelper.setAdbPath(adbPath);
    }

    private void handleDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.COPY);
    }

    private void handleDragDropped(DragEvent event) {
        FileCell fileCell = (FileCell) event.getGestureTarget();
        FileViewModel viewModel = fileCell.getItem();

        String targetDirectory;
        if (viewModel == null) {
            targetDirectory = mNavigator.getCurrentPath();
        } else {
            FileModel fileModel = viewModel.getFileModel();
            targetDirectory = viewModel.getFileModel().getName();
        }

        Dragboard dragboard = event.getDragboard();
        dragboard.getFiles().forEach(it -> Log.d("======> Sending "
                + it.getAbsolutePath()
                + " to "
                + targetDirectory));
    }

    private void handleDelete(FileViewModel model) {
        if (model == null) {
            return;
        }

        FileModel fileModel = model.getFileModel();
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Delete");
        dialog.setHeaderText(fileModel.getName());
        dialog.setContentText("This is unrecoverable.\nAre you sure?\n\n" +
                "Delete command: \n"
                + CommandHelper.deleteCommand(mNavigator.getCurrentPath()
                + fileModel.getName()));

        dialog.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                mManager.deleteRecursively(fileModel.getName());
            }
        });
    }

    private void handleNavigateInto(FileViewModel model) {
        if (model == null) {
            return;
        }
        FileModel fileModel = model.getFileModel();

        // Apply caches first.
        // Replace them in onCacheUpdated()
        synchronized (this) {
            String cachedPath = null;
            if (model instanceof TopDirModel) {
                cachedPath = mNavigator.getParentPath();
            } else if (fileModel.isDirectory()) {
                cachedPath = mNavigator.getCurrentPath()
                        + fileModel.getName()
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

        } else if (fileModel.isEffectivelyDirectory()) {
            mNavigator.navigateInto(fileModel.getName());
        }
    }

    private void handleSelectAdb() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select ADB Executable");
        File adbFile = fileChooser.showOpenDialog(getPrimaryStage());
        if (adbFile != null) {
            applyAdbPath(adbFile.getAbsolutePath());
        }
    }

    public void handleMouseClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY
                && event.getClickCount() >= 2) {
            FileViewModel model = mListView.getSelectionModel().getSelectedItem();
            handleNavigateInto(model);
        }
    }
}
