package com.huagu.vcoin.main.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fabout;
import com.huagu.vcoin.main.service.admin.AboutService;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

@Controller
public class AboutController extends BaseController {
    @Autowired
    private AboutService aboutService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConstantMap map;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/aboutList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/aboutList");
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
        if (keyWord != null && keyWord.trim().length() > 0) {
            filter.append("and ftitle like '%" + keyWord + "%' \n");
            modelAndView.addObject("keywords", keyWord);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filter.append("order by " + orderField + "\n");
        } else {
            filter.append("order by ftype \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filter.append(orderDirection + "\n");
        } else {
            filter.append("desc \n");
        }
        List<Fabout> list = this.aboutService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("aboutList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "aboutList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fabout", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goAboutJSP")
    public ModelAndView goAboutJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fabout about = this.aboutService.findById(fid);
            modelAndView.addObject("fabout", about);
        }

        String[] args = this.map.get("helpType").toString().split("#");
        Map<String, String> type = new HashMap<String, String>();
        for (int i = 0; i < args.length; i++) {
            type.put(args[i], args[i]);
        }
        modelAndView.addObject("type", type);

        return modelAndView;
    }

    @RequestMapping("ssadmin/updateAbout")
    public ModelAndView updateAbout() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fid"));
        Fabout about = this.aboutService.findById(fid);
        about.setFcontent(request.getParameter("fcontent"));
        about.setFcontentEn(request.getParameter("fcontentEn"));
        about.setFtitle(request.getParameter("ftitle"));
        about.setFtitleEn(request.getParameter("ftitleEn"));
        about.setFtype(request.getParameter("ftype"));
        this.aboutService.updateObj(about);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/saveAbout")
    public ModelAndView saveAbout() throws Exception {
        JspPage modelAndView = new JspPage(request);
        Fabout about = new Fabout();
        about.setFcontent(request.getParameter("fcontent"));
        about.setFcontentEn(request.getParameter("fcontentEn"));
        about.setFtitle(request.getParameter("ftitle"));
        about.setFtitleEn(request.getParameter("ftitleEn"));
        about.setFtype(request.getParameter("ftype"));
        this.aboutService.saveObj(about);

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "保存成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteAbout")
    public ModelAndView deleteAbout() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        this.aboutService.deleteObj(fid);

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "删除成功");
        /*modelAndView.addObject("statusCode", 300);
        modelAndView.addObject("message", "此信息为标准信息，只能修改不能删除！");*/
        return modelAndView;
    }
}
