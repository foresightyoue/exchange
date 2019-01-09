package com.huagu.vcoin.main.controller.front;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zhongyinghui.security.utils.HttpClientUtils;
import com.zhongyinghui.security.utils.SecurityUtils;

public class test {
    public static void main(String[] args) throws IOException {

        Map<String, Object> param = new LinkedHashMap<>();
        param.put("targetUserId", "RY881007973");
        param.put("srcUserId", "RY881007971");
        param.put("coins", "1");
        param.put("coinType", "RYH");

        String securityKey = SecurityUtils.getSecurityKey(param, "123");
        String url = "http://47.106.227.118:8080/api/app/transferAccount";
        JSONObject json = new JSONObject(param);
        json.put("securityKey", securityKey);
        String s = HttpClientUtils.doPost(url, json);
        System.out.println(s);

    }
}
