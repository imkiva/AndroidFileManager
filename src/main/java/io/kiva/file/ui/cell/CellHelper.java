package io.kiva.file.ui.cell;

import io.kiva.file.core.model.FileModel;
import io.kiva.fx.FxApplication;
import javafx.scene.image.Image;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class CellHelper {
    static String buildLabelFor(FileModel model) {
        StringBuilder builder = new StringBuilder(model.getName());
        switch (model.getFileType()) {
            case SYMBOL_LINK:
                builder.append("  ->  ").append(model.getSymbolLinkName());
                break;
        }
        return builder.toString();
    }

    public static Image buildIconFor(FxApplication app, FileModel model) {
        switch (model.getFileType()) {
            case DIRECTORY:
                return app.loadImage("file_type_dir.png");
            case SYMBOL_LINK: {
                FileModel target = model.getSymbolLinkTarget();
                if (target != null && target.isDirectory()) {
                    return app.loadImage("file_type_dir.png");
                }
            }
            default:
                return app.loadImage("file_type_unknown.png");
        }
    }
}
