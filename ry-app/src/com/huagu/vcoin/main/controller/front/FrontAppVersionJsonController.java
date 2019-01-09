package com.huagu.vcoin.main.controller.front;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.FappVersion;
import com.huagu.vcoin.main.service.front.FrontOthersService;

import net.sf.json.JSONObject;

/**
 * @Description
 * @author Peng
 * @date 2018年3月20日
 */
@Controller
public class FrontAppVersionJsonController extends BaseController {

    @Autowired
    private FrontOthersService frontOthersService;

    @ResponseBody
    @RequestMapping(value = { "/m/appversion/newversion", "/user/api/newversion",
            "/m/api/newversion" }, produces = JsonEncode)
    public String getNewAppversion(HttpServletRequest request, @RequestParam("type") int type) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<FappVersion> findNewVersion = frontOthersService.findNewVersion(type);
            FappVersion fappVersion = findNewVersion.get(0);
            jsonObject.accumulate("result", true);
            jsonObject.accumulate("message", "");
            jsonObject.accumulate("appVersion", fappVersion.getVersion());
            jsonObject.accumulate("appUpdateReset", fappVersion.isUpdateReset());
            jsonObject.accumulate("appUpdateReadme", fappVersion.getUpdateReadme().split("\r\n"));
            jsonObject.accumulate("startupImage", "");
            jsonObject.accumulate("adImages", new ArrayList<>());
            jsonObject.accumulate("url", fappVersion.getUrl());
            jsonObject.accumulate("name", fappVersion.getName());
        } catch (Exception e) {
            jsonObject.accumulate("result", false);
            jsonObject.accumulate("message", e.getMessage());
        }
        return jsonObject.toString();
    }

}
