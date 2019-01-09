package com.huagu.vcoin.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Flimittrade;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.LimittradeService;
import com.huagu.vcoin.main.service.admin.TradeMappingService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

@Controller
public class LimittradeController extends BaseController {
    @Autowired
    private LimittradeService limittradeService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private TradeMappingService tradeMappingService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/limittradeList")
    public ModelAndView limittradeList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/limittradeList");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        // if(keyWord != null && keyWord.trim().length() >0){
        // filter.append("and (fname like '%"+keyWord+"%' or \n");
        // filter.append("furl like '%"+keyWord+"%' ) \n");
        // modelAndView.addObject("keywords", keyWord);
        // }
        // if(orderField != null && orderField.trim().length() >0){
        // filter.append("order by "+orderField+"\n");
        // }else{
        // filter.append("order by fcreateTime \n");
        // }
        //
        // if(orderDirection != null && orderDirection.trim().length() >0){
        // filter.append(orderDirection+"\n");
        // }else{
        // filter.append("desc \n");
        // }

        List<Flimittrade> list = this.limittradeService.list((currentPage - 1) * numPerPage, numPerPage, filter + "",
                true);
        modelAndView.addObject("limittradeList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "limittradeList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Flimittrade", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goLimittradeJSP")
    public ModelAndView goLimittradeJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Flimittrade flimittrade = this.limittradeService.findById(fid);
            modelAndView.addObject("flimittrade", flimittrade);
        }

        String filter = "where fstatus=" + TrademappingStatusEnum.ACTIVE;
        List<Ftrademapping> trademappings = this.tradeMappingService.list(0, 0, filter, false);
        modelAndView.addObject("trademappings", trademappings);
        return modelAndView;
    }

    @RequestMapping("ssadmin/saveLimittrade")
    public ModelAndView saveLimittrade() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        int vid = Integer.parseInt(request.getParameter("vid"));
        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping(vid);
        String filter = "where ftrademapping.fid=" + vid;
        int count = this.adminService.getAllCount("Flimittrade", filter);
        if (count > 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "此币种已存在记录");
            return modelAndView;
        }
        Flimittrade limittrade = new Flimittrade();
        limittrade.setFtrademapping(ftrademapping);
        limittrade.setFpercent(Double.valueOf(request.getParameter("fpercent")));
        limittrade.setFdownprice(Double.valueOf(request.getParameter("fdownprice")));
        limittrade.setFupprice(Double.valueOf(request.getParameter("fupprice")));
        this.limittradeService.saveObj(limittrade);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "新增成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteLimittrade")
    public ModelAndView deleteLimittrade() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        this.limittradeService.deleteObj(fid);

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "删除成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateLimittrade")
    public ModelAndView updateLimittrade() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("fid"));
        Flimittrade limittrade = this.limittradeService.findById(fid);
        int vid = Integer.parseInt(request.getParameter("vid"));
        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping(vid);
        String filter = "where ftrademapping.fid=" + vid + " and fid <>" + fid;
        int count = this.adminService.getAllCount("Flimittrade", filter);
        if (count > 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "此币种已存在记录");
            return modelAndView;
        }
        limittrade.setFtrademapping(ftrademapping);
        limittrade.setFpercent(Double.valueOf(request.getParameter("fpercent")));
        limittrade.setFdownprice(Double.valueOf(request.getParameter("fdownprice")));
        limittrade.setFupprice(Double.valueOf(request.getParameter("fupprice")));
        this.limittradeService.updateObj(limittrade);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

}
