package com.wind.database.run.service.impl;

import com.wind.base.exception.NoloseException;
import com.wind.database.run.client.DataxClient;
import com.wind.database.run.client.TaskClient;
import com.wind.database.run.service.DatabasesService;
import com.wind.doamin.ResultCode;
import com.wind.entity.task.Task;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class DatabasesServiceImpl implements DatabasesService {

    @Autowired
    private DataxClient dataxClient;

    @Autowired
    ExecutorService executorService;

    @Autowired
    TaskClient taskClient;

    @Override
    public void runDatabase(JSONObject json)throws NoloseException {

         json = extractJson(json);
        //接收额外参数集合
        Map other = new HashMap();

        try {
            JSONArray jobs =  json.getJSONArray("jobs");

            //获取任务组中的额外参数 向other中添加
            other.put("dataxId",json.getString("dataxId"));
            other.put("desc",json.getString("desc"));

            BlockingQueue queue = new LinkedBlockingDeque();
            jobs.forEach(job->queue.offer(job));

            runJob(queue,other);
        } catch (Exception e) {
            throw new NoloseException(ResultCode.ERROR,"错误，无法存入队列");
        }

    }

    public JSONObject extractJson(JSONObject json){
        String id = json.getString("id");
        String dataxId = json.getString("rwlogid");
        Task task = taskClient.getTask(id);
        JSONObject jobs = JSONObject.fromObject(task.getTaskJob());

        //向获取到的任务组中添加额外参数
        jobs.put("dataxId",dataxId);
        jobs.put("desc",task.getTaskDesc());

        return jobs;
    }

    public void runJob(BlockingQueue queue, Map<String,String> other)  {

        executorService.execute(() ->
            queue.forEach(q->{
                JSONObject job = (JSONObject) q;
                JSONObject jobOR = job.getJSONObject("job");

                //添加setting
                jobOR.put("setting", dataxClient.toSetting().getData().get("setting"));
                job.put("job",jobOR);

                //向每个任务中添加额外参数
                job.put("dataxId",other.get("dataxId"));
                job.put("desc",other.get("desc"));

                try {
                    dataxClient.toJson(job);
                }catch (Exception e){}

            }));

    }


}
