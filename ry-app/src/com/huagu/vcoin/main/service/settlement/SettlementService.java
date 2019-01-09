package com.huagu.vcoin.main.service.settlement;

import com.alibaba.fastjson.JSON;
import com.huagu.vcoin.main.controller.front.FrontUserJsonController;
import com.huagu.vcoin.util.Constant;
import com.zhongyinghui.security.utils.HttpClientUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettlementService {

    public com.alibaba.fastjson.JSONObject updateUser(String tid,String mobile,String recommendId) {
        try {
            com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
            json.put("tid",tid);
            if(StringUtils.isNotEmpty(mobile)) {
                json.put("mobile",mobile);
            }
            if(StringUtils.isNotEmpty(recommendId)) {
                json.put("recommendId",recommendId);
            }
            String url = Constant.SettlementIP + "/api/settlement/setUser";
            String resultStr = HttpClientUtils.doPost(url, json);
            com.alibaba.fastjson.JSONObject resJson = JSON.parseObject(resultStr);
//            if(resJson != null && StringUtils.isNotEmpty((String)resJson.get("status")) && resJson.get("status").equals("200")) {
//                return true;
//            }else{
//                return false;
//            }
            return resJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String tid,String recommendId,String mobile) {
        try {
            String code = "";
            String url = Constant.SettlementIP + "/api/settlement/synUserNexus";
            Map<String, Object> params = new HashMap<>();
            params.put("tid", tid);
            params.put("recommendId", recommendId);
            params.put("mobile", mobile);
            String securityKey = com.huagu.vcoin.main.controller.front.SecurityUtils.getSecurityKey(params,
                    FrontUserJsonController.securtyKey);
            params.put("securityKey", securityKey);
            com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject(params);
            String resultStr = HttpClientUtils.doPost(url, json);
            System.out.println("返回结果是：" + resultStr);
            if (FrontUserJsonController.notEmpty(resultStr)) {
                JSONObject resultJson = JSONObject.fromObject(resultStr);
                code = resultJson.getString("status");
            }
            if (code.equals("200")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean login(String tid,String recommendId,String mobile) {
        String code = "";
        String msg = "";
        String url = Constant.userRegister1;
        Map<String, Object> params = new HashMap<>();
        params.put("tid", tid);
        params.put("recommendId", recommendId);
        params.put("mobile", mobile);
        String securityKey = com.huagu.vcoin.main.controller.front.SecurityUtils.getSecurityKey(params,
                FrontUserJsonController.securtyKey);
        params.put("securityKey", securityKey);

        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject(params);
        try {
            String resultStr = HttpClientUtils.doPost(url, json);
            System.out.println("返回结果是：" + resultStr);
            if (FrontUserJsonController.notEmpty(resultStr)) {
                JSONObject resultJson = JSONObject.fromObject(resultStr);
                code = resultJson.getString("status");
                msg = resultJson.getString("msg");
            }
            if (code.equals("200")) {
                return true;
            } else {
                if(msg.contains("用户已经存在") || msg.contains("账户已经存在") || msg.contains("注册电话号码已经存在")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
