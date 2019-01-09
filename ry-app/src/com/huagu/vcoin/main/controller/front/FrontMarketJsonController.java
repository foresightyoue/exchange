package com.huagu.vcoin.main.controller.front;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.cerc.jdb.core.TDateTime;
import com.alibaba.fastjson.JSON;
import com.huagu.coa.common.cons.AppDB;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.front.data.Fullperiod;
import com.huagu.vcoin.main.controller.front.data.KlineData;
import com.huagu.vcoin.main.controller.front.data.MarketData;
import com.huagu.vcoin.main.controller.front.data.MarketItem;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;

import cn.cerc.jdb.cache.Buffer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class FrontMarketJsonController extends BaseController {
    private static final Logger log = Logger.getLogger(FrontMarketJsonController.class);

    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private FrontUserService frontUserService;

    // 交易中心
    @ResponseBody
    @RequestMapping(value = "/real/market", produces = { JsonEncode })
    public String marketRefresh(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int symbol,
            @RequestParam(required = false, defaultValue = "0") String uuid) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping(symbol);
        if (ftrademapping == null) {
            return null;
        }

        jsonObject.accumulate("p_new", this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping.getFid())));
        jsonObject.accumulate("high", this.redisUtil.get(RedisConstant.getHighestPrizeKey(ftrademapping.getFid())));
        jsonObject.accumulate("low", this.redisUtil.get(RedisConstant.getLowestPrizeKey(ftrademapping.getFid())));
        jsonObject.accumulate("vol", this.redisUtil.get(RedisConstant.getTotal(ftrademapping.getFid())));
        jsonObject.accumulate("buy1", this.redisUtil.get(RedisConstant.getHighestBuyPrizeKey(ftrademapping.getFid())));
        jsonObject.accumulate("sell1", this.redisUtil.get(RedisConstant.getLowestSellPrizeKey(ftrademapping.getFid())));

        JSONArray sellDepthList = JSONArray
                .fromObject(this.redisUtil.get(RedisConstant.getDepthKey(ftrademapping.getFid(), false, 20)));
        jsonObject.accumulate("sells", sellDepthList);

        JSONArray buyDepthList = JSONArray
                .fromObject(this.redisUtil.get(RedisConstant.getDepthKey(ftrademapping.getFid(), true, 20)));
        jsonObject.accumulate("buys", buyDepthList);

        JSONArray recentDealList = JSONArray
                .fromObject(this.redisUtil.get(RedisConstant.getSuccessKey(ftrademapping.getFid(), 20)));
        jsonObject.accumulate("trades", recentDealList);

        if (!"".equalsIgnoreCase(uuid) || null != uuid) {
            // 用户钱包
            Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(Integer.parseInt(uuid));
            if (null == fwallet) {
                jsonObject.accumulate("USDAvailable", "0");
                jsonObject.accumulate("CoinAvailable", "0");
                jsonObject.accumulate("coinList", new JSONObject());
                return jsonObject.toString();
            } else {
                jsonObject.accumulate("USDAvailable", fwallet.getFtotal());
            }
            Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                    .findVirtualWallet(Integer.parseInt(uuid));
            for (Map.Entry<Integer, Fvirtualwallet> entry : fvirtualwallets.entrySet()) {
                if (symbol == entry.getValue().getFvirtualcointype().getFid()) {
                    jsonObject.accumulate("CoinAvailable", entry.getValue().getFtotal());
                }
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.accumulate(entry.getValue().getFvirtualcointype().getFid() + "",
                        entry.getValue().getFvirtualcointype().getfShortName());
                jsonObject.accumulate("coinList", jsonObject1);
            }
        } else {
            jsonObject.accumulate("USDAvailable", "0");
            jsonObject.accumulate("CoinAvailable", "0");
        }

        return jsonObject.toString();
    }

    // K线中心 -- 右边板块
    @ResponseBody
    @RequestMapping(value = "/real/market2", produces = { JsonEncode })
    public String marketRefresh2(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        String redisKey = RedisConstant.getstart24Price(symbol);
        Buffer buff = new Buffer(redisKey);
        if (buff.isNull())
            return null;
        // System.out.println("read from buffer[RefreshMarket]");
        return buff.getString("data");
    }

    @ResponseBody
    @RequestMapping(value = "/m/real/marketforapp2", produces = { JsonEncode })
    public String marketRefreshForApp2(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int symbol) {
        int mapId = Integer.valueOf(request.getParameter("symbol"));
        int roseStatus = 0;
        String redisKey = RedisConstant.getstart24Price(symbol);
        Buffer buff = new Buffer(redisKey);
        if (buff.isNull())
            return null;
        com.alibaba.fastjson.JSONObject obj = JSON.parseObject(buff.getString("data"));
        try (Mysql mysql = new Mysql()) {
            MyQuery ds3 = new MyQuery(mysql);
            // if (state == 0) {
            ds3.add("select lastprice.fshou as fshou,firstprice.fkai as fkai");
            ds3.add("from");
            ds3.add("(select fshou from %s where ftrademapping=%d", AppDB.fperiod, mapId);
            ds3.add("and ftime between '%s' and '%s' order by fid desc limit 1) lastprice,",
                    TDateTime.Now().incDay(-1), TDateTime.Now());
            ds3.add("(select fkai from %s where ftrademapping=%d", AppDB.fperiod, mapId);
            ds3.add("and ftime between '%s' and '%s' order by fid limit 1) firstprice",
                    TDateTime.Now().incDay(-1),
                    TDateTime.Now());
            ds3.open();
            if (!ds3.eof()) {
                if (ds3.getDouble("fkai") != 0) {
                    double delta = ds3.getDouble("fshou") - ds3.getDouble("fkai");
                    if(delta > 0 ) {
                        roseStatus = 1;
                    }else{
                        roseStatus = -1;
                    }
                }
            }
        }
        Map<String, Integer> map = new HashedMap();
        map.put("roseStatus", roseStatus);
        obj.putAll(map);
        return obj.toJSONString();
    }
    @ResponseBody
    @RequestMapping(value = "/m/real/marketforapp", produces = { JsonEncode })
    public String marketRefreshForApp(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        double p_new = 0, rose = 0;
        if (!"0".equals(request.getParameter("marketPrice"))) {
            p_new = Double.parseDouble(request.getParameter("marketPrice"));
        }

        MarketData data = new MarketData();

        try (Mysql mysql = new Mysql()) {
            // 卖价
            MyQuery ds = new MyQuery(mysql);
            ds.add("select ft.fPrize,sum(ft.fleftCount) fCount from %s fm ", "ftrademapping");
            ds.add("inner join %s ft on ft.ftrademapping=fm.fid", "fentrust");
            ds.add("where fm.fid=%s and ft.fEntrustType=1 ", symbol);
            ds.add("and ft.fStatus in(1,2) ");
            ds.add("group by ft.fPrize ");
            ds.add("order by ft.fPrize asc");
            ds.setMaximum(10);
            ds.open();

            int i = 1;
            while (ds.fetch()) {
                MarketItem item = new MarketItem(i, ds.getDouble("fPrize"), ds.getDouble("fCount"));
                data.getSells().add(item);
                i++;
            }

            // 买价
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select ft.fPrize,sum(ft.fleftCount) fCount from %s fm ", "ftrademapping");
            ds1.add("inner join %s ft on ft.ftrademapping=fm.fid", "fentrust");
            ds1.add("where fm.fid=%s and ft.fEntrustType=0", symbol);
            ds1.add("and ft.fStatus in(1,2) ");
            ds1.add("group by ft.fPrize ");
            ds1.add("order by ft.fPrize desc");
            ds1.setMaximum(10);
            ds1.open();
            int j = 1;
            while (ds1.fetch()) {
                MarketItem item = new MarketItem(j, ds1.getDouble("fPrize"), ds1.getDouble("fCount"));
                data.getBuys().add(item);
                j++;
            }

            // 24h涨跌
            MyQuery ds3 = new MyQuery(mysql);
            ds3.add("select (lastprice.fshou-firstprice.fkai)/firstprice.fkai AS fchangerate ,lastprice.fshou AS fshou "
                    + "from (select fshou from fperiod "
                    + "where ftrademapping=%d and ftime>=date_sub(now(),interval 1 day) "
                    + "and ftime< now() order by fid desc limit 1) lastprice,"
                    + "(select fkai from fperiod where ftrademapping=%d and ftime>=date_sub(now(),interval 1 day) "
                    + "and ftime< now() order by fid limit 1) firstprice", symbol, symbol);
            ds3.open();
            if (!ds3.eof()) {
                rose = ds3.getDouble("fchangerate") * 100;
                p_new = ds3.getDouble("fshou");
            }
        }

        // try (Mysql mysql = new Mysql()) {
        // MyQuery ds4 = new MyQuery(mysql);
        // ds4.add("select lastprice.fshou as fshou,firstprice.fkai as fkai from
        // (select fshou from fperiod "
        // + "where ftrademapping =%d and ftime >= date_sub(now(),interval 1
        // day) "
        // + "and ftime< now() order by fid desc limit 1) lastprice,"
        // + " (select fkai from fperiod where ftrademapping = %d and ftime
        // >=date_sub(now(),interval 1 day ) "
        // + " and ftime < now() order by fid limit 1) firstprice", symbol,
        // symbol);
        // ds4.open();
        // if (!ds4.eof()) {
        // p_new = ds4.getDouble("fshou");
        //
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        data.setP_new(p_new);
        data.setRose(Math.round(rose * 10000) / 10000.0);
        return JSONObject.fromObject(data).toString();
    }

    @ResponseBody
    @RequestMapping("/kline/fullperiod")
    public String period(@RequestParam(required = true) int step, @RequestParam(required = true) int symbol)
            throws Exception {

        Map<Integer, Integer> items = new HashMap<>();
        items.put(60, 1);// 1分钟
        items.put(60 * 3, 3);// 3分钟
        items.put(60 * 5, 5);// 5分钟
        items.put(60 * 15, 15);// 15分钟
        items.put(60 * 30, 30);// 30分钟
        items.put(60 * 60, 60);// 1小时
        items.put(60 * 60 * 2, 60 * 2);// 2小时
        items.put(60 * 60 * 4, 60 * 4);// 4小时
        items.put(60 * 60 * 6, 60 * 6);// 6小时
        items.put(60 * 60 * 12, 60 * 12);// 12小时
        items.put(60 * 60 * 24, 60 * 24);// 1天
        items.put(60 * 60 * 24 * 3, 60 * 24 * 3);// 3天
        items.put(60 * 60 * 24 * 7, 60 * 24 * 7);// 7天

        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select pd.* from %s pd", "fperiod");
            ds.add("inner join ftrademapping mp on pd.ftrademapping=mp.fid");
            ds.add("inner join fvirtualcointype ty1 on mp.fvirtualcointype1=ty1.fId");
            ds.add("where mp.fid=%d", symbol);
            ds.add("and pd.ftime>='1970-01-01 00:00:00'");
            ds.add("and round((unix_timestamp(ftime)-unix_timestamp('1970-01-01 00:00:00')) / 60) %%%d =0",
                    items.get(step));
            ds.open();

            Fullperiod obj = new Fullperiod();
            while (ds.fetch()) {
                obj.addItem(ds.getDateTime("ftime").getData(), ds.getDouble("fkai"), ds.getDouble("fgao"),
                        ds.getDouble("fdi"), ds.getDouble("fshou"), ds.getDouble("fliang"));
            }
            log.info(obj.toString());
            return obj.toString();
        }

        // int key = 0;
        // switch (step) {
        // case 60:
        // key = 0;
        // break;
        // case 60 * 3:
        // key = 1;
        // break;
        // case 60 * 5:
        // key = 2;
        // break;
        // case 60 * 15:
        // key = 3;
        // break;
        // case 60 * 30:
        // key = 4;
        // break;
        // case 60 * 60:
        // key = 5;
        // break;
        // case 60 * 60 * 2:
        // key = 6;
        // break;
        // case 60 * 60 * 4:
        // key = 7;
        // break;
        // case 60 * 60 * 6:
        // key = 8;
        // break;
        // case 60 * 60 * 12:
        // key = 9;
        // break;
        // case 60 * 60 * 24:
        // key = 10;
        // break;
        // case 60 * 60 * 24 * 3:
        // key = 11;
        // break;
        // case 60 * 60 * 24 * 7:
        // key = 12;
        // break;
        // }
        // String ret = (String)
        // this.redisUtil.get(RedisConstant.getfullperiodKey(symbol, key));
        // return ret;
        //
    }

    @ResponseBody
    @RequestMapping("/kline/fulldepth")
    public String depth(@RequestParam(required = true) int symbol, @RequestParam(required = true) int step)
            throws Exception {
        KlineData obj = new KlineData();
        obj.getDepth().addBidsSampleData();
        obj.getDepth().addAsksSampleData();
        obj.getPeriod().setMarketFrom(symbol);
        obj.getPeriod().setCoinVol(symbol);
        obj.getPeriod().setType(step);
        JSONObject json = JSONObject.fromObject(obj);
        log.info(json.toString());
        return json.toString();

        // JSONObject jsonObject = new JSONObject();
        //
        // String redisBidJsonKey = RedisConstant.getaskBidJsonKey(symbol);
        // String depth = (String) this.redisUtil.get(redisBidJsonKey);
        // log.info("redisBidJsonKey " + redisBidJsonKey);
        //
        // jsonObject.accumulate("depth", depth);
        //
        // String redisJsonKey = RedisConstant.getperiodJsonKey(symbol, step /
        // 60);
        // log.info("redisJsonKey " + redisJsonKey);
        // JSONObject period =
        // JSONObject.fromObject(this.redisUtil.get(redisJsonKey));
        //
        // String redisKlinePeroidKey =
        // RedisConstant.getlatestKlinePeroidKey(symbol,
        // step / 60);
        // log.info("redisKlinePeroidKey " + redisKlinePeroidKey);
        // try {
        // Object data = this.redisUtil.get(redisKlinePeroidKey);
        // if (data == null) {
        // log.warn("fulldepth data 为 null");
        // } else {
        // period.accumulate("data", data);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // jsonObject.accumulate("period", period);
        // String ret = jsonObject.toString();
        // // String ret =
        // //
        // "{\"depth\":{\"bids\":[[\"10093.00000000\",\"0.2539\"],[\"10092.99000000\",\"1.2078\"],[\"10090.46000000\",\"6.6623\"],[\"10090.32000000\",\"22.8160\"],[\"10090.01000000\",\"9.6556\"],[\"10090.00000000\",\"7.8618\"],[\"10089.89000000\",\"5.5880\"],[\"10089.86000000\",\"6.6384\"],[\"10089.82000000\",\"0.4779\"],[\"10089.75000000\",\"3.7340\"]],\"asks\":[[\"10093.42000000\",\"67.6749\"],[\"10096.11000000\",\"9.8128\"],[\"10096.18000000\",\"4.2315\"],[\"10096.51000000\",\"0.8517\"],[\"10096.69000000\",\"2.1921\"],[\"10096.99000000\",\"12.0563\"],[\"10097.25000000\",\"5.7769\"],[\"10097.26000000\",\"0.8328\"],[\"10097.54000000\",\"8.0480\"],[\"10098.96000000\",\"2.7366\"]],\"date\":1520413532},\"period\":{\"marketFrom\":12,\"coinVol\":12,\"type\":900,\"data\":[]}}";
        // return ret;
    }

    @ResponseBody
    @RequestMapping("/kline/trade_json")
    public String trade_json(@RequestParam(required = true) int id) throws Exception {

        StringBuffer content = new StringBuffer();
        content.append("chart_1h = {symbol:\"BTC_CNY\",symbol_view:\"CNY/CNY\",ask:1.2,time_line:"
                + this.redisUtil.get(RedisConstant.getIndexJsonKey(id, 5)) + "};");

        content.append("chart_5m = {time_line:" + this.redisUtil.get(RedisConstant.getIndexJsonKey(id, 2)) + "};");
        content.append("chart_15m = {time_line:" + this.redisUtil.get(RedisConstant.getIndexJsonKey(id, 3)) + "};");
        content.append("chart_30m = {time_line:" + this.redisUtil.get(RedisConstant.getIndexJsonKey(id, 4)) + "};");
        content.append("chart_8h = {time_line:" + this.redisUtil.get(RedisConstant.getIndexJsonKey(id, 8)) + "};");
        content.append("chart_1d = {time_line:" + this.redisUtil.get(RedisConstant.getIndexJsonKey(id, 10)) + "};");

        return content.toString();
    }
}
