package io.kiva.android.file.core;

import io.kiva.android.file.core.utils.SystemProperty;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class FileManager {
    private static final String USER_DIR_PROPERTY = "user.dir";

    private final DirectoryNavigator mDirectoryNavigator = new DirectoryNavigator();

    public FileManager() {
        this(SystemProperty.get(USER_DIR_PROPERTY));
    }

    public FileManager(String mainDir) {
        mDirectoryNavigator.navigate(mainDir);
    }

    public DirectoryNavigator getNavigator() {
        return mDirectoryNavigator;
    }
}
