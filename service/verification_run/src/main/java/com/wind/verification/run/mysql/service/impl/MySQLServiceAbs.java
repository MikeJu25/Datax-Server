package com.wind.verification.run.mysql.service.impl;

import com.wind.base.exception.NoloseException;
import com.wind.doamin.ResultCode;
import com.wind.entity.mysql.MySQLHandler;
import com.wind.entity.mysql.Connection;
import com.wind.utils.database.DatabaseUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public abstract class MySQLServiceAbs {
    JSONObject parameter = new JSONObject();

    public JSONObject sqlProcessor(MySQLHandler mySQLHandler, boolean hasQuerySQL) {
        List<Connection> connections = mySQLHandler.getConnection();
        int columnSize = 0;
        Connection connection = new Connection();

        boolean passwordProvided = DatabaseUtil.stringChecker(mySQLHandler.getPassword());
        boolean nameProvided = DatabaseUtil.stringChecker(mySQLHandler.getUsername());
        boolean columnProvided = DatabaseUtil.listChecker(mySQLHandler.getColumn());
        boolean tableAndURLProvided = connection.connectionChecker(mySQLHandler.getConnection());
        boolean querySQLProvided = true;
        if (hasQuerySQL) {
            querySQLProvided = connection.hasQuerySQL(connections.get(0));
        }
        if (columnProvided) {
            columnSize = mySQLHandler.getColumn().size();
        }

        JSONObject result = new JSONObject();
        JSONObject handler = new JSONObject();

        if (!passwordProvided) {
            throw new NoloseException(ResultCode.ERROR, "password参数缺失");
        } else if (!nameProvided) {
            throw new NoloseException(ResultCode.ERROR, "userName参数缺失");
        } else if (!columnProvided && !querySQLProvided) {
            throw new NoloseException(ResultCode.ERROR, "column参数缺失");
        } else if (hasQuerySQL) {
            if (querySQLProvided) {
                List<JSONObject> connectionsJson = new ArrayList<>();
                for (Connection c : connections) {
                    JSONObject collectionJson = new JSONObject();
                    collectionJson.put("querySql",c.getQuerySql());
                    collectionJson.put("jdbcUrl", c.getJdbcUrl());
                    connectionsJson.add(collectionJson);
                    result.put("tempSql", c.getQuerySql());
                }
                JSONObject filledParameter = fillInBackbone(mySQLHandler, connectionsJson);
                handler.put("name", "mysqlreader");
                handler.put("parameter", filledParameter);
                result.put("reader", handler);

                return result;
            } else if (tableAndURLProvided) {
                List<JSONObject> connectionsJson = new ArrayList<>();

                for (Connection c : connections) {
                    JSONObject collectionJson = new JSONObject();
                    collectionJson.put("table", c.getTable());
                    collectionJson.put("jdbcUrl", c.getJdbcUrl());
//                if (hasQuerySQL && c.getQuerySql().size() != 0) {
//                    collectionJson.put("querySql", c.getQuerySql());
//                }
                    connectionsJson.add(collectionJson);
                }

                JSONObject filledParameter = fillInBackbone(mySQLHandler, connectionsJson);
                parameter.put("column", mySQLHandler.getColumn());

                handler.put("name", "mysqlreader");
                handler.put("parameter", filledParameter);
                result.put("reader", handler);
                result.put("size",columnSize);

                return result;
            } else {
                throw new NoloseException(ResultCode.ERROR, "table或jdbcUrl参数缺失");
            }
        } else {
            List<JSONObject> connectionsJson = new ArrayList<>();

            for (Connection c : connections) {
                JSONObject collectionJson = new JSONObject();
                collectionJson.put("table", c.getTable());
                collectionJson.put("jdbcUrl", c.getJdbcUrl().get(0));
                connectionsJson.add(collectionJson);
            }

            JSONObject filledParameter = fillInBackbone(mySQLHandler, connectionsJson);
            parameter.put("column", mySQLHandler.getColumn());

            handler.put("name", "mysqlwriter");
            handler.put("parameter", filledParameter);
            result.put("writer", handler);

            return result;
        }
    }

    private JSONObject fillInBackbone(MySQLHandler mySQLHandler, List<JSONObject> connectionsJson) {
        parameter.put("username", mySQLHandler.getUsername());
        parameter.put("password", mySQLHandler.getPassword());
        parameter.put("connection", connectionsJson);
        return parameter;
    }
}
