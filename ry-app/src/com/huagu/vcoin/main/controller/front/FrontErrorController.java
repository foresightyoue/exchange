package com.huagu.vcoin.main.controller.front;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;

@Controller
public class FrontErrorController extends BaseController {

    @RequestMapping("/error/error")
    public ModelAndView error404(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("front/error/error");
        return modelAndView;
    }
}
