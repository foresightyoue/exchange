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
import com.huagu.vcoin.main.Enum.AutotradeStatusEnum;
import com.huagu.vcoin.main.Enum.SynTypeEnum;
import com.huagu.vcoin.main.Enum.TradeTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fautotrade;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.AutotradeService;
import com.huagu.vcoin.main.service.admin.TradeMappingService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

@Controller
public class AutotradeController extends BaseController {
    @Autowired
    private AutotradeService autotradeService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();
    @Autowired
    private TradeMappingService tradeMappingService;

    @RequestMapping("/ssadmin/autotradeList")
    public ModelAndView autotradeList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/autotradeList");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        // 当前页
        int currentPage = 1;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");

        List<Fautotrade> list = this.autotradeService.list((currentPage - 1) * numPerPage, numPerPage, filter + "",
                true);
        modelAndView.addObject("autotradeList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "autotradeList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fautotrade", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goAutotradeJSP")
    public ModelAndView goAutotradeJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fautotrade fautotrade = this.autotradeService.findById(fid);
            modelAndView.addObject("fautotrade", fautotrade);
        }
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, TradeTypeEnum.getEnumString(1));
        modelAndView.addObject("map", map);

        String sql = "where fstatus=1";
        List<Ftrademapping> allType = this.tradeMappingService.list(0, 0, sql, false);
        modelAndView.addObject("allType", allType);

        Map<Integer, String> typemap = new HashMap<Integer, String>();
        typemap.put(0, SynTypeEnum.getEnumString(0));
        typemap.put(1, SynTypeEnum.getEnumString(1));
        typemap.put(2, SynTypeEnum.getEnumString(2));
        typemap.put(3, SynTypeEnum.getEnumString(3));
        typemap.put(4, SynTypeEnum.getEnumString(4));
        modelAndView.addObject("typemap", typemap);
        return modelAndView;
    }

    @RequestMapping("ssadmin/saveAutotrade")
    public ModelAndView saveAutotrade() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        Fautotrade autotrade = new Fautotrade();
        int userid = Integer.parseInt(request.getParameter("fuserid"));
        List<Fuser> fusers = this.userService.list(0, 0, "where fid=" + userid, false);
        if (fusers == null || fusers.size() == 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "用户不存在");
            return modelAndView;
        }

        autotrade.setFsynType(Integer.parseInt(request.getParameter("fsynType")));

        autotrade.setFstatus(AutotradeStatusEnum.NORMAL);
        autotrade.setFuser(fusers.get(0));
        autotrade.setFcreatetime(Utils.getTimestamp());
        autotrade.setFmaxprice(Double.valueOf(request.getParameter("fmaxprice")));
        autotrade.setFminprice(Double.valueOf(request.getParameter("fminprice")));
        autotrade.setFmaxqty(Double.valueOf(request.getParameter("fmaxqty")));
        autotrade.setFminqty(Double.valueOf(request.getParameter("fminqty")));
        autotrade.setFtype(Integer.parseInt(request.getParameter("ftype")));
        autotrade.setFtimes(Integer.parseInt(request.getParameter("ftimes")));
        autotrade.setFstoptimes(Integer.parseInt(request.getParameter("fstoptimes")));
        autotrade.setFstandardprice(Double.valueOf(request.getParameter("fstandardprice")));
        int vid = Integer.parseInt(request.getParameter("vid"));
        Ftrademapping trademapping = this.tradeMappingService.findById(vid);
        autotrade.setFtrademapping(trademapping);
        this.autotradeService.saveObj(autotrade);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "新增成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateAutotrade")
    public ModelAndView updateAutotrade() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int uid = Integer.parseInt(request.getParameter("uid"));
        Fautotrade autotrade = this.autotradeService.findById(uid);
        int userid = Integer.parseInt(request.getParameter("fuserid"));
        List<Fuser> fusers = this.userService.list(0, 0, "where fid=" + userid, false);
        if (fusers == null || fusers.size() == 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "用户不存在");
            return modelAndView;
        }

        autotrade.setFsynType(Integer.parseInt(request.getParameter("fsynType")));

        autotrade.setFstatus(AutotradeStatusEnum.NORMAL);
        autotrade.setFuser(fusers.get(0));
        autotrade.setFcreatetime(Utils.getTimestamp());
        autotrade.setFmaxprice(Double.valueOf(request.getParameter("fmaxprice")));
        autotrade.setFminprice(Double.valueOf(request.getParameter("fminprice")));
        autotrade.setFmaxqty(Double.valueOf(request.getParameter("fmaxqty")));
        autotrade.setFminqty(Double.valueOf(request.getParameter("fminqty")));
        autotrade.setFtype(Integer.parseInt(request.getParameter("ftype")));
        autotrade.setFtimes(Integer.parseInt(request.getParameter("ftimes")));
        autotrade.setFstoptimes(Integer.parseInt(request.getParameter("fstoptimes")));
        autotrade.setFstandardprice(Double.valueOf(request.getParameter("fstandardprice")));
        int vid = Integer.parseInt(request.getParameter("vid"));
        Ftrademapping trademapping = this.tradeMappingService.findById(vid);
        autotrade.setFtrademapping(trademapping);
        this.autotradeService.updateObj(autotrade);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "更新成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteAutotrade")
    public ModelAndView deleteAutotrade() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        JspPage modelAndView = new JspPage(request);
        this.autotradeService.deleteObj(fid);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "删除成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/doAutotrade")
    public ModelAndView doAutotrade() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        int type = Integer.parseInt(request.getParameter("type"));
        JspPage modelAndView = new JspPage(request);
        Fautotrade autotrade = this.autotradeService.findById(fid);
        if (type == 1) {
            autotrade.setFstatus(AutotradeStatusEnum.FORBIN);
        } else {
            autotrade.setFstatus(AutotradeStatusEnum.NORMAL);
        }

        this.autotradeService.updateObj(autotrade);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作成功");
        return modelAndView;
    }
}
