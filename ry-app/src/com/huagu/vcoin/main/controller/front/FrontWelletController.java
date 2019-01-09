package com.huagu.vcoin.main.controller.front;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cerc.jdb.mysql.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.MyMD5Utils;
import com.zhongyinghui.security.utils.HttpClientUtils;

import cn.cerc.jdb.core.Record;

@Controller
public class FrontWelletController extends BaseController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private FrontUserService frontUserService;

    @RequestMapping(value = "m/trade/SCwellet")
    public ModelAndView SCwellet() {
        JspPage modelAndview = new JspPage(request);
        modelAndview.setJspFile("front/trade/balance");
        return modelAndview;
    }

    @RequestMapping(value = "m/trade/CFwellet")
    public ModelAndView CFwellet() {
        JspPage modelandview = new JspPage(request);
        String walletId = request.getParameter("walletId");
        String Wellettype = request.getParameter("Wellettype");
        String wellet = request.getParameter("wellet");
        modelandview.addObject("walletId", walletId);
        modelandview.addObject("wellet", wellet);
        modelandview.addObject("Wellettype", Wellettype);
        modelandview.setJspFile("front/trade/balance1");
        return modelandview;
    }

    @RequestMapping(value = "m/trade/Zcwellet")
    public ModelAndView Zcwellet() {
        JspPage modelandview = new JspPage(request);
        String walletId = request.getParameter("walletId");
        modelandview.addObject("walletId", walletId);
        modelandview.setJspFile("front/trade/ZcWellet");
        return modelandview;
    }

    @RequestMapping(value = "m/trade/CZwellet")
    public ModelAndView CZwellet() throws IOException {
        JspPage modelandview = new JspPage(request);
        modelandview.setJspFile("front/trade/balance1");
        com.alibaba.fastjson.JSONObject json1 = new com.alibaba.fastjson.JSONObject();
        json1.put("userId", "RY009");
        json1.put("type", "RYB");
        json1.put("currency", "10.0");
        String str = HttpClientUtils.doPost(Constant.SettlementIP + "/api/trading/userRecharge", json1);
        int status = JSON.parseObject(str).getInteger("status");
        if (status == 200) {
            System.out.println("成功！");
        }
        return modelandview;
    }

    @RequestMapping(value = "m/trade/Hbwellet")
    public ModelAndView Hbwellet() throws IOException {
        JspPage modelandview = new JspPage(request);
        String walletId = request.getParameter("walletId");
        modelandview.addObject("walletId", walletId);
        modelandview.setJspFile("front/trade/HbWellet");
        return modelandview;
    }

    @RequestMapping("/m/ryb/scproduct")
    public ModelAndView details(HttpServletRequest request) {
        JspPage modelAndView = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        String fid = request.getParameter("fid");
        modelAndView.addObject("fid", fid);
        modelAndView.setJspFile("/front/ryb/scproduct");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "m/trade/returnUri", produces = { JsonEncode })
    public String returnUri(@RequestBody Map<String, Object> param) throws IOException {
        JSONObject json = JSON.parseObject(JSON.toJSONString((param.get("json"))));
        return HttpClientUtils.doPost(param.get("uri") + "", json);
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/getjson", produces = { JsonEncode })
    public String getjson(@RequestParam Map<String, Object> param) throws IOException {
        JSONObject json = new JSONObject();
        Fuser fuser = GetCurrentUser(request);
        json.put("userId", fuser.getFloginName());
        String uri = Constant.SettlementIP + param.get("uri");
        /* String uri = "http://127.0.0.1" + param.get("uri"); */
        param.remove("uri");
        String securityKey = SecurityUtils.getSecurityKey(param, FrontUserJsonController.securtyKey);
        json.put("securityKey", securityKey);
        for (String s : param.keySet()) {
            json.put(s, param.get(s));
        }
        return HttpClientUtils.doPost(uri, json);
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/getjson1", produces = { JsonEncode })
    public String getjson1(@RequestParam Map<String, Object> param) throws IOException {
        JSONObject json = new JSONObject();
        String uri = Constant.SettlementIP + param.get("uri");
        /* String uri = "http://127.0.0.1" + param.get("uri"); */
        param.remove("uri");
        String securityKey = SecurityUtils.getSecurityKey(param, FrontUserJsonController.securtyKey);
        json.put("securityKey", securityKey);
        for (String s : param.keySet()) {
            json.put(s, param.get(s));
        }
        return HttpClientUtils.doPost(uri, json);
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/getjson2", produces = { JsonEncode })
    public String getjson2(@RequestParam Map<String, Object> param) throws IOException {
        JSONObject json = new JSONObject();
        Fuser fuser = GetCurrentUser(request);
        if (fuser == null) {
            json.put("status", 300);
            json.put("msg", "请重新登录");
            return json.toString();
        }
        param.put("userId", fuser.getFloginName());
        String str = "";
        String uri = Constant.SettlementIP + param.get("uri");
        /* String uri = "http://127.0.0.1" + param.get("uri"); */
        param.remove("uri");
        if(uri.indexOf("/api/app/mutualRYHRYB") != -1 || uri.indexOf("/api/app/smallWalletTransfer") != -1
                || uri.indexOf("/api/app/transferMall") != -1|| uri.indexOf("/api/app/cnyExchangeRyb") != -1
                || uri.indexOf("/api/app/tradeExchange") != -1 || uri.indexOf("/api/app/transferAccount") != -1) {
        	try (Mysql mysql = new Mysql()) {
        		MyQuery ds = new MyQuery(mysql);
        		ds.add("SELECT * from fuser WHERE fId = %s",fuser.getFid());
        		ds.open();
        		String pwd = param.get("pswd")+"";
        		if("".equals(pwd)) {
        			json.put("status", 300);
        			json.put("msg", "请输入密码！");
        			return json.toJSONString();
        		}
        		if(MyMD5Utils.encoding(pwd).equals(ds.getString("fTradePassword"))) {
        			param.remove("pswd");
        		}else {
        			json.put("status", 300);
        			json.put("msg", "密码不正确！");
        			return json.toJSONString();
        		}
        	} catch (Exception e) {
        		e.printStackTrace();
        		json.put("status", 300);
        		json.put("msg", "网络错误！");
        		return json.toJSONString();
        	}
        }
        String securityKey = SecurityUtils.getSecurityKey(param, FrontUserJsonController.securtyKey);
        param.put("securityKey", securityKey);
        for (String s : param.keySet()) {
            str += s + "=" + param.get(s) + "&";
        }
        if(uri.indexOf("/api/app/transferMall") != -1){
            // TODO 添加转账到商城记录时   加入用户名称  查询用户的名称
            param.put("userName",fuser.getFrealName());
            return HttpClientUtils.doPost(uri,new JSONObject(param));
        }else{
            return GetResponse(str, uri);
        }
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/getjson3", produces = { JsonEncode })
    public String getjson3(@RequestParam Map<String, Object> param) throws IOException {
        JSONObject json = new JSONObject();
        Fuser fuser = GetCurrentUser(request);
        param.put("userId", fuser.getFloginName());
        String str = "";
        String uri = Constant.OSSURL + param.get("uri");
        /* String uri = "http://127.0.0.1" + param.get("uri"); */
        param.remove("uri");
        String securityKey = SecurityUtils.getSecurityKey(param, FrontUserJsonController.securtyKey);
        json.put("securityKey", securityKey);
        param.put("securityKey", securityKey);
        for (String s : param.keySet()) {
            str += s + "=" + param.get(s) + "&";
        }
        return GetResponse(str, uri);
    }

    @ResponseBody
    @RequestMapping(value = "m/trade/tradeExchange", produces = { JsonEncode })
    public String tradeExchange(@RequestParam Map<String, Object> param) throws IOException {
        JSONObject json = new JSONObject();
        Fuser user = GetCurrentUser(request);
        param.put("userId", user.getFloginName());
        String str = "";
        String uri = Constant.SettlementIP + "/api/app/tradeExchange";
        try (Mysql mysql = new Mysql()) {
        	MyQuery ds = new MyQuery(mysql);
        	ds.add("SELECT * from fuser WHERE fId = %s",user.getFid());
        	ds.open();
			String pwd = param.get("pswd")+"";
			if("".equals(pwd)) {
				json.put("status", 300);
                json.put("msg", "请输入密码！");
                return json.toJSONString();
			}
			if(MyMD5Utils.encoding(pwd).equals(ds.getString("fTradePassword"))) {
				param.remove("pswd");
			}else {
				json.put("status", 300);
                json.put("msg", "密码不正确！");
                return json.toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", 300);
            json.put("msg", "网络错误！");
            return json.toJSONString();
		}
        String securityKey = SecurityUtils.getSecurityKey(param, FrontUserJsonController.securtyKey);
        param.put("securityKey", securityKey);
        JSONObject j = new JSONObject();
        j.putAll(param);
        for (String s : param.keySet()) {
            str += s + "=" + param.get(s) + "&";
        }
        String json1 = GetResponse(str, uri);
        return json1;
    }

    @ResponseBody
    @RequestMapping(value = "api/trading/queryBlance", produces = { JsonEncode })
    public String queryBlance(HttpServletResponse resp, HttpServletRequest req, @RequestParam Map<String, Object> param)
            throws IOException {
        JSONObject json = new JSONObject();
        boolean b = SecurityUtils.isCorrectRequest(req, resp,
                FrontUserJsonController.securtyKey);
        if (b) {
            try (Mysql mysql = new Mysql()) {
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("SELECT fId from fuser WHERE floginName = '%s' or fTelephone = '%s' ", param.get("userId") + "",
                        param.get("userId") + "");
                ds1.open();
                if (ds1.size() < 1) {
                    json.put("status", 300);
                    json.put("msg", "未查找到用户");
                    return json.toJSONString();
                }
                MyQuery ds = new MyQuery(mysql);
                ds.add("SELECT fShortName,fTotal,fFrozen from fvirtualwallet w INNER JOIN fvirtualcointype t on w.fVi_fId = t.fId WHERE fuid = %s",
                        ds1.getInt("fId"));
                ds.open();
                Map<String, Map<String, Object>> list = new HashMap<>();
                for (Record record : ds) {
                    list.put(record.getString("fShortName"), record.getItems());
                }
                json.put("data", list);
                json.put("status", 200);
                json.put("msg", "请求成功");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                json.put("status", 500);
                json.put("msg", "请求失败");
            }
        } else {
            json.put("status", 501);
            json.put("msg", "签名不对！");
        }
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "m/trade/transferAccount", produces = { JsonEncode })
    public String transferAccount(@RequestParam Map<String, Object> param) throws IOException {
        JSONObject json = new JSONObject();
        Fuser user = GetCurrentUser(request);
        String str = "";
        String uri = Constant.SettlementIP + "/api/app/transferAccount";
        try (Mysql mysql = new Mysql()) {
        	MyQuery ds = new MyQuery(mysql);
        	ds.add("SELECT * from fuser WHERE fId = %s",user.getFid());
        	ds.open();
			String pwd = param.get("pswd")+"";
			if("".equals(pwd)) {
				json.put("status", 300);
                json.put("msg", "请输入密码！");
                return json.toJSONString();
			}
			if(MyMD5Utils.encoding(pwd).equals(ds.getString("fTradePassword"))) {
				param.remove("pswd");
				param.put("srcUserId", user.getFloginName());
		        String securityKey = SecurityUtils.getSecurityKey(param, FrontUserJsonController.securtyKey);
		        param.put("securityKey", securityKey);
		        JSONObject j = new JSONObject();
		        j.putAll(param);
//		        String s1 = HttpClientUtils.doPost(uri, j);
		        for (String s : param.keySet()) {
		            str += s + "=" + param.get(s) + "&";
		        }
			}else {
				json.put("status", 300);
                json.put("msg", "密码不正确！");
                return json.toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", 300);
            json.put("msg", "网络错误！");
		}
        String json1 = GetResponse(str, uri);
        return json1.toString();
    }

    public static String GetResponse(String Info, String URL) throws IOException {
        String path = "http://newhcc.dsrled.com/index.php?m=member&c=public&a=receive_r&mid=IEdcdeRDDw4dy4e7h";

        // 1, 得到URL对象
        URL url = new URL(URL);
        // 2, 打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 3, 设置提交类型
        conn.setRequestMethod("POST");
        // 4, 设置允许写出数据,默认是不允许 false
        conn.setDoOutput(true);
        conn.setDoInput(true);// 当前的连接可以从服务器读取内容, 默认是true
        // 5, 获取向服务器写出数据的流
        OutputStream os = conn.getOutputStream();
        // 参数是键值队 , 不以"?"开始
        os.write(Info.getBytes());
        // os.write("googleTokenKey=&username=admin&password=5df5c29ae86331e1b5b526ad90d767e4".getBytes());
        os.flush();
        // 6, 获取响应的数据
        // 得到服务器写回的响应数据
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        String str = br.readLine();
        System.out.println("响应内容为:  " + str);

        return str;
    }

    @RequestMapping(value = "m/trade/ToBill")
    public ModelAndView ToBill() {
        JspPage modelAndview = new JspPage(request);
        modelAndview.setJspFile("front/trade/ToBill");
        return modelAndview;
    }

    @RequestMapping(value = "m/financial/walletBill")
    public ModelAndView walletBill() {
        JspPage modelAndview = new JspPage(request);
        modelAndview.setJspFile("front/financial/walletBill");
        return modelAndview;
    }

    @RequestMapping(value = "m/ryb/transferBill")
    public ModelAndView transferBill() {
        JspPage modelAndview = new JspPage(request);
        modelAndview.setJspFile("/front/ryb/transferBill");
        return modelAndview;
    }

    @RequestMapping(value = "m/trade/ToActivation")
    public ModelAndView ToActivation() {
        JspPage modelAndview = new JspPage(request);
        Fuser user = GetCurrentUser(request);
        modelAndview.addObject("userId", user.getFloginName());
        modelAndview.setJspFile("front/financial/ToActivation");
        return modelAndview;
    }

    @ResponseBody
    @RequestMapping(value = "m/trade/Activation", produces = { JsonEncode })
    public String Activation(@RequestParam Map<String, Object> param) {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        Fuser user = GetCurrentUser(request);
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT fId,fIntroUser_id from fuser where fId = %s", user.getFid());
            ds.open();
            String fIntroUser_id = ds.getString("fIntroUser_id");
            if ("".equals(fIntroUser_id)) {
                ds.edit();
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("SELECT fId,floginName,fTelephone from fuser where floginName = '%s' or fTelephone = '%s'",
                        param.get("recommendId") + "", param.get("recommendId") + "");
                ds1.open();
                if (ds1.size() > 0) {
                    ds.setField("fIntroUser_id", ds1.getInt("fId"));
                    JSONObject json1 = JSON.parseObject(this.getjson1(param));
                    int s = json1.getInteger("status");
                    if (s == 200) {
                        ds.post();
                    } else {
                        json.accumulate("status", 500);
                        json.accumulate("msg", json1.getString("msg"));
                        return json.toString();
                    }
                }else {
                    json.accumulate("status", 500);
                    json.accumulate("msg", "系统中未找到该用户");
                    return json.toString();
                }
            } else {
                json.accumulate("status", 500);
                json.accumulate("msg", "该用户已激活");
                return json.toString();
            }
            json.accumulate("status", 200);
            json.accumulate("msg", "请求成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.accumulate("status", 500);
            json.accumulate("msg", "请求失败");
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "m/trade/IsActivation", produces = { JsonEncode })
    public String IsActivation() {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        Fuser user = GetCurrentUser(request);
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT fId,fIntroUser_id,fIsMerchant from fuser where fId = %s", user.getFid());
            ds.open();
            String fIntroUser_id = ds.getString("fIntroUser_id");
            boolean IsActivation = false;
            if (!"".equals(fIntroUser_id)) {
                IsActivation = true;
            }
            int fIsMerchant = ds.getInt("fIsMerchant");
            json.accumulate("status", 200);
            json.accumulate("IsActivation", IsActivation);
            json.accumulate("fIsMerchant", ds.getInt("fIsMerchant"));
            json.accumulate("msg", "请求成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.accumulate("status", 200);
            json.accumulate("msg", "请求失败");
        }
        return json.toString();
    }


	@RequestMapping(value = "m/ryb/childTranster")
    public ModelAndView childTranster() {
        JspPage modelAndview = new JspPage(request);
        String walletId = request.getParameter("walletId");
        modelAndview.addObject("walletId", walletId);
        String walletId1 = request.getParameter("walletId1");
        modelAndview.addObject("walletId1", walletId1);
        modelAndview.setJspFile("front/ryb/transferAccounts2");
        return modelAndview;
    }
	
	@RequestMapping(value = "m/ryb/RYHTranster")
    public ModelAndView RYHTranster() {
        JspPage modelAndview = new JspPage(request);
        String walletId = request.getParameter("walletId");
        modelAndview.addObject("walletId", walletId);
        modelAndview.setJspFile("front/ryb/transferAccounts3");
        return modelAndview;
    }
	
    @RequestMapping(value = "m/ryb/tosearch")
    public ModelAndView tosearch() {
        JspPage modelAndview = new JspPage(request);
        modelAndview.setJspFile("front/financial/search");
        return modelAndview;
    }
    
    @ResponseBody
    @RequestMapping(value = "m/ryb/search", produces = { JsonEncode })
    public String search(@RequestParam(required = false, defaultValue = "") String key) {
    	net.sf.json.JSONObject json = new net.sf.json.JSONObject();
    	List<Map<String, Object>> list = new ArrayList<>();
    	try (Mysql mysql = new Mysql()) {
    		MyQuery ds = new MyQuery(mysql);
    		ds.add("SELECT fId,ftradedesc from ftrademapping WHERE fstatus = 1 and ftradedesc like '%%%s%%'",key);
    		ds.open();
    		for (Record record : ds) {
				list.add(record.getItems());
			}
    		if(ds.size()>0) {
    			json.accumulate("list", list);
    			json.accumulate("status", 200);
    			json.accumulate("msg", "请求成功");
    		}else {
    			json.accumulate("status", 300);
    			json.accumulate("msg", "没有搜索到内容");
    		}
		} catch (Exception e) {
			e.printStackTrace();
			json.accumulate("status", 500);
    		json.accumulate("msg", "网络异常");
		}
    	return json.toString();
    }

    // 当前用户的购买订单
    @ResponseBody
    @RequestMapping(value = "m/trade/getOrders", produces = { JsonEncode })
    public String getOrders() {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        List<Map<String, Object>> orderListIng = new ArrayList<>();
        List<Map<String, Object>> orderListOver = new ArrayList<>();
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sql.add("DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sql.add("fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fTrade_Object=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sql.add("and o.fUsr_id='%s'", currentUser.getFid());
            sql.add("and (fTrade_Status = '1' or fTrade_Status = '2' or fTrade_Status = '5')");
            sql.add("order by fOrder_Time desc");
            sql.open();
            for (Record record : sql) {
                orderListIng.add(record.getItems());
            }
            json.accumulate("IngOrders", orderListIng);
        } catch (Exception e) {
            json.accumulate("code", 500);
            json.accumulate("message", "获取失败");
        }
        // 已完成的
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sql.add("DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sql.add("fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fTrade_Object=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sql.add("and o.fUsr_id='%s'", currentUser.getFid());
            sql.add("and (fTrade_Status = '3' or fTrade_Status = '4' or fTrade_Status = '6')");
            sql.add("order by fOrder_Time desc");
            sql.open();
            for (Record record : sql) {
                orderListOver.add(record.getItems());
            }
            json.accumulate("overOrders", orderListOver);
            json.accumulate("code", 200);
            json.accumulate("message", "请求成功");
        } catch (Exception e) {
            json.accumulate("code", 500);
            json.accumulate("message", "获取失败");
        }
        return json.toString();
    }

    // 查询当前用户卖出订单
    @ResponseBody
    @RequestMapping(value = "m/trade/getOrderShells", produces = { JsonEncode })
    public String getOrderShells() {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        List<Map<String, Object>> orderListIng = new ArrayList<>();
        List<Map<String, Object>> orderListOver = new ArrayList<>();
        int maxNum = 10;
        // 进行中
        int totalListIng = 0;
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sql.add("DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sql.add("fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fUsr_id=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sql.add("and fTrade_Object = '%s'", currentUser.getFid());
            sql.add("and (fTrade_Status = '1' or fTrade_Status = '2' or fTrade_Status = '5')");
            sql.add("order by fOrder_Time desc");
            sql.open();
            for (Record record : sql) {
                orderListIng.add(record.getItems());
            }
            json.accumulate("IngOrders", orderListIng);
        } catch (Exception e) {
            json.accumulate("code", 500);
            json.accumulate("message", "获取失败");
        }
        // 已完成的
        int totalListOver = 0;
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery sql = new MyQuery(handle);
            sql.add("select o.fId,o.fCreateTime,o.fAm_fId,o.fUsr_id,o.fType,fOrderId,fTrade_Price,fTrade_Count,fTrade_SumMoney,u.floginName,");
            sql.add("DATE_FORMAT(fTrade_DeadTime,'%s') as fTrade_DeadTime,DATE_FORMAT(fOrder_Time,'%s') as fOrder_Time,",
                    "%Y-%m-%d %H:%m:%s", "%Y-%m-%d %H:%m:%s");
            sql.add("fTrade_Object,fTrade_Status,fTrade_Method,c.fShortName from %s o left join %s c on o.fAm_fId=c.fId left join %s u on o.fUsr_id=u.fId where 1=1",
                    "t_userOtcOrder", "fvirtualcointype", "fuser");
            sql.add("and fTrade_Object = '%s'", currentUser.getFid());
            sql.add("and (fTrade_Status = '3' or fTrade_Status = '4' or fTrade_Status = '6')");
            sql.add("order by fOrder_Time desc");
            sql.open();
            for (Record record : sql) {
                orderListOver.add(record.getItems());
            }
            json.accumulate("overOrders", orderListOver);
            json.accumulate("code", 200);
            json.accumulate("message", "请求成功");
        } catch (Exception e) {
            json.accumulate("code", 500);
            json.accumulate("message", "获取失败");
        }
        return json.toString();
    }

    // 查询当前用户发布的广告
    @ResponseBody
    @RequestMapping(value = "m/trade/getUserOrders", produces = { JsonEncode })
    public String getUserOrders() {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        Fuser currentUser = GetCurrentUser(request);
        List<Map<String, Object>> ordersList = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            // 查询接单广场所有数据
            MyQuery getbuy = new MyQuery(mysql);
            getbuy.add(
                    "SELECT o.fId, v.fShortName fShortName,o.isCheck isCheck, o.fCreateTime, o.fAm_fId, o.fUsr_id, fUsr_Name,fUnitprice,fCount,fMoney,fOrdertype,fAdr,fSmallprice,fBigprice,fLowprice,fReceipttype,fMsg,isCertified,isUndo,o.fStatus fStatus FROM %s o",
                    "t_otcorders");
            getbuy.add(" LEFT JOIN %s v ON o.fAm_fId = v.fId", "fvirtualcointype");
            /* getbuy.add(" LEFT JOIN %s uo ON uo.fAd_Id = o.fId", "t_userotcorder"); */
            getbuy.add(" WHERE fUsr_Name = '%s'", currentUser.getFnickName());
            getbuy.add("order by fCreateTime desc");
            getbuy.open();
            for (Record record : getbuy) {
                ordersList.add(record.getItems());
            }
            json.accumulate("UserOrders", ordersList);
            json.accumulate("code", 200);
            json.accumulate("message", "请求成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.accumulate("code", 500);
            json.accumulate("message", "获取失败");
        }
        return json.toString();
    }
    
    @RequestMapping(value = "m/ryb/RYBWellet")
    public ModelAndView toRYBWellet() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryb/RYBWellet");
        return modelAndview;
    }

    /**
     * RYG主页
     * @return
     */
    @RequestMapping(value = "m/ryg/index")
    public ModelAndView rygIndex() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/ryg");
        return modelAndview;
    }

    /**
     * RYG详情
     * @return
     */
    @RequestMapping(value = "m/ryg/rygBill")
    public ModelAndView rygBill() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/rygBill");
        return modelAndview;
    }

    /**
     * 瑞积分详情
     * @return
     */
    @RequestMapping(value = "m/ryg/rypBill")
    public ModelAndView rypBill() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/rypBill");
        return modelAndview;
    }

    /**
     * CNY详情
     * @return
     */
    @RequestMapping(value = "m/ryg/cnyBill")
    public ModelAndView cnyBill() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/rypBill");
        return modelAndview;
    }

    /**
     * Cny提现
     * @return
     */
    @RequestMapping(value = "m/ryg/cnyWallet")
    public ModelAndView cnyWallet() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/cnyWallet");
        return modelAndview;
    }

    /**
     * RYH详情
     * @return
     */
    @RequestMapping(value = "m/ryg/ryhBill")
    public ModelAndView ryhBill() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/rypBill");
        return modelAndview;
    }

    /**
     * RYH提现
     * @return
     */
    @RequestMapping(value = "m/ryg/ryhWallet")
    public ModelAndView ryhWallet() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/ryhWallet");
        return modelAndview;
    }

    /**
     * 瑞积分兑换
     * @return
     */
    @RequestMapping(value = "m/ryg/rypTransfer")
    public ModelAndView rypTransfer() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/rypTransfer");
        return modelAndview;
    }

    /**
     * CNY转账
     * @return
     */
    @RequestMapping(value = "m/ryg/cnyTransfer")
    public ModelAndView cnyTransfer() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/cnyTransfer");
        return modelAndview;
    }

    /**
     * RYH转账
     * @return
     */
    @RequestMapping(value = "m/ryg/ryhTransfer")
    public ModelAndView ryhTransfer() {
        JspPage modelAndview = new JspPage(request);
        Fuser currentUser = GetCurrentUser(request);
        modelAndview.addObject("tid", currentUser.getFloginName());
        modelAndview.setJspFile("front/ryg/ryhTransfer");
        return modelAndview;
    }

    @ResponseBody
    @RequestMapping(value = "m/trade/IsCheckIdentity", produces = { JsonEncode })
    public String IsCheckIdentity() {
    	net.sf.json.JSONObject json = new net.sf.json.JSONObject();
    	try {
    		Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
    		if("".equals(fuser.getFidentityNo()) && "500233199301011414".equals(fuser.getFidentityNo())) {
    			json.accumulate("code", 500);
    			json.accumulate("message", "请先到安全中心进行实名认证");
    			return json.toString();
    		}
    		if(!fuser.getFpostRealValidate()) {
    			json.accumulate("code", 500);
    			json.accumulate("message", "请先到安全中心进行身份证照片认证");
    			return json.toString();
    		}
    		if(fuser.getFpostRealValidate()&&fuser.getFhasRealValidate()&&fuser.getFaudit() == 2) {
    			json.accumulate("code", 500);
    			json.accumulate("message", "身份认证审核中，请耐心等候");
    			return json.toString();
    		}
		} catch (Exception e) {
			e.printStackTrace();
			json.accumulate("code", 500);
	        json.accumulate("message", "请重新登陆");
	        return json.toString();
		}
    	json.accumulate("code", 200);
        json.accumulate("message", "身份已认证");
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "m/trade/transferMallCheck", produces = { JsonEncode })
    public String transferMallCheck() {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        try {
            Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
            if("".equals(fuser.getFidentityNo()) && "500233199301011414".equals(fuser.getFidentityNo())) {
                json.accumulate("code", 500);
                json.accumulate("message", "请先到安全中心进行实名认证");
                return json.toString();
            }
            if(!fuser.getFpostRealValidate()) {
                json.accumulate("code", 500);
                json.accumulate("message", "请先到安全中心进行身份证照片认证");
                return json.toString();
            }
            if(fuser.getFpostRealValidate()&&fuser.getFhasRealValidate()&&fuser.getFaudit() == 2) {
                json.accumulate("code", 500);
                json.accumulate("message", "身份认证审核中，请耐心等候");
                return json.toString();
            }
            if(fuser.getFaudit() != 1) {
                json.accumulate("code", 500);
                json.accumulate("message", "身份认证中，请耐心等候");
                return json.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.accumulate("code", 500);
            json.accumulate("message", "请重新登陆");
            return json.toString();
        }
        json.accumulate("code", 200);
        json.accumulate("message", "身份已认证");
        return json.toString();
    }

}
