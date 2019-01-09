package com.huagu.vcoin.main.controller.front;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;

@Controller
public class FrontQuotationsController extends BaseController {

    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private ConstantMap constantMap;

    @RequestMapping("/json")
    public ModelAndView json(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("front/real/json");
        return modelAndView;
    }
}
