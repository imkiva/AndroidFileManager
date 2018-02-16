package io.kiva.file.core.parser;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ParseResult {
    public String mPermission;
    public String mGroup;
    public String mOwner;
    public long mSize;
    public String mTime;
    public String mName;
    public String mSymbolLinkName;
    public FileType mType;

    @Override
    public String toString() {
        return "ParseResult [" +
                "name: " +
                mName +
                ", type: " +
                mType.getName() +
                ", group: " +
                mGroup +
                ", owner: " +
                mOwner +
                ", size: " +
                mSize +
                ", permission: " +
                mPermission +
                ", symbol link: " +
                mSymbolLinkName +
                "]";
    }
}
