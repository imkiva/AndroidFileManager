package io.kiva.android.file.core;

import io.kiva.android.file.core.model.FileModel;

import java.util.List;

/**
 * @author kiva
 * @date 2018/2/16
 */
public interface OnCacheUpdatedListener {
    void onCacheUpdated(String path, List<FileModel> newCache);
}
