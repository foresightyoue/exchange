package com.huagu.vcoin.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fshareplanlog;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.SharePlanLogService;
import com.huagu.vcoin.util.Utils;

@Controller
public class SharePlanLogController extends BaseController {
    @Autowired
    private SharePlanLogService sharePlanLogService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/sharePlanLogList")
    public ModelAndView sharePlanLogList(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/sharePlanLogList");
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
            filter.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
            filter.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("fuser.fid =" + fid + " or \n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            filter.append("fuser.frealName like '%" + keyWord + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }
        String parentId = request.getParameter("parentId");
        if (parentId != null && parentId.trim().length() > 0) {
            filter.append("and fshareplan.fid=" + parentId);
            modelAndView.addObject("parentId", parentId);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filter.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filter.append(orderDirection + "\n");
        }
        List<Fshareplanlog> list = this.sharePlanLogService.list((currentPage - 1) * numPerPage, numPerPage,
                filter + "", true);
        modelAndView.addObject("sharePlanLogList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "sharePlanLogList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fshareplanlog", filter + ""));
        return modelAndView;
    }

}
