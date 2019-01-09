package com.huagu.vcoin.main.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huagu.vcoin.main.dao.FusersettingDAO;
import com.huagu.vcoin.main.service.wallet.TradingService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.IdentityTypeEnum;
import com.huagu.vcoin.main.Enum.RegTypeEnum;
import com.huagu.vcoin.main.Enum.UserGradeEnum;
import com.huagu.vcoin.main.Enum.UserStatusEnum;
import com.huagu.vcoin.main.Enum.UserTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.front.FrontUserJsonController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fcapitaloperation;
import com.huagu.vcoin.main.model.Fintrolinfo;
import com.huagu.vcoin.main.model.Fscore;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fusersetting;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.CapitaloperationService;
import com.huagu.vcoin.main.service.admin.ScoreService;
import com.huagu.vcoin.main.service.admin.SystemArgsService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.admin.UsersettingService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.MD5;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.other.utils;
import cn.cerc.security.sapi.JayunSecurity;

@Controller
public class UserController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private FusersettingDAO usersettingDAO;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CapitaloperationService capitaloperationService;
    @Autowired
    private UsersettingService usersettingService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private TradingService tradingService;

    private String msg = "";

    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/userList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userList");
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
        String uid = request.getParameter("uid");
        String fhasImgValidate = request.getParameter("fhasImgValidate");
        String startDate = request.getParameter("startDate");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                if (uid != null && !"".equals(uid)) {
                    int fid = Integer.parseInt(uid);
                    filter.append("and fid =" + fid + " \n");
                }
                filter.append("and (floginName like '%" + keyWord + "%' or \n");
                filter.append("fnickName like '%" + keyWord + "%'  or \n");
                filter.append("fId like '%" + keyWord + "%'  or \n");
                filter.append("frealName like '%" + keyWord + "%'  or \n");
                filter.append("ftelephone like '%" + keyWord + "%'  or \n");
                filter.append("femail like '%" + keyWord + "%' )\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (StringUtils.isNotBlank(fhasImgValidate)) {
            filter.append("and fhasImgValidate = " + fhasImgValidate + " \n");
            modelAndView.add("fhasImgValidate",fhasImgValidate);
        }

        Map<Integer, String> typeMap = new HashMap<Integer, String>();
        typeMap.put(0, "全部");
        typeMap.put(UserStatusEnum.NORMAL_VALUE, UserStatusEnum.getEnumString(UserStatusEnum.NORMAL_VALUE));
        typeMap.put(UserStatusEnum.FORBBIN_VALUE, UserStatusEnum.getEnumString(UserStatusEnum.FORBBIN_VALUE));
        modelAndView.addObject("typeMap", typeMap);

        if (request.getParameter("ftype") != null && request.getParameter("ftype").trim().length() > 0) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and fstatus=" + request.getParameter("ftype") + " \n");
            }
            modelAndView.addObject("ftype", request.getParameter("ftype"));
        }

        try {
            if (request.getParameter("troUid") != null && request.getParameter("troUid").trim().length() > 0) {
                int troUid = Integer.parseInt(request.getParameter("troUid"));
                filter.append("and fIntroUser_id.fid=" + troUid + " \n");
                modelAndView.addObject("troUid", troUid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (request.getParameter("flevel") != null && request.getParameter("flevel").trim().length() > 0) {
                int flevel = Integer.parseInt(request.getParameter("flevel"));
                filter.append("and fscore.flevel=" + flevel + "\n");
                modelAndView.addObject("flevel", flevel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (request.getParameter("troNo") != null && request.getParameter("troNo").trim().length() > 0) {
                String troNo = request.getParameter("troNo").trim();
                filter.append("and fuserNo like '%" + troNo + "%' \n");
                modelAndView.addObject("troNo", troNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (request.getParameter("fisleader") != null && request.getParameter("fisleader").trim().length() > 0) {
                int fisleader = Integer.parseInt(request.getParameter("fisleader"));
                filter.append("and fisleader = " + request.getParameter("fisleader") + " \n");
                modelAndView.addObject("fisleader", fisleader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (request.getParameter("fIsMerchant") != null
                    && request.getParameter("fIsMerchant").trim().length() > 0) {
                int fIsMerchant = Integer.parseInt(request.getParameter("fIsMerchant"));
                filter.append("and fIsMerchant = " + request.getParameter("fIsMerchant") + " \n");
                modelAndView.addObject("fIsMerchant", fIsMerchant);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        List<Fuser> list = this.userService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("userList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "listUser");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fuser", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/viewUser1")
    public ModelAndView viewUser1() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/viewUser1");
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
            filter.append("and (floginName like '%" + keyWord + "%' or \n");
            filter.append("fnickName like '%" + keyWord + "%'  or \n");
            filter.append("frealName like '%" + keyWord + "%'  or \n");
            filter.append("ftelephone like '%" + keyWord + "%'  or \n");
            filter.append("femail like '%" + keyWord + "%'  or \n");
            filter.append("fidentityNo like '%" + keyWord + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }

        Map typeMap = new HashMap();
        typeMap.put(0, "全部");
        typeMap.put(UserStatusEnum.NORMAL_VALUE, UserStatusEnum.getEnumString(UserStatusEnum.NORMAL_VALUE));
        typeMap.put(UserStatusEnum.FORBBIN_VALUE, UserStatusEnum.getEnumString(UserStatusEnum.FORBBIN_VALUE));
        modelAndView.addObject("typeMap", typeMap);

        if (request.getParameter("ftype") != null && request.getParameter("ftype").trim().length() > 0) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and fstatus=" + request.getParameter("ftype") + " \n");
            }
            modelAndView.addObject("ftype", request.getParameter("ftype"));
        }

        try {
            if (request.getParameter("troUid") != null && request.getParameter("troUid").trim().length() > 0) {
                int troUid = Integer.parseInt(request.getParameter("troUid"));
                filter.append("and fIntroUser_id.fid=" + troUid + " \n");
                modelAndView.addObject("troUid", troUid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (request.getParameter("cid") != null) {
            int cid = Integer.parseInt(request.getParameter("cid"));
            Fcapitaloperation c = this.capitaloperationService.findById(cid);
            filter.append("and fid =" + c.getFuser().getFid() + " \n");
            modelAndView.addObject("cid", cid);
        }

        if (orderField != null && orderField.trim().length() > 0) {
            filter.append("order by " + orderField + "\n");
        } else {
            filter.append("order by fregisterTime \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filter.append(orderDirection + "\n");
        } else {
            filter.append("desc \n");
        }

        List<Fuser> list = this.userService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("userList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "viewUser1");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fuser", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/userLookup")
    public ModelAndView userLookup() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userLookup");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");

        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            boolean flag = true;
            int fid = 0;
            try {
                fid = Integer.parseInt(keyWord);
            } catch (Exception e) {
                flag = false;
            }
            filter.append("and(");
            if (flag) {
                filter.append("fid =" + fid + " or \n");
            }
            filter.append("floginName like '%" + keyWord + "%' or \n");
            filter.append("fnickName like '%" + keyWord + "%'  or \n");
            filter.append("frealName like '%" + keyWord + "%'  or \n");
            filter.append("ftelephone like '%" + keyWord + "%'  or \n");
            filter.append("femail like '%" + keyWord + "%'  or \n");
            filter.append("fidentityNo like '%" + keyWord + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }
        filter.append("order by fId desc \n");

        List<Fuser> list = this.userService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("userList1", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "operationLogList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fuser", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/userAuditList")
    public ModelAndView userAuditList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userAuditList");
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
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("and fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (floginName like '%" + keyWord + "%' or \n");
                filter.append("fnickName like '%" + keyWord + "%'  or \n");
                filter.append("frealName like '%" + keyWord + "%'  or \n");
                filter.append("ftelephone like '%" + keyWord + "%'  or \n");
                filter.append("femail like '%" + keyWord + "%'  or \n");
                filter.append("fidentityNo like '%" + keyWord + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }
        filter.append("and fpostBankValidate=1 and fhasBankValidate=0 \n");

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
        List<Fuser> list = this.userService.listUserForAudit((currentPage - 1) * numPerPage, numPerPage, filter + "",
                true);
        modelAndView.addObject("userList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "listUser");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fuser", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/userAuditList1")
    public ModelAndView userAuditList1() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userAuditList1");
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
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("and fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (floginName like '%" + keyWord + "%' or \n");
                filter.append("fnickName like '%" + keyWord + "%'  or \n");
                filter.append("frealName like '%" + keyWord + "%'  or \n");
                filter.append("ftelephone like '%" + keyWord + "%'  or \n");
                filter.append("femail like '%" + keyWord + "%'  or \n");
                filter.append("fidentityNo like '%" + keyWord + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }
        int faudit = 2;
        try {
            if (request.getParameter("faudit") != null
                    && request.getParameter("faudit").trim().length() > 0) {
                faudit = Integer.parseInt(request.getParameter("faudit"));
                if(faudit == 2){
                    filter.append("and fpostImgValidate=1 and fhasImgValidate=0 and faudit="+ faudit +" \n");
                }else if(3 == Integer.parseInt(request.getParameter("faudit"))){

                }else {
                    filter.append(" and faudit="+ faudit +" \n");
                }
                modelAndView.addObject("faudit", faudit);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        List<Fuser> list = this.userService.listUserForAudit((currentPage - 1) * numPerPage, numPerPage, filter + "",
                true);
        modelAndView.addObject("userList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "listUser");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fuser", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/userAuditList2")
    public ModelAndView userAuditList2() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userAuditList2");
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
                filter.append("and fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (floginName like '%" + keyWord + "%' or \n");
                filter.append("fnickName like '%" + keyWord + "%'  or \n");
                filter.append("frealName like '%" + keyWord + "%'  or \n");
                filter.append("ftelephone like '%" + keyWord + "%'  or \n");
                filter.append("femail like '%" + keyWord + "%'  or \n");
                filter.append("fidentityNo like '%" + keyWord + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }
        filter.append("and fhasVideoValidate=0 \n");

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
        List<Fuser> list = this.userService.listUserForAudit((currentPage - 1) * numPerPage, numPerPage, filter + "",
                true);
        modelAndView.addObject("userList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "listUser");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fuser", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/ajaxDone")
    public ModelAndView ajaxDone() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/userForbbin")
    public ModelAndView userForbbin() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        // modelAndView.setViewName("redirect:/pages/ssadmin/comm/ajaxDone.jsp")
        // ;
        int fid = Integer.parseInt(request.getParameter("uid"));
        int status = Integer.parseInt(request.getParameter("status"));
        Fuser user = this.userService.findById(fid);
        if (status == 1) {
            if (user.getFstatus() == UserStatusEnum.FORBBIN_VALUE) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "会员已禁用，无需做此操作");
                return modelAndView;
            }
            modelAndView.addObject("statusCode", 200);
            modelAndView.addObject("message", "禁用成功");
            user.setFstatus(UserStatusEnum.FORBBIN_VALUE);
        } else if (status == 2) {
            if (user.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "会员状态为正常，无需做此操作");
                return modelAndView;
            }
            modelAndView.addObject("statusCode", 200);
            modelAndView.addObject("message", "解除禁用成功");
            user.setFstatus(UserStatusEnum.NORMAL_VALUE);
        } else if (status == 3) {// 重设登录密码
            modelAndView.addObject("statusCode", 200);
            modelAndView.addObject("message", "重设登录密码成功，密码为:ABC123");
            String salt = user.getSalt();
            if (null == salt) {
                user.setFloginPassword(MD5.get("ABC123"));
            } else {
                user.setFloginPassword(Utils.MD5("ABC123", user.getSalt()));
            }

        } else if (status == 4) {// 重设交易密码
            modelAndView.addObject("statusCode", 200);
            modelAndView.addObject("message", "会员交易密码重置成功");
            user.setFtradePassword(null);
        }

        this.userService.updateObj(user);
        return modelAndView;
    }

    @RequestMapping("/ssadmin/userOTCAuditList")
    public ModelAndView userOTCAuditList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userOTCAuditList");
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
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("and fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (floginName like '%" + keyWord + "%' or \n");
                filter.append("fnickName like '%" + keyWord + "%'  or \n");
                filter.append("frealName like '%" + keyWord + "%'  or \n");
                filter.append("ftelephone like '%" + keyWord + "%'  or \n");
                filter.append("femail like '%" + keyWord + "%'  or \n");
                filter.append("fidentityNo like '%" + keyWord + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }
        filter.append("and fSellerStatus=1 \n");

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
        List<Fuser> list = this.userService.listUserForAudit((currentPage - 1) * numPerPage, numPerPage, filter + "",
                true);
        modelAndView.addObject("userList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "listUser");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fuser", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/auditUser")
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public ModelAndView auditUser() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int status = Integer.parseInt(request.getParameter("status"));
        int fid = Integer.parseInt(request.getParameter("uid"));
        String type = request.getParameter("type");

        Fuser user = this.userService.findById(fid);
        if (status == 1) {
            // user.setFhasImgValidateTime(Utils.getTimestamp());
            if(!type.toLowerCase().equals("otc")) {
                user.setFhasBankValidate(true);
                user.setFaudit(0);
            }else{
                user.setfSellerStatus(2);
                user.setfIsMerchant(user.getfSellerLevel());
            }
            modelAndView.addObject("message", "审核成功");
        } else {
            if(!type.toLowerCase().equals("otc")) {
                user.setFhasImgValidateTime(null);
                /*
                 * user.setFhasImgValidate(false); user.setFpostImgValidate(false);
                 * user.setFpostImgValidateTime(null); user.setfIdentityPath(null);
                 * user.setfIdentityPath2(null); user.setfIdentityPath3(null);
                 */
                user.setFhasBankValidate(false);
                user.setfpostBankValidate(false);
                user.setfBankPath(null);
                user.setfBankPath2(null);
                user.setfBankPath3(null);
                user.setFaudit(2);
                user.setAuthGrade(2);
            }else{
                user.setfSellerStatus(3);
                if(user.getfSellerLevel() != 1) {
                    // TODO RYB对应的ID 应改为实时查询得到
                    String rybType = String.valueOf(6);
                    String uid = String.valueOf(fid);
                    double rybCoin = tradingService.getCoin(uid,rybType);
                    if (user.getfSellerLevel() == 2) {
                        rybCoin = rybCoin + 100;
                    }
                    else if(user.getfSellerLevel() == 3) {
                        rybCoin = rybCoin + 300;
                    }
                    //回退RYB
                    tradingService.updateCoin(uid,rybType,String.valueOf(rybCoin));
                }
            }
            modelAndView.addObject("message", "提交成功");

        }
        try {
            this.userService.updateObj(user);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "网络异常");
            return modelAndView;
        }

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");


        return modelAndView;
    }

    @RequestMapping("/ssadmin/auditUser1")
    public ModelAndView auditUser1() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int status = Integer.parseInt(request.getParameter("status"));
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fuser user = this.userService.findById(fid);
        Fscore fscore = user.getFscore();
        Fuser fintrolUser = null;
        Fintrolinfo introlInfo = null;
        Fvirtualwallet fvirtualwallet = null;
        if (status == 1) {
            String[] auditSendCoin = this.systemArgsService.getValue("auditSendCoin").split("#");
            int coinID = Integer.parseInt(auditSendCoin[0]);
            double coinQty = Double.valueOf(auditSendCoin[1]);
            // if (!user.getFhasRealValidate()) {
            if (user.getfIntroUser_id() != null) {
                fintrolUser = this.userService.findById(user.getfIntroUser_id().getFid());
                fintrolUser.setfInvalidateIntroCount(fintrolUser.getfInvalidateIntroCount() + 1);
                // }
                if (coinQty > 0) {
                    fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fintrolUser.getFid(), coinID);
                    fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + coinQty);
                    String taskId = utils.newGuid();
                    introlInfo = new Fintrolinfo();
                    introlInfo.setFcreatetime(Utils.getTimestamp());
                    introlInfo.setFiscny(false);
                    introlInfo.setFqty(coinQty);
                    introlInfo.setFuser(fintrolUser);
                    introlInfo.setFname(fvirtualwallet.getFvirtualcointype().getFname());
                    introlInfo.setTaskId(taskId);
                    introlInfo.setFtitle("会员" + user.getFid() + "实名认证成功，奖励"
                            + fvirtualwallet.getFvirtualcointype().getFname() + coinQty + "个！");
                    try (Mysql handle = new Mysql()) {
                        MyQuery ds = new MyQuery(handle);
                        ds.add("select UID_,userId_,coinId_,total_,frozen_,totalChange_,frozenChange_,changeReason_,changeDate_,guid_,taskId_,entrustId_ from %s",
                                AppDB.t_walletlog);
                        ds.open();
                        ds.append();
                        ds.setField("userId_", fintrolUser.getFid());
                        ds.setField("coinId_", fvirtualwallet.getFvirtualcointype().getFid());
                        ds.setField("total_", fvirtualwallet.getFtotal());
                        ds.setField("frozen_", fvirtualwallet.getFfrozen());
                        ds.setField("totalChange_", coinQty);
                        ds.setField("frozenChange_", 0);
                        ds.setField("changeReason_", "账户注册奖励!");
                        ds.setField("changeDate_", TDateTime.Now());
                        ds.setField("taskId_", taskId);
                        ds.setField("entrustId_", "0");
                        ds.post();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            user.setFhasImgValidateTime(Utils.getTimestamp());
            user.setFhasImgValidate(true);
            user.setfIdentifyReason(null);
            user.setFaudit(1);

//            FrontUserJsonController.authInfo(user.getFloginName(), user.getFrealName(), user.getFtelephone(), "身份证",
//                    user.getFidentityNo(), user.getFpostImgValidateTime().toString(),
//                    user.getFhasImgValidateTime().toString(), user.getfIdentityPath(), user.getfIdentityPath2(),
//                    user.getfIdentityPath3(), "已认证", "");
        } else {
            String identifyReason = request.getParameter("identifyReason");
            user.setfIdentifyReason(identifyReason);
            user.setFhasImgValidateTime(null);
            user.setFhasImgValidate(false);
            user.setFpostImgValidate(false);
            user.setFpostImgValidateTime(null);
            user.setfIdentityPath(null);
            user.setfIdentityPath2(null);
            user.setfIdentityPath3(null);
            user.setFaudit(0);
            user.setAuthGrade(2);
        }
        try {
            // this.userService.updateObj(user);
            this.userService.updateObj(user, fscore, fintrolUser, fvirtualwallet, introlInfo);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "网络异常");
            return modelAndView;
        }

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "审核成功");

        return modelAndView;
    }

    @RequestMapping("/ssadmin/auditUser2")
    public ModelAndView auditUser2() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int status = Integer.parseInt(request.getParameter("status"));
        int fid = Integer.parseInt(request.getParameter("uid"));

        Fuser user = this.userService.findById(fid);
        if (status == 1) {
            user.setFhasVideoValidate(true);
            user.setFhasVideoValidateTime(Utils.getTimestamp());
        } else {
            user.setFhasVideoValidate(false);
            user.setFhasVideoValidateTime(null);
        }
        try {
            this.userService.updateObj(user);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "网络异常");
            return modelAndView;
        }

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "审核成功");

        return modelAndView;
    }

    @RequestMapping("ssadmin/goUserJSP")
    public ModelAndView goUserJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fuser user = this.userService.findById(fid);
            user.setfIdentityPath(Utils.addOssUrl(user.getfIdentityPath()));
            user.setfIdentityPath2(Utils.addOssUrl(user.getfIdentityPath2()));
            user.setfIdentityPath3(Utils.addOssUrl(user.getfIdentityPath3()));
            modelAndView.addObject("fuser", user);
            List<Fusersetting> alls = this.usersettingService.list(0, 0, "where fuser.fid=" + fid, false);
            if (alls == null || alls.size() == 0) {
                Fusersetting fusersetting = new Fusersetting();
                fusersetting.setFisAutoReturnToAccount(false);
                fusersetting.setFuser(user);
                fusersetting.setFticketQty(0d);
                fusersetting.setFsendDate(null);
                fusersetting.setFissend(false);
                usersettingDAO.save(fusersetting);
                alls.add(fusersetting);
            }

            Fusersetting usersetting = alls.get(0);
            modelAndView.addObject("usersetting", usersetting);

            Map<Integer, String> map = new HashMap<Integer, String>();
            map.put(IdentityTypeEnum.SHENFENZHENG, IdentityTypeEnum.getEnumString(IdentityTypeEnum.SHENFENZHENG));
            map.put(IdentityTypeEnum.JUNGUANGZHEN, IdentityTypeEnum.getEnumString(IdentityTypeEnum.JUNGUANGZHEN));
            map.put(IdentityTypeEnum.HUZHAO, IdentityTypeEnum.getEnumString(IdentityTypeEnum.HUZHAO));
            map.put(IdentityTypeEnum.TAIWAN, IdentityTypeEnum.getEnumString(IdentityTypeEnum.TAIWAN));
            map.put(IdentityTypeEnum.GANGAO, IdentityTypeEnum.getEnumString(IdentityTypeEnum.GANGAO));
            modelAndView.addObject("identityTypeMap", map);
        }

        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(UserGradeEnum.LEVEL0, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL0));
        map.put(UserGradeEnum.LEVEL1, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL1));
        map.put(UserGradeEnum.LEVEL2, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL2));
        map.put(UserGradeEnum.LEVEL3, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL3));
        map.put(UserGradeEnum.LEVEL4, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL4));
        map.put(UserGradeEnum.LEVEL5, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL5));
        modelAndView.addObject("typeMap", map);

        Map<Integer, String> userType = new HashMap<Integer, String>();
        for (int i = 0; i <= 4; i++) {
            userType.put(i, UserTypeEnum.getEnumString(i));
        }
        modelAndView.addObject("userType", userType);
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateUserLevel")
    public ModelAndView updateUserLevel() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fuser user = this.userService.findById(fid);
        Fscore score = user.getFscore();
        int newLevel = Integer.parseInt(request.getParameter("newLevel"));
        score.setFlevel(newLevel);
        this.scoreService.updateObj(score);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "修改成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateIntroCount")
    public ModelAndView updateIntroCount(@RequestParam(required = true) int fInvalidateIntroCount) throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fuser user = this.userService.findById(fid);
        user.setfInvalidateIntroCount(fInvalidateIntroCount);
        this.userService.updateObj(user);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "修改成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateUserGrade")
    public ModelAndView updateUserGrade() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fuser user = this.userService.findById(fid);
        Fscore fscore = user.getFscore();
        fscore.setFlevel(Integer.parseInt(request.getParameter("fuserGrade")));
        this.scoreService.updateObj(fscore);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "修改成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateIntroPerson")
    public ModelAndView updateIntroPerson() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fuser user = this.userService.findById(fid);
        String id = request.getParameter("fintrolId");
        String no = request.getParameter("fintrolUserNo");
        if (no != null && no.trim().length() > 0) {
            String fintrolUserNo = no.trim();
            int c = this.adminService.getAllCount("Fuser", "where fuserNo='" + fintrolUserNo + "'");
            if (c == 0) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "服务中心号不存在");
                return modelAndView;
            }
            user.setFintrolUserNo(fintrolUserNo);
        } else {
            user.setFintrolUserNo(null);
        }

        if (id != null && id.trim().length() > 0) {
            int fintrolId = Integer.parseInt(id.trim());
            Fuser fintrolUser = this.userService.findById(fintrolId);
            if (fintrolUser == null) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "用户不存在");
                return modelAndView;
            }
            user.setfIntroUser_id(fintrolUser);
        } else {
            user.setfIntroUser_id(null);
        }

        this.userService.updateObj(user);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "修改成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateUserScore")
    public ModelAndView updateUserScore() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fusersetting usersetting = this.usersettingService.findById(fid);
        usersetting.setFscore(Double.valueOf(request.getParameter("fscore")));
        this.usersettingService.updateObj(usersetting);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "修改成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/cancelGoogleCode")
    public ModelAndView cancelGoogleCode() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fuser user = this.userService.findById(fid);
        user.setFgoogleAuthenticator(null);
        user.setFgoogleBind(false);
        user.setFgoogleurl(null);
        user.setFgoogleValidate(false);
        user.setFopenSecondValidate(false);
        user.setFgoogleCheck(false);
        this.userService.updateObj(user);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "重置谷歌认证成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/cancelTel")
    public ModelAndView cancelTel() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fuser user = this.userService.findById(fid);
        user.setFtelephone(null);
        user.setFisTelephoneBind(false);
        user.setFareaCode(null);
        this.userService.updateObj(user);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "重置手机号码成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/setMerchant")
    public ModelAndView setMerchant() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        int fIsMerchant = Integer.parseInt(request.getParameter("fIsMerchant"));
        Fuser user = this.userService.findById(fid);
        if (fIsMerchant != 0) {
            try (Mysql mysql = new Mysql()) {
                if (fIsMerchant == 2) {
                    MyQuery ds = new MyQuery(mysql);
                    ds.add("select fId from %s", "fuser");
                    ds.add("where fIsMerchant = 2");
                    ds.setMaximum(1);
                    ds.open();
                    if (!ds.eof()) {
                        modelAndView.addObject("statusCode", 300);
                        modelAndView.addObject("message", "设置失败，超级商户只能设置一个，请先取消之前的超级商户");
                        return modelAndView;
                    }
                }
                MyQuery ds = new MyQuery(mysql);
                ds.add("select fId from %s", "fbankinfo_withdraw");
                ds.add("where FUs_fId = %d", fid);
                ds.open();
                if (ds.eof()) {
                    modelAndView.addObject("statusCode", 300);
                    modelAndView.addObject("message", "该用户没有绑定银行卡，设置商户失败");
                    return modelAndView;
                }
            }
        }
        user.setfIsMerchant(fIsMerchant);
        this.userService.updateObj(user);
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "设置成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    private static enum ExportFiled {
        会员UID, 推荐人UID, 会员登录名, 会员状态, 昵称, 真实姓名, 会员等级, 累计推荐注册数, 电话号码, 邮箱地址, 证件类型, 证件号码, 注册时间, 上次登录时间;
    }

    private List<Fuser> getUserList() {
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        String uid = request.getParameter("uid");
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("and fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (floginName like '%" + keyWord + "%' or \n");
                filter.append("fnickName like '%" + keyWord + "%'  or \n");
                filter.append("frealName like '%" + keyWord + "%'  or \n");
                filter.append("ftelephone like '%" + keyWord + "%'  or \n");
                filter.append("femail like '%" + keyWord + "%'  or \n");
                filter.append("fidentityNo like '%" + keyWord + "%' )\n");
            }
        }
        if (uid != null && uid.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(uid);
                filter.append("and fid =" + fid + " \n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (request.getParameter("ftype") != null && request.getParameter("ftype").trim().length() > 0) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and fstatus=" + request.getParameter("ftype") + " \n");
            }
        }

        try {
            if (request.getParameter("troUid") != null && request.getParameter("troUid").trim().length() > 0) {
                int troUid = Integer.parseInt(request.getParameter("troUid"));
                filter.append("and fIntroUser_id.fid=" + troUid + " \n");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        List<Fuser> list = this.userService.list(0, 0, filter + "", false);
        return list;
    }

    @RequestMapping("ssadmin/userExport")
    public ModelAndView userExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=userList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }

        List<Fuser> userList = getUserList();
        for (Fuser user : userList) {
            e.createRow(rowIndex++);
            for (ExportFiled filed : ExportFiled.values()) {
                switch (filed) {
                case 会员UID:
                    e.setCell(filed.ordinal(), user.getFid());
                    break;
                case 推荐人UID:
                    if (user.getfIntroUser_id() != null)
                        e.setCell(filed.ordinal(), user.getfIntroUser_id().getFid());
                    break;
                case 会员登录名:
                    e.setCell(filed.ordinal(), user.getFloginName());
                    break;
                case 会员状态:
                    e.setCell(filed.ordinal(), user.getFstatus_s());
                    break;
                case 昵称:
                    e.setCell(filed.ordinal(), user.getFnickName());
                    break;
                case 真实姓名:
                    e.setCell(filed.ordinal(), user.getFrealName());
                    break;
                case 会员等级:
                    if (user.getFscore() != null)
                        e.setCell(filed.ordinal(), "VIP" + user.getFscore().getFlevel());
                    break;
                case 累计推荐注册数:
                    e.setCell(filed.ordinal(), user.getfInvalidateIntroCount());
                    break;
                case 电话号码:
                    e.setCell(filed.ordinal(), user.getFtelephone());
                    break;
                case 邮箱地址:
                    e.setCell(filed.ordinal(), user.getFemail());
                    break;
                case 证件类型:
                    e.setCell(filed.ordinal(), user.getFidentityType_s());
                    break;
                case 证件号码:
                    e.setCell(filed.ordinal(), user.getFidentityNo());
                    break;
                case 注册时间:
                    e.setCell(filed.ordinal(), user.getFregisterTime());
                    break;
                case 上次登录时间:
                    e.setCell(filed.ordinal(), user.getFlastLoginTime());
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

    @RequestMapping("/ssadmin/setUserNo")
    public ModelAndView setUserNo() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fuser user = this.userService.findById(fid);
        String userNo = request.getParameter("fuserNo");
        if (userNo == null || userNo.trim().length() == 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "服务中心号不能为空");
            return modelAndView;
        } else if (userNo.trim().length() > 100) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "服务中心号长度不能大于100个字符");
            return modelAndView;
        }

        if (user.getFuserNo() != null && user.getFuserNo().trim().length() > 0 && !user.getFuserNo().equals(userNo)) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "该用户已存在服务中心号，不允许修改！");
            return modelAndView;
        }

        String filter = "where fuserNo='" + userNo + "' and fid <>" + user.getFid();
        List<Fuser> fusers = this.userService.list(0, 0, filter, false);
        if (fusers.size() > 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "该服务中心号已存在！");
            return modelAndView;
        }

        user.setFuserNo(userNo);
        user.setFuserType(Integer.parseInt(request.getParameter("fuserType")));
        // user.setfServiceSubRate(request.getParameter("fServiceSubRate"));
        user.setfServiceTradeRate(request.getParameter("fServiceTradeRate"));
        this.userService.updateObj(user);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "设置成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/setProxyNo")
    public ModelAndView setProxyNo() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fuser user = this.userService.findById(fid);
        String fProxyNumber = request.getParameter("fProxyNumber");
        if (fProxyNumber == null || fProxyNumber.trim().length() == 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "省市县号不能为空");
            return modelAndView;
        } else if (fProxyNumber.trim().length() > 100) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "省市县号长度不能大于100个字符");
            return modelAndView;
        }

        if (user.getfProxyNumber() != null && user.getfProxyNumber().trim().length() > 0
                && !user.getfProxyNumber().equals(fProxyNumber)) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "该用户已存在省市县号，不允许修改！");
            return modelAndView;
        }

        String filter = "where fProxyNumber='" + fProxyNumber + "' and fid <>" + user.getFid();
        List<Fuser> fusers = this.userService.list(0, 0, filter, false);
        if (fusers.size() > 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "该省市县号已存在！");
            return modelAndView;
        }

        user.setfProxyNumber(fProxyNumber);
        user.setfProxySubRate(Double.valueOf(request.getParameter("fProxySubRate")));
        user.setfProxyTradeRate(Double.valueOf(request.getParameter("fProxyTradeRate")));
        this.userService.updateObj(user);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message", "设置成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/cancelPhone")
    public ModelAndView cancelPhone() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fuser user = this.userService.findById(fid);
        user.setFtelephone(null);
        user.setFisTelephoneBind(false);
        user.setFisTelValidate(false);
        this.userService.updateObj(user);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "重置手机绑定成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/addUsers")
    public ModelAndView addUsers() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        for (int i = 0; i < 10; i++) {
            Fuser fuser = new Fuser();

            String regName = Utils.getRandomString(10) + "@163.com";
            fuser.setSalt(Utils.getUUID());
            fuser.setFrealName("系统生成");
            fuser.setfIntroUser_id(null);
            fuser.setFregType(RegTypeEnum.EMAIL_VALUE);
            fuser.setFemail(regName);
            fuser.setFisMailValidate(true);
            fuser.setFnickName(regName.split("@")[0]);
            fuser.setFloginName(regName);

            fuser.setFregisterTime(Utils.getTimestamp());
            fuser.setFloginPassword(Utils.MD5("123456abc", fuser.getSalt()));
            fuser.setFtradePassword(null);
            String ip = getIpAddr(request);
            fuser.setFregIp(ip);
            fuser.setFlastLoginIp(ip);
            fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
            fuser.setFlastLoginTime(Utils.getTimestamp());
            fuser.setFlastUpdateTime(Utils.getTimestamp());
            boolean saveFlag = false;
            try {
                saveFlag = this.frontUserService.saveRegister(fuser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/setTiger")
    public ModelAndView setTiger() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fuser user = this.userService.findById(fid);
        if (user.isFistiger()) {
            user.setFistiger(false);
        } else {
            user.setFistiger(true);
        }
        this.userService.updateObj(user);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "设置成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/auditUserALL")
    public ModelAndView auditUserALL() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        String ids = request.getParameter("ids");
        String[] idString = ids.split(",");
        int type = Integer.parseInt(request.getParameter("type"));
        for (int i = 0; i < idString.length; i++) {
            int id = Integer.parseInt(idString[i]);
            Fuser user = this.userService.findById(id);
            Fscore fscore = user.getFscore();
            Fuser fintrolUser = null;
            Fvirtualwallet fvirtualwallet = null;
            String[] auditSendCoin = this.systemArgsService.getValue("auditSendCoin").split("#");
            int coinID = Integer.parseInt(auditSendCoin[0]);
            double coinQty = Double.valueOf(auditSendCoin[1]);
            Fintrolinfo introlInfo = null;
            if (type == 1) {
                if (!user.getFhasRealValidate()) {
                    if (!fscore.isFissend() && user.getfIntroUser_id() != null) {
                        fintrolUser = this.userService.findById(user.getfIntroUser_id().getFid());
                        fintrolUser.setfInvalidateIntroCount(fintrolUser.getfInvalidateIntroCount() + 1);
                        fscore.setFissend(true);
                    }
                    if (coinQty > 0) {
                        fvirtualwallet = this.frontUserService.findVirtualWalletByUser(user.getFid(), coinID);
                        fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + coinQty);
                        introlInfo = new Fintrolinfo();
                        introlInfo.setFcreatetime(Utils.getTimestamp());
                        introlInfo.setFiscny(false);
                        introlInfo.setFqty(coinQty);
                        introlInfo.setFuser(user);
                        introlInfo.setFname(fvirtualwallet.getFvirtualcointype().getFname());
                        introlInfo.setFtitle(
                                "实名认证成功，奖励" + fvirtualwallet.getFvirtualcointype().getFname() + coinQty + "个！");
                    }
                }

                user.setFhasRealValidateTime(Utils.getTimestamp());
                user.setFhasRealValidate(true);
                user.setFisValid(true);
            } else {
                user.setFhasRealValidate(false);
                user.setFpostRealValidate(false);
                user.setFidentityNo(null);
                user.setFrealName(null);
            }
            try {
                this.userService.updateObj(user, fscore, fintrolUser, fvirtualwallet, introlInfo);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "审核成功");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/goAdminDetailJSP")
    public ModelAndView AdminDetail() {
        String url = request.getParameter("url");
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName(url);
        if (request.getParameter("uid") == null || "".equals(request.getParameter("uid"))) {
            log.error("uid is null");

        }
        int fid = Integer.parseInt(request.getParameter("uid"));
        // 会员详情
        try (Mysql mysql = new Mysql()) {
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select fu.fNickName,fu.fTelephone,fu.fsex,fu.fRealName,fu.flastLoginIp,fu.floginName,fu.fEmail,fu.fIdentityPath,fu.fIdentityPath2,fu.fIdentityPath3,fu.fIdentityNo from %s fu",
                    "fuser");
            ds1.add(" where fu.fid = '%s'", fid);
            ds1.open();
            if (!ds1.eof()) {
                Map<String, Object> ma = new HashMap<String, Object>();
                ma.put("fNickName", ds1.getString("fNickName"));
                ma.put("fTelephone", ds1.getString("fTelephone"));
                ma.put("fsex", ds1.getString("fsex"));
                ma.put("floginName", ds1.getString("floginName"));
                ma.put("fRealName", ds1.getString("fRealName"));
                ma.put("flastLoginIp", ds1.getString("flastLoginIp"));
                ma.put("fIdentityPath", ds1.getString("fIdentityPath"));
                ma.put("fIdentityPath2", ds1.getString("fIdentityPath2"));
                ma.put("fIdentityPath3", ds1.getString("fIdentityPath3"));
                ma.put("fIdentityNo", ds1.getString("fIdentityNo"));
                ma.put("fEmail", ds1.getString("fEmail"));
                jspPage.addObject("info", ma);
            }
            // jspPage.addObject("adminDetail", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Mysql mysql1 = new Mysql()) {
            // 查询该会员的提现地址
            MyQuery ds = new MyQuery(mysql1);
            ds.add("select fw.fAdderess,ty1.fShortName,fw.fCreateTime from  %s fw", "fvirtualaddress_withdraw");
            ds.add(" inner join %s fu", "fuser");
            ds.add(" on fw.fuid = fu.fId ");
            ds.add(" LEFT JOIN %s ty1", "fvirtualcointype");
            ds.add(" on fw.fVi_fId = ty1.fId where fu.fid = '%s' order by fw.fCreateTime desc", fid);
            ds.setMaximum(10);
            ds.open();
            List<Map<String, Object>> addresses = new ArrayList<Map<String, Object>>();
            while (ds.fetch()) {
                Map<String, Object> map = new HashMap<>();
                map.put("fAdderess", ds.getString("fAdderess"));
                map.put("fShortName", ds.getString("fShortName"));
                map.put("fCreateTime", ds.getString("fCreateTime"));
                addresses.add(map);
            }
            ds.first();
            // 查询该会员的充值地址
            MyQuery ds1 = new MyQuery(mysql1);
            ds1.add("select fw.fAdderess,ty1.fShortName,fw.fCreateTime from %s fw", "fvirtualaddress");
            ds1.add(" left join %s fu", "fuser");
            ds1.add(" on fw.fuid = fu.fId ");
            ds1.add(" LEFT JOIN %s ty1", "fvirtualcointype");
            ds1.add(" on fw.fVi_fId = ty1.fId where fu.fid = '%s' order by fw.fCreateTime desc", fid);
            ds1.setMaximum(10);
            ds1.open();
            List<Map<String, Object>> alladdree = new ArrayList<Map<String, Object>>();
            while (ds1.fetch()) {
                Map<String, Object> ma = new HashMap<String, Object>();
                ma.put("fAdderess", ds1.getString("fAdderess"));
                ma.put("fShortName", ds1.getString("fShortName"));
                ma.put("fCreateTime", ds1.getString("fCreateTime"));
                alladdree.add(ma);
            }
            ds1.first();
            jspPage.addObject("alladdree", alladdree);
            jspPage.addObject("addresses", addresses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jspPage;

    }

    // 跳转到修改手机号弹窗页面，uri： /ssadmin/auditUserTelPhone
    @RequestMapping("ssadmin/getUserInfo")
    public ModelAndView getUserInfo() throws Exception {
        String url = request.getParameter("url");
        String uid = request.getParameter("uid");
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName(url);
        if (uid != null && !"".equals(uid)) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fuser user = this.userService.findById(fid);
            jspPage.addObject("fuser", user);
        }
        return jspPage;
    }

    // 修改绑定手机号
    @RequestMapping("/ssadmin/auditUserTelPhone")
    public ModelAndView auditUserTelPhone() throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        String uid = request.getParameter("uid");
        String fNewPhone = request.getParameter("fNewPhone");
        if (uid == null || "".equals(uid)) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "用户fid不正确");
            return jspPage;
        }
        if (!fNewPhone.matches(Constant.PhoneReg)) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "手机号格式不正确");
            return jspPage;
        }
        int fid = Integer.parseInt(uid);
        Fuser user = this.userService.findById(fid);
        user.setFtelephone(fNewPhone);
        try {
            this.userService.updateObj(user);
            // 修改绑定手机号成功，则修改聚安用户手机号
            JayunSecurity security = new JayunSecurity(request);
            security.register(user.getFloginName(), fNewPhone);
            msg = security.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "修改绑定手机号失败" + msg);
            return jspPage;
        }

        jspPage.addObject("statusCode", 200);
        jspPage.addObject("callbackType", "closeCurrent");
        jspPage.addObject("message", "修改绑定手机号成功" + msg);
        return jspPage;
    }

    @RequestMapping("ssadmin/toUpdatePrice")
    public ModelAndView getVirtualCapital_list() throws Exception {
        String url = request.getParameter("url");
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName(url);
        String begin = "0";
        String end = "10";
        String value = "";
        Mysql sql = new Mysql();
        MyQuery fvalue = new MyQuery(sql);
        fvalue.add("select * from %s where fId=13020", "fsystemargs");
        fvalue.setMaximum(1);
        fvalue.open();
        if (!fvalue.eof()) {
            value = fvalue.getString("fValue");
            begin = value.split(",")[0];
            end = value.split(",")[1];
        }
        jspPage.addObject("begin", begin);
        jspPage.addObject("end", end);
        return jspPage;
    }

    // 修改提现自动审核起始值
    @RequestMapping("/ssadmin/updatePrice")
    public ModelAndView virtualCapital_list() throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        String begin = request.getParameter("begin");
        String end = request.getParameter("end");
        if (begin == null || "".equals(begin) || end == null || "".equals(end)) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "起始值不正确");
            return jspPage;
        }
        double begin1 = Double.parseDouble(begin);
        double end1 = Double.parseDouble(end);
        if (begin1 >= end1 || begin1 < 0 || end1 < 0) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "起始值不正确");
            return jspPage;
        }
        try {
            Mysql sql = new Mysql();
            MyQuery fvalue = new MyQuery(sql);
            fvalue.add("select * from %s ", "fsystemargs");
            fvalue.add("where fKey='priceRange'");
            fvalue.open();
            if (!fvalue.eof()) {
                fvalue.edit();
                fvalue.setField("fValue", String.format("%s,%s", begin, end));
                fvalue.post();
            }
        } catch (Exception e) {
            e.printStackTrace();
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "修改提现自动审核起始值失败");
            return jspPage;
        }
        jspPage.addObject("statusCode", 200);
        jspPage.addObject("callbackType", "closeCurrent");
        jspPage.addObject("message", "修改提现自动审核起始值成功");
        return jspPage;
    }

    @RequestMapping("/ssadmin/setLeader")
    public ModelAndView setLeader() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        int fisleader = Integer.parseInt(request.getParameter("fisleader"));
        Fuser user = this.userService.findById(fid);
        if (user == null) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "设置失败");
            return modelAndView;
        }
        boolean flag = fisleader == 1 ? true : false;
        try {
            user.setFisleader(flag);
            this.userService.updateObj(user);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "设置失败");
            return modelAndView;
        }

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "设置成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/setSystemPro")
    public ModelAndView setSystemPro() {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        int fCanLoginTeamManager = Integer.parseInt(request.getParameter("fCanLoginTeamManager"));
        Fuser user = this.userService.findById(fid);
        if (user == null) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "设置权限失败");
            return modelAndView;
        }
        boolean flag = fCanLoginTeamManager == 1 ? true : false;
        try {
            user.setfCanLoginTeamManager(flag);
            this.userService.updateObj(user);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "设置权限失败");
            return modelAndView;
        }
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "设置成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }
    
    // 后台调整用户认证级别
    @RequestMapping("/ssadmin/auditUserAuthGrade")
    public ModelAndView auditUserAuthGrade() throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        String uid = request.getParameter("uid");
        int AuthGradeType = Integer.parseInt(request.getParameter("selectRz"));
        if (uid == null || "".equals(uid)) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "用户fid不正确");
            return jspPage;
        }
        int fid = Integer.parseInt(uid);
        Fuser user = this.userService.findById(fid);
        if(AuthGradeType == 1)
        {
        	user.setAuthGrade(1);
        	user.setFpostRealValidate(true);
        	user.setFhasRealValidate(true);
        	user.setFpostImgValidate(false);
        	user.setFhasImgValidate(false);
        	user.setFpostBankValidate(false);
        	user.setFhasBankValidate(false);
        }
        else if(AuthGradeType == 2)
        {
        	user.setAuthGrade(2);
        	user.setFpostRealValidate(true);
        	user.setFhasRealValidate(true);
        	user.setFpostImgValidate(true);
        	user.setFhasImgValidate(true);
        	user.setFpostBankValidate(false);
        	user.setFhasBankValidate(false);
        }
        else if(AuthGradeType == 3)
        {
        	user.setAuthGrade(3);
        	user.setFpostRealValidate(true);
        	user.setFhasRealValidate(true);
        	user.setFpostImgValidate(true);
        	user.setFhasImgValidate(true);
        	user.setFpostBankValidate(true);
        	user.setFhasBankValidate(true);
        }
        else
        {
        	user.setAuthGrade(0);
        	user.setFpostRealValidate(false);
        	user.setFhasRealValidate(false);
        	user.setFpostImgValidate(false);
        	user.setFhasImgValidate(false);
        	user.setFpostBankValidate(false);
        	user.setFhasBankValidate(false);
        }
        try {
            this.userService.updateObj(user);
        } catch (Exception e) {
            e.printStackTrace();
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "用户认证级别修改失败");
            return jspPage;
        }
        jspPage.addObject("statusCode", 200);
        jspPage.addObject("callbackType", "closeCurrent");
        jspPage.addObject("message", "用户认证级别修改成功");
        return jspPage;
    }
}
