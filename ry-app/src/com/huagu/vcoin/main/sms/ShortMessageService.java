package com.huagu.vcoin.main.sms;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 云片短信发送
 * 
 * @author WeiYF
 *
 */
public class ShortMessageService {
    // 查账户信息的http地址
    private static String URI_GET_USER_INFO = "https://sms.yunpian.com/v2/user/get.json";

    // 智能匹配模板发送接口的http地址
    private static String URI_SEND_SMS = "https://sms.yunpian.com/v2/sms/single_send.json";

    // 编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";

    // 发送通知短信
    public static void send(String phone, String content) {
        try {
            // 修改为您的apikey.apikey可在官网（http://www.yunpian.com)登录后获取
            String apikey = "a5762d3adefd91b00424a7c153561295";

            // 修改为您要发送的手机号
            String mobile = phone;

            /**************** 查账户信息调用示例 *****************/
            System.out.println(ShortMessageService.getUserInfo(apikey));

            /**************** 使用智能匹配模板接口发短信(推荐) 方式1 *****************/
            // 设置您要发送的内容(内容必须和某个模板匹配
            String text = content;
            // 发短信调用示例
            System.out.println("调用结果==>" + ShortMessageService.sendSms(apikey, text, mobile));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 发送验证码
    public static int sendMsg(String phone, String content, String msg) {
        int code = 0;
        try {
            // 修改为您的apikey.apikey可在官网（http://www.yunpian.com)登录后获取
            String apikey = "a5762d3adefd91b00424a7c153561295";

            // 修改为您要发送的手机号
            String mobile = phone;

            /**************** 查账户信息调用示例 *****************/
            System.out.println(ShortMessageService.getUserInfo(apikey));

            /**************** 使用智能匹配模板接口发短信(推荐) 方式1 *****************/
            // 设置您要发送的内容(内容必须和某个模板匹配
            String text = content;
            // 发短信调用示例
            System.out.println("调用结果==>" + ShortMessageService.sendSms(apikey, text, mobile));
            code = 0;
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
            cd.setField("fMsg", "短信通知发送成功");
            cd.setField("fFacilitator", "云片网");
            cd.setField("fCreateTime", new Date());
            cd.post();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            code = 1;
            e.printStackTrace();
        }
        return code;
    }
    
    /**
     * 取账户信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */

    public static String getUserInfo(String apikey) throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        return post(URI_GET_USER_INFO, params);
    }

    /**
     * 智能匹配模板接口发短信
     *
     * @param apikey
     *            apikey
     * @param text
     *            短信内容
     * @param mobile
     *            接受的手机号
     * @return json格式字符串
     * @throws IOException
     */

    public static String sendSms(String apikey, String text, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", text);
        params.put("mobile", mobile);
        return post(URI_SEND_SMS, params);
    }


    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url
     *            提交的URL
     * @param paramsMap
     *            提交<参数，值>Map
     * @return 提交响应
     */

    public static String post(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, ENCODING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

    public static boolean isTrue() {
        boolean status = false;
        if (Constant.jayun_stop == 1) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    public static String getCode() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return result;
    }
    private static Logger logger = LoggerFactory.getLogger(ShortMessageService.class);

    public static boolean CheckCode(String phone, String code, Date time) {
        boolean status = false;
        // 获取前2分钟的时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.MINUTE, -2);
        String format = formatter.format(calendar.getTime());
        String format1 = formatter.format(time);
        logger.info("进入验证方法+ startTime="+format+"endTime="+format1);
        Mysql mysql = new Mysql();
        MyQuery jy = new MyQuery(mysql);
        jy.add("select * from %s ", "fsms");
        jy.add(" where fTelephone = '%s' ", phone);
        jy.add(" and fCode = '%s' ", code);
        jy.add(" and fCreateTime between '%s' and '%s' ", format, format1);
        jy.open();
        if (!jy.eof()) {
            if (jy.size() > 0) {
                status = true;
            } else {
                status = false;
            }
        }
        return status;
    }
}
