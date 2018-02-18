package io.kiva.file.core.command;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author kiva
 * @date 2018/2/19
 */
public class CreateDirectoryCommandTest {

    @Test
    public void test() {
        assertEquals("adb shell mkdir \"abc\"", new CreateDirectoryCommand("abc").toString());
        assertEquals("adb shell mkdir \"a\\ b\\ c\"", new CreateDirectoryCommand("a b c").toString());
    }
}