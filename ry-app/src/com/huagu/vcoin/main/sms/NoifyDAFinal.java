package com.huagu.vcoin.main.sms;

import com.huagu.vcoin.main.comm.AliyunSMSConfig;

/**
 * 新的客户订单
 */
public class NoifyDAFinal extends CustomNotifyMobile {
    private String code;

    @Override
    public String getTemplateCode() {
        return AliyunSMSConfig.NoifyDAFinal;
    }

    @Override
    public String getContent() {
        return String.format("{code:'%s'}", code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static void main(String[] args) {
        NoifyDAFinal obj = new NoifyDAFinal();
        obj.setCode("334455");
        obj.send("18702165754");
    }
}
