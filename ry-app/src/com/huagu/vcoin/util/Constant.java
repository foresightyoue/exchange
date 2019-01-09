package com.huagu.vcoin.util;

public class Constant {
    public final static boolean isRelease = true;// must change when release
    public final static String WEBROOT = Configuration.getInstance().getValue("WEBROOT");
    public final static String Domain = Configuration.getInstance().getValue("Domain");
    public final static String GoogleAuthName = Configuration.getInstance().getValue("GoogleAuthName");
    public final static String MailName = Configuration.getInstance().getValue("MailName");

    public final static String Endpoint = Configuration.getInstance().getValue("Endpoint");
    public final static String AccessKeyId = Configuration.getInstance().getValue("AccessKeyId");
    public final static String AccessKeySecret = Configuration.getInstance().getValue("AccessKeySecret");
    public final static String BucketName = Configuration.getInstance().getValue("BucketName");
    public final static String OSSURL = Configuration.getInstance().getValue("OSSURL");
    public final static String IS_OPEN_OSS = Configuration.getInstance().getValue("IS_OPEN_OSS");
    public final static String AppLevel = Configuration.getInstance().getValue("AppLevel");
    public final static String authInfo = Configuration.getInstance().getValue("authInfo");
    public final static String userRegister1 = Configuration.getInstance().getValue("userRegister1");
    public final static String f_url = Configuration.getInstance().getValue("f_url");
    public final static String s_username = Configuration.getInstance().getValue("s_username");
    public final static String s_password = Configuration.getInstance().getValue("s_password");
    public final static String f_url_other = Configuration.getInstance().getValue("f_url_other");
    public final static int jayun_stop = Integer.parseInt(Configuration.getInstance().getValue("jayun.stop"));
    public final static String SettlementIP = Configuration.getInstance().getValue("SettlementIP");
    public final static String hLAccountEnable = Configuration.getInstance().getValue("HLAccountEnable");
    public final static String walletConfigEnable = Configuration.getInstance().getValue("WalletConfigEnable");
    public final static String ossSite = Configuration.getInstance().getValue("oss.site");
    public final static boolean closeLimit = false;

    public final static int VIP = 6;// VIP等级

    public static Long messageTime = 3 * 60 * 1000L;// 短信有效时间
    public static Long mailTime = 30 * 60 * 1000L;// 邮件有效时间
    public static Long twiceTime = 1 * 60 * 1000L;// 两次请求的间隔时间

    /*
     * 分页数量
     */
    public final static int RecordPerPage = 20;// 充值记录分页
    public final static int AppRecordPerPage = 10;// app分页数量
    public final static int SHOPPerPage = 16;// app分页数量

    public final static int VirtualCoinWithdrawTimes = 10;// 虚拟币每天提现次数
    public final static int CnyWithdrawTimes = 10;// 人民币每天体现次数
    public static final boolean TradeSelf = true;//

    public final static String uploadPicDirectory = "upload1" + "/" + "system";
    public final static String AdminShopDirectory = "upload" + "/" + "shop";
    public final static String IdentityPicDirectory = "upload" + "/" + "identity_picture";
    public final static String AdminArticleDirectory = "upload" + "/" + "admin_article";
    public final static String DataBaseDirectory = "upload" + "/" + "dataBase";
    public static final String EmailReg = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";// 邮箱正则
    //    public static final String PhoneReg = "^((1[0-9]{2})|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";
    public static final String PhoneReg = "^\\d+$";
    public final static int ErrorCountLimit = 10;// 错误N次之后需要等待2小时才能重试
    public final static int ErrorCountAdminLimit = 30;// 后台登录错误

    public final static double exchange = 6.5; // 写死人民币对Btc的汇率

    public final static double etcExchange = 7.1; // 以太坊对btc的汇率

    public final static double ltcExchange = 8.5; // 莱特币对btc的汇率

    public final static double btxjExchange = 10.5; // 比特现金对btc的汇率

    public final static double zlcExchange = 6.5; // 中农比对btc的汇率

    public final static double tdtExchange = 10.5; // 泰达币对btc的汇率

    public final static String SQL_DELET_FUSER = "delete\n" +
            "fuser,\n" +
            "emailvalidate,\n" +
            "fapi,\n" +
            "fasset,\n" +
            "fautotrade,\n" +
            "fbalanceearning,\n" +
            "fbalanceflow,\n" +
            "fbalancelog,\n" +
            "fbankinfo,\n" +
            "fbankinfo_withdraw,\n" +
            "fcapitaloperation,\n" +
            "fcoinvotelog,\n" +
            "fentrust,\n" +
            "fentrustplan,\n" +
            "fgoodsaddress,\n" +
            "fintrolinfo,\n" +
            "flotterylog,\n" +
            "fmessage,\n" +
            "foperationlog,\n" +
            "fproxy,\n" +
            "fquestion,\n" +
            "fscore_record,\n" +
            "fshareplanlog,\n" +
            "fshoppinglog,\n" +
            "fsubscriptionlog,\n" +
            "ftransportlog,\n" +
            "fusersetting,\n" +
            "fvalidateemail,\n" +
            "fvalidatemessage,\n" +
            "fvalidatemessage_copy,\n" +
            "fvirtualaddress,\n" +
            "fvirtualaddress_withdraw,\n" +
            "fvirtualcaptualoperation,\n" +
            "fvirtualoperationlog\n" +
            "from fuser fuser\n" +
            "LEFT JOIN emailvalidate emailvalidate  ON fuser.fId=emailvalidate.FUs_fId \n" +
            "LEFT JOIN fapi fapi ON fuser.fId=fapi.fuser\n" +
            "LEFT JOIN fasset fasset ON fuser.fId=fasset.fuser\n" +
            "LEFT JOIN fautotrade fautotrade ON fuser.fId=fautotrade.fuserid\n" +
            "LEFT JOIN fbalanceearning fbalanceearning ON fuser.fId=fbalanceearning.fuserid\n" +
            "LEFT JOIN fbalanceflow fbalanceflow ON fuser.fId=fbalanceflow.fuserid\n" +
            "LEFT JOIN fbalancelog fbalancelog ON fuser.fId=fbalancelog.fuserid \n" +
            "LEFT JOIN fbankinfo fbankinfo ON fuser.fId=fbankinfo.FUs_fId\n" +
            "LEFT JOIN fbankinfo_withdraw fbankinfo_withdraw ON fuser.fId=fbankinfo_withdraw.FUs_fId\n" +
            "LEFT JOIN fcapitaloperation fcapitaloperation ON fuser.fId=fcapitaloperation.FUs_fId\n" +
            "LEFT JOIN fcoinvotelog fcoinvotelog ON fuser.fId=fcoinvotelog.fuser\n" +
            "LEFT JOIN fentrust fentrust ON fuser.fId=fentrust.FUs_fId\n" +
            "LEFT JOIN fentrustplan  fentrustplan ON fuser.fId=fentrustplan.FUs_fId\n" +
            "LEFT JOIN fgoodsaddress fgoodsaddress ON fuser.fId=fgoodsaddress.fuserid\n" +
            "LEFT JOIN fintrolinfo fintrolinfo  ON fuser.fId=fintrolinfo.fuserid\n" +
            "LEFT JOIN flotterylog flotterylog ON fuser.fId=flotterylog.fuserid\n" +
            "LEFT JOIN fmessage fmessage ON fuser.fId=fmessage.FReceiverId\n" +
            "LEFT JOIN foperationlog foperationlog ON fuser.fId=foperationlog.fuid\n" +
            "LEFT JOIN fproxy fproxy  ON fuser.fId=fproxy.fuserid\n" +
            "LEFT JOIN fquestion fquestion ON fuser.fId=fquestion.fuid\n" +
            "LEFT JOIN fscore_record fscore_record ON fuser.fId=fscore_record.fuser\n" +
            "LEFT JOIN fshareplanlog fshareplanlog ON fuser.fId=fshareplanlog.fuserId\n" +
            "LEFT JOIN fshoppinglog fshoppinglog ON fuser.fId=fshoppinglog.fuserId\n" +
            "LEFT JOIN fsubscriptionlog fsubscriptionlog ON fuser.fId=fsubscriptionlog.fuser_fid\n" +
            "LEFT JOIN ftransportlog ftransportlog ON fuser.fId=ftransportlog.fuser\n" +
            "LEFT JOIN fusersetting fusersetting ON fuser.fId=fusersetting.fuser \n" +
            "LEFT JOIN fvalidateemail fvalidateemail ON fuser.fId=fvalidateemail.FUs_fId\n" +
            "LEFT JOIN fvalidatemessage fvalidatemessage ON fuser.fId=fvalidatemessage.FUs_fId\n" +
            "LEFT JOIN fvalidatemessage_copy fvalidatemessage_copy ON fuser.fId=fvalidatemessage_copy.FUs_fId\n" +
            "LEFT JOIN fvirtualaddress fvirtualaddress ON fuser.fId=fvirtualaddress.fuid\n" +
            "LEFT JOIN fvirtualaddress_withdraw fvirtualaddress_withdraw ON fuser.fId=fvirtualaddress_withdraw.fuid\n" +
            "LEFT JOIN fvirtualcaptualoperation fvirtualcaptualoperation ON fuser.fId=fvirtualcaptualoperation.FUs_fId2\n" +
            "LEFT JOIN fvirtualoperationlog  fvirtualoperationlog ON fuser.fId=fvirtualoperationlog.FUserId\n" +
            "WHERE fuser.fId=";
}
