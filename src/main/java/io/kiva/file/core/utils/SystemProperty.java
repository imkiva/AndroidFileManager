package io.kiva.file.core.utils;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class SystemProperty {
    public static String get(String key) {
        return AccessController.doPrivileged(
                (PrivilegedAction<String>) () -> System.getProperty(key)
        );
    }
}
