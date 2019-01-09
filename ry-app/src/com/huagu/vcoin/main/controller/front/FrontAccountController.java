package com.huagu.vcoin.main.controller.front;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.huagu.vcoin.main.Enum.*;
import org.apache.commons.collections.map.HashedMap;
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
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.comm.KeyValues;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fasset;
import com.huagu.vcoin.main.model.FbankinfoWithdraw;
import com.huagu.vcoin.main.model.Fcapitaloperation;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualaddress;
import com.huagu.vcoin.main.model.FvirtualaddressWithdraw;
import com.huagu.vcoin.main.model.Fvirtualcaptualoperation;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.model.Fwithdrawfees;
import com.huagu.vcoin.main.model.Systembankinfo;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.CapitaloperationService;
import com.huagu.vcoin.main.service.admin.VirtualCapitaloperationService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.admin.WithdrawFeesService;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.FrontAccountService;
import com.huagu.vcoin.main.service.front.FrontBankInfoService;
import com.huagu.vcoin.main.service.front.FrontSystemArgsService;
import com.huagu.vcoin.main.service.front.FrontTradeService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;
import com.huagu.vcoin.main.service.front.UtilsService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.MyMD5Utils;
import com.huagu.vcoin.util.PaginUtil;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.cache.Buffer;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import net.sf.json.JSONObject;

@Controller
public class FrontAccountController extends BaseController {
    private static Logger log = Logger.getLogger(FrontAccountController.class);
    @Autowired
    private FrontBankInfoService frontBankInfoService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private WithdrawFeesService withdrawFeesService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CapitaloperationService capitaloperationService;
    @Autowired
    private VirtualCapitaloperationService virtualCapitaloperationService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FrontSystemArgsService frontSystemArgsService;

    @RequestMapping("/m/financial/zichan")
    public ModelAndView ziChan(HttpServletRequest request, @RequestParam(required = false) String menuFlag) {
        request.getSession().setAttribute("menuFlag", menuFlag);
        JspPage modelAndView = new JspPage(request);
        Fuser fuser = GetCurrentUser(request);
        if(null != fuser) {
            if(Constant.hLAccountEnable.equals("true")){
                if(null != fuser.getFloginName() && fuser.getFloginName().toLowerCase().startsWith("ry")) {
                    modelAndView.add("userEnable", true);
                }else if(null != fuser.getFloginName() && !fuser.getFloginName().toLowerCase().startsWith("ry")) {
                    modelAndView.add("userEnable", false);
                }
            }
        }
        // 后台id错误用户钱包
        Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(fuser.getFid());
        // TODO hqs 把CNY单独提出来,不放到可充币和提币的列表里面,CNY只做展示
        modelAndView.addObject("CNY", fwallet.getFtotal() + fwallet.getFfrozen());
        modelAndView.addObject("fwallet", fwallet);
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
        modelAndView.addObject("fvirtualwallets", fvirtualwallets);
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
                        //map.put(ds1.getString("fvirtualcointype1"), (1 / fshou));
                        map.put(ds1.getString("fvirtualcointype2"), (fshou));
                        list.add(map);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        Map<Integer, Integer> tradeMappings = (Map) this.constantMap.get("tradeMappings");
        for (Map.Entry<Integer, Fvirtualwallet> entry : fvirtualwallets.entrySet()) {
            if (entry.getValue().getFvirtualcointype().getFtype() == CoinTypeEnum.FB_CNY_VALUE)
                continue;
            try {
                Fvirtualwallet wallet = entry.getValue();
                Fvirtualcointype coinType = wallet.getFvirtualcointype();
//                Integer tradeMappingId = tradeMappings.get(coinType.getFid());
//                if (tradeMappingId == null) {
//                    // FIXME: 此处发生频率太高，须修正 2018/5/18
//                    log.warn("tradeMappingId is null");
//                }
                // 币种,钱包 数据校验 负数置为0
                double fFrozen2 = wallet.getFfrozen() < 0 ? 0 : wallet.getFfrozen();
                double fTotal2 = wallet.getFtotal() < 0 ? 0 : wallet.getFtotal();
                boolean fromKline = false;
                if(!CollectionUtils.isEmpty(list)) {
                    for (Map<String, Double> arr : list) {
                        for (String in : arr.keySet()) {
                            if (Integer.parseInt(in) == (coinType.getFid())) {
                                fromKline = true;
                                totalCapital += ((fFrozen2 + fTotal2) * arr.get(in));
                            }
                        }
                    }
                }
                if(!fromKline) {
                    if ("比特币".equals(coinType.getFname())) {
                        // 币种 ,钱包数据校验 负数置为0
                        double fFrozen3 = wallet.getFfrozen() < 0 ? 0 : wallet.getFfrozen();
                        //double fTotal3 = wallet.getFfrozen() < 0 ? 0 : wallet.getFtotal();
                        double fTotal3 = wallet.getFtotal() < 0 ? 0 : wallet.getFtotal();
                        double exchangerate = 0;
                        if (map1.get("fny") != null) {
                            exchangerate = map1.get("fny") * (fFrozen3 + fTotal3);
                        } else {
                            String text = new FrontUserJsonController().getBTCPrice();
                            exchangerate = Double.parseDouble(text) * (fFrozen3 + fTotal3);
                        }
                        totalCapital += exchangerate;
                    }else{
                        totalCapital += ((fFrozen2 + fTotal2) * coinType.getFcurrentCNY());
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        modelAndView.addObject("totalCapitalTrade", Utils.getDouble(totalCapital, 6));
        modelAndView.addObject("exchangerate", Utils.getDouble(totalCapital, 6));

        Fuser user = this.frontUserService.findById(GetCurrentUser(request).getFid());
        modelAndView.addObject("fuser", user);
        modelAndView.setJspFile("/front/financial/zichan");
        return modelAndView;

    }

    @RequestMapping({ "/m/personal/assets" })
    public ModelAndView assets(HttpServletRequest request, @RequestParam(required = false) String menuFlag) {
        request.getSession().setAttribute("menuFlag", menuFlag);
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelAndView.addObject("tid", user.getFloginName());
        modelAndView.setJspFile("/front/financial/assets");
        return modelAndView;

    }

    @RequestMapping("/m/bank/card")
    public ModelAndView card(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        MyQuery ds = new MyQuery(new Mysql());
        Fuser user = this.GetCurrentUser(request);

        ds.add("select fId,substring(fAccount,-4) cardNum,fBankname bankName,fBanknamez bankCode from %s ",
                AppDB.t_userreceipt);
        ds.add("where fType=0 and fUsr_id='%s'", user.getFid());
        ds.open();
        List<Map<String, Object>> list = new ArrayList<>();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
            modelAndView.addObject("bankCard", list);
        }
        modelAndView.setJspFile("/front/financial/bankCard");
        return modelAndView;
    }

    @RequestMapping("/m/financial/recharge")
    public ModelAndView recharge(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        MyQuery ds = new MyQuery(new Mysql());
        Fuser user = this.GetCurrentUser(request);

        ds.add("select fId,substring(fAccount,-4) cardNum,fBankname bankName,fBanknamez bankCode from %s ",
                AppDB.t_userreceipt);
        ds.add("where fType=0 and fUsr_id='%s'", user.getFid());
        ds.open();
        List<Map<String, Object>> list = new ArrayList<>();
        if (ds.eof()) {
            modelAndView.addObject("add", true);
        } else {
            for (Record record : ds) {
                list.add(record.getItems());
            }
            modelAndView.addObject("add", false);
            modelAndView.addObject("bankCard", list);

        }
        modelAndView.setJspFile("/front/financial/recharge");
        return modelAndView;
    }

    @RequestMapping("/m/add/bankCard")
    public ModelAndView bankCard(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("/front/financial/addBankCard");
        return modelAndView;

    }

    @RequestMapping("/m/addBankCard")
    @ResponseBody
    public String addBankCard(HttpServletRequest request,
            @RequestParam(required = true, defaultValue = "") String cardNum,
            @RequestParam(required = true, defaultValue = "") String bankName,
            @RequestParam(required = false, defaultValue = "") String bankCode,
            @RequestParam(required = true, defaultValue = "") String password) {
        JSONObject jsonObject = new JSONObject();
        Fuser user = this.GetCurrentUser(request);

        try (Mysql mysql = new Mysql()) {
        	MyQuery pwd = new MyQuery(mysql);
        	pwd.add("SELECT fId,fTradePassword from fuser WHERE fId = %s",user.getFid());
        	pwd.open();
        	if("".equals(password)) {
        		jsonObject.put("code", -200);
                jsonObject.put("msg", "密码不能为空");
                return jsonObject.toString();
        	}
        	if(!MyMD5Utils.encoding(password).equals(pwd.getString("fTradePassword"))) {
        		jsonObject.put("code", -200);
                jsonObject.put("msg", "密码错误！");
                return jsonObject.toString();
        	}
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s ", AppDB.t_userreceipt);
            ds.add("where fType=0 and fUsr_id='%s' and fAccount='%s'", user.getFid(), cardNum);
            ds.open();
            if (ds.eof()) {
                ds.append();
                ds.setField("fUsr_id", user.getFid());
                ds.setField("fName", user.getFrealName());
                ds.setField("fAccount", cardNum);
                ds.setField("fBankname", bankName);
                ds.setField("fBanknamez", bankCode);
                ds.setField("fType", 0);
                ds.setField("fCreateTime", TDateTime.Now());
                ds.post();
                jsonObject.put("code", 200);
                jsonObject.put("msg", "银行卡添加成功");
            } else {
                jsonObject.put("code", -200);
                jsonObject.put("msg", "银行卡已存在");
            }
        } catch (Exception e) {
            jsonObject.put("code", -200);
            jsonObject.put("msg", e.getMessage());
            return jsonObject.toString();
        }
        return jsonObject.toString();
    }
    
    
    @RequestMapping("/m/delBankCard")
    @ResponseBody
    public String delBankCard(HttpServletRequest request,
            @RequestParam(required = true, defaultValue = "") String cardfId) {
        JSONObject jsonObject = new JSONObject();

        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s ", AppDB.t_userreceipt);
            ds.add("where fId = %s", cardfId);
            ds.open();
            if (!ds.eof()) {
            	ds.delete();
            	jsonObject.put("code", 200);
                jsonObject.put("msg", "删除成功！");
            }
        } catch (Exception e) {
            jsonObject.put("code", -200);
            jsonObject.put("msg", e.getMessage());
            return jsonObject.toString();
        }
        return jsonObject.toString();
    }

    @RequestMapping("/m/ryb/index")
    public ModelAndView ryb(HttpServletRequest request) {
        Fuser user = GetCurrentUser(request);
        JspPage modelAndView = new JspPage(request);
        modelAndView.addObject("tid", user.getFloginName());
        modelAndView.setJspFile("/front/ryb/ryb");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/exchange")
    public ModelAndView exchange(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelAndView.addObject("tid", user.getFloginName());
        modelAndView.setJspFile("/front/ryb/exchange");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/exchange_1")
    public ModelAndView exchange_1(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelAndView.addObject("tid", user.getFloginName());
        modelAndView.setJspFile("/front/ryb/exchange_1");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/proceedsToTrade")
    public ModelAndView proceedsToTrade(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelAndView.addObject("tid", user.getFloginName());
        modelAndView.setJspFile("/front/ryb/proceedsToTrade");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/transferSell")
    public ModelAndView transferSell(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelAndView.addObject("tid", user.getFloginName());
        modelAndView.setJspFile("/front/ryb/transferSell");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/transactionToEarnings")
    public ModelAndView transactionToEarnings(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("/front/ryb/transactionToEarnings");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/transferAccounts")
    public ModelAndView transferAccounts(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelAndView.addObject("tid", user.getFloginName());
        modelAndView.setJspFile("/front/ryb/transferAccounts");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/staticRelease")
    public ModelAndView staticRelease(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelAndView.addObject("tid", user.getFloginName());
        modelAndView.setJspFile("/front/ryb/staticRelease");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/details")
    public ModelAndView details(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        String fid = request.getParameter("fid");
        String welletId = request.getParameter("welletId");
        modelAndView.addObject("welletId", welletId);
        String wellet = request.getParameter("wellet");
        modelAndView.addObject("wellet", wellet);
        String type = request.getParameter("type");
        modelAndView.addObject("type", type );
        modelAndView.addObject("fid", fid);
        modelAndView.addObject("user", user.getFloginName());
        modelAndView.setJspFile("/front/ryb/details");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/rybWallet")
    public ModelAndView rybWallet(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("/front/ryb/rybWallet");
        return modelAndView;
    }

    @RequestMapping("/m/ryb/rybTransferAccounts")
    public ModelAndView rybTransferAccounts(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("/front/ryb/rybTransferAccounts");
        return modelAndView;
    }

    @RequestMapping({ "/m/account/rechargeCny", "account/rechargeCny" })
    public ModelAndView rechargeCny(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "0") int type) throws Exception {

        if (type != 0 /* && type !=3 && type !=4 */) {
            type = 0;
        }

        JspPage modelAndView = new JspPage(request);

        // 人民币随机尾数
        int randomMoney = (new Random().nextInt(80) + 11);
        // 系统银行账号
        List<Systembankinfo> systembankinfos = this.frontBankInfoService.findAllSystemBankInfo();
        modelAndView.addObject("randomMoney", randomMoney);
        modelAndView.addObject("bankInfo", systembankinfos);

        // record
        Fuser fuser = this.GetCurrentUser(request);
        StringBuffer filter = new StringBuffer();
        filter.append("where fuser.fid=" + fuser.getFid() + " \n");
        filter.append("and ftype =" + CapitalOperationTypeEnum.RMB_IN + "\n");
        if (type != 0) {
            filter.append("and fremittanceType='" + RemittanceTypeEnum.getEnumString(type) + "' \n");
        } else {
            filter.append("and systembankinfo is not null \n");
        }

        filter.append(" order by fid desc \n");
        List<Fcapitaloperation> list = this.utilsService.list(PaginUtil.firstResult(currentPage, maxResults),
                maxResults, filter.toString(), true, Fcapitaloperation.class);

        int totalCount = this.adminService.getAllCount("Fcapitaloperation", filter.toString());
        String url = "/account/rechargeCny.html?type=" + type + "&";
        String pagin = PaginUtil.generatePagin(PaginUtil.totalPage(totalCount, maxResults), currentPage, url);

        // 最小充值金额
        double minRecharge = Double.parseDouble(this.constantMap.get("minrechargecny").toString().trim());
        modelAndView.addObject("minRecharge", minRecharge);

        Map<Integer, String> bankTypes = new HashMap<Integer, String>();
        for (int i = 1; i <= BankTypeEnum.QT; i++) {
            if (BankTypeEnum.getEnumString(i) != null && BankTypeEnum.getEnumString(i).trim().length() > 0) {
                bankTypes.put(i, BankTypeEnum.getEnumString(i));
            }
        }
        modelAndView.addObject("bankTypes", bankTypes);

        // boolean isproxy = false;
        // String ss = "where fuser.fid="+fuser.getFid()+" and fstatus=1";
        // int cc = this.adminService.getAllCount("Fproxy", ss);
        // if(cc >0){
        // isproxy = true;
        // }
        // modelAndView.addObject("isproxy", isproxy) ;

        modelAndView.addObject("list", list);
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("currentPage_page", currentPage);
        modelAndView.addObject("fuser", GetCurrentUser(request));
        modelAndView.addObject("type", type);
        String uri = request.getRequestURI();
        if (!uri.startsWith("/m/")) {
            modelAndView.setJspFile("front/account/account_rechargecny" + type);
        } else {
            modelAndView.setJspFile("front/account/rechargeCny");
        }
        return modelAndView;
    }

    @RequestMapping({ "m/account/rechargeBtcList", "/m/account/renminbichongzhijilu" })
    public ModelAndView rechargeBtcList(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        List<Fvirtualcaptualoperation> list = null;
        String symbol = request.getParameter("symbol");
        if(symbol != null) {
        	if(!"".equals(symbol)) {
        		list = virtualCapitaloperationService.list(0, 0, "where ftype = 1 and famount >0 and fuser.fid = "+GetCurrentUser(request).getFid()+" and fvirtualcointype.fid = "+symbol, false);
        	}
        }
        modelAndView.addObject("fvirtualcaptualoperations", list);
        modelAndView.setJspFile("front/account/rechargeBtcList");
        return modelAndView;

    }

    @RequestMapping(value = "app/account/rechargeCnyApp", produces = { JsonEncode })
    @ResponseBody
    public String rechargeCnyApp() throws Exception {

        JSONObject jsonObject = new JSONObject();

        // 人民币随机尾数
        int randomMoney = (new Random().nextInt(80) + 11);
        jsonObject.accumulate("randomMoney", randomMoney);
        jsonObject.accumulate("rate", this.constantMap.get("rate").toString());
        // 系统银行账号
        List<Systembankinfo> systembankinfos = this.frontBankInfoService.findAllSystemBankInfo();

        for (Systembankinfo systembankinfo : systembankinfos) {
            JSONObject js = new JSONObject();
            js.accumulate("bid", systembankinfo.getFid());
            js.accumulate("bname", systembankinfo.getFbankName());
            js.accumulate("bnumber", systembankinfo.getFbankNumber());
            jsonObject.accumulate("bankList", js);
        }

        return jsonObject.toString();
    }

    @RequestMapping(value = "app/account/rechargeBtcApp", produces = { JsonEncode })
    @ResponseBody
    public String rechargeBtcApp(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int symbol,
            @RequestParam(required = false, defaultValue = "0") String uuid) throws Exception {

        JSONObject jsonObject = new JSONObject();

        Fuser fuser = this.frontUserService.findById(Integer.parseInt(uuid));
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE
                || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal
                || !fvirtualcointype.isFisrecharge()) {
            String filter = "where fstatus=1 and fisrecharge=1 and ftype <> " + CoinTypeEnum.FB_CNY_VALUE
                    + " order by fid asc";
            List<Fvirtualcointype> alls = this.virtualCoinService.list(0, 1, filter, true);
            if (alls == null || alls.size() == 0) {
                return jsonObject.toString();
            }
            fvirtualcointype = alls.get(0);
        }
        Fvirtualaddress fvirtualaddress = new Fvirtualaddress();
        // 如果是以太代币，则显示以太坊充值地址
        if (fvirtualcointype.isFisToken()) {
            List<Fvirtualcointype> tokenCoinList = this.frontVirtualCoinService.findFvirtualCoinTypeEth();
            Fvirtualcointype tokenCoinn = new Fvirtualcointype();
            if (tokenCoinList.size() > 0) {
                tokenCoinn = tokenCoinList.get(0);
                jsonObject.accumulate("tokenFid", tokenCoinn.getFid());
            }
            fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, tokenCoinn);
        } else {
            fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);
        }
        if (null != fvirtualaddress) {
            jsonObject.accumulate("address", fvirtualaddress.getFadderess());
        } else {
            jsonObject.accumulate("address", "");
        }
        jsonObject.accumulate("des", fvirtualcointype.getFregDesc());
        jsonObject.accumulate("isToken", fvirtualcointype.isFisToken());
        jsonObject.accumulate("fid", fvirtualcointype.getFid());
        return jsonObject.toString();
    }

    @RequestMapping("account/withdrawCny")
    public ModelAndView withdrawCny(@RequestParam(required = false, defaultValue = "1") int currentPage,
            HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        String filter = "where fuser.fid=" + fuser.getFid() + " and fbankType >0";
        List<FbankinfoWithdraw> fbankinfoWithdraws = this.frontUserService.findFbankinfoWithdrawByFuser(0, 0, filter,
                false);
        for (FbankinfoWithdraw fbankinfoWithdraw : fbankinfoWithdraws) {
            int l = fbankinfoWithdraw.getFbankNumber().length();
            fbankinfoWithdraw.setFbankNumber(fbankinfoWithdraw.getFbankNumber().substring(l - 4, l));
        }

        Map<Integer, String> bankTypes = new HashMap<Integer, String>();
        for (int i = 1; i <= BankTypeEnum.QT; i++) {
            if (BankTypeEnum.getEnumString(i) != null && BankTypeEnum.getEnumString(i).trim().length() > 0) {
                bankTypes.put(i, BankTypeEnum.getEnumString(i));
            }
        }
        modelAndView.addObject("bankTypes", bankTypes);
        // 后台id错误用户钱包
        if (isLogon(request)) {
            log.info(getUserId());
            Mysql handle = new Mysql();
            MyQuery ds = new MyQuery(handle);
            ds.add("select * from %s", AppDB.Fuser);
            ds.add(" where fId = %d", getUserId());
            ds.open();
            if (ds.eof()) {
                removeAdminSession(request);
                CleanSession(request);
                RemoveSecondLoginSession(request);
                modelAndView.setViewName("redirect:/user/login.html");
                return modelAndView;
            }
        }
        Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(GetCurrentUser(request).getFid());
        request.getSession().setAttribute("fwallet", fwallet);
        Fwithdrawfees ffees = this.withdrawFeesService.findFfee(fwallet.getFvirtualcointype().getFid(),
                fuser.getFscore().getFlevel());
        double fee = ffees.getFfee();
        double amt = ffees.getFamt();
        double last = ffees.getFlastFee();
        modelAndView.addObject("fee", fee);
        modelAndView.addObject("amt", amt);
        modelAndView.addObject("last", last);

        // 獲得千12條交易記錄
        String param = "where fuser.fid=" + fuser.getFid() + " and ftype=" + CapitalOperationTypeEnum.RMB_OUT
                + " order by fid desc";
        List<Fcapitaloperation> fcapitaloperations = this.frontAccountService
                .findCapitalList(PaginUtil.firstResult(currentPage, maxResults), maxResults, param, true);
        int totalCount = this.adminService.getAllCount("Fcapitaloperation", param.toString());
        String url = "/account/withdrawCny.html?";
        String pagin = PaginUtil.generatePagin(totalCount / maxResults + ((totalCount % maxResults) == 0 ? 0 : 1),
                currentPage, url);

        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("isBindTelephone", isBindTelephone);

        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("fcapitaloperations", fcapitaloperations);
        modelAndView.addObject("fuser", fuser);
        modelAndView.addObject("fbankinfoWithdraws", fbankinfoWithdraws);
        modelAndView.setJspFile("front/account/account_withdrawcny");
        return modelAndView;
    }

    @RequestMapping(value = { "/m/account/rechargeBtc", "account/rechargeBtc" })
    public ModelAndView rechargeBtc(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        JspPage modelAndView = new JspPage(request);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE
                || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal
                || !fvirtualcointype.isFisrecharge()) {
            String filter = "where fstatus=1 and fisrecharge=1 and ftype <> " + CoinTypeEnum.FB_CNY_VALUE
                    + " order by fid asc";
            List<Fvirtualcointype> alls = this.virtualCoinService.list(0, 1, filter, true);
            if (alls == null || alls.size() == 0) {
                modelAndView.setJspFile("front/account/account_rechargebtc");
                return modelAndView;
            }
            fvirtualcointype = alls.get(0);
        }
        symbol = fvirtualcointype.getFid();
        Fvirtualaddress fvirtualaddress = new Fvirtualaddress();
        // 如果是以太代币，则显示以太坊充值地址
        if (fvirtualcointype.isFisToken()) {
            List<Fvirtualcointype> tokenCoinList = this.frontVirtualCoinService.findFvirtualCoinTypeEth();
            Fvirtualcointype tokenCoinn = new Fvirtualcointype();
            if (tokenCoinList.size() > 0) {
                tokenCoinn = tokenCoinList.get(0);
            }
            fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);
        } else {
            fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);
        }

        // 最近十次充值记录
        StringBuilder builder = new StringBuilder();
        builder.append("where fuser.fid=" + fuser.getFid());
        builder.append("and ftype=" + VirtualCapitalOperationTypeEnum.COIN_IN);
        builder.append("and fvirtualcointype.fid=" + fvirtualcointype.getFid());
        builder.append("and fStatus=" + VirtualCapitalOperationInStatusEnum.SUCCESS);
        builder.append("order by fid desc");
        String filter = builder.toString();

        List<Fvirtualcaptualoperation> fvirtualcaptualoperations = this.utilsService.list(
                PaginUtil.firstResult(currentPage, maxResults), maxResults, filter, true,
                Fvirtualcaptualoperation.class);
        int totalCount = this.adminService.getAllCount("Fvirtualcaptualoperation", filter.toString());
        String url = "/account/rechargeBtc.html?symbol=" + symbol + "&";
        String pagin = PaginUtil.generatePagin(totalCount / maxResults + ((totalCount % maxResults) == 0 ? 0 : 1),
                currentPage, url);

        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("fvirtualcaptualoperations", fvirtualcaptualoperations);
        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("fvirtualaddress", fvirtualaddress);
        modelAndView.setJspFile("front/account/account_rechargebtc");
        return modelAndView;
    }

    @RequestMapping(value = { "/m/account/withdrawBtc", "account/withdrawBtc" })
    public ModelAndView withdrawBtc(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal
                || !fvirtualcointype.isFIsWithDraw() || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE) {
            String filter = "where fstatus=1 and FIsWithDraw=1 and ftype <>" + CoinTypeEnum.FB_CNY_VALUE
                    + " order by fid asc";
            List<Fvirtualcointype> alls = this.virtualCoinService.list(0, 1, filter, true);
            if (alls == null || alls.size() == 0) {
                modelAndView.setJspFile("front/account/account_withdrawbtc");
                return modelAndView;
            }
            fvirtualcointype = alls.get(0);
        }
        int time = this.frontAccountService.getTodayVirtualCoinWithdrawTimes(fuser,fvirtualcointype.getFid());
        modelAndView.addObject("canWithdrawTime", fvirtualcointype.getfWithdrawTimes() > time);
        double hasWithdrawAmount = getVirtualCoinWithdrawAmount(fuser,fvirtualcointype.getFid());
        modelAndView.addObject("hasWithdrawAmount", hasWithdrawAmount);

        symbol = fvirtualcointype.getFid();
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                fvirtualcointype.getFid());

        String sql = "where fuser.fid=" + fuser.getFid() + " and fvirtualcointype.fid=" + symbol;
        List<FvirtualaddressWithdraw> fvirtualaddressWithdraws = this.frontVirtualCoinService
                .findFvirtualaddressWithdraws(0, 0, sql, false);

        // 近10条提现记录
        String filter = "where fuser.fid=" + fuser.getFid() + " and ftype=" + VirtualCapitalOperationTypeEnum.COIN_OUT
                + " and fvirtualcointype.fid=" + fvirtualcointype.getFid() + " order by fid desc";
        List<Fvirtualcaptualoperation> fvirtualcaptualoperations = this.utilsService.list(
                PaginUtil.firstResult(currentPage, maxResults), maxResults, filter, true,
                Fvirtualcaptualoperation.class);
        int totalCount = this.adminService.getAllCount("Fvirtualcaptualoperation", filter.toString());
        String url = "/account/withdrawBtc.html?symbol=" + symbol + "&";
        String pagin = PaginUtil.generatePagin(totalCount / maxResults + ((totalCount % maxResults) == 0 ? 0 : 1),
                currentPage, url);

        modelAndView.addObject("pagin", pagin);

        int isEmptyAuth = 0;
        if (fuser.isFisTelephoneBind() || fuser.getFgoogleBind()) {
            isEmptyAuth = 1;
        }
        modelAndView.addObject("isEmptyAuth", isEmptyAuth);

        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("isBindTelephone", isBindTelephone);

        String priceRange = frontSystemArgsService.getSystemArgs("priceRange");// 获取提现自动审核起始值系统参数

        try (Mysql mysql = new Mysql()) {
            MyQuery queryFee = new MyQuery(mysql);
            queryFee.add("select fid,ffee,flevel,fvid from %s where 1=1", "fwithdrawfees");
            queryFee.add(" and fvid = %s", symbol);
            queryFee.add(" and flevel = %s", 1);// 用户等级先写死的
            queryFee.open();
            if (!queryFee.eof()) {
                modelAndView.addObject("ffee", queryFee.getDouble("ffee"));
            }
        }
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select * from %s ", "fsystemargs");
        ds.add("where fKey='priceRange'");
        ds.open();
        String[] btc = ds.getString("fValue").split(",");
        modelAndView.addObject("minwithdrawbtc", btc[0]);
        modelAndView.addObject("maxwithdrawbtc", btc[1]);
        modelAndView.addObject("priceRange", priceRange);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("fvirtualcaptualoperations", fvirtualcaptualoperations);
        modelAndView.addObject("fvirtualwallet", fvirtualwallet);
        modelAndView.addObject("fuser", fuser);
        modelAndView.addObject("fvirtualaddressWithdraws", fvirtualaddressWithdraws);
        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        modelAndView.setJspFile("front/account/account_withdrawbtc");
        return modelAndView;
    }

    private double getVirtualCoinWithdrawAmount(Fuser fuser, int coinId) {
        Mysql mysql = new Mysql();
        MyQuery query = new MyQuery(mysql);
        query.add("select sum(fAmount) as amount from fvirtualcaptualoperation where fus_fid2=%s and fvi_fid2 = %s and ftype = %s and fstatus!=%s",
                fuser.getFid(),coinId,VirtualCapitalOperationTypeEnum.COIN_OUT, VirtualCapitalOperationOutStatusEnum.Cancel);
        query.open();
        double amount = 0.0;
        while (query.fetch()){
            amount = query.getDouble("amount");
        }
        return amount;
    }

    @RequestMapping(value = { "/m/account/transfer", "account/transfer" })
    public ModelAndView transfer(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // boolean flag = false;
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal
                || !fvirtualcointype.isFIsWithDraw() || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE) {
            String filter = "where fstatus=1 and FIsWithDraw=1 and ftype <>" + CoinTypeEnum.FB_CNY_VALUE
                    + " order by fid asc";
            List<Fvirtualcointype> alls = this.virtualCoinService.list(0, 1, filter, true);
            if (alls == null || alls.size() == 0) {
                modelAndView.setJspFile("front/account/account_transfer");
                return modelAndView;
            }
            fvirtualcointype = alls.get(0);
        }
        symbol = fvirtualcointype.getFid();

        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                fvirtualcointype.getFid());
        String sql = "where fuser.fid=" + fuser.getFid() + " and fvirtualcointype.fid=" + symbol;
        List<FvirtualaddressWithdraw> fvirtualaddressWithdraws = this.frontVirtualCoinService
                .findFvirtualaddressWithdraws(0, 0, sql, false);
        List<Map<String, Object>> list = new ArrayList<>();
        // 近10条划转记录
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s ", "ftransfer");
            ds.add(" where fUs_fId2 ='%s'", fuser.getFid());
            ds.add("order by fCreateTime desc ");
            ds.setMaximum(10);
            ds.open();
            if (!ds.eof()) {
                for (Record record : ds) {
                    list.add(record.getItems());
                }
                // flag = true;
                modelAndView.addObject("account", list.get(0).get("fAccount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int isEmptyAuth = 0;
        if (fuser.isFisTelephoneBind() || fuser.getFgoogleBind()) {
            isEmptyAuth = 1;
        }
        modelAndView.addObject("isEmptyAuth", isEmptyAuth);

        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("isBindTelephone", isBindTelephone);

        String priceRange = frontSystemArgsService.getSystemArgs("priceRange");// 获取提现自动审核起始值系统参数

        // modelAndView.addObject("code", flag);
        modelAndView.addObject("priceRange", priceRange);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("ftransfer", list);
        modelAndView.addObject("count", list.size());
        modelAndView.addObject(AppDB.fvirtualwallet, fvirtualwallet);
        modelAndView.addObject("fuser", fuser);
        modelAndView.addObject("fvirtualaddressWithdraws", fvirtualaddressWithdraws);
        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        modelAndView.setJspFile("front/account/account_transfer");
        return modelAndView;
    }

    /**
     * 划转到封神榜
     * 
     * @param request
     * @param currentPage
     * @param symbol
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/m/account/transform", "account/transform" })
    public ModelAndView transform(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "7") int symbol) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // boolean flag = false;
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal
                || !fvirtualcointype.isFIsWithDraw() || fvirtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE) {
            String filter = "where fstatus=1 and FIsWithDraw=1 and ftype <>" + CoinTypeEnum.FB_CNY_VALUE
                    + " order by fid asc";
            List<Fvirtualcointype> alls = this.virtualCoinService.list(0, 1, filter, true);
            if (alls == null || alls.size() == 0) {
                modelAndView.setJspFile("front/account/account_transform");
                return modelAndView;
            }
            fvirtualcointype = alls.get(0);
        }
        symbol = fvirtualcointype.getFid();

        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                fvirtualcointype.getFid());
        String sql = "where fuser.fid=" + fuser.getFid() + " and fvirtualcointype.fid=" + symbol;
        List<FvirtualaddressWithdraw> fvirtualaddressWithdraws = this.frontVirtualCoinService
                .findFvirtualaddressWithdraws(0, 0, sql, false);
        List<Map<String, Object>> list = new ArrayList<>();
        // 近10条划转记录
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s ", "ftransform");
            ds.add(" where fUs_fId2 ='%s'", fuser.getFid());
            ds.add("order by fCreateTime desc ");
            ds.setMaximum(10);
            ds.open();
            if (!ds.eof()) {
                for (Record record : ds) {
                    list.add(record.getItems());
                }
                modelAndView.addObject("account", list.get(0).get("fAccount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int isEmptyAuth = 0;
        if (fuser.isFisTelephoneBind() || fuser.getFgoogleBind()) {
            isEmptyAuth = 1;
        }
        modelAndView.addObject("isEmptyAuth", isEmptyAuth);

        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("isBindTelephone", isBindTelephone);

        String priceRange = frontSystemArgsService.getSystemArgs("priceRange");// 获取提现自动审核起始值系统参数

        modelAndView.addObject("priceRange", priceRange);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("ftransform", list);
        modelAndView.addObject("count", list.size());
        modelAndView.addObject(AppDB.fvirtualwallet, fvirtualwallet);
        modelAndView.addObject("fuser", fuser);
        modelAndView.addObject("fvirtualaddressWithdraws", fvirtualaddressWithdraws);
        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        modelAndView.setJspFile("front/account/account_transform");
        return modelAndView;
    }

    @RequestMapping(value = { "/account/record", "/m/account/record", "/m/zichan/bill" })
    public ModelAndView record(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int recordType,
            @RequestParam(required = false, defaultValue = "0") int symbol,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "2") int datetype,
            @RequestParam(required = false, defaultValue = "") String begindate,
            @RequestParam(required = false, defaultValue = "") String from,
            @RequestParam(required = false, defaultValue = "") String enddate) throws Exception {
        JspPage modelAndView = new JspPage(request);
        boolean fromBill = "bill".equals(from);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!(datetype >= 1 && datetype <= 4)) {
            datetype = 2;
        }
        if (enddate == null || enddate.trim().length() == 0) {
            enddate = sdf.format(new Date());
        } else {
            try {
                enddate = sdf.format(sdf.parse(HtmlUtils.htmlEscape(enddate)));
            } catch (Exception e) {
                e.printStackTrace();
                enddate = "";
            }
        }
        if (begindate == null || begindate.trim().length() == 0) {
            switch (datetype) {
            case 1:
                begindate = sdf.format(new Date());
                break;
            case 2:
                begindate = Utils.getAfterDay(7);
                break;
            case 3:
                begindate = Utils.getAfterDay(15);
                break;
            case 4:
                begindate = Utils.getAfterDay(30);
                break;
            }
        } else {
            try {
                begindate = sdf.format(sdf.parse(HtmlUtils.htmlEscape(begindate)));
            } catch (Exception e) {
                e.printStackTrace();
                begindate = "";
            }
        }

        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || (fvirtualcointype.getFtype() != CoinTypeEnum.FB_COIN_VALUE
                && fvirtualcointype.getFtype() != CoinTypeEnum.COIN_VALUE)) {
            fvirtualcointype = this.frontVirtualCoinService.findFirstFirtualCoin();
            symbol = fvirtualcointype.getFid();
        }

        if (recordType > TradeRecordTypeEnum.BTC_WITHDRAW) {
            recordType = TradeRecordTypeEnum.BTC_WITHDRAW;
        }

        String filter = "where fstatus=1 and ftype <>" + CoinTypeEnum.FB_CNY_VALUE
                + " and (FIsWithDraw=1 or fisrecharge=1)";
        List<Fvirtualcointype> fvirtualcointypes = this.virtualCoinService.list(0, 0, filter, false);
        // 过滤器
        List<KeyValues> filters = new ArrayList<KeyValues>();
        for (int i = 1; i <= TradeRecordTypeEnum.BTC_WITHDRAW; i++) {
            if (i == 1 || i == 2) {
                // KeyValues keyValues = new KeyValues();
                // String key = "/account/record.html?recordType=" + i +
                // "&symbol=0";
                // String value = TradeRecordTypeEnum.getEnumString(i);
                // keyValues.setKey(key);
                // keyValues.setValue(value);
                // filters.add(keyValues);
                continue;
            } else {
                String key = "/account/record.html?recordType=" + i + "&symbol=";
                for (int j = 0; j < fvirtualcointypes.size(); j++) {
                    String value = TradeRecordTypeEnum.getEnumString(i);
                    Fvirtualcointype vc = fvirtualcointypes.get(j);

                    if (i == TradeRecordTypeEnum.BTC_WITHDRAW) {
                        if (!vc.isFIsWithDraw()) {
                            continue;
                        }
                    }

                    if (i == TradeRecordTypeEnum.BTC_RECHARGE) {
                        if (!vc.isFisrecharge()) {
                            continue;
                        }
                    }

                    value = vc.getfShortName() + value;
                    KeyValues keyValues = new KeyValues();
                    String param = String.valueOf(vc.getFid());
                    if (fromBill) {
                        param += "&from=bill";
                    }
                    keyValues.setKey(key + param);
                    keyValues.setValue(value);
                    filters.add(keyValues);
                }
            }
        }

        // 内容
        List list = new ArrayList<>();
        int totalCount = 0;
        String pagin = "";
        String param = "";
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        switch (recordType) {
        case TradeRecordTypeEnum.CNY_RECHARGE:
            param = "where fuser.fid=" + fuser.getFid() + " and ftype=" + CapitalOperationTypeEnum.RMB_IN
            // + " and date_format(fcreatetime,'%Y-%m-%d')>='" + begindate + "'"
                    + " and fcreatetime >='" + begindate + "'"
                    // + " and date_format(fcreatetime,'%Y-%m-%d')<='" + enddate + "' order by fid
                    // desc";
                    + " and fcreatetime <='" + enddate + "' order by fid desc";
            list = this.virtualCapitaloperationService.list(PaginUtil.firstResult(currentPage, totalCount), maxResults,
                    param, true);
            totalCount = this.adminService.getAllCount("Fcapitaloperation", param);
            break;

        case TradeRecordTypeEnum.CNY_WITHDRAW:
            param = "where fuser.fid=" + fuser.getFid() + " and ftype=" + CapitalOperationTypeEnum.RMB_OUT
                    + " and fcreatetime >='" + begindate + "'"
                    + " and fcreatetime <='" + enddate + "' order by fid desc";
            list = this.virtualCapitaloperationService.list(PaginUtil.firstResult(currentPage, totalCount), maxResults,
                    param, true);
            totalCount = this.adminService.getAllCount("Fcapitaloperation", param);

            break;
        case TradeRecordTypeEnum.BTC_RECHARGE:
            param = "where fuser.fid=" + fuser.getFid() + " and fvirtualcointype.fid=" + fvirtualcointype.getFid()
                    + " and ftype =" + VirtualCapitalOperationTypeEnum.COIN_IN
                    + " and fcreatetime>='" + begindate + "'"
                    + " and fcreatetime<='" + enddate + "' order by fid desc";
            list = this.virtualCapitaloperationService.list(
                    PaginUtil.firstResult(currentPage, totalCount), maxResults, param, true);
            totalCount = this.adminService.getAllCount("Fvirtualcaptualoperation", param);

            break;
        case TradeRecordTypeEnum.BTC_WITHDRAW:
            param = "where fuser.fid=" + fuser.getFid() + " and fvirtualcointype.fid=" + fvirtualcointype.getFid()
                    + " and ftype =" + VirtualCapitalOperationTypeEnum.COIN_OUT
                    + " and fcreatetime >='" + begindate + "'"
                    + " and fcreatetime <='" + enddate + "' order by fid desc";
            list = this.virtualCapitaloperationService.list(
                    PaginUtil.firstResult(currentPage, totalCount), maxResults, param, true);
            totalCount = this.adminService.getAllCount("Fvirtualcaptualoperation", param);

            break;
        // case TradeRecordTypeEnum.BTC_BUY:
        // param = "where fuser.fid="+fuser.getFid()+" and
        // fentrustType="+EntrustTypeEnum.BUY+" and
        // fvirtualcointype.fid="+fvirtualcointype.getFid()
        // +" and date_format(fcreatetime,'%Y-%m-%d')>='"+begindate+"'"+" and
        // date_format(fcreatetime,'%Y-%m-%d')<='"+enddate+"' order by fid
        // desc";
        // list =
        // this.frontTradeService.findFentrustHistory(PaginUtil.firstResult(currentPage,
        // totalCount), param, true);
        // totalCount = this.adminService.getAllCount("Fentrust", param);
        //
        // break;
        // case TradeRecordTypeEnum.BTC_SELL:
        // param = "where fuser.fid="+fuser.getFid()+" and
        // fentrustType="+EntrustTypeEnum.SELL+" and
        // fvirtualcointype.fid="+fvirtualcointype.getFid()
        // +" and date_format(fcreatetime,'%Y-%m-%d')>='"+begindate+"'"+" and
        // date_format(fcreatetime,'%Y-%m-%d')<='"+enddate+"' order by fid
        // desc";
        // list =
        // this.frontTradeService.findFentrustHistory(PaginUtil.firstResult(currentPage,
        // totalCount), param, true);
        // totalCount = this.adminService.getAllCount("Fentrust", param);
        //
        // break;
        }

        String url = "/account/record.html?recordType=" + recordType + "&symbol=" + symbol + "&datetype=" + datetype
                + "&begindate=" + begindate + "&enddate=" + enddate + "&";
        pagin = PaginUtil.generatePagin(totalCount / maxResults + ((totalCount % maxResults) == 0 ? 0 : 1), currentPage,
                url);
        modelAndView.addObject("datetype", datetype);
        modelAndView.addObject("begindate", begindate);
        modelAndView.addObject("enddate", enddate);
        modelAndView.addObject("list", list);
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("recordType", recordType);
        modelAndView.addObject("symbol", symbol);
        modelAndView.addObject("filters", filters);
        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        modelAndView.addObject("from", from);
        if (recordType == 1 || recordType == 2) {
            modelAndView.addObject("select", TradeRecordTypeEnum.getEnumString(recordType));
        } else {
            modelAndView.addObject("select",
                    fvirtualcointype.getfShortName() + TradeRecordTypeEnum.getEnumString(recordType));
        }
        modelAndView.setJspFile("front/account/account_record");
        return modelAndView;
    }

    @RequestMapping(value = "/account/refTenbody")
    public ModelAndView refTenbody(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "0") int type) throws Exception {
        if (type != 0 /* && type !=2 && type !=3 && type !=4 */) {
            type = 0;
        }

        JspPage modelAndView = new JspPage(request);

        Fuser fuser = this.GetCurrentUser(request);
        StringBuffer filter = new StringBuffer();
        filter.append("where fuser.fid=" + fuser.getFid() + " \n");
        filter.append("and ftype =" + CapitalOperationTypeEnum.RMB_IN + "\n");
        if (type != 0) {
            filter.append("and fremittanceType='" + RemittanceTypeEnum.getEnumString(type) + "' \n");
        } else {
            filter.append("and systembankinfo is not null \n");
        }
        filter.append(" order by fid desc \n");
        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * Constant.AppRecordPerPage,
                Constant.AppRecordPerPage, filter.toString(), true);

        int totalCount = this.adminService.getAllCount("Fcapitaloperation", filter.toString());
        String url = "/account/rechargeCny.html?type=" + type + "&";
        String pagin = PaginUtil.generatePagin(
                totalCount / Constant.AppRecordPerPage + ((totalCount % Constant.AppRecordPerPage) == 0 ? 0 : 1),
                currentPage, url);

        modelAndView.addObject("list", list);
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("currentPage_page", currentPage);
        modelAndView.setJspFile("front/account/reftenbody");
        return modelAndView;
    }

    @RequestMapping(value = "/app/account/refTenbodyApp", produces = { JsonEncode })
    @ResponseBody
    public String refTenbodyApp(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "0") int type) throws Exception {
        if (type != 0 /* && type !=2 && type !=3 && type !=4 */) {
            type = 0;
        }

        JspPage modelAndView = new JspPage(request);

        Fuser fuser = this.GetCurrentUser(request);
        StringBuffer filter = new StringBuffer();
        filter.append("where fuser.fid=" + fuser.getFid() + " \n");
        filter.append("and ftype =" + CapitalOperationTypeEnum.RMB_IN + "\n");
        if (type != 0) {
            filter.append("and fremittanceType='" + RemittanceTypeEnum.getEnumString(type) + "' \n");
        } else {
            filter.append("and systembankinfo is not null \n");
        }
        filter.append(" order by fid desc \n");
        List<Fcapitaloperation> list = this.capitaloperationService.list((currentPage - 1) * Constant.AppRecordPerPage,
                Constant.AppRecordPerPage, filter.toString(), true);

        int totalCount = this.adminService.getAllCount("Fcapitaloperation", filter.toString());
        String url = "/account/rechargeCny.html?type=" + type + "&";
        String pagin = PaginUtil.generatePagin(
                totalCount / Constant.AppRecordPerPage + ((totalCount % Constant.AppRecordPerPage) == 0 ? 0 : 1),
                currentPage, url);

        modelAndView.addObject("list", list);
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("currentPage_page", currentPage);
        modelAndView.setJspFile("front/account/reftenbody");
        return null;
    }

    @RequestMapping("/m/financial/income")
    public ModelAndView income(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("/front/financial/income");
        return modelAndView;
    }
}
