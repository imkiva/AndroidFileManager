package io.kiva.file.core.parser;

/**
 * @author kiva
 * @date 2018/2/14
 */
public enum FileType {
    UNKNOWN("Unknown"),
    FILE("File"),
    DIRECTORY("Directory"),
    SYMBOL_LINK("SymbolLink"),
    BLOCK_DEVICE("BlockDevice"),
    CHAR_DEVICE("CharDevice"),
    NAMED_PIPE("Pipe"),
    SOCKET("Socket");

    private String mName;

    FileType(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }
}
