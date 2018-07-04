package org.wso2.carbon.connector.Medium.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.carbon.connector.core.ConnectException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.wso2.carbon.connector.Medium.util.Constants.PARAM_VALUE;

/**
 * Util class for Medium Connector.
 */
public class MediumConnectorUtil {

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    public static void replaceTemplateParameters(String templateValue, String paramListObjString) {
        JSONArray paramArr = new JSONArray(paramListObjString);
        Map<String, String> parameterMap = new HashMap<>();
        for (int i = 0; i < paramArr.length(); i++) {
            JSONObject paramJSONObject = paramArr.getJSONObject(i);
            String key = Constants.TEMPLATE_PARAM_OPEN_BRACE
                            + paramJSONObject.getString(Constants.PARAM_KEY)
                            + Constants.TEMPLATE_PARAM_CLOSE_BRACE;
            parameterMap.put(key, paramJSONObject.getString(PARAM_VALUE));
        }

        for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet()) {
            templateValue.replaceAll(parameterEntry.getKey(), parameterEntry.getValue());
        }
    }

    public static String getTemplateToString(String selectedTemplateFilePath) throws
        ConnectException {
        File selectedTemplate = new File(selectedTemplateFilePath);
        String templateValue;

        if (selectedTemplate.exists()) {
            try {
                templateValue = MediumConnectorUtil.readFile(selectedTemplateFilePath,
                                                             Charset.defaultCharset());
            } catch (IOException e) {
                throw new ConnectException("Error occurred while reading the template.");
            }
        } else {
            throw new ConnectException("Template for post type specified does not exist.");
        }
        return templateValue;
    }
}
