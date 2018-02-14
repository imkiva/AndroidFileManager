package io.kiva.android.file.core;

import io.kiva.android.file.core.utils.SystemProperty;

import java.security.AccessController;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class FileManager {
    private static final String USER_DIR_PROPERTY = "user.dir";

    private final DirectoryStack mDirectoryStack = new DirectoryStack();

    public FileManager() {
        this(SystemProperty.get(USER_DIR_PROPERTY));
    }

    public FileManager(String mainDir) {
        FilePathHelper.splitPath(mainDir)
                .stream()
                .filter(s -> !s.isEmpty())
                .forEach(mDirectoryStack::push);
    }

    public DirectoryStack getDirectoryStack() {
        return mDirectoryStack;
    }
}
