package com.huagu.vcoin.main.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.BalanceRecTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fbalancetype;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.BalancetypeService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.util.Utils;

@Controller
public class BalancetypeController extends BaseController {
    @Autowired
    private BalancetypeService balancetypeService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private VirtualCoinService virtualCoinService;

    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/balancetypeList")
    public ModelAndView balancetypeList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/balancetypeList");
        // 当前页
        int currentPage = 1;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        List<Fbalancetype> list = this.balancetypeService.list((currentPage - 1) * numPerPage, numPerPage, filter + "",
                true);
        modelAndView.addObject("balancetypeList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "balancetypeList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fbalancetype", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goBalancetypeJSP")
    public ModelAndView goBalancetypeJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fbalancetype fbalancetype = this.balancetypeService.findById(fid);
            modelAndView.addObject("fbalancetype", fbalancetype);
        }
        List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
        modelAndView.addObject("allType", allType);

        Map<Integer, String> typeMap = new HashMap<Integer, String>();
        typeMap.put(BalanceRecTypeEnum.CNY, BalanceRecTypeEnum.getEnumString(BalanceRecTypeEnum.CNY));
        typeMap.put(BalanceRecTypeEnum.GWQ, BalanceRecTypeEnum.getEnumString(BalanceRecTypeEnum.GWQ));
        typeMap.put(BalanceRecTypeEnum.XMB, BalanceRecTypeEnum.getEnumString(BalanceRecTypeEnum.XMB));
        modelAndView.addObject("typeMap", typeMap);
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateBalancetype")
    public ModelAndView updateBalancetype() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fbalancetype balancetype = this.balancetypeService.findById(fid);
        balancetype.setFcreatetime(Utils.getTimestamp());
        balancetype.setFname(request.getParameter("fname"));
        balancetype.setFrate(Double.valueOf(request.getParameter("frate")));
        balancetype.setFday(Integer.parseInt(request.getParameter("fday")));
        // balancetype.setFrecType(Integer.parseInt(request.getParameter("frecType")));
        int vid = Integer.parseInt(request.getParameter("vid"));
        Fvirtualcointype vt = this.virtualCoinService.findById(vid);
        balancetype.setFvirtualcointype(vt);
        this.balancetypeService.updateObj(balancetype);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteBalancetype")
    public ModelAndView deleteBalancetype() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        this.balancetypeService.deleteObj(fid);

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "删除成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/saveBalancetype")
    public ModelAndView saveBalancetype() throws Exception {
        JspPage modelAndView = new JspPage(request);
        Fbalancetype balancetype = new Fbalancetype();
        balancetype.setFcreatetime(Utils.getTimestamp());
        balancetype.setFname(request.getParameter("fname"));
        balancetype.setFrate(Double.valueOf(request.getParameter("frate")));
        balancetype.setFday(Integer.parseInt(request.getParameter("fday")));
        // balancetype.setFrecType(Integer.parseInt(request.getParameter("frecType")));
        int vid = Integer.parseInt(request.getParameter("vid"));
        Fvirtualcointype vt = this.virtualCoinService.findById(vid);
        balancetype.setFvirtualcointype(vt);
        this.balancetypeService.saveObj(balancetype);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "保存成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

}
