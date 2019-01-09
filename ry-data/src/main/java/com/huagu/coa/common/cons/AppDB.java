package com.huagu.coa.common.cons;

import cn.cerc.jdb.core.ServerConfig;

public class AppDB {
    // 邮箱验证链接，status:0没有点击�?1已经点击
    public static final String emailvalidate = "emailvalidate"; // FOREIGN KEY (`FUs_fId`) REFERENCES `fuser` (`fId`)
    // 关于系统，各类介绍文�?
    public static final String Fabout = "fabout"; //
    // 系统管理员档�?
    public static final String fadmin = "fadmin"; //
    // 广告列表
    public static final String fads = "fads"; //
    //
    public static final String fapi = "fapi"; // FOREIGN KEY (`fuser`) REFERENCES `fuser` (`fId`)
    // 资讯列表
    public static final String Farticle = "Farticle"; //
    // 资讯类型
    public static final String farticletype = "farticletype"; //
    // 历史资产记录
    public static final String fasset = "fasset"; // FOREIGN KEY (`fuser`) REFERENCES `fuser` (`fId`)
    public static final String fautotrade = "fautotrade"; //
    public static final String fbalance = "fbalance"; //
    public static final String fbalanceearning = "fbalanceearning"; //
    public static final String fbalanceflow = "fbalanceflow"; //
    public static final String fbalancelog = "fbalancelog"; //
    public static final String fbalancetype = "fbalancetype"; //
    public static final String fbankin = "fbankin"; //
    public static final String fbankinfo = "fbankinfo"; //
    public static final String fbankinfo_withdraw = "fbankinfo_withdraw"; //
    // 资金记录
    public static final String fcapitaloperation = "fcapitaloperation"; //
    public static final String fcoinvote = "fcoinvote"; //
    public static final String fcoinvotelog = "fcoinvotelog"; //
    public static final String fcountlimit = "fcountlimit"; //
    public static final String fentrust = "fentrust"; //
    public static final String fentrustlog = "fentrustlog"; //
    public static final String fentrustplan = "fentrustplan"; //
    public static final String ffees = "ffees"; //
    public static final String ffriendlink = "ffriendlink"; //
    public static final String fgoods = "fgoods"; //
    public static final String fgoodsaddress = "fgoodsaddress"; //
    public static final String fgoodscomm = "fgoodscomm"; //
    public static final String fgoodtype = "fgoodtype"; //

    // 推广讯息
    public static final String Fintrolinfo = "Fintrolinfo"; //
    public static final String flevel_setting = "flevel_setting"; //
    public static final String flimittrade = "flimittrade"; //
    public static final String flog = "flog"; //
    public static final String flotteryaward = "flotteryaward"; //
    public static final String flotterylog = "flotterylog"; //
    public static final String flotteryrule = "flotteryrule"; //
    public static final String fmessage = "fmessage"; //
    public static final String foperationlog = "foperationlog"; //
    public static final String fpaycode = "fpaycode"; //
    public static final String fperiod = "fperiod"; //
    // 虚拟地址�?
    public static final String fpool = "fpool"; //
    public static final String fproxy = "fproxy"; //
    // 提问记录列表
    public static final String fquestion = "fquestion"; //
    public static final String frole = "frole"; //
    public static final String frole_security = "frole_security"; //
    public static final String fscore = "fscore"; //
    public static final String fscore_record = "fscore_record"; //
    public static final String fscore_setting = "fscore_setting"; //
    public static final String fsecurity = "fsecurity"; //
    public static final String fshareplan = "fshareplan"; //
    public static final String fshareplanlog = "fshareplanlog"; //
    public static final String fshoppinglog = "fshoppinglog"; //
    public static final String fsubscription = "fsubscription"; //
    public static final String fsubscriptiondetail = "fsubscriptiondetail"; //
    public static final String fsubscriptionlog = "fsubscriptionlog"; //
    public static final String fsubscriptionrule = "fsubscriptionrule"; //
    public static final String fsystemargs = "fsystemargs"; //
    // 历史收盘价列�?
    public static final String ftradehistory = "ftradehistory"; //
    // 法币类型匹配列表
    public static final String Ftrademapping = "ftrademapping"; //
    public static final String ftransportlog = "ftransportlog"; //
    // 用户信息�?
    public static final String Fuser = "Fuser"; //
    // 用户设置
    public static final String fusersetting = "fusersetting"; // FOREIGN KEY (`fuser`) REFERENCES `fuser` (`fId`)
    // �?要发送邮件列表，status�?0未发送，1已经发�??
    public static final String fvalidateemail = "fvalidateemail"; // FOREIGN KEY (`FUs_fId`) REFERENCES `fuser` (`fId`)
    // �?要发送的短信，status�?0未发送，1已经发�??
    public static final String fvalidatemessage = "fvalidatemessage"; // FOREIGN KEY (`FUs_fId`) REFERENCES `fuser`
                                                                      // (`fId`)
    public static final String fvirtualaddress = "fvirtualaddress"; //
    // FIXME: 此数据表与对象名称不同，后须统一�?
    public static final String FvirtualaddressWithdraw = "FvirtualaddressWithdraw"; // fvirtualaddress_withdraw
    // 虚拟币操作�?�表
    public static final String fvirtualcaptualoperation = "fvirtualcaptualoperation"; //
    // 虚拟币类型列�?
    public static final String fvirtualcointype = "fvirtualcointype"; //
    public static final String fvirtualoperationlog = "fvirtualoperationlog"; //
    // 虚拟币钱�?
    public static final String fvirtualwallet = "fvirtualwallet"; //
    public static final String fwebbaseinfo = "fwebbaseinfo"; //
    public static final String fwithdrawfees = "fwithdrawfees"; //
    public static final String my18_cz_temp = "my18_cz_temp"; //
    public static final String my18_pay_temp = "my18_pay_temp"; //
    // 系统充�?�银行卡信息，包括银行�?�卡号�?�姓�?, 状�?�：（是否停用银行卡�?
    public static final String systembankinfo = "systembankinfo"; //

    // 每日对账�?
    public static final String ffinancestatement = "ffinancestatement";
    // 财务人员转账数量
    public static final String fwallettransferrecord = "fwallettransferrecord";

    // USDT 汇主地址�?客户地址转比特币记录
    public static final String omni_btc_balance = "omni_btc_balance";

    // 自动参数设置�?
    public static final String fAutoSet = "fautoset";

    // 钱包日志�?
    public static final String t_walletlog = "t_walletlog";
    
    //奖励
    public static final String fintrolinfo = "fintrolinfo";//
    
    //ctc
    public static final String fctcorder = "fctcorder";//
    
    //管理员�?�择的需要对账的用户ID
    public static final String fcheckbalance_fusid = "fcheckbalance_fusid";
    
    //指定用户对账信息�?
    public static final String fuserReconciliationRecord = "fuserReconciliationRecord";
    
    //划账
    public static final String ftransfer = "ftransfer";
    
    //溢出追查�?
    public static final String fcheckoverflowlist = "fcheckoverflowlist";
    
    // 及时通讯�?
    public static final String fmessaget_ = "fmessaget_";
    //地址�?
    public static final String t_withdrawallog = "t_withdrawallog";
    // OTC�?
    public static final String t_userotcorder = "t_userotcorder";
    //第三方数据表
    public static final String fzbkline = "fzbkline";
    //用户收款账户表
    public static final String t_userreceipt = "t_userreceipt";

    // 设置聚安发�?�登录�?�知模版编号
    public static final String loginTemplateId = "yt01";

    public static boolean enableTaskService() {
        String taskServiceEnabled = "task.service";
        ServerConfig config = ServerConfig.getInstance();
        return "1".equals(config.getProperty(taskServiceEnabled, null));
    }

    public static boolean isTestVersion() {
        ServerConfig config = ServerConfig.getInstance();
        return "test".equals(config.getProperty("version", "beta"));
    }
}
