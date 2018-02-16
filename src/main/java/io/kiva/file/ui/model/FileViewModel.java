package io.kiva.file.ui.model;

import io.kiva.file.core.model.FileModel;

/**
 * @author kiva
 * @date 2018/2/16
 */
public class FileViewModel {
    private FileModel mFileModel;

    public FileViewModel(FileModel fileModel) {
        this.mFileModel = fileModel;
    }

    public FileModel getFileModel() {
        return mFileModel;
    }
}
