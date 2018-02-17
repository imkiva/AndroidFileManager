package io.kiva.file.core.command;

import io.kiva.process.Command;

/**
 * @author kiva
 * @date 2018/2/17
 */
public class LsCommand extends Command {
    public static final String SIGNAL = Command.makeSignal("LS");

    public LsCommand(String path) {
        super(CommandHelper.listCommand(path), SIGNAL, path);
    }
}
