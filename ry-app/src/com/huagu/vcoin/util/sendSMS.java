package com.huagu.vcoin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * 文件名称：sendSMS_demo.java
 * 
 * 文件作用：美联软通 http接口使用实例
 * 
 * 创建时间：2012-05-18
 * 
 * 
 * 返回值 说明 success:msgid 提交成功，发送状态请见4.1 error:msgid 提交失败 error:Missing username
 * 用户名为空 error:Missing password 密码为空 error:Missing apikey APIKEY为空 error:Missing
 * recipient 手机号码为空 error:Missing message content 短信内容为空 error:Account is
 * blocked 帐号被禁用 error:Unrecognized encoding 编码未能识别 error:APIKEY or password
 * error APIKEY 或密码错误 error:Unauthorized IP address 未授权 IP 地址 error:Account
 * balance is insufficient 余额不足 error:Black keywords is:党中央 屏蔽词
 */

public class sendSMS {
    private static Logger log = Logger.getLogger(sendSMS.class);

    /**
     * @param args
     * @throws IOException
     */
    public static void send_old(String name, String password, String key, String content, String tel)
            throws IOException {
        try {
            // 发送内容

            // 创建StringBuffer对象用来操作字符串
            StringBuffer sb = new StringBuffer("http://m.5c.com.cn/api/send/?");

            // APIKEY
            sb.append("apikey=" + key);

            // 用户名
            sb.append("&username=" + name);

            // 向StringBuffer追加密码
            sb.append("&password=" + password);

            // 向StringBuffer追加手机号码
            sb.append("&mobile=" + tel);

            // 向StringBuffer追加消息内容转URL标准码
            sb.append("&content=" + URLEncoder.encode(content, "GBK"));

            // 创建url对象
            URL url = new URL(sb.toString());

            // 打开url连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置url请求方式 ‘get’ 或者 ‘post’
            connection.setRequestMethod("POST");

            // 发送
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            // 返回发送结果
            String inputline = in.readLine();

            // 输出结果
            log.info("send mobile: " + tel + ", result: " + inputline);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean send(String name, String password, String key, String content, String tel)
            throws IOException {
        boolean flag = false;
        try {
            // 创建StringBuffer对象用来操作字符串
            String httpUrl = "http://api.smsbao.com/sms";
            if (tel.indexOf("+") > 0) {
                httpUrl = "http://api.smsbao.com/wsms";
            }
            StringBuffer httpArg = new StringBuffer();
            httpArg.append("u=").append(name).append("&");
            httpArg.append("p=").append(md5(password)).append("&");
            httpArg.append("m=").append(tel).append("&");
            httpArg.append("c=").append(encodeUrlString(content, "UTF-8"));

            String result = request(httpUrl, httpArg.toString());
            if ("0".equals(result)) {
                flag = true;
            }
            // 输出结果
            log.info("send mobile: " + tel + ", result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String[] args) {

        String testUsername = "bizhangwang88"; // 在短信宝注册的用户名
        String testPassword = "9bAERQv5HcI8vNVX"; // 在短信宝注册的密码
        String testPhone = "+19177206480";
        String testContent = "【BZW365】Your verification code is: 746175.Please do not reveal your verification code to others.If not the operation, please change the password in time to prevent stolen!"; // 注意测试时，也请带上公司简称或网站签名，发送正规内容短信。千万不要发送无意义的内容：例如
        // 测一下、您好。否则可能会收不到

        String httpUrl = "http://api.smsbao.com/sms";
        if (testPhone.indexOf("+") > 0) {
            httpUrl = "http://api.smsbao.com/wsms";
        }

        StringBuffer httpArg = new StringBuffer();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(md5(testPassword)).append("&");
        httpArg.append("m=").append(testPhone).append("&");
        httpArg.append("c=").append(encodeUrlString(testContent, "UTF-8"));

        String result = request(httpUrl, httpArg.toString());
        System.out.println(result);
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = reader.readLine();
            if (strRead != null) {
                sbf.append(strRead);
                while ((strRead = reader.readLine()) != null) {
                    sbf.append("\n");
                    sbf.append(strRead);
                }
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static String encodeUrlString(String str, String charset) {
        String strret = null;
        if (str == null)
            return str;
        try {
            strret = java.net.URLEncoder.encode(str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strret;
    }

}
