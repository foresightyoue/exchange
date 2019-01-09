package com.huagu.vcoin.main.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.RegTypeEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

@Controller
public class SuserController extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConstantMap map;
    @Autowired
    private FrontUserService frontUserService;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    /*
     * @RequestMapping("/m/trade/trade_coin") public ModelAndView coin(){ JspPage
     * modelAndView = new JspPage(request);
     * modelAndView.setJspFile("front/trade/trade_coin"); return modelAndView;
     * 
     * }
     */
    @RequestMapping("/m/appversion/appversion")
    public ModelAndView appVersion(HttpServletRequest request, @RequestParam(required = false, defaultValue = "") String WebDownApp) {
        JspPage modelAndView = new JspPage(request);
        modelAndView.add("WebDownApp", WebDownApp);
        modelAndView.setJspFile("front/appversion/appversion");
        return modelAndView;
    }

    @RequestMapping("/m/security/loginPwd")
    public ModelAndView loginPwd() throws Exception {
        JspPage modelAndView = new JspPage(request);

        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        int level = fuser.getFscore().getFlevel();
        modelAndView.addObject("level", level);

        String device_name = new Constant().GoogleAuthName + ":" + fuser.getFloginName();
        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        boolean isBindEmail = fuser.getFisMailValidate();
        boolean isTradePassword = false;
        boolean isLoginPassword = true;
        String telNumber = "";
        String email = "";
        if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0) {
            isTradePassword = true;
        }
        int bindType = 0;
        if (isBindGoogle) {
            bindType = bindType + 1;
        }
        if (isBindTelephone) {
            bindType = bindType + 1;
            telNumber = fuser.getFtelephone().substring(0, fuser.getFtelephone().length() - 5) + "****";
        }
        if (isBindEmail) {
            bindType = bindType + 1;
            String[] args = fuser.getFemail().split("@");
            email = args[0].substring(0, args[0].length() - (args[0].length() >= 3 ? 3 : 1)) + "****" + args[1];
        }
        if (isTradePassword) {
            bindType = bindType + 1;
        }
        if (isLoginPassword) {
            bindType = bindType + 1;
        }

        modelAndView.addObject("bindType", bindType);

        String loginName = "";
        if (fuser.getFregType() != RegTypeEnum.EMAIL_VALUE) {
            loginName = fuser.getFloginName().substring(0, fuser.getFloginName().length() - 5) + "****";
        } else {
            String[] args = fuser.getFloginName().split("@");
            loginName = args[0].substring(0, args[0].length() - (args[0].length() >= 3 ? 3 : 1)) + "****" + args[1];
        }

        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("telNumber", telNumber);
        modelAndView.addObject("email", email);
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("isBindTelephone", isBindTelephone);
        modelAndView.addObject("isBindEmail", isBindEmail);
        modelAndView.addObject("isTradePassword", isTradePassword);
        modelAndView.addObject("isLoginPassword", isLoginPassword);

        modelAndView.addObject("device_name", device_name);
        modelAndView.addObject("fuser", fuser);
        modelAndView.setJspFile("front/security/loginPwd");
        return modelAndView;
    }

}
