package com.huagu.vcoin.main.controller.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Farticle;
import com.huagu.vcoin.main.model.Farticletype;
import com.huagu.vcoin.main.service.admin.ArticleService;
import com.huagu.vcoin.main.service.front.FrontOthersService;
import com.huagu.vcoin.main.service.front.UtilsService;
import com.huagu.vcoin.util.MobileUtils;
import com.huagu.vcoin.util.PaginUtil;

import cn.cerc.jdb.core.Record;

@Controller
public class FrontServiceController extends BaseController {

    @Autowired
    private FrontOthersService frontOthersService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UtilsService utilsService;

    @RequestMapping(value = { "/service/ourService", "/m/service/ourService" })
    public ModelAndView ourService(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int id,
            @RequestParam(required = false, defaultValue = "1") int currentPage) throws Exception {// 12,5,5
        JspPage modelAndView = new JspPage(request);
        int page = 10;

        if (MobileUtils.isMURL(request)) {
            List<Farticletype> farticletypes = this.utilsService.list(0, 0, "order by fid desc", false,
                    Farticletype.class);
            modelAndView.addObject("farticletypes", farticletypes);
            List<List<Farticle>> arts = new ArrayList<List<Farticle>>();
            for (int i = 0; i < farticletypes.size(); i++) {
                List<Farticle> farticles = this.frontOthersService.findFarticle(farticletypes.get(i).getFid(), 0,
                        Integer.MAX_VALUE);
                arts.add(farticles);
            }
            modelAndView.addObject("arts", arts);
        } else {
            List<Farticle> farticles = this.frontOthersService.findFarticle(id, (currentPage - 1) * page, page);
            modelAndView.addObject("farticles", farticles);
        }

        int total = this.frontOthersService.findFarticleCount(id);
        String pagin = PaginUtil.generatePagin(total / page + (total % page == 0 ? 0 : 1), currentPage,
                "/service/ourService.html?id=" + id + "&");

        Farticletype atype = this.frontOthersService.findFarticleTypeById(id);
        modelAndView.addObject("atype", atype);

        modelAndView.addObject("id", id);
        modelAndView.addObject("pagin", pagin);

        modelAndView.setJspFile("front/service/index");
        return modelAndView;
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/user/api/noticex", "/m/api/noticex" }, produces = { JsonEncode })
    public String notice(HttpServletRequest request) throws Exception {
        JSONObject result = new JSONObject();
        List<Farticletype> farticletypes = this.utilsService.list(0, 0, "order by fid desc", false, Farticletype.class);
        Map<String, Object> arts = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < farticletypes.size(); i++) {
            List<Farticle> farticles = this.frontOthersService.findFarticle(farticletypes.get(i).getFid(), 0,
                    Integer.MAX_VALUE);
            for (Farticle farticle : farticles) {
                arts = new HashMap<>();
                arts.put("fid", farticle.getFid());
                arts.put("title", farticle.getFtitle_short());
                arts.put("date", String.format("%s", farticle.getFlastModifyDate()));
                list.add(arts);
            }
        }
        result.put("notices", list);
        return result.toString();
    }

    @RequestMapping("/m/mine/help")
    public ModelAndView help(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        List<Map<String, Object>> help = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fid,ftitle from %s ", AppDB.Fabout);
            ds.add("where ftype='帮助中心'");
            ds.open();
            for (Record record : ds) {
                help.add(record.getItems());
            }
        }
        modelAndView.add("help", help);
        modelAndView.setJspFile("front/help/index");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = { "/user/api/help", "/m/api/help" }, produces = { JsonEncode })
    public String apihelp(HttpServletRequest request) throws Exception {
        JSONObject result = new JSONObject();
        List<Map<String, Object>> data = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fid,ftitle from %s ", AppDB.Fabout);
            ds.add("where ftype='帮助中心'");
            ds.open();
            for (Record record : ds) {
                data.add(record.getItems());
            }
            result.put("data", data);
        }

        return result.toString();
    }

    @RequestMapping("/m/help/info")
    public ModelAndView helpInfo(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int id)
            throws Exception {
        JspPage modelAndView = new JspPage(request);
        List<Map<String, Object>> help = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fid,ftitle,fcontent from %s ", AppDB.Fabout);
            ds.add("where fid=%d", id);
            ds.setMaximum(1);
            ds.open();
            modelAndView.add("help", ds.getCurrent());
        }
        modelAndView.setJspFile("front/help/article");
        return modelAndView;
    }

    @RequestMapping(value = { "/service/article", "/m/service/article" })
    public ModelAndView article(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int id)
            throws Exception {
        JspPage modelAndView = new JspPage(request);
        Farticle farticle = this.frontOthersService.findFarticleById(id);
        if (farticle == null) {
            modelAndView.setViewName("redirect:/service/ourService.html");
            return modelAndView;
        }

        if (MobileUtils.isMURL(request)) {
            farticle.setFcontent(farticle.getFcontent().replace("width", "widthx"));
        }
        modelAndView.addObject("farticle", farticle);

        String filter = "where farticletype.fid=" + farticle.getFarticletype().getFid() + " order by fid desc";
        List<Farticle> hotsArticles = this.articleService.list(0, 6, filter, true);
        modelAndView.addObject("hotsArticles", hotsArticles);
        modelAndView.setJspFile("front/service/article");
        return modelAndView;
    }
}
