package com.huagu.vcoin.main.Enum;

public class LendEntrustFinishLogTypeEnum {
    public static final int BUY = 0;// 买
    public static final int SELL = 1;// 卖

    public static String getEnumString(int value) {
        String name = "";
        if (value == BUY) {
            name = "买";
        } else if (value == SELL) {
            name = "卖";
        }

        return name;
    }

}
