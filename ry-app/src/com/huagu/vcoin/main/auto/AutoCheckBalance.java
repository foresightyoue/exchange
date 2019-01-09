package com.huagu.vcoin.main.auto;

import org.apache.log4j.Logger;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationInStatusEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationTypeEnum;

import cn.cerc.jdb.core.DataSet;
import cn.cerc.jdb.core.TDate;
import cn.cerc.jdb.core.TDateTime;
import site.jayun.vcoin.wallet.BTCUtils;
import site.jayun.vcoin.wallet.ETHUtils;
import site.jayun.vcoin.wallet.OmniUtils;
import site.jayun.vcoin.wallet.WalletConfig;
import site.jayun.vcoin.wallet.WalletFactory;
import site.jayun.vcoin.wallet.WalletUtil;

/**
 * 每晚12点自动计算昨日的钱包余额
 */
public class AutoCheckBalance {
    private static final Logger log = Logger.getLogger(AutoCheckBalance.class);

    private TDateTime today = TDate.Today();
    private TDateTime yesterday = TDate.Today().incDay(-1);

    private String dateFrom;
    private String dateTo;
    private Mysql handle = new Mysql();

    public void execute() {
        dateFrom = String.format("%s 00:00:00", today);
        dateTo = String.format("%s 23:59:59", today);

        MyQuery cdsCoin = new MyQuery(handle);
        cdsCoin.add("select * from %s", AppDB.fvirtualcointype);
        // cdsCoin.add("where fId=6");
        cdsCoin.open();

        while (cdsCoin.fetch()) {
            WalletConfig config = new WalletConfig();
            config.setAccessKey(cdsCoin.getString("faccess_key"));
            config.setSecretKey(cdsCoin.getString("fsecrt_key"));
            config.setIP(cdsCoin.getString("fip"));
            config.setPort(cdsCoin.getString("fport"));
            config.setPassword(cdsCoin.getString("fpassword"));
            config.setMainAddress(cdsCoin.getString("mainAddr"));

            boolean isEth = cdsCoin.getBoolean("fisEth");

            String coinType = cdsCoin.getString("fShortName");
            WalletUtil util;
            if ("USDT".equals(coinType)) {
                util = new OmniUtils(config);
            } else {
                util = WalletFactory.build(config, isEth ? "ETH" : "BTC");
            }

            String coinId = cdsCoin.getString("fId");
            DataSet dataYes = getYesterday(coinId);

            MyQuery dataDaily = new MyQuery(handle);
            dataDaily.add("select * from %s", "ffinancestatement");
            dataDaily.add("where fCoinType='%s'", coinId);
            dataDaily.add("and fStatisticsTime='%s'", today);
            dataDaily.open();
            if (dataDaily.eof()) {
                dataDaily.append();
                dataDaily.setField("fCoinType", coinId);
                dataDaily.setField("fStatisticsTime", today);
                dataDaily.setField("createDate_", TDateTime.Now());
            } else {
                dataDaily.edit();
            }

            // 今日期初余额 = 昨日期末余额
            double initBookBalance = dataYes.getDouble("fCurrentBookBalance");
            double initWalletBalance = dataYes.getDouble("fCurrentWalletBalance");
            double initDiffBalance = initBookBalance - initWalletBalance;

            dataDaily.setField("fBeforeBookBalance", initBookBalance);
            dataDaily.setField("fBeforeWalletBalance", initWalletBalance);
            dataDaily.setField("fBeforeDifference", initDiffBalance);

            // 本期余额
            double incBookBalance = getIncBookBalance(coinId);
            double incWalltBalance = getIncWalltBalance(coinType, util);
            double incDiffBalance = incBookBalance - incWalltBalance;

            dataDaily.setField("fCurrentBookBalance", incBookBalance);
            dataDaily.setField("fCurrentWalletBalance", incWalltBalance);
            dataDaily.setField("fCurrentDifference", incDiffBalance);

            // 本期充值
            double bookRecharge = getBookRecharge(coinId);
            double walletRecharge = getWalletRecharge(coinType, util);

            dataDaily.setField("fBookRecharge", bookRecharge);
            dataDaily.setField("fWalletRecharge", walletRecharge);

            // 本期提币
            double bookWithdrawal = getBookWithdrawal(coinId);
            double walletWithdrawal = getWalletWithdrawal(coinType, util);
            dataDaily.setField("fBookWithdrawal", bookWithdrawal);
            dataDaily.setField("fWalletWithdrawal", walletWithdrawal);

            // 本期账面正在提币中
            double bookWithdrawaling = getBookWithdrawaling(coinId);
            dataDaily.setField("fBookWithdrawaling", bookWithdrawaling);

            // 买入卖出
            double totalBuy = getTotalBuy(today, coinId);
            double totalSell = getTotalSell(today, coinId);
            dataDaily.setField("fBuyOrderSum", totalBuy);
            dataDaily.setField("fSellOrderSum", totalSell);

            // 本期调整
            double manualAdj = getManualAdj(today, coinId);
            double handIn = getHandIn(today, coinId);
            double handOut = getHandOut(today, coinId);

            dataDaily.setField("fManualAdjustment", manualAdj);
            dataDaily.setField("fHandIn", handIn);
            dataDaily.setField("fHandOut", handOut);
            dataDaily.setField("updateDate_", TDateTime.Now());
            dataDaily.post();
        }
    }

    // 财务期初记录
    public DataSet getYesterday(String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select * from %s", "ffinancestatement");
        ds.add("where fCoinType='%s'", coinId);
        ds.add("and fStatisticsTime='%s'", yesterday);
        ds.open();
        log.info("财务期初记录 " + ds.getCommandText());

        // 昨日无记录，初始化为0
        if (ds.eof()) {
            DataSet dataSet = new DataSet();
            dataSet.append();
            dataSet.setField("fCurrentBookBalance", 0);
            dataSet.setField("fCurrentWalletBalance", 0);
            return dataSet;
        }
        return ds;
    }

    // 每日账面余额汇总
    private double getIncBookBalance(String coinId) {
        // 查询系统参数表是否开启统计机器人订单
        String isStatisticsRobot = "off";
        MyQuery ds0 = new MyQuery(handle);
        ds0.add("select fValue from %s", AppDB.fsystemargs);
        ds0.add("where fKey = '%s'", "isStatisticsRobot");
        ds0.open();
        if (!ds0.eof()) {
            isStatisticsRobot = ds0.getString("fValue");
        }
        MyQuery ds = new MyQuery(handle);
        ds.add("select (sum(fTotal) + sum(fFrozen)) as amount_");
        ds.add("from %s", AppDB.fvirtualwallet);
        ds.add("where fVi_fId='%s'", coinId);
        if ("off".equals(isStatisticsRobot)) {
            ds.add("and fuid not in (%d,%d)", 2, 33);
        }
        ds.open();
        log.info("每日账面余额汇总 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    // 每日钱包余额汇总
    private double getIncWalltBalance(String coinType, WalletUtil util) {
        try {
            if (util instanceof OmniUtils) {
                OmniUtils omni = (OmniUtils) util;
                return omni.getOmniWalletBalance(OmniUtils.propertyId);
            }

            if (util instanceof ETHUtils) {
                ETHUtils eth = (ETHUtils) util;
                return eth.getWalletBalance();
            }

            BTCUtils btc = (BTCUtils) util;
            return btc.getWalletBalance();
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    // 本期账面充值汇总
    private double getBookRecharge(String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select fVi_fId2 as coinId,sum(fAmount) as amount_,sum(ffees) as fee_");
        ds.add("from %s", AppDB.fvirtualcaptualoperation);
        ds.add("where fVi_fId2='%s'", coinId);
        ds.add("and fType=%d and fStatus=%d", VirtualCapitalOperationTypeEnum.COIN_IN,
                VirtualCapitalOperationInStatusEnum.SUCCESS);
        ds.add("and fCreateTime between '%s' and '%s'", dateFrom, dateTo);
        ds.add("group by fVi_fId2");
        ds.open();
        log.info("每日账面充值汇总 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    // TODO 每日钱包充值汇总
    private double getWalletRecharge(String coinType, WalletUtil util) {
        try {
            if (util instanceof OmniUtils) {
                OmniUtils omni = (OmniUtils) util;
                return omni.getOmniWalletRecharge(today.getDate());
            }

            if (util instanceof ETHUtils) {
                ETHUtils eth = (ETHUtils) util;
                // eth.getWalletRecharge(today.getDate());
                return 0;
            }

            BTCUtils btc = (BTCUtils) util;
            return btc.getWalletRecharge(today.getDate());
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    // 本期账面提币汇总
    private double getBookWithdrawal(String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select fVi_fId2 as coinId,sum(fAmount) as amount_,sum(ffees) as fee_");
        ds.add("from %s", AppDB.fvirtualcaptualoperation);
        ds.add("where fVi_fId2='%s'", coinId);
        ds.add("and fType=%d and fStatus=%d", VirtualCapitalOperationTypeEnum.COIN_OUT,
                VirtualCapitalOperationInStatusEnum.SUCCESS);
        ds.add("and fCreateTime between '%s' and '%s'", dateFrom, dateTo);
        ds.add("group by fVi_fId2");
        ds.open();
        log.info("本期账面提币 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    // TODO 本期钱包提币汇总
    private double getWalletWithdrawal(String coinType, WalletUtil util) {
        try {
            if (util instanceof OmniUtils) {
                OmniUtils omni = (OmniUtils) util;
            }

            if (util instanceof ETHUtils) {
                ETHUtils eth = (ETHUtils) util;
            }

            BTCUtils btc = (BTCUtils) util;
            return 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    // 指定日期买入
    private double getTotalBuy(TDateTime today, String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select sum(f.fCount-f.fleftCount) as amount_");
        ds.add("from %s f", AppDB.fentrust);
        ds.add("left join %s m on f.ftrademapping = m.fid", AppDB.Ftrademapping);
        ds.add("where f.fEntrustType=0");// 0 买入
        ds.add("and m.fvirtualcointype2='%s'", coinId);
        ds.add("and (f.fStatus=2 or f.fStatus=3)");
        ds.add("and to_days('%s')-to_days(f.fCreateTime)<=0", today);
        ds.open();
        log.info("指定日期买入 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    // 指定日期卖出
    private double getTotalSell(TDateTime today, String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select sum(f.fCount-f.fleftCount) as amount_");
        ds.add("from %s f", AppDB.fentrust);
        ds.add("left join %s m on f.ftrademapping = m.fid", AppDB.Ftrademapping);
        ds.add("where f.fEntrustType=1");// 1 卖出
        ds.add("and m.fvirtualcointype2='%s'", coinId);
        ds.add("and (f.fStatus=2 or f.fStatus=3)");
        ds.add("and to_days('%s')-to_days(f.fCreateTime)<=0", today);
        ds.open();
        log.info("指定日期卖出 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    // 财务人员手工充值数量
    private double getManualAdj(TDateTime today, String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select sum(FQty) amount_");
        ds.add("from %s", AppDB.fvirtualoperationlog);
        ds.add("where FVirtualCoinTypeId='%s' and FStatus=2", coinId);
        ds.add("and to_days('%s') - to_days(FCreateTime) <= 0", today);
        ds.open();
        log.info("财务人员手工充值数量 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    // 财务人员手工转入数量
    private double getHandIn(TDateTime today, String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select sum(fCount) as amount_");
        ds.add("from %s", "fwallettransferrecord");
        // 1-转入
        ds.add("where fCoinType='%s' and fType=1", coinId);
        ds.add("and to_days('%s')-to_days(fCreateTime)<=0", today);
        ds.open();
        log.info("财务人员手工转入数量 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    // 财务人员手工转出数量
    private double getHandOut(TDateTime today, String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select sum(fCount) as amount_");
        ds.add("from %s", "fwallettransferrecord");
        // 0-转出
        ds.add("where fCoinType='%s' and fType=0", coinId);
        ds.add("and to_days('%s')-to_days(fCreateTime)<=0", today);
        ds.open();
        log.info("财务人员手工转出数量 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    private double getBookWithdrawaling(String coinId) {
        MyQuery ds = new MyQuery(handle);
        ds.add("select fVi_fId2 as coinId,sum(fAmount) as amount_,sum(ffees) as fee_");
        ds.add("from %s", AppDB.fvirtualcaptualoperation);
        ds.add("where fVi_fId2='%s'", coinId);
        ds.add("and fType=%d and (fStatus=%d or fStatus = %d or fStatus = %d)",
                VirtualCapitalOperationTypeEnum.COIN_OUT, VirtualCapitalOperationInStatusEnum.WAIT_2,
                VirtualCapitalOperationInStatusEnum.WAIT_0, VirtualCapitalOperationInStatusEnum.WAIT_1);
        ds.add("and fCreateTime between '%s' and '%s'", dateFrom, dateTo);
        ds.add("group by fVi_fId2");
        ds.open();
        log.info("本期账面正在提币中 " + ds.getCommandText());
        return ds.eof() ? 0 : ds.getDouble("amount_");
    }

    private void setToday(TDateTime today) {
        this.today = today;
    }

    private void setYesterday(TDateTime yesterday) {
        this.yesterday = yesterday;
    }

    // 回算今天之前3个月的历史
    public static void main(String[] args) {
        AutoCheckBalance.countHistory();
    }

    private static void countHistory() {
        AutoCheckBalance obj = new AutoCheckBalance();

        TDateTime dateFrom = TDate.Today().incDay(-3);
        TDateTime dateTo = TDate.Today();

        int diff = dateTo.compareDay(dateFrom);

        int count = 0;
        for (int i = 0; i < diff + 1; i++) {
            TDateTime today = dateFrom.incDay(count);
            obj.setToday(today);
            obj.setYesterday(today.incDay(-1));

            log.info(today);

            obj.execute();
            count++;
        }
    }

    public static void countHistory(TDateTime startDate, TDateTime endDate) {
        AutoCheckBalance obj = new AutoCheckBalance();

        TDateTime dateFrom = startDate;
        TDateTime dateTo = endDate;

        int diff = dateTo.compareDay(dateFrom);

        int count = 0;
        for (int i = 0; i < diff + 1; i++) {
            TDateTime today = dateFrom.incDay(count);
            obj.setToday(today);
            obj.setYesterday(today.incDay(-1));

            log.info(today);

            obj.execute();
            count++;
        }
    }

}
