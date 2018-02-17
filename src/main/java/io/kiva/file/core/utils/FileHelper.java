package io.kiva.file.core.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author kiva
 * @date 2018/2/14
 */
public final class FileHelper {
    public static List<String> splitPath(String path) {
        return Arrays.asList(path.split(File.separator));
    }

    public static String dirName(String fileName) {
        return new File(fileName).getParent();
    }

    public static String baseName(String fileName) {
        return new File(fileName).getName();
    }

    public static boolean isRootDirectory(String dir) {
        return dir.trim().equals(File.separator);
    }
}
