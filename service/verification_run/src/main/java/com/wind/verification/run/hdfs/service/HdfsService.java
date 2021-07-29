package com.wind.verification.run.hdfs.service;

import com.wind.entity.hdfs.HdfsReader;
import com.wind.entity.hdfs.HdfsWriter;
import net.sf.json.JSONObject;

public interface HdfsService {
    JSONObject readHdfs(HdfsReader hdfsReader);

    JSONObject writeHdfs(HdfsWriter hdfsWriter);
}
