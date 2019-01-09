package com.huagu.vcoin.main.controller.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.sms.ShortMessageService;

import cn.cerc.security.sapi.JayunSecurity;
import cn.cerc.security.sapi.SendMode;
import net.sf.json.JSONObject;

@Controller
public class SecurityEnvironment extends BaseController {
    /**
     * 用于Form中，向UI(jsp)传递当前是否安全，若不安全则显示输入验证码画面
     * 
     * @param jspPage
     */
    public static boolean check(JspPage jspPage) {
        Fuser fuser = new BaseController().GetCurrentUser(jspPage.getRequest());
        if (fuser == null) {
            jspPage.addObject("securityEnvironment", true);
            return true;
        }
        String uri = jspPage.getRequest().getRequestURI();
        jspPage.getRequest().getSession().setAttribute("deviceId", "webclient");
        JayunSecurity api = new JayunSecurity(jspPage.getRequest());
        if (api.checkEnvironment(fuser.getFloginName())) {
            jspPage.addObject("message", api.getMessage());
            jspPage.addObject("securityEnvironment", true);
            String code = jspPage.getRequest().getParameter("securityCode");
            if (uri.indexOf("ssadmin") > -1 && code != null && !"".equals(code)) {
                jspPage.setViewName("ssadmin/index");
            }
            return true;
        } else {
            String code = jspPage.getRequest().getParameter("securityCode");
            if ("".equals(code))
                jspPage.addObject("message", "验证码不能为空,请输入验证码");
            else
                jspPage.addObject("message", api.getMessage());
            jspPage.setViewName("security/SecurityVerify");
            jspPage.addObject("action", uri);
            return false;
        }
    }

    @ResponseBody
    @RequestMapping(value = { "/user/sendSMS", "/ssadmin/sendSMS" }, produces = BaseController.JsonEncode)
    public String sendSMS(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        getSession(request).setAttribute("deviceId", "webclient");
        boolean sendVoice = Boolean.valueOf(request.getParameter("sendVoice"));
        Fuser fuser = new BaseController().GetCurrentUser(request);
        if (ShortMessageService.isTrue()) {
            String code = ShortMessageService.getCode();
            ShortMessageService.sendMsg(fuser.getFtelephone(), "【瑞银钱包】您的验证码是" + code + "，如非本人操作，请忽略本短信", code);
        }
        else{
            JayunSecurity api = new JayunSecurity(request);
            if (sendVoice) {
                api.setSendMode(SendMode.VOICE);
            }
            if (api.requestVerify(fuser.getFloginName())) {
                map.put("result", true);
                map.put("message", api.getMessage());
            } else {
                map.put("result", false);
                map.put("message", api.getMessage());
            }
        }
        return JSONObject.fromObject(map).toString();
    }
}
