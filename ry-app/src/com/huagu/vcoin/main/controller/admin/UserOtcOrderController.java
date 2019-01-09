package com.huagu.vcoin.main.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import cn.cerc.jbean.core.ServiceStatus;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;
import cn.cerc.jdb.other.utils;
import net.sf.json.JSONObject;

@Controller
public class UserOtcOrderController extends BaseController {

    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/userotcOrderList.html")
    public ModelAndView userotcOrderList() {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/userotcOrderList");
        // 当前页
        int currentPage = 1;
        int totalCount = 0;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        String keywords = request.getParameter("keywords");
        String userlogin = request.getParameter("userlogin");
        String fTrade_Status = request.getParameter("fTrade_Status");
        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select b.*,c.fName from t_userotcorder b left join fuser a on b.fusr_id = a.fid left join fvirtualcointype c on c.fId = b.fAm_fId");
        ds.add("where 1=1 ");
        if (StringUtils.isNotEmpty(fTrade_Status)) {
            ds.add("and b.fTrade_Status = '%s'", fTrade_Status);
            modelAndView.addObject("fTrade_Status", fTrade_Status);
        }
        if (StringUtils.isNotEmpty(keywords)) {
            ds.add("and b.fOrderId = '%s'", keywords);
            modelAndView.addObject("keywords", keywords);
        }
        if (StringUtils.isNotEmpty(userlogin)) {
            ds.add(" and ( b.fUsr_id = '%s'", userlogin);
            ds.add(" or a.floginName = '%s'", userlogin);
            ds.add(" or a.fTelephone = '%s' )", userlogin);
            modelAndView.addObject("userlogin", userlogin);
        }
        ds.add(" order by b.fOrder_Time desc");
        ds.setOffset((currentPage - 1) * numPerPage);
        ds.setMaximum(numPerPage);
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        MyQuery cds = new MyQuery(mysql);
        cds.add("select b.*,c.fName from t_userotcorder b left join fuser a on b.fusr_id = a.fid left join fvirtualcointype c on c.fId = b.fAm_fId");
        cds.add("where 1=1 ");
        if (StringUtils.isNotEmpty(fTrade_Status)) {
            cds.add("and b.fTrade_Status = '%s'", fTrade_Status);
            modelAndView.addObject("fTrade_Status", fTrade_Status);
        }
        if (StringUtils.isNotEmpty(keywords)) {
            cds.add("and b.fOrderId = '%s'", keywords);
            modelAndView.addObject("keywords", keywords);
        }
        if (StringUtils.isNotEmpty(userlogin)) {
            cds.add(" and ( b.fUsr_id = '%s'", userlogin);
            cds.add(" or a.floginName = '%s'", userlogin);
            cds.add(" or a.fTelephone = '%s' )", userlogin);
            modelAndView.addObject("userlogin", userlogin);
        }
        cds.add(" order by b.fOrder_Time desc");
        cds.open();
        totalCount = cds.size();
        modelAndView.addObject("userotcOrderList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalCount", totalCount);

        return modelAndView;
    }

    private static enum ExportFiled {
        订单id, 创建时间, 币种id, 用户账户, 日志类型, 订单号, 下单时间, 交易价格, 交易数量, 交易金额, 交易对象, 交易状态, 交易方式
    }

    @SuppressWarnings("unused")
    private List<Map<String, Object>> userotcList() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from t_userotcOrder");
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        return list;
    }

    @RequestMapping("ssadmin/userotctradeExport")
    public ModelAndView userotctradeExport(HttpServletResponse response) throws IOException {
        JspPage modelAndView = new JspPage(request);
        String[] status = new String[] { "进行中", "已结束" };
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=userotcList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> usero2cList = userotcList();
        for (Map<String, Object> map : usero2cList) {
            e.createRow(rowIndex++);
            for (ExportFiled filed : ExportFiled.values()) {
                switch (filed) {
                case 订单id:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fId")));
                    break;
                case 创建时间:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCreateTime")));
                    break;
                case 币种id:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAm_fId")));
                    break;
                case 用户账户:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fUsr_id")));

                    break;
                case 日志类型:
                    if ("0".equals(String.valueOf(map.get("fType")))) {
                        e.setCell(filed.ordinal(), "收款");
                    } else {
                        e.setCell(filed.ordinal(), "借出");
                    }
                    break;
                case 订单号:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("OrderId")));
                    break;
                case 下单时间:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fOrder_Time")));
                    break;
                case 交易价格:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fTrade_Price")));
                    break;
                case 交易数量:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fTrade_Count")));
                    break;
                case 交易金额:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fTrade_SumMoney")));
                    break;
                case 交易对象:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fTrade_Object")));
                    break;
                case 交易状态:
                    if ("0".equals(String.valueOf(map.get("fTrade_Status")))) {
                        e.setCell(filed.ordinal(), "收款");
                    } else {
                        e.setCell(filed.ordinal(), "借出");
                    }
                    break;
                case 交易方式:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fTrade_Method")));
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

    @RequestMapping("ssadmin/userotcOrdersCancel")
    public ModelAndView otcOrdersCancel(@RequestParam(required = true) String uid) throws Exception {
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
            try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
                for (int i = 0; i < fids.length; i++) {
                    int fid1 = Integer.parseInt(fids[i]);
                    MyQuery queryStatus = new MyQuery(mysql);
                    queryStatus.add("select * from %s where 1=1", "t_userotcorder");
                    queryStatus.add("and fId = %s", fid1);
                    queryStatus.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                    queryStatus.open();
                    // 已下单的状态才能撤单
                    if (queryStatus.getCurrent().getString("fTrade_Status").equals("1")
                            || queryStatus.getCurrent().getString("fTrade_Status").equals("5")) {
                        if (queryStatus.getCurrent().getString("fType").equals("1")) {// 购买
                            MyQuery buy = new MyQuery(mysql);
                            buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
                            buy.add(" WHERE fuid = '%s'", queryStatus.getCurrent().getString("fTrade_Object"));
                            buy.add(" AND fVi_fId = '%s'", queryStatus.getCurrent().getString("fAm_fId"));
                            buy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                            buy.open();
                            if (!buy.eof()) {
                                Double count = buy.getCurrent().getDouble("fTotal")
                                        + queryStatus.getCurrent().getDouble("fTrade_Count");
                                Double count1 = buy.getCurrent().getDouble("fFrozen")
                                        - queryStatus.getCurrent().getDouble("fTrade_Count");
                                buy.edit();
                                buy.setField("fTotal", count);
                                buy.setField("fFrozen", count1);
                                buy.setField("fLastUpdateTime", TDateTime.Now());
                                buy.post();
                                success++;
                            }
                            if (!queryStatus.eof()) {
                                queryStatus.edit();
                                queryStatus.setField("fTrade_Status", "4");
                                queryStatus.post();
                            }
                        } else if (queryStatus.getCurrent().getString("fType").equals("2")) {// 卖出
                            MyQuery buy = new MyQuery(mysql);
                            buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
                            buy.add(" WHERE fuid = '%s'", queryStatus.getCurrent().getString("fUsr_id"));
                            buy.add(" AND fVi_fId = '%s'", queryStatus.getCurrent().getString("fAm_fId"));
                            buy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                            buy.open();
                            if (!buy.eof()) {
                                Double count = buy.getCurrent().getDouble("fTotal")
                                        + queryStatus.getCurrent().getDouble("fTrade_Count");
                                Double count1 = buy.getCurrent().getDouble("fFrozen")
                                        - queryStatus.getCurrent().getDouble("fTrade_Count");
                                buy.edit();
                                buy.setField("fTotal", count);
                                buy.setField("fFrozen", count1);
                                buy.setField("fLastUpdateTime", TDateTime.Now());
                                buy.post();
                                success++;
                            }
                            if (!queryStatus.eof()) {
                                queryStatus.edit();
                                queryStatus.setField("fTrade_Status", "4");
                                queryStatus.post();
                            }
                        }
                    } else {
                        fail++;
                    }
                }
                tx.commit();
            } catch (Exception e) {
                jspPage.addObject("statusCode", 300);
                jspPage.addObject("message", "撤销失败" + e.getMessage());
                return jspPage;
            }
            jspPage.addObject("statusCode", 200);
            jspPage.addObject("message", "撤销成功,撤销成功数量" + success + "，失败数量" + fail);
            // jspPage.addObject("callbackType", "closeCurrent");
            return jspPage;
        }
    }

    // 查看申诉申请
    @ResponseBody
    @RequestMapping(value = "/ssadmin/otcseeAppeal", produces = JsonEncode)
    public String seeAppeal(HttpServletRequest request, @RequestParam(required = true) String orderId) {
        JSONObject json = new JSONObject();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add(" select a.fimgpath1,a.fimgpath2,a.fimgpath3,a.fremark,o.fType from %s a ", "t_userotcorderappeal");
            ds.add(" inner JOIN t_userotcorder o ");
            ds.add(" on o.fOrderId = a.fctcorderid ");
            ds.add(" where a.fctcorderid = '%s' ", orderId);
            ds.open();
            json.accumulate("imgpath1", ds.getString("fimgpath1"));
            json.accumulate("imgpath2", ds.getString("fimgpath2"));
            json.accumulate("imgpath3", ds.getString("fimgpath3"));
            json.accumulate("remark", ds.getString("fremark"));
            json.accumulate("orderId", orderId);
            json.accumulate("type", ds.getString("fType"));
        }
        return json.toString();
    }

    // 买入订单申诉
    @ResponseBody
    @RequestMapping(value = { "/ssadmin/userconfirmOrder1" }, produces = JsonEncode)
    public String confirmOrder1(HttpServletRequest request, @RequestParam(required = true) String orderId) {
        JSONObject json = new JSONObject();
        ServiceStatus serviceStatus = new ServiceStatus(false);
        Mysql mysql = new Mysql();
        MyQuery queryStatus = new MyQuery(mysql);
        queryStatus.add("select * from %s where 1=1", "t_userotcorder");
        queryStatus.add("and fOrderId = %s", orderId);
        queryStatus.open();
        	MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s where 1=1", "t_userotcorderappeal");
            ds.add("and fctcorderid = %s", orderId);
            ds.open();
            MyQuery ty = new MyQuery(mysql);
            ty.add("select * from fvirtualcointype where fId = '%s' ", queryStatus.getInt("fAm_fId"));
            ty.open();
            MyQuery hl = new MyQuery(mysql);
            hl.add("select * from %s ", "fotcfees");
            hl.add("where fvid = '%s' ", ty.getInt("fId"));
            hl.add("and flevel = 1 ");
            hl.open();
            if (ds.getString("fStatus").equals("0")) {
            // 减少卖家可用金额和冻结资金
            MyQuery sell = new MyQuery(mysql);
            sell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
            sell.add(" WHERE fuid = '%s'", queryStatus.getString("fTrade_Object"));
            sell.add(" AND fVi_fId = '%s'", queryStatus.getCurrent().getString("fAm_fId"));
            sell.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            sell.open();
            Double count = sell.getCurrent().getDouble("fFrozen")
                    - (queryStatus.getCurrent().getDouble("fTrade_Count") * (1 + hl.getDouble("ffee")));
            if (!sell.eof()) {
            	//申述订单放行增加卖家余额日志
            	MyQuery selllog = new MyQuery(mysql);
            	selllog.add("select * from  %s","t_walletlog");
            	selllog.setMaximum(1);
            	selllog.open();
            	selllog.append();
            	selllog.setField("userId_", queryStatus.getString("fTrade_Object"));
            	selllog.setField("coinId_", queryStatus.getCurrent().getString("fAm_fId")); 
            	selllog.setField("total_", sell.getDouble("fTotal"));
            	selllog.setField("frozen_", sell.getDouble("fFrozen"));
            	selllog.setField("totalChange_", queryStatus.getCurrent().getDouble("fTrade_Count") * (1 + hl.getDouble("ffee")));
            	selllog.setField("frozenChange_", queryStatus.getCurrent().getDouble("fTrade_Count") * (1 + hl.getDouble("ffee")));
            	selllog.setField("changeReason_", "OTC订单放行");
            	selllog.setField("changeDate_", TDateTime.Now());
            	selllog.setField("taskId_", queryStatus.getString("taskId_"));
            	selllog.post();
                sell.edit();
                sell.setField("fFrozen", count);
                sell.post();
            }

            // 增加买家可用金额
            MyQuery buy = new MyQuery(mysql);
            buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
            buy.add(" WHERE fuid = '%s'", queryStatus.getCurrent().getString("fUsr_id"));
            buy.add(" AND fVi_fId = '%s'", queryStatus.getCurrent().getString("fAm_fId"));
            buy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            buy.open();
            Double count1 = buy.getCurrent().getDouble("fTotal") + queryStatus.getCurrent().getDouble("fTrade_Count");
            if (!buy.eof()) {
            	//申述订单放行增加买家余额日志
            	MyQuery buglog = new MyQuery(mysql);
            	buglog.add("select * from  %s","t_walletlog");
            	buglog.setMaximum(1);
            	buglog.open();
            	buglog.append();
            	buglog.setField("userId_", queryStatus.getString("fUsr_id"));
            	buglog.setField("coinId_", queryStatus.getCurrent().getString("fAm_fId")); 
            	buglog.setField("total_", sell.getDouble("fTotal"));
            	buglog.setField("frozen_", sell.getDouble("fFrozen"));
            	buglog.setField("totalChange_", queryStatus.getCurrent().getDouble("fTrade_Count"));
            	buglog.setField("frozenChange_", queryStatus.getCurrent().getDouble("fTrade_Count"));
            	buglog.setField("changeReason_", "OTC订单放行");
            	buglog.setField("changeDate_", TDateTime.Now());
            	buglog.setField("taskId_", queryStatus.getString("taskId_"));
            	buglog.post();
                buy.edit();
                buy.setField("fTotal", count1);
                buy.post();
            }
            MyQuery de = new MyQuery(mysql);
            de.add("select * from %s " ,"ffee_detail");
            de.setMaximum(1);
            MyQuery ye = new MyQuery(mysql);
            ye.add("select * from %s ", "fvirtualwallet");
            ye.add("where fuid = '%s' ", queryStatus.getInt("fTrade_Object"));
            ye.add("and fVi_fId = '%s' " ,queryStatus.getInt("fAm_fId"));
            ye.open();
            de.append();
            de.setField("fUser_id", queryStatus.getInt("fTrade_Object"));
            de.setField("fCoin_id", queryStatus.getInt("fAm_fId"));
            de.setField("ffee", hl.getDouble("ffee"));
            de.setField("fOrder_type", 2);
            de.setField("ffee_time", TDateTime.Now());
            de.setField("fOrder_object", queryStatus.getInt("fUsr_id"));
            de.setField("fTrade_type", queryStatus.getString("fType"));
            de.setField("fTrade_count", queryStatus.getDouble("fTrade_Count"));
            de.setField("ffee_sum", queryStatus.getDouble("fTrade_Count") * hl.getDouble("ffee"));
            de.setField("fLevel", 1);
            de.setField("fTotal", ye.getDouble("fTotal"));
            de.setField("fFrozen", ye.getDouble("fFrozen"));
            de.setField("guid", utils.newGuid());
            de.post();
            json.accumulate("code", 0);
        } else if (ds.getString("fStatus").equals("1")) {
            // 减少卖家可用金额和冻结资金
            MyQuery sell = new MyQuery(mysql);
            sell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
            sell.add(" WHERE fuid = '%s'", queryStatus.getString("fUsr_id"));
            sell.add(" AND fVi_fId = '%s'", queryStatus.getCurrent().getString("fAm_fId"));
            sell.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            sell.open();
            Double count = sell.getCurrent().getDouble("fFrozen") - queryStatus.getCurrent().getDouble("fTrade_Count");
            if (!sell.eof()) {
            	//申述订单放行增加卖家余额日志
            	MyQuery selllog = new MyQuery(mysql);
            	selllog.add("select * from  %s","t_walletlog");
            	selllog.setMaximum(1);
            	selllog.open();
            	selllog.append();
            	selllog.setField("userId_", queryStatus.getString("fUsr_id"));
            	selllog.setField("coinId_", queryStatus.getCurrent().getString("fAm_fId")); 
            	selllog.setField("total_", sell.getDouble("fTotal"));
            	selllog.setField("frozen_", sell.getDouble("fFrozen"));
            	selllog.setField("totalChange_", queryStatus.getCurrent().getDouble("fTrade_Count"));
            	selllog.setField("frozenChange_", queryStatus.getCurrent().getDouble("fTrade_Count"));
            	selllog.setField("changeReason_", "OTC订单放行");
            	selllog.setField("changeDate_", TDateTime.Now());
            	selllog.setField("taskId_", queryStatus.getString("taskId_"));
            	selllog.post();
                sell.edit();
                sell.setField("fFrozen", count);
                sell.post();
            }

            // 增加买家可用金额
            MyQuery buy = new MyQuery(mysql);
            buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
            buy.add(" WHERE fuid = '%s'", queryStatus.getCurrent().getString("fTrade_Object"));
            buy.add(" AND fVi_fId = '%s'", queryStatus.getCurrent().getString("fAm_fId"));
            buy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            buy.open();
            Double count1 = buy.getCurrent().getDouble("fTotal")
                    + (queryStatus.getCurrent().getDouble("fTrade_Count") * (1 - hl.getDouble("ffee")));
            if (!buy.eof()) {
            	//申述订单放行增加买家余额日志
            	MyQuery buglog = new MyQuery(mysql);
            	buglog.add("select * from  %s","t_walletlog");
            	buglog.setMaximum(1);
            	buglog.open();
            	buglog.append();
            	buglog.setField("userId_", queryStatus.getString("fTrade_Object"));
            	buglog.setField("coinId_", queryStatus.getCurrent().getString("fAm_fId")); 
            	buglog.setField("total_", sell.getDouble("fTotal"));
            	buglog.setField("frozen_", sell.getDouble("fFrozen"));
            	buglog.setField("totalChange_", queryStatus.getCurrent().getDouble("fTrade_Count")* (1 - hl.getDouble("ffee")));
            	buglog.setField("frozenChange_", queryStatus.getCurrent().getDouble("fTrade_Count")* (1 - hl.getDouble("ffee")));
            	buglog.setField("changeReason_", "OTC订单放行");
            	buglog.setField("changeDate_", TDateTime.Now());
            	buglog.setField("taskId_", queryStatus.getString("taskId_"));
            	buglog.post();
                buy.edit();
                buy.setField("fTotal", count1);
                buy.post();
            }
            MyQuery de = new MyQuery(mysql);
            de.add("select * from %s " ,"ffee_detail");
            de.setMaximum(1);
            MyQuery ye = new MyQuery(mysql);
            ye.add("select * from %s ", "fvirtualwallet");
            ye.add("where fuid = '%s' ", queryStatus.getInt("fTrade_Object"));
            ye.add("and fVi_fId = '%s' " ,queryStatus.getInt("fAm_fId"));
            ye.open();
            de.append();
            de.setField("fUser_id", queryStatus.getInt("fTrade_Object"));
            de.setField("fCoin_id", queryStatus.getInt("fAm_fId"));
            de.setField("ffee", hl.getDouble("ffee"));
            de.setField("fOrder_type", 2);
            de.setField("ffee_time", TDateTime.Now());
            de.setField("fOrder_object", queryStatus.getInt("fUsr_id"));
            de.setField("fTrade_type", queryStatus.getString("fType"));
            de.setField("fTrade_count", queryStatus.getDouble("fTrade_Count"));
            de.setField("ffee_sum", queryStatus.getDouble("fTrade_Count") * hl.getDouble("ffee"));
            de.setField("fLevel", 1);
            de.setField("fTotal", ye.getDouble("fTotal"));
            de.setField("fFrozen", ye.getDouble("fFrozen"));
            de.setField("guid", utils.newGuid());
            de.post();
            json.accumulate("code", 0);
        } else {
            json.accumulate("code", -1);
            json.accumulate("msg", serviceStatus.getMessage());
        }
        return json.toString();
    }

    // 买入订单申诉成功，修改订单
    @ResponseBody
    @RequestMapping(value = "/ssadmin/usersuccessAppeal", produces = JsonEncode)
    public String successAppeal(HttpServletRequest request, @RequestParam(required = true) String orderId) {
        JSONObject resultJson = new JSONObject();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add(" select * from %s  ", "t_userotcorder");
            ds.add(" where fOrderId = '%s' ", orderId);
            ds.open();
            String fTrade_Status = ds.getString("fTrade_Status");
            if("3".equals(fTrade_Status)){
            	resultJson.accumulate("code", 1);
            }else{
            	 if (!ds.eof()) {
                     ds.edit();
                     ds.setField("fTrade_Status", 3);// 订单状态改成订单完成
                     ds.post();
                     resultJson.accumulate("code", 0);
                 } else {
                     resultJson.accumulate("code", 1);
                 }
            }
        }
        return resultJson.toString();
    }

    // 买入订单取消申诉
    @ResponseBody
    @RequestMapping(value = "/ssadmin/usererrorAppeal", produces = JsonEncode)
    public String errorAppeal(HttpServletRequest request, @RequestParam(required = true) String orderId) {
        JSONObject resultJson = new JSONObject();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add(" select * from %s  ", "t_userotcorder");
            ds.add(" where fOrderId = '%s' ", orderId);
            ds.open();
            if (!ds.eof()) {
                ds.edit();
                ds.setField("fTrade_Status", 2);// 订单状态改成待验证
                ds.post();
                resultJson.accumulate("code", 0);
            } else {
                resultJson.accumulate("code", 1);
            }
        }
        return resultJson.toString();
    }

}