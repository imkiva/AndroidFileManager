package io.kiva.android.file.core.model;

import io.kiva.android.file.core.parser.FileType;
import io.kiva.android.file.core.parser.IOutputParser;
import io.kiva.android.file.core.parser.ParseResult;
import io.kiva.android.file.core.process.ProcessOutput;
import io.kiva.android.file.core.utils.Log;

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
}
