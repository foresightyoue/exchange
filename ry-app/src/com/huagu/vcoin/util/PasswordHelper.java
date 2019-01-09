package com.huagu.vcoin.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;

public class PasswordHelper {

    private static String algorithmName = "md5";
    private static int hashIterations = 2;

    public static String encryString(String pwd, String salt) {
        String newPassword = new SimpleHash(algorithmName, Utils.getMD5_32_xx(pwd), ByteSource.Util.bytes(salt),
                hashIterations).toHex();

        return newPassword;
    }

    public static void main(String args[]) throws Exception {
        Mysql handle = new Mysql();
        MyQuery ds = new MyQuery(handle);
        ds.add("select fId,fLoginPassword,fTradePassword,salt from %s", "fuser");
        ds.open();
        while (ds.fetch()) {
            String loginPassword = ds.getString("fLoginPassword");
            String tradePassword = ds.getString("fTradePassword");
            String salt = ds.getString("salt");
            String newLoginPwd = new SimpleHash(algorithmName, loginPassword, ByteSource.Util.bytes(salt),
                    hashIterations).toHex();
            String newtradePwd = "";
            if (tradePassword != null && !"".equals(tradePassword)) {
                newtradePwd = new SimpleHash(algorithmName, tradePassword, ByteSource.Util.bytes(salt), hashIterations)
                        .toHex();
            }
            ds.edit();
            ds.setField("fLoginPassword", newLoginPwd);
            if (!"".equals(newtradePwd)) {
                ds.setField("fTradePassword", newtradePwd);
            }
            ds.post();
        }
        // 跟新后台用户
        MyQuery dsAdmin = new MyQuery(handle);
        dsAdmin.add("select fId,fPassword,salt from %s", "fadmin");
        dsAdmin.open();
        while (dsAdmin.fetch()) {
            String fPassword = dsAdmin.getString("fPassword");
            String salt = dsAdmin.getString("salt");
            String newPassword = new SimpleHash(algorithmName, fPassword, ByteSource.Util.bytes(salt), hashIterations)
                    .toHex();
            dsAdmin.edit();
            dsAdmin.setField("fPassword", newPassword);
            dsAdmin.post();
        }
    }
}