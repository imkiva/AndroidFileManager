package io.kiva.android.file.core.parser.impl;

import io.kiva.android.file.core.parser.FileType;
import io.kiva.android.file.core.parser.IOutputParser;
import io.kiva.android.file.core.parser.ParseResult;
import io.kiva.android.file.core.process.ProcessOutput;
import io.kiva.android.file.core.utils.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.kiva.android.file.core.parser.FileType.*;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class LsOutputParser implements IOutputParser {
    private static final int GROUP_PERMISSION = 1;
    private static final int GROUP_GROUP = 2;
    private static final int GROUP_OWNER = 3;
    private static final int GROUP_SIZE = 4;
    private static final int GROUP_TIME = 5;
    private static final int GROUP_NAME = 11;
    private static final int GROUP_SYMBOL_LINK = 14;

    public static final Pattern PATTERN = Pattern.compile(
            "([dlspbc-][rwxtsSX-]{9})\\s*" + // permission
                    "([a-zA-Z0-9_]+)\\s*" + // group
                    "([a-zA-Z0-9_]+)\\s*" + // owner
                    "([0-9]*)\\s*?" + // size
                    "(((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])\\s*([01]?[0-9]|2[0-3]):[0-5][0-9])\\s*" + // time
                    "([^<>/\\\\|:\"*?]+(\\.\\w+)?)\\s*" + // name
                    "(->\\s*" +
                    "(([^<>/\\\\|:\"*?]|[/])+(\\.\\w+)?)" + // symbol link name
                    ")?"
    );

    @Override
    public ParseResult parse(ProcessOutput processOutput) {
        String line = processOutput.getLine();
        Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            Log.e("Cannot match output: " + line);
            return null;
        }

        ParseResult result = new ParseResult();

        result.mGroup = matcher.group(GROUP_GROUP);
        result.mOwner = matcher.group(GROUP_OWNER);
        result.mTime = matcher.group(GROUP_TIME);
        result.mName = matcher.group(GROUP_NAME);
        result.mSymbolLinkName = matcher.group(GROUP_SYMBOL_LINK);

        String permission = matcher.group(GROUP_PERMISSION);
        result.mType = parseFileType(permission.charAt(0));
        result.mPermission = permission.substring(1);

        switch (result.mType) {
            case FILE:
                result.mSize = Long.parseLong(matcher.group(GROUP_SIZE));
                break;
            case DIRECTORY:
                result.mSize = 4096;
                break;
            default:
                result.mSize = 0;
                break;
        }
        return result;
    }

    private static FileType parseFileType(char c) {
        switch (c) {
            case '-':
                return FILE;
            case 'd':
                return DIRECTORY;
            case 'l':
                return SYMBOL_LINK;
            case 'b':
                return BLOCK_DEVICE;
            case 'c':
                return CHAR_DEVICE;
            case 'p':
                return NAMED_PIPE;
            case 's':
                return SOCKET;
            default:
                return UNKNOWN;
        }
    }
}
