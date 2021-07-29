package com.wind.database.run.controller;

import com.wind.base.exception.NoloseException;
import com.wind.database.run.client.TaskClient;
import com.wind.database.run.service.DatabasesService;
import com.wind.doamin.R;
import com.wind.entity.task.Task;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/database")
@CrossOrigin
public class DatabaseController {
    @Autowired
    DatabasesService databasesService;

    @Autowired
    TaskClient taskClient;


    @PostMapping("/carried")
    public R carried(@RequestBody JSONObject json){
        try {
            databasesService.runDatabase(json);
        }catch (NoloseException e){
            return R.error().data("msg",e.getMsg());
        }
        return R.ok().data("msg","已存入请求队列").data("type","datax").data("rwlogid",json.getString("rwlogid"));
    }

    //Todo 要删除
    /*@PostMapping("/runDatabase")
    public R runDatabase(@RequestBody JSONObject jsonObject){
        JSONArray jobs =  jsonObject.getJSONArray("jobs");
        String dataxId = jsonObject.getString("dataxId");
        String desc = jsonObject.getString("desc");
        BlockingQueue queue = new LinkedBlockingDeque();

        jobs.forEach(job->queue.offer(job));

        try {
            databasesService.runJob(queue,dataxId,desc);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return R.ok().data("msg","已存入请求队列").data("rwlogid",dataxId).data("type","datax");
    }*/



}
