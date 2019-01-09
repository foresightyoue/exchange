package com.huagu.vcoin.main.sms;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public abstract class CustomNotifyMobile {
    private static final Logger log = Logger.getLogger(CustomNotifyMobile.class);

    public abstract String getTemplateCode();

    public abstract String getContent();

    private String message;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean send(String phoneNumber) {
        if (phoneNumber.startsWith("+86")) {
            phoneNumber = phoneNumber.substring(2, phoneNumber.length() - 1);
        }
        if (!phoneNumber.matches("1\\d{10}")) {
            throw new RuntimeException(String.format("%s 不是一个有效的手机号码！", phoneNumber));
        }

        Aliyundysms sms = new Aliyundysms();
        sms.setPhoneNumbers(phoneNumber);
        sms.setTemplateCode(getTemplateCode());
        if (sms.send(getContent())) {
            log.info("手机：" + phoneNumber + "发送短信成功");
            return true;
        }
        this.setMessage(sms.getMessage());
        return false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
