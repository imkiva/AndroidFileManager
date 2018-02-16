package io.kiva.file.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class DirectoryNavigatorTest {
    @Test
    public void navigate() {
        DirectoryNavigator navigator = new DirectoryNavigator();
        navigator.navigate("/Users/kiva/Documents");
        assertEquals("/Users/kiva/Documents/", navigator.getCurrentPath());

        navigator.navigateTop();
        assertEquals("/Users/kiva/", navigator.getCurrentPath());

        navigator.navigateTop();
        assertEquals("/Users/", navigator.getCurrentPath());

        navigator.navigateTop();
        assertEquals("/", navigator.getCurrentPath());

        navigator.navigateTop();
        assertEquals("/", navigator.getCurrentPath());

        navigator.navigateInto("usr", "lib", "jvm");
        assertEquals("/usr/lib/jvm/", navigator.getCurrentPath());

        navigator.navigateInto("bin");
        assertEquals("/usr/lib/jvm/bin/", navigator.getCurrentPath());
        assertEquals("/usr/lib/jvm/", navigator.getParentPath());

        navigator.navigate("/");
        assertEquals("/", navigator.getParentPath());

        navigator.navigate("");
        assertEquals("/", navigator.getParentPath());
    }
}