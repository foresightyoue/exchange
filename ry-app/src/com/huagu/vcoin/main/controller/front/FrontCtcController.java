package com.huagu.vcoin.main.controller.front;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.huagu.vcoin.main.model.Fabout;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.front.FrontOthersService;
import com.huagu.vcoin.main.service.front.FrontSystemArgsService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.sms.ShortMessageService;

import cn.cerc.jbean.core.ServiceStatus;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDate;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.BatchScript;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;
import cn.cerc.jdb.other.utils;
import net.sf.json.JSONObject;
import site.jayun.vcoin.bourse.dao.WalletAmount;

/**
 * @Description
 * @author Peng
 * @date 2018年3月23日
 */
@Controller
public class FrontCtcController extends BaseController {

    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontOthersService frontOthersService;
    @Autowired
    private FrontSystemArgsService frontSystemArgsService;

    @RequestMapping("/ctc/index")
    public ModelAndView ctcIndex(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "-1") int fId) {
        ModelAndView modelAndView = new ModelAndView();
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        if (currentUser != null) {
            // 用户虚拟钱包
            Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                    .findVirtualWallet(currentUser.getFid());
            modelAndView.addObject("fvirtualwallets", fvirtualwallets);
        }
        // 币种列表
        List<Map<String, Object>> cointTypeList = new ArrayList<>();
        // 当前交易币种信息
        Map<String, Object> coinMap = new HashMap<>();
        List<Map<String, Object>> sellList = new ArrayList<>();
        List<Map<String, Object>> buyList = new ArrayList<>();
        List<Map<String, Object>> orderList = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY from %s", "fvirtualcointype");
            ds.add("where fstatus=1 and fisCtcCoin = 1");
            ds.open();
            for (Record record : ds) {
                cointTypeList.add(record.getItems());
            }

            if (fId == -1) {
                if (cointTypeList.size() != 0) {
                    coinMap = cointTypeList.get(0);
                    fId = (int) cointTypeList.get(0).get("fId");
                }
            } else {
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice from %s", "fvirtualcointype");
                ds1.add("where fId = %s", fId);
                ds1.open();
                coinMap = ds1.getCurrent().getItems();
            }

            // 查询卖出订单
            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select ( select u.floginName from fuser u where u.fId = fBuyerId ) buyerName,");
            ds2.add("o.fNum,o.fCreateTime,o.fStatus,o.fType,c.fShortName");
            ds2.add("from %s o", "fctcorder");
            ds2.add("left join %s c on o.fCoinType = c.fId", "fvirtualcointype");
            ds2.add("where o.fType = %d", 1);
            // ds2.add("and fStatus = %d",2 );
            // ds2.add("and fCoinType = %s", fId);
            ds2.add("order by o.fCreateTime desc");
            ds2.setMaximum(10);
            ds2.open();
            for (Record record : ds2) {
                Map<String, Object> items = record.getItems();
                String name = (String) items.get("buyerName");
                if (name != null) {
                    if (name.indexOf("@") != -1) {
                        name = name.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
                    } else {
                        name = name.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    }
                } else {
                    name = "待分配";
                }
                items.put("buyerName", name);
                sellList.add(items);
            }
            // 查询买入订单
            MyQuery ds3 = new MyQuery(mysql);
            ds3.add("select ( select u.floginName from fuser u where u.fId = fSellerId ) sellerName,");
            ds3.add("o.fNum,o.fCreateTime,o.fStatus,o.fType,c.fShortName");
            ds3.add("from %s o", "fctcorder");
            ds3.add("left join %s c on o.fCoinType = c.fId", "fvirtualcointype");
            ds3.add("where o.fType = %d", 0);
            // ds3.add("and fCoinType = %s", fId);
            // ds3.add("and fStatus = %d",2 );
            ds3.add("order by o.fCreateTime desc");
            ds3.setMaximum(10);
            ds3.open();
            for (Record record : ds3) {
                Map<String, Object> items = record.getItems();
                String name = (String) items.get("sellerName");
                if (name != null) {
                    if (name.indexOf("@") != -1) {
                        name = name.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
                    } else {
                        name = name.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    }
                } else {
                    name = "待分配";
                }
                items.put("sellerName", name);
                buyList.add(items);
            }

            if (currentUser != null) {
                // 最近兑换记录
                MyQuery ds4 = new MyQuery(mysql);
                ds4.add("select fId,fNum,fCreateTime,fStatus,fType,fPrice,fCreateTime from %s", "fctcorder");
                ds4.add("where fCoinType = %s", fId);
                // ds3.add("and fStatus = %d",2 );
                ds4.add("and fCreateUser = %d", currentUser.getFid());
                ds4.add("order by fCreateTime desc");
                ds4.setMaximum(10);
                ds4.open();
                for (Record record : ds4) {
                    orderList.add(record.getItems());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("cointTypeList", cointTypeList);
        modelAndView.addObject("coinMap", coinMap);
        modelAndView.addObject("login_user", currentUser);
        modelAndView.addObject("buyList", buyList);
        modelAndView.addObject("sellList", sellList);
        modelAndView.addObject("orderList", orderList);
        modelAndView.setViewName("front/ctc/index");
        return modelAndView;
    }

    @RequestMapping("/ctc/merchantCenter")
    public ModelAndView merchantCenter(HttpServletRequest request, @RequestParam(required = true) int fId) {
        ModelAndView modelAndView = new ModelAndView();
        ArrayList<Object> orderList = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            // 查询商户接到的订单
            MyQuery ds = new MyQuery(mysql);
            ds.add("select t.*,u.fNickName nickname,c.fShortName ");
            ds.add("from (select fId, fBuyerId otherSideId, fCreateUser, fCreateTime ,fOrderNum, ");
            ds.add("fType, fCoinType, fPrice, fNum, fCnyTotal, fStatus  from %s", "fctcorder");
            ds.add("where fType = 0 AND fSellerId = %d", fId);
            ds.add("union");
            ds.add("select fId, fSellerId otherSideId, fCreateUser,fCreateTime, fOrderNum, fType, fCoinType, fPrice, fNum, fCnyTotal, fStatus "
                    + "from %s", "fctcorder");
            ds.add("where fType = 1 AND fBuyerId = %d) t", fId);
            ds.add("left join %s u on u.fId = t.otherSideId", "fuser");
            ds.add("left join %s c on c.fId = t.fCoinType", "fvirtualcointype");
            ds.add("order by fCreateTime desc");
            ds.open();
            for (Record record : ds) {
                orderList.add(record.getItems());
            }
        }
        modelAndView.addObject("orderList", orderList);
        modelAndView.setViewName("front/ctc/listForMerchant");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/ctc/trade")
    public String trade(HttpServletRequest request, @RequestParam(required = true) int tradeType,
            @RequestParam(required = true) int coinType, @RequestParam(required = true) double price,
            @RequestParam(required = true) double num,
            @RequestParam(required = false, defaultValue = "0") int fis) {
        JSONObject jsonObject = new JSONObject();
        Map<Integer, Fvirtualwallet> fvirtualwallets;
        Map<String, Object> coinMap = new HashMap<>();
        int orderFid = 0;
        Fuser currentUser = GetCurrentUser(request);
        if (currentUser != null) {
            // 用户虚拟钱包
            fvirtualwallets = this.frontUserService.findVirtualWallet(currentUser.getFid());
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先登录");
            return jsonObject.toString();
        }
        String systemArgs = frontSystemArgsService.getSystemArgs("c2cSwitch");
        if ("off".equals(systemArgs)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "C2C交易暂未开启");
            return jsonObject.toString();
        }

        try (Mysql mysql = new Mysql()) {

            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select u.fid from %s u", "fuser");
            ds2.add("inner join %s b on b.FUs_fId = u.fid ", "fbankinfo_withdraw");
            ds2.add("where u.fid = %d", GetCurrentUser(request).getFid());
            ds2.open();
            if (ds2.eof()) {
                jsonObject.accumulate("code", -4);
                jsonObject.accumulate("msg", "未绑定银行卡");
                return jsonObject.toString();
            }
            // 查询订单
            if (fis == 0) {
                MyQuery ds = new MyQuery(mysql);
                ds.add("select * from %s", "fctcorder");
                ds.add("where fCreateUser='%s'", currentUser.getFid());
                ds.add(" and fType = '%s'", tradeType);
                ds.add(" and fStatus in (0,1)");
                ds.open();
                if (!ds.eof()) {
                    jsonObject.accumulate("code", "500");
                    jsonObject.accumulate("msg", "有订单在派单或确认中,请稍后下单!");
                    return jsonObject.toString();
                }
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select fId, fShortName,fctcSellPrice,fcurrentCNY,fctcBuyPrice from %s", "fvirtualcointype");
            ds1.add("where fstatus=1 and ftype != 0");
            ds1.add("and fId = %s", coinType);
            ds1.open();
            coinMap = ds1.getCurrent().getItems();

        }
        if (num <= 0) {
            jsonObject.accumulate("code", -2);
            jsonObject.accumulate("msg", "买入卖出量输入有误");
            return jsonObject.toString();
        }
        if (tradeType == 0) { // 买
            if (price != (double) coinMap.get("fctcBuyPrice")) {
                jsonObject.accumulate("code", -2);
                jsonObject.accumulate("msg", "数据异常");
                return jsonObject.toString();
            }
        } else {
            if (price != (double) coinMap.get("fctcSellPrice")) {
                jsonObject.accumulate("code", -2);
                jsonObject.accumulate("msg", "数据异常");
                return jsonObject.toString();
            }
        }
        BigDecimal priceD = new BigDecimal(price);
        BigDecimal numD = new BigDecimal(num);
        BigDecimal totalCnyD = priceD.multiply(numD);
        if (tradeType == 1) {
            Fvirtualwallet fvirtualwallet = fvirtualwallets.get(coinType);
            double ftotal = fvirtualwallet.getFtotal();
            if (num > ftotal) {
                jsonObject.accumulate("code", -2);
                jsonObject.accumulate("msg", "当前余额不足,交易失败");
                return jsonObject.toString();
            }
        }

        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery ds = new MyQuery(handle);
            ds.add("select * from %s", "fctcorder");
            ds.setMaximum(0);
            ds.open();
            Calendar calendar = Calendar.getInstance();
            String year = calendar.get(Calendar.YEAR) + "";
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String monthStr = month + "", dayStr = "";
            if (month < 10) {
                monthStr = "0" + month;
            }
            if (day < 10) {
                dayStr = "0" + day;
            }
            ds.append();
            int randonNum = (int) (Math.random() * 900) + 100;
            ds.setField("fOrderNum", year + monthStr + dayStr + new Date().getTime() + randonNum);
            ds.setField("fCreateTime", TDateTime.Now());
            ds.setField("fCreateUser", currentUser.getFid());
            ds.setField("fType", tradeType);
            ds.setField("fCoinType", coinType);
            if (tradeType == 0) {
                ds.setField("fBuyerId", currentUser.getFid());
            } else {
                ds.setField("fSellerId", currentUser.getFid());
            }
            String taskId = utils.newGuid();
            ds.setField("fPrice", price);
            ds.setField("fNum", num);
            ds.setField("fCnyTotal", totalCnyD.doubleValue());
            ds.setField("fStatus", 0);
            ds.setField("taskId", taskId);
            ds.post();
            orderFid = ds.getInt("fId");
            if (tradeType == 1) {
                try {
                    BatchScript script = new BatchScript(handle);
                    WalletAmount wallet = new WalletAmount(handle, currentUser.getFid(), coinType, taskId);
                    wallet.lock(script, num, "C2C挂单");
                    script.exec();
                } catch (Exception e) {
                    jsonObject.accumulate("code", -3);
                    jsonObject.accumulate("msg", e.getMessage());
                    return jsonObject.toString();
                }
            }
            tx.commit();
            ServiceStatus serviceStatus = new ServiceStatus(false);
            if (!assignOrder(serviceStatus)) {
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", serviceStatus.getMessage());
                return jsonObject.toString();
            }
        } catch (Exception e) {
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", "发生异常，请稍后重试");
            return jsonObject.toString();
        }
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("orderFid", orderFid);
        return jsonObject.toString();
    }

    @RequestMapping("/ctc/instructions")
    public ModelAndView instructions(HttpServletRequest request) {
        JspPage jspPage = new JspPage(request);
        List<Fabout> faboutList = this.frontOthersService.findFaboutByProperty("ftitle", "C2C操作说明");
        Fabout fabout = null;
        if (faboutList.size() != 0) {
            fabout = faboutList.get(0);
        }
        jspPage.addObject("fabout", fabout);
        jspPage.setViewName("/front/ctc/instructions");
        return jspPage;
    }

    @ResponseBody
    @RequestMapping(value = { "/ctc/confirmOrder" }, produces = JsonEncode)
    public String confirmOrder(@RequestParam(required = true) String orderNo, HttpServletRequest request) {

        JSONObject json = new JSONObject();
        ServiceStatus serviceStatus = new ServiceStatus(false);

        if (GetCurrentUser(request) == null) {
            json.accumulate("code", -1);
            json.accumulate("msg", "尚未登陆");
            return json.toString();
        }

        boolean payOrder = payOrder(orderNo, serviceStatus);
        if (payOrder) {
            json.accumulate("code", 0);
            json.accumulate("msg", "放行成功!");
        } else {
            json.accumulate("code", -1);
            json.accumulate("msg", serviceStatus.getMessage());
        }
        return json.toString();
    }

    public boolean payOrder(String orderNo, ServiceStatus result) {
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery mqOrder = new MyQuery(handle);
            mqOrder.add("select fId,fOrderNum,fType,fCoinType,fSellerId,fBuyerId,fPrice,fNum,");
            mqOrder.add("fCnyTotal,fStatus,fUpdateUser,fConfirmTime from %s", "fctcorder");
            mqOrder.add("where fOrderNum='%s'", orderNo);
            mqOrder.add("and (fStatus = 1 or fStatus = 3)");
            mqOrder.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            mqOrder.open();
            if (mqOrder.eof()) {
                result.setMessage("订单不存在或者状态不对");
                return false;
            }
            int fCoinType = mqOrder.getInt("fCoinType");
            int fSellerId = mqOrder.getInt("fSellerId");
            int fBuyerId = mqOrder.getInt("fBuyerId");
            int fNum = mqOrder.getInt("fNum");
            String taskId = utils.newGuid();
            BatchScript script = new BatchScript(handle);
            WalletAmount wallet = new WalletAmount(handle, fSellerId, fCoinType, taskId);
            wallet.unlock(script, fNum, "C2C成交解冻");
            wallet.moveTo(script, fBuyerId, fNum, "C2C成交转帐");
            script.exec();
            
            mqOrder.edit();
            mqOrder.setField("fStatus", 2);// 完成
            mqOrder.setField("fUpdateUser", fSellerId);
            mqOrder.setField("fConfirmTime", TDateTime.Now());
            mqOrder.setField("taskId", taskId);
            mqOrder.post();
            tx.commit();
            // 发送交易成功通知
            Fuser buyer = frontUserService.findById(fBuyerId);
            ShortMessageService.send(buyer.getFtelephone(), "【瑞银钱包】您订单尾号为" + orderNo.substring(orderNo.length() - 6)
                    + "订单于" + TDateTime.Now().toString() + "完成交易。");
        } catch (Exception e) {
            result.setMessage("放行失败,请联系客服");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean frozen(int fCoinType, int fuid, double fNum, ServiceStatus result) {
        try (Mysql mysql = new Mysql()) {
            MyQuery mqAccount = new MyQuery(mysql);
            mqAccount.add("select fId,fVi_fId,fTotal,fFrozen,fuid,fLastUpdateTime from %s", "fvirtualwallet");
            mqAccount.add("where fVi_fId=%d", fCoinType);
            mqAccount.add("and fuid=%d", fuid);
            mqAccount.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            mqAccount.open();
            if (mqAccount.eof()) {
                result.setMessage("用户账户不存在");
                return false;
            }
            double fTotal = mqAccount.getDouble("fTotal");
            double fFrozen = mqAccount.getDouble("fFrozen");
            double overTotal = fTotal - fNum;
            double overFrozen = fFrozen + fNum;
            if (fNum <= 0 || fTotal <= 0 || overTotal < 0 || overFrozen < 0) {
                result.setMessage("订单金额错误");
                return false;
            }
            mqAccount.edit();
            mqAccount.setField("fTotal", overTotal);
            mqAccount.setField("fFrozen", overFrozen);
            mqAccount.setField("fLastUpdateTime", TDateTime.Now());
            mqAccount.post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean assignOrder(ServiceStatus result) {
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery dsFctcorder = new MyQuery(handle);
            dsFctcorder.add(
                    "select fId,fOrderNum,fType,fCoinType,fSellerId,fBuyerId,fPrice,fNum,fStatus,fSellerId,fCreateUser");
            dsFctcorder.add("from %s", "fctcorder");
            dsFctcorder.add("where fstatus=0 and fCreateUser in (select fid from fuser)");
            dsFctcorder.add(" order by fCreateTime desc");
            dsFctcorder.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            dsFctcorder.open();
            while (dsFctcorder.fetch()) {
                // 获取下单用户身份
                int fCreateUser = dsFctcorder.getInt("fCreateUser");
                Fuser user = frontUserService.findById(fCreateUser);
                int fCoinType = dsFctcorder.getInt("fCoinType");
                double fNum = dsFctcorder.getDouble("fNum");
                if (dsFctcorder.getInt("fType") == 0) {// 0买 1卖
                    // 获取要分配的商家
                    MyQuery ds = new MyQuery(handle);
                    ds.add("select u.fId ");
                    ds.add("from %s u inner join %s a", "fuser", "fvirtualwallet");
                    ds.add("on u.fId=a.fuid");
                    ds.add("where a.fVi_fId=%d", fCoinType);
                    ds.add("and a.fTotal>=%f", fNum);
                    if (user.getfIsMerchant() == 0) {
                        ds.add("and fIsMerchant=1");
                    } else {
                        ds.add("and fIsMerchant=2");
                    }
                    ds.add("and u.fId<>'%s'", dsFctcorder.getInt("fBuyerId"));
                    ds.open();
                    if (!ds.eof()) {
                        int index = (int) (Math.random() * ds.size());
                        Record record = ds.getIndex(index);
                        int userID = record.getInt("fId");// 商家ID
                        // 更新订单状态和商家id
                        String taskId=utils.newGuid();
                        dsFctcorder.edit();
                        dsFctcorder.setField("fSellerId", userID);
                        dsFctcorder.setField("fStatus", 1);
                        dsFctcorder.setField("fUpdateUser", userID);
                        dsFctcorder.setField("taskId", taskId);
                        dsFctcorder.post();
                        // 更新商家账户冻结款
                        BatchScript script = new BatchScript(handle);
                        WalletAmount wallet = new WalletAmount(handle, userID, fCoinType, taskId);
                        wallet.lock(script, fNum, "商家冻结");
                        script.exec();
                    }
                } else if (dsFctcorder.getInt("fType") == 1) {
                    // 获取要分配的商家
                    MyQuery ds = new MyQuery(handle);
                    ds.add("select u.fId ");
                    ds.add("from %s u inner join %s a", "fuser", "fvirtualwallet");
                    ds.add("on u.fId=a.fuid");
                    ds.add("where a.fVi_fId=%d", dsFctcorder.getInt("fCoinType"));
                    if (user.getfIsMerchant() == 0) {
                        ds.add("and fIsMerchant=1");
                    } else {
                        ds.add("and fIsMerchant=2");
                    }
                    ds.add("and u.fId<>'%s'", dsFctcorder.getInt("fSellerId"));
                    ds.open();
                    if (!ds.eof()) {
                        int index = (int) (Math.random() * ds.size());
                        Record record = ds.getIndex(index);
                        int userID = record.getInt("fId");// 商家ID

                        dsFctcorder.edit();
                        dsFctcorder.setField("fBuyerId", userID);
                        dsFctcorder.setField("fStatus", 1);
                        dsFctcorder.setField("fUpdateUser", userID);
                        dsFctcorder.post();
                    }
                } else {
                    result.setMessage("订单类别错误!");
                }
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // 查询订单列表
    @RequestMapping(value = { "/ctc/list", "/ctc/list2" })
    public ModelAndView ctcList(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "-1") int fId) {
        ModelAndView ModelAndView1 = new ModelAndView();

        Fuser currentUser = GetCurrentUser(request);
        int currenUserId = 0;
        if (currentUser != null) {
            currenUserId = currentUser.getFid();
        }

        List<Map<String, Object>> orderList = new ArrayList<>();
        try (Mysql sql = new Mysql()) {
            MyQuery list = new MyQuery(sql);
            list.add("select a1.fNickName as buyerName,a2.fNickName as sellName,");
            list.add("b.fId,b.fOrderNum,b.fType,b.fCnyTotal,b.fNum,b.fCreateTime,");
            list.add("b.fStatus,b.fCreateUser,b.fBuyerId,b.fSellerId,t.fShortName AS cointype");
            list.add("from %s b", "fctcorder");
            list.add("left join %s a1 on a1.fId =b.fBuyerId", "fuser");
            list.add("left join %s a2 on a2.fId =b.fSellerId", "fuser");
            list.add("left join %s t on t.fId=b.fCoinType", "fvirtualcointype");
            list.add("where b.fCreateUser=%d", currenUserId);
            list.add("order by b.fCreateTime desc");
            list.open();
            for (Record record : list) {
                orderList.add(record.getItems());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView1.addObject("orderList", orderList);
        ModelAndView1.addObject("currentUser", currentUser);
        if (request.getRequestURI().startsWith("/ctc/list2")) {
            ModelAndView1.setViewName("front/trade/list");
        } else {
            ModelAndView1.setViewName("front/ctc/list");
        }
        return ModelAndView1;
    }

    // 查询订单详情
    @RequestMapping("/ctc/details")
    public ModelAndView ctcDetails(HttpServletRequest request, @RequestParam(required = true) int fId) {
        ModelAndView ModelAndView2 = new ModelAndView();
        Fuser currentUser = GetCurrentUser(request);
        int currenUserId = 0;
        if (currentUser != null) {
            currenUserId = currentUser.getFid();
        }
        List<Map<String, Object>> detailsList = new ArrayList<>();
        Map<String, Object> payTyte1 = new HashMap<String, Object>();
        try (Mysql sql1 = new Mysql()) {
            MyQuery list = new MyQuery(sql1);
            list.add("select a.fNickName as sellName,c.fNickName as buyerName,t.fShortName AS cointype,b.*,");
            list.add("b.fSellerId as sfId,b.fBuyerId as bfId");
            list.add("from %s b", "fctcorder");
            list.add("left join %S a on a.fId =b.fSellerId", "fuser");
            list.add("left join %s c on c.fId =b.fBuyerId", "fuser");
            list.add("left join %s t on t.fId=b.fCoinType", "fvirtualcointype");
            list.add("where b.fId=%d", fId);
            list.open();
            list.setMaximum(1);
            for (Record record : list) {
                detailsList.add(record.getItems());
            }

            // 查询付款方式
            MyQuery payType = new MyQuery(sql1);
            payType.add("select a.fNickName as nickName,a.fRealName as realName,b.*");
            payType.add("from %s b", "fbankinfo_withdraw");
            payType.add("left join %s a on a.fId=b.FUs_fId", "fuser");
            payType.add("where a.fId=%d", list.getInt("sfId"));
            payType.open();
            if (!payType.eof()) {
                payTyte1 = payType.getCurrent().getItems();
            }
            // 查询付款方式
            MyQuery buyType = new MyQuery(sql1);
            buyType.add("select a.fNickName as nickName,a.fRealName as realName,b.*");
            buyType.add("from %s b", "fbankinfo_withdraw");
            buyType.add("left join %s a on a.fId=b.FUs_fId", "fuser");
            buyType.add("where a.fId=%d", list.getInt("bfId"));
            buyType.open();
            if (!buyType.eof()) {
                ModelAndView2.addObject("fBuyName", buyType.getString("fName"));
                ModelAndView2.addObject("fBuyBankNumber", buyType.getString("fBankNumber"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView2.addObject("detailsList", detailsList);
        ModelAndView2.addObject("payType", payTyte1);
        ModelAndView2.addObject("currentUser", currentUser);
        ModelAndView2.setViewName("front/ctc/details");
        return ModelAndView2;
    }

    @RequestMapping("/ctc/orderInfo")
    @ResponseBody
    public String orderInfo(@RequestParam(required = true) String orderFid) {
        JSONObject json = new JSONObject();
        Map<String, Object> items = new HashMap<>();
        double fCnyTotal = 0f;
        String[] status = new String[] { "待派单", "待确认", "交易完成" };
        String statusName = "";
        int fStatus;
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fSellerId,fStatus,fCnyTotal from %s", "fctcorder");
            ds.add("where fId = %s", orderFid);
            ds.open();
            if (ds.eof()) {
                json.accumulate("code", -1);
                json.accumulate("data", "订单号不存在");
                return json.toString();
            }
            String fSellerId = ds.getString("fSellerId");
            if (StringUtils.isEmpty(fSellerId)) {
                json.accumulate("code", -1);
                json.accumulate("data", "系统派单中，稍后进入订单详情查看卖家收款信息");
                return json.toString();
            }
            fCnyTotal = ds.getDouble("fCnyTotal");
            fStatus = ds.getInt("fStatus");
            statusName = status[fStatus];

            MyQuery payType = new MyQuery(mysql);
            payType.add("select a.fNickName as nickName,a.fRealName as realName,b.*");
            payType.add("from %s b", "fbankinfo_withdraw");
            payType.add("left join %s a on a.fId=b.FUs_fId", "fuser");
            payType.add("where B.FUs_fId=%s", fSellerId);
            payType.open();
            if (!payType.eof()) {
                items = payType.getCurrent().getItems();
            }
        }
        items.put("fCnyTotal", fCnyTotal);
        items.put("statusName", statusName);
        items.put("fStatus", fStatus);
        json.accumulate("data", items);
        json.accumulate("code", 0);
        return json.toString();
    }

    // 提交申诉申请
    @RequestMapping("/ctc/appeal")
    public ModelAndView ctcAppeal(HttpServletRequest request, @RequestParam(required = true) int orderId) {
        ModelAndView ModelAndView = new ModelAndView();
        ModelAndView.addObject("orderId", orderId);
        ModelAndView.setViewName("front/ctc/appeal");
        return ModelAndView;
    }

    // 保存申诉申请
    @ResponseBody
    @RequestMapping(value = "/ctc/addAppeal", produces = JsonEncode)
    public String AddctcAppeal(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();

        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s", "fctcorderappeal");
            ds.add("where fctcorderid = %s", request.getParameter("orderId"));
            ds.open();
            if (ds.eof()) {
                ds.append();
                ds.setField("fctcorderid", request.getParameter("orderId"));
                ds.setField("fimgpath1", request.getParameter("path1"));
                ds.setField("fimgpath2", request.getParameter("path2"));
                ds.setField("fimgpath3", request.getParameter("path3"));
                ds.setField("fremark", request.getParameter("remark"));
                ds.post();

                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select * from %s", "fctcorder");
                ds1.add("where fId = %s", request.getParameter("orderId"));
                ds1.open();
                if (!ds1.eof()) {
                    ds1.edit();
                    ds1.setField("fappeal", 1);
                    ds1.setField("fStatus", 3);
                    ds1.post();
                }
                resultJson.accumulate("code", 0);
                resultJson.accumulate("msg", "提交成功!");
            } else {
                resultJson.accumulate("code", 1);
                resultJson.accumulate("msg", "一个订单只能申诉一次，不可重复提交申诉!");
            }
        }
        return resultJson.toString();
    }

    // 取消C2C挂单
    @ResponseBody
    @RequestMapping(value = "/ctc/cancel", produces = JsonEncode)
    public String cancel(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery ds = new MyQuery(handle);
            ds.add("select * from %s", "fctcorder");
            ds.add("where fId = %s", request.getParameter("orderId"));
            ds.open();
            if (!ds.eof()) {
                int fStatus = ds.getInt("fStatus");
                if (2 == fStatus) {
                    resultJson.accumulate("code", 500);
                    resultJson.accumulate("msg", "订单为已确认状态,禁止取消");
                    return resultJson.toString();
                }
                String taskId = utils.newGuid();
                ds.edit();
                ds.setField("fStatus", 4);
                ds.setField("taskId", taskId);
                ds.post();
                int ftype = ds.getInt("ftype");
                int fCoinType = ds.getInt("fCoinType");// 获取币种的类型
                double fnum = ds.getDouble("fNum"); // 获取币种的数量
                if (0 != fStatus) {
                    BatchScript script = new BatchScript(handle);
                    if (0 == ftype) {
                        int fSellerId = ds.getInt("fSellerId");// 商家id
                        WalletAmount wallet = new WalletAmount(handle, fSellerId, fCoinType, taskId);
                        wallet.unlock(script, fnum, "取消ctc订单");
                    } else {
                        int fBuyerId = ds.getInt("fBuyerId");// 买方id
                        WalletAmount wallet = new WalletAmount(handle, fBuyerId, fCoinType, taskId);
                        wallet.unlock(script, fnum, "取消ctc订单");
                    }
                    script.exec();
                }
            }
            tx.commit();
            resultJson.accumulate("code", 0);
            resultJson.accumulate("msg", "订单取消成功!");
        } catch (Exception e) {
            resultJson.accumulate("code", 1);
            resultJson.accumulate("msg", "订单取消失败！");
        }
        return resultJson.toString();
    }

    // 查看申诉申请
    @ResponseBody
    @RequestMapping(value = "/ssadmin/seeAppeal", produces = JsonEncode)
    public String seeAppeal(HttpServletRequest request, @RequestParam(required = true) int orderId) {
        JSONObject json = new JSONObject();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add(" select a.fimgpath1,a.fimgpath2,a.fimgpath3,a.fremark,o.fType from %s a ", "fctcorderappeal");
            ds.add(" inner JOIN fctcorder o ");
            ds.add(" on o.fid = a.fctcorderid ");
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

    // 买入订单申诉成功，修改订单
    @ResponseBody
    @RequestMapping(value = "/ssadmin/successAppeal", produces = JsonEncode)
    public String successAppeal(HttpServletRequest request, @RequestParam(required = true) int orderNo) {
        JSONObject resultJson = new JSONObject();
        Fadmin fadmin = (Fadmin) request.getSession().getAttribute("login_admin");
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add(" select * from %s  ", "fctcorder");
            ds.add(" where fId = '%s' ", orderNo);
            ds.open();
            if (!ds.eof()) {
                ds.edit();
                ds.setField("fStatus", 2);// 订单状态改成 已完成
                ds.setField("fappeal", 2);// 申诉状态改为 申诉完成fappeal
                ds.setField("fremark", "管理员" + fadmin.getFname() + "在" + TDate.Now() + "时间通过后台放行");
                ds.post();
                resultJson.accumulate("code", 0);
            } else {
                resultJson.accumulate("code", 1);
            }
        }
        return resultJson.toString();
    }

    // 买入订单申诉
    @ResponseBody
    @RequestMapping(value = { "/ssadmin/confirmOrder1" }, produces = JsonEncode)
    public String confirmOrder1(HttpServletRequest request, @RequestParam(required = true) String OrderNum) {
        JSONObject json = new JSONObject();
        ServiceStatus serviceStatus = new ServiceStatus(false);
        boolean payOrder = payOrder(OrderNum, serviceStatus);
        if (payOrder) {
            json.accumulate("code", 0);
        } else {
            json.accumulate("code", -1);
            json.accumulate("msg", serviceStatus.getMessage());
        }
        return json.toString();
    }

    // 卖出订单申诉
    @ResponseBody
    @RequestMapping(value = { "/ssadmin/confirmOrder2" }, produces = JsonEncode)
    public String confirmOrder2(HttpServletRequest request, @RequestParam(required = true) String orderNo) {
        JSONObject json = new JSONObject();
        Fadmin fadmin = (Fadmin) request.getSession().getAttribute("login_admin");
        // 查询订单
        try (Mysql handle = new Mysql()) {
            MyQuery ds = new MyQuery(handle);
            ds.add(" select * from %s  ", "fctcorder");
            ds.add(" where fId = '%s' ", orderNo);
            ds.open();
            if (!ds.eof()) {
                // 准备往钱包，回退冻结资金
                int fCoinType = ds.getInt("fCoinType");// 获取币种
                int fSellerId = ds.getInt("fSellerId");// 获取卖方的id
                double fNum = ds.getDouble("fNum");// 获取币种数量
                String taskId = utils.newGuid();
                try {
                    BatchScript script = new BatchScript(handle);
                    WalletAmount wallet = new WalletAmount(handle, fSellerId, fCoinType, taskId);
                    wallet.unlock(script, fNum, "卖单申诉解冻");
                    script.exec();
                    
                    // 修改订单状态
                    ds.edit();
                    ds.setField("taskId", taskId);
                    ds.setField("fStatus", 4);// 订单状态改成 已取消
                    ds.setField("fappeal", 2);// 申诉状态改为 申诉完成
                    ds.setField("fremark", "管理员" + fadmin.getFname() + "在" + TDate.Now() + "时间通过后台放行");
                    ds.post();

                    json.accumulate("code", "200");
                    json.accumulate("msg", "取消订单成功!");
                } catch (Exception e) {
                    json.accumulate("code", "500");
                    json.accumulate("msg", e.getMessage());
                    return json.toString();
                }
            }
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/ctc/querydetails" }, produces = JsonEncode)
    public String sjctcDetails(HttpServletRequest request) {
        String time = request.getParameter("time");// 时间
        String type = request.getParameter("type");// 买0 卖1
        String upDown = request.getParameter("upDown");// 上一篇 0 下一篇 1
        String dataType = request.getParameter("dataType");// 商家自己下的单 0 客户下给商家的单
        String orderId = request.getParameter("orderId");// 当前订单id

        JSONObject json = new JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        int currenUserId = 0;
        if (currentUser != null) {
            currenUserId = currentUser.getFid();
        }

        String fId = "";// 订单id
        if (dataType.equals("0")) {
            try (Mysql sql = new Mysql()) {
                MyQuery list = new MyQuery(sql);
                list.add("SELECT * FROM fctcorder");
                list.add("WHERE fCreateUser = %d", currenUserId);
                list.add("and fType = %s", type);
                if (upDown.equals("0")) {
                    list.add("and fCreateTime > '%s'", time);
                    list.add("ORDER BY fCreateTime ASC");
                } else {
                    list.add("and fCreateTime < '%s'", time);
                    list.add("ORDER BY fCreateTime DESC");
                }
                list.open();
                if (!list.eof()) {
                    fId = list.getIndex(0).getString("fId");
                } else {
                    json.accumulate("code", 500);
                    json.accumulate("msg", "目前已是当前/最后,暂未查询出新数据");
                    return json.toString();
                }
            }
        } else {
            try (Mysql sql = new Mysql()) {
                MyQuery list = new MyQuery(sql);
                list.add("SELECT * FROM fctcorder");
                list.add("WHERE 1=1 ");
                if (type.equals("0"))
                    list.add("and fSellerId = %d", currenUserId);
                else
                    list.add("and fBuyerId = %d", currenUserId);
                list.add("and fType = %s", type);
                if (upDown.equals("0")) {
                    list.add("and fCreateTime > '%s'", time);
                    list.add("ORDER BY fCreateTime ASC");
                } else {
                    list.add("and fCreateTime < '%s'", time);
                    list.add("ORDER BY fCreateTime DESC");
                }
                list.open();
                if (!list.eof()) {
                    fId = list.getIndex(0).getString("fId");
                } else {
                    json.accumulate("code", 500);
                    json.accumulate("msg", "目前已是当前/最后，暂未查询出数据");
                    return json.toString();
                }
            }
        }
        if (orderId.equals(fId)) {
            json.accumulate("code", 500);
            json.accumulate("msg", "目前已是当前/最后，暂未查询出数据");
            return json.toString();
        }

        json.accumulate("code", 200);
        json.accumulate("fId", fId);
        return json.toString();
    }

    // C2C商户自己订单详情界面
    @RequestMapping("/ctc/sjdetails")
    public ModelAndView ctcsjDetails(HttpServletRequest request, @RequestParam(required = true) int fId) {
        ModelAndView ModelAndView2 = new ModelAndView();
        Fuser currentUser = GetCurrentUser(request);
        int currenUserId = 0;
        if (currentUser != null) {
            currenUserId = currentUser.getFid();
        }
        List<Map<String, Object>> detailsList = new ArrayList<>();
        Map<String, Object> payTyte1 = new HashMap<String, Object>();
        try (Mysql sql1 = new Mysql()) {
            MyQuery list = new MyQuery(sql1);
            list.add("select a.fNickName as sellName,c.fNickName as buyerName,t.fShortName AS cointype,b.*,");
            list.add("b.fSellerId as sfId,b.fBuyerId as bfId");
            list.add("from %s b", "fctcorder");
            list.add("left join %S a on a.fId =b.fSellerId", "fuser");
            list.add("left join %s c on c.fId =b.fBuyerId", "fuser");
            list.add("left join %s t on t.fId=b.fCoinType", "fvirtualcointype");
            list.add("where b.fId=%d", fId);
            list.open();
            list.setMaximum(1);
            for (Record record : list) {
                detailsList.add(record.getItems());
            }

            // 查询付款方式
            MyQuery payType = new MyQuery(sql1);
            payType.add("select a.fNickName as nickName,a.fRealName as realName,b.*");
            payType.add("from %s b", "fbankinfo_withdraw");
            payType.add("left join %s a on a.fId=b.FUs_fId", "fuser");
            payType.add("where a.fId=%d", list.getInt("sfId"));
            payType.open();
            if (!payType.eof()) {
                payTyte1 = payType.getCurrent().getItems();
            }
            // 查询付款方式
            MyQuery buyType = new MyQuery(sql1);
            buyType.add("select a.fNickName as nickName,a.fRealName as realName,b.*");
            buyType.add("from %s b", "fbankinfo_withdraw");
            buyType.add("left join %s a on a.fId=b.FUs_fId", "fuser");
            buyType.add("where a.fId=%d", list.getInt("bfId"));
            buyType.open();
            if (!buyType.eof()) {
                ModelAndView2.addObject("fBuyName", buyType.getString("fName"));
                ModelAndView2.addObject("fBuyBankNumber", buyType.getString("fBankNumber"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView2.addObject("detailsList", detailsList);
        ModelAndView2.addObject("payType", payTyte1);
        ModelAndView2.addObject("currentUser", currentUser);
        ModelAndView2.setViewName("front/ctc/sjdetails");
        return ModelAndView2;
    }

    // C2C商户订单详情界面
    @RequestMapping("/ctc/sjdetails1")
    public ModelAndView ctcsjDetails1(HttpServletRequest request, @RequestParam(required = true) int fId) {
        ModelAndView ModelAndView2 = new ModelAndView();
        Fuser currentUser = GetCurrentUser(request);
        int currenUserId = 0;
        if (currentUser != null) {
            currenUserId = currentUser.getFid();
        }
        List<Map<String, Object>> detailsList = new ArrayList<>();
        Map<String, Object> payTyte1 = new HashMap<String, Object>();
        try (Mysql sql1 = new Mysql()) {
            MyQuery list = new MyQuery(sql1);
            list.add("select a.fNickName as sellName,c.fNickName as buyerName,t.fShortName AS cointype,b.*,");
            list.add("b.fSellerId as sfId,b.fBuyerId as bfId");
            list.add("from %s b", "fctcorder");
            list.add("left join %S a on a.fId =b.fSellerId", "fuser");
            list.add("left join %s c on c.fId =b.fBuyerId", "fuser");
            list.add("left join %s t on t.fId=b.fCoinType", "fvirtualcointype");
            list.add("where b.fId=%d", fId);
            list.open();
            list.setMaximum(1);
            for (Record record : list) {
                detailsList.add(record.getItems());
            }

            // 查询付款方式
            MyQuery payType = new MyQuery(sql1);
            payType.add("select a.fNickName as nickName,a.fRealName as realName,b.*");
            payType.add("from %s b", "fbankinfo_withdraw");
            payType.add("left join %s a on a.fId=b.FUs_fId", "fuser");
            payType.add("where a.fId=%d", list.getInt("sfId"));
            payType.open();
            if (!payType.eof()) {
                payTyte1 = payType.getCurrent().getItems();
            }
            // 查询付款方式
            MyQuery buyType = new MyQuery(sql1);
            buyType.add("select a.fNickName as nickName,a.fRealName as realName,b.*");
            buyType.add("from %s b", "fbankinfo_withdraw");
            buyType.add("left join %s a on a.fId=b.FUs_fId", "fuser");
            buyType.add("where a.fId=%d", list.getInt("bfId"));
            buyType.open();
            if (!buyType.eof()) {
                ModelAndView2.addObject("fBuyName", buyType.getString("fName"));
                ModelAndView2.addObject("fBuyBankNumber", buyType.getString("fBankNumber"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView2.addObject("detailsList", detailsList);
        ModelAndView2.addObject("payType", payTyte1);
        ModelAndView2.addObject("currentUser", currentUser);
        ModelAndView2.setViewName("front/ctc/sjdetails1");
        return ModelAndView2;
    }
}
