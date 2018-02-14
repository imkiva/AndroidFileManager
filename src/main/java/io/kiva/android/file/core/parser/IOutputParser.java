package io.kiva.android.file.core.parser;

import io.kiva.android.file.core.process.ProcessOutput;

/**
 * @author kiva
 * @date 2018/2/14
 */
public interface IOutputParser {
    ParseResult parse(ProcessOutput processOutput);
}
