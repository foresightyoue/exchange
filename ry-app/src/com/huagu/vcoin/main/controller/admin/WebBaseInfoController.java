package com.huagu.vcoin.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.comm.ParamArray;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fwebbaseinfo;
import com.huagu.vcoin.main.service.admin.WebBaseInfoService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSender;

@Controller
public class WebBaseInfoController extends BaseController {
    @Autowired
    private WebBaseInfoService webBaseInfoService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MessageSender messageSender;

    @RequestMapping("/ssadmin/webBaseInfoList")
    public ModelAndView index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/webBaseInfo");
        List<Fwebbaseinfo> webBaseList = this.webBaseInfoService.findAll();
        if (webBaseList.size() > 0) {
            modelAndView.addObject("webBaseInfo", webBaseList.get(0));
        }
        return modelAndView;
    }

    @RequestMapping("/ssadmin/saveOrUpdateWebInfo")
    public ModelAndView saveOrUpdateWebInfo(ParamArray param) throws Exception {
        Fwebbaseinfo fwebbaseinfo = param.getFwebbaseinfo();
        Fwebbaseinfo newInfo = null;
        if (request.getParameter("fwebbaseinfo.fid") != null) {
            int fid = Integer.parseInt(request.getParameter("fwebbaseinfo.fid"));
            newInfo = this.webBaseInfoService.findById(fid);
            newInfo.setFbeianInfo(fwebbaseinfo.getFbeianInfo());
            newInfo.setFcopyRights(fwebbaseinfo.getFcopyRights());
            newInfo.setFdescription(fwebbaseinfo.getFdescription());
            newInfo.setFkeywords(fwebbaseinfo.getFkeywords());
            newInfo.setFsystemMail(fwebbaseinfo.getFsystemMail());
            newInfo.setFtitle(fwebbaseinfo.getFtitle());
            this.webBaseInfoService.updateObj(newInfo);
        } else {
            newInfo = new Fwebbaseinfo();
            newInfo.setFbeianInfo(fwebbaseinfo.getFbeianInfo());
            newInfo.setFcopyRights(fwebbaseinfo.getFcopyRights());
            newInfo.setFdescription(fwebbaseinfo.getFdescription());
            newInfo.setFkeywords(fwebbaseinfo.getFkeywords());
            newInfo.setFsystemMail(fwebbaseinfo.getFsystemMail());
            newInfo.setFtitle(fwebbaseinfo.getFtitle());
            this.webBaseInfoService.saveObj(newInfo);
        }

        this.messageSender.publish(ChannelConstant.constantmap, "webinfo");

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "保存成功");
        modelAndView.addObject("callbackType", "closeCurrent");

        return modelAndView;
    }

}
