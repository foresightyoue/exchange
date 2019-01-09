package com.huagu.vcoin.main.controller.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fuser;

import cn.cerc.jdb.core.Record;
import net.sf.json.JSONObject;

@Controller
public class FrontPersonalController extends BaseController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "m/security/balance")
    public ModelAndView SelectBalance() {
        JspPage jspPage = new JspPage(request);
        Fuser fuser = GetCurrentUser(request);
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT * from fvirtualwallet WHERE fuid = %s  ORDER BY fId", fuser.getFid());
            ds.setMaximum(1);
            ds.setOffset(0);
            ds.open();
            int cointype = ds.getInt("fVi_fId");
            double balance = ds.getDouble("fTotal");
            jspPage.addObject("fTotal", balance);
            jspPage.addObject("cointype", cointype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jspPage.setJspFile("front/security/balance");
        return jspPage;
    }

    @ResponseBody
    @RequestMapping(value = "m/security/gethistoty")
    public String gethistory(@RequestParam(required = false, defaultValue = "0") int type,
            @RequestParam(required = false, defaultValue = "0") int cointype) {
        JSONObject json = new JSONObject();
        Fuser fuser = GetCurrentUser(request);
        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT * from fvirtualwalletlog WHERE fuid = %s and fVi_fid = %s ",
                    fuser.getFid(), cointype);
            if (type != 0) {
                ds.add(" and fPurpose = %s", type);
            }
            ds.add(" ORDER BY fId DESC");
            ds.open();
            for (Record record : ds) {
                Map<String, Object> map = new HashMap<>();
                map.put("fPurpose", record.getInt("fPurpose"));
                map.put("fupdatetime", record.getString("fupdatetime"));
                map.put("ftotalafter", record.getInt("ftotalafter"));
                map.put("change", record.getInt("ftotalafter") - record.getInt("ftimebefore"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(list);
    }

}
