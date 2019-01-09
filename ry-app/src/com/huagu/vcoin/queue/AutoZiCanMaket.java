package com.huagu.vcoin.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.front.FrontUserJsonController;
import com.huagu.vcoin.main.controller.front.data.ZiCanData;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.UtilsService;

import cn.cerc.jdb.cache.Buffer;
import cn.cerc.jdb.core.TDateTime;
import net.sf.json.JSONObject;

@Controller
@Component
public class AutoZiCanMaket extends BaseController implements Runnable {
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Scheduled(fixedRate = 1500)
    public void run() {
        String redisKey = RedisConstant.getAllZiCanAccount();
        String data = QueryAllZiCan();
        Buffer buffer = new Buffer(redisKey);
        buffer.setField("ZiCanData", data);
        buffer.post();
    }

    public String QueryAllZiCan() {
        ZiCanData ziCan = new ZiCanData();
        List<Map<String, Double>> list = new ArrayList<>();
        Map<String, Double> map1 = new HashMap<>();
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0, "where fstatus=? order by fid asc", false,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);
        for(Ftrademapping ftrademapping :ftrademappings){
            Map<String,Double> map = new HashMap<>();
            Double tmp = (Double) this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping.getFid()));
            double price = tmp != null ? tmp : 0;
            String fid = "";
            try (Mysql mysql = new Mysql()) {
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select max(pd.fgao) as fgao,max(pd.fshou) as fshou,max(pd.fliang) as fliang,min(pd.fdi) as fdi,max(pd.fkai) as fkai,ty1.fcurrentCNY as fny,mp.fvirtualcointype2 as fvirtualcointype2,mp.fvirtualcointype1 as fvirtualcointype1,ty1.fName as fName from %s pd",
                        "fperiod");
                ds1.add("inner join ftrademapping mp on pd.ftrademapping=mp.fid");
                ds1.add("inner join fvirtualcointype ty1 on mp.fvirtualcointype1 = ty1.fId");
                ds1.add("where mp.fid = %d", ftrademapping.getFid());
                ds1.open();
                MyQuery cds = new MyQuery(mysql);
                cds.add("select lastprice.fshou as fshou,firstprice.fkai as fkai");
                cds.add("from");
                cds.add("(select fshou from %s where ftrademapping=%d", AppDB.fperiod, ftrademapping.getFid());
                cds.add("and ftime between '%s' and '%s' order by fid desc limit 1) lastprice,",
                        TDateTime.Now().incDay(-1), TDateTime.Now());
                cds.add("(select fkai from %s where ftrademapping=%d", AppDB.fperiod, ftrademapping.getFid());
                cds.add("and ftime between '%s' and '%s' order by fid limit 1) firstprice", TDateTime.Now().incDay(-1),
                        TDateTime.Now());
                cds.open();
                double fshou = ds1.getDouble("fshou");
                if (!cds.eof()) {
                    fshou = cds.getDouble("fshou");
                }
                MyQuery ds = new MyQuery(mysql);
                ds.add("select fId,fName from %s", AppDB.fvirtualcointype);
                ds.add(" where fName = '%s'", "比特币");
                ds.open();
                if (!ds.eof()) {
                    fid = ds.getString("fId");
                }
                if ("比特币".equals(ds1.getString("fName"))) {
                    map.put(ds1.getString("fvirtualcointype2"), fshou);
                    map1.put("fny", ds1.getDouble("fny"));
                    list.add(map);
                }
                if(fshou != 0){
                    if (fid.equals(ds1.getString("fvirtualcointype2"))) {
                        map.put(ds1.getString("fvirtualcointype1"), (1 / fshou));
                        list.add(map);
                    }
                }
                double fny = 0;
                if (map1.get("fny") == null) {
                    fny = Double.parseDouble(new FrontUserJsonController().getBTCPrice());
                } else {
                    fny = map1.get("fny");
                }
                ziCan.setFny(fny);
                ziCan.setZiCans(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONObject.fromObject(ziCan).toString();
    }

}
