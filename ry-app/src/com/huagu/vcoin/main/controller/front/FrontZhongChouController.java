package com.huagu.vcoin.main.controller.front;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.SubscriptionTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fsubscription;
import com.huagu.vcoin.main.model.Fsubscriptionlog;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.SubscriptionLogService;
import com.huagu.vcoin.main.service.admin.SubscriptionService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.PaginUtil;
import com.huagu.vcoin.util.Utils;

import net.sf.json.JSONObject;

@Controller
public class FrontZhongChouController extends BaseController {
    @Autowired
    private SubscriptionLogService subscriptionLogService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userSerivce;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    @RequestMapping("/crowd/index")
    public ModelAndView index(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);

        String filter = "where ftype=" + SubscriptionTypeEnum.RMB + "  order by fid desc";
        List<Fsubscription> fsubscriptions = this.subscriptionService.list(0, 0, filter, false);
        if (fsubscriptions == null || fsubscriptions.size() == 0) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        long now = Utils.getTimestamp().getTime();
        for (Fsubscription fsubscription : fsubscriptions) {
            String status = "";
            if (fsubscription.getFbeginTime().getTime() > now) {
                status = "未开始";
            }
            if (fsubscription.getFbeginTime().getTime() < now && fsubscription.getFendTime().getTime() > now) {
                status = "进行中";
            }
            if (fsubscription.getFendTime().getTime() < now) {
                status = "已结束";
            }
            fsubscription.setFstatus(status);
        }

        modelAndView.addObject("fsubscriptions", fsubscriptions);
        modelAndView.setJspFile("front/zhongchou/index");
        return modelAndView;
    }

    @RequestMapping(value = "/app/crowd/subIndex", produces = { JsonEncode })
    @ResponseBody
    public String subIndex(HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        JspPage modelAndView = new JspPage(request);

        String filter = "where ftype=" + SubscriptionTypeEnum.RMB + "  order by fid desc";
        List<Fsubscription> fsubscriptions = this.subscriptionService.list(0, 0, filter, false);
        if (fsubscriptions == null || fsubscriptions.size() == 0) {
            return jsonObject.toString();
        }

        long now = Utils.getTimestamp().getTime();
        for (Fsubscription fsubscription : fsubscriptions) {
            JSONObject js = new JSONObject();
            js.accumulate("ftitle", fsubscription.getFtitle());
            js.accumulate("fAlreadyByCount", fsubscription.getfAlreadyByCount());
            js.accumulate("ftotal", fsubscription.getFtotal());
            String status = "";
            if (!fsubscription.getFisopen()) {
                status = "0";
            }
            if (fsubscription.getFbeginTime().getTime() > now) {
                status = "0";
            }
            if (fsubscription.getFbeginTime().getTime() < now && fsubscription.getFendTime().getTime() > now) {
                status = "1";
            }
            if (fsubscription.getFendTime().getTime() < now) {
                status = "2";
            }
            js.accumulate("status", status);
            jsonObject.accumulate("subList", js);
        }

        return jsonObject.toString();
    }

    @RequestMapping(value = "/app/crowd/viewApp", produces = { JsonEncode })
    @ResponseBody
    public String viewApp(HttpServletRequest request, @RequestParam(required = true, defaultValue = "0") int fid,
            @RequestParam(required = true, defaultValue = "0") String uuid) throws Exception {

        JSONObject jsonObject = new JSONObject();

        Fsubscription fsubscription = this.subscriptionService.findById(fid);
        if (fsubscription == null || fsubscription.getFtype() != SubscriptionTypeEnum.RMB) {
            return jsonObject.toString();
        }

        String status = "";
        long now = Utils.getTimestamp().getTime();
        if (!fsubscription.getFisopen()) {
            status = "0";
        }
        if (fsubscription.getFbeginTime().getTime() > now) {
            status = "0";
        }
        if (fsubscription.getFbeginTime().getTime() < now && fsubscription.getFendTime().getTime() > now) {
            status = "1";
        }
        if (fsubscription.getFendTime().getTime() < now) {
            status = "2";
        }
        jsonObject.accumulate("Status", status);
        fsubscription.setFstatus(status);

        String url = null;
        double totalAmt = 0d;
        // Fvirtualwallet fvirtualwallet =
        // this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
        // fsubscription.getFvirtualcointypeCost().getFid());
        // totalAmt = fvirtualwallet.getFtotal();

        String fcost_vi_ids = fsubscription.getFcost_vi_ids();
        String[] vi_ids = fcost_vi_ids.split(",");
        List<Fvirtualcointype> coinType = new ArrayList<Fvirtualcointype>();
        List<Fvirtualwallet> walletList = new ArrayList<Fvirtualwallet>();
        for (String ids : vi_ids) {
            JSONObject js1 = new JSONObject();
            JSONObject js2 = new JSONObject();
            Fvirtualcointype c = this.frontVirtualCoinService.findFvirtualCoinById(Integer.parseInt(ids));
            js1.accumulate("fid", c.getFid());
            js1.accumulate("name", c.getfShortName());
            jsonObject.accumulate("coinList", js1);
            Fvirtualwallet w = this.frontUserService.findVirtualWalletByUser(Integer.parseInt(uuid),
                    Integer.parseInt(ids));
            js2.accumulate("aviliable", w.getFtotal());
            jsonObject.accumulate("walletList", js2);
        }

        String prices = fsubscription.getFprices();
        String[] priceArray = prices.split("/");
        List priceList = new ArrayList<>();
        for (String price : priceArray) {
            JSONObject js3 = new JSONObject();
            js3.accumulate("price", price);
            jsonObject.accumulate("priceList", js3);
        }
        jsonObject.accumulate("ftitle", fsubscription.getFtitle());
        jsonObject.accumulate("fAlreadyByCount", fsubscription.getfAlreadyByCount());
        jsonObject.accumulate("Ftotal", fsubscription.getFtotal());
        return jsonObject.toString();
    }

    @RequestMapping("/crowd/view")
    public ModelAndView view(HttpServletRequest request, @RequestParam(required = true, defaultValue = "0") int fid)
            throws Exception {
        JspPage modelAndView = new JspPage(request);

        Fsubscription fsubscription = this.subscriptionService.findById(fid);
        if (fsubscription == null || fsubscription.getFtype() != SubscriptionTypeEnum.RMB) {
            modelAndView.setViewName("redirect:/crowd/index.html");
            return modelAndView;
        }

        String status = "";
        long now = Utils.getTimestamp().getTime();
        if (fsubscription.getFbeginTime().getTime() > now) {
            status = "未开始";
        }
        if (fsubscription.getFbeginTime().getTime() < now && fsubscription.getFendTime().getTime() > now) {
            status = "进行中";
        }
        if (fsubscription.getFendTime().getTime() < now) {
            status = "已结束";
        }
        fsubscription.setFstatus(status);

        long s = fsubscription.getFbeginTime().getTime() - now;
        long e = fsubscription.getFendTime().getTime() - now;

        modelAndView.addObject("s", s / 1000L);
        modelAndView.addObject("e", e / 1000L);

        String url = null;
        double totalAmt = 0d;
        Fuser fuser = this.userSerivce.findById(GetCurrentUser(request).getFid());
        if (fsubscription.getFvirtualcointypeCost().getFtype() == CoinTypeEnum.FB_CNY_VALUE) {
            url = "/account/rechargeCny.html";
        } else {
            url = "/account/rechargeBtc.html?symbol=" + fsubscription.getFvirtualcointypeCost().getFid();
        }
        // Fvirtualwallet fvirtualwallet =
        // this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
        // fsubscription.getFvirtualcointypeCost().getFid());
        // totalAmt = fvirtualwallet.getFtotal();

        String fcost_vi_ids = fsubscription.getFcost_vi_ids();
        String[] vi_ids = fcost_vi_ids.split(",");
        List<Fvirtualcointype> coinType = new ArrayList<Fvirtualcointype>();
        List<Fvirtualwallet> walletList = new ArrayList<Fvirtualwallet>();
        for (String ids : vi_ids) {
            coinType.add(this.frontVirtualCoinService.findFvirtualCoinById(Integer.parseInt(ids)));
            walletList.add(this.frontUserService.findVirtualWalletByUser(fuser.getFid(), Integer.parseInt(ids)));
        }
        modelAndView.addObject("coinType", coinType);

        String prices = fsubscription.getFprices();
        String[] priceArray = prices.split("/");
        List priceList = new ArrayList<>();
        for (String price : priceArray) {
            priceList.add(price);
        }
        modelAndView.addObject("priceList", priceList);

        modelAndView.addObject("walletList", walletList);
        modelAndView.addObject("rechargeUrl", url);
        modelAndView.addObject("fsubscription", fsubscription);
        modelAndView.setJspFile("front/zhongchou/detail");
        return modelAndView;
    }

    @RequestMapping("/crowd/logs")
    public ModelAndView logs(@RequestParam(required = false, defaultValue = "1") int currentPage,
            HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);

        String filter = "where fuser.fid=" + GetCurrentUser(request).getFid() + " order by fid desc";
        List<Fsubscriptionlog> subscriptionlogs = this.subscriptionLogService
                .list((currentPage - 1) * Constant.RecordPerPage, Constant.RecordPerPage, filter, true);
        int total = this.adminService.getAllCount("Fsubscriptionlog", filter);
        String pagin = PaginUtil.generatePagin(
                total / Constant.RecordPerPage + (total % Constant.RecordPerPage == 0 ? 0 : 1), currentPage,
                "/crowd/logs.html?");
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("subscriptionlogs", subscriptionlogs);

        modelAndView.setJspFile("front/zhongchou/logs");
        return modelAndView;
    }

}
