package com.huagu.vcoin.main.controller.front;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.huagu.vcoin.main.Enum.CoinVoteStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fcoinvote;
import com.huagu.vcoin.main.model.Fcoinvotelog;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.CoinVoteService;
import com.huagu.vcoin.main.service.admin.CoinVotelogService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.PaginUtil;
import com.huagu.vcoin.util.Utils;

import net.sf.json.JSONObject;

@Controller
public class FrontCoinVoteController extends BaseController {

    @Autowired
    private CoinVotelogService coinVotelogService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CoinVoteService coinVoteService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private ConstantMap map;

    @RequestMapping("/vote/list")
    public ModelAndView voteList(@RequestParam(required = false, defaultValue = "1") int currentPage,
            HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("front/coinvote/voteList");

        boolean flag = true;
        if (currentPage > 1) {
            flag = false;
        }
        modelAndView.addObject("flag", flag);

        String filter = " where fstatus=" + CoinVoteStatusEnum.NORMAL_VALUE + " order by (fyes-fno) desc ";
        List<Fcoinvote> coinvotes = this.coinVoteService.list((currentPage - 1) * Constant.RecordPerPage,
                Constant.RecordPerPage, filter, true);
        int total = this.adminService.getAllCount("Fcoinvote", filter);
        String pagin = PaginUtil.generatePagin(
                total / Constant.RecordPerPage + (total % Constant.RecordPerPage == 0 ? 0 : 1), currentPage,
                "/vote/list.html?");
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("coinvotes", coinvotes);

        return modelAndView;
    }

    @RequestMapping("/vote/details")
    public ModelAndView details(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int id)
            throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setJspFile("front/coinvote/voteDetailList");

        Fcoinvote vote = this.coinVoteService.findById(id);
        if (vote == null || vote.getFstatus() == CoinVoteStatusEnum.ABNORMAL_VALUE) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        boolean islogin = false;
        if (GetCurrentUser(request) != null) {
            islogin = true;
        }
        modelAndView.addObject("islogin", islogin);
        modelAndView.addObject("vote", vote);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = { "/json/fcurrentCNY" }, produces = { JsonEncode })
    public String fcurrentCNY(HttpServletRequest request, @RequestParam(required = true) String symbol) {
        JSONObject jsonObject = new JSONObject();
        List findByProperty = frontVirtualCoinService.findByProperty("fShortName", symbol);
        Fvirtualcointype fvirtualcointype = null;
        double fcurrentCNY = 0;
        if (findByProperty.size() > 0) {
            fvirtualcointype = (Fvirtualcointype) findByProperty.get(0);
            fcurrentCNY = fvirtualcointype.getFcurrentCNY();
        }

        jsonObject.accumulate("fcurrentCNY", fcurrentCNY);
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/json/coinVote", produces = JsonEncode)
    public String coinVote(HttpServletRequest request, @RequestParam(required = true) int id,
            @RequestParam(required = true) int vote) throws Exception {

        JSONObject jsonObject = new JSONObject();

        if (vote != 0 && vote != 1) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作！");
            return jsonObject.toString();
        }

        if (GetCurrentUser(request) == null) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先登录！");
            return jsonObject.toString();
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        if (!fuser.getFhasRealValidate()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "您还未通过实名认证！");
            return jsonObject.toString();
        }

        String[] args = this.map.getString("oneXiaoFei").trim().split("#");// 消耗1TMC
        double xiaofei = Double.valueOf(args[1]);
        int vid = Integer.parseInt(args[0]);
        int times = Integer.parseInt(args[2]);
        // TMC
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(vid);
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(GetCurrentUser(request).getFid(),
                fvirtualcointype.getFid());
        if (fvirtualwallet.getFtotal() < xiaofei) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "投票需要消耗" + xiaofei + fvirtualcointype.getFname() + "，您的余额不足！");
            return jsonObject.toString();
        }
        fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() - xiaofei);

        Fcoinvote fcoinvote = this.coinVoteService.findById(id);
        if (fcoinvote == null || fcoinvote.getFstatus() == CoinVoteStatusEnum.ABNORMAL_VALUE) {
            jsonObject.accumulate("code", -12);
            jsonObject.accumulate("msg", "无效的投票！");
            return jsonObject.toString();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String key = sdf.format(new Date());
        String sql = "where fuser.fid=" + GetCurrentUser(request).getFid()
        // + " and DATE_FORMAT(fcreatetime,'%Y-%m-%d') ='" + key + "' and
        // fcoinvote.fid=" + fcoinvote.getFid();
                + " and fcreatetime >=curdate() and curdate() <='" + key + "' and fcoinvote.fid=" + fcoinvote.getFid();
        int count = this.adminService.getAllCount("Fcoinvotelog", sql);
        if (count + 1 > times && !fuser.isFistiger()) {
            jsonObject.accumulate("code", -12);
            jsonObject.accumulate("msg", "每天只允许投票" + times + "次！");
            return jsonObject.toString();
        }

        Fcoinvotelog fcoinvotelog = new Fcoinvotelog();
        fcoinvotelog.setFcoinvote(fcoinvote);
        fcoinvotelog.setFcreatetime(Utils.getTimestamp());
        fcoinvotelog.setFuser(GetCurrentUser(request));
        fcoinvotelog.setVote(vote);

        if (vote == 0) {
            fcoinvote.setFno(fcoinvote.getFno() + 1);
        } else {
            fcoinvote.setFyes(fcoinvote.getFyes() + 1);
        }

        boolean flag = false;
        try {
            this.coinVoteService.updateFcoinvote(fvirtualwallet, fcoinvote, fcoinvotelog);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (flag == true) {
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "投票成功！");
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", -3);
            jsonObject.accumulate("msg", "网络错误，请稍后再试！");
            return jsonObject.toString();
        }

    }

    @ResponseBody
    @RequestMapping(value = { "/json/fNewPrice" }, produces = { JsonEncode })
    public String fNewPrice(HttpServletRequest request, @RequestParam(required = true) int symbol,
            @RequestParam(required = false, defaultValue = "0") double p_new) {
        JSONObject jsonObject = new JSONObject();
        int num = 0;
        try (Mysql mysql = new Mysql()) {
            // 买价
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select ft.fPrize,ft.flastUpdatTime,ft.fStatus from %s ft", AppDB.fentrust);
            ds1.add(" inner join %s fm", AppDB.Ftrademapping);
            ds1.add(" on ft.ftrademapping = fm.fid");
            ds1.add(" where fm.fid =%s and ft.fEntrustType = 0", symbol);
            ds1.add(" and ft.fStatus in(3)");
            ds1.add(" order by fPrize desc");
            ds1.setMaximum(1);
            ds1.open();

            // 卖价
            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select ft.fPrize,ft.flastUpdatTime,ft.fStatus from %s ft", AppDB.fentrust);
            ds2.add(" inner join %s fm", AppDB.Ftrademapping);
            ds2.add(" on ft.ftrademapping = fm.fid");
            ds2.add(" where fm.fid = %s and ft.fEntrustType = 1", symbol);
            ds2.add(" and ft.fStatus in(3)");
            ds2.add(" order by fPrize desc");
            ds2.setMaximum(1);
            ds2.open();
            if (!ds1.eof() && !ds2.eof()) {
                if (ds1.getDouble("fPrize") <= p_new) {
                    p_new = ds1.getDouble("fPrize");
                    num = 1;
                } else if (ds2.getDouble("fPrize") >= p_new) {
                    p_new = ds2.getDouble("fPrize");
                    num = 0;
                } else if (ds2.getDouble("fPrize") < p_new && p_new < ds1.getDouble("fPrize")) {
                    p_new = p_new;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.accumulate("num", num);
        jsonObject.accumulate("p_new", p_new);
        return jsonObject.toString();
    }
}
