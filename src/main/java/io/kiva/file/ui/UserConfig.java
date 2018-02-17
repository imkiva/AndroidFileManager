package io.kiva.file.ui;

import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author kiva
 * @date 2018/2/18
 */
public class UserConfig {
    private static final String ADB_PATH = "adb_path";
    private static final String LAST_PATH = "last_path";

    private Preferences mPreference;

    UserConfig(Class<?> clazz) {
        mPreference = Preferences.userNodeForPackage(clazz);
    }

    Optional<String> getAdbPath() {
        return Optional.ofNullable(mPreference.get(ADB_PATH, null));
    }

    Optional<String> getLastPath() {
        return Optional.ofNullable(mPreference.get(LAST_PATH, null));
    }

    void setAdbPath(String adbPath) {
        mPreference.put(ADB_PATH, adbPath);
        sync();
    }

    void setLastPath(String lastPath) {
        mPreference.put(LAST_PATH, lastPath);
        sync();
    }

    void sync() {
        try {
            mPreference.sync();
        } catch (BackingStoreException ignore) {
        }
    }
}
