package com.wind.entity.mysql;

import lombok.Data;
import java.util.ArrayList;

@Data
public class MySQLReader extends MySQLHandler {
    protected ArrayList<String> where;
    protected String splitPk, dbId, dbName;
}
