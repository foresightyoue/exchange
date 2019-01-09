package com.huagu.vcoin.queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.controller.front.data.MarketData;
import com.huagu.vcoin.main.controller.front.data.MarketItem;
import com.huagu.vcoin.main.controller.front.data.MarketItem2;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;

import cn.cerc.jdb.cache.Buffer;
import net.sf.json.JSONObject;

@Component
public class RefreshMarket implements Runnable {
    private Mysql handle;
    @Override
    @Scheduled(fixedRate = 1500)
    public void run() {
        handle = new Mysql();
        try {
            MyQuery ds1 = new MyQuery(handle);
            ds1.add("select fid from %s", AppDB.Ftrademapping);
            ds1.add("where fstatus=1");
            ds1.open();
            while (ds1.fetch()) {
                int symbol = ds1.getInt("fid");
                String redisKey = RedisConstant.getstart24Price(symbol);
                String data = processOne(symbol);
                Buffer buff = new Buffer(redisKey);
                buff.setField("data", data);
                buff.post();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            handle.close();
        }
    }

    private String processOne(int symbol) {
        double p_new = 0;
        double rose = 0;
        MarketData data = new MarketData();
        // MyQuery vms = new MyQuery(handle);
        // vms.add("select fid,state from %s mp", AppDB.Ftrademapping);
        // vms.add(" where mp.fid = %d", symbol);
        // vms.open();
        // int state = vms.getInt("state");
        // String tableName = state == 0 ? "fperiod" : "fzbkline";
        // 卖价
        MyQuery ds = new MyQuery(handle);
        // if (state == 0) {
            ds.add("select ft.fPrize,sum(ft.fleftCount) fCount from %s ft ", "fentrust");
            ds.add("inner join %s fm on ft.ftrademapping=fm.fid", "ftrademapping");
            // ds.add("where fm.fid=%s and ft.fEntrustType=1 and fcreatetime>=curdate() -1",
            // symbol);
            ds.add("where fm.fid=%s and ft.fEntrustType=1", symbol);
            ds.add("and ft.fStatus in(1,2) ");
            ds.add("group by ft.fPrize ");
            ds.add("order by ft.fPrize asc");
       // } else {
          //  ds.add("select ft.fSellPrice fPrize,sum(ft.fSellAmount) fCount from %s ft", "fzbdepth");
            //ds.add("inner join %s fm on ft.ftrademapping=fm.fid", "ftrademapping");
            //ds.add("where fm.fid = %s and ft.fEntrustType=1", symbol);
            //ds.add("group by fPrize ");
            //ds.add("order by fPrize asc");
        //}
        ds.setMaximum(20);
        ds.open();
        int i = 1;
        while (ds.fetch()) {
            MarketItem item = new MarketItem(i, ds.getDouble("fPrize"), ds.getDouble("fCount"));
            data.getSells().add(item);
            i++;
        }

        // 买价
        MyQuery ds1 = new MyQuery(handle);
        // if (state == 0) {
            ds1.add("select ft.fPrize,sum(ft.fleftCount) fCount from %s ft ", "fentrust");
            ds1.add("inner join %s fm on ft.ftrademapping=fm.fid", "ftrademapping");
            ds1.add("where fm.fid=%s and ft.fEntrustType=0", symbol);
            ds1.add("and ft.fStatus in(1,2) ");
            ds1.add("group by ft.fPrize ");
            ds1.add("order by ft.fPrize desc");
        // } else {
        // ds1.add("select ft.fBuyPrice fPrize,sum(ft.fBuyAmount) fCount from %s ft",
        // "fzbdepth");
        // ds1.add("inner join %s fm on ft.ftrademapping=fm.fid", "ftrademapping");
        // ds1.add("where fm.fid = %s and ft.fEntrustType=0", symbol);
        // ds1.add("group by fPrize ");
        // ds1.add("order by fPrize desc");
        // }
        ds1.setMaximum(20);
        ds1.open();
        int j = 1;
        while (ds1.fetch()) {
            MarketItem item = new MarketItem(j, ds1.getDouble("fPrize"), ds1.getDouble("fCount"));
            data.getBuys().add(item);
            j++;
        }
        MyQuery cdsTrades = new MyQuery(handle);
        // if (state == 0) {
            cdsTrades.add("select distinct log.fid,log.fPrize,log.fCount,log.fAmount,log.fCreateTime from %s log",
                    "fentrustlog");
            cdsTrades.add("inner join %s map on log.ftrademapping=map.fid", "ftrademapping");
            cdsTrades.add("where map.fid=%d", symbol);
            cdsTrades.add("and log.fEntrustType = %d", 1);
            cdsTrades.add("order by log.fCreateTime desc");
        // } else {
        // cdsTrades.add(
        // "select distinct log.fid,log.fPrize,log.fAmount fCount,log.fCreateTime from
        // %s log ",
        // "fzb_trades");
        // cdsTrades.add("inner join %s map on log.ftrademapping=map.fid",
        // "ftrademapping");
        // cdsTrades.add("where map.fid = %d", symbol);
        // cdsTrades.add("order by log.fCreateTime desc");
        // }
        cdsTrades.setMaximum(100);
        cdsTrades.open();

        while (cdsTrades.fetch()) {
            MarketItem2 item = new MarketItem2(cdsTrades.getInt("fid"), cdsTrades.getDouble("fPrize"),
                    cdsTrades.getDouble("fCount"), cdsTrades.getDateTime("fCreateTime").getTime());
            data.getTrades().add(item);
        }

        MyQuery ds2 = new MyQuery(handle);
        ds2.add("select max(pd.fgao) fgao,max(pd.fshou) as fshou,max(pd.fliang) as fliang,min(pd.fdi) as fdi,max(pd.fkai) as fkai,ty1.fcurrentCNY as fny from %s pd",
                AppDB.fperiod);
        ds2.add(" inner join %s mp on pd.ftrademapping = mp.fid", AppDB.Ftrademapping);
        ds2.add(" inner join %s ty1 on mp.fvirtualcointype1 = ty1.fId", AppDB.fvirtualcointype);
        ds2.add(" where mp.fid = '%s'", symbol);
        ds2.open();
        int k = 1;
        while (ds2.fetch()) {
            MarketItem item = new MarketItem(k, ds2.getDouble("fshou"), ds2.getDouble("fliang"));
            data.getPricenews().add(item);
            k++;
        }
        ds2.first();
        // 24h涨跌
        MyQuery ds3 = new MyQuery(handle);
        ds3.add("select (lastprice.fshou-firstprice.fkai)/firstprice.fkai AS fchangerate,lastprice.fshou "
                + "from (select fshou from fperiod "
                + "where ftrademapping=%d and ftime>=date_sub(now(),interval 1 day) "
                + "and ftime< now() order by fid desc limit 1) lastprice,"
                + "(select fkai from fperiod where ftrademapping=%d and ftime>=date_sub(now(),interval 1 day) "
                + "and ftime< now() order by fid limit 1) firstprice", symbol, symbol);
        ds3.open();
        MyQuery ds5 = new MyQuery(handle);
        ds5.add("select distinct log.fid,log.fPrize,log.fCount,log.fAmount,log.fCreateTime from %s log", "fentrustlog");
        ds5.add("inner join %s map on log.ftrademapping=map.fid", "ftrademapping");
        ds5.add("where map.fid=%d", symbol);
        ds5.add("and log.fEntrustType = %d", 1);
        ds5.add("order by log.fCreateTime desc");
        ds5.setMaximum(1);
        ds5.open();
        double fPrize = 0L;
        if (!ds5.eof()) {
            fPrize = ds5.getDouble("fPrize");
        }
        if (!ds3.eof()) {
            rose = ((fPrize - ds3.getDouble("fshou")) / fPrize) * 100;
            // rose = ds3.getDouble("fchangerate") * 100;
        }

        // 买价
        MyQuery ds4 = new MyQuery(handle);

        ds4.add("select lastprice.fshou as fshou,firstprice.fkai as fkai from (select fshou from %s ", AppDB.fperiod);
        //if (state == 0) {
            ds4.add("where ftrademapping =%d and ftime >= date_sub(now(),interval 1 day) ", symbol);
            ds4.add("and ftime< now() order by fid desc limit 1) lastprice,");
            ds4.add(" (select fkai from %s where ftrademapping = %d and ftime >=date_sub(now(),interval 1 day ) ",
                AppDB.fperiod, symbol);
            ds4.add(" and ftime < now() order by fid limit 1) firstprice");
        //} else {
          //  ds4.add("where ftrademapping =%d and fCreateTime >= date_sub(now(),interval 1 day) ", symbol);
            //ds4.add("and fCreateTime< now() order by fid desc limit 1) lastprice,");
            //ds4.add(" (select fkai from %s where ftrademapping = %d and fCreateTime >=date_sub(now(),interval 1 day ) ",
              //      tableName, symbol);
            //ds4.add(" and fCreateTime < now() order by fid limit 1) firstprice");
        // }
        ds4.open();
        if (!ds4.eof()) {
            p_new = ds4.getDouble("fshou");
        }
        data.setP_new(p_new);
        data.setRose(Math.round(rose * 10000) / 10000.0);
        return JSONObject.fromObject(data).toString();
    }
}