package com.huagu.vcoin.main.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.EntrustTypeEnum;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CapitalOperationInStatus;
import com.huagu.vcoin.main.Enum.CapitalOperationOutStatus;
import com.huagu.vcoin.main.Enum.CapitalOperationTypeEnum;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationInStatusEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationOutStatusEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationTypeEnum;
import com.huagu.vcoin.main.auto.AutoCheckBalance;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.CapitaloperationService;
import com.huagu.vcoin.main.service.admin.EntrustService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.admin.VirtualCapitaloperationService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.admin.VirtualWalletService;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDate;
import cn.cerc.jdb.core.TDateTime;
import net.sf.json.JSONArray;

@Controller
public class ReportController extends BaseController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private CapitaloperationService capitaloperationService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private VirtualCapitaloperationService virtualCapitaloperationService;
    @Autowired
    private EntrustService entrustService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private VirtualCoinService virtualCoinService;
    // 每页显示多少条数据
    private int numPerPage = 500;

    @RequestMapping("ssadmin/totalReport")
    public ModelAndView totalReport() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.setViewName("ssadmin/totalReport");
        // 总会员数
        modelAndView.addObject("totalUser", this.adminService.getAllCount("Fuser", "where fstatus=1"));
        // 总认证数
        modelAndView.addObject("totalValidateUser", this.adminService.getAllCount("Fuser", "where fhasRealValidate=1"));

        // 今天注册数
        modelAndView.addObject("todayTotalUser", this.adminService.getAllCount("Fuser",
                // "where
                // date_format(fregisterTime,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')"));
                "where fregisterTime >= curdate() "));
        // 今天认证数
        modelAndView.addObject("todayValidateUser", this.adminService.getAllCount("Fuser",
                // "where
                // date_format(fhasRealValidateTime,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')"));
                "where fhasRealValidateTime >=curdate() "));

        // 全站总币数量
        List<Map> virtualQtyList = this.virtualWalletService.getTotalQty();
        modelAndView.addObject("virtualQtyList", virtualQtyList);

        // 今日充值总金额
        Map amountInMap = this.capitaloperationService.getTotalAmountIn(CapitalOperationTypeEnum.RMB_IN,
                "(" + CapitalOperationInStatus.Come + ")", true);
        Map totalAmountInMap = this.capitaloperationService.getTotalAmountIn(CapitalOperationTypeEnum.RMB_IN,
                "(" + CapitalOperationInStatus.Come + ")", false);
        modelAndView.addObject("amountInMap", amountInMap);
        modelAndView.addObject("totalAmountInMap", totalAmountInMap);

        // String s1 = "SELECT sum(famount) from foperationlog where fstatus=2 and
        // date_format(fcreatetime,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')";
        String s1 = "SELECT sum(famount) from foperationlog where fstatus=2 and fcreatetime >= curdate() ";
        String s2 = "SELECT sum(famount) from foperationlog where fstatus=2";
        double todayOpAmt = this.adminService.getSQLValue(s1);
        double totalOpAmt = this.adminService.getSQLValue(s2);
        modelAndView.addObject("todayOpAmt", todayOpAmt);
        modelAndView.addObject("totalOpAmt", totalOpAmt);

        // 今日提现总金额
        Map amountOutMap = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                "(" + CapitalOperationOutStatus.OperationSuccess + ")", true);
        Map amountOutMap1 = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                "(" + CapitalOperationOutStatus.OperationSuccess + ")", false);
        modelAndView.addObject("amountOutMap", amountOutMap);
        modelAndView.addObject("amountOutMap1", amountOutMap1);

        // 所有提现未转帐总金额
        String coStatus = "(" + CapitalOperationOutStatus.WaitForOperation + ","
                + CapitalOperationOutStatus.OperationLock + ")";
        Map amountOutWaitingMap = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                coStatus, false);
        modelAndView.addObject("amountOutWaitingMap", amountOutWaitingMap);

        // 今日充值虚拟币总数量
        List virtualInMap = this.virtualCapitaloperationService.getTotalAmount(VirtualCapitalOperationTypeEnum.COIN_IN,
                "(" + VirtualCapitalOperationInStatusEnum.SUCCESS + ")", true);
        modelAndView.addObject("virtualInMap", virtualInMap);

        // 今日提现虚拟币
        List virtualOutSuccessMap = this.virtualCapitaloperationService.getTotalAmount(
                VirtualCapitalOperationTypeEnum.COIN_OUT,
                "(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ")", true);
        modelAndView.addObject("virtualOutSuccessMap", virtualOutSuccessMap);

        // 累计提现虚拟币
        List virtualOutSuccessTotalMap = this.virtualCapitaloperationService.getTotalAmount(
                VirtualCapitalOperationTypeEnum.COIN_OUT,
                "(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ")", false);
        modelAndView.addObject("virtualOutSuccessTotalMap", virtualOutSuccessTotalMap);

        // 今日提现虚拟币
        List virtualOutFeesMap = this.virtualCapitaloperationService.getTotalFees(
                VirtualCapitalOperationTypeEnum.COIN_OUT,
                "(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ")", true);
        modelAndView.addObject("virtualOutFeesMap", virtualOutFeesMap);

        // 累计提现虚拟币
        List virtualOutFeesTotalMap = this.virtualCapitaloperationService.getTotalFees(
                VirtualCapitalOperationTypeEnum.COIN_OUT,
                "(" + VirtualCapitalOperationOutStatusEnum.OperationSuccess + ")", false);
        modelAndView.addObject("virtualOutFeesTotalMap", virtualOutFeesTotalMap);

        // 等待提现虚拟币
        String voStatus = "(" + VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
                + VirtualCapitalOperationOutStatusEnum.OperationLock + ")";

        List virtualOutWaitingMap = this.virtualCapitaloperationService
                .getTotalAmount(VirtualCapitalOperationTypeEnum.COIN_OUT, voStatus, false);
        modelAndView.addObject("virtualOutWaitingMap", virtualOutWaitingMap);

        // 当前委托买入
        List entrustBuyMap = this.entrustService.getTotalQty(EntrustTypeEnum.BUY);
        modelAndView.addObject("entrustBuyMap", entrustBuyMap);

        // 当前委托卖出
        List entrustSellMap = this.entrustService.getTotalQty(EntrustTypeEnum.SELL);
        modelAndView.addObject("entrustSellMap", entrustSellMap);

        // 今日提现总手续费
        Map amountOutFeeMap1 = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                "(" + CapitalOperationOutStatus.OperationSuccess + ")", true, true);
        Map amountOutFeeMap2 = this.capitaloperationService.getTotalAmount(CapitalOperationTypeEnum.RMB_OUT,
                "(" + CapitalOperationOutStatus.OperationSuccess + ")", false, true);
        modelAndView.addObject("amountOutFeeMap1", amountOutFeeMap1);
        modelAndView.addObject("amountOutFeeMap2", amountOutFeeMap2);

        List entrustBuyFeesMap1 = this.entrustService.getTotalQty(EntrustTypeEnum.BUY, true);
        List entrustBuyFeesMap2 = this.entrustService.getTotalQty(EntrustTypeEnum.BUY, false);
        modelAndView.addObject("entrustBuyFeesMap1", entrustBuyFeesMap1);
        modelAndView.addObject("entrustBuyFeesMap2", entrustBuyFeesMap2);

        List entrustSellFeesMap1 = this.entrustService.getTotalQty(EntrustTypeEnum.SELL, true);
        List entrustSellFeesMap2 = this.entrustService.getTotalQty(EntrustTypeEnum.SELL, false);
        modelAndView.addObject("entrustSellFeesMap1", entrustSellFeesMap1);
        modelAndView.addObject("entrustSellFeesMap2", entrustSellFeesMap2);

        // 累计手续费
        double entrustSellFeesMap11 = this.entrustService.getTotalSumFees(EntrustTypeEnum.SELL, true);
        double entrustSellFeesMap22 = this.entrustService.getTotalSumFees(EntrustTypeEnum.SELL, false);
        modelAndView.addObject("entrustSellFeesMap11", entrustSellFeesMap11);
        modelAndView.addObject("entrustSellFeesMap22", entrustSellFeesMap22);

        modelAndView.addObject("rel", "totalReport");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/userReport")
    public ModelAndView userReport() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userReport");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if ((startDate == null || startDate.trim().length() == 0)
                && (endDate == null || endDate.trim().length() == 0)) {
            Calendar cal_1 = Calendar.getInstance();
            // 设置为1号,当前日期既为本月第一天
            cal_1.set(Calendar.DAY_OF_MONTH, 1);
            startDate = sdf.format(cal_1.getTime());
            Calendar cale = Calendar.getInstance();
            // 设置为1号,当前日期既为本月第一天
            cale.set(Calendar.DATE, cale.getActualMaximum(Calendar.DATE));
            endDate = sdf.format(cale.getTime());
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(endDate));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        if (startDate != null && startDate.trim().length() > 0) {
            // filter.append("and date_format(Fregistertime,'%Y-%m-%d') >='" + startDate +
            // "' \n");
            filter.append("and Fregistertime >= curdate() and  curdate() >='" + startDate + "' \n");
        }
        if (endDate != null && endDate.trim().length() > 0) {
            // filter.append("and date_format(Fregistertime,'%Y-%m-%d') <='" + endDate + "'
            // \n");
            filter.append("and Fregistertime <= curdate() and  curdate() <='" + endDate + "' \n");
        }
        List all = this.userService.getUserGroup(filter + "");
        double total = 0;
        JSONArray key = new JSONArray();
        JSONArray value = new JSONArray();

        if (all != null && all.size() > 0) {
            Iterator it = all.iterator();
            while (it.hasNext()) {
                Object[] o = (Object[]) it.next();
                key.add(o[1]);
                value.add(o[0]);
                total = total + Double.valueOf(o[0].toString());
            }
        }
        modelAndView.addObject("key", key);
        modelAndView.addObject("value", value);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @RequestMapping("/ssadmin/capitaloperationReport")
    public ModelAndView capitaloperationReport() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(request.getParameter("url"));
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if ((startDate == null || startDate.trim().length() == 0)
                && (endDate == null || endDate.trim().length() == 0)) {
            Calendar cal_1 = Calendar.getInstance();
            // 设置为1号,当前日期既为本月第一天
            cal_1.set(Calendar.DAY_OF_MONTH, 1);
            startDate = sdf.format(cal_1.getTime());
            Calendar cale = Calendar.getInstance();
            // 设置为1号,当前日期既为本月第一天
            cale.set(Calendar.DATE, cale.getActualMaximum(Calendar.DATE));
            endDate = sdf.format(cale.getTime());
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        // filter.append("and date_format(fLastUpdateTime,'%Y-%m-%d') >='" + startDate +
        // "' \n");
        filter.append("and fLastUpdateTime >= curdate() and curdate() >='" + startDate + "' \n");
        // filter.append("and date_format(fLastUpdateTime,'%Y-%m-%d') <='" + endDate +
        // "' \n");
        filter.append("and fLastUpdateTime <= curdate() and curdate() <='" + endDate + "' \n");
        filter.append("and ftype=" + request.getParameter("type") + "\n");
        filter.append("and fstatus =" + request.getParameter("status") + "\n");
        List all = this.capitaloperationService.getTotalGroup(filter + "");

        double total = 0;
        JSONArray key = new JSONArray();
        JSONArray value = new JSONArray();

        if (all != null && all.size() > 0) {
            Iterator it = all.iterator();
            while (it.hasNext()) {
                Object[] o = (Object[]) it.next();
                key.add(o[1]);
                value.add(o[0]);
                total = total + Double.valueOf(o[0].toString());
            }
        }

        modelAndView.addObject("key", key);
        modelAndView.addObject("value", value);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @RequestMapping("/ssadmin/vcOperationReport")
    public ModelAndView vcOperationReport() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JspPage modelAndView = new JspPage(request);
        List<Fvirtualcointype> allType = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        modelAndView.addObject("allType", allType);
        if (request.getParameter("vid") != null) {
            int vid = Integer.parseInt(request.getParameter("vid"));
            Fvirtualcointype coinType = this.virtualCoinService.findById(vid);
            modelAndView.addObject("vid", vid);
            modelAndView.addObject("coinType", coinType.getFname());
        }
        modelAndView.setViewName(request.getParameter("url"));
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if ((startDate == null || startDate.trim().length() == 0)
                && (endDate == null || endDate.trim().length() == 0)) {
            Calendar cal_1 = Calendar.getInstance();
            // 设置为1号,当前日期既为本月第一天
            cal_1.set(Calendar.DAY_OF_MONTH, 1);
            startDate = sdf.format(cal_1.getTime());
            Calendar cale = Calendar.getInstance();
            // 设置为1号,当前日期既为本月第一天
            cale.set(Calendar.DATE, cale.getActualMaximum(Calendar.DATE));
            endDate = sdf.format(cale.getTime());
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        filter.append("and fLastUpdateTime >= curdate() and curdate() >='" + startDate + "' \n");
        filter.append("and fLastUpdateTime <= curdate() and curdate() <='" + endDate + "' \n");
        filter.append("and ftype=" + request.getParameter("type") + "\n");
        filter.append("and fstatus =" + request.getParameter("status") + "\n");
        filter.append("and fVi_fId2 =" + request.getParameter("vid") + "\n");
        List all = null;

        if (request.getParameter("vid") != null) {
            all = this.virtualCapitaloperationService.getTotalGroup(filter + "");
        }

        double total = 0;
        JSONArray key = new JSONArray();
        JSONArray value = new JSONArray();

        if (all != null && all.size() > 0) {
            Iterator it = all.iterator();
            while (it.hasNext()) {
                Object[] o = (Object[]) it.next();
                key.add(o[1]);
                value.add(o[0]);
                total = total + Double.valueOf(o[0].toString());
            }
        }

        modelAndView.addObject("key", key);
        modelAndView.addObject("value", value);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @RequestMapping("/ssadmin/dailyReconciliation")
    public ModelAndView dailyReconciliation() {
        ModelAndView modelAndView = new ModelAndView();
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String ftype = request.getParameter("ftype");
        int currentPage = 1;
        int totalCount = 0;
        if (StringUtils.isNotEmpty(request.getParameter("pageNum"))) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map<Integer, Object> typeMap = new HashMap<>();
        typeMap.put(0, "全部");
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(new Date().getTime());
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select f.*,c.fShortName from %s f", "ffinancestatement");
            ds.add("left join fvirtualcointype c on c.fid = f.fCoinType");
            if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
                ds.add("where f.fStatisticsTime between '%s' and '%s'", startDate, endDate + " 23:59:59");
            } else if (StringUtils.isNotEmpty(startDate)) {
                ds.add("where f.fStatisticsTime >= '%s'", startDate);
            } else if (StringUtils.isNotEmpty(endDate)) {
                ds.add("where f.fStatisticsTime <= '%s'", endDate + " 23:59:59");
            } else {
                ds.add("where f.fStatisticsTime between '%s' and '%s' ", currentDate, currentDate + " 23:59:59");
            }
            if (ftype != null && !ftype.equals("0")) {
                ds.add("and f.fCoinType = '%s'", ftype);
            }
            totalCount = ds.open().size();
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add(ds.getCommandText());
            ds1.setOffset((currentPage - 1) * Utils.getNumPerPage());
            ds1.setMaximum(Utils.getNumPerPage());
            ds1.open();
            for (Record record : ds1) {
                dataList.add(record.getItems());
            }
        }
        modelAndView.addObject("typeMap", typeMap);
        modelAndView.addObject("dataList", dataList);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        modelAndView.addObject("ftype", ftype);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("numPerPage", Utils.getNumPerPage());
        modelAndView.setViewName("ssadmin/dailyReconciliation");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/doAutoCheckBalance")
    @ResponseBody
    public ModelAndView doAutoCheckBalance() {
        JspPage modelAndView = new JspPage(request);
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        TDateTime dateFrom;
        TDateTime dateTo;
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            dateFrom = TDate.Today();
            dateTo = TDate.Today().incDay(-1);
        } else {
            dateFrom = TDateTime.fromDate(startDate);
            dateTo = TDateTime.fromDate(endDate);
        }
        AutoCheckBalance.countHistory(dateTo, dateFrom);
        modelAndView.addObject("message", "统计成功");
        modelAndView.addObject("statusCode", 200);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/transferRecord")
    public ModelAndView transferRecord() {
        ModelAndView modelAndView = new ModelAndView();
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String ftype = request.getParameter("ftype");
        int currentPage = 1;
        int totalCount = 0;
        if (StringUtils.isNotEmpty(request.getParameter("pageNum"))) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map<Integer, Object> typeMap = new HashMap<>();
        typeMap.put(0, "全部");
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select t.*,c.fShortName from %s t", "fwallettransferrecord");
            ds.add("left join %s c on t.fCoinType = c.fId", "fvirtualcointype");
            ds.add("where 1=1");
            if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
                ds.add("and t.fCreateTime between '%s' and '%s'", startDate, endDate + " 23:59:59");
            } else if (StringUtils.isNotEmpty(startDate)) {
                ds.add("and t.fCreateTime >= '%s'", startDate);
            } else if (StringUtils.isNotEmpty(endDate)) {
                ds.add("and t.fCreateTime <= '%s'", endDate + " 23:59:59");
            }
            if (ftype != null && !ftype.equals("0")) {
                ds.add("and t.fCoinType = '%s'", ftype);
            }
            totalCount = ds.open().size();
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add(ds.getCommandText());
            ds1.setOffset((currentPage - 1) * Utils.getNumPerPage());
            ds1.setMaximum(Utils.getNumPerPage());
            ds1.open();
            for (Record record : ds1) {
                dataList.add(record.getItems());
            }
        }
        modelAndView.addObject("typeMap", typeMap);
        modelAndView.addObject("dataList", dataList);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        modelAndView.addObject("ftype", ftype);
        modelAndView.addObject("pageNum", currentPage);
        modelAndView.addObject("numPerPage", Utils.getNumPerPage());
        modelAndView.setViewName("ssadmin/transferRecordList");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/transformRecord")
    public ModelAndView transformRecord() {
        ModelAndView modelAndView = new ModelAndView();
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String ftype = request.getParameter("ftype");
        int currentPage = 1;
        int totalCount = 0;
        if (StringUtils.isNotEmpty(request.getParameter("pageNum"))) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map<Integer, Object> typeMap = new HashMap<>();
        typeMap.put(0, "全部");
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select t.*,c.fShortName from %s t", "fwallettransferrecord");
            ds.add("left join %s c on t.fCoinType = c.fId", "fvirtualcointype");
            ds.add("where 1=1");
            if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
                ds.add("and t.fCreateTime between '%s' and '%s'", startDate, endDate + " 23:59:59");
            } else if (StringUtils.isNotEmpty(startDate)) {
                ds.add("and t.fCreateTime >= '%s'", startDate);
            } else if (StringUtils.isNotEmpty(endDate)) {
                ds.add("and t.fCreateTime <= '%s'", endDate + " 23:59:59");
            }
            if (ftype != null && !ftype.equals("0")) {
                ds.add("and t.fCoinType = '%s'", ftype);
            }
            totalCount = ds.open().size();
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add(ds.getCommandText());
            ds1.setOffset((currentPage - 1) * Utils.getNumPerPage());
            ds1.setMaximum(Utils.getNumPerPage());
            ds1.open();
            for (Record record : ds1) {
                dataList.add(record.getItems());
            }
        }
        modelAndView.addObject("typeMap", typeMap);
        modelAndView.addObject("dataList", dataList);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        modelAndView.addObject("ftype", ftype);
        modelAndView.addObject("pageNum", currentPage);
        modelAndView.addObject("numPerPage", Utils.getNumPerPage());
        modelAndView.setViewName("ssadmin/transformRecordList");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/saveTransferRecord")
    public ModelAndView saveTransferRecord() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        String fCoinType = request.getParameter("fCoinType");
        String fCount = request.getParameter("fCount");
        String fType = request.getParameter("fType");
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s", "fwallettransferrecord");
            ds.setMaximum(0);
            ds.open();
            ds.append();
            ds.setField("fCoinType", fCoinType);
            ds.setField("fCount", fCount);
            ds.setField("fType", fType);
            ds.setField("fCreateUser", GetCurrentUser(request).getFloginName());
            ds.setField("fCreateTime", TDateTime.Now());
            ds.setField("fUpdateTime", TDateTime.Now());
            ds.post();
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "新增成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    private static enum ExportFiled {
        编号, 币种, 统计时间, 前一日账面余额, 前一日钱包实际余额, 前一日总差额, 账面余额, 钱包余额, 总差额, 账面充值数额, 用户钱包实际转入额, 账面提现数额, 账面申请提现中数额, 用户钱包实际转出额, 买入总额, 卖出总额, 后台手工调整数额, 财务人员手工转入数额, 财务人员手工转出数额, 更新时间, 创建时间;
    }

    private List<Map<String, Object>> getDailyReconciliationList() {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String ftype = request.getParameter("ftype");
        List<Map<String, Object>> dataList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(new Date().getTime());
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select f.*,c.fShortName from %s f", "ffinancestatement");
            ds.add("left join fvirtualcointype c on c.fid = f.fCoinType");
            if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
                ds.add("where f.fStatisticsTime between '%s' and '%s'", startDate, endDate + " 23:59:59");
            } else if (StringUtils.isNotEmpty(startDate)) {
                ds.add("where f.fStatisticsTime >= '%s'", startDate);
            } else if (StringUtils.isNotEmpty(endDate)) {
                ds.add("where f.fStatisticsTime <= '%s'", endDate + " 23:59:59");
            } else {
                ds.add("where f.fStatisticsTime between '%s' and '%s' ", currentDate, currentDate + " 23:59:59");
            }
            if (ftype != null && !ftype.equals("0")) {
                ds.add("and f.fCoinType = '%s'", ftype);
            }
            ds.open();
            for (Record record : ds) {
                dataList.add(record.getItems());
            }
        }
        return dataList;
    }

    @RequestMapping("ssadmin/dailyReconciliationExport")
    public void dailyReconciliationExport(HttpServletResponse response) throws Exception {
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=dailyReconciliation.xls");
        XlsExport e = new XlsExport();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int rowIndex = 0;
        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> dailyReconciliationList = getDailyReconciliationList();
        for (Map<String, Object> map : dailyReconciliationList) {
            e.createRow(rowIndex++);
            for (ExportFiled filed : ExportFiled.values()) {
                switch (filed) {
                case 编号:
                    e.setCell(filed.ordinal(), (int) map.get("fId"));
                    break;
                case 币种:
                    e.setCell(filed.ordinal(), (String) map.get("fShortName"));
                    break;
                case 统计时间:
                    e.setCell(filed.ordinal(), format.format((Date) map.get("fStatisticsTime")));
                    break;
                case 前一日账面余额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fBeforeBookBalance")));
                    break;
                case 前一日钱包实际余额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fBeforeWalletBalance")));
                    break;
                case 前一日总差额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fBeforeDifference")));
                    break;
                case 账面余额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCurrentBookBalance")));
                    break;
                case 钱包余额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCurrentWalletBalance")));
                    break;
                case 总差额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCurrentDifference")));
                    break;
                case 账面充值数额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fBookRecharge")));
                    break;
                case 用户钱包实际转入额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fWalletRecharge")));
                    break;
                case 账面提现数额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fBookWithdrawal")));
                    break;
                case 账面申请提现中数额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fBookWithdrawaling")));
                    break;
                case 用户钱包实际转出额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fWalletWithdrawal")));
                    break;
                case 买入总额:
                    e.setCell(filed.ordinal(), (int) map.get("fBuyOrderSum"));
                    break;
                case 卖出总额:
                    e.setCell(filed.ordinal(), (int) map.get("fSellOrderSum"));
                    break;
                case 后台手工调整数额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fManualAdjustment")));
                    break;
                case 财务人员手工转入数额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fHandIn")));
                    break;
                case 财务人员手工转出数额:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fHandOut")));
                    break;
                case 更新时间:
                    e.setCell(filed.ordinal(), format.format((Date) map.get("updateDate_")));
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), format.format((Date) map.get("createDate_")));
                    break;
                default:
                    break;
                }
            }
        }

        e.exportXls(response);
        response.getOutputStream().close();
    }

    @RequestMapping("/ssadmin/userWalletReport")
    public ModelAndView viewUserWallet() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userWalletReport");

        List<Map<String, Object>> allLists = new ArrayList<Map<String, Object>>();
        // 当前页
        int currentPage = 1;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        String keywords = request.getParameter("keywords");
        StringBuffer sqlString = new StringBuffer();
        sqlString.append("SELECT * from totalView where 1=1 \n");
        // if (keywords != null && keywords.trim().length() > 0) {
        // try {
        // int userid = Integer.parseInt(keywords);
        // sqlString.append(" and fuser.fuid=" + userid + " \n");
        // } catch (Exception e) {
        // sqlString.append(" and (floginName like '%" + keywords + "%' or \n");
        // sqlString.append(" frealName like '%" + keywords + "%' or \n");
        // sqlString.append(" ftelephone like '%" + keywords + "%') \n");
        // }
        // modelAndView.addObject("keywords", keywords);
        // }
        // String coin = request.getParameter("coin");
        // if (coin != null && coin.trim().length() > 0) {
        // if (!coin.equals("全部")) {
        // sqlString.append(" and coinName ='" + coin + "' \n");
        // }
        // modelAndView.addObject("coin", coin);
        // }
        sqlString.append("and (ftotal >0 or ffrozen >0 or regAmt >0 or wAmt >0 or buy >0 or sell >0) \n");
        sqlString.append(" order by fuid desc,ftotal desc,ffrozen desc,regAmt desc,wAmt desc,buy desc,sell desc \n");
        List xx = this.adminService.getSQLList(sqlString.toString());
        int totalNum = xx.size();
        sqlString.append("limit " + (currentPage - 1) * numPerPage + "," + numPerPage + " \n");

        List all = this.adminService.getSQLList(sqlString.toString());
        if (all != null && all.size() > 0) {
            for (int i = 0; i < all.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                Object[] objects = (Object[]) all.get(i);
                String uid = objects[0].toString();
                String loginName = objects[1].toString();
                String realName = objects[2] + "";
                String telephone = objects[3] + "";
                String coinName = objects[4].toString();
                String total = objects[5].toString();
                String frozen = objects[6].toString();
                String regAmt = objects[7].toString();
                String wAmt = objects[8].toString();
                String buy = objects[9].toString();
                String sell = objects[10].toString();

                map.put("uid", uid);
                map.put("loginName", loginName);
                map.put("realName", realName);
                map.put("telephone", telephone);
                map.put("coinName", coinName);
                map.put("total", total);
                map.put("frozen", frozen);
                map.put("regAmt", regAmt);
                map.put("wAmt", wAmt);
                map.put("buy", buy);
                map.put("sell", sell);

                allLists.add(map);
            }
        }

        List<String> allList = new ArrayList<>();
        allList.add("全部");
        List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
        for (Fvirtualcointype fvirtualcointype : allType) {
            allList.add(fvirtualcointype.getFname());
        }
        modelAndView.addObject("allList", allList);

        modelAndView.addObject("userList", allLists);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "viewUserWallet");
        // 总数量
        modelAndView.addObject("totalCount", totalNum);
        return modelAndView;
    }

    @RequestMapping("/ssadmin/queryCoinNumber")
    public ModelAndView queryCoinNumber() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/queryCoinNumberList");

        int currentPage = 1;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        List<Fvirtualcointype> allType = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        modelAndView.addObject("allType", allType);
        int num = 10;
        int ftype = 1;
        if (request.getParameter("ftype") != null) {
            ftype = Integer.parseInt(request.getParameter("ftype"));
            modelAndView.addObject("ftype", ftype);
        }
        if (request.getParameter("number") != null) {
            num = Integer.parseInt(request.getParameter("number"));
            modelAndView.addObject("number", num);
        }

        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.setMaximum(num);
            ds.add("select fu.fId,fu.floginName,fu.fNickName,ft.fName,fu.fRealName,fv.fTotal+fv.fFrozen as fTotal,fu.fIdentityNo from %s fv",
                    "fvirtualwallet");
            ds.add("left join %s fu on fv.fuid=fu.fId", "fuser");
            ds.add("left join %s ft on fv.fVi_fId=ft.fId", "fvirtualcointype");
            ds.add("where fv.fVi_fId=%s", ftype);
            ds.add(" and fv.fuid != '881007072'");
            ds.add("order by fv.fTotal desc");
            ds.open();

            modelAndView.addObject("coinNumberList", ds.getRecords());
            modelAndView.addObject("totalCount", ds.getRecords().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        return modelAndView;
    }

}
