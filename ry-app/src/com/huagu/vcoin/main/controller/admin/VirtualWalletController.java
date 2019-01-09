package com.huagu.vcoin.main.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.admin.VirtualWalletService;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import cn.cerc.jdb.core.Record;

@Controller
public class VirtualWalletController extends BaseController {
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/virtualwalletList")
    public ModelAndView Index() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualwalletList");
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
        filter.append(" and fuser.fid not in (0) \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
                filter.append(" fuser.fnickName like '%" + keyWord + "%') \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }
        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(flastUpdateTime,'%Y-%m-%d') = '" +
            // logDate +
            // "' \n");
            filter.append("and  flastUpdateTime >= '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }

//        filter.append(" and fvirtualcointype.ftype <>" + CoinTypeEnum.FB_CNY_VALUE + " \n");

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

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
        List<Fvirtualwallet> list = this.virtualWalletService.list((currentPage - 1) * numPerPage, numPerPage,
                filter + "", true);
        modelAndView.addObject("virtualwalletList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualwalletList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fvirtualwallet", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/virtualwalletChangeList")
    public ModelAndView change() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualwalletChangeList");
        // 当前页
        int currentPage = 1;
        int totalCount = 0;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        String userId = request.getParameter("userId");// 用户id
        String fShortName = request.getParameter("fShortName");// 币种
        String changeDate = request.getParameter("changeDate");// 变更时间

        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select w.uid_,w.userId_,w.coinId_,w.total_,w.frozen_,w.totalChange_,w.frozenChange_,w.changeReason_,w.changeDate_,w.guid_,w.taskId_,c.fShortName,"
                    + "w.entrustId_ from %s w left join %s c on c.fId=w.coinId_", "t_walletlog", "fvirtualcointype");
            ds.add("where 1=1");
            if (StringUtils.isNotEmpty(userId)) {
                ds.add("and userId_ = '%s'", userId);
                modelAndView.addObject("userId", userId);
            }
            if (StringUtils.isNotEmpty(fShortName)) {
                if (!"全部".equals(fShortName)) {
                    ds.add("and c.fShortName like '%s'", new String("%" + fShortName + "%"));
                    modelAndView.add("fShortName", fShortName);
                }
            }
            if (changeDate != null && changeDate.trim().length() > 0) {
                ds.add("and changeDate_ like '%s'", new String("%" + changeDate + "%"));
                modelAndView.addObject("changeDate", changeDate);
            }
            ds.setOffset((currentPage - 1) * numPerPage);
            ds.setMaximum(numPerPage);
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }

            // 查询总条数
            /*
             * MyQuery cds = new MyQuery(mysql); cds.add(
             * "select w.uid_,w.userId_,w.coinId_,w.total_,w.frozen_,w.totalChange_,w.frozenChange_,w.changeReason_,w.changeDate_,w.guid_,w.taskId_,c.fShortName,"
             * + "w.entrustId_ from %s w left join %s c on c.fId=w.coinId_", "t_walletlog",
             * "fvirtualcointype"); cds.add("where 1=1");
             * 
             * if (StringUtils.isNotEmpty(userId)) { cds.add( "and userId_ = '%s'", userId);
             * } if (StringUtils.isNotEmpty(fShortName)) { cds.add(
             * "and c.fShortName like '%s'", new String("%" + fShortName + "%")); } if
             * (changeDate != null && changeDate.trim().length() > 0) {
             * cds.add("and changeDate_ like '%s'", new String("%" + changeDate + "%")); }
             * cds.open(); totalCount = cds.size();
             */
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (StringUtils.isNotEmpty(userId)) {
            filter.append(" and w.userId_ =" + userId + "\n");
        }
        // " and (fuser.frealName like '%" + keyWord + "%') \n"
        if (StringUtils.isNotEmpty(fShortName)) {
            filter.append(" and (c.fShortName like '%" + fShortName + "%') \n");
        }
        if (changeDate != null && changeDate.trim().length() > 0) {
            filter.append(" and changeDate_ like '%" + changeDate + "%'\n");
        }
        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
        }
        typeMap.put("", "全部");
        modelAndView.addObject("typeMap", typeMap);
        modelAndView.addObject("virtualwalletChangeList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "virtualwalletChangeList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount",
                this.virtualWalletService.getAllCounts("t_walletlog", "fvirtualcointype", filter + ""));
        return modelAndView;
    }

    private static enum ExportFiled {
        登录名, 会员昵称, 会员真实姓名, 币种类型, 总数量, 冻结数量;
    }

    private List<Fvirtualwallet> getUserList() {
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
            filter.append("fuser.fnickName like '%" + keyWord + "%' or \n");
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("fuser.fid =" + fid + " or \n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            filter.append("fuser.frealName like '%" + keyWord + "%') \n");
        }

        filter.append("and (ftotal >0 or ffrozen >0) \n");

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and fvirtualcointype.fid=" + type + "\n");
            }
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
        List<Fvirtualwallet> list = this.virtualWalletService.list(0, 0, filter.toString(), false);
        return list;
    }

    @RequestMapping("ssadmin/virtualwalletExport")
    public ModelAndView virtualwalletExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=virtualwalletExport.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }

        List<Fvirtualwallet> lists = getUserList();
        for (Fvirtualwallet fvirtualwallet : lists) {
            e.createRow(rowIndex++);
            for (ExportFiled filed : ExportFiled.values()) {
                switch (filed) {
                case 登录名:
                    e.setCell(filed.ordinal(), fvirtualwallet.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    e.setCell(filed.ordinal(), fvirtualwallet.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    e.setCell(filed.ordinal(), fvirtualwallet.getFuser().getFrealName());
                    break;
                case 币种类型:
                    e.setCell(filed.ordinal(), fvirtualwallet.getFvirtualcointype().getFname());
                    break;
                case 总数量:
                    e.setCell(filed.ordinal(), fvirtualwallet.getFtotal());
                    break;
                case 冻结数量:
                    e.setCell(filed.ordinal(), fvirtualwallet.getFfrozen());
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "导出成功");
        return modelAndView;
    }

}
