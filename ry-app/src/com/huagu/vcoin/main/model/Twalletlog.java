package com.huagu.vcoin.main.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_walletlog", catalog = "vcoindb")
public class Twalletlog implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -8086386880404562406L;
    private int uid_; // fid
    private Fuser fuser; // 用户id
    private Fvirtualcointype fvirtualcointype; // 币种Id
    private double total_;
    private double frozen_;
    private double totalChange_; // 可用余额变更值
    private double frozenChange_;// 冻结余额变更值
    private String changeReason_;// 变更原因
    private Timestamp changeDate_; // 变更时间
    private String guid_;// 更新guid
    private String taskId_;
    private Fentrust fentrust;

    /** default constructor */
    public Twalletlog() {
    }

    public Twalletlog(int uid_, Fuser fuser, Fvirtualcointype fvirtualcointype, double total_, double frozen_,
            double totalChange_, double frozenChange_, String changeReason_, Timestamp changeDate_, String guid_,
            String taskId_, Fentrust fentrust) {
        this.uid_ = uid_;
        this.fuser = fuser;
        this.fvirtualcointype = fvirtualcointype;
        this.total_ = total_;
        this.frozen_ = frozen_;
        this.totalChange_ = totalChange_;
        this.frozenChange_ = totalChange_;
        this.changeReason_ = changeReason_;
        this.changeDate_ = changeDate_;
        this.uid_ = uid_;
        this.taskId_ = taskId_;
        this.fentrust = fentrust;
    }

    // Property accessors
    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "uid_", unique = true, nullable = false)
    public int getUid_() {
        return uid_;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId_")
    public void setUid_(int uid_) {
        this.uid_ = uid_;
    }

    public Fuser getFuser() {
        return fuser;
    }

    public void setFuser(Fuser fuser) {
        this.fuser = fuser;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coinId_")
    public Fvirtualcointype getFvirtualcointype() {
        return fvirtualcointype;
    }

    public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
        this.fvirtualcointype = fvirtualcointype;
    }

    @Column(name = "total_", precision = 12, scale = 0)
    public double getTotal_() {
        return total_;
    }

    public void setTotal_(double total_) {
        this.total_ = total_;
    }

    @Column(name = "frozen_", precision = 12, scale = 0)
    public double getFrozen_() {
        return frozen_;
    }

    public void setFrozen_(double frozen_) {
        this.frozen_ = frozen_;
    }

    @Column(name = "totalChange_", precision = 12, scale = 0)
    public double getTotalChange_() {
        return totalChange_;
    }

    public void setTotalChange_(double totalChange_) {
        this.totalChange_ = totalChange_;
    }

    @Column(name = "frozenChange_", precision = 12, scale = 0)
    public double getFrozenChange_() {
        return frozenChange_;
    }

    public void setFrozenChange_(double frozenChange_) {
        this.frozenChange_ = frozenChange_;
    }

    @Column(name = "changeReason_", length = 32)
    public String getChangeReason_() {
        return changeReason_;
    }

    public void setChangeReason_(String changeReason_) {
        this.changeReason_ = changeReason_;
    }

    @Column(name = "changeDate_", length = 0)
    public Timestamp getChangeDate_() {
        return changeDate_;
    }

    public void setChangeDate_(Timestamp changeDate_) {
        this.changeDate_ = changeDate_;
    }

    @Column(name = "guid_", length = 128)
    public String getGuid_() {
        return guid_;
    }

    public void setGuid_(String guid_) {
        this.guid_ = guid_;
    }

    @Column(name = "taskId_", length = 128)
    public String getTaskId_() {
        return taskId_;
    }

    public void setTaskId_(String taskId_) {
        this.taskId_ = taskId_;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrustId_")
    public Fentrust getFentrust() {
        return fentrust;
    }

    public void setFentrust(Fentrust fentrust) {
        this.fentrust = fentrust;
    }

}
