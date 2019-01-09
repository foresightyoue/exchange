package com.huagu.vcoin.main.controller.front;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CountLimitTypeEnum;
import com.huagu.vcoin.main.Enum.LogTypeEnum;
import com.huagu.vcoin.main.Enum.RegTypeEnum;
import com.huagu.vcoin.main.Enum.UserStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Flog;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.LogService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontValidateService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.PaginUtil;
import com.huagu.vcoin.util.Utils;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.taobao.api.internal.util.StringUtils;

import cn.cerc.security.sapi.JayunSecurity;
import net.sf.json.JSONObject;

@Controller
public class FrontUserController extends BaseController {

    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private LogService logService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConstantMap map;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontValidateService frontValidateService;

    /*
     * @param type:0登录，1注册
     */
    @RequestMapping("/user/login")
    public ModelAndView login(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "") String forwardUrl) throws Exception {
        JspPage modelAndView = new JspPage(request);

        // 推荐注册
        try {
            Fuser intro = null;
            Cookie cs[] = request.getCookies();
            if (cs != null) {
                for (Cookie cookie : cs) {
                    if (cookie.getName().endsWith("r")) {
                        intro = this.frontUserService.findById(Integer.parseInt(cookie.getValue()));
                        break;
                    }
                }
            }
            if (intro != null) {
                modelAndView.addObject("intro", intro.getFid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 服务中心
        try {
            List<Fuser> services = null;
            Cookie cs[] = request.getCookies();
            if (cs != null) {
                for (Cookie cookie : cs) {
                    if (cookie.getName().endsWith("sn")) {
                        services = this.frontUserService.findUserByProperty("fuserNo", cookie.getValue());
                        break;
                    }
                }
            }
            if (services != null && services.size() == 1) {
                modelAndView.addObject("service", services.get(0).getFuserNo().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (GetCurrentUser(request) == null) {
            modelAndView.addObject("forwardUrl", forwardUrl);
        } else {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                CleanSession(request);
            }
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        int isIndex = 1;
        modelAndView.addObject("isIndex", isIndex);

        modelAndView.setJspFile("front/user/sub_user_login");
        return modelAndView;
    }

    @RequestMapping("/m/user/login")
    public ModelAndView mLogin(@RequestParam(required = false, defaultValue = "") String forwardUrl,
            HttpServletRequest request, @CookieValue(value = "loginName", required = false) String loginName,
            @CookieValue(value = "password", required = false) String password) {
        JspPage modelAndView = new JspPage(request);

        if (loginName != null && password != null) {
            try {

                int longLogin = -1;// 0是手机，1是邮箱
                String keyString = "ftelephone";
                // TODO 登录只用floginName 注册时的手机号账号登录
                if (loginName.matches(Constant.PhoneReg) == true) {
                    keyString = "ftelephone";
                    longLogin = 0;
                }
                List<Fuser> fusers = this.frontUserService.findUserByProperty(keyString, loginName);
                Fuser fuser = new Fuser();
                fuser.setFloginName(loginName);// 将登录字段校验改为floginName
                fuser.setFloginPassword(password);
                fuser.setSalt(fusers.get(0).getSalt());
                int insertdata = fusers.get(0).getfInsertData();
                String ip = getIpAddr(request);
                fuser = this.frontUserService.updateCheckLogin(fuser, ip, longLogin == 1, insertdata);
                if (fuser != null) {
                    String isCanlogin = constantMap.get("isCanlogin").toString().trim();
                    if (!isCanlogin.equals("1")) {
                        boolean isCanLogin = false;
                        String[] canLoginUsers = constantMap.get("canLoginUsers").toString().split("#");
                        for (int i = 0; i < canLoginUsers.length; i++) {
                            if (canLoginUsers[i].equals(String.valueOf(fuser.getFid()))) {
                                isCanLogin = true;
                                break;
                            }
                        }
                        if (!isCanLogin) {
                            modelAndView.setJspFile("front/user/sub_user_login");
                            return modelAndView;
                        }
                    }
                }

                if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
                    if (fuser.getFopenSecondValidate()) {
                        String deviceId = request.getParameter("deviceId");
                        if (deviceId == null || "".equals(deviceId)) {
                            deviceId = "webclient";
                        }
                        getSession(request).setAttribute("deviceId", deviceId);

                        // 登录成功向聚安注册用户
                        JayunSecurity security = new JayunSecurity(request);
                        security.register(fuser.getFloginName(), fuser.getFtelephone());
                        SetSecondLoginSession(request, fuser);
                    } else {
                        SetSession(fuser, request);
                        modelAndView.setRedirectJsp("/index.html");
                        return modelAndView;
                    }
                } else {
                    modelAndView.setJspFile("front/user/sub_user_login");
                    return modelAndView;
                }
            } catch (Exception e) {
                modelAndView.setJspFile("front/user/sub_user_login");
                return modelAndView;
            }
        }
        if (GetCurrentUser(request) == null) {
            modelAndView.addObject("forwardUrl", forwardUrl);
        } else {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                CleanSession(request);
            }
            modelAndView.setJspFile("front/user/sub_user_login");
            return modelAndView;
        }
        modelAndView.setJspFile("front/user/sub_user_login");
        return modelAndView;
    }

    // 注册
    @RequestMapping(value = { "/user/register", "/m/user/register" })
    public ModelAndView register(HttpServletRequest request, HttpServletResponse resp) throws Exception {
        JspPage modelAndView = new JspPage(request);

        // 已经登录跳回首页
        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                CleanSession(request);
            }
        }
        if (GetCurrentUser(request) != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        // 推荐注册
        try {
            Fuser intro = null;
            Cookie cs[] = request.getCookies();
            if (cs != null) {
                for (Cookie cookie : cs) {
                    if (cookie.getName().endsWith("r")) {
                        intro = this.frontUserService.findById(Integer.parseInt(cookie.getValue()));
                        break;
                    }
                }
            }
            if (intro != null) {
                modelAndView.addObject("intro", intro.getFid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 服务中心
        try {
            List<Fuser> services = null;
            Cookie cs[] = request.getCookies();
            if (cs != null) {
                for (Cookie cookie : cs) {
                    if (cookie.getName().endsWith("sn")) {
                        services = this.frontUserService.findUserByProperty("fuserNo", cookie.getValue());
                        break;
                    }
                }
            }
            if (services != null && services.size() == 1) {
                modelAndView.addObject("service", services.get(0).getFuserNo().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int isIndex = 1;
        modelAndView.addObject("isIndex", isIndex);

        modelAndView.setJspFile("front/user/sub_user_register");
        return modelAndView;
    }

    // 短信注册
    @RequestMapping(value = { "/user/registerSMS" })
    public ModelAndView registerSMS(HttpServletRequest request, HttpServletResponse resp) throws Exception {
        JspPage modelAndView = new JspPage(request);

        // 已经登录跳回首页
        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                CleanSession(request);
            }
        }
        if (GetCurrentUser(request) != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        // 推荐注册
        try {
            Fuser intro = null;
            Cookie cs[] = request.getCookies();
            for (Cookie cookie : cs) {
                if (cookie.getName().endsWith("r")) {
                    intro = this.frontUserService.findById(Integer.parseInt(cookie.getValue()));
                    break;
                }
            }
            if (intro != null) {
                modelAndView.addObject("intro", intro.getFid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 服务中心
        try {
            List<Fuser> services = null;
            Cookie cs[] = request.getCookies();
            for (Cookie cookie : cs) {
                if (cookie.getName().endsWith("sn")) {
                    services = this.frontUserService.findUserByProperty("fuserNo", cookie.getValue());
                    break;
                }
            }
            if (services != null && services.size() == 1) {
                modelAndView.addObject("service", services.get(0).getFuserNo().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int isIndex = 1;
        modelAndView.addObject("isIndex", isIndex);

        modelAndView.setJspFile("front/user/sub_user_registerSMS");
        return modelAndView;
    }

    @RequestMapping(value = { "/user/logout", "/m/user/logout" })
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        this.CleanSession(request);
        String uri = request.getRequestURI();
        if (!uri.startsWith("/m/")) {
            modelAndView.setViewName("redirect:/index.html");
        } else {
            modelAndView.setRedirectJsp("/front/user/sub_user_login.html");
            RemoveSecondLoginSession(request);
            clearCookie(response, request);
        }
        return modelAndView;
    }

    public void clearCookie(HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

    }

    @RequestMapping("/m/security")
    public ModelAndView securityApp(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        boolean isBindGoogle = fuser.getFgoogleBind();
        boolean isBindTelephone = fuser.isFisTelephoneBind();
        boolean isBindEmail = fuser.getFisMailValidate();
        boolean isPostRealValidate = fuser.getFpostRealValidate();
        String fname = fuser.getFrealName();
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("isBindTelephone", isBindTelephone);
        modelAndView.addObject("isBindEmail", isBindEmail);
        modelAndView.addObject("isPostRealValidate", isPostRealValidate);
        modelAndView.addObject("fname", fname);
        modelAndView.addObject("fuser", fuser);
        modelAndView.setJspFile("front/security/security");
        return modelAndView;

    }

    @RequestMapping(value = { "/user/security", "/m/securityEmail" })
    public ModelAndView security(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // 环境安全检测
        // String salt = Utils.MD5(Constant.AppLevel,
        // "0bca36ef25364cdbaf72133d59c47aad");
        /*
         * if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
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
        boolean fGoogleCheck = fuser.isFgoogleCheck();
        if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0) {
            isTradePassword = true;
        }
        int bindType = 0;
        // if(fuser.getFhasRealValidate()){
        // bindType = bindType+1 ;
        // }
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
        modelAndView.addObject("fGoogleCheck", fGoogleCheck);
        String uri = request.getRequestURI();
        if (!uri.startsWith("/m/")) {
            modelAndView.setViewName("/front/user/user_security");
        } else {
            if ("0".equals(request.getParameter("type"))) {
                modelAndView.setJspFile("front/security/securityEmail");
            } else if ("1".equals(request.getParameter("type"))) {
                modelAndView.setJspFile("front/security/securityTelephone");
            } else if ("2".equals(request.getParameter("type"))) {
                if (isTradePassword) {
                    modelAndView.setJspFile("front/security/tradePwdUpdate");
                } else {
                    modelAndView.setJspFile("front/security/tradePwd");
                }
            } else if ("3".equals(request.getParameter("type"))) {
                modelAndView.setJspFile("front/security/securityTelephoneBindUpdate");
            } else {
                modelAndView.setJspFile("front/security/securityTelephone");
            }
        }
        return modelAndView;
    }

    @RequestMapping("/m/security/securityGoogle")
    public ModelAndView securityGoogle(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        boolean isBindGoogle = fuser.getFgoogleBind();
        String device_name = new Constant().GoogleAuthName + ":" + fuser.getFloginName();
        modelAndView.addObject("isBindGoogle", isBindGoogle);
        modelAndView.addObject("device_name", device_name);
        modelAndView.addObject("isBindTelephone", fuser.isFisTelephoneBind());
        modelAndView.setJspFile("front/security/securityGoogle");
        return modelAndView;
    }

    @RequestMapping(value = { "/m/user/realCertification", "/user/realCertification" })
    public ModelAndView realCertification(HttpServletRequest request) throws Exception {
        JspPage page = new JspPage(request);
        String flag = request.getParameter("flag");
        if (!StringUtils.isEmpty(flag)) {
            page.addObject("flag", flag);
        }
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        page.addObject("fuser", fuser);
        if ("admin".equals(fuser.getFrealName())) {
            String uri = request.getRequestURI();
            if (!uri.startsWith("/m/")) {
                page.setJspFile("front/user/realCertification");
            } else {
                page.setJspFile("front/security/realId");
            }
        } else {
            page.setJspFile("front/user/realCertification");
        }
        return page;
    }

    @RequestMapping("/m/security/realId")
    public ModelAndView mRealCertification(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        modelAndView.addObject("fuser", fuser);
        // if(fuser.getFpostRealValidate()){
        // modelAndView.setViewName("front"+Mobilutils.M(request)+"/user/realCertification2")
        // ;
        // }else{
        modelAndView.setJspFile("front/security/realId");
        // modelAndView.setJspFile("front/security/realIdentity");
        // modelAndView.setJspFile("front/security/realbackId");
        // }
        return modelAndView;
    }

    @RequestMapping("/user/userloginlog")
    public ModelAndView userloginlog(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "1") int type) throws Exception {
        JspPage modelAndView = new JspPage(request);

        if (type != 1 && type != 2) {
            type = 1;
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        modelAndView.addObject("fuser", fuser);

        String filter = null;
        String url = "";
        if (type == 1) {
            url = "userloginlog";
            filter = "where fkey2='" + fuser.getFloginName() + "' and ftype=" + LogTypeEnum.User_LOGIN
                    + " order by fid desc";
        } else {
            url = "usersettinglog";
            filter = "where fkey2='" + fuser.getFloginName() + "' and ftype <>" + LogTypeEnum.User_LOGIN
                    + " order by fid desc";
        }

        List<Flog> logs = this.logService.list((currentPage - 1) * Constant.RecordPerPage, Constant.RecordPerPage,
                filter, true);
        int total = this.adminService.getAllCount("Flog", filter);
        String pagin = PaginUtil.generatePagin(
                total / Constant.RecordPerPage + (total % Constant.RecordPerPage == 0 ? 0 : 1), currentPage,
                "/user/userloginlog.html?type=" + type + "&");
        modelAndView.addObject("pagin", pagin);
        modelAndView.addObject("logs", logs);

        modelAndView.setJspFile("front/user/" + url);
        return modelAndView;
    }

    // ///user/userinfo
    // @RequestMapping(value="/user/userinfo")
    // public ModelAndView userinfo(
    // HttpServletRequest request,
    // @RequestParam(required=false,defaultValue="1")int currentPage
    // ) throws Exception{
    // ModelAndView modelAndView = new ModelAndView() ;
    //
    // Fuser fuser =
    // this.frontUserService.findById(GetSession(request).getFid()) ;
    // int level = fuser.getFscore().getFlevel() ;
    // modelAndView.addObject("level", level) ;
    // modelAndView.addObject("fuser", fuser) ;
    //
    // String device_name = new
    // Constant().GoogleAuthName+":"+fuser.getFloginName();
    // boolean isBindGoogle = fuser.getFgoogleBind() ;
    // boolean isBindTelephone = fuser.isFisTelephoneBind() ;
    // boolean isBindEmail = fuser.getFisMailValidate();
    // boolean isTradePassword = false;
    // boolean isLoginPassword = true;
    // String telNumber = "";
    // String email = "";
    // if(fuser.getFtradePassword() != null &&
    // fuser.getFtradePassword().trim().length() >0){
    // isTradePassword = true;
    // }
    // if(isBindTelephone){
    // telNumber = fuser.getFtelephone().substring(0,
    // fuser.getFtelephone().length()-5)+"****";
    // }
    // if(isBindEmail){
    // String[] args = fuser.getFemail().split("@");
    // email = args[0].substring(0,
    // args[0].length()-(args[0].length()>=3?3:1))+"****"+args[1];
    // }
    //
    // String loginName = "";
    // if(fuser.getFregType() != RegTypeEnum.EMAIL_VALUE){
    // loginName = fuser.getFloginName().substring(0,
    // fuser.getFloginName().length()-5)+"****";
    // }else{
    // String[] args = fuser.getFloginName().split("@");
    // loginName = args[0].substring(0,
    // args[0].length()-(args[0].length()>=3?3:1))+"****"+args[1];
    // }
    //
    //// String isActive = "未激活";
    //// StringBuffer sf = new StringBuffer();
    //// sf.append("select count(fid) from fuser where fisTelephoneBind=1 \n");
    //// sf.append(" and fpostRealValidate=1 and fhasRealValidate=1 and
    // fid="+fuser.getFid()+" and \n");
    //// sf.append("(fid in( \n");
    //// sf.append("select FUs_fId from fcapitaloperation where ftype=1 and
    // fStatus=3 and FUs_fId="+fuser.getFid()+"\n");
    //// sf.append(") or fid in(SELECT fuser from fpaycode where fstatus=2 and
    // fuser="+fuser.getFid()+") or fid in(SELECT fuid from foperationlog where
    // fstatus=2 and fuid="+fuser.getFid()+")) \n");
    //// double count = this.adminService.getSQLValue(sf.toString());
    //// if(count >0d){
    //// isActive = "已激活";
    //// }
    ////
    //// modelAndView.addObject("isActive", isActive) ;
    // modelAndView.addObject("device_name", device_name) ;
    // modelAndView.addObject("loginName", loginName) ;
    // modelAndView.addObject("telNumber", telNumber) ;
    // modelAndView.addObject("email", email) ;
    // modelAndView.addObject("isBindGoogle", isBindGoogle) ;
    // modelAndView.addObject("isBindTelephone", isBindTelephone) ;
    // modelAndView.addObject("isBindEmail", isBindEmail) ;
    // modelAndView.addObject("isTradePassword", isTradePassword) ;
    // modelAndView.addObject("isLoginPassword", isLoginPassword) ;
    //
    //
    // modelAndView.setViewName("/front/user/userinfo") ;
    // return modelAndView ;
    // }

    @RequestMapping(value = "/link/qq/call")
    public ModelAndView qqCall(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);

        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                CleanSession(request);
            }
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        response.setContentType("text/html;charset=utf-8");
        try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("/front/user/call");
        return modelAndView;

    }

    @RequestMapping(value = "/link/wx/callback")
    public ModelAndView AfterWeiXinLogin(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);

        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                CleanSession(request);
            }
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        try {
            String code = request.getParameter("code");
            String openID = null;
            String access_token = null;
            if ("".equals(code) || code == null || code.trim().length() == 0) {
                System.out.print("没有获取到响应参数");
            } else {
                String APPID = this.map.get("weixinAppID").toString().trim();
                String SECRET = this.map.get("weixinSECRET").toString().trim();
                String nickName = null;
                try {
                    String u = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + SECRET
                            + "&code=" + code + "&grant_type=authorization_code";
                    URL url = new URL(u);
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuffer sb = new StringBuffer();
                    String tmp = null;
                    while ((tmp = br.readLine()) != null) {
                        sb.append(tmp);
                    }
                    openID = JSONObject.fromObject(sb.toString()).getString("openid");
                    access_token = JSONObject.fromObject(sb.toString()).getString("access_token");
                } catch (Exception e1) {
                }
                if (openID != null && openID.trim().length() > 0) {
                    Fuser fuser = this.frontUserService.findByQQlogin(openID);
                    if (fuser == null) {
                        // 推广
                        Fuser intro = null;
                        try {
                            Cookie cs[] = request.getCookies();
                            for (Cookie cookie : cs) {
                                if (cookie.getName().endsWith("r")) {
                                    intro = this.frontUserService.findById(Integer.parseInt(cookie.getValue()));
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        fuser = new Fuser();
                        if (intro != null) {
                            fuser.setfIntroUser_id(intro);
                        }

                        String xx = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid="
                                + openID + "&lang=zh_CN";
                        URL url = new URL(xx);
                        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                        StringBuffer sb = new StringBuffer();
                        String tmp = null;
                        while ((tmp = br.readLine()) != null) {
                            sb.append(tmp);
                        }
                        nickName = HtmlUtils
                                .htmlEscape(JSONObject.fromObject(sb.toString()).getString("nickname").trim());
                        fuser.setSalt(Utils.getUUID());
                        fuser.setQqlogin(openID);
                        fuser.setFnickName(nickName);
                        fuser.setFloginName(nickName + "_" + Utils.getRandomString(6));

                        fuser.setFregType(RegTypeEnum.WX_VALUE);
                        fuser.setFisMailValidate(false);
                        String ip = getIpAddr(request);
                        fuser.setFregIp(ip);
                        fuser.setFlastLoginIp(ip);

                        fuser.setFregisterTime(Utils.getTimestamp());
                        fuser.setFloginPassword(Utils.MD5(openID, fuser.getSalt()));
                        fuser.setFtradePassword(null);
                        fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
                        fuser.setFlastLoginTime(Utils.getTimestamp());
                        fuser.setFlastUpdateTime(Utils.getTimestamp());
                        boolean saveFlag = false;
                        try {
                            saveFlag = this.frontUserService.saveRegister(fuser);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (saveFlag) {
                            this.SetSession(fuser, request);
                        }

                    } else {
                        // 登录
                        if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                            String ip = getIpAddr(request);
                            fuser.setFlastLoginIp(ip);
                            fuser.setFlastLoginTime(Utils.getTimestamp());
                            this.frontUserService.updateFUser(fuser, null, LogTypeEnum.User_LOGIN, ip);
                            this.SetSession(fuser, request);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }

    @RequestMapping(value = "/link/qq/callback")
    public ModelAndView AfterQQLogin(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);

        if (GetCurrentUser(request) != null) {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                CleanSession(request);
            }
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        try {
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            String accessToken = null, openID = null;

            if (accessTokenObj.getAccessToken().equals("")) {
                System.out.print("没有获取到响应参数");
            } else {
                accessToken = accessTokenObj.getAccessToken();

                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj = new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();

                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                String nickName = HtmlUtils.htmlEscape(userInfoBean.getNickname().trim());

                if (openID != null && openID.trim().length() > 0) {
                    Fuser fuser = this.frontUserService.findByQQlogin(openID);
                    if (fuser == null) {
                        // 推广
                        Fuser intro = null;
                        try {
                            Cookie cs[] = request.getCookies();
                            for (Cookie cookie : cs) {
                                if (cookie.getName().endsWith("r")) {
                                    intro = this.frontUserService.findById(Integer.parseInt(cookie.getValue()));
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        fuser = new Fuser();
                        if (intro != null) {
                            fuser.setfIntroUser_id(intro);
                        }
                        fuser.setSalt(Utils.getUUID());
                        fuser.setQqlogin(openID);
                        fuser.setFnickName(nickName);
                        fuser.setFloginName(nickName + "_" + Utils.getRandomString(6));

                        fuser.setFregType(RegTypeEnum.QQ_VALUE);
                        fuser.setFisMailValidate(false);
                        String ip = getIpAddr(request);
                        fuser.setFregIp(ip);
                        fuser.setFlastLoginIp(ip);

                        fuser.setFregisterTime(Utils.getTimestamp());
                        fuser.setFloginPassword(Utils.MD5(openID, fuser.getSalt()));
                        fuser.setFtradePassword(null);
                        fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
                        fuser.setFlastLoginTime(Utils.getTimestamp());
                        fuser.setFlastUpdateTime(Utils.getTimestamp());
                        boolean saveFlag = false;
                        try {
                            saveFlag = this.frontUserService.saveRegister(fuser);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (saveFlag) {
                            this.SetSession(fuser, request);
                        }

                    } else {
                        // 登录
                        if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                            String ip = getIpAddr(request);
                            fuser.setFlastLoginIp(ip);
                            fuser.setFlastLoginTime(Utils.getTimestamp());
                            this.frontUserService.updateFUser(fuser, null, LogTypeEnum.User_LOGIN, ip);
                            this.SetSession(fuser, request);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }
}
