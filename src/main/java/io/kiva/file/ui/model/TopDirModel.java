package io.kiva.file.ui.model;

import io.kiva.file.core.model.FileModel;
import io.kiva.file.core.parser.FileType;
import io.kiva.file.core.parser.ParseResult;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class TopDirModel extends FileViewModel {
    private static final FileModel DEFAULT_TOP_DIR_MODEL;

    static {
        ParseResult parseResult = new ParseResult();
        parseResult.mType = FileType.DIRECTORY;
        parseResult.mGroup = "unknown";
        parseResult.mOwner = "unknown";
        parseResult.mTime = "unknown";
        parseResult.mSize = 4096;
        parseResult.mPermission = "d---------";
        parseResult.mName = "<Top Directory>";
        DEFAULT_TOP_DIR_MODEL = new FileModel(parseResult);
    }

    public TopDirModel() {
        super(DEFAULT_TOP_DIR_MODEL);
    }
}
