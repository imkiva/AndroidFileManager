package io.kiva.process;

import io.kiva.file.core.utils.OperatingSystem;
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
        switch (OperatingSystem.get()) {
            case WINDOWS:
                shellProcess.addCommand("dir");
                break;
            case SOLARIS:
            case BSD:
            case AIX:
            case LINUX:
                shellProcess.addCommand("ls -l /");
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
        shellProcess.addCommand("exit 80");
        shellProcess.close();
        assertEquals(80, shellProcess.getExitCode());
    }
}