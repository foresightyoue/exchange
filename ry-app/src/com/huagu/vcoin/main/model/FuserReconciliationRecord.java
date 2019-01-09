package com.huagu.vcoin.main.model;


import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * Entrust entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fuserReconciliationRecord", catalog = "vcoindb")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FuserReconciliationRecord implements java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8684374340865090684L;
	/**
	 * 
	 */
	// Fields
    private int fId;
    private int fusid;
    private double fusdtDifference;
    private double fatDifference;
    private double fpcDifference;
    private double fusdtftotal;
    private double fusdtffrozen;
    private double fusdtTotal;
    private double fatftotal;
    private double fatffrozen;
    private double fatTotal;
    private double fpcftotal;
    private double fpcffrozen;
    private double fpcTotal;
    private double faccountsUsdtTotal;
    private double faccountsAtTotal;
    private double faccountsPcTotal;
    private double fusdtCZ;
    private double fatCZ;
    private double fbuyATSellUsdtCount;
    private double fbuyATCount;
    private double fbuyUsdtSellATCount;
    private double fsellATCount;
    private double fbuyPCSellUsdtCount;
    private double fbuyPCCount;
    private double fbuyUsdtSellPCCount;
    private double fsellPCCount;
    private double fatJiangLi;
    private double fusdtHTTJ;
    private double fatHTTJ;
    private double fpcHTTJ;
    private String fname;
    private double fusdtCTCBuy;
    private double fusdtCTCSell;
    private double fAtftransfer;
    //private double fotcSellUsdt;
    
    
 
   @Column(name = "fname")
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	// Property accessors
    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")  
    @Column(name = "fId", unique = true, nullable = false)
	public int getfId() {
		return fId;
	}

	public void setfId(int fId) {
		this.fId = fId;
	}
	
	@Column(name = "fusid")
	public int getFusid(){
		return fusid;
	}

	public void setFusid(int fusid) {
		this.fusid = fusid;
	}

	@Column(name = "fusdtDifference", precision = 12, scale = 0)
	public double getFusdtDifference() {
		return fusdtDifference;
	}

	public void setFusdtDifference(double fusdtDifference) {
		this.fusdtDifference = fusdtDifference;
	}
	
	@Column(name = "fatDifference", precision = 12, scale = 0)
	public double getFatDifference() {
		return fatDifference;
	}

	public void setFatDifference(double fatDifference) {
		this.fatDifference = fatDifference;
	}
	
	@Column(name = "fpcDifference", precision = 12, scale = 0)
	public double getFpcDifference() {
		return fpcDifference;
	}

	public void setFpcDifference(double fpcDifference) {
		this.fpcDifference = fpcDifference;
	}

	@Column(name = "fusdtftotal", precision = 12, scale = 0)
	public double getFusdtftotal() {
		return fusdtftotal;
	}

	public void setFusdtftotal(double fusdtftotal) {
		this.fusdtftotal = fusdtftotal;
	}

	@Column(name = "fusdtffrozen", precision = 12, scale = 0)
	public double getFusdtffrozen() {
		return fusdtffrozen;
	}

	public void setFusdtffrozen(double fusdtffrozen) {
		this.fusdtffrozen = fusdtffrozen;
	}

	@Column(name = "fusdtTotal", precision = 12, scale = 0)
	public double getFusdtTotal() {
		return fusdtTotal;
	}

	public void setFusdtTotal(double fusdtTotal) {
		this.fusdtTotal = fusdtTotal;
	}

	@Column(name = "fatftotal", precision = 12, scale = 0)
	public double getFatftotal() {
		return fatftotal;
	}

	public void setFatftotal(double fatftotal) {
		this.fatftotal = fatftotal;
	}

	@Column(name = "fatffrozen", precision = 12, scale = 0)
	public double getFatffrozen() {
		return fatffrozen;
	}

	public void setFatffrozen(double fatffrozen) {
		this.fatffrozen = fatffrozen;
	}

	@Column(name = "fatTotal", precision = 12, scale = 0)
	public double getFatTotal() {
		return fatTotal;
	}

	public void setFatTotal(double fatTotal) {
		this.fatTotal = fatTotal;
	}

	@Column(name = "fpcftotal", precision = 12, scale = 0)
	public double getFpcftotal() {
		return fpcftotal;
	}

	public void setFpcftotal(double fpcftotal) {
		this.fpcftotal = fpcftotal;
	}

	@Column(name = "fpcffrozen", precision = 12, scale = 0)
	public double getFpcffrozen() {
		return fpcffrozen;
	}

	public void setFpcffrozen(double fpcffrozen) {
		this.fpcffrozen = fpcffrozen;
	}

	@Column(name = "fpcTotal", precision = 12, scale = 0)
	public double getFpcTotal() {
		return fpcTotal;
	}

	public void setFpcTotal(double fpcTotal) {
		this.fpcTotal = fpcTotal;
	}

	@Column(name = "faccountsUsdtTotal", precision = 12, scale = 0)
	public double getFaccountsUsdtTotal() {
		return faccountsUsdtTotal;
	}

	public void setFaccountsUsdtTotal(double faccountsUsdtTotal) {
		this.faccountsUsdtTotal = faccountsUsdtTotal;
	}

	@Column(name = "faccountsAtTotal", precision = 12, scale = 0)
	public double getFaccountsAtTotal() {
		return faccountsAtTotal;
	}

	public void setFaccountsAtTotal(double faccountsAtTotal) {
		this.faccountsAtTotal = faccountsAtTotal;
	}

	@Column(name = "faccountsPcTotal", precision = 12, scale = 0)
	public double getFaccountsPcTotal() {
		return faccountsPcTotal;
	}

	public void setFaccountsPcTotal(double faccountsPcTotal) {
		this.faccountsPcTotal = faccountsPcTotal;
	}

	@Column(name = "fusdtCZ", precision = 12, scale = 0)
	public double getFusdtCZ() {
		return fusdtCZ;
	}

	public void setFusdtCZ(double fusdtCZ) {
		this.fusdtCZ = fusdtCZ;
	}

	@Column(name = "fatCZ", precision = 12, scale = 0)
	public double getFatCZ() {
		return fatCZ;
	}

	public void setFatCZ(double fatCZ) {
		this.fatCZ = fatCZ;
	}

	@Column(name = "fbuyATSellUsdtCount", precision = 12, scale = 0)
	public double getFbuyATSellUsdtCount() {
		return fbuyATSellUsdtCount;
	}

	public void setFbuyATSellUsdtCount(double fbuyATSellUsdtCount) {
		this.fbuyATSellUsdtCount = fbuyATSellUsdtCount;
	}

	@Column(name = "fbuyATCount", precision = 12, scale = 0)
	public double getFbuyATCount() {
		return fbuyATCount;
	}

	public void setFbuyATCount(double fbuyATCount) {
		this.fbuyATCount = fbuyATCount;
	}

	@Column(name = "fbuyUsdtSellATCount", precision = 12, scale = 0)
	public double getFbuyUsdtSellATCount() {
		return fbuyUsdtSellATCount;
	}

	public void setFbuyUsdtSellATCount(double fbuyUsdtSellATCount) {
		this.fbuyUsdtSellATCount = fbuyUsdtSellATCount;
	}

	@Column(name = "fsellATCount", precision = 12, scale = 0)
	public double getFsellATCount() {
		return fsellATCount;
	}

	public void setFsellATCount(double fsellATCount) {
		this.fsellATCount = fsellATCount;
	}

	@Column(name = "fbuyPCSellUsdtCount", precision = 12, scale = 0)
	public double getFbuyPCSellUsdtCount() {
		return fbuyPCSellUsdtCount;
	}

	public void setFbuyPCSellUsdtCount(double fbuyPCSellUsdtCount) {
		this.fbuyPCSellUsdtCount = fbuyPCSellUsdtCount;
	}

	@Column(name = "fbuyPCCount", precision = 12, scale = 0)
	public double getFbuyPCCount() {
		return fbuyPCCount;
	}

	public void setFbuyPCCount(double fbuyPCCount) {
		this.fbuyPCCount = fbuyPCCount;
	}

	@Column(name = "fbuyUsdtSellPCCount", precision = 12, scale = 0)
	public double getFbuyUsdtSellPCCount() {
		return fbuyUsdtSellPCCount;
	}

	public void setFbuyUsdtSellPCCount(double fbuyUsdtSellPCCount) {
		this.fbuyUsdtSellPCCount = fbuyUsdtSellPCCount;
	}

	@Column(name = "fsellPCCount", precision = 12, scale = 0)
	public double getFsellPCCount() {
		return fsellPCCount;
	}

	public void setFsellPCCount(double fsellPCCount) {
		this.fsellPCCount = fsellPCCount;
	}

	@Column(name = "fatJiangLi", precision = 12, scale = 0)
	public double getFatJiangLi() {
		return fatJiangLi;
	}

	public void setFatJiangLi(double fatJiangLi) {
		this.fatJiangLi = fatJiangLi;
	}

	@Column(name = "fusdtHTTJ", precision = 12, scale = 0)
	public double getFusdtHTTJ() {
		return fusdtHTTJ;
	}

	public void setFusdtHTTJ(double fusdtHTTJ) {
		this.fusdtHTTJ = fusdtHTTJ;
	}

	@Column(name = "fatHTTJ", precision = 12, scale = 0)
	public double getFatHTTJ() {
		return fatHTTJ;
	}

	public void setFatHTTJ(double fatHTTJ) {
		this.fatHTTJ = fatHTTJ;
	}

	@Column(name = "fpcHTTJ", precision = 12, scale = 0)
	public double getFpcHTTJ() {
		return fpcHTTJ;
	}

	public void setFpcHTTJ(double fpcHTTJ) {
		this.fpcHTTJ = fpcHTTJ;
	}

	@Column(name = "fusdtCTCBuy", precision = 12, scale = 0)
	public double getFusdtCTCBuy() {
		return fusdtCTCBuy;
	}

	public void setFusdtCTCBuy(double fusdtCTCBuy) {
		this.fusdtCTCBuy = fusdtCTCBuy;
	}

	@Column(name = "fusdtCTCSell", precision = 12, scale = 0)
	public double getFusdtCTCSell() {
		return fusdtCTCSell;
	}

	public void setFusdtCTCSell(double fusdtCTCSell) {
		this.fusdtCTCSell = fusdtCTCSell;
	}

	@Column(name = "fAtftransfer", precision = 12, scale = 0)
	public double getfAtftransfer1() {
		return fAtftransfer;
	}

	public void setfAtftransfer1(double fAtftransfer) {
		this.fAtftransfer = fAtftransfer;
	}
	
	/*@Column(name = "fotcSellUsdt", precision = 12, scale = 0)
	public double getFotcSellUsdt() {
			return fotcSellUsdt;
    }

	public void setFotcSellUsdt(double fotcSellUsdt) {
			this.fotcSellUsdt = fotcSellUsdt;
	}*/
}