package com.huagu.vcoin.main.controller.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Ftradehistory;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.main.service.front.UtilsService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

import net.sf.json.JSONObject;

@Controller
public class FrontMarketController extends BaseController {

    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private UtilsService utilsService;

    /*
     * https://www.okcoin.com/market.do?symbol=0
     */
    @RequestMapping("/market")
    public ModelAndView market(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        JspPage modelAndView = new JspPage(request);

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0,
                " where fstatus=? order by fvirtualcointypeByFvirtualcointype1.ftype asc", false, Ftrademapping.class,
                TrademappingStatusEnum.ACTIVE);
        if (ftrademapping == null || ftrademapping.getFstatus() != TrademappingStatusEnum.ACTIVE) {
            if (ftrademappings.size() > 0) {
                modelAndView.setViewName("redirect:/market.html?symbol=" + ftrademappings.get(0).getFid());
                return modelAndView;
            }
        }

        modelAndView.addObject("ftrademappings", ftrademappings);
        modelAndView.addObject("ftrademapping", ftrademapping);
        modelAndView.addObject("symbol", symbol);
        modelAndView.setJspFile("front/market/market");
        return modelAndView;
    }


    @RequestMapping(value = { "/trademarket", "/m/market/trademarket" })
    public ModelAndView trademarket(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0, " where fstatus=?", false,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);
        if (ftrademapping == null || ftrademapping.getFstatus() != TrademappingStatusEnum.ACTIVE) {
            if (ftrademappings.size() > 0) {
                modelAndView.setViewName("redirect:/trademarket.html?symbol=" + ftrademappings.get(0).getFid());
                return modelAndView;
            } else {
                modelAndView.setViewName("redirect:/");
                return modelAndView;
            }
        }

        // Flimittrade limittrade = this.isLimitTrade(ftrademapping.getFid());
        // boolean isLimittrade = false;
        // double upPrice = 0d;
        // double downPrice = 0d;
        // if(limittrade != null){
        // isLimittrade = true;
        // upPrice =
        // Utils.getDouble(limittrade.getFupprice()+limittrade.getFupprice()*limittrade.getFpercent(),
        // ftrademapping.getFcount1());
        // downPrice =
        // Utils.getDouble(limittrade.getFdownprice()-limittrade.getFdownprice()*limittrade.getFpercent(),
        // ftrademapping.getFcount1());
        // if(downPrice <0) downPrice=0;
        // }
        // modelAndView.addObject("isLimittrade", isLimittrade) ;
        // modelAndView.addObject("upPrice", upPrice) ;
        // modelAndView.addObject("downPrice", downPrice) ;

        String userid = "";
        boolean tradePassword = false;
        boolean isTelephoneBind = false;
        int login = 0;
        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            userid = String.valueOf(fuser.getFid());
            if (fuser.isFisTelephoneBind()) {
                isTelephoneBind = true;
            }
            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0) {
                tradePassword = true;
            }
            login = 1;
            modelAndView.addObject("fuser", fuser);
        }
        try (Mysql mysql = new Mysql()) {
            MyQuery ds3 = new MyQuery(mysql);
            ds3.add("select min(pd.fdi) as fdi,max(pd.fgao) as fgao,sum(fliang) as fliang from %s pd", AppDB.fperiod);
            ds3.add("inner join %s mp on pd.ftrademapping = mp.fid ", AppDB.Ftrademapping);
            ds3.add("inner join %s ty1 on mp.fvirtualcointype1 = ty1.fId", AppDB.fvirtualcointype);
            ds3.add(" where mp.fid ='%s' and ftime >=date_sub(now(),INTERVAL 1 day) and pd.fdi > 0", symbol);
            ds3.open();
            if (!ds3.eof()) {
                modelAndView.addObject("total", ds3.getDouble("fliang"));
                modelAndView.addObject("high", ds3.getDouble("fgao"));
                modelAndView.addObject("low", ds3.getDouble("fdi"));
            }
            MyQuery ds4 = new MyQuery(mysql);
            ds4.add("select pd.fshou from %s pd", AppDB.fperiod);
            ds4.add("inner join %s mp on pd.ftrademapping = mp.fid", AppDB.Ftrademapping);
            ds4.add("inner join %s ty1 on mp.fvirtualcointype1 = ty1.fid", AppDB.fvirtualcointype);
            ds4.add(" where pd.ftrademapping = '%s' and ftime >= date_sub(now(),INTERVAL 1 day) and ftime < now()",
                    symbol);
            ds4.add(" order by pd.fid desc");
            ds4.setMaximum(1);
            ds4.open();
            if (!ds4.eof()) {
                modelAndView.addObject("price", ds4.getDouble("fshou"));
            }
            // 24h涨跌
            MyQuery ds5 = new MyQuery(mysql);
            ds5.add("select (lastprice.fshou-firstprice.fkai)/firstprice.fkai AS fchangerate "
                    + "from (select fshou from fperiod "
                    + "where ftrademapping=%d and ftime>=date_sub(now(),interval 1 day) "
                    + "and ftime< now() order by fid desc limit 1) lastprice,"
                    + "(select fkai from fperiod where ftrademapping=%d and ftime>=date_sub(now(),interval 1 day) "
                    + "and ftime< now() order by fid limit 1) firstprice", symbol, ftrademapping.getFid());
            ds5.open();
            if (!ds3.eof()) {
                double rose = ds3.getDouble("fchangerate") * 100;
                modelAndView.addObject("rose", rose);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Mysql mysql = new Mysql()) {
            // 买价
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select ft.fPrize,ft.flastUpdatTime,ft.fStatus from %s ft", AppDB.fentrust);
            ds1.add(" inner join %s fm", AppDB.Ftrademapping);
            ds1.add(" on ft.ftrademapping = fm.fid");
            ds1.add(" where fm.fid =%s and ft.fEntrustType = 0", symbol);
            ds1.add(" and ft.fStatus in(3)");
            ds1.add(" order by flastUpdatTime desc");
            ds1.setMaximum(1);
            ds1.open();

            // 卖价
            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select ft.fPrize,ft.flastUpdatTime,ft.fStatus from %s ft", AppDB.fentrust);
            ds2.add(" inner join %s fm", AppDB.Ftrademapping);
            ds2.add(" on ft.ftrademapping = fm.fid");
            ds2.add(" where fm.fid = %s and ft.fEntrustType = 1", symbol);
            ds2.add(" and ft.fStatus in(3)");
            ds2.add(" order by flastUpdatTime desc");
            ds2.setMaximum(1);
            ds2.open();
            double p_new = 0L;
            if (!ds1.eof() && !ds2.eof()) {
                if (ds1.getDouble("fPrize") <= p_new) {
                    p_new = ds1.getDouble("fPrize");
                } else if (ds2.getDouble("fPrice") >= p_new) {
                    p_new = ds2.getDouble("fPrice");
                } else if (ds2.getDouble("fPrice") < p_new && p_new < ds1.getDouble("fPrice")) {
                    p_new = p_new;
                }
                modelAndView.addObject("p_new", p_new);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Mysql mysql = new Mysql()) {
            MyQuery cds = new MyQuery(mysql);
            cds.add("select fSymbol from %s ft", AppDB.Ftrademapping);
            cds.add(" left join %s fv", AppDB.fvirtualcointype);
            cds.add(" on ft.fvirtualcointype1 = fv.fId");
            cds.add(" where ft.fId = '%s'", symbol);
            cds.open();
            modelAndView.add("fSymbol", cds.getString("fSymbol"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("userid", userid);
        modelAndView.addObject("tradePassword", tradePassword);
        modelAndView.addObject("isTelephoneBind", isTelephoneBind);
        modelAndView.addObject("login", login);

        // 是否需要输入交易密码
        modelAndView.addObject("needTradePasswd", super.isNeedTradePassword(request));

        modelAndView.addObject("ftrademappings", ftrademappings);
        modelAndView.addObject("ftrademapping", ftrademapping);
        modelAndView.addObject("symbol", symbol);
        modelAndView.setJspFile("front/market/trademarket");
        return modelAndView;
    }

    @RequestMapping("/kline/fullstart")
    public ModelAndView start(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        JspPage modelAndView = new JspPage(request);

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(symbol);
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 1, " where fstatus=? ", true,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);

        if (ftrademapping == null) {
            if (ftrademappings.size() > 0) {
                modelAndView.setViewName("redirect:/kline/fullstart.html?symbol=" + ftrademappings.get(0).getFid());
                return modelAndView;
            } else {
                modelAndView.setViewName("redirect:/");
                return modelAndView;
            }
        }

        modelAndView.addObject("ftrademapping", ftrademapping);
        modelAndView.addObject("ftrademappings", ftrademappings);
        modelAndView.setJspFile("front/market/start");
        return modelAndView;
    }

    @RequestMapping("/kline/trade")
    public ModelAndView trade(HttpServletRequest request, @RequestParam(required = true) int id) throws Exception {
        JspPage modelAndView = new JspPage(request);

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(id);
        modelAndView.addObject("ftrademapping", ftrademapping);

        modelAndView.setJspFile("front/market/kline");
        return modelAndView;
    }

    @RequestMapping(value = { "/m/market/m/tv-chart", "/m/tv-chart" })
    public ModelAndView tv_chart(HttpServletRequest request) {
        JspPage jspPage = new JspPage(request);
        jspPage.add("symbol", request.getParameter("symbol"));
        jspPage.add("interval", request.getParameter("interval"));
        jspPage.add("timeframe", request.getParameter("timeframe"));
        jspPage.add("toolbarbg", request.getParameter("toolbarbg"));
        jspPage.add("studiesAccess", request.getParameter("studiesAccess"));
        jspPage.add("widgetbar", request.getParameter("widgetbar"));
        jspPage.add("drawingsAccess", request.getParameter("drawingsAccess"));
        jspPage.add("timeFrames", request.getParameter("timeFrames"));
        jspPage.add("locale", request.getParameter("locale"));
        jspPage.add("uid", request.getParameter("uid"));
        jspPage.add("clientId", request.getParameter("clientId"));
        jspPage.add("userId", request.getParameter("userId"));
        jspPage.add("chartsStorageUrl", request.getParameter("chartsStorageUrl"));
        jspPage.add("chartsStorageVer", request.getParameter("chartsStorageVer"));
        jspPage.add("indicatorsFile", request.getParameter("indicatorsFile"));
        jspPage.add("customCSS", request.getParameter("customCSS"));
        jspPage.add("autoSaveDelay", request.getParameter("autoSaveDelay"));
        jspPage.add("debug", request.getParameter("debug"));
        jspPage.add("snapshotUrl", request.getParameter("snapshotUrl"));
        jspPage.add("timezone", request.getParameter("timezone"));
        jspPage.add("studyCountLimit", request.getParameter("studyCountLimit"));
        jspPage.add("ssreqdelay", request.getParameter("ssreqdelay"));
        jspPage.add("hidetoptoolbar", request.getParameter("hidetoptoolbar"));
        jspPage.setJspFile("front/market/tv-chart");
        return jspPage;
    }


    @ResponseBody
    @RequestMapping(value = "/json/PCfNewPrice", produces = JsonEncode)
    public String getPCNewPrice(HttpServletRequest request, @RequestParam(required = true) int symbol,
            @RequestParam(required = false, defaultValue = "0") double p_new) {
        JSONObject jsonObject = new JSONObject();
        int num = 0;
        try (Mysql mysql = new Mysql()) {
            // 买价
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select ft.fPrize,ft.flastUpdatTime,ft.fStatus from %s ft", AppDB.fentrust);
            ds1.add(" inner join %s fm", AppDB.Ftrademapping);
            ds1.add(" on ft.ftrademapping = fm.fid");
            ds1.add(" where fm.fid =%s and ft.fEntrustType = 0", symbol);
            ds1.add(" and ft.fStatus in(3)");
            ds1.add(" order by fPrize desc");
            ds1.setMaximum(1);
            ds1.open();

            // 卖价
            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select ft.fPrize,ft.flastUpdatTime,ft.fStatus from %s ft", AppDB.fentrust);
            ds2.add(" inner join %s fm", AppDB.Ftrademapping);
            ds2.add(" on ft.ftrademapping = fm.fid");
            ds2.add(" where fm.fid = %s and ft.fEntrustType = 1", symbol);
            ds2.add(" and ft.fStatus in(3)");
            ds2.add(" order by fPrize desc");
            ds2.setMaximum(1);
            ds2.open();
            if (!ds1.eof() && !ds2.eof()) {
                if (ds1.getDouble("fPrize") <= p_new) {
                    p_new = ds1.getDouble("fPrize");
                    num = 1;
                } else if (ds2.getDouble("fPrize") >= p_new) {
                    p_new = ds2.getDouble("fPrize");
                    num = 0;
                } else if (ds2.getDouble("fPrize") < p_new && p_new < ds1.getDouble("fPrice")) {
                    p_new = p_new;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.accumulate("num", num);
        jsonObject.accumulate("p_new", p_new);
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/json/fNewPrice1" }, produces = { JsonEncode })
    public String fNewPrice1(HttpServletRequest request, @RequestParam(required = true) int symbol,
            @RequestParam(required = false, defaultValue = "0") double marketPrice) {
        JSONObject jsonObject = new JSONObject();
        double p_new = 0, rose = 0;
        if (0 != marketPrice) {
            p_new = marketPrice;
        }
        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping(symbol);
        if(ftrademapping == null){
            return null;
        }
        double price = 10.0;
        List<Ftradehistory> ftradehistorys = (List<Ftradehistory>) constantMap.get("tradehistory");
        for (Ftradehistory ftradehistory : ftradehistorys) {
            if (ftradehistory.getFtrademapping().getFid().intValue() == ftrademapping.getFid().intValue()) {
                price = ftradehistory.getFprice();
                break;
            }
        }
        try (Mysql mysql = new Mysql()) {
            MyQuery ds3 = new MyQuery(mysql);
            ds3.add("select min(pd.fdi) as fdi,max(pd.fgao) as fgao,sum(fliang) as fliang from %s pd", AppDB.fperiod);
            ds3.add("inner join %s mp on pd.ftrademapping = mp.fid ", AppDB.Ftrademapping);
            ds3.add("inner join %s ty1 on mp.fvirtualcointype1 = ty1.fId", AppDB.fvirtualcointype);
            ds3.add(" where mp.fid ='%s' and ftime >=date_sub(now(),INTERVAL 1 day) and pd.fdi > 0", symbol);
            ds3.open();
            if (!ds3.eof()) {
                jsonObject.accumulate("total", ds3.getDouble("fliang"));
                jsonObject.accumulate("high", ds3.getDouble("fgao"));
                jsonObject.accumulate("low", ds3.getDouble("fdi"));
            }
            MyQuery ds4 = new MyQuery(mysql);
            ds4.add("select pd.fshou from %s pd", AppDB.fperiod);
            ds4.add("inner join %s mp on pd.ftrademapping = mp.fid", AppDB.Ftrademapping);
            ds4.add("inner join %s ty1 on mp.fvirtualcointype1 = ty1.fid", AppDB.fvirtualcointype);
            ds4.add(" where pd.ftrademapping = '%s' and ftime >= date_sub(now(),INTERVAL 1 day) and ftime < now()",
                    symbol);
            ds4.add(" order by pd.fid desc");
            ds4.setMaximum(1);
            ds4.open();
            if (!ds4.eof()) {
                jsonObject.accumulate("price", ds4.getDouble("fshou"));
            }
            // 24h涨跌
            MyQuery ds5 = new MyQuery(mysql);
            ds5.add("select (lastprice.fshou-firstprice.fkai)/firstprice.fkai AS fchangerate "
                    + "from (select fshou from fperiod "
                    + "where ftrademapping=%d and ftime>=date_sub(now(),interval 1 day) "
                    + "and ftime< now() order by fid desc limit 1) lastprice,"
                    + "(select fkai from fperiod where ftrademapping=%d and ftime>=date_sub(now(),interval 1 day) "
                    + "and ftime< now() order by fid limit 1) firstprice", symbol, ftrademapping.getFid());
            ds5.open();
            if (!ds5.eof()) {
                rose = ds5.getDouble("fchangerate") * 100;

            }
            /*
             * if (0 == marketPrice) { MyQuery ds6 = new MyQuery(mysql); ds6.
             * add(" select max(pd.fgao) fgao,max(pd.fshou) as fshou,max(pd.fliang) as fliang,min(pd.fdi) as fid,max(pd.fkai) as fkai,ty1.fcurrentCNY as fny from %s pd"
             * , AppDB.fperiod); ds6.add(" inner join %s mp on pd.ftrademapping = mp.fid",
             * AppDB.Ftrademapping);
             * ds6.add(" inner join %s ty1 on mp.fvirtualcointype1 =ty1.fId",
             * AppDB.fvirtualcointype); ds6.add(" where mp.fid = %s", symbol); ds6.open();
             * p_new = ds6.getDouble("fshou"); jsonObject.accumulate("p_new",p_new); }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if (0 != marketPrice) {
            try (Mysql mysql = new Mysql()) {
            /*
             * MyQuery ds7 = new MyQuery(mysql); ds7.
             * add("select distinct sum(l.famount)/sum(l.fcount) afPrize,l.fen_fid,f.fPrize from %s f"
             * , AppDB.fentrust); ds7.add(" left join %s l on", AppDB.fentrustlog);
             * ds7.add(" if(f.fEntrustType = 0,f.fid =l.fdeal_fid,f.fid = l.FEn_fId)");
             * ds7.add(""); ds7.add(" left join %s map ", AppDB.Ftrademapping);
             * ds7.add(" on l.ftrademapping=map.fid"); ds7.add(" where map.fid = %s",
             * symbol); ds7.add(" and f.fstatus in(3) group by f.fid ");
             * ds7.add(" order by l.fCreateTime desc"); ds7.setMaximum(1); ds7.open();
             * double sellPrice = ds7.getDouble("afPrize"); double buyPrice =
             * ds7.getDouble("fPrize"); if (sellPrice >= p_new) { p_new = sellPrice; } else
             * if (buyPrice <= p_new) { p_new = buyPrice; } else if (sellPrice < p_new &&
             * p_new < buyPrice) { p_new = (sellPrice + buyPrice) / 2; }
             */
            MyQuery ds7 = new MyQuery(mysql);
            ds7.add("select lastprice.fshou as fshou,firstprice.fkai as fkai from (select fshou from fperiod "
                    + "where ftrademapping = %d and ftime >= date_sub(now(),interval 1 day) "
                    + "and ftime < now() order by fid desc limit 1) lastprice,"
                    + " ( select fkai from fperiod where ftrademapping = %d and ftime >= date_sub(now(),interval 1 day ) "
                    + " and ftime < now() order by fid limit 1) firstprice", symbol, symbol);
            ds7.open();
            if (!ds7.eof()) {
                p_new = ds7.getDouble("fshou");
            }
            jsonObject.accumulate("p_new", p_new);
            } catch (Exception e) {
            e.printStackTrace();
            }
        // }
        jsonObject.accumulate("rose", rose);
        return jsonObject.toString();
    }

}
