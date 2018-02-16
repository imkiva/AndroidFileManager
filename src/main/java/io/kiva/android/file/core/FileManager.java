package io.kiva.android.file.core;

import io.kiva.android.file.core.model.ModelFactory;
import io.kiva.android.file.core.parser.impl.DirOutputParser;
import io.kiva.android.file.core.parser.impl.LsOutputParser;
import io.kiva.android.file.core.process.IOutputListener;
import io.kiva.android.file.core.process.ProcessOutput;
import io.kiva.android.file.core.process.ShellProcess;
import io.kiva.android.file.core.utils.Platform;
import io.kiva.android.file.core.utils.SystemProperty;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class FileManager implements OnDirectoryChangedListener, IOutputListener {
    private static final String USER_DIR_PROPERTY = "user.dir";
    private static final ModelFactory FACTORY;

    static {
        switch (Platform.get()) {
            case WINDOWS:
                FACTORY = new ModelFactory(new DirOutputParser());
                break;
            case LINUX:
            case AIX:
            case BSD:
            case SOLARIS:
                FACTORY = new ModelFactory(new LsOutputParser());
                break;
            default:
                throw new RuntimeException("Unknown platform");
        }
    }

    private final DirectoryNavigator mDirectoryNavigator;
    private final ShellProcess mShell;

    public FileManager() {
        this(SystemProperty.get(USER_DIR_PROPERTY));
    }

    public FileManager(String mainDir) {
        mDirectoryNavigator = new DirectoryNavigator();
        mDirectoryNavigator.navigate(mainDir);
        mDirectoryNavigator.addOnDirectoryChangedListener(this);
        mShell = ShellProcess.open();
        mShell.setOutputListener(this);
    }

    public DirectoryNavigator getNavigator() {
        return mDirectoryNavigator;
    }

    @Override
    public void onDirectoryChanged(String newPath) {
        mShell.writeCommand(FileHelper.buildAndroidListCommand(newPath));
    }

    @Override
    public void onNewOutput(ProcessOutput output) {

    }
}
