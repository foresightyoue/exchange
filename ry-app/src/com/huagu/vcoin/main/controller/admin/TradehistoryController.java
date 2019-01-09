package com.huagu.vcoin.main.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Ftradehistory;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.TradehistoryService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import cn.cerc.jdb.core.Record;

@Controller
public class TradehistoryController extends BaseController {
    @Autowired
    private TradehistoryService tradehistoryService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/tradehistoryList")
    @Transactional
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/tradehistoryList");
        // 当前页
        int currentPage = 1;
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and ftrademapping.fvirtualcointypeByFvirtualcointype2.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }

        if (request.getParameter("ftype1") != null) {
            int type1 = Integer.parseInt(request.getParameter("ftype1"));
            if (type1 != 0) {
                filter.append("and ftrademapping.fvirtualcointypeByFvirtualcointype1.fid=" + type1 + "\n");
            }
            modelAndView.addObject("ftype1", type1);
        } else {
            modelAndView.addObject("ftype1", 0);
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fdate,'%Y-%m-%d') = '" + logDate +
            // "' \n");
            filter.append("and fdate >= curdate() and curdate() = '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
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

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map<Integer, String> typeMap = new HashMap<Integer, String>();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        List<Fvirtualcointype> type1 = this.virtualCoinService.findAll(CoinTypeEnum.COIN_VALUE, 1);
        Map<Integer, String> typeMap1 = new HashMap<Integer, String>();
        for (Fvirtualcointype fvirtualcointype : type1) {
            typeMap1.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap1.put(0, "全部");
        modelAndView.addObject("typeMap1", typeMap1);

        List<Ftradehistory> list = this.tradehistoryService.list((currentPage - 1) * numPerPage, numPerPage,
                filter + "", true);
        modelAndView.addObject("tradehistoryList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "tradehistoryList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Ftradehistory", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/ctctradeList.html")
    @Transactional
    public ModelAndView ctctradeList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/ctctradeList");
        // 当前页
        int currentPage = 1;
        int totalCount = 0;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        String keywords = request.getParameter("keywords");
        String tradetype = request.getParameter("tradetype");
        String buyer = request.getParameter("buyer");
        String seller = request.getParameter("seller");
        String orderstatus = request.getParameter("orderstatus");

        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from ( ");
            ds.add("select o.fId,o.fCreateTime,o.fType,c.fShortName fCoinType,o.fappeal,");
            ds.add("(select floginName from fuser where fuser.fId = o.fBuyerId) fBuyerName,");
            ds.add("(select floginName from fuser where fuser.fId = o.fSellerId) fSellerName,");
            ds.add("(select floginName from fuser where fuser.fId = o.fUpdateUser) fUpdateUserName,");
            ds.add("(select floginName from fuser where fuser.fId = o.fCreateUser) fCreateUserName,");
            ds.add("o.fPrice,o.fNum,o.fCnyTotal,o.fConfirmTime,o.fStatus,o.fremark,o.fOrderNum from %s o", "fctcorder");
            ds.add("left join %s c on c.fId = o.fCoinType", "fvirtualcointype");
            ds.add(") t ");
            ds.add("where 1=1");

            if (StringUtils.isNotEmpty(keywords)) {
                ds.add("and t.fId = '%s'", keywords);
                modelAndView.addObject("keywords", keywords);
            }
            if (!"-1".equals(tradetype) && StringUtils.isNotEmpty(tradetype)) {
                ds.add("and t.fType = '%s'", tradetype);
                modelAndView.addObject("tradetype", tradetype);
            }
            if (StringUtils.isNotEmpty(buyer)) {
                ds.add("and t.fBuyerName = '%s'", buyer);
                modelAndView.addObject("buyer", buyer);
            }
            if (StringUtils.isNotEmpty(seller)) {
                ds.add("and t.fSellerName = '%s'", seller);
                modelAndView.addObject("seller", seller);
            }
            if (StringUtils.isNotEmpty(orderstatus) && !"-1".equals(orderstatus)) {
                ds.add("and t.fStatus = '%s'", orderstatus);
                modelAndView.addObject("orderstatus", orderstatus);
            }
            String logDate = request.getParameter("logDate");
            if (logDate != null && logDate.trim().length() > 0) {
                // ds.add("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') >= '" +
                // logDate + "' \n");
                ds.add("and fcreateTime >= curdate() and curdate() >= '" + logDate + "' \n");
                modelAndView.addObject("logDate", logDate);
            }

            String logDate2 = request.getParameter("logDate2");
            if (logDate2 != null && logDate2.trim().length() > 0) {
                // ds.add("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') <= '" +
                // logDate2 + "' \n");
                ds.add("and  fcreateTime <= curdate() and curdate() <= '" + logDate2 + "' \n");
                modelAndView.addObject("logDate2", logDate2);
            }
            ds.setOffset((currentPage - 1) * numPerPage);
            ds.setMaximum(numPerPage);
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
            MyQuery cds = new MyQuery(mysql);
            cds.add("select * from ( ");
            cds.add("select o.fId,o.fCreateTime,o.fType,c.fShortName fCoinType,o.fappeal,");
            cds.add("(select floginName from fuser where fuser.fId = o.fBuyerId) fBuyerName,");
            cds.add("(select floginName from fuser where fuser.fId = o.fSellerId) fSellerName,");
            cds.add("(select floginName from fuser where fuser.fId = o.fUpdateUser) fUpdateUserName,");
            cds.add("(select floginName from fuser where fuser.fId = o.fCreateUser) fCreateUserName,");
            cds.add("o.fPrice,o.fNum,o.fCnyTotal,o.fConfirmTime,o.fStatus,o.fremark,o.fOrderNum from %s o",
                    "fctcorder");
            cds.add("left join %s c on c.fId = o.fCoinType", "fvirtualcointype");
            cds.add(") t ");
            cds.add("where 1=1");

            if (StringUtils.isNotEmpty(keywords)) {
                cds.add("and t.fId = '%s'", keywords);
            }
            if (!"-1".equals(tradetype) && StringUtils.isNotEmpty(tradetype)) {
                cds.add("and t.fType = '%s'", tradetype);
            }
            if (StringUtils.isNotEmpty(buyer)) {
                cds.add("and t.fBuyerName = '%s'", buyer);
            }
            if (StringUtils.isNotEmpty(seller)) {
                cds.add("and t.fSellerName = '%s'", seller);
            }
            if (StringUtils.isNotEmpty(orderstatus) && !"-1".equals(orderstatus)) {
                cds.add("and t.fStatus = '%s'", orderstatus);
            }
            String logDate1 = request.getParameter("logDate");
            if (logDate1 != null && logDate1.trim().length() > 0) {
                // ds.add("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') >= '" +
                // logDate + "' \n");
                cds.add("and fcreateTime >= curdate() and curdate() >= '" + logDate1 + "' \n");
            }

            String logDate22 = request.getParameter("logDate2");
            if (logDate22 != null && logDate22.trim().length() > 0) {
                // ds.add("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') <= '" +
                // logDate2 + "' \n");
                cds.add("and  fcreateTime <= curdate() and curdate() <= '" + logDate22 + "' \n");
            }
            cds.open();
            totalCount = cds.size();
        }
        modelAndView.addObject("ctctradeList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "ctctradeList");
        modelAndView.addObject("currentPage", currentPage);
        /*
         * modelAndView.addObject("keywords", keywords);
         * modelAndView.addObject("tradetype", tradetype);
         * modelAndView.addObject("orderstatus", orderstatus);
         * modelAndView.addObject("buyer", buyer); modelAndView.addObject("seller",
         * seller);
         */
        // 总数量
        modelAndView.addObject("totalCount", totalCount);
        return modelAndView;
    }

    private static enum ExportFiled {
        订单号, 交易币种, 订单创建者, 类型, 买方, 卖方, 单价, 数量, 总金额, 状态, 下单时间, 订单最后改动用户, 确认时间, 备注
    }

    @SuppressWarnings("unused")
    private List<Map<String, Object>> ctcList() {
        String keywords = request.getParameter("keywords");
        String tradetype = request.getParameter("tradetype");
        String buyer = request.getParameter("buyer");
        String seller = request.getParameter("seller");
        String orderstatus = request.getParameter("orderstatus");

        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from ( ");
            ds.add("select o.fId,o.fCreateTime,o.fType,c.fShortName fCoinType,o.fappeal,");
            ds.add("(select floginName from fuser where fuser.fId = o.fBuyerId) fBuyerName,");
            ds.add("(select floginName from fuser where fuser.fId = o.fSellerId) fSellerName,");
            ds.add("(select floginName from fuser where fuser.fId = o.fUpdateUser) fUpdateUserName,");
            ds.add("(select floginName from fuser where fuser.fId = o.fCreateUser) fCreateUserName,");
            ds.add("o.fPrice,o.fNum,o.fCnyTotal,o.fConfirmTime,o.fStatus,o.fremark from %s o", "fctcorder");
            ds.add("left join %s c on c.fId = o.fCoinType", "fvirtualcointype");
            ds.add(") t ");
            ds.add("where 1=1");

            if (StringUtils.isNotEmpty(keywords)) {
                ds.add("and t.fId = '%s'", keywords);
            }
            if (!"-1".equals(tradetype) && StringUtils.isNotEmpty(tradetype)) {
                ds.add("and t.fType = '%s'", tradetype);
            }
            if (StringUtils.isNotEmpty(buyer)) {
                ds.add("and t.fBuyerName = '%s'", buyer);
            }
            if (StringUtils.isNotEmpty(seller)) {
                ds.add("and t.fSellerName = '%s'", seller);
            }
            if (StringUtils.isNotEmpty(orderstatus) && !"-1".equals(orderstatus)) {
                ds.add("and t.fStatus = '%s'", orderstatus);
            }
            String logDate = request.getParameter("logDate");
            if (logDate != null && logDate.trim().length() > 0) {
                // ds.add("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') >= '" +
                // logDate + "' \n");
                ds.add("and fcreateTime >= curdate() and curdate() >= '" + logDate + "' \n");
            }

            String logDate2 = request.getParameter("logDate2");
            if (logDate2 != null && logDate2.trim().length() > 0) {
                // ds.add("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') <= '" +
                // logDate2 + "' \n");
                ds.add("and  fcreateTime <= curdate() and curdate() <= '" + logDate2 + "' \n");
            }
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        return list;

    }

    @RequestMapping("ssadmin/ctctradeExport")
    public ModelAndView ctctradeExport(HttpServletResponse response) throws IOException {
        JspPage modelAndView = new JspPage(request);
        String[] status = new String[] { "待派单", "待确认", "交易完成", "申诉中", "已取消" };
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=c2cList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> c2cList = ctcList();
        for (Map<String, Object> map : c2cList) {
            e.createRow(rowIndex++);
            for (ExportFiled filed : ExportFiled.values()) {
                switch (filed) {
                case 订单号:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fId")));
                    break;
                case 交易币种:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCoinType")));
                    break;
                case 订单创建者:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCreateUserName")));
                    break;
                case 类型:
                    if ("0".equals(String.valueOf(map.get("fType")))) {
                        e.setCell(filed.ordinal(), "买入");
                    } else {
                        e.setCell(filed.ordinal(), "卖出");
                    }
                    break;
                case 买方:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fBuyerName")));
                    break;
                case 卖方:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fSellerName")));
                    break;
                case 单价:
                    e.setCell(filed.ordinal(), String.valueOf(((double) map.get("fPrice"))));
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fNum")));
                    break;
                case 总金额:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fCnyTotal")));
                    break;
                case 状态:
                    if ("0".equals(map.get("fappeal"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(map.get("fStatus")))]);
                    } else if ("2".equals(map.get("fappeal"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(map.get("fStatus")))]);
                    } else if ("1".equals(map.get("fappeal"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(map.get("fStatus")))]);
                    }
                    break;
                case 下单时间:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCnyTotal")));
                    break;
                case 订单最后改动用户:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fUpdateUserName")));
                    break;
                case 确认时间:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fConfirmTime")));
                    break;
                case 备注:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fremark")));
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
}
