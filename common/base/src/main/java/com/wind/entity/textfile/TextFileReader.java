package com.wind.entity.textfile;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TextFileReader extends TextFileHandler {
    private ArrayList<TextFileBlock> column;
    private Boolean skipHeader;
    private CsvReaderConfig csvReaderConfig;
    private String fieldDelimiter;

    public boolean gainSafetySwitch() {
        return csvReaderConfig.getSafetySwitch();
    }

    public boolean gainSkipEmptyRecords() {
        return csvReaderConfig.getSkipEmptyRecords();
    }

    public boolean gainUseTextQualifier() {
        return csvReaderConfig.getUseTextQualifier();
    }

}
