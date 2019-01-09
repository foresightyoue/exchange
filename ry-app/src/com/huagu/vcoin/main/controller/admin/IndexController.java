package com.huagu.vcoin.main.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CountLimitTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.main.service.front.FrontValidateService;
import com.huagu.vcoin.main.sms.ShortMessageService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDate;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.security.sapi.JayunSecurity;
import cn.cerc.security.sapi.SendMode;
import net.sf.json.JSONObject;

@Controller
public class IndexController extends BaseController {
    private static final Logger log = Logger.getLogger(IndexController.class);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @RequestMapping("/ssadmin/index")
    public ModelAndView Index() throws Exception {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/index");
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select count(*) as count from %s", "fuser");
            ds.open();

            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select sum(total) as Rtotal from ( select (fc.fAmount*fv.fcurrentCNY) as total,fc.fCreateTime,fAmount from %s fc",
                    "fvirtualcaptualoperation");
            ds1.add(" left join %s fv on fc.fVi_fId2 = fv.FId where fc.fType = '1') fg", "fvirtualcointype");
            // ds1.add(" where DATE_FORMAT(fg.fCreateTime,'%Y-%m-%d') =
            // DATE_FORMAT(now(),'%Y-%m-%d')");
            ds1.add(" where fg.fCreateTime >= curdate()");
            ds1.open();

            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select sum(total) as Wtotal from (select (fc.fAmount*fv.fcurrentCNY) as total,fc.fCreateTime from %s fc",
                    "fvirtualcaptualoperation");
            ds2.add(" left join %s fv on fc.fVi_fId2 = fv.FId where fc.fType = '2') fg", "fvirtualcointype");
            // ds2.add(" where DATE_FORMAT(fg.fCreateTime,'%Y-%m-%d') =
            // DATE_FORMAT(now(),'%Y-%m-%d')");
            ds2.add(" where fg.fCreateTime >= curdate()");
            ds2.open();

            /*
             * MyQuery ds3 = new MyQuery(mysql); ds3.
             * add("select COUNT(fg.fid) as count1,sum(fg.amount) as amount1 from (select fe.fid,(fe.fsuccessAmount*ty1.fcurrentCNY) as amount,mp.fvirtualcointype1,fe.fsuccessAmount,fe.fCreateTime from %s fe"
             * , "fentrust"); ds3.add("left join %s mp on fe.ftrademapping = mp.fid ",
             * "ftrademapping"); ds3.
             * add("left join %s ty1 on mp.fvirtualcointype1=ty1.fId WHERE fe.fsuccessAmount<>0) fg"
             * , "fvirtualcointype"); // ds3.add("where
             * DATE_FORMAT(fg.fCreateTime,'%Y-%m-%d') = // DATE_FORMAT(now(),'%Y-%m-%d')");
             * ds3.add(" where fg.fCreateTime >= curdate()"); ds3.setMaximum(1); ds3.open();
             */

            modelAndView.addObject("count", ds.getInt("count"));// 会员数量
            modelAndView.addObject("Rtotal", ds1.getDouble("Rtotal"));// 充值
            modelAndView.addObject("Wtotal", ds2.getDouble("Wtotal"));// 提现
            modelAndView.addObject("fAmount", 111);// 交易金额
            modelAndView.addObject("fCount", 111);// 交易数量
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Mysql mysql1 = new Mysql()) {
            MyQuery ds = new MyQuery(mysql1);
            // ds.add("select ft.fName,ft.fShortName,SUM(CASE WHEN fl.fType=1 THEN
            // fl.fAmount END) AS chongzhi,SUM(CASE WHEN fl.fType=2 THEN fl.fAmount END) AS
            // tixian from %s fv",
            // "fvirtualwallet");
            // ds.add("left join %s fl on fv.fVi_fId=fl.fVi_fId2",
            // "fvirtualcaptualoperation");
            // ds.add("left join %s ft on fv.fVi_fId=ft.fId", "fvirtualcointype");
            // ds.add("where ft.fId<>0 and ft.fstatus=1");
            // ds.add("and fl.fCreateTime between '%s' and '%s'", TDate.Today().incDay(-2),
            // TDateTime.Now());
            // ds.add("GROUP by ft.fId");
            // ds.open();
            modelAndView.addObject("list", ds.getRecords());

            /*
             * MyQuery ds1 = new MyQuery(mysql1);
             * ds1.add("select fAmount,fsuccessAmount,fCount,fleftCount,fprize from %s fe",
             * "fentrust"); ds1.add(" left join %s mp", "ftrademapping"); ds1.
             * add(" on fe.ftrademapping = mp.fid left join fvirtualcointype ty1 on mp.fvirtualcointype1=ty1.fId "
             * ); ds1.add(" order by fCreateTime desc"); ds1.setMaximum(10); ds1.open();
             * List<Map<String, Object>> list2 = new ArrayList<>(); while (ds1.fetch()) {
             * Map<String, Object> ma = new HashMap<>(); ma.put("fAmount",
             * ds1.getDouble("fAmount")); ma.put("fsuccessAmount",
             * ds1.getDouble("fsuccessAmount")); ma.put("fCount", ds1.getDouble("fCount"));
             * ma.put("fleftCount", ds1.getDouble("fleftCount")); ma.put("fprize",
             * ds1.getDouble("fprize")); list2.add(ma); } modelAndView.addObject("list2",
             * list2);
             */
            MyQuery ds3 = new MyQuery(mysql1);
            ds3.add("select fId,fregType,floginName,fNickName,fStatus from %s", "fuser");
            ds3.add("where fRegisterTime between '%s' and '%s'", TDate.Today().incDay(-2), TDateTime.Now());
            ds3.add("order by fRegisterTime desc");
            ds3.open();

            while (ds3.fetch()) {
                if (ds3.getInt("fregType") == 0) {
                    ds3.setField("fregType", "手机注册");
                } else if (ds3.getInt("fregType") == 1) {
                    ds3.setField("fregType", "邮箱注册");
                } else if (ds3.getInt("fregType") == 2) {
                    ds3.setField("fregType", "微信注册");
                } else {
                    ds3.setField("fregType", "QQ注册");
                }
                if (ds3.getInt("fStatus") == 1) {
                    ds3.setField("fStatus", "正常");
                } else {
                    ds3.setField("fStatus", "禁用");
                }
            }
            Fadmin fadmin = (Fadmin) request.getSession().getAttribute("login_admin");
            MyQuery ds4 = new MyQuery(mysql1);
            ds4.add("select fisversion,fId from %s", AppDB.fadmin);
            ds4.add(" where fId = '%s'", fadmin.getFid());
            ds4.open();
            modelAndView.addObject("fisversion", ds4.getString("fisversion"));
            modelAndView.addObject("userList", ds3.getRecords());

        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("dateTime", sdf1.format(new Date()));
        modelAndView.addObject("login_admin", request.getSession().getAttribute("login_admin"));
        return modelAndView;
    }

    @RequestMapping(value = "/ssadmin/d6d5d4d37164a3d0b363d0d64d1f782a", method = RequestMethod.GET)
    public ModelAndView login() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/login");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/ssadmin/sendmsg", produces = { JsonEncode })
    public String sendMsg(HttpServletRequest request) throws Exception {
        getSession(request).setAttribute("deviceId", "webclient");
        String name = request.getParameter("name");
        boolean sendVoice = Boolean.valueOf(request.getParameter("sendVoice"));
        Map<String, Object> map = new HashMap<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery cdsTmp = new MyQuery(mysql);
            cdsTmp.add("select * from %s", AppDB.fadmin);
            cdsTmp.add("where fName='%s'", name);
            cdsTmp.open();
            if (cdsTmp.eof()) {
                map.put("message", "没有找到对应的用户，请核查！");
                map.put("status", false);
                return JSONObject.fromObject(map).toString();
            }
            String phone = cdsTmp.getString("fphone");
            if ("".equals(phone)) {
                map.put("message", "账户未设置安全手机号码，请联系管理员设置安全手机号码！");
                map.put("status", false);
                return JSONObject.fromObject(map).toString();
            }
            if (ShortMessageService.isTrue()) {
                String code = ShortMessageService.getCode();
                int status = ShortMessageService.sendMsg(phone, "【瑞银钱包】系统登录验证码为" + code , code);
                if (status == 0) {
                    map.put("status", true);
                } else {
                    map.put("status", false);
                }
                map.put("message", "验证码已发送,请查收！");
            } else {
                // 后台设置手机号后重新注册聚安
                request.getSession().setAttribute("deviceId", "webclient");
                JayunSecurity api = new JayunSecurity(request);
                // 发送验证码
                if (api.register(name, phone)) {
                    if (sendVoice)
                        api.setSendMode(SendMode.VOICE);
                    map.put("status", api.requestVerify(name));
                }
                map.put("message", api.getMessage());
            }
        }
        return JSONObject.fromObject(map).toString();
    }

    @RequestMapping(value = "/ssadmin/d6d5d4d37164a3d0b363d0d64d1f782a", method = RequestMethod.POST)
    public ModelAndView submitLogin_ys333(HttpServletRequest request, @RequestParam(required = true) String name,
            @RequestParam(required = true) String password,
            @RequestParam(required = false, defaultValue = "0") String google,
            @RequestParam(required = true) String vcode, @RequestParam(required = true) String msgcode)
            throws Exception {

        JspPage modelAndView = new JspPage(request);
        try (Mysql mysql = new Mysql()) {
            MyQuery cdsTmp = new MyQuery(mysql);
            cdsTmp.add("select * from %s", AppDB.fadmin);
            cdsTmp.add("where fName='%s'", name);
            cdsTmp.open();
            if (cdsTmp.eof()) {
                modelAndView.addObject("error", "没有找到对应的用户，请核查！");
                modelAndView.setViewName("/ssadmin/login");
                return modelAndView;
            }
            String phone = cdsTmp.getString("fphone");
            if ("".equals(phone)) {
                modelAndView.addObject("error", "账户未设置安全手机号码，请联系管理员设置安全手机号码！");
                modelAndView.setViewName("/ssadmin/login");
                return modelAndView;
            }
            // 非test版本则需要验证码
            if (!AppDB.isTestVersion()) {
                if (!cdsTmp.getBoolean("fisversion")) {
                    if ("".equals(msgcode)) {
                        modelAndView.addObject("error", "短信验证码不能为空！");
                        modelAndView.setViewName("/ssadmin/login");
                        return modelAndView;
                    }
                    // 验证环境安全
                    getSession(request).setAttribute("deviceId", "webclient");
                    if (ShortMessageService.isTrue()) {
                        boolean status = ShortMessageService.CheckCode(phone, msgcode, new Date());
                        if (!status) {
                            modelAndView.addObject("error", "请输入正确的短信验证码！");
                            modelAndView.setViewName("/ssadmin/login");
                            return modelAndView;
                        }
                    } else {
                        JayunSecurity api = new JayunSecurity(request);
                        if (!api.checkVerify(name, msgcode)) {
                            modelAndView.addObject("error", "请输入正确的短信验证码！");
                            modelAndView.setViewName("/ssadmin/login");
                            return modelAndView;
                        }
                    }
                }
            }
        }

        if (name == null || "".equals(name.trim()) || password == null || "".equals(password.trim()) || vcode == null
                || "".equals(vcode.trim())) {
            modelAndView.setViewName("redirect:/ssadmin/d6d5d4d37164a3d0b363d0d64d1f782a.html");
            return modelAndView;
        }

        String ip = getIpAddr(request);
        int admin_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.AdminLogin);
        if (admin_limit <= 0) {
            modelAndView.addObject("error", "连续登录错误30次，为安全起见，禁止登录2小时！");
            modelAndView.setViewName("/ssadmin/login");
            return modelAndView;
        }
        String checkCode = (String) getSession(request).getAttribute("checkcode");
        if (checkCode == null || "".equals(checkCode)) {
            modelAndView.addObject("error", "图片验证码已经失效，请点击刷新验证码！");
            modelAndView.setViewName("/ssadmin/login");
            return modelAndView;
        }

        if (!checkCode.equalsIgnoreCase(vcode)) {
            modelAndView.addObject("error", "验证码错误！");
            modelAndView.setViewName("/ssadmin/login");
            return modelAndView;
        }

        List<Fadmin> admins = this.adminService.findByProperty("fname", name);
        if (admins == null || admins.size() != 1) {
            modelAndView.addObject("error", "管理员不存在！");
            modelAndView.setViewName("/ssadmin/login");
            return modelAndView;
        }

        Subject admin = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, Utils.MD5(password, admins.get(0).getSalt()));
        token.setRememberMe(true);
        token.setHost(getIpAddr(request));
        try {
            admin.login(token);
            Fuser fuser = new Fuser();
            fuser.setFloginName(admins.get(0).getFname());
            fuser.setFtelephone(admins.get(0).getFphone());
            fuser.setFid(admins.get(0).getFid());
            fuser.setFstatus(1);
            SetSession(fuser, request);
            // 注册聚安
            request.getSession().setAttribute("deviceId", "webclient");
            JayunSecurity security = new JayunSecurity(request);
            security.register(fuser.getFloginName(), fuser.getFtelephone());

            // 发送登录通知
            security.send(admins.get(0).getFname(), AppDB.loginTemplateId,
                    String.format("[后台用户] %s", admins.get(0).getFname()), TDateTime.Now().toString());
        } catch (Exception e) {
            e.printStackTrace();
            token.clear();
            this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.AdminLogin);
            modelAndView.addObject("error", e.getMessage());
            modelAndView.setViewName("/ssadmin/login");
            return modelAndView;
        }

        modelAndView.setViewName("redirect:/ssadmin/index.html");
        return modelAndView;
    }

    @RequestMapping("/ssadmin/appdownload")
    public ModelAndView appdownload() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("/ssadmin/appdownload");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "ssadmin/goVirtualCoinTypeJSP3", produces = { JsonEncode })
    public String goVirtualCoinTypeJSP3(@RequestParam(required = false) int isversion) {
        byte isver = (byte) isversion;
        JSONObject jsonObject = new JSONObject();
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery cd = new MyQuery(mysql);
            cd.add("select fisversion,fId from %s", AppDB.Fuser);
            cd.open();
            if (cd.eof()) {
                log.warn("数据为空!");
            }
            for (Record re : cd) {
                MyQuery cds = new MyQuery(mysql);
                cds.add("select fisversion,fId from %s", AppDB.Fuser);
                cds.add(" where fId = '%s'", re.getString("fId"));
                cds.open();
                cds.edit();
                cds.setField("fisversion", isver);
                cds.post();
            }
            MyQuery dd = new MyQuery(mysql);
            dd.add("select fisversion,fId from %s", AppDB.fadmin);
            dd.open();
            if (dd.eof()) {
                log.warn("数据为空!");
            }
            for (Record re : dd) {
                MyQuery dds = new MyQuery(mysql);
                dds.add("select fisversion,fId from %s", AppDB.fadmin);
                dds.add(" where fId = '%s'", re.getString("fId"));
                dds.open();
                dds.edit();
                dds.setField("fisversion", isver);
                dds.post();
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.accumulate("code", "200");
        jsonObject.accumulate("msg", "操作成功");
        return jsonObject.toString();
    }
}
