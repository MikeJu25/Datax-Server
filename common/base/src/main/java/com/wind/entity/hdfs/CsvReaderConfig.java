package com.wind.entity.hdfs;

import lombok.Data;

@Data
public class CsvReaderConfig {
    private Boolean caseSensitive, trimWhitespace, useTextQualifier,
            useComments, safetySwitch, skipEmptyRecords, captureRawRecord;
    private String textQualifier, delimiter, recordDelimiter, comment;
    private Integer escapeMode;
}
