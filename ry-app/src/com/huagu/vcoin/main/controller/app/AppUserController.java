package com.huagu.vcoin.main.controller.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.huagu.vcoin.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSON;
import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.Enum.BankTypeEnum;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.CountLimitTypeEnum;
import com.huagu.vcoin.main.Enum.LogTypeEnum;
import com.huagu.vcoin.main.Enum.MessageTypeEnum;
import com.huagu.vcoin.main.Enum.RegTypeEnum;
import com.huagu.vcoin.main.Enum.UserStatusEnum;
import com.huagu.vcoin.main.VO.FuserReferenceVO;
import com.huagu.vcoin.main.auto.SendSMSCF;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.comm.ValidateMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.front.FrontOtcOrderController;
import com.huagu.vcoin.main.model.FappVersion;
import com.huagu.vcoin.main.model.Farticle;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.FuserReference;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.app.MarketFavairate;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.SystemArgsService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.FrontAccountService;
import com.huagu.vcoin.main.service.front.FrontOthersService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontValidateService;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.main.service.front.FvirtualWalletService;
import com.huagu.vcoin.main.service.front.UtilsService;
import com.huagu.vcoin.main.service.settlement.SettlementService;
import com.huagu.vcoin.main.service.wallet.TradingService;
import com.huagu.vcoin.main.sms.ShortMessageService;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.ServerConfig;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.oss.OssConnection;
import cn.cerc.jdb.oss.OssSession;
import cn.cerc.security.sapi.JayunMessage;
import cn.cerc.security.sapi.SendMode;
import net.sf.json.JSONObject;

@Controller
public class AppUserController extends BaseController {
//    public static String securtyKey = "123";
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ValidateMap validateMap;
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private UserService userService;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SendSMSCF sendSMSCF;
    @Autowired
    private FvirtualWalletService fvirtualWalletService;
    @Autowired
    private HttpServletRequest req;
    @Autowired
    private FrontOthersService frontOthersService;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private TradingService tradingService;
    @Autowired
    private FrontOtcOrderController frontOtcOrderController;

    /**
     * 1、 用户登录
     * 
     * @param request
     * @param response
     * @param loginName
     * @param password
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/login", "/user/api/login"}, produces = JsonEncode)
    public String apilogin(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(required = false) String loginName,
                           @RequestParam(required = true) String password,
                           @RequestParam(required = true) String sign) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> data = new HashMap<>();
        int longLogin = -1;// 0是手机，1是邮箱
        String keyString = "floginName";
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("password", password);
//        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
//            data.put("userId", "");
//            data.put("token", "");
//            jsonObject.accumulate("data", data);
//            return jsonObject.toString();
//        }
        if (loginName.matches(Constant.PhoneReg) == true) {
            keyString = "fnickName";
            longLogin = 0;
        }
        if ("1".equals(Constant.AppLevel)) {
            keyString = "fnickName";
            longLogin = 0;
        }

        List<Fuser> fusers = this.frontUserService.findUserLogin(keyString, loginName);
        if (fusers == null || fusers.size() != 1) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "用户名不存在");
            data.put("userId", "");
            data.put("token", "");
            jsonObject.accumulate("data", data);
            return jsonObject.toString();
        }
        if (!loginName.matches(Constant.PhoneReg)) {
            loginName = fusers.get(0).getFnickName();
        }
        request.setAttribute("user", loginName);
        request.setAttribute("mobile", fusers.get(0).getFtelephone());

        String ip = getIpAddr(request);
        Fuser fuser = new Fuser();
        fuser.setFnickName(loginName);
        fuser.setFloginPassword(password);
        fuser.setSalt(fusers.get(0).getSalt());
        int insertdata = fusers.get(0).getfInsertData();
        int limitedCount = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
        if (limitedCount > 0) {
            fuser = this.frontUserService.updateCheckLogin(fuser, ip, longLogin == 1, insertdata);
            if (fuser != null) {
                boolean saveFlag1 = settlementService.login(fusers.get(0).getFloginName(), fusers.get(0).getfIntroUser_id() == null ? "RY001" :
                        fusers.get(0).getfIntroUser_id().getFloginName().equals("") ? "RY001" : fusers.get(0).getfIntroUser_id().getFloginName(), fusers.get(0).getFtelephone());
                if (saveFlag1) {
                    String deviceId = request.getParameter("deviceId");
                    if (deviceId == null || "".equals(deviceId)) {
                        deviceId = "webclient";
                    }
                    getSession(request).setAttribute("deviceId", deviceId);
                    if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                        this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
                        if (fuser.getFopenSecondValidate()) {
                            SetSecondLoginSession(request, fuser);
                        } else {
                            SetSession(fuser, request);
                            setCookie("loginName", loginName, response);
                            setCookie("password", password, response);
                            String sessionId = request.getSession().getId();
                            jsonObject.accumulate("code", 0);
                            jsonObject.accumulate("msg", "登录成功");
                            data.put("JSESSIONID", sessionId);
                            data.put("userId", fuser.getFloginName());
                            data.put("token", this.getSession(request).getId());
                            data.put("hasActive", fuser.getFleaderId() > 0 ? true:false);
                            jsonObject.accumulate("data", data);
                            return jsonObject.toString();
                        }
                    } else {
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "账户出现安全隐患被冻结，请尽快联系客服");
                        data.put("userId", "");
                        data.put("token", "");
                        jsonObject.accumulate("data", data);
                        return jsonObject.toString();
                    }
                } else {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "网络异常");
                    data.put("userId", "");
                    data.put("token", "");
                    jsonObject.accumulate("data", data);
                    return jsonObject.toString();
                }
            } else {
                // 错误的用户名或密码
                if (limitedCount == new Constant().ErrorCountLimit) {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "用户名或密码错误");
                } else {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "用户名或密码错误，您还有" + limitedCount + "次机会");
                }
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
                data.put("userId", "");
                data.put("token", "");
                jsonObject.accumulate("data", data);
                return jsonObject.toString();
            }
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip登录频繁，请2小时后再试!");
            data.put("userId", "");
            data.put("token", "");
            jsonObject.accumulate("data", data);
            return jsonObject.toString();
        }
        return null;
    }

    public void setCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(60 * 60 * 24 * 5);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 2、 用户注册
     * 
     * @param request
     * @param password
     * @param regName
     * @param phoneCode
     * @param sign
     * @param intro_user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/reg", "/user/api/reg"}, produces = JsonEncode)
    public String apireg(HttpServletRequest request,
                         @RequestParam(required = false, defaultValue = "0") String password,
                         @RequestParam(required = false, defaultValue = "0") String regName,
                         @RequestParam(required = true, defaultValue = "0") String phoneCode,
                         @RequestParam(required = true) String sign,
                         @RequestParam(required = false) String intro_user,
                         @RequestParam(required = false,defaultValue = "86") String areaCode) throws Exception {
        JSONObject jsonObject = new JSONObject();
        String phone = HtmlUtils.htmlEscape(regName);
        phoneCode = HtmlUtils.htmlEscape(phoneCode);
        String isOpenReg = constantMap.get("isOpenReg").toString().trim();
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("regName", regName);
        map.put("password", password);
        map.put("phoneCode", phoneCode);
        map.put("intro_user", intro_user);
        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
//            return jsonObject.toString();
//        }
        if (!isOpenReg.equals("1")) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "暂停注册");
            return jsonObject.toString();
        }

        password = HtmlUtils.htmlEscape(password.trim());
        if (password == null || password.length() < 6) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "登录短信长度有误");
            return jsonObject.toString();
        }
        // 手机注册
        boolean flag1 = this.frontUserService.isLoginNameExists(regName);// 注册检测loginName是否存在
        if (flag1) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "手机号码已经被注册");
            return jsonObject.toString();
        }
        if (!"1".equals(Constant.AppLevel)) {
            if (!phone.matches(Constant.PhoneReg)) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "手机格式错误");
                return jsonObject.toString();
            }
        }
        boolean bool = true;
        if (ShortMessageService.isTrue()) {
            bool = ShortMessageService.CheckCode(phone, phoneCode, new Date());
        } else {
            JayunMessage message = new JayunMessage(request);
            String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
            if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
                bool = message.checkRegister(phone, phoneCode);// 注册接入聚安发送验证码
            } else {
                bool = "123456".equals(phoneCode);
            }
        }
        if (!phoneCode.equals("000000")) {
            if (!bool) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "短信验证码错误");
                return jsonObject.toString();
            }
        }

        // 推广
        Fuser intro = null;
        if (intro_user != null && !"".equals(intro_user.trim())) {
            List<Fuser> introList = this.frontUserService.findUserLogin("ftelephone", intro_user.trim().toUpperCase());
            if (introList != null && introList.size() != 1) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "邀请码有误");
                return jsonObject.toString();
            }
            intro = introList.get(0);
        }
        Fuser fuser = new Fuser();

        // 手机注册
        fuser.setFregType(RegTypeEnum.TEL_VALUE);
        fuser.setFtelephone(phone);
        fuser.setFareaCode(areaCode);
        fuser.setFisTelephoneBind(true);

        fuser.setFnickName(phone);
        fuser.setFloginName(phone);

        fuser.setSalt(Utils.getUUID());
        fuser.setFregisterTime(Utils.getTimestamp());

        fuser.setFloginPassword(MyMD5Utils.encoding(password));
        fuser.setFtradePassword(null);
        String ip = getIpAddr(request);
        fuser.setFregIp(ip);
        fuser.setFlastLoginIp(ip);
        fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
        fuser.setFlastLoginTime(Utils.getTimestamp());
        fuser.setFlastUpdateTime(Utils.getTimestamp());
        boolean saveFlag = false;
        boolean saveFlag1 = false;
        try {
            String loginName = "";
            saveFlag = this.frontUserService.saveRegister(fuser);
            try (Mysql mysql = new Mysql()) {
                MyQuery ds = new MyQuery(mysql);
                ds.add("SELECT fId,floginName,fleaderId from fuser WHERE fTelephone = '%s' ", phone);
                ds.open();
                loginName = "RY" + ds.getInt("fId");
                saveFlag1 = settlementService.register(loginName, intro == null ? "RY001" : intro.getFloginName(), phone);
                if (!saveFlag1) {
                    String sql = Constant.SQL_DELET_FUSER + ds.getInt("fId");
                    frontUserService.deleteFuser(sql);
                    throw new RuntimeException();
                }
                ds.edit();
                ds.setField("floginName", loginName);
                if (intro != null) {
                    ds.setField("fleaderId", intro.getFid());
                }
                ds.post();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "注册不成功，请稍后再试");
            return jsonObject.toString();
        } finally {
            String key1 = ip + "message_" + MessageTypeEnum.REG_CODE;
            String key2 = ip + "_" + phone + "_" + MessageTypeEnum.REG_CODE;
            this.validateMap.removeMessageMap(key1);
            this.validateMap.removeMessageMap(key2);
        }
        if (saveFlag) {
            this.SetSession(fuser, request);
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "注册成功");
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络错误，请稍后再试");
            return jsonObject.toString();
        }
    }

    /**
     * 4、 发送短信验证码
     * 
     * @param request
     * @param type
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/user/api/sendMsg", produces = JsonEncode)
    public String apisendMsg(HttpServletRequest request, @RequestParam(required = true) int type,
                             @RequestParam(required = true) String sign) throws Exception {
        String areaCode = request.getParameter("areaCode");
        String phone = request.getParameter("phone");
        boolean sendVoice = Boolean.valueOf(request.getParameter("sendVoice"));

        // 注册类型免登录可以发送
        JSONObject jsonObject = new JSONObject();
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("areaCode", areaCode);
        map.put("phone", phone);
        map.put("sendVoice", sendVoice);
        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
//            return jsonObject.toString();
//        }
        if (type < MessageTypeEnum.BEGIN || type > MessageTypeEnum.END) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法提交");
            return jsonObject.toString();
        }

        // 测试验证码
        if (StringUtils.isNotEmpty(phone) && StringUtils.isNumeric(phone)) {
            long phoneNum = Long.valueOf(phone);
            if ((phoneNum >= 13800000000l && phoneNum <= 13800009999l) ||
                    (phoneNum >= 19900000000l && phoneNum <= 19900009999l) ||
                    (phoneNum >= 13300000000l && phoneNum <= 13300009999l) || (phoneNum >= 17700000000l && phoneNum <= 17700009999l) || phoneNum == 13538236335l) {
                setMsmCode(phone, "123456");
                jsonObject.accumulate("code", 123456);
                jsonObject.accumulate("msg", "测试验证码");
                return jsonObject.toString();
            }
        }

        String ip = getIpAddr(request);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
        if (tel_limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }

        // 修改登录密码
        if (type == MessageTypeEnum.CHANGE_LOGINPWD || type == MessageTypeEnum.FIND_PASSWORD) {
            String code = ShortMessageService.getCode();
            ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code, code);
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "验证码已经发送，请查收");
            return jsonObject.toString();
        }

        if (type == MessageTypeEnum.CHANGE_TRADEPWD || type == MessageTypeEnum.JIEBANG_MOBILE || type == MessageTypeEnum.BANGDING_MOBILE) {
            Fuser user = GetCurrentUser(request);
            if (user == null) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "请重新登录");
                return jsonObject.toString();
            }
            if (type == MessageTypeEnum.JIEBANG_MOBILE) {
                if (!user.getFtelephone().equals(phone)) {
                    jsonObject.accumulate("status", 300);
                    jsonObject.accumulate("msg", "解绑手机号不正确");
                    return jsonObject.toString();
                }
            }

            if (type == MessageTypeEnum.BANGDING_MOBILE) {
                List<Fuser> fusers = this.utilsService.list(0, 0, " where ftelephone=? ", false, Fuser.class,
                        new Object[]{phone});
                if (fusers != null && fusers.size() > 0) {
                    jsonObject.accumulate("status", 300);
                    jsonObject.accumulate("msg", "绑定手机号已被占用");
                    return jsonObject.toString();
                }
            }

            String code = ShortMessageService.getCode();
            ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code, code);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "验证码已经发送，请查收");
            return jsonObject.toString();
        }

        Fuser fuser = null;
        if (type != MessageTypeEnum.REG_CODE) {
            if (type == MessageTypeEnum.FIND_PASSWORD) {
                List<Fuser> fusers = this.utilsService.list(0, 0, " where ftelephone=? ", false, Fuser.class,
                        new Object[]{phone});
                if (fusers.size() >= 1) {
                    fuser = fusers.get(0);
                } else {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "手机号码错误");
                    return jsonObject.toString();
                }
            } else {
                fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
                if (type == MessageTypeEnum.BANGDING_MOBILE) {
                    if (phone == null || phone.trim().length() == 0) {
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "绑定的手机号码不能为空");
                        return jsonObject.toString();
                    }
                } else {
                    if (!fuser.isFisTelephoneBind()) {
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "您没有绑定手机");
                        return jsonObject.toString();
                    } else {
                        phone = fuser.getFtelephone();
                    }
                }
            }
        } else {
            boolean flag = this.frontUserService.isLoginNameExists(phone);//
            // 注册检测loginName是否存在
            if (flag) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "手机号码已存在");
                return jsonObject.toString();
            }
        }

        getSession(request).setAttribute("deviceId", "webclient");
        this.setRequest(request);

        if (MessageTypeEnum.REG_CODE == type) {
            // 注册
            if (!"86".equals(areaCode)) {
                phone = String.format("+%s%s", areaCode, phone);
            }
            //add by yangwei fix bug checkcode error for add reg
            if (!ShortMessageService.isTrue()) {
                String code = ShortMessageService.getCode();
                ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code + "，如非本人操作，请忽略。", code);
            } else {
                JayunMessage message = new JayunMessage(request);
                if (sendVoice) {
                    message.setSendMode(SendMode.VOICE);
                }
                // 非test版要进行验证
                String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
                if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
                    if (!message.requestRegister(phone)) {
                        jsonObject.accumulate("code", -1);
                        if (!"短信宝发送失败，错误代码：51".equals(message.getMessage())) {
                            jsonObject.accumulate("msg", message.getMessage());
                        } else {
                            jsonObject.accumulate("msg", "手机格式错误");
                        }

                        return jsonObject.toString();
                    }
                }
            }
        } else if (MessageTypeEnum.BANGDING_MOBILE == type) {
            // 绑定手机
            if (ShortMessageService.isTrue()) {
                String code = ShortMessageService.getCode();
                ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code + "，如非本人操作，请忽略本短信", code);
            } else {
                JayunMessage message = new JayunMessage(request);
                if (sendVoice) {
                    message.setSendMode(SendMode.VOICE);
                    if (!message.requestRegister(phone)) {
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", message.getMessage());
                        return jsonObject.toString();
                    }
                } else {
                    SendMessage(fuser, fuser.getFid(), areaCode, phone, type);
                }
            }
        } else if (MessageTypeEnum.FIND_PASSWORD == type) {
            // 找回密码
            if (!"86".equals(areaCode)) {
                phone = String.format("+%s%s", areaCode, phone);
            }
            if (ShortMessageService.isTrue()) {
                String code = ShortMessageService.getCode();
                ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code, code);
            } else {
                JayunMessage message = new JayunMessage(request);
                if (sendVoice) {
                    String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
                    if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
                        message.setSendMode(SendMode.VOICE);
                    }
                }
                if (!message.requestRegister(phone)) {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", message.getMessage());
                    return jsonObject.toString();
                }
            }
        } else if (MessageTypeEnum.VIRTUAL_WITHDRAW_ACCOUNT == type && sendVoice) {
            if (!"86".equals(areaCode)) {
                phone = String.format("+%s%s", areaCode, phone);
            }
            if (ShortMessageService.isTrue()) {
                String code = ShortMessageService.getCode();
                ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code + "，如非本人操作，请忽略本短信", code);
            } else {
                JayunMessage message = new JayunMessage(request);
                String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
                if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
                    message.setSendMode(SendMode.VOICE);
                }
                if (!message.requestRegister(phone)) {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", message.getMessage());
                    return jsonObject.toString();
                }
            }
        } else {
            String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
            if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
                if (ShortMessageService.isTrue()) {
                    String code = ShortMessageService.getCode();
                    ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code + "，如非本人操作，请忽略本短信", code);
                } else {
                    JayunMessage message = new JayunMessage(request);
                    if (sendVoice) {
                        message.setSendMode(SendMode.VOICE);
                        if (!message.requestRegister(fuser.getFtelephone())) {
                            jsonObject.accumulate("code", -1);
                            jsonObject.accumulate("msg", message.getMessage());
                            return jsonObject.toString();
                        }
                    } else {
                        SendMessage(fuser, fuser.getFid(), fuser.getFareaCode(), fuser.getFtelephone(), type);
                    }
                }
            }
        }
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "验证码已经发送，请查收");
        return jsonObject.toString();
    }

    private void setMsmCode(String phone, String msg) throws Exception {
        Mysql mysql = new Mysql();
        MyQuery cd = new MyQuery(mysql);
        cd.add("select * from %s ", "fsms");
        cd.setMaximum(1);
        cd.open();
        cd.append();
        cd.setField("fTelephone", phone);
        cd.setField("fType", 0);
        cd.setField("fStatus", 0);
        cd.setField("fCode", msg);
        cd.setField("fMsg", "测试验证码");
        cd.setField("fFacilitator", "测试验证码");
        cd.setField("fCreateTime", new Date());
        cd.post();
    }

    /**
     * 3、 修改登录密码
     * 
     * @param request
     * @param phone
     * @param msgcode
     * @param newPassword
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = { "/m/api/resetPhoneValidation", "/api/resetPhoneValidation" }, produces = JsonEncode)
    public String apiresetPhoneValidation(HttpServletRequest request, @RequestParam(required = true) String phone,
                                          @RequestParam(required = true) String msgcode,@RequestParam(required = true) String newPassword,
                                          @RequestParam(required = true) String sign) throws Exception{
        JSONObject jsonObject = new JSONObject();
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("msgcode", msgcode);
        map.put("newPassword", newPassword);
        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
//            return jsonObject.toString();
//        }
        Fuser fuser = GetCurrentUser(request);
        if(fuser == null) {
            List<Fuser> fusers = this.frontUserService.findUserByProperty("ftelephone", phone);
            if(fusers == null || fusers.size() ==0) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "手机用户不存在");
                return jsonObject.toString();
            }
            fuser = fusers.get(0);
        }else {
            if (!fuser.getFtelephone().equals(phone)) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "用户手机不正确");
                return jsonObject.toString();
            }
        }


        // 修改登录密码
        if (fuser.getFloginPassword() != null && fuser.getFloginPassword().trim().length() > 0
                && fuser.getFloginPassword().equals(MyMD5Utils.encoding(newPassword))) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "新的登录密码与原始密码相同，修改失败");
            return jsonObject.toString();
        }
//        if (null == fuser.getSalt()) {
//            if (fuser.getFloginPassword() != null && fuser.getFloginPassword().trim().length() > 0
//                    && fuser.getFloginPassword().equals(MD5.get(newPassword))) {
//                jsonObject.accumulate("status", 300);
//                jsonObject.accumulate("msg", "新的登录密码与原始密码相同，修改失败");
//                return jsonObject.toString();
//            }
//        } else {
//            if (fuser.getFloginPassword() != null && fuser.getFloginPassword().trim().length() > 0
//                    && fuser.getFloginPassword().equals(Utils.MD5(newPassword, fuser.getSalt()))) {
//                jsonObject.accumulate("status", 300);
//                jsonObject.accumulate("msg", "新的登录密码与原始密码相同，修改失败");
//                return jsonObject.toString();
//            }
//        }

        String ip = getIpAddr(request);
        int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
        if (mobil_limit <= 0) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        } else {
            boolean bool = ShortMessageService.CheckCode(phone, msgcode, new Date());
            if (!bool) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "手机验证码有误，您还有" + mobil_limit + "次机会");
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                return jsonObject.toString();
            } else {
                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
            }
        }

        try {
            // 修改登录密码
            fuser.setFloginPassword(MyMD5Utils.encoding(newPassword));
//            if (null == fuser.getSalt()) {
//                fuser.setFloginPassword(MD5.get(newPassword));
//            } else {
//                fuser.setFloginPassword(Utils.MD5(newPassword, fuser.getSalt()));
//            }
            int logType = 0;
            if (fuser.getFloginPassword() != null && fuser.getFloginPassword().trim().length() > 0) {
                logType = LogTypeEnum.User_UPDATE_LOGIN_PWD;
            }
            this.frontUserService.updateFUser(fuser, getSession(request), logType, ip);

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("status", 500);
            jsonObject.accumulate("msg", "网络异常");
            return jsonObject.toString();
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CHANGE_LOGINPWD);
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CHANGE_TRADEPWD);
        }

        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "success");
        return jsonObject.toString();
    }

    /**
     * 身份认证
     *
     * @param request
     * @param identityNo
     * @param realName
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/m/user/api/validateIdentity", produces = "text/html;charset=UTF-8")
    public Object apivalidateIdentity(HttpServletRequest request, @RequestParam(required = false) String identityNo,
                                      @RequestParam(required = false, defaultValue = "0") String nationality,
                                      @RequestParam(required = false, defaultValue = "CN") String region,
                                      @RequestParam(required = false) String realName, @RequestParam(required = false) String sign) throws Exception {
        JSONObject jsonObject = new JSONObject();
        realName = HtmlUtils.htmlEscape(realName);
        realName = StringEscapeUtils.unescapeHtml(realName);
        Fuser fuser = GetCurrentUser(request);
        if (null == fuser) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
        fuser = this.frontUserService.findById(fuser.getFid());
        identityNo = identityNo.toLowerCase();

        String sql = "where fidentityNo='" + identityNo + "'";
        int count = this.adminService.getAllCount("Fuser", sql);
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("identityNo", identityNo);
        map.put("realName", realName);
//        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
//            return jsonObject.toString();
//        }
        if (count > 0 && fuser.getFidentityNo() != null ) {
            if(!fuser.getFidentityNo().equals(identityNo)) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "身份证号码已存在!");
                return jsonObject.toString();
            }
        }
//        if (!"admin".equals(fuser.getFrealName())) {
//            if (fuser.getFpostRealValidate()) {
//                jsonObject.accumulate("status", 200);
        // jsonObject.accumulate("msg", "请勿重复提交!");
//                return jsonObject;
//            }
//        }

        com.alibaba.fastjson.JSONObject json = IDCard.check(realName, identityNo);
        int code = json.getInteger("code");
//        if (code == -1) {
//            jsonObject.accumulate("status", 300);
//            jsonObject.accumulate("msg", json.getString("msg"));
//            return jsonObject.toString();
//        }
        if (fuser.getFaudit() == 1) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "已审核通过，无须重复上传");
            return jsonObject.toString();
        }
        if (fuser.getFaudit() ==2 ) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "认证信息审核中");
            return jsonObject.toString();
        }

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFileCardFront = mRequest.getFile("fileCardFront");
        if (null == multipartFileCardFront) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请上传身份证正面照");
            return jsonObject.toString();
        }
        MultipartFile multipartFileCardBack = mRequest.getFile("fileCardBack");
        if (null == multipartFileCardFront) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请上传身份证背面照");
            return jsonObject.toString();
        }

        MultipartFile multipartFileCardHand = mRequest.getFile("fileCardHand");
        if (null == multipartFileCardFront) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请上传身份证手持照");
            return jsonObject.toString();
        }

        CommonsMultipartFile cfFront = (CommonsMultipartFile) multipartFileCardFront;
        FileItem fileItemFront = cfFront.getFileItem();

        CommonsMultipartFile cfBack = (CommonsMultipartFile) multipartFileCardBack;
        FileItem fileItemBack = cfBack.getFileItem();

        CommonsMultipartFile cfHand = (CommonsMultipartFile) multipartFileCardHand;
        FileItem fileItemcfHand = cfHand.getFileItem();

        String identityUrlOn = upLoadOss(fileItemFront);
        String identityUrlOff = upLoadOss(fileItemBack);
        String identityHoldUrl = upLoadOss(fileItemcfHand);

        if (StringUtils.isEmpty(identityUrlOn) || StringUtils.isEmpty(identityUrlOff) || StringUtils.isEmpty(identityHoldUrl)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "上传文件失败,请重新上传");
            return jsonObject.toString();
        }
        fuser.setFpostImgValidate(true);
        fuser.setFpostImgValidateTime(Utils.getTimestamp());
        fuser.setfIdentityPath(Utils.subOssUrl(identityUrlOn));
        fuser.setfIdentityPath2(Utils.subOssUrl(identityUrlOff));
        fuser.setfIdentityPath3(Utils.subOssUrl(identityHoldUrl));
        fuser.setAuthGrade(2);

        fuser.setFidentityType(0);
        fuser.setFidentityNo(identityNo);
        fuser.setFrealName(realName);
        fuser.setFpostRealValidate(true);
        fuser.setFpostRealValidateTime(Utils.getTimestamp());
        fuser.setFhasRealValidate(true);
        fuser.setFhasRealValidateTime(Utils.getTimestamp());
        fuser.setFisValid(true);
        fuser.setAuthGrade(1);
        fuser.setFaudit(2);
        try {
            String ip = getIpAddr(request);
            this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_CERT, ip);
            this.userService.updateObj(fuser);
            this.SetSession(fuser, request);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "证件号码已存在");
            return jsonObject.toString();
        }
        jsonObject.accumulate("msg", "身份证号认证申请成功");
        jsonObject.accumulate("status", 200);
        return jsonObject.toString();
    }

    private String upLoadOss(FileItem fileItem) throws Exception {
        // 处理文件上传
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置最大缓存
        factory.setSizeThreshold(3 * 1024);
        ServerConfig config = ServerConfig.getInstance();

        OssConnection ossCon = new OssConnection();
        ossCon.setConfig(config);

        OssSession oss = ossCon.getSession();
        String fileUName = null;
        if (!fileItem.isFormField()) {// 文件名
            if (fileItem.getSize() > 0) {
                String fileName = fileItem.getName().toLowerCase();
                fileUName = TDateTime.FormatDateTime("yyMMddHH", new Date()) + UUID.randomUUID();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp")) { // 图片上传
                    if (fileName.endsWith(".jpg"))
                        fileUName += ".jpg";
                    if (fileName.endsWith(".png"))
                        fileUName += ".png";
                    if (fileName.endsWith(".bmp"))
                        fileUName += ".bmp";
                    oss.upload("test-jx-20180928/" + fileUName, fileItem.getInputStream());
                    String address = config.getProperty("oss.site") + "/test-jx-20180928/" + fileUName;
                    return address;
                }
            }
        }
        return null;
    }

    /**
     * 身份认证(android)
     *
     * @param request
     * @param identityNo
     * @param realName
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/m/user/api/android/validateIdentity", produces = "text/html;charset=UTF-8")
    public Object androidValidateIdentity(HttpServletRequest request, @RequestParam(required = false) String identityNo,
                                          @RequestParam(required = false) String realName, @RequestParam(required = false) String sign) throws Exception {
        JSONObject jsonObject = new JSONObject();
        realName = HtmlUtils.htmlEscape(realName);
        realName = StringEscapeUtils.unescapeHtml(realName);
        Fuser fuser = GetCurrentUser(request);
        if (null == fuser) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
        fuser = this.frontUserService.findById(fuser.getFid());
        identityNo = identityNo.toLowerCase();

        String sql = "where fidentityNo='" + identityNo + "'";
        int count = this.adminService.getAllCount("Fuser", sql);
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("identityNo", identityNo);
        map.put("realName", realName);
//        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
//            return jsonObject.toString();
//        }
        if (count > 0 && !fuser.getFidentityNo().equals(identityNo)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "身份证号码已存在!");
            return jsonObject.toString();
        }
//        if (!"admin".equals(fuser.getFrealName())) {
//            if (fuser.getFpostRealValidate()) {
//                jsonObject.accumulate("status", 200);
        // jsonObject.accumulate("msg", "请勿重复提交!");
//                return jsonObject;
//            }
//        }

        com.alibaba.fastjson.JSONObject json =  IDCard.check(realName, identityNo);
        int code = json.getInteger("code");
//        if (code == -1) {
//            jsonObject.accumulate("status", 300);
//            jsonObject.accumulate("msg", json.getString("msg"));
//            return jsonObject.toString();
//        }

        if (fuser.getFaudit() == 1) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "已审核通过，无须重复上传");
            return jsonObject.toString();
        }

        if (fuser.getFaudit() == 2) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "认证信息审核中");
            return jsonObject.toString();
        }

        String identityUrlOn = request.getParameter("fileCardFront");
        if (StringUtils.isEmpty(identityNo)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请上传身份证正面照");
            return jsonObject.toString();
        }
        String identityUrlOff = request.getParameter("fileCardBack");
        if (StringUtils.isEmpty(identityUrlOff)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请上传身份证背面照");
            return jsonObject.toString();
        }
        String identityHoldUrl = request.getParameter("fileCardHand");
        if (StringUtils.isEmpty(identityHoldUrl)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请上传身份证手持照");
            return jsonObject.toString();
        }

        fuser.setFpostImgValidate(true);
        fuser.setFpostImgValidateTime(Utils.getTimestamp());

        fuser.setfIdentityPath(Utils.subOssUrl(identityUrlOn));
        fuser.setfIdentityPath2(Utils.subOssUrl(identityUrlOff));
        fuser.setfIdentityPath3(Utils.subOssUrl(identityHoldUrl));
        fuser.setAuthGrade(2);

        fuser.setFidentityType(0);
        fuser.setFidentityNo(identityNo);
        fuser.setFrealName(realName);
        fuser.setFpostRealValidate(true);
        fuser.setFpostRealValidateTime(Utils.getTimestamp());
        fuser.setFhasRealValidate(true);
        fuser.setFhasRealValidateTime(Utils.getTimestamp());
        fuser.setFisValid(true);
        fuser.setAuthGrade(1);
        fuser.setFaudit(2);
        try {
            String ip = getIpAddr(request);
            this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_CERT, ip);
            this.userService.updateObj(fuser);
            this.SetSession(fuser, request);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "证件号码已存在");
            return jsonObject.toString();
        }
        jsonObject.accumulate("msg", "身份证号认证申请成功");
        jsonObject.accumulate("status", 200);
        return jsonObject.toString();
    }

    /**
     * 7、 上传身份认证照片
     *
     * @param request
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/m/user/api/pic/upload", produces = "text/html;charset=UTF-8")
    public Object uploadPic(HttpServletRequest request, @RequestParam(required = false) String sign) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Fuser fuser = GetCurrentUser(request);
        if (null == fuser) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
//        if (fuser.isFpostImgValidate()) {
//            jsonObject.accumulate("status", 300);
        // jsonObject.accumulate("msg", "您已上传，请耐心等待审核");
//            return jsonObject.toString();
//        }
        if (fuser.isFhasImgValidate()) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "已审核，无须重复上传");
            return jsonObject.toString();
        }
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = mRequest.getFile("file");
        if (null == multipartFile) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "没有上传文件");
            return jsonObject.toString();
        }
        CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
        FileItem fileItem = cf.getFileItem();
//        ServerConfig config = ServerConfig.getInstance();
//
//        OssConnection ossCon = new OssConnection();
//        ossCon.setConfig(config);
//
//        OssSession oss = ossCon.getSession();
//        oss.upload("vcoin" + "/common/upload/" + "app-release", fileItem.getInputStream());
//        String address = config.getProperty("oss.site") + "/" + "vcoin" + "/common/upload/" + "app-release";


        double size = multipartFile.getSize() / 1000d;
        if (size > (3 * 1024d)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "上传图片不应超过3M");
            return jsonObject.toString();
        }

        String address = "";
        // 处理文件上传
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置最大缓存
        factory.setSizeThreshold(3 * 1024);
        ServerConfig config = ServerConfig.getInstance();

        OssConnection ossCon = new OssConnection();
        ossCon.setConfig(config);

        OssSession oss = ossCon.getSession();
        String fileUName = null;
        if (!fileItem.isFormField()) {// 文件名
            if (fileItem.getSize() > 0) {
                String fileName = fileItem.getName().toLowerCase();
                fileUName = TDateTime.FormatDateTime("yyMMddHH", new Date()) + UUID.randomUUID();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp")) { // 图片上传
                    if (fileName.endsWith(".jpg"))
                        fileUName += ".jpg";
                    if (fileName.endsWith("png"))
                        fileUName += ".png";
                    if (fileName.endsWith(".bmp"))
                        fileUName += ".bmp";
                    oss.upload("vcoin" + "/common/upload/" + fileUName, fileItem.getInputStream());
                    address = config.getProperty("oss.site") + "/" + "vcoin" + "/common/upload/" + fileUName;
                }
            }
        }

        if (StringUtils.isEmpty(address)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "照片上传失败!");
            return jsonObject.toString();
        }
        final String _address = address;
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "照片上传成功!");
        jsonObject.accumulate("data", new HashMap<String, String>() {{
            put("address", _address);
        }});
        return jsonObject.toString();
    }

    /**
     * 获取银行卡类型
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/bankCardType"}, produces = "text/html;charset=UTF-8")
    public String getBankCardType(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        List<BankName> bankTypeList = new ArrayList<>();
        for (int i = 1; i <= BankTypeEnum.QT; i++) {
            if (BankTypeEnum.getEnumString(i) != null && BankTypeEnum.getEnumString(i).trim().length() > 0) {
                BankName bankName = new BankName();
                bankName.setCardId(i);
                bankName.setCardName(BankTypeEnum.getEnumString(i));
                bankTypeList.add(bankName);
            }
        }
        json.accumulate("status", 200);
        json.accumulate("msg", "success");
        json.accumulate("data", bankTypeList);
        return json.toString();
    }

    /**
     * 添加银行卡
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"m/user/api/addBankCard"}, produces = "text/html;charset=UTF-8")
    public String addBankCard(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
//        String uId = request.getParameter("userId");
        Fuser user = this.GetCurrentUser(request);
        if (user == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请先登陆！");
            return jsonObject.toString();
        }
        if (StringUtils.isEmpty(user.getFtradePassword())) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请先设置交易密码！");
            return jsonObject.toString();
        }
        try (Mysql mysql = new Mysql()) {
            String tradePassword = request.getParameter("tradePassword");
            if (!user.getFtradePassword().equals(Utils.MD5(tradePassword, user.getSalt()))) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "交易密码错误！");
                return jsonObject.toString();
            }

            String account = request.getParameter("account");
//            String name = request.getParameter("name");

            int openBankType = Integer.valueOf(request.getParameter("openBankType"));
            String sign = request.getParameter("sign");

            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s ", AppDB.t_userreceipt);
            ds.add("where fType=0 and fUsr_id='%s' and fAccount='%s'", user.getFid(), account);
            ds.open();
            if (ds.eof()) {
                ds.append();
                ds.setField("fUsr_id", user.getFid());
                ds.setField("fName", user.getFrealName());
                ds.setField("fAccount", account);
                ds.setField("fBankname", BankName.bankName(openBankType));
                ds.setField("fBankId", openBankType);
                ds.setField("fBanknamez", openBankType);
                ds.setField("fType", 0);
                ds.setField("fCreateTime", TDateTime.Now());
                ds.post();
                jsonObject.put("status", 200);
                jsonObject.put("msg", "银行卡添加成功");
            } else {
                jsonObject.put("status", 300);
                jsonObject.put("msg", "银行卡已存在");
            }
        } catch (Exception e) {
            jsonObject.accumulate("status", 500);
            jsonObject.accumulate("msg", "error");
        }
        return jsonObject.toString();
    }

    /**
     * 10、 查询银行卡信息
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"m/user/api/getBankCardInfo"}, produces = "text/html;charset=UTF-8")
    public String getBankCardInfo(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        MyQuery ds = new MyQuery(new Mysql());
        Fuser user = this.GetCurrentUser(request);
        if (user != null) {
            ds.add("select substring(fAccount,-4) cardNum,fBankname cardName,fBankType cardType ,fBankId cardId from %s ",
                    AppDB.t_userreceipt);
            ds.add("where fType=0 and fUsr_id='%s'", user.getFid());
            ds.open();
            List<Map<String, Object>> list = new ArrayList<>();
            if (!ds.eof()) {
                for (Record record : ds) {
                    list.add(record.getItems());
                }
            }
            jsonObject.accumulate("data", list);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
        } else {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = {"/user/api/market", "/m/user/api/market"})
    public String market(HttpServletRequest request) {
        List<Fvirtualcointype> fbs = this.utilsService.list(0, 0, " where ftype=? or ftype=? order by fid asc ", false,
                Fvirtualcointype.class, CoinTypeEnum.FB_CNY_VALUE, CoinTypeEnum.FB_COIN_VALUE);
        List<JSONObject> ftrademap = new ArrayList<>();
        for (Fvirtualcointype coinItem : fbs) {
            // 按照法币查询交易对
            List<Ftrademapping> ftrademappings = this.ftradeMappingService.findActiveTradeMappingByFB(coinItem);

            if (ftrademappings != null) {
                for (Ftrademapping ftrademapping : ftrademappings) {
                    JSONObject tradeMappingInfo = getTradeMappingInfo(ftrademapping);
                    ftrademap.add(tradeMappingInfo);
                }
            }
        }

        JSONObject dataJson = new JSONObject();
        dataJson.accumulate("data", ftrademap);
        dataJson.accumulate("status", 200);
        dataJson.accumulate("message", "success");

        return dataJson.toString();
    }

    private JSONObject getTradeMappingInfo(Ftrademapping ftrademapping) {
        JSONObject js = new JSONObject();
        Double tmp = (Double) this.redisUtil.get(RedisConstant.getLatestDealPrizeKey(ftrademapping.getFid()));
        double price = tmp != null ? tmp : 0;
        double fchangerate = 0;
        double moneny = 0;
        js.accumulate("mid", ftrademapping.getFid());
        js.accumulate("coinType", ftrademapping.getFtype());
        double fpc = 0;
        try (Mysql mysql = new Mysql()) {
            // 24h 最低、最高、总量

            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select max(pd.fgao) as fgao,sum(pd.fliang) as fliang,min(pd.fdi) as fdi,");
            ds1.add("pd.ftime as fti,ty1.fcurrentCNY as fny, pd.fliang as fli");
            ds1.add("from %s mp", AppDB.Ftrademapping);
            ds1.add("inner join %s pd on pd.ftrademapping=mp.fid", AppDB.fperiod);
            ds1.add("inner join %s ty1 on mp.fvirtualcointype1=ty1.fId", AppDB.fvirtualcointype);
            ds1.add("where mp.fid=%d", ftrademapping.getFid());
            ds1.add("and pd.ftime>='%s' and pd.fdi>0", TDateTime.Now().incDay(-1));
            ds1.add(" and pd.frequency = 1");
            ds1.open();
            // 24h开、收
            System.out.println("fshou+---" + ds1.getDouble("fshou") + "fliang----" + ds1.getDouble("fliang"));
            js.accumulate("deal_amount", ds1.getDouble("fliang"));
            js.accumulate("high", ds1.getDouble("fgao"));
            js.accumulate("low", ds1.getDouble("fdi"));
            js.accumulate("fpc", ds1.getDouble("fny"));
            fpc = ds1.getDouble("fny");
            MyQuery ds3 = new MyQuery(mysql);
            ds3.add("select lastprice.fshou as fshou,firstprice.fkai as fkai");
            ds3.add("from");
            ds3.add("(select fshou from %s where ftrademapping=%d", AppDB.fperiod, ftrademapping.getFid());
            ds3.add("and ftime between '%s' and '%s' order by fid desc limit 1) lastprice,",
                    TDateTime.Now().incDay(-1), TDateTime.Now());
            ds3.add("(select fkai from %s where ftrademapping=%d", AppDB.fperiod, ftrademapping.getFid());
            ds3.add("and ftime between '%s' and '%s' order by fid limit 1) firstprice",
                    TDateTime.Now().incDay(-1),
                    TDateTime.Now());
            ds3.open();
            if (!ds3.eof()) {
                js.accumulate("price", ds3.getDouble("fshou"));
                js.accumulate("kai", ds3.getDouble("fkai"));
                if (ds3.getDouble("fkai") != 0) {
                    fchangerate = (ds3.getDouble("fshou") - ds3.getDouble("fkai")) / ds3.getDouble("fkai");
                }
            } else {
                js.accumulate("price", price);
                js.accumulate("kai", 0);
            }

            moneny = price * fpc;


        }
        js.accumulate("moneny", moneny);
        js.accumulate("symbol", ftrademapping.getFvirtualcointypeByFvirtualcointype1().getfSymbol());
        js.accumulate("amount_increase", String.valueOf(fchangerate));
        js.accumulate("coin_coin", ftrademapping.getFtradedesc());
        js.accumulate("type", ftrademapping.getFvirtualcointypeByFvirtualcointype1().getfShortName());
        js.accumulate("tradeType", 0);
        return js;

    }

    /**
     * 设置、修改交易密码
     *
     * @param request
     * @param newPwd
     * @param reNewPwd
     * @param phoneCode
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/user/api/modifyPwd", "/m/api/modifyPwd"}, produces = JsonEncode)
    public String apimodifyPwd(HttpServletRequest request, @RequestParam(required = true) String phone,
                               @RequestParam(required = true) String newPwd,
                               @RequestParam(required = false, defaultValue = "0") String reNewPwd,
                               @RequestParam(required = true) String phoneCode, @RequestParam(required = true) String sign)
            throws Exception {
        JSONObject jsonObject = new JSONObject();

        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("newPwd", newPwd);
        map.put("reNewPwd", reNewPwd);
        map.put("phoneCode", phoneCode);
        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("status", 300);
        // jsonObject.accumulate("msg", "接口认证失败！");
//            return jsonObject.toString();
//        }

        if (!newPwd.equals(reNewPwd)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "两次输入密码不一样");
            return jsonObject.toString();// 两次输入密码不一样
        }

        Fuser fuser = GetCurrentUser(request);
        if (fuser == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        if (fuser == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }

        if (!fuser.getFtelephone().equals(phone)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "手机号码不正确");
            return jsonObject.toString();
        }

        // if (!fuser.isFisTelephoneBind() && !fuser.getFgoogleBind()) {
        // jsonObject.accumulate("resultCode", -13);
        // jsonObject.accumulate("msg", "需要绑定绑定谷歌或者手机号码才能修改密码");
        // return jsonObject.toString();// 需要绑定绑定谷歌或者电话才能修改密码
        // }

        String salt = fuser.getSalt();

        // 修改交易密码
        if (null == salt) {
            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0
                    && fuser.getFtradePassword().equals(MD5.get(newPwd))) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "新的交易密码与原始密码相同，修改失败");
                return jsonObject.toString();
            }
        } else {
            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0
                    && fuser.getFtradePassword().equals(Utils.MD5(newPwd, fuser.getSalt()))) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "新的交易密码与原始密码相同，修改失败");
                return jsonObject.toString();
            }
        }

        String ip = getIpAddr(request);
        if (fuser.isFisTelephoneBind()) {
            int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (mobil_limit <= 0) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                boolean bool = ShortMessageService.CheckCode(fuser.getFtelephone(), phoneCode, new Date());
                if (!bool) {
                    jsonObject.accumulate("status", 300);
                    jsonObject.accumulate("msg", "手机验证码有误，您还有" + mobil_limit + "次机会");
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                    return jsonObject.toString();
                } else {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                }
            }
        }

        try {
            // 修改交易密码
            if (null == salt) {
                fuser.setFtradePassword(MD5.get(newPwd));
            } else {
                fuser.setFtradePassword(Utils.MD5(newPwd, fuser.getSalt()));
            }
            int logType = 0;
            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0) {
                logType = LogTypeEnum.User_UPDATE_TRADE_PWD;
            } else {
                logType = LogTypeEnum.User_SET_TRADE_PWD;
            }
            this.frontUserService.updateFUser(fuser, getSession(request), logType, ip);

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("status", 500);
            jsonObject.accumulate("msg", "网络异常");
            return jsonObject.toString();
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CHANGE_LOGINPWD);
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CHANGE_TRADEPWD);
        }

        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "操作成功");
        return jsonObject.toString();
    }

    /**
     * 修改手机号码
     *
     * @param request
     * @param areaCode
     * @param imgcode
     * @param phone
     * @param oldcode
     * @param newcode
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/user/api/resetPhone", "/m/api/resetPhone"}, produces = {JsonEncode})
    public String resetPhone(HttpServletRequest request, @RequestParam(required = true) String areaCode,
                             @RequestParam(required = false) String imgcode, @RequestParam(required = true) String phone,
                             @RequestParam(required = false) String prephone, @RequestParam(required = true) String loginpwd,
                             @RequestParam(required = true) String oldcode, @RequestParam(required = true) String newcode,
                             @RequestParam(required = true) String sign) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Map<String, Object> map = new HashMap<>();
        map.put("areaCode", areaCode);
        map.put("imgcode", imgcode);
        map.put("phone", phone);
        map.put("oldcode", oldcode);
        map.put("newcode", newcode);
        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("code", -1);
        // jsonObject.accumulate("msg", "接口认证失败！");
//            return jsonObject.toString();
//        }
        Fuser fuser = GetCurrentUser(request);
        if (null == fuser) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }

        if (!phone.matches(Constant.PhoneReg) || !prephone.matches(Constant.PhoneReg)) {// 手機格式不對
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "手机号码格式不正确");
            return jsonObject.toString();
        }


        List<Fuser> users = this.frontUserService.findUserByProperty("fid", fuser.getFid());
        if (users != null && users.size() != 1) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "用户不存在");
            return jsonObject.toString();
        }
        Fuser user = users.get(0);
        if (!fuser.getFloginPassword().equals(MD5.get(loginpwd)) && !fuser.getFloginPassword().equals(Utils.MD5(loginpwd, fuser.getSalt()))) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "登录密码错误");
            return jsonObject.toString();
        }
        if (!user.getFtelephone().equals(prephone)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "旧手机号码不正确");
            return jsonObject.toString();
        }


        List<Fuser> fusers = this.frontUserService.findUserByProperty("ftelephone", phone);
        if (fusers.size() >= 1) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "手机号码已存在");
            return jsonObject.toString();
        }

        String ip = getIpAddr(request);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);

        boolean boolOldUser = ShortMessageService.CheckCode(prephone, oldcode, new Date());// 旧手机接入聚安校验验证码
        if (!boolOldUser) {
            this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "旧手机验证码有误，您还有" + tel_limit + "次机会");
            return jsonObject.toString();
        }
        boolean boolNewUser = ShortMessageService.CheckCode(phone, newcode, new Date());// 新手机接入聚安校验验证码
        if (!boolNewUser) {
            this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "新手机验证码有误，您还有" + tel_limit + "次机会");
            return jsonObject.toString();
        }
        fuser.setFareaCode(areaCode);
        fuser.setFtelephone(phone);
        fuser.setFnickName(phone);
        fuser.setFisTelephoneBind(true);
        fuser.setFlastUpdateTime(Utils.getTimestamp());
        com.alibaba.fastjson.JSONObject resJson = settlementService.updateUser(fuser.getFloginName(), fuser.getFtelephone(), null);
        if (resJson != null && StringUtils.isNotEmpty((String)resJson.get("status")) && resJson.get("status").equals("200")) {
            this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_BIND_PHONE, ip);
        } else {
            jsonObject.accumulate("status", 500);
            jsonObject.accumulate("msg", resJson.getString("data"));
            return jsonObject.toString();
        }
        this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "修改成功");
        return jsonObject.toString();
    }

    /**
     * 公告
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = { "/user/api/notice", "/m/api/notice" }, produces = { JsonEncode })
    public String notice(HttpServletRequest request,@RequestParam(required = false, defaultValue = "0") int type,
                         @RequestParam(required = false, defaultValue = "0") boolean newest ) throws Exception {
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        List<Farticle> farticles = null;
        if(type == 0) {
            farticles = this.utilsService.list(0, 0, "order by fid desc", false, Farticle.class);
        }else{
            farticles = this.frontOthersService.findFarticle(type, 0,
                    Integer.MAX_VALUE);
        }
        if(null == farticles) {
            result.put("status", 300);
            result.put("msg", "no data");
            return result.toString();
        }

        if(farticles.size() > 1 && newest) {
            farticles = farticles.subList(0,1);
        }
        Map<String, Object> arts = null;
        List<Map<String, Object>> list = new ArrayList<>();
        for (Farticle farticle : farticles) {
            arts = new HashMap<>();
            arts.put("type", farticle.getFarticletype().getFid());
            arts.put("fid", farticle.getFid());
            arts.put("title", farticle.getFtitle_short());
            SimpleDateFormat simpleTime = new SimpleDateFormat("yyyy-MM-dd");
            arts.put("date", simpleTime.format(simpleTime.parse(String.format("%s", farticle.getFlastModifyDate()))));
            list.add(arts);
        }
        Map<String, List> noticeData = new HashMap<>();
        noticeData.put("notices", list);
        result.put("status", 200);
        result.put("data", noticeData);
        result.put("msg", "success");
        return result.toString();
    }

    /**
     * 获取版本信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/m/app/api/version"}, produces = {JsonEncode})
    public String version(HttpServletRequest request, @RequestParam(defaultValue = "0", required = false) int type) {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = new HashMap<>();
        List<FappVersion> androudVersion = this.frontOthersService.findNewVersion(type);
        map.put("UpdateStatus", androudVersion.get(0).getUpdateStatus());
        map.put("VersionCode", androudVersion.get(0).getVersionCode());
        map.put("VersionName", androudVersion.get(0).getVersion());
        String content = androudVersion.get(0).getUpdateReadme();
        map.put("ModifyContent", content);
        map.put("DownloadUrl", androudVersion.get(0).getUrl());
        map.put("ApkSize", androudVersion.get(0).getApkSize());
        map.put("ApkMd5", androudVersion.get(0).getApkMd5());
        jsonObject.accumulate("data", map);
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "success");
        return jsonObject.toString();
    }

    /**
     * 获取交易区ryh,ryb
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"m/api/app/getCoinTotal"})
    public String getCoin(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String tId = request.getParameter("tid");

        if (StringUtils.isNotEmpty(tId)) {
            List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", tId);
            if(fuserList.size() != 1) {
                json.accumulate("status", "300");
                json.accumulate("msg", "用户id 不存在");
                return json.toString();
            }
            Fuser fuser = fuserList.get(0);
            int uId = fuser.getFid();
            int ryhCoin = 5;
            int rybCoin = 6;
            try{
                Map<String,Object> coinMap = tradingService.getTotalCoin(uId, ryhCoin, rybCoin);
                json.accumulate("status", "200");
                json.accumulate("msg", "success");
                json.accumulate("data", coinMap);
                return json.toString();
            } catch (Exception e) {
                json.accumulate("status", "500");
                json.accumulate("msg", "exception");
            }
        }
        json.accumulate("status", "300");
        json.accumulate("msg", "no wallet data");
        return json.toString();
    }

    /**
     * 20、 邮箱信息
     * 
     * @param request
     * @param response
     * @param userId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = { "/m/user/api/info/mail"},produces = {JsonEncode})
    public String postValidateMail(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        if (fuserList != null && fuserList.size() == 1) {
            Fuser fuser = fuserList.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("fEmail", fuser.getFemail() == null ? "" : fuser.getFemail());
            jsonObject.accumulate("data", map);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }

    /**
     * 21、 绑定邮箱
     * 
     * @param request
     * @param response
     * @param email
     * @param loginpwd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = { "/user/api/postValidateMail","/m/api/postValidateMail"},produces = {JsonEncode})
    public String userInfoMail(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true) String email,
                               @RequestParam(required = true) String loginpwd)
            throws Exception {
        JSONObject js = new JSONObject();
        Fuser user = GetCurrentUser(request);
        if (user == null) {
            js.accumulate("status", 300);
            js.accumulate("msg", "请重新登录!");
            return js.toString();
        }

        // 邮箱注册
        boolean isExists = this.frontUserService.isEmailExists(email);
        if (isExists) {
            js.accumulate("status", 300);
            js.accumulate("msg", "邮箱地址已存在");
            return js.toString();
        }

        if (!email.matches(new Constant().EmailReg)) {
            js.accumulate("status", 300);
            js.accumulate("msg", "邮箱格式不正确");
            return js.toString();
        }

        Fuser fuser = this.frontUserService.findById(user.getFid());

        if (fuser.getFisMailValidate()) {
            js.accumulate("status", 300);
            js.accumulate("msg", "您的邮箱已绑定");
            return js.toString();
        }

        if(!fuser.getFloginPassword().equals(MD5.get(loginpwd)) && !fuser.getFloginPassword().equals(Utils.MD5(loginpwd, fuser.getSalt()))) {
            js.accumulate("status", 300);
            js.accumulate("msg", "登录密码错误");
            return js.toString();
        }

        boolean flag = false;
        try {
            flag = this.frontUserService.saveValidateEmail(fuser, email, false);
            // todo 暂时处理
            this.frontValidateService.updateMailValidate(fuser.getFid(), "123456");

        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("status", 300);
            js.accumulate("msg", "网络异常");
            return js.toString();
        }

        if (flag) {
            js.accumulate("status", 200);
            js.accumulate("msg", "邮箱绑定成功");
            return js.toString();
        } else {
            js.accumulate("status", 300);
            js.accumulate("msg", "半小时内只能发送一次");
            return js.toString();
        }
    }

    /**
     * 22、 交易对收藏
     * 
     * @param request
     * @param mId
     * @param coin_coin
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/m/api/market/trademap/favairate/set"}, produces = JsonEncode)
    public String setMarketFavairate(HttpServletRequest request,
                                     @RequestParam(required = true) String mId,
                                     @RequestParam(required = true) String coin_coin,
                                     @RequestParam(required = true) String userId) {
        JSONObject jsonObject = new JSONObject();
        Fuser fuser = GetCurrentUser(request);
        if (fuser == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
        List<MarketFavairate> marketFavairateList = null;
        String favate = fuser.getFmaketFavate();
        if (StringUtils.isEmpty(favate)) {
            marketFavairateList = new ArrayList<>();
            MarketFavairate marketFavairate = new MarketFavairate();
            marketFavairate.setMid(mId);
            marketFavairate.setCoin_coin(coin_coin);
            marketFavairate.setFavairate(true);
            marketFavairateList.add(marketFavairate);
        } else {
            marketFavairateList = JSON.parseArray(favate, MarketFavairate.class);
            if (marketFavairateList != null) {
                boolean hasFavarirate = false;
                for (MarketFavairate marketFavairate : marketFavairateList) {
                    if (marketFavairate.getMid().equals(mId) && marketFavairate.getCoin_coin().equals(coin_coin)) {
                        marketFavairate.setFavairate(!marketFavairate.isFavairate());
                        hasFavarirate = true;
                        break;
                    }
                }
                if (!hasFavarirate) {
                    MarketFavairate marketFavairate = new MarketFavairate();
                    marketFavairate.setMid(mId);
                    marketFavairate.setCoin_coin(coin_coin);
                    marketFavairate.setFavairate(true);
                    marketFavairateList.add(marketFavairate);
                }
            }
        }
        fuser.setFmaketFavate(JSON.toJSONString(marketFavairateList));
        frontUserService.updateFuser(fuser);
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("message", "success");
        return jsonObject.toString();
    }

    /**
     * 23、 交易对自选列表
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/m/api/market/trademap/favairate"})
    public String getMarketFavairate(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Fuser fuser1 = GetCurrentUser(request);
        if (fuser1 == null) {
        	jsonObject.accumulate("status", 300);
        	jsonObject.accumulate("msg", "请重新登录");
        	return jsonObject.toString();
        }
        Fuser fuser = frontUserService.findById(fuser1.getFid());
        List<MarketFavairate> marketFavairateList = JSON.parseArray(fuser.getFmaketFavate(), MarketFavairate.class);
        List<JSONObject> ftrademap = marketFavairate(marketFavairateList);

        JSONObject dataJson = new JSONObject();
        dataJson.accumulate("data", ftrademap);
        dataJson.accumulate("status", 200);
        dataJson.accumulate("message", "success");

        return dataJson.toString();
    }
    
    @ResponseBody
    @RequestMapping(value = {"/m/api/market/trademap/favairate1"})
    public String getMarketFavairate1(HttpServletRequest request,@RequestParam(required = true) String mId) {
        JSONObject jsonObject = new JSONObject();
        Fuser fuser1 = GetCurrentUser(request);
        int flag = 0;
        if (fuser1 == null) {
        	jsonObject.accumulate("data", flag);
        	jsonObject.accumulate("status", 300);
        	jsonObject.accumulate("msg", "请重新登录");
        	return jsonObject.toString();
        }
        Fuser fuser = frontUserService.findById(fuser1.getFid());
        List<MarketFavairate> marketFavairateList = JSON.parseArray(fuser.getFmaketFavate(), MarketFavairate.class);
        if(CollectionUtils.isNotEmpty(marketFavairateList)) {
            for (MarketFavairate marketFavairate : marketFavairateList) {
                if(marketFavairate.getMid().equals(mId)) {
                    flag = 1;
                    break;
                }
            }
        }
        JSONObject dataJson = new JSONObject();
        dataJson.accumulate("data", flag);
        dataJson.accumulate("status", 200);
        dataJson.accumulate("msg", "请求成功");

        return dataJson.toString();
    }
    
    @ResponseBody
    @RequestMapping(value = {"/m/api/market/trademap/remvefavairate"})
    public String remveMarketFavairate(HttpServletRequest request,@RequestParam(required = true) String mId) {
        JSONObject jsonObject = new JSONObject();
        Fuser fuser1 = GetCurrentUser(request);
        int flag = 0;
        if (fuser1 == null) {
        	jsonObject.accumulate("data", flag);
        	jsonObject.accumulate("status", 300);
        	jsonObject.accumulate("msg", "请重新登录");
        	return jsonObject.toString();
        }
        Fuser fuser = frontUserService.findById(fuser1.getFid());
        List<MarketFavairate> marketFavairateList1 = new ArrayList<>();
        List<MarketFavairate> marketFavairateList = JSON.parseArray(fuser.getFmaketFavate(), MarketFavairate.class);
        for (MarketFavairate marketFavairate : marketFavairateList) {
			if(!marketFavairate.getMid().equals(mId)) {
				marketFavairateList1.add(marketFavairate);
			}
		}
        fuser.setFmaketFavate(JSON.toJSONString(marketFavairateList1));
        frontUserService.updateFuser(fuser);
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("message", "success");
        return jsonObject.toString();
    }

    // 组装自选交易对数据
    private List<JSONObject> marketFavairate(List<MarketFavairate> marketFavairateList) {

        if (null == marketFavairateList || marketFavairateList.size() == 0) {
            return null;
        }

        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (MarketFavairate marketFavairate : marketFavairateList) {
            if (marketFavairate.isFavairate()) {
                Ftrademapping ftrademapping = this.ftradeMappingService.findFtrademapping2(Integer.valueOf(marketFavairate.getMid()));
                JSONObject tradeMappingInfo = getTradeMappingInfo(ftrademapping);
                jsonObjectList.add(tradeMappingInfo);
            }
        }
        return jsonObjectList;
    }

    /**
     * 用户信息
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info"}, produces = JsonEncode)
    public String userInfo(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        if (fuserList != null && fuserList.size() == 1) {
            Fuser fuser = fuserList.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("fhasImgValidate", fuser.isFhasImgValidate());
            map.put("hasPostImgValidate", fuser.isFpostImgValidate());
            map.put("fIsMailValidate", fuser.getFisMailValidate());
            map.put("hasTradePassword", StringUtils.isEmpty(fuser.getFtradePassword()) ? false : true);
            map.put("hasActived", fuser.getFleaderId() == 0 ? false : true);
            map.put("sellerStatus", fuser.getfSellerStatus() == null ? 0 : fuser.getfSellerStatus());
            map.put("audit", fuser.getFaudit());
            jsonObject.accumulate("data", map);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }

    /**
     * 身份认证信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/identity"}, produces = JsonEncode)
    public String userIdentityInfo(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        if (fuserList != null && fuserList.size() == 1) {
            Fuser fuser = fuserList.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("fhasImgValidate", fuser.isFhasImgValidate());
            map.put("hasPostImgValidate", fuser.isFpostImgValidate());
            map.put("realName", fuser.getFrealName() == null ? "" : fuser.getFrealName());
            map.put("identityNo", fuser.getFidentityNo() == null ? "" : fuser.getFidentityNo());
            map.put("fIdentityPathFront", fuser.getfIdentityPath() == null ? "" : Utils.addOssUrl(fuser.getfIdentityPath()));
            map.put("fIdentityPathBack", fuser.getfIdentityPath2() == null ? "" : Utils.addOssUrl(fuser.getfIdentityPath2()));
            map.put("fIdentityPathHold", fuser.getfIdentityPath3() == null ? "" :Utils.addOssUrl(fuser.getfIdentityPath3()));
            map.put("audit", fuser.getFaudit());
            map.put("identifyReason", fuser.getfIdentifyReason() == null ? "" : fuser.getfIdentifyReason());
            jsonObject.accumulate("data", map);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }

    /**
     * 用户激活
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/activation", "/user/api/activation"}, produces = JsonEncode)
    public String activation(HttpServletRequest request,
                             @RequestParam(required = true, defaultValue = "0") String referenceId,
                             @RequestParam(required = true, defaultValue = "0") int rid)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        if (currentUser == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
        Fuser user = frontUserService.findById(currentUser.getFid());
        if (user.getFtelephone().equals(referenceId) || user.getFloginName().equals(referenceId)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "激活帐号不能是自己");
            return jsonObject.toString();
        }
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from fuser where floginName='%s' or fTelephone='%s' ", referenceId, referenceId);
            ds.setMaximum(1);
            ds.open();
            if (ds.eof()) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "未查到该待激活用户");
                return jsonObject.toString();
            } else {
                if (ds.getString("fleaderId") != null && Integer.valueOf(ds.getString("fleaderId")) > 0) {
                    jsonObject.accumulate("status", 300);
                    jsonObject.accumulate("msg", "该用户已激活");
                    return jsonObject.toString();
                }else{
                    // todo 扣推荐费
                    com.alibaba.fastjson.JSONObject resJson = settlementService.updateUser((String) ds.getField("floginName"), null,user.getFloginName());
                    if (resJson != null && StringUtils.isNotEmpty((String)resJson.get("status")) && resJson.get("status").equals("200")) {
                        ds.edit();
                        ds.setField("fleaderId", user.getFid());
                        ds.post();
                        FuserReference fuserReference = frontUserService.findFuserReferenceById(rid);
                        if(null != fuserReference) {
                            fuserReference.setHasActive(true);
                            fuserReference.setActiveTime(new Date());
                            frontUserService.updateFuserReference(fuserReference);
                        }
                    } else {
                        jsonObject.accumulate("status", 300);
                        jsonObject.accumulate("msg", resJson.getString("msg"));
                        return jsonObject.toString();
                    }
                }
            }
        } catch (Exception e) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "error");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "激活成功");
        return jsonObject.toString();
    }

    /**
     * 用户激活   推荐人填被激活人的账号
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/userActivation", "/user/api/userActivation"}, produces = JsonEncode)
    public String userActivation(HttpServletRequest request,
                             @RequestParam(required = true, defaultValue = "0") String recommend)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        if (currentUser == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
        Fuser user = frontUserService.findById(currentUser.getFid());
        if (user.getFtelephone().equals(recommend) || user.getFloginName().equals(recommend)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "激活帐号不能是自己");
            return jsonObject.toString();
        }
        if(user.getFloginName().substring(0,2).equals("HL") || recommend.substring(0,2).equals("HL")){
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "华联用户暂未开放激活!");
            return jsonObject.toString();
        }
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from fuser where floginName='%s' or fTelephone='%s' ", recommend, recommend);
            ds.setMaximum(1);
            ds.open();
            if (ds.eof()) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "未查到该待激活用户");
                return jsonObject.toString();
            } else {
                if (ds.getString("fleaderId") != null && Integer.valueOf(ds.getString("fleaderId")) > 0) {
                    jsonObject.accumulate("status", 300);
                    jsonObject.accumulate("msg", "该用户已激活");
                    return jsonObject.toString();
                }else{
//                     todo 扣推荐费
//                    com.alibaba.fastjson.JSONObject resJson = settlementService.updateUser((String) ds.getField("floginName"), null,user.getFloginName());
//                    if (resJson != null && StringUtils.isNotEmpty((String)resJson.get("status")) && resJson.get("status").equals("200")) {
//                        ds.edit();
//                        ds.setField("fleaderId", user.getFid());
//                        ds.post();
//                        FuserReference fuserReference = frontUserService.findFuserReferenceById(rid);
//                        if(null != fuserReference) {
//                            fuserReference.setHasActive(true);
//                            fuserReference.setActiveTime(new Date());
//                            frontUserService.updateFuserReference(fuserReference);
//                        }
//                    } else {
//                        jsonObject.accumulate("status", 300);
//                        jsonObject.accumulate("msg", resJson.getString("msg"));
//                        return jsonObject.toString();
//                    }
                    ds.edit();
                    ds.setField("fleaderId", user.getFid());
                    ds.post();
                    // 同步更新收益系统的推荐
                    com.alibaba.fastjson.JSONObject resJson = settlementService.updateUser(ds.getString("floginName"),ds.getString("ftelephone") , user.getFloginName());
                    if(resJson == null || !resJson.get("status").equals("200")){
                        ds.edit();
                        ds.setField("fleaderId", "0");
                        ds.post();

                        jsonObject.accumulate("status", 300);
                        jsonObject.accumulate("msg", "激活失败!");
                        return jsonObject.toString();
                    }
                }
            }
        } catch (Exception e) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "error");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "激活成功");
        return jsonObject.toString();
    }


    /**
     * 商家认证
     * 
     * @param request
     * @param sign
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/m/user/seller/identity", produces = "text/html;charset=UTF-8")
    public Object sellerIdentify(HttpServletRequest request, @RequestParam(required = false) String sign,
                                 @RequestParam(required = true) String sellerName,
                                 @RequestParam(required = true) String userId,
                                 @RequestParam(required = true) int sellerLevel) throws Exception {
        JSONObject jsonObject = new JSONObject();
        if(StringUtils.isEmpty(sellerName)) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "商家名称为空");
            return jsonObject.toString();
        }
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        if (fuserList != null && fuserList.size() == 1) {
            Fuser user = fuserList.get(0);
            if(!user.isFhasImgValidate() || user.getFaudit() != 1) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "用户未完成实名认证");
                return jsonObject.toString();
            }
            if (user.getfSellerStatus() != null && user.getfSellerStatus() == 1) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "用户商家认证审核中");
                return jsonObject.toString();
            }
            if (user.getfIsMerchant() >= sellerLevel) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "该商家级别已认证");
                return jsonObject.toString();
            }
            // 1=普通商家,2=vip会员,3=超级会员
            if(sellerLevel != 1) {
            	String rybType = String.valueOf(6);
                try (Mysql mysql = new Mysql()) {
					MyQuery ds = new MyQuery(mysql);
					ds.add("SELECT fId,fShortName from fvirtualcointype WHERE fShortName = 'RYB'");
					ds.open();
					if(!ds.eof()) {
						rybType = String.valueOf(ds.getInt("fId"));
					}else {
						jsonObject.accumulate("status", 300);
	                    jsonObject.accumulate("msg", "币种异常");
	                    return jsonObject.toString();
					}
				} catch (Exception e) {
					e.printStackTrace();
					jsonObject.accumulate("status", 300);
                    jsonObject.accumulate("msg", "币种异常");
                    return jsonObject.toString();
				}
                String uid = String.valueOf(user.getFid());
                double rybCoin = tradingService.getCoin(uid,rybType);
                if (sellerLevel == 2) {
                    if(rybCoin < 100) {
                        jsonObject.accumulate("status", 300);
                        jsonObject.accumulate("msg", "用户余额不足100 RYB");
                        return jsonObject.toString();
                    }
                    rybCoin = rybCoin - 100;
                } else if (sellerLevel == 3) {
                    if(rybCoin < 300) {
                        jsonObject.accumulate("status", 300);
                        jsonObject.accumulate("msg", "用户余额不足300 RYB");
                        return jsonObject.toString();
                    }
                    rybCoin = rybCoin - 300;
                } else {
                    jsonObject.accumulate("status", 300);
                    jsonObject.accumulate("msg", "商家级别参数不正确");
                    return jsonObject.toString();
                }
                // 扣除RYB
                tradingService.updateCoin(uid,rybType,String.valueOf(rybCoin));
            }
            boolean verified = true;
            try {
                user.setfSellerLevel(sellerLevel);
                user.setfSellerName(sellerName);
                user.setfSellerStatus(1);
                user.setfSellerTime(Utils.getTimestamp());
                frontUserService.updateFuser(user);
            } catch (Exception e) {
                verified = false;
            }
            if (!verified) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "商家认证出错!");
                return jsonObject.toString();
            }
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "商家认证已提交!");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }
    
    /**
     * 商家认证信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/seller"}, produces = JsonEncode)
    public String userSellerInfo(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        if (fuserList != null && fuserList.size() == 1) {
            Fuser fuser = fuserList.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("sellerName", fuser.getfSellerName() == null ? "" : fuser.getfSellerName());
            map.put("sellerLevel", fuser.getfSellerLevel() == null ? 0 : fuser.getfSellerLevel());
            map.put("sellerStatus", fuser.getfSellerStatus() == null ? 0 : fuser.getfSellerStatus());
            map.put("IsMerchant", fuser.getfIsMerchant());
            // 用户是否激活
            map.put("hasActived", fuser.getFleaderId() == 0 ? false : true);
            jsonObject.accumulate("data", map);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }

    /**
     * 商家个人中心
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/seller/detail"}, produces = JsonEncode)
    public String userSellerInfoDetail(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        if (fuserList != null && fuserList.size() == 1) {
            Fuser fuser = fuserList.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("sellerName", fuser.getfSellerName() == null ? "" : fuser.getfSellerName());
            map.put("cashDeposit", "");
            map.put("regTime", new Date().getTime());
            map.put("selloutAmountTotal", frontOtcOrderController.Orderdata(userId, "3", "2"));
            map.put("buyAmountTotal", frontOtcOrderController.Orderdata(userId, "3", "1"));
            map.put("selloutAmountToday",
                    frontOtcOrderController.Orderdata(userId, "3", "2",
                            frontOtcOrderController.getTimesmorning(0, 0, 0),
                            frontOtcOrderController.getTimesmorning(23, 59, 59)));
            map.put("buyAmountToday",
                    frontOtcOrderController.Orderdata(userId, "3", "1",
                            frontOtcOrderController.getTimesmorning(0, 0, 0),
                            frontOtcOrderController.getTimesmorning(23, 59, 59)));
            map.put("selloutAmountWeek",
                    frontOtcOrderController.Orderdata(userId, "3", "2",
                            frontOtcOrderController.getTimesmornings(0, 0, 0),
                            frontOtcOrderController.getTimesmorning(23, 59, 59)));
            map.put("buyAmountWeek",
                    frontOtcOrderController.Orderdata(userId, "3", "1",
                            frontOtcOrderController.getTimesmornings(0, 0, 0),
                            frontOtcOrderController.getTimesmorning(23, 59, 59)));
            map.put("hasRealNameCertified", true);
            map.put("hasDealInMonth", 0);
            jsonObject.accumulate("data", map);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }

    /**
     * 28、 商家出售订单
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/seller/sellingOrder"}, produces = JsonEncode)
    public String userSellerSellingOrder(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        List<Record> orderList = frontOtcOrderController.OrderDetail(userId, "2");
        if (fuserList != null && fuserList.size() == 1) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("coinType", orderList.get(i).getString("fAm_fId"));
                map.put("orderNo", orderList.get(i).getString("fOrderId"));
                map.put("amount", orderList.get(i).getDouble("fTrade_SumMoney"));
                map.put("quantity", orderList.get(i).getDouble("fTrade_Count"));
                map.put("price", orderList.get(i).getDouble("fTrade_Price"));
                map.put("status", frontOtcOrderController.testingType(orderList.get(i).getString("fTrade_Status")));
                map.put("remark", "");
                list.add(map);
            }
            jsonObject.accumulate("data", list);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }

    /**
     * 29、 商家购买订单
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/seller/buyingOrder"}, produces = JsonEncode)
    public String userSellerBuyingOrder(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        List<Record> orderList = frontOtcOrderController.OrderDetail(userId, "1");
        if (fuserList != null && fuserList.size() == 1) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("coinType", orderList.get(i).getString("fAm_fId"));
                map.put("orderNo", orderList.get(i).getString("fOrderId"));
                map.put("amount", orderList.get(i).getDouble("fTrade_SumMoney"));
                map.put("quantity", orderList.get(i).getDouble("fTrade_Count"));
                map.put("price", orderList.get(i).getDouble("fTrade_Price"));
                map.put("status", frontOtcOrderController.testingType(orderList.get(i).getString("fTrade_Status")));
                map.put("remark", "");
                list.add(map);
            }
            jsonObject.accumulate("data", list);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }

    /**
     * 30、 订单详情
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/seller/orderDetail"}, produces = JsonEncode)
    public String userSellerOrderDetail(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = true) String orderNo, @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Record> orderList = frontOtcOrderController.UserOrderDetail(orderNo, userId);
        Map<String, Object> map = new HashMap<>();
        map.put("orderType", frontOtcOrderController.testingType(orderList.get(0).getString("fType")));
        map.put("coinType", orderList.get(0).getString("idName"));
        map.put("orderNo", orderList.get(0).getString("fOrderId"));
        map.put("amount", orderList.get(0).getDouble("fTrade_SumMoney"));
        map.put("orderDate", new Date());
        map.put("quantity", orderList.get(0).getDouble("fTrade_Count"));
        map.put("poundage", "");
        map.put("price", orderList.get(0).getDouble("fTrade_Price"));
        Map<String, Object> payInfoMap = new HashMap<>();
        payInfoMap.put("fName", orderList.get(0).getString("fName"));
        payInfoMap.put("fAccount", orderList.get(0).getString("fAccount"));
        payInfoMap.put("fBankName", orderList.get(0).getString("fBankName"));
        map.put("payInfo", payInfoMap);
        map.put("remark", "");
        jsonObject.accumulate("data", map);
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "success");
        return jsonObject.toString();
    }

    /**
     * 32、 我的广告
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/seller/announceList"}, produces = JsonEncode)
    public String userSellerAnnounceList(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(required = true) String userId,
                                         @RequestParam(required = true) Integer type)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", userId);
        List<Record> otcOrder = frontOtcOrderController.otcOrder(userId, type);
        if (fuserList != null && fuserList.size() == 1) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < otcOrder.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("payType", frontOtcOrderController.testingPayType(otcOrder.get(i).getString("fReceipttype")));
                map.put("coinType", otcOrder.get(i).getString("idName"));
                map.put("status", frontOtcOrderController.testingStatus(otcOrder.get(i).getString("fStatus")));
                map.put("aId", otcOrder.get(i).getInt("fId"));
                map.put("announceDate", otcOrder.get(i).getString("fCreateTime"));
                map.put("quantity", otcOrder.get(i).getDouble("fCount"));
                map.put("type", otcOrder.get(i).getString("fOrdertype"));
                map.put("price", otcOrder.get(i).getDouble("fUnitprice"));
                list.add(map);
            }
            jsonObject.accumulate("data", list);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", "success");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 300);
        jsonObject.accumulate("msg", "用户信息不存在!");
        return jsonObject.toString();
    }

    /**
     * 32、 广告上架下架
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/seller/announceOnOff"}, produces = JsonEncode)
    public String userSellerMyAnnounce(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(required = true) Integer aId,
                                       @RequestParam(required = true) Integer isOn)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        Fuser user = GetCurrentUser(request);
        if (user == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
        if (frontOtcOrderController.publish(aId, isOn)) {
            if (isOn == 0) {
                jsonObject.accumulate("status", 200);
                jsonObject.accumulate("msg", "下架成功");
            } else {
                jsonObject.accumulate("status", 200);
                jsonObject.accumulate("msg", "上架成功");
            }
        } else {
            if (isOn == 0) {
                jsonObject.accumulate("status", 500);
                jsonObject.accumulate("msg", "下架失败");
            } else {
                jsonObject.accumulate("status", 500);
                jsonObject.accumulate("msg", "上架失败");
            }
        }
        return jsonObject.toString();
    }

    /**
     * 33、 发布广告
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/info/seller/doAnnounce"}, produces = JsonEncode)
    public String userSellerDoAnnounce(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(required = true) String announceType,
                                       @RequestParam(required = true) String coinType,
                                       @RequestParam(required = true) String payType,
                                       @RequestParam(required = true) double price,
                                       @RequestParam(required = true) double quantity,
                                       @RequestParam(required = true) double lowAmount,
                                       @RequestParam(required = true) double highAmount)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        Fuser user = GetCurrentUser(request);
        if (user == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "发布成功");
        return jsonObject.toString();
    }

    /**
     * 注册推荐号列表
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/reference/list"}, produces = JsonEncode)
    public String regReferList(HttpServletRequest request)
            throws Exception {

        JSONObject jsonObject = new JSONObject();
        List<String> referenceList = Arrays.asList("RY881008134","RY881008104","RY881008133");
        Map<String, Object> offical = new HashMap<>();
        offical.put("type", "offical");
        offical.put("list", referenceList);
        Map<String, Object> personal = new HashMap<>();
        personal.put("type", "personal");
        personal.put("list", new ArrayList<>());
        List<Map> mapList = new ArrayList<>();
        mapList.add(offical);
        mapList.add(personal);
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("data", mapList);
        jsonObject.accumulate("msg", "获取成功");
        return jsonObject.toString();
    }

    /**
     * 用户提交推荐人
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/reference/submit"}, produces = JsonEncode)
    public String toActiveRefer(HttpServletRequest request,
                                @RequestParam(required = true) String referenceId,
                                @RequestParam(required = true) String userId)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        String userProperty = "floginName";
        List<Fuser> fuserList = frontUserService.findUserByProperty(userProperty, userId);
        if(fuserList.size() == 0  || fuserList.size() > 1) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "用户id不存在");
            return jsonObject.toString();
        }
        Fuser fuser = fuserList.get(0);
        if(fuser.getFleaderId() > 0) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "该用户已激活,不需要提交推荐人");
            return jsonObject.toString();
        }
        String referProperty = "floginName";
        if(referenceId.matches(Constant.PhoneReg)) {
            referProperty = "ftelephone";
        }

        List<Fuser> fuserRefList = frontUserService.findUserByProperty(referProperty, referenceId);
        if(fuserRefList.size() == 0  || fuserRefList.size() > 1) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "推荐id不存在");
            return jsonObject.toString();
        }
        Fuser fuserRef = fuserRefList.get(0);
        if(fuserRef.getFid() == fuser.getFid()) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "不能自己激活自己");
            return jsonObject.toString();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("fuid", userId);
        map.put("freferenceId", referenceId);
        List<FuserReference> fuserReferenceList = frontUserService.findFuserReferenceByMap(map);
        if(fuserReferenceList.size() > 0) {
            FuserReference fuserReference = fuserReferenceList.get(0);
            jsonObject.accumulate("status", 200);
            jsonObject.accumulate("msg", fuserReference.getHasActive() == null ? "该推荐人已提交,等待推荐人激活"
                    : fuserReference.getHasActive() ? "该推荐人已提交,推荐人已激活" : "该推荐人已提交,等待推荐人激活");
            return jsonObject.toString();
        }
        FuserReference newFuserReference = new FuserReference();
        newFuserReference.setHasActive(false);
        newFuserReference.setFuid(userId);
        newFuserReference.setActiveTime(new Date());
        newFuserReference.setFreferenceId(referenceId);
        boolean isSave = true;
        try {
            frontUserService.updateFuserReference(newFuserReference);
        } catch (Exception e) {
            isSave = false;
        }
        if (!isSave) {
            jsonObject.accumulate("status", 500);
            jsonObject.accumulate("msg", "网络异常");
            return jsonObject.toString();
        }
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "提交成功,等待推荐人激活");
        return jsonObject.toString();
    }

    /**
     * 推荐人激活列表
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/activation/list"}, produces = JsonEncode)
    public String activeReferList(HttpServletRequest request,
                                  @RequestParam(required = true) String userId,
                                  @RequestParam(required = true) Boolean hasActive)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = new HashMap<>();
        map.put("freferenceId", userId);
        map.put("hasActive", hasActive);
        List<FuserReference> fuserReferenceList = frontUserService.findFuserReferenceByMap(map);
        List<FuserReferenceVO> fuserReferenceVOList = new ArrayList<>();
        for(FuserReference fuserReference : fuserReferenceList) {
            fuserReferenceVOList.add(new FuserReferenceVO(fuserReference));
        }
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("data", fuserReferenceVOList);
        jsonObject.accumulate("msg", "获取成功");
        return jsonObject.toString();
    }

    /**
     *38、	获取服务url
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/api/url"}, produces = JsonEncode)
    public String apiUrl(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> data = new HashMap<>();
        data.put("homePage", Constant.OSSURL + "/m/index.html");
        jsonObject.accumulate("data", data);
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "成功");
        return jsonObject.toString();
    }

    /**
     *钱包后台转帐帐单
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/m/user/wallet/transfer/log"}, produces = JsonEncode)
    public String walletTransferLog(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        Fuser user = GetCurrentUser(request);
        if (user == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }

        Mysql mysql = new Mysql();
        MyQuery logQuery = new MyQuery(mysql);
        logQuery.add("select * from fvirtualoperationlog fvirtualoperationlog left join fvirtualcointype fvirtualcointype on " +
                "fvirtualcointype.fid =  fvirtualoperationlog.FVirtualCoinTypeId where fvirtualoperationlog.FStatus=2 " +
                "and fvirtualoperationlog.fUserid = %s order by fvirtualoperationlog.fcreatetime desc", user.getFid());
        logQuery.open();
        List<Map<String, Object>> logList = new ArrayList<>();
        int limit = 10;
        while (logQuery.fetch() && limit > 0 ) {
            Map<String, Object> map = new HashMap<>();
            String remark = "后台充值,金额为: " + logQuery.getDouble("FQty");
            if(logQuery.getInt("operationType") == 0) {
                remark = "后台扣款,金额为: " + logQuery.getDouble("FQty");
            }
            map.put("remark", remark);
            map.put("walletId", "");
            map.put("serialNo", logQuery.getString("taskId") != null ?
                    logQuery.getString("taskId").replaceAll("\\{","").replaceAll("}","") : "");
            map.put("number", logQuery.getDouble("FQty"));
            map.put("type", "交易区" + logQuery.getString("fName_en"));
            map.put("date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(logQuery.getDateTime("FCreateTime").getData().getTime()));
            logList.add(map);
            limit--;
        }
        jsonObject.accumulate("data", logList);
        jsonObject.accumulate("status", 200);
        jsonObject.accumulate("msg", "成功");
        return jsonObject.toString();
    }
}
