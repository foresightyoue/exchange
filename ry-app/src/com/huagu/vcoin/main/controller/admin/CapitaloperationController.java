package com.huagu.vcoin.main.controller.admin;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CapitalOperationInStatus;
import com.huagu.vcoin.main.Enum.CapitalOperationOutStatus;
import com.huagu.vcoin.main.Enum.CapitalOperationTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Fcapitaloperation;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.CapitaloperationService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

@Controller
public class CapitaloperationController extends BaseController {
    @Autowired
    private CapitaloperationService capitaloperationService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/capitaloperationList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/capitaloperationList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {

            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append("famount like '%" + keyWord + "%') \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            // filterSQL.append("and DATE_FORMAT(fLastUpdateTime,'%Y-%m-%d') >= '" + logDate
            // + "' \n");
            filterSQL.append("and  fLastUpdateTime >= curdate() and curdate() >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            // filterSQL.append("and DATE_FORMAT(fLastUpdateTime,'%Y-%m-%d') <= '" +
            // logDate2 + "' \n");
            filterSQL.append("and   fLastUpdateTime <=curdate() and curdate() <= '" + logDate2 + "' \n");
            modelAndView.addObject("logDate2", logDate2);
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append("AND fid =" + capitalId + " \n");
                modelAndView.addObject("capitalId", capitalId);
            }
        }

        String status = request.getParameter("fstatus");
        if (status != null && status.trim().length() > 0) {
            String fstatus = status.trim();
            if (!fstatus.equals("0")) {
                if (fstatus.indexOf("充值") != -1) {
                    filterSQL.append("AND ftype =" + CapitalOperationTypeEnum.RMB_IN + " \n");
                    filterSQL.append("AND fstatus =" + fstatus.replace("充值-", "") + " \n");
                } else if (fstatus.indexOf("提现") != -1) {
                    filterSQL.append("AND ftype =" + CapitalOperationTypeEnum.RMB_OUT + " \n");
                    filterSQL.append("AND fstatus =" + fstatus.replace("提现-", "") + " \n");
                }
            }
            modelAndView.addObject("fstatus", fstatus);
        } else {
            modelAndView.addObject("fstatus", "0");
        }

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        Map<String, String> statusMap = new HashMap<String, String>();
        statusMap.put("0", "全部");
        statusMap.put("充值-" + CapitalOperationInStatus.Come,
                "充值-" + CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.Come));
        statusMap.put("充值-" + CapitalOperationInStatus.Invalidate,
                "充值-" + CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.Invalidate));
        statusMap.put("充值-" + CapitalOperationInStatus.NoGiven,
                "充值-" + CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.NoGiven));
        statusMap.put("充值-" + CapitalOperationInStatus.WaitForComing,
                "充值-" + CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.WaitForComing));
        statusMap.put("提现-" + CapitalOperationOutStatus.Cancel,
                "提现-" + CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.Cancel));
        statusMap.put("提现-" + CapitalOperationOutStatus.OperationLock,
                "提现-" + CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationLock));
        statusMap.put("提现-" + CapitalOperationOutStatus.OperationSuccess,
                "提现-" + CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationSuccess));
        statusMap.put("提现-" + CapitalOperationOutStatus.WaitForOperation,
                "提现-" + CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation));
        modelAndView.addObject("statusMap", statusMap);

        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * numPerPage, numPerPage,
                filterSQL + "", true);
        modelAndView.addObject("capitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "capitaloperationList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fcapitaloperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/capitalInList")
    public ModelAndView capitalInList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        String type = "(" + CapitalOperationTypeEnum.RMB_IN + ")";
        String status = "(" + CapitalOperationInStatus.WaitForComing + ")";
        modelAndView.setViewName("ssadmin/capitalInList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");

        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append("famount like '%" + keyWord + "%') \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            //filterSQL.append("and  DATE_FORMAT(fLastUpdateTime,'%Y-%m-%d') >= '" + logDate + "' \n");
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            // filterSQL.append("and DATE_FORMAT(fLastUpdateTime,'%Y-%m-%d') <= '" +
            // logDate2 + "' \n");
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
            modelAndView.addObject("logDate2", logDate2);
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
                modelAndView.addObject("capitalId", capitalId);
            }
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        // 今日充值总金额
        Map amountInMap = this.capitaloperationService.getTotalAmountIn(CapitalOperationTypeEnum.RMB_IN,
                "(" + CapitalOperationInStatus.Come + ")", true);
        modelAndView.addObject("amountInMap", amountInMap);

        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * numPerPage, numPerPage,
                filterSQL + "", true);
        modelAndView.addObject("capitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "capitalInList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fcapitaloperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/capitalInSucList")
    public ModelAndView capitalInSucList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        String type = "(" + CapitalOperationTypeEnum.RMB_IN + ")";
        String status = "(" + CapitalOperationInStatus.Come + ")";
        modelAndView.setViewName("ssadmin/capitalInSucList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");

        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append("famount like '%" + keyWord + "%') \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            //filterSQL.append("and  DATE_FORMAT(fLastUpdateTime,'%Y-%m-%d') >= '" + logDate + "' \n");
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            // filterSQL.append("and DATE_FORMAT(fLastUpdateTime,'%Y-%m-%d') <= '" +
            // logDate2 + "' \n");
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
            modelAndView.addObject("logDate2", logDate2);
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
                modelAndView.addObject("capitalId", capitalId);
            }
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * numPerPage, numPerPage,
                filterSQL + "", true);
        modelAndView.addObject("capitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "capitalInSucList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fcapitaloperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/capitalOutList1")
    public ModelAndView capitalOutList1() throws Exception {
        String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
        String status = "(" + CapitalOperationOutStatus.WaitForOperation + "," + CapitalOperationOutStatus.OperationLock
                + ")";
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/capitalOutList1");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append(" AND (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append(" famount like '%" + keyWord + "%') \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
            modelAndView.addObject("logDate2", logDate2);
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
                modelAndView.addObject("capitalId", capitalId);
            }
        }
        filterSQL.append(" and fone =1 and ftwo=0 and fthree=0 \n");

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        double currentAmt = this.adminService.getSQLValue2("select sum(famount) from Fcapitaloperation " + filterSQL);
        modelAndView.addObject("currentAmt", currentAmt);

        // 今日提现总金额
        Map amountOutMap = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                "(" + CapitalOperationOutStatus.OperationSuccess + ")", true);
        modelAndView.addObject("amountOutMap", amountOutMap);

        // 所有提现未转帐总金额
        String coStatus = "(" + CapitalOperationOutStatus.WaitForOperation + ","
                + CapitalOperationOutStatus.OperationLock + ")";
        Map amountOutWaitingMap = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                coStatus, false);
        modelAndView.addObject("amountOutWaitingMap", amountOutWaitingMap);

        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * numPerPage, numPerPage,
                filterSQL + "", true);
        modelAndView.addObject("capitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "capitalOutList1");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fcapitaloperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/capitalOutList2")
    public ModelAndView capitalOutList2() throws Exception {
        String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
        String status = "(" + CapitalOperationOutStatus.WaitForOperation + "," + CapitalOperationOutStatus.OperationLock
                + ")";
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/capitalOutList2");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append(" AND (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append(" famount like '%" + keyWord + "%') \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
            modelAndView.addObject("logDate2", logDate2);
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
                modelAndView.addObject("capitalId", capitalId);
            }
        }
        filterSQL.append(" and fone =1 and ftwo=1 and fthree=0 \n");
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        double currentAmt = this.adminService.getSQLValue2("select sum(famount) from Fcapitaloperation " + filterSQL);
        modelAndView.addObject("currentAmt", currentAmt);

        // 今日提现总金额
        Map amountOutMap = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                "(" + CapitalOperationOutStatus.OperationSuccess + ")", true);
        modelAndView.addObject("amountOutMap", amountOutMap);

        // 所有提现未转帐总金额
        String coStatus = "(" + CapitalOperationOutStatus.WaitForOperation + ","
                + CapitalOperationOutStatus.OperationLock + ")";
        Map amountOutWaitingMap = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                coStatus, false);
        modelAndView.addObject("amountOutWaitingMap", amountOutWaitingMap);

        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * numPerPage, numPerPage,
                filterSQL + "", true);
        modelAndView.addObject("capitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "capitalOutList2");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fcapitaloperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/capitalOutList3")
    public ModelAndView capitalOutList3() throws Exception {
        String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
        String status = "(" + CapitalOperationOutStatus.WaitForOperation + "," + CapitalOperationOutStatus.OperationLock
                + ")";
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/capitalOutList3");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append(" AND (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append(" famount like '%" + keyWord + "%') \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
            modelAndView.addObject("logDate2", logDate2);
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
                modelAndView.addObject("capitalId", capitalId);
            }
        }
        filterSQL.append(" and fone =1 and ftwo=1 and fthree=1 \n");
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        double currentAmt = this.adminService.getSQLValue2("select sum(famount) from Fcapitaloperation " + filterSQL);
        modelAndView.addObject("currentAmt", currentAmt);

        // 今日提现总金额
        Map amountOutMap = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                "(" + CapitalOperationOutStatus.OperationSuccess + ")", true);
        modelAndView.addObject("amountOutMap", amountOutMap);

        // 所有提现未转帐总金额
        String coStatus = "(" + CapitalOperationOutStatus.WaitForOperation + ","
                + CapitalOperationOutStatus.OperationLock + ")";
        Map amountOutWaitingMap = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                coStatus, false);
        modelAndView.addObject("amountOutWaitingMap", amountOutWaitingMap);

        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * numPerPage, numPerPage,
                filterSQL + "", true);
        modelAndView.addObject("capitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "capitalOutList3");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fcapitaloperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/capitalOutSucList")
    public ModelAndView capitalOutSucList() throws Exception {
        String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
        String status = "(" + CapitalOperationOutStatus.OperationSuccess + ")";
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/capitalOutSucList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append(" AND (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append(" famount like '%" + keyWord + "%') \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
            modelAndView.addObject("logDate2", logDate2);
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
                modelAndView.addObject("capitalId", capitalId);
            }
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * numPerPage, numPerPage,
                filterSQL + "", true);
        modelAndView.addObject("capitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "capitalOutSucList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fcapitaloperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goCapitaloperationJSP")
    public ModelAndView goCapitaloperationJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fcapitaloperation capitaloperation = this.capitaloperationService.findById(fid);
            modelAndView.addObject("capitaloperation", capitaloperation);
        }
        return modelAndView;
    }

    @RequestMapping("/ssadmin/capitalInCancel")
    public ModelAndView capitalInCancel() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fcapitaloperation capitalOperation = this.capitaloperationService.findById(fid);
        int status = capitalOperation.getFstatus();
        if (status == CapitalOperationInStatus.Come || status == CapitalOperationInStatus.Invalidate) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "取消失败");
            return modelAndView;
        }
        capitalOperation.setFstatus(CapitalOperationInStatus.Invalidate);
        this.capitaloperationService.updateObj(capitalOperation);
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "取消成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/capitalInAudit")
    public ModelAndView capitalInAudit() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fcapitaloperation capitalOperation = this.capitaloperationService.findById(fid);
        int status = capitalOperation.getFstatus();
        if (status != CapitalOperationInStatus.WaitForComing) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            String status_s = CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.WaitForComing);
            modelAndView.addObject("message", "审核失败,只有状态为:" + status_s + "的充值记录才允许审核!");
            return modelAndView;
        }
        // 根据用户查钱包最后修改时间
        Fvirtualwallet walletInfo = this.frontUserService.findWalletByUser(capitalOperation.getFuser().getFid());
        if (walletInfo == null) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "审核失败，会员钱包信息异常!");
            return modelAndView;
        }
        double amount = capitalOperation.getFamount();

        Fadmin admin = (Fadmin) request.getSession().getAttribute("login_admin");
        // 充值操作
        capitalOperation.setFstatus(CapitalOperationInStatus.Come);
        capitalOperation.setfLastUpdateTime(Utils.getTimestamp());
        capitalOperation.setfAuditee_id(admin);
        // 钱包操作
        walletInfo.setFlastUpdateTime(Utils.getTimestamp());
        walletInfo.setFtotal(walletInfo.getFtotal() + amount);

        boolean flag = false;
        try {
            this.capitaloperationService.updateCapital(capitalOperation, walletInfo, true);
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        if (!flag) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "审核失败");
            return modelAndView;
        }
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "审核成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/capitalOutAudit")
    public ModelAndView capitalOutAudit(@RequestParam(required = true) int type) throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fcapitaloperation capitalOperation = this.capitaloperationService.findById(fid);
        int status = capitalOperation.getFstatus();

        switch (type) {
        case 1:
            if (status != CapitalOperationOutStatus.OperationLock) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationLock);
                modelAndView.addObject("message", "审核失败,只有状态为:‘" + status_s + "’的提现记录才允许审核!");
                return modelAndView;
            }
            break;
        case 2:
            if (status != CapitalOperationOutStatus.WaitForOperation) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation);
                modelAndView.addObject("message", "锁定失败,只有状态为:‘" + status_s + "’的提现记录才允许锁定!");
                return modelAndView;
            }
            break;
        case 3:
            if (status != CapitalOperationOutStatus.OperationLock) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationLock);
                modelAndView.addObject("message", "取消锁定失败,只有状态为:‘" + status_s + "’的提现记录才允许取消锁定!");
                return modelAndView;
            }
            break;
        case 4:
            if (status == CapitalOperationOutStatus.Cancel || status == CapitalOperationOutStatus.OperationSuccess) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "取消失败!");
                return modelAndView;
            }
            break;
        default:
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "非法提交！");
            return modelAndView;
        }

        // 根据用户查钱包最后修改时间
        Fvirtualwallet walletInfo = this.frontUserService.findWalletByUser(capitalOperation.getFuser().getFid());
        if (walletInfo == null) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "审核失败，会员钱包信息异常!");
            return modelAndView;
        }

        double amount = capitalOperation.getFamount();
        double frees = capitalOperation.getFfees();
        double totalAmt = Utils.getDouble(amount + frees, 2);

        // 充值操作
        Fadmin admin = (Fadmin) request.getSession().getAttribute("login_admin");
        capitalOperation.setfLastUpdateTime(Utils.getTimestamp());
        capitalOperation.setfAuditee_id(admin);

        // 钱包操作//1审核,2锁定,3取消锁定,4取消提现
        String tips = "";
        switch (type) {
        case 1:
            capitalOperation.setFstatus(CapitalOperationOutStatus.OperationSuccess);

            walletInfo.setFlastUpdateTime(Utils.getTimestamp());
            walletInfo.setFfrozen(walletInfo.getFfrozen() - totalAmt);
            tips = "审核";
            break;
        case 2:
            capitalOperation.setFstatus(CapitalOperationOutStatus.OperationLock);
            tips = "锁定";
            break;
        case 3:
            capitalOperation.setFstatus(CapitalOperationOutStatus.WaitForOperation);
            tips = "取消锁定";
            break;
        case 4:
            capitalOperation.setFstatus(CapitalOperationOutStatus.Cancel);
            walletInfo.setFlastUpdateTime(Utils.getTimestamp());
            walletInfo.setFfrozen(walletInfo.getFfrozen() - totalAmt);
            walletInfo.setFtotal(walletInfo.getFtotal() + totalAmt);
            tips = "取消";
            break;
        }

        boolean flag = false;
        try {
            this.capitaloperationService.updateCapital(capitalOperation, walletInfo, false);
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        if (!flag) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", tips + "失败");
            return modelAndView;
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", tips + "成功");
        return modelAndView;
    }

    private static enum ExportFiled {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 状态, 金额, 手续费, 备注, 创建时间, 最后修改时间, 审核人;
    }

    private List<Fcapitaloperation> getCapitalOperationList(String type, String status, int times) {
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        if (type != null && type.trim().length() > 0) {
            filterSQL.append("and ftype IN " + type + "\n");
        }
        if (status != null && status.trim().length() > 0) {
            filterSQL.append("AND fstatus IN " + status + "\n");
        }
        if (times == 1) {
            filterSQL.append(" and fone =1 and ftwo=0 and fthree=0 \n");
        } else if (times == 2) {
            filterSQL.append(" and fone =1 and ftwo=1 and fthree=0 \n");
        } else if (times == 3) {
            filterSQL.append(" and fone =1 and ftwo=1 and fthree=1 \n");
        }
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append(" AND (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append(" famount like '%" + keyWord + "%') \n");
            }
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
            }
        }

        try {
            if (request.getParameter("fstatus") != null && request.getParameter("fstatus").trim().length() > 0) {
                String fstatus = new String(request.getParameter("fstatus").getBytes("iso8859-1"), "utf-8");
                if (!fstatus.equals("0")) {
                    if (fstatus.indexOf("充值") != -1) {
                        filterSQL.append("AND ftype =" + CapitalOperationTypeEnum.RMB_IN + " \n");
                        filterSQL.append("AND fstatus =" + fstatus.replace("充值-", "") + " \n");
                    } else if (fstatus.indexOf("提现") != -1) {
                        filterSQL.append("AND ftype =" + CapitalOperationTypeEnum.RMB_OUT + " \n");
                        filterSQL.append("AND fstatus =" + fstatus.replace("提现-", "") + " \n");
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }
        List<Fcapitaloperation> list = this.capitaloperationService.list(0, 0, filterSQL + "", false);
        return list;
    }

    @RequestMapping("ssadmin/capitaloperationExport")
    public ModelAndView capitaloperationExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=capitaloperationList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        String type = null;
        String status = null;
        List<Fcapitaloperation> capitalOperationList = getCapitalOperationList(type, status, 0);
        for (Fcapitaloperation capitalOperation : capitalOperationList) {
            e.createRow(rowIndex++);
            for (ExportFiled filed : ExportFiled.values()) {
                switch (filed) {
                case 订单ID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFid());
                    break;
                case 会员UID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFrealName());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), capitalOperation.getFstatus_s());
                    break;
                case 金额:
                    e.setCell(filed.ordinal(), capitalOperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), capitalOperation.getFfees());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), capitalOperation.getFcreateTime());
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), capitalOperation.getfLastUpdateTime());
                    break;
                case 审核人:
                    if (capitalOperation.getfAuditee_id() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getfAuditee_id().getFname());
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);
        response.getOutputStream().close();

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "导出成功");
        return modelAndView;
    }

    private static enum Export1Filed {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 类型, 状态, 金额, 手续费, 开户行信息, 银行, 收款帐号, 手机号码, 创建时间, 最后修改时间;
    }

    @RequestMapping("ssadmin/capitalOutExport1")
    public ModelAndView capitalOutExport1(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=capitalOutList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export1Filed filed : Export1Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
        String status = "(" + CapitalOperationOutStatus.WaitForOperation + "," + CapitalOperationOutStatus.OperationLock
                + ")";
        int times = 1;
        List<Fcapitaloperation> capitalOperationList = getCapitalOperationList(type, status, times);
        for (Fcapitaloperation capitalOperation : capitalOperationList) {
            e.createRow(rowIndex++);
            for (Export1Filed filed : Export1Filed.values()) {
                switch (filed) {
                case 订单ID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFid());
                    break;
                case 会员UID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFrealName());
                    break;
                case 类型:
                    e.setCell(filed.ordinal(), capitalOperation.getFtype_s());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), capitalOperation.getFstatus_s());
                    break;
                case 金额:
                    e.setCell(filed.ordinal(), capitalOperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), capitalOperation.getFfees());
                    break;
                case 开户行信息:
                    e.setCell(filed.ordinal(), capitalOperation.getFaddress());
                    break;
                case 银行:
                    e.setCell(filed.ordinal(), capitalOperation.getfBank());
                    break;
                case 收款帐号:
                    e.setCell(filed.ordinal(), capitalOperation.getFaccount_s());
                    break;
                case 手机号码:
                    e.setCell(filed.ordinal(), capitalOperation.getfPhone());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), capitalOperation.getFcreateTime());
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), capitalOperation.getfLastUpdateTime());
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);
        response.getOutputStream().close();

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "导出成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/capitalOutExport2")
    public ModelAndView capitalOutExport2(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=capitalOutList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export1Filed filed : Export1Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
        String status = "(" + CapitalOperationOutStatus.WaitForOperation + "," + CapitalOperationOutStatus.OperationLock
                + ")";
        int times = 2;
        List<Fcapitaloperation> capitalOperationList = getCapitalOperationList(type, status, times);
        for (Fcapitaloperation capitalOperation : capitalOperationList) {
            e.createRow(rowIndex++);
            for (Export1Filed filed : Export1Filed.values()) {
                switch (filed) {
                case 订单ID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFid());
                    break;
                case 会员UID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFrealName());
                    break;
                case 类型:
                    e.setCell(filed.ordinal(), capitalOperation.getFtype_s());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), capitalOperation.getFstatus_s());
                    break;
                case 金额:
                    e.setCell(filed.ordinal(), capitalOperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), capitalOperation.getFfees());
                    break;
                case 开户行信息:
                    e.setCell(filed.ordinal(), capitalOperation.getFaddress());
                    break;
                case 银行:
                    e.setCell(filed.ordinal(), capitalOperation.getfBank());
                    break;
                case 收款帐号:
                    e.setCell(filed.ordinal(), capitalOperation.getFaccount_s());
                    break;
                case 手机号码:
                    e.setCell(filed.ordinal(), capitalOperation.getfPhone());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), capitalOperation.getFcreateTime());
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), capitalOperation.getfLastUpdateTime());
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);
        response.getOutputStream().close();

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "导出成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/capitalOutExport")
    public ModelAndView capitalOutExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=capitalOutList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export1Filed filed : Export1Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
        String status = "(" + CapitalOperationOutStatus.WaitForOperation + "," + CapitalOperationOutStatus.OperationLock
                + ")";
        int times = 3;
        List<Fcapitaloperation> capitalOperationList = getCapitalOperationList(type, status, times);
        for (Fcapitaloperation capitalOperation : capitalOperationList) {
            e.createRow(rowIndex++);
            for (Export1Filed filed : Export1Filed.values()) {
                switch (filed) {
                case 订单ID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFid());
                    break;
                case 会员UID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFrealName());
                    break;
                case 类型:
                    e.setCell(filed.ordinal(), capitalOperation.getFtype_s());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), capitalOperation.getFstatus_s());
                    break;
                case 金额:
                    e.setCell(filed.ordinal(), capitalOperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), capitalOperation.getFfees());
                    break;
                case 开户行信息:
                    e.setCell(filed.ordinal(), capitalOperation.getFaddress());
                    break;
                case 银行:
                    e.setCell(filed.ordinal(), capitalOperation.getfBank());
                    break;
                case 收款帐号:
                    e.setCell(filed.ordinal(), capitalOperation.getFaccount_s());
                    break;
                case 手机号码:
                    e.setCell(filed.ordinal(), capitalOperation.getfPhone());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), capitalOperation.getFcreateTime());
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), capitalOperation.getfLastUpdateTime());
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);
        response.getOutputStream().close();

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "导出成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/capitalOutAuditAll")
    public ModelAndView capitalOutAuditAll() throws Exception {
        JspPage modelAndView = new JspPage(request);
        String ids = request.getParameter("ids");
        String[] idString = ids.split(",");
        // 充值操作
        Fadmin admin = (Fadmin) request.getSession().getAttribute("login_admin");
        for (int i = 0; i < idString.length; i++) {
            int id = Integer.parseInt(idString[i]);
            Fcapitaloperation capitalOperation = this.capitaloperationService.findById(id);
            int status = capitalOperation.getFstatus();
            if (status != CapitalOperationOutStatus.WaitForOperation) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation);
                modelAndView.addObject("message", "锁定失败,只有状态为:‘" + status_s + "’的提现记录才允许锁定!");
                return modelAndView;
            }
            capitalOperation.setfLastUpdateTime(Utils.getTimestamp());
            capitalOperation.setfAuditee_id(admin);
            capitalOperation.setFstatus(CapitalOperationOutStatus.OperationLock);
            this.capitaloperationService.updateObj(capitalOperation);
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "批量锁定成功");
        return modelAndView;
    }

    public List<Fcapitaloperation> getCapitalInList() throws Exception {
        String type = "(" + CapitalOperationTypeEnum.RMB_IN + ")";
        String status = "(" + CapitalOperationInStatus.WaitForComing + ")";
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");

        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append("famount like '%" + keyWord + "%') \n");
            }
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
            }
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fcapitaloperation> list = this.capitaloperationService.list(0, 0, filterSQL + "", false);
        return list;
    }

    public List<Fcapitaloperation> getCapitalInSucList() throws Exception {
        String type = "(" + CapitalOperationTypeEnum.RMB_IN + ")";
        String status = "(" + CapitalOperationInStatus.Come + ")";
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");

        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append("fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append("famount like '%" + keyWord + "%') \n");
            }
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
            }
        }

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fcapitaloperation> list = this.capitaloperationService.list(0, 0, filterSQL + "", false);
        return list;
    }

    public List<Fcapitaloperation> getCapitalOutSucList() throws Exception {
        String type = "(" + CapitalOperationTypeEnum.RMB_OUT + ")";
        String status = "(" + CapitalOperationOutStatus.OperationSuccess + "," + CapitalOperationOutStatus.OperationLock
                + ")";
        String keyWord = request.getParameter("keywords");
        String capitalId = request.getParameter("capitalId");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        filterSQL.append("and ftype IN " + type + "\n");
        filterSQL.append("AND fstatus IN " + status + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append(" AND (fBank like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fAccount like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPhone like '%" + keyWord + "%' OR \n");
                filterSQL.append(" fPayee like '%" + keyWord + "%' OR \n");
                filterSQL.append(" famount like '%" + keyWord + "%') \n");
            }
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime>= curdate() and  curdate() >= '" + logDate + "' \n");
        }

        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            filterSQL.append("and fLastUpdateTime<= curdate() and curdate() <= '" + logDate2 + "' \n");
        }

        if (capitalId != null && capitalId.trim().length() > 0) {
            boolean flag = Utils.isNumeric(capitalId);
            if (flag) {
                filterSQL.append(" AND fid =" + capitalId + "\n");
            }
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fcapitaloperation> list = this.capitaloperationService.list(0, 0, filterSQL + "", false);
        return list;
    }

    private static enum Export3Filed {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 类型, 状态, 金额, 手续费, 开户行信息, 银行, 收款帐号, 手机号码, 创建时间, 最后修改时间;
    }

    @RequestMapping("ssadmin/capitaloperationOutSucExport")
    public ModelAndView capitaloperationOutSucExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=capitalOutSucList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export3Filed filed : Export3Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Fcapitaloperation> capitalOperationList = getCapitalOutSucList();
        for (Fcapitaloperation capitalOperation : capitalOperationList) {
            e.createRow(rowIndex++);
            for (Export3Filed filed : Export3Filed.values()) {
                switch (filed) {
                case 订单ID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFid());
                    break;
                case 会员UID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFrealName());
                    break;
                case 类型:
                    e.setCell(filed.ordinal(), capitalOperation.getFtype_s());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), capitalOperation.getFstatus_s());
                    break;
                case 金额:
                    e.setCell(filed.ordinal(), capitalOperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), capitalOperation.getFfees());
                    break;
                case 开户行信息:
                    e.setCell(filed.ordinal(), capitalOperation.getFaddress());
                    break;
                case 银行:
                    e.setCell(filed.ordinal(), capitalOperation.getfBank());
                    break;
                case 收款帐号:
                    e.setCell(filed.ordinal(), capitalOperation.getFaccount_s());
                    break;
                case 手机号码:
                    e.setCell(filed.ordinal(), capitalOperation.getfPhone());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), capitalOperation.getFcreateTime());
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), capitalOperation.getfLastUpdateTime());
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);
        response.getOutputStream().close();

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "导出成功");
        return modelAndView;
    }

    private static enum Export4Filed {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 充值方式, 金额, 手续费, 官方帐号银行, 官方帐号, 创建时间, 最后修改时间;
    }

    @RequestMapping("ssadmin/capitaloperationInExport")
    public ModelAndView capitaloperationInExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=capitalInList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export4Filed filed : Export4Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Fcapitaloperation> capitalOperationList = getCapitalInList();
        for (Fcapitaloperation capitalOperation : capitalOperationList) {
            e.createRow(rowIndex++);
            for (Export4Filed filed : Export4Filed.values()) {
                switch (filed) {
                case 订单ID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFid());
                    break;
                case 会员UID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFrealName());
                    break;
                case 充值方式:
                    e.setCell(filed.ordinal(), capitalOperation.getFremittanceType());
                    break;
                case 金额:
                    e.setCell(filed.ordinal(), capitalOperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), capitalOperation.getFfees());
                    break;
                case 官方帐号银行:
                    if (capitalOperation.getSystembankinfo() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getSystembankinfo().getFbankName());
                    break;
                case 官方帐号:
                    if (capitalOperation.getSystembankinfo() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getSystembankinfo().getFbankNumber());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), capitalOperation.getFcreateTime());
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), capitalOperation.getfLastUpdateTime());
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);
        response.getOutputStream().close();

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "导出成功");
        return modelAndView;
    }

    private static enum Export5Filed {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 充值方式, 金额, 手续费, 官方帐号银行, 官方帐号, 创建时间, 最后修改时间;
    }

    @RequestMapping("ssadmin/capitaloperationInSucExport")
    public ModelAndView capitaloperationInSucExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=capitalInSucList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export5Filed filed : Export5Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Fcapitaloperation> capitalOperationList = getCapitalInSucList();
        for (Fcapitaloperation capitalOperation : capitalOperationList) {
            e.createRow(rowIndex++);
            for (Export5Filed filed : Export5Filed.values()) {
                switch (filed) {
                case 订单ID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFid());
                    break;
                case 会员UID:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (capitalOperation.getFuser() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getFuser().getFrealName());
                    break;
                case 充值方式:
                    e.setCell(filed.ordinal(), capitalOperation.getFremittanceType());
                    break;
                case 金额:
                    e.setCell(filed.ordinal(), capitalOperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), capitalOperation.getFfees());
                    break;
                case 官方帐号银行:
                    if (capitalOperation.getSystembankinfo() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getSystembankinfo().getFbankName());
                    break;
                case 官方帐号:
                    if (capitalOperation.getSystembankinfo() != null)
                        e.setCell(filed.ordinal(), capitalOperation.getSystembankinfo().getFbankNumber());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), capitalOperation.getFcreateTime());
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), capitalOperation.getfLastUpdateTime());
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);
        response.getOutputStream().close();

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "导出成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/capitalOutaAuditAll")
    public ModelAndView capitalOutaAuditAll() throws Exception {
        JspPage modelAndView = new JspPage(request);
        String ids = request.getParameter("ids");
        String[] idString = ids.split(",");
        for (int i = 0; i < idString.length; i++) {
            int id = Integer.parseInt(idString[i]);
            Fcapitaloperation capitalOperation = this.capitaloperationService.findById(id);
            if (capitalOperation.getFstatus() != CapitalOperationOutStatus.OperationLock) {
                continue;
            }
            Fvirtualwallet walletInfo = this.frontUserService.findWalletByUser(capitalOperation.getFuser().getFid());
            double amount = capitalOperation.getFamount();
            double frees = capitalOperation.getFfees();
            double totalAmt = Utils.getDouble(amount + frees, 2);
            // 充值操作
            Fadmin admin = (Fadmin) request.getSession().getAttribute("login_admin");
            capitalOperation.setfLastUpdateTime(Utils.getTimestamp());
            capitalOperation.setfAuditee_id(admin);
            capitalOperation.setFstatus(CapitalOperationOutStatus.OperationSuccess);

            walletInfo.setFlastUpdateTime(Utils.getTimestamp());
            walletInfo.setFfrozen(walletInfo.getFfrozen() - totalAmt);
            try {
                this.capitaloperationService.updateCapital(capitalOperation, walletInfo, false);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作完成");
        return modelAndView;
    }

    @RequestMapping("ssadmin/capitalOutaAuditAll1")
    public ModelAndView capitalOutaAuditAll1() throws Exception {
        JspPage modelAndView = new JspPage(request);
        String ids = request.getParameter("ids");
        String[] idString = ids.split(",");
        int j = 0;
        for (int i = 0; i < idString.length; i++) {
            int id = Integer.parseInt(idString[i]);
            Fcapitaloperation capitalOperation = this.capitaloperationService.findById(id);
            if (capitalOperation.getFstatus() != CapitalOperationOutStatus.OperationLock) {
                j = j + 1;
                continue;
            }
            capitalOperation.setFtwo(true);
            try {
                this.capitaloperationService.updateObj(capitalOperation);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作完成,其中" + j + "笔操作失败！");
        return modelAndView;
    }

    @RequestMapping("ssadmin/capitalOutaAuditAll2")
    public ModelAndView capitalOutaAuditAll2() throws Exception {
        JspPage modelAndView = new JspPage(request);
        String ids = request.getParameter("ids");
        String[] idString = ids.split(",");
        int j = 0;
        for (int i = 0; i < idString.length; i++) {
            int id = Integer.parseInt(idString[i]);
            Fcapitaloperation capitalOperation = this.capitaloperationService.findById(id);
            if (capitalOperation.getFstatus() != CapitalOperationOutStatus.OperationLock) {
                j = j + 1;
                continue;
            }
            capitalOperation.setFthree(true);
            try {
                this.capitaloperationService.updateObj(capitalOperation);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作完成,其中" + j + "笔操作失败！");
        return modelAndView;
    }
}
