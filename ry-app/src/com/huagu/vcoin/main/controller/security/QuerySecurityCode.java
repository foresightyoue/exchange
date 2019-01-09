package com.huagu.vcoin.main.controller.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.service.front.FrontUserService;

import cn.cerc.jbean.tools.CURL;
import cn.cerc.jdb.core.DataSet;
import cn.cerc.jdb.other.utils;
import cn.cerc.security.sapi.JayunAPI;
import net.sf.json.JSONObject;

@Controller
public class QuerySecurityCode extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FrontUserService frontUserService;

    @RequestMapping("/ssadmin/querySecurityCode")
    public ModelAndView querySecurityCode() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/securityCodeList");
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
        int currentPage = 1;
        String mobile = request.getParameter("mobile");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        int totalCount = 0;
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile == null ? "" : mobile);
        String result = CURL.doPost(JayunAPI.getHost() + "/api/message.searchMsgLog", params, "UTF-8");
        JSONObject json = JSONObject.fromObject(result);
        DataSet dataOut = new DataSet();
        if (json.has("result")) {
            if (json.has("data")) {
                dataOut.setJSON(json.getString("data"));
            }
            totalCount = dataOut.getRecords().size();
        } else {
            modelAndView.addObject("error", "查询失败");
        }
        if (totalCount == 0) {
            modelAndView.addObject("error", "未查询到手机号相应数据");
        }

        while (dataOut.fetch()) {
            if (dataOut.getString("mobile_").length() >= 7) {
                dataOut.setField("mobile", utils.confused(dataOut.getString("mobile_"), 3, 4));
            }
            if ("通知短信".equals(dataOut.getString("type_"))) {
                dataOut.setField("verifyCode_", "无");
            }
        }

        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("smsLogList", dataOut.getRecords());
        modelAndView.addObject("numPerPage", 1000);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalCount", totalCount);

        return modelAndView;
    }
}
