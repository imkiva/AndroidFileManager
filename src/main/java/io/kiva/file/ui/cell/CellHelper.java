package io.kiva.file.ui.cell;

import io.kiva.file.core.model.FileModel;

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
}
