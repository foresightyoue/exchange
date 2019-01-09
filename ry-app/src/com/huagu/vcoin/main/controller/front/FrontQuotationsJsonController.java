package com.huagu.vcoin.main.controller.front;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Ftradehistory;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.main.service.front.UtilsService;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.TDateTime;
import net.sf.json.JSONObject;

@Controller
public class FrontQuotationsJsonController extends BaseController {
    private static Logger log = Logger.getLogger(FrontQuotationsJsonController.class);
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private RedisUtil redisUtil;

    @ResponseBody
    @RequestMapping(value = "/real/price", produces = { JsonEncode })
    public String priceMarket() {
        JSONObject jsonObject = new JSONObject();

        double latestDealPrize = (Double) this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(1));
        jsonObject.accumulate("result", true);
        jsonObject.accumulate("last", latestDealPrize);
        return jsonObject.toString();
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/real/indexmarket", "/m/real/indexmarket"}, produces = { JsonEncode })
    public String indexmarket(HttpServletRequest request, HttpServletResponse resp,
            @RequestParam(required = false) String lasts, @RequestParam(required = false) String areas) {
        JSONObject jsonObject = new JSONObject();
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0, " where fstatus=? order by fid asc ", false,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);

        for (Ftrademapping ftrademapping : ftrademappings) {
            JSONObject js = new JSONObject();
            //int state = ftrademapping.getState();
            //String tableName = state == 0 ?"fperiod" :"fzbkline";
            Double tmp = (Double) this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping.getFid()));
            double price = tmp != null ? tmp : 0;
            double fchangerate = 0;
            JspPage jspPage = new JspPage(request);

            String lastRMB = jspPage.getRequest().getParameter("last");
            String arsa = jspPage.getRequest().getParameter("area");
            if (!"".equals(lastRMB) && lastRMB != null && !"".equals(arsa) && arsa != null) {
                try {
                    Mysql handle = new Mysql();
                    MyQuery dsUser = new MyQuery(handle);
                    dsUser.add("select * from %s where fShortName='%s'", "fvirtualcointype", arsa);
                    dsUser.open();

                    if (!dsUser.eof()) {
                        dsUser.edit();
                        dsUser.setField("fcurrentCNY", lastRMB);
                        dsUser.post();
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
            try (Mysql mysql = new Mysql()) {
                // 24h 最低、最高、总量
                
                MyQuery ds1 = new MyQuery(mysql);
              //  if(state == 0) {
                    ds1.add("select max(pd.fgao) as fgao,sum(pd.fliang) as fliang,min(pd.fdi) as fdi,");
                    ds1.add("pd.ftime as fti,ty1.fcurrentCNY as fny, pd.fliang as fli");
                    ds1.add("from %s mp", AppDB.Ftrademapping);
                    ds1.add("inner join %s pd on pd.ftrademapping=mp.fid", AppDB.fperiod);
                    ds1.add("inner join %s ty1 on mp.fvirtualcointype1=ty1.fId", AppDB.fvirtualcointype);
                    ds1.add("where mp.fid=%d", ftrademapping.getFid());
                    ds1.add("and pd.ftime>='%s' and pd.fdi>0", TDateTime.Now().incDay(-1));
                    ds1.add(" and pd.frequency = 1");
                //}else {
                  //  ds1.add("select max(pd.fgao) as fgao,sum(pd.fliang) as fliang,min(pd.fdi) as fdi,");
                    //ds1.add("pd.fCreateTime as fti,ty1.fcurrentCNY as fny,pd.fliang as fli");
                    //ds1.add("from %s mp", AppDB.Ftrademapping);
                    //ds1.add("inner join %s pd on pd.ftrademapping=mp.fid", tableName);
                    //ds1.add("inner join %s ty1 on mp.fvirtualcointype1=ty1.fId", AppDB.fvirtualcointype);
                    //ds1.add("where mp.fid=%d", ftrademapping.getFid());
                    //ds1.add("and pd.fCreateTime>='%s' and pd.fdi>0", TDateTime.Now().incDay(-1));
                    //ds1.add(" and pd.frequency = 1");
                // }
                ds1.open();
                // 24h开、收
                log.info("fshou+---" + ds1.getDouble("fshou") + "fliang----" + ds1.getDouble("fliang"));
                js.accumulate("total", ds1.getDouble("fliang"));
                js.accumulate("high", ds1.getDouble("fgao"));
                js.accumulate("low", ds1.getDouble("fdi"));
                js.accumulate("fpc", ds1.getDouble("fny"));

                MyQuery ds3 = new MyQuery(mysql);
                // if (state == 0) {
                    ds3.add("select lastprice.fshou as fshou,firstprice.fkai as fkai");
                    ds3.add("from");
                    ds3.add("(select fshou from %s where ftrademapping=%d", AppDB.fperiod, ftrademapping.getFid());
                    ds3.add("and ftime between '%s' and '%s' order by fid desc limit 1) lastprice,",
                        TDateTime.Now().incDay(-1), TDateTime.Now());
                    ds3.add("(select fkai from %s where ftrademapping=%d", AppDB.fperiod, ftrademapping.getFid());
                    ds3.add("and ftime between '%s' and '%s' order by fid limit 1) firstprice",
                            TDateTime.Now().incDay(-1),
                        TDateTime.Now());
                // } else {
                // ds3.add("select lastprice.fshou as fshou,firstprice.fkai as fkai");
                // ds3.add("from");
                // ds3.add("(select fshou from %s where ftrademapping=%d", tableName,
                // ftrademapping.getFid());
                // ds3.add("and fCreateTime between '%s' and '%s' order by fid desc limit 1)
                // lastprice,",
                // TDateTime.Now().incDay(-1), TDateTime.Now());
                // ds3.add("(select fkai from %s where ftrademapping=%d", tableName,
                // ftrademapping.getFid());
                // ds3.add("and fCreateTime between '%s' and '%s' order by fid limit 1)
                // firstprice",
                // TDateTime.Now().incDay(-1), TDateTime.Now());
                // }
                ds3.open();
                if (!ds3.eof()) {
                    js.accumulate("price", ds3.getDouble("fshou"));
                    js.accumulate("kai", ds3.getDouble("fkai"));
                    if (ds3.getDouble("fkai") != 0) {
                        fchangerate = (ds3.getDouble("fshou") - ds3.getDouble("fkai")) / ds3.getDouble("fkai");
                    }
                } else {
                    js.accumulate("price", price);
                    js.accumulate("kai", 0);
                }
                MyQuery ds2 = new MyQuery(mysql);
                // if (state == 0) {
                // ds2.add("select fshou as flian from %s", AppDB.fperiod);
                // ds2.add("where ftime>='%s' and ftrademapping=%d", TDateTime.Now().incDay(-3),
                // ftrademapping.getFid());
                // ds2.add(" and frequency = 1");
                // ds2.add("order by ftime asc");
                // } else {
                // ds2.add("select fshou as flian from %s", tableName);
                // ds2.add("where fCreateTime>='%s' and ftrademapping=%d",
                // TDateTime.Now().incDay(-3),
                // ftrademapping.getFid());
                // ds2.add(" and frequency = 1");
                // ds2.add("order by fCreateTime asc");
                // }
                // ds2.setMaximum(500);
                ds2.add("select fshou as flian from");
                ds2.add(" (select fshou,ftime from %s", AppDB.fperiod);
                ds2.add("where ftime>='%s' and ftrademapping=%d", TDateTime.Now().incDay(-3), ftrademapping.getFid());
                ds2.add(" and frequency = 1");
                ds2.add("order by ftime desc");
                ds2.add("limit 500 ) pd");
                ds2.add(" order by pd.ftime asc");
                ds2.open();

                List<Object> list = new ArrayList<>();
                List<Object> lists = new ArrayList<>();
                String num1 = "1521169200000";
                int num2 = 36;
                DecimalFormat df = new DecimalFormat("0.0000");
                while (ds2.fetch()) {
                    if (list.size() < 6) {
                        if (list.size() == 0) {
                            num1 = Integer.parseInt(num1.substring(0, 8)) + num2 + "00000";
                            list.add(num1);
                        }
                        list.add(df.format(ds2.getDouble("flian")));
                    } else {
                        lists.add(list);
                        list = new ArrayList<>();
                    }
                    if (lists.size() >= 72) {
                        break;
                    }
                }
                js.put("fli", lists);
            }
            js.accumulate("symbol", ftrademapping.getFvirtualcointypeByFvirtualcointype1().getfSymbol());

            double s7 = -8888d;
            List<Ftradehistory> ftradehistoryss = (List<Ftradehistory>) constantMap.get("ftradehistory7D");
            for (Ftradehistory ftradehistory : ftradehistoryss) {
                if (ftradehistory.getFtrademapping().getFid().intValue() == ftrademapping.getFid().intValue()) {
                    s7 = ftradehistory.getFprice();
                    break;
                }
            }
            double last7 = 0d;
            if (s7 != -8888d) {
                last7 = Utils.getDouble((price - s7) / s7 * 100, 2);
            }
            js.accumulate("rose", String.valueOf(fchangerate));
            js.accumulate("rose7", last7);

            jsonObject.accumulate(String.valueOf(ftrademapping.getFid()), js);
        }
        return jsonObject.toString();
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/user/api/indexmarket", "/m/user/api/indexmarket"}, produces = { JsonEncode })
    public String apiIndexmarket(HttpServletRequest request, HttpServletResponse resp,
            @RequestParam(required = false) String lasts, @RequestParam(required = false) String areas) {
        JSONObject jsonObject = new JSONObject();
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0, " where fstatus=? order by fid asc ", false,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);

        for (Ftrademapping ftrademapping : ftrademappings) {
            JSONObject js = new JSONObject();
            Double tmp = (Double) this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping.getFid()));
            double price = tmp != null ? tmp : 0;
            double fchangerate = 0;
            JspPage jspPage = new JspPage(request);

            String lastRMB = jspPage.getRequest().getParameter("last");
            String arsa = jspPage.getRequest().getParameter("area");
            if (!"".equals(lastRMB) && lastRMB != null && !"".equals(arsa) && arsa != null) {
                try {
                    Mysql handle = new Mysql();
                    MyQuery dsUser = new MyQuery(handle);
                    dsUser.add("select * from %s where fShortName='%s'", "fvirtualcointype", arsa);
                    dsUser.open();

                    if (!dsUser.eof()) {
                        dsUser.edit();
                        dsUser.setField("fcurrentCNY", lastRMB);
                        dsUser.post();
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
            try (Mysql mysql = new Mysql()) {
                // 24h 最低、最高、总量
                
                MyQuery ds1 = new MyQuery(mysql);
                    ds1.add("select max(pd.fgao) as fgao,sum(pd.fliang) as fliang,min(pd.fdi) as fdi,");
                    ds1.add("pd.ftime as fti,ty1.fcurrentCNY as fny, pd.fliang as fli");
                    ds1.add("from %s mp", AppDB.Ftrademapping);
                    ds1.add("inner join %s pd on pd.ftrademapping=mp.fid", AppDB.fperiod);
                    ds1.add("inner join %s ty1 on mp.fvirtualcointype1=ty1.fId", AppDB.fvirtualcointype);
                    ds1.add("where mp.fid=%d", ftrademapping.getFid());
                    ds1.add("and pd.ftime>='%s' and pd.fdi>0", TDateTime.Now().incDay(-1));
                    ds1.add(" and pd.frequency = 1");
                ds1.open();
                // 24h开、收
                log.info("fshou+---" + ds1.getDouble("fshou") + "fliang----" + ds1.getDouble("fliang"));
                js.accumulate("total", ds1.getDouble("fliang"));
                js.accumulate("high", ds1.getDouble("fgao"));
                js.accumulate("low", ds1.getDouble("fdi"));
                js.accumulate("fpc", ds1.getDouble("fny"));

                MyQuery ds3 = new MyQuery(mysql);
                    ds3.add("select lastprice.fshou as fshou,firstprice.fkai as fkai");
                    ds3.add("from");
                    ds3.add("(select fshou from %s where ftrademapping=%d", AppDB.fperiod, ftrademapping.getFid());
                    ds3.add("and ftime between '%s' and '%s' order by fid desc limit 1) lastprice,",
                        TDateTime.Now().incDay(-1), TDateTime.Now());
                    ds3.add("(select fkai from %s where ftrademapping=%d", AppDB.fperiod, ftrademapping.getFid());
                    ds3.add("and ftime between '%s' and '%s' order by fid limit 1) firstprice",
                            TDateTime.Now().incDay(-1),
                        TDateTime.Now());
                ds3.open();
                if (!ds3.eof()) {
                    js.accumulate("price", ds3.getDouble("fshou"));
                    js.accumulate("kai", ds3.getDouble("fkai"));
                    if (ds3.getDouble("fkai") != 0) {
                        fchangerate = (ds3.getDouble("fshou") - ds3.getDouble("fkai")) / ds3.getDouble("fkai");
                    }
                } else {
                    js.accumulate("price", price);
                    js.accumulate("kai", 0);
                }
                MyQuery ds2 = new MyQuery(mysql);
                ds2.add("select fshou as flian from");
                ds2.add(" (select fshou,ftime from %s", AppDB.fperiod);
                ds2.add("where ftime>='%s' and ftrademapping=%d", TDateTime.Now().incDay(-3), ftrademapping.getFid());
                ds2.add(" and frequency = 1");
                ds2.add("order by ftime desc");
                ds2.add("limit 500 ) pd");
                ds2.add(" order by pd.ftime asc");
                ds2.open();

                List<Object> list = new ArrayList<>();
                List<Object> lists = new ArrayList<>();
                String num1 = "1521169200000";
                int num2 = 36;
                DecimalFormat df = new DecimalFormat("0.0000");
                while (ds2.fetch()) {
                    if (list.size() < 6) {
                        if (list.size() == 0) {
                            num1 = Integer.parseInt(num1.substring(0, 8)) + num2 + "00000";
                            list.add(num1);
                        }
                        list.add(df.format(ds2.getDouble("flian")));
                    } else {
                        lists.add(list);
                        list = new ArrayList<>();
                    }
                    if (lists.size() >= 72) {
                        break;
                    }
                }
                js.put("fli", lists);
            }
            js.accumulate("symbol", ftrademapping.getFvirtualcointypeByFvirtualcointype1().getfSymbol());

            double s7 = -8888d;
            List<Ftradehistory> ftradehistoryss = (List<Ftradehistory>) constantMap.get("ftradehistory7D");
            for (Ftradehistory ftradehistory : ftradehistoryss) {
                if (ftradehistory.getFtrademapping().getFid().intValue() == ftrademapping.getFid().intValue()) {
                    s7 = ftradehistory.getFprice();
                    break;
                }
            }
            double last7 = 0d;
            if (s7 != -8888d) {
                last7 = Utils.getDouble((price - s7) / s7 * 100, 2);
            }
            js.accumulate("rose", String.valueOf(fchangerate));
            js.accumulate("rose7", last7);

            jsonObject.accumulate(String.valueOf(ftrademapping.getFid()), js);
        }
        return jsonObject.toString();
    }

    
    public double SplitAndRound(double a, int n) {
        a = a * Math.pow(10, n);
        return (Math.round(a)) / (Math.pow(10, n));
    }

    @ResponseBody
    @RequestMapping("/real/userassets")
    public String userassets(HttpServletRequest request, @RequestParam(required = true) int symbol) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping(symbol);
        Fuser fuser = GetCurrentUser(request);
        if (fuser == null) {
            // 可用
            jsonObject.accumulate("availableCny", 0);
            jsonObject.accumulate("availableCoin", 0);
            jsonObject.accumulate("frozenCny", 0);
            jsonObject.accumulate("frozenCoin", 0);
            // 借貸明細
            JSONObject leveritem = new JSONObject();
            leveritem.accumulate("total", 0);
            leveritem.accumulate("asset", 0);
            leveritem.accumulate("score", 0);
            jsonObject.accumulate("leveritem", leveritem);
            // 人民幣明細
            JSONObject cnyitem = new JSONObject();
            cnyitem.accumulate("total", 0);
            cnyitem.accumulate("frozen", 0);
            cnyitem.accumulate("borrow", 0);
            jsonObject.accumulate("cnyitem", cnyitem);
            // 人民幣明細
            JSONObject coinitem = new JSONObject();
            coinitem.accumulate("id", symbol);
            coinitem.accumulate("total", 0);
            coinitem.accumulate("frozen", 0);
            coinitem.accumulate("borrow", 0);
            jsonObject.accumulate("coinitem", coinitem);
        } else {
            fuser = this.frontUserService.findById(fuser.getFid());
            Fvirtualwallet fwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                    ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFid());
            Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                    ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFid());

            // 可用
            jsonObject.accumulate("availableCny", fwallet.getFtotal());
            jsonObject.accumulate("availableCoin", fvirtualwallet.getFtotal());
            jsonObject.accumulate("frozenCny", fwallet.getFfrozen());
            jsonObject.accumulate("frozenCoin", fvirtualwallet.getFfrozen());
            // 借貸明細
            JSONObject leveritem = new JSONObject();
            leveritem.accumulate("total", 0);
            leveritem.accumulate("asset", 0);
            leveritem.accumulate("score", 0);
            jsonObject.accumulate("leveritem", leveritem);
            // 人民幣明細
            JSONObject cnyitem = new JSONObject();
            cnyitem.accumulate("total", fwallet.getFtotal());
            cnyitem.accumulate("frozen", fwallet.getFfrozen());
            cnyitem.accumulate("borrow", 0);
            jsonObject.accumulate("cnyitem", cnyitem);
            // 人民幣明細
            JSONObject coinitem = new JSONObject();
            coinitem.accumulate("id", symbol);
            coinitem.accumulate("total", fvirtualwallet.getFtotal());
            coinitem.accumulate("frozen", fvirtualwallet.getFfrozen());
            coinitem.accumulate("borrow", 0);
            jsonObject.accumulate("coinitem", coinitem);
        }

        return jsonObject.toString();

    }

}
