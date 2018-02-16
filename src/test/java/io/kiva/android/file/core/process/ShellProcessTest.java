package io.kiva.android.file.core.process;

import io.kiva.android.file.core.utils.Platform;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ShellProcessTest {

    @Test
    public void output() {
        ShellProcess shellProcess = ShellProcess.open();
        shellProcess.setSafeExit(true);
        shellProcess.setOutputListener(System.out::println);
        switch (Platform.get()) {
            case WINDOWS:
                shellProcess.writeCommand("dir");
                break;
            case SOLARIS:
            case BSD:
            case AIX:
            case LINUX:
                shellProcess.writeCommand("ls -l /");
                break;
        }
        shellProcess.close();
    }

    @Test
    public void openAndClose() {
        ShellProcess shellProcess = ShellProcess.open();
        assertNotNull(shellProcess);
        shellProcess.close();
    }

    @Test
    public void exitCode() {
        ShellProcess shellProcess = ShellProcess.open();
        assertNotNull(shellProcess);
        shellProcess.setSafeExit(true);
        shellProcess.writeCommand("exit 80");
        shellProcess.close();
        assertEquals(80, shellProcess.getExitCode());
    }
}