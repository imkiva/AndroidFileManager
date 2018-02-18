package io.kiva.file.core.model;

import io.kiva.file.core.parser.FileType;
import io.kiva.file.core.parser.ParseResult;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class FileModel {
    private ParseResult mParseResult;
    private FileModel mSymbolLinkTarget;

    public boolean isEffectivelyDirectory() {
        if (isDirectory()) {
            return true;

        } else if (getFileType() == FileType.SYMBOL_LINK) {
            FileModel target = getSymbolLinkTarget();
            return target != null && target.isEffectivelyDirectory();
        }
        return false;
    }

    public FileModel getSymbolLinkTarget() {
        return mSymbolLinkTarget;
    }

    public FileModel(ParseResult parseResult) {
        this.mParseResult = parseResult;
    }

    public FileType getFileType() {
        return mParseResult.mType;
    }

    public boolean isDirectory() {
        return mParseResult.mType == FileType.DIRECTORY;
    }

    public String getPermission() {
        return mParseResult.mPermission;
    }

    public String getGroup() {
        return mParseResult.mGroup;
    }

    public String getOwner() {
        return mParseResult.mOwner;
    }

    public long getSize() {
        return mParseResult.mSize;
    }

    public String getTime() {
        return mParseResult.mTime;
    }

    public String getName() {
        return mParseResult.mName;
    }

    public String getSymbolLinkName() {
        return mParseResult.mSymbolLinkName;
    }

    @Override
    public String toString() {
        return mParseResult.toString();
    }

    void setSymbolLinkTarget(FileModel mSymbolLinkTarget) {
        this.mSymbolLinkTarget = mSymbolLinkTarget;
    }
}
