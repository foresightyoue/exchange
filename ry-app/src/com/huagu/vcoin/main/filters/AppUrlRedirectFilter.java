package com.huagu.vcoin.main.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.MobileUtils;

/**
 * 请求重定向
 **/
public class AppUrlRedirectFilter implements Filter {
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

        String uri = req.getRequestURI().toLowerCase().trim();

        // 不接受任何jsp请求
        if (uri.endsWith(".jsp")) {
            return;
        }

        // 只拦截.html结尾的请求
        if (!uri.endsWith(".html")) {
            chain.doFilter(request, response);
            return;
        } else {
            String redirect = MobileUtils.Redirect(req, uri);
            if (redirect != null) {
                resp.sendRedirect(redirect);
                return;
            }
        }

        ///////////////////////////////////////////////////////////////////////////////
        if (uri.startsWith("/app/index")// 首页
                || uri.startsWith("/m/index")// 首页
                || uri.startsWith("/app/logindex")// 我的
                || uri.startsWith("/m/top5paihang")// app apiiiiii
                || uri.startsWith("/m/json2")// app apiiiiii
                || uri.startsWith("/m/appapi.html")// app apiiiiii
                || uri.startsWith("/m/app/article.html") || uri.startsWith("/m/reg-notice")
                || uri.startsWith("/m/validate/reset")

                || uri.startsWith("/m/qqLogin")// qq
                || uri.startsWith("/m/link/wx/callback")// qq
                || uri.startsWith("/m/link/qq/call")// qq
                || uri.startsWith("/m/error/")// error
                || uri.startsWith("/m/api/")// api
                || uri.startsWith("/m/data/ticker.html") || uri.startsWith("/m/user/sendfindpasswordmsg")// api
                || uri.startsWith("/m/user/sendregmsg")// api
                || uri.startsWith("/m/json/findpassword")// api
                || uri.startsWith("/m/line/period-btcdefault.html") || uri.startsWith("/m/data/depth-btcdefault.html")
                || uri.startsWith("/m/data/stock-btcdefault.html") || uri.startsWith("/m/index_chart.html")
                || uri.startsWith("/m/user/login")// 登录
                || uri.startsWith("/m/user/logout")// 退出
                || uri.startsWith("/m/user/reg")// 注册
                || uri.startsWith("/m/trademarket.html")// 注册
                || uri.startsWith("/m/app/suc.html")// 注册
                || uri.startsWith("/m/real/")// 行情
                || uri.startsWith("/m/market")// 行情
                || uri.startsWith("/m/block/") || uri.startsWith("/m/kline/")// 行情
                || uri.startsWith("/m/json.html")// 行情
                || uri.startsWith("/m/json/")// 行情
                || uri.startsWith("/m/validate/")// 邮件激活,找回密码
                || uri.startsWith("/m/about/")// 文章管理
                || (uri.startsWith("/m/crowd/index")) || uri.startsWith("/m/service/")// 文章管理
                || uri.startsWith("/m/shop/index.html")// 文章管理
                || uri.startsWith("/m/shop/view.html")// 文章管理
                || uri.startsWith("/m/shop/showdetails")// 文章管理
                || uri.startsWith("/m/ryb/tosearch")// 搜索
                || uri.startsWith("/m/ryb/search")// 搜索
                || uri.startsWith("/m/dowload/index")// 文章管理
                || uri.startsWith("/m/vote/list")// 文章管理
                || uri.startsWith("/m/business.html")// 文章管理
                || uri.startsWith("/m/login/sendmsg")// 发送短信
                || uri.startsWith("/m/vote/details")// 文章管理
                || uri.startsWith("/m/trade/type")// 文章管理
                || uri.startsWith("/m/trade/coin")// 文章管理
                || uri.startsWith("/m/trade/entrustinfo") || uri.startsWith("/m/user/sendMailCode".toLowerCase())// 注册邮件
                || uri.startsWith("/m/user/sendMsg".toLowerCase())// 注册短信
                || uri.startsWith("/m/appversion")// 版本更新
                || uri.startsWith("/m/introl/register")// 推广注册
                || uri.startsWith("/m/introl/index")// 推广注册
                || uri.startsWith("/m/user/api/login")// 登录接口
                || uri.startsWith("/m/user/api/reg")// 注册接口
                || uri.startsWith("/m/api/resetPhoneValidation".toLowerCase())// 找回密码(验证)接口
                || uri.startsWith("/m/api/resetPasswordPhone".toLowerCase())// 找回密码(修改)接口
                || uri.startsWith("/m/api/otc/userReceipt".toLowerCase())// 微信、支付宝账号接口
                || uri.startsWith("/m/introl/giftIndex".toLowerCase())// 推广注册
                || uri.startsWith("/m/tv-chart".toLowerCase())// 推广注册
                || uri.startsWith("/m/user/api/android/validateIdentity".toLowerCase())//身份认证(android)
                || uri.startsWith("/m/user/api/bankCardType".toLowerCase())
                || uri.startsWith("/m/user/api/addBankCard".toLowerCase())
                || uri.startsWith("/m/user/api/getBankCardInfo".toLowerCase())
                || uri.startsWith("/m/user/api/activation".toLowerCase())
                || uri.startsWith("/m/user/api/market".toLowerCase())
                || uri.startsWith("/m/app/api/version".toLowerCase())
                || uri.startsWith("/m/api/app/getCoinTotal".toLowerCase())
                || uri.startsWith("/m/user/api/info/mail".toLowerCase())
                || uri.startsWith("/m/api/postValidateMail".toLowerCase())
                || uri.startsWith("/m/api/market/trademap/favairate/set".toLowerCase())
                || uri.startsWith("/m/api/market/trademap/favairate".toLowerCase())
                || uri.startsWith("/m/user/api/info".toLowerCase())
                || uri.startsWith("/m/user/api/info/identity".toLowerCase())
                || uri.startsWith("/m/user/seller/identity".toLowerCase())
                || uri.startsWith("/m/user/api/info/seller".toLowerCase())
                || uri.startsWith("/m/user/api/info/seller/detail".toLowerCase())
                || uri.startsWith("/m/reference/list".toLowerCase())
                || uri.startsWith("/m/user/reference/submit".toLowerCase())
                || uri.startsWith("/m/user/api/activation/list".toLowerCase())
                || uri.startsWith("/m/user/api/info/seller/sellingOrder".toLowerCase())
                || uri.startsWith("/m/user/api/info/seller/buyingOrder".toLowerCase())
                || uri.startsWith("/m/user/api/info/seller/orderDetail".toLowerCase())
                || uri.startsWith("/m/user/api/info/seller/announceList".toLowerCase())
                || uri.startsWith("/m/user/api/info/seller/announceOnOff".toLowerCase())
                || uri.startsWith("/m/user/api/info/seller/doAnnounce".toLowerCase())
                || uri.startsWith("/m/user/api/url".toLowerCase())
                || uri.startsWith("/m/user/api/pic/upload".toLowerCase())//上传身份认证照片
                || uri.startsWith("/m/marketforappt".toLowerCase())
                || uri.startsWith("/m/user/wallet/transfer/log".toLowerCase())
        ) {
            chain.doFilter(request, response);
            return;
        } else {
            Object login_user = req.getSession().getAttribute("login_user");
            if (login_user == null) {
                resp.sendRedirect(Constant.OSSURL + "/m/user/login.html");
                return;
            }
            // 提交身份认证信息
            if (((Fuser) login_user).getFpostRealValidate() || ("admin".equals(((Fuser) login_user).getFrealName()))) {
                chain.doFilter(request, response);
                return;
            }
            chain.doFilter(request, response);
//            if (uri.startsWith("/m/tradepwd.html")||uri.startsWith("/m/security/realid.html")) {
//                chain.doFilter(request, response);
//            } else {
//            	if("".equals(((Fuser) login_user).getFrealName())) {
//            		resp.sendRedirect(Constant.OSSURL + "/m/tradepwd.html");
//            	}else {
//            		resp.sendRedirect(Constant.OSSURL + "/m/security/realId.html");
//            	}
//            }
        }

    }

}
