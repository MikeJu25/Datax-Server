package com.wind.database.run.service;

import com.wind.base.exception.NoloseException;
import net.sf.json.JSONObject;

import java.util.concurrent.BlockingQueue;

public interface DatabasesService {

     void runDatabase(JSONObject json)throws NoloseException;
}
