package com.wind.verification.run.hdfs.service.impl;

import com.wind.base.exception.NoloseException;
import com.wind.doamin.ResultCode;
import com.wind.entity.hdfs.HdfsHandler;
import com.wind.utils.database.DatabaseUtil;
import net.sf.json.JSONObject;

public abstract class HdfsServiceAbs {
    public JSONObject hdfsProcessor(HdfsHandler hdfsHandler, boolean isReader) {

        boolean pathProvided = DatabaseUtil.isStringExist(hdfsHandler.getPath());
        boolean defaultFSProvided = DatabaseUtil.isStringExist(hdfsHandler.getDefaultFS());
        boolean fileTypeProvided = DatabaseUtil.isStringExist(hdfsHandler.getFileType());
        boolean compressProvided = DatabaseUtil.isStringExist(hdfsHandler.getCompress());

        if (!pathProvided) {
            throw new NoloseException(ResultCode.ERROR, "path参数缺失");
        } else if (!defaultFSProvided) {
            throw new NoloseException(ResultCode.ERROR, "defaultFS参数缺失");
        } else if (!fileTypeProvided) {
            throw new NoloseException(ResultCode.ERROR, "fileType参数缺失");
        } else {
            JSONObject result = new JSONObject();
            JSONObject handler = new JSONObject();
            JSONObject parameter = new JSONObject();

            if (compressProvided) {
                parameter.put("compress", hdfsHandler.getCompress());
            }

            parameter.put("path", hdfsHandler.getPath());
            parameter.put("defualtFS", hdfsHandler.getDefaultFS());
            parameter.put("fileType", hdfsHandler.getFileType());
            parameter.put("encoding", "UTF-8");

            handler.put("name", isReader ? "hdfsreader": "hdfswriter");
            handler.put("parameter", parameter);
            result.put(isReader ? "reader":"writer", handler);

            return result;
        }
    }
}
