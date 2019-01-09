package com.huagu.vcoin.main.controller.front;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.*;
import com.huagu.vcoin.main.model.*;
import com.huagu.vcoin.main.service.front.*;
import com.huagu.vcoin.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.comm.ValidateMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.PoolService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.admin.VirtualaddressService;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.sms.ShortMessageService;

import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;
import cn.cerc.jdb.other.utils;
import cn.cerc.security.sapi.JayunSecurity;
import net.sf.json.JSONObject;

@Controller
public class FrontAccountJsonController extends BaseController {
    private static final Logger log = Logger.getLogger(FrontAccountJsonController.class);

    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private FrontBankInfoService frontBankInfoService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private ValidateMap messageValidateMap;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private ConstantMap map;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ValidateMap validateMap;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private PoolService poolService;
    @Autowired
    private VirtualaddressService virtualaddressService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UtilsService utilsService;

    @RequestMapping(value = "/app/account/alipayManual", produces = { JsonEncode })
    @ResponseBody
    public String alipayManual(HttpServletRequest request, @RequestParam(required = true) double money,
            @RequestParam(required = true) double cnymoney, @RequestParam(required = true) int type,
            @RequestParam(required = true) int sbank, @RequestParam(required = true) String uuid) throws Exception {
        JSONObject jsonObject = new JSONObject();
        money = Utils.getDouble(money, 2);
        double minRecharge = Double.parseDouble(this.map.get("minrechargecny").toString());
        if (money < minRecharge) {
            // 非法
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "最小充值金额为$" + minRecharge);
            return jsonObject.toString();
        }

        if (type != 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }

        Systembankinfo systembankinfo = this.frontBankInfoService.findSystembankinfoById(sbank);
        if (systembankinfo == null || systembankinfo.getFstatus() == SystemBankInfoEnum.ABNORMAL) {
            // 银行账号停用
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "银行帐户不存在");
            return jsonObject.toString();
        }

        String filter = "where fuser.fid=" + uuid + " and ftype=1 and fstatus in(1,2)";
        int count = this.adminService.getAllCount("Fcapitaloperation", filter);
        if (count > 0) {
            // 银行账号停用
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "您有未完成的充值记录");
            return jsonObject.toString();
        }

        Fcapitaloperation fcapitaloperation = new Fcapitaloperation();
        fcapitaloperation.setFamount(money);
        fcapitaloperation.setfCnyAmount(cnymoney);
        fcapitaloperation.setSystembankinfo(systembankinfo);
        fcapitaloperation.setFcreateTime(Utils.getTimestamp());
        fcapitaloperation.setFtype(CapitalOperationTypeEnum.RMB_IN);
        fcapitaloperation.setFuser(this.GetCurrentUser(request));
        fcapitaloperation.setFstatus(CapitalOperationInStatus.NoGiven);
        fcapitaloperation.setFremittanceType(systembankinfo.getFbankName());

        this.frontAccountService.addFcapitaloperation(fcapitaloperation);

        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("money", String.valueOf(fcapitaloperation.getFamount()));
        jsonObject.accumulate("tradeId", fcapitaloperation.getFid());
        jsonObject.accumulate("fbankName", systembankinfo.getFbankName());
        jsonObject.accumulate("fownerName", systembankinfo.getFownerName());
        jsonObject.accumulate("fbankAddress", systembankinfo.getFbankAddress());
        jsonObject.accumulate("fbankNumber", systembankinfo.getFbankNumber());
        return jsonObject.toString();
    }

    @RequestMapping("financial/assetsrecord")
    public ModelAndView assetsrecord(HttpServletRequest request,
                                     @RequestParam(required = false, defaultValue = "1") int currentPage) throws Exception {
        JspPage modelAndView = new JspPage(request);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        List<Fvirtualcointype> fvirtualcointypes = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        modelAndView.addObject("fvirtualcointypes", fvirtualcointypes);

        int maxResults = Constant.RecordPerPage;
        int firstResult = PaginUtil.firstResult(currentPage, maxResults);
        String filter = " where fuser.fid=" + fuser.getFid() + " and status=1 order by fid desc ";
        List<Fasset> list = this.utilsService.list(firstResult, maxResults, filter, true, Fasset.class);
        int total = this.utilsService.count(filter, Fasset.class);
        String pagin = PaginUtil.generatePagin(PaginUtil.firstResult(currentPage, maxResults), currentPage,
                "/financial/assetsrecord.html?");
        modelAndView.addObject("list", list);
        modelAndView.addObject("pagin", pagin);

        // 处理json
        for (Fasset fasset : list) {
            fasset.parseJson(fvirtualcointypes);
        }

        modelAndView.setJspFile("front/financial/assetsrecord");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = { "/json/fcurrentZiCan", "/m/json/fcurrentzican" }, produces = JsonEncode)
    public String fcurrentZiCan(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if(null == GetCurrentUser(request)) {
            jsonObject.put("status", 300);
            jsonObject.put("msg", "请重新登录");
            return jsonObject.toString();
        }
        // 后台id错误用户钱包
        Map<String, Object> data = new HashMap<>();
        Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(GetCurrentUser(request).getFid());
        // TODO hqs 把CNY单独提出来,不放到可充币和提币的列表里面,CNY只做展示
        data.put("CNY", fwallet.getFtotal() + fwallet.getFfrozen());
        // 虚拟钱包
        Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                .findVirtualWallet(GetCurrentUser(request).getFid());
        // TODO hqs 如果虚拟钱包包含CNY,去除CNY
        if(fvirtualwallets != null && fvirtualwallets.size() > 0){
            Set<Integer> integers = fvirtualwallets.keySet();
            Iterator<Integer> iterar = integers.iterator();
            Integer cnymv = null;
            while (iterar.hasNext()) {
                cnymv = iterar.next();
                if("CNY".equals(fvirtualwallets.get(cnymv).getFvirtualcointype().getfShortName())){
                    fvirtualwallets.remove(cnymv);
                    break;
                }
            }
            Set<Integer> integ = fvirtualwallets.keySet();
            Iterator<Integer> iterator = integ.iterator();
            Integer rybmv = null;
            while (iterator.hasNext()) {
                rybmv = iterator.next();
                if("RYB".equals(fvirtualwallets.get(rybmv).getFvirtualcointype().getfShortName())){
                    fvirtualwallets.remove(rybmv);
                    break;
                }
            }
        }
        // 估计总资产
        // CNY+冻结CNY+（币种+冻结币种）*最高买价
        double totalCapital = 0F;
        // 币种,钱包 数据校验 负数置为0
        double fFrozen1 = fwallet.getFfrozen() < 0 ? 0 : fwallet.getFfrozen();
        double fTotal1 = fwallet.getFtotal() < 0 ? 0 : fwallet.getFtotal();
        totalCapital += fFrozen1 + fTotal1;
        List<Map<String, Double>> list = new ArrayList<>();
        Map<String, Double> map1 = new HashMap<>();
        List<Ftrademapping> ftrademappings = this.utilsService.list1(0, 0, "where fstatus=? order by fid asc", false,
                Ftrademapping.class, TrademappingStatusEnum.ACTIVE);
        for (Ftrademapping ftrademapping : ftrademappings) {
            Map<String, Double> map = new HashMap<>();
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
                if (!cds.eof()) {
                    fshou = cds.getDouble("fshou");
                }
                try (Mysql mysql1 = new Mysql()) {
                    MyQuery ds = new MyQuery(mysql1);
                    ds.add("select fId,fName from %s", AppDB.fvirtualcointype);
                    ds.add(" where fName = '%s'", "比特币");
                    ds.open();
                    if (!ds.eof()) {
                        fid = ds.getString("fId");
                    }
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
                        map.put(ds1.getString("fvirtualcointype2"), (fshou));
                        list.add(map);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        Map<Integer, Integer> tradeMappings = (Map) this.constantMap.get("tradeMappings");
        List<Map<String, Object>> listMoney = new ArrayList<>();
        for (Map.Entry<Integer, Fvirtualwallet> entry : fvirtualwallets.entrySet()) {
            if (entry.getValue().getFvirtualcointype().getFtype() == CoinTypeEnum.FB_CNY_VALUE)
                continue;
            try {
                Fvirtualwallet wallet = entry.getValue();
                Fvirtualcointype coinType = wallet.getFvirtualcointype();
                Map<String, Object> tmpData = new HashMap<>();
                tmpData.put("fid", coinType.getFid());
//                Integer tradeMappingId = tradeMappings.get(coinType.getFid());
//                if (tradeMappingId == null) {
//                    // FIXME: 此处发生频率太高，须修正 2018/5/18
//                    log.warn("tradeMappingId is null");
//                }
                // 币种,钱包 数据校验 负数置为0
                double fFrozen = wallet.getFfrozen() < 0 ? 0 : wallet.getFfrozen();
                double fTotal = wallet.getFtotal() < 0 ? 0 : wallet.getFtotal();
                tmpData.put("ftotal", fTotal);
                tmpData.put("ffrozen", fFrozen);
                listMoney.add(tmpData);
                boolean fromKline = false;
                if(!CollectionUtils.isEmpty(list)) {
                    for (Map<String, Double> arr : list) {
                        for (String in : arr.keySet()) {
                            if (Integer.parseInt(in) == (coinType.getFid())) {
                                fromKline = true;
                                totalCapital += ((fFrozen + fTotal) * arr.get(in));
                            }
                        }
                    }
                }
                if(!fromKline) {
                    if ("比特币".equals(coinType.getFname())) {
                        // 币种 ,钱包数据校验 负数置为0
                        double exchangerate = 0;
                        if (map1.get("fny") != null) {
                            exchangerate = map1.get("fny") * (fFrozen + fTotal);
                        } else {
                            String text = new FrontUserJsonController().getBTCPrice();
                            exchangerate = Double.parseDouble(text) * (fFrozen + fTotal);
                        }
                        totalCapital += exchangerate;
                    }else{
                        totalCapital += ((fFrozen + fTotal) * coinType.getFcurrentCNY());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        data.put("listMoney", listMoney);
        data.put("exchangerate", Utils.getDouble(totalCapital, 6));
        jsonObject.put("status", 200);
        jsonObject.put("data", data);
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/account/rechargeCnySubmit", produces = { JsonEncode })
    public String rechargeCnySubmit(HttpServletRequest request, @RequestParam(required = false) String bank,
            @RequestParam(required = false) String account, @RequestParam(required = false) String payee,
            @RequestParam(required = false) String phone, @RequestParam(required = false) int type,
            @RequestParam(required = true) int desc// 记录的id
    ) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Fcapitaloperation fcapitaloperation = this.frontAccountService.findCapitalOperationById(desc);
        if (fcapitaloperation == null || fcapitaloperation.getFuser().getFid() != GetCurrentUser(request).getFid()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }

        if (fcapitaloperation.getFstatus() != CapitalOperationInStatus.NoGiven
                || fcapitaloperation.getFtype() != CapitalOperationTypeEnum.RMB_IN) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
            return jsonObject.toString();
        }

        // fcapitaloperation.setfBank(bank) ;
        // fcapitaloperation.setfAccount(account) ;
        // fcapitaloperation.setfPayee(payee) ;
        // fcapitaloperation.setfPhone(phone) ;
        fcapitaloperation.setFstatus(CapitalOperationInStatus.WaitForComing);
        fcapitaloperation.setFischarge(false);
        fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());
        try {
            this.frontAccountService.updateCapitalOperation(fcapitaloperation);
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/account/alipayTransfer", produces = { JsonEncode })
    public String alipayTransfer(HttpServletRequest request, @RequestParam(required = true) double money,
            @RequestParam(required = true) String accounts, @RequestParam(required = true) String imageCode,
            @RequestParam(required = true) int type) throws Exception {
        JSONObject jsonObject = new JSONObject();
        accounts = HtmlUtils.htmlEscape(accounts.trim());
        money = Utils.getDouble(money, 2);
        if (type != 3 && type != 4) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }
        if (accounts.length() > 100) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "账号有误");
            return jsonObject.toString();
        }
        double minRecharge = Double.parseDouble(this.map.get("minrechargecny").toString());
        if (money < minRecharge) {
            // 非法
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "最小充值金额为￥" + minRecharge);
            return jsonObject.toString();
        }

        if (vcodeValidate(request, imageCode) == false) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请输入正确的图片验证码");
            return jsonObject.toString();
        }

        // if(type ==3){
        // jsonObject.accumulate("code", 0);
        // jsonObject.accumulate("msg", "操作成功");
        // jsonObject.accumulate("payId", GetSession(request).getFid());
        // return jsonObject.toString();
        // }else{
        Fcapitaloperation fcapitaloperation = new Fcapitaloperation();
        fcapitaloperation.setFuser(GetCurrentUser(request));
        fcapitaloperation.setFamount(money);
        fcapitaloperation.setFremittanceType(RemittanceTypeEnum.getEnumString(type));
        fcapitaloperation.setfBank(RemittanceTypeEnum.getEnumString(type));
        fcapitaloperation.setfAccount(accounts);
        fcapitaloperation.setfPayee(null);
        fcapitaloperation.setfPhone(null);
        fcapitaloperation.setFstatus(CapitalOperationInStatus.WaitForComing);
        fcapitaloperation.setFcreateTime(Utils.getTimestamp());
        fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());
        fcapitaloperation.setFtype(CapitalOperationTypeEnum.RMB_IN);
        fcapitaloperation.setFfees(0d);
        fcapitaloperation.setFischarge(false);
        this.frontAccountService.addFcapitaloperation(fcapitaloperation);
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "操作成功");
        jsonObject.accumulate("payId", fcapitaloperation.getFid());
        return jsonObject.toString();
        // }

    }

    /*
     * var param={tradePwd:tradePwd,withdrawBalance:withdrawBalance,phoneCode:
     * phoneCode, totpCode:totpCode};
     * 
     */
    @ResponseBody
    @RequestMapping("/account/withdrawCnySubmit")
    public String withdrawCnySubmit(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") String tradePwd,
            @RequestParam(required = true) double withdrawBalance,
            @RequestParam(required = false, defaultValue = "0") String phoneCode,
            @RequestParam(required = false, defaultValue = "0") String totpCode,
            @RequestParam(required = true) int withdrawBlank) throws Exception {
        JSONObject jsonObject = new JSONObject();
        // 最大提现人民币
        double max_double = Double.parseDouble(this.map.get("maxwithdrawcny").toString());
        double min_double = Double.parseDouble(this.map.get("minwithdrawcny").toString());

        if (withdrawBalance < min_double) {
            // 提现金额不能小于10
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "提现金额不能小于" + min_double);
            return jsonObject.toString();
        }

        if (withdrawBalance > max_double) {
            // 提现金额不能大于指定值
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "提现金额不能大于" + max_double);
            return jsonObject.toString();
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        int time = this.frontAccountService.getTodayCnyWithdrawTimes(fuser);
        int VirtualCNYWithdrawTimes = Integer.parseInt(this.map.getString("VirtualCNYWithdrawTimes"));
        if (time >= VirtualCNYWithdrawTimes) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "每天允许提现次：" + VirtualCNYWithdrawTimes + "次！");
            return jsonObject.toString();
        }

        Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(fuser.getFid());
        FbankinfoWithdraw fbankinfoWithdraw = this.frontUserService.findByIdWithBankInfos(withdrawBlank);
        if (fbankinfoWithdraw == null || fbankinfoWithdraw.getFuser().getFid() != fuser.getFid()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "提现账号不合法");
            return jsonObject.toString();
        }

        if (fwallet.getFtotal() < withdrawBalance) {
            // 资金不足
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "您的余额不足");
            return jsonObject.toString();
        }

        if (fuser.getFtradePassword() == null) {
            // 没有设置交易密码
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先设置交易密码");
            return jsonObject.toString();
        }

        if (!fuser.getFgoogleBind() && !fuser.isFisTelephoneBind()) {
            // 没有绑定谷歌或者手机
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先绑定GOOGLE验证或者绑定手机号码");
            return jsonObject.toString();
        }

        String ip = getIpAddr(request);
        if (fuser.getFtradePassword() != null) {
            int trade_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD);
            if (trade_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                boolean flag = fuser.getFtradePassword().equals(Utils.MD5(tradePwd, fuser.getSalt()));
                if (!flag) {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "交易密码有误，您还有" + trade_limit + "次机会");
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD);
                    return jsonObject.toString();
                } else if (trade_limit < new Constant().ErrorCountLimit) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TRADE_PASSWORD);
                }
            }
        }

        if (fuser.getFgoogleBind()) {
            int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
            if (google_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                boolean flag = GoogleAuth.auth(Long.parseLong(totpCode.trim()), fuser.getFgoogleAuthenticator());
                if (!flag) {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "谷歌验证码有误，您还有" + google_limit + "次机会");
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                    return jsonObject.toString();
                } else if (google_limit < new Constant().ErrorCountLimit) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
                }
            }
        }

        if (fuser.isFisTelephoneBind()) {
            int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (tel_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                boolean flag = false;
                if (ShortMessageService.isTrue()) {
                    flag = ShortMessageService.CheckCode(fuser.getFtelephone(), phoneCode, new Date());
                } else {
                    JayunSecurity security = new JayunSecurity(request);
                    flag = security.checkVerify(fuser.getFloginName(), phoneCode);// 接入聚安校验短信验证码
                }
                if (!flag) {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "手机验证码有误，您还有" + tel_limit + "机会");
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                    return jsonObject.toString();
                } else if (tel_limit < new Constant().ErrorCountLimit) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                }
            }
        }

        boolean withdraw = false;
        try {
            withdraw = this.frontAccountService.updateWithdrawCNY(withdrawBalance, fuser, fbankinfoWithdraw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.messageValidateMap.removeMessageMap(MessageTypeEnum.getEnumString(MessageTypeEnum.CNY_TIXIAN));
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CNY_TIXIAN);
        }

        if (withdraw) {
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "提现成功");
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/account/withdrawBtcSubmit", produces = { JsonEncode })
    public String withdrawBtcSubmit(HttpServletRequest request, @RequestParam(required = true) int withdrawAddr,
            @RequestParam(required = true) double max_double, @RequestParam(required = true) double min_double,
            @RequestParam(required = true) double withdrawAmount, @RequestParam(required = true) String tradePwd,
            @RequestParam(required = true) double withdrawffee,
            @RequestParam(required = false, defaultValue = "0") String totpCode,
            @RequestParam(required = false, defaultValue = "0") String phoneCode,
            @RequestParam(required = true) int symbol) throws Exception {

        JSONObject jsonObject = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || !fvirtualcointype.isFIsWithDraw()
                || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE
                || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                fvirtualcointype.getFid());
        FvirtualaddressWithdraw fvirtualaddressWithdraw = this.frontVirtualCoinService
                .findFvirtualaddressWithdraw(withdrawAddr);
        if (fvirtualaddressWithdraw == null || fvirtualaddressWithdraw.getFuser().getFid() != fuser.getFid()
                || fvirtualaddressWithdraw.getFvirtualcointype().getFid() != symbol) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }

        if (!fuser.isFisTelephoneBind() && !fuser.getFgoogleBind()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先绑定谷歌验证或者手机号码");
            return jsonObject.toString();
        }

//        int time = this.frontAccountService.getTodayVirtualCoinWithdrawTimes(fuser,fvirtualcointype.getFid());
//        int VirtualCoinWithdrawTimes = 10;
//        try {
            // System.out.print(this.map);
            // VirtualCoinWithdrawTimes =
            // Integer.parseInt(this.map.getString("VirtualCoinWithdrawTimes"));
//        } catch (Exception e1) {
//            log.info("取不到交易限制次数，临时设置为10");
//            e1.printStackTrace();
//        }

//        if (time >= VirtualCoinWithdrawTimes) {
//            jsonObject.accumulate("code", -1);
//            jsonObject.accumulate("msg", "每天允许提现次：" + VirtualCoinWithdrawTimes + "次！");
//            return jsonObject.toString();
//        }

        String ip = getIpAddr(request);
        int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
        int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);

        if (fuser.getFtradePassword() == null) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先设置交易密码");
            return jsonObject.toString();
        } else {
            int trade_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD);
            if (trade_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            }

            if (!fuser.getFtradePassword().equals(MyMD5Utils.encoding(tradePwd))) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "交易密码有误，您还有" + trade_limit + "次机会");
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD);
                return jsonObject.toString();
            } else if (trade_limit < new Constant().ErrorCountLimit) {
                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TRADE_PASSWORD);
            }
        }

        /*
         * double max_double =
         * Double.parseDouble(this.map.get("maxwithdrawbtc").toString()); double
         * min_double = Double.parseDouble(this.map.get("minwithdrawbtc").toString());
         */
//        if (withdrawAmount < min_double) {
//            jsonObject.accumulate("code", -1);
//            jsonObject.accumulate("msg", "最小提现数量为：" + min_double);
//            return jsonObject.toString();
//        }
//
//        if (withdrawAmount > max_double) {
//            jsonObject.accumulate("code", -1);
//            jsonObject.accumulate("msg", "最大提现数量为：" + max_double);
//            return jsonObject.toString();
//        }

        // 余额不足
        Double ffeeSum = withdrawffee + withdrawAmount;
        if (fvirtualwallet.getFtotal() < withdrawAmount) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "您的余额不足");
            return jsonObject.toString();
        }
        if (fvirtualwallet.getFtotal() < ffeeSum) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "您的余额不足");
            return jsonObject.toString();
        }
        String filter = "where fadderess='" + fvirtualaddressWithdraw.getFadderess() + "'";
        int cc = this.adminService.getAllCount("Fvirtualaddress", filter);
        /*
         * if (cc > 0) { jsonObject.accumulate("code", -1); jsonObject.accumulate("msg", "站内会员不允许互转"); return
         * jsonObject.toString(); }
         */

//        if (fuser.getFgoogleBind() && fuser.isFgoogleCheck()) {
//            if (google_limit <= 0) {
//                jsonObject.accumulate("code", -1);
//                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
//                return jsonObject.toString();
//            }
//
//            boolean googleValidate = GoogleAuth.auth(Long.parseLong(totpCode.trim()), fuser.getFgoogleAuthenticator());
//            if (!googleValidate) {
//                jsonObject.accumulate("code", -1);
//                jsonObject.accumulate("msg", "谷歌验证码有误，您还有" + google_limit + "次机会");
//                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
//                return jsonObject.toString();
//            } else if (google_limit < new Constant().ErrorCountLimit) {
//                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
//            }
//        }

        JayunSecurity security = new JayunSecurity(request);
        if ("0".equals(phoneCode) && !security.isSecurity(fuser.getFloginName())) {
            jsonObject.accumulate("code", -50);
            jsonObject.accumulate("msg", security.getMessage());
            return jsonObject.toString();
        }

        /*
         * if (fuser.isFisTelephoneBind() && !"0".equals(phoneCode)) { if (mobil_limit <= 0) {
         * jsonObject.accumulate("code", -1); jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!"); return
         * jsonObject.toString(); }
         * 
         * boolean bool = security.checkVerify(fuser.getFloginName(), phoneCode);// 调用聚安校验 if (!bool) {
         * jsonObject.accumulate("code", -1); jsonObject.accumulate("msg", "手机证码有误，您还有" + mobil_limit + "次机会" +
         * security.getMessage()); this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE); return
         * jsonObject.toString(); } else if (mobil_limit < new Constant().ErrorCountLimit) {
         * this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE); } }
         */

        try (Mysql mysql = new Mysql()) {
            /*
             * Map<Integer, Integer> tradeMappings = (Map)
             * this.constantMap.get("tradeMappings"); Integer ftrademapping_id =
             * tradeMappings.get(fvirtualcointype.getFid()); double curPrice = 0d; if
             * (ftrademapping_id != null &&
             * this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping_id)) !=
             * null) { curPrice = (Double)
             * this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping_id)); }
             * this.frontVirtualCoinService.updateWithdrawBtc(fvirtualaddressWithdraw,
             * fvirtualcointype, fvirtualwallet, withdrawAmount, fuser, curPrice);
             */
            MyQuery queryadress = new MyQuery(mysql);
            queryadress.add("select * from %s where 1=1", "fvirtualaddress_withdraw");
            queryadress.add(" and fId =%s", withdrawAddr);
            queryadress.open();
            MyQuery virtualcapital = new MyQuery(mysql);
            virtualcapital.add(
                    "select FUs_fId2,fVi_fId2,fCreateTime,fAmount,ffees,fType,fStatus,flastUpdateTime,withdraw_virtual_address,version,fconfirmations,fhasOwner from %s ",
                    "fvirtualcaptualoperation");
            virtualcapital.setMaximum(1);
            virtualcapital.open();
            virtualcapital.append();
            virtualcapital.setField("FUs_fId2", fuser.getFid());
            virtualcapital.setField("fVi_fId2", fvirtualcointype.getFid());
            virtualcapital.setField("fCreateTime", TDateTime.Now());
            virtualcapital.setField("fAmount", withdrawAmount);
            virtualcapital.setField("ffees", withdrawffee);
            virtualcapital.setField("fType", 2);
            virtualcapital.setField("fStatus", 1);
            virtualcapital.setField("flastUpdateTime", TDateTime.Now());
            virtualcapital.setField("withdraw_virtual_address", queryadress.getString("fAdderess"));
            virtualcapital.setField("version", 0);
            virtualcapital.setField("fconfirmations", 0);
            virtualcapital.setField("fhasOwner", 0);
            virtualcapital.post();

            MyQuery withdrawFrozen = new MyQuery(mysql);
            withdrawFrozen.add("select * from %s where 1=1", "fvirtualwallet");
            withdrawFrozen.add(" and fuid = %s", fuser.getFid());
            withdrawFrozen.add(" and fVi_fId = %s", fvirtualcointype.getFid());
            withdrawFrozen.open();

            MyQuery feeDetail = new MyQuery(mysql);
            feeDetail.add("select * from %s ", "t_withdrawlog");
            feeDetail.setMaximum(1);
            feeDetail.open();
            feeDetail.append();
            feeDetail.setField("fuser_id", fuser.getFid());
            feeDetail.setField("fcoin_id", fvirtualcointype.getFid());
            feeDetail.setField("ftotal", withdrawFrozen.getDouble("fTotal") - ffeeSum);
            feeDetail.setField("frozen", ffeeSum);
            feeDetail.setField("fwithdraw_count", withdrawAmount);
            feeDetail.setField("ffee_sum", withdrawffee);
            feeDetail.setField("ffee", withdrawffee);
            feeDetail.setField("ffee_time", TDateTime.Now());
            feeDetail.setField("taskId", utils.newGuid());
            feeDetail.setField("fstatus", 1);
            feeDetail.post();

            if (!withdrawFrozen.eof()) {
                withdrawFrozen.edit();
                withdrawFrozen.setField("fTotal", withdrawFrozen.getDouble("fTotal") - ffeeSum);
                withdrawFrozen.setField("fFrozen", withdrawFrozen.getDouble("fFrozen") + ffeeSum);
                withdrawFrozen.setField("fLastUpdateTime", TDateTime.Now());
                withdrawFrozen.post();
            }
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.VIRTUAL_TIXIAN);
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/account/transferBtcSubmit", produces = { JsonEncode })
    public String transferBtcSubmit(HttpServletRequest request, @RequestParam(required = true) String transferAccount,
            @RequestParam(required = true) double withdrawAmount, @RequestParam(required = true) String tradePwd,
            @RequestParam(required = false, defaultValue = "0") String totpCode,
            @RequestParam(required = true) int symbol) throws Exception {

        JSONObject jsonObject = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || !fvirtualcointype.isFIsWithDraw()
                || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE
                || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                fvirtualcointype.getFid());

        if (!fuser.isFisTelephoneBind() && !fuser.getFgoogleBind()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先绑定谷歌验证或者手机号码");
            return jsonObject.toString();
        }

        int time = this.frontAccountService.getTodayVirtualCoinWithdrawTimes(fuser,fvirtualcointype.getFid());
        int VirtualCoinWithdrawTimes = 10;
        try {
            // System.out.print(this.map);
            // VirtualCoinWithdrawTimes =
            // Integer.parseInt(this.map.getString("VirtualCoinWithdrawTimes"));
        } catch (Exception e1) {
            log.info("取不到交易限制次数，临时设置为10");
            e1.printStackTrace();
        }

        if (time >= VirtualCoinWithdrawTimes) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "每天允许提现次：" + VirtualCoinWithdrawTimes + "次！");
            return jsonObject.toString();
        }

        String ip = getIpAddr(request);
        int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);

        if (fuser.getFtradePassword() == null) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先设置交易密码");
            return jsonObject.toString();
        } else {
            int trade_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD);
            if (trade_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            }

            /*
             * if (!fuser.getFtradePassword().equals(Utils.MD5(tradePwd, fuser.getSalt()))) {
             * jsonObject.accumulate("code", -1); jsonObject.accumulate("msg", "交易密码有误，您还有" + trade_limit + "次机会");
             * this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD); return
             * jsonObject.toString(); } else if (trade_limit < new Constant().ErrorCountLimit) {
             * this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TRADE_PASSWORD); }
             */
        }

        double max_double = Double.parseDouble(this.map.get("maxwithdrawat").toString());
        double min_double = Double.parseDouble(this.map.get("minwithdrawat").toString());
        if (withdrawAmount < min_double) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "最小提现数量为：" + min_double);
            return jsonObject.toString();
        }

        if (withdrawAmount > max_double) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "最大提现数量为：" + max_double);
            return jsonObject.toString();
        }

        // 余额不足
        if (fvirtualwallet.getFtotal() < withdrawAmount) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "您的余额不足");
            return jsonObject.toString();
        }

        if (fuser.getFgoogleBind() && fuser.isFgoogleCheck()) {
            if (google_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            }

            boolean googleValidate = GoogleAuth.auth(Long.parseLong(totpCode.trim()), fuser.getFgoogleAuthenticator());
            if (!googleValidate) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "谷歌验证码有误，您还有" + google_limit + "次机会");
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                return jsonObject.toString();
            } else if (google_limit < new Constant().ErrorCountLimit) {
                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
            }
        }

        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            // MyQuery user = new MyQuery(handle);
            // user.add("select * from %s ", "ftransfer");
            // user.add("where fUs_fId2 = %d ", getUserId());
            // user.add("order by fCreateTime desc");
            // user.setMaximum(1);
            // user.open();
            // if (!user.eof()) {
            // transferAccount = user.getString("fAccount");
            // }
            MyQuery ds = new MyQuery(handle);
            String taskId = utils.newGuid();
            ds.add("select * from %s", "ftransfer");
            ds.open();
            ds.append();
            ds.setField("taskId", taskId);
            ds.setField("fUs_fId2", getUserId());
            ds.setField("fAccount", transferAccount);
            ds.setField("fVi_fId2", symbol);
            ds.setField("fStatus", 0);
            ds.setField("fAmount", withdrawAmount);
            ds.setField("fCreateTime", TDateTime.Now());
            ds.setField("flastUpdateTime", TDateTime.Now());
            ds.post();
            ds = new MyQuery(handle);
            ds.add("select * from %s ", "fvirtualwallet");
            ds.add("where fuid='%s' ", getUserId());
            ds.add("and fVi_fId=%d", symbol);
            ds.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            ds.open();
            if (ds.eof()) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "网络异常");
                return jsonObject.toString();
            }
            ds.edit();
            ds.setField("fTotal", ds.getDouble("fTotal") - withdrawAmount);
            ds.setField("fFrozen", ds.getDouble("fFrozen") + withdrawAmount);
            ds.post();

            // 虚拟钱包扣钱
            MyQuery cds = new MyQuery(handle);
            cds.add("select UID_,userId_,coinId_,total_,frozen_,totalChange_,frozenChange_,changeReason_,changeDate_,guid_,taskId_,entrustId_ from %s",
                    AppDB.t_walletlog);
            cds.setMaximum(1);
            cds.open();
            cds.append();
            cds.setField("userId_", getUserId());
            cds.setField("coinId_", symbol);
            cds.setField("total_", ds.getDouble("fTotal"));
            cds.setField("frozen_", ds.getDouble("frozen_"));
            cds.setField("totalChange_", -withdrawAmount);
            cds.setField("frozenChange_", -withdrawAmount);
            cds.setField("changeReason_", "划转钱包");
            cds.setField("changeDate_", TDateTime.Now());
            cds.setField("entrustId_", "0");
            cds.setField("taskId_", taskId);
            cds.post();
            tx.commit();
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.VIRTUAL_TIXIAN);
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/account/transformBtcSubmit", produces = { JsonEncode })
    public String transformBtcSubmit(HttpServletRequest request, @RequestParam(required = true) String transferAccount,
            @RequestParam(required = true) double withdrawAmount, @RequestParam(required = true) String tradePwd,
            @RequestParam(required = false, defaultValue = "0") String totpCode,
            @RequestParam(required = true) int symbol) throws Exception {

        JSONObject jsonObject = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || !fvirtualcointype.isFIsWithDraw()
                || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE
                || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                fvirtualcointype.getFid());

        if (!fuser.isFisTelephoneBind() && !fuser.getFgoogleBind()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先绑定谷歌验证或者手机号码");
            return jsonObject.toString();
        }

        int time = this.frontAccountService.getTodayVirtualCoinWithdrawTimes(fuser,fvirtualcointype.getFid());
        int VirtualCoinWithdrawTimes = 10;
        try {
            // System.out.print(this.map);
            // VirtualCoinWithdrawTimes =
            // Integer.parseInt(this.map.getString("VirtualCoinWithdrawTimes"));
        } catch (Exception e1) {
            log.info("取不到交易限制次数，临时设置为10");
            e1.printStackTrace();
        }

        if (time >= VirtualCoinWithdrawTimes) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "每天允许提现次：" + VirtualCoinWithdrawTimes + "次！");
            return jsonObject.toString();
        }

        String ip = getIpAddr(request);
        int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);

        if (fuser.getFtradePassword() == null) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先设置交易密码");
            return jsonObject.toString();
        } else {
            int trade_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TRADE_PASSWORD);
            if (trade_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            }
        }

        double max_double = Double.parseDouble(this.map.get("maxwithdrawat").toString());
        double min_double = Double.parseDouble(this.map.get("minwithdrawat").toString());
        if (withdrawAmount < 1) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "最小划转数量为：" + 1);
            return jsonObject.toString();
        }

        // if (withdrawAmount > max_double) {
        // jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "最大划转数量为：" + max_double);
        // return jsonObject.toString();
        // }

        // 余额不足
        if (fvirtualwallet.getFtotal() < withdrawAmount) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "您的余额不足");
            return jsonObject.toString();
        }

        if (fuser.getFgoogleBind() && fuser.isFgoogleCheck()) {
            if (google_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            }

            boolean googleValidate = GoogleAuth.auth(Long.parseLong(totpCode.trim()), fuser.getFgoogleAuthenticator());
            if (!googleValidate) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "谷歌验证码有误，您还有" + google_limit + "次机会");
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                return jsonObject.toString();
            } else if (google_limit < new Constant().ErrorCountLimit) {
                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
            }
        }

        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery ds = new MyQuery(handle);
            String taskId = utils.newGuid();
            ds.add("select * from %s", "ftransform");
            ds.open();
            ds.append();
            ds.setField("taskId", taskId);
            ds.setField("fUs_fId2", getUserId());
            ds.setField("fAccount", transferAccount);
            ds.setField("fVi_fId2", symbol);
            ds.setField("fStatus", 0);
            ds.setField("fAmount", withdrawAmount);
            ds.setField("fCreateTime", TDateTime.Now());
            ds.setField("flastUpdateTime", TDateTime.Now());
            ds.post();
            ds = new MyQuery(handle);
            ds.add("select * from %s ", "fvirtualwallet");
            ds.add("where fuid='%s' ", getUserId());
            ds.add("and fVi_fId=%d", symbol);
            ds.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            ds.open();
            if (ds.eof()) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "网络异常");
                return jsonObject.toString();
            }
            ds.edit();
            ds.setField("fTotal", ds.getDouble("fTotal") - withdrawAmount);
            ds.setField("fFrozen", ds.getDouble("fFrozen") + withdrawAmount);
            ds.post();

            // 虚拟钱包扣钱
            MyQuery cds = new MyQuery(handle);
            cds.add("select UID_,userId_,coinId_,total_,frozen_,totalChange_,frozenChange_,changeReason_,changeDate_,guid_,taskId_,entrustId_ from %s",
                    AppDB.t_walletlog);
            cds.setMaximum(1);
            cds.open();
            cds.append();
            cds.setField("userId_", getUserId());
            cds.setField("coinId_", symbol);
            cds.setField("total_", ds.getDouble("fTotal"));
            cds.setField("frozen_", ds.getDouble("frozen_"));
            cds.setField("totalChange_", -withdrawAmount);
            cds.setField("frozenChange_", -withdrawAmount);
            cds.setField("changeReason_", "划转钱包");
            cds.setField("changeDate_", TDateTime.Now());
            cds.setField("entrustId_", "0");
            cds.setField("taskId_", taskId);
            cds.post();
            tx.commit();
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.VIRTUAL_TIXIAN);
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/account/cancelRechargeCnySubmit")
    public String cancelRechargeCnySubmit(HttpServletRequest request, int id) throws Exception {
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fcapitaloperation fcapitaloperation = this.frontAccountService.findCapitalOperationById(id);
        if (fcapitaloperation.getFuser().getFid() == fuser.getFid()
                && fcapitaloperation.getFtype() == CapitalOperationTypeEnum.RMB_IN
                && (fcapitaloperation.getFstatus() == CapitalOperationInStatus.NoGiven
                        || fcapitaloperation.getFstatus() == CapitalOperationInStatus.WaitForComing)) {
            fcapitaloperation.setFstatus(CapitalOperationInStatus.Invalidate);
            fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());
            try {
                this.frontAccountService.updateCapitalOperation(fcapitaloperation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return String.valueOf(0);
    }

    @ResponseBody
    @RequestMapping("/account/subRechargeCnySubmit")
    public String subRechargeCnySubmit(HttpServletRequest request, int id) throws Exception {
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fcapitaloperation fcapitaloperation = this.frontAccountService.findCapitalOperationById(id);
        if (fcapitaloperation.getFuser().getFid() == fuser.getFid()
                && fcapitaloperation.getFtype() == CapitalOperationTypeEnum.RMB_IN
                && fcapitaloperation.getFstatus() == CapitalOperationInStatus.NoGiven) {
            fcapitaloperation.setFstatus(CapitalOperationInStatus.WaitForComing);
            fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());
            try {
                this.frontAccountService.updateCapitalOperation(fcapitaloperation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return String.valueOf(0);
    }

    @ResponseBody
    @RequestMapping("/account/cancelWithdrawcny")
    public String cancelWithdrawcny(HttpServletRequest request, int id) throws Exception {
        Fcapitaloperation fcapitaloperation = this.frontAccountService.findCapitalOperationById(id);
        if (fcapitaloperation != null && fcapitaloperation.getFuser().getFid() == GetCurrentUser(request).getFid()
                && fcapitaloperation.getFtype() == CapitalOperationTypeEnum.RMB_OUT
                && fcapitaloperation.getFstatus() == CapitalOperationOutStatus.WaitForOperation) {
            try {
                this.frontAccountService.updateCancelWithdrawCny(fcapitaloperation,
                        this.frontUserService.findById(GetCurrentUser(request).getFid()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return String.valueOf(0);
    }

    @ResponseBody
    @RequestMapping("/account/cancelWithdrawBtc")
    public String cancelWithdrawBtc(HttpServletRequest request, @RequestParam(required = true) int id)
            throws Exception {
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fvirtualcaptualoperation fvirtualcaptualoperation = this.frontVirtualCoinService
                .findFvirtualcaptualoperationById(id);
        if (fvirtualcaptualoperation != null && fvirtualcaptualoperation.getFuser().getFid() == fuser.getFid()
                && fvirtualcaptualoperation.getFtype() == VirtualCapitalOperationTypeEnum.COIN_OUT
                && fvirtualcaptualoperation.getFstatus() == VirtualCapitalOperationOutStatusEnum.WaitForOperation) {

            try {
                this.frontAccountService.updateCancelWithdrawBtc(fvirtualcaptualoperation, fuser);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return String.valueOf(0);
    }

    @ResponseBody
    @RequestMapping("/account/getVirtualAddress")
    public String getVirtualAddress(HttpServletRequest request, int symbol) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        String filter = "where fuser.fid=" + fuser.getFid() + " and fvirtualcointype.fid=" + symbol;
        int count = this.adminService.getAllCount("Fvirtualaddress", filter);
        if (count > 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "已存在地址，不允许重复获取");
            return jsonObject.toString();
        }
        Fvirtualcointype coin = this.virtualCoinService.findById(symbol);
        if (coin.isFisrecharge()) {
            Fpool fpool = this.poolService.getOneFpool(coin);
            if (fpool == null) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "地址库地址不足，请联系管理员");
                return jsonObject.toString();
            }
            String address = fpool.getFaddress();
            Fvirtualaddress fvirtualaddress = new Fvirtualaddress();
            fvirtualaddress.setFadderess(address);
            fvirtualaddress.setFcreateTime(Utils.getTimestamp());
            fvirtualaddress.setFuser(fuser);
            fvirtualaddress.setFvirtualcointype(coin);
            if (address == null || address.trim().equalsIgnoreCase("null") || address.trim().equals("")) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "地址库地址不足，请联系管理员");
                return jsonObject.toString();
            }
            fpool.setFstatus(1);
            this.poolService.updateObj(fpool);
            this.virtualaddressService.saveObj(fvirtualaddress);
            jsonObject.accumulate("address", fvirtualaddress.getFadderess());
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "该虚拟币不支持充值");
            return jsonObject.toString();
        }

        jsonObject.accumulate("code", 0);
        return jsonObject.toString();
    }
}
