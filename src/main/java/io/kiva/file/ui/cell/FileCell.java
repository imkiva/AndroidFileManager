package io.kiva.file.ui.cell;

import io.kiva.file.core.model.FileModel;
import io.kiva.file.ui.model.FileViewModel;
import io.kiva.fx.FxApplication;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class FileCell extends ListCell<FileViewModel> {
    private FxApplication mApplication;

    public FileCell(FxApplication application) {
        this.mApplication = application;
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
        text.setText(CellHelper.buildLabelFor(model));

        Image image = model.isDirectory()
                ? mApplication.loadImage("file_type_dir.png")
                : mApplication.loadImage("file_type_unknown.png");

        ImageView imageView = new ImageView(image);

        pane.setLeft(imageView);
        pane.setCenter(text);
        this.setGraphic(pane);
    }
}