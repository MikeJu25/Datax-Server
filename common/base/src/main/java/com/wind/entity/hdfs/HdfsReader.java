package com.wind.entity.hdfs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HdfsReader extends HdfsHandler {
    protected List<ReaderBlock> column;
    protected String fileDelimiter, nullFormat;
    protected CsvReaderConfig csvReaderConfigs;
}
