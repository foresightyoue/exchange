package com.huagu.vcoin.main.auto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huagu.vcoin.main.sms.NoifyDAFinal;

@Component
public class SendSMSCF {
    private static final Logger log = LoggerFactory.getLogger(SendSMSCF.class);

    /** 发送短信 **/
    @SuppressWarnings({ "deprecation", "resource" })
    public boolean send(String mob, String msg) {
        String str = "";
        try {
            // 创建HttpClient实例
            HttpClient httpclient = new DefaultHttpClient();

            // 构造一个post对象
            HttpPost httpPost = new HttpPost("http://120.55.197.77:1210/Services/MsgSend.asmx/SendMsg");
            // 添加所需要的post内容
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("userCode", "blockchain"));
            nvps.add(new BasicNameValuePair("userPass", "Block2017"));
            nvps.add(new BasicNameValuePair("DesNo", mob));
            nvps.add(new BasicNameValuePair("Msg", msg));
            nvps.add(new BasicNameValuePair("Channel", "0"));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                str = convertStreamToString(instreams);
                log.debug(str);
            }

            Document doc = null;
            doc = DocumentHelper.parseText(str); // 将字符串转为XML

            if (doc == null)
                return false;
            Element rootElt = doc.getRootElement(); // 获取根节点
            if (rootElt == null)
                return false;
            // System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
            log.debug("根节点的值：" + rootElt.getText()); // 拿到根节点的名称
            if (rootElt.getText() == null || "".equals(rootElt.getText()))
                return false;
            if (Long.parseLong(rootElt.getText()) > 0) {
                return true;
            } else {
                return false;
            }

        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public boolean sendAliSms(String code, String mobile) {
        NoifyDAFinal obj = new NoifyDAFinal();
        obj.setCode(code);
        log.warn(String.format("mobile: %s, result: %s", mobile, code));
        return obj.send(mobile);
    }

    public static void main(String[] args) {
        SendSMSCF smsSender = new SendSMSCF();
        smsSender.send("18576659165", "您的验证码是：351736。请不要把验证码泄露给其他人。如非本人操作，请及时修改密码以防被盗！【chain】");
    }
}
