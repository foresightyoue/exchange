package com.huagu.vcoin.main.controller.admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinEnum;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Ffees;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.FfeesService;
import com.huagu.vcoin.main.service.admin.TradeMappingService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSender;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

@Controller
public class TradeMappingController extends BaseController {
    @Autowired
    private TradeMappingService tradeMappingService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FfeesService feesService;
    @Autowired
    private MessageSender messageSender;

    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/tradeMappingList")
    public ModelAndView tradeMappingList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/tradeMappingList");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        // if(keyWord != null && keyWord.trim().length() >0){
        // filter.append("and (fname like '%"+keyWord+"%' or \n");
        // filter.append("furl like '%"+keyWord+"%' ) \n");
        // modelAndView.addObject("keywords", keyWord);
        // }
        // if(orderField != null && orderField.trim().length() >0){
        // filter.append("order by "+orderField+"\n");
        // }else{
        // filter.append("order by fcreateTime \n");
        // }
        //
        // if(orderDirection != null && orderDirection.trim().length() >0){
        // filter.append(orderDirection+"\n");
        // }else{
        // filter.append("desc \n");
        // }

        List<Ftrademapping> list = this.tradeMappingService.list((currentPage - 1) * numPerPage, numPerPage,
                filter + "", true);
        modelAndView.addObject("tradeMappingList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "tradeMappingList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Ftrademapping", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goTradeMappingJSP")
    public ModelAndView goLimittradeJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Ftrademapping ftradeMapping = this.tradeMappingService.findById(fid);
            modelAndView.addObject("ftradeMapping", ftradeMapping);

            String filter = "where ftrademapping.fid=" + fid + " order by flevel asc";
            List<Ffees> allFees = this.feesService.list(0, 0, filter, false);
            DecimalFormat df = new DecimalFormat("#,##0.00%");
            List<Map<String, Object>> list = new ArrayList<>();
            for (Ffees Ffee : allFees) {
                Map<String, Object> ma = new HashMap<>();
                ma.put("fid", Ffee.getFid());
                String ffee = df.format(Ffee.getFfee());
                ma.put("ffee", ffee);
                ma.put("flevel", Ffee.getFlevel());
                String buyfee = df.format(Ffee.getFbuyfee());
                ma.put("fbuyfee", buyfee);
                list.add(ma);
            }
            modelAndView.add("list", list);
            // modelAndView.addObject("allFees", allFees);
        }

        List<Fvirtualcointype> fvirtualcointypes = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        modelAndView.addObject("fvirtualcointypes", fvirtualcointypes);

        List<Fvirtualcointype> fvirtualcointypes_fb = this.virtualCoinService.findAll(CoinTypeEnum.COIN_VALUE, 1);
        modelAndView.addObject("fvirtualcointypes_fb", fvirtualcointypes_fb);

        Map<Integer, String> type = new HashMap<Integer, String>();
        type.put(1, CoinEnum.getEnumString(1));
        type.put(2, CoinEnum.getEnumString(2));
        type.put(3, CoinEnum.getEnumString(3));
        modelAndView.addObject("type", type);

        return modelAndView;
    }

    @RequestMapping("ssadmin/saveTradeMapping")
    public ModelAndView saveTradeMapping() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        int fvirtualcointype1 = Integer.parseInt(request.getParameter("fvirtualcointype1"));
        int fvirtualcointype2 = Integer.parseInt(request.getParameter("fvirtualcointype2"));
        String filter = "where fvirtualcointypeByFvirtualcointype1.fid=" + fvirtualcointype1
                + " and fvirtualcointypeByFvirtualcointype2.fid=" + fvirtualcointype2;
        int count = this.adminService.getAllCount("Ftrademapping", filter);
        if (count > 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "匹配记录重复，请更正");
            return modelAndView;
        }
        int count1 = Integer.parseInt(request.getParameter("fcount1"));
        int count2 = Integer.parseInt(request.getParameter("fcount2"));
        if (count1 > 6 || count1 < 1 || count2 > 6 || count2 < 1) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "小数位最小1位，最大6位");
            return modelAndView;
        }
        Ftrademapping tradeMapping = new Ftrademapping();

        /*
         * tradeMapping.setFtigerUid(request.getParameter("ftigerUid"));
         * tradeMapping.setFintrolRate(request.getParameter("fintrolRate")); String
         * fisIntrol = request.getParameter("fisIntrol"); if(fisIntrol != null &&
         * fisIntrol.trim().length() >0){ tradeMapping.setFisIntrol(true); }else{
         * tradeMapping.setFisIntrol(false); } String fislimit =
         * request.getParameter("fislimit"); if(fislimit != null &&
         * fislimit.trim().length() >0){ tradeMapping.setFislimit(true); }else{
         * tradeMapping.setFislimit(false); }
         */

        tradeMapping.setFtype(Integer.parseInt(request.getParameter("ftype")));
        // tradeMapping.setFtraderate(Double.valueOf(request.getParameter("ftraderate")));

        tradeMapping.setFcount1(count1);
        tradeMapping.setFcount2(count2);
        tradeMapping.setFmaxtimes(Integer.parseInt(request.getParameter("fmaxtimes")));
        tradeMapping.setFminBuyAmount(Double.valueOf(request.getParameter("fminBuyAmount")));
        tradeMapping.setFminBuyCount(Double.valueOf(request.getParameter("fminBuyCount")));
        tradeMapping.setFminBuyPrice(Double.valueOf(request.getParameter("fminBuyPrice")));

        tradeMapping.setFmaxBuyCount(Double.valueOf(request.getParameter("fmaxBuyCount")));
        tradeMapping.setFmaxSellCount(Double.valueOf(request.getParameter("fmaxSellCount")));

        tradeMapping.setFminSellAmount(Double.valueOf(request.getParameter("fminSellAmount")));
        tradeMapping.setFminSellCount(Double.valueOf(request.getParameter("fminSellCount")));
        tradeMapping.setFminSellPrice(Double.valueOf(request.getParameter("fminSellPrice")));

        tradeMapping.setFprice(Double.valueOf(request.getParameter("fprice")));
        tradeMapping.setFstatus(TrademappingStatusEnum.FOBBID);
        tradeMapping.setFtradeTime(request.getParameter("ftradeTime"));
        Fvirtualcointype cointype1 = this.virtualCoinService.findById(fvirtualcointype1);
        Fvirtualcointype cointype2 = this.virtualCoinService.findById(fvirtualcointype2);
        tradeMapping.setFvirtualcointypeByFvirtualcointype1(cointype1);
        tradeMapping.setFvirtualcointypeByFvirtualcointype2(cointype2);
        tradeMapping.setFtradedesc(cointype2.getfShortName()+"/"+cointype1.getfShortName());
        this.tradeMappingService.saveObj(tradeMapping);

        for (int i = 1; i <= Constant.VIP; i++) {
            Ffees fees = new Ffees();
            fees.setFlevel(i);
            fees.setFbuyfee(0d);
            fees.setFtrademapping(tradeMapping);
            fees.setFfee(0d);
            this.feesService.saveObj(fees);
        }

        this.messageSender.publish(ChannelConstant.expireCache, "");

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "新增成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteTradeMapping")
    public ModelAndView deleteTradeMapping() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        Ftrademapping tradeMapping = this.tradeMappingService.findById(fid);
        tradeMapping.setFstatus(TrademappingStatusEnum.FOBBID);
        this.tradeMappingService.updateObj(tradeMapping);

        this.messageSender.publish(ChannelConstant.expireCache, "");

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "禁用成功，请重启TOMCAT");
        return modelAndView;
    }

    @RequestMapping("ssadmin/goTradeMapping")
    public ModelAndView goTradeMapping() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        Ftrademapping tradeMapping = this.tradeMappingService.findById(fid);
        tradeMapping.setFstatus(TrademappingStatusEnum.ACTIVE);
        this.tradeMappingService.updateObj(tradeMapping);

        this.messageSender.publish(ChannelConstant.expireCache, "");

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "启用成功，请重启TOMCAT");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateTradeMapping")
    public ModelAndView updateTradeMapping() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        int count1 = Integer.parseInt(request.getParameter("fcount1"));
        int count2 = Integer.parseInt(request.getParameter("fcount2"));
        if (count1 > 6 || count1 < 1 || count2 > 6 || count2 < 1) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "小数位最小1位，最大6位");
            return modelAndView;
        }
        Ftrademapping tradeMapping = this.tradeMappingService.findById(fid);
        tradeMapping.setFcount1(count1);
        tradeMapping.setFcount2(count2);
        tradeMapping.setFminBuyAmount(Double.valueOf(request.getParameter("fminBuyAmount")));
        tradeMapping.setFminBuyCount(Double.valueOf(request.getParameter("fminBuyCount")));
        tradeMapping.setFminBuyPrice(Double.valueOf(request.getParameter("fminBuyPrice")));

        tradeMapping.setFminSellAmount(Double.valueOf(request.getParameter("fminSellAmount")));
        tradeMapping.setFminSellCount(Double.valueOf(request.getParameter("fminSellCount")));
        tradeMapping.setFminSellPrice(Double.valueOf(request.getParameter("fminSellPrice")));

        tradeMapping.setFmaxBuyCount(Double.valueOf(request.getParameter("fmaxBuyCount")));
        tradeMapping.setFmaxSellCount(Double.valueOf(request.getParameter("fmaxSellCount")));

        tradeMapping.setFmaxtimes(Integer.parseInt(request.getParameter("fmaxtimes")));
        tradeMapping.setFprice(Double.valueOf(request.getParameter("fprice")));
        // tradeMapping.setFstatus(TrademappingStatusEnum.FOBBID);
        tradeMapping.setFtradeTime(request.getParameter("ftradeTime"));
        tradeMapping.setFisActive(request.getParameter("fisActive")==null? false : request.getParameter("fisActive").equals("on"));

        /*
         * tradeMapping.setFtigerUid(request.getParameter("ftigerUid"));
         * tradeMapping.setFintrolRate(request.getParameter("fintrolRate")); String
         * fisIntrol = request.getParameter("fisIntrol"); if(fisIntrol != null &&
         * fisIntrol.trim().length() >0){ tradeMapping.setFisIntrol(true); }else{
         * tradeMapping.setFisIntrol(false); } String fislimit =
         * request.getParameter("fislimit"); if(fislimit != null &&
         * fislimit.trim().length() >0){ tradeMapping.setFislimit(true); }else{
         * tradeMapping.setFislimit(false); }
         */

        tradeMapping.setFtype(Integer.parseInt(request.getParameter("ftype")));
        tradeMapping.setFtradedesc(request.getParameter("ftradedesc"));

        this.tradeMappingService.updateObj(tradeMapping);

        this.messageSender.publish(ChannelConstant.expireCache, "");

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateTradeFee")
    public ModelAndView updateTradeFee() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fid"));
        List<Ffees> all = this.feesService.findByProperty("ftrademapping.fid", fid);

        // add by hank
        for (Ffees ffees : all) {
            String feeKey = "fee" + ffees.getFid();
            String buyfeeKey = "fbuyfee" + ffees.getFid();
            double fee = Double.valueOf(request.getParameter(feeKey));
            double buyfee = Double.valueOf(request.getParameter(buyfeeKey));

            if (fee >= 1 || fee < 0 || buyfee >= 1 || buyfee < 0) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "手续费率只能是小于1的小数！");
                return modelAndView;
            }
        }

        for (Ffees ffees : all) {
            String feeKey = "fee" + ffees.getFid();
            String buyfeeKey = "fbuyfee" + ffees.getFid();
            double fee = Double.valueOf(request.getParameter(feeKey));
            double buyfee = Double.valueOf(request.getParameter(buyfeeKey));
            ffees.setFfee(fee);
            ffees.setFbuyfee(buyfee);
            this.feesService.updateObj(ffees);
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "更新成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("#,##0.00\u2030");
        System.out.println(df.format(1231423.3823));
    }
}
