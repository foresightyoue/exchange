package com.huagu.vcoin.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.cerc.jdb.core.TDateTime;
import com.huagu.vcoin.util.DBKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.OperationlogEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualoperationlog;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.admin.VirtualOperationLogService;
import com.huagu.vcoin.main.service.admin.VirtualWalletService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.mysql.BatchScript;
import cn.cerc.jdb.other.utils;
import site.jayun.vcoin.bourse.core.RdsQuery;
import site.jayun.vcoin.bourse.dao.WalletAmount;
import site.jayun.vcoin.bourse.dao.WalletException;
import site.jayun.vcoin.bourse.merge.Locker;

@Controller
public class VirtualOperationLogController extends BaseController {
    @Autowired
    private VirtualOperationLogService virtualOperationLogService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/virtualoperationlogList")
    public ModelAndView Index() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualoperationlogList");

        // 环境安全检测
        /*String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }*/

        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        String taskId = request.getParameter("taskId");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
            filter.append("fuser.fnickName like '%" + keyWord + "%' or \n");
            boolean flag = true;
            int fid = 0;
            try {
                fid = Integer.parseInt(keyWord);
            } catch (Exception e) {
                // e.printStackTrace();
                flag = false;
            }
            if (flag) {
                filter.append("fuser.fid =" + fid + " or \n");
            }
            filter.append("fuser.frealName like '%" + keyWord + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') >= '" + logDate + "'
            // \n");
            filter.append("and fcreateTime >=curdate() and curdate() >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }
        if (taskId != null && taskId.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') >= '" + taskId + "'
            // \n");
            filter.append("and taskId = '" + taskId + "' \n");
            modelAndView.addObject("taskId", taskId);
        }
        String logDate2 = request.getParameter("logDate2");
        if (logDate2 != null && logDate2.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') <= '" + logDate2 + "'
            // \n");
            filter.append("and  fcreateTime <= curdate() and curdate() <= '" + logDate2 + "' \n");
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
        List<Fvirtualoperationlog> list = this.virtualOperationLogService.list((currentPage - 1) * numPerPage,
                numPerPage, filter + "", true);
        modelAndView.addObject("virtualoperationlogList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "operationLogList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fvirtualoperationlog", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goVirtualOperationLogJSP")
    public ModelAndView goVirtualOperationLogJSP() throws Exception {

        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        // 环境安全检测
        /*String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }*/
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fvirtualoperationlog virtualoperationlog = this.virtualOperationLogService.findById(fid);
            modelAndView.addObject("virtualoperationlog", virtualoperationlog);
        }
//        List<Fvirtualcointype> allType = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
        modelAndView.addObject("allType", allType);
        modelAndView.setViewName(url);
        return modelAndView;
    }

    @RequestMapping("ssadmin/saveVirtualOperationLog")
    public ModelAndView saveVirtualOperationLog() throws Exception {
        JspPage modelAndView = new JspPage(request);
        Fvirtualoperationlog virtualoperationlog = new Fvirtualoperationlog();
        int userId = Integer.parseInt(request.getParameter("userLookup.id"));
        Fuser user = this.userService.findById(userId);
        int vid = Integer.parseInt(request.getParameter("vid"));
        Fvirtualcointype coinType = this.virtualCoinService.findById(vid);
        Double fqty = Double.valueOf(request.getParameter("fqty"));
        String operationType = request.getParameter("operationType");
        if(operationType.equals("0")) {
            String sql = "where fvirtualcointype.fid=" + vid + "and fuser.fid=" + userId;
            List<Fvirtualwallet> all = this.virtualWalletService.list(0, 0, sql, false);
            if (all != null && all.size() == 1) {
                Fvirtualwallet fvirtualwallet = all.get(0);
                if(fvirtualwallet.getFtotal() - fqty < 0) {
                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                    modelAndView.addObject("statusCode", 200);
                    modelAndView.addObject("message", "资金不足,操作失败");
                    modelAndView.addObject("callbackType", "closeCurrent");
                    return modelAndView;
                }
            }
        }
        virtualoperationlog.setFqty(fqty);
        virtualoperationlog.setFvirtualcointype(coinType);
        virtualoperationlog.setFuser(user);
        virtualoperationlog.setTaskId(DBKeyUtils.getSerialNo());
        virtualoperationlog.setFstatus(OperationlogEnum.SAVE);
        if (request.getParameter("fisSendMsg") != null) {
            virtualoperationlog.setFisSendMsg(1);
        } else {
            virtualoperationlog.setFisSendMsg(0);
        }
        virtualoperationlog.setOperationType(operationType);
        this.virtualOperationLogService.saveObj(virtualoperationlog);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteVirtualOperationLog")
    public ModelAndView deleteVirtualOperationLog() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fvirtualoperationlog virtualoperationlog = this.virtualOperationLogService.findById(fid);
        if (virtualoperationlog.getFstatus() != OperationlogEnum.SAVE) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "删除失败，记录已审核");
            return modelAndView;
        }

        this.virtualOperationLogService.deleteObj(fid);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "删除成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/auditVirtualOperationLog")
    public ModelAndView auditVirtualOperationLog() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        boolean flag = false;
        Fvirtualoperationlog virtualoperationlog = this.virtualOperationLogService.findById(fid);

        if (virtualoperationlog.getFstatus() != OperationlogEnum.SAVE) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "已审核，不允许重复审核");
            return modelAndView;
        }

        try {
            double qty = virtualoperationlog.getFqty();
            int coinTypeId = virtualoperationlog.getFvirtualcointype().getFid();
            int userId = virtualoperationlog.getFuser().getFid();
            String sql = "where fvirtualcointype.fid=" + coinTypeId + "and fuser.fid=" + userId;
            List<Fvirtualwallet> all = this.virtualWalletService.list(0, 0, sql, false);
            if (all != null && all.size() == 1) {
                // 将更新用户钱包冻结金额改为转账模式
                String taskId = virtualoperationlog.getTaskId();
                Mysql handle = new Mysql();
                BatchScript script = new BatchScript(handle);
                WalletAmount wallet = new WalletAmount(handle, WalletAmount.masterUserId, coinTypeId, taskId);
                if(virtualoperationlog.getOperationType().equals("1")) {
                    wallet.moveTo(script, userId, qty, "充值审核", true);
                }else {
                    wallet.returnTo(script, userId, qty, "扣款审核", true);
                }
                script.exec();
                handle.close();

                // Fvirtualwallet virtualwallet = all.get(0);
                // virtualwallet.setFfrozen(virtualwallet.getFfrozen() + qty);

                Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
                virtualoperationlog.setFstatus(OperationlogEnum.FFROZEN);
                virtualoperationlog.setFcreator(sessionAdmin);
                virtualoperationlog.setFcreateTime(Utils.getTimestamp());
                // 将virtualwallet 赋值为null
                this.virtualOperationLogService.updateVirtualOperationLog(null, virtualoperationlog);
            } else {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "会员钱包有误");
                return modelAndView;
            }
            flag = true;
        } catch (Exception e) {
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

    @RequestMapping("ssadmin/sendVirtualOperationLog")
    public ModelAndView sendVirtualOperationLog() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        boolean flag = false;
        Fvirtualoperationlog virtualoperationlog = this.virtualOperationLogService.findById(fid);

        if (virtualoperationlog.getFstatus() != OperationlogEnum.FFROZEN) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "只会状态为冻结，才允许发放！");
            return modelAndView;
        }

        try {
            double qty = virtualoperationlog.getFqty();
            int coinTypeId = virtualoperationlog.getFvirtualcointype().getFid();
            int userId = virtualoperationlog.getFuser().getFid();
            String sql = "where fvirtualcointype.fid=" + coinTypeId + "and fuser.fid=" + userId;
            List<Fvirtualwallet> all = this.virtualWalletService.list(0, 0, sql, false);
            if (all != null && all.size() == 1) {
                String taskId = virtualoperationlog.getTaskId();
                Mysql handle = new Mysql();
                BatchScript script = new BatchScript(handle);
                WalletAmount wallet = new WalletAmount(handle, userId, coinTypeId, taskId);
                if(virtualoperationlog.getOperationType().equals("1")) {
                    wallet.unlock(script, qty, "充值发放");
                }else{
                    wallet.unlockReturn(script, qty, "扣款完成");
                }
                script.exec();
                handle.close();

                // 更新日志状态
                Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
                virtualoperationlog.setFstatus(OperationlogEnum.AUDIT);
                virtualoperationlog.setFcreator(sessionAdmin);
                virtualoperationlog.setFcreateTime(Utils.getTimestamp());
                // 将virtualwallet 赋值为null
                this.virtualOperationLogService.updateVirtualOperationLog(null, virtualoperationlog);
            } else {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "会员钱包有误");
                return modelAndView;
            }
            flag = true;
        } catch (Exception e) {
            flag = false;
        }

        if (!flag) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "发放失败");
            return modelAndView;
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        if(virtualoperationlog.getOperationType().equals("1")) {
            modelAndView.addObject("message", "发放成功");
        }else{
            modelAndView.addObject("message", "扣款成功");
        }
        return modelAndView;
    }
}
