package com.huagu.vcoin.main.controller.admin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.OperationlogEnum;
import com.huagu.vcoin.main.Enum.RemittanceTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Foperationlog;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.OperationLogService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.util.Utils;

@Controller
public class OperationLogController extends BaseController {
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/operationLogList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/operationLogList");
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
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
                filter.append("fuser.fnickName like '%" + keyWord + "%' or \n");
                filter.append("fkey1 like '%" + keyWord + "%' or \n");
                filter.append("fuser.frealName like '%" + keyWord + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') >= '" + logDate + "'
            // \n");
            filter.append("and fcreateTime >= curdate() and curdate() >='" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') <= '" + logDate2 + "'
            // \n");
            filter.append("and fcreateTime <= curdate() and curdate() <= '" + logDate2 + "' \n");
            modelAndView.addObject("logDate2", logDate2);
        }

        if (orderField != null && orderField.trim().length() > 0) {
            filter.append("order by " + orderField + "\n");
        } else {
            filter.append("order by fid \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filter.append(orderDirection + "\n");
        } else {
            filter.append("desc \n");
        }
        List<Foperationlog> list = this.operationLogService.list((currentPage - 1) * numPerPage, numPerPage,
                filter + "", true);
        modelAndView.addObject("operationlogList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "operationLogList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Foperationlog", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goOperationLogJSP")
    public ModelAndView goOperationLogJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Foperationlog operationlog = this.operationLogService.findById(fid);
            modelAndView.addObject("operationlog", operationlog);
        }
        modelAndView.setViewName(url);
        Map<Integer, String> typeMap = new HashMap<Integer, String>();
        typeMap.put(RemittanceTypeEnum.Type1, RemittanceTypeEnum.getEnumString(RemittanceTypeEnum.Type1));
        typeMap.put(RemittanceTypeEnum.Type2, RemittanceTypeEnum.getEnumString(RemittanceTypeEnum.Type2));
        typeMap.put(RemittanceTypeEnum.Type3, RemittanceTypeEnum.getEnumString(RemittanceTypeEnum.Type3));
        modelAndView.addObject("typeMap", typeMap);
        return modelAndView;
    }

    @RequestMapping("ssadmin/saveOperationLog")
    public ModelAndView saveOperationLog() throws Exception {
        Foperationlog operationlog = new Foperationlog();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        String dateString = sdf.format(new java.util.Date());
        Timestamp tm = Timestamp.valueOf(dateString);
        int userId = Integer.parseInt(request.getParameter("userLookup.id"));
        int ftype = Integer.parseInt(request.getParameter("ftype"));
        String famount = request.getParameter("famount");
        String fdescription = request.getParameter("fdescription");

        Fuser user = this.userService.findById(userId);
        operationlog.setFcreateTime(tm);
        operationlog.setFlastUpdateTime(tm);
        operationlog.setFamount(Double.valueOf(famount));
        operationlog.setFdescription(fdescription);
        operationlog.setFtype(ftype);
        operationlog.setFuser(user);
        operationlog.setFstatus(OperationlogEnum.SAVE);
        this.operationLogService.saveObj(operationlog);

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "新增成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteOperationLog")
    public ModelAndView deleteOperationLog() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        Foperationlog operationLog = this.operationLogService.findById(fid);
        if (operationLog.getFstatus() != OperationlogEnum.SAVE) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "删除失败");
            return modelAndView;
        }

        this.operationLogService.deleteObj(fid);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "删除成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/auditOperationLog")
    public ModelAndView auditOperationLog() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        boolean flag = false;
        try {
            Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
            flag = this.operationLogService.updateOperationLog(fid, sessionAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        if (!flag) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "审核失败");
            return modelAndView;
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "审核成功");
        return modelAndView;
    }
}
