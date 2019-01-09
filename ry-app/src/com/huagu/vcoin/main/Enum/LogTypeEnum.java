package com.huagu.vcoin.main.Enum;

public class LogTypeEnum {
    public static final int User_LOGIN = 1;//
    public static final int User_BIND_PHONE = 2;//
    public static final int User_BIND_EMAIL = 3;//
    public static final int User_SET_TRADE_PWD = 4;//
    public static final int User_UPDATE_TRADE_PWD = 5;//
    public static final int User_UPDATE_LOGIN_PWD = 6;//
    public static final int User_UPDATE_GOOGLE = 7;//
    public static final int User_BIND_GOOGLE = 8;//
    public static final int User_CERT = 9;//
    public static final int User_RESET_PWD = 10;//
    public static final int Admin_LOGIN = 11;//
    public static final int Admin_ADD = 12;//
    public static final int Admin_UPDATE = 13;//
    public static final int Admin_ROLE = 14;//

    public static String getEnumString(int value) {
        String name = "";
        if (value == User_LOGIN) {
            name = "用户登录";
        } else if (value == User_BIND_PHONE) {
            name = "绑定手机";
        } else if (value == User_BIND_EMAIL) {
            name = "绑定邮箱";
        } else if (value == User_SET_TRADE_PWD) {
            name = "设置交易密码";
        } else if (value == User_UPDATE_TRADE_PWD) {
            name = "更新交易密码";
        } else if (value == User_UPDATE_LOGIN_PWD) {
            name = "更新登录密码";
        } else if (value == User_UPDATE_GOOGLE) {
            name = "更新谷歌验证器";
        } else if (value == User_BIND_GOOGLE) {
            name = "绑定谷歌验证器";
        } else if (value == User_CERT) {
            name = "实名认证";
        } else if (value == User_RESET_PWD) {
            name = "重置登录密码";
        } else if (value == Admin_LOGIN) {
            name = "管理员登录";
        } else if (value == Admin_ADD) {
            name = "新增管理员";
        } else if (value == Admin_UPDATE) {
            name = "修改管理员";
        } else if (value == Admin_ROLE) {
            name = "修改管理员角色";
        }

        return name;
    }

}
