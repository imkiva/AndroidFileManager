package io.kiva.file.core.command;

import io.kiva.process.Command;

import java.io.File;
import java.util.List;

/**
 * @author kiva
 * @date 2018/2/18
 */
public class PushFileCommand extends Command {
    public static final String SIGNAL = Command.makeSignal("PUSH");

    public PushFileCommand(String targetDirectory, List<File> sources) {
        super(CommandHelper.pushCommand(targetDirectory, sources),
                SIGNAL,
                targetDirectory);
    }
}
