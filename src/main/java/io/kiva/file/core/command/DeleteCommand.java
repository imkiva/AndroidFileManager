package io.kiva.file.core.command;

import io.kiva.process.Command;

/**
 * @author kiva
 * @date 2018/2/17
 */
public class DeleteCommand extends Command {
    public static final String SIGNAL = Command.makeSignal("RM_RF");

    public DeleteCommand(String path) {
        super(CommandHelper.createDirectoryCommand(path), SIGNAL, path);
    }
}
