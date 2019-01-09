package com.huagu.vcoin.main.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.vcoin.main.controller.BaseController;

import net.sf.json.JSONObject;

/**
 * 
 * at 回转接口
 *
 */
@Controller
public class RotationController extends BaseController {

    @ResponseBody
    @RequestMapping(value = { "coin/rotation" }, produces = JsonEncode)
    public String resetPhoneValidation(HttpServletRequest request, @RequestParam(required = true) String id,
            @RequestParam(required = true) String name, @RequestParam(required = true) double amount,
            @RequestParam(required = false) String sign) {
        JSONObject result = new JSONObject();
        result.put("status", true);
        result.put("msg", "测试");

        return result.toString();
    }
}
