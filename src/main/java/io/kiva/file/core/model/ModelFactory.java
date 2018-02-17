package io.kiva.file.core.model;

import io.kiva.file.core.parser.FileType;
import io.kiva.file.core.parser.IOutputParser;
import io.kiva.file.core.parser.ParseResult;
import io.kiva.file.core.parser.impl.DirOutputParser;
import io.kiva.file.core.parser.impl.LsOutputParser;
import io.kiva.file.core.utils.Log;
import io.kiva.file.core.utils.Platform;
import io.kiva.process.ProcessOutput;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ModelFactory {
    private IOutputParser mParser;

    public static ModelFactory create() {
        switch (Platform.get()) {
            case WINDOWS:
                return new ModelFactory(new DirOutputParser());
            case LINUX:
            case AIX:
            case BSD:
            case SOLARIS:
                return new ModelFactory(new LsOutputParser());
            default:
                throw new RuntimeException("Unknown platform");
        }
    }

    public ModelFactory(IOutputParser mParser) {
        this.mParser = mParser;
    }

    public FileModel parseModel(ProcessOutput processOutput) {
        ParseResult result = mParser.parse(processOutput);
        if (result == null) {
            Log.e("Cannot parse: " + processOutput.getLine());
            return null;
        }

        FileModel model;
        if (result.mType == FileType.DIRECTORY) {
            model = new DirectoryModel(result);
        } else {
            model = new FileModel(result);
        }
        return model;
    }

    public void setSymbolLinkTarget(FileModel model, FileModel target) {
        model.setSymbolLinkTarget(target);
    }
}
