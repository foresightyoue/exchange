package com.huagu.vcoin.main.controller.front;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.sms.ShortMessageService;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.ServerConfig;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;
import cn.cerc.jdb.other.utils;
import net.sf.json.JSONObject;

@Controller
public class FrontOtcOrderController extends BaseController {

    @ResponseBody
    @RequestMapping(value = { "/otc/orderPay", "/m/otc/orderPay" }, produces = JsonEncode)
    public String otcOrder(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        String fId = request.getParameter("fId");
        String coin_id = request.getParameter("coin_id");
        String usrId = request.getParameter("userId");
        String payMethod = request.getParameter("payMethod");
        String buyPrice = request.getParameter("buyPrice");
        String sum_money = request.getParameter("sum_money");
        // String limit_money = request.getParameter("limit_money");
        String buy_counts = request.getParameter("buy_counts");
        String type = request.getParameter("type");
        String fShortName = request.getParameter("fShortName");
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery ds = new MyQuery(handle);
            ds.add("select fId,fCreateTime,fAd_Id,fAm_fId,fUsr_id,fType,fOrderId,fOrder_Time,fTrade_Price,fTrade_Count,fTrade_SumMoney,fTrade_DeadTime,"
                    + "fTrade_Object,fTrade_Status,fTrade_Method,taskId_ from %s", "t_userOtcOrder");
            ds.open();
            ds.append();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String taskId = utils.newGuid();
            Calendar nowTime = Calendar.getInstance();
            long orderId = System.currentTimeMillis();
            nowTime.add(Calendar.MINUTE, 45);
            ds.setField("fCreateTime", TDateTime.Now());
            ds.setField("fAm_fId", coin_id);
            ds.setField("fAd_Id", fId);
            ds.setField("fUsr_id", currentUser.getFid());
            ds.setField("fOrderId", orderId);
            ds.setField("fOrder_Time", TDateTime.Now());
            ds.setField("fTrade_DeadTime", sdf.format(nowTime.getTime()));
            ds.setField("fTrade_Price", buyPrice);
            ds.setField("fTrade_Count", buy_counts);
            ds.setField("fTrade_SumMoney", sum_money);
            ds.setField("fTrade_Object", usrId);
            ds.setField("fTrade_Status", "1");
            ds.setField("taskId_", taskId);
            if (type.equals("1")) {
                ds.setField("fType", "1");
            } else {
                ds.setField("fType", "2");
            }
            ds.setField("fTrade_Method", payMethod);
            ds.post();
            // 执行自动取消订单
            timeOrder(String.valueOf(orderId));
            if (type.equals("1")) {
                // 购买的时候冻结卖家的资产
                MyQuery dsSell = new MyQuery(handle);
                dsSell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s WHERE 1=1",
                        "fvirtualwallet");
                dsSell.add(" and fuid = '%s'", usrId);
                dsSell.add(" and fVi_fId = '%s'", coin_id);
                dsSell.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                dsSell.open();
                MyQuery ty = new MyQuery(handle);
                ty.add("select * from fvirtualcointype where fShortName = '%s' ", fShortName);
                ty.open();
                MyQuery hl = new MyQuery(handle);
                hl.add("select * from %s ", "fotcfees");
                hl.add("where fvid = '%s' ", ty.getInt("fId"));
                hl.add("and flevel = 1");
                hl.open();
                if (!dsSell.eof()) {
                    // 向t_walletlog表里增加日志记录
                	MyQuery selllog = new MyQuery(handle);
                	selllog.add("select * from %s ","t_walletlog");
                	selllog.setMaximum(1);
                	selllog.open();
                	selllog.append();
                	selllog.setField("userId_", usrId);
                	selllog.setField("coinId_", coin_id);
                	selllog.setField("total_", dsSell.getDouble("fTotal"));
                	selllog.setField("frozen_", dsSell.getDouble("fFrozen"));
                	selllog.setField("totalChange_", Double.parseDouble(buy_counts) * (1 + hl.getDouble("ffee")));
                	selllog.setField("frozenChange_", Double.parseDouble(buy_counts) * (1 + hl.getDouble("ffee")));
                    selllog.setField("changeReason_", "OTC购买");
                	selllog.setField("changeDate_", TDateTime.Now());
                	selllog.setField("taskId_", taskId);
                	selllog.post();
                    double fTotal = dsSell.getDouble("fTotal");
                    double fTotalNew = fTotal - (Double.parseDouble(buy_counts) * (1 + hl.getDouble("ffee")));
                    double fFrozen = dsSell.getDouble("fFrozen");
                    double fFrozenNew = fFrozen + (Double.parseDouble(buy_counts) * (1 + hl.getDouble("ffee")));
                    dsSell.edit();
                    dsSell.setField("fLastUpdateTime", TDateTime.Now());
                    dsSell.setField("fTotal", fTotalNew);
                    dsSell.setField("fFrozen", fFrozenNew);
                    dsSell.post();
                }
            } else if (type.equals("2")) {
                // 出售的时候冻结自己的资产
                MyQuery dsBuy = new MyQuery(handle);
                dsBuy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s WHERE 1=1", "fvirtualwallet");
                dsBuy.add(" and fuid = '%s'", currentUser.getFid());
                dsBuy.add(" and fVi_fId = '%s'", coin_id);
                dsBuy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                dsBuy.open();
                if (!dsBuy.eof()) {
                    // 向t_walletlog表里增加日志记录
                	MyQuery buylog = new MyQuery(handle);
                	buylog.add("select * from %s ","t_walletlog");
                    buylog.setMaximum(1);
                    buylog.open();
                    buylog.append();
                    buylog.setField("userId_", currentUser.getFid());
                    buylog.setField("coinId_", coin_id);
                    buylog.setField("total_", dsBuy.getDouble("fTotal"));
                    buylog.setField("frozen_", dsBuy.getDouble("fFrozen"));
                    buylog.setField("totalChange_", Double.parseDouble(buy_counts));
                    buylog.setField("frozenChange_", Double.parseDouble(buy_counts));
                    buylog.setField("changeReason_", "OTC出售");
                    buylog.setField("changeDate_", TDateTime.Now());
                    buylog.setField("taskId_", taskId);
                    buylog.post();
                    double fTotal = dsBuy.getDouble("fTotal");
                    double fTotalNew = fTotal - Double.parseDouble(buy_counts);
                    double fFrozen = dsBuy.getDouble("fFrozen");
                    double fFrozenNew = fFrozen + Double.parseDouble(buy_counts);
                    dsBuy.edit();
                    dsBuy.setField("fLastUpdateTime", TDateTime.Now());
                    dsBuy.setField("fTotal", fTotalNew);
                    dsBuy.setField("fFrozen", fFrozenNew);
                    dsBuy.post();
                }
            }
            /*
             * MyQuery ds1 = new MyQuery(handle);
             * ds1.add("select fId,fStatus,fUsr_id from %s where 1=1","t_otcorders");
             * ds1.add("and fId='%s'",fId); ds1.open(); if(!ds1.eof()){ ds1.edit();
             * ds1.setField("fStatus", "1"); ds1.post(); }
             */

            MyQuery user = new MyQuery(handle);
            user.add("select u.fTelephone from %s o ", "t_otcorders");
            user.add("left join %s u on ", AppDB.Fuser);
            user.add("u.fId=o.fUsr_id ");
            user.add("where o.fId =%s", fId);
            user.open();
            String phone = user.getString("fTelephone");
            String msg = null;
            int ordertype = user.getInt("fOrdertype");
            if (ordertype == 0) {
                msg = String.format("【瑞银钱包】您的广告于%s有新订单，订单号为：%s。", TDateTime.Now().toString(), orderId);
            } else if (ordertype == 1) {
                msg = String.format("【瑞银钱包】您的卖单于%s有新订单，订单号为：%s。", TDateTime.Now().toString(), orderId);
            }
            ShortMessageService.send(phone, msg);

            tx.commit();
            resultJson.accumulate("code", 0);
            resultJson.accumulate("msg", "下单成功！");
        } catch (Exception e) {
            resultJson.accumulate("code", 1);
            resultJson.accumulate("msg", "下单失败！");
        }
        return resultJson.toString();
    }

    // OTC买家订单列表
    @ResponseBody
    @RequestMapping(value = { "/otc/orderList", "/m/otc/orderList" })
    public ModelAndView orderList(HttpServletRequest request,
        @RequestParam(required = false, defaultValue = "1") int currentPage) {
        JspPage ModelAndView = new JspPage(request);
        String fw = request.getParameter("fw");
        String coin_id = request.getParameter("coinType");
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        List<Map<String, Object>> orderListIng = new ArrayList<>();
        List<Map<String, Object>> orderListOver = new ArrayList<>();
        int maxNum = 8;
        // 进行中
        int totalListIng = 0;
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sql.add("DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sql.add("fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fTrade_Object=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sql.add("and o.fUsr_id='%s'", currentUser.getFid());
            sql.add("and (fTrade_Status = '1' or fTrade_Status = '2' or fTrade_Status = '5')");
            sql.add("order by fOrder_Time desc");
            sql.setMaximum(maxNum);
            sql.setOffset((currentPage - 1) * maxNum);
            sql.open();
            for (Record record : sql) {
                orderListIng.add(record.getItems());
            }
            MyQuery sqltotal = new MyQuery(handle);
            sqltotal.add(
                    "select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sqltotal.add(
                    "DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sqltotal.add(
                    "fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fTrade_Object=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sqltotal.add("and o.fUsr_id='%s'", currentUser.getFid());
            sqltotal.add("and (fTrade_Status = '1' or fTrade_Status = '2' or fTrade_Status = '5')");
            sqltotal.add("order by fOrder_Time desc");
            sqltotal.open();
            totalListIng = sqltotal.getRecords().size();
        } catch (Exception e) {
            ModelAndView.addObject("code", 200);
            ModelAndView.addObject("message", "获取失败");
        }
        int page_countsIng = 1;
        int page_finalIng = 1;
        if (totalListIng % maxNum > 0) {
            page_finalIng = totalListIng / maxNum + 1;
            page_countsIng = totalListIng / maxNum + 1;
        } else {
            page_finalIng = totalListIng / maxNum;
            page_countsIng = totalListIng / maxNum;
        }
        // 已完成的
        int totalListOver = 0;
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sql.add("DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sql.add("fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fTrade_Object=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sql.add("and o.fUsr_id='%s'", currentUser.getFid());
            sql.add("and (fTrade_Status = '3' or fTrade_Status = '4' or fTrade_Status = '6')");
            sql.add("order by fOrder_Time desc");
            sql.setMaximum(maxNum);
            sql.setOffset((currentPage - 1) * maxNum);
            sql.open();
            for (Record record : sql) {
                orderListOver.add(record.getItems());
            }
            MyQuery sqltotal = new MyQuery(handle);
            sqltotal.add(
                    "select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sqltotal.add(
                    "DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sqltotal.add(
                    "fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fTrade_Object=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sqltotal.add("and o.fUsr_id='%s'", currentUser.getFid());
            sqltotal.add("and (fTrade_Status = '3' or fTrade_Status = '4' or fTrade_Status = '6')");
            sqltotal.add("order by fOrder_Time desc");
            sqltotal.open();
            totalListOver = sqltotal.getRecords().size();
        } catch (Exception e) {
            ModelAndView.addObject("code", 200);
            ModelAndView.addObject("message", "获取失败");
        }
        int page_countsOver = 1;
        int page_finalOver = 1;
        if (totalListOver % maxNum > 0) {
            page_finalOver = totalListOver / maxNum + 1;
            page_countsOver = totalListOver / maxNum + 1;
        } else {
            page_finalOver = totalListOver / maxNum;
            page_countsOver = totalListOver / maxNum;
        }
        ModelAndView.addObject("orderListOver", orderListOver);
        ModelAndView.addObject("page_countsOver", page_countsOver);
        ModelAndView.addObject("page_finalOver", page_finalOver);
        ModelAndView.addObject("totalListOver", totalListOver);
        ModelAndView.addObject("fw",fw);
        ModelAndView.addObject("coin_id", coin_id);
        ModelAndView.addObject("orderListIng", orderListIng);
        ModelAndView.addObject("page_countsIng", page_countsIng);
        ModelAndView.addObject("page_finalIng", page_finalIng);
        ModelAndView.addObject("totalListIng", totalListIng);
        ModelAndView.addObject("currentPage", currentPage);
        // ModelAndView.setViewName("front/otc/orderList");
        ModelAndView.setJspFile("front/otc/orderList");
        return ModelAndView;
    }

    // 去下单
    @ResponseBody
    @RequestMapping(value = { "/otc/toOrder", "/m/otc/toOrder" }, produces = JsonEncode)
    public ModelAndView toOrder(HttpServletRequest request) {
        JspPage ModelAndView = new JspPage(request);
        String fId = request.getParameter("fId");
        String type = request.getParameter("type");
        String coin_id = request.getParameter("coin_id");
        String jiaoyi = request.getParameter("jiaoyi");
        String haoping = request.getParameter("haoping");
        String userId = request.getParameter("userId");
        String payMethod = request.getParameter("payMethod");
        String limit_money = request.getParameter("limit_money").replace("CNY", "").trim();
        String buyPrice = request.getParameter("buyPrice").replace("CNY", "").trim();
        String fShortName = "";
        String floginName = "";
        String fMsg = "";
        double buyfTotal;
        double sellfTotal;
        double ffee;
        try (Mysql handle = new Mysql()) {
            // 当前登录用户
            Fuser currentUser = GetCurrentUser(request);
            MyQuery dd = new MyQuery(handle);
            dd.add(" select o.fId,o.fMsg from %s o", "t_otcorders");
            dd.add(" where o.fId = %s", fId);
            dd.open();
            fMsg = dd.getString("fMsg");
            MyQuery sqlCoin = new MyQuery(handle);
            sqlCoin.add(
                    "select u.floginName,o.fAm_fId,c.fId,c.fShortName,o.fMsg from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fUsr_id=u.fId where 1=1",
                    "t_otcorders", "fvirtualcointype", "fuser");
            sqlCoin.add("and o.fAm_fId ='%s'", coin_id);
            sqlCoin.setMaximum(1);
            sqlCoin.open();
            fShortName = sqlCoin.getCurrent().getString("fShortName");
            floginName = sqlCoin.getCurrent().getString("floginName");
            // 购买时查看卖家余额
            MyQuery buy = new MyQuery(handle);
            buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
            buy.add(" WHERE fuid = '%s'", userId);
            buy.add(" AND fVi_fId = '%s'", coin_id);
            buy.open();
            buyfTotal = buy.getCurrent().getDouble("fTotal");
            // 出售时查看自己余额
            MyQuery sell = new MyQuery(handle);
            sell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
            sell.add(" WHERE fuid = '%s'", currentUser.getFid());
            sell.add(" AND fVi_fId = '%s'", coin_id);
            sell.open();
            sellfTotal = sell.getCurrent().getDouble("fTotal");
            // 出售时判断是否绑定银行卡
            MyQuery yh = new MyQuery(handle);
            yh.add("select * from %s ", "fbankinfo_withdraw");
            yh.add("where FUs_fId = '%s' ", currentUser.getFid());
            yh.open();
            if (yh.eof()) {
				ModelAndView.addObject("yh",-4);
			}
            // 出售时判断是否绑定微信
            MyQuery wx = new MyQuery(handle);
            wx.add("select fId,fUsr_id,fType,fName,fAccount,fImgUrl from %s", "t_userreceipt");
            wx.add(" where 1 = 1");
            wx.add(" and fUsr_id = '%s'", currentUser.getFid());
            wx.add(" AND fType = 2");
            wx.open();
            if (wx.eof()) {
				ModelAndView.addObject("wx",-5);
			}
            // 出售时判断是否绑定微信
            MyQuery zfb = new MyQuery(handle);
            zfb.add("select fId,fUsr_id,fType,fName,fAccount,fImgUrl from %s", "t_userreceipt");
            zfb.add(" where 1 = 1");
            zfb.add(" and fUsr_id = '%s'", currentUser.getFid());
            zfb.add(" AND fType = 1");
            zfb.open();
            if (zfb.eof()) {
				ModelAndView.addObject("zfb",-6);
			}
            MyQuery hl = new MyQuery(handle);
            hl.add("select * from %s ", "fotcfees");
            hl.add("where fvid = '%s' ", coin_id);
            hl.add("and flevel = 1");
            hl.open();
            ffee = hl.getDouble("ffee");
        }
        ModelAndView.addObject("sellfTotal", sellfTotal);
        ModelAndView.addObject("buyfTotal", buyfTotal);
        ModelAndView.addObject("fShortName", fShortName);
        ModelAndView.addObject("floginName", floginName);
        ModelAndView.addObject("fId", fId);
        ModelAndView.addObject("jiaoyi", jiaoyi);
        ModelAndView.addObject("haoping", haoping);
        ModelAndView.addObject("userId", userId);
        ModelAndView.addObject("coin_id", coin_id);
        ModelAndView.addObject("payMethod", payMethod);
        ModelAndView.addObject("buyPrice", buyPrice);
        ModelAndView.addObject("type", type);
        ModelAndView.addObject("limit_money", limit_money);
        ModelAndView.addObject("fMsg", fMsg);
        ModelAndView.addObject("ffee", ffee);
        // ModelAndView.setViewName("front/otc/toOrder");
        ModelAndView.setJspFile("front/otc/toOrder");
        return ModelAndView;
    }

    // 去支付
    @ResponseBody
    @RequestMapping(value = { "/otc/payOrder", "/m/otc/payOrder" })
    public ModelAndView payOrder(HttpServletRequest request) {
        JspPage ModelAndView = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        String orderId = request.getParameter("orderId").trim();
        String type_ = request.getParameter("eq");
        String fw = request.getParameter("fw");
        String coin_id = request.getParameter("coinType");
        try (Mysql handle = new Mysql()) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fType orderType,o.fId,o.fCreateTime,o.fAd_Id fAd_Id,o.fAm_fId,o.fUsr_id,o.fOrderId,o.fOrder_Time,o.fTrade_DeadTime,fTrade_Price,fTrade_Count,IFNULL(fNickName,fRealName) name,"
                    + "fTrade_SumMoney,fTrade_Object,fTrade_Status,fTrade_Method,e.fImgUrl,e.fAccount,e.fName,e.fType"
                    + " from %s o left join %s u on o.fTrade_Object=u.fId left join %s e on o.fUsr_id=e.fUsr_id where 1=1",
                    "t_userOtcOrder", "fuser", "t_userreceipt");
            sql.add("and fOrderId='%s'", orderId);
            sql.open();
            Record record = sql.getCurrent();
            String fAm_fId = record.getString("fAm_fId");
            int fId = record.getInt("fUsr_id");
            MyQuery sql1 = new MyQuery(handle);
            sql1.add("select floginName from fuser where fId =%s", fId);
            sql1.open();
            MyQuery sqlCoin = new MyQuery(handle);
            sqlCoin.add("select o.fAm_fId,c.fId,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype");
            sqlCoin.add("and o.fAm_fId ='%s'", fAm_fId);
            sqlCoin.setMaximum(1);
            sqlCoin.open();
            // 判断收款方
            int state = sql.getCurrent().getInt("orderType");
            int Bank_fuid = 0;
            if (state == 1) {
                Bank_fuid = Integer.parseInt(sql.getString("fTrade_Object"));
                ModelAndView.addObject("buyuser", fId);
                ModelAndView.addObject("buyUsr_name", sql1.getCurrent().getField("floginName"));
                ModelAndView.addObject("selluser", Bank_fuid);
                ModelAndView.addObject("sellUsr_name", sql.getCurrent().getField("name"));
            } else {
                Bank_fuid = fId;
                ModelAndView.addObject("buyuser", Integer.parseInt(sql.getString("fTrade_Object")));
                ModelAndView.addObject("buyUsr_name", sql.getCurrent().getField("name"));
                ModelAndView.addObject("selluser", Bank_fuid);
                ModelAndView.addObject("sellUsr_name", sql1.getCurrent().getField("floginName"));
            }
            MyQuery sqlMethod = new MyQuery(handle);
            if ("支付宝".equals(sql.getString("fTrade_Method"))) {
                sqlMethod.add("SELECT fName,fAccount,fImgUrl from t_userreceipt WHERE fType = 1 and fUsr_id = %s",
                        Bank_fuid);
                sqlMethod.open();
                if (sqlMethod.size() > 0) {
                    Record method = sqlMethod.getCurrent();
                    ModelAndView.addObject("method", method);
                }

            } else if ("微信".equals(sql.getString("fTrade_Method"))) {
                sqlMethod.add("SELECT fName,fAccount,fImgUrl from t_userreceipt WHERE fType = 2 and fUsr_id = %s",
                        Bank_fuid);
                sqlMethod.open();
                if (sqlMethod.size() > 0) {
                    Record method = sqlMethod.getCurrent();
                    ModelAndView.addObject("method", method);
                }
            }
            // 查询商家银行账户信息
            MyQuery sqlBank = new MyQuery(handle);
            sqlBank.add(
            		"SELECT fName,fAccount,fBankname from t_userreceipt WHERE fType = 0 and fUsr_id = %s",
                     Bank_fuid);
            sqlBank.open();
            if (sqlBank.size() > 0) {
                ModelAndView.addObject("bankNew", sqlBank.getCurrent());
            }
            ModelAndView.addObject("fShortName", sqlCoin.getCurrent().getString("fShortName"));
            ModelAndView.addObject("orderD", record);
            ModelAndView.addObject("type_", type_);

            String addr = this.getRequest().getHeader("Host");
            addr = new ServerConfig().getProperty("app.addr");

            ModelAndView.addObject("addr", addr);
            if (currentUser.getFid() == sql.getCurrent().getInt("fUsr_id")) {
                ModelAndView.addObject("user_", sql.getCurrent().getString("fUsr_id"));
                ModelAndView.addObject("to_", sql.getCurrent().getString("fTrade_Object"));
            } else {
                ModelAndView.addObject("user_", sql.getCurrent().getString("fTrade_Object"));
                ModelAndView.addObject("to_", sql.getCurrent().getString("fUsr_id"));
            }

            System.err.println(
                    addr + "------" + currentUser.getFid() + "------" + sql.getCurrent().getString("fTrade_Object"));
            // ModelAndView.setViewName("front/otc/orderDetail");
            ModelAndView.setJspFile("front/otc/orderDetail");
        }
        ModelAndView.addObject("coin_id", coin_id);
        ModelAndView.addObject("fuser", currentUser);
        ModelAndView.addObject("fw", fw);// 判断是服务订单是还是买卖订单
        return ModelAndView;
    }

    // 取消订单
    @ResponseBody
    @RequestMapping(value = { "/otc/cancleOrder", "/m/otc/cancleOrder" })
    public ModelAndView cancleOrder(HttpServletRequest request) {
        // Fuser currentUser = GetCurrentUser(request);
        JSONObject json = new JSONObject();
        String orderId = request.getParameter("orderId").trim();
        String coin_type = request.getParameter("coinType");
        String fw = request.getParameter("fw").trim();
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select fId,fOrderId,fTrade_Status,fTrade_SumMoney,fAm_fId,fType,fUsr_id,fTrade_Object,fTrade_Count,taskId_ from %s where 1=1",
                    "t_userOtcOrder");
            sql.add(" and fOrderId = '%s'", orderId);
            sql.open();
            String fTrade_Status = sql.getString("fTrade_Status");
            if(fTrade_Status.equals("4")){
                ModelAndView modelAndView = new ModelAndView();
                String uri = request.getRequestURI().toLowerCase().trim();
                if (!uri.startsWith("/m/")) {
                    modelAndView
                            .setViewName("redirect:/otc/orderList.html?coinType=" + coin_type + "&flag=false&fw=0");// 重定向
                } else {
                    modelAndView
                            .setViewName("redirect:/m/otc/orderList.html?coinType=" + coin_type + "&flag=false&fw=0");
                }
                
            }else{ 
            if (!sql.eof()) {
                if (sql.getInt("fTrade_Status") == 1) {
                    sql.edit();
                    sql.setField("fTrade_Status", 4);
                    sql.post();
                } else {
                    String uri = request.getRequestURI().toLowerCase().trim();
                    ModelAndView modelAndView = new ModelAndView();
                    if (!uri.startsWith("/m/")) {
                            modelAndView.setViewName(
                                    "redirect:/otc/orderList.html?coinType=" + coin_type + "&flag=false&fw=0");// 重定向
                    } else {
                            modelAndView.setViewName(
                                    "redirect:/m/otc/orderList.html?coinType=" + coin_type + "&flag=false&fw=0");
                    }
                    return modelAndView;
                }
            }
          }
            /*
             * Double fTrade_Count = sql.getCurrent().getDouble("fTrade_Count"); String fUsr_id =
             * sql.getString("fUsr_id"); String fTrade_Object = sql.getString("fTrade_Object"); String coin_id =
             * sql.getString("fAm_fId"); String type = sql.getString("fType"); MyQuery ty = new MyQuery(handle); ty.add(
             * "select * from fvirtualcointype where fId = '%s' ", sql.getInt("fAm_fId")); ty.open(); MyQuery hl = new
             * MyQuery(handle); hl.add("select * from %s ", "fotcfees"); hl.add("where fvid = '%s' ", ty.getInt("fId"));
             * hl.add("and flevel = 1"); hl.open(); if (type.equals("1")) { // 购买的时候冻结卖家的资产 MyQuery dsSell = new
             * MyQuery(handle); dsSell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s WHERE 1=1",
             * "fvirtualwallet"); dsSell.add(" and fuid = '%s'", fTrade_Object); dsSell.add(" and fVi_fId = '%s'",
             * coin_id); dsSell.getDefaultOperator().setUpdateMode(UpdateMode.loose); dsSell.open(); double fTotal =
             * dsSell.getCurrent().getDouble("fTotal"); double fTotalNew = fTotal + (fTrade_Count * (1 +
             * hl.getDouble("ffee"))); double fFrozen = dsSell.getCurrent().getDouble("fFrozen"); double fFrozenNew =
             * fFrozen - (fTrade_Count * (1 + hl.getDouble("ffee"))); if (!dsSell.eof()) { dsSell.edit();
             * dsSell.setField("fLastUpdateTime", TDateTime.Now()); dsSell.setField("fTotal", fTotalNew);
             * dsSell.setField("fFrozen", fFrozenNew); dsSell.post(); } }
             */
                Double fTrade_Count = sql.getCurrent().getDouble("fTrade_Count");
                String fUsr_id = sql.getString("fUsr_id");
                String fTrade_Object = sql.getString("fTrade_Object");
                String coin_id = sql.getString("fAm_fId");
                String type = sql.getString("fType");
                MyQuery ty = new MyQuery(handle);
                ty.add("select * from fvirtualcointype where fId = '%s' ", sql.getInt("fAm_fId"));
                ty.open();
                MyQuery hl = new MyQuery(handle);
                hl.add("select * from %s ", "fotcfees");
                hl.add("where fvid = '%s' ", ty.getInt("fId"));
                hl.add("and flevel = 1");
                hl.open();
                if (type.equals("1")) {
                // 购买的时候冻结卖家的资产
                    MyQuery dsSell = new MyQuery(handle);
                    dsSell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s WHERE 1=1",
                            "fvirtualwallet");
                    dsSell.add(" and fuid = '%s'", fTrade_Object);
                    dsSell.add(" and fVi_fId = '%s'", coin_id);
                    dsSell.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                    dsSell.open();
                    double fTotal = dsSell.getCurrent().getDouble("fTotal");
                    double fTotalNew = fTotal + (fTrade_Count * (1 + hl.getDouble("ffee")));
                    double fFrozen = dsSell.getCurrent().getDouble("fFrozen");
                    double fFrozenNew = fFrozen - (fTrade_Count * (1 + hl.getDouble("ffee")));
                    if (!dsSell.eof()) {
                    // 向t_walletlog表里增加日志记录
                    	MyQuery selllog = new MyQuery(handle);
                        selllog.add("select * from %s", "t_walletlog");
                    	selllog.setMaximum(1);
                    	selllog.open();
                    	selllog.append();
                    	selllog.setField("userId_", fTrade_Object);
                    	selllog.setField("coinId_", coin_id); 
                    	selllog.setField("total_", dsSell.getDouble("fTotal"));
                    	selllog.setField("frozen_", dsSell.getDouble("fFrozen"));
                    	selllog.setField("totalChange_", fTrade_Count * (1 + hl.getDouble("ffee")));
                    	selllog.setField("frozenChange_", fTrade_Count * (1 + hl.getDouble("ffee")));
                    selllog.setField("changeReason_", "OTC订单取消");
                    	selllog.setField("changeDate_", TDateTime.Now());
                    	selllog.setField("taskId_", sql.getString("taskId_"));
                    	selllog.post();
                    	
                        dsSell.edit();
                        dsSell.setField("fLastUpdateTime", TDateTime.Now());
                        dsSell.setField("fTotal", fTotalNew);
                        dsSell.setField("fFrozen", fFrozenNew);
                        dsSell.post();
                    }
                } else if (type.equals("2")) {
                // 出售的时候冻结自己的资产
                    MyQuery dsBuy = new MyQuery(handle);
                    dsBuy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s WHERE 1=1", "fvirtualwallet");
                    dsBuy.add(" and fuid = '%s'", fUsr_id);
                    dsBuy.add(" and fVi_fId = '%s'", coin_id);
                    dsBuy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                    dsBuy.open();
                    double fTotal = dsBuy.getCurrent().getDouble("fTotal");
                    double fTotalNew = fTotal + fTrade_Count;
                    double fFrozen = dsBuy.getCurrent().getDouble("fFrozen");
                    double fFrozenNew = fFrozen - fTrade_Count;
                    if (!dsBuy.eof()) {
                    // 向t_walletlog表里增加日志记录
                    	MyQuery buylog = new MyQuery(handle);
                    	buylog.add("select * from %s ","t_walletlog");
                        buylog.setMaximum(1);
                        buylog.open();
                        buylog.append();
                        buylog.setField("userId_", fUsr_id);
                        buylog.setField("coinId_", coin_id);
                        buylog.setField("total_", dsBuy.getDouble("fTotal"));
                        buylog.setField("frozen_", dsBuy.getDouble("fFrozen"));
                        buylog.setField("totalChange_", fTrade_Count);
                        buylog.setField("frozenChange_", fTrade_Count);
                    buylog.setField("changeReason_", "OTC订单取消");
                        buylog.setField("changeDate_", TDateTime.Now());
                        buylog.setField("taskId_", sql.getString("taskId_"));
                        buylog.post();
                        dsBuy.edit();
                        dsBuy.setField("fLastUpdateTime", TDateTime.Now());
                        dsBuy.setField("fTotal", fTotalNew);
                        dsBuy.setField("fFrozen", fFrozenNew);
                        dsBuy.post();
                    }
                }
                tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fw.equals("1")) {
            String uri = request.getRequestURI().toLowerCase().trim();
            ModelAndView modelAndView = new ModelAndView();
            if (!uri.startsWith("/m/")) {
                modelAndView.setViewName("redirect:/otc/orderShellList.html?coinType=" + coin_type);// 重定向
            } else {
                modelAndView.setViewName("redirect:/m/otc/orderShellList.html?coinType=" + coin_type);
            }
            return modelAndView;
        } else {
            String uri = request.getRequestURI().toLowerCase().trim();
            ModelAndView modelAndView = new ModelAndView();
            if (!uri.startsWith("/m/")) {
                modelAndView.setViewName("redirect:/otc/orderList.html?coinType=" + coin_type);// 重定向
            } else {
                modelAndView.setViewName("redirect:/m/otc/orderList.html?coinType=" + coin_type);
            }
            return modelAndView;
        }
    }

    // OTC订单支付完成
    @ResponseBody
    @RequestMapping(value = { "/otc/orderok", "/m/otc/orderok" }, produces = JsonEncode)
    public String orderOk(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        int id = currentUser.getFid();
        if (id < 0) {
            resultJson.accumulate("code", 2);
            resultJson.accumulate("msg", "用户未登录！");
        } else {
            try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
                MyQuery cds = new MyQuery(handle);
                cds.add("select fId,fCreateTime,fAm_fId,fUsr_id,fType,fOrderId,fTrade_DeadTime,fOrder_Time,fTrade_Price,fTrade_Count,fTrade_SumMoney,fTrade_Object,fTrade_Status,fTrade_Method from %s",
                        AppDB.t_userotcorder);
                cds.add(" where fOrderId = '%s'", request.getParameter("orderId"));
                cds.add(" and fTrade_Status = 4");
                cds.open();
                if (!cds.eof()) {
                    resultJson.accumulate("code", 200);
                    resultJson.accumulate("message", "该订单已取消,不能进行支付!");
                    return resultJson.toString();
                }
                MyQuery sql = new MyQuery(handle);
                sql.add("select fId,fCreateTime,fAm_fId,fUsr_id,fType,fOrderId,fTrade_DeadTime,fOrder_Time,fTrade_Price,fTrade_Count,fTrade_SumMoney,fTrade_Object,fTrade_Status,fTrade_Method from %s",
                        "t_userOtcOrder");
                sql.add(" where fOrderId = '%s'", request.getParameter("orderId"));
                sql.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                sql.open();
                if (!sql.eof()) {
                    sql.edit();
                    sql.setField("fTrade_Status", "2");
                    sql.post();
                }

                /*
                 * MyQuery upAd = new MyQuery(handle); upAd.add(
                 * "SELECT fId,fCreateTime,isRealName,fAm_fId,fUsr_id,fUsr_Name,fUnitprice,fCount,fMoney,fOrdertype,fAdr,fSmallprice,fBigprice,fLowprice,fReceipttype,fMsg,isCertified,isUndo,fStatus FROM %s"
                 * , "t_otcorders"); upAd.add(" where fId = '%s'",
                 * request.getParameter("fAd_Id"));
                 * upAd.getDefaultOperator().setUpdateMode(UpdateMode.loose); upAd.open(); if
                 * (!upAd.eof()) { upAd.edit(); upAd.setField("isUndo", "0");
                 * upAd.setField("fStatus", "1"); upAd.post(); }
                 */

                resultJson.accumulate("code", 100);
                resultJson.accumulate("message", "提交成功，请及时通知卖家/买家释放此订单！");

                MyQuery user = new MyQuery(handle);
                user.add("select o.fOrderId,o.fType,u.fTelephone from %s o ", "t_userOtcOrder");
                user.add("left join %s u on ", "fuser");
                if (sql.getString("fType").equals("1")) {
                    user.add("u.fId = o.fTrade_Object ");
                } else if (sql.getString("fType").equals("2")) {
                    user.add("u.fId = o.fUsr_id ");
                }
                user.add("where o.fId =%s", sql.getCurrent().getString("fId"));
                user.open();
                String phone = user.getString("fTelephone");
                String order = user.getString("fOrderId");
                order = order.substring(order.length() - 4);
                ShortMessageService.send(phone,
                        "【瑞银钱包】您尾号为" + order + "的订单于" + TDateTime.Now().toString() + "已完成支付,请及时释放此订单。");
                tx.commit();
            } catch (Exception e) {
                resultJson.accumulate("code", 200);
                resultJson.accumulate("message", "提交失败，网络异常！");
            }
        }
        return resultJson.toString();
    }

    // OTC订单释放
    @ResponseBody
    @RequestMapping(value = { "/otc/orderfreed", "/m/otc/orderfreed" }, produces = JsonEncode)
    public String orderFreed(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        int id = currentUser.getFid();
        String fShortName = request.getParameter("fShortName");
        String fId = null;
        if (id < 0) {
            resultJson.accumulate("code", 2);
            resultJson.accumulate("msg", "用户未登录！");
        } else {
            try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
                MyQuery sql = new MyQuery(handle);
                sql.add("select fId,fCreateTime,fAm_fId,fUsr_id,fType,fOrderId,fTrade_DeadTime,fOrder_Time,fTrade_Price,fTrade_Count,fTrade_SumMoney,fTrade_Object,fTrade_Status,fTrade_Method,taskId_ from %s",
                        "t_userOtcOrder");
                sql.add(" where fOrderId = '%s'", request.getParameter("orderId"));
                sql.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                sql.open();
                String fTrade_Status = sql.getString("fTrade_Status");
                if(fTrade_Status.equals("3")){
                	resultJson.accumulate("code", 300);
                    resultJson.accumulate("message", "请勿重复提交！");
                }else{
                	if (!sql.eof()) {
                        sql.edit();
                        sql.setField("fTrade_Status", "3");
                        sql.post();
                        fId = sql.getString("fId");
                    }

                    MyQuery upAd = new MyQuery(handle);
                    upAd.add(
                            "SELECT fId,fCreateTime,isRealName,fAm_fId,fUsr_id,fUsr_Name,fUnitprice,fCount,fMoney,fOrdertype,fAdr,fSmallprice,fBigprice,fLowprice,fReceipttype,fMsg,isCertified,isUndo,fStatus FROM %s",
                            "t_otcorders");
                    upAd.add(" where fId = '%s'", request.getParameter("fAd_Id"));
                    upAd.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                    upAd.open();
                    if (!upAd.eof()) {
                        upAd.edit();
                        upAd.setField("isUndo", "1");
                        upAd.setField("fStatus", "0");
                        upAd.post();
                    }
                    MyQuery ty = new MyQuery(handle);
                    ty.add("select * from fvirtualcointype where fShortName = '%s' ", fShortName);
                    ty.open();
                    MyQuery hl = new MyQuery(handle);
                    hl.add("select * from %s ", "fotcfees");
                    hl.add("where fvid = '%s' ", ty.getInt("fId"));
                    hl.open();
                    if (sql.getString("fType").equals("1")) {
                        // 减少卖家可用金额和冻结资金
                        MyQuery sell = new MyQuery(handle);
                        sell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
                        sell.add(" WHERE fuid = '%s'", currentUser.getFid());
                        sell.add(" AND fVi_fId = '%s'", sql.getCurrent().getString("fAm_fId"));
                        sell.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                        sell.open();

                        Double count = sell.getCurrent().getDouble("fFrozen")
                                - (sql.getCurrent().getDouble("fTrade_Count") * (1 + hl.getDouble("ffee")));
                        // 向t_walletlog表里增加卖家余额变化日志记录
                    	MyQuery selllog = new MyQuery(handle);
                    	selllog.add("select * from %s ","t_walletlog");
                    	selllog.setMaximum(1);
                    	selllog.open();
                    	selllog.append();
                    	selllog.setField("userId_", currentUser.getFid());
                    	selllog.setField("coinId_", sql.getCurrent().getString("fAm_fId")); 
                    	selllog.setField("total_", sell.getDouble("fTotal"));
                    	selllog.setField("frozen_", sell.getDouble("fFrozen"));
                    	selllog.setField("totalChange_", sql.getCurrent().getDouble("fTrade_Count") * (1 + hl.getDouble("ffee")));
                    	selllog.setField("frozenChange_", sql.getCurrent().getDouble("fTrade_Count") * (1 + hl.getDouble("ffee")));
                        selllog.setField("changeReason_", "OTC订单释放");
                    	selllog.setField("changeDate_", TDateTime.Now());
                    	selllog.setField("taskId_", sql.getString("taskId_"));
                    	selllog.post();
                        if (!sell.eof()) {
                            sell.edit();
                            sell.setField("fFrozen", count);
                            sell.post();
                        }

                        // 增加买家可用金额
                        MyQuery buy = new MyQuery(handle);
                        buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
                        buy.add(" WHERE fuid = '%s'", sql.getCurrent().getString("fUsr_id"));
                        buy.add(" AND fVi_fId = '%s'", sql.getCurrent().getString("fAm_fId"));
                        buy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                        buy.open();
                        
                        // 订单释放向t_walletlog表里增加买家日志记录
                    	MyQuery buylog = new MyQuery(handle);
                    	buylog.add("select * from %s ","t_walletlog");
                        buylog.setMaximum(1);
                        buylog.open();
                        buylog.append();
                        buylog.setField("userId_", sql.getCurrent().getString("fUsr_id"));
                        buylog.setField("coinId_", sql.getCurrent().getString("fAm_fId"));
                        buylog.setField("total_", buy.getDouble("fTotal"));
                        buylog.setField("frozen_", buy.getDouble("fFrozen"));
                        buylog.setField("totalChange_", sql.getCurrent().getDouble("fTrade_Count"));
                        buylog.setField("frozenChange_", sql.getCurrent().getDouble("fTrade_Count"));
                        buylog.setField("changeReason_", "OTC订单释放");
                        buylog.setField("changeDate_", TDateTime.Now());
                        buylog.setField("taskId_", sql.getString("taskId_"));
                        buylog.post();
                        Double count1 = buy.getCurrent().getDouble("fTotal") + sql.getCurrent().getDouble("fTrade_Count");
                        if (!buy.eof()) {
                            buy.edit();
                            buy.setField("fTotal", count1);
                            buy.post();
                        }
                        
                    } else if (sql.getString("fType").equals("2")) {
                        // 减少卖家可用金额和冻结资金
                        MyQuery sell = new MyQuery(handle);
                        sell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
                        sell.add(" WHERE fuid = '%s'", currentUser.getFid());
                        sell.add(" AND fVi_fId = '%s'", sql.getCurrent().getString("fAm_fId"));
                        sell.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                        sell.open();
                        Double count = sell.getCurrent().getDouble("fFrozen") - sql.getCurrent().getDouble("fTrade_Count");
                        // 向t_walletlog表里增加卖家余额变化日志记录
                    	MyQuery selllog = new MyQuery(handle);
                    	selllog.add("select * from %s ","t_walletlog");
                    	selllog.setMaximum(1);
                    	selllog.open();
                    	selllog.append();
                    	selllog.setField("userId_", currentUser.getFid());
                    	selllog.setField("coinId_", sql.getCurrent().getString("fAm_fId")); 
                    	selllog.setField("total_", sell.getDouble("fTotal"));
                    	selllog.setField("frozen_", sell.getDouble("fFrozen"));
                    	selllog.setField("totalChange_", sql.getCurrent().getDouble("fTrade_Count"));
                    	selllog.setField("frozenChange_", sql.getCurrent().getDouble("fTrade_Count"));
                        selllog.setField("changeReason_", "OTC订单释放");
                    	selllog.setField("changeDate_", TDateTime.Now());
                    	selllog.setField("taskId_", sql.getString("taskId_"));
                    	selllog.post();
                        if (!sell.eof()) {
                            sell.edit();
                            sell.setField("fFrozen", count);
                            sell.post();
                        }

                        // 增加买家可用金额
                        MyQuery buy = new MyQuery(handle);
                        buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
                        buy.add(" WHERE fuid = '%s'", sql.getCurrent().getString("fTrade_Object"));
                        buy.add(" AND fVi_fId = '%s'", sql.getCurrent().getString("fAm_fId"));
                        buy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                        buy.open();
                        Double count1 = buy.getCurrent().getDouble("fTotal")
                                + (sql.getCurrent().getDouble("fTrade_Count") * (1 - hl.getDouble("ffee")));
                        // 订单释放向t_walletlog表里增加买家日志记录
                    	MyQuery buylog = new MyQuery(handle);
                    	buylog.add("select * from %s ","t_walletlog");
                        buylog.setMaximum(1);
                        buylog.open();
                        buylog.append();
                        buylog.setField("userId_", sql.getCurrent().getString("fTrade_Object"));
                        buylog.setField("coinId_", sql.getCurrent().getString("fAm_fId"));
                        buylog.setField("total_", buy.getDouble("fTotal"));
                        buylog.setField("frozen_", buy.getDouble("fFrozen"));
                        buylog.setField("totalChange_", sql.getCurrent().getDouble("fTrade_Count") * (1 - hl.getDouble("ffee")));
                        buylog.setField("frozenChange_", sql.getCurrent().getDouble("fTrade_Count") * (1 - hl.getDouble("ffee")));
                        buylog.setField("changeReason_", "OTC订单释放");
                        buylog.setField("changeDate_", TDateTime.Now());
                        buylog.setField("taskId_", sql.getString("taskId_"));
                        buylog.post();
                        if (!buy.eof()) {
                            buy.edit();
                            buy.setField("fTotal", count1);
                            buy.post();
                        }
                    }
                    // 插入otc手续费明细
                    if (sql.getString("fType").equals("1")) {
                        MyQuery de = new MyQuery(handle);
                        de.add("select * from %s " ,"ffee_detail");
                        de.setMaximum(1);
                        MyQuery ye = new MyQuery(handle);
                        ye.add("select * from %s ", "fvirtualwallet");
                        ye.add("where fuid = '%s' ", sql.getInt("fTrade_Object"));
                        ye.add("and fVi_fId = '%s' " ,sql.getInt("fAm_fId"));
                        ye.open();
                        de.append();
                        de.setField("fUser_id", sql.getInt("fTrade_Object"));
                        de.setField("fCoin_id", sql.getInt("fAm_fId"));
                        de.setField("ffee", hl.getDouble("ffee"));
                        de.setField("fOrder_type", 2);
                        de.setField("ffee_time", TDateTime.Now());
                        de.setField("fOrder_object", sql.getInt("fUsr_id"));
                        de.setField("fTrade_type", sql.getString("fType"));
                        de.setField("fTrade_count", sql.getDouble("fTrade_Count"));
                        de.setField("ffee_sum", sql.getDouble("fTrade_Count") * hl.getDouble("ffee"));
                        de.setField("fLevel", 1);
                        de.setField("fTotal", ye.getDouble("fTotal"));
                        de.setField("fFrozen", ye.getDouble("fFrozen"));
                        de.setField("guid", utils.newGuid());
                        de.post();
                    }else{
                    	MyQuery de = new MyQuery(handle);
                        de.add("select * from %s " ,"ffee_detail");
                        de.setMaximum(1);
                        MyQuery ye = new MyQuery(handle);
                        ye.add("select * from %s ", "fvirtualwallet");
                        ye.add("where fuid = '%s' ", sql.getInt("fTrade_Object"));
                        ye.add("and fVi_fId = '%s' " ,sql.getInt("fAm_fId"));
                        ye.open();
                        de.append();
                        de.setField("fUser_id", sql.getInt("fTrade_Object"));
                        de.setField("fCoin_id", sql.getInt("fAm_fId"));
                        de.setField("ffee", hl.getDouble("ffee"));
                        de.setField("fOrder_type", 2);
                        de.setField("ffee_time", TDateTime.Now());
                        de.setField("fOrder_object", sql.getInt("fUsr_id"));
                        de.setField("fTrade_type", sql.getString("fType"));
                        de.setField("fTrade_count", sql.getDouble("fTrade_Count"));
                        de.setField("ffee_sum", sql.getDouble("fTrade_Count") * hl.getDouble("ffee"));
                        de.setField("fLevel", 1);
                        de.setField("fTotal", ye.getDouble("fTotal"));
                        de.setField("fFrozen", ye.getDouble("fFrozen"));
                        de.setField("guid", utils.newGuid());
                        de.post();
                    }
                   
                    resultJson.accumulate("code", 100);
                    resultJson.accumulate("message", "释放成功！");
                    MyQuery user = new MyQuery(handle);
                    user.add("select o.fOrderId,u.* from %s o ", "t_userOtcOrder");
                    user.add("left join %s u on ", AppDB.Fuser);
                    user.add("u.fId = o.fUsr_id ");
                    user.add("where o.fId =%s", fId);
                    user.open();
                    MyQuery user1 = new MyQuery(handle);
                    user1.add("select o.fOrderId,u.* from %s o ", "t_userOtcOrder");
                    user1.add("left join %s u on ", AppDB.Fuser);
                    user1.add("u.fId = o.fTrade_Object ");
                    user1.add("where o.fId =%s", fId);
                    user1.open();
                    String sellphone = user.getString("fTelephone");
                    String buyphone = user1.getString("fTelephone");
                    String order = user.getString("fOrderId");
                    order = order.substring(order.length() - 4);
                    ShortMessageService.send(sellphone,
                            "【瑞银钱包】您尾号为" + order + "的订单于" + TDateTime.Now().toString() + "已完成释放。");
                    ShortMessageService.send(buyphone,
                            "【瑞银钱包】您尾号为" + order + "的订单于" + TDateTime.Now().toString() + "已完成释放。");
                    tx.commit();
                }
            } catch (Exception e) {
                resultJson.accumulate("code", 200);
                resultJson.accumulate("message", "释放失败，网络异常！");
            }
        }
        return resultJson.toString();
    }

    // OTC订单列表
    @ResponseBody
    @RequestMapping(value = { "/otc/orderShellList", "/m/otc/orderShellList" })
    public ModelAndView orderShellList(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage) {
        JspPage ModelAndView = new JspPage(request);
        // 当前登录用户
        String fw = request.getParameter("fw");
        String coin_id = request.getParameter("coinType");
        Fuser currentUser = GetCurrentUser(request);
        List<Map<String, Object>> orderListIng = new ArrayList<>();
        List<Map<String, Object>> orderListOver = new ArrayList<>();
        int maxNum = 10;
        // 进行中
        int totalListIng = 0;
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sql.add("DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sql.add("fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fUsr_id=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sql.add("and fTrade_Object = '%s'", currentUser.getFid());
            sql.add("and (fTrade_Status = '1' or fTrade_Status = '2' or fTrade_Status = '5')");
            sql.add("order by fOrder_Time desc");
            sql.setMaximum(maxNum);
            sql.setOffset((currentPage - 1) * maxNum);
            sql.open();
            for (Record record : sql) {
                orderListIng.add(record.getItems());
            }
            MyQuery sqltotal = new MyQuery(handle);
            sqltotal.add(
                    "select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sqltotal.add(
                    "DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sqltotal.add(
                    "fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fUsr_id=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sqltotal.add("and fTrade_Object = '%s'", currentUser.getFid());
            sqltotal.add("and (fTrade_Status = '1' or fTrade_Status = '2' or fTrade_Status = '5')");
            sqltotal.add("order by fOrder_Time desc");
            sqltotal.open();
            totalListIng = sqltotal.getRecords().size();
        } catch (Exception e) {
            ModelAndView.addObject("code", 200);
            ModelAndView.addObject("message", "获取失败");
        }
        int page_countsIng = 1;
        int page_finalIng = 1;
        if (totalListIng % maxNum > 0) {
            page_finalIng = totalListIng / maxNum + 1;
            page_countsIng = totalListIng / maxNum + 1;
        } else {
            page_finalIng = totalListIng / maxNum;
            page_countsIng = totalListIng / maxNum;
        }
        // 已完成的
        int totalListOver = 0;
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sql.add("DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sql.add("fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fUsr_id=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sql.add("and fTrade_Object = '%s'", currentUser.getFid());
            sql.add("and (fTrade_Status = '3' or fTrade_Status = '4' or fTrade_Status = '6')");
            sql.add("order by fOrder_Time desc");
            sql.setMaximum(maxNum);
            sql.setOffset((currentPage - 1) * maxNum);
            sql.open();
            for (Record record : sql) {
                orderListOver.add(record.getItems());
            }
            MyQuery sqltotal = new MyQuery(handle);
            sqltotal.add(
                    "select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sqltotal.add(
                    "DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sqltotal.add(
                    "fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fUsr_id=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sqltotal.add("and fTrade_Object = '%s'", currentUser.getFid());
            sqltotal.add("and (fTrade_Status = '3' or fTrade_Status = '4' or fTrade_Status = '6')");
            sqltotal.add("order by fOrder_Time desc");
            sqltotal.open();
            totalListOver = sqltotal.getRecords().size();
        } catch (Exception e) {
            ModelAndView.addObject("code", 200);
            ModelAndView.addObject("message", "获取失败");
        }
        int page_countsOver = 1;
        int page_finalOver = 1;
        if (totalListOver % maxNum > 0) {
            page_finalOver = totalListOver / maxNum + 1;
            page_countsOver = totalListOver / maxNum + 1;
        } else {
            page_finalOver = totalListOver / maxNum;
            page_countsOver = totalListOver / maxNum;
        }
        ModelAndView.addObject("orderListOver", orderListOver);
        ModelAndView.addObject("page_countsOver", page_countsOver);
        ModelAndView.addObject("page_finalOver", page_finalOver);
        ModelAndView.addObject("totalListOver", totalListOver);
        ModelAndView.addObject("fw", fw);
        ModelAndView.addObject("coin_id", coin_id);
        ModelAndView.addObject("orderListIng", orderListIng);
        ModelAndView.addObject("page_countsIng", page_countsIng);
        ModelAndView.addObject("page_finalIng", page_finalIng);
        ModelAndView.addObject("totalListIng", totalListIng);
        ModelAndView.addObject("currentPage", currentPage);
        // ModelAndView.setViewName("front/otc/orderShellList");
        ModelAndView.setJspFile("front/otc/orderShellList");
        return ModelAndView;
    }

    // 提交申诉申请
    @RequestMapping(value = { "/otc/appeal", "/m/otc/appeal" })
    public ModelAndView otcAppeal(HttpServletRequest request, @RequestParam(required = true) String orderId) {
        JspPage modelAndView = new JspPage(request);
        String fw = request.getParameter("fw");
        String coin_id = request.getParameter("coinType");
        modelAndView.addObject("orderId", orderId);
        modelAndView.addObject("coin_id", coin_id);
        // ModelAndView.setViewName("front/otc/appeal");
        modelAndView.setJspFile("front/otc/appeal");
        return modelAndView;
    }

    // 保存申诉申请
    @ResponseBody
    @RequestMapping(value = { "/otc/addAppeal", "/m/otc/addAppeal" }, produces = JsonEncode)
    public String AddotcAppeal(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        try (Mysql mysql = new Mysql()) {
            MyQuery cds = new MyQuery(mysql);
            cds.add("select * from %s", AppDB.t_userotcorder);
            cds.add(" where fOrderId = %s", request.getParameter("orderId"));
            cds.add(" and fTrade_Status in (3,4)");
            cds.open();
            if (!cds.eof()) {
                resultJson.accumulate("code", -1);
                resultJson.accumulate("msg", "该订单状态已改变,不能进行申诉!");
                return resultJson.toString();
            }
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s", "t_userotcorderappeal");
            ds.add("where fctcorderid = %s", request.getParameter("orderId"));
            ds.setMaximum(1);
            ds.open();
            if (ds.eof()) {
                ds.append();
                ds.setField("fctcorderid", request.getParameter("orderId"));
                ds.setField("fimgpath1", request.getParameter("path1"));
                ds.setField("fimgpath2", request.getParameter("path2"));
                ds.setField("fimgpath3", request.getParameter("path3"));
                ds.setField("fremark", request.getParameter("remark"));
                ds.setField("fStatus", request.getParameter("fw"));
                ds.post();

                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select * from %s", "t_userotcorder");
                ds1.add("where fOrderId = %s", request.getParameter("orderId"));
                ds1.open();
                if (!ds1.eof()) {
                    ds1.edit();
                    ds1.setField("fTrade_Status", 5);
                    ds1.post();
                }
                resultJson.accumulate("code", 0);
                resultJson.accumulate("msg", "提交成功!");
            } else {
                MyQuery ds2 = new MyQuery(mysql);
                ds2.add("select * from %s", "t_userotcorder");
                ds2.add("where fOrderId = %s", request.getParameter("orderId"));
                ds2.setMaximum(1);
                ds2.open();
                if ("2".equals(ds2.getString("fTrade_Status"))) {
                    ds2.edit();
                    ds2.setField("fTrade_Status", 5);
                    ds2.post();
                    ds.edit();
                    ds.setField("fctcorderid", request.getParameter("orderId"));
                    ds.setField("fimgpath1", request.getParameter("path1"));
                    ds.setField("fimgpath2", request.getParameter("path2"));
                    ds.setField("fimgpath3", request.getParameter("path3"));
                    ds.setField("fremark", request.getParameter("remark"));
                    ds.setField("fStatus", request.getParameter("fw"));
                    ds.post();
                    resultJson.accumulate("code", 0);
                    resultJson.accumulate("msg", "重新提交申诉成功!");
                } else {
                    resultJson.accumulate("code", 1);
                    resultJson.accumulate("msg", "一个订单只能申诉一次，不可重复提交申诉!");
                }
            }
        }
        return resultJson.toString();
    }

    // 催单
    @ResponseBody
    @RequestMapping(value = "/otc/urge")
    public String Urge(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = true) String orderId) throws Exception {
        JSONObject resultJson = new JSONObject();
        try (Mysql handle = new Mysql();) {
            MyQuery user = new MyQuery(handle);
            user.add("select o.fOrderId,u.* from %s o ", "t_userOtcOrder");
            user.add("left join %s u on ", AppDB.Fuser);
            user.add("u.fId = o.fTrade_Object ");
            user.add("where o.fOrderId =%s", orderId);
            user.open();
            String phone = user.getString("fTelephone");
            String order = user.getString("fOrderId");
            order = order.substring(order.length() - 4);
            ShortMessageService.send(phone,
                    "【CoaCoin】您尾号为" + order + "的订单于" + TDateTime.Now().toString() + "接到催单,请尽快解决！。");
            resultJson.accumulate("code", 0);
        } catch (Exception e) {
            resultJson.accumulate("code", 1);
        }
        return resultJson.toString();
    }

    // 提示卖家充值
    @ResponseBody
    @RequestMapping(value = "/otc/tourge")
    public String toUrge(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = true) String userId) throws Exception {
        JSONObject resultJson = new JSONObject();
        try (Mysql handle = new Mysql();) {
            MyQuery user = new MyQuery(handle);
            user.add("select o.fUsr_id,u.* from %s o ", "t_userOtcOrder");
            user.add("left join %s u on ", AppDB.Fuser);
            user.add("u.fId = o.fUsr_id ");
            user.add("where o.fUsr_id =%s", userId);
            user.open();
            String phone = user.getString("fTelephone");
            ShortMessageService.send(phone, "【瑞银钱包】您的余额已不足订单出售，请及时充值！");
            resultJson.accumulate("code", 0);
        } catch (Exception e) {
            resultJson.accumulate("code", 1);
        }
        return resultJson.toString();
    }

    // 后台定时取消订单
    public void timeOrder(String orderId) {
        Timer timer = new Timer();
        System.out.println("进入定时器");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("-------设定执行取消订单任务--------");
                try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
                    MyQuery sqlStatus = new MyQuery(handle);
                    sqlStatus.add("select fId,fOrderId,fTrade_Status from %s where 1=1", "t_userOtcOrder");
                    sqlStatus.add(" and fOrderId = '%s'", orderId);
                    sqlStatus.open();
                    String fTrade_Status = sqlStatus.getString("fTrade_Status");
                    if (fTrade_Status.equals("1")) {
                        MyQuery sql = new MyQuery(handle);
                        sql.add("select fId,fOrderId,fTrade_Status,fTrade_SumMoney,fAm_fId,fType,fUsr_id,fTrade_Object,fTrade_Count,taskId_ from %s where 1=1",
                                "t_userOtcOrder");
                        sql.add(" and fOrderId = '%s'", orderId);
                        sql.open();
                        if (!sql.eof()) {
                            if (sql.getInt("fTrade_Status") == 1) {
                                sql.edit();
                                sql.setField("fTrade_Status", 4);
                                sql.post();
                            }
                        }
                        Double fTrade_Count = sql.getCurrent().getDouble("fTrade_Count");
                        String fUsr_id = sql.getString("fUsr_id");
                        String fTrade_Object = sql.getString("fTrade_Object");
                        String coin_id = sql.getString("fAm_fId");
                        String type = sql.getString("fType");
                        if (type.equals("1")) {
                            // 购买的时候冻结卖家的资产
                            MyQuery dsSell = new MyQuery(handle);
                            dsSell.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s WHERE 1=1",
                                    "fvirtualwallet");
                            dsSell.add(" and fuid = '%s'", fTrade_Object);
                            dsSell.add(" and fVi_fId = '%s'", coin_id);
                            dsSell.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                            dsSell.open();
                            double fTotal = dsSell.getCurrent().getDouble("fTotal");
                            double fTotalNew = fTotal + fTrade_Count;
                            double fFrozen = dsSell.getCurrent().getDouble("fFrozen");
                            double fFrozenNew = fFrozen - fTrade_Count;
                            if (!dsSell.eof()) {
                                // 向t_walletlog表里增加日志记录
                            	MyQuery selllog = new MyQuery(handle);
                            	selllog.add("select * from %s ","t_walletlog");
                            	selllog.setMaximum(1);
                            	selllog.open();
                            	selllog.append();
                            	selllog.setField("userId_", fTrade_Object);
                            	selllog.setField("coinId_", coin_id); 
                            	selllog.setField("total_", dsSell.getDouble("fTotal"));
                            	selllog.setField("frozen_", dsSell.getDouble("fFrozen"));
                            	selllog.setField("totalChange_", fTrade_Count);
                            	selllog.setField("frozenChange_", fTrade_Count);
                                selllog.setField("changeReason_", "OTC订单取消");
                            	selllog.setField("changeDate_", TDateTime.Now());
                            	selllog.setField("taskId_", sql.getString("taskId_"));
                            	selllog.post();
                            	
                                dsSell.edit();
                                dsSell.setField("fLastUpdateTime", TDateTime.Now());
                                dsSell.setField("fTotal", fTotalNew);
                                dsSell.setField("fFrozen", fFrozenNew);
                                dsSell.post();
                            }
                        } else if (type.equals("2")) {
                            // 出售的时候冻结自己的资产
                            MyQuery dsBuy = new MyQuery(handle);
                            dsBuy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s WHERE 1=1",
                                    "fvirtualwallet");
                            dsBuy.add(" and fuid = '%s'", fUsr_id);
                            dsBuy.add(" and fVi_fId = '%s'", coin_id);
                            dsBuy.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                            dsBuy.open();
                            double fTotal = dsBuy.getCurrent().getDouble("fTotal");
                            double fTotalNew = fTotal + fTrade_Count;
                            double fFrozen = dsBuy.getCurrent().getDouble("fFrozen");
                            double fFrozenNew = fFrozen - fTrade_Count;
                            if (!dsBuy.eof()) {
                                // 向t_walletlog表里增加日志记录
                            	MyQuery buylog = new MyQuery(handle);
                            	buylog.add("select * from %s ","t_walletlog");
                                buylog.setMaximum(1);
                                buylog.open();
                                buylog.append();
                                buylog.setField("userId_", fUsr_id);
                                buylog.setField("coinId_", coin_id);
                                buylog.setField("total_", dsBuy.getDouble("fTotal"));
                                buylog.setField("frozen_", dsBuy.getDouble("fFrozen"));
                                buylog.setField("totalChange_", fTrade_Count);
                                buylog.setField("frozenChange_", fTrade_Count);
                                buylog.setField("changeReason_", "OTC订单取消");
                                buylog.setField("changeDate_", TDateTime.Now());
                                buylog.setField("taskId_", sql.getString("taskId_"));
                                buylog.post();
                            	
                                dsBuy.edit();
                                dsBuy.setField("fLastUpdateTime", TDateTime.Now());
                                dsBuy.setField("fTotal", fTotalNew);
                                dsBuy.setField("fFrozen", fFrozenNew);
                                dsBuy.post();
                            }
                        }
                    } else {
                        System.out.println("订单已被取消、申诉中或者已完成！");
                    }
                    tx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2700000);// 设定指定的时间time,此处为45分钟
    }

    public String Orderdata(String fId, String status, String type) {
        Double count = 0.0d;
        Mysql mysql = new Mysql();
        MyQuery mz = new MyQuery(mysql);
        mz.add("select * from %s ", "t_userotcorder");
        mz.add("where fUsr_id = '%s' ", fId.substring(2));
        mz.add("and fTrade_Status = '%s' ", status);
        mz.add("and fType = '%s' ", type);
        mz.open();
        for (Record record : mz) {
            count += record.getDouble("fTrade_Count");
        }
        MyQuery mz1 = new MyQuery(mysql);
        mz1.add("select * from %s ", "t_userotcorder");
        mz1.add("where fTrade_Object = '%s' ", fId.substring(2));
        mz1.add("and fTrade_Status = '%s' ", status);
        if ("1".equals(type)) {
            mz1.add("and fType = '%s' ", "2");
        } else {
            mz1.add("and fType = '%s' ", "1");
        }
        mz1.open();
        for (Record record : mz1) {
            count += record.getDouble("fTrade_Count");
        }
        return count.toString();
    }

    public String Orderdata(String fId, String status, String type, String start, String end) {
        Double count = 0.0d;
        Mysql mysql = new Mysql();
        MyQuery mz = new MyQuery(mysql);
        mz.add("select * from %s ", "t_userotcorder");
        mz.add("where fUsr_id = '%s' ", fId.substring(2));
        mz.add("and fTrade_Status = '%s' ", status);
        mz.add("and fType = '%s' ", type);
        mz.add("and fOrder_Time > '%s' ", start);
        mz.add("and fOrder_Time < '%s' ", end);
        mz.open();
        for (Record record : mz) {
            count += record.getDouble("fTrade_Count");
        }
        MyQuery mz1 = new MyQuery(mysql);
        mz1.add("select * from %s ", "t_userotcorder");
        mz1.add("where fTrade_Object = '%s' ", fId.substring(2));
        mz1.add("and fTrade_Status = '%s' ", status);
        if ("1".equals(type)) {
            mz1.add("and fType = '%s' ", "2");
        } else {
            mz1.add("and fType = '%s' ", "1");
        }
        mz1.add("and fOrder_Time > '%s' ", start);
        mz1.add("and fOrder_Time < '%s' ", end);
        mz1.open();
        for (Record record : mz1) {
            count += record.getDouble("fTrade_Count");
        }
        return count.toString();
    }

    public List<Record> OrderDetail(String fId, String type) {
        Mysql mysql = new Mysql();
        // 查询对应订单详情
        MyQuery od = new MyQuery(mysql);
        od.add("select * from %s o ", "t_userotcorder");
        od.add("left join %s r ", "t_userreceipt");
        od.add("on o.fUsr_id = r.fUsr_id");
        od.add("where o.fUsr_id = '%s' ", fId.substring(2));
        od.add("and o.fType = '%s' ", type);
        od.open();
        return od.getRecords();
    }

    public List<Record> UserOrderDetail(String orderId, String fId) {
        Mysql mysql = new Mysql();
        // 查询对应订单详情
        MyQuery od = new MyQuery(mysql);
        od.add("select *,v.fId,v.fName idName from %s o ", "t_userotcorder");
        od.add("left join %s r ", "t_userreceipt");
        od.add("on o.fUsr_id = r.fUsr_id");
        od.add("left join %s v ", "fvirtualcointype");
        od.add("on o.fAm_fId = v.fId");
        od.add("where o.fUsr_id = '%s' ", fId.substring(2));
        od.add("and o.fOrderId = '%s' ", orderId);
        od.setMaximum(1);
        od.open();
        return od.getRecords();
    }

    public List<Record> otcOrder(String fId, int type) {
        Mysql mysql = new Mysql();
        // 查询对应广告
        MyQuery od = new MyQuery(mysql);
        od.add("select *,u.fName idName from %s u ", "t_userotcorder");
        od.add("left join %s o ", "fvirtualcointype");
        od.add("u.fAm_fId = o.fId ");
        od.add("where u.fUsr_id = '%s' ", fId.substring(2));
        if(type != 2) {
            od.add("and u.fOrdertype = '%s' ", type);
        }
        od.open();
        return od.getRecords();
    }

    public boolean publish(int orderId, int type) {
        Mysql mysql = new Mysql();
        // 查询对应广告
        MyQuery gb = new MyQuery(mysql);
        gb.add("select * from %s", "t_otcorders");
        gb.add("where fId = %d ", orderId);
        gb.open();
        if (gb.eof()) {
            return false;
        }
        if (type == 0) {
            gb.edit();
            gb.setField("fStatus", "4");
            gb.post();
        } else {
            gb.edit();
            gb.setField("fStatus", "3");
            gb.post();
        }
        return true;
    }

    public String testingType(String type) {
        String typeName = "";
        if ("1".equals(type)) {
            typeName = "已下单";
        }
        if ("2".equals(type)) {
            typeName = "待验证";
        }
        if ("3".equals(type)) {
            typeName = "已完成";
        }
        if ("4".equals(type)) {
            typeName = "已取消";
        }
        return typeName;
    }

    public String testingPayType(String type) {
        String typeName = "";
        if ("0".equals(type)) {
            typeName = "银行转账";
        }
        if ("1".equals(type)) {
            typeName = "支付宝";
        }
        if ("2".equals(type)) {
            typeName = "微信";
        }
        return typeName;
    }

    public String testingStatus(String type) {
        String typeName = "";
        if ("1".equals(type)) {
            typeName = "审核中";
        }
        if ("2".equals(type)) {
            typeName = "未通过";
        }
        if ("3".equals(type)) {
            typeName = "已通过";
        }
        if ("4".equals(type)) {
            typeName = "已下架";
        }
        return typeName;
    }

    public String getTimesmorning(int day, int month, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), day, month, second);
        Date beginOfDate = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(beginOfDate);
    }

    public String getTimesmornings(int day, int month, int second) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), day, month, second);
        Date beginOfDate = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(beginOfDate);
    }
}
