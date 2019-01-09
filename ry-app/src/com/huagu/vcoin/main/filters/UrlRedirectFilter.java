package com.huagu.vcoin.main.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.huagu.vcoin.util.HttpUtils;
import net.sf.json.util.JSONUtils;
import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.utils.IOUtils;
import com.alibaba.fastjson.JSONObject;
import com.huagu.vcoin.main.controller.front.FrontUserJsonController;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.util.Constant;
import com.zhongyinghui.security.utils.HttpClientUtils;
import com.zhongyinghui.security.utils.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;

/**
 * 请求重定向
 **/
public class UrlRedirectFilter implements Filter {
    private static final Logger log = Logger.getLogger(UrlRedirectFilter.class);
    private static List<String> commonPages = new ArrayList<>();

    static {
        commonPages.add("/index.html"); // 首页
        commonPages.add("/indexbitbs.html");// 测试首页
        commonPages.add("/appapi.html"); // app api
        commonPages.add("/app/article.html");
        commonPages.add("/qqLogin");// qq
        commonPages.add("/link/wx/callback");// qq
        commonPages.add("/link/qq/call");// qq
        commonPages.add("/error/");// error
        commonPages.add("/api/");// api
        commonPages.add("/data/ticker.html");
        commonPages.add("/user/sendfindpasswordmsg");// api
        commonPages.add("/user/sendregmsg");// api
        commonPages.add("/json/findpassword");// api
        commonPages.add("/line/period-btcdefault.html");
        commonPages.add("/data/depth-btcdefault.html");
        commonPages.add("/data/stock-btcdefault.html");
        commonPages.add("/index_chart.html");
        commonPages.add("/user/login");// 登录
        commonPages.add("/user/logout");// 退出
        commonPages.add("/user/reg");// 注册
        commonPages.add("/trademarket.html");// 注册
        commonPages.add("/app/suc.html");// 注册
        commonPages.add("/real/");// 行情
        commonPages.add("/market");// 行情
        commonPages.add("/block/");
        commonPages.add("/kline/");// 行情
        commonPages.add("/json.html");// 行情
        commonPages.add("/json/");// 行情
        commonPages.add("/validate/");// 邮件激活,找回密码
        commonPages.add("/about/");// 文章管理
        commonPages.add("/crowd/index"); // 代币售卖
        commonPages.add("/service/");// 文章管理
        commonPages.add("/shop/index.html");// 文章管理
        commonPages.add("/shop/view.html");// 文章管理
        commonPages.add("/shop/showdetails");// 文章管理
        commonPages.add("/dowload/index");// 文章管理
        commonPages.add("/vote/list");// 文章管理
        commonPages.add("/business.html");// 文章管理
        commonPages.add("/vote/details");// 文章管理
        commonPages.add("/trade/type");// 文章管理
        commonPages.add("/trade/coin");// 文章管理
        commonPages.add("/trade/entrustinfo");
        commonPages.add("/user/sendMailCode".toLowerCase());// 注册邮件
        commonPages.add("/user/sendMsg".toLowerCase());// 注册短信
        commonPages.add("/user/sendSMS".toLowerCase());// 安全验证码
        commonPages.add("/ssadmin/sendMsg".toLowerCase());// 后台发送短信验证码
        commonPages.add("/appversion/appversion");// 安装页面
        commonPages.add("/ctc/index");// C2C首页
        commonPages.add("/otc/index");// O2C首页
        commonPages.add("/otc/orderList");// O2C首页
        commonPages.add("/ctc/instructions");// C2C操作说明
        commonPages.add("/index/ctcstatus");// C2C开启状态
        commonPages.add("/user/api/login");// 登录接口
        commonPages.add("/user/api/reg");// 注册接口
        commonPages.add("/api/resetPhoneValidation".toLowerCase());// 找回密码(验证)接口
        commonPages.add("/api/resetPasswordPhone".toLowerCase());// 找回密码(修改)接口
        commonPages.add("/user/api/sendMsg".toLowerCase());// 发送短信接口
        commonPages.add("/user/api/validateIdentity".toLowerCase());// 身份认证接口
        commonPages.add("/user/api/market".toLowerCase());
        commonPages.add("/user/api/addBankCard".toLowerCase());
        commonPages.add("/user/api/indexmarket".toLowerCase());
        commonPages.add("/user/api/otcMenuList".toLowerCase());
        commonPages.add("/user/api/otcList".toLowerCase());
        commonPages.add("/user/api/upload".toLowerCase());
        commonPages.add("/user/api/postValidateMail".toLowerCase());
        commonPages.add("/user/api/coin".toLowerCase());
        commonPages.add("/user/api/trade/sellBtcSubmit".toLowerCase());
        commonPages.add("/user/api/trade/buyBtcSubmit".toLowerCase());
        commonPages.add("/user/api/resetPhone".toLowerCase());
        commonPages.add("/user/api/modifyPwd".toLowerCase());
        commonPages.add("/user/api/help".toLowerCase());
        commonPages.add("/user/api/question".toLowerCase());
        commonPages.add("/user/api/notice".toLowerCase());
        commonPages.add("/user/api/newversion".toLowerCase());
        commonPages.add("/user/api/myposter".toLowerCase());
        commonPages.add("/user/api/activation".toLowerCase());
        commonPages.add("/api/user/updateOutAddress".toLowerCase());// 银行卡接口
        commonPages.add("/tradeview/");
        commonPages.add("/validate/resetPhone".toLowerCase());
        commonPages.add("/validate/resetPwdPhone".toLowerCase());
        commonPages.add("/user/synUsers".toLowerCase());
        commonPages.add("/user/synUser_import".toLowerCase());
        commonPages.add("/user/transferCoin".toLowerCase());
        commonPages.add("/index/atprice".toLowerCase());
        commonPages.add("/index/ctcMenu".toLowerCase());
        commonPages.add("/coin/rotation".toLowerCase());
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            tosaveLog(req, resp);
        }catch (Exception e){
            e.printStackTrace();
        }

        String uri = req.getRequestURI().toLowerCase().trim();
        if (!"/real/indexmarket.html".equals(uri) && !"/kline/trade_history.html".equals(uri)
                && !"/real/market2.html".equals(uri) && !"/real/userassets.html".equals(uri)
                && !"/tradeview/history.html".equals(uri)) {
            log.info(uri);
        }

        // 不接受任何jsp请求
        if (uri.endsWith(".jsp")) {
            return;
        }

        // 只拦截.html结尾的请求
        if (!uri.endsWith(".html") || uri.indexOf("app") > -1) {
            System.out.println(!uri.startsWith("/api/app/") && !uri.startsWith("/api/trading/"));
            if (!uri.startsWith("/api/app/") && !uri.startsWith("/api/trading/")) {
                chain.doFilter(request, response);
                return;
            }
        }

        /*
         * String redirect = MobileUtils.Redirect(req, uri); if (redirect != null) {
         * resp.sendRedirect(redirect); return; }
         */

        if (uri.startsWith("/m/")) {
            chain.doFilter(request, response);
            return;
        }

        for (String item : commonPages) {
            if (uri.startsWith(item)) {
                if (!"/real/indexmarket.html".equals(uri) && !"/kline/trade_history.html".equals(uri)
                        && !"/real/market2.html".equals(uri) && !"/real/userassets.html".equals(uri)
                        && !"/tradeview/history.html".equals(uri))
                    log.info("进入公开页：" + uri);
                if (uri.startsWith("/api/")) {
                    if (uri.startsWith("/api/app/")) {
                        uri = req.getRequestURI();
                        String securityKey = SecurityUtils.getSecurityKey(req, resp,
                                FrontUserJsonController.securtyKey);
                        Map<String, String> requestParam = SecurityUtils.getParamJson(req, resp);
                        requestParam.put("securityKey", securityKey);
                        JSONObject obj = new JSONObject();
                        obj.putAll(requestParam);
                        String result = HttpClientUtils.doPost(Constant.SettlementIP + uri, obj);
                        req.setCharacterEncoding("utf-8");
                        resp.setCharacterEncoding("utf-8");
                        resp.setHeader("Content-type", "text/html;charset=utf-8");
                        resp.getWriter().print(result);
                        resp.setStatus(200);
                        return;
                    }

                    if (!uri.startsWith("/api/trading/")) {

                        if (uri.startsWith("/api/app/")) {
                            // app 掉收益系统 boolean b =
                            boolean b = SecurityUtils.isCorrectRequest(req, resp, FrontUserJsonController.securtyKey);
                            if (!b) {
//                                System.out.println("........." + req.getContentLength());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
//                                System.out.println("........." + IOUtils.read(reader));
//                                System.out.println("........." + req.getParameter("userId"));
                                Map<String, String[]> map = req.getParameterMap();
                                JSONObject json1 = new JSONObject();
                                String str = "";
                                str += "uri=" + uri + "&";
                                for (String string : map.keySet()) {
                                    for (String string2 : map.get(string)) {
                                        json1.put(string, string2);
                                        str += string + "=" + string2 + "&";
                                    }
                                }
                                resp.sendRedirect(Constant.OSSURL + "/api/trading/getjson.html?" + str);
                                return;

                            }
                        }

                        /* resp.sendRedirect(Constant.OSSURL + "/api/trading/return.html"); */
                    }
                }
                chain.doFilter(request, response);
                return;
            }
        }

        if (uri.startsWith("/ssadmin/")) {
            log.info("进入后台页：" + uri);
            if (uri.startsWith("/ssadmin/d6d5d4d37164a3d0b363d0d64d1f782a.html")
                    || uri.startsWith("/ssadmin/submitlogin__ys333.html")
                    || req.getSession().getAttribute("login_admin") != null) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 发现有登录讯息
        Object login_user = req.getSession().getAttribute("login_user");
        if (login_user == null) {
            if (uri.startsWith("/ssadmin/")) {
                resp.sendRedirect(Constant.OSSURL + "/ssadmin/d6d5d4d37164a3d0b363d0d64d1f782a.html");
            } else {
                resp.sendRedirect(Constant.OSSURL + "/user/login.html?forwardUrl=" + req.getRequestURI().trim());
            }
            return;
        }

        // 提交身份认证信息
        if (((Fuser) login_user).getFpostRealValidate() || ("admin".equals(((Fuser) login_user).getFrealName()))) {
            chain.doFilter(request, response);
            return;
        }

        if (uri.startsWith("/user/realCertification.html".toLowerCase())
                || uri.startsWith("/user/validateidentity.html") || uri.startsWith("/user/validateidentity1.html")) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(Constant.OSSURL + "/user/realCertification.html");
        }
        return;

    }

    /**
     * 向结算系统保存日志
     *
     * @param req
     * @param resp
     */
    private void tosaveLog(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        if(requestURI.endsWith(".jpg") || requestURI.endsWith(".png") ||
                requestURI.endsWith(".js") || requestURI.endsWith(".css") ||
                requestURI.equals("/error") || requestURI.endsWith(".gif") ||
                requestURI.endsWith(".woff2") || requestURI.endsWith(".map")
                || requestURI.endsWith(".ico")
                ){

            return;
        }
        Map<String, Object> requestParam = new HashMap<>();

        String ipAddr = HttpUtils.getIpAddr(req);
        String type = "normal";
        requestParam.put("method",requestURI);
        requestParam.put("ip",ipAddr);
        Map<String, String> paramJson = HttpUtils.readRequestParam(req,resp);
        requestParam.put("params",JSON.toJSONString(paramJson));
        requestParam.put("operation",type);
//        requestParam.put("gmtCreate",new Date());
        requestParam.put("app","trading");
        String securityKey = SecurityUtils.getSecurityKey(requestParam, FrontUserJsonController.securtyKey);
        requestParam.put("securityKey",securityKey);

        String requestUrl = Constant.SettlementIP + "/api/settlement/saveLog";
        JSONObject jsonObject = new JSONObject(requestParam);
        String result = HttpClientUtils.doPost(requestUrl, jsonObject);
        System.out.println(result);

    }
}
