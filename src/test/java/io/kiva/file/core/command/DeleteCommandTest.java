package io.kiva.file.core.command;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author kiva
 * @date 2018/2/19
 */
public class DeleteCommandTest {

    @Test
    public void test() {
        assertEquals("adb shell rm -rf \"abc\"", new DeleteCommand("abc").toString());
    }
}