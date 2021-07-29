package com.wind.datax.run.service.impl;

import com.wind.base.exception.NoloseException;
import com.wind.datax.run.domain.Other;
import com.wind.doamin.ResultCode;
import com.wind.datax.run.domain.Setting;
import com.wind.datax.run.service.DataxService;
import com.wind.datax.run.util.DataxUtil;
import com.wind.utils.json.JsonUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class DataxServiceImpl implements DataxService {
    @Value("${Datax.path}")
    private String filePath;
    @Value("${Datax.name}")
    private String fileName;
    @Value("${Datax.home}")
    private String home;


    @Autowired
    private Setting settingBean;


    @Override
    public synchronized void toJson(JSONObject jsonObject) {

         jsonObject = editOther(jsonObject);

        if (!JsonUtil.createJsonFile(jsonObject.toString(), filePath, fileName)) throw new NoloseException(ResultCode.ERROR,"Json创建失败");

        try {
            DataxUtil.runJson(home, filePath+"/"+fileName+".json");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new NoloseException(ResultCode.ERROR,throwable.getMessage());
        }
    }

    @Override
    public JSONObject toSetting() {
        JSONObject setting = new JSONObject();
        JSONObject speed = new JSONObject();
        JSONObject errorLimit = new JSONObject();


        speed.put("channel",settingBean.getSpeed_channel());
//        speed.put("byte",settingBean.getSpeed_byte());
//        speed.put("record",settingBean.getSpeed_record());

        errorLimit.put("record",settingBean.getErrorLimit_record());
        errorLimit.put("percentage",settingBean.getErrorLimit_percentage());

        setting.put("speed",speed);
        setting.put("errorLimit",errorLimit);

        return setting;
    }


    private JSONObject editOther(JSONObject jsonObject){
        Other.DATAX_ID =  jsonObject.getString("dataxId");
        Other.DESC = jsonObject.getString("desc");

        //执行任务前删除额外信息
        jsonObject.remove("dataxId");
        jsonObject.remove("desc");
        return jsonObject;
    }
}
