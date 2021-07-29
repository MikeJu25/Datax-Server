package com.wind.entity.hdfs;

import lombok.Data;

@Data
public abstract class HdfsHandler {
    protected String path, defaultFS, fileType, compress;
}
