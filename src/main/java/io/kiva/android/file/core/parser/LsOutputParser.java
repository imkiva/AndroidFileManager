package io.kiva.android.file.core.parser;

import io.kiva.android.file.core.process.ProcessOutput;

import java.util.regex.Pattern;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class LsOutputParser implements IOutputParser {
    public static final Pattern PATTERN = Pattern.compile(
            "([dlspbc-][rwxtsSX-]{9})\\s*" + // permission
                    "([a-zA-Z0-9_]+)\\s*" + // group
                    "([a-zA-Z0-9_]+)\\s*" + // owner
                    "([0-9]*\\s*)?" + // size
                    "(((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])\\s*([01]?[0-9]|2[0-3]):[0-5][0-9])\\s*" + // time
                    "([^<>/\\\\|:\"*?]+(\\.\\w+)?)\\s*" + // name
                    "(->\\s*" +
                    "(([^<>/\\\\|:\"*?]|[/])+(\\.\\w+)?)" + // symbol link name
                    ")?"
    );

    @Override
    public ParseResult parse(ProcessOutput processOutput) {
        String line = processOutput.getLine();
        return null;
    }
}
