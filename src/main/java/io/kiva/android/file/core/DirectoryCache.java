package io.kiva.android.file.core;

import io.kiva.android.file.core.model.FileModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kiva
 * @date 2018/2/16
 */
final class DirectoryCache {
    private final Map<String, ArrayList<FileModel>> mCache = new HashMap<>();

    DirectoryCache() {
    }

    void clear() {
        mCache.clear();
    }

    void update(String path, List<FileModel> updated) {
        ArrayList<FileModel> cache = getInternal(path);
        cache.clear();
        cache.addAll(updated);
    }

    List<FileModel> get(String path) {
        return getInternal(path);
    }

    private ArrayList<FileModel> getInternal(String path) {
        return mCache.computeIfAbsent(path, k -> new ArrayList<>());
    }
}
