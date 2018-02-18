package io.kiva.file.core.command;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author kiva
 * @date 2018/2/19
 */
public class LsCommandTest {

    @Test
    public void test() {
        assertEquals("adb shell ls -al \"abc\"", new LsCommand("abc").toString());
    }
}