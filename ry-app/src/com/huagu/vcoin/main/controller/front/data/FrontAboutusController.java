package com.huagu.vcoin.main.controller.front.data;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.main.controller.BaseController;
@Controller
public class FrontAboutusController extends BaseController {
	 @RequestMapping("/m/financial/FrontAboutusController/aboutus")
	 public ModelAndView aboutus(HttpServletRequest request){
	        ModelAndView retMap = new ModelAndView();  //返回新的ModelAndView
	        retMap.setViewName("app/financial/aboutus");
	        return retMap;
	        }
}
