package io.kiva.android.file.core;

import java.util.Stack;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class FileManager {
    private static final String USER_DIR_PROPERTY = "user.dir";

    private final Stack<String> mDirs = new Stack<>();

    public FileManager() {
        this(System.getProperty(USER_DIR_PROPERTY));
    }

    public FileManager(String mainDirectory) {
        mDirs.push(mainDirectory);
    }
}
