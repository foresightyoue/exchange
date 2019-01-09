package com.huagu.vcoin.main.controller.front;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huagu.vcoin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.Enum.CountLimitTypeEnum;
import com.huagu.vcoin.main.Enum.LogTypeEnum;
import com.huagu.vcoin.main.Enum.MessageTypeEnum;
import com.huagu.vcoin.main.Enum.SendMailTypeEnum;
import com.huagu.vcoin.main.comm.MultipleValues;
import com.huagu.vcoin.main.comm.ValidateMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Emailvalidate;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FrontValidateService;
import com.huagu.vcoin.main.sms.ShortMessageService;

import cn.cerc.security.sapi.JayunMessage;
import net.sf.json.JSONObject;

@Controller
public class FrontValidateJsonController extends BaseController {

    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private ValidateMap validateMap;
    @Autowired
    private UserService userService;

    // 通过邮箱找回登录密码
    @ResponseBody
    @RequestMapping(value = "/validate/sendEmail", produces = JsonEncode)
    public String queryEmail(HttpServletRequest request, @RequestParam(required = true) String imgcode,
            @RequestParam(required = true) String idcardno, @RequestParam(required = true) String email)
            throws Exception {
        JSONObject jsonObject = new JSONObject();

        if (vcodeValidate(request, imgcode) == false) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "图片验证码错误");
            return jsonObject.toString();
        }

        if (!email.matches(new Constant().EmailReg)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "邮箱格式不正确");
            return jsonObject.toString();
        }

        List<Fuser> fusers = this.frontUserService.findUserByProperty("femail", email);
        if (fusers.size() == 1) {
            Fuser fuser = fusers.get(0);
            if (fuser.getFisMailValidate()) {
                // 验证身份证号码
                if (fuser.getFhasRealValidate() == true) {
                    if (idcardno.trim().equals(fuser.getFidentityNo()) == false) {
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "证件号码错误");
                        return jsonObject.toString();
                    }
                }

                String ip = getIpAddr(request);
                Emailvalidate ev = this.validateMap.getMailMap(ip + "_" + SendMailTypeEnum.FindPassword);
                if (ev == null || Utils.getTimestamp().getTime() - ev.getFcreateTime().getTime() > 5 * 60 * 1000L) {
                    boolean flag = false;
                    try {
                        flag = this.frontValidateService.saveSendFindPasswordMail(ip, fuser, email, request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (flag) {
                        jsonObject.accumulate("code", 0);
                        jsonObject.accumulate("msg", "找回密码邮件已经发送，请及时查收");
                        return jsonObject.toString();
                    } else {
                        jsonObject.accumulate("code", -4);
                        jsonObject.accumulate("msg", "网络错误，请稍后再试");
                        return jsonObject.toString();
                    }
                } else {
                    jsonObject.accumulate("code", -4);
                    jsonObject.accumulate("msg", "请求过于频繁，请稍后再试");
                    return jsonObject.toString();
                }
            } else {
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", "该邮箱没有通过验证，不能用于找回密码");
                return jsonObject.toString();
            }
        } else {
            jsonObject.accumulate("code", -2);
            jsonObject.accumulate("msg", "用户不存在");
            return jsonObject.toString();
        }

    }

    // 通过手机找回登录密码
    @ResponseBody
    @RequestMapping(value = { "/m/validate/resetPhoneValidation",
            "/validate/resetPhoneValidation" }, produces = JsonEncode)
    public String resetPhoneValidation(HttpServletRequest request, @RequestParam(required = true) String phone,
            @RequestParam(required = true) String msgcode, @RequestParam(required = true) String idcardno,
            @RequestParam(required = false) String totpCode, @RequestParam(required = true) String imgcode,
            @RequestParam(required = true) String loginName) throws Exception {
        String areaCode = "86";
        JSONObject jsonObject = new JSONObject();

        if (!phone.matches(Constant.PhoneReg)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "手机格式不正确");
            return jsonObject.toString();
        }
        // 图片验证码
        String checkCode = (String) request.getSession().getAttribute("checkcode");

        if (checkCode == null || "".equals(checkCode)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "图片验证码已经失效，请点击刷新验证码！");
            return jsonObject.toString();
        }
        if (!checkCode.equals(imgcode)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "图片验证码不正确");
            return jsonObject.toString();
        }
        List<Fuser> fusers = this.frontUserService.findUserByProperty("floginName", loginName);

        if (fusers.size() == 1) {
            Fuser fuser = fusers.get(0);
            phone = fuser.getFtelephone();
            // 谷歌验证码
            String ip = getIpAddr(request);
            int limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.GOOGLE);
            if (limit <= 0) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "此ip操作频繁，请2小时后再试!");
                return jsonObject.toString();
            } else {
                if (GetCurrentUser(request) != null) {
                    if (fuser.getFgoogleBind() && fuser.isFgoogleCheck()) {
                        if (!GoogleAuth.auth(Long.parseLong(totpCode), fuser.getFgoogleAuthenticator())) {
                            jsonObject.accumulate("code", -1);
                            jsonObject.accumulate("msg", "GOOGLE验证码有误，您还有" + limit + "次机会");
                            this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.GOOGLE);
                            return jsonObject.toString();
                        }
                    }
                }
            }
            JayunMessage message = new JayunMessage(request);
            if (!message.checkRegister(phone, msgcode)) { // 聚安短信认证
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", message.getMessage());
                return jsonObject.toString();
            }
            if (fuser.isFisTelephoneBind()) {
                // 验证身份证号码
                if (fuser.getFhasRealValidate() == true) {
                    if (idcardno.trim().equals(fuser.getFidentityNo()) == false) {
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "证件号码错误");
                        return jsonObject.toString();
                    }
                }

                // 往session放数据
                MultipleValues values = new MultipleValues();
                values.setValue1(fuser.getFid());
                values.setValue2(Utils.getTimestamp());
                request.getSession().setAttribute("resetPasswordByPhone", values);

                this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.FIND_PASSWORD);
                jsonObject.accumulate("code", 0);
                jsonObject.accumulate("msg", "验证成功，将跳转到修改密码界面");
                return jsonObject.toString();
            } else {
                jsonObject.accumulate("code", -3);
                jsonObject.accumulate("msg", "该手机没有通过验证，不能用于找回密码");
                return jsonObject.toString();
            }
        } else {
            jsonObject.accumulate("code", -2);
            jsonObject.accumulate("msg", "用户不存在");
            return jsonObject.toString();
        }

    }

    // 通过手机找回登录密码
    @ResponseBody
    @RequestMapping(value = { "/m/validate/resetPhoneValidation1",
            "/validate/resetPhoneValidation1" }, produces = JsonEncode)
    public String resetPhoneValidation1(@RequestParam(required = true) String phone,
            @RequestParam(required = true) String msgcode, @RequestParam(required = true) String password) {
        JSONObject json = new JSONObject();

        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT * from fuser WHERE fTelephone = '%s'", phone);
            ds.open();
            if (ds.size() > 0) {
            	ds.edit();

            	// 检验验证码
                boolean bool = false;
                String areaCode = ds.getString("fareaCode");
                if(ShortMessageService.isTrue()){
                    if ("86".equals(areaCode)) {
                        bool = ShortMessageService.CheckCode(phone, msgcode, new Date());
                    }else{
                        bool = ShortMessageService.CheckCode("+"+areaCode + phone, msgcode, new Date());
                    }
                }
                if (!bool) {
                    json.accumulate("code", 500);
                    json.accumulate("msg", "验证码错误！");
                    return json.toString();
                }

            	//  ds.setField("fLoginPassword", Utils.MD5(password, ds.getString("salt")));
                ds.setField("fLoginPassword", MyMD5Utils.encoding(password));
                ds.post();
                json.accumulate("code", 200);
                json.accumulate("msg", "修改成功！");
            } else {
                json.accumulate("code", 500);
                json.accumulate("msg", "该用户不存在！");
            }
        } catch (Exception e) {
            json.accumulate("code", 500);
            json.accumulate("msg", "网络错误！");
        }
        return json.toString();
    }

    // 通过手机找回登录密码
    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/m/api/resetPhoneValidationx", "/api/resetPhoneValidationx" }, produces = JsonEncode)
    public String apiresetPhoneValidation(HttpServletRequest request, @RequestParam(required = true) String phone,
            @RequestParam(required = true) String msgcode, @RequestParam(required = true) String idcardno,
            @RequestParam(required = true) String loginName, @RequestParam(required = true) String sign,@RequestParam(required = true,defaultValue = "86")String areaCode)
            throws Exception {
        ;
        JSONObject jsonObject = new JSONObject();
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("phone", phone);
        map.put("msgcode", msgcode);
        map.put("idcardno", idcardno);
        String sort = KeyUtil.sort(map);
//        if (!sort.equals(sign)) {
//            jsonObject.accumulate("code", -1);
//            jsonObject.accumulate("msg", "接口认证失败！");
//            return jsonObject.toString();
//        }
        if (!phone.matches(Constant.PhoneReg)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "手机格式不正确");
            return jsonObject.toString();
        }
        List<Fuser> fusers = this.frontUserService.findUserByProperty("floginName", loginName);

        if (fusers.size() == 1) {
            Fuser fuser = fusers.get(0);
            phone = fuser.getFtelephone();
            // 谷歌验证码
//            String ip = getIpAddr(request);
//            if (!"123456".equals(msgcode)) {
//                jsonObject.accumulate("code", -1);
//                jsonObject.accumulate("msg", "短信验证码错误");
//                return jsonObject.toString();
//            }
            JayunMessage message = new JayunMessage(request);
            boolean bool = message.checkRegister(phone, msgcode);

            if (!bool) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "短信验证码错误");
                return jsonObject.toString();
            }
            /*
             * JayunMessage message = new JayunMessage(request); if (!message.checkRegister(phone, msgcode)) { // 聚安短信认证
             * jsonObject.accumulate("code", -1); jsonObject.accumulate("msg", message.getMessage()); return
             * jsonObject.toString(); }
             */
            if (fuser.isFisTelephoneBind()) {
                // 验证身份证号码
                if (fuser.getFhasRealValidate() == true) {
                    if (idcardno.trim().equals(fuser.getFidentityNo()) == false) {
                        jsonObject.accumulate("code", -1);
                        jsonObject.accumulate("msg", "证件号码错误");
                        return jsonObject.toString();
                    }
                }

                // 往session放数据
//                MultipleValues values = new MultipleValues();
//                values.setValue1(fuser.getFid());
//                values.setValue2(Utils.getTimestamp());
//                request.getSession().setAttribute("resetPasswordByPhone", values);

                this.validateMap.removeMessageMap(fuser.getFid() + "_" + MessageTypeEnum.FIND_PASSWORD);
                jsonObject.accumulate("code", 0);
//                jsonObject.accumulate("uid", String.valueOf(fuser.getFid()));
                jsonObject.accumulate("uid", fuser.getFid());
                jsonObject.accumulate("msg", "验证成功，将跳转到修改密码界面");
                return jsonObject.toString();
            } else {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "该手机没有通过验证，不能用于找回密码");
                return jsonObject.toString();
            }
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "用户不存在");
            return jsonObject.toString();
        }

    }

    // 邮件重置密码最后一步
    @ResponseBody
    @RequestMapping(value = "/validate/resetPassword", produces = JsonEncode)
    public String resetPassword(HttpServletRequest request, @RequestParam(required = true) String newPassword,
            @RequestParam(required = true) String newPassword2, @RequestParam(required = true) int fid,
            @RequestParam(required = true) int ev_id, @RequestParam(required = true) String newuuid) throws Exception {
        JSONObject jsonObject = new JSONObject();

        boolean flag = false;
        try {
            flag = this.frontValidateService.canSendFindPwdMsg(fid, ev_id, newuuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag) {
            jsonObject.accumulate("code", -6);
            jsonObject.accumulate("msg", "重置密码页面已经失效");
            return jsonObject.toString();
        }

        // 密码
        if (newPassword.length() < 6) {
            jsonObject.accumulate("code", -2);
            jsonObject.accumulate("msg", "密码必须6~15位");
            return jsonObject.toString();
        }

        if (!newPassword.equals(newPassword)) {
            jsonObject.accumulate("code", -3);
            jsonObject.accumulate("msg", "两次密码输入不一样");
            return jsonObject.toString();
        }

        Fuser fuser = this.frontUserService.findById(fid);
        String salt = fuser.getSalt();
        if (null == salt) {
            if (MD5.get(newPassword).equals(fuser.getFtradePassword())) {
                jsonObject.accumulate("code", -4);
                jsonObject.accumulate("msg", "登录密码不能和交易密码一样");
                return jsonObject.toString();
            }
        } else {
            if (Utils.MD5(newPassword, fuser.getSalt()).equals(fuser.getFtradePassword())) {
                jsonObject.accumulate("code", -4);
                jsonObject.accumulate("msg", "登录密码不能和交易密码一样");
                return jsonObject.toString();
            }
        }

        boolean updateFlag = false;
        if (null == salt) {
            fuser.setFloginPassword(MD5.get(newPassword));
        } else {
            fuser.setFloginPassword(Utils.MD5(newPassword, fuser.getSalt()));
        }

        try {
            String ip = getIpAddr(request);
            this.frontUserService.updateFUser(fuser, null, LogTypeEnum.User_RESET_PWD, ip);
            updateFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (updateFlag) {
            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "登录密码重置成功");
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", -5);
            jsonObject.accumulate("msg", "网络错误，请稍后再试");
            return jsonObject.toString();
        }

    }

    // 手机重置密码最后一步
    @ResponseBody
    @RequestMapping(value = "/validate/resetPasswordPhone", produces = JsonEncode)
    public String resetPasswordPhone(HttpServletRequest request, @RequestParam(required = true) String newPassword,
            @RequestParam(required = true) String newPassword2) throws Exception {
        JSONObject jsonObject = new JSONObject();

        boolean isValidate = false;
        Fuser fuser = null;
        Object resetPasswordByPhone = request.getSession().getAttribute("resetPasswordByPhone");
        if (resetPasswordByPhone != null) {
            MultipleValues values = (MultipleValues) resetPasswordByPhone;
            Integer fuserid = (Integer) values.getValue1();
            Timestamp time = (Timestamp) values.getValue2();

            if (Utils.timeMinus(Utils.getTimestamp(), time) < 300) {
                fuser = this.frontUserService.findById(fuserid);
                isValidate = true;
            }
        }

        if (!isValidate) {
            jsonObject.accumulate("code", -6);
            jsonObject.accumulate("msg", "重置密码页面已经失效");
            return jsonObject.toString();
        }

        // 密码
        if (newPassword.length() < 6) {
            jsonObject.accumulate("code", -2);
            jsonObject.accumulate("msg", "密码必须6~15位");
            return jsonObject.toString();
        }

        if (!newPassword.equals(newPassword)) {
            jsonObject.accumulate("code", -3);
            jsonObject.accumulate("msg", "两次密码输入不一样");
            return jsonObject.toString();
        }
        String salt = fuser.getSalt();
        if (null == salt) {
            if (MD5.get(newPassword).equals(fuser.getFtradePassword())) {
                jsonObject.accumulate("code", -4);
                jsonObject.accumulate("msg", "登录密码不能和交易密码一样");
                return jsonObject.toString();
            }
        } else {
            if (Utils.MD5(newPassword, fuser.getSalt()).equals(fuser.getFtradePassword())) {
                jsonObject.accumulate("code", -4);
                jsonObject.accumulate("msg", "登录密码不能和交易密码一样");
                return jsonObject.toString();
            }
        }

        boolean updateFlag = false;
        if (null == salt) {
            fuser.setFloginPassword(MD5.get(newPassword));
        } else {
            fuser.setFloginPassword(Utils.MD5(newPassword, fuser.getSalt()));
        }

        try {
            String ip = getIpAddr(request);
            this.frontUserService.updateFUser(fuser, null, LogTypeEnum.User_RESET_PWD, ip);
            updateFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (updateFlag) {
            request.getSession().removeAttribute("resetPasswordByPhone");

            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "登录密码重置成功");
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", -5);
            jsonObject.accumulate("msg", "网络错误，请稍后再试");
            return jsonObject.toString();
        }

    }

    @ResponseBody
    @RequestMapping(value = { "/api/resetPasswordPhone", "/m/api/resetPasswordPhone" }, produces = JsonEncode)
    public String apiresetPasswordPhone(HttpServletRequest request, @RequestParam(required = true) String newPassword,
            @RequestParam(required = true) String newPassword2,
            @RequestParam(required = true) int uid,@RequestParam(required = true) String sign)
            throws Exception {
        JSONObject jsonObject = new JSONObject();

        boolean isValidate = false;
        Fuser fuser = null;
        fuser = this.userService.findById(uid);
//        Object resetPasswordByPhone = request.getSession().getAttribute("resetPasswordByPhone");
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("newPassword", newPassword);
        map.put("newPassword2", newPassword2);
        map.put("uid", uid);
        String sort = KeyUtil.sort(map);
        if (!sort.equals(sign)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "接口认证失败！");
            return jsonObject.toString();
        }
//        if (resetPasswordByPhone != null) {
//            MultipleValues values = (MultipleValues) resetPasswordByPhone;
//            Integer fuserid = (Integer) values.getValue1();
//            Timestamp time = (Timestamp) values.getValue2();
//
//            if (Utils.timeMinus(Utils.getTimestamp(), time) < 300) {
//                fuser = this.frontUserService.findById(fuserid);
//                isValidate = true;
//            }
//        }

//        if (!isValidate) {
//            jsonObject.accumulate("code", -1);
//            jsonObject.accumulate("msg", "重置密码页面已经失效");
//            return jsonObject.toString();
//        }

        // 密码
        if (newPassword.length() < 6) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "密码必须6~15位");
            return jsonObject.toString();
        }

        if (!newPassword.equals(newPassword)) {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "两次密码输入不一样");
            return jsonObject.toString();
        }
        String salt = fuser.getSalt();
        if (null == salt) {
            if (MD5.get(newPassword).equals(fuser.getFtradePassword())) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "登录密码不能和交易密码一样");
                return jsonObject.toString();
            }
        } else {
            if (Utils.MD5(newPassword, fuser.getSalt()).equals(fuser.getFtradePassword())) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "登录密码不能和交易密码一样");
                return jsonObject.toString();
            }
        }

        boolean updateFlag = false;
        if (null == salt) {
            fuser.setFloginPassword(MD5.get(newPassword));
        } else {
            fuser.setFloginPassword(Utils.MD5(newPassword, fuser.getSalt()));
        }

        try {
            String ip = getIpAddr(request);
            this.frontUserService.updateFUser(fuser, null, LogTypeEnum.User_RESET_PWD, ip);
            updateFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (updateFlag) {
            request.getSession().removeAttribute("resetPasswordByPhone");

            jsonObject.accumulate("code", 0);
            jsonObject.accumulate("msg", "登录密码重置成功");
            return jsonObject.toString();
        } else {
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络错误，请稍后再试");
            return jsonObject.toString();
        }

    }

    // 绑定邮箱
    @ResponseBody
    @RequestMapping("/validate/postValidateMail")
    public String postMail(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") String email)
            throws Exception {
        JSONObject js = new JSONObject();
        if (GetCurrentUser(request) == null) {
            js.accumulate("code", -1);
            js.accumulate("msg", "非法操作");
            return js.toString();
        }

        // 邮箱注册
        boolean isExists = this.frontUserService.isEmailExists(email);
        if (isExists) {
            js.accumulate("code", -1);
            js.accumulate("msg", "邮箱地址已存在");
            return js.toString();
        }

        if (!email.matches(new Constant().EmailReg)) {
            js.accumulate("code", -1);
            js.accumulate("msg", "邮箱格式不正确");
            return js.toString();
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        if (fuser.getFisMailValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "您的邮箱已经绑定成功");
            return js.toString();
        }

        boolean flag = false;
        try {
            flag = this.frontUserService.saveValidateEmail(fuser, email, false);
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", -1);
            js.accumulate("msg", "网络异常");
            return js.toString();
        }

        if (flag) {
            js.accumulate("code", 0);
            js.accumulate("msg", "发送成功");
            return js.toString();
        } else {
            js.accumulate("code", -1);
            js.accumulate("msg", "半小时内只能发送一次");
            return js.toString();
        }
    }
    
    // 绑定邮箱
    @Deprecated
    @ResponseBody
    @RequestMapping(value = { "/user/api/postValidateMailx","/m/api/postValidateMailx"})
    public String postValidateMail(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") String email)
            throws Exception {
        JSONObject js = new JSONObject();
        if (GetCurrentUser(request) == null) {
            js.accumulate("code", -1);
            js.accumulate("msg", "非法操作");
            return js.toString();
        }

        // 邮箱注册
        boolean isExists = this.frontUserService.isEmailExists(email);
        if (isExists) {
            js.accumulate("code", -1);
            js.accumulate("msg", "邮箱地址已存在");
            return js.toString();
        }

        if (!email.matches(new Constant().EmailReg)) {
            js.accumulate("code", -1);
            js.accumulate("msg", "邮箱格式不正确");
            return js.toString();
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());

        if (fuser.getFisMailValidate()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "您的邮箱已经绑定成功");
            return js.toString();
        }

        boolean flag = false;
        try {
            flag = this.frontUserService.saveValidateEmail(fuser, email, false);
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", -1);
            js.accumulate("msg", "网络异常");
            return js.toString();
        }

        if (flag) {
            js.accumulate("code", 0);
            js.accumulate("msg", "发送成功");
            return js.toString();
        } else {
            js.accumulate("code", -1);
            js.accumulate("msg", "半小时内只能发送一次");
            return js.toString();
        }
    }

}
