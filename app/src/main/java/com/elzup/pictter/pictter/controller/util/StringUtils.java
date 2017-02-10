package com.elzup.pictter.pictter.controller.util;

import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtils {

    public static String join(String delimiter, List<String> strs) {
        return Joiner.on(delimiter).join(strs);
    }

    public static String join(String delimiter, String[] strs) {
        return join(delimiter, Arrays.asList(strs));
    }

    public static Map<String, String> getQueryMap(String query) {
        if (query.charAt(0) == '?') {
            query = query.substring(1, query.length());
        }
        Map<String, String> map = new HashMap<>();
        String[] params = query.split("&");
        for (String param : params) {
            String key = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(key, value);
        }
        return map;
    }
}
