package io.kiva.android.file.core.model;

import io.kiva.android.file.core.parser.IOutputParser;
import io.kiva.android.file.core.parser.ParseResult;
import io.kiva.android.file.core.process.ProcessOutput;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class ModelFactory {
    private IOutputParser mParser;

    public ModelFactory(IOutputParser mParser) {
        this.mParser = mParser;
    }

    public FileModel parseModel(ProcessOutput processOutput) {
        ParseResult result = mParser.parse(processOutput);
        if (result == null) {
            return null;
        }
        FileModel model;
        if (result.isDirectory) {
            model = new DirectoryModel(result);
        } else {
            model = new FileModel(result);
        }
        return model;
    }
}
