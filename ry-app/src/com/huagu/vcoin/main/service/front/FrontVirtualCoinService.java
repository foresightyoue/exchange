package com.huagu.vcoin.main.service.front;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationInStatusEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationOutStatusEnum;
import com.huagu.vcoin.main.Enum.VirtualCapitalOperationTypeEnum;
import com.huagu.vcoin.main.Enum.VirtualCoinTypeStatusEnum;
import com.huagu.vcoin.main.dao.FvirtualaddressDAO;
import com.huagu.vcoin.main.dao.FvirtualaddressWithdrawDAO;
import com.huagu.vcoin.main.dao.FvirtualcaptualoperationDAO;
import com.huagu.vcoin.main.dao.FvirtualcointypeDAO;
import com.huagu.vcoin.main.dao.FvirtualwalletDAO;
import com.huagu.vcoin.main.dao.FwithdrawfeesDAO;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualaddress;
import com.huagu.vcoin.main.model.FvirtualaddressWithdraw;
import com.huagu.vcoin.main.model.Fvirtualcaptualoperation;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fvirtualwallet;
import com.huagu.vcoin.main.model.Fwithdrawfees;
import com.huagu.vcoin.util.Utils;

import site.jayun.vcoin.wallet.BTCUtils;
import site.jayun.vcoin.wallet.OmniUtils;
import site.jayun.vcoin.wallet.WalletConfig;
import site.jayun.vcoin.wallet.WalletFactory;
import site.jayun.vcoin.wallet.WalletUtil;

@Service
public class FrontVirtualCoinService {
    @Autowired
    private FvirtualcointypeDAO fvirtualcointypeDAO;
    @Autowired
    private FwithdrawfeesDAO withdrawfeesDAO;
    @Autowired
    private FvirtualaddressDAO fvirtualaddressDAO;
    @Autowired
    private FvirtualaddressWithdrawDAO fvirtualaddressWithdrawDAO;
    @Autowired
    private FvirtualcaptualoperationDAO fvirtualcaptualoperationDAO;
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private FrontSystemArgsService frontSystemArgsService;

    public void updateWithdrawBtc(FvirtualaddressWithdraw fvirtualaddressWithdraw, Fvirtualcointype fvirtualcointype,
            Fvirtualwallet fvirtualwallet, double withdrawAmount, double ffees, Fuser fuser) {
        try {
            fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() - withdrawAmount);
            fvirtualwallet.setFfrozen(fvirtualwallet.getFfrozen() + withdrawAmount);
            fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp());
            this.fvirtualwalletDAO.attachDirty(fvirtualwallet);

            Fvirtualcaptualoperation fvirtualcaptualoperation = new Fvirtualcaptualoperation();
            fvirtualcaptualoperation.setFamount(withdrawAmount - ffees);
            fvirtualcaptualoperation.setFcreateTime(Utils.getTimestamp());
            fvirtualcaptualoperation.setFfees(ffees);
            fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());
            fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation);
            fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_OUT);
            fvirtualcaptualoperation.setFuser(fuser);
            fvirtualcaptualoperation.setFvirtualcointype(fvirtualcointype);
            fvirtualcaptualoperation.setWithdraw_virtual_address(fvirtualaddressWithdraw.getFadderess());
            this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public List<Fvirtualcointype> findFvirtualCoinType(int status, int coinType) {
        List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findByParam(0, 0,
                " where fstatus=" + status + " and ftype =" + coinType + " order by fid asc ", false,
                Fvirtualcointype.class);
        return list;
    }

    /**
     * 查询以太坊一条记录
     * 
     * @return
     */
    public List<Fvirtualcointype> findFvirtualCoinTypeEth() {
        List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findByParam(0, 0, " where fisEth=1 order by fid asc ",
                false, Fvirtualcointype.class);
        return list;
    }

    public Fvirtualcointype findFvirtualCoinById(int id) {
        Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(id);
        return fvirtualcointype;
    }

    public List findByProperty(String propertyName, Object value) {
        return this.fvirtualcointypeDAO.findByProperty(propertyName, value);
    }

    public Fvirtualcointype findFirstFirtualCoin() {
        Fvirtualcointype fvirtualcointype = null;
        String filter = "where fstatus=" + VirtualCoinTypeStatusEnum.Normal + " and ftype=" + CoinTypeEnum.COIN_VALUE;
        List<Fvirtualcointype> list = this.fvirtualcointypeDAO.list(0, 0, filter, false);
        if (list.size() > 0) {
            fvirtualcointype = list.get(0);
        } else {
            fvirtualcointype = (Fvirtualcointype) this.fvirtualcointypeDAO.findAll(CoinTypeEnum.FB_CNY_VALUE, 1).get(0);
        }
        return fvirtualcointype;
    }

    public Fvirtualcointype findFirstFirtualCoin_Wallet() {
        Fvirtualcointype fvirtualcointype = null;
        String filter = "where fstatus=" + VirtualCoinTypeStatusEnum.Normal + " and FIsWithDraw=1";
        List<Fvirtualcointype> list = this.fvirtualcointypeDAO.list(0, 0, filter, false);
        if (list.size() > 0) {
            fvirtualcointype = list.get(0);
        }
        return fvirtualcointype;
    }

    public Fvirtualaddress findFvirtualaddress(Fuser fuser, Fvirtualcointype fvirtualcointype) {
        return this.fvirtualaddressDAO.findFvirtualaddress(fuser, fvirtualcointype);
    }

    public List<Fvirtualaddress> findFvirtualaddress(Fvirtualcointype fvirtualcointype, String address) {
        return this.fvirtualaddressDAO.findFvirtualaddress(fvirtualcointype, address);
    }

    public FvirtualaddressWithdraw findFvirtualaddressWithdraw(int fid) {
        return this.fvirtualaddressWithdrawDAO.findById(fid);
    }

    public List<FvirtualaddressWithdraw> findFvirtualaddressWithdraws(int firstResult, int maxResults, String filter,
            boolean isFY) {
        return this.fvirtualaddressWithdrawDAO.list(firstResult, maxResults, filter, isFY);
    }

    public int findFvirtualcaptualoperationCount(Fuser fuser, int type[], int status[],
            Fvirtualcointype[] fvirtualcointypes, String order) {
        return this.fvirtualcaptualoperationDAO.findFvirtualcaptualoperationCount(fuser, type, status,
                fvirtualcointypes, order);
    }

    public List<Fvirtualcaptualoperation> findFvirtualcaptualoperations(int firstResult, int maxResults, String filter,
            boolean isFY) {
        return this.fvirtualcaptualoperationDAO.findByParam(firstResult, maxResults, filter, isFY,
                Fvirtualcaptualoperation.class);
    }

    public int findFvirtualcaptualoperationsCount(String filter) {
        return this.fvirtualcaptualoperationDAO.findByParamCount(filter, Fvirtualcaptualoperation.class);
    }

    public void updateFvirtualaddressWithdraw(FvirtualaddressWithdraw fvirtualaddressWithdraw) {
        this.fvirtualaddressWithdrawDAO.save(fvirtualaddressWithdraw);
    }

    public void updateDelFvirtualaddressWithdraw(FvirtualaddressWithdraw fvirtualaddressWithdraw) {
        this.fvirtualaddressWithdrawDAO.delete(fvirtualaddressWithdraw);
    }

    public Fwithdrawfees findFfees(int virtualCoinTypeId, int level) {
        return this.withdrawfeesDAO.findFfee(virtualCoinTypeId, level);
    }

    public void updateWithdrawBtc(FvirtualaddressWithdraw fvirtualaddressWithdraw, Fvirtualcointype fvirtualcointype,
            Fvirtualwallet fvirtualwallet, double withdrawAmount, Fuser fuser, double curPrice) {
        Fvirtualcaptualoperation fvirtualcaptualoperation = new Fvirtualcaptualoperation();
        try (Mysql handle = new Mysql()) {
            String priceRange = frontSystemArgsService.getSystemArgs("priceRange");// 获取提现自动审核起始值系统参数
            double begin = 0.0;
            double end = 0.0;
            if (priceRange != null && !"".equals(priceRange)) {
                begin = Double.parseDouble(priceRange.split(",")[0]);
                end = Double.parseDouble(priceRange.split(",")[1]);
            }
            withdrawAmount = Utils.getDouble(withdrawAmount, 4);
            if (withdrawAmount > begin && withdrawAmount < end) {
                // 冻结数量
                double frozenRmb = Utils.getDouble(fvirtualwallet.getFfrozen(), 4);
                if (frozenRmb - withdrawAmount < -0.0001) {
                    throw new RuntimeException(String.format("冻结数量%s小于提现数量:%s", frozenRmb, withdrawAmount));
                }

                // 自动发送
                String user_address = fvirtualaddressWithdraw.getFadderess();
                String trade_number;

                WalletConfig config = initWalletConfig(fvirtualcointype);

                if (fvirtualcaptualoperation.getFtradeUniqueNumber() != null
                        && fvirtualcaptualoperation.getFtradeUniqueNumber().trim().length() > 0) {
                    throw new RuntimeException(String.format("非法操作！请检查钱包！"));
                }

                try {
                    String coinType = fvirtualcointype.getfShortName();

                    int userId = fuser.getFid();
                    int coinId = fvirtualcointype.getFid();

                    WalletUtil util = WalletFactory.build(config, fvirtualcointype.isFisEth() ? "ETH" : "BTC");
                    String mainAddress = config.getMainAddress();
                    if ("USDT".equals(coinType)) {
                        util = new OmniUtils(config);
                    }

                    // 检查余额
                    double balance = getMainBalance(util, mainAddress);

                    if (balance < withdrawAmount) {
                        throw new RuntimeException(String.format("钱包余额：%s小于提现金额:%s", balance, withdrawAmount));
                    }

                    // 校验提现地址
                    boolean isUsable = util.validateAddress(user_address);
                    if (!isUsable) {
                        throw new RuntimeException(String.format("提现地址无效"));
                    }

                    // 执行虚拟币转账
                    if (util instanceof OmniUtils) {
                        DecimalFormat df = new DecimalFormat("0.########");

                        double btcBalance = util.getBalance(((BTCUtils) util).getAccount(mainAddress));
                        if (btcBalance <= 0) {
                            throw new RuntimeException("USDT钱包的 比特币 余额不足");
                        }

                        trade_number = ((OmniUtils) util).sendOmniTransaction(mainAddress, user_address,
                                OmniUtils.propertyId, df.format(withdrawAmount));
                    } else {
                        trade_number = util.sendTransaction(mainAddress, user_address, withdrawAmount);
                    }
                    if (trade_number == null) {
                        throw new RuntimeException(String.format("钱包转账失败  %s", util.getMessage()));
                    }

                    // 虚拟币钱包扣钱
                    fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() - withdrawAmount);
                    fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp());

                    this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
                    // 更新虚拟币操作状态
                    double feeRate = this.withdrawfeesDAO
                            .findFfee(fvirtualcointype.getFid(), fuser.getFscore().getFlevel()).getFfee();
                    fvirtualcaptualoperation.setFamount(withdrawAmount * (1 - feeRate));
                    fvirtualcaptualoperation.setFcreateTime(Utils.getTimestamp());
                    fvirtualcaptualoperation.setFfees(withdrawAmount * feeRate);
                    fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());
                    fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_OUT);
                    fvirtualcaptualoperation.setFuser(fuser);
                    fvirtualcaptualoperation.setFremark("价格：" + curPrice);
                    fvirtualcaptualoperation.setFvirtualcointype(fvirtualcointype);
                    fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationSuccess);
                    fvirtualcaptualoperation.setWithdraw_virtual_address(user_address);

                    fvirtualcaptualoperation.setFtradeUniqueNumber(trade_number);// 流水号

                    this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(String.format("钱包连接失败"));
                }
            } else {
                fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() - withdrawAmount);
                fvirtualwallet.setFfrozen(fvirtualwallet.getFfrozen() + withdrawAmount);
                fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp());
                this.fvirtualwalletDAO.attachDirty(fvirtualwallet);

                double feeRate = this.withdrawfeesDAO.findFfee(fvirtualcointype.getFid(), fuser.getFscore().getFlevel())
                        .getFfee();

                fvirtualcaptualoperation.setFamount(withdrawAmount * (1 - feeRate));
                fvirtualcaptualoperation.setFcreateTime(Utils.getTimestamp());
                fvirtualcaptualoperation.setFfees(withdrawAmount * feeRate);
                fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());
                fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation);
                fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_OUT);
                fvirtualcaptualoperation.setFuser(fuser);
                fvirtualcaptualoperation.setFremark("价格：" + curPrice);
                fvirtualcaptualoperation.setFvirtualcointype(fvirtualcointype);
                fvirtualcaptualoperation.setWithdraw_virtual_address(fvirtualaddressWithdraw.getFadderess());
                this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private double getMainBalance(WalletUtil util, String mainAddress) {
        if (util instanceof OmniUtils) {
            return ((OmniUtils) util).getOmniBalance(mainAddress, OmniUtils.propertyId);
        }

        if (util instanceof BTCUtils) {
            return util.getBalance(((BTCUtils) util).getAccount(mainAddress));
        }

        return util.getBalance(mainAddress);
    }

    private WalletConfig initWalletConfig(Fvirtualcointype fvirtualcointype) {
        WalletConfig config = new WalletConfig();
        config.setAccessKey(fvirtualcointype.getFaccess_key());
        config.setSecretKey(fvirtualcointype.getFsecrt_key());
        config.setIP(fvirtualcointype.getFip());
        config.setPort(fvirtualcointype.getFport());
        config.setPassword(fvirtualcointype.getFpassword());
        config.setMainAddress(fvirtualcointype.getMainAddr());
        return config;
    }

    public void addFvirtualcaptualoperation(Fvirtualcaptualoperation fvirtualcaptualoperation) {
        this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation);
    }

    public List<Fvirtualcaptualoperation> findFvirtualcaptualoperationByProperty(String key, Object value) {
        return this.fvirtualcaptualoperationDAO.findByProperty(key, value);
    }

    public Fvirtualcaptualoperation findFvirtualcaptualoperationById(int id) {
        return this.fvirtualcaptualoperationDAO.findById(id);
    }

    // 比特币自动充值并加币
    public void updateFvirtualcaptualoperationCoinIn(Fvirtualcaptualoperation fvirtualcaptualoperation)
            throws Exception {
        try {
            Fvirtualcaptualoperation real = this.fvirtualcaptualoperationDAO
                    .findById(fvirtualcaptualoperation.getFid());
            if (real != null && real.getFstatus() != VirtualCapitalOperationInStatusEnum.SUCCESS) {
                real.setFstatus(fvirtualcaptualoperation.getFstatus());
                real.setFconfirmations(fvirtualcaptualoperation.getFconfirmations());
                real.setFlastUpdateTime(Utils.getTimestamp());
                real.setFamount(fvirtualcaptualoperation.getFamount());
                this.fvirtualcaptualoperationDAO.attachDirty(real);

                if (real.getFstatus() == VirtualCapitalOperationInStatusEnum.SUCCESS && real.isFhasOwner()) {
                    Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO
                            .findById(real.getFvirtualcointype().getFid());
                    Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(real.getFuser().getFid(),
                            fvirtualcointype.getFid());
                    fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + real.getFamount());
                    fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp());
                    this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // 比特币自动充值并加币
    public void updateFvirtualcaptualoperationCoinInxx(Fvirtualcaptualoperation fvirtualcaptualoperation)
            throws Exception {
        try {
            fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());
            this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation);
            if (fvirtualcaptualoperation.getFstatus() == VirtualCapitalOperationInStatusEnum.SUCCESS
                    && fvirtualcaptualoperation.isFhasOwner()) {
                Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO
                        .findById(fvirtualcaptualoperation.getFvirtualcointype().getFid());
                Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO
                        .findVirtualWallet(fvirtualcaptualoperation.getFuser().getFid(), fvirtualcointype.getFid());
                fvirtualwallet.setFtotal(fvirtualwallet.getFtotal() + fvirtualcaptualoperation.getFamount());
                fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp());
                this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public List<Fvirtualaddress> findFvirtualaddressByProperty(String key, Object value) {
        List<Fvirtualaddress> fvirtualaddresses = this.fvirtualaddressDAO.findByProperty(key, value);
        for (Fvirtualaddress fvirtualaddress : fvirtualaddresses) {
            fvirtualaddress.getFuser().getFnickName();
        }
        return fvirtualaddresses;
    }

    public boolean isExistsCanWithdrawCoinType() {
        List<Fvirtualcointype> fvirtualcointypes = this.fvirtualcointypeDAO.findByParam(0, 0,
                " where FIsWithDraw=1 and fstatus=1 ", false, Fvirtualcointype.class);
        return fvirtualcointypes.size() > 0;
    }

}
