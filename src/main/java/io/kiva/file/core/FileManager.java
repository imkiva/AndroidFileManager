package io.kiva.file.core;

import io.kiva.file.core.command.CreateDirectoryCommand;
import io.kiva.file.core.command.DeleteCommand;
import io.kiva.file.core.command.LsCommand;
import io.kiva.file.core.model.FileModel;
import io.kiva.file.core.model.ModelFactory;
import io.kiva.file.core.parser.FileType;
import io.kiva.file.core.utils.FileHelper;
import io.kiva.file.core.utils.Log;
import io.kiva.process.Command;
import io.kiva.process.IOutputListener;
import io.kiva.process.ProcessOutput;
import io.kiva.process.ShellProcess;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class FileManager implements OnDirectoryChangedListener, IOutputListener {

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

    private final ArrayList<FileManagerCallback> mListeners = new ArrayList<>();
    private final DirectoryNavigator mDirectoryNavigator = new DirectoryNavigator();
    private final DirectoryCache mCache = new DirectoryCache();
    private final ShellProcess mShell;

    private String mLoadingPath;
    private ArrayList<FileModel> mLoadingModels;

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

    public List<FileModel> getCachedCurrentFileList() {
        return getCachedFileList(mDirectoryNavigator.getCurrentPath());
    }

    public List<FileModel> getCachedFileList(String path) {
        return mCache.get(path);
    }

    public void addOnCacheUpdatedListener(FileManagerCallback listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        this.mListeners.add(listener);
    }

    public void removeOnCacheUpdatedListener(FileManagerCallback listener) {
        this.mListeners.remove(listener);
    }

    @Override
    public void onDirectoryChanged(String newPath) {
        Log.d("Updating cache for " + newPath);
        mLoadingPath = newPath;
        mLoadingModels = new ArrayList<>();
        mShell.addCommand(new LsCommand(newPath));
    }

    public void createDirectory(String dirName) {
        createDirectory(mDirectoryNavigator.getCurrentPath(), dirName);
    }

    public void createDirectory(String parentPath, String dirName) {
        File file = new File(parentPath, dirName);
        mShell.addCommand(new CreateDirectoryCommand(file.getAbsolutePath()));
    }

    @Override
    public void onNewOutput(ProcessOutput output) {
        String line = output.getLine();
        if (!Command.isSignal(line)) {
            FileModel model = FACTORY.parseModel(output);
            if (model != null) {
                mLoadingModels.add(model);
            }
            return;
        }

        if (line.startsWith(LsCommand.SIGNAL)) {
            Log.d("Cache for " + mLoadingPath + " updated");
            resolveSymbolLink(mLoadingModels);
            mLoadingModels.sort(COMPARATOR);
            mCache.update(mLoadingPath, mLoadingModels);
            notifyChangeUpdated(mLoadingPath, mLoadingModels);
            mLoadingPath = null;
            mLoadingModels = null;
            return;
        }

        if (line.startsWith(CreateDirectoryCommand.SIGNAL)) {
            String directory = line.substring(CreateDirectoryCommand.SIGNAL.length());
            notifyDirectoryCreated(directory);
            return;
        }

        if (line.startsWith(DeleteCommand.SIGNAL)) {
            String path = line.substring(DeleteCommand.SIGNAL.length());
            notifyDirectoryCreated(path);
            return;
        }
    }

    private void notifyChangeUpdated(String path, List<FileModel> cache) {
        mListeners.forEach(listener -> listener.onCacheUpdated(path, cache));
    }

    private void notifyDirectoryCreated(String dir) {
        mListeners.forEach(listener -> listener.onDirectoryCreated(dir));
    }

    private void notifyFileDeleted(String path) {
        mListeners.forEach(listener -> listener.onFileDeleted(path));
    }

    public void dispose() {
        mShell.close();
    }

    private void resolveSymbolLink(ArrayList<FileModel> fileModels) {
        fileModels.parallelStream()
                .filter(model -> model.getFileType() == FileType.SYMBOL_LINK)
                .forEach(model -> {
                    String targetName = model.getSymbolLinkName();

                    // target is in the same dir as symbol link file
                    if (!targetName.contains(File.separator)) {
                        FACTORY.setSymbolLinkTarget(model, findTarget(fileModels, targetName));
                        return;
                    }

                    String whereTarget = FileHelper.dirName(targetName);
                    String targetBaseName = FileHelper.baseName(targetName);
                    FACTORY.setSymbolLinkTarget(model,
                            findTarget(mCache.get(whereTarget), targetBaseName));
                });
    }

    private FileModel findTarget(List<FileModel> fileModels, String targetName) {
        return fileModels.stream()
                .filter(model -> model.getName().equals(targetName))
                .findFirst()
                .orElse(null);
    }
}
