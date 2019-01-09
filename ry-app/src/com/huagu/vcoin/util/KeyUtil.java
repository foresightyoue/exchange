package com.huagu.vcoin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyUtil {

    private static final String key = "abc123";
    
    public static String sort(Map<String, Object> map) {
        String value = "";
        String secret = "";
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());

        // 排序方法
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                // return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        // 排序后
        for (Map.Entry<String, Object> m : infoIds) {
            value += String.valueOf(m.getValue());
        }

        // MD5加密
        secret = MD5.get(key + value);
        return secret;
    }
    
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", 12);
        map.put("areaCode", 86);
        map.put("phone", "18565782739");
        map.put("sendVoice", false);
        String sort = sort(map);
        System.out.println(sort);
    }

}
