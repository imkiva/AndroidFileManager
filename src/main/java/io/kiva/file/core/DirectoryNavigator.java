package io.kiva.file.core;

import io.kiva.file.core.utils.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class DirectoryNavigator {
    private List<OnDirectoryChangedListener> mListeners = new ArrayList<>();
    private Stack<String> mDirs = new Stack<>();

    public DirectoryNavigator() {
    }

    public DirectoryNavigator(String mainDir) {
        navigate(mainDir);
    }

    public void addOnDirectoryChangedListener(OnDirectoryChangedListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        mListeners.add(listener);
    }

    public void removeOnDirectoryChangedListener(OnDirectoryChangedListener listener) {
        mListeners.remove(listener);
    }

    private void push(String path) {
        mDirs.push(path);
        notifyDirectoryChanged();
    }

    private void pop() {
        if (!mDirs.empty()) {
            mDirs.pop();
            notifyDirectoryChanged();
        }
    }

    private void notifyDirectoryChanged() {
        String newPath = getCurrentPath();
        mListeners.forEach(it -> it.onDirectoryChanged(newPath));
    }

    public void navigateTop() {
        pop();
    }

    public void navigateInto(String... dir) {
        navigate(getCurrentPath() + joinToPath(Arrays.stream(dir)));
    }

    public void navigate(String newPath) {
        mDirs.clear();
        FileHelper.splitPath(newPath)
                .stream()
                .filter(s -> !s.isEmpty())
                .forEach(it -> mDirs.push(it));
        notifyDirectoryChanged();
    }

    public String getCurrentPath() {
        return joinToPath(mDirs.stream());
    }

    public String getParentPath() {
        if (mDirs.empty()) {
            return File.separator;
        }
        return joinToPath(mDirs.stream().limit(mDirs.size() - 1));
    }

    private String joinToPath(Stream<String> stream) {
        StringBuilder builder = new StringBuilder(File.separator);
        stream.forEach(it -> builder.append(it).append(File.separator));
        return builder.toString();
    }
}
