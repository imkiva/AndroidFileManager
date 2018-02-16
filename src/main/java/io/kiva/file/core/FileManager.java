package io.kiva.file.core;

import io.kiva.file.core.model.FileModel;
import io.kiva.file.core.model.ModelFactory;
import io.kiva.file.core.utils.FileHelper;
import io.kiva.file.core.utils.Log;
import io.kiva.process.IOutputListener;
import io.kiva.process.ProcessOutput;
import io.kiva.process.ShellProcess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class FileManager implements OnDirectoryChangedListener, IOutputListener {
    private static final String COMMAND_END = "----AFM---COMMAND-END----";
    private static final ModelFactory FACTORY = ModelFactory.create();
    private static final Comparator<FileModel> COMPARATOR = (lhs, rhs) -> {
        boolean lhsIsDir = lhs.isDirectory();
        boolean rhsIsDir = rhs.isDirectory();
        if (lhsIsDir && !rhsIsDir) {
            return -1;
        } else if (!lhsIsDir && rhsIsDir) {
            return 1;
        } else if (lhsIsDir) {
            return lhs.getName()
                    .toLowerCase()
                    .compareTo(rhs.getName().toLowerCase());
        } else {
            return lhs.getName().compareTo(rhs.getName());
        }
    };

    private final ArrayList<OnCacheUpdatedListener> mListeners = new ArrayList<>();
    private final DirectoryNavigator mDirectoryNavigator = new DirectoryNavigator();
    private final DirectoryCache mCache = new DirectoryCache();
    private final ShellProcess mShell;

    private String mLoadingPath;
    private ArrayList<FileModel> mLoading;

    public FileManager() {
        mShell = ShellProcess.open();
        mShell.setSafeExit(true);
        mShell.setWaitTimeout(1000);
        mShell.setOutputListener(this);
        mDirectoryNavigator.addOnDirectoryChangedListener(this);
    }

    public DirectoryNavigator getNavigator() {
        return mDirectoryNavigator;
    }

    public List<FileModel> getCurrentFileList() {
        return mCache.get(getNavigator().getCurrentPath());
    }

    public void addOnCacheUpdatedListener(OnCacheUpdatedListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        this.mListeners.add(listener);
    }

    public void removeOnCacheUpdatedListener(OnCacheUpdatedListener listener) {
        this.mListeners.remove(listener);
    }

    @Override
    public void onDirectoryChanged(String newPath) {
        Log.d("Updating cache for " + newPath);
        mLoading = new ArrayList<>();
        mLoadingPath = newPath;
        mShell.writeCommand(FileHelper.buildAndroidListCommand(newPath)
                + ";"
                + FileHelper.buildAndroidFinishCommand(COMMAND_END));
    }

    @Override
    public void onNewOutput(ProcessOutput output) {
        Log.i("-> " + output.getLine());
        if (output.getLine().equals(COMMAND_END)) {
            Log.d("Cache for " + mLoadingPath + " updated");
            mLoading.sort(COMPARATOR);
            mCache.update(mLoadingPath, mLoading);
            notifyChangeUpdated(mLoadingPath, mLoading);
            mLoadingPath = null;
            mLoading = null;
            return;
        }

        FileModel model = FACTORY.parseModel(output);
        if (model != null) {
            mLoading.add(model);
        }
    }

    private void notifyChangeUpdated(String path, List<FileModel> cache) {
        mListeners.forEach(listener -> listener.onCacheUpdated(path, cache));
    }

    public void dispose() {
        mShell.close();
    }
}
