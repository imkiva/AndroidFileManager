package io.kiva.file.core;

import io.kiva.file.core.model.FileModel;

import java.util.List;

/**
 * @author kiva
 * @date 2018/2/16
 */
@FunctionalInterface
public interface OnCacheUpdatedListener {
    void onCacheUpdated(String path, List<FileModel> newCache);
}
