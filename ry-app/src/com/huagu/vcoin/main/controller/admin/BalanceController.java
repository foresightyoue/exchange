package com.huagu.vcoin.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fbalance;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.BalanceService;
import com.huagu.vcoin.util.Utils;

@Controller
public class BalanceController extends BaseController {
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/balanceList")
    public ModelAndView balanceList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/balanceList");
        // 当前页
        int currentPage = 1;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        List<Fbalance> list = this.balanceService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("balanceList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "balanceList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fbalance", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goBalanceJSP")
    public ModelAndView goBalanceJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fbalance fbalance = this.balanceService.findById(fid);
            modelAndView.addObject("fbalance", fbalance);
        }
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateBalance")
    public ModelAndView updateBalance() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fbalance balance = this.balanceService.findById(fid);
        balance.setFcreatetime(Utils.getTimestamp());
        balance.setFtitle(request.getParameter("ftitle"));
        balance.setFmaxqty(Double.valueOf(request.getParameter("fmaxqty")));
        balance.setFminqty(Double.valueOf(request.getParameter("fminqty")));
        this.balanceService.updateObj(balance);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

}
