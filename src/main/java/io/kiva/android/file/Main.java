package io.kiva.android.file;

import io.kiva.android.file.core.DirectoryNavigator;
import io.kiva.android.file.core.FileManager;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        DirectoryNavigator navigator = fileManager.getNavigator();
    }
}
