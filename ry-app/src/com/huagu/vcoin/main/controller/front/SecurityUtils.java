package com.huagu.vcoin.main.controller.front;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 系统接口加密方式
 *
 * @author sunyalong
 * Created by 94331 on 2018/8/16.
 */
public class SecurityUtils {

    /**
     * 判断此次请求是否为正确的请求,也就是拿到加密的key自动判断
     *
     * @param request
     * @param SourceSecurityKey
     * @return
     */
    public static boolean isCorrectRequest(HttpServletRequest request,HttpServletResponse response,String SourceSecurityKey){
        // 获得所有的请求参数
        Map parameterMap = getParamJson(request,response);
        // 获得请求的 securityKey
        String requestSecurityKey = (String) parameterMap.get("securityKey");
        if (requestSecurityKey == null) {
            return false;
        }
        parameterMap.remove("securityKey");
        String key = getSecurityKey(parameterMap, SourceSecurityKey);
        return key.equals(requestSecurityKey) ? true : false;
    }

    /**
     * 获得加密后的key
     *
     * @param request
     * @param SourceSecurityKey
     * @return
     */
    public static String getSecurityKey(HttpServletRequest request,HttpServletResponse response,String SourceSecurityKey){
        Map parameterMap = getParamJson(request,response);
        String key = getSecurityKey(parameterMap, SourceSecurityKey);
        return key;
    }


    /**
     * 获得加密后的kay
     *
     * @param param
     * @param securityKey
     * @return
     */
    public static String getSecurityKey(Map<String,Object> param,String securityKey){
        String[] result = getParams(param);
        result = sort(result);
        StringBuilder sb = new StringBuilder();
        for (String s : result) {
            sb.append(s);
        }
        sb.append(securityKey);
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }

    /**
     * 将一个 map转化为 数组
     *
     * @param param
     * @return
     */
    public static String[] getParams(Map<String,Object> param){
        String[] r = new String[param.size()];
        int index = 0;
        for (String s : param.keySet()) {
            r[index] = String.valueOf(param.get(s));
            index++;
        }
        return r;
    }


    /**
     * 对字符串数组进行排序
     *
     * @param keys
     * @return
     * */
    private  static String[] sort(String[] keys){
        for (int i = 0; i < keys.length - 1; i++) {
            for (int j = 0; j < keys.length - i -1; j++) {
                String pre = keys[j];
                String next = keys[j + 1];
                if(isMoreThan(pre, next)){
                    String temp = pre;
                    keys[j] = next;
                    keys[j+1] = temp;
                }
            }
        }
        return keys;
    }

    /**
     * 比较两个字符串的大小，按字母的ASCII码比较
     * @param pre
     * @param next
     * @return
     * */
    private static boolean isMoreThan(String pre, String next){
        if(null == pre || null == next || "".equals(pre) || "".equals(next)){
            return false;
        }

        char[] c_pre = pre.toCharArray();
        char[] c_next = next.toCharArray();

        int minSize = Math.min(c_pre.length, c_next.length);

        for (int i = 0; i < minSize; i++) {
            if((int)c_pre[i] > (int)c_next[i]){
                return true;
            }else if((int)c_pre[i] < (int)c_next[i]){
                return false;
            }
        }
        if(c_pre.length > c_next.length){
            return true;
        }

        return false;
    }

    /**
     * 获得请求的json字符串
     *
     * @param request
     * @param response
     * @return
     */
    public static Map<String,String> getParamJson(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        String json;
        if (contentType != null && contentType.equals("application/json")) {
            json = readJson(request, response);
        } else {
            Map<String, String[]> parameterMap = request.getParameterMap();
            json = JSONObject.toJSONString(parameterMap).replace("[", "").replace("]", "");
        }
        Map<String,String> parse = JSON.parseObject(json,Map.class);
        return parse;
    }

    /**
     * 读取application/json请求格式个json字符串
     *
     * @param request
     * @param response
     * @return
     */
    public static String readJson(HttpServletRequest request, HttpServletResponse response){
        String param = null;
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            param = jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

    public static String getRequestJson(HttpServletRequest request) {
        boolean isGetRequest = request.getMethod().toLowerCase().equals("get");
        try {
            if (isGetRequest) {
                return new String(request.getQueryString().getBytes(
                        "ISO-8859-1"), "UTF-8").replaceAll("%22", "\"");
            }
            int contentLength = request.getContentLength();
            if (contentLength < 0) {
                return null;
            }
            byte buffer[] = new byte[contentLength];
            for (int i = 0; i < contentLength;) {

                int readlen = request.getInputStream().read(buffer, i,
                        contentLength - i);
                if (readlen == -1) {
                    break;
                }
                i += readlen;
            }
            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
