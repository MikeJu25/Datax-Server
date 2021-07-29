package com.wind.datax.run.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wind.entity.log.Log;
import com.wind.datax.run.service.LogService;
import com.wind.doamin.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
@CrossOrigin
public class LogController {

    @Autowired
    LogService logService;

    @PostMapping("/list")
    public R insert(@RequestBody Log log){
        QueryWrapper wrapper = new QueryWrapper<Log>();
        wrapper.eq("log_datax_id",log.getLogDataxId());
        List list = logService.list(wrapper);
        return R.ok().data("list",list);
    }
}
