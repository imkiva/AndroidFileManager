package io.kiva.android.file.core;

import io.kiva.android.file.core.model.FileModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class DirectoryCache {
    private final Map<String, ArrayList<FileModel>> mCache = new HashMap<>();

    DirectoryCache() {
    }
}
