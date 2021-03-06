package io.kiva.file.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class FileManagerTest {
    @Test
    public void mainDir() {
        FileManager manager = new FileManager();
        manager.getNavigator().navigate("/storage/sdcard0");
        assertEquals("/storage/sdcard0/", manager.getNavigator().getCurrentPath());
    }
}