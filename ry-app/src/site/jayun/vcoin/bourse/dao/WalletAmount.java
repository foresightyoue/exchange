//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package site.jayun.vcoin.bourse.dao;

import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.BatchScript;
import cn.cerc.jdb.mysql.SqlOperator;
import org.apache.log4j.Logger;
import site.jayun.vcoin.bourse.core.RdsQuery;
import site.jayun.vcoin.bourse.merge.Locker;

public class WalletAmount {
    private static Logger log = Logger.getLogger(WalletAmount.class);
    public static final int robotBuyId = 2;
    public static final int robotSellId = 33;
    public static final int masterUserId = 881007072;
    private IHandle handle;
    private int userId;
    private int coinId;
    private String taskId;
    private boolean autoLock = true;
    private int entrustId;

    public WalletAmount(IHandle handle, int userId, int coinId, String taskId) {
        this.handle = handle;
        this.userId = userId;
        this.coinId = coinId;
        this.taskId = taskId;
    }

    public void lock(BatchScript script, double num, String remark) throws WalletException {
        Locker locker = new Locker("entrust", this.userId + "-" + this.coinId, new Object[0]);
        Throwable var6 = null;

        try {
            if (this.autoLock && !locker.lock("Wallet.lock")) {
                throw new WalletException(locker.getMessage());
            }

            RdsQuery wallet = this.openWallet();
            this.checkAndSaveLog(this.userId, wallet, -num, num, remark);
            BatchScript bs = new BatchScript(this.handle);
            bs.add("update %s", new Object[]{"fvirtualwallet"});
            bs.add("set fTotal=fTotal+(%s),", new Object[]{-num});
            bs.add("fFrozen=fFrozen+(%s),", new Object[]{num});
            bs.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
            bs.add("where fVi_fId=%s", new Object[]{this.coinId});
            bs.add("and fuid=%s", new Object[]{this.userId});
            bs.exec();
        } catch (Throwable var16) {
            var6 = var16;
            throw var16;
        } finally {
            if (locker != null) {
                if (var6 != null) {
                    try {
                        locker.close();
                    } catch (Throwable var15) {
                        var6.addSuppressed(var15);
                    }
                } else {
                    locker.close();
                }
            }

        }

    }

    public void unlockReturn(BatchScript script, double num, String remark) throws WalletException {
        Locker locker = new Locker("entrust", this.userId + "-" + this.coinId, new Object[0]);
        Throwable var6 = null;

        try {
            if (this.autoLock && !locker.lock("Wallet.unlock")) {
                throw new WalletException(locker.getMessage());
            }
            RdsQuery wallet = this.openWallet();
            this.checkAndSaveLog(this.userId, wallet, 0, -num, remark);
            script.add("update %s", new Object[]{"fvirtualwallet"});
            script.add("set fFrozen=fFrozen+(%s),", new Object[]{-num});
            script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
            script.add("where fVi_fId=%s", new Object[]{this.coinId});
            script.add("and fuid=%s", new Object[]{this.userId});
            script.addSemicolon();

            RdsQuery ds1 = this.loadWallet(WalletAmount.masterUserId);
            this.checkAndSaveLog(WalletAmount.masterUserId, ds1,  num,0.0D,  remark);
            script.add("update %s", new Object[]{"fvirtualwallet"});
            script.add("set fTotal=fTotal+(%s),", new Object[]{num});
            script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
            script.add("where fVi_fId=%s", new Object[]{this.coinId});
            script.add("and fuid=%s", new Object[]{WalletAmount.masterUserId});
            script.addSemicolon();

        } catch (Throwable var15) {
            var6 = var15;
            throw var15;
        } finally {
            if (locker != null) {
                if (var6 != null) {
                    try {
                        locker.close();
                    } catch (Throwable var14) {
                        var6.addSuppressed(var14);
                    }
                } else {
                    locker.close();
                }
            }

        }

    }

    public void unlock(BatchScript script, double num, String remark) throws WalletException {
        Locker locker = new Locker("entrust", this.userId + "-" + this.coinId, new Object[0]);
        Throwable var6 = null;

        try {
            if (this.autoLock && !locker.lock("Wallet.unlock")) {
                throw new WalletException(locker.getMessage());
            }

            RdsQuery wallet = this.openWallet();
            this.checkAndSaveLog(this.userId, wallet, num, -num, remark);
            script.add("update %s", new Object[]{"fvirtualwallet"});
            script.add("set fTotal=fTotal+(%s),", new Object[]{num});
            script.add("fFrozen=fFrozen+(%s),", new Object[]{-num});
            script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
            script.add("where fVi_fId=%s", new Object[]{this.coinId});
            script.add("and fuid=%s", new Object[]{this.userId});
            script.addSemicolon();
        } catch (Throwable var15) {
            var6 = var15;
            throw var15;
        } finally {
            if (locker != null) {
                if (var6 != null) {
                    try {
                        locker.close();
                    } catch (Throwable var14) {
                        var6.addSuppressed(var14);
                    }
                } else {
                    locker.close();
                }
            }

        }

    }

    public void unlockFrozen(BatchScript script, double fleftCount, String remark, boolean isLimit) throws WalletException {
        Locker locker = new Locker("entrust", this.userId + "-" + this.coinId, new Object[0]);
        Throwable var7 = null;

        try {
            if (this.autoLock && !locker.lock("Wallet.unlock")) {
                throw new WalletException(locker.getMessage());
            }

            RdsQuery wallet = this.openWallet();
            double num = Math.min(fleftCount, wallet.getDouble("fFrozen"));
            this.checkAndSaveLog(this.userId, wallet, num, -num, remark);
            script.add("update %s", new Object[]{"fvirtualwallet"});
            script.add("set fTotal=fTotal+(%s),", new Object[]{num});
            script.add("fFrozen=fFrozen+(%s),", new Object[]{-num});
            if (isLimit) {
                script.add("fcanSellQty=fcanSellQty+(%s),", new Object[]{num});
            }

            script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
            script.add("where fVi_fId=%s", new Object[]{this.coinId});
            script.add("and fuid=%s", new Object[]{this.userId});
            script.addSemicolon();
        } catch (Throwable var18) {
            var7 = var18;
            throw var18;
        } finally {
            if (locker != null) {
                if (var7 != null) {
                    try {
                        locker.close();
                    } catch (Throwable var17) {
                        var7.addSuppressed(var17);
                    }
                } else {
                    locker.close();
                }
            }

        }

    }

    public void moveTo(BatchScript script, int toUser, double amount, String remark) throws WalletException {
        this.moveTo(script, toUser, amount, remark, false);
    }

    public void returnTo(BatchScript script, int toUser, double num, String remark, boolean isFrozen) throws WalletException {
        if (num != 0.0D) {
            Locker locker = new Locker("entrust", this.userId + "-" + this.coinId, new Object[0]);
            Throwable var8 = null;

            try {
                locker.add(toUser + "-" + this.coinId);
                if (this.autoLock && !locker.lock("moveTo")) {
                    throw new WalletException(locker.getMessage());
                }
                RdsQuery ds2;
                if (isFrozen) {
                    ds2 = this.loadWallet(toUser);
                    this.checkAndSaveLogOfReturn(toUser, ds2, num,num, remark);
                    script.add("update %s", new Object[]{"fvirtualwallet"});
                    script.add("set fTotal=fTotal+(%s),", new Object[]{-num});
                    script.add("fFrozen=fFrozen+(%s),", new Object[]{num});
                    script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
                    script.add("where fVi_fId=%s", new Object[]{this.coinId});
                    script.add("and fuid=%s", new Object[]{toUser});
                    script.addSemicolon();
                } else {
                    ds2 = this.loadWallet(toUser);
                    this.checkAndSaveLog(toUser, ds2, 0.0D, num, remark);
                    script.add("update %s", new Object[]{"fvirtualwallet"});
                    script.add("set fFrozen=fFrozen+(%s),", new Object[]{-num});
                    script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
                    script.add("where fVi_fId=%s", new Object[]{this.coinId});
                    script.add("and fuid=%s", new Object[]{toUser});
                    script.addSemicolon();

                    RdsQuery ds1 = this.loadWallet(this.userId);
                    this.checkAndSaveLog(this.userId, ds1,  0.0D, num, remark);
                    script.add("update %s", new Object[]{"fvirtualwallet"});
                    script.add("set fTotal=fTotal+(%s),", new Object[]{-num});
                    script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
                    script.add("where fVi_fId=%s", new Object[]{this.coinId});
                    script.add("and fuid=%s", new Object[]{this.userId});
                    script.addSemicolon();
                }
            } catch (Throwable var18) {
                var8 = var18;
                throw var18;
            } finally {
                if (locker != null) {
                    if (var8 != null) {
                        try {
                            locker.close();
                        } catch (Throwable var17) {
                            var8.addSuppressed(var17);
                        }
                    } else {
                        locker.close();
                    }
                }

            }
        }
    }

    public void moveTo(BatchScript script, int toUser, double num, String remark, boolean isFrozen) throws WalletException {
        if (num != 0.0D) {
            Locker locker = new Locker("entrust", this.userId + "-" + this.coinId, new Object[0]);
            Throwable var8 = null;

            try {
                locker.add(toUser + "-" + this.coinId);
                if (this.autoLock && !locker.lock("moveTo")) {
                    throw new WalletException(locker.getMessage());
                }

                RdsQuery ds1 = this.loadWallet(this.userId);
                this.checkAndSaveLog(this.userId, ds1, -num, 0.0D, remark);
                script.add("update %s", new Object[]{"fvirtualwallet"});
                script.add("set fTotal=fTotal+(%s),", new Object[]{-num});
                script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
                script.add("where fVi_fId=%s", new Object[]{this.coinId});
                script.add("and fuid=%s", new Object[]{this.userId});
                script.addSemicolon();
                RdsQuery ds2;
                if (isFrozen) {
                    ds2 = this.loadWallet(toUser);
                    this.checkAndSaveLog(toUser, ds2, 0.0D, num, remark);
                    script.add("update %s", new Object[]{"fvirtualwallet"});
                    script.add("set fFrozen=fFrozen+(%s),", new Object[]{num});
                    script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
                    script.add("where fVi_fId=%s", new Object[]{this.coinId});
                    script.add("and fuid=%s", new Object[]{toUser});
                    script.addSemicolon();
                } else {
                    ds2 = this.loadWallet(toUser);
                    this.checkAndSaveLog(toUser, ds2, num, 0.0D, remark);
                    script.add("update %s", new Object[]{"fvirtualwallet"});
                    script.add("set fTotal=fTotal+(%s),", new Object[]{num});
                    script.add("fLastUpdateTime='%s' ", new Object[]{TDateTime.Now()});
                    script.add("where fVi_fId=%s", new Object[]{this.coinId});
                    script.add("and fuid=%s", new Object[]{toUser});
                    script.addSemicolon();
                }
            } catch (Throwable var18) {
                var8 = var18;
                throw var18;
            } finally {
                if (locker != null) {
                    if (var8 != null) {
                        try {
                            locker.close();
                        } catch (Throwable var17) {
                            var8.addSuppressed(var17);
                        }
                    } else {
                        locker.close();
                    }
                }

            }

        }
    }

    public RdsQuery openWallet() {
        RdsQuery wallet = new RdsQuery(this.handle);
        wallet.add("select * from %s", new Object[]{"fvirtualwallet"});
        wallet.add("where fVi_fId=%s", new Object[]{this.coinId});
        wallet.add("and fuid=%s", new Object[]{this.userId});
        wallet.open();
        if (wallet.eof()) {
            log.warn("找不到用户的钱包记录，已自动修复");
            wallet.append();
            wallet.setField("fVi_fId", this.coinId);
            wallet.setField("fTotal", 0);
            wallet.setField("fFrozen", 0);
            wallet.setField("fLastUpdateTime", TDateTime.Now());
            wallet.setField("fuid", this.userId);
            wallet.setField("fBorrowBtc", 0);
            wallet.setField("fCanlendBtc", 0);
            wallet.setField("fFrozenLendBtc", 0);
            wallet.setField("fAlreadyLendBtc", 0);
            wallet.setField("version", 0);
            wallet.setField("fHaveAppointBorrowBtc", 0);
            wallet.setField("fcanSellQty", 0);
            wallet.post();
        }

        return wallet;
    }

    private RdsQuery loadWallet(int user) {
        RdsQuery query = new RdsQuery(this.handle);
        query.add("select * from %s", new Object[]{"fvirtualwallet"});
        query.add("where fVi_fId=%s", new Object[]{this.coinId});
        query.add("and fuid=%s", new Object[]{user});
        query.open();
        if (query.eof()) {
            log.warn("找不到用户的钱包记录，已自动修复");
            query.append();
            query.setField("fVi_fId", this.coinId);
            query.setField("fTotal", 0);
            query.setField("fFrozen", 0);
            query.setField("fLastUpdateTime", TDateTime.Now());
            query.setField("fuid", user);
            query.setField("fBorrowBtc", 0);
            query.setField("fCanlendBtc", 0);
            query.setField("fFrozenLendBtc", 0);
            query.setField("fAlreadyLendBtc", 0);
            query.setField("version", 0);
            query.setField("fHaveAppointBorrowBtc", 0);
            query.setField("fcanSellQty", 0);
            query.post();
        }

        return query;
    }

    public void checkAndSaveLogOfReturn(int user, RdsQuery wallet, double fTotal, double fFrozen, String remark) throws WalletException {
        if (user != 2 && user != 33 && user != 881007072) {
            if (fTotal != 0.0D || fFrozen != 0.0D) {
                if (fTotal != 0.0D) {
                    this.checkValue(user, wallet, "fTotal", "钱包", fTotal);
                }
                Record log = new Record();
                log.setField("userId_", user);
                log.setField("coinId_", this.coinId);
                log.setField("total_", wallet.getDouble("fTotal"));
                log.setField("frozen_", wallet.getDouble("fFrozen"));
                log.setField("totalChange_", fTotal);
                log.setField("frozenChange_", fFrozen);
                log.setField("changeReason_", remark);
                log.setField("changeDate_", TDateTime.Now());
                log.setField("taskId_", this.taskId);
                log.setField("entrustId_", this.entrustId);
                SqlOperator operator = new SqlOperator(this.handle);
                operator.setTableName("t_walletlog");
                operator.getPrimaryKeys().add("uid_");
                operator.insert(log);
            }
        }
    }

    public void checkAndSaveLog(int user, RdsQuery wallet, double fTotal, double fFrozen, String remark) throws WalletException {
        if (user != 2 && user != 33 && user != 881007072) {
            if (fTotal != 0.0D || fFrozen != 0.0D) {
                if (fTotal != 0.0D) {
                    this.checkValue(user, wallet, "fTotal", "钱包", fTotal);
                }

                if (fFrozen != 0.0D) {
                    this.checkValue(user, wallet, "fFrozen", "冻结钱包", fFrozen);
                }

                Record log = new Record();
                log.setField("userId_", this.userId);
                log.setField("coinId_", this.coinId);
                log.setField("total_", wallet.getDouble("fTotal"));
                log.setField("frozen_", wallet.getDouble("fFrozen"));
                log.setField("totalChange_", fTotal);
                log.setField("frozenChange_", fFrozen);
                log.setField("changeReason_", remark);
                log.setField("changeDate_", TDateTime.Now());
                log.setField("taskId_", this.taskId);
                log.setField("entrustId_", this.entrustId);
                SqlOperator operator = new SqlOperator(this.handle);
                operator.setTableName("t_walletlog");
                operator.getPrimaryKeys().add("uid_");
                operator.insert(log);
            }
        }
    }

    private void checkValue(int user, RdsQuery wallet, String fieldCode, String fieldName, double num) throws WalletException {
        if (wallet.getDouble(fieldCode) + num < 0.0D && Math.abs(wallet.getDouble(fieldCode) + num) > 1.0E-5D) {
            throw new WalletException(String.format("[%s]%s%s余额不足, %s: %s + (%s) < 0 ", user, fieldName, this.coinId, fieldCode, wallet.getDouble(fieldCode), num), 1);
        }
    }

    public boolean isAutoLock() {
        return this.autoLock;
    }

    public void setAutoLock(boolean autoLock) {
        this.autoLock = autoLock;
    }

    public int getEntrustId() {
        return this.entrustId;
    }

    public void setEntrustId(int entrustId) {
        this.entrustId = entrustId;
    }
}
