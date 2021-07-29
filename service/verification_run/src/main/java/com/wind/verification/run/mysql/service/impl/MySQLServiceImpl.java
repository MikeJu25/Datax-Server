package com.wind.verification.run.mysql.service.impl;

import com.wind.base.exception.NoloseException;
import com.wind.doamin.ResultCode;
import com.wind.entity.mysql.MySQLReader;
import com.wind.entity.mysql.MySQLWriter;
import com.wind.utils.database.DatabaseUtil;
import com.wind.verification.run.client.SqlChecker;
import com.wind.verification.run.mysql.service.MySQLService;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MySQLServiceImpl extends MySQLServiceAbs implements MySQLService {
    @Autowired
    SqlChecker sqlChecker;

    @Override
    public JSONObject readSQL(MySQLReader mySQLReader) {

        boolean splitPKProvided = DatabaseUtil.isStringExist(mySQLReader.getSplitPk());
        boolean whereProvided = DatabaseUtil.listNotEmpty(mySQLReader.getWhere());

        JSONObject result = sqlProcessor(mySQLReader, true);
        JSONObject reader = result.getJSONObject("reader");

        try {
            JSONArray tempSql = result.getJSONArray("tempSql");
            Object o = tempSql.get(0);
            JSONObject parameter = reader.getJSONObject("parameter");

            Map<String,Object> feedSqlChecker = new HashMap<>();
            feedSqlChecker.put("connectionId", mySQLReader.getDbId());
            feedSqlChecker.put("databaseName",mySQLReader.getDbName());
            feedSqlChecker.put("sql",o);

            JSONObject sqlCheckerResult = sqlChecker.checkSQL(feedSqlChecker);
            boolean isSucceeded = sqlCheckerResult.getBoolean("success");
            if (isSucceeded) {
                JSONObject data = sqlCheckerResult.getJSONObject("data");
                String size = data.getString("size");
                int columnSize = Integer.parseInt(size);
                result.put("size", columnSize);
            } else {
                String message = sqlCheckerResult.getString("message");
                throw new NoloseException(ResultCode.ERROR, message);
            }
        } catch (HttpMessageNotReadableException e) {
            System.out.println("ignore this message ^o^");
        } catch (JSONException e) {
            System.out.println("ignore this message ^o^");
        }


        result.remove("tempSql");

        if (splitPKProvided) {
            parameter.put("splitPk", mySQLReader.getSplitPk());
        }
        if (whereProvided) {
            parameter.put("where", mySQLReader.getWhere());
        }
        return result;
    }

    @Override
    public JSONObject writeSQL(MySQLWriter mySQLWriter) {

        int batchSize = 1024;
        String mode = "insert";
        int inputBatchSize = mySQLWriter.getBatchSize();
        boolean modeProvided = DatabaseUtil.stringChecker(mySQLWriter.getWriteMode());
        boolean modeTypeValid = false;
        if (modeProvided) {
            modeTypeValid = DatabaseUtil.modeCheckerSQL(mySQLWriter.getWriteMode());
        }
//        boolean preSqlProvided = DatabaseUtil.listNotEmpty(mySQLWriter.getPreSql());
//        boolean postSqlProvided = DatabaseUtil.listNotEmpty(mySQLWriter.getPostSql());
//        boolean sessionProvided = DatabaseUtil.listNotEmpty(mySQLWriter.getSession());

        if (DatabaseUtil.integerChecker(inputBatchSize)) {
            if (!DatabaseUtil.lessThanMax(inputBatchSize, 4096)) {
                throw new NoloseException(ResultCode.ERROR, "batchSize应小于等于4096");

            } else if (!DatabaseUtil.multipleOfTwo(inputBatchSize)) {
                throw new NoloseException(ResultCode.ERROR, "batchSize应为2的次方");
            } else {
                batchSize = inputBatchSize;
            }
        }
        if (modeProvided) {
            if (modeTypeValid) {
                mode = mySQLWriter.getWriteMode();
            } else {
                throw new NoloseException(ResultCode.ERROR, "writeMode参数错误");
            }
        }

        JSONObject result = sqlProcessor(mySQLWriter, false);
        JSONObject writer = result.getJSONObject("writer");
        JSONObject parameter = writer.getJSONObject("parameter");
        parameter.put("writeMode", mode);
        parameter.put("batchSize", batchSize);

//        if (sessionProvided) {
//            parameter.put("session", mySQLWriter.getSession());
//        }
//        if (preSqlProvided) {
//            parameter.put("preSql", mySQLWriter.getPreSql());
//        }
//        if (postSqlProvided) {
//            parameter.put("postSql", mySQLWriter.getPostSql());
//        }
        return result;
    }
}
