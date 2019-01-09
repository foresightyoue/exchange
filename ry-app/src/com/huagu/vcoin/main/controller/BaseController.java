package com.huagu.vcoin.main.controller;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.huagu.vcoin.main.Enum.MessageTypeEnum;
import com.huagu.vcoin.main.Enum.UserStatusEnum;
import com.huagu.vcoin.main.Enum.ValidateMailStatusEnum;
import com.huagu.vcoin.main.auto.TaskList;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.comm.MessageValidate;
import com.huagu.vcoin.main.comm.ValidateMap;
import com.huagu.vcoin.main.model.Emailvalidate;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Flimittrade;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvalidateemail;
import com.huagu.vcoin.main.service.admin.LimittradeService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontValidateService;
import com.huagu.vcoin.util.CheckMobile;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.ConstantKeys;
import com.huagu.vcoin.util.OSSPostObject;
import com.huagu.vcoin.util.Utils;

import cn.cerc.security.sapi.JayunMessage;
import cn.cerc.security.sapi.JayunSecurity;

public class BaseController {
    private static Logger log = Logger.getLogger(BaseController.class);
    public static final String JsonEncode = "application/json;charset=UTF-8";
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private ValidateMap validateMap;
    @Autowired
    private TaskList taskList;
    @Autowired
    private LimittradeService limittradeService;
    private HttpServletRequest request;

    public static final int maxResults = Constant.RecordPerPage;
    public static final int appMaxResults = Constant.AppRecordPerPage;

    public HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    public void setAdminSession(HttpServletRequest request, Fadmin fadmin) {
        getSession(request).setAttribute("login_admin", fadmin);
        this.CleanSession(request);
    }

    public void removeAdminSession(HttpServletRequest request) {
        getSession(request).removeAttribute("login_admin");
    }

    // 获得管理员session
    public Fadmin getAdminSession(HttpServletRequest request) {
        Object session = getSession(request).getAttribute("login_admin");
        Fadmin fadmin = null;
        if (session != null) {
            fadmin = (Fadmin) session;
        }
        return fadmin;
    }

    // 获得session中的当前用户
    public Fuser GetCurrentUser(HttpServletRequest request) {
        if (request != null)
            this.request = request;
        Fuser fuser = null;
        HttpSession session = getSession(this.request);
        Object session_user = session.getAttribute("login_user");
        if (session_user != null) {
            fuser = (Fuser) session_user;
            if (fuser.getFstatus() != UserStatusEnum.NORMAL_VALUE)
                return null;
        }
        return fuser;
    }

    public Fuser GetSecondLoginSession(HttpServletRequest request) {
        HttpSession session = getSession(request);
        return (Fuser) session.getAttribute("second_login_user");
    }

    public void SetSecondLoginSession(HttpServletRequest request, Fuser fuser) {
        HttpSession session = getSession(request);
        session.setAttribute("second_login_user", fuser);
    }

    public void RemoveSecondLoginSession(HttpServletRequest request) {
        HttpSession session = getSession(request);
        session.setAttribute("second_login_user", null);
    }

    public void SetSession(Fuser fuser, HttpServletRequest request) {
        HttpSession session = getSession(request);
        session.setAttribute("login_user", fuser);
    }

    public void CleanSession(HttpServletRequest request) {
        try {
            HttpSession session = getSession(request);
            String key = GetCurrentUser(request).getFid() + "trade";
            getSession(request).removeAttribute(key);
            session.setAttribute("login_user", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNeedTradePassword(HttpServletRequest request) {
        if (GetCurrentUser(request) == null)
            return true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String key = GetCurrentUser(request).getFid() + "trade";
        Object obj = getSession(request).getAttribute(key);

        if (obj == null) {
            return true;
        } else {
            try {
                double hour = Double.valueOf(this.constantMap.getString("tradePasswordHour"));
                double lastHour = Utils
                        .getDouble((sdf.parse(obj.toString()).getTime() - new Date().getTime()) / 1000 / 60 / 60, 2);
                if (lastHour >= hour) {
                    getSession(request).removeAttribute(key);
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                return false;
            }
        }
    }

    public void setNoNeedPassword(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String key = GetCurrentUser(request).getFid() + "trade";
        getSession(request).setAttribute(key, sdf.format(new Date()));
    }

    // for menu
    @ModelAttribute
    public void menuSelect(HttpServletRequest request) {
        // banner菜单
        String uri = request.getRequestURI();
        String selectMenu = null;
        if (uri.startsWith("/trade/coin")) {
            selectMenu = "financial";
        } else if (uri.startsWith("/about/wallet")) {
            selectMenu = "wallet";
        } else if (uri.startsWith("/about/")) {
            selectMenu = "about";
        } else if (uri.startsWith("/user/login") || uri.startsWith("/user/register")) {
            selectMenu = "index";
        } else if (uri.startsWith("/financial") || uri.startsWith("/balance/") || uri.startsWith("/crowd/logs")
                || uri.startsWith("/trade/entrust") || uri.startsWith("/account")) {
            selectMenu = "financial";
        } else if (uri.startsWith("/crowd/index") || uri.startsWith("/crowd/detail")) {
            selectMenu = "crowd";
        } else if (uri.startsWith("/vote/")) {
            selectMenu = "vote";
        } else if (uri.startsWith("/user/") || uri.startsWith("introl/mydivide")) {
            selectMenu = "user";
        } else if (uri.startsWith("/market.html")) {
            selectMenu = "market";
        } else {
            selectMenu = "index";
        }
        request.setAttribute("selectMenu", selectMenu);
        // 左侧菜单
        if (uri.startsWith("/trade") || uri.startsWith("/user") || uri.startsWith("/shop/")
                || uri.startsWith("/divide/") || uri.startsWith("/crowd/") || uri.startsWith("/balance/")
                || uri.startsWith("/introl/mydivide") || uri.startsWith("/account") || uri.startsWith("/financial")
                || uri.startsWith("/coinage/") || uri.startsWith("/vote/")// 融资融币
                || uri.startsWith("/question") || uri.startsWith("/ctc/list2")) {
            String leftMenu = null;

            if (uri.startsWith("/question/questionColumn")) {
                leftMenu = "questionColumn";
            } else if (uri.startsWith("/question/question")) {
                leftMenu = "question";
            } else if (uri.startsWith("/user/realCertification")) {
                leftMenu = "real";
            }
            if (uri.startsWith("/question/message")) {
                leftMenu = "message";
            } else if (uri.startsWith("/user/security")) {
                leftMenu = "security";
            } else if (uri.startsWith("/user/userloginlog")) {
                leftMenu = "loginlog";
            } else if (uri.startsWith("/account/record")) {
                leftMenu = "record";
            } else if (uri.startsWith("/financial/accountalipay") || uri.startsWith("/financial/accountbank")
                    || uri.startsWith("/financial/accountcoin")) {
                leftMenu = "accountAdd";
            } else if (uri.startsWith("/crowd/mylogs") || uri.startsWith("/crowd/logs")) {
                leftMenu = "mylogs";
            } else if (uri.startsWith("/account/deduct")) {
                leftMenu = "record";
            } else if (uri.startsWith("/account/rechargeCny") || uri.startsWith("/account/proxyCode")
                    || uri.startsWith("/account/payCode")) {
                leftMenu = "recharge";
            } else if (uri.startsWith("/account/withdrawCny")) {
                leftMenu = "withdraw";
            } else if (uri.startsWith("/account/rechargeBtc")) {
                leftMenu = "recharge";
            } else if (uri.startsWith("/account/withdrawBtc")) {
                leftMenu = "withdraw";
            } else if (uri.startsWith("/account/btcTransport")) {
                leftMenu = "btcTransport";
            } else if (uri.startsWith("/trade/coin")) {
                leftMenu = "tradeCoin";
            } else if (uri.startsWith("/trade/entrust")) {
                try {
                    String strStatus = request.getParameter("status");
                    if (strStatus == null || "".equals(strStatus))
                        strStatus = "0";
                    int status = Integer.parseInt(strStatus.trim());
                    if (status == 0) {
                        leftMenu = "entrust0";
                    } else {
                        leftMenu = "entrust1";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    leftMenu = "entrust0";
                }
            } else if (uri.startsWith("/financial/assetsrecord")) {
                leftMenu = "assetsrecord";
            } else if (uri.startsWith("/financial/index")) {
                leftMenu = "financial";
            } else if (uri.startsWith("/introl/")) {
                leftMenu = "intro";
            } else if (uri.startsWith("/ctc/list2")) {
                leftMenu = "c2cOrder";
            }
            request.setAttribute("leftMenu", leftMenu);
        }
    }

    @ModelAttribute
    public void addConstant(HttpServletRequest request) {// 此方法会在每个controller前执行

        String userAgent = request.getHeader("USER-AGENT");
        boolean isphone = CheckMobile.check(userAgent);
        request.setAttribute("isphone", isphone);

        // 前端常量
        request.setAttribute("constant", constantMap.getMap());
        String ossURL = OSSPostObject.URL;
        if (Constant.IS_OPEN_OSS.equals("false")) {
            ossURL = "";
        }
        request.setAttribute("oss_url", ossURL);
    }

    // 发送短信验证码，已登录用户
    public boolean SendMessage(Fuser fuser, int fuserid, String areaCode, String phone, int type)
            throws UnknownHostException {
        boolean canSend = true;

        MessageValidate messageValidate = this.validateMap.getMessageMap(fuserid + "_" + type);
        if (messageValidate != null && Utils.timeMinus(Utils.getTimestamp(), messageValidate.getCreateTime()) < 120) {
            return canSend;
        }
        /*if (!"86".equals(areaCode)) {
            phone = String.format("%s%s", areaCode, phone);
        }*/
        if (MessageTypeEnum.BANGDING_MOBILE == type) {
            JayunMessage message = new JayunMessage(request);
            canSend = message.requestRegister(phone);
            log.info("聚安返回信息：" + message.getMessage());
            return canSend;
        } else {
            JayunSecurity security = new JayunSecurity(request);
            canSend = security.requestVerify(fuser.getFloginName());
            log.info("聚安返回信息：" + security.getMessage());
            return canSend;
        }
    }

    public boolean validateMessageCode(Fuser fuser, String areaCode, String phone, int type, String code) {
        boolean match = true;
        JayunSecurity security = new JayunSecurity(request);
        match = security.checkVerify(fuser.getFloginName(), code);
        log.info("聚安返回信息：" + security.getMessage());
        return match;
    }

    public boolean validateMessageCode(String ip, String areaCode, String phone, int type, String code) {
        if (type != MessageTypeEnum.REG_CODE && type != MessageTypeEnum.FIND_PASSWORD) {
            log.error("调用方法错误");
            return false;
        }
        if (!"86".equals(areaCode)) {
            phone = String.format("+%s%s", areaCode, phone);
        }
        JayunMessage message = new JayunMessage(request);
        boolean status = message.checkRegister(phone, code);
        log.info("聚安返回信息：" + message.getMessage());
        return status;
    }

    // 发送邮件验证码，未登录用户
    public boolean SendMail(String ip, String mail, int type) {
        String key1 = ip + "mail_" + type;
        String key2 = ip + "_" + mail + "_" + type;
        log.info("==========KEY1==========" + key1);
        log.info("==========KEY2==========" + key2);
        // 限制ip120秒发送
        Emailvalidate mailValidate = this.validateMap.getMailMap(key1);
        if (mailValidate != null && Utils.timeMinus(Utils.getTimestamp(), mailValidate.getFcreateTime()) < 120) {
            return false;
        }

        mailValidate = this.validateMap.getMailMap(key2);
        if (mailValidate != null && Utils.timeMinus(Utils.getTimestamp(), mailValidate.getFcreateTime()) < 120) {
            return false;
        }

        Emailvalidate mailValidate2 = new Emailvalidate();
        mailValidate2.setCode(Utils.randomInteger(6));
        mailValidate2.setFcreateTime(Utils.getTimestamp());
        mailValidate2.setFmail(mail);

        this.validateMap.putMailMap(key1, mailValidate2);
        this.validateMap.putMailMap(key2, mailValidate2);

        Fvalidateemail fvalidateemail = new Fvalidateemail();
        fvalidateemail.setEmail(mail);
        fvalidateemail.setFcontent(
                this.constantMap.getString(ConstantKeys.regmailContent).replace("#code#", mailValidate2.getCode()));
        fvalidateemail.setFcreateTime(Utils.getTimestamp());
        fvalidateemail.setFstatus(ValidateMailStatusEnum.Not_send);
        fvalidateemail.setFtitle(this.constantMap.getString(ConstantKeys.WEB_NAME) + "注册验证码");
        this.frontValidateService.addFvalidateemail(fvalidateemail);

        this.taskList.returnMailList(fvalidateemail.getFid());

        return true;
    }

    public boolean validateMailCode(String ip, String mail, int type, String code) {
        // String key1 = ip+"mail_"+type ;
        String key2 = ip + "_" + mail + "_" + type;
        log.info("==========KEY2==========" + key2);
        boolean match = true;
        Emailvalidate emailvalidate = this.validateMap.getMailMap(key2);
        if (emailvalidate == null) {
            match = false;
        } else {
            if (!emailvalidate.getFmail().equals(mail) || !emailvalidate.getCode().equals(code)) {
                match = false;
            } else {
                match = true;
                // this.validateMap.removeMailMap(key1);
                // this.validateMap.removeMailMap(key2);
            }
        }

        return match;
    }

    public int totalPage(int totalCount, int maxResults) {
        return totalCount / maxResults + (totalCount % maxResults == 0 ? 0 : 1);
    }

    public Flimittrade isLimitTrade(int vid) {
        Flimittrade flimittrade = null;
        String filter = "where ftrademapping.fid=" + vid;
        List<Flimittrade> flimittrades = this.limittradeService.list(0, 0, filter, false);
        if (flimittrades != null && flimittrades.size() > 0) {
            flimittrade = flimittrades.get(0);
        }
        return flimittrade;
    }

    // 图片验证码
    public boolean vcodeValidate(HttpServletRequest request, String vcode) {
        Object session_code = request.getSession().getAttribute("checkcode");
        if (session_code == null || !vcode.equalsIgnoreCase((String) session_code)) {
            return false;
        } else {
            getSession(request).removeAttribute("checkcode");
            return true;
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        try {
            String ip = request.getHeader("X-Forwarded-For");
            try {
                if (ip != null && ip.trim().length() > 0) {
                    log.info("xxxxxxxxxxxxx" + ip.split(",")[0]);
                    return ip.split(",")[0];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ip = request.getHeader("X-Real-IP");
                if ((ip != null && ip.trim().length() > 0) && (!"unKnown".equalsIgnoreCase(ip))) {
                    return ip;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("http_client_ip");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            // 如果是多级代理，那么取第一个ip为客户ip
            if (ip != null && ip.indexOf(",") != -1) {
                ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
            }
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
            return request.getRemoteAddr();
        }
    }

    // 判断当前是否有登录
    public boolean isLogon(HttpServletRequest request) {
        return GetCurrentUser(request) != null;
    }

    // 取得当前用户的id，若无，则返回0
    public int getUserId() {
        if (this.request == null)
            throw new RuntimeException("请先调用 isLogon，并判断返回值是否为true");

        Fuser user = GetCurrentUser(this.request);
        return user != null ? user.getFid() : 0;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}
