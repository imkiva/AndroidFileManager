package io.kiva.android.file.core.parser;

import io.kiva.android.file.core.model.FileModel;
import io.kiva.android.file.core.model.ModelFactory;
import io.kiva.android.file.core.process.ShellProcess;
import org.junit.Test;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class UnionTest {
    @Test
    public void adbShell() {
        ShellProcess process = ShellProcess.open();
        ModelFactory factory = new ModelFactory(new LsOutputParser());
        process.setOutputListener(output -> {
            FileModel model = factory.parseModel(output);
            if (model != null) {
                System.out.println(model.toString());
            }
        });
        process.setSafeExit(true);
        process.setWaitTimeout(1000);
        process.writeCommand("adb shell ls -l /sdcard/");

        process.close();
    }
}
