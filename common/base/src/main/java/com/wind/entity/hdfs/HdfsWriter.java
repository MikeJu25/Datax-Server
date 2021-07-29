package com.wind.entity.hdfs;

import lombok.Data;

import java.util.ArrayList;

@Data
public class HdfsWriter extends HdfsHandler {
    protected String fileName, writeMode, compress, fieldDelimiter;
    protected ArrayList<WriterBlock> column;
}
