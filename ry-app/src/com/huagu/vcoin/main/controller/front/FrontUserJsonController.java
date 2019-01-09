package com.huagu.vcoin.main.controller.front;

import cn.cerc.jdb.core.DataQuery;
import cn.cerc.jdb.core.ServerConfig;
import cn.cerc.jdb.core.TDate;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.oss.OssConnection;
import cn.cerc.jdb.oss.OssSession;
import cn.cerc.jdb.other.utils;
import cn.cerc.security.sapi.JayunMessage;
import cn.cerc.security.sapi.JayunSecurity;
import cn.cerc.security.sapi.JayunVerify;
import cn.cerc.security.sapi.SendMode;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.*;
import com.huagu.vcoin.main.auto.SendSMSCF;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.comm.ValidateMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.front.data.UFile;
import com.huagu.vcoin.main.controller.front.data.UploadFile;
import com.huagu.vcoin.main.model.*;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.SystemArgsService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.comm.redis.RedisConstant;
import com.huagu.vcoin.main.service.comm.redis.RedisUtil;
import com.huagu.vcoin.main.service.front.*;
import com.huagu.vcoin.main.service.settlement.SettlementService;
import com.huagu.vcoin.main.sms.ShortMessageService;
import com.huagu.vcoin.util.*;
import com.zhongyinghui.security.utils.HttpClientUtils;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.INTERNAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FrontUserJsonController extends BaseController {

    public static String securtyKey = "123";
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private SettlementService settlementService;
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

    private  Logger logger = LoggerFactory.getLogger(getClass());


    /*
     * 邮箱或者和手机号码是否重复是否重复
     * 
     * @param type:0手机，1邮箱
     * 
     * @Return 0重复，1正常
     */
    @ResponseBody
    @RequestMapping(value = "/user/reg/chcekregname")
    public String chcekregname(@RequestParam(required = false, defaultValue = "0") String name,
            @RequestParam(required = false, defaultValue = "1") int type,
            @RequestParam(required = false, defaultValue = "0") int random) throws Exception {
        JSONObject jsonObject = new JSONObject();

        if (type == 0) {
            // 手机账号
            boolean flag = this.frontUserService.isLoginNameExists(name);// 修改为注册时检测loginName是否存在
            if (flag) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "手机号码错误或已存在");
            } else {
                jsonObject.accumulate("code", 0);
            }
        } else {
            // 邮箱账号
            boolean flag = this.frontUserService.isEmailExists(name);
            if (flag) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "邮箱错误或已存在");
            } else {
                jsonObject.accumulate("code", 0);
            }
        }

        return jsonObject.toString();

    }

    /*
     * @Param regType:0手机，1email
     * 
     * @Return 1正常，-2名字重复，-4邮箱格式不对，-5客户端你没打开cookie
     */
    @ResponseBody
    @RequestMapping(value = { "/m/user/reg/index", "/user/reg/index" }, produces = JsonEncode)
    public String regIndex(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int random,
            @RequestParam(required = false, defaultValue = "0") String password,
            @RequestParam(required = false, defaultValue = "0") String regName,
            @RequestParam(required = false, defaultValue = "0") int regType, // 0手机、1邮箱
            @RequestParam(required = false, defaultValue = "0") String vcode,
            @RequestParam(required = false, defaultValue = "0") String ecode,
            @RequestParam(required = true, defaultValue = "0") String phoneCode,
             @RequestParam(required = false, defaultValue = "86") String areaCode,
            String recommend) throws Exception {

        JSONObject jsonObject = new JSONObject();

        String phone = HtmlUtils.htmlEscape(regName);
        phoneCode = HtmlUtils.htmlEscape(phoneCode);
        String isOpenReg = constantMap.get("isOpenReg").toString().trim();
        if (!isOpenReg.equals("1")) {
            jsonObject.accumulate("code", -888);
            jsonObject.accumulate("msg", "暂停注册");
            return jsonObject.toString();
        }
        if (!request.getRequestURI().startsWith("/m")) {
            String checkCode = (String) request.getSession().getAttribute("checkcode");
            if (checkCode == null || "".equals(checkCode)) {
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", "图片验证码已经失效，请点击刷新验证码");
                return jsonObject.toString();
            }

            if (!checkCode.equals(vcode)) {
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", "图片验证码错误");
                return jsonObject.toString();
            }
        }

        password = HtmlUtils.htmlEscape(password.trim());
        if (password == null || password.length() < 6) {
            jsonObject.accumulate("code", -11);
            jsonObject.accumulate("msg", "登录短信长度有误");
            return jsonObject.toString();
        }
        // 邮箱Che
        if (regType == 0) {
            // 手机注册

            boolean flag1 = this.frontUserService.isLoginNameExists(regName);// 注册检测loginName是否存在
            if (flag1) {
                jsonObject.accumulate("code", -22);
                jsonObject.accumulate("msg", "手机号码已经被注册");
                return jsonObject.toString();
            }
            if (!"1".equals(Constant.AppLevel)) {
                if (!phone.matches(Constant.PhoneReg)) {
                    jsonObject.accumulate("code", -22);
                    jsonObject.accumulate("msg", "手机格式错误");
                    return jsonObject.toString();
                }
            }
            // (((((   bool = ShortMessageService.CheckCode(phone, phoneCode, new Date());
//            JayunMessage message = new JayunMessage(request);
            boolean bool = false;
            if(ShortMessageService.isTrue()){
                if ("86".equals(areaCode)) {
                    bool = ShortMessageService.CheckCode(phone, phoneCode, new Date());
                }else{
                    bool = ShortMessageService.CheckCode("+"+areaCode + phone, phoneCode, new Date());
                }
            }

            if (!bool) {
                jsonObject.accumulate("code", -20);
                jsonObject.accumulate("msg", "短信验证码错误");
                return jsonObject.toString();
            }


        } else {
            // 邮箱注册
            boolean flag = this.frontUserService.isEmailExists(regName);
            if (flag) {
                jsonObject.accumulate("code", -12);
                jsonObject.accumulate("msg", "邮箱已经存在");
                return jsonObject.toString();
            }

            boolean mailValidate = validateMailCode(getIpAddr(request), phone, SendMailTypeEnum.RegMail, ecode);
            if (!mailValidate) {
                jsonObject.accumulate("code", -10);
                jsonObject.accumulate("msg", "邮箱验证码错误");
                return jsonObject.toString();
            }

            if (!regName.matches(new Constant().EmailReg)) {
                jsonObject.accumulate("code", -12);
                jsonObject.accumulate("msg", "邮箱格式错误");
                return jsonObject.toString();
            }

        }

        // 推广
        Fuser intro = null;
        String intro_user = request.getParameter("intro_user");

        try {
            if (intro_user != null && !"".equals(intro_user.trim())) {
                intro = this.frontUserService.findById(Integer.parseInt(intro_user.trim()));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (intro == null) {
            String isMustIntrol = constantMap.get("isMustIntrol").toString().trim();
            if (isMustIntrol.equals("1")) {
                jsonObject.accumulate("code", -200);
                jsonObject.accumulate("msg", "请填写正确的邀请码");
                return jsonObject.toString();
            }
        }

        Fuser fuser = new Fuser();
        if (intro != null) {
            fuser.setfIntroUser_id(intro);
        }

        // 推广中心号
        try {
            String service_no = HtmlUtils.htmlEscape(request.getParameter("service_no").trim()).replace("'", "");
            if (service_no != null && service_no.trim().length() > 0 && service_no.trim().length() < 100) {
                String filter = "where fuserNo='" + service_no.trim() + "'";
                int c = this.adminService.getAllCount("Fuser", filter);
                if (c > 0) {
                    fuser.setFintrolUserNo(service_no.trim());
                }
            } else {
                fuser.setFintrolUserNo(null);
            }
        } catch (Exception e1) {
        }

        if (fuser.getFintrolUserNo() == null || fuser.getFintrolUserNo().trim().length() == 0) {
            if (intro != null && intro.getFintrolUserNo() != null && intro.getFintrolUserNo().trim().length() > 0) {
                fuser.setFintrolUserNo(intro.getFintrolUserNo());
            } else if (intro != null && intro.getFuserNo() != null && intro.getFuserNo().trim().length() > 0) {
                fuser.setFintrolUserNo(intro.getFuserNo());
            }
        }

        if (regType == 0) {
            // 手机注册 hqs
            fuser.setFregType(RegTypeEnum.TEL_VALUE);
            fuser.setFtelephone(phone);
            fuser.setFareaCode(areaCode);
            fuser.setFisTelephoneBind(true);

            fuser.setFnickName(phone);
            fuser.setFloginName(phone);
        } else {
            fuser.setFregType(RegTypeEnum.EMAIL_VALUE);
            fuser.setFemail(regName);
            fuser.setFisMailValidate(true);
            fuser.setFnickName(regName.split("@")[0]);
            fuser.setFloginName(regName);
        }

        fuser.setSalt(Utils.getUUID());
        fuser.setFregisterTime(Utils.getTimestamp());
        String aa = "1";
        /* fuser.setFloginPassword(Utils.MD5(aa, fuser.getSalt())); */
        fuser.setFloginPassword(MyMD5Utils.encoding(password));
        fuser.setFtradePassword(null);
        String ip = getIpAddr(request);
        fuser.setFregIp(ip);
        fuser.setFlastLoginIp(ip);
        fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
        fuser.setFlastLoginTime(Utils.getTimestamp());
        fuser.setFlastUpdateTime(Utils.getTimestamp());

        // 如果用户注册时输入了推荐人账号,则不用激活,是否要调用激活的接口?
        if(recommend != null && !"".equals(recommend)){
            // 1为已激活状态
            List<Fuser> fu = frontUserService.findUserByProperty("ftelephone",recommend);
            if(fu != null && fu.size() == 1){

            }else{
                fu = this.frontUserService.findUserByProperty("floginName", recommend);
                if(fu == null || fu.size() != 1){
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "推荐人不存在，请核对后再试!");
                    return jsonObject.toString();
                }
            }
            if(fu.get(0).getFloginName().substring(0,2).equals("HL")){
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "华联用户暂无此权限!");
                return jsonObject.toString();
            }
            if(fu.get(0).getFleaderId() != 0){
                fuser.setFleaderId(fu.get(0).getFid());
            }else{
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "推荐人未激活,请推荐人激活后再注册!");
                return jsonObject.toString();
            }
        }

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
                saveFlag1 = settlementService.register(loginName, recommend == null || "".equals(recommend) ? "RY001" : recommend, phone);
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
            if (regType == 0) {
                String key1 = ip + "message_" + MessageTypeEnum.REG_CODE;
                String key2 = ip + "_" + phone + "_" + MessageTypeEnum.REG_CODE;
                this.validateMap.removeMessageMap(key1);
                this.validateMap.removeMessageMap(key2);
            } else {
                String key1 = ip + "mail_" + SendMailTypeEnum.RegMail;
                String key2 = ip + "_" + phone + "_" + SendMailTypeEnum.RegMail;
                this.validateMap.removeMailMap(key1);
                this.validateMap.removeMailMap(key2);
            }
        }

        if (saveFlag) {

            this.SetSession(fuser, request);
            new JayunSecurity(request).register(phone, phone); // 注册聚安

            if (intro != null) {
                // 奖励糖果
                String[] auditSendCoin = this.systemArgsService.getValue("auditSendCoin").split("#");
                String taskId = utils.newGuid();
                int coinID = Integer.parseInt(auditSendCoin[0]);
                double coinQty = Double.valueOf(auditSendCoin[1]);
                Fintrolinfo introlInfo = null;
                if (coinQty > 0) {
                    Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                            coinID);
                    Fscore fscore = fuser.getFscore();
                    fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + coinQty);
                    introlInfo = new Fintrolinfo();
                    introlInfo.setFcreatetime(Utils.getTimestamp());
                    introlInfo.setFiscny(false);
                    introlInfo.setFqty(coinQty);
                    introlInfo.setFuser(fuser);
                    introlInfo.setTaskId(taskId);
                    introlInfo.setFname(fvirtualwallet.getFvirtualcointype().getFname());
                    introlInfo.setFtitle("注册成功！奖励" + coinQty + fvirtualwallet.getFvirtualcointype().getFname());
                    this.userService.updateUser1(fvirtualwallet, fscore, introlInfo);
                    try (Mysql handle = new Mysql()) {
                        MyQuery ds = new MyQuery(handle);
                        ds.add("select UID_,userId_,coinId_,total_,frozen_,totalChange_,frozenChange_,changeReason_,changeDate_,guid_,taskId_,entrustId_ from %s",
                                AppDB.t_walletlog);
                        ds.open();
                        ds.append();
                        ds.setField("userId_", fuser.getFid());
                        ds.setField("coinId_", fvirtualwallet.getFvirtualcointype().getFid());
                        ds.setField("total_", fvirtualwallet.getFtotal());
                        ds.setField("frozen_", fvirtualwallet.getFfrozen());
                        ds.setField("totalChange_", coinQty);
                        ds.setField("frozenChange_", 0);
                        ds.setField("changeReason_", "账户注册奖励!");
                        ds.setField("changeDate_", TDateTime.Now());
                        ds.setField("entrustId_", "0");
                        ds.setField("taskId_", taskId);
                        ds.post();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(coinID);
                jsonObject.accumulate("msg", "注册成功！奖励" + coinQty + fvirtualcointype.getFname());
                jsonObject.accumulate("code", 0);
                return jsonObject.toString();
            }
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "注册成功");
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", -10);
            jsonObject.accumulate("msg", "网络错误，请稍后再试");
            return jsonObject.toString();
        }
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/m/user/api/regx", "/user/api/regx" }, produces = JsonEncode)
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
        if (!sort.equals(sign)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "接口认证失败！");
            return jsonObject.toString();
        }
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
        if (!ShortMessageService.isTrue()) {
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
        if (!bool) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "短信验证码错误");
            return jsonObject.toString();
        }
        /*
         * if (!phoneCode.equals("000000")) { if (!bool) { jsonObject.accumulate("code",
         * -1); jsonObject.accumulate("msg", "短信验证码错误"); return jsonObject.toString(); }
         * }
         */

        // 推广
        Fuser intro = null;

        try {
            if (intro_user != null && !"".equals(intro_user.trim())) {
                intro = this.frontUserService.findById(Integer.parseInt(intro_user.trim()));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (intro == null) {
            String isMustIntrol = constantMap.get("isMustIntrol").toString().trim();
            if (isMustIntrol.equals("1")) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "请填写正确的邀请码");
                return jsonObject.toString();
            }
        }

        Fuser fuser = new Fuser();
        if (intro != null) {
            fuser.setfIntroUser_id(intro);
        }

        // 推广中心号
        try {
            String service_no = HtmlUtils.htmlEscape(request.getParameter("service_no").trim()).replace("'", "");
            if (service_no != null && service_no.trim().length() > 0 && service_no.trim().length() < 100) {
                String filter = "where fuserNo='" + service_no.trim() + "'";
                int c = this.adminService.getAllCount("Fuser", filter);
                if (c > 0) {
                    fuser.setFintrolUserNo(service_no.trim());
                }
            } else {
                fuser.setFintrolUserNo(null);
            }
        } catch (Exception e1) {
        }

        if (fuser.getFintrolUserNo() == null || fuser.getFintrolUserNo().trim().length() == 0) {
            if (intro != null && intro.getFintrolUserNo() != null && intro.getFintrolUserNo().trim().length() > 0) {
                fuser.setFintrolUserNo(intro.getFintrolUserNo());
            } else if (intro != null && intro.getFuserNo() != null && intro.getFuserNo().trim().length() > 0) {
                fuser.setFintrolUserNo(intro.getFuserNo());
            }
        }

        // 手机注册
        fuser.setFregType(RegTypeEnum.TEL_VALUE);
        fuser.setFtelephone(phone);
        fuser.setFareaCode(areaCode);
        fuser.setFisTelephoneBind(true);

        fuser.setFnickName(phone);
        fuser.setFloginName(phone);

        fuser.setSalt(Utils.getUUID());
        fuser.setFregisterTime(Utils.getTimestamp());
        String aa = "1";
        /* fuser.setFloginPassword(Utils.MD5(aa, fuser.getSalt())); */
        fuser.setFloginPassword(Utils.MD5(password, fuser.getSalt()));
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
                ds.add("SELECT fId,floginName from fuser WHERE fTelephone = '%s' ", phone);
                ds.open();
                loginName = "RY" + ds.getInt("fId");
                ds.edit();
                ds.setField("floginName", loginName);
                ds.post();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
            /*
             * if (intro_user == "") { saveFlag1 = userRegister1(phone, password, "",
             * System.currentTimeMillis() / 1000, phone); } else { saveFlag1 =
             * userRegister1(phone, password, intro.getFtelephone(),
             * System.currentTimeMillis() / 1000, phone); }
             */
            saveFlag1 = userRegister1(loginName, password, "RY002", System.currentTimeMillis() / 1000, phone);
            /*
             * if (!intro_user.equals("")) { saveFlag1 = userRegister1(loginName, password,
             * "RY002", System.currentTimeMillis() / 1000, phone); }
             */

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            String key1 = ip + "message_" + MessageTypeEnum.REG_CODE;
            String key2 = ip + "_" + phone + "_" + MessageTypeEnum.REG_CODE;
            this.validateMap.removeMessageMap(key1);
            this.validateMap.removeMessageMap(key2);
        }

        if (saveFlag) {

            this.SetSession(fuser, request);
            new JayunSecurity(request).register(phone, phone); // 注册聚安

            if (intro != null) {
                // 奖励糖果
                String[] auditSendCoin = this.systemArgsService.getValue("auditSendCoin").split("#");
                String taskId = utils.newGuid();
                int coinID = Integer.parseInt(auditSendCoin[0]);
                double coinQty = Double.valueOf(auditSendCoin[1]);
                Fintrolinfo introlInfo = null;
                if (coinQty > 0) {
                    Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
                            coinID);
                    Fscore fscore = fuser.getFscore();
                    fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + coinQty);
                    introlInfo = new Fintrolinfo();
                    introlInfo.setFcreatetime(Utils.getTimestamp());
                    introlInfo.setFiscny(false);
                    introlInfo.setFqty(coinQty);
                    introlInfo.setFuser(fuser);
                    introlInfo.setTaskId(taskId);
                    introlInfo.setFname(fvirtualwallet.getFvirtualcointype().getFname());
                    introlInfo.setFtitle("注册成功！奖励" + coinQty + fvirtualwallet.getFvirtualcointype().getFname());
                    this.userService.updateUser1(fvirtualwallet, fscore, introlInfo);
                    try (Mysql handle = new Mysql()) {
                        MyQuery ds = new MyQuery(handle);
                        ds.add("select UID_,userId_,coinId_,total_,frozen_,totalChange_,frozenChange_,changeReason_,changeDate_,guid_,taskId_,entrustId_ from %s",
                                AppDB.t_walletlog);
                        ds.open();
                        ds.append();
                        ds.setField("userId_", fuser.getFid());
                        ds.setField("coinId_", fvirtualwallet.getFvirtualcointype().getFid());
                        ds.setField("total_", fvirtualwallet.getFtotal());
                        ds.setField("frozen_", fvirtualwallet.getFfrozen());
                        ds.setField("totalChange_", coinQty);
                        ds.setField("frozenChange_", 0);
                        ds.setField("changeReason_", "账户注册奖励!");
                        ds.setField("changeDate_", TDateTime.Now());
                        ds.setField("entrustId_", "0");
                        ds.setField("taskId_", taskId);
                        ds.post();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(coinID);
                jsonObject.accumulate("msg", "注册成功！奖励" + coinQty + fvirtualcointype.getFname());
                jsonObject.accumulate("code", 0);
                return jsonObject.toString();
            }
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "注册成功");
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络错误，请稍后再试");
            return jsonObject.toString();
        }
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/m/user/api/activationx", "/user/api/activationx" }, produces = JsonEncode)
    public String activation(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") String userPhone,
            @RequestParam(required = false, defaultValue = "0") String userNmae)
            throws Exception {

        JSONObject jsonObject = new JSONObject();
        Fuser user = GetCurrentUser(request);
        if (user == null) {
            jsonObject.accumulate("status", 300);
            jsonObject.accumulate("msg", "请重新登录");
            return jsonObject.toString();
        }
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from fuser where floginName='%s' or fTelephone='%s' ", userNmae, userPhone);
            ds.setMaximum(1);
            ds.open();
            if (ds.eof()) {
                jsonObject.accumulate("status", 300);
                jsonObject.accumulate("msg", "未查到该上级用户");
                return jsonObject.toString();
            } else {
                MyQuery dsEdit = new MyQuery(mysql);
                dsEdit.add("select * from fuser where fId=%s", user.getFid());
                dsEdit.setMaximum(1);
                dsEdit.open();
                dsEdit.edit();
                dsEdit.setField("fIntroUser_id", ds.getField("fId"));
                dsEdit.post();
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

    /*
     * http://localhost:8090/user/login/index.html?random=78 loginName=asdjf@adf.cn
     * longLogin=0 password=adsfasdf
     */

    @ResponseBody
    @RequestMapping(value = { "/m/user/login/index", "/user/login/index" }, produces = JsonEncode)
    public String loginIndex(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false, defaultValue = "0") int random,
            @RequestParam(required = false) String imgCode, @RequestParam(required = true) String loginName,
            @RequestParam(required = true) String password, @RequestParam(required = true) String vCode)
            throws Exception {
        JSONObject jsonObject = new JSONObject();

        int longLogin = -1;// 0是手机，1是邮箱
        String keyString = "floginName";
        String keyString1 = "ftelephone";

        /*if (longLogin == -1) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "用户名错误");
            return jsonObject.toString();
        }*/

        List<Fuser> fusers = new ArrayList<>();
        if (loginName.matches(Constant.PhoneReg) == true) {
            fusers = this.frontUserService.findUserByProperty(keyString1, loginName);
        } else {
            fusers = this.frontUserService.findUserByProperty(keyString, loginName);
        }
        
        if (fusers == null || fusers.size() != 1) {
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", "用户名不存在");
            return jsonObject.toString();
        }
        loginName = fusers.get(0).getFloginName();
       /* boolean bool = ShortMessageService.CheckCode(fusers.get(0).getFtelephone(), vCode, new Date());
        if (!bool) {
        	jsonObject.accumulate("code", -1);
        	jsonObject.accumulate("msg", "手机验证码错误！");
        	return jsonObject.toString();
        }*/
        request.setAttribute("user", loginName);
        request.setAttribute("mobile", fusers.get(0).getFtelephone());

        // SAPISMS sms = new SAPISMS(request);
        // System.out.println(sms.sendToUser(loginName, "000001", "000000"));

        String ip = getIpAddr(request);
        Fuser fuser = new Fuser();
        fuser.setFloginName(loginName);// 将登录字段校验改为floginName
        fuser.setFloginPassword(password);
        fuser.setSalt(fusers.get(0).getSalt());
        int insertdata = fusers.get(0).getfInsertData();
        int limitedCount = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
        if (limitedCount > 0) {
            fuser = this.frontUserService.updateCheckLoginH5(fuser, ip, longLogin == 1, insertdata);
            if (fuser != null) {

                String deviceId = request.getParameter("deviceId");
                // String deviceId = device;
                if (deviceId == null || "".equals(deviceId)) {
                    deviceId = "webclient";
                }
                getSession(request).setAttribute("deviceId", deviceId);

                // 登录成功向聚安注册用户
//                JayunSecurity security = new JayunSecurity(request);
//                security.register(fuser.getFloginName(), fuser.getFtelephone());
////
////                // 发送登录通知
//                security.send(loginName, AppDB.loginTemplateId, loginName, TDateTime.Now().toString());
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
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "网站暂时不允许登录");
                        return jsonObject.toString();
                    }
                }

                if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
                    if (fuser.getFopenSecondValidate()) {
                        SetSecondLoginSession(request, fuser);
                    } else {
                        boolean saveFlag1 = settlementService.login(fuser.getFloginName(), fuser.getfIntroUser_id() == null ? "RY001" :
                                fuser.getfIntroUser_id().getFloginName().equals("") ? "RY001" : fuser.getfIntroUser_id().getFloginName(), fuser.getFtelephone());
                    	if(!saveFlag1) {
                    		jsonObject.accumulate("code", -1);
                            jsonObject.accumulate("msg", "系统繁忙，请稍少重试");
                            return jsonObject.toString();
                    	}
                        SetSession(fuser, request);
                        jsonObject.accumulate("uuid", fuser.getFid());
                        jsonObject.accumulate("fpostRealValidate", fuser.getFpostRealValidate());
                        jsonObject.accumulate("fgoogleValidate", fuser.getFgoogleValidate());
                        jsonObject.accumulate("fisTelValidate", fuser.getFisTelValidate());
                        jsonObject.accumulate("code", 0);
                        jsonObject.accumulate("msg", "登录成功");
                        if (request.getRequestURI().startsWith("/m/user/login/index")) {
                            setCookie("loginName", loginName, response);
                            setCookie("password", password, response);
                        }
                        String ftelephone = fuser.getFtelephone();
                        /*if (!"1".equals(Constant.AppLevel)) {
                            if (StringUtils.isNotEmpty(ftelephone)) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String content = "【以太国际】**" + ftelephone.substring(8, 11) + "在"
                                        + dateFormat.format(new Date()) + "(GMT+08:00)登录以太国际，若非您本人操作，请及时修改密码)";
                                sendSMSCF.send(ftelephone, content);

                            }
                        } else {
                            if (StringUtils.isNotEmpty(ftelephone)) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String content = "【以太国际】**" + ftelephone + "在" + dateFormat.format(new Date())
                                        + "(GMT+08:00)登录以太国际，若非您本人操作，请及时修改密码)";
                                sendSMSCF.send(ftelephone, content);

                            }
                        }*/
                        return jsonObject.toString();
                    }
                } else {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "账户出现安全隐患被冻结，请尽快联系客服");
                    return jsonObject.toString();
                }
            } else {
                // 错误的用户名或密码
                if (limitedCount == new Constant().ErrorCountLimit) {
                    jsonObject.accumulate("code", -2);
                    jsonObject.accumulate("msg", "用户名或密码错误");
                } else {
                    jsonObject.accumulate("code", -2);
                    jsonObject.accumulate("msg", "用户名或密码错误，您还有" + limitedCount + "次机会");
                }
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
                return jsonObject.toString();
            }
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip登录频繁，请2小时后再试!");
            return jsonObject.toString();
        }
        return null;
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/m/user/api/loginx", "/user/api/loginx" }, produces = JsonEncode)
    public String apilogin(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = true) String loginName, @RequestParam(required = true) String password,
            @RequestParam(required = true) String sign)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> data = new HashMap<>();
        int longLogin = -1;// 0是手机，1是邮箱
        String keyString = "floginName";
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("password", password);
        String sort = KeyUtil.sort(map);
        if (!sort.equals(sign)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "接口认证失败！");
            data.put("userId", "");
            data.put("token", "");
            jsonObject.accumulate("data", data);
            return jsonObject.toString();
        }
        // TODO 登录只用floginName 注册时的手机号账号登录
        if (loginName.matches(Constant.PhoneReg) == true) {
            keyString = "floginName";
            longLogin = 0;
        }
        if ("1".equals(Constant.AppLevel)) {
            keyString = "floginName";
            longLogin = 0;
        }

        if (longLogin == -1) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "用户名错误");
            data.put("userId", "");
            data.put("token", "");
            jsonObject.accumulate("data", data);
            return jsonObject.toString();
        }

        List<Fuser> fusers = this.frontUserService.findUserByProperty(keyString, loginName);
        if (fusers == null || fusers.size() != 1) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "用户名不存在");
            data.put("userId", "");
            data.put("token", "");
            jsonObject.accumulate("data", data);
            return jsonObject.toString();
        }
        request.setAttribute("user", loginName);
        request.setAttribute("mobile", fusers.get(0).getFtelephone());

        String ip = getIpAddr(request);
        Fuser fuser = new Fuser();
        fuser.setFloginName(loginName);// 将登录字段校验改为floginName
        fuser.setFloginPassword(password);
        fuser.setSalt(fusers.get(0).getSalt());
        int insertdata = fusers.get(0).getfInsertData();
        int limitedCount = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
        if (limitedCount > 0) {

            fuser = this.frontUserService.updateCheckLogin(fuser, ip, longLogin == 1, insertdata);
            if (fuser != null) {

                String deviceId = request.getParameter("deviceId");
                // String deviceId = device;
                if (deviceId == null || "".equals(deviceId)) {
                    deviceId = "webclient";
                }
                getSession(request).setAttribute("deviceId", deviceId);

                // 登录成功向聚安注册用户
                JayunSecurity security = new JayunSecurity(request);
                security.register(fuser.getFloginName(), fuser.getFtelephone());

                // 发送登录通知
                security.send(loginName, AppDB.loginTemplateId, loginName, TDateTime.Now().toString());
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
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "网站暂时不允许登录");
                        data.put("userId", "");
                        data.put("token", "");
                        jsonObject.accumulate("data", data);
                        return jsonObject.toString();
                    }
                }

                if (fuser.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
                    if (fuser.getFopenSecondValidate()) {
                        SetSecondLoginSession(request, fuser);
                    } else {
                        HttpSession session = getSession(request);
                        session.setAttribute("login_user", fuser);
                        String sessionId = session.getId();
                        jsonObject.accumulate("code", 0);
                        jsonObject.accumulate("msg", "登录成功");
                        data.put("userId", String.valueOf(fuser.getFid()));
                        data.put("token", this.getSession(request).getId());
                        data.put("sessionId", sessionId);
                        jsonObject.accumulate("data", data);
                        if (request.getRequestURI().startsWith("/m/user/login/index")) {
                            setCookie("loginName", loginName, response);
                            setCookie("password", password, response);
                        }
                        String ftelephone = fuser.getFtelephone();
                        if (!"1".equals(Constant.AppLevel)) {
                            if (StringUtils.isNotEmpty(ftelephone)) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String content = "【以太国际】**" + ftelephone.substring(8, 11) + "在"
                                        + dateFormat.format(new Date()) + "(GMT+08:00)登录以太国际，若非您本人操作，请及时修改密码)";
                                sendSMSCF.send(ftelephone, content);

                            }
                        } else {
                            if (StringUtils.isNotEmpty(ftelephone)) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String content = "【以太国际】**" + ftelephone + "在" + dateFormat.format(new Date())
                                        + "(GMT+08:00)登录以太国际，若非您本人操作，请及时修改密码)";
                                sendSMSCF.send(ftelephone, content);

                            }
                        }
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

    /*
     * 添加谷歌设备
     */
    @ResponseBody
    @RequestMapping(value = "/user/googleAuth", produces = { JsonEncode })
    public String googleAuth(HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        if (fuser.getFgoogleBind()) {
            // 已经绑定机器了，属于非法提交
            jsonObject.accumulate("code", -1);
            return jsonObject.toString();
        }

        Map<String, String> map = GoogleAuth.genSecret(fuser.getFloginName());
        String totpKey = map.get("secret");
        String qecode = map.get("url");

        fuser.setFgoogleAuthenticator(totpKey);
        fuser.setFgoogleurl(qecode);
        fuser.setFlastUpdateTime(Utils.getTimestamp());
        this.frontUserService.updateFUser(fuser, getSession(request), -1, null);

        jsonObject.accumulate("qecode", qecode);
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("totpKey", totpKey);

        return jsonObject.toString();
    }

    /*
     * 添加设备认证
     */
    @ResponseBody
    @RequestMapping(value = "/user/validateAuthenticator", produces = { JsonEncode })
    public String validateAuthenticator(HttpServletRequest request, @RequestParam(required = true) String code,
            @RequestParam(required = true) String totpKey) throws Exception {
        JSONObject jsonObject = new JSONObject();

        String ip = getIpAddr(request);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        boolean b_status = fuser.getFgoogleBind() == false && totpKey.equals(fuser.getFgoogleAuthenticator())
                && !totpKey.trim().equals("");

        if (!b_status) {
            // 非法提交
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "您已绑定GOOGLE验证器，请勿重复操作");
            return jsonObject.toString();
        }

        int limitedCount = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
        if (limitedCount > 0) {
            boolean auth = GoogleAuth.auth(Long.parseLong(code), fuser.getFgoogleAuthenticator());
            if (auth) {
                jsonObject.accumulate("code", 0);// 0成功，-1，错误
                jsonObject.accumulate("msg", "绑定成功");
                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);

                fuser.setFgoogleBind(true);
                fuser.setFgoogleValidate(false);
                this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_BIND_GOOGLE, ip);
                return jsonObject.toString();
            } else {
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "GOOGLE验证码有误，您还有" + limitedCount + "次机会");
                return jsonObject.toString();
            }
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }

    }

    /*
     * 查看谷歌密匙
     */
    @ResponseBody
    @RequestMapping(value = "/user/getGoogleTotpKey")
    public String getGoogleTotpKey(HttpServletRequest request, @RequestParam(required = true) String totpCode)
            throws Exception {
        JSONObject jsonObject = new JSONObject();

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        String ip = getIpAddr(request);
        int limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
        if (limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        } else {
            if (fuser.getFgoogleBind()) {
                if (GoogleAuth.auth(Long.parseLong(totpCode), fuser.getFgoogleAuthenticator())) {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);

                    jsonObject.accumulate("qecode", fuser.getFgoogleurl());
                    jsonObject.accumulate("code", 0);
                    jsonObject.accumulate("totpKey", fuser.getFgoogleAuthenticator());
                    jsonObject.accumulate("msg", "验证成功");
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
                    return jsonObject.toString();
                } else {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "GOOGLE验证码有误，您还有" + limit + "次机会");
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                    return jsonObject.toString();
                }
            } else {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "您未绑定GOOGLE验证器");
                return jsonObject.toString();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/user/getGoogleCheck")
    public String getGoogleCheck(HttpServletRequest request, @RequestParam(required = true) int statusCode,
            @RequestParam(required = true) String phoneCode) throws Exception {
        JSONObject jsonObject = new JSONObject();
        String msg = statusCode == 1 ? "已启用Google邮箱验证！" : "已关闭Google邮箱验证！";

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        String ip = getIpAddr(request);
        if (fuser.isFisTelephoneBind()) {
            int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (mobil_limit <= 0) {
                jsonObject.accumulate("code", -7);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                JayunSecurity security = new JayunSecurity(request);
                if ("".equals(phoneCode)) {
                    if (!security.isSecurity(fuser.getFloginName())) {
                        jsonObject.accumulate("code", -50);
                        jsonObject.accumulate("msg", security.getMessage());
                        return jsonObject.toString();
                    }
                } else {
                    boolean bool = false;
                    if (ShortMessageService.isTrue()) {
                        bool = ShortMessageService.CheckCode(fuser.getFtelephone(), phoneCode, new Date());
                    } else {
                        bool = security.checkVerify(fuser.getFloginName(), phoneCode);// 接入聚安校验短信验证码
                    }
                    if (!bool) {
                        jsonObject.accumulate("code", -7);
                        jsonObject.accumulate("msg",
                                String.format("手机验证码有误，您还有%s次机会" + security.getMessage(), mobil_limit));
                        this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                        return jsonObject.toString();
                    } else {
                        this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                    }
                }
            }
        }

        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s", AppDB.Fuser);
            ds.add("where fid=%d", fuser.getFid());
            ds.open();
            ds.edit();
            ds.setField("fGoogleCheck", statusCode);
            ds.post();
            fuser.setFgoogleCheck(statusCode == 1);
            SetSession(fuser, request);
        } catch (Exception e) {
            jsonObject.accumulate("msg", e.getMessage());
            return jsonObject.toString();
        }
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", msg);
        return jsonObject.toString();
    }

    /*
     * 发送手机验证码
     * http://localhost:8899/user/sendValidateCode.html?random=46&areaCode=86&
     * msgType=0&phone=15017549972
     */
    @ResponseBody
    @RequestMapping(value = "/user/sendValidateCode", produces = { JsonEncode })
    public String sendValidateCode(HttpServletRequest request,
            @RequestParam(required = true, defaultValue = "0") String areaCode,
            @RequestParam(required = true, defaultValue = "0") String phone) throws Exception {
        if (!areaCode.equals("86") || phone.matches(Constant.PhoneReg)) {
            boolean isPhoneExists = this.frontUserService.isTelephoneExists(phone);
            if (isPhoneExists) {
                return String.valueOf(-3);// 手机账号存在
            } else {
                Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

                super.SendMessage(fuser, fuser.getFid(), areaCode, phone, MessageTypeEnum.BANGDING_MOBILE);
                return String.valueOf(0);
            }
        } else {
            return String.valueOf(-2);// 手机号码格式不正确
        }
    }

    /*
     * 綁定手機 http://localhost:8899/user/validatePhone.html?random=35
     * &areaCode=86&code=333333&phone=15017549972&totpCode=333333
     */
    @ResponseBody
    @RequestMapping(value = "/user/validatePhone", produces = { JsonEncode })
    public String validatePhone(HttpServletRequest request, @RequestParam(required = true) int isUpdate, // 0是绑定手机，1是换手机号码
            @RequestParam(required = true) String areaCode, @RequestParam(required = true) String imgcode,
            @RequestParam(required = true) String phone, @RequestParam(required = true) String oldcode,
            @RequestParam(required = true) String newcode,
            @RequestParam(required = false, defaultValue = "0") String totpCode) throws Exception {
        JSONObject jsonObject = new JSONObject();
        // areaCode = areaCode.replace("+", "");
        if (!phone.matches(Constant.PhoneReg)) {// 手機格式不對
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "手机号码格式不正确");
            return jsonObject.toString();
        }

        // 添加通过 phone查询用户如果查询到了说明该手机号码已被绑定
        boolean flag = this.frontUserService.isLoginNameExists(phone);//
        if (flag) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", phone + "手机号码已经被绑定");
            return jsonObject.toString();
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        if (isUpdate == 0) {
            if (fuser.isFisTelephoneBind()) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "您已绑定了手机号码");
                return jsonObject.toString();
            }
        } else {
            if (!fuser.isFisTelephoneBind()) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "您还未绑定手机号码");
                return jsonObject.toString();
            }
        }

        String ip = getIpAddr(request);
        int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
        if (google_limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }
        if (tel_limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }

        if (fuser.getFgoogleBind() && fuser.isFgoogleCheck()) {
            boolean googleAuth = GoogleAuth.auth(Long.parseLong(totpCode), fuser.getFgoogleAuthenticator());
            if (!googleAuth) {
                // 谷歌驗證失敗
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "GOOGLE验证码有误，您还有" + google_limit + "次机会");
                return jsonObject.toString();
            } else {
                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
            }
        }

        if (isUpdate == 1) {
            JayunSecurity security = new JayunSecurity(request);
            if ("".equals(oldcode)) {
                if (!security.isSecurity(fuser.getFloginName())) {
                    jsonObject.accumulate("code", -50);
                    jsonObject.accumulate("msg", security.getMessage());
                    return jsonObject.toString();
                }
            } else {
                if (!ShortMessageService.isTrue()) {
                    if (!ShortMessageService.CheckCode(phone, oldcode, new Date())) {
                        this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "旧手机验证码有误，您还有" + tel_limit + "次机会");
                        return jsonObject.toString();
                    }
                } else {
                    if (!security.checkVerify(fuser.getFloginName(), oldcode)) {
                        // 手機驗證錯誤
                        this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "旧手机验证码有误，您还有" + tel_limit + "次机会");
                        return jsonObject.toString();
                    } else {
                        this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                    }
                }
            }
        }

        // 验证验证码
        String oldPhoneAreaCode = fuser.getFareaCode();
        boolean newPhone = false;
        boolean oldPhone = false;
        if(ShortMessageService.isTrue()){
            if ("86".equals(areaCode)) {
                newPhone = ShortMessageService.CheckCode(phone, newcode,new Date());
            }else{
                newPhone = ShortMessageService.CheckCode("+"+areaCode + phone, newcode, new Date());
            }
        }

        if(ShortMessageService.isTrue()){
            if ("86".equals(oldPhoneAreaCode)) {
                oldPhone = ShortMessageService.CheckCode(fuser.getFtelephone(), oldcode,new Date());
            }else{
                oldPhone = ShortMessageService.CheckCode("+"+oldPhoneAreaCode + fuser.getFtelephone(), oldcode, new Date());
            }
        }

        if (oldPhone && newPhone) {
            // 判斷手機是否被綁定了
            // List<Fuser> fusers =
            // this.frontUserService.findUserByProperty("ftelephone",
            // phone);
            // if (fusers.size() > 0) {// 手機號碼已經被綁定了
            // jsonObject.accumulate("code", -1);
            // jsonObject.accumulate("msg", "手机号码已存在");
            // return jsonObject.toString();
            // }

            if (vcodeValidate(request, imgcode) == false) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "请输入正确的图片验证码");
                return jsonObject.toString();
            }

            fuser.setFareaCode(areaCode);
            fuser.setFtelephone(phone);
            if (fuser.getFregType() == RegTypeEnum.TEL_VALUE) {
                // fuser.setFloginName(phone);
            }
            fuser.setFisTelephoneBind(true);
            fuser.setFlastUpdateTime(Utils.getTimestamp());
            fuser.setFnickName(phone);
            try {
                com.alibaba.fastjson.JSONObject resJson = settlementService.updateUser(fuser.getFloginName(), fuser.getFtelephone(), null);
                if (resJson != null && StringUtils.isNotEmpty((String)resJson.get("status")) && resJson.get("status").equals("200")) {
                    this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_BIND_PHONE, ip);
                } else {
                    jsonObject.accumulate("status", 500);
                    jsonObject.accumulate("msg", resJson.getString("data"));
                    return jsonObject.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "网络异常");
                return jsonObject.toString();
            } finally {
                // 成功
                this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.BANGDING_MOBILE);
                this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.JIEBANG_MOBILE);
            }

            this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
            // 重新注册聚安

            // -----------------------------
            JayunSecurity security = new JayunSecurity(request);
            security.register(fuser.getFloginName(), phone);

            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "绑定成功");
            return jsonObject.toString();
        } else {
            // 手機驗證錯誤
            this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "手机验证码有误，您还有" + tel_limit + "次机会");
            return jsonObject.toString();
        }
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/user/api/resetPhonex", "/m/api/resetPhonex" }, produces = { JsonEncode })
    public String resetPhone(HttpServletRequest request, @RequestParam(required = true) String areaCode,
            @RequestParam(required = true) String imgcode, @RequestParam(required = true) String phone,
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
        if (!sort.equals(sign)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "接口认证失败！");
            return jsonObject.toString();
        }

        if (!phone.matches(Constant.PhoneReg)) {// 手機格式不對
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "手机号码格式不正确");
            return jsonObject.toString();
        }

        // Fuser fuser = this.frontUserService.findById(881007910);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        String ip = getIpAddr(request);
        int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
        if (google_limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }
        if (tel_limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }

        JayunSecurity security = new JayunSecurity(request);
        if ("".equals(oldcode)) {
            if (!security.isSecurity(fuser.getFloginName())) {
                jsonObject.accumulate("code", -50);
                jsonObject.accumulate("msg", security.getMessage());
                return jsonObject.toString();
            }
        } else {
            if (ShortMessageService.isTrue()) {
                if (!ShortMessageService.CheckCode(phone, oldcode, new Date())) {
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "旧手机验证码有误，您还有" + tel_limit + "次机会");
                    return jsonObject.toString();
                }
            } else {
                if (!security.checkVerify(fuser.getFloginName(), oldcode)) {
                    // 手機驗證錯誤
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "旧手机验证码有误，您还有" + tel_limit + "次机会");
                    return jsonObject.toString();
                } else {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                }
            }
        }
        JayunMessage message = new JayunMessage(request);
        boolean bool = message.checkRegister(phone, newcode);// 接入聚安校验验证码
        if (bool) {

            if (vcodeValidate(request, imgcode) == false) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "请输入正确的图片验证码");
                return jsonObject.toString();
            }

            fuser.setFareaCode(areaCode);
            fuser.setFtelephone(phone);
            fuser.setFloginName(phone);
            fuser.setFisTelephoneBind(true);
            fuser.setFlastUpdateTime(Utils.getTimestamp());
            try {
                this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_BIND_PHONE, ip);
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "网络异常");
                return jsonObject.toString();
            } finally {
                // 成功
                this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.BANGDING_MOBILE);
                this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.JIEBANG_MOBILE);
            }

            this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
            // 重新注册聚安
            security = new JayunSecurity(request);
            security.register(fuser.getFloginName(), phone);

            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "修改成功");
            return jsonObject.toString();
        } else {
            // 手機驗證錯誤
            this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "手机验证码有误，您还有" + tel_limit + "次机会");
            return jsonObject.toString();
        }
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/user/api/sendMsgx", produces = JsonEncode)
    public String apisendMsg(HttpServletRequest request, @RequestParam(required = true) int type,
            @RequestParam(required = true) String areaCode, @RequestParam(required = true) String phone,
            @RequestParam(required = true) boolean sendVoice,
            @RequestParam(required = true) String sign) throws Exception {


        // 注册类型免登录可以发送
        JSONObject jsonObject = new JSONObject();
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("areaCode", areaCode);
        map.put("phone", phone);
        map.put("sendVoice", sendVoice);
        String sort = KeyUtil.sort(map);
        if (!sort.equals(sign)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "接口认证失败！");
            return jsonObject.toString();
        }
        if (type < MessageTypeEnum.BEGIN || type > MessageTypeEnum.END) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法提交");
            return jsonObject.toString();
        }

        String ip = getIpAddr(request);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
        if (tel_limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }

        Fuser fuser = null;
        if (type != MessageTypeEnum.REG_CODE) {
            if (type == MessageTypeEnum.FIND_PASSWORD) {
                List<Fuser> fusers = this.utilsService.list(0, 0, " where ftelephone=? ", false, Fuser.class,
                        new Object[] { phone });
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
                phone = phone = "+" + areaCode + phone;
            }
            if (!ShortMessageService.isTrue()) {
                String code = ShortMessageService.getCode();
                ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code + "，如非本人操作，请忽略本短信", code);
            }
            else {
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
            if (!ShortMessageService.isTrue()) {
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
                phone = phone = "+" + areaCode + phone;
            }
            if (!ShortMessageService.isTrue()) {
                String code = ShortMessageService.getCode();
                ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code + "，如非本人操作，请忽略本短信", code);
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
                phone = phone = "+" + areaCode + phone;
            }
            if (!ShortMessageService.isTrue()) {
                String code = ShortMessageService.getCode();
                ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code + "，如非本人操作，请忽略本短信", code);
            }
            else {
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
                if (!ShortMessageService.isTrue()) {
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

    /**
     * 发送短信给已登录用户，但这个用户需要给其他用户发送
     *
     *
     * @param request
     * @return
     * @throws Exception
     */
    public String strangeUser(HttpServletRequest request) throws Exception {
        String areaCode = request.getParameter("areaCode");
        String phone = request.getParameter("phone");
        // 获得验证码
        String code = ShortMessageService.getCode();
        // 发送短信
        YunPianUtils.sendMsg(areaCode, phone, code);
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "验证码已经发送，请查收");
        return jsonObject.toString();
    }

    /**
     * 发送给登录用户，或者未登录用户短信。
     *
     * @param request
     * @param type
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/user/sendMsg")
    public String sendMsg(HttpServletRequest request, @RequestParam(required = true) int type) throws Exception {
        String areaCode = request.getParameter("areaCode");
        String phone = request.getParameter("phone");
        String vcode = request.getParameter("vcode");
        String sendNumberAuth =  request.getParameter("sendNumberAuth");
        if(sendNumberAuth != null){
            return strangeUser(request);
        }


        boolean sendVoice = Boolean.valueOf(request.getParameter("sendVoice"));

        // 注册类型免登录可以发送
        JSONObject jsonObject = new JSONObject();

        if (type < MessageTypeEnum.BEGIN || type > MessageTypeEnum.END) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法提交");
            return jsonObject.toString();
        }


        String ip = getIpAddr(request);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
        if (tel_limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }

        Fuser fuser = null;
        if (type != MessageTypeEnum.REG_CODE) {
            if (type == MessageTypeEnum.FIND_PASSWORD) {
                List<Fuser> fusers = this.utilsService.list(0, 0, " where ftelephone=? ", false, Fuser.class,
                        new Object[] { phone });
                if (fusers.size() >= 1) {
                    fuser = fusers.get(0);
                } else {
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "手机号码错误");
                    return jsonObject.toString();
                }
            } else {
                fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
                areaCode = fuser.getFareaCode();
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
            if (ShortMessageService.isTrue()) {
                // 获得验证码
                String code = ShortMessageService.getCode();
                // 发送短信
                YunPianUtils.sendMsg(areaCode, phone, code);
            }
            else {
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
                // 获得验证码
                String code = ShortMessageService.getCode();
                // 发送短信
                YunPianUtils.sendMsg(areaCode, phone, code);
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

            if (ShortMessageService.isTrue()) {
                // 获得验证码
                String code = ShortMessageService.getCode();
                // 发送短信
                YunPianUtils.sendMsg(areaCode, phone, code);
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
            if (ShortMessageService.isTrue()) {
                // 获得验证码
                String code = ShortMessageService.getCode();
                // 发送短信
                YunPianUtils.sendMsg(areaCode, phone, code);
            }
            else {
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
        	if (ShortMessageService.isTrue()) {
                // 获得验证码
                String code = ShortMessageService.getCode();
                // 发送短信
                YunPianUtils.sendMsg(areaCode, phone, code);
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
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "验证码已经发送，请查收");
        return jsonObject.toString();

    }

    // 发送邮件验证码
    @ResponseBody
    @RequestMapping(value = "/user/sendMailCode")
    public String sendMailCode(HttpServletRequest request, @RequestParam(required = true) int type) throws Exception {
        String email = request.getParameter("email");
        String vcode = request.getParameter("vcode");

        // 注册类型免登录可以发送
        JSONObject jsonObject = new JSONObject();

        if (type != SendMailTypeEnum.RegMail && GetCurrentUser(request) == null) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法提交");
            return jsonObject.toString();
        }

        if (type < SendMailTypeEnum.BEGIN || type > SendMailTypeEnum.END) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法提交");
            return jsonObject.toString();
        }

        // 注册需要验证码
        // if(type == SendMailTypeEnum.RegMail){
        // if(vcodeValidate(request, vcode) == false ){
        // jsonObject.accumulate("code", -1) ;
        // jsonObject.accumulate("msg", "请输入正确的图片验证码");
        // return jsonObject.toString() ;
        // }
        // }

        String ip = getIpAddr(request);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.REG_MAIL);
        if (tel_limit <= 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
            return jsonObject.toString();
        }

        SendMail(getIpAddr(request), email, type);

        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "验证码已经发送，请查收");
        return jsonObject.toString();

    }

    /**
     * 增加提现支付宝
     **/
    /*
     * @ResponseBody
     * 
     * @RequestMapping("/user/updateOutAlipayAddress") public String updateOutAlipayAddress( HttpServletRequest request,
     * 
     * @RequestParam(required=true)String account,
     * 
     * @RequestParam(required=false,defaultValue="0")String phoneCode,
     * 
     * @RequestParam(required=false,defaultValue="0")String totpCode,
     * 
     * @RequestParam(required=true)String payeeAddr//开户姓名 ) throws Exception{ JSONObject jsonObject = new JSONObject() ;
     * 
     * Fuser fuser = this.frontUserService.findById(GetSession(request).getFid()) ; boolean isGoogleBind =
     * fuser.getFgoogleBind() ; boolean isTelephoneBind = fuser.isFisTelephoneBind() ; if(!isGoogleBind &&
     * !isTelephoneBind){ jsonObject.accumulate("code", -1) ; jsonObject.accumulate("msg", "请先绑定GOOGLE验证或手机号码") ; return
     * jsonObject.toString(); }
     * 
     * account = HtmlUtils.htmlEscape(account); phoneCode = HtmlUtils.htmlEscape(phoneCode); totpCode =
     * HtmlUtils.htmlEscape(totpCode); payeeAddr = HtmlUtils.htmlEscape(payeeAddr);
     * 
     * if(fuser.getFrealName().equals(payeeAddr)){ jsonObject.accumulate("code", -1) ; jsonObject.accumulate("msg",
     * "支付宝姓名必须与您的实名认证姓名一致") ; return jsonObject.toString(); }
     * 
     * if(account.length() >200){ jsonObject.accumulate("code", -1) ; jsonObject.accumulate("msg", "支付宝帐号有误") ; return
     * jsonObject.toString(); }
     * 
     * String ip = getIpAddr(request) ; int google_limit = this.frontValidateService.getLimitCount(ip,
     * CountLimitTypeEnum.GOOGLE) ; int tel_limit = this.frontValidateService.getLimitCount(ip,
     * CountLimitTypeEnum.TELEPHONE) ;
     * 
     * if(fuser.isFisTelephoneBind()){ if(tel_limit<=0){ jsonObject.accumulate("code", -1) ;
     * jsonObject.accumulate("msg", "手机验证码错误，请稍候再试") ; return jsonObject.toString(); }else{
     * if(!validateMessageCode(fuser, fuser.getFareaCode(), fuser.getFtelephone(), MessageTypeEnum.CNY_ACCOUNT_WITHDRAW,
     * phoneCode)){ //手機驗證錯誤 this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE) ;
     * jsonObject.accumulate("code", -1) ; jsonObject.accumulate("msg", "手机验证码错误，您还有"+(tel_limit-1)+"次机会") ; return
     * jsonObject.toString(); }else{ this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE) ; } }
     * }
     * 
     * 
     * if(fuser.getFgoogleBind()){ if(google_limit<=0){ jsonObject.accumulate("code", -1) ; jsonObject.accumulate("msg",
     * "GOOGLE验证码错误，请稍候再试") ; return jsonObject.toString(); }else{ boolean googleAuth =
     * GoogleAuth.auth(Long.parseLong(totpCode),fuser.getFgoogleAuthenticator()) ;
     * 
     * if(!googleAuth){ //谷歌驗證失敗 this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE) ;
     * jsonObject.accumulate("code", -1) ; jsonObject.accumulate("msg", "GOOGLE验证码错误，您还有"+(google_limit-1)+"次机会") ;
     * return jsonObject.toString(); }else{ this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE) ;
     * } }
     * 
     * }
     * 
     * //成功 try { FbankinfoWithdraw fbankinfoWithdraw = new FbankinfoWithdraw();
     * fbankinfoWithdraw.setFbankNumber(account) ; fbankinfoWithdraw.setFbankType(0) ;
     * fbankinfoWithdraw.setFcreateTime(Utils.getTimestamp()) ; fbankinfoWithdraw.setFname("支付宝帐号") ;
     * fbankinfoWithdraw.setFstatus(BankInfoStatusEnum.NORMAL_VALUE) ; fbankinfoWithdraw.setFaddress(payeeAddr);
     * fbankinfoWithdraw.setFothers(null); fbankinfoWithdraw.setFuser(fuser);
     * this.frontUserService.updateBankInfoWithdraw(fbankinfoWithdraw) ; jsonObject.accumulate("code", 0) ;
     * jsonObject.accumulate("msg", "操作成功") ; this.validateMap.removeMessageMap(fuser.getFid()+"_"+MessageTypeEnum.
     * CNY_ACCOUNT_WITHDRAW); } catch (Exception e) { jsonObject.accumulate("code", -1) ; jsonObject.accumulate("msg",
     * "网络异常") ; }
     * 
     * return jsonObject.toString() ; }
     */

    /**
     * 增加提现银行
     */
    @ResponseBody
    @RequestMapping("/user/updateOutAddress")
    public String updateOutAddress(HttpServletRequest request, @RequestParam(required = true) String account,
            @RequestParam(required = false, defaultValue = "0") String phoneCode,
            @RequestParam(required = false, defaultValue = "0") String totpCode,
            @RequestParam(required = true) int openBankType, @RequestParam(required = true) String address,
            @RequestParam(required = true) String prov, @RequestParam(required = true) String city,
            @RequestParam(required = true, defaultValue = "0") String dist,
            @RequestParam(required = true) String payeeAddr// 开户姓名
    ) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        boolean isGoogleBind = fuser.getFgoogleBind();
        boolean isTelephoneBind = fuser.isFisTelephoneBind();
        if (!isGoogleBind && !isTelephoneBind) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先绑定GOOGLE验证或手机号码");
            return jsonObject.toString();
        }

        address = HtmlUtils.htmlEscape(address);
        account = HtmlUtils.htmlEscape(account);
        phoneCode = HtmlUtils.htmlEscape(phoneCode);
        totpCode = HtmlUtils.htmlEscape(totpCode);
        prov = HtmlUtils.htmlEscape(prov);
        city = HtmlUtils.htmlEscape(city);
        dist = HtmlUtils.htmlEscape(dist);
        payeeAddr = fuser.getFrealName();

        String last = prov + city;
        if (!dist.equals("0")) {
            last = last + dist;
        }

        if (account.length() < 10) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "银行帐号不合法");
            return jsonObject.toString();
        }

        if (address.length() > 300) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "详细地址太长");
            return jsonObject.toString();
        }

        if (last.length() > 50) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }

        // if(fuser.getFrealName().equals(payeeAddr)){
        // jsonObject.accumulate("code", -1) ;
        // jsonObject.accumulate("msg", "银行卡账号名必须与您的实名认证姓名一致") ;
        // return jsonObject.toString();
        // }

        String bankName = BankTypeEnum.getEnumString(openBankType);
        if (bankName == null || bankName.trim().length() == 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "开户银行有误");
            return jsonObject.toString();
        }

        String ip = getIpAddr(request);
        int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
        int tel_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);

        if (fuser.isFisTelephoneBind()) {
            if (tel_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                boolean bool = false;
                if (ShortMessageService.isTrue()) {
                    bool = ShortMessageService.CheckCode(fuser.getFtelephone(), phoneCode, new Date());
                } else {
                    JayunSecurity security = new JayunSecurity(request);
                    bool = security.checkVerify(fuser.getFloginName(), phoneCode);// 接入聚安校验短信验证码
                }
                /* System.err.println(security.getMessage()); */
                // !validateMessageCode(fuser, fuser.getFareaCode(),
                // fuser.getFtelephone(),
                // MessageTypeEnum.CNY_ACCOUNT_WITHDRAW, phoneCode)
                if (!bool) {
                    // 手機驗證錯誤
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "手机验证码错误，您还有" + tel_limit + "次机会");
                    return jsonObject.toString();
                } else {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                }
            }
        }

        if (fuser.getFgoogleBind() && fuser.isFgoogleCheck()) {
            if (google_limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                boolean googleAuth = GoogleAuth.auth(Long.parseLong(totpCode), fuser.getFgoogleAuthenticator());

                if (!googleAuth) {
                    // 谷歌驗證失敗
                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                    jsonObject.accumulate("code", -1);
                    jsonObject.accumulate("msg", "GOOGLE验证码错误，您还有" + google_limit + "次机会");
                    return jsonObject.toString();
                } else {
                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
                }
            }

        }

        // 成功
        try {
            FbankinfoWithdraw fbankinfoWithdraw = new FbankinfoWithdraw();
            fbankinfoWithdraw.setFbankNumber(account);
            fbankinfoWithdraw.setFbankType(openBankType);
            fbankinfoWithdraw.setFcreateTime(Utils.getTimestamp());
            fbankinfoWithdraw.setFname(bankName);
            fbankinfoWithdraw.setFstatus(BankInfoStatusEnum.NORMAL_VALUE);
            fbankinfoWithdraw.setFaddress(last);
            fbankinfoWithdraw.setFothers(address);
            fbankinfoWithdraw.setFuser(fuser);
            this.frontUserService.updateBankInfoWithdraw(fbankinfoWithdraw);
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CNY_ACCOUNT_WITHDRAW);
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/m/user/api/addbankcard", "/user/api/addBankCard" }, produces = JsonEncode)
    public String apiupdateOutAddress(HttpServletRequest request, @RequestParam(required = true) String account,
            @RequestParam(required = true) String name, @RequestParam(required = true) String tradePassword,
            @RequestParam(required = true) int openBankType, @RequestParam(required = true) String sign)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("openBankType", openBankType);
        map.put("tradePassword", tradePassword);
        map.put("name", name);
        String sort = KeyUtil.sort(map);
        if (!sort.equals(sign)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "接口认证失败！");
            return jsonObject.toString();
        }

        Fuser user = this.GetCurrentUser(request);
        if (user == null) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先登陆！");
            return jsonObject.toString();
        }

        if (!user.getFtradePassword().equals(Utils.MD5(tradePassword, user.getSalt()))) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "交易密码错误！");
            return jsonObject.toString();
        }

        try (Mysql mysql = new Mysql()) {
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
                ds.setField("fBanknamez", openBankType);
                ds.setField("fType", 0);
                ds.setField("fCreateTime", TDateTime.Now());
                ds.post();
                jsonObject.put("code", 0);
                jsonObject.put("msg", "银行卡添加成功");
            } else {
                jsonObject.put("code", -1);
                jsonObject.put("msg", "银行卡已存在");
            }
        } catch (Exception e) {
            jsonObject.put("code", -1);
            jsonObject.put("msg", e.getMessage());
            return jsonObject.toString();
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/user/deleteBankAddress")
    public String deleteBankAddress(HttpServletRequest request, @RequestParam(required = true) int fid)
            throws Exception {
        JSONObject jsonObject = new JSONObject();

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        FbankinfoWithdraw fbankinfoWithdraw = this.frontUserService.findByIdWithBankInfos(fid);
        if (fbankinfoWithdraw == null) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "记录不存在");
            return jsonObject.toString();
        }
        if (fuser.getFid() != fbankinfoWithdraw.getFuser().getFid()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }
        // 成功
        try {
            this.frontUserService.updateDelBankInfoWithdraw(fbankinfoWithdraw);
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/user/detelCoinAddress")
    public String detelCoinAddress(HttpServletRequest request, @RequestParam(required = true) int fid)
            throws Exception {
        JSONObject jsonObject = new JSONObject();

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        FvirtualaddressWithdraw virtualaddressWithdraw = this.frontVirtualCoinService.findFvirtualaddressWithdraw(fid);
        if (virtualaddressWithdraw == null) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "记录不存在");
            return jsonObject.toString();
        }
        if (fuser.getFid() != virtualaddressWithdraw.getFuser().getFid()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "非法操作");
            return jsonObject.toString();
        }
        // 成功
        try {
            this.frontVirtualCoinService.updateDelFvirtualaddressWithdraw(virtualaddressWithdraw);
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/user/modifyPwd", "/m/user/modifyPwd" })
    public String modifyPwd(HttpServletRequest request, @RequestParam(required = true) String newPwd,
            @RequestParam(required = false, defaultValue = "0") String originPwd,
            @RequestParam(required = false, defaultValue = "0") String phoneCode,
            @RequestParam(required = false, defaultValue = "0") int pwdType,
            @RequestParam(required = true) String reNewPwd,
            @RequestParam(required = false, defaultValue = "0") String totpCode) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        if (!newPwd.equals(reNewPwd)) {
            jsonObject.accumulate("code", -3);
            jsonObject.accumulate("msg", "两次输入密码不一样");
            return jsonObject.toString();// 两次输入密码不一样
        }

        if (!fuser.isFisTelephoneBind()) {
            jsonObject.accumulate("resultCode", -13);
            jsonObject.accumulate("msg", "需要绑定手机号码才能修改密码");
            return jsonObject.toString();// 需要绑定绑定谷歌或者电话才能修改密码
        }

        String salt = fuser.getSalt();

        if (pwdType == 0) {
            // 修改登录密码
            if (!fuser.getFloginPassword().equals(MyMD5Utils.encoding(originPwd))) {
                jsonObject.accumulate("code", -5);
                jsonObject.accumulate("msg", "原始登录密码错误");
                return jsonObject.toString();// 原始密码错误
            }
//            if (null == salt) {
//                if (!fuser.getFloginPassword().equals(MD5.get(originPwd))) {
//                    jsonObject.accumulate("code", -5);
//                    jsonObject.accumulate("msg", "原始登录密码错误");
//                    return jsonObject.toString();// 原始密码错误
//                }
//            } else {
//                if (!fuser.getFloginPassword().equals(Utils.MD5(originPwd, fuser.getSalt()))) {
//                    jsonObject.accumulate("code", -5);
//                    jsonObject.accumulate("msg", "原始登录密码错误");
//                    return jsonObject.toString();// 原始密码错误
//                }
//            }

        }

        if (pwdType == 0) {
            // 修改交易密码
            if (fuser.getFloginPassword().equals(MyMD5Utils.encoding(newPwd))) {
                jsonObject.accumulate("code", -10);
                jsonObject.accumulate("msg", "新的登录密码与原始密码相同，修改失败");
                return jsonObject.toString();
            }
//            if (null == salt) {
//                if (fuser.getFloginPassword().equals(MD5.get(newPwd))) {
//                    jsonObject.accumulate("code", -10);
//                    jsonObject.accumulate("msg", "新的登录密码与原始密码相同，修改失败");
//                    return jsonObject.toString();
//                }
//            } else {
//                if (fuser.getFloginPassword().equals(Utils.MD5(newPwd, fuser.getSalt()))) {
//                    jsonObject.accumulate("code", -10);
//                    jsonObject.accumulate("msg", "新的登录密码与原始密码相同，修改失败");
//                    return jsonObject.toString();
//                }
//            }

        } else {
            // 修改交易密码
            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0
                    && fuser.getFtradePassword().equals(MyMD5Utils.encoding(newPwd))) {
                jsonObject.accumulate("code", -10);
                jsonObject.accumulate("msg", "新的交易密码与原始密码相同，修改失败");
                return jsonObject.toString();
            }
            if(fuser.getFloginPassword().equals(MyMD5Utils.encoding(newPwd))) {
            	jsonObject.accumulate("code", -10);
                jsonObject.accumulate("msg", "交易密码与登录密码相同，修改失败");
                return jsonObject.toString();
            }
//            if (null == salt) {
//                if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0
//                        && fuser.getFtradePassword().equals(MD5.get(newPwd))) {
//                    jsonObject.accumulate("code", -10);
//                    jsonObject.accumulate("msg", "新的交易密码与原始密码相同，修改失败");
//                    return jsonObject.toString();
//                }
//            } else {
//                if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0
//                        && fuser.getFtradePassword().equals(Utils.MD5(newPwd, fuser.getSalt()))) {
//                    jsonObject.accumulate("code", -10);
//                    jsonObject.accumulate("msg", "新的交易密码与原始密码相同，修改失败");
//                    return jsonObject.toString();
//                }
//            }

        }

        String ip = getIpAddr(request);
        if (fuser.isFisTelephoneBind()) {
            int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (mobil_limit <= 0) {
                jsonObject.accumulate("code", -7);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                // boolean mobilValidate = false;
                JayunSecurity security = new JayunSecurity(request);
                if ("".equals(phoneCode)) {
                    if (!security.isSecurity(fuser.getFloginName())) {
                        jsonObject.accumulate("code", -50);
                        jsonObject.accumulate("msg", security.getMessage());
                        return jsonObject.toString();
                    }
                } else {
                    boolean bool = false;
                    if(ShortMessageService.isTrue()){
                        if ("86".equals(fuser.getFareaCode())) {
                            bool = ShortMessageService.CheckCode(fuser.getFtelephone(), phoneCode, new Date());
                        }else{
                            bool = ShortMessageService.CheckCode("+"+fuser.getFareaCode() + fuser.getFtelephone(), phoneCode, new Date());
                        }
                    }

//                    if (ShortMessageService.isTrue()) {
//                        bool = ShortMessageService.CheckCode(fuser.getFtelephone(), phoneCode, new Date());
//                    } else {
//                        bool = security.checkVerify(fuser.getFloginName(), phoneCode);// 接入聚安校验短信验证码
//                    }
                    if (!bool) {
                        jsonObject.accumulate("code", -7);
                        jsonObject.accumulate("msg", "手机验证码有误，您还有" + mobil_limit + "次机会");
                        this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                        return jsonObject.toString();
                    } else {
                        this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                    }
                }
            }
        }
//        if (fuser.getFgoogleBind() && fuser.isFgoogleCheck()) {
//            int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
//            if (google_limit <= 0) {
//                jsonObject.accumulate("code", -6);
//                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
//                return jsonObject.toString();
//            } else {
//                if (!GoogleAuth.auth(Long.parseLong(totpCode), fuser.getFgoogleAuthenticator())) {
//                    jsonObject.accumulate("code", -6);
//                    jsonObject.accumulate("msg", "GOOGLE验证码有误，您还有" + google_limit + "次机会");
//                    this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
//                    return jsonObject.toString();
//                } else {
//                    this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
//                }
//            }
//        }
        try {
            if (pwdType == 0) {
                // 修改登录密码
                fuser.setFloginPassword(MyMD5Utils.encoding(newPwd));
//                if (null == salt) {
//                    fuser.setFloginPassword(MD5.get(newPwd));
//                } else {
//                    fuser.setFloginPassword(Utils.MD5(newPwd, fuser.getSalt()));
//                }
                this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_UPDATE_LOGIN_PWD, ip);
            } else {
                // 修改交易密码
                fuser.setFtradePassword(MyMD5Utils.encoding(newPwd));
//                if (null == salt) {
//                    fuser.setFtradePassword(MD5.get(newPwd));
//                } else {
//                    fuser.setFtradePassword(Utils.MD5(newPwd, fuser.getSalt()));
//                }
                int logType = 0;
                if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0) {
                    logType = LogTypeEnum.User_UPDATE_TRADE_PWD;
                } else {
                    logType = LogTypeEnum.User_SET_TRADE_PWD;
                }
                this.frontUserService.updateFUser(fuser, getSession(request), logType, ip);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -3);
            jsonObject.accumulate("msg", "网络异常");
            return jsonObject.toString();
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CHANGE_LOGINPWD);
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CHANGE_TRADEPWD);
        }

        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "操作成功");
        return jsonObject.toString();
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/user/api/modifyPwdx", "/m/api/modifyPwdx" })
    public String apimodifyPwd(HttpServletRequest request, @RequestParam(required = true) String newPwd,
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
        if (!sort.equals(sign)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "接口认证失败！");
            return jsonObject.toString();
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        if (!newPwd.equals(reNewPwd)) {
            jsonObject.accumulate("code", -3);
            jsonObject.accumulate("msg", "两次输入密码不一样");
            return jsonObject.toString();// 两次输入密码不一样
        }

        // if (!fuser.isFisTelephoneBind() && !fuser.getFgoogleBind()) {
        // jsonObject.accumulate("resultCode", -13);
        // jsonObject.accumulate("msg", "需要绑定绑定谷歌或者手机号码才能修改密码");
        // return jsonObject.toString();// 需要绑定绑定谷歌或者电话才能修改密码
        // }

        String salt = fuser.getSalt();

        // 修改交易密码
        if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0
                && fuser.getFtradePassword().equals(MyMD5Utils.encoding(newPwd))) {
            jsonObject.accumulate("code", -10);
            jsonObject.accumulate("msg", "新的交易密码与原始密码相同，修改失败");
            return jsonObject.toString();
        }

//        if (null == salt) {
//            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0
//                    && fuser.getFtradePassword().equals(MD5.get(newPwd))) {
//                jsonObject.accumulate("code", -10);
//                jsonObject.accumulate("msg", "新的交易密码与原始密码相同，修改失败");
//                return jsonObject.toString();
//            }
//        } else {
//            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0
//                    && fuser.getFtradePassword().equals(Utils.MD5(newPwd, fuser.getSalt()))) {
//                jsonObject.accumulate("code", -10);
//                jsonObject.accumulate("msg", "新的交易密码与原始密码相同，修改失败");
//                return jsonObject.toString();
//            }
//        }

        String ip = getIpAddr(request);
        if (fuser.isFisTelephoneBind()) {
            int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (mobil_limit <= 0) {
                jsonObject.accumulate("code", -7);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                JayunSecurity security = new JayunSecurity(request);
                if ("".equals(phoneCode)) {
                    if (!security.isSecurity(fuser.getFloginName())) {
                        jsonObject.accumulate("code", -50);
                        jsonObject.accumulate("msg", security.getMessage());
                        return jsonObject.toString();
                    }
                } else {
                    boolean bool = false;
                    if (ShortMessageService.isTrue()) {
                        bool = ShortMessageService.CheckCode(fuser.getFtelephone(), phoneCode, new Date());
                    } else {
                        bool = security.checkVerify(fuser.getFloginName(), phoneCode);// 接入聚安校验短信验证码
                    }
                    if (!bool) {
                        jsonObject.accumulate("code", -7);
                        jsonObject.accumulate("msg", "手机验证码有误，您还有" + mobil_limit + "次机会");
                        this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                        return jsonObject.toString();
                    } else {
                        this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
                    }
                }
            }
        }

        try {
            // 修改交易密码
            fuser.setFtradePassword(MyMD5Utils.encoding(newPwd));
//            if (null == salt) {
//                fuser.setFtradePassword(MD5.get(newPwd));
//            } else {
//                fuser.setFtradePassword(Utils.MD5(newPwd, fuser.getSalt()));
//            }
            int logType = 0;
            if (fuser.getFtradePassword() != null && fuser.getFtradePassword().trim().length() > 0) {
                logType = LogTypeEnum.User_UPDATE_TRADE_PWD;
            } else {
                logType = LogTypeEnum.User_SET_TRADE_PWD;
            }
            this.frontUserService.updateFUser(fuser, getSession(request), logType, ip);

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -3);
            jsonObject.accumulate("msg", "网络异常");
            return jsonObject.toString();
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CHANGE_LOGINPWD);
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.CHANGE_TRADEPWD);
        }

        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "操作成功");
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/user/modifyWithdrawBtcAddr")
    public String modifyWithdrawBtcAddr(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") String phoneCode,
            @RequestParam(required = false, defaultValue = "0") String totpCode,
            @RequestParam(required = true) int symbol, @RequestParam(required = true) String withdrawAddr,
            @RequestParam(required = true) String withdrawBtcPass,
            @RequestParam(required = false, defaultValue = "REMARK") String withdrawRemark) throws Exception {
        JSONObject jsonObject = new JSONObject();
        withdrawAddr = HtmlUtils.htmlEscape(withdrawAddr.trim());
        withdrawRemark = HtmlUtils.htmlEscape(withdrawRemark.trim());
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        if (!fuser.getFgoogleBind() && !fuser.isFisTelephoneBind()) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "请先绑定GOOGLE验证或手机号码");
            return jsonObject.toString();
        }

        if (fuser.getFtradePassword() == null || fuser.getFtradePassword().trim().length() == 0) {
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", "请先设置交易密码");
            return jsonObject.toString();
        }

        if (!fuser.getFtradePassword().equals(MyMD5Utils.encoding(withdrawBtcPass))) {
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", "交易密码不正确");
            return jsonObject.toString();
        }

        if (withdrawRemark.length() > 100) {
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", "备注超出长度");
            return jsonObject.toString();
        }

        // if(withdrawAddr.length() != 42 && withdrawAddr.length() != 34){
        // jsonObject.accumulate("code", -4) ;
        // jsonObject.accumulate("msg","提现地址格式有误") ;
        // return jsonObject.toString() ;
        // }

        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (fvirtualcointype == null || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal
                || !fvirtualcointype.isFIsWithDraw()) {
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", "该币种不存在");
            return jsonObject.toString();
        }

        String ip = getIpAddr(request);
        JayunSecurity security = new JayunSecurity(request);
        if ("0".equals(phoneCode) && !security.isSecurity(fuser.getFloginName())) {
            jsonObject.accumulate("code", -50);
            jsonObject.accumulate("msg", security.getMessage());
            return jsonObject.toString();
        }
        if (fuser.isFisTelephoneBind() && !"0".equals(phoneCode)) {
            boolean bool = false;
            if (ShortMessageService.isTrue()) {
                bool = ShortMessageService.CheckCode(fuser.getFtelephone(), phoneCode, new Date());
            } else {
                bool = security.checkVerify(fuser.getFloginName(), phoneCode);// 接入聚安校验短信验证码
            }
            int mobil_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
            if (mobil_limit <= 0) {
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else if (!bool) {
                // !validateMessageCode(fuser, fuser.getFareaCode(),
                // fuser.getFtelephone(),
                // MessageTypeEnum.VIRTUAL_WITHDRAW_ACCOUNT, phoneCode)
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", "手机验证码不正确,您还有" + mobil_limit + "次机会");
                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.TELEPHONE);
                return jsonObject.toString();
            } else if (mobil_limit < new Constant().ErrorCountLimit) {
                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.TELEPHONE);
            }
        }

//        if (fuser.getFgoogleBind() && fuser.isFgoogleCheck()) {
//            int google_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
//            if (google_limit <= 0) {
//                jsonObject.accumulate("code", -2);
//                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
//                return jsonObject.toString();
//            } else if (!GoogleAuth.auth(Long.parseLong(totpCode.trim()), fuser.getFgoogleAuthenticator())) {
//                jsonObject.accumulate("code", -2);
//                jsonObject.accumulate("msg", "GOOGLE验证码不正确,您还有" + google_limit + "次机会");
//                this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
//                return jsonObject.toString();
//            } else if (google_limit < new Constant().ErrorCountLimit) {
//                this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.GOOGLE);
//            }
//        }

        FvirtualaddressWithdraw fvirtualaddressWithdraw = new FvirtualaddressWithdraw();
        fvirtualaddressWithdraw.setFadderess(withdrawAddr);
        fvirtualaddressWithdraw.setFcreateTime(Utils.getTimestamp());
        fvirtualaddressWithdraw.setFremark(withdrawRemark);
        fvirtualaddressWithdraw.setFuser(fuser);
        fvirtualaddressWithdraw.setFvirtualcointype(fvirtualcointype);
        try {
            this.frontVirtualCoinService.updateFvirtualaddressWithdraw(fvirtualaddressWithdraw);
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -4);
            jsonObject.accumulate("msg", "网络异常");
        } finally {
            this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.VIRTUAL_WITHDRAW_ACCOUNT);
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/user/validateIdentity", produces = { JsonEncode })
    public String validateIdentity(HttpServletRequest request, @RequestParam(required = true) String identityNo,
            @RequestParam(required = false, defaultValue = "0") int identityType,
            @RequestParam(required = true) String realName) throws Exception {
        JSONObject js = new JSONObject();
        realName = HtmlUtils.htmlEscape(realName);
        realName = StringEscapeUtils.unescapeHtml(realName);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        identityNo = identityNo.toLowerCase();
        JSONObject jsonObject = new JSONObject();

        String sql = "where fidentityNo='" + identityNo + "'";
        int count = this.adminService.getAllCount("Fuser", sql);
        if (count > 0) {
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", "身份证号码已存在!");
            return jsonObject.toString();
        }
        if (!"admin".equals(fuser.getFrealName())) {
            if (fuser.getFpostRealValidate()) {
                jsonObject.accumulate("code", 1);
                jsonObject.accumulate("msg", "请勿重复提交!");
                return jsonObject.toString();
            }
        }
        /*if (ShortMessageService.isTrue()) {
            IDCard idCard = new IDCard();
            com.alibaba.fastjson.JSONObject result = idCard.check(realName, identityNo);
            if (result.getIntValue("code") == -1) {
                jsonObject.accumulate("code", 1);
                jsonObject.accumulate("msg", result.getString("msg"));
                return jsonObject.toString();
            }
        } else {
            JayunVerify verifyUtil = new JayunVerify(request);
            if (!verifyUtil.idCard(fuser.getFloginName(), realName, identityNo)) {
                jsonObject.accumulate("code", 1);
                jsonObject.accumulate("msg", verifyUtil.getMessage());
                return jsonObject.toString();
            }
        }*/

        // Fscore fscore = fuser.getFscore();
        // Fuser fintrolUser = null;
        // Fintrolinfo introlInfo = null;
        // Fvirtualwallet fvirtualwallet = null;
        // String[] auditSendCoin =
        // this.systemArgsService.getValue("auditSendCoin").split("#");
        // int coinID = Integer.parseInt(auditSendCoin[0]);
        // double coinQty = Double.valueOf(auditSendCoin[1]);
        // if (!fscore.isFissend() && fuser.getfIntroUser_id() != null) {
        // fintrolUser =
        // this.frontUserService.findById(fuser.getfIntroUser_id().getFid());
        // fintrolUser.setfInvalidateIntroCount(fintrolUser.getfInvalidateIntroCount()
        // + 1);
        // fscore.setFissend(true);
        // }
        // if (coinQty > 0) {
        // fvirtualwallet =
        // this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
        // coinID);
        // fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + coinQty);
        // introlInfo = new Fintrolinfo();
        // introlInfo.setFcreatetime(Utils.getTimestamp());
        // introlInfo.setFiscny(false);
        // introlInfo.setFqty(coinQty);
        // introlInfo.setFuser(fuser);
        // introlInfo.setFname(fvirtualwallet.getFvirtualcointype().getFname());
        // introlInfo.setFtitle("实名认证成功，奖励" +
        // fvirtualwallet.getFvirtualcointype().getFname() + coinQty + "个！");
        // }
        fuser.setFidentityType(identityType);
        fuser.setFidentityNo(identityNo);
        fuser.setFrealName(realName);
        fuser.setFpostRealValidate(true);
        fuser.setFpostRealValidateTime(Utils.getTimestamp());
        fuser.setFhasRealValidate(true);
        fuser.setFhasRealValidateTime(Utils.getTimestamp());
        fuser.setFisValid(true);
        fuser.setAuthGrade(1);
        try {
            String ip = getIpAddr(request);
            this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_CERT, ip);
            this.userService.updateObj(fuser);
            this.SetSession(fuser, request);
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", 1);
            js.accumulate("msg", "证件号码已存在");
            return js.toString();
        }
        js.accumulate("msg", "身份证号认证成功");
        js.accumulate("code", 0);
        return js.toString();
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/user/api/validateIdentityx", produces = { JsonEncode })
    public String apivalidateIdentity(HttpServletRequest request, @RequestParam(required = true) String identityNo,
            @RequestParam(required = true) String realName, @RequestParam(required = true) String sign,
            @RequestParam(required = true) String identityUrlOn, @RequestParam(required = true) String identityUrlOff,
            @RequestParam(required = true) String identityHoldUrl)
            throws Exception {
        JSONObject js = new JSONObject();
        realName = HtmlUtils.htmlEscape(realName);
        realName = StringEscapeUtils.unescapeHtml(realName);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        identityNo = identityNo.toLowerCase();
        JSONObject jsonObject = new JSONObject();

        String sql = "where fidentityNo='" + identityNo + "'";
        int count = this.adminService.getAllCount("Fuser", sql);
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("identityNo", identityNo);
        map.put("realName", realName);
        String sort = KeyUtil.sort(map);
        if (!sort.equals(sign)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "接口认证失败！");
            return jsonObject.toString();
        }
        if (count > 0) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "身份证号码已存在!");
            return jsonObject.toString();
        }
        if (!"admin".equals(fuser.getFrealName())) {
            if (fuser.getFpostRealValidate()) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "请勿重复提交!");
                return jsonObject.toString();
            }
        }
        if (fuser.isFpostBankValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "您已上传，请耐心等待审核");
            return js.toString();
        }
        if (fuser.isFhasBankValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "已审核，无须重复上传");
            return js.toString();
        }
        // JayunVerify verifyUtil = new JayunVerify(request);
        // if (!verifyUtil.idCard(fuser.getFloginName(), realName, identityNo)) {
        // jsonObject.accumulate("code", 1);
        // jsonObject.accumulate("msg", verifyUtil.getMessage());
        // return jsonObject.toString();
        // }

        identityUrlOn = HtmlUtils.htmlEscape(identityUrlOn);
        identityUrlOff = HtmlUtils.htmlEscape(identityUrlOff);
        identityHoldUrl = HtmlUtils.htmlEscape(identityHoldUrl);
        fuser.setfpostBankValidate(true);
        fuser.setFpostImgValidateTime(Utils.getTimestamp());
        fuser.setfBankPath(identityUrlOn);
        fuser.setfBankPath2(identityUrlOff);
        fuser.setfBankPath3(identityHoldUrl);
        fuser.setFidentityType(0);
        fuser.setFidentityNo(identityNo);
        fuser.setFrealName(realName);
        fuser.setFpostRealValidate(true);
        fuser.setFpostRealValidateTime(Utils.getTimestamp());
        fuser.setFhasRealValidate(true);
        fuser.setFhasRealValidateTime(Utils.getTimestamp());
        fuser.setFisValid(true);
        fuser.setAuthGrade(3);
        try(Mysql mysql = new Mysql()) {
            String ip = getIpAddr(request);
            this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_CERT, ip);
            this.userService.updateObj(fuser);
            this.SetSession(fuser, request);
            MyQuery img = new MyQuery(mysql);
            img.add("select * from %s ", "");
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", -1);
            js.accumulate("msg", "网络异常！");
            return js.toString();
        }
        js.accumulate("msg", "身份证号认证成功");
        js.accumulate("code", 0);
        return js.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/common/upload", produces = { "text/html;charset=UTF-8" })
    public String upload(HttpServletRequest request) throws Exception {

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = mRequest.getFile("file");
        CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
        FileItem fileItem = cf.getFileItem();

        String realName = multipartFile.getOriginalFilename();
        JSONObject resultJson = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(mRequest).getFid());
        if (realName != null && realName.trim().toLowerCase().endsWith("jsp")) {
            return "xx";
        }
        if (fuser.isFpostImgValidate() && fuser.isFpostBankValidate()) {
            return "xxx";
        }

        if (fuser.isFhasImgValidate() && fuser.isFhasBankValidate()) {
            return "xxx";
        }

        double size = multipartFile.getSize() / 1000d;
        if (size > (3 * 1024d)) {
            resultJson.accumulate("code", "1");
            resultJson.accumulate("msg", "上传图片不应超过3M");
            return resultJson.toString();
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
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp") || fileName.endsWith(".jpeg")) { // 图片上传
                    if (fileName.endsWith(".jpg"))
                        fileUName += ".jpg";
                    if (fileName.endsWith("png"))
                        fileUName += ".png";
                    if (fileName.endsWith(".bmp"))
                        fileUName += ".bmp";
                    if (fileName.endsWith(".jpeg"))
                        fileUName += ".jpeg";
                    oss.upload("vcoin" + "/common/upload/" + fileUName, fileItem.getInputStream());
                    address = config.getProperty("oss.site") + "/" + "vcoin" + "/common/upload/" + fileUName;
                } else {
                    resultJson.accumulate("code", "1");
                    resultJson.accumulate("msg", "上传图片格式不正确");
                    return resultJson.toString();
                }
            }
        }

        resultJson.accumulate("code", 0);
        resultJson.accumulate("resultUrl", address);

        return resultJson.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/user/validateKyc", produces = { JsonEncode })
    public String validateKyc(HttpServletRequest request, @RequestParam(required = true) String identityUrlOn,
            @RequestParam(required = true) String identityUrlOff, @RequestParam(required = true) String identityHoldUrl)
            throws Exception {
        JSONObject js = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        if (fuser.isFpostImgValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "您已上传，请耐心等待审核");
            return js.toString();
        }
        if (fuser.isFhasImgValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "已审核，无须重复上传");
            return js.toString();
        }
        identityUrlOn = HtmlUtils.htmlEscape(identityUrlOn);
        identityUrlOff = HtmlUtils.htmlEscape(identityUrlOff);
        identityHoldUrl = HtmlUtils.htmlEscape(identityHoldUrl);
        fuser.setFpostImgValidate(true);
        fuser.setFpostImgValidateTime(Utils.getTimestamp());

        fuser.setfIdentityPath(Utils.subOssUrl(identityUrlOn));
        fuser.setfIdentityPath2(Utils.subOssUrl(identityUrlOff));
        fuser.setfIdentityPath3(Utils.subOssUrl(identityHoldUrl));

        fuser.setAuthGrade(2);
        fuser.setFaudit(2);

        try {
            this.frontUserService.updateFuser(fuser);
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", -1);
            js.accumulate("msg", "网络异常，请重新上传");
            return js.toString();
        }
        js.accumulate("msg", "上传成功，请耐心等待审核");
        js.accumulate("code", 0);
        return js.toString();
    }

    @RequestMapping(value = "/json/userAsset")
    public ModelAndView userAsset(HttpServletRequest request) {
        if (GetCurrentUser(request) == null)
            return null;// 用户没登录不需执行以下内容
        JspPage modelAndView = new JspPage(request);
        // 用户钱包
        Mysql handle = new Mysql();
        MyQuery ds = new MyQuery(handle);
        ds.add("select * from  %s", AppDB.Fuser);
        ds.add(" where fId = %d", getUserId());
        ds.open();
        if (ds.eof()) {
            removeAdminSession(request);
            CleanSession(request);
            RemoveSecondLoginSession(request);
            modelAndView.setJspFile("front/user/sub_user_login");
            return modelAndView;
        }
        Fvirtualwallet fwallet = this.frontUserService.findWalletByUser(GetCurrentUser(request).getFid());
        request.getSession().setAttribute("fwallet", fwallet);
        // 虚拟钱包
        Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService
                .findVirtualWallet(GetCurrentUser(request).getFid());
        request.getSession().setAttribute("fvirtualwallets", fvirtualwallets);
        // 估计总资产
        // CNY+冻结CNY+（币种+冻结币种）*最高买价
        double totalCapital = 0F;
        totalCapital += fwallet.getFtotal() + fwallet.getFfrozen();
        Map<Integer, Integer> tradeMappings = (Map) this.constantMap.get("tradeMappings");
        for (Map.Entry<Integer, Fvirtualwallet> entry : fvirtualwallets.entrySet()) {
            if (tradeMappings.containsKey(entry.getValue().getFvirtualcointype().getFid()) == false)
                continue;

            double price = (Double) this.redisUtil.get(RedisConstant
                    .getLatestDealPrizeKey(tradeMappings.get(entry.getValue().getFvirtualcointype().getFid())));
            totalCapital += (entry.getValue().getFfrozen() + entry.getValue().getFtotal()) * price;
        }

        request.getSession().setAttribute("totalNet", Utils.getDouble(totalCapital, 2));
        request.getSession().setAttribute("totalCapital", Utils.getDouble(totalCapital, 2));

        request.getSession().setAttribute("totalNetTrade", Utils.getDouble(totalCapital, 2));
        request.getSession().setAttribute("totalCapitalTrade", Utils.getDouble(totalCapital, 2));

        modelAndView.setJspFile("front/comm/asset");
        return modelAndView;
    }

    private static void uploadFile(InputStream inputStream, String realPath) {
        // 获取文件
        File file = new File(realPath);
        OutputStream out = null;
        try {
            int len = 0;
            byte[] buf = new byte[1024];
            // 获取输出流
            out = new FileOutputStream(file);
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e2) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/user/validateIdentity1", produces = { JsonEncode })
    public String validateIdentity1(HttpServletRequest request, @RequestParam(required = true) String identityNo,
            @RequestParam(required = false, defaultValue = "0") int identityType,
            @RequestParam(required = true) String realName) throws Exception {
        JSONObject js = new JSONObject();
        realName = HtmlUtils.htmlEscape(realName);
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        identityNo = identityNo.toLowerCase();
        String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        JSONObject jsonObject = new JSONObject();
        if (identityNo.trim().length() != 15 && identityNo.trim().length() != 18) {
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", "身份证号码长度应该为15位或18位!");
            return jsonObject.toString();
        }

        String Ai = "";
        if (identityNo.length() == 18) {
            Ai = identityNo.substring(0, 17);
        } else if (identityNo.length() == 15) {
            Ai = identityNo.substring(0, 6) + "19" + identityNo.substring(6, 15);
        }
        if (Utils.isNumeric(Ai) == false) {
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", "身份证号码有误!");
            return jsonObject.toString();
        }
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (Utils.isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", "身份证号码有误!");
            return jsonObject.toString();
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                jsonObject.accumulate("code", 1);
                jsonObject.accumulate("msg", "身份证号码有误!");
                return jsonObject.toString();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", "身份证号码有误!");
            return jsonObject.toString();
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", "身份证号码有误!");
            return jsonObject.toString();
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = Utils.getAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", "身份证号码有误!");
            return jsonObject.toString();
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (identityNo.length() == 18) {
            if (Ai.equals(identityNo) == false) {
                jsonObject.accumulate("code", 1);
                jsonObject.accumulate("msg", "身份证号码有误!");
                return jsonObject.toString();
            }
        } else {
            return "";
        }

        if (realName.trim().length() > 50) {
            jsonObject.accumulate("code", 1);
            jsonObject.accumulate("msg", "真实姓名不合法!");
            return jsonObject.toString();
        }

        // String sql = "where fidentityNo='" + identityNo + "'";
        // int count = this.adminService.getAllCount("Fuser", sql);
        // if (count > 0) {
        // jsonObject.accumulate("code", 1);
        // jsonObject.accumulate("msg", "身份证号码已存在!");
        // return jsonObject.toString();
        // }
        /*
         * if (fuser.getFpostRealValidate()) { jsonObject.accumulate("code", 1); jsonObject.accumulate("msg",
         * "请勿重复提交!"); return jsonObject.toString(); }
         */
        // IDCardVerifyUtil verifyUtil = new IDCardVerifyUtil();
        // boolean isTrue = verifyUtil.isRealPerson(realName, identityNo);
        // if (!isTrue) {
        // jsonObject.accumulate("code", 1);
        // jsonObject.accumulate("msg", "您的姓名与身份证号有误，请核对!");
        // return jsonObject.toString();
        // }

        // Fscore fscore = fuser.getFscore();
        // Fuser fintrolUser = null;
        // Fintrolinfo introlInfo = null;
        // Fvirtualwallet fvirtualwallet = null;
        // String[] auditSendCoin =
        // this.systemArgsService.getValue("auditSendCoin").split("#");
        // int coinID = Integer.parseInt(auditSendCoin[0]);
        // double coinQty = Double.valueOf(auditSendCoin[1]);
        // if (!fscore.isFissend() && fuser.getfIntroUser_id() != null) {
        // fintrolUser =
        // this.frontUserService.findById(fuser.getfIntroUser_id().getFid());
        // fintrolUser.setfInvalidateIntroCount(fintrolUser.getfInvalidateIntroCount()
        // + 1);
        // fscore.setFissend(true);
        // }
        // if (coinQty > 0) {
        // fvirtualwallet =
        // this.frontUserService.findVirtualWalletByUser(fuser.getFid(),
        // coinID);
        // fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + coinQty);
        // introlInfo = new Fintrolinfo();
        // introlInfo.setFcreatetime(Utils.getTimestamp());
        // introlInfo.setFiscny(false);
        // introlInfo.setFqty(coinQty);
        // introlInfo.setFuser(fuser);
        // introlInfo.setFname(fvirtualwallet.getFvirtualcointype().getFname());
        // introlInfo.setFtitle("实名认证成功，奖励" +
        // fvirtualwallet.getFvirtualcointype().getFname() + coinQty + "个！");
        // }
        fuser.setFidentityType(0);
        fuser.setFidentityNo(identityNo);
        fuser.setFrealName(realName);
        fuser.setAuthGrade(0);
        try {
            String ip = getIpAddr(request);
            this.frontUserService.updateFUser(fuser, getSession(request), LogTypeEnum.User_CERT, ip);
            this.userService.updateObj(fuser);
            this.SetSession(fuser, request);
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", 1);
            js.accumulate("msg", "证件号码已存在");
            return js.toString();
        }
        js.accumulate("code", 0);
        return js.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/user/validateKyc1", produces = { JsonEncode })
    public String validateKyc1(HttpServletRequest request, @RequestParam(required = true) String identityUrlOn,
            @RequestParam(required = true) String identityUrlOff, @RequestParam(required = true) String identityHoldUrl)
            throws Exception {
        JSONObject js = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        if (fuser.isFpostBankValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "您已上传，请耐心等待审核");
            return js.toString();
        }
        if (fuser.isFhasBankValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "已审核，无须重复上传");
            return js.toString();
        }
        identityUrlOn = HtmlUtils.htmlEscape(identityUrlOn);
        identityUrlOff = HtmlUtils.htmlEscape(identityUrlOff);
        identityHoldUrl = HtmlUtils.htmlEscape(identityHoldUrl);
        fuser.setfpostBankValidate(true);
        fuser.setFpostImgValidateTime(Utils.getTimestamp());
        fuser.setfBankPath(identityUrlOn);
        fuser.setfBankPath2(identityUrlOff);
        fuser.setfBankPath3(identityHoldUrl);
        fuser.setAuthGrade(3);
        try {
            this.frontUserService.updateFuser(fuser);
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", -1);
            js.accumulate("msg", "网络异常，请重新上传");
            return js.toString();
        }
        js.accumulate("msg", "上传成功，请耐心等待审核");
        js.accumulate("code", 0);
        return js.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/m/introl/index" }, produces = JsonEncode)
    public String introReg(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int random,
            @RequestParam(required = false, defaultValue = "0") String regName,
            @RequestParam(required = true, defaultValue = "0") String phoneCode,
            @RequestParam(required = true, defaultValue = "0") String password,
                           String recommend) throws Exception {
        String areaCode = "86";

        JSONObject jsonObject = new JSONObject();

        if(recommend.substring(0,2).equals("HL")){
            jsonObject.accumulate("code", -888);
            jsonObject.accumulate("msg", "暂停注册");
            return jsonObject.toString();
        }

        String phone = HtmlUtils.htmlEscape(regName);
        phoneCode = HtmlUtils.htmlEscape(phoneCode);
        String isOpenReg = constantMap.get("isOpenReg").toString().trim();
        if (!isOpenReg.equals("1")) {
            jsonObject.accumulate("code", -888);
            jsonObject.accumulate("msg", "暂停注册");
            return jsonObject.toString();
        }
        if(org.apache.commons.lang3.StringUtils.isBlank(password)){
            password = phone.substring(phone.length() - 6, phone.length());
        }
        // 手机注册
        boolean flag1 = this.frontUserService.isLoginNameExists(regName);// 注册检测loginName是否存在
        if (flag1) {
            jsonObject.accumulate("code", -22);
            jsonObject.accumulate("msg", "手机号码已经被注册");
            return jsonObject.toString();
        }

        if (!phone.matches(Constant.PhoneReg)) {
            jsonObject.accumulate("code", -22);
            jsonObject.accumulate("msg", "手机格式错误");
            return jsonObject.toString();
        }
        JayunMessage message = new JayunMessage(request);
        boolean bool = message.checkRegister(phone, phoneCode);// 检测聚安发送的验证码

        if (!bool) {
            jsonObject.accumulate("code", -20);
            jsonObject.accumulate("msg", "短信验证码错误");
            return jsonObject.toString();
        }

        // 推广
        Fuser intro = null;
        String intro_user = request.getParameter("intro_user");

        try {
            if (intro_user != null && !"".equals(intro_user.trim())) {
                intro = this.frontUserService.findById(Integer.parseInt(intro_user.trim()));
            } else {
                jsonObject.accumulate("code", -200);
                jsonObject.accumulate("msg", "邀请码错误");
                return jsonObject.toString();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (intro == null) {
            jsonObject.accumulate("code", -200);
            jsonObject.accumulate("msg", "邀请码错误");
            return jsonObject.toString();
        }

        Fuser fuser = new Fuser();
        fuser.setfIntroUser_id(intro);
        fuser.setFintrolUserNo(null);

        if (intro != null && intro.getFintrolUserNo() != null && intro.getFintrolUserNo().trim().length() > 0) {
            fuser.setFintrolUserNo(intro.getFintrolUserNo());
        } else if (intro != null && intro.getFuserNo() != null && intro.getFuserNo().trim().length() > 0) {
            fuser.setFintrolUserNo(intro.getFuserNo());
        }

        // 手机注册  hqs
        fuser.setFregType(RegTypeEnum.TEL_VALUE);
        fuser.setFtelephone(phone);
        fuser.setFareaCode(areaCode);
        fuser.setFisTelephoneBind(true);
        fuser.setFnickName(phone);
        fuser.setFloginName(phone);
        fuser.setFisTelephoneBind(true);
        fuser.setSalt(Utils.getUUID());
        fuser.setFregisterTime(Utils.getTimestamp());
        fuser.setFloginPassword(MyMD5Utils.encoding(password));
        fuser.setFtradePassword(null);
        String ip = getIpAddr(request);
        fuser.setFregIp(ip);
        fuser.setFlastLoginIp(ip);
        fuser.setFregType(RegTypeEnum.TEL_VALUE);
        fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
        fuser.setFlastLoginTime(Utils.getTimestamp());
        fuser.setFlastUpdateTime(Utils.getTimestamp());
        fuser.setFleaderId(recommend == null ? 0 : Integer.parseInt(recommend.substring(2)));
        boolean saveFlag = false;
        try {
            saveFlag = this.frontUserService.saveRegister(fuser);
            Mysql mysql = new Mysql();
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT fId,floginName,fleaderId from fuser WHERE fTelephone = '%s' ", phone);
            ds.open();
            String loginName = "RY" + ds.getInt("fId");
            boolean saveFlag1 = settlementService.register(loginName, recommend == null ? "RY001" : recommend , phone);
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
            e.printStackTrace();
        } finally {
            String key1 = ip + "message_" + MessageTypeEnum.REG_CODE;
            String key2 = ip + "_" + phone + "_" + MessageTypeEnum.REG_CODE;
            this.validateMap.removeMessageMap(key1);
            this.validateMap.removeMessageMap(key2);
        }

        if (saveFlag) {
            // 奖励糖果
            String[] auditSendCoin = this.systemArgsService.getValue("auditSendCoin").split("#");
            int coinID = Integer.parseInt(auditSendCoin[0]);
            double coinQty = Double.valueOf(auditSendCoin[1]);
//            if (coinQty > 0) {
//                Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), coinID);
//                Fscore fscore = fuser.getFscore();
//                fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + coinQty);
//                this.userService.updateUser(fvirtualwallet, fscore);
//            }
            Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(coinID);
            jsonObject.accumulate("code", 0);
//            jsonObject.accumulate("msg", coinQty + fvirtualcointype.getFname() + "<br/>请安装app查看<br/>初始密码为手机号后6位");
            jsonObject.accumulate("msg", "<br/>请安装app查看"+ (phone.substring(phone.length() - 6, phone.length()).equals(password) ? "<br/>初始密码为手机号后6位":"") );
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", -10);
            jsonObject.accumulate("msg", "网络错误，请稍后再试");
            return jsonObject.toString();
        }
    }

    @ResponseBody
    @RequestMapping(value = { "/user/validateKyc3", "/m/user/validateKyc3" }, produces = { JsonEncode })
    public String validateKyc3(HttpServletRequest request) throws Exception {
        JSONObject js = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        if (fuser.isFpostImgValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "您已上传，请耐心等待审核");
            return js.toString();
        }
        if (fuser.isFhasImgValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "已审核，无须重复上传");
            return js.toString();
        }
        String uri = request.getRequestURI();
        if (!uri.startsWith("/m/")) {
            fuser.setAuthGrade(1);
        }
        fuser.setFaudit(0);

        try {
            this.frontUserService.updateFuser(fuser);
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", -1);
            js.accumulate("msg", "网络异常，请重新上传");
            return js.toString();
        }
        js.accumulate("msg", "上传成功，请耐心等待审核");
        js.accumulate("code", 0);
        return js.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/user/validateKyc4", "/m/user/validateKyc4" }, produces = { JsonEncode })
    public String validateKyc4(HttpServletRequest request) throws Exception {
        JSONObject js = new JSONObject();
        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        String uri = request.getRequestURI();
        if (!uri.startsWith("/m/")) {
            fuser.setAuthGrade(2);
        }
        fuser.setFaudit(1);
        try {
            this.frontUserService.updateFuser(fuser);
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", -1);
            js.accumulate("msg", "网络异常，请重新上传");
            return js.toString();
        }
        js.accumulate("msg", "上传成功，请耐心等待审核");
        js.accumulate("code", 0);
        return js.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/common/imgUpload", produces = { "text/html;charset=UTF-8" })
    public String imgUpload(HttpServletRequest request) throws Exception {

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = mRequest.getFile("file");
        CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
        FileItem fileItem = cf.getFileItem();

        String realName = multipartFile.getOriginalFilename();
        JSONObject resultJson = new JSONObject();
        if (realName != null && realName.trim().toLowerCase().endsWith("jsp")) {
            return "xx";
        }

        double size = multipartFile.getSize() / 1000d;
        if (size > (3 * 1024d)) {
            resultJson.accumulate("code", "1");
            resultJson.accumulate("msg", "上传图片不应超过3M");
            return resultJson.toString();
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

        resultJson.accumulate("code", 0);
        resultJson.accumulate("resultUrl", address);

        return resultJson.toString();
    }

    /*
     * youyouAPP 同步用户注册信息到 coa项目里
     */
    @ResponseBody
    @RequestMapping(value = { "/user/synUsers" }, produces = { JsonEncode })
    public String synUsers(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") String password, // 密码
            @RequestParam(required = false, defaultValue = "0") String regName, // 账号
            @RequestParam(required = false, defaultValue = "0") int regType, // 0手机、1邮箱
            @RequestParam(required = true, defaultValue = "0") String fleaderId, // 父级id
            @RequestParam(required = true, defaultValue = "0") String publicKey) // 公钥
            throws Exception {
        JSONObject jsonObject = new JSONObject();

        // 先验证用户是否存在
        if (regType == 0) {
            // 手机账号
            boolean flag = this.frontUserService.isLoginNameExists(regName);// 修改为注册时检测loginName是否存在
            if (flag) {
                jsonObject.accumulate("code", 500);
                jsonObject.accumulate("msg", "手机号码错误或已存在");
                return jsonObject.toString();
            }
        } else {
            // 邮箱账号
            boolean flag = this.frontUserService.isEmailExists(regName);
            if (flag) {
                jsonObject.accumulate("code", 500);
                jsonObject.accumulate("msg", "邮箱错误或已存在");
                return jsonObject.toString();
            }
        }
        // 公钥
        String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0ZwomOC2j1Tzn8HIgUIW9w7NGawE0iQ3tx+Brd8p+YRO60VdU8m5009I169zROpxmi4OC56axjOKI6uDL+Wob9amJFn2dj4iWHUC+DLrcE3mA4goIvqZAV7JOKIWOqcY9BBR19ZOYmDc5wdqErB3+OsdfKiKdfOVGcDNTA5UQ8QIDAQAB";

        // 判断公钥
        if (null == publicKey || "".equals(publicKey)) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "公钥不允许为空!");
            return jsonObject.toString();
        }
        if (!PUBLICKEY.equals(publicKey)) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "公钥不匹配!");
            return jsonObject.toString();
        }

        // 给用户表添加数据
        Fuser fuser = new Fuser();
        String phone = HtmlUtils.htmlEscape(regName);
        String areaCode = "86";

        if (regType == 0) {
            // Pattern p =
            // Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            // Matcher m = p.matcher(phone);
            // if (!m.matches()) {
            // jsonObject.accumulate("code", 500);
            // jsonObject.accumulate("msg", "手机格式有误!");
            // return jsonObject.toString();
            // }
            // 手机注册
            fuser.setFregType(RegTypeEnum.TEL_VALUE);
            fuser.setFtelephone(phone);
            fuser.setFareaCode(areaCode);
            fuser.setFisTelephoneBind(true);
            fuser.setFnickName(phone);
            fuser.setFloginName(phone);
        } else {
            // String RULE_EMAIL =
            // "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
            // // 正则表达式的模式 编译正则表达式
            // Pattern p = Pattern.compile(RULE_EMAIL);
            // // 正则表达式的匹配器
            // Matcher m = p.matcher(regName);
            // if (!m.matches()) {
            // jsonObject.accumulate("code", 500);
            // jsonObject.accumulate("msg", "邮箱格式有误!");
            // return jsonObject.toString();
            // }
            fuser.setFregType(RegTypeEnum.EMAIL_VALUE);
            fuser.setFemail(regName);
            fuser.setFisMailValidate(true);
            fuser.setFnickName(regName.split("@")[0]);
            fuser.setFloginName(regName);
        }

        fuser.setSalt(Utils.getUUID());
        fuser.setFregisterTime(Utils.getTimestamp());
        fuser.setFloginPassword(password);
        fuser.setFtradePassword(null);
        fuser.setFregIp(getIpAddr(request));
        fuser.setFlastLoginIp(getIpAddr(request));
        fuser.setFcoaleaderId(fleaderId);
        fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
        fuser.setFlastLoginTime(Utils.getTimestamp());
        fuser.setFlastUpdateTime(Utils.getTimestamp());
        fuser.setfInsertData(1);// 插入数据
        boolean saveFlag = false;
        try {// 执行save操作
            saveFlag = this.frontUserService.saveRegister(fuser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (saveFlag) {
            jsonObject.accumulate("code", 200);
            jsonObject.accumulate("msg", "注册成功");
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "网络错误，请稍后再试");
            return jsonObject.toString();
        }
    }

    /**
     * 
     * @param username
     *            账号信息，同手机号
     * @param realname
     *            真实姓名
     * @param phone
     *            手机号
     * @param idType
     *            证件类型 身份证 军官证 护照 香港居民证件
     * @param idCardNo
     *            证件号
     * @param authTime
     *            认证时间
     * @param passTime
     *            通过时间
     * @param idCardFrontUrl
     *            证件照正面
     * @param idCardBackUrl
     *            证件照反面
     * @param idCardHandInUrl
     *            证件手持照片
     * @param status
     *            认证结果 认证中 已认证 已撤销
     * @param remark
     *            备注
     */
    // 同步C1接口数据
    public static boolean authInfo(String username, String realname, String phone, String idType, String idCardNo,
            String authTime, String passTime, String idCardFrontUrl, String idCardBackUrl, String idCardHandInUrl,
            String status, String remark) {
        String code = "";
        String msg = "";
        String url = Constant.authInfo;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("realname", realname);
        params.put("phone", phone);
        params.put("idType", idType);
        params.put("idCardNo", idCardNo);
        params.put("authTime", authTime);
        params.put("passTime", passTime);
        params.put("idCardFrontUrl", idCardFrontUrl);
        params.put("idCardBackUrl", idCardBackUrl);
        params.put("idCardHandInUrl", idCardHandInUrl);
        params.put("status", status);
        params.put("remark", remark);
        params.put("time", authTime);
        params.put("sign",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6p0XWjscY+gsyqKRhw9MeLsEmhFdBRhT2emOck/F1Omw38ZWhJxh9kDfs5HzFJMrVozgU+SJFDONxs8UB0wMILKRmqfLcfClG9MyCNuJkkfm0HFQv1hRGdOvZPXj3Bckuwa7FrEXBRYUhK7vJ40afumspthmse6bs6mZxNn/mALZ2X07uznOrrc2rk41Y2HftduxZw6T4EmtWuN2x4CZ8gwSyPAW5ZzZJLQ6tZDojBK4GZTAGhnn3bg5bBsBlw2+FLkCQBuDsJVsFPiGh/b6K/");

        String resultStr = HttpUtils.post(url, params);
        System.out.println("返回结果是：" + resultStr);
        if (FrontUserJsonController.notEmpty(resultStr) && resultStr.contains("code")) {
            JSONObject resultJson = JSONObject.fromObject(resultStr);
            code = resultJson.getString("code");
            msg = resultJson.getString("msg");
        }
        if (code.equals(200)) {
            return true;
        } else {
            return false;
        }
    }

    // 同步用户接口
    public static boolean userRegister1(String username, String password, String parent_username, Long time,
            String phone) throws IOException {
        String code = "";
        String msg = "";
        String url = Constant.SettlementIP + "/api/settlement/synUserNexus" ;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tid", username);
        params.put("recommendId", parent_username);
        String securityKey = com.huagu.vcoin.main.controller.front.SecurityUtils.getSecurityKey(params,
                FrontUserJsonController.securtyKey);
        params.put("securityKey", securityKey);
        /*
         * params.put("username", username); params.put("password", MD5.get(password));
         * params.put("parent_username", parent_username); params.put("time",
         * time.toString()); params.put("phone", phone); params.put("sign",
         * "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6p0XWjscY+gsyqKRhw9MeLsEmhFdBRhT2emOck/F1Omw38ZWhJxh9kDfs5HzFJMrVozgU+SJFDONxs8UB0wMILKRmqfLcfClG9MyCNuJkkfm0HFQv1hRGdOvZPXj3Bckuwa7FrEXBRYUhK7vJ40afumspthmse6bs6mZxNn/mALZ2X07uznOrrc2rk41Y2HftduxZw6T4EmtWuN2x4CZ8gwSyPAW5ZzZJLQ6tZDojBK4GZTAGhnn3bg5bBsBlw2+FLkCQBuDsJVsFPiGh/b6K/"
         * );
         */

        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        for (String s : params.keySet()) {
            json.put(s, params.get(s));
        }
        String resultStr = HttpClientUtils.doPost(url, json);
        System.out.println("返回结果是：" + resultStr);
        if (FrontUserJsonController.notEmpty(resultStr) && resultStr.contains("code")) {
            JSONObject resultJson = JSONObject.fromObject(resultStr);
            code = resultJson.getString("status");
            msg = resultJson.getString("msg");
        }
        if (code.equals(200)) {
            return true;
        } else {
            return false;
        }
    }

    // 判断不为空
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    /**
     * 批量导入数据库
     */
    @ResponseBody
    @RequestMapping(value = "/user/synUser_import", produces = { JsonEncode })
    public String saveMedicines(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        DataQuery data = null;
        long startTime = System.currentTimeMillis();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select * from fuser_import1 where fisDealed = 0");
            ds1.setMaximum(40000);
            data = ds1.open();
            // 循环用户临时表数据
            for (int i = 0; i < data.size(); i++) {
                Fuser fuser = new Fuser();
                // 登陆账号手机
                String phone = HtmlUtils.htmlEscape(data.getIndex(i).getString("floginName"));
                String areaCode = "86";
                fuser.setFregType(RegTypeEnum.TEL_VALUE);
                fuser.setFtelephone(data.getIndex(i).getString("fTelephone"));
                fuser.setFareaCode(areaCode);
                fuser.setFisTelephoneBind(true);
                fuser.setFnickName(data.getIndex(i).getString("fNickName"));
                fuser.setFrealName(data.getIndex(i).getString("fRealName"));
                fuser.setFloginName(phone);
                fuser.setFregisterTime(Utils.getTimestamp());
                fuser.setFloginPassword(data.getIndex(i).getString("fLoginPassword"));
                fuser.setFtradePassword(data.getIndex(i).getString("fTradePassword"));
                fuser.setFregisterTime(Timestamp.valueOf(data.getIndex(i).getString("fRegisterTime")));
                fuser.setFregIp(getIpAddr(request));
                fuser.setFlastLoginIp(getIpAddr(request));
                fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
                fuser.setFlastLoginTime(Utils.getTimestamp());
                fuser.setFlastUpdateTime(Utils.getTimestamp());
                fuser.setFcoaleaderId(data.getIndex(i).getString("fIntroUser_id"));
                fuser.setFolduId(data.getIndex(i).getString("folduid"));// 旧id
                fuser.setAuthGrade(data.getIndex(i).getInt("fauthGrade"));// 认证等级
                fuser.setfInsertData(1);// 插入数据
                try {// 执行save操作
                    this.frontUserService.saveRegister(fuser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int userFid = fuser.getFid();
                try (Mysql mysql1 = new Mysql()) {
                    MyQuery ds3 = new MyQuery(mysql1);
                    ds3.add("select * from fvirtualaddress");
                    ds3.setMaximum(1);
                    ds3.open();
                    if (null != data.getString("fcoinaddress1") && !"".equals(data.getString("fcoinaddress1"))) {
                        ds3.setField("fVi_fId", 1);
                        ds3.setField("fAdderess", data.getString("fcoinaddress1"));
                        ds3.setField("fuid", userFid);
                        ds3.setField("fCreateTime", TDate.Now());
                        ds3.setField("version", TDate.Now());
                        ds3.post();
                    }
                    if (null != data.getString("fcoinaddress2") && !"".equals(data.getString("fcoinaddress2"))) {
                        ds3.setField("fVi_fId", 2);
                        ds3.setField("fAdderess", data.getString("fcoinaddress2"));
                        ds3.setField("fuid", userFid);
                        ds3.setField("fCreateTime", TDate.Now());
                        ds3.setField("version", TDate.Now());
                        ds3.post();
                    }
                    if (null != data.getString("fcoinaddress3") && !"".equals(data.getString("fcoinaddress3"))) {
                        ds3.setField("fVi_fId", 3);
                        ds3.setField("fAdderess", data.getString("fcoinaddress3"));
                        ds3.setField("fuid", userFid);
                        ds3.setField("fCreateTime", TDate.Now());
                        ds3.setField("version", TDate.Now());
                        ds3.post();
                    }
                }
                // 修改当前查询记录的状态
                data.edit();
                data.setField("fisDealed", 1);
                data.post();
            }
        }
        // 执行方法
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println();
        jsonObject.accumulate("code", 500);
        jsonObject.accumulate("msg", "导入成功            " + "执行时间：" + excTime + "s");
        return jsonObject.toString();
    }

    /*
     * 转币
     */
    @ResponseBody
    @RequestMapping(value = { "/user/transferCoin" }, produces = { JsonEncode })
    public String transferCoin(HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") String username, // 用户loginname
            @RequestParam(required = false, defaultValue = "0") int coinFid, // 币种fid
            @RequestParam(required = true, defaultValue = "0") double count, // 数量
            @RequestParam(required = true, defaultValue = "0") String publicKey) // 公钥
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        // 公钥
        String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0ZwomOC2j1Tzn8HIgUIW9w7NGawE0iQ3tx+Brd8p+YRO60VdU8m5009I169zROpxmi4OC56axjOKI6uDL+Wob9amJFn2dj4iWHUC+DLrcE3mA4goIvqZAV7JOKIWOqcY9BBR19ZOYmDc5wdqErB3+OsdfKiKdfOVGcDNTA5UQ8QIDAQAB";

        // 判断公钥
        if (null == publicKey || "".equals(publicKey)) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "公钥不允许为空!");
            transferCoinLog(null, username, coinFid, count, publicKey, 500, "公钥不允许为空!", jsonObject.toString());
            return jsonObject.toString();
        }
        if (!PUBLICKEY.equals(publicKey)) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "公钥不匹配!");
            transferCoinLog(null, username, coinFid, count, publicKey, 500, "公钥不匹配!", jsonObject.toString());
            return jsonObject.toString();
        }
        // 先验证用户是否存在
        List<Fuser> fuserList = frontUserService.findUserByProperty("floginName", username);

        if (fuserList == null || fuserList.size() <= 0) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "该用户不存在");
            transferCoinLog(null, username, coinFid, count, publicKey, 500, "该用户不存在", jsonObject.toString());
            return jsonObject.toString();
        }
        Fuser fuser = fuserList.get(0);
        String fuserid = String.valueOf(fuser.getFid());
        // 验证币种信息

        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(coinFid);
        if (fvirtualcointype == null) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "该币种不存在");
            transferCoinLog(fuserid, username, coinFid, count, publicKey, 500, "该币种不存在", jsonObject.toString());
            return jsonObject.toString();
        }
        String fShortName = fvirtualcointype.getfShortName().toUpperCase();
        if (!(fShortName.equals("USDT") || fShortName.equals("AT"))) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "该币种暂不支持转移");
            transferCoinLog(fuserid, username, coinFid, count, publicKey, 500, "该币种暂不支持转移", jsonObject.toString());
            return jsonObject.toString();
        }

        // 给用户增加虚拟币数量
        Fvirtualwallet fvirtualwallet = fvirtualWalletService.findFvirtualwallet(fuser.getFid(), coinFid);
        if (fvirtualwallet == null) {
            Fvirtualwallet fvirtualwalletfix = new Fvirtualwallet();
            Timestamp flastUpdateTime = new Timestamp(new Date().getTime());
            // 修复钱包
            fvirtualwalletfix.setFvirtualcointype(fvirtualcointype);
            fvirtualwalletfix.setFtotal(0);
            fvirtualwalletfix.setFfrozen(0);
            fvirtualwalletfix.setFlastUpdateTime(flastUpdateTime);
            fvirtualwalletfix.setFuser(fuser);
            fvirtualwalletfix.setFborrowBtc(0);
            fvirtualwalletfix.setFcanlendBtc(0);
            fvirtualwalletfix.setFfrozenLendBtc(0);
            fvirtualwalletfix.setFalreadyLendBtc(0);
            fvirtualwalletfix.setVersion(0);
            fvirtualwalletfix.setfHaveAppointBorrowBtc(0);
            fvirtualwalletfix.setFcanSellQty(0);

            this.fvirtualWalletService.save(fvirtualwalletfix);
            try {
            } catch (Exception e) {
                jsonObject.accumulate("code", 500);
                jsonObject.accumulate("msg", "修复钱包失败!");
                transferCoinLog(fuserid, username, coinFid, count, publicKey, 500, "修复钱包失败!", jsonObject.toString());
                return jsonObject.toString();
            }
            fvirtualwallet = fvirtualWalletService.findFvirtualwallet(fuser.getFid(), coinFid);
        }

        fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + count);
        try {
            this.userService.updateObj(fuser, null, fvirtualwallet);
        } catch (Exception e) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "更新钱包失败!");
            transferCoinLog(fuserid, username, coinFid, count, publicKey, 200, "更新钱包失败!", jsonObject.toString());
            return jsonObject.toString();
        }

        // 添加财务日志信息
        Timestamp fcreateTime = new Timestamp(new Date().getTime());
        Fvirtualcaptualoperation fvirtualcaptualoperation = new Fvirtualcaptualoperation();
        fvirtualcaptualoperation.setFuser(fuser);
        fvirtualcaptualoperation.setFvirtualcointype(fvirtualcointype);
        fvirtualcaptualoperation.setFcreateTime(fcreateTime);
        fvirtualcaptualoperation.setFamount(count);
        fvirtualcaptualoperation.setFfees(0);
        fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN);
        fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS);
        fvirtualcaptualoperation.setFlastUpdateTime(fcreateTime);
        fvirtualcaptualoperation.setFremark("平台转账");
        try {
            frontVirtualCoinService.addFvirtualcaptualoperation(fvirtualcaptualoperation);
        } catch (Exception e) {
            jsonObject.accumulate("code", 500);
            jsonObject.accumulate("msg", "财务日志记录失败!");
            transferCoinLog(fuserid, username, coinFid, count, publicKey, 500, "财务日志记录失败!", jsonObject.toString());
            return jsonObject.toString();
        }

        jsonObject.accumulate("code", 200);
        jsonObject.accumulate("msg", "转移成功!");
        transferCoinLog(fuserid, username, coinFid, count, publicKey, 200, "转移成功!", jsonObject.toString());
        return jsonObject.toString();
    }

    /*
     * 转币日志
     */
    @SuppressWarnings("unused")
    private static void transferCoinLog(String fuserid, String username, int coinFid, double count, String publicKey,
            int code, String msg, String data) {
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s", "fvirtualcaptualoperationtoyylog");
            ds.open();
            ds.append();
            ds.setField("fuserid", fuserid);
            ds.setField("fcreatetime", TDateTime.Now());
            ds.setField("finput_username", username);
            ds.setField("finput_coinFid", coinFid);
            ds.setField("finput_count", count);
            ds.setField("finput_publicKey", publicKey);
            ds.setField("foutput_code", code);
            ds.setField("foutput_msg", msg);
            ds.setField("foutput_data", data);
            ds.post();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/sns/uploadFile", produces = "text/plain; charset=utf-8")
    public @ResponseBody String upload(@RequestParam(value = "file", required = false) MultipartFile file,
            HttpServletRequest request) {
        UploadFile upFile = new UploadFile();
        ServerConfig config = ServerConfig.getInstance();
        OssConnection ossCon = new OssConnection();
        ossCon.setConfig(config);
        OssSession oss = ossCon.getSession();
        String fileName = file.getOriginalFilename();
        String name = UUID.randomUUID().toString() + "---" + fileName;
        // File targetFile = new File(path, name);
        /*
         * if (targetFile.exists()) { targetFile.mkdirs(); }
         */
        try {
            // file.transferTo(targetFile);
            oss.upload("vcoin" + "/common/upload/" + name, file.getInputStream());
            upFile.setCode(0);
            UFile uf = new UFile();
            uf.setName(fileName);
            uf.setSrc(config.getProperty("oss.site") + "/" + "vcoin" + "/common/upload/" + name);
            upFile.setData(uf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(upFile);

    }

    @ResponseBody
    @RequestMapping(value = "api/trading/findUserById", produces = { JsonEncode })
    public String findUserById(HttpServletResponse resp, @RequestParam Map<String, Object> param) {
        JSONObject json = new JSONObject();
        boolean b = SecurityUtils.isCorrectRequest(req, resp, FrontUserJsonController.securtyKey);
        if (b) {
            try {
                Mysql mysql = new Mysql();
                MyQuery ds = new MyQuery(mysql);
                ds.add("SELECT fId from fuser WHERE floginName = '%s' or fTelephone = '%s' ", param.get("userId") + "",
                        param.get("userId") + "");
                ds.open();
                if (ds.size() < 1) {
                    json.accumulate("status", 300);
                    json.accumulate("msg", "未查找到用户");
                } else {
                    Fuser user = this.frontUserService.findById(ds.getInt("fId"));
                    if (user != null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("username", user.getFloginName());
                        map.put("certification", "未认证");
                        map.put("realName", user.getFrealName());
                        map.put("fIdentityNo", user.getFidentityNo());
                        if (user.getfIntroUser_id() != null) {
                            map.put("fIntroUser", user.getfIntroUser_id().getFloginName());
                        } else {
                            map.put("fIntroUser", "null");
                        }
                        json.accumulate("data", map);
                        json.accumulate("status", 200);
                        json.accumulate("msg", "请求成功");
                    } else {
                        json.accumulate("status", 300);
                        json.accumulate("msg", "未查找到用户");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                json.accumulate("status", 500);
                json.accumulate("msg", "查找失败！");
            }
        } else {
            json.accumulate("status", 500);
            json.accumulate("msg", "签名不对！");
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/userRecharge", produces = { JsonEncode })
    public String userRecharge(HttpServletResponse resp, @RequestParam Map<String, Object> param) {
        JSONObject json = new JSONObject();
        boolean b = SecurityUtils.isCorrectRequest(req, resp, FrontUserJsonController.securtyKey);
        if (b) {
            try (Mysql mysql = new Mysql()) {
                MyQuery ds2 = new MyQuery(mysql);
                ds2.add("SELECT fId from fvirtualcointype WHERE fName_en = '%s'", param.get("coinType") + "");
                ds2.open();
                if (ds2.size() < 1) {
                    json.accumulate("status", 203);
                    json.accumulate("msg", "该币不存在");
                    return json.toString();
                }
                MyQuery ds3 = new MyQuery(mysql);
                ds3.add("SELECT fId from fuser WHERE floginName = '%s' or fTelephone = '%s' ", param.get("userId") + "",
                        param.get("userId") + "");
                ds3.open();
                if (ds3.size() < 1) {
                    json.accumulate("status", 300);
                    json.accumulate("msg", "未查找到用户");
                    return json.toString();
                }
                int coinType = ds2.getInt("fId");
                int uId = ds3.getInt("fId");
                MyQuery ds = new MyQuery(mysql);
                ds.add("SELECT * from fvirtualwallet WHERE fuid = %s ", uId);
                ds.add("and fVi_fId = %s", coinType);
                ds.open();
                if (ds.size() > 0) {
                    ds.edit();
                    String transferType = param.get("transferType") + "";
                    if (transferType.equals("RTT")) {
                        ds.setField("fTotal", ds.getDouble("fTotal") + Double.parseDouble(param.get("coins") + ""));
                    } else if (transferType.equals("TTR")) {
                        double ftotal = ds.getDouble("fTotal");
                        double coin = Double.parseDouble(param.get("coins") + "");
                        if (ftotal >= coin) {
                            ds.setField("fTotal", ds.getDouble("fTotal") - Double.parseDouble(param.get("coins") + ""));
                        } else {
                            json.accumulate("status", 202);
                            json.accumulate("msg", "余额不足");
                            return json.toString();
                        }
                    }
                    ds.post();
                    json.accumulate("status", 200);
                    json.accumulate("msg", "请求成功");
                } else {
                    json.accumulate("status", 204);
                    json.accumulate("msg", "钱包异常，请稍后再试");
                    return json.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                json.accumulate("status", 500);
                json.accumulate("msg", "请求失败");
            }
        } else {
            json.accumulate("status", 500);
            json.accumulate("msg", "签名不对！");
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/setSymmetricKey", produces = { JsonEncode })
    public String setSymmetricKey(HttpServletResponse resp, @RequestParam Map<String, Object> param) {
        JSONObject json = new JSONObject();
        securtyKey = param.get("key") + "";
        json.accumulate("status", 200);
        json.accumulate("msg", "请求成功");
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/coinPrice", produces = { JsonEncode })
    public String getjson(HttpServletResponse resp, @RequestParam Map<String, Object> param) {
        // 传入中文参数并设置编码格式
        JSONObject json = new JSONObject();
        boolean b = SecurityUtils.isCorrectRequest(req, resp, FrontUserJsonController.securtyKey);
        if (b) {
            Map<String, Object> map = new HashMap<>();
            map.put("BTC", 45000);
            json.accumulate("data", map);
            json.accumulate("status", 200);
            json.accumulate("msg", "请求成功！");
        } else {
            json.accumulate("status", 500);
            json.accumulate("msg", "签名不对！");
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/sendMsg", produces = { JsonEncode })
    public String sendMsg(HttpServletResponse resp, @RequestParam Map<String, Object> param) {
        // 传入中文参数并设置编码格式
        JSONObject json = new JSONObject();
        boolean b = SecurityUtils.isCorrectRequest(req, resp, FrontUserJsonController.securtyKey);
        if (b) {
            try {
                ShortMessageService.sendMsg(param.get("phone") + "", param.get("content") + "", param.get("msg") + "");
                json.accumulate("status", 200);
                json.accumulate("msg", "请求成功！");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                json.accumulate("status", 300);
                json.accumulate("msg", "发送失败！");
            }
        } else {
            json.accumulate("status", 500);
            json.accumulate("msg", "签名不对！");
        }
        return json.toString();
    }

    /**
     * 找回密码发送验证码
     *
     * @param request
     * @param resp
     * @param loginName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "m/login/sendMsg", produces = { JsonEncode })
    public String sendMsg1(HttpServletRequest request, HttpServletResponse resp,
            @RequestParam(required = false, defaultValue = "") String loginName) {
        // 传入中文参数并设置编码格式
        JSONObject json = new JSONObject();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT fId,floginName,fTelephone,fareaCode from fuser WHERE floginName = '%s' or fTelephone = '%s'", loginName,loginName);
            ds.open();
            if (ds.size() < 1) {
                json.accumulate("status", 300);
                json.accumulate("msg", "该用户不存在！");
                return json.toString();
            }

            // 发送短信
            if (ShortMessageService.isTrue()) {
                String areaCode = ds.getString("fareaCode");
                String phoneNum = ds.getString("fTelephone");
                // 获得验证码
                String code = ShortMessageService.getCode();
                // 发送短信
                YunPianUtils.sendMsg(areaCode, phoneNum, code);
            }
            json.accumulate("status", 200);
            json.accumulate("msg", "发送成功！");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            json.accumulate("status", 300);
            json.accumulate("msg", "发送失败！");
        }
        return json.toString();
    }

    public static String getEthPrice() {
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            // GET请求直接在链接后面拼上请求参数
            String mPath = "http://api.zb.cn/data/v1/ticker?market=eth_qc";
            URL url = new URL(mPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            // 设置连接超时时间和读取超时时间
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 连接服务器
            conn.connect();
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JsonParser parser = new JsonParser(); // 创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(result.toString());
        JsonObject object11 = (JsonObject) object.get("ticker");
        String gas = object11.get("last").getAsString();
        return gas;
    }

    public static String getBTCPrice() {
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            // GET请求直接在链接后面拼上请求参数
            String mPath = "http://api.zb.cn/data/v1/ticker?market=btc_qc";
            URL url = new URL(mPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            // 设置连接超时时间和读取超时时间
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 连接服务器
            conn.connect();
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JsonParser parser = new JsonParser(); // 创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(result.toString());
        JsonObject object11 = (JsonObject) object.get("ticker");
        String gas = object11.get("last").getAsString();
        return gas;
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/checkpwd", produces = { JsonEncode })
    public String checkpwd(HttpServletResponse resp, @RequestParam Map<String, Object> param) {
        // 传入中文参数并设置编码格式
        JSONObject json = new JSONObject();
        Fuser user = GetCurrentUser(req);
        try (Mysql mysql = new Mysql()) {
        	MyQuery ds = new MyQuery(mysql);
        	ds.add("SELECT * from fuser WHERE fId = %s",user.getFid());
        	ds.open();
			String pwd = param.get("pwd")+"";
			if("".equals(pwd)) {
				json.accumulate("status", 300);
                json.accumulate("msg", "请输入密码！");
			}
			if(MyMD5Utils.encoding(pwd).equals(ds.getString("fTradePassword"))) {
				json.accumulate("status", 200);
                json.accumulate("msg", "密码正确！");
			}else {
				json.accumulate("status", 300);
                json.accumulate("msg", "密码不正确！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.accumulate("status", 300);
            json.accumulate("msg", "网络错误！");
		}
        return json.toString();
    }

    /**
     * 判断用户是否有权限进入商城  华联用户不可以进入商城
     * @param resp
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "api/trading/goToShopping", produces = { JsonEncode })
    public String goToShopping(HttpServletResponse resp, @RequestParam Map<String, Object> param) {
        // 传入中文参数并设置编码格式
        JSONObject json = new JSONObject();
        Fuser user = GetCurrentUser(req);
        try {

            if(user.getFloginName().substring(0,2).equals("RY")) {
                json.accumulate("status", 200);
                json.accumulate("msg", "瑞银用户,可以进入商城！");
            }else {
                json.accumulate("status", 300);
                json.accumulate("msg", "暂未开放！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.accumulate("status", 300);
            json.accumulate("msg", "网络错误！");
        }
        return json.toString();
    }

}
