package io.kiva.android.file.core;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author kiva
 * @date 2018/2/14
 */
final class FilePathHelper {
    static List<String> splitPath(String path) {
        return Arrays.asList(path.split(File.separator));
    }
}
