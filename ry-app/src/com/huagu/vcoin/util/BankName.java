package com.huagu.vcoin.util;

public class BankName {
    private Integer cardId;
    private String cardName;

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public static String bankName(int key) {
        switch (key) {
        case 1: return "工商银行";
        case 2: return "建设银行";
        case 3: return "农业银行";
        case 4: return "交通银行";
        case 5: return "招商银行";
        case 6: return "邮政储蓄银行";
        case 7: return "中国银行";
        case 8: return "中国民生银行";
        case 9: return "中国光大银行";
        case 10: return "兴业银行";
        case 11: return "上海浦东发展银行";
        case 12: return "中信银行";
        case 13: return "平安银行";
        case 14: return "华夏银行";
        case 15:
        default:
            return "其他银行";
        }
    }
    public static void main(String[] args) {
        BankName s=new BankName();
        System.out.println(s.bankName(15));
    }
}
