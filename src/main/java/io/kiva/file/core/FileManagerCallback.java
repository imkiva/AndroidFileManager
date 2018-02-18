package io.kiva.file.core;

import io.kiva.file.core.model.FileModel;

import java.util.List;

/**
 * @author kiva
 * @date 2018/2/16
 */
public interface FileManagerCallback {
    void onCacheUpdated(String path, List<FileModel> newCache);

    void onDirectoryCreated(String dir);

    void onFileDeleted(String path);

    void onAllFilesPushed(String targetDirectory);
}
