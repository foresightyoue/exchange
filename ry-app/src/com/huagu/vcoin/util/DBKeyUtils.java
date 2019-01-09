package com.huagu.vcoin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: hqs
 * @Date: 2018/12/24 16:20
 * @Description: 生成流水号
 */
public class DBKeyUtils {

    private static long iCount = 5001L;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 获取流水号
     * @return
     */
    public static String getSerialNo() {
        return getSerialNo("");
    }

    public static String getSerialNo(String sPrefix) {
        if(iCount > 9999L) iCount = 5001L;
        return sPrefix + getFormatDate() + iCount++;
    }

    private static String getFormatDate() {
        return sdf.format(new Date());
    }

}
