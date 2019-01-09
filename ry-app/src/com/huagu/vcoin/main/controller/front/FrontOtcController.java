package com.huagu.vcoin.main.controller.front;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;
import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.front.FrontOthersService;
import com.huagu.vcoin.main.service.front.FrontSystemArgsService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.util.Constant;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author l
 * @date 2018年6月11日
 */
@Controller
public class FrontOtcController extends BaseController {

    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontOthersService frontOthersService;
    @Autowired
    private FrontSystemArgsService frontSystemArgsService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = { "/otc/index", "/m/otc/index" })
    public ModelAndView ctcIndex(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "-1") int fId) {
        DecimalFormat df = new DecimalFormat("######0.00");
        JspPage modelAndView = new JspPage(request);
        String coinType = request.getParameter("coinType");
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        if (currentUser == null) {
            modelAndView.addObject("code", "200");
            modelAndView.addObject("message", "您还未登陆，请先登陆！");
        } else {
            if (currentUser != null) {
                // 用户虚拟钱包
                Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                        .findVirtualWallet(currentUser.getFid());
                modelAndView.addObject("fvirtualwallets", fvirtualwallets);
            }
            // 币种列表
            List<Map<String, Object>> cointTypeList = new ArrayList<>();
            // 当前交易币种信息
            // Map<String, Object> coinMap = new HashMap<>();
            List<Map<String, Object>> ordersList = new ArrayList<>();
            List<Map<String, Object>> ordersSellList = new ArrayList<>();
            List<Map<String, Object>> bzList = new ArrayList<>();
            try (Mysql mysql = new Mysql()) {
                /*
                 * MyQuery ds = new MyQuery(mysql); ds.
                 * add("select fId, fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY from %s",
                 * "fvirtualcointype"); ds.add("where fstatus=1 and fisOtcCoin = 1"); ds.open();
                 * for (Record record : ds) { cointTypeList.add(record.getItems()); }
                 * 
                 * if (fId == -1) { if (cointTypeList.size() != 0) { coinMap =
                 * cointTypeList.get(0); fId = (int) cointTypeList.get(0).get("fId"); } } else {
                 * MyQuery ds1 = new MyQuery(mysql);
                 * ds1.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice from %s",
                 * "fvirtualcointype"); ds1.add("where fId = %s", fId); ds1.open(); coinMap =
                 * ds1.getCurrent().getItems(); }
                 */
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (Mysql mysql = new Mysql()) {
                // 查询接单广场所有数据
                MyQuery getbuy = new MyQuery(mysql);
                getbuy.add(
                        "SELECT a.fId,a.fCreateTime,a.fAm_fId,a.fUsr_id,a.fUsr_Name,a.fUnitprice,a.fCount,a.fMoney,a.fOrdertype,a.fAdr,a.fSmallprice,a.fBigprice,a.fLowprice,a.fReceipttype,a.fMsg,a.isCertified,a.isRealName,b.counts,ft.fShortName,ft.fId coinId,f.fIsMerchant FROM %s a LEFT JOIN fuser f on a.fUsr_id = f.fId",
                        "t_otcorders");
                getbuy.add(
                        " left join (select fAd_id,count(1) as counts from t_userotcorder group by fAd_id) b ON a.fId = b.fAd_id ");
                getbuy.add("left join %s ft", AppDB.fvirtualcointype);
                getbuy.add(" ON a.fAm_fId = ft.fId");
                getbuy.add(" WHERE a.isUndo = '1'");
                getbuy.add(" AND a.fAm_fId = %s", coinType);
                getbuy.add(" AND a.fOrdertype = '0'");
                getbuy.add(" AND a.fStatus = '0'");
                getbuy.add(" AND a.isCheck = '1'");
                getbuy.add(" AND a.fUsr_id <> '%s'", currentUser.getFid());
                getbuy.add("order by f.fIsMerchant desc,a.fSmallprice desc,a.fCreateTime desc");
                getbuy.open();
                for (Record record : getbuy) {
                    ordersList.add(record.getItems());
                }
                // 查询接单广场所有数据
                MyQuery getSell = new MyQuery(mysql);
                getSell.add(
                        "SELECT a.fId,a.fCreateTime,a.fAm_fId,a.fUsr_Name,a.fUsr_id,a.fUnitprice,a.fCount,a.fMoney,a.fOrdertype,a.fAdr,a.fSmallprice,a.fBigprice,a.fLowprice,a.fReceipttype,a.fMsg,a.isCertified,a.isRealName,b.counts,ft.fShortName,ft.fId,f.fIsMerchant coinId FROM %s a LEFT JOIN fuser f on a.fUsr_id = f.fId",
                        "t_otcorders");
                getSell.add(
                        " left join (select fAd_id,count(1) as counts from t_userotcorder group by fAd_id) b ON a.fId = b.fAd_id ");
                getSell.add("left join %s ft", AppDB.fvirtualcointype);
                getSell.add(" ON a.fAm_fId = ft.fId");
                getSell.add(" WHERE a.isUndo = '1'");
                getSell.add(" AND a.fAm_fId = %s", coinType);
                getSell.add(" AND a.fOrdertype = '1'");
                getSell.add(" AND a.fStatus = '0'");
                getSell.add(" AND a.isCheck = '1'");
                getSell.add(" AND a.fUsr_id <> '%s'", currentUser.getFid());
                getSell.add("order by f.fIsMerchant desc,a.fSmallprice desc,a.fCreateTime desc");
                getSell.open();
                for (Record record : getSell) {
                    ordersSellList.add(record.getItems());

                }
                //查询可otc交易的币种
                MyQuery bz = new MyQuery(mysql);
                bz.add("select fId,fName_en from %s " ,"fvirtualcointype");
                bz.add(" where fisOtcCoin = 1 ");
                bz.open();
                if (!bz.eof()) {
                	for (Record record : bz) {
                        bzList.add(record.getItems());
                    }
				}
                MyQuery coinDs = new MyQuery(mysql);
                coinDs.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY from %s",
                        AppDB.fvirtualcointype);
                coinDs.add("where fId = %s", coinType);
                coinDs.open();
                modelAndView.addObject("coinTypeName", coinDs.getString("fShortName"));
                modelAndView.addObject("coinType", coinType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            modelAndView.addObject("cointTypeList", cointTypeList); // 币种信息list
            // modelAndView.addObject("coinMap", coinMap);// 币种ID
            modelAndView.addObject("login_user", currentUser);
            modelAndView.addObject("ordersList", ordersList);
            modelAndView.addObject("ordersSellList", ordersSellList);
            // modelAndView.addObject("bzList", bzList);
        }
        // modelAndView.setViewName("front/otc/index");
        modelAndView.setJspFile("front/otc/index");
        return modelAndView;
    }

    @RequestMapping(value = { "/otc/otcUserOrder", "/m/otc/otcUserOrder" })
    public ModelAndView otcUserOrder(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "-1") int fId) {
        JspPage modelAndView = new JspPage(request);
        // 当前登录用户
        String coin_id = request.getParameter("coinType");
        Fuser currentUser = GetCurrentUser(request);
        if (currentUser != null) {
            // 用户虚拟钱包
            Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                    .findVirtualWallet(currentUser.getFid());
            modelAndView.addObject("fvirtualwallets", fvirtualwallets);
        }
        // 币种列表
        List<Map<String, Object>> cointTypeList = new ArrayList<>();
        // 当前交易币种信息
        Map<String, Object> coinMap = new HashMap<>();
        List<Map<String, Object>> ordersList = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY from %s", "fvirtualcointype");
            ds.add("where fstatus=1 and fisOtcCoin = 1");
            ds.open();
            for (Record record : ds) {
                cointTypeList.add(record.getItems());
            }

            if (fId == -1) {
                if (cointTypeList.size() != 0) {
                    coinMap = cointTypeList.get(0);
                    fId = (int) cointTypeList.get(0).get("fId");
                }
            } else {
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice from %s", "fvirtualcointype");
                ds1.add("where fId = %s", fId);
                ds1.open();
                coinMap = ds1.getCurrent().getItems();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Mysql mysql = new Mysql()) {
            // 查询接单广场所有数据
            MyQuery getbuy = new MyQuery(mysql);
            getbuy.add(
                    "SELECT o.fId, v.fShortName fShortName,o.isCheck isCheck, o.fCreateTime, o.fAm_fId, o.fUsr_id, fUsr_Name,fUnitprice,fCount,fMoney,fOrdertype,fAdr,fSmallprice,fBigprice,fLowprice,fReceipttype,fMsg,isCertified,isUndo,o.fStatus fStatus FROM %s o",
                    "t_otcorders");
            getbuy.add(" LEFT JOIN %s v ON o.fAm_fId = v.fId", "fvirtualcointype");
            /* getbuy.add(" LEFT JOIN %s uo ON uo.fAd_Id = o.fId", "t_userotcorder"); */
            getbuy.add(" WHERE fUsr_Name = '%s'", currentUser.getFnickName());
            getbuy.add("order by fCreateTime desc");
            getbuy.open();
            for (Record record : getbuy) {
                ordersList.add(record.getItems());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("coin_id", coin_id);
        modelAndView.addObject("cointTypeList", cointTypeList);
        modelAndView.addObject("coinMap", coinMap);
        modelAndView.addObject("login_user", currentUser);
        modelAndView.addObject("ordersList", ordersList);
        // modelAndView.setViewName("front/otc/ctcOrderList");
        modelAndView.setJspFile("front/otc/ctcOrderList");
        return modelAndView;
    }

    @RequestMapping(value = { "/otc/toPublishAd", "/m/otc/toPublishAd" })
    public ModelAndView publishAd(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "-1") int fId) {
        JspPage modelAndView = new JspPage(request);
        String typeVal = request.getParameter("typeVal");
        Double fTotal = null;
        Double ffee = null;
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        if (currentUser != null) {
            // 用户虚拟钱包
            Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                    .findVirtualWallet(currentUser.getFid());
            modelAndView.addObject("fvirtualwallets", fvirtualwallets);
        }
        int fuserId = currentUser.getFid();
        List<Map<String, Object>> binding = new ArrayList<>();
        Map<String, Object> bind = null;
        try (Mysql mysql = new Mysql()) {
            MyQuery dsBank = new MyQuery(mysql);
            dsBank.add("select * from %s where 1=1", "t_userreceipt");
            dsBank.add(" and fUsr_id = '%s'", fuserId);
            dsBank.add(" and fType = '%s'", 0);
            dsBank.open();
            if (!dsBank.eof()) {
                bind = new HashMap<>();
                bind.put("index", "0");
                bind.put("name", "银行转账");
                binding.add(bind);
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select * from %s where 1=1", "t_userreceipt");
            ds1.add(" and fUsr_id = '%s'", fuserId);
            ds1.add(" and fType = '%s'", 1);
            ds1.open();
            if (!ds1.eof()) {
                bind = new HashMap<>();
                bind.put("index", "1");
                bind.put("name", "支付宝");
                binding.add(bind);
                modelAndView.addObject("fr1", ds1.getCurrent());
            }
            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select * from %s where 1=1", "t_userreceipt");
            ds2.add(" and fUsr_id = '%s'", fuserId);
            ds2.add(" and fType = '%s'", 2);
            ds2.open();
            if (!ds2.eof()) {
                bind = new HashMap<>();
                bind.put("index", "2");
                bind.put("name", "微信");
                binding.add(bind);
                modelAndView.addObject("fr2", ds2.getCurrent());
            }
            if (bind == null) {
                if (!dsBank.eof()) {
                    String fBankNumber = dsBank.getCurrent().getString("fBankNumber");
                    if (fBankNumber != "" || fBankNumber != null) {
                        modelAndView.addObject("bankStatus", 0);
                    } else {
                        modelAndView.addObject("bankStatus", 1);
                    }
                } else {
                    modelAndView.addObject("bankStatus", 1);
                }
                modelAndView.addObject("msg", "请先设置收款方式。");
                // modelAndView.setViewName("front/user/userReceiptOption");
                modelAndView.setJspFile("/front/financial/bankCard");
                return modelAndView;
            } else {
                modelAndView.addObject("binding", binding);
            }
        }
        // 币种列表
        List<Map<String, Object>> cointTypeList = new ArrayList<>();
        // 当前交易币种信息
        Map<String, Object> coinMap = new HashMap<>();
        List<Map<String, Object>> ordersbuyList = new ArrayList<>();
        // 0 未完善收款信息 1 已完善收款信息
        String checked = "0";
        String checkedBank = "0";
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY from %s", "fvirtualcointype");
            ds.add("where fstatus=1 and fisOtcCoin = 1");
            ds.open();
            for (Record record : ds) {
                cointTypeList.add(record.getItems());
            }

            if (fId == -1) {
                if (cointTypeList.size() != 0) {
                    coinMap = cointTypeList.get(0);
                    fId = (int) cointTypeList.get(0).get("fId");
                }
            } else {
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice from %s", "fvirtualcointype");
                ds1.add("where fId = %s", fId);
                ds1.open();
                coinMap = ds1.getCurrent().getItems();
            }
            // 发布广告查看自己的余额
            MyQuery buy = new MyQuery(mysql);
            buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
            buy.add(" WHERE fuid = '%s'", currentUser.getFid());
            buy.add(" AND fVi_fId = '%s'", typeVal);
            buy.open();
            fTotal = buy.getCurrent().getDouble("fTotal");
            MyQuery coinName = new MyQuery(mysql);
            coinName.add("select fId,fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY from %s",
                    AppDB.fvirtualcointype);
            coinName.add(" where fId= %s", typeVal);
            coinName.open();
            modelAndView.add("fShortName", coinName.getString("fShortName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Mysql mysql = new Mysql()) {
            // 查询接单广场所有数据
            /*
             * MyQuery getbuy = new MyQuery(mysql); getbuy.
             * add("SELECT fId,fCreateTime,fAm_fId,fUsr_id,fUnitprice,fCount,fMoney,fOrdertype,fAdr,fSmallprice,fBigprice,fLowprice,fReceipttype,fMsg,isCertified,isUndo,fStatus FROM %s"
             * , "t_otcorders"); getbuy.add(" WHERE isUndo = '1'");
             * getbuy.add(" AND fOrdertype = '0'"); getbuy.add(" AND fStatus = '0'");
             * getbuy.add("order by fCreateTime desc"); getbuy.open(); for (Record record :
             * getbuy) { ordersbuyList.add(record.getItems()); }
             */

            MyQuery ds0 = new MyQuery(mysql);
            ds0.add("select fId,fUsr_id,fType,fName,fAccount,fImgUrl from %s", "t_userreceipt");
            ds0.add(" where 1 = 1");
            ds0.add(" and fUsr_id = '%s'", currentUser.getFid());
            ds0.add(" AND fType in (0,1,2)");
            ds0.open();
            if (ds0.size() < 1)
                checked = "0";
            else
                checked = "1";
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select FUs_fId,fName,fBankNumber from %s where 1=1", "fbankinfo_withdraw");
            ds1.add(" and FUs_fId = '%s'", currentUser.getFid());
            ds1.open();
            if (ds1.size() == 0)
                checkedBank = "0";
            else
                checkedBank = "1";
            MyQuery hl = new MyQuery(mysql);
            hl.add("select * from %s ", "fotcfees");
            hl.add("where fvid = '%s' ", typeVal);
            hl.add("and flevel = 1");
            hl.open();
            ffee = hl.getDouble("ffee");
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("fTotal", fTotal);
        modelAndView.addObject("typeVal", typeVal);
        modelAndView.addObject("cointTypeList", cointTypeList);
        modelAndView.addObject("coinMap", coinMap);
        modelAndView.addObject("login_user", currentUser);
        // modelAndView.addObject("ordersList", ordersbuyList);
        modelAndView.addObject("checked", checked);
        modelAndView.addObject("checkedBank", checkedBank);
        modelAndView.addObject("ffee", ffee);
        // modelAndView.setViewName("front/otc/publishad");
        modelAndView.setJspFile("front/otc/publishad");
        return modelAndView;
    }

    // 添加广告信息
    @ResponseBody
    @RequestMapping(value = { "/otc/publishAd", "/m/otc/publishAd" }, produces = JsonEncode)
    public String cancel(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        String real;
        if (currentUser.getFhasRealValidate())
            real = "0";
        else
            real = "1";
        int id = currentUser.getFid();
        if (id < 0) {
            resultJson.accumulate("code", 2);
            resultJson.accumulate("msg", "用户未登录！");
        } else {
            try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
                MyQuery ds = new MyQuery(handle);
                ds.add("SELECT fId,fCreateTime,isRealName,fAm_fId,fUsr_id,fUsr_Name,fUnitprice,fCount,fMoney,fOrdertype,fAdr,fSmallprice,fBigprice,fLowprice,fReceipttype,fMsg,isCertified,isUndo,fStatus,isCheck FROM %s",
                        "t_otcorders");
                if ("edit".equals(request.getParameter("orderType_"))) {
                    ds.add(" where fId = '%s'", request.getParameter("fid_"));
                    ds.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                    ds.open();
                    ds.edit();
                    ds.setField("fAm_fId", request.getParameter("Btype_"));
                    ds.setField("fUsr_id", currentUser.getFid());
                    ds.setField("isRealName", real);
                    ds.setField("fUnitprice", request.getParameter("price_"));
                    ds.setField("fCount", 0);
                    ds.setField("fMoney", 0);
                    ds.setField("fOrdertype", request.getParameter("type_"));
                    ds.setField("fAdr", request.getParameter("adr_"));
                    ds.setField("fSmallprice", request.getParameter("fSmallprice_"));
                    ds.setField("fBigprice", request.getParameter("fBigprice_"));
                    ds.setField("fLowprice", request.getParameter("fLowprice_"));
                    ds.setField("fReceipttype", request.getParameter("fReceipttype_"));
                    ds.setField("fMsg", request.getParameter("msg_"));
                    ds.setField("isCertified", request.getParameter("isCertified_"));
                    ds.setField("isUndo", 1);
                    ds.setField("fStatus", 0);
                    ds.setField("fUsr_Name", currentUser.getFnickName());
                    ds.setField("isCheck", 0);
                    ds.post();
                    resultJson.accumulate("code", 0);
                    resultJson.accumulate("msg", "广告修改成功！");
                } else {
                    ds.setMaximum(0);
                    ds.open();
                    ds.append();
                    ds.setField("fCreateTime", TDateTime.Now());
                    ds.setField("fAm_fId", request.getParameter("Btype_"));
                    ds.setField("fUsr_id", currentUser.getFid());
                    ds.setField("isRealName", real);
                    ds.setField("fUnitprice", request.getParameter("price_"));
                    ds.setField("fCount", 0);
                    ds.setField("fMoney", 0);
                    ds.setField("fOrdertype", request.getParameter("type_"));
                    ds.setField("fAdr", request.getParameter("adr_"));
                    ds.setField("fSmallprice", request.getParameter("fSmallprice_"));
                    ds.setField("fBigprice", request.getParameter("fBigprice_"));
                    ds.setField("fLowprice", request.getParameter("fLowprice_"));
                    ds.setField("fReceipttype", request.getParameter("fReceipttype_"));
                    ds.setField("fMsg", request.getParameter("msg_"));
                    ds.setField("isCertified", request.getParameter("isCertified_"));
                    ds.setField("isUndo", 1);
                    ds.setField("fStatus", 0);
                    ds.setField("fUsr_Name", currentUser.getFnickName());
                    ds.setField("isCheck", 0);
                    ds.post();
                    resultJson.accumulate("code", 0);
                    resultJson.accumulate("msg", "广告发布成功！");
                }

                tx.commit();
            } catch (Exception e) {
                resultJson.accumulate("code", 1);
                resultJson.accumulate("msg", "广告发布失败！");
            }
        }
        return resultJson.toString();
    }

    @RequestMapping("/m/otc/orderMenu")
    public ModelAndView orderList(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        String coin_id = request.getParameter("coinType");
        modelAndView.addObject("coin_id", coin_id);
        modelAndView.setJspFile("/front/otc/orderMenu");
        return modelAndView;
    }

    @RequestMapping("/m/otc/otcMenu")
    public ModelAndView otcMenu(HttpServletRequest request, @RequestParam(required = false) String menuFlag) {
        request.getSession().setAttribute("menuFlag", menuFlag);
        List<Map<String, Object>> list = new ArrayList<>();
        JspPage modelAndView = new JspPage(request);
        try (Mysql handle = new Mysql()) {
            MyQuery myds = new MyQuery(handle);
            myds.add("select fId,fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY,fisActive from %s", AppDB.fvirtualcointype);
            myds.add(" where fstatus=1 and fisOtcCoin = 1");
            myds.open();
            while (myds.fetch()) {
                Map<String, Object> ctcMap = new HashMap<String, Object>();
                ctcMap.put("coinType", myds.getString("fId"));
                ctcMap.put("fShortName", myds.getString("fShortName"));
                ctcMap.put("fisActive", myds.getBoolean("fisActive"));
                list.add(ctcMap);
            }
        }
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
        modelAndView.add("menuList", list);
        modelAndView.setJspFile("/front/otc/otcMenu");
        return modelAndView;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/m/user/api/otcMenuList", "/user/api/otcMenuList" }, produces = JsonEncode)
    public String apiupdateOutAddress(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql handle = new Mysql()) {
            MyQuery myds = new MyQuery(handle);
            myds.add("select fId,fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY from %s", AppDB.fvirtualcointype);
            myds.add(" where fstatus=1 and fisOtcCoin = 1");
            myds.open();
            while (myds.fetch()) {
                Map<String, Object> ctcMap = new HashMap<String, Object>();
                ctcMap.put("coinType", myds.getString("fId"));
                ctcMap.put("fShortName", myds.getString("fShortName"));
                list.add(ctcMap);
            }
        }
        jsonObject.accumulate("menuList", list);
        return jsonObject.toString();
    }
    
    @ResponseBody
    @RequestMapping(value = { "/m/user/api/otcList", "/user/api/otcList" }, produces = JsonEncode)
    public String apiupdateOutAddress(HttpServletRequest request,
            @RequestParam(required = true) int coinType) {
        JSONObject jsonObject = new JSONObject();
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        if (currentUser == null) {
            jsonObject.accumulate("code", "-1");
            jsonObject.accumulate("msg", "您还未登陆，请先登陆！");
        } else {
            jsonObject.accumulate("code", "0");
            jsonObject.accumulate("msg", "");
            // 当前交易币种信息
            List<Map<String, Object>> ordersList = new ArrayList<>();
            List<Map<String, Object>> ordersSellList = new ArrayList<>();
            List<Map<String, Object>> bzList = new ArrayList<>();
            try (Mysql mysql = new Mysql()) {
                // 查询接单广场所有数据
                MyQuery getbuy = new MyQuery(mysql);
                getbuy.add(
                        "SELECT a.fId,a.fCreateTime,a.fAm_fId,a.fUsr_id,a.fUsr_Name,a.fUnitprice,a.fCount,a.fMoney,a.fOrdertype,a.fAdr,a.fSmallprice,a.fBigprice,a.fLowprice,a.fReceipttype,a.fMsg,a.isCertified,a.isRealName,b.counts,ft.fShortName,ft.fId coinId FROM %s a",
                        "t_otcorders");
                getbuy.add(
                        " left join (select fAd_id,count(1) as counts from t_userotcorder group by fAd_id) b ON a.fId = b.fAd_id ");
                getbuy.add("left join %s ft", AppDB.fvirtualcointype);
                getbuy.add(" ON a.fAm_fId = ft.fId");
                getbuy.add(" WHERE a.isUndo = '1'");
                getbuy.add(" AND a.fAm_fId = %s", coinType);
                getbuy.add(" AND a.fOrdertype = '0'");
                getbuy.add(" AND a.fStatus = '0'");
                getbuy.add(" AND a.isCheck = '1'");
                getbuy.add(" AND a.fUsr_id <> '%s'", currentUser.getFid());
                getbuy.add("order by a.fCreateTime desc");
                getbuy.open();
                for (Record record : getbuy) {
                    ordersList.add(record.getItems());
                }
                // 查询接单广场所有数据
                MyQuery getSell = new MyQuery(mysql);
                getSell.add(
                        "SELECT a.fId,a.fCreateTime,a.fAm_fId,a.fUsr_Name,a.fUsr_id,a.fUnitprice,a.fCount,a.fMoney,a.fOrdertype,a.fAdr,a.fSmallprice,a.fBigprice,a.fLowprice,a.fReceipttype,a.fMsg,a.isCertified,a.isRealName,b.counts,ft.fShortName,ft.fId coinId FROM %s a",
                        "t_otcorders");
                getSell.add(
                        " left join (select fAd_id,count(1) as counts from t_userotcorder group by fAd_id) b ON a.fId = b.fAd_id ");
                getSell.add("left join %s ft", AppDB.fvirtualcointype);
                getSell.add(" ON a.fAm_fId = ft.fId");
                getSell.add(" WHERE a.isUndo = '1'");
                getSell.add(" AND a.fAm_fId = %s", coinType);
                getSell.add(" AND a.fOrdertype = '1'");
                getSell.add(" AND a.fStatus = '0'");
                getSell.add(" AND a.isCheck = '1'");
                getSell.add(" AND a.fUsr_id <> '%s'", currentUser.getFid());
                getSell.add("order by a.fCreateTime desc");
                getSell.open();
                for (Record record : getSell) {
                    ordersSellList.add(record.getItems());

                }
                //查询可otc交易的币种
                MyQuery bz = new MyQuery(mysql);
                bz.add("select fId,fName_en from %s " ,"fvirtualcointype");
                bz.add(" where fisOtcCoin = 1 ");
                bz.open();
                if (!bz.eof()) {
                    for (Record record : bz) {
                        bzList.add(record.getItems());
                    }
                }
                MyQuery coinDs = new MyQuery(mysql);
                coinDs.add("select fId, fShortName,fctcSellPrice,fctcBuyPrice,fcurrentCNY from %s",
                        AppDB.fvirtualcointype);
                coinDs.add("where fId = %s", coinType);
                coinDs.open();
                jsonObject.accumulate("coinTypeName", coinDs.getString("fShortName"));
                jsonObject.accumulate("coinType", coinType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonObject.accumulate("login_user", currentUser);
            jsonObject.accumulate("ordersList", ordersList);
            jsonObject.accumulate("ordersSellList", ordersSellList);
        }
        return jsonObject.toString();
    }

    // 撤销广告信息
    @ResponseBody
    @RequestMapping(value = { "/otc/undoAd", "/m/otc/undoAd" }, produces = JsonEncode)
    public String undoAd(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        int id = currentUser.getFid();
        if (id < 0) {
            resultJson.accumulate("code", 2);
            resultJson.accumulate("msg", "用户未登录！");
        } else {
            try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
                MyQuery ds = new MyQuery(handle);
                ds.add("SELECT fId,fCreateTime,isRealName,fAm_fId,fUsr_id,fUsr_Name,fUnitprice,fCount,fMoney,fOrdertype,fAdr,fSmallprice,fBigprice,fLowprice,fReceipttype,fMsg,isCertified,isUndo,fStatus FROM %s",
                        "t_otcorders");
                ds.add(" where 1 = 1");
                ds.add(" and fId = '%s'", request.getParameter("fid_"));
                ds.open();
                if (!ds.eof()) {
                    ds.edit();
                    ds.setField("fStatus", 3);
                    ds.setField("isUndo", 0);
                    ds.post();
                    resultJson.accumulate("code", 0);
                    resultJson.accumulate("msg", "广告撤销成功！");
                } else {
                    resultJson.accumulate("code", 1);
                    resultJson.accumulate("msg", "广告撤销失败，原因：广告编号有误！");
                }

                tx.commit();
            } catch (Exception e) {
                resultJson.accumulate("code", 1);
                resultJson.accumulate("msg", "广告撤销失败！");
            }
        }
        return resultJson.toString();
    }

    @RequestMapping(value = { "/otc/editPublishAd", "/m/otc/editPublishAd" })
    public ModelAndView editPublishAd(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "-1") int fId) {
        JspPage modelAndView = new JspPage(request);
        Double fTotal = null;
        int fAm_fId = 0;
        // 当前登录用户
        Fuser currentUser = GetCurrentUser(request);
        Map<String, Object> ad = new HashMap<>();
        // 0 未完善收款信息 1 已完善收款信息
        String checked = "0";
        try (Mysql mysql = new Mysql()) {
            MyQuery getAd = new MyQuery(mysql);
            getAd.add(
                    "select fId,fCreateTime,isRealName,fAm_fId,fUsr_id,fUsr_Name,fUnitprice,fCount,fMoney,fOrdertype,fAdr,fSmallprice,fBigprice,fLowprice,fReceipttype,fMsg,isCertified,isUndo,fStatus from %s",
                    "t_otcorders");
            getAd.add(" where 1 = 1");
            getAd.add(" and fId = '%s'", request.getParameter("fid_"));
            getAd.open();
            if (!getAd.eof()) {
                fAm_fId = getAd.getInt("fAm_fId");
                ad = getAd.getCurrent().getItems();
            }

            MyQuery ds0 = new MyQuery(mysql);
            ds0.add("select fId,fUsr_id,fType,fName,fAccount,fImgUrl from %s", "t_userreceipt");
            ds0.add(" where 1 = 1");
            ds0.add(" and fUsr_id = '%s'", currentUser.getFid());
            ds0.add(" AND fType in (0,1,2)");
            ds0.open();
            if (ds0.size() < 3)
                checked = "0";
            else
                checked = "1";
            MyQuery buy = new MyQuery(mysql);
            buy.add("SELECT fId,fVi_fId,fTotal,fFrozen,fLastUpdateTime,fuid FROM %s", "fvirtualwallet");
            buy.add(" WHERE fuid = '%s'", currentUser.getFid());
            buy.add(" AND fVi_fId = '%s'", fAm_fId);
            buy.open();
            fTotal = buy.getCurrent().getDouble("fTotal");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int fuserId = currentUser.getFid();
        List<Map<String, Object>> binding = new ArrayList<>();
        Map<String, Object> bind = null;
        try (Mysql mysql = new Mysql()) {
            MyQuery dsBank = new MyQuery(mysql);
            dsBank.add("select * from %s where 1=1", "fbankinfo_withdraw");
            dsBank.add(" and FUs_fId = '%s'", fuserId);
            dsBank.setMaximum(1);
            dsBank.open();
            if (!dsBank.eof()) {
                bind = new HashMap<>();
                bind.put("index", "0");
                bind.put("name", "银行转账");
                binding.add(bind);
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select * from %s where 1=1", "t_userreceipt");
            ds1.add(" and fUsr_id = '%s'", fuserId);
            ds1.add(" and fType = '%s'", 1);
            ds1.open();
            if (!ds1.eof()) {
                bind = new HashMap<>();
                bind.put("index", "1");
                bind.put("name", "支付宝");
                binding.add(bind);
                modelAndView.addObject("fr1", ds1.getCurrent());
            }
            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select * from %s where 1=1", "t_userreceipt");
            ds2.add(" and fUsr_id = '%s'", fuserId);
            ds2.add(" and fType = '%s'", 2);
            ds2.open();
            if (!ds2.eof()) {
                bind = new HashMap<>();
                bind.put("index", "2");
                bind.put("name", "微信");
                binding.add(bind);
                modelAndView.addObject("fr2", ds2.getCurrent());
            }
            if (bind == null) {
                if (!dsBank.eof()) {
                    String fBankNumber = dsBank.getCurrent().getString("fBankNumber");
                    if (fBankNumber != "" || fBankNumber != null) {
                        modelAndView.addObject("bankStatus", 0);
                    } else {
                        modelAndView.addObject("bankStatus", 1);
                    }
                } else {
                    modelAndView.addObject("bankStatus", 1);
                }
                modelAndView.addObject("msg", "请先设置收款方式。");
                // modelAndView.setViewName("front/user/userReceiptOption");
                modelAndView.setJspFile("front/user/userReceiptOption");
                return modelAndView;
            } else {
                modelAndView.addObject("binding", binding);
            }
        }
        modelAndView.addObject("login_user", currentUser);
        modelAndView.addObject("checked", checked);
        modelAndView.addObject("ad", ad);
        modelAndView.addObject("orderType_", "edit");
        modelAndView.addObject("fTotal", fTotal);
        modelAndView.addObject("typeVal", fAm_fId);
        modelAndView.addObject("types_", request.getParameter("types_"));
        // modelAndView.setViewName("front/otc/publishad");
        modelAndView.setJspFile("front/otc/publishad");
        return modelAndView;
    }

    @RequestMapping(value = "m/otc/OtcApply")
    public ModelAndView OtcApply() throws IOException {
        JspPage modelandview = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        Fuser fuser = frontUserService.findById(user.getFid());
        modelandview.addObject("userId", fuser.getFloginName());
        if (fuser != null) {
            if(!fuser.isFhasImgValidate() || fuser.getFaudit() != 1) {
            	modelandview.addObject("msg", "请先完成实名认证");
            	modelandview.setJspFile("front/financial/index2");
                return modelandview;
            }
            if (fuser.getfSellerStatus() != null && fuser.getfSellerStatus() == 1 && fuser.getfIsMerchant()<=0) {
            	modelandview.addObject("msg", "用户商家认证审核中");
            	modelandview.setJspFile("front/financial/index2");
                return modelandview;
            }
            if (fuser.getfIsMerchant()>0) {
            	modelandview.addObject("Level", fuser.getfSellerLevel());
            	modelandview.addObject("time", fuser.getfSellerTime());
            	modelandview.addObject("status", fuser.getfSellerStatus());
            	modelandview.addObject("name", fuser.getfSellerName());
            	modelandview.addObject("IsMerchant", fuser.getfIsMerchant());
            	modelandview.setJspFile("front/otc/shopcenter");
                return modelandview;
            }
        }
        modelandview.setJspFile("front/otc/OtcApply");
        return modelandview;
    }
    
    
    @RequestMapping(value = "m/otc/OtcApply1")
    public ModelAndView OtcApply1() throws IOException {
        JspPage modelandview = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        Fuser fuser = frontUserService.findById(user.getFid());
        modelandview.addObject("userId", fuser.getFloginName());
        modelandview.setJspFile("front/otc/OtcApply");
        return modelandview;
    }

    @ResponseBody
    @RequestMapping(value = "m/otc/OtcToApply", produces = { JsonEncode })
    public String OtcToApply(@RequestParam(required = false, defaultValue = "0") int isMerchant)
            throws IOException {
        JSONObject json = new JSONObject();
        Fuser user = GetCurrentUser(request);
        Fuser fuser = frontUserService.findById(user.getFid());
        if (fuser != null) {
            if(!fuser.isFhasImgValidate() || fuser.getFaudit() != 2) {
                json.accumulate("status", 300);
                json.accumulate("msg", "用户未完成实名认证");
                return json.toString();
            }
        }
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT fId,fIsMerchant from fuser WHERE fId = %s", user.getFid());
            ds.open();
            ds.edit();
            if (isMerchant == 0) {
                ds.setField("fIsMerchant", "-1");
                ds.post();
                json.accumulate("status", 200);
                json.accumulate("msg", "已提交申请，请耐心等候");
            } else if (isMerchant == -1) {
                json.accumulate("status", 204);
                json.accumulate("msg", "您已提交过申请，请勿重复提交");
            } else if (isMerchant == 1) {
                json.accumulate("status", 204);
                json.accumulate("msg", "您已经是商户");
            } else {
                json.accumulate("status", 204);
                json.accumulate("msg", "异常操作！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.accumulate("status", 500);
            json.accumulate("msg", "网络错误");
        }
        return json.toString();
    }
    
    @RequestMapping(value = "m/otc/OtcOrderInfo")
    public ModelAndView OtcOrderInfo() throws IOException {
        JspPage modelandview = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelandview.setJspFile("front/otc/orderInfo_ry");
        return modelandview;
    }
}
