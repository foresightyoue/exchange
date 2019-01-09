package com.huagu.vcoin.main.controller.admin;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fwithdrawfees;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.WithdrawFeesService;
import com.huagu.vcoin.util.Utils;

@Controller
public class WithdrawFeesController extends BaseController {
    @Autowired
    private WithdrawFeesService withdrawFeesService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/withdrawFeesList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/withdrawFeesList");
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
        if (keyWord != null && keyWord.trim().length() > 0) {
            Pattern pattern = Pattern.compile("[0-9]*");
            boolean flag = pattern.matcher(keyWord).matches();
            if (flag) {
                filter.append("and flevel =" + keyWord + " \n");
                modelAndView.addObject("keywords", keyWord);
            }
        }

        filter.append(" and fvirtualcointype.ftype=" + CoinTypeEnum.FB_CNY_VALUE + " \n");

        if (orderField != null && orderField.trim().length() > 0) {
            filter.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filter.append(orderDirection + "\n");
        }

        List<Fwithdrawfees> list = this.withdrawFeesService.list((currentPage - 1) * numPerPage, numPerPage,
                filter + "", true);
        modelAndView.addObject("withdrawFeesList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "withdrawFeesList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fwithdrawfees", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goWithdrawFeesJSP")
    public ModelAndView goWithdrawFeesJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fwithdrawfees withdrawfees = this.withdrawFeesService.findById(fid);
            modelAndView.addObject("withdrawfees", withdrawfees);
        }
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateWithdrawFees")
    public ModelAndView updateWithdrawFees(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fwithdrawfees withdrawfees = this.withdrawFeesService.findById(fid);
        withdrawfees.setFfee(Double.valueOf(request.getParameter("ffee")));
        withdrawfees.setFamt(Double.valueOf(request.getParameter("famt")));
        withdrawfees.setFlastFee(Double.valueOf(request.getParameter("flastFee")));
        if (withdrawfees.getFfee() >= 1 || withdrawfees.getFfee() < 0) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "手续费率只能是小于1的小数！");
            return modelAndView;
        }

        this.withdrawFeesService.updateObj(withdrawfees);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }
}
