package com.huagu.vcoin.util;

import com.huagu.vcoin.main.model.Fvirtualcointype;

import java.util.Base64;

public class DeCodeUtil {


    public static void deCode(Fvirtualcointype fvirtualcointype) {
        if (null == fvirtualcointype) {
            return;
        }

        if(fvirtualcointype.getfShortName().toUpperCase().equals("BTC")) {
            if(null != fvirtualcointype.getFaccess_key() && fvirtualcointype.getFaccess_key().length() > 30) {
                String buser = new String(Base64.getDecoder().decode(fvirtualcointype.getFaccess_key()));
                fvirtualcointype.setFaccess_key(buser.substring(26-3-1,26-3-1 + 14));
            }
            if(null != fvirtualcointype.getFsecrt_key() && fvirtualcointype.getFsecrt_key().length() > 20) {
                String bkey = new String(Base64.getDecoder().decode(fvirtualcointype.getFsecrt_key()));
                fvirtualcointype.setFsecrt_key(bkey.substring(9 - 1,9 - 1 + 16));
            }
            if(null != fvirtualcointype.getMainAddr() && fvirtualcointype.getMainAddr().length() > 50) {
                fvirtualcointype.setMainAddr(fvirtualcointype.getMainAddr().substring(26 - 15 -1 ,26 - 15 -1 + 34));
            }

        }else if(fvirtualcointype.getfShortName().toUpperCase().equals("USDT")) {
            if(null != fvirtualcointype.getFaccess_key() && fvirtualcointype.getFaccess_key().length() > 30) {
                String buser = new String(Base64.getDecoder().decode(fvirtualcointype.getFaccess_key()));
                fvirtualcointype.setFaccess_key(buser.substring(17 - 1,17 - 1 + 14));
            }
            if(null != fvirtualcointype.getFsecrt_key() && fvirtualcointype.getFsecrt_key().length() > 20) {
                String bkey = new String(Base64.getDecoder().decode(fvirtualcointype.getFsecrt_key()));
                fvirtualcointype.setFsecrt_key(bkey.substring(4 - 1 ,4 - 1 + 16));
            }
            if(null != fvirtualcointype.getMainAddr() && fvirtualcointype.getMainAddr().length() > 50) {
                fvirtualcointype.setMainAddr(fvirtualcointype.getMainAddr().substring(26 - 3 -1 ,26 - 3 -1 + 34));
            }
        }else {
            if(null != fvirtualcointype.getFpassword() && fvirtualcointype.getFpassword().length() > 30) {
                String ekey = new String(Base64.getDecoder().decode(fvirtualcointype.getFpassword()));
                fvirtualcointype.setFpassword(ekey.substring(21 -1 ,21 -1 + 14));
            }
            if(null != fvirtualcointype.getMainAddr() && fvirtualcointype.getMainAddr().length() > 60) {
                fvirtualcointype.setMainAddr("0x" + fvirtualcointype.getMainAddr().substring(26 - 10 -1  ,26 - 10 -1 + 40));
            }
        }
    }

    public static void main(String... args) {
        Fvirtualcointype fvirtualcointype = new Fvirtualcointype();
        deCode(fvirtualcointype);
    }
}
