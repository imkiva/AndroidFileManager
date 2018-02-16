package io.kiva.file.ui;

import io.kiva.file.core.DirectoryNavigator;
import io.kiva.file.core.FileManager;
import io.kiva.file.core.OnCacheUpdatedListener;
import io.kiva.file.core.model.FileModel;
import io.kiva.file.core.utils.FileHelper;
import io.kiva.file.core.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class CommandLine implements OnCacheUpdatedListener {
    private final FileManager mManager;
    private final DirectoryNavigator mNavigator;

    public CommandLine(String mainDir) {
        Log.setLogEnabled(false);
        mManager = new FileManager();
        mManager.addOnCacheUpdatedListener(this);
        mNavigator = mManager.getNavigator();
        // Jump to main dir
        mNavigator.navigate(mainDir);
    }

    public void main() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            boolean skipNextShow = false;
            do {
                if (!skipNextShow) {
                    showFiles();
                }
                skipNextShow = false;

                if ((line = reader.readLine()) == null) {
                    break;
                }

                line = line.trim();
                if (line.equals("#exit")) {
                    break;

                } else if (line.equals("#pwd")) {
                    skipNextShow = true;
                    System.out.println(mNavigator.getCurrentPath());

                } else if (line.startsWith("/")) {
                    mNavigator.navigate(line);

                } else if (line.equals(".") || line.isEmpty()) {
                    continue;

                } else if (line.equals("..")) {
                    mNavigator.navigateTop();

                } else {
                    List<String> path = FileHelper.splitPath(line);
                    mNavigator.navigateInto(path.toArray(new String[path.size()]));
                }
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mManager.dispose();
        }
    }

    @Override
    public void onCacheUpdated(String path, List<FileModel> newCache) {
        showFiles(newCache);
    }

    private void showFiles(List<FileModel> files) {
        files.forEach(fileModel ->
                System.out.printf(Locale.getDefault(),
                        "%s%s\n",
                        fileModel.getName(),
                        fileModel.isDirectory() ? "/" : "")
        );
    }

    private void showFiles() {
        showFiles(mManager.getCurrentFileList());
    }
}
