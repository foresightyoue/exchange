package com.huagu.vcoin.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("all")
public class IDCard {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    // 配置您申请的KEY
    public static final String APPKEY = "e24c83d4c3a7dac93596e5a5be2f163f";

    public static JSONObject check(String user_uname, String anchor_card) {
        JSONObject result = new JSONObject();
        JSONObject info = JSONObject.parseObject(info(user_uname, anchor_card));
        if ("成功".equals(info.get("reason"))) {
            result.put("code", 0);
            result.put("msg", "认证通过");
            return result;
        } else {
            result.put("code", -1);
            result.put("msg", info.get("reason"));
            return result;
        }
    }

    public static String info(String user_uname, String anchor_card) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("realname", user_uname);
        params.put("idcard", anchor_card);
        params.put("key", APPKEY);
        String s;
        try {
            s = request("http://op.juhe.cn/idcard/query", params, "POST");
            return s;
        } catch (Exception e) {
            log.info(e.getMessage());
            return "认证失败";
        }
    }
    
    public static String request(String strUrl, Map<String, Object> params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            log.info(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                log.info(e.getMessage());
            }
        }
        return sb.toString();
    }
}