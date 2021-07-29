package com.wind.verification.run.mysql.controller;

import com.wind.doamin.R;
import com.wind.entity.mysql.MySQLReader;
import com.wind.entity.mysql.MySQLWriter;
import com.wind.verification.run.mysql.service.MySQLService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping
public class MysqlController {

    @Autowired
    MySQLService service;


    @PostMapping("/readSQL")
    public R readSQL(@RequestBody MySQLReader mySQLReader) {
        JSONObject result = service.readSQL(mySQLReader);
        return (result == null) ? R.error().data("error", "参数缺失") : R.ok().data("json", result);
    }
    @PostMapping("/writeSQL")
    public R writeSQL(@RequestBody MySQLWriter mySQLWriter) {
        JSONObject result = service.writeSQL(mySQLWriter);
        return (result == null) ? R.error().data("error", "参数缺失") : R.ok().data("json", result);
    }


}
