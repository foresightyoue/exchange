package com.huagu.vcoin.main.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.EntrustStatusEnum;
import com.huagu.coa.common.cons.EntrustTypeEnum;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fentrust;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.EntrustService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSender;
import com.huagu.vcoin.main.service.front.FrontTradeService;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import site.jayun.vcoin.bourse.core.BourseEngine;

@Controller
public class EntrustController extends BaseController {
    @Autowired
    private EntrustService entrustService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private MessageSender messageSender;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/entrustList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/entrustList");
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
                filter.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filter.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filter.append("fuser.fnickName like '%" + keyWord + "%' ) \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

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

        String status = request.getParameter("status");
        if (status != null && status.trim().length() > 0) {
            if (!status.equals("0")) {
                filter.append("and fstatus=" + status + " \n");
            }
            modelAndView.addObject("status", status);
        } else {
            modelAndView.addObject("status", 0);
        }

        String category = request.getParameter("category");
        if (category != null && category.trim().length() > 0) {
            if (!category.equals("-1")) {
                filter.append("and isrobotType=" + category + " \n");
            }
            modelAndView.addObject("category", category);
        } else {
            modelAndView.addObject("category", -1);
        }

        String entype = request.getParameter("entype");
        if (entype != null && entype.trim().length() > 0) {
            if (!entype.equals("-1")) {
                filter.append("and fentrustType=" + entype + " \n");
            }
            modelAndView.addObject("entype", entype);
        } else {
            modelAndView.addObject("entype", -1);
        }

        try {
            String price = request.getParameter("price");
            if (price != null && price.trim().length() > 0) {
                double p = Double.valueOf(price);
                filter.append("and fprize >=" + p + " \n");
            }
            modelAndView.addObject("price", price);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String price = request.getParameter("price1");
            if (price != null && price.trim().length() > 0) {
                double p = Double.valueOf(price);
                filter.append("and fprize <=" + p + " \n");
            }
            modelAndView.addObject("price1", price);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d HH:mm:ss')
            // >= '" +
            // logDate + "' \n");
            filter.append("and  fcreateTime >=  '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate1 = request.getParameter("logDate1");
        if (logDate1 != null && logDate1.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d HH:mm:ss')
            // <= '" +
            // logDate1 + "' \n");
            filter.append("and  fcreateTime <= '" + logDate1 + "' \n");
            modelAndView.addObject("logDate1", logDate1);
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

        Map<Integer, String> categoryMap = new HashMap<Integer, String>(); // 交易类别
        categoryMap.put(0, "机器人交易");
        categoryMap.put(1, "普通用户交易");
        categoryMap.put(-1, "全部");
        modelAndView.addObject("categoryMap", categoryMap);

        Map<Integer, String> statusMap = new HashMap<Integer, String>();
        statusMap.put(EntrustStatusEnum.AllDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.AllDeal));
        statusMap.put(EntrustStatusEnum.Cancel, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Cancel));
        statusMap.put(EntrustStatusEnum.Going, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Going));
        statusMap.put(EntrustStatusEnum.PartDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.PartDeal));
        statusMap.put(0, "全部");
        modelAndView.addObject("statusMap", statusMap);

        Map<Integer, String> entypeMap = new HashMap<Integer, String>();
        entypeMap.put(EntrustTypeEnum.BUY, EntrustTypeEnum.getEnumString(EntrustTypeEnum.BUY));
        entypeMap.put(EntrustTypeEnum.SELL, EntrustTypeEnum.getEnumString(EntrustTypeEnum.SELL));
        entypeMap.put(-1, "全部");
        modelAndView.addObject("entypeMap", entypeMap);

        // double fees = this.adminService.getSQLValue2("select sum(ffees) from
        // Fentrust" + filter.toString());
        Mysql mysql = new Mysql();
        MyQuery sx = new MyQuery(mysql);
        sx.add("select sum(linshi.ffees) as sumffees from (");
        // sx.add("SELECT ffees from %s " + filter.toString(), AppDB.fentrust);
        sx.add(" select f.fid,f.ffees from %s f left join %s fuser on ", AppDB.fentrust, AppDB.Fuser);
        sx.add(" f.FUs_fId = fuser.fId");
        sx.add(" where 1=1");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                sx.add("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                sx.add("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                sx.add("fuser.frealName like '%" + keyWord + "%' OR \n");
                sx.add("fuser.fnickName like '%" + keyWord + "%' ) \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }
        if (request.getParameter("ftype1") != null) {
            int type3 = Integer.parseInt(request.getParameter("ftype1"));
            if (type3 != 0) {
                filter.append("and ftrademapping.fvirtualcointypeByFvirtualcointype1.fid=" + type3 + "\n");
            }
            modelAndView.addObject("ftype1", type3);
        } else {
            modelAndView.addObject("ftype1", 0);
        }
        if (status != null && status.trim().length() > 0) {
            if (!status.equals("0")) {
                filter.append("and f.fstatus=" + status + " \n");
            }
            modelAndView.addObject("status", status);
        } else {
            modelAndView.addObject("status", 0);
        }
        if (category != null && category.trim().length() > 0) {
            if (!category.equals("-1")) {
                sx.add("and f.isrobotType=" + category + " \n");
            }
            modelAndView.addObject("category", category);
        } else {
            modelAndView.addObject("category", -1);
        }
        if (entype != null && entype.trim().length() > 0) {
            if (!entype.equals("-1")) {
                sx.add("and f.fentrustType=" + entype + " \n");
            }
            modelAndView.addObject("entype", entype);
        } else {
            modelAndView.addObject("entype", -1);
        }
        try {
            String price = request.getParameter("price");
            if (price != null && price.trim().length() > 0) {
                double p = Double.valueOf(price);
                sx.add("and f.fprize >=" + p + " \n");
            }
            modelAndView.addObject("price", price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String price = request.getParameter("price1");
            if (price != null && price.trim().length() > 0) {
                double p = Double.valueOf(price);
                sx.add("and f.fprize <=" + p + " \n");
            }
            modelAndView.addObject("price1", price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (logDate != null && logDate.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d HH:mm:ss')
            // >= '" +
            // logDate + "' \n");
            sx.add("and  f.fcreateTime >=  '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }
        if (logDate1 != null && logDate1.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d HH:mm:ss')
            // <= '" +
            // logDate1 + "' \n");
            sx.add("and  f.fcreateTime <= '" + logDate1 + "' \n");
            modelAndView.addObject("logDate1", logDate1);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            sx.add("order by " + orderField + "\n");
        } else {
            sx.add("order by fid \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            sx.add(orderDirection + "\n");
        } else {
            sx.add("desc \n");
        }
        sx.add("limit "+(currentPage - 1) * numPerPage+","+numPerPage);
        sx.add(") linshi");
        sx.open();
        double totalAmt = this.adminService.getSQLValue2("select sum(fleftCount) from Fentrust " + filter.toString());
        double totalQty = this.adminService
                .getSQLValue2("select sum(fsuccessAmount) from Fentrust " + filter.toString());

        modelAndView.addObject("fees", Utils.getDouble(sx.getCurrent().getDouble("sumffees"), 2));
        modelAndView.addObject("totalAmt", Utils.getDouble(totalAmt, 2));
        modelAndView.addObject("totalQty", Utils.getDouble(totalQty, 2));

        List<Fentrust> list = this.entrustService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("entrustList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "entrustList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fentrust", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/cancelEntrust")
    public ModelAndView cancelEntrust() throws Exception {
        String ids = request.getParameter("ids");
        String[] idString = ids.split(",");
        try (Mysql handle = new Mysql()) {
            BourseEngine engine = new BourseEngine(handle);
            for (int i = 0; i < idString.length; i++) {
                try {
                    int orderId = Integer.parseInt(idString[i]);
                    String error = engine.cancelOrder(-1, orderId);
                    if (error == null)
                        this.messageSender.publish(ChannelConstant.CANCEL_ENTRUST, String.valueOf(orderId));
                    else
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "取消成功");
        return modelAndView;
    }

    private static enum Export1Filed {
        订单ID, 会员UID, 会员登录名, 会员昵称, 会员真实姓名, 法币, 交易虚拟币, 交易类型, 状态, 单价, 数量, 未成交数量, 已成交数量, 总金额, 成交总金额, 总手续费, 剩余手续费, 修改时间, 创建时间;
    }

    @RequestMapping("ssadmin/entrustExport.html")
    public ModelAndView entrustExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=entrustList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (Export1Filed filed : Export1Filed.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }

        List<Fentrust> alls = getList();
        for (Fentrust entrust : alls) {
            e.createRow(rowIndex++);
            for (Export1Filed filed : Export1Filed.values()) {
                switch (filed) {
                case 订单ID:
                    e.setCell(filed.ordinal(), entrust.getFid());
                    break;
                case 会员UID:
                    if (entrust.getFuser() != null)
                        e.setCell(filed.ordinal(), entrust.getFuser().getFid());
                    break;
                case 会员登录名:
                    if (entrust.getFuser() != null)
                        e.setCell(filed.ordinal(), entrust.getFuser().getFloginName());
                    break;
                case 会员昵称:
                    if (entrust.getFuser() != null)
                        e.setCell(filed.ordinal(), entrust.getFuser().getFnickName());
                    break;
                case 会员真实姓名:
                    if (entrust.getFuser() != null)
                        e.setCell(filed.ordinal(), entrust.getFuser().getFrealName());
                    break;
                case 法币:
                    e.setCell(filed.ordinal(),
                            entrust.getFtrademapping().getFvirtualcointypeByFvirtualcointype1().getFname());
                    break;
                case 交易虚拟币:
                    e.setCell(filed.ordinal(),
                            entrust.getFtrademapping().getFvirtualcointypeByFvirtualcointype2().getFname());
                    break;
                case 交易类型:
                    e.setCell(filed.ordinal(), entrust.getFentrustType_s());
                    break;
                case 状态:
                    e.setCell(filed.ordinal(), entrust.getFstatus_s());
                    break;
                case 单价:
                    e.setCell(filed.ordinal(), entrust.getFprize());
                    break;
                case 数量:
                    e.setCell(filed.ordinal(), entrust.getFcount());
                    break;
                case 未成交数量:
                    e.setCell(filed.ordinal(), entrust.getFleftCount());
                    break;
                case 已成交数量:
                    e.setCell(filed.ordinal(), entrust.getFcount() - entrust.getFleftCount());
                    break;
                case 总金额:
                    e.setCell(filed.ordinal(), entrust.getFamount());
                    break;
                case 成交总金额:
                    e.setCell(filed.ordinal(), entrust.getFsuccessAmount());
                    break;
                case 总手续费:
                    e.setCell(filed.ordinal(), entrust.getFfees());
                    break;
                case 剩余手续费:
                    e.setCell(filed.ordinal(), entrust.getFleftfees());
                    break;
                case 修改时间:
                    e.setCell(filed.ordinal(), entrust.getFlastUpdatTime());
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), entrust.getFcreateTime());
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

    public List<Fentrust> getList() throws Exception {
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filter.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filter.append("fuser.fnickName like '%" + keyWord + "%' ) \n");
            }
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and ftrademapping.fvirtualcointypeByFvirtualcointype2.fid=" + type + "\n");
            }
        }

        if (request.getParameter("ftype1") != null) {
            int type1 = Integer.parseInt(request.getParameter("ftype1"));
            if (type1 != 0) {
                filter.append("and ftrademapping.fvirtualcointypeByFvirtualcointype1.fid=" + type1 + "\n");
            }
        }

        String status = request.getParameter("status");
        if (status != null && status.trim().length() > 0) {
            if (!status.equals("0")) {
                filter.append("and fstatus=" + status + " \n");
            }
        }

        String entype = request.getParameter("entype");
        if (entype != null && entype.trim().length() > 0) {
            if (!entype.equals("-1")) {
                filter.append("and fentrustType=" + entype + " \n");
            }
        }

        try {
            String price = request.getParameter("price");
            if (price != null && price.trim().length() > 0) {
                double p = Double.valueOf(price);
                filter.append("and fprize >=" + p + " \n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String price = request.getParameter("price1");
            if (price != null && price.trim().length() > 0) {
                double p = Double.valueOf(price);
                filter.append("and fprize <=" + p + " \n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            filter.append("and  fcreateTime>= now() and now() >= '" + logDate + "' \n");
        }

        String logDate1 = request.getParameter("logDate1");
        if (logDate1 != null && logDate1.trim().length() > 0) {
            filter.append("and  fcreateTime <=now() and now() <= '" + logDate1 + "' \n");
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

        List<Fentrust> list = this.entrustService.list(0, 0, filter + "", false);
        return list;
    }

}
