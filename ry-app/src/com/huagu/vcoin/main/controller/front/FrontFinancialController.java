package com.huagu.vcoin.main.controller.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.huagu.vcoin.main.Enum.BankTypeEnum;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.Enum.VirtualCoinTypeStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.FbankinfoWithdraw;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.FvirtualaddressWithdraw;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;
import com.huagu.vcoin.main.service.front.UtilsService;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.TDateTime;
import net.sf.json.JSONObject;

@Controller
public class FrontFinancialController extends BaseController {
    private static Logger log = Logger.getLogger(FrontFinancialController.class);

    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UtilsService utilsService;

    @RequestMapping(value = "/m/financial/index")
    public ModelAndView AppIndex(HttpServletRequest request, @RequestParam(required = false) String menuFlag) {
        request.getSession().setAttribute("menuFlag", menuFlag);
        JspPage modelAndView = new JspPage(request);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        modelAndView.addObject("fuser", fuser);
        modelAndView.setJspFile("front/financial/index2");
        return modelAndView;
    }

    @RequestMapping(value = "/financial/index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(required = false) String menuFlag)
            throws Exception {
        request.getSession().setAttribute("menuFlag", menuFlag);
        JspPage modelAndView = new JspPage(request);

        // 用户钱包
        Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(GetCurrentUser(request).getFid());
        modelAndView.addObject("fwallet", fwallet);
        // 虚拟钱包
        Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                .findVirtualWallet(GetCurrentUser(request).getFid());
        modelAndView.addObject("fvirtualwallets", fvirtualwallets);
        // 估计总资产
        // CNY+冻结CNY+（币种+冻结币种）*最高买价
        double totalCapital = 0F;
        // 币种、钱包 数据校验 负数置为0
        double fTotal1 = fwallet.getFtotal() < 0 ? 0 : fwallet.getFtotal();
        double fFrozen1 = fwallet.getFfrozen() < 0 ? 0 : fwallet.getFfrozen();
        totalCapital += fFrozen1 + fTotal1;
        List<Map<String, Double>> list = new ArrayList<>();
        Map<String, Double> map1 = new HashMap<>();
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0, "where fstatus=? order by fid asc", false,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);
        for (Ftrademapping ftrademapping : ftrademappings) {
            Map<String, Double> map = new HashMap<>();
            Double tmp = (Double) this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping.getFid()));
            double price = tmp != null ? tmp : 0;
            String fid = "";
            try (Mysql mysql = new Mysql()) {
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select max(pd.fgao) as fgao, max(pd.fshou) as fshou,max(pd.fliang) as fliang,min(pd.fdi) as fdi,max(pd.fkai) as fkai,ty1.fcurrentCNY as fny ,mp.fvirtualcointype2 as fvirtualcointype2,mp.fvirtualcointype1 as fvirtualcointype1,ty1.fName as fName from %s pd",
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
                if(!cds.eof()) {
                    fshou = cds.getDouble("fshou");
                }
                try (Mysql mysql1 = new Mysql()) {
                    MyQuery ds = new MyQuery(mysql1);
                    ds.add("select fId,fName" + " from %s ", AppDB.fvirtualcointype);
                    ds.add("where fName = '%s'", "比特币");
                    ds.open();
                    fid = ds.getString("fId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if ("比特币".equals(ds1.getString("fName"))) {
                    map.put(ds1.getString("fvirtualcointype2"), fshou);
                    map1.put("fny", ds1.getDouble("fny"));
                    list.add(map);
                }
                if (fshou != 0) {
                    if (fid.equals(ds1.getString("fvirtualcointype2"))) {
                        map.put(ds1.getString("fvirtualcointype1"), (1 / (fshou)));
                        list.add(map);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<Integer, Integer> tradeMappings = (Map) this.constantMap.get("tradeMappings");
        for (Map.Entry<Integer, Fvirtualwallet> entry : fvirtualwallets.entrySet()) {
            if (entry.getValue().getFvirtualcointype().getFtype() == CoinTypeEnum.FB_CNY_VALUE)
                continue;
            try {
                Fvirtualwallet wallet = entry.getValue();
                Fvirtualcointype coinType = wallet.getFvirtualcointype();
                Integer tradeMappingId = tradeMappings.get(coinType.getFid());
                if (tradeMappingId == null) {
                    //FIXME: 此处发生频率太高，须修正 2018/5/18
                    log.warn("tradeMappingId is null");
                }
                // String redisKey =
                // RedisConstant.getLatestDealPrizeKey(tradeMappingId);
                // double latestDealPrize = (Double)
                // this.redisUtil.get(redisKey);
                // totalCapital += (wallet.getFfrozen() + wallet.getFtotal()) *
                // latestDealPrize;
                for (Map<String, Double> arr : list) {
                    for (String in : arr.keySet()) {
                        if (Integer.parseInt(in) == (coinType.getFid())) {
                            // 币种、钱包 数据校验 负数置为0
                            double fFrozen2 = wallet.getFfrozen() < 0 ? 0 : wallet.getFfrozen();
                            double fTotal2 = wallet.getFtotal() < 0 ? 0 : wallet.getFtotal();
                            totalCapital += (fFrozen2 + fTotal2) * arr.get(in);
                        }
                    }
                }
                if ("比特币".equals(coinType.getFname())) {
                    // 币种、钱包 数据校验 负数置为0
                    double fFrozen3 = wallet.getFfrozen() < 0 ? 0 : wallet.getFfrozen();
                    double fTotal3 = wallet.getFtotal() < 0 ? 0 : wallet.getFtotal();
                    totalCapital += (fFrozen3 + fTotal3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double exchangerate = map1.get("fny") * totalCapital;
        modelAndView.addObject("totalCapitalTrade", Utils.getDouble(totalCapital, 6));
        modelAndView.addObject("exchangerate", Utils.getDouble(exchangerate, 6));
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        modelAndView.addObject("fuser", fuser);

        modelAndView.setJspFile("front/financial/index");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = { "/financial/indexs" }, produces = { JsonEncode })
    public String indexs(HttpServletRequest request, @RequestParam(required = false) String menuFlag,
            @RequestParam(required = false) String lasts, @RequestParam(required = false) String areas)
            throws Exception {
        request.getSession().setAttribute("menuFlag", menuFlag);
        JspPage modelAndView = new JspPage(request);
        String lastRMB = modelAndView.getRequest().getParameter("last");
        String arsa = "BTC";// modelAndView.getRequest().getParameter("area");
        try {
            Mysql handle = new Mysql();
            MyQuery dsUser = new MyQuery(handle);
            dsUser.add("select * from %s where fShortName='%s'", "fvirtualcointype", arsa);
            dsUser.open();

            dsUser.edit();
            dsUser.setField("fcurrentCNY", lastRMB);
            dsUser.post();

        } catch (Exception e) {
            log.info(e.getMessage());
            System.out.println(e);
        }

        // 用户钱包
        Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(GetCurrentUser(request).getFid());
        modelAndView.addObject("fwallet", fwallet);
        // 虚拟钱包
        Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                .findVirtualWallet(GetCurrentUser(request).getFid());
        modelAndView.addObject("fvirtualwallets", fvirtualwallets);
        // 估计总资产
        // CNY+冻结CNY+（币种+冻结币种）*最高买价
        double totalCapital = 0F;
        totalCapital += fwallet.getFtotal() + fwallet.getFfrozen();
        List<Map<String, Double>> list = new ArrayList<>();
        Map<String, Double> map1 = new HashMap<>();
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0, "where fstatus=? order by fid asc", false,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);
        for (Ftrademapping ftrademapping : ftrademappings) {
            Map<String, Double> map = new HashMap<>();
            Double tmp = (Double) this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping.getFid()));
            double price = tmp != null ? tmp : 0;
            try (Mysql mysql = new Mysql()) {
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select max(pd.fgao) as fgao, max(pd.fshou) as fshou,max(pd.fliang) as fliang,min(pd.fdi) as fdi,max(pd.fkai) as fkai,ty1.fcurrentCNY as fny ,mp.fvirtualcointype2 as fvirtualcointype2,ty1.fName as fName from %s pd",
                        "fperiod");
                ds1.add("inner join ftrademapping mp on pd.ftrademapping=mp.fid");
                ds1.add("inner join fvirtualcointype ty1 on mp.fvirtualcointype1 = ty1.fId");
                ds1.add("where mp.fid = %d", ftrademapping.getFid());
                ds1.open();
                if ("比特币".equals(ds1.getString("fName"))) {
                    map.put(ds1.getString("fvirtualcointype2"), ds1.getDouble("fshou"));
                    map1.put("fny", ds1.getDouble("fny"));
                    list.add(map);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        Map<Integer, Integer> tradeMappings = (Map) this.constantMap.get("tradeMappings");
        for (Map.Entry<Integer, Fvirtualwallet> entry : fvirtualwallets.entrySet()) {
            if (entry.getValue().getFvirtualcointype().getFtype() == CoinTypeEnum.FB_CNY_VALUE)
                continue;
            try {
                Fvirtualwallet wallet = entry.getValue();
                Fvirtualcointype coinType = wallet.getFvirtualcointype();
                Integer tradeMappingId = tradeMappings.get(coinType.getFid());
                if (tradeMappingId == null)
                    log.error("tradeMappingId is null");
                // String redisKey =
                // RedisConstant.getLatestDealPrizeKey(tradeMappingId);
                // double latestDealPrize = (Double)
                // this.redisUtil.get(redisKey);
                // totalCapital += (wallet.getFfrozen() + wallet.getFtotal()) *
                // latestDealPrize;
                for (Map<String, Double> arr : list) {
                    for (String in : arr.keySet()) {
                        if (Integer.parseInt(in) == (coinType.getFid())) {
                            totalCapital += (wallet.getFfrozen() + wallet.getFtotal()) * arr.get(in);
                        }
                    }
                }
                if ("比特币".equals(coinType.getFname())) {
                    totalCapital += (wallet.getFfrozen() + wallet.getFtotal());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double exchangerate = map1.get("fny") * totalCapital;
        modelAndView.addObject("totalCapitalTrade", Utils.getDouble(totalCapital, 6));
        modelAndView.addObject("exchangerate", Utils.getDouble(exchangerate, 6));
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        modelAndView.addObject("fuser", fuser);

        JSONObject jsonObject = new JSONObject();

        jsonObject.accumulate("exchangerate", Utils.getDouble(exchangerate, 6));

        return jsonObject.toString();
    }

    @RequestMapping("/financial/accountbank")
    public ModelAndView accountbank(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        modelAndView.addObject("fuser", fuser);

        Map<Integer, String> bankTypes = new HashMap<Integer, String>();
        for (int i = 1; i <= BankTypeEnum.QT; i++) {
            if (BankTypeEnum.getEnumString(i) != null && BankTypeEnum.getEnumString(i).trim().length() > 0) {
                bankTypes.put(i, BankTypeEnum.getEnumString(i));
            }
        }
        modelAndView.addObject("bankTypes", bankTypes);

        String filter = "where fuser.fid=" + fuser.getFid() + " and fbankType >0";
        List<FbankinfoWithdraw> bankinfos = this.frontUserService.findFbankinfoWithdrawByFuser(0, 0, filter, false);
        for (FbankinfoWithdraw fbankinfoWithdraw : bankinfos) {
            try {
                int length = fbankinfoWithdraw.getFbankNumber().length();
                String number = "**** **** **** " + fbankinfoWithdraw.getFbankNumber().substring(length - 4, length);
                fbankinfoWithdraw.setFbankNumber(number);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        modelAndView.addObject("bankinfos", bankinfos);

        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("isBindTelephone", isBindTelephone);

        modelAndView.setJspFile("front/financial/accountbank0");
        return modelAndView;
    }
    /*
     * @RequestMapping("/financial/accountalipay") public ModelAndView
     * accountalipay( HttpServletRequest request ) throws Exception{
     * ModelAndView modelAndView = new ModelAndView() ; Fuser fuser =
     * this.frontUserService.findById(GetSession(request).getFid()) ;
     * modelAndView.addObject("fuser", fuser) ;
     * 
     * String filter = "where fuser.fid="+fuser.getFid()+" and fbankType =0";
     * List<FbankinfoWithdraw> bankinfos =
     * this.frontUserService.findFbankinfoWithdrawByFuser(0, 0, filter, false);
     * for (FbankinfoWithdraw fbankinfoWithdraw : bankinfos) { try { int length
     * = fbankinfoWithdraw.getFbankNumber().length(); String number =
     * fbankinfoWithdraw.getFbankNumber().substring(0,3)+"****"+
     * fbankinfoWithdraw. getFbankNumber().substring(length-4,length);
     * fbankinfoWithdraw.setFbankNumber(number); } catch (Exception e) {} }
     * modelAndView.addObject("bankinfos", bankinfos) ;
     * 
     * boolean isBindGoogle = fuser.getFgoogleBind() ; boolean isBindTelephone =
     * fuser.isFisTelephoneBind() ; modelAndView.addObject("isBindGoogle",
     * isBindGoogle) ; modelAndView.addObject("isBindTelephone",
     * isBindTelephone) ;
     * 
     * 
     * modelAndView.setViewName("front"+Mobilutils.M(request)+
     * "/financial/accountalipay") ; return modelAndView ; }
     */

    @RequestMapping("/financial/accountcoin")
    public ModelAndView accountcoin(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int symbol) throws Exception {
        JspPage modelAndView = new JspPage(request);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal
                || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE || !fvirtualcointype.isFIsWithDraw()) {
            String filter = "where fstatus=1 and FIsWithDraw=1 and ftype <>" + CoinTypeEnum.FB_CNY_VALUE
                    + " order by fid asc";
            List<Fvirtualcointype> alls = this.virtualCoinService.list(0, 1, filter, true);
            if (alls == null || alls.size() == 0) {
                modelAndView.setViewName("redirect:/");
                return modelAndView;
            }
            fvirtualcointype = alls.get(0);
        }
        symbol = fvirtualcointype.getFid();
        String coinName = fvirtualcointype.getfShortName();

        String filter = "where fuser.fid=" + fuser.getFid() + " and fvirtualcointype.fid=" + symbol;
        List<FvirtualaddressWithdraw> alls = this.frontVirtualCoinService.findFvirtualaddressWithdraws(0, 0, filter,
                false);
        modelAndView.addObject("alls", alls);

        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("isBindTelephone", isBindTelephone);

        modelAndView.addObject("fuser", fuser);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("coinName", coinName);
        modelAndView.setJspFile("front/financial/accountcoin");
        return modelAndView;
    }
}
