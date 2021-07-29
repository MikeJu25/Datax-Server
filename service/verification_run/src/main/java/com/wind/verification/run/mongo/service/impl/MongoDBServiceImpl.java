package com.wind.verification.run.mongo.service.impl;

import com.wind.entity.mongodb.MongoDBReader;
import com.wind.entity.mongodb.MongoDBWriter;
import com.wind.entity.mongodb.UpsertInfo;
import com.wind.utils.database.DatabaseUtil;
import com.wind.verification.run.mongo.service.MongoDBService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MongoDBServiceImpl extends MongoDBServiceAbs implements MongoDBService {
    @Override
    public JSONObject readMongo(MongoDBReader mongoDBReader) {
        boolean queryProvided = DatabaseUtil.isStringExist(mongoDBReader.getQuery());

        JSONObject result = mongoProcessor(mongoDBReader, true);
        JSONObject reader = result.getJSONObject("reader");
        JSONObject parameter = reader.getJSONObject("parameter");

        if (queryProvided) {
            parameter.put("query", mongoDBReader.getQuery());
        }

        return result;
    }

    @Override
    public JSONObject writeMongo(MongoDBWriter mongoDBWriter) {
        JSONObject upsertInfoJson = new JSONObject();

        JSONObject result = mongoProcessor(mongoDBWriter, false);
        JSONObject writer = result.getJSONObject("writer");
        JSONObject parameter = writer.getJSONObject("parameter");

        if (mongoDBWriter.getUpsertInfo() != null) {
            UpsertInfo upsertInfo = mongoDBWriter.getUpsertInfo();
            if (upsertInfo.getIsUpsert().equals("true")) {
                if (upsertInfo.getIsUpsert() != null) {
                    upsertInfoJson.put("isUpsert", upsertInfo.getIsUpsert());
                }
                if (upsertInfo.getUpsertKey() != null) {
                    upsertInfoJson.put("upsertKey", upsertInfo.getUpsertKey());
                }
                parameter.put("upsertInfo", upsertInfoJson);
            }
        }

        return result;
    }

}
