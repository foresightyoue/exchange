package com.huagu.vcoin.main.comm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.LinkTypeEnum;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.model.Farticle;
import com.huagu.vcoin.main.model.Ffriendlink;
import com.huagu.vcoin.main.model.Ftradehistory;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.EntrustlogService;
import com.huagu.vcoin.main.service.admin.FriendLinkService;
import com.huagu.vcoin.main.service.admin.TradeMappingService;
import com.huagu.vcoin.main.service.admin.TradehistoryService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSubscriptor;
import com.huagu.vcoin.main.service.front.FrontOthersService;
import com.huagu.vcoin.main.service.front.FrontSystemArgsService;
import com.huagu.vcoin.main.service.front.UtilsService;

import redis.clients.jedis.JedisPubSub;

public class ConstantMap implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ConstantMap.class);
    @Autowired
    private FrontSystemArgsService frontSystemArgsService;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private FriendLinkService friendLinkService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private TradehistoryService tradehistoryService;
    @Autowired
    private FrontOthersService frontOtherService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private TradeMappingService tradeMappingService;
    @Autowired
    private EntrustlogService entrustlogService;
    @Autowired
    private MessageSubscriptor messageSubscriptor;
    @Autowired
    private JedisPubSub jedisPubSub;

    private Map<String, Object> map = new HashMap<String, Object>();

    public void init() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                messageSubscriptor.subscribe(jedisPubSub, ChannelConstant.names);
            }
        }).start();

        SystemArgs();
        virtualCoinType();
        allWithdrawCoins();
        allRechargeCoins();
        ffriendlinks();
        quns();
        webinfo();
        tradehistory();
        news();
        fbs();
        tradeMappings();
        ftradehistory7D();
        totalActUser();
        totalActAmt();
        initValue();
        initValue1();
    }
    
    public void initValue() {
        this.put("VirtualCoinWithdrawTimes", 10);
        this.put("maxwithdrawbtc", 100);
        this.put("minwithdrawbtc", 0.00000001);
    }

    public void initValue1() {
        this.put("VirtualCoinWithdrawTimes", 10);
        this.put("maxwithdrawat", 1000000);
        this.put("minwithdrawat", 100);
    }

    public void SystemArgs() {
        Map<String, String> tMap = this.frontSystemArgsService.findAllMap();
        for (Map.Entry<String, String> entry : tMap.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void virtualCoinType() {
        List<Fvirtualcointype> fvirtualcointypes = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        map.put("virtualCoinType", fvirtualcointypes);
    }

    public void allWithdrawCoins() {
        String filter = "where fstatus=1 and FIsWithDraw=1 and ftype <>" + CoinTypeEnum.FB_CNY_VALUE;
        List<Fvirtualcointype> allWithdrawCoins = this.virtualCoinService.list(0, 0, filter, false);
        map.put("allWithdrawCoins", allWithdrawCoins);
    }

    public void allRechargeCoins() {
        String filter = "where fstatus=1 and fisrecharge=1 and ftype <>" + CoinTypeEnum.FB_CNY_VALUE;
        List<Fvirtualcointype> allRechargeCoins = this.virtualCoinService.list(0, 0, filter, false);
        map.put("allRechargeCoins", allRechargeCoins);
    }

    public void ffriendlinks() {
        String filter = "where ftype=" + LinkTypeEnum.LINK_VALUE + " order by forder asc";
        List<Ffriendlink> ffriendlinks = this.friendLinkService.list(0, 0, filter, false);
        map.put("ffriendlinks", ffriendlinks);
    }

    public void quns() {
        String filter = "where ftype=" + LinkTypeEnum.QQ_VALUE + " order by forder asc";
        List<Ffriendlink> ffriendlinks = this.friendLinkService.list(0, 0, filter, false);
        map.put("quns", ffriendlinks);
    }

    public void webinfo() {
        map.put("webinfo", this.frontSystemArgsService.findFwebbaseinfoById(1));
    }

    public void tradehistory() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String key = sdf.format(c.getTime());
        // String filter = "where DATE_FORMAT(fdate,'%Y-%m-%d') ='" + key + "'";
        String filter = "where fdate >= curdate()";
        try {
            List<Ftradehistory> ftradehistorys = this.tradehistoryService.list(0, 0, filter, false);
            map.put("tradehistory", ftradehistorys);
        } catch (Exception e) {
            log.error("系统启动时，初始化出错, filter: " + filter);
            e.printStackTrace();
        }
    }

    public void news() {
        List<Farticle> farticles = this.frontOtherService.findFarticle(1, 0, 1);
        if (farticles != null && farticles.size() > 0) {
            map.put("news", farticles);
        }
    }

    public void fbs() {
        String filter = " select distinct a.fvirtualcointypeByFvirtualcointype1 from  Ftrademapping a where a.fstatus=? order by a.fvirtualcointypeByFvirtualcointype1.ftype asc";
        List<Fvirtualcointype> fbs = this.utilsService.findHQL(0, 0, filter, false, Ftrademapping.class,
                TrademappingStatusEnum.ACTIVE);
        map.put("fbs", fbs);
    }

    public void tradeMappings() {
        Map<Integer, Integer> tradeMappings = new HashMap<Integer, Integer>();
        String sql1 = "where fstatus=" + TrademappingStatusEnum.ACTIVE;
        List<Ftrademapping> mappings = this.tradeMappingService.list(0, 0, sql1, false);
        for (Ftrademapping ftrademapping : mappings) {
            tradeMappings.put(ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFid(), ftrademapping.getFid());
        }
        map.put("tradeMappings", tradeMappings);
        map.put("tradeMappingss", mappings);
    }

    public void ftradehistory7D() {

        List<Ftrademapping> all = this.tradeMappingService.list(0, 0,
                " where fstatus=" + TrademappingStatusEnum.ACTIVE + " ", false);
        List<Ftradehistory> ftradehistory7D = new ArrayList<Ftradehistory>();
        for (Ftrademapping ftrademapping : all) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -7);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String key = sdf.format(c.getTime());
            String xx = "where fdate>=curdate()-7 and fdate<curdate()-6 and fvid=" + ftrademapping.getFid();
            List<Ftradehistory> ftradehistorys = this.tradehistoryService.list(0, 1, xx, true);
            if (ftradehistorys != null && ftradehistorys.size() > 0) {
                ftradehistory7D.add(ftradehistorys.get(0));
            }
        }
        map.put("ftradehistory7D", ftradehistory7D);
    }

    public void totalActUser() {
        map.put("totalActUser", this.adminService.getAllCount("Fuser", "where fstatus=1"));
    }

    public void totalActAmt() {
        map.put("totalActAmt", this.entrustlogService.getTotalTradeAmt().intValue());
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public synchronized void put(String key, Object value) {
        log.info("ConstantMap put key:" + key + ",value:" + value + ".");
        map.put(key, value);
    }

    public Object get(String key) {
        Object result = map.get(key);
        if (result == null) {
            if ("minrechargecny".equals(key)) { // 最小充值金额
                result = new Double(0);
            }
        }
        return result;
    }

    public String getString(String key) {
        return (String) map.get(key);
    }
}
