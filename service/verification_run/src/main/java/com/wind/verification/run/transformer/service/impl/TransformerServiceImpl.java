package com.wind.verification.run.transformer.service.impl;
import com.wind.base.exception.NoloseException;
import com.wind.doamin.ResultCode;
import com.wind.verification.run.transformer.service.TransformerService;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransformerServiceImpl implements TransformerService {
    public JSONObject handleTransformer(List<JSONObject> jsonObjects) {
        JSONObject transformer = new JSONObject();
        List<JSONObject> result = new ArrayList<>();

        if (jsonObjects.size() == 0) {
            throw new NoloseException(ResultCode.ERROR, "未找到有效transformer");
        }

        for (JSONObject jsonObject: jsonObjects) {
            JSONObject udfObject = new JSONObject();
            JSONObject parameter = new JSONObject();

            if (compareName(jsonObject,"Substr") || compareName(jsonObject,"Filter")) {
                if (compareName(jsonObject,"Substr")) {
                    udfObject.put("name", "dx_substr");
                }
                if (compareName(jsonObject,"Filter")) {
                    udfObject.put("name", "dx_filter");
                }

                indexTypeChecker(jsonObject.get("columnIndex"));
                int index = (int) jsonObject.get("columnIndex");
                indexChecker(index);
                parameter.put("columnIndex", index);

                List<Object> paras = (List<Object>) jsonObject.get("paras");
                checkParas(paras, 2);

                parameter.put("paras", paras);
                udfObject.put("parameter", parameter);

                result.add(udfObject);
            } else if (compareName(jsonObject,"Pad") || compareName(jsonObject,"Replace")) {
                if (compareName(jsonObject,"Pad")) {
                    udfObject.put("name", "dx_pad");
                }
                if (compareName(jsonObject,"Replace")) {
                    udfObject.put("name", "dx_replace");
                }

                indexTypeChecker(jsonObject.get("columnIndex"));
                int index = (int) jsonObject.get("columnIndex");
                indexChecker(index);
                parameter.put("columnIndex", index);

                List<Object> paras = (List<Object>) jsonObject.get("paras");
                checkParas(paras, 3);

                parameter.put("paras", paras);
                udfObject.put("parameter", parameter);

                result.add(udfObject);
            } else if (compareName(jsonObject,"Groovy")) {
                codeChecker(jsonObject.get("code"));
                JSONArray strings;
                try {
                    strings = jsonObject.getJSONArray("extraPackage");
                } catch (JSONException e) {
                    throw new NoloseException(ResultCode.ERROR, "extraPackage格式错误,应当为一个数组");
                }

                listChecker(strings);

                parameter.put("code", jsonObject.get("code"));
                parameter.put("extraPackage", strings);
                udfObject.put("parameter", parameter);
            } else {
                throw new NoloseException(ResultCode.ERROR, "udf名称不支持");
            }
        }

        transformer.put("transformer", result);
        // transformer.put("transformer", jsonObjects);

        return transformer;
    }

    private boolean compareName(JSONObject jsonObject, String name) {
        return jsonObject.get("name").equals(name);
    }

    private void codeChecker(Object code) {
        if (!(code instanceof String) || ((String) code).isEmpty()) {
            throw new NoloseException(ResultCode.ERROR, "自定义的code必须为非空字符串，即代码片段，具体示例请参照文档");
        }
    }

    private void listChecker(JSONArray extraPackage) {
        if (extraPackage.isEmpty()) {
            throw new NoloseException(ResultCode.ERROR, "extraPackage应当为非空数组，以import开头");
        }
    }

    private void indexTypeChecker(Object columnIndex) {
        if (!(columnIndex instanceof Integer)) {
            throw new NoloseException(ResultCode.ERROR, "columnIndex应为整数类型");
        }
    }

    private void checkParas(List<Object> paras, int numOfArgs) {
        if (paras.size() > numOfArgs) {
            throw new NoloseException(ResultCode.ERROR, "paras中参数数量不得超过" + numOfArgs + "个");
        }
        if (paras.size() <= numOfArgs - 1) {
            throw new NoloseException(ResultCode.ERROR, "缺少参数，paras中第一二个参数必填");
        }
        if (!(paras.get(0) instanceof String)) {
            throw new NoloseException(ResultCode.ERROR, "paras中第一个参数需为字符串类型");
        }
        for (int i = 1; i < paras.size(); i++) {
            if (!(paras.get(i) instanceof String)) {
                throw new NoloseException(ResultCode.ERROR, "paras中除第一个参数外其他参数需为字符串类型");
            }
        }
    }

    private void indexChecker(int index) {
        if (index < 1) {
            throw new NoloseException(ResultCode.ERROR, "columnIndex应大于等于1");
        }
    }

}
