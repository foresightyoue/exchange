package com.huagu.vcoin.main.controller.front;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.EntrustStatusEnum;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fentrust;
import com.huagu.vcoin.main.model.Flimittrade;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.EntrustService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.FrontTradeService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.main.service.front.UtilsService;
import com.huagu.vcoin.util.PaginUtil;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.TDate;
import cn.cerc.jdb.core.TDateTime;
import net.sf.json.JSONObject;

@Controller
public class FrontTradeController extends BaseController {
    private static Logger log = Logger.getLogger(FrontIndexController.class);
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private EntrustService entrustService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = { "m/trade/coin", "/trade/coin" })
    public ModelAndView coin(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int coinType,
            @RequestParam(required = false, defaultValue = "0") int tradeType) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // 环境安全检测
//        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
//        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
//            if (!SecurityEnvironment.check(modelAndView)) {
//                return modelAndView;
//            }
//        }
        request.getSession().setAttribute("menuFlag", "trade");
        int userid = 0;
        Fuser fuser = null;
        boolean isTelephoneBind = false;
        if (GetCurrentUser(request) != null) {
            fuser = this.userService.findById(GetCurrentUser(request).getFid());
            userid = fuser.getFid();
            isTelephoneBind = fuser.isFisTelephoneBind();
        }

        tradeType = tradeType < 0 ? 0 : tradeType;
        tradeType = tradeType > 1 ? 1 : tradeType;

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(coinType);
        List<Ftrademapping> ftrademappings = null;
        if (ftrademapping == null || ftrademapping.getFstatus() == TrademappingStatusEnum.FOBBID) {
            ftrademappings = this.utilsService.list(0, 1, " where fstatus=? order by fid asc ", true,
                    Ftrademapping.class, TrademappingStatusEnum.ACTIVE);
            if (ftrademappings.size() > 0) {
                modelAndView
                        .setRedirectJsp("/trade/coin.html?coinType=" + ftrademappings.get(0).getFid() + "&tradeType=0");
                return modelAndView;
            } else {
                modelAndView.setRedirectJsp("/index.html");
                return modelAndView;
            }
        }

        // 限制法币为人民币和比特币
        if (ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_CNY_VALUE
                && ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFtype() != CoinTypeEnum.FB_COIN_VALUE) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        ftrademappings = this.ftradeMappingService
                .findActiveTradeMappingByFB(ftrademapping.getFvirtualcointypeByFvirtualcointype1());
        Fvirtualcointype coin1 = ftrademapping.getFvirtualcointypeByFvirtualcointype1();
        Fvirtualcointype coin2 = ftrademapping.getFvirtualcointypeByFvirtualcointype2();

        Flimittrade limittrade = this.isLimitTrade(ftrademapping.getFid());
        boolean isLimittrade = false;
        double upPrice = 0d;
        double downPrice = 0d;
        if (limittrade != null) {
            isLimittrade = true;
            upPrice = Utils.getDouble(limittrade.getFupprice() + limittrade.getFupprice() * limittrade.getFpercent(),
                    ftrademapping.getFcount1());
            downPrice = Utils.getDouble(
                    limittrade.getFdownprice() - limittrade.getFdownprice() * limittrade.getFpercent(),
                    ftrademapping.getFcount1());
            if (downPrice < 0)
                downPrice = 0;
        }
        modelAndView.addObject("isLimittrade", isLimittrade);
        modelAndView.addObject("upPrice", upPrice);
        modelAndView.addObject("downPrice", downPrice);

        boolean isTradePassword = false;
        if (userid != 0 && fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0) {
            isTradePassword = true;
        }

        List<Fentrust> fentrusts = this.frontTradeService.findFentrustHistory(userid, coinType, null, 0, 10,
                " fid desc ", new int[] { EntrustStatusEnum.Going, EntrustStatusEnum.PartDeal });

        try (Mysql mysql = new Mysql()) {
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select ty.fcurrentCNY as fny");
            ds1.add("from %s ty", AppDB.fvirtualcointype);
            ds1.add("inner join %s mp on mp.fvirtualcointype1=ty.fId", AppDB.Ftrademapping);
            ds1.add("where ty.fId=%d", coinType);
            ds1.open();
            if (!ds1.eof()) {
                modelAndView.addObject("fny", ds1.getDouble("fny"));
            }

            MyQuery ds3 = new MyQuery(mysql);
            ds3.add("select min(pd.fdi) as fdi,max(pd.fgao) as fgao,sum(fliang) as fliang");
            ds3.add("from %s pd", AppDB.fperiod);
            ds3.add("inner join %s mp on pd.ftrademapping=mp.fid", AppDB.Ftrademapping);
            ds3.add("where pd.ftrademapping=%d and pd.ftime>='%s' and pd.fdi>0 and pd.frequency = 1", coinType,
                    TDateTime.Now().incDay(-1));
            ds3.open();
            if (!ds3.eof()) {
                modelAndView.addObject("total", ds3.getDouble("fliang"));
                modelAndView.addObject("high", ds3.getDouble("fgao"));
                modelAndView.addObject("low", ds3.getDouble("fdi"));
            }

            MyQuery ds4 = new MyQuery(mysql);
            ds4.add("select fshou from %s", AppDB.fperiod);
            ds4.add("where ftrademapping=%d and ftime between '%s' and '%s'", coinType, TDateTime.Now().incDay(-1),
                    TDateTime.Now());
            ds4.add("order by fid desc");
            ds4.setMaximum(1);
            ds4.open();
            if (!ds4.eof()) {
                modelAndView.addObject("price", ds4.getDouble("fshou"));
            }

            MyQuery ds5 = new MyQuery(mysql);
            ds5.add("select lastprice.fshou,firstprice.fkai,(lastprice.fshou-firstprice.fkai)/firstprice.fkai as fchangerate");
            ds5.add("from ");
            ds5.add("(select fshou from %s", AppDB.fperiod);
            ds5.add("where ftrademapping=%d and ftime between '%s' and '%s'", coinType, TDateTime.Now().incDay(-1),
                    TDateTime.Now());
            ds5.add("order by fid desc limit 1) lastprice,");
            ds5.add("(select fkai from %s", AppDB.fperiod);
            ds5.add("where ftrademapping=%d and ftime between '%s' and '%s'", coinType, TDateTime.Now().incDay(-1),
                    TDateTime.Now());
            ds5.add("order by fid limit 1) firstprice");
            ds5.open();
            if (!ds5.eof()) {
                modelAndView.addObject("fchangerate", ds5.getDouble("fchangerate"));
            }

            MyQuery getB = new MyQuery(mysql);
            getB.add("select mp.ftradedesc as ftradedesc from %s mp", AppDB.Ftrademapping);
            getB.add("inner join %s ct on mp.fvirtualcointype2=ct.fId", AppDB.fvirtualcointype);
            getB.add("where ct.fId=%s group by ct.fId", coinType);
            getB.open();
            if (!getB.eof()) {
                String[] strs = getB.getString("ftradedesc").split("/");
                String B1 = strs[0];
                String B2 = strs[1];

                MyQuery getB1 = new MyQuery(mysql);
                getB1.add("select ct.fisrecharge from %s mp", AppDB.Ftrademapping);
                getB1.add("inner join %s ct on mp.fvirtualcointype2=ct.fId", AppDB.fvirtualcointype);
                getB1.add("where ct.fShortName='%s' group by ct.fShortName", B1);
                getB1.open();
                if (!getB1.eof()) {
                    modelAndView.addObject("fisrecharge", getB1.getInt("fisrecharge"));
                }

                MyQuery getB2 = new MyQuery(mysql);
                getB2.add("select ct.fisrecharge as fisrecharge1 from %s mp", AppDB.Ftrademapping);
                getB2.add("inner join %s ct on mp.fvirtualcointype2=ct.fId", AppDB.fvirtualcointype);
                getB2.add("where ct.fShortName='%s' group by ct.fShortName", B2);
                getB2.open();
                if (!getB2.eof()) {
                    modelAndView.addObject("fisrecharge1", getB2.getInt("fisrecharge1"));
                }
            }

            // 查询法币种类
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s", AppDB.fvirtualcointype);
            ds.add("where fId in(select fvirtualcointype1 from %s", AppDB.Ftrademapping);
            ds.add("where fstatus=1 group by fvirtualcointype1)");
            ds.open();

            List<Map<String, String>> coins = new ArrayList<>();
            while (ds.fetch()) {
                Map<String, String> coinMap = new HashMap<>();
                coinMap.put("fShortName", ds.getString("fShortName"));
                coins.add(coinMap);
            }
            modelAndView.add("coins", coins);

            MyQuery dsAmount = new MyQuery(mysql);
            dsAmount.add("select sum(fm.fAmount) as ffAmount");
            dsAmount.add("from (");
            dsAmount.add("select fAmount,fCreateTime from %s fe", AppDB.fentrust);
            dsAmount.add("inner join %s fu on fe.FUs_fId=fu.fid", AppDB.Fuser);
            dsAmount.add("where fe.FUs_fId=%d", userid);
            dsAmount.add("and fe.fCreateTime>='%s') fm", TDate.Today());
            dsAmount.open();
            modelAndView.addObject("ffAmount", dsAmount.getDouble("ffAmount"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("------------");
        }

        Map<Fvirtualcointype, List<Ftrademapping>> fMap = new TreeMap<Fvirtualcointype, List<Ftrademapping>>(
                new Comparator<Fvirtualcointype>() {

                    @Override
                    public int compare(Fvirtualcointype o1, Fvirtualcointype o2) {
                        Integer type1 = Integer.parseInt(o1.getFtype() + "");
                        Integer type2 = Integer.parseInt(o2.getFtype() + "");
                        if (o1.getFtype() == o2.getFtype()) {
                            return o1.getFid().compareTo(o2.getFid());
                        } else {
                            return type1.compareTo(type2);
                        }
                    }
                });
        List<Fvirtualcointype> fbs = this.utilsService.list(0, 0, " where ftype=? or ftype=? order by fid asc ", false,
                Fvirtualcointype.class, CoinTypeEnum.FB_CNY_VALUE, CoinTypeEnum.FB_COIN_VALUE);
        log.info("fbs.size: " + fbs.size());
        for (Fvirtualcointype coinItem : fbs) {
            List<Ftrademapping> ftrademapping1 = this.ftradeMappingService.findActiveTradeMappingByFB(coinItem);
            if (ftrademapping1.size() > 0) {
                fMap.put(coinItem, ftrademapping1);
            } else {
                log.info("coin: " + coinItem.getFname());
            }
        }
        modelAndView.add("fMap", fMap);

        modelAndView.addObject("needTradePasswd", super.isNeedTradePassword(request));
        modelAndView.addObject("fentrusts", fentrusts);
        modelAndView.addObject("fuser", fuser);
        modelAndView.addObject("userid", userid);
        modelAndView.addObject("isTradePassword", isTradePassword);
        modelAndView.addObject("isTelephoneBind", isTelephoneBind);
        modelAndView.addObject("recommendPrizesell", this.redisUtil.get(RedisConstant.getHighestBuyPrizeKey(coinType)));
        modelAndView.addObject("recommendPrizebuy", this.redisUtil.get(RedisConstant.getLowestSellPrizeKey(coinType)));
        modelAndView.addObject("coin1", coin1);
        modelAndView.addObject("coin2", coin2);
        modelAndView.addObject("ftrademappings", ftrademappings);
        modelAndView.addObject("ftrademapping", ftrademapping);
        modelAndView.addObject("coinType", coinType);
        modelAndView.addObject("tradeType", tradeType);
        modelAndView.setJspFile("front/trade/trade_coin");
        return modelAndView;
    }
    
    @ResponseBody
    @RequestMapping(value = { "m/api/coin", "/user/api/coin" }, produces = JsonEncode)
    public String apicoin(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int coinType,
            @RequestParam(required = false, defaultValue = "0") int tradeType) throws Exception {
        JSONObject jsonObject = new JSONObject();
        request.getSession().setAttribute("menuFlag", "trade");
        int userid = 881007898;
        Fuser fuser = null;
        boolean isTelephoneBind = false;
        if (GetCurrentUser(request) == null) {
            fuser = this.userService.findById(881007898);
            userid = fuser.getFid();
            isTelephoneBind = fuser.isFisTelephoneBind();
        }

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(coinType);
        List<Ftrademapping> ftrademappings = null;
        if (ftrademapping == null || ftrademapping.getFstatus() == TrademappingStatusEnum.FOBBID) {
            ftrademappings = this.utilsService.list(0, 1, " where fstatus=? order by fid asc ", true,
                    Ftrademapping.class, TrademappingStatusEnum.ACTIVE);
        }
        ftrademappings = this.ftradeMappingService
                .findActiveTradeMappingByFB(ftrademapping.getFvirtualcointypeByFvirtualcointype1());
        Fvirtualcointype coin1 = ftrademapping.getFvirtualcointypeByFvirtualcointype1();
        Fvirtualcointype coin2 = ftrademapping.getFvirtualcointypeByFvirtualcointype2();

        Flimittrade limittrade = this.isLimitTrade(ftrademapping.getFid());
        boolean isLimittrade = false;
        double upPrice = 0d;
        double downPrice = 0d;
        if (limittrade != null) {
            isLimittrade = true;
            upPrice = Utils.getDouble(limittrade.getFupprice() + limittrade.getFupprice() * limittrade.getFpercent(),
                    ftrademapping.getFcount1());
            downPrice = Utils.getDouble(
                    limittrade.getFdownprice() - limittrade.getFdownprice() * limittrade.getFpercent(),
                    ftrademapping.getFcount1());
            if (downPrice < 0)
                downPrice = 0;
        }
        jsonObject.accumulate("isLimittrade", isLimittrade);
        jsonObject.accumulate("upPrice", upPrice);
        jsonObject.accumulate("downPrice", downPrice);

        boolean isTradePassword = false;
        if (userid != 0 && fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0) {
            isTradePassword = true;
        }

        List<Fentrust> fentrusts = this.frontTradeService.findFentrustHistory(userid, coinType, null, 0, 10,
                " fid desc ", new int[] { EntrustStatusEnum.Going, EntrustStatusEnum.PartDeal });

        try (Mysql mysql = new Mysql()) {
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select ty.fcurrentCNY as fny");
            ds1.add("from %s ty", AppDB.fvirtualcointype);
            ds1.add("inner join %s mp on mp.fvirtualcointype1=ty.fId", AppDB.Ftrademapping);
            ds1.add("where ty.fId=%d", coinType);
            ds1.open();
            if (!ds1.eof()) {
                jsonObject.accumulate("fny", ds1.getDouble("fny"));
            }

            MyQuery ds3 = new MyQuery(mysql);
            ds3.add("select min(pd.fdi) as fdi,max(pd.fgao) as fgao,sum(fliang) as fliang");
            ds3.add("from %s pd", AppDB.fperiod);
            ds3.add("inner join %s mp on pd.ftrademapping=mp.fid", AppDB.Ftrademapping);
            ds3.add("where pd.ftrademapping=%d and pd.ftime>='%s' and pd.fdi>0 and pd.frequency = 1", coinType,
                    TDateTime.Now().incDay(-1));
            ds3.open();
            if (!ds3.eof()) {
                jsonObject.accumulate("total", ds3.getDouble("fliang"));
                jsonObject.accumulate("high", ds3.getDouble("fgao"));
                jsonObject.accumulate("low", ds3.getDouble("fdi"));
            }

            MyQuery ds4 = new MyQuery(mysql);
            ds4.add("select fshou from %s", AppDB.fperiod);
            ds4.add("where ftrademapping=%d and ftime between '%s' and '%s'", coinType, TDateTime.Now().incDay(-1),
                    TDateTime.Now());
            ds4.add("order by fid desc");
            ds4.setMaximum(1);
            ds4.open();
            if (!ds4.eof()) {
                jsonObject.accumulate("price", ds4.getDouble("fshou"));
            }

            MyQuery ds5 = new MyQuery(mysql);
            ds5.add("select lastprice.fshou,firstprice.fkai,(lastprice.fshou-firstprice.fkai)/firstprice.fkai as fchangerate");
            ds5.add("from ");
            ds5.add("(select fshou from %s", AppDB.fperiod);
            ds5.add("where ftrademapping=%d and ftime between '%s' and '%s'", coinType, TDateTime.Now().incDay(-1),
                    TDateTime.Now());
            ds5.add("order by fid desc limit 1) lastprice,");
            ds5.add("(select fkai from %s", AppDB.fperiod);
            ds5.add("where ftrademapping=%d and ftime between '%s' and '%s'", coinType, TDateTime.Now().incDay(-1),
                    TDateTime.Now());
            ds5.add("order by fid limit 1) firstprice");
            ds5.open();
            if (!ds5.eof()) {
                jsonObject.accumulate("fchangerate", ds5.getDouble("fchangerate"));
            }

            MyQuery getB = new MyQuery(mysql);
            getB.add("select mp.ftradedesc as ftradedesc from %s mp", AppDB.Ftrademapping);
            getB.add("inner join %s ct on mp.fvirtualcointype2=ct.fId", AppDB.fvirtualcointype);
            getB.add("where ct.fId=%s group by ct.fId", coinType);
            getB.open();
            if (!getB.eof()) {
                String[] strs = getB.getString("ftradedesc").split("/");
                String B1 = strs[0];
                String B2 = strs[1];

                MyQuery getB1 = new MyQuery(mysql);
                getB1.add("select ct.fisrecharge from %s mp", AppDB.Ftrademapping);
                getB1.add("inner join %s ct on mp.fvirtualcointype2=ct.fId", AppDB.fvirtualcointype);
                getB1.add("where ct.fShortName='%s' group by ct.fShortName", B1);
                getB1.open();
                if (!getB1.eof()) {
                    jsonObject.accumulate("fisrecharge", getB1.getInt("fisrecharge"));
                }

                MyQuery getB2 = new MyQuery(mysql);
                getB2.add("select ct.fisrecharge as fisrecharge1 from %s mp", AppDB.Ftrademapping);
                getB2.add("inner join %s ct on mp.fvirtualcointype2=ct.fId", AppDB.fvirtualcointype);
                getB2.add("where ct.fShortName='%s' group by ct.fShortName", B2);
                getB2.open();
                if (!getB2.eof()) {
                    jsonObject.accumulate("fisrecharge1", getB2.getInt("fisrecharge1"));
                }
            }

            // 查询法币种类
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s", AppDB.fvirtualcointype);
            ds.add("where fId in(select fvirtualcointype1 from %s", AppDB.Ftrademapping);
            ds.add("where fstatus=1 group by fvirtualcointype1)");
            ds.open();

            List<Map<String, String>> coins = new ArrayList<>();
            while (ds.fetch()) {
                Map<String, String> coinMap = new HashMap<>();
                coinMap.put("fShortName", ds.getString("fShortName"));
                coins.add(coinMap);
            }
            jsonObject.accumulate("coins", coins);

            MyQuery dsAmount = new MyQuery(mysql);
            dsAmount.add("select sum(fm.fAmount) as ffAmount");
            dsAmount.add("from (");
            dsAmount.add("select fAmount,fCreateTime from %s fe", AppDB.fentrust);
            dsAmount.add("inner join %s fu on fe.FUs_fId=fu.fid", AppDB.Fuser);
            dsAmount.add("where fe.FUs_fId=%d", userid);
            dsAmount.add("and fe.fCreateTime>='%s') fm", TDate.Today());
            dsAmount.open();
            jsonObject.accumulate("ffAmount", dsAmount.getDouble("ffAmount"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("------------");
        }

        Map<Fvirtualcointype, List<Ftrademapping>> fMap = new TreeMap<Fvirtualcointype, List<Ftrademapping>>(
                new Comparator<Fvirtualcointype>() {

                    @Override
                    public int compare(Fvirtualcointype o1, Fvirtualcointype o2) {
                        Integer type1 = Integer.parseInt(o1.getFtype() + "");
                        Integer type2 = Integer.parseInt(o2.getFtype() + "");
                        if (o1.getFtype() == o2.getFtype()) {
                            return o1.getFid().compareTo(o2.getFid());
                        } else {
                            return type1.compareTo(type2);
                        }
                    }
                });
        List<Fvirtualcointype> fbs = this.utilsService.list(0, 0, " where ftype=? or ftype=? order by fid asc ", false,
                Fvirtualcointype.class, CoinTypeEnum.FB_CNY_VALUE, CoinTypeEnum.FB_COIN_VALUE);
        log.info("fbs.size: " + fbs.size());
        for (Fvirtualcointype coinItem : fbs) {
            List<Ftrademapping> ftrademapping1 = this.ftradeMappingService.findActiveTradeMappingByFB(coinItem);
            if (ftrademapping1.size() > 0) {
                fMap.put(coinItem, ftrademapping1);
            } else {
                log.info("coin: " + coinItem.getFname());
            }
        }
        Map<String, Object> map=null;
        List<Map<String, Object>> fdata=new ArrayList<>();
        for (List<Ftrademapping> tm :fMap.values()) {
            for (int i = 0; i < tm.size(); i++) {
                Ftrademapping ftp = tm.get(i);
                String shortName1 = ftp.getFvirtualcointypeByFvirtualcointype1().getfShortName();
                String shortName2 = ftp.getFvirtualcointypeByFvirtualcointype2().getfShortName();
                Integer fid = ftp.getFid();
                map=new HashMap<>();
                map.put("shortName1", shortName1);
                map.put("shortName2", shortName2);
                map.put("fid", fid);
                fdata.add(map);
            }
        }
        
        jsonObject.accumulate("fMap", fdata);

        jsonObject.accumulate("needTradePasswd", super.isNeedTradePassword(request));
        jsonObject.accumulate("fentrusts", fentrusts);
        jsonObject.accumulate("fhasRealValidate", fuser.getFhasRealValidate());
        jsonObject.accumulate("fhasImgValidate", fuser.isFhasImgValidate());
        jsonObject.accumulate("fhasBankValidate", fuser.isFhasBankValidate());
        jsonObject.accumulate("userid", userid);
        jsonObject.accumulate("isTradePassword", isTradePassword);
        jsonObject.accumulate("isTelephoneBind", isTelephoneBind);
        jsonObject.accumulate("recommendPrizesell", this.redisUtil.get(RedisConstant.getHighestBuyPrizeKey(coinType)));
        jsonObject.accumulate("recommendPrizebuy", this.redisUtil.get(RedisConstant.getLowestSellPrizeKey(coinType)));
        jsonObject.accumulate("fShortName1", coin1.getfShortName());
        jsonObject.accumulate("fShortName2", coin2.getfShortName());
//        jsonObject.accumulate("ftrademappings", ftrademappings);
        jsonObject.accumulate("tfid", ftrademapping.getFid());
        jsonObject.accumulate("tminBuyCount", ftrademapping.getFminBuyCount());
        jsonObject.accumulate("coinType", coinType);
        jsonObject.accumulate("tradeType", tradeType);
        return jsonObject.toString();
    }

    /*
     * https://www.okcoin.com/trade/entrust.do?symbol=1
     */
    @RequestMapping(value = { "/trade/entrust", "/m/trade/trade_entrust1" })
    public ModelAndView entrust(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int type,
            @RequestParam(required = false, defaultValue = "0") int symbol,
            @RequestParam(required = false, defaultValue = "0") int status,
            @RequestParam(required = false, defaultValue = "0") int fstatus,
            @RequestParam(required = false, defaultValue = "1") int currentPage) throws Exception {
        String uri = request.getRequestURI();
        int maxNum = maxResults;
        boolean isApp = false;
        if (uri.startsWith("/m/")) {
            isApp = true;
        }
        if (isApp) {
            maxNum = appMaxResults;
        }
        JspPage modelAndView = new JspPage(request);
        Map<Integer, Double> afPrizeMap = new HashMap<>();
        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        if (ftrademapping != null) {
            modelAndView.addObject("ftrademapping", ftrademapping);
        } else {
            symbol = 0;
        }

        String filter = "where fuser.fid=" + GetCurrentUser(request).getFid();
        if (status == 0) {
            // 正在委托
            if (fstatus == 1) {
                filter = filter + " and fstatus ='1'";
            }
            if (fstatus == 2) {
                filter = filter + " and fstatus ='2'";
            }
            if (fstatus == 0) {
                filter = filter + " and fstatus in (1,2,3,4)";
            }
        } else {
            // 委托完成
            if (fstatus == 3) {
                filter = filter + " and fstatus ='3'";
            }
            if (fstatus == 4) {
                filter = filter + " and fstatus ='4'";
            }
            if (fstatus == 0) {
                filter = filter + " and fstatus in (3,4)";
            }

            try (Mysql mysql = new Mysql()) {
                MyQuery ds = new MyQuery(mysql);
                ds.add("select  sum(l.famount)/sum(l.fcount) afPrize,l.fen_fid,f.* ");
                ds.add("from  %s f left join %s l on ", "fentrust", "fentrustlog");
                ds.add("if ( f.fEntrustType = 0, f.fid = l.fdeal_fid, f.fid = l.FEn_fId )");
                ds.add("where f.FUs_fId = %d and f.fstatus in(3,4) ", GetCurrentUser(request).getFid());
                if (symbol != 0) {
                    ds.add("and f.ftrademapping= %d", symbol);
                }
                ds.add("group by f.fid");
                ds.add("order by f.fid desc");
                ds.setMaximum(maxNum);
                ds.setOffset((currentPage - 1) * maxNum);
                ds.open();
                while (ds.fetch()) {
                    afPrizeMap.put(ds.getInt("fId"), ds.getDouble("afPrize"));
                }
            }

        }

        if (symbol != 0) {
            filter = filter + " and ftrademapping.fid=" + symbol;
        }
        filter = filter + " order by fCreateTime desc";
        List<Fentrust> fentrusts = this.entrustService.list((currentPage - 1) * maxNum, maxNum, filter, true);

        //
        // this.frontTradeService.findFentrustHistory(
        // GetSession(request).getFid(),
        // ftrademapping.getFid(),
        // null, PaginUtil.firstResult(currentPage, maxResults),
        // maxResults,
        // "id desc ", fstatus) ;

        int total = this.adminService.getAllCount("Fentrust", filter);
        int totalPage = total / maxResults + (total % maxResults == 0 ? 0 : 1);
        String pagin = PaginUtil.generatePagin(total / maxNum + (total % maxNum == 0 ? 0 : 1), currentPage,
                "/trade/entrust.html?symbol=" + symbol + "&status=" + status + "&");

        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalPage", totalPage);
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("fentrusts", fentrusts);
        modelAndView.addObject("afPrizeMap", afPrizeMap);
        modelAndView.addObject("total", total);
        modelAndView.addObject("status", status);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("maxNum", maxNum);
        modelAndView.addObject("fstatus", fstatus);
        if (!isApp) {
            modelAndView.setJspFile("front/trade/trade_entrust");
        } else {
            modelAndView.addObject("type", type);
            modelAndView.setJspFile("front/trade/trade_entrust1");
        }
        return modelAndView;
    }

    /*
     * https://www.okcoin.com/trade/entrust.do?symbol=1
     */
    @RequestMapping(value = { "/trade/entrust0", "/m/trade/entrust0" })
    public ModelAndView entrust0(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int coinType,
            @RequestParam(required = false, defaultValue = "0") int symbol,
            @RequestParam(required = false, defaultValue = "0") int status,
            @RequestParam(required = false, defaultValue = "1") int currentPage) throws Exception {

        JspPage modelAndView = new JspPage(request);
        Map<Integer, Double> afPrizeMap = new HashMap<>();
        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        if (ftrademapping != null) {
            modelAndView.addObject("ftrademapping", ftrademapping);
        } else {
            symbol = 0;
        }

        String filter = "where fuser.fid=" + GetCurrentUser(request).getFid();
        if (status == 0) {
            // 正在委托
            filter = filter + " and fstatus in (1,2)";
        } else {
            // 委托完成
            filter = filter + " and fstatus in (3,4)";
            try (Mysql mysql = new Mysql()) {
                MyQuery ds = new MyQuery(mysql);
                ds.add("select  sum(l.famount)/sum(l.fcount) afPrize,l.fen_fid,f.* ");
                ds.add("from  %s f left join %s l on ", AppDB.fentrust, "fentrustlog");
                ds.add("if ( f.fEntrustType = 0, f.fid = l.fdeal_fid, f.fid = l.FEn_fId )");
                ds.add("where f.FUs_fId = %d and f.fstatus in(3,4) ", GetCurrentUser(request).getFid());
                if (symbol != 0) {
                    ds.add("and f.ftrademapping= %d", symbol);
                }
                ds.add("group by f.fid");
                ds.add("order by f.fid desc");
                ds.setMaximum(maxResults);
                ds.setOffset((currentPage - 1) * maxResults);
                ds.open();
                while (ds.fetch()) {
                    afPrizeMap.put(ds.getInt("fId"), ds.getDouble("afPrize"));
                }
            }
        }

        if (symbol != 0) {
            filter = filter + " and ftrademapping.fid=" + symbol;
        }
        filter = filter + " order by fid desc";
        List<Fentrust> fentrusts = this.entrustService.list((currentPage - 1) * maxResults, maxResults, filter, true);

        //
        // this.frontTradeService.findFentrustHistory(
        // GetSession(request).getFid(),
        // ftrademapping.getFid(),
        // null, PaginUtil.firstResult(currentPage, maxResults),
        // maxResults,
        // "id desc ", fstatus) ;

        int total = this.adminService.getAllCount("Fentrust", filter);
        String pagin = PaginUtil.generatePagin(total / maxResults + (total % maxResults == 0 ? 0 : 1), currentPage,
                "/trade/entrust.html?symbol=" + symbol + "&status=" + status + "&");
        // 查询分区币种信息
        List<Ftrademapping> ftrademappings = this.ftradeMappingService
                .findActiveTradeMappingByFB(ftrademapping.getFvirtualcointypeByFvirtualcointype1());

        modelAndView.addObject("ftrademappings", ftrademappings);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("fentrusts", fentrusts);
        modelAndView.addObject("afPrizeMap", afPrizeMap);
        modelAndView.addObject("status", status);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("coinType", coinType);
        modelAndView.addObject("total", total);
        modelAndView.setJspFile("front/trade/trade_entrust0");
        return modelAndView;
    }

    /*
     * http://localhost:8899/trade/entrustInfo.html?type=0&random=74&_=
     * 1393130976495
     */
    /*
     * @param type:0未成交前十条＿成交剿0板
     * 
     * @param symbol:1币种
     */
    @RequestMapping(value = { "/m/trade/entrustInfo", "/trade/entrustInfo" })
    public ModelAndView entrustInfo(HttpServletRequest request, @RequestParam(required = true) int type,
            @RequestParam(required = true) int symbol, @RequestParam(required = true) int tradeType) throws Exception {

        JspPage modelAndView = new JspPage(request);

        int userid = 0;
        if (GetCurrentUser(request) != null) {
            userid = GetCurrentUser(request).getFid();
        }

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        if (ftrademapping == null || ftrademapping.getFstatus() == TrademappingStatusEnum.FOBBID) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        Fvirtualcointype coin1 = ftrademapping.getFvirtualcointypeByFvirtualcointype1();
        Fvirtualcointype coin2 = ftrademapping.getFvirtualcointypeByFvirtualcointype2();

        List<Fentrust> fentrusts1 = null;
        fentrusts1 = this.frontTradeService.findFentrustHistory(userid, symbol, null, 0, 10, " fid desc ",
                new int[] { EntrustStatusEnum.Going, EntrustStatusEnum.PartDeal });

        List<Fentrust> fentrusts2 = null;
        fentrusts2 = this.frontTradeService.findFentrustHistory(userid, symbol, null, 0, 10, " fid desc ",
                new int[] { EntrustStatusEnum.Cancel, EntrustStatusEnum.AllDeal });

        if (GetCurrentUser(request) == null) {
            modelAndView.addObject("ispost", false);
        } else {
            modelAndView.addObject("ispost", GetCurrentUser(request).getFpostRealValidate());
        }

        modelAndView.addObject("ftrademapping", ftrademapping);
        modelAndView.addObject("coin1", coin1);
        modelAndView.addObject("coin2", coin2);
        modelAndView.addObject("tradeType", tradeType);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("type", type);
        modelAndView.addObject("fentrusts1", fentrusts1);
        modelAndView.addObject("fentrusts2", fentrusts2);
        modelAndView.setJspFile("front/trade/entrust_info");
        return modelAndView;
    }

    /*
     * @param symbol:1币种
     */
    @RequestMapping(value = "/app/entrustInfoApp", produces = { JsonEncode })
    @ResponseBody
    public String entrustInfoApp(@RequestParam(required = true) int symbol, @RequestParam(required = true) String uuid)
            throws Exception {

        JSONObject jsonObject = new JSONObject();

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        if (ftrademapping == null || ftrademapping.getFstatus() == TrademappingStatusEnum.FOBBID) {
            return jsonObject.toString();
        }

        List<Fentrust> fentrusts1 = null;
        fentrusts1 = this.frontTradeService.findFentrustHistory(Integer.parseInt(uuid), symbol, null, 0, 100,
                " fid desc ", new int[] { EntrustStatusEnum.Going, EntrustStatusEnum.PartDeal });

        for (Fentrust fentrust : fentrusts1) {
            JSONObject js = new JSONObject();
            js.accumulate("Fid", fentrust.getFid());
            js.accumulate("Time", fentrust.getFcreateTime().toString());
            js.accumulate("Type", fentrust.getFentrustType_s());
            js.accumulate("Price", fentrust.getFprize());
            js.accumulate("Total", fentrust.getFcount());
            js.accumulate("Amount", fentrust.getFamount());
            jsonObject.accumulate("entrustList", js);
        }

        return jsonObject.toString();
    }

    /*
     * @param symbol:1币种
     */
    @RequestMapping(value = "/app/dealInfoApp", produces = { JsonEncode })
    @ResponseBody
    public String dealInfoApp(@RequestParam(required = true) int symbol, @RequestParam(required = true) String uuid)
            throws Exception {

        JSONObject jsonObject = new JSONObject();

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        if (ftrademapping == null || ftrademapping.getFstatus() == TrademappingStatusEnum.FOBBID) {
            return jsonObject.toString();
        }

        List<Fentrust> fentrusts1 = null;
        fentrusts1 = this.frontTradeService.findFentrustHistory(Integer.parseInt(uuid), symbol, null, 0, 10,
                " fid desc ", new int[] { EntrustStatusEnum.Cancel, EntrustStatusEnum.AllDeal });

        for (Fentrust fentrust : fentrusts1) {
            JSONObject js = new JSONObject();
            js.accumulate("Fid", fentrust.getFid());
            js.accumulate("Time", fentrust.getFcreateTime().toString());
            js.accumulate("Type", fentrust.getFentrustType_s());
            js.accumulate("Price", fentrust.getFprize());
            js.accumulate("Total", fentrust.getFcount());
            js.accumulate("Amount", fentrust.getFamount());
            jsonObject.accumulate("dealList", js);
        }

        return jsonObject.toString();
    }

}
