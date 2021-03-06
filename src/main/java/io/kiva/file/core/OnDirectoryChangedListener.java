package io.kiva.file.core;

/**
 * @author kiva
 * @date 2018/2/15
 */
@FunctionalInterface
public interface OnDirectoryChangedListener {
    void onDirectoryChanged(String newPath);
}
