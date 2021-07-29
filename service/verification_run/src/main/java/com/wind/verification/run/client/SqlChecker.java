package com.wind.verification.run.client;

import net.sf.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@FeignClient(name = "database")
public interface SqlChecker {
    @PostMapping(value = "/connection/db/sqlCheck", consumes = "application/json")
    JSONObject checkSQL(Map<String, Object> jsonObject);
}
