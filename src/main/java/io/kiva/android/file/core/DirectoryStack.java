package io.kiva.android.file.core;

import java.io.File;
import java.util.Stack;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class DirectoryStack {
    private Stack<String> mDirs = new Stack<>();

    public void push(String path) {
        mDirs.push(path);
    }

    public String join() {
        StringBuilder builder = new StringBuilder(File.separator);
        mDirs.forEach(it -> builder.append(it).append(File.separator));
        return builder.toString();
    }
}
