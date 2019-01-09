package com.huagu.vcoin.main.Enum;

public class CoinTypeEnum {
    public static final int FB_CNY_VALUE = 0;// 法币
    public static final int FB_COIN_VALUE = 1;// 虚拟币
    public static final int COIN_VALUE = 2;

    public static String getEnumString(int value) {
        String name = "";
        if (value == FB_CNY_VALUE) {
            name = "法币-美元";
        } else if (value == FB_COIN_VALUE) {
            name = "法币-虚拟币";
        } else if (value == COIN_VALUE) {
            name = "普通虚拟币";
        }
        return name;
    }

}
