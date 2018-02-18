package io.kiva.file.core.command;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * @author kiva
 * @date 2018/2/19
 */
public class PushFileCommandTest {

    @Test
    public void test() {
        assertEquals("adb push \"/a\" \"/b\" \"target\"",
                new PushFileCommand("target",
                        new File("/a"),
                        new File("/b")).toString());
    }
}