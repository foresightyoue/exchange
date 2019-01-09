package com.huagu.vcoin.main.service.front;

import static cn.cerc.jdb.other.utils.roundTo;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.coa.common.cons.EntrustStatusEnum;
import com.huagu.coa.common.cons.EntrustTypeEnum;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.dao.FentrustDAO;
import com.huagu.vcoin.main.dao.FentrustlogDAO;
import com.huagu.vcoin.main.dao.FentrustplanDAO;
import com.huagu.vcoin.main.dao.FfeesDAO;
import com.huagu.vcoin.main.dao.FintrolinfoDAO;
import com.huagu.vcoin.main.dao.FsubscriptionDAO;
import com.huagu.vcoin.main.dao.FsubscriptionlogDAO;
import com.huagu.vcoin.main.dao.FtrademappingDAO;
import com.huagu.vcoin.main.dao.FuserDAO;
import com.huagu.vcoin.main.dao.FvirtualwalletDAO;
import com.huagu.vcoin.main.dao.UtilsDAO;
import com.huagu.vcoin.main.model.Fentrust;
import com.huagu.vcoin.main.model.Fentrustlog;
import com.huagu.vcoin.main.model.Fentrustplan;
import com.huagu.vcoin.main.model.Fintrolinfo;
import com.huagu.vcoin.main.model.Fsubscription;
import com.huagu.vcoin.main.model.Fsubscriptionlog;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.mysql.BatchScript;
import cn.cerc.jdb.other.utils;
import site.jayun.vcoin.bourse.core.BourseEngine;
import site.jayun.vcoin.bourse.dao.WalletAmount;
import site.jayun.vcoin.bourse.entity.Entrust;
import site.jayun.vcoin.bourse.merge.Locker;

@Service
public class FrontTradeService {
    // private static final Logger log =
    // LoggerFactory.getLogger(FrontTradeService.class);

    @Autowired
    private FentrustDAO fentrustDAO;
    @Autowired
    private FentrustlogDAO fentrustlogDAO;
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private FentrustplanDAO fentrustplanDAO;
    @Autowired
    private FfeesDAO ffeesDAO;
    @Autowired
    private FintrolinfoDAO fintrolinfoDAO;
    @Autowired
    private FsubscriptionDAO fsubscriptionDAO;
    @Autowired
    private FsubscriptionlogDAO fsubscriptionlogDAO;
    @Autowired
    private FtrademappingDAO ftrademappingDAO;
    @Autowired
    private UtilsDAO utilsDAO;
    @Autowired
    private FuserDAO fuserDAO;

    // 手续费率
    private Map<String, Double> rates = new HashMap<String, Double>();

    public void putRates(String key, double value) {
        synchronized (this.rates) {
            this.rates.put(key, value);
        }
    }

    public double getRates(int vid, boolean isbuy, int level) {
        String key = vid + "_" + (isbuy ? "buy" : "sell") + "_" + level;
        synchronized (this.rates) {
            return this.rates.get(key);
        }
    }

    // 撮合
    public void updateDealMaking(Ftrademapping ftrademapping, Fentrust buy, Fentrust sell, Fentrustlog buyLog,
            Fentrustlog sellLog, int id) {

        double buyFee = 0D;
        if (buy.isFisLimit() == false) {// limit=0
            buyFee = (buyLog.getFamount() / buy.getFamount()) * buy.getFfees();
        } else {// limit==1
            double ffeeRate = this.getRates(buy.getFtrademapping().getFid(), true,
                    buy.getFuser().getFscore().getFlevel());
            buyFee = buyLog.getFcount() * ffeeRate;
        }
        buyLog.setFfees(buyFee);
        if (buy.isFisLimit()) {
            buy.setFcount(buy.getFcount() + buyLog.getFcount());
            buy.setFsuccessAmount(buy.getFsuccessAmount() + (buyLog.getFamount()));
            buy.setFfees(buy.getFfees() + buyFee);
            buy.setFlastUpdatTime(Utils.getTimestamp());
            if (buy.getFamount() - buy.getFsuccessAmount() < 0.000001D) {
                buy.setFstatus(EntrustStatusEnum.AllDeal);
            } else {
                buy.setFstatus(EntrustStatusEnum.PartDeal);
            }
        } else {
            buy.setFleftCount(buy.getFleftCount() - buyLog.getFcount());
            buy.setFsuccessAmount(buy.getFsuccessAmount() + (buyLog.getFamount()));
            buy.setFlastUpdatTime(Utils.getTimestamp());
            buy.setFleftfees(buy.getFleftfees() - buyFee);
            if (buy.getFleftCount() < 0.000001D) {
                buy.setFstatus(EntrustStatusEnum.AllDeal);
            } else {
                buy.setFstatus(EntrustStatusEnum.PartDeal);
            }
        }

        double sellFee = 0D;
        if (sell.isFisLimit() == false) {// limit==0
            sellFee = (buyLog.getFcount() / sell.getFcount()) * sell.getFfees();
        } else {// limit==1
            double sellRate = this.getRates(sell.getFtrademapping().getFid(), false,
                    sell.getFuser().getFscore().getFlevel());
            sellFee = sellRate * sellLog.getFamount();
        }
        sellLog.setFfees(sellFee);
        if (sell.isFisLimit()) {
            sell.setFsuccessAmount(sell.getFsuccessAmount() + buyLog.getFamount());
            sell.setFamount(sell.getFamount() + buyLog.getFamount());
            sell.setFleftCount(sell.getFleftCount() - buyLog.getFcount());
            sell.setFfees(sell.getFfees() + sellFee);
            sell.setFlastUpdatTime(Utils.getTimestamp());
            if (sell.getFleftCount() < 0.000001F) {
                sell.setFstatus(EntrustStatusEnum.AllDeal);
            } else {
                sell.setFstatus(EntrustStatusEnum.PartDeal);
            }

        } else {
            sell.setFleftCount(sell.getFleftCount() - buyLog.getFcount());
            sell.setFsuccessAmount(sell.getFsuccessAmount() + (sellLog.getFamount()));
            sell.setFleftfees(sell.getFleftfees() - sellFee);
            sell.setFlastUpdatTime(Utils.getTimestamp());
            if (sell.getFleftCount() < 0.000001D) {
                sell.setFstatus(EntrustStatusEnum.AllDeal);
            } else {
                sell.setFstatus(EntrustStatusEnum.PartDeal);
            }
        }

        Fvirtualwallet fbuyWallet = this.fackFvirtualwallet(buy.getFuser().getFid(),
                ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFid());
        Fvirtualwallet fbuyVirtualwallet = this.fackFvirtualwallet(buy.getFuser().getFid(),
                ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFid());
        Fvirtualwallet fsellWallet = this.fackFvirtualwallet(sell.getFuser().getFid(),
                ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFid());
        Fvirtualwallet fsellVirtualwallet = this.fackFvirtualwallet(sell.getFuser().getFid(),
                ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFid());

        fsellWallet = theSame(fsellWallet, fbuyWallet, fbuyVirtualwallet, fsellVirtualwallet);
        fbuyVirtualwallet = theSame(fbuyVirtualwallet, fbuyWallet, fsellWallet, fsellVirtualwallet);
        fsellVirtualwallet = theSame(fsellVirtualwallet, fbuyWallet, fbuyVirtualwallet, fsellWallet);

        // 买法币
        fbuyWallet.setFfrozen(fbuyWallet.getFfrozen() - buyLog.getFamount());
        // 卖法币
        fsellWallet.setFtotal(fsellWallet.getFtotal() + buyLog.getFamount() - sellFee);
        // 买虚拟
        fbuyVirtualwallet.setFtotal(fbuyVirtualwallet.getFtotal() + buyLog.getFcount() - buyFee);
        // 卖虚拟
        fsellVirtualwallet.setFfrozen(fsellVirtualwallet.getFfrozen() - buyLog.getFcount());

        if (buy.getFstatus() == EntrustStatusEnum.AllDeal) {
            // 因为有人低价卖出，冻结剩余部分返回钱包
            double left_amount = (buy.getFamount() - buy.getFsuccessAmount());
            fbuyWallet.setFfrozen(fbuyWallet.getFfrozen() - left_amount);
            fbuyWallet.setFtotal(fbuyWallet.getFtotal() + left_amount);
        }

        fentrustlogDAO.save(buyLog);
        fentrustlogDAO.save(sellLog);

        fentrustDAO.attachDirty(buy);
        fentrustDAO.attachDirty(sell);

        this.updateFackFvirtualwallet(fsellVirtualwallet, fbuyVirtualwallet, fbuyWallet, fsellWallet);

    }

    private void updateFackFvirtualwallet(Fvirtualwallet w1, Fvirtualwallet w2, Fvirtualwallet w3, Fvirtualwallet w4) {
        String hql = "update Fvirtualwallet set ftotal=ftotal+? , ffrozen=ffrozen+? , version=version+1 where fuser.fid=? and fvirtualcointype.fid=?";
        int count = this.utilsDAO.executeHQL(hql, w1.getFtotal(), w1.getFfrozen(), w1.getFack_uid(), w1.getFack_vid());

        if (w2.getFack_id().equals(w1.getFack_id()) == false) {
            count = this.utilsDAO.executeHQL(hql, w2.getFtotal(), w2.getFfrozen(), w2.getFack_uid(), w2.getFack_vid());
        }

        if (w3.getFack_id().equals(w1.getFack_id()) == false && w3.getFack_id().equals(w2.getFack_id()) == false) {
            count = this.utilsDAO.executeHQL(hql, w3.getFtotal(), w3.getFfrozen(), w3.getFack_uid(), w3.getFack_vid());
        }

        if (w4.getFack_id().equals(w1.getFack_id()) == false && w4.getFack_id().equals(w2.getFack_id()) == false
                && w4.getFack_id().equals(w3.getFack_id()) == false) {
            count = this.utilsDAO.executeHQL(hql, w4.getFtotal(), w4.getFfrozen(), w4.getFack_uid(), w4.getFack_vid());
        }

    }

    private Fvirtualwallet fackFvirtualwallet(int fuserid, int vid) {
        Fvirtualwallet fvirtualwallet = new Fvirtualwallet();
        fvirtualwallet.setFack_uid(fuserid);
        fvirtualwallet.setFack_vid(vid);

        fvirtualwallet.setFack_id(fuserid + "_" + vid);
        return fvirtualwallet;
    }

    private Fvirtualwallet theSame(Fvirtualwallet v1, Fvirtualwallet v2, Fvirtualwallet v3, Fvirtualwallet v4) {
        if (v1.getFack_id().equals(v2.getFack_id())) {
            return v2;
        }
        if (v1.getFack_id().equals(v3.getFack_id())) {
            return v3;
        }
        if (v1.getFack_id().equals(v4.getFack_id())) {
            return v4;
        }
        return v1;
    }

    public Fentrust findFentrustById(int id) {
        return this.fentrustDAO.findById(id);
    }

    public List<Fentrustlog> findFentrustLogByFentrust(Fentrust fentrust) {
        boolean isBuy = fentrust.getFentrustType() == 0;
        String propertyName = isBuy ? "fdeal_fid" : "fentrust.fid";
        Object value = isBuy ? fentrust.getFid().toString() : fentrust.getFid();
        return this.fentrustlogDAO.findByProperty(propertyName, value);
    }

    // 最新成交记录
    public List<Fentrust> findLatestSuccessDeal(int coinTypeId, int fentrustType, int count) {
        return this.fentrustDAO.findLatestSuccessDeal(coinTypeId, fentrustType, count);
    }

    public List<Fentrust> findAllGoingFentrust(int coinTypeId, int fentrustType, boolean isLimit) {
        return this.fentrustDAO.findAllGoingFentrust(coinTypeId, fentrustType, isLimit);
    }

    // 获得24小时内的成交记录
    public List<Fentrustlog> findLatestSuccessDeal24(int coinTypeId, int hour) {
        List<Fentrustlog> list = this.fentrustlogDAO.findLatestSuccessDeal24(coinTypeId, 24);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }

    public Fentrustlog findLatestDeal(int coinTypeId) {
        Fentrustlog fentrust = this.fentrustDAO.findLatestDeal(coinTypeId);
        if (fentrust == null)
            return null;
        return fentrust;
    }

    // 委托记录
    public List<Fentrust> findFentrustHistory(int fuid, int fvirtualCoinTypeId, int[] entrust_type, int first_result,
            int max_result, String order, int entrust_status[], Date beginDate, Date endDate) throws Exception {
        List<Fentrust> list = this.fentrustDAO.getFentrustHistory(fuid, fvirtualCoinTypeId, entrust_type, first_result,
                max_result, order, entrust_status, beginDate, endDate);
        return list;
    }

    // 计划委托
    public List<Fentrustplan> findEntrustPlan(int type, int status[]) {
        List<Fentrustplan> list = this.fentrustplanDAO.findEntrustPlan(type, status);

        return list;
    }

    // 委托买入
    public Fentrust updateEntrustBuy(int tradeMappingID, double tradeNum, double tradeCnyPrice, Fuser fuser,
            boolean isLimit, HttpServletRequest req) throws Exception {
        // 更改钱包余额
        Timestamp time = new Timestamp(new Date().getTime());
        Ftrademapping mapping = this.ftrademappingDAO.findById(tradeMappingID);
        // 后台设置的手续费率
        double ffeeRate = this.ffeesDAO.findFfee(tradeMappingID, fuser.getFscore().getFlevel()).getFbuyfee();
        // 买入总价格
        double amount = 0F;
        if (isLimit) {
            amount = tradeCnyPrice;
            ffeeRate = 0;
        } else {
            // 总成交金额
            amount = roundTo(tradeNum * tradeCnyPrice, -8);
        }
        Integer coinId = mapping.getFvirtualcointypeByFvirtualcointype1().getFid();
        String taskId = utils.newGuid();

        try (Locker locker = new Locker("entrust", fuser.getFid() + "-" + coinId)) {
            if (!locker.lock("updateEntrustBuy", 3000))
                throw new RuntimeException(locker.getMessage());

            Mysql handle = new Mysql();
            BourseEngine engine = new BourseEngine(handle);
            engine.appendBuyEntrust(tradeMappingID, tradeNum, tradeCnyPrice, ffeeRate, isLimit, fuser.getFid());
            
            // 更改钱包余额
            BatchScript script = new BatchScript(handle);
            WalletAmount wallet = new WalletAmount(handle, fuser.getFid(), coinId, taskId);
            wallet.setAutoLock(false);
            wallet.lock(script, amount, "委托买入"); // 锁定usdt
            script.exec();

            // 增加买单
            Fentrust entrust = new Fentrust();
            entrust.setFcount(tradeNum);
            entrust.setFleftCount(tradeNum);
            entrust.setFprize(tradeCnyPrice);
            entrust.setFamount(amount);
            entrust.setFfees(roundTo(amount * ffeeRate, -8));
            entrust.setFleftfees(roundTo(amount * ffeeRate, -8));
            entrust.setFcreateTime(time);
            entrust.setFentrustType(EntrustTypeEnum.BUY);
            entrust.setFisLimit(isLimit);
            entrust.setFlastUpdatTime(Utils.getTimestamp());
            entrust.setFstatus(EntrustStatusEnum.Going);
            entrust.setFsuccessAmount(0F);
            entrust.setFhasSubscription(false);
            entrust.setFuser(fuser);
            entrust.setFtrademapping(mapping);
            entrust.setIsRobotType(1);
            entrust.setTaskId(taskId);
            this.fentrustDAO.save(entrust);
            this.fuserDAO.attachDirty(fuser);

            return entrust;
        }
    }

    // 委托卖出
    public Fentrust updateEntrustSell(int tradeMappingID, double tradeNum, double tradeCnyPrice, Fuser fuser,
            boolean isLimit, HttpServletRequest req) throws Exception {
        if (isLimit)
            throw new RuntimeException("暂不支持此交易类型");

        Timestamp time = new Timestamp(new Date().getTime());
        double ffeeRate = this.ffeesDAO.findFfee(tradeMappingID, fuser.getFscore().getFlevel()).getFfee();
        // 总手续费人民币
        double ffee = roundTo(tradeNum * ffeeRate, -8);
        Ftrademapping mapping = this.ftrademappingDAO.findById(tradeMappingID);
        Integer coinId = mapping.getFvirtualcointypeByFvirtualcointype2().getFid();
        String taskId = utils.newGuid();

        try (Locker locker = new Locker("entrust", fuser.getFid() + "-" + coinId)) {
            if (!locker.lock("updateEntrustSell", 3000))
                throw new RuntimeException(locker.getMessage());
            // 更改钱包余额
            Mysql handle = new Mysql();
            BourseEngine engine = new BourseEngine(handle);
            engine.appendSellEntrust(tradeMappingID,tradeNum ,tradeCnyPrice, ffeeRate, isLimit, fuser.getFid());

            BatchScript script = new BatchScript(handle);
            WalletAmount wallet = new WalletAmount(handle, fuser.getFid(), coinId, taskId);
            wallet.setAutoLock(false);
            wallet.lock(script, tradeNum, "委托卖出"); // 锁定at
            script.exec();

            // 提交订单
            Fentrust entrust = new Fentrust();
            entrust.setFamount(roundTo(tradeNum * tradeCnyPrice, -8));
            entrust.setFprize(tradeCnyPrice);
            entrust.setFcount(tradeNum);
            entrust.setFleftCount(tradeNum);
            entrust.setFfees(ffee);
            entrust.setFleftfees(ffee);
            entrust.setFcreateTime(time);
            entrust.setFentrustType(EntrustTypeEnum.SELL);
            entrust.setFisLimit(isLimit);
            entrust.setFlastUpdatTime(time);
            entrust.setFstatus(EntrustStatusEnum.Going);
            entrust.setFsuccessAmount(0F);
            entrust.setFuser(fuser);
            entrust.setFhasSubscription(false);
            entrust.setFtrademapping(mapping);
            entrust.setIsRobotType(1);
            entrust.setTaskId(taskId);
            this.fentrustDAO.save(entrust);

            this.fuserDAO.attachDirty(fuser);
            return entrust;
        }
    }

    // 委托记录
    public List<Fentrust> findFentrustHistory(int firstResult, int maxResults, String filter, boolean isFY)
            throws Exception {
        List<Fentrust> list = this.fentrustDAO.list(firstResult, maxResults, filter, isFY);
        return list;
    }

    // 委托记录
    public List<Fentrust> findFentrustHistory(int fuid, int fvirtualCoinTypeId, int[] entrust_type, int first_result,
            int max_result, String order, int entrust_status[]) throws Exception {
        List<Fentrust> list = this.fentrustDAO.getFentrustHistory(fuid, fvirtualCoinTypeId, entrust_type, first_result,
                max_result, order, entrust_status);
        for (Fentrust fentrust : list) {
            Ftrademapping ftrademapping = fentrust.getFtrademapping();
            ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFname();
            ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFname();
        }
        return list;
    }

    public int findFentrustHistoryCount(int fuid, int fvirtualCoinTypeId, int[] entrust_type, int entrust_status[])
            throws Exception {
        return this.fentrustDAO.getFentrustHistoryCount(fuid, fvirtualCoinTypeId, entrust_type, entrust_status);
    }

    public List<Fentrustplan> findFentrustplan(int fuser, int fvirtualcointype, int[] fstatus, int firtResult,
            int maxResult, String order) {
        return this.fentrustplanDAO.findFentrustplan(fuser, fvirtualcointype, fstatus, firtResult, maxResult, order);
    }

    public Fentrustplan findFentrustplanById(int id) {
        return this.fentrustplanDAO.findById(id);
    }

    public long findFentrustplanCount(int fuser, int fvirtualcointype, int[] fstatus) {
        return this.fentrustplanDAO.findFentrustplanCount(fuser, fvirtualcointype, fstatus);
    }

    public Fsubscription findFsubscriptionById(int id) {
        return this.fsubscriptionDAO.findById(id);
    }

    public Fsubscription findFirstSubscription(int type) {
        Fsubscription fsubscription = null;
        List<Fsubscription> fsubscriptions = this.fsubscriptionDAO.findByParam(0, 1,
                " where fisopen=1 and ftype = " + type + " order by fid asc ", true, Fsubscription.class);
        if (fsubscriptions.size() > 0) {
            fsubscription = fsubscriptions.get(0);
        }
        return fsubscription;
    }

    public List<Fsubscriptionlog> findFsubscriptionlogByParam(int firstResult, int maxResults, String filter,
            boolean isFY) {
        return this.fsubscriptionlogDAO.findByParam(firstResult, maxResults, filter, isFY, Fsubscriptionlog.class);
    }

    public List<Fsubscriptionlog> findFsubScriptionLog(Fuser fuser, int id) {
        List<Fsubscriptionlog> fsubscriptionlogs = this.fsubscriptionlogDAO.findByParam(0, 0,
                " where fuser.fid=" + fuser.getFid() + " and fsubscription.fid=" + id + " order by fid desc", false,
                Fsubscriptionlog.class);
        return fsubscriptionlogs;
    }

    public List<Fentrust> findFentrustByParam(int firstResult, int maxResults, String filter, boolean isFY) {
        return this.fentrustDAO.findByParam(firstResult, maxResults, filter, isFY, Fentrust.class);
    }

    public int findFentrustByParamCount(String filter) {
        return this.fentrustDAO.findByParamCount(filter, Fentrust.class);
    }

    public void updateFeeLog(Fentrust entrust, Fvirtualwallet fvirtualwallet) {
        try {
            this.fentrustDAO.attachDirty(entrust);
            this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateSubscription(Fvirtualwallet fvirtualwalletCost, Fvirtualwallet fvirtualwalletCost2,
            Fvirtualwallet fvirtualwallet, Fsubscriptionlog fsubscriptionlog, Fsubscription fsubscription) {
        try {
            if (fvirtualwalletCost != null) {
                this.fvirtualwalletDAO.attachDirty(fvirtualwalletCost);
            }
            if (fvirtualwalletCost2 != null) {
                this.fvirtualwalletDAO.attachDirty(fvirtualwalletCost2);
            }
            this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
            this.fsubscriptionDAO.attachDirty(fsubscription);
            this.fsubscriptionlogDAO.save(fsubscriptionlog);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateSubscription(Fvirtualwallet fvirtualwalletCost, Fvirtualwallet fvirtualwallet,
            Fsubscriptionlog fsubscriptionlog, Fsubscription fsubscription) {
        try {
            if (fvirtualwalletCost != null) {
                this.fvirtualwalletDAO.attachDirty(fvirtualwalletCost);
            }
            this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
            this.fsubscriptionDAO.attachDirty(fsubscription);
            this.fsubscriptionlogDAO.save(fsubscriptionlog);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Fentrust> findFentrustsByParam(int firstResult, int maxResults, String filter, boolean isFY) {
        return this.fentrustDAO.findByParam(firstResult, maxResults, filter, isFY, Fentrust.class);
    }

    public void updateFentrust(Fentrust fentrust) {
        try {
            this.fentrustDAO.attachDirty(fentrust);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateCoinFentrust(Fentrust fentrust, List<Fvirtualwallet> fvirtualwallets,
            List<Fintrolinfo> fintrolinfos) {
        try {
            this.fentrustDAO.attachDirty(fentrust);
            for (Fintrolinfo fintrolinfo : fintrolinfos) {
                this.fintrolinfoDAO.save(fintrolinfo);
            }
            for (Fvirtualwallet fvirtualwallet : fvirtualwallets) {
                this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 加密
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    private static String encrypt(String data, String key) throws Exception {
        Key k = toKey(Base64.decodeBase64(key.getBytes())); // 还原密钥
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM); // 实例化Cipher对象，它用于完成实际的加密操作
        cipher.init(Cipher.ENCRYPT_MODE, k); // 初始化Cipher对象，设置为加密模式
        return new String(Base64.encodeBase64(cipher.doFinal(data.getBytes()))); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
    }

    private static String decrypt(String data, String key) throws Exception {
        Key k = toKey(Base64.decodeBase64(key.getBytes()));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k); // 初始化Cipher对象，设置为解密模式
        return new String(cipher.doFinal(Base64.decodeBase64(data.getBytes()))); // 执行解密操作
    }

    public double getBuyFfee(int tradeMappingID, Fuser fuser) {
        double ffee = this.ffeesDAO.findFfee(tradeMappingID, fuser.getFscore().getFlevel()).getFfee();
        return ffee;
    }

}
