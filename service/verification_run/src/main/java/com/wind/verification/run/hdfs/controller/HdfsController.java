package com.wind.verification.run.hdfs.controller;

import com.wind.doamin.R;
import com.wind.entity.hdfs.HdfsReader;
import com.wind.entity.hdfs.HdfsWriter;
import com.wind.entity.mongodb.MongoDBReader;
import com.wind.entity.mongodb.MongoDBWriter;
import com.wind.verification.run.hdfs.service.HdfsService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HdfsController {

    @Autowired
    HdfsService hdfsService;

    @PostMapping("/readHdfs")
    public R readHdfs(@RequestBody HdfsReader hdfsReader) {
        JSONObject result = hdfsService.readHdfs(hdfsReader);
        return (result == null) ? R.error().data("error", "参数缺失") : R.ok().data("json", result);
    }

    @PostMapping("/writeHdfs")
    public R writeHdfs(@RequestBody HdfsWriter hdfsWriter) {
        JSONObject result = hdfsService.writeHdfs(hdfsWriter);
        return (result == null) ? R.error().data("error", "参数缺失") : R.ok().data("json", result);
    }
}
