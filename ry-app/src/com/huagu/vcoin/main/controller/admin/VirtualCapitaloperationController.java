package com.huagu.vcoin.main.controller.admin;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huagu.vcoin.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.ERCWalletConfig;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.coa.common.tool.TokenClient;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CapitalOperationOutStatus;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationInStatusEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationOutStatusEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.front.FrontUserJsonController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualcaptualoperation;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.VirtualCapitaloperationService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.admin.VirtualWalletService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.sms.ShortMessageService;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;
import cn.cerc.jdb.other.utils;
import net.sf.json.JSONObject;
import site.jayun.vcoin.wallet.BTCUtils;
import site.jayun.vcoin.wallet.OmniUtils;
import site.jayun.vcoin.wallet.WalletConfig;
import site.jayun.vcoin.wallet.WalletFactory;
import site.jayun.vcoin.wallet.WalletUtil;

@Controller
public class VirtualCapitaloperationController extends BaseController {
    private static final Logger log = Logger.getLogger(VirtualCapitaloperationController.class);
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private VirtualCapitaloperationService virtualCapitaloperationService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/virtualCaptualoperationList")
    public ModelAndView Index() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualCaptualoperationList1");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
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
                filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
                filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        filterSQL.append("and fuser.fid is not null \n");

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

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list((currentPage - 1) * numPerPage,
                numPerPage, filterSQL.toString(), true);
        modelAndView.addObject("virtualCapitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitaloperationList");
        // 总数量
        modelAndView.addObject("totalCount",
                this.adminService.getAllCount("Fvirtualcaptualoperation", filterSQL.toString()));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/virtualCapitalInList")
    public ModelAndView virtualCapitalInList() throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/virtualCapitalInList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype=" + VirtualCapitalOperationTypeEnum.COIN_IN + "\n");
        // filterSQL.append(String.format("and (fStatus=%s or fStatus=%s) \n",
        // VirtualCapitalOperationInStatusEnum.WAIT_2,
        // VirtualCapitalOperationInStatusEnum.SUCCESS));
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
                filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
            }
            jspPage.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            jspPage.addObject("ftype", type);
        } else {
            jspPage.addObject("ftype", 0);
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        jspPage.addObject("typeMap", typeMap);

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
        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list((currentPage - 1) * numPerPage,
                numPerPage, filterSQL + "", true);
        jspPage.addObject("virtualCapitaloperationList", list);
        jspPage.addObject("numPerPage", numPerPage);
        jspPage.addObject("currentPage", currentPage);
        jspPage.addObject("rel", "virtualCapitalInList");
        // 总数量
        jspPage.addObject("totalCount", this.adminService.getAllCount("Fvirtualcaptualoperation", filterSQL + ""));
        return jspPage;
    }

    @RequestMapping("/ssadmin/virtualCapitalOutList")
    public ModelAndView virtualCapitalOutList() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualCapitalOutList");
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype=" + VirtualCapitalOperationTypeEnum.COIN_OUT + "\n");

        if(request.getParameter("fStatus") != null && request.getParameter("fStatus").trim().length() > 0){
            int fStatus = Integer.parseInt(request.getParameter("fStatus"));
            if(fStatus == 0){
                filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
                        + VirtualCapitalOperationOutStatusEnum.OperationLock + ","
                        + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ","
                        + VirtualCapitalOperationOutStatusEnum.Operationing + ","
                        + VirtualCapitalOperationOutStatusEnum.Cancel + ") \n");
            }else if(fStatus == 1){
                filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.WaitForOperation + ") \n");
            }else if(fStatus == 2){
                filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.OperationLock + ") \n");
            }else if(fStatus == 3){
                filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ") \n");
            }else if(fStatus == 4){
                filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.Operationing + ") \n");
            }else if(fStatus == 5){
                filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.Cancel + ") \n");
            }else{
                filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
                        + VirtualCapitalOperationOutStatusEnum.OperationLock + ","
                        + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ","
                        + VirtualCapitalOperationOutStatusEnum.Operationing + ","
                        + VirtualCapitalOperationOutStatusEnum.Cancel + ") \n");
            }
            modelAndView.addObject("fStatus", fStatus);
        }

        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append(" and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
                filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "desc \n");
        }else{
            filterSQL.append("order by fcreateTime desc \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list((currentPage - 1) * numPerPage,
                numPerPage, filterSQL + "", true);
        modelAndView.addObject("virtualCapitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalOutList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fvirtualcaptualoperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/virtualFtransformList")
    public ModelAndView virtualFtransformList() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualFtransformList");
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and a.fStatus = 0 \n");
        // filterSQL.append("and fstatus IN(" +
        // VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
        // + VirtualCapitalOperationOutStatusEnum.OperationLock + ")\n");
        // filterSQL.append("and a.fStatus = 0 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append(" and b.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (b.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("b.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("b.frealName like '%" + keyWord + "%' ) \n");
                // filterSQL.append("withdraw_virtual_address like '%" + keyWord
                // + "%' OR \n");
                // filterSQL.append("recharge_virtual_address like '%" + keyWord
                // + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and c.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        // List<Fvirtualcaptualoperation> list =
        // this.virtualCapitaloperationService.list((currentPage - 1) *
        // numPerPage,
        // numPerPage, filterSQL + "", true);
        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
        ds.add("from %s a left join %s b ", "ftransform", "fuser");
        ds.add("on a.fUs_fId2 = b.fId ");
        ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
        // ds.add("where a.fStatus = 0 ");// 0 未审核
        ds.add(filterSQL.toString());
        ds.add(" order by a.fCreateTime desc ");
        ds.setOffset((currentPage - 1) * numPerPage);
        ds.setMaximum(numPerPage);
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        MyQuery ds1 = new MyQuery(mysql);
        ds1.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
        ds1.add("from %s a left join %s b ", "ftransform", "fuser");
        ds1.add("on a.fUs_fId2 = b.fId ");
        ds1.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
        // ds.add("where a.fStatus = 0 ");// 0 未审核
        ds1.add(filterSQL.toString());
        ds1.add(" order by a.fCreateTime desc ");
        ds1.open();

        modelAndView.addObject("virtualFtransformList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualFtransformList");
        // 总数量
        modelAndView.addObject("totalCount", ds1.size());
        return modelAndView;
    }

    @RequestMapping("/ssadmin/virtualCapitalOutSucList")
    public ModelAndView virtualCapitalOutSucList() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualCapitalOutSucList");
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
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype=" + VirtualCapitalOperationTypeEnum.COIN_OUT + "\n");
        filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ")\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
                filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list((currentPage - 1) * numPerPage,
                numPerPage, filterSQL + "", true);
        modelAndView.addObject("virtualCapitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalOutSucList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fvirtualcaptualoperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/virtualTransferList")
    public ModelAndView virtualTransferList() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualTransferList");
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and a.fStatus = 0 \n");
        // filterSQL.append("and fstatus IN(" +
        // VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
        // + VirtualCapitalOperationOutStatusEnum.OperationLock + ")\n");
        // filterSQL.append("and a.fStatus = 0 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append(" and b.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (b.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("b.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("b.frealName like '%" + keyWord + "%' ) \n");
                // filterSQL.append("withdraw_virtual_address like '%" + keyWord
                // + "%' OR \n");
                // filterSQL.append("recharge_virtual_address like '%" + keyWord
                // + "%' )\n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and c.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        // List<Fvirtualcaptualoperation> list =
        // this.virtualCapitaloperationService.list((currentPage - 1) *
        // numPerPage,
        // numPerPage, filterSQL + "", true);
        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
        ds.add("from %s a left join %s b ", "ftransfer", "fuser");
        ds.add("on a.fUs_fId2 = b.fId ");
        ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
        // ds.add("where a.fStatus = 0 ");// 0 未审核
        ds.add(filterSQL.toString());
        ds.add(" order by a.fCreateTime desc ");
        ds.setOffset((currentPage - 1) * numPerPage);
        ds.setMaximum(numPerPage);
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        MyQuery ds1 = new MyQuery(mysql);
        ds1.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
        ds1.add("from %s a left join %s b ", "ftransfer", "fuser");
        ds1.add("on a.fUs_fId2 = b.fId ");
        ds1.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
        // ds.add("where a.fStatus = 0 ");// 0 未审核
        ds1.add(filterSQL.toString());
        ds1.add(" order by a.fCreateTime desc ");
        ds1.open();

        modelAndView.addObject("ftransferList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalOutList");
        // 总数量
        modelAndView.addObject("totalCount", ds1.size());
        return modelAndView;
    }

    @RequestMapping("/ssadmin/virtualTransferSucList")
    public ModelAndView virtualTransferSucList() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualTransferSucList");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        // 当前页
        int currentPage = 1;
        int total = 0;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and a.fStatus <> 0 \n");
        /*
         * filterSQL.append("where 1=1 and ftype=" +
         * VirtualCapitalOperationTypeEnum.COIN_OUT + "\n"); filterSQL.append(
         * "and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess +
         * ")\n");
         */
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and b.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (b.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("b.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("b.frealName like '%" + keyWord + "%' ) \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and c.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
            ds.add("from %s a left join %s b ", "ftransfer", "fuser");
            ds.add("on a.fUs_fId2 = b.fId ");
            ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
            // ds.add("where a.fStatus = 1 ");// 1 划转成功
            ds.add(filterSQL.toString());
            ds.add("order by a.fCreateTime desc ");
            ds.setOffset((currentPage - 1) * numPerPage);
            ds.setMaximum(numPerPage);
            ds.open();
            if (!ds.eof()) {
                for (Record record : ds) {
                    list.add(record.getItems());
                }
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
            ds1.add("from %s a left join %s b ", "ftransfer", "fuser");
            ds1.add("on a.fUs_fId2 = b.fId ");
            ds1.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
            // ds.add("where a.fStatus = 1 ");// 1 划转成功
            ds1.add(filterSQL.toString());
            ds1.add("order by a.fCreateTime desc ");
            ds1.open();
            total = ds.size();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelAndView.addObject("ftransferList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalOutSucList");
        // 总数量
        modelAndView.addObject("totalCount", total);
        return modelAndView;
    }

    // 划转记录
    @RequestMapping("ssadmin/goTransferRecord")
    public ModelAndView goTransferRecord(@RequestParam(required = true) int uid) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/transferRecord");
        // String ftype = request.getParameter("ftype");
        List<Map<String, Object>> list = new ArrayList<>();
        int total = 0;
        // 当前页
        int currentPage = 1;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select c.finput_moneytype,c.finput_coins,c.foutput_msg,c.foutput_code,c.fcreatetime,c.finput_username,b.fid uid,b.floginName,b.fnickName,b.frealName from %s a ",
                    "ftransfer");
            ds.add("left join %s b on a.fUs_fId2 = b.fId  ", AppDB.Fuser);
            ds.add("left join %s c on b.floginName = c.fuserid ", "ftransfertoyylog");
            ds.add("where a.fId = %d ", uid);
            ds.setOffset((currentPage - 1) * numPerPage);
            ds.setMaximum(numPerPage);
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select c.finput_moneytype,c.finput_coins,c.foutput_msg,c.foutput_code,c.fcreatetime,c.finput_username,b.fid uid,b.floginName,b.fnickName,b.frealName from %s a ",
                    "ftransfer");
            ds1.add("left join %s b on a.fUs_fId2 = b.fId  ", AppDB.Fuser);
            ds1.add("left join %s c on b.floginName = c.fuserid ", "ftransfertoyylog");
            ds1.add("where a.fId = %d ", uid);
            ds1.add("order by c.fcreatetime desc");
            ds1.open();
            total = ds1.size();
        }
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalCount", total);
        modelAndView.addObject("ftransferList", list);
        return modelAndView;
    }

    // 划转记录
    @RequestMapping("ssadmin/goTransformRecord")
    public ModelAndView goTransformRecord(@RequestParam(required = true) int uid) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/transformRecord");
        // String ftype = request.getParameter("ftype");
        List<Map<String, Object>> list = new ArrayList<>();
        int total = 0;
        // 当前页
        int currentPage = 1;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select c.finput_moneytype,c.finput_coins,c.foutput_msg,c.foutput_code,c.fcreatetime,c.finput_username,b.fid uid,b.floginName,b.fnickName,b.frealName from %s a ",
                    "ftransform");
            ds.add("left join %s b on a.fUs_fId2 = b.fId  ", AppDB.Fuser);
            ds.add("left join %s c on b.floginName = c.fuserid ", "ftransformlog");
            ds.add("where a.fId = %d ", uid);
            ds.setOffset((currentPage - 1) * numPerPage);
            ds.setMaximum(numPerPage);
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select c.finput_moneytype,c.finput_coins,c.foutput_msg,c.foutput_code,c.fcreatetime,c.finput_username,b.fid uid,b.floginName,b.fnickName,b.frealName from %s a ",
                    "ftransform");
            ds1.add("left join %s b on a.fUs_fId2 = b.fId  ", AppDB.Fuser);
            ds1.add("left join %s c on b.floginName = c.fuserid ", "ftransformlog");
            ds1.add("where a.fId = %d ", uid);
            ds1.add("order by c.fcreatetime desc");
            ds1.open();
            total = ds1.size();
        }
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalCount", total);
        modelAndView.addObject("ftransferList", list);
        return modelAndView;
    }

    // 取消划转
    @RequestMapping("/ssadmin/goTransferChangeStatus")
    public ModelAndView goTransferChangeStatus(@RequestParam(required = true) String uid) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        if (uid == "" || uid == null) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "请选择信息!");
            return modelAndView;
        } else {
            String[] uids = uid.split(",");
            try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
                int success = 0;
                for (int i = 0; i < uids.length; i++) {
                    int uid_ = Integer.parseInt(uids[i]);
                    MyQuery ds = new MyQuery(mysql);
                    ds.add("select fId,fUs_fId2,fAmount,fVi_fId2,fStatus,flastUpdateTime from %s ", "ftransfer");
                    ds.add("where fId='%d' ", uid_);
                    ds.add("and fStatus = 0 ");// 0 未审核
                    ds.open();
                    double fAmount = ds.getDouble("fAmount");
                    ds.edit();
                    ds.setField("fStatus", 2);
                    ds.setField("flastUpdateTime", TDateTime.Now());
                    ds.post();
                    if (!ds.eof()) {
                        MyQuery ds2 = new MyQuery(mysql);
                        ds2.add("select * from %s ", "fvirtualwallet");
                        ds2.add(" where fuid = '%s'", ds.getInt("fUs_fId2"));
                        ds2.add(" and fVi_fId='%d' ", ds.getInt("fVi_fId2"));
                        ds2.open();
                        if (!ds2.eof()) {
                            double fFrozen = ds2.getDouble("fFrozen");
                            double fTotal = ds2.getDouble("fTotal");

                            MyQuery ds3 = new MyQuery(mysql);
                            ds3.add("select fId,fFrozen,fTotal,flastUpdateTime from %s ", "fvirtualwallet");
                            ds3.add("where fId = '%s'", ds2.getInt("fId"));
                            ds3.open();
                            ds3.edit();
                            ds3.setField("fFrozen", fFrozen - fAmount);
                            ds3.setField("fTotal", fTotal + fAmount);
                            ds3.setField("flastUpdateTime", TDateTime.Now());
                            ds3.post();
                        }
                        success++;
                    } else {
                        modelAndView.addObject("statusCode", 300);
                        modelAndView.addObject("message", "划转失败，请刷新列表后重试！");
                    }
                }
                tx.commit();
                modelAndView.addObject("statusCode", 200);
                modelAndView.addObject("message", "划转取消成功，划转取消成功" + success + "个！");
            }
            return modelAndView;
        }
    }

    // 取消划转
    @RequestMapping("/ssadmin/goTransformChangeStatus")
    public ModelAndView goTransformChangeStatus(@RequestParam(required = true) String uid) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        if (uid == "" || uid == null) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "请选择信息!");
            return modelAndView;
        } else {
            String[] uids = uid.split(",");
            try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
                int success = 0;
                for (int i = 0; i < uids.length; i++) {
                    int uid_ = Integer.parseInt(uids[i]);
                    MyQuery ds = new MyQuery(mysql);
                    ds.add("select fId,fUs_fId2,fAmount,fVi_fId2,fStatus,flastUpdateTime from %s ", "ftransform");
                    ds.add("where fId='%d' ", uid_);
                    ds.add("and fStatus = 0 ");// 0 未审核
                    ds.open();
                    double fAmount = ds.getDouble("fAmount");
                    ds.edit();
                    ds.setField("fStatus", 2);
                    ds.setField("flastUpdateTime", TDateTime.Now());
                    ds.post();
                    if (!ds.eof()) {
                        MyQuery ds2 = new MyQuery(mysql);
                        ds2.add("select * from %s ", "fvirtualwallet");
                        ds2.add(" where fuid = '%s'", ds.getInt("fUs_fId2"));
                        ds2.add(" and fVi_fId='%d' ", ds.getInt("fVi_fId2"));
                        ds2.open();
                        if (!ds2.eof()) {
                            double fFrozen = ds2.getDouble("fFrozen");
                            double fTotal = ds2.getDouble("fTotal");

                            MyQuery ds3 = new MyQuery(mysql);
                            ds3.add("select fId,fFrozen,fTotal,flastUpdateTime from %s ", "fvirtualwallet");
                            ds3.add("where fId = '%s'", ds2.getInt("fId"));
                            ds3.open();
                            ds3.edit();
                            ds3.setField("fFrozen", fFrozen - fAmount);
                            ds3.setField("fTotal", fTotal + fAmount);
                            ds3.setField("flastUpdateTime", TDateTime.Now());
                            ds3.post();
                        }
                        success++;
                    } else {
                        modelAndView.addObject("statusCode", 300);
                        modelAndView.addObject("message", "划转失败，请刷新列表后重试！");
                    }
                }
                tx.commit();
                modelAndView.addObject("statusCode", 200);
                modelAndView.addObject("message", "划转取消成功，划转取消成功" + success + "个！");
            }
            return modelAndView;
        }
    }

    @RequestMapping("ssadmin/goVirtualCapitaloperationChangeStatus")
    public ModelAndView goVirtualCapitaloperationChangeStatus(@RequestParam(required = true) int type,
            @RequestParam(required = true) int uid) throws Exception {

        JspPage modelAndView = new JspPage(request);
        Fvirtualcaptualoperation fvirtualcaptualoperation = this.virtualCapitaloperationService.findById(uid);
        fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());

        int userId = fvirtualcaptualoperation.getFuser().getFid();
        Fvirtualcointype fvirtualcointype = fvirtualcaptualoperation.getFvirtualcointype();
        int coinTypeId = fvirtualcointype.getFid();
        List<Fvirtualwallet> virtualWallet = this.virtualWalletService.findByTwoProperty("fuser.fid", userId,
                "fvirtualcointype.fid", coinTypeId);
        if (virtualWallet == null || virtualWallet.size() == 0) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "审核失败，会员虚拟币钱包信息异常!");
            return modelAndView;
        }
        Fvirtualwallet virtualWalletInfo = virtualWallet.get(0);

        int status = fvirtualcaptualoperation.getFstatus();
        String tips = "";
        switch (type) {
        case 1:
            tips = "锁定";
            if (status != CapitalOperationOutStatus.WaitForOperation) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation);
                modelAndView.addObject("message", "锁定失败,只有状态为:‘" + status_s + "’的充值记录才允许锁定!");
                return modelAndView;
            }
            fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationLock);
            break;
        case 2:
            tips = "取消锁定";
            if (status != CapitalOperationOutStatus.OperationLock) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationLock);
                modelAndView.addObject("message", "取消锁定失败,只有状态为:‘" + status_s + "’的充值记录才允许取消锁定!");
                return modelAndView;
            }
            fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation);
            break;
        case 3:
            tips = "取消提现";
            if (status == CapitalOperationOutStatus.Cancel) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "取消提现失败,该记录已处于取消状态!");
                return modelAndView;
            }
            double fee = fvirtualcaptualoperation.getFfees();
            double famount = fvirtualcaptualoperation.getFamount();
            fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.Cancel);
            virtualWalletInfo.setFfrozen(virtualWalletInfo.getFfrozen() - fee - famount);
            virtualWalletInfo.setFtotal(virtualWalletInfo.getFtotal() + fee + famount);
            virtualWalletInfo.setFlastUpdateTime(Utils.getTimestamp());
            break;
        }

        boolean flag = false;
        try {
            this.virtualCapitaloperationService.updateObj(fvirtualcaptualoperation);
            this.virtualWalletService.updateObj(virtualWalletInfo);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (flag) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 200);
            modelAndView.addObject("message", tips + "成功！");
        } else {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "未知错误，请刷新列表后重试！");
        }

        return modelAndView;
    }

    @RequestMapping("ssadmin/goVirtualCapitaloperationJSP")
    public ModelAndView goVirtualCapitaloperationJSP() throws Exception {
        /*
         * href="ssadmin/goVirtualCapitaloperationJSP.html? uid={sid_user}&amp;
         * url=ssadmin/viewVirtualCaptual
         */
        String url = request.getParameter("url");
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName(url);
        WalletConfig config = new WalletConfig();
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService.findById(fid);
            config.setAccessKey(virtualcaptualoperation.getFvirtualcointype().getFaccess_key());
            config.setIP(virtualcaptualoperation.getFvirtualcointype().getFip());
            config.setPort(virtualcaptualoperation.getFvirtualcointype().getFport());
            config.setSecretKey(virtualcaptualoperation.getFvirtualcointype().getFsecrt_key());
            jspPage.addObject("virtualCapitaloperation", virtualcaptualoperation);
        }
        return jspPage;
    }

    @RequestMapping("ssadmin/goTransferJSP")
    public ModelAndView goTransferJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName(url);
        WalletConfig config = new WalletConfig();
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            List<Map<String, Object>> list = new ArrayList<>();
            Mysql mysql = new Mysql();
            MyQuery ds = new MyQuery(mysql);
            ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fId cFid,c.fname");
            ds.add("from %s a left join %s b ", "ftransfer", "fuser");
            ds.add("on a.fUs_fId2 = b.fId ");
            ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
            ds.add("where a.fStatus = 0 and a.fId=%d", fid);// 0 未审核
            ds.open();
            ds.setMaximum(1);
            if (!ds.eof()) {
                for (Record record : ds) {
                    list.add(record.getItems());
                }
            }
            jspPage.addObject("ftransfer", list.get(0));
        }
        return jspPage;
    }

    /**
     * 虚拟币审核提现
     */
    @RequestMapping("ssadmin/virtualCapitalOutAudit")
    public ModelAndView virtualCapitalOutAudit() throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));

        Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService.findById(fid);
        int status = virtualcaptualoperation.getFstatus();
        if (status != VirtualCapitalOperationOutStatusEnum.OperationLock) {
            jspPage.addObject("statusCode", 300);
            String status_s = VirtualCapitalOperationOutStatusEnum
                    .getEnumString(VirtualCapitalOperationOutStatusEnum.OperationLock);
            jspPage.addObject("message", "审核失败,只有状态为:" + status_s + "的提现记录才允许审核!");
            return jspPage;
        }

        // 根据用户查钱包最后修改时间
        int userId = virtualcaptualoperation.getFuser().getFid();
        Fvirtualcointype fvirtualcointype = virtualcaptualoperation.getFvirtualcointype();
        DeCodeUtil.deCode(fvirtualcointype);
        int coinId = fvirtualcointype.getFid();

        Fvirtualwallet virtualWalletInfo = frontUserService.findVirtualWalletByUser(userId, coinId);
        // 提现数量+手续数量
        double amount = Utils.getDouble(virtualcaptualoperation.getFamount() + virtualcaptualoperation.getFfees(), 4);
        // 提现数量
        double amountonly = Utils.getDouble(virtualcaptualoperation.getFamount(), 4);

        // 冻结数量
        double frozenRmb = Utils.getDouble(virtualWalletInfo.getFfrozen(), 4);
        if (frozenRmb - amount < -0.0001) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "审核失败,冻结数量:" + frozenRmb + "小于提现数量:" + amount + "，操作异常!");
            return jspPage;
        }

        // 自动发送
        String user_address = virtualcaptualoperation.getWithdraw_virtual_address();
        String trade_number;

        // 初始化钱包配置
        WalletConfig config = initWalletConfig(fvirtualcointype);

        if (virtualcaptualoperation.getFtradeUniqueNumber() != null
                && virtualcaptualoperation.getFtradeUniqueNumber().trim().length() > 0) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "非法操作！请检查钱包！");
            return jspPage;
        }

        try (Mysql handle = new Mysql()) {
            String coinType = fvirtualcointype.getfShortName();
            WalletUtil util = WalletFactory.build(config, fvirtualcointype.isFisEth() ? "ETH" : "BTC");
            String mainAddress = config.getMainAddress();
            if ("USDT".equals(coinType)) {
                util = new OmniUtils(config);
            }

            // 检查余额
            if(fvirtualcointype.isFisToken() == true){
                // double balance = virtualWalletInfo.getFfrozen();//2.0代币余额
            	String ipandport = "http://" + fvirtualcointype.getFip() + ":" + fvirtualcointype.getFport() + "/";
        		Web3j web3j = Web3j.build(new HttpService(ipandport));
        		String faddress =fvirtualcointype.getFaddress();
        		String balance = TokenClient.getTokenBalance(web3j, mainAddress, faddress).toString();
            	  if (Double.valueOf(balance) < amount) {
                      jspPage.addObject("statusCode", 300);
                    jspPage.addObject("message", "审核失败，钱包代币余额：" + balance + "小于提现金额" + amount);
                      return jspPage;
                  }
            }else{
                double balance = getMainBalance(util, mainAddress);// 余额
            	  if (balance < amount) {
                      jspPage.addObject("statusCode", 300);
                    jspPage.addObject("message", "审核失败，钱包余额：" + balance + "小于提现金额" + amount);
                      return jspPage;
                  }
            }
             

            // 校验提现地址
            boolean isUsable = util.validateAddress(user_address);
            if (!isUsable) {
                jspPage.addObject("statusCode", 300);
                jspPage.addObject("message", "提现地址无效");
                return jspPage;
            }

            // 执行虚拟币转账
            if (util instanceof OmniUtils) {
                DecimalFormat df = new DecimalFormat("0.########");

                double btcBalance = util.getBalance(((BTCUtils) util).getAccount(mainAddress));
                if (btcBalance <= 0) {
                    jspPage.addObject("statusCode", 300);
                    jspPage.addObject("message", "USDT钱包的 比特币 余额不足");
                    return jspPage;
                }

                trade_number = ((OmniUtils) util).sendOmniTransaction(mainAddress, user_address, OmniUtils.propertyId,
                        df.format(amountonly));
            } else if (fvirtualcointype.isFisToken() == true) {
                // TokenClient.setGasprice(3);
                ERCWalletConfig EWC = new ERCWalletConfig();
                EWC.setIP(config.getIP());
                EWC.setPort(config.getPort());
                EWC.setMainAddress(config.getMainAddress());
                EWC.setAccessKey(config.getAccessKey());
                EWC.setSecretKey(config.getSecretKey());
                TokenClient.setConfig(EWC);
                TokenClient.setContractAddress(fvirtualcointype.getFaddress());
                trade_number = TokenClient.sendTokenTransaction(fvirtualcointype.getMainAddr(),
                        fvirtualcointype.getFpassword(), user_address, fvirtualcointype.getFaddress(),
                        // 处理double类型的amount,四位小数以内
                        BigInteger.valueOf((long) (amountonly * Math.pow(10, TokenClient.getTokenDecimals()))));
                log.info("交易单ID： " + trade_number);
            } else {
                trade_number = util.sendTransaction(mainAddress, user_address, amountonly);
            }

            if (trade_number == null) {
                jspPage.addObject("statusCode", 300);
                jspPage.addObject("message", String.format("钱包转账失败  %s", util.getMessage()));
                return jspPage;
            }
            String num = utils.newGuid();
            // 更新虚拟币操作状态
            TDateTime date = TDateTime.Now();
            try (Mysql handle1 = new Mysql(); Transaction tx = new Transaction(handle1)) {
                MyQuery cdsOpera = new MyQuery(handle1);
                cdsOpera.add("select * from %s where fId='%s'", AppDB.fvirtualcaptualoperation, fid);
                cdsOpera.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                cdsOpera.open();

                cdsOpera.edit();
                if (fvirtualcointype.isFisToken() == true) {
                    cdsOpera.setField("fStatus", VirtualCapitalOperationOutStatusEnum.Operationing);
                }else{
                cdsOpera.setField("fStatus", VirtualCapitalOperationOutStatusEnum.OperationSuccess);
                }
                cdsOpera.setField("flastUpdateTime", date);
                cdsOpera.setField("ftradeUniqueNumber", trade_number);// 交易流水号
                cdsOpera.setField("taskId", num);
                cdsOpera.post();

                // 虚拟钱包扣钱
                MyQuery walletds = new MyQuery(handle1);
                walletds.add("SELECT * from %s", AppDB.fvirtualwallet);
                walletds.add("where fuid = %s and fVi_fId = %s", userId, coinId);
                walletds.open();
                double fFrozen = walletds.getRecords().get(0).getDouble("fFrozen");
                walletds.edit();
                walletds.setField("fLastUpdateTime", date);
                walletds.setField("fFrozen", fFrozen - amount);
                walletds.post();
                tx.commit();
            } catch (Exception e) {
                log.info(String.format("提现失败,请检查钱包信息，%s。", e.getMessage()));
            }
            // 虚拟钱包扣钱（待完成）
            // BatchScript script = new BatchScript(handle);
            // WalletAmount wallet = new WalletAmount(handle, userId, coinId, num);
            //// wallet.unlock(script, amount, coinId + "提现");
            // wallet.moveTo(script, userId, amount, coinId + "提现");
            // script.exec();
        } catch (Exception e) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "审核失败" + e.getMessage());
            return jspPage;
        }
        // 发送提现成功通知
        Fuser fuser = frontUserService.findById(userId);
        ShortMessageService.send(fuser.getFtelephone(),
                "【瑞银钱包】您于" + TDateTime.Now().toString() + "已成功提现" + amount + fvirtualcointype.getfShortName() + "。");

        jspPage.addObject("statusCode", 200);
        jspPage.addObject("message", "审核成功");
        jspPage.addObject("callbackType", "closeCurrent");
        return jspPage;
    }

    /**
     * 虚拟币审核划转
     * 
     * @param mysql
     */
    @RequestMapping("ssadmin/transferAuditing")
    public ModelAndView transferAuditing(@RequestParam(required = true) String uid) throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        if (uid == "" || uid == null) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "请选择信息!");
            return jspPage;
        } else {
            String[] fids = uid.split(",");
            // int fid = Integer.parseInt(request.getParameter("fid"));
            int success = 0;
            int fail = 0;
            b1: for (int i = 0; i < fids.length; i++) {
                try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
                    int fid1 = Integer.parseInt(fids[i]);
                    MyQuery queryStatus = new MyQuery(mysql);
                    queryStatus.add("select fstatus,fID from %s where 1=1", "fentrust");
                    queryStatus.add("and fId=%s for update", fid1);
                    queryStatus.setMaximum(-1);
                    queryStatus.open();
                    MyQuery ds = new MyQuery(mysql);
                    ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
                    ds.add("from %s a left join %s b ", "ftransfer", "fuser");
                    ds.add("on a.fUs_fId2 = b.fId ");
                    ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
                    ds.add("where a.fStatus = 0 ");// 0 未审核
                    ds.add("and a.fId=%d", fid1);
                    ds.open();
                    if (ds.eof()) {
                        jspPage.addObject("message", "审核失败,只有状态为:" + 0 + "的划转记录才允许审核!");
                        return jspPage;
                    }
                    String account = ds.getString("fAccount");
                    double fAmount = ds.getDouble("fAmount");// 划转数量
                    String fUs_fId2 = ds.getString("fUs_fId2");// 会员ID
                    String fVi_fId2 = ds.getString("fVi_fId2");
                    MyQuery ds1 = new MyQuery(mysql);
                    ds1.add("select * from %s", "fvirtualwallet");
                    ds1.add("where fuid='%s' ", fUs_fId2);
                    ds1.add("and fVi_fId='%s'", fVi_fId2);
                    ds1.open();
                    double fFrozen = ds1.getDouble("fFrozen");
                    List<Fvirtualcointype> types = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
                    Map typeMap = new HashMap();
                    for (Fvirtualcointype fvirtualcointype : types) {
                        typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
                    }
                    String coinType = typeMap.get(Integer.parseInt(fVi_fId2)).toString().toLowerCase();
                    // 调用第三方接口进行划转
                    if (fFrozen >= fAmount) {
                        ds1.edit();
                        ds1.setField("fFrozen", fFrozen - fAmount);
                        ds1.post();
                        MyQuery ds2 = new MyQuery(mysql);
                        ds2.add("select * from %s ", "ftransfer");
                        ds2.add("where fid='%d' ", fid1);
                        ds2.add("and fStatus = 0 ");// 0 未审核
                        ds2.open();
                        ds2.edit();
                        String taskId = utils.newGuid();
                        ds2.setField("fStatus", 1);
                        ds2.setField("flastUpdateTime", TDateTime.Now());
                        ds2.setField("taskId", taskId);
                        ds2.post();
                        MyQuery ds3 = new MyQuery(mysql);
                        ds3.add("select UID_,userId_,coinId_,total_,frozen_,totalChange_,frozenChange_,changeReason_,changeDate_,guid_,taskId_,entrustId_ from %s",
                                AppDB.t_walletlog);
                        ds3.setMaximum(1);
                        ds3.open();
                        ds3.append();
                        ds3.setField("userId_", ds2.getInt("fUs_fId2"));
                        ds3.setField("coinId_", ds2.getInt("fVi_fId2"));
                        ds3.setField("total_", ds1.getDouble("fTotal"));
                        ds3.setField("frozen_", ds1.getDouble("fFrozen"));
                        ds3.setField("totalChange_", fAmount);
                        ds3.setField("frozenChange_", fAmount);
                        ds3.setField("changeReason_", "划转钱包");
                        ds3.setField("changeDate_", TDateTime.Now());
                        ds3.setField("entrustId_", "0");
                        ds3.setField("taskId_", taskId);
                        ds3.post();
                        Map<String, String> map = transfer(account, ds.getString("fAmount"), coinType,
                                ds.getString("fId"));
                        String sign = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6p0XWjscY+gsyqKRhw9MeLsEmhFdBRhT2emOck/F1Omw38ZWhJxh9kDfs5HzFJMrVozgU+SJFDONxs8UB0wMILKRmqfLcfClG9MyCNuJkkfm0HFQv1hRGdOvZPXj3Bckuwa7FrEXBRYUhK7vJ40afumspthmse6bs6mZxNn/mALZ2X07uznOrrc2rk41Y2HftduxZw6T4EmtWuN2x4CZ8gwSyPAW5ZzZJLQ6tZDojBK4GZTAGhnn3bg5bBsBlw2+FLkCQBuDsJVsFPiGh/b6K/";
                        String type = Integer.parseInt(fVi_fId2) == 7 ? "at" : "coa";
                        MyQuery dsa = new MyQuery(mysql);
                        dsa.add("select fId,ftransferid,fuserid,fcreatetime,finput_username,finput_coins,finput_moneytype,finput_wallet_log_id,finput_sign,"
                                + "foutput_returenvalue,foutput_msg,foutput_data,foutput_code from %s",
                                "ftransfertoyylog");
                        dsa.open();
                        dsa.append();
                        dsa.setField("ftransferid", fid1);
                        dsa.setField("fuserid", account);
                        dsa.setField("fcreatetime", TDateTime.Now());
                        dsa.setField("finput_username", account);
                        dsa.setField("finput_coins", fAmount);
                        dsa.setField("finput_moneytype", type);
                        dsa.setField("finput_wallet_log_id", ds.getString("fId"));
                        dsa.setField("finput_sign", sign);
                        dsa.setField("foutput_returenvalue", map.get("returenvalue"));
                        dsa.setField("foutput_msg", map.get("msg"));
                        dsa.setField("foutput_data", map.get("returenvalue"));
                        dsa.setField("foutput_code", Integer.parseInt(map.get("code")));
                        dsa.post();
                        if (!"200".equals(map.get("code"))) {
                            fail++;
                            continue b1;
                        }
                    } else {
                        jspPage.addObject("statusCode", 300);
                        jspPage.addObject("message", "审核失败,冻结数量,小于划转数量!");
                        return jspPage;
                    }
                    success++;
                    tx.commit();
                } catch (Exception e) {
                    jspPage.addObject("statusCode", 300);
                    jspPage.addObject("message", "审核失败" + e.getMessage());
                    return jspPage;
                }
            }
            if (fail > success && success == 0) {
                jspPage.addObject("statusCode", 300);
            } else {
                jspPage.addObject("statusCode", 200);
            }
            jspPage.addObject("message", "审核成功,审核成功数量" + success + "，失败数量" + fail);
            jspPage.addObject("callbackType", "closeCurrent");
            return jspPage;
        }
    }

    /**
     * 虚拟币审核划转 --> to ->封神榜
     * 
     * @param username
     * @return
     */
    @RequestMapping("ssadmin/othertransferAuditing")
    public ModelAndView otherTransferAuditing(@RequestParam(required = true) String uid) {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        if (uid == "" || uid == null) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "请选择信息!");
            return jspPage;
        } else {
            String[] fids = uid.split(",");
            int success = 0;
            int fail = 0;
            b1: for (int i = 0; i < fids.length; i++) {
                try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
                    int fid1 = Integer.parseInt(fids[i]);
                    MyQuery queryStatus = new MyQuery(mysql);
                    queryStatus.add("select fstatus,fID from %s where 1=1", "fentrust");
                    queryStatus.add("and fId=%s for update", fid1);
                    queryStatus.setMaximum(-1);
                    queryStatus.open();
                    MyQuery ds = new MyQuery(mysql);
                    ds.add("select a.*,b.fid,b.frealName,c.fname");
                    ds.add(" from %s a left join %s b", "ftransform", "fuser");
                    ds.add(" on a.fUs_fId2 = b.fId ");
                    ds.add(" left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
                    ds.add(" where a.fStatus = 0"); // 未审核
                    ds.add(" and a.fId=%d", fid1);
                    ds.open();
                    if (ds.eof()) {
                        jspPage.addObject("message", "审核失败,只有状态为:" + 0 + "的划转记录才允许审核!");
                        return jspPage;
                    }
                    String account = ds.getString("fAccount");
                    double fAmount = ds.getDouble("fAmount");// 划转数量
                    String fUs_fId2 = ds.getString("fUs_fId2");// 会员ID
                    String fVi_fId2 = ds.getString("fVi_fId2");
                    MyQuery ds1 = new MyQuery(mysql);
                    ds1.add("select * from %s", "fvirtualwallet");
                    ds1.add("where fuid='%s' ", fUs_fId2);
                    ds1.add("and fVi_fId='%s'", fVi_fId2);
                    ds1.open();
                    double fFrozen = ds1.getDouble("fFrozen");
                    List<Fvirtualcointype> types = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
                    Map typeMap = new HashMap();
                    for (Fvirtualcointype fvirtualcointype : types) {
                        typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
                    }
                    String coinType = typeMap.get(Integer.parseInt(fVi_fId2)).toString().toLowerCase();
                    // 调用第三方接口进行划转
                    if (fFrozen >= fAmount) {
                        ds1.edit();
                        ds1.setField("fFrozen", fFrozen - fAmount);
                        ds1.post();
                        MyQuery ds2 = new MyQuery(mysql);
                        ds2.add("select * from %s ", "ftransform");
                        ds2.add("where fid='%d' ", fid1);
                        ds2.add("and fStatus = 0 ");// 0 未审核
                        ds2.open();
                        ds2.edit();
                        String taskId = utils.newGuid();
                        ds2.setField("fStatus", 1);
                        ds2.setField("flastUpdateTime", TDateTime.Now());
                        ds2.setField("taskId", taskId);
                        ds2.post();
                        MyQuery ds3 = new MyQuery(mysql);
                        ds3.add("select UID_,userId_,coinId_,total_,frozen_,totalChange_,frozenChange_,changeReason_,changeDate_,guid_,taskId_,entrustId_ from %s",
                                AppDB.t_walletlog);
                        ds3.setMaximum(1);
                        ds3.open();
                        ds3.append();
                        ds3.setField("userId_", ds2.getInt("fUs_fId2"));
                        ds3.setField("coinId_", ds2.getInt("fVi_fId2"));
                        ds3.setField("total_", ds1.getDouble("fTotal"));
                        ds3.setField("frozen_", ds1.getDouble("fFrozen"));
                        ds3.setField("totalChange_", fAmount);
                        ds3.setField("frozenChange_", fAmount);
                        ds3.setField("changeReason_", "划转封神平台钱包");
                        ds3.setField("changeDate_", TDateTime.Now());
                        ds3.setField("entrustId_", "0");
                        ds3.setField("taskId_", taskId);
                        ds3.post();
                        Map<String, String> map = otherTransfer(account, ds.getString("fAmount"));
                        String sign = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6p0XWjscY+gsyqKRhw9MeLsEmhFdBRhT2emOck/F1Omw38ZWhJxh9kDfs5HzFJMrVozgU+SJFDONxs8UB0wMILKRmqfLcfClG9MyCNuJkkfm0HFQv1hRGdOvZPXj3Bckuwa7FrEXBRYUhK7vJ40afumspthmse6bs6mZxNn/mALZ2X07uznOrrc2rk41Y2HftduxZw6T4EmtWuN2x4CZ8gwSyPAW5ZzZJLQ6tZDojBK4GZTAGhnn3bg5bBsBlw2+FLkCQBuDsJVsFPiGh/b6K/";
                        String type = Integer.parseInt(fVi_fId2) == 7 ? "at" : "coa";
                        MyQuery dsa = new MyQuery(mysql);
                        dsa.add("select fId,ftransferid,fuserid,fcreatetime,finput_username,finput_coins,finput_moneytype,finput_wallet_log_id,finput_sign,"
                                + "foutput_returenvalue,foutput_msg,foutput_data,foutput_code from %s",
                                "ftransformlog");
                        dsa.setMaximum(1);
                        dsa.open();
                        dsa.append();
                        dsa.setField("ftransferid", fid1);
                        dsa.setField("fuserid", account);
                        dsa.setField("fcreatetime", TDateTime.Now());
                        dsa.setField("finput_username", account);
                        dsa.setField("finput_coins", fAmount);
                        dsa.setField("finput_moneytype", type);
                        dsa.setField("finput_wallet_log_id", ds.getString("fId"));
                        dsa.setField("finput_sign", sign);
                        dsa.setField("foutput_returenvalue", map.get("returenvalue"));
                        dsa.setField("foutput_msg", map.get("msg"));
                        dsa.setField("foutput_data", map.get("returenvalue"));
                        dsa.setField("foutput_code", Integer.parseInt(map.get("code")));
                        dsa.post();
                        if (!"0".equals(map.get("code"))) {
                            fail++;
                            continue b1;
                        }
                    } else {
                        jspPage.addObject("statusCode", 300);
                        jspPage.addObject("message", "审核失败,冻结数量,小于划转数量!");
                        return jspPage;
                    }
                    success++;
                    tx.commit();
                } catch (Exception e) {
                    jspPage.addObject("statusCode", 300);
                    jspPage.addObject("message", "审核失败" + e.getMessage());
                    return jspPage;
                }
            }
            if (fail > success && success == 0) {
                jspPage.addObject("statusCode", 300);
            } else {
                jspPage.addObject("statusCode", 200);
            }
            jspPage.addObject("message", "审核成功,审核成功数量" + success + "，失败数量" + fail);
            return jspPage;
        }
    }

    // 同步用户接口
    public static Map<String, String> transfer(String username, String coin, String type, String id) {
        String code = "";
        String msg = "";
        String url = Constant.f_url;
        type = "at".equals(type) ? "newAt" : type;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("coin", coin);
        params.put("moneytype", type);
        params.put("wallet_log_id", id);
        params.put("sign",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6p0XWjscY+gsyqKRhw9MeLsEmhFdBRhT2emOck/F1Omw38ZWhJxh9kDfs5HzFJMrVozgU+SJFDONxs8UB0wMILKRmqfLcfClG9MyCNuJkkfm0HFQv1hRGdOvZPXj3Bckuwa7FrEXBRYUhK7vJ40afumspthmse6bs6mZxNn/mALZ2X07uznOrrc2rk41Y2HftduxZw6T4EmtWuN2x4CZ8gwSyPAW5ZzZJLQ6tZDojBK4GZTAGhnn3bg5bBsBlw2+FLkCQBuDsJVsFPiGh/b6K/");

        String resultStr = HttpUtils.post(url, params);
        if (FrontUserJsonController.notEmpty(resultStr) && resultStr.contains("code")) {
            JSONObject resultJson = JSONObject.fromObject(resultStr);
            code = resultJson.getString("code");
            msg = resultJson.getString("msg");
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("returenvalue", resultStr);
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    // 同步封神榜接口
    public static Map<String, String> otherTransfer(String username, String coin) {
        String code = "";
        String msg = "";
        String url = Constant.f_url_other;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("at", coin);
        params.put("private_key",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6p0XWjscY+gsyqKRhw9MeLsEmhFdBRhT2emOck/F1Omw38ZWhJxh9kDfs5HzFJMrVozgU+SJFDONxs8UB0wMILKRmqfLcfClG9MyCNuJkkfm0HFQv1hRGdOvZPXj3Bckuwa7FrEXBRYUhK7vJ40afumspthmse6bs6mZxNn/mALZ2X07uznOrrc2rk41Y2HftduxZw6T4EmtWuN2x4CZ8gwSyPAW5ZzZJLQ6tZDojBK4GZTAGhnn3bg5bBsBlw2+FLkCQBuDsJVsFPiGh/b6K/");
        String resultStr = HttpUtils.post(url, params);
        if (FrontUserJsonController.notEmpty(resultStr) && resultStr.contains("code")) {
            JSONObject resultJson = JSONObject.fromObject(resultStr);
            code = resultJson.getString("code");
            msg = resultJson.getString("msg");
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("returenvalue", resultStr);
        map.put("code", code);
        map.put("msg", msg);
        return map;

    }

    private double getMainBalance(WalletUtil util, String mainAddress) {
        if (util instanceof OmniUtils) {
            return ((OmniUtils) util).getOmniBalance(mainAddress, OmniUtils.propertyId);
        }

        if (util instanceof BTCUtils) {
            return util.getBalance(((BTCUtils) util).getAccount(mainAddress));
        }

        return util.getBalance(mainAddress);
    }

    private WalletConfig initWalletConfig(Fvirtualcointype fvirtualcointype) {
        WalletConfig config = new WalletConfig();
        config.setAccessKey(fvirtualcointype.getFaccess_key());
        config.setSecretKey(fvirtualcointype.getFsecrt_key());
        config.setIP(fvirtualcointype.getFip());
        config.setPort(fvirtualcointype.getFport());
        config.setPassword(fvirtualcointype.getFpassword());
        config.setMainAddress(fvirtualcointype.getMainAddr());
        return config;
    }

    /**
     * 虚拟币审核提现
     */
    public ModelAndView virtualCapitalOutAudit_old() throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));

        Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService.findById(fid);
        int status = virtualcaptualoperation.getFstatus();
        if (status != VirtualCapitalOperationOutStatusEnum.OperationLock) {
            jspPage.addObject("statusCode", 300);
            String status_s = VirtualCapitalOperationOutStatusEnum
                    .getEnumString(VirtualCapitalOperationOutStatusEnum.OperationLock);
            jspPage.addObject("message", "审核失败,只有状态为:" + status_s + "的提现记录才允许审核!");
            return jspPage;
        }

        // 根据用户查钱包最后修改时间
        int userId = virtualcaptualoperation.getFuser().getFid();
        Fvirtualcointype fvirtualcointype = virtualcaptualoperation.getFvirtualcointype();
        int coinTypeId = fvirtualcointype.getFid();

        Fvirtualwallet virtualWalletInfo = frontUserService.findVirtualWalletByUser(userId, coinTypeId);
        // 提现数量
        double amount = Utils.getDouble(virtualcaptualoperation.getFamount() + virtualcaptualoperation.getFfees(), 4);
        // 手续费
        double frozenRmb = Utils.getDouble(virtualWalletInfo.getFfrozen(), 4);
        if (frozenRmb - amount < -0.0001) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "审核失败,冻结数量:" + frozenRmb + "小于提现数量:" + amount + "，操作异常!");
            return jspPage;
        }

        if (fvirtualcointype.isFisautosend()) {
            WalletConfig btcMsg = new WalletConfig();
            btcMsg.setAccessKey(fvirtualcointype.getFaccess_key());
            btcMsg.setSecretKey(fvirtualcointype.getFsecrt_key());
            btcMsg.setIP(fvirtualcointype.getFip());
            btcMsg.setPassword(fvirtualcointype.getFpassword());
            btcMsg.setPort(fvirtualcointype.getFport());

            try {
                if (!fvirtualcointype.isFisEth()) {
                    BTCUtils btcUtils = new BTCUtils(btcMsg);
                    double balance = btcUtils.getBalance(null);
                    if (balance < amount + 0.1d) {
                        jspPage.addObject("statusCode", 300);
                        jspPage.addObject("message", "审核失败，钱包余额：" + balance + "小于" + amount);
                        return jspPage;
                    }

                    // 校验提现地址
                    boolean isTrue = btcUtils.validateAddress(virtualcaptualoperation.getWithdraw_virtual_address());
                    if (!isTrue) {
                        jspPage.addObject("statusCode", 300);
                        jspPage.addObject("message", "提现地址无效");
                        return jspPage;
                    }
                }

            } catch (Exception e1) {
                jspPage.addObject("statusCode", 300);
                jspPage.addObject("message", "审核失败，钱包连接失败");
                return jspPage;
            }
        }

        if (virtualcaptualoperation.getFtradeUniqueNumber() != null
                && virtualcaptualoperation.getFtradeUniqueNumber().trim().length() > 0) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "非法操作！请检查钱包！");
            return jspPage;
        }

        // 充值操作
        // virtualcaptualoperation
        // .setFstatus(VirtualCapitalOperationOutStatusEnum.OperationSuccess);
        virtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());

        // 钱包操作
        virtualWalletInfo.setFlastUpdateTime(Utils.getTimestamp());
        virtualWalletInfo.setFfrozen(virtualWalletInfo.getFfrozen() - amount);
        try {
            this.virtualCapitaloperationService.updateCapital(virtualcaptualoperation, virtualWalletInfo,
                    fvirtualcointype);
        } catch (Exception e) {
            e.printStackTrace();
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", e.getMessage());
            return jspPage;
        }
        jspPage.addObject("statusCode", 200);
        jspPage.addObject("message", "审核成功");
        jspPage.addObject("callbackType", "closeCurrent");
        return jspPage;
    }

    @RequestMapping("ssadmin/auditVCInlog")
    public ModelAndView auditVCInlog() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService.findById(fid);
        int status = virtualcaptualoperation.getFstatus();
        int type = virtualcaptualoperation.getFtype();
        if (status == VirtualCapitalOperationInStatusEnum.SUCCESS || type != VirtualCapitalOperationTypeEnum.COIN_IN) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "网络异常");
            return modelAndView;
        }

        // 根据用户查钱包最后修改时间
        int userId = virtualcaptualoperation.getFuser().getFid();
        Fvirtualcointype fvirtualcointype = virtualcaptualoperation.getFvirtualcointype();
        int coinTypeId = fvirtualcointype.getFid();

        Fvirtualwallet virtualWalletInfo = frontUserService.findVirtualWalletByUser(userId, coinTypeId);
        // 钱包操作
        virtualWalletInfo.setFlastUpdateTime(Utils.getTimestamp());
        virtualWalletInfo.setFtotal(virtualWalletInfo.getFtotal() + virtualcaptualoperation.getFamount());

        virtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS);
        try {
            this.virtualCapitaloperationService.updateCapital(virtualcaptualoperation, virtualWalletInfo);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "网络异常");
            return modelAndView;
        }
        // 发送充值成功通知
        Fuser fuser = frontUserService.findById(userId);
        ShortMessageService.send(fuser.getFtelephone(), "【以太国际】您于" + TDateTime.Now().toString() + "已成功充值"
                + virtualcaptualoperation.getFamount() + fvirtualcointype.getfShortName() + "。");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "审核成功");
        return modelAndView;
    }

    private static enum Export1Filed {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 虚拟币类型, 充值地址, 交易ID, 确认数, 状态, 交易类型, 数量, 手续费, 创建时间, 修改时间;
    }

    private static enum Export2Filed {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 虚拟币类型, 提现地址, 交易ID, 状态, 交易类型, 数量, 手续费, 创建时间, 修改时间;
    }

    private static enum Export3Filed {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 虚拟币类型, 提现地址, 充值地址, 交易ID, 状态, 交易类型, 数量, 手续费, 创建时间, 修改时间;
    }

    private static enum Export4Filed {
        会员UID, 登录名, 会员昵称, 会员真实姓名, 虚拟币类型, 状态, 交易类型, 数量, 划转账号, 创建时间, 最后修改时间;
    }

    private static enum Export5Filed {
        会员UID, 登录名, 会员昵称, 会员真实姓名, 虚拟币类型, 状态, 交易类型, 数量, 划转账号, 创建时间, 最后修改时间;
    }

    public List<Fvirtualcaptualoperation> getInList() throws Exception {
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype=" + VirtualCapitalOperationTypeEnum.COIN_IN + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
                filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
            }
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }

        filterSQL.append("and fuser.fid is not null \n");

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL.toString(),
                false);

        return list;
    }

    public List<Fvirtualcaptualoperation> getAllList() throws Exception {
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
                filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
            }
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }
        filterSQL.append("and fuser.fid is not null \n");

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

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL.toString(),
                false);

        return list;
    }

    public List<Fvirtualcaptualoperation> getOutList() throws Exception {

        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype=" + VirtualCapitalOperationTypeEnum.COIN_OUT + "\n");
        filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
                + VirtualCapitalOperationOutStatusEnum.OperationLock + ")\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("fuser.fid =" + fid + " or \n");
            } catch (Exception e) {
                filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
                filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
            }
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL + "", false);
        return list;
    }

    public List<Fvirtualcaptualoperation> getOutSucList() throws Exception {
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype=" + VirtualCapitalOperationTypeEnum.COIN_OUT + "\n");
        filterSQL.append("and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ")\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("fuser.fid =" + fid + " or \n");
            } catch (Exception e) {
                filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
                filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
            }
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL.toString(),
                false);

        return list;
    }

    @RequestMapping("ssadmin/virtualCapitalInListExport.html")
    public ModelAndView virtualCapitalInListExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=virtualCapitalInList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export1Filed filed : Export1Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }

        List<Fvirtualcaptualoperation> alls = getInList();
        for (Fvirtualcaptualoperation virtualcaptualoperation : alls) {
            e.createRow(rowIndex++);
            for (Export1Filed filed : Export1Filed.values()) {
                switch (filed) {
                case 订单ID:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFid());
                    break;
                case 会员UID:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFrealName());
                    break;
                case 虚拟币类型:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFvirtualcointype().getFname());
                    break;
                case 确认数:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFconfirmations());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFstatus_s());
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFtype_s());
                    break;
                case 充值地址:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getRecharge_virtual_address());
                    break;
                case 交易ID:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFtradeUniqueNumber());
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFfees());
                    break;
                case 修改时间:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFlastUpdateTime());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFcreateTime());
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

    @RequestMapping("ssadmin/virtualCapitalOutSucListExport.html")
    public ModelAndView virtualCapitalOutSucListExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=virtualCapitalOutSucList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export2Filed filed : Export2Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }

        List<Fvirtualcaptualoperation> alls = getOutSucList();
        for (Fvirtualcaptualoperation virtualcaptualoperation : alls) {
            e.createRow(rowIndex++);
            for (Export2Filed filed : Export2Filed.values()) {
                switch (filed) {
                case 订单ID:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFid());
                    break;
                case 会员UID:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFrealName());
                    break;
                case 虚拟币类型:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFvirtualcointype().getFname());
                    break;
                case 提现地址:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getWithdraw_virtual_address());
                    break;
                case 交易ID:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFtradeUniqueNumber());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFstatus_s());
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFtype_s());
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFfees());
                    break;
                case 修改时间:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFlastUpdateTime());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFcreateTime());
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

    @RequestMapping("ssadmin/virtualCapitalOutListExport.html")
    public ModelAndView virtualCapitalOutListExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=virtualCapitalOutList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export2Filed filed : Export2Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }

        List<Fvirtualcaptualoperation> alls = getOutList();
        for (Fvirtualcaptualoperation virtualcaptualoperation : alls) {
            e.createRow(rowIndex++);
            for (Export2Filed filed : Export2Filed.values()) {
                switch (filed) {
                case 订单ID:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFid());
                    break;
                case 会员UID:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFrealName());
                    break;
                case 虚拟币类型:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFvirtualcointype().getFname());
                    break;
                case 提现地址:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getWithdraw_virtual_address());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFstatus_s());
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFtype_s());
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFfees());
                    break;
                case 修改时间:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFlastUpdateTime());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFcreateTime());
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

    @RequestMapping("ssadmin/virtualCapitalAllListExport.html")
    public ModelAndView virtualCapitalAllListExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=virtualCapitalAllList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export3Filed filed : Export3Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }

        List<Fvirtualcaptualoperation> alls = getAllList();
        for (Fvirtualcaptualoperation virtualcaptualoperation : alls) {
            e.createRow(rowIndex++);
            for (Export3Filed filed : Export3Filed.values()) {
                switch (filed) {
                case 订单ID:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFid());
                    break;
                case 会员UID:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (virtualcaptualoperation.getFuser() != null)
                        e.setCell(filed.ordinal(), virtualcaptualoperation.getFuser().getFrealName());
                    break;
                case 虚拟币类型:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFvirtualcointype().getFname());
                    break;
                case 提现地址:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getWithdraw_virtual_address());
                    break;
                case 充值地址:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getRecharge_virtual_address());
                    break;
                case 交易ID:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFtradeUniqueNumber());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFstatus_s());
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFtype_s());
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFamount());
                    break;
                case 手续费:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFfees());
                    break;
                case 修改时间:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFlastUpdateTime());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), virtualcaptualoperation.getFcreateTime());
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

    @RequestMapping("ssadmin/virtualSucOp")
    public ModelAndView virtualSucOp() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int uid = Integer.parseInt(request.getParameter("uid"));
        Fvirtualcaptualoperation ca = this.virtualCapitaloperationService.findById(uid);
        if (ca.getFtradeUniqueNumber() != null && ca.getFtradeUniqueNumber().trim().length() > 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "请勿重复拔币");
            return modelAndView;
        }
        ca.setFisaudit(false);
        this.virtualCapitaloperationService.updateObj(ca);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateVcTradeNumber")
    public ModelAndView updateVcTradeNumber() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        try {
            int fid = Integer.parseInt(request.getParameter("fid"));
            Fvirtualcaptualoperation vc = this.virtualCapitaloperationService.findById(fid);
            if (vc == null) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "记录不存在");
                return modelAndView;
            }
            if (vc.getFtype() != 2 && vc.getFstatus() != 3) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "只有成功提成的记录才允许补交易ID");
                return modelAndView;
            }

            if (vc.getFtradeUniqueNumber() != null && vc.getFtradeUniqueNumber().trim().length() > 0) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "记录已存在交易ID，不允许覆盖");
                return modelAndView;
            }
            vc.setFtradeUniqueNumber(request.getParameter("ftradeUniqueNumber"));
            this.virtualCapitaloperationService.updateObj(vc);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "网络异常");
            return modelAndView;
        }

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/virtualCapital_list")
    public ModelAndView updateFvalesNumber() throws Exception {
        JspPage jspPage = new JspPage(request);
        String begin = request.getParameter("begin");
        String end = request.getParameter("end");
        if (begin != null && end != null) {
            double begin1 = Double.parseDouble(begin);
            double end1 = Double.parseDouble(end);
            if (end1 > begin1) {
                Mysql sql = new Mysql();
                MyQuery fvalue = new MyQuery(sql);
                fvalue.add("select * from %s where fId=13020", "fsystemargs");
                fvalue.open();
                if (!fvalue.eof()) {
                    fvalue.edit();
                    fvalue.setField("fValue", String.format("%s,%s", begin, end));
                    fvalue.post();
                }
            }
        }
        jspPage.addObject("message", "审核成功");
        jspPage.setViewName("redirect:/ssadmin/virtualCapitalOutList.html");
        return jspPage;
    }

    @RequestMapping("ssadmin/viewtransferListExport")
    public ModelAndView viewtransferListExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=viewtransferList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export4Filed filed : Export4Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> viewtransferList = getviewtransferList();
        for (Map<String, Object> map : viewtransferList) {
            e.createRow(rowIndex++);
            for (Export4Filed filed : Export4Filed.values()) {
                switch (filed) {
                case 会员UID:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("uid")));
                    break;
                case 登录名:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("floginName")));
                    break;
                case 会员昵称:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fnickName")));
                    break;
                case 会员真实姓名:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("frealName")));
                    break;
                case 虚拟币类型:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fname")));
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), "待审核");
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), "虚拟币划转");
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), Double.parseDouble(String.valueOf(map.get("fAmount"))));
                    break;
                case 划转账号:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAccount")));
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), Timestamp.valueOf(String.valueOf(map.get("fCreateTime"))));
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), Timestamp.valueOf(String.valueOf(map.get("flastUpdateTime"))));
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

    @RequestMapping("ssadmin/viewftransformListExport")
    public ModelAndView viewftransformListExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=viewftransformList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export4Filed filed : Export4Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> viewtransformList = getviewtransformList();
        for (Map<String, Object> map : viewtransformList) {
            e.createRow(rowIndex++);
            for (Export4Filed filed : Export4Filed.values()) {
                switch (filed) {
                case 会员UID:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("uid")));
                    break;
                case 登录名:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("floginName")));
                    break;
                case 会员昵称:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fnickName")));
                    break;
                case 会员真实姓名:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("frealName")));
                    break;
                case 虚拟币类型:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fname")));
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), "待审核");
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), "虚拟币划转");
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), Double.parseDouble(String.valueOf(map.get("fAmount"))));
                    break;
                case 划转账号:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAccount")));
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), Timestamp.valueOf(String.valueOf(map.get("fCreateTime"))));
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), Timestamp.valueOf(String.valueOf(map.get("flastUpdateTime"))));
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

    private List<Map<String, Object>> getviewtransferList() {
        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
        ds.add("from %s a left join %s b ", "ftransfer", "fuser");
        ds.add("on a.fUs_fId2 = b.fId ");
        ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
        ds.add("where a.fStatus = 0 ");// 0 未审核
        ds.add("order by a.fCreateTime desc ");
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        return list;
    }

    private List<Map<String, Object>> getviewtransformList() {
        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
        ds.add("from %s a left join %s b ", "ftransform", "fuser");
        ds.add("on a.fUs_fId2 = b.fId ");
        ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
        ds.add("where a.fStatus = 0 ");// 0 未审核
        ds.add("order by a.fCreateTime desc ");
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        return list;
    }

    @RequestMapping("ssadmin/virtualTransferSucListExport")
    public ModelAndView virtualTransferSucListExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=TransferSucList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export5Filed filed : Export5Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> list = virtualTransferSucList1();
        for (Map<String, Object> map : list) {
            e.createRow(rowIndex++);
            for (Export5Filed filed : Export5Filed.values()) {
                switch (filed) {
                case 会员UID:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("uid")));
                    break;
                case 登录名:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("floginName")));
                    break;
                case 会员昵称:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fnickName")));
                    break;
                case 会员真实姓名:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("frealName")));
                    break;
                case 虚拟币类型:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fname")));
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), "划转成功");
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), "虚拟币划转");
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), Double.parseDouble(String.valueOf(map.get("fAmount"))));
                    break;
                case 划转账号:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAccount")));
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), Timestamp.valueOf(String.valueOf(map.get("fCreateTime"))));
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), Timestamp.valueOf(String.valueOf(map.get("flastUpdateTime"))));
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

    @RequestMapping("ssadmin/virtualTransformSucListExport")
    public ModelAndView virtualTransformSucListExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=TransformSucList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export5Filed filed : Export5Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> list = virtualTransformSucList1();
        for (Map<String, Object> map : list) {
            e.createRow(rowIndex++);
            for (Export5Filed filed : Export5Filed.values()) {
                switch (filed) {
                case 会员UID:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("uid")));
                    break;
                case 登录名:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("floginName")));
                    break;
                case 会员昵称:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fnickName")));
                    break;
                case 会员真实姓名:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("frealName")));
                    break;
                case 虚拟币类型:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fname")));
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), "划转成功");
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), "虚拟币划转");
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), Double.parseDouble(String.valueOf(map.get("fAmount"))));
                    break;
                case 划转账号:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAccount")));
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), Timestamp.valueOf(String.valueOf(map.get("fCreateTime"))));
                    break;
                case 最后修改时间:
                    e.setCell(filed.ordinal(), Timestamp.valueOf(String.valueOf(map.get("flastUpdateTime"))));
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

    private List<Map<String, Object>> virtualTransferSucList1() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
            ds.add("from %s a left join %s b ", "ftransfer", "fuser");
            ds.add("on a.fUs_fId2 = b.fId ");
            ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
            ds.add("where a.fStatus = 1 ");// 1 划转成功
            ds.add("order by a.fCreateTime desc ");
            ds.open();
            if (!ds.eof()) {
                for (Record record : ds) {
                    list.add(record.getItems());
                }
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Map<String, Object>> virtualTransformSucList1() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
            ds.add("from %s a left join %s b ", "ftransform", "fuser");
            ds.add("on a.fUs_fId2 = b.fId ");
            ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
            ds.add("where a.fStatus = 1 ");// 1 划转成功
            ds.add("order by a.fCreateTime desc ");
            ds.open();
            if (!ds.eof()) {
                for (Record record : ds) {
                    list.add(record.getItems());
                }
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping("ssadmin/virtualFtransformSucList")
    public ModelAndView virtualSucList() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualFtransformSucList");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        // 当前页
        int currentPage = 1;
        int total = 0;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        String fStatus = request.getParameter("fStatus");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ");
        if ("0".equals(fStatus) || fStatus == null) {
            filterSQL.append(" a.fStatus <> 0 \n");
            modelAndView.addObject("fStatus", 0);
        } else if ("1".equals(fStatus)) {
            filterSQL.append(" a.fStatus = 1 \n");
            modelAndView.addObject("fStatus", 1);
        } else if ("2".equals(fStatus)) {
            filterSQL.append(" a.fStatus = 2 \n");
            modelAndView.addObject("fStatus", 2);
        }

        /*
         * filterSQL.append("where 1=1 and ftype=" +
         * VirtualCapitalOperationTypeEnum.COIN_OUT + "\n"); filterSQL.append(
         * "and fstatus IN(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess +
         * ")\n");
         */
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filterSQL.append("and b.fid =" + fid + " \n");
            } catch (Exception e) {
                filterSQL.append("and (b.floginName like '%" + keyWord + "%' OR \n");
                filterSQL.append("b.fnickName like '%" + keyWord + "%' OR \n");
                filterSQL.append("b.frealName like '%" + keyWord + "%' ) \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and c.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);
        Map statusMap = new HashMap();
        statusMap.put(0, "全部");
        statusMap.put(1, "划转成功");
        statusMap.put(2, "划转失败");
        modelAndView.addObject("statusMap", statusMap);
        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
            ds.add("from %s a left join %s b ", "ftransform", "fuser");
            ds.add("on a.fUs_fId2 = b.fId ");
            ds.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
            // ds.add("where a.fStatus = 1 ");// 1 划转成功
            ds.add(filterSQL.toString());
            ds.add("order by a.fCreateTime desc ");
            ds.setOffset((currentPage - 1) * numPerPage);
            ds.setMaximum(numPerPage);
            ds.open();
            if (!ds.eof()) {
                for (Record record : ds) {
                    list.add(record.getItems());
                }
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select a.*,b.fid uid,b.floginName,b.fnickName,b.frealName,c.fname");
            ds1.add("from %s a left join %s b ", "ftransfer", "fuser");
            ds1.add("on a.fUs_fId2 = b.fId ");
            ds1.add("left join %s c on a.fVi_fId2=c.fId", "fvirtualcointype");
            // ds.add("where a.fStatus = 1 ");// 1 划转成功
            ds1.add(filterSQL.toString());
            ds1.add("order by a.fCreateTime desc ");
            ds1.open();
            total = ds.size();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelAndView.addObject("ftransferList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalOutSucList");
        // 总数量
        modelAndView.addObject("totalCount", total);
        return modelAndView;
    }

    public static void main(String[] args) {
        VirtualCapitaloperationController vs = new VirtualCapitaloperationController();
        // vs.otherTransferAuditing("admin",);
    }
}