package com.huagu.vcoin.main.controller.front;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.coa.common.cons.EntrustStatusEnum;
import com.huagu.coa.common.cons.EntrustTypeEnum;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fentrust;
import com.huagu.vcoin.main.model.Fentrustlog;
import com.huagu.vcoin.main.model.Flimittrade;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSender;
import com.huagu.vcoin.main.service.front.FrontTradeService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.util.Utils;

import net.sf.json.JSONObject;
import site.jayun.vcoin.bourse.core.BourseEngine;

@Controller
public class FrontTradeJsonController extends BaseController {
    private static final Logger log = Logger.getLogger(FrontTradeJsonController.class);
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private MessageSender messageSender;

    @ResponseBody
    @RequestMapping(value = "/trade/buyBtcSubmit", produces = { JsonEncode })
    public String buyBtcSubmit(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int limited, @RequestParam(required = true) int symbol,
            @RequestParam(required = true) double tradeAmount, @RequestParam(required = true) double tradeCnyPrice,
            @RequestParam(required = false, defaultValue = "") String tradePwd,
            @RequestParam(required = false, defaultValue = "0") double lastprice) throws Exception {
        limited = 0;

        JSONObject jsonObject = new JSONObject();

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        // if (tradeCnyPrice > 6 * lastprice || tradeCnyPrice < lastprice / 5) {
        // jsonObject.accumulate("resultCode",-500);
        // jsonObject.accumulate("msg", "挂单价格超出价格,请重新输入!");
        // return jsonObject.toString();
        // }
        if (ftrademapping == null || ftrademapping.getFstatus() == TrademappingStatusEnum.FOBBID) {
            jsonObject.accumulate("resultCode", -100);
            jsonObject.accumulate("msg", "该币暂时不能交易");
            return jsonObject.toString();
        }

        // 限制法币为人民币和比特币
        if (ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_CNY_VALUE
                && ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_COIN_VALUE) {
            jsonObject.accumulate("resultCode", -1);
            jsonObject.accumulate("msg", "网络错误");
            return jsonObject.toString();

        }

        Fvirtualcointype coin1 = ftrademapping.getFvirtualcointypeByFvirtualcointype1();
        Fvirtualcointype coin2 = ftrademapping.getFvirtualcointypeByFvirtualcointype2();
        double minBuyCount = ftrademapping.getFminBuyCount();
        double minBuyPrice = ftrademapping.getFminBuyPrice();
        double minBuyAmount = ftrademapping.getFminBuyAmount();
        double maxBuyCount = ftrademapping.getFmaxBuyCount();

        tradeAmount = Utils.getDouble(tradeAmount, ftrademapping.getFcount2());
        tradeCnyPrice = Utils.getDouble(tradeCnyPrice, ftrademapping.getFcount1());

        // 定价单
        if (limited == 0) {

            if (tradeAmount < minBuyCount) {
                jsonObject.accumulate("resultCode", -1);
                jsonObject.accumulate("msg", "最小交易数量：" + coin1.getfSymbol() + minBuyCount);
                return jsonObject.toString();
            }

            if (tradeAmount > maxBuyCount) {
                jsonObject.accumulate("resultCode", -1);
                jsonObject.accumulate("msg", "最大交易数量：" + coin1.getfSymbol() + maxBuyCount);
                return jsonObject.toString();
            }

            if (tradeCnyPrice < minBuyPrice) {
                jsonObject.accumulate("resultCode", -3);
                jsonObject.accumulate("msg", "最小挂单价格：" + coin1.getfSymbol() + minBuyPrice);
                return jsonObject.toString();
            }

            double total = Utils.getDouble(tradeAmount * tradeCnyPrice, ftrademapping.getFcount1());
            if (total < minBuyAmount) {
                jsonObject.accumulate("resultCode", -3);
                jsonObject.accumulate("msg", "最小挂单金额：" + coin1.getfSymbol() + minBuyAmount);
                return jsonObject.toString();
            }

            Flimittrade limittrade = this.isLimitTrade(ftrademapping.getFid());
            double upPrice = 0d;
            double downPrice = 0d;
            if (limittrade != null) {
                upPrice = Utils.getDouble(
                        limittrade.getFupprice() + limittrade.getFupprice() * limittrade.getFpercent(),
                        ftrademapping.getFcount1());
                downPrice = Utils.getDouble(
                        limittrade.getFdownprice() - limittrade.getFdownprice() * limittrade.getFpercent(),
                        ftrademapping.getFcount1());
                if (downPrice < 0)
                    downPrice = 0;
                if (tradeCnyPrice > upPrice) {
                    jsonObject.accumulate("resultCode", -500);
                    jsonObject.accumulate("msg", "挂单价格不能高于涨停价:" + upPrice + coin1.getFname());
                    return jsonObject.toString();
                }
                if (tradeCnyPrice < downPrice) {
                    jsonObject.accumulate("resultCode", -600);
                    jsonObject.accumulate("msg", "挂单价格不能低于跌停价:" + downPrice + coin1.getFname());
                    return jsonObject.toString();
                }
            }

        } else {
            if (tradeCnyPrice < minBuyAmount) {
                jsonObject.accumulate("resultCode", -33);
                jsonObject.accumulate("msg", "最小交易金额：" + minBuyAmount + coin1.getFname());
                return jsonObject.toString();
            }
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        if (Utils.openTrade(ftrademapping, Utils.getTimestamp()) == false/* && !fuser.isFistiger() */) {
            jsonObject.accumulate("resultCode", -400);
            jsonObject.accumulate("msg", "现在不是交易时间");
            return jsonObject.toString();
        }

        double totalTradePrice = 0F;
        if (limited == 0) {
            totalTradePrice = tradeAmount * tradeCnyPrice;
        } else {
            totalTradePrice = tradeAmount;
        }
        Fvirtualwallet fwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), coin1.getFid());
        if (fwallet.getFtotal() < totalTradePrice) {
            jsonObject.accumulate("resultCode", -4);
            jsonObject.accumulate("msg", coin1.getFname() + "余额不足");
            return jsonObject.toString();
        }
        double ffee = this.frontTradeService.getBuyFfee(symbol, fuser);
        if (fwallet.getFtotal() < (totalTradePrice + (totalTradePrice * ffee))) {
            jsonObject.accumulate("resultCode", -4);
            jsonObject.accumulate("msg", "交易额度过大,余额不足");
            return jsonObject.toString();
        }
        if (isNeedTradePassword(request)) {
            if (fuser.getFtradePassword() == null) {
                jsonObject.accumulate("resultCode", -5);
                jsonObject.accumulate("msg",
                        "您还没有设置交易密码，请到安全中心设置<a class='text-danger' href='/user/security.html'>安全中心&gt;&gt;</a>");
                return jsonObject.toString();
            }
        }

        String ip = getIpAddr(request);
        Fentrust fentrust = null;
        try {
            if (ftrademapping.getFmaxtimes() > 0 && !fuser.isFistiger()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String now = sdf.format(new Date());
                String sql = "where (fstatus in(1,2,3) or (fstatus =4 and fsuccessAmount >0)) and fuser.fid="
                        + fuser.getFid() + " and ftrademapping.fid=" + ftrademapping.getFid()
                        // + " and date_format(fCreateTime,'%Y-%m-%d')='" + now
                        // + "'";
                        + " and fCreateTime >= curdate() and curdate() ='" + now + "'";
                int times = this.adminService.getAllCount("Fentrust", sql);
                if (times >= ftrademapping.getFmaxtimes()) {
                    jsonObject.accumulate("resultCode", -600);
                    jsonObject.accumulate("msg", "每天最大有效挂单次数:" + ftrademapping.getFmaxtimes());
                    return jsonObject.toString();
                }
            }

            fuser.setFlastUpdateTime(Utils.getTimestamp());
            fentrust = this.frontTradeService.updateEntrustBuy(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1,
                    request);
            if (fentrust != null) {
                this.messageSender.publish(ChannelConstant.NEW_ENTRUSTS, String.valueOf(fentrust.getFid()));
                jsonObject.accumulate("resultCode", 0);
                jsonObject.accumulate("msg", "下单成功");
                setNoNeedPassword(request);
            } else {
                jsonObject.accumulate("code", -200);
                jsonObject.accumulate("msg", "网络错误，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("resultCode", -200);
            jsonObject.accumulate("msg", e.getMessage());
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/trade/sellBtcSubmit", produces = { JsonEncode })
    public String sellBtcSubmit(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int limited, // 是否按照市场价买入
            @RequestParam(required = true) int symbol, // 币种
            @RequestParam(required = true) double tradeAmount, // 数量
            @RequestParam(required = true) double tradeCnyPrice, // 单价
            @RequestParam(required = false, defaultValue = "") String tradePwd,
            @RequestParam(required = false, defaultValue = "0") double lastprice) throws Exception {
        limited = 0;

        JSONObject jsonObject = new JSONObject();
        // if (tradeCnyPrice > 6 * lastprice || tradeCnyPrice < lastprice / 5) {
        // jsonObject.accumulate("resultCode", -500);
        // jsonObject.accumulate("msg", "挂单价格超出价格,请重新输入!");
        // return jsonObject.toString();
        // }
        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        if (ftrademapping == null || ftrademapping.getFstatus() != TrademappingStatusEnum.ACTIVE) {
            jsonObject.accumulate("resultCode", -100);
            jsonObject.accumulate("msg", "该币暂时不能交易");
            return jsonObject.toString();
        }

        if (ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_CNY_VALUE
                && ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_COIN_VALUE) {
            jsonObject.accumulate("resultCode", -1);
            jsonObject.accumulate("msg", "网络错误");
            return jsonObject.toString();

        }

        tradeAmount = Utils.getDouble(tradeAmount, ftrademapping.getFcount2());
        tradeCnyPrice = Utils.getDouble(tradeCnyPrice, ftrademapping.getFcount1());
        Fvirtualcointype coin1 = ftrademapping.getFvirtualcointypeByFvirtualcointype1();
        Fvirtualcointype coin2 = ftrademapping.getFvirtualcointypeByFvirtualcointype2();
        double minBuyCount = ftrademapping.getFminSellCount();
        double maxSellCount = ftrademapping.getFmaxSellCount();
        double minBuyPrice = ftrademapping.getFminSellPrice();
        double minBuyAmount = ftrademapping.getFminSellAmount();

        if (limited == 0) {

            if (tradeAmount < minBuyCount) {
                jsonObject.accumulate("resultCode", -1);
                jsonObject.accumulate("msg", "最小交易数量：" + coin1.getfSymbol() + minBuyCount);
                return jsonObject.toString();
            }
            if (tradeAmount > maxSellCount) {
                jsonObject.accumulate("resultCode", -1);
                jsonObject.accumulate("msg", "最大交易数量：" + coin1.getfSymbol() + maxSellCount);
                return jsonObject.toString();
            }

            if (tradeCnyPrice < minBuyPrice) {
                jsonObject.accumulate("resultCode", -3);
                jsonObject.accumulate("msg", "最小挂单价格：" + coin1.getfSymbol() + minBuyPrice);
                return jsonObject.toString();
            }
            double total = Utils.getDouble(tradeAmount * tradeCnyPrice, ftrademapping.getFcount1());
            if (total < minBuyAmount) {
                jsonObject.accumulate("resultCode", -3);
                jsonObject.accumulate("msg", "最小挂单金额：" + coin1.getfSymbol() + minBuyAmount);
                return jsonObject.toString();
            }
            Flimittrade limittrade = this.isLimitTrade(ftrademapping.getFid());
            double upPrice = 0d;
            double downPrice = 0d;
            if (limittrade != null) {
                upPrice = Utils.getDouble(
                        limittrade.getFupprice() + limittrade.getFupprice() * limittrade.getFpercent(),
                        ftrademapping.getFcount1());
                downPrice = Utils.getDouble(
                        limittrade.getFdownprice() - limittrade.getFdownprice() * limittrade.getFpercent(),
                        ftrademapping.getFcount1());
                if (downPrice < 0)
                    downPrice = 0;
                if (tradeCnyPrice > upPrice) {
                    jsonObject.accumulate("resultCode", -500);
                    jsonObject.accumulate("msg", "挂单价格不能高于涨停价:" + upPrice + coin1.getFname());
                    return jsonObject.toString();
                }
                if (tradeCnyPrice < downPrice) {
                    jsonObject.accumulate("resultCode", -600);
                    jsonObject.accumulate("msg", "挂单价格不能低于跌停价:" + downPrice + coin1.getFname());
                    return jsonObject.toString();
                }
            }

        } else {
            if (tradeAmount < minBuyCount) {
                jsonObject.accumulate("resultCode", -33);
                jsonObject.accumulate("msg", "最小交易数量：" + minBuyCount + coin2.getFname());
                return jsonObject.toString();
            }
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        // 是否开放交易
        if (Utils.openTrade(ftrademapping, Utils.getTimestamp()) == false/* && !fuser.isFistiger() */) {
            jsonObject.accumulate("resultCode", -400);
            jsonObject.accumulate("msg", "现在不是交易时间");
            return jsonObject.toString();
        }

        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), coin2.getFid());
        if (fvirtualwallet == null) {
            jsonObject.accumulate("resultCode", -200);
            jsonObject.accumulate("msg", "系统错误，请联系管理员");
            return jsonObject.toString();
        }
        if (fvirtualwallet.getFtotal() < tradeAmount) {
            jsonObject.accumulate("resultCode", -4);
            jsonObject.accumulate("msg", coin2.getFname() + "余额不足");
            return jsonObject.toString();
        }
        double ffee = this.frontTradeService.getBuyFfee(symbol, fuser);
        if (fvirtualwallet.getFtotal() < tradeAmount ) {
            jsonObject.accumulate("resultCode", -4);
            jsonObject.accumulate("msg", "交易额度过大,余额不足");
            return jsonObject.toString();
        }
        if (!fuser.isFistiger()) {
            if (ftrademapping.isFislimit()) {
                if (fvirtualwallet.getFcanSellQty() < tradeAmount) {
                    jsonObject.accumulate("resultCode", -4);
                    jsonObject.accumulate("msg", "您今天最多还可以卖出:" + fvirtualwallet.getFcanSellQty());
                    return jsonObject.toString();
                }
            }
        }

        if (isNeedTradePassword(request)) {
            if (fuser.getFtradePassword() == null) {
                jsonObject.accumulate("resultCode", -5);
                jsonObject.accumulate("msg",
                        "您还没有设置交易密码，请到安全中心设置<a class='text-danger' href='/user/security.html'>安全中心&gt;&gt;</a>");
                return jsonObject.toString();
            }
        }

        String ip = getIpAddr(request);

        boolean flag = false;
        Fentrust fentrust = null;
        try {
            fuser.setFlastUpdateTime(Utils.getTimestamp());
            fentrust = this.frontTradeService.updateEntrustSell(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1,
                    request);
            if (fentrust != null) {
                this.messageSender.publish(ChannelConstant.NEW_ENTRUSTS, String.valueOf(fentrust.getFid()));
                jsonObject.accumulate("resultCode", 0);
                jsonObject.accumulate("msg", "下单成功");
                setNoNeedPassword(request);
            } else {
                jsonObject.accumulate("code", -200);
                jsonObject.accumulate("msg", "网络错误，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("resultCode", -200);
            jsonObject.accumulate("msg", e.getMessage());
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/trade/cancelEntrust", produces = JsonEncode)
    public String cancelEntrust(HttpServletRequest request, @RequestParam(required = true) int id) throws Exception {
        int userId = GetCurrentUser(request).getFid();
        JSONObject jsonObject = new JSONObject();
        try (Mysql handle = new Mysql()) {
            BourseEngine engine = new BourseEngine(handle);
            String error = engine.cancelOrder(userId, id);
            if (error == null) {
                this.messageSender.publish(ChannelConstant.CANCEL_ENTRUST, String.valueOf(id));
                jsonObject.accumulate("code", 0);
                jsonObject.accumulate("msg", "取消成功");
            } else {
                jsonObject.accumulate("code", 1);
                jsonObject.accumulate("msg", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", e.getMessage());
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/trade/entrustLog", "/m/trade/entrustLog" }, produces = JsonEncode)
    public String entrustLog(HttpServletRequest request, @RequestParam(required = true) int id) throws Exception {

        JSONObject jsonObject = new JSONObject();

        if (GetCurrentUser(request) == null) {
            jsonObject.accumulate("result", false);
        }

        Fentrust fentrust = this.frontTradeService.findFentrustById(id);
        if (fentrust == null) {
            jsonObject.accumulate("result", false);
        } else {
            List<Fentrustlog> fentrustlogs = this.frontTradeService.findFentrustLogByFentrust(fentrust);

            jsonObject.accumulate("result", true);
            jsonObject.accumulate("title", "详细成交情况[委托ID:" + id + "]");

            Ftrademapping ftrademapping = this.ftradeMappingService
                    .findFtrademapping2(fentrust.getFtrademapping().getFid());
            Fvirtualcointype coin1 = ftrademapping.getFvirtualcointypeByFvirtualcointype1();
            Fvirtualcointype coin2 = ftrademapping.getFvirtualcointypeByFvirtualcointype2();

            StringBuffer content = new StringBuffer();
            if (request.getRequestURI().startsWith("/m/trade/entrustLog")) {
                content.append("<div> <table class=\"table\"> " + "<tr> " + "<th>成交价格</th> " + "<th>成交数量</th> "
                        + "<th>成交金额</th> " + "</tr>");

                if (fentrustlogs.size() == 0) {
                    content.append("<tr><td colspan='6' class='no-data-tips'><span>暂无委托</span></td></tr>");
                }
                for (Fentrustlog fentrustlog : fentrustlogs) {
                    content.append("<tr> <td>" + coin1.getfSymbol() + Utils.number2String(fentrustlog.getFprize())
                            + "</td>" + "<td>" + coin2.getfSymbol() + Utils.number2String(fentrustlog.getFcount())
                            + "</td>" + "<td>" + coin1.getfSymbol() + Utils.number2String(fentrustlog.getFamount())
                            + "</td>" + "</tr>");
                }

            } else {
                content.append("<div> <table class=\"table\"> " + "<tr> " + "<td>成交时间</td> " + "<td>委托类别</td> "
                        + "<td>成交价格</td> " + "<td>成交数量</td> " + "<td>成交金额</td> " + "</tr>");

                if (fentrustlogs.size() == 0) {
                    content.append("<tr><td colspan='6' class='no-data-tips'><span>暂无委托</span></td></tr>");
                }
                for (Fentrustlog fentrustlog : fentrustlogs) {
                    content.append("<tr> " + "<td class='gray'>"
                            + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fentrustlog.getFcreateTime())
                            + "</td> " + "<td class='"
                            + (fentrust.getFentrustType() == EntrustTypeEnum.BUY ? "text-success" : "text-danger")
                            + "'>" + (fentrust.getFentrustType() == EntrustTypeEnum.BUY ? "卖出" : "买入") + "</td>"
                            + "<td>" + coin1.getfSymbol() + Utils.number2String(fentrustlog.getFprize()) + "</td>"
                            + "<td>" + coin2.getfSymbol() + Utils.number2String(fentrustlog.getFcount()) + "</td>"
                            + "<td>" + coin1.getfSymbol() + Utils.number2String(fentrustlog.getFamount()) + "</td>"
                            + "</tr>");
                }

            }

            content.append("</table> </div>");
            jsonObject.accumulate("content", content.toString());
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/trade/cancelAllEntrust", produces = JsonEncode)
    public String cancelAllEntrust(HttpServletRequest request, @RequestParam(required = true) int id,
            @RequestParam(required = true, defaultValue = "0") int type) throws Exception {

        JSONObject jsonObject = new JSONObject();

        int userId = GetCurrentUser(request).getFid();
        String filter = "where fuser.fid=" + userId + " and ftrademapping.fid=" + id + " and fstatus in(1,2)";
        if (type == 1) {
            filter = filter + " and fentrustType=" + EntrustTypeEnum.BUY;
        } else if (type == 2) {
            filter = filter + " and fentrustType=" + EntrustTypeEnum.SELL;
        }
        List<Fentrust> fentrusts = this.frontTradeService.findFentrustByParam(0, 0, filter, false);
        try (Mysql handle = new Mysql()) {
            BourseEngine engine = new BourseEngine(handle);
            for (Fentrust fentrust : fentrusts) {
                String error = engine.cancelOrder(userId, fentrust.getFid());
                if (error != null) {
                    jsonObject.accumulate("code", 1);
                    jsonObject.accumulate("msg", error);
                    return jsonObject.toString();
                }
                this.messageSender.publish(ChannelConstant.CANCEL_ENTRUST, String.valueOf(fentrust.getFid()));
            }
        }
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "取消成功");
        return jsonObject.toString();
    }

    /*
     * @param type:0未成交前十条，1成交前10条
     * 
     * @param symbol:1币种
     */
    @ResponseBody
    @RequestMapping(value = "/kline/trade_history", produces = JsonEncode)
    public String trade_history(HttpServletRequest request, @RequestParam(required = true) int symbol)
            throws Exception {

        JSONObject jsonObject = new JSONObject();

        int userid = 0;
        if (GetCurrentUser(request) != null) {
            userid = GetCurrentUser(request).getFid();
        }

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping(symbol);
        if (ftrademapping == null || ftrademapping.getFstatus() == TrademappingStatusEnum.FOBBID) {
            return null;
        }
        {
            int entrust_status[] = null;
            entrust_status = new int[] { EntrustStatusEnum.Going, EntrustStatusEnum.PartDeal };

            List<Fentrust> fentrusts1 = null;
            fentrusts1 = this.frontTradeService.findFentrustHistory(userid, symbol, null, 0, 10, " fid desc ",
                    entrust_status);
            for (Fentrust fentrust : fentrusts1) {
                fentrust.setFtrademapping(null);
                fentrust.setFuser(null);
            }
            jsonObject.accumulate("fentrusts1", fentrusts1);
        }
        {
            int entrust_status[] = null;
            entrust_status = new int[] { EntrustStatusEnum.Cancel, EntrustStatusEnum.AllDeal };

            List<Fentrust> fentrusts2 = null;
            fentrusts2 = this.frontTradeService.findFentrustHistory(userid, symbol, null, 0, 10, " fid desc ",
                    entrust_status);
            for (Fentrust fentrust : fentrusts2) {
                fentrust.setFtrademapping(null);
                fentrust.setFuser(null);
            }
            jsonObject.accumulate("fentrusts2", fentrusts2);
        }
        jsonObject.accumulate("code", 0);
        return jsonObject.toString();
    }
}
