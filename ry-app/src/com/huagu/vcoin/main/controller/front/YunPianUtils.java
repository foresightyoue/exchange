package com.huagu.vcoin.main.controller.front;

import com.huagu.vcoin.main.sms.ShortMessageService;

/**
 * 云片发送短信验证码
 */
public class YunPianUtils {

    public static Object sendMsg(String areaCode,String phone,String code) {
        if (!"86".equals(areaCode)) {
            phone = "+" + areaCode + phone;
        }
        if(areaCode.equals("86")){
            ShortMessageService.sendMsg(phone, "【瑞银钱包】您的验证码是" + code, code);
        }else {
            ShortMessageService.sendMsg(phone, "【RYWallet】Your verification code is " + code, code);
        }
        return null;
    }
}
