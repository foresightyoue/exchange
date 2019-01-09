package com.huagu.vcoin.main.controller.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fquestion;
import com.huagu.vcoin.main.service.front.FrontQuestionService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.util.PaginUtil;

import net.sf.json.JSONObject;
@Controller
public class FrontContactController extends BaseController {

    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontQuestionService frontQuestionService;
	  @RequestMapping("/m/financial/FrontContactController/contact")
    public ModelAndView contact(HttpServletRequest request) {
        ModelAndView retMap = new ModelAndView();  //返回新的ModelAndView
        retMap.setViewName("app/financial/contact");
        return retMap;
        }

    @RequestMapping("/m/financial/contactlist")
    public ModelAndView contactlist(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage) {
        ModelAndView retMap = new ModelAndView(); // 返回新的ModelAndView
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("fuser.fid", GetCurrentUser(request).getFid());
        int totalCount = this.frontQuestionService.findByParamCount(param);
        int totalPage = totalCount / maxResults + (totalCount % maxResults == 0 ? 0 : 1);
        currentPage = currentPage < 1 ? 1 : currentPage;
        currentPage = currentPage > totalPage ? totalCount : currentPage;

        List<Fquestion> list = this.frontQuestionService.findByParam(param,
                PaginUtil.firstResult(currentPage, maxResults), maxResults, "fcreateTime desc");

        String pagin = PaginUtil.generatePagin(totalPage, currentPage,
                "/m/financial/contactlist.html?");
        retMap.addObject("pagin", pagin);
        retMap.addObject("list", list);
        retMap.addObject("currentPage", currentPage);
        retMap.setViewName("app/financial/contactList");
        return retMap;
    }
    
    @ResponseBody
    @RequestMapping(value = "m/trade/getcontact", produces = { JsonEncode })
    public String getcontact(HttpServletRequest request) {
    	JSONObject json = new JSONObject();
    	Map<String, Object> param = new HashMap<String, Object>();
        param.put("fuser.fid", GetCurrentUser(request).getFid());
        param.put("isallsys", 1);
        try {
        	int totalCount = this.frontQuestionService.findByParamCount(param);
        	json.accumulate("Count", totalCount);
        	json.accumulate("code", 200);
        	json.accumulate("msg", "请求成功！");
		} catch (Exception e) {
			e.printStackTrace();
			json.accumulate("code", 500);
        	json.accumulate("msg", "请求失败！");
		}
        return json.toString();
    }
}
