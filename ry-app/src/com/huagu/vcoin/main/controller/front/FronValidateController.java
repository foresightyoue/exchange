package com.huagu.vcoin.main.controller.front;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.UserStatusEnum;
import com.huagu.vcoin.main.comm.MultipleValues;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Emailvalidate;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontValidateService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

@Controller
public class FronValidateController extends BaseController {
    @Autowired
    protected FrontValidateService frontValidateService;
    @Autowired
    private FrontUserService frontUserService;

    @RequestMapping(value = { "/m/validate/reset", "/validate/reset" }) // 密码重置请求
    public ModelAndView reset(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);

        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.FORBBIN_VALUE) {
                CleanSession(request);
            }

            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        modelAndView.setJspFile("front/validate/reset");
        return modelAndView;
    }

    @RequestMapping("/validate/resetEmail") // 密码重置请求
    public ModelAndView resetEmail(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.FORBBIN_VALUE) {
                CleanSession(request);
            }

            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        modelAndView.setJspFile("front/validate/resetEmail");
        return modelAndView;
    }

    @RequestMapping("/validate/resetPhone") // 密码重置请求
    public ModelAndView resetPhone(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.FORBBIN_VALUE) {
                CleanSession(request);
            }

            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        modelAndView.setJspFile("front/validate/resetPhone");
        return modelAndView;
    }

    // 通过邮件找回密码第二步
    @RequestMapping("validate/resetPwd")
    public ModelAndView resetPwd(HttpServletRequest request, @RequestParam(required = true) int uid,
            @RequestParam(required = true) String uuid) throws Exception {
        JspPage modelAndView = new JspPage(request);
        Emailvalidate emailvalidate = null;
        try {
            emailvalidate = this.frontValidateService.updateFindPasswordMailValidate(uid, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (emailvalidate != null) {
            Fuser fuser = this.frontUserService.findById(emailvalidate.getFuser().getFid());
            modelAndView.addObject("emailvalidate", emailvalidate);
            modelAndView.addObject("fuser", fuser);
        } else {
            modelAndView.setViewName("redirect:/validate/resetEmail.html");
            return modelAndView;
        }

        modelAndView.setJspFile("front/validate/resetPwd");
        return modelAndView;
    }

    // 通过手机找回密码第二步
    @RequestMapping(value = { "/m/validate/resetPwdPhone", "validate/resetPwdPhone" })
    public ModelAndView resetPwd(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);

        Object resetPasswordByPhone = request.getSession().getAttribute("resetPasswordByPhone");
        if (resetPasswordByPhone != null) {
            MultipleValues values = (MultipleValues) resetPasswordByPhone;
            Integer fuserid = (Integer) values.getValue1();
            Timestamp time = (Timestamp) values.getValue2();

            if (Utils.timeMinus(Utils.getTimestamp(), time) < 300) {
                Fuser fuser = this.frontUserService.findById(fuserid);
                modelAndView.addObject("fuser", fuser);
                modelAndView.setJspFile("front/validate/resetPwdPhone");
                return modelAndView;
            }
        }
        modelAndView.setViewName("redirect:/validate/resetPhone.html");
        return modelAndView;
    }

    /*
     * 邮件验证点击
     */
    @RequestMapping(value = "/validate/mail_validate")
    public ModelAndView mailValidate(HttpServletRequest request, @RequestParam(required = true) int uid,
            @RequestParam(required = true) String uuid) throws Exception {
        JspPage modelAndView = new JspPage(request);
        boolean flag = false;
        try {
            flag = this.frontValidateService.updateMailValidate(uid, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelAndView.addObject("validate", flag);
        modelAndView.setJspFile("front/user/reg_regconfirm");
        return modelAndView;
    }

}
