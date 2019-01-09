package com.huagu.vcoin.main.controller.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fentrust;
import com.huagu.vcoin.main.model.Flimittrade;
import com.huagu.vcoin.main.model.Ftradehistory;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSender;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.FrontOthersService;
import com.huagu.vcoin.main.service.front.FrontTradeService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.main.service.front.UtilsService;
import com.huagu.vcoin.util.Utils;

import net.sf.json.JSONObject;

/**
 * Created by dkyear
 *
 * 2017/12/4 0004.
 */
@Controller
public class AppIndexController extends BaseController {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private FrontOthersService frontOtherService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AdminService adminService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private MessageSender messageSender;

    @RequestMapping(value = { "/app/index" }, produces = { JsonEncode })
    @ResponseBody
    public String index() {
        JSONObject jsonObject = new JSONObject();
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0, " where fstatus=? order by fid asc ", false,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);
        for (Ftrademapping ftrademapping : ftrademappings) {
            JSONObject js = new JSONObject();
            double price = (Double) this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping.getFid()));

            js.accumulate("fid", ftrademapping.getFid());
            js.accumulate("name", ftrademapping.getFvirtualcointypeByFvirtualcointype2().getfShortName());
            js.accumulate("symbol", ftrademapping.getFvirtualcointypeByFvirtualcointype1().getfSymbol());
            js.accumulate("price", price);
            js.accumulate("total", this.redisUtil.get(RedisConstant.getTotal(ftrademapping.getFid())));
            js.accumulate("amt", this.redisUtil.get(RedisConstant.get24Total(ftrademapping.getFid())));
            js.accumulate("max",
                    Utils.getDouble(price * ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFtotalqty(), 2));
            js.accumulate("url", ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFurl());
            double s = (Double) this.redisUtil.get(RedisConstant.getstart24Price(ftrademapping.getFid()));
            double s7 = -8888d;

            List<Ftradehistory> ftradehistorys = (List<Ftradehistory>) constantMap.get("tradehistory");
            for (Ftradehistory ftradehistory : ftradehistorys) {
                if (ftradehistory.getFtrademapping().getFid().intValue() == ftrademapping.getFid().intValue()) {
                    s = ftradehistory.getFprice();
                    break;
                }
            }
            List<Ftradehistory> ftradehistoryss = (List<Ftradehistory>) constantMap.get("ftradehistory7D");
            for (Ftradehistory ftradehistory : ftradehistoryss) {
                if (ftradehistory.getFtrademapping().getFid().intValue() == ftrademapping.getFid().intValue()) {
                    s7 = ftradehistory.getFprice();
                    break;
                }
            }

            double last = 0d;
            double last7 = 0d;
            try {
                last = Utils.getDouble((price - s) / s * 100, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (s7 == -8888d) {
                    last7 = 0d;
                } else {
                    last7 = Utils.getDouble((price - s7) / s7 * 100, 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            js.accumulate("rose", last);
            js.accumulate("rose7", last7);
            js.accumulate("rate", this.constantMap.get("rate").toString());
            jsonObject.accumulate("coinlist", js);
        }

        return jsonObject.toString();
    }

    @RequestMapping(value = { "/app/logindex" }, produces = { JsonEncode })
    @ResponseBody
    public String logindex(HttpServletRequest request, @RequestParam(required = true, defaultValue = "0") String uuid) {
        JSONObject jsonObject = new JSONObject();
        JspPage modelAndView = new JspPage(request);

        // 用户钱包
        Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(Integer.parseInt(uuid));
        modelAndView.addObject("fwallet", fwallet);
        // 虚拟钱包
        Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService.findVirtualWallet(Integer.parseInt(uuid));
        modelAndView.addObject("fvirtualwallets", fvirtualwallets);
        // 估计总资产
        // CNY+冻结CNY+（币种+冻结币种）*最高买价
        double totalCapital = 0F;
        totalCapital += fwallet.getFtotal() + fwallet.getFfrozen();
        Map<Integer, Integer> tradeMappings = (Map) this.constantMap.get("tradeMappings");
        JSONObject jsonObject1 = new JSONObject();
        for (Map.Entry<Integer, Fvirtualwallet> entry : fvirtualwallets.entrySet()) {
            if (entry.getValue().getFvirtualcointype().getFtype() == CoinTypeEnum.FB_CNY_VALUE)
                continue;
            try {
                double latestDealPrize = (Double) this.redisUtil.get(RedisConstant
                        .getLatestDealPrizeKey(tradeMappings.get(entry.getValue().getFvirtualcointype().getFid())));
                totalCapital += (entry.getValue().getFfrozen() + entry.getValue().getFtotal()) * latestDealPrize;
                JSONObject js = new JSONObject();
                js.accumulate("name", entry.getValue().getFvirtualcointype().getfShortName());
                js.accumulate("total", entry.getValue().getFtotal());
                js.accumulate("frozen", entry.getValue().getFfrozen());
                js.accumulate("fid", entry.getValue().getFvirtualcointype().getFid_s());
                jsonObject.accumulate("list", js);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // modelAndView.addObject("totalCapitalTrade",
        // Utils.getDouble(totalCapital,
        // 2));
        jsonObject.accumulate("totalCapitalTrade", Utils.getDouble(totalCapital, 2));// 总资产
        jsonObject.accumulate("USDTotal", fwallet.getFtotal());// 美元资产
        jsonObject.accumulate("USDFrozen", fwallet.getFfrozen());// 冻结资产

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/app/trade/buyBtcSubmit", "/user/api/trade/buyBtcSubmit","/m/api/trade/buyBtcSubmit" }, produces = { JsonEncode })
    public String buyBtcSubmit(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int limited, @RequestParam(required = true) int symbol,
            @RequestParam(required = true) double tradeAmount, @RequestParam(required = true) double tradeCnyPrice,
            @RequestParam(required = false, defaultValue = "") String tradePwd,
            @RequestParam(required = true, defaultValue = "0") String uuid/*,
            @RequestParam(required = false, defaultValue = "") String sign*/) throws Exception {
        limited = 0;

        JSONObject jsonObject = new JSONObject();
        
        // 判断接口签名是否匹配
        // Map<String, Object> map = new HashMap<>();
        // map.put("limited", limited);
        // map.put("symbol", symbol);
        // map.put("tradeAmount", tradeAmount);
        // map.put("tradeCnyPrice", tradeCnyPrice);
        // map.put("tradePwd", tradePwd);
        // map.put("uuid", uuid);
        // String sort = KeyUtil.sort(map);
        // if (!sort.equals(sign)) {
        // jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
        // return jsonObject.toString();
        // }

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        if (ftrademapping == null || ftrademapping.getFstatus() == TrademappingStatusEnum.FOBBID) {
            jsonObject.accumulate("code", -100);
            jsonObject.accumulate("msg", "该币暂时不能交易");
            return jsonObject.toString();
        }

        // 限制法币为人民币和比特币
        if (ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_CNY_VALUE
                && ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_COIN_VALUE) {
            jsonObject.accumulate("code", -1);
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
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "最小交易数量：" + coin1.getfSymbol() + minBuyCount);
                return jsonObject.toString();
            }

            if (tradeAmount > maxBuyCount) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "最大交易数量：" + coin1.getfSymbol() + maxBuyCount);
                return jsonObject.toString();
            }

            if (tradeCnyPrice < minBuyPrice) {
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", "最小挂单价格：" + coin1.getfSymbol() + minBuyPrice);
                return jsonObject.toString();
            }

            double total = Utils.getDouble(tradeAmount * tradeCnyPrice, ftrademapping.getFcount1());
            if (total < minBuyAmount) {
                jsonObject.accumulate("code", -3);
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
                    jsonObject.accumulate("code", -500);
                    jsonObject.accumulate("msg", "挂单价格不能高于涨停价:" + upPrice + coin1.getFname());
                    return jsonObject.toString();
                }
                if (tradeCnyPrice < downPrice) {
                    jsonObject.accumulate("code", -600);
                    jsonObject.accumulate("msg", "挂单价格不能低于跌停价:" + downPrice + coin1.getFname());
                    return jsonObject.toString();
                }
            }

        } else {
            if (tradeCnyPrice < minBuyAmount) {
                jsonObject.accumulate("code", -33);
                jsonObject.accumulate("msg", "最小交易金额：" + minBuyAmount + coin1.getFname());
                return jsonObject.toString();
            }
        }

        Fuser fuser = this.frontUserService.findById(Integer.parseInt(uuid));

        if (Utils.openTrade(ftrademapping, Utils.getTimestamp()) == false/* && !fuser.isFistiger() */) {
            jsonObject.accumulate("code", -400);
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
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", coin1.getFname() + "余额不足");
            return jsonObject.toString();
        }

        if (isNeedTradePassword(request)) {
            if (tradePwd == null || tradePwd.trim().length() == 0) {
                jsonObject.accumulate("code", -50);
                jsonObject.accumulate("msg", "交易密码错误");
                return jsonObject.toString();
            }

            if (fuser.getFtradePassword() == null) {
                jsonObject.accumulate("code", -5);
                jsonObject.accumulate("msg",
                        "您还没有设置交易密码，请到安全中心设置<a class='text-danger' href='/user/security.html'>安全中心&gt;&gt;</a>");
                return jsonObject.toString();
            }
            if (!Utils.MD5(tradePwd, fuser.getSalt()).equals(fuser.getFtradePassword())) {
                jsonObject.accumulate("code", -2);
                jsonObject.accumulate("msg", "交易密码错误");
                return jsonObject.toString();
            }
        }

        boolean flag = false;
        Fentrust fentrust = null;
        try {

            if (ftrademapping.getFmaxtimes() > 0 && !fuser.isFistiger()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String now = sdf.format(new Date());
                String sql = "where (fstatus in(1,2,3) or (fstatus =4 and fsuccessAmount >0)) and fuser.fid="
                        + fuser.getFid() + " and ftrademapping.fid=" + ftrademapping.getFid()
                        // + " and date_format(fCreateTime,'%Y-%m-%d')='" + now + "'";
                        + " and fCreateTime >= curdate() and curdate() ='" + now + "'";
                int times = this.adminService.getAllCount("Fentrust", sql);
                if (times >= ftrademapping.getFmaxtimes()) {
                    jsonObject.accumulate("code", -600);
                    jsonObject.accumulate("msg", "每天最大有效挂单次数:" + ftrademapping.getFmaxtimes());
                    return jsonObject.toString();
                }
            }

            fuser.setFlastUpdateTime(Utils.getTimestamp());
            fentrust = this.frontTradeService.updateEntrustBuy(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1,
                    request);
            if (fentrust != null) {
                this.messageSender.publish(ChannelConstant.NEW_ENTRUSTS, String.valueOf(fentrust.getFid()));
                jsonObject.accumulate("code", 0);
                jsonObject.accumulate("msg", "下单成功");
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
    @RequestMapping(value = { "/app/trade/sellBtcSubmit", "/user/api/trade/sellBtcSubmit","/m/api/trade/sellBtcSubmit" }, produces = { JsonEncode })
    public String sellBtcSubmit(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int limited, // 是否按照市场价买入
            @RequestParam(required = true) int symbol, // 币种
            @RequestParam(required = true) double tradeAmount, // 数量
            @RequestParam(required = true) double tradeCnyPrice, // 单价
            @RequestParam(required = false, defaultValue = "") String tradePwd,
            @RequestParam(required = true, defaultValue = "0") String uuid/*,
            @RequestParam(required = false, defaultValue = "") String sign*/) throws Exception {
        limited = 0;

        JSONObject jsonObject = new JSONObject();
        
        // 判断接口签名是否匹配
        // Map<String, Object> map = new HashMap<>();
        // map.put("limited", limited);
        // map.put("symbol", symbol);
        // map.put("tradeAmount", tradeAmount);
        // map.put("tradeCnyPrice", tradeCnyPrice);
        // map.put("tradePwd", tradePwd);
        // map.put("uuid", uuid);
        // String sort = KeyUtil.sort(map);
        // if (!sort.equals(sign)) {
        // jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
        // return jsonObject.toString();
        // }

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        if (ftrademapping == null || ftrademapping.getFstatus() != TrademappingStatusEnum.ACTIVE) {
            jsonObject.accumulate("code", -100);
            jsonObject.accumulate("msg", "该币暂时不能交易");
            return jsonObject.toString();
        }

        if (ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_CNY_VALUE
                && ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_COIN_VALUE) {
            jsonObject.accumulate("code", -1);
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
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "最小交易数量：" + coin1.getfSymbol() + minBuyCount);
                return jsonObject.toString();
            }
            if (tradeAmount > maxSellCount) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "最大交易数量：" + coin1.getfSymbol() + maxSellCount);
                return jsonObject.toString();
            }

            if (tradeCnyPrice < minBuyPrice) {
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", "最小挂单价格：" + coin1.getfSymbol() + minBuyPrice);
                return jsonObject.toString();
            }
            double total = Utils.getDouble(tradeAmount * tradeCnyPrice, ftrademapping.getFcount1());
            if (total < minBuyAmount) {
                jsonObject.accumulate("code", -3);
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
                    jsonObject.accumulate("code", -500);
                    jsonObject.accumulate("msg", "挂单价格不能高于涨停价:" + upPrice + coin1.getFname());
                    return jsonObject.toString();
                }
                if (tradeCnyPrice < downPrice) {
                    jsonObject.accumulate("code", -600);
                    jsonObject.accumulate("msg", "挂单价格不能低于跌停价:" + downPrice + coin1.getFname());
                    return jsonObject.toString();
                }
            }

        } else {
            if (tradeAmount < minBuyCount) {
                jsonObject.accumulate("code", -33);
                jsonObject.accumulate("msg", "最小交易数量：" + minBuyCount + coin2.getFname());
                return jsonObject.toString();
            }
        }

        Fuser fuser = this.frontUserService.findById(Integer.parseInt(uuid));

        // 是否开放交易
        if (Utils.openTrade(ftrademapping, Utils.getTimestamp()) == false/* && !fuser.isFistiger() */) {
            jsonObject.accumulate("code", -400);
            jsonObject.accumulate("msg", "现在不是交易时间");
            return jsonObject.toString();
        }

        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), coin2.getFid());
        if (fvirtualwallet == null) {
            jsonObject.accumulate("code", -200);
            jsonObject.accumulate("msg", "系统错误，请联系管理员");
            return jsonObject.toString();
        }
        if (fvirtualwallet.getFtotal() < tradeAmount) {
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", coin2.getFname() + "余额不足");
            return jsonObject.toString();
        }

        if (!fuser.isFistiger()) {
            if (ftrademapping.isFislimit()) {
                if (fvirtualwallet.getFcanSellQty() < tradeAmount) {
                    jsonObject.accumulate("code", -4);
                    jsonObject.accumulate("msg", "您今天最多还可以卖出:" + fvirtualwallet.getFcanSellQty());
                    return jsonObject.toString();
                }
            }
        }

        if (isNeedTradePassword(request)) {
            if (tradePwd == null || tradePwd.trim().length() == 0) {
                jsonObject.accumulate("code", -50);
                jsonObject.accumulate("msg", "交易密码错误");
                return jsonObject.toString();
            }

            if (fuser.getFtradePassword() == null) {
                jsonObject.accumulate("code", -5);
                jsonObject.accumulate("msg",
                        "您还没有设置交易密码，请到安全中心设置<a class='text-danger' href='/user/security.html'>安全中心&gt;&gt;</a>");
                return jsonObject.toString();
            }
            if (!Utils.MD5(tradePwd, fuser.getSalt()).equals(fuser.getFtradePassword())) {
                jsonObject.accumulate("code", -2);
                jsonObject.accumulate("msg", "交易密码错误");
                return jsonObject.toString();
            }
        }

        Fentrust fentrust = null;
        try {
            fuser.setFlastUpdateTime(Utils.getTimestamp());
            fentrust = this.frontTradeService.updateEntrustSell(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1,
                    request);
            if (fentrust != null) {
                this.messageSender.publish(ChannelConstant.NEW_ENTRUSTS, String.valueOf(fentrust.getFid()));
                jsonObject.accumulate("code", 0);
                jsonObject.accumulate("msg", "下单成功");
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

}
