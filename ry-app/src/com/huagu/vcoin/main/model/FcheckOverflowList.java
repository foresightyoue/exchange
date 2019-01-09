package com.huagu.vcoin.main.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * Entrust entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fcheckoverflowlist", catalog = "vcoindb")
public class FcheckOverflowList implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8294094564128247445L;

    /**
	 * 
	 */
	

    private int fId;
    private int fuserid;
    private String floginName;
    private String fRealName;
    private String fTelephone;
    private double fBB57UsdtMoneyChange;
    private double fBB57UsdtMoneyzenChange;
    private double fBB57AtCountChange;
    private double fBB57AtCountzenChange;
    private double fBB60UsdtMoneyChange;
    private double fBB60UsdtMoneyzenChange;
    private double fBB60PcCountChange;
    private double fBB60PcCountzenChange;
    private double fUsdtCz;
    private double fAtCz;
    private double fAtJiangLi;
    private double fHTTJUsdtMoney;
    private double fHTTJUsdtMoneyzen;
    private double fHTTJATMoney;
    private double fHTTJATMoneyzen;
    private double fHTTJPCMoney;
    private double fHTTJPCMoneyzen;
    private double fFtransferAtMoney;
    private double fFtransferAtMoneyzen;
    private double fFctcorderUsdtMoney;
    private double fFctcorderUsdtMoneyzen;
    private double fUsdtChangeSum;
    private double fUsdtzenChangeSum;
    private double fAtChangeSum;
    private double fAtzenChangeSum;
    private double fPcChangeSum;
    private double fPczenChangeSum;
    private double fStartUsdt;
    private double fStartUsdtzen;
    private double fStartAt;
    private double fStartAtzen;
    private double fStartPc;
    private double fStartPczen;
    private double fEndUsdt;
    private double fEndUsdtzen;
    private double fEndAt;
    private double fEndAtzen;
    private double fEndPc;
    private double fEndPczen;
    private double fDifferenceUsdt;
    private double fDifferenceUsdtzen;
    private double fDifferenceAt;
    private double fDifferenceAtzen;
    private double fDifferencePc;
    private double fDifferencePczen;
    
    
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

	@Column(name = "fuserid")
	public int getFuserid() {
		return fuserid;
	}
	public void setFuserid(int fuserid) {
		this.fuserid = fuserid;
	}

	@Column(name = "floginName")
	public String getFloginName() {
		return floginName;
	}
	public void setFloginName(String floginName) {
		this.floginName = floginName;
	}

	@Column(name = "fRealName")
	public String getfRealName() {
		return fRealName;
	}
	public void setfRealName(String fRealName) {
		this.fRealName = fRealName;
	}

	@Column(name = "fTelephone")
	public String getfTelephone() {
		return fTelephone;
	}
	public void setfTelephone(String fTelephone) {
		this.fTelephone = fTelephone;
	}

	@Column(name = "fBB57UsdtMoneyChange", precision = 12, scale = 0)
	public double getfBB57UsdtMoneyChange() {
		return fBB57UsdtMoneyChange;
	}
	public void setfBB57UsdtMoneyChange(double fBB57UsdtMoneyChange) {
		this.fBB57UsdtMoneyChange = fBB57UsdtMoneyChange;
	}

	@Column(name = "fBB57UsdtMoneyzenChange", precision = 12, scale = 0)
	public double getfBB57UsdtMoneyzenChange() {
		return fBB57UsdtMoneyzenChange;
	}
	public void setfBB57UsdtMoneyzenChange(double fBB57UsdtMoneyzenChange) {
		this.fBB57UsdtMoneyzenChange = fBB57UsdtMoneyzenChange;
	}

	@Column(name = "fBB57AtCountChange", precision = 12, scale = 0)
	public double getfBB57AtCountChange() {
		return fBB57AtCountChange;
	}
	public void setfBB57AtCountChange(double fBB57AtCountChange) {
		this.fBB57AtCountChange = fBB57AtCountChange;
	}

	@Column(name = "fBB57AtCountzenChange", precision = 12, scale = 0)
	public double getfBB57AtCountzenChange() {
		return fBB57AtCountzenChange;
	}
	public void setfBB57AtCountzenChange(double fBB57AtCountzenChange) {
		this.fBB57AtCountzenChange = fBB57AtCountzenChange;
	}

	@Column(name = "fBB60UsdtMoneyChange", precision = 12, scale = 0)
	public double getfBB60UsdtMoneyChange() {
		return fBB60UsdtMoneyChange;
	}
	public void setfBB60UsdtMoneyChange(double fBB60UsdtMoneyChange) {
		this.fBB60UsdtMoneyChange = fBB60UsdtMoneyChange;
	}

	@Column(name = "fBB60UsdtMoneyzenChange", precision = 12, scale = 0)
	public double getfBB60UsdtMoneyzenChange() {
		return fBB60UsdtMoneyzenChange;
	}
	public void setfBB60UsdtMoneyzenChange(double fBB60UsdtMoneyzenChange) {
		this.fBB60UsdtMoneyzenChange = fBB60UsdtMoneyzenChange;
	}

	@Column(name = "fBB60PcCountChange", precision = 12, scale = 0)
	public double getfBB60PcCountChange() {
		return fBB60PcCountChange;
	}
	public void setfBB60PcCountChange(double fBB60PcCountChange) {
		this.fBB60PcCountChange = fBB60PcCountChange;
	}

	@Column(name = "fBB60PcCountzenChange", precision = 12, scale = 0)
	public double getfBB60PcCountzenChange() {
		return fBB60PcCountzenChange;
	}
	public void setfBB60PcCountzenChange(double fBB60PcCountzenChange) {
		this.fBB60PcCountzenChange = fBB60PcCountzenChange;
	}

	@Column(name = "fUsdtCz", precision = 12, scale = 0)
	public double getfUsdtCz() {
		return fUsdtCz;
	}
	public void setfUsdtCz(double fUsdtCz) {
		this.fUsdtCz = fUsdtCz;
	}

	@Column(name = "fAtCz", precision = 12, scale = 0)
	public double getfAtCz() {
		return fAtCz;
	}
	public void setfAtCz(double fAtCz) {
		this.fAtCz = fAtCz;
	}

	@Column(name = "fAtJiangLi", precision = 12, scale = 0)
	public double getfAtJiangLi() {
		return fAtJiangLi;
	}
	public void setfAtJiangLi(double fAtJiangLi) {
		this.fAtJiangLi = fAtJiangLi;
	}

	@Column(name = "fHTTJUsdtMoney", precision = 12, scale = 0)
	public double getfHTTJUsdtMoney() {
		return fHTTJUsdtMoney;
	}
	public void setfHTTJUsdtMoney(double fHTTJUsdtMoney) {
		this.fHTTJUsdtMoney = fHTTJUsdtMoney;
	}

	@Column(name = "fHTTJUsdtMoneyzen", precision = 12, scale = 0)
	public double getfHTTJUsdtMoneyzen() {
		return fHTTJUsdtMoneyzen;
	}
	public void setfHTTJUsdtMoneyzen(double fHTTJUsdtMoneyzen) {
		this.fHTTJUsdtMoneyzen = fHTTJUsdtMoneyzen;
	}

	@Column(name = "fHTTJATMoney", precision = 12, scale = 0)
	public double getfHTTJATMoney() {
		return fHTTJATMoney;
	}
	public void setfHTTJATMoney(double fHTTJATMoney) {
		this.fHTTJATMoney = fHTTJATMoney;
	}

	@Column(name = "fHTTJATMoneyzen", precision = 12, scale = 0)
	public double getfHTTJATMoneyzen() {
		return fHTTJATMoneyzen;
	}
	public void setfHTTJATMoneyzen(double fHTTJATMoneyzen) {
		this.fHTTJATMoneyzen = fHTTJATMoneyzen;
	}

	@Column(name = "fHTTJPCMoney", precision = 12, scale = 0)
	public double getfHTTJPCMoney() {
		return fHTTJPCMoney;
	}
	public void setfHTTJPCMoney(double fHTTJPCMoney) {
		this.fHTTJPCMoney = fHTTJPCMoney;
	}

	@Column(name = "fHTTJPCMoneyzen", precision = 12, scale = 0)
	public double getfHTTJPCMoneyzen() {
		return fHTTJPCMoneyzen;
	}
	public void setfHTTJPCMoneyzen(double fHTTJPCMoneyzen) {
		this.fHTTJPCMoneyzen = fHTTJPCMoneyzen;
	}

	@Column(name = "fFtransferAtMoney", precision = 12, scale = 0)
	public double getfFtransferAtMoney() {
		return fFtransferAtMoney;
	}
	public void setfFtransferAtMoney(double fFtransferAtMoney) {
		this.fFtransferAtMoney = fFtransferAtMoney;
	}

	@Column(name = "fFtransferAtMoneyzen", precision = 12, scale = 0)
	public double getfFtransferAtMoneyzen() {
		return fFtransferAtMoneyzen;
	}
	public void setfFtransferAtMoneyzen(double fFtransferAtMoneyzen) {
		this.fFtransferAtMoneyzen = fFtransferAtMoneyzen;
	}

	@Column(name = "fFctcorderUsdtMoney", precision = 12, scale = 0)
	public double getfFctcorderUsdtMoney() {
		return fFctcorderUsdtMoney;
	}
	public void setfFctcorderUsdtMoney(double fFctcorderUsdtMoney) {
		this.fFctcorderUsdtMoney = fFctcorderUsdtMoney;
	}

	@Column(name = "fFctcorderUsdtMoneyzen", precision = 12, scale = 0)
	public double getfFctcorderUsdtMoneyzen() {
		return fFctcorderUsdtMoneyzen;
	}
	public void setfFctcorderUsdtMoneyzen(double fFctcorderUsdtMoneyzen) {
		this.fFctcorderUsdtMoneyzen = fFctcorderUsdtMoneyzen;
	}

	@Column(name = "fUsdtChangeSum", precision = 12, scale = 0)
	public double getfUsdtChangeSum() {
		return fUsdtChangeSum;
	}
	public void setfUsdtChangeSum(double fUsdtChangeSum) {
		this.fUsdtChangeSum = fUsdtChangeSum;
	}
 
	@Column(name = "fUsdtzenChangeSum", precision = 12, scale = 0)
	public double getfUsdtzenChangeSum() {
		return fUsdtzenChangeSum;
	}
	public void setfUsdtzenChangeSum(double fUsdtzenChangeSum) {
		this.fUsdtzenChangeSum = fUsdtzenChangeSum;
	}

	@Column(name = "fAtChangeSum", precision = 12, scale = 0)
	public double getfAtChangeSum() {
		return fAtChangeSum;
	}
	public void setfAtChangeSum(double fAtChangeSum) {
		this.fAtChangeSum = fAtChangeSum;
	}

	@Column(name = "fAtzenChangeSum", precision = 12, scale = 0)
	public double getfAtzenChangeSum() {
		return fAtzenChangeSum;
	}
	public void setfAtzenChangeSum(double fAtzenChangeSum) {
		this.fAtzenChangeSum = fAtzenChangeSum;
	}

	@Column(name = "fPcChangeSum", precision = 12, scale = 0)
	public double getfPcChangeSum() {
		return fPcChangeSum;
	}
	public void setfPcChangeSum(double fPcChangeSum) {
		this.fPcChangeSum = fPcChangeSum;
	}

	@Column(name = "fPczenChangeSum", precision = 12, scale = 0)
	public double getfPczenChangeSum() {
		return fPczenChangeSum;
	}
	public void setfPczenChangeSum(double fPczenChangeSum) {
		this.fPczenChangeSum = fPczenChangeSum;
	}

	@Column(name = "fStartUsdt", precision = 12, scale = 0)
	public double getfStartUsdt() {
		return fStartUsdt;
	}
	public void setfStartUsdt(double fStartUsdt) {
		this.fStartUsdt = fStartUsdt;
	}

	@Column(name = "fStartUsdtzen", precision = 12, scale = 0)
	public double getfStartUsdtzen() {
		return fStartUsdtzen;
	}
	public void setfStartUsdtzen(double fStartUsdtzen) {
		this.fStartUsdtzen = fStartUsdtzen;
	}

	@Column(name = "fStartAt", precision = 12, scale = 0)
	public double getfStartAt() {
		return fStartAt;
	}
	public void setfStartAt(double fStartAt) {
		this.fStartAt = fStartAt;
	}

	@Column(name = "fStartAtzen", precision = 12, scale = 0)
	public double getfStartAtzen() {
		return fStartAtzen;
	}
	public void setfStartAtzen(double fStartAtzen) {
		this.fStartAtzen = fStartAtzen;
	}

	@Column(name = "fStartPc", precision = 12, scale = 0)
	public double getfStartPc() {
		return fStartPc;
	}
	public void setfStartPc(double fStartPc) {
		this.fStartPc = fStartPc;
	}

	@Column(name = "fStartPczen", precision = 12, scale = 0)
	public double getfStartPczen() {
		return fStartPczen;
	}
	public void setfStartPczen(double fStartPczen) {
		this.fStartPczen = fStartPczen;
	}

	@Column(name = "fEndUsdt", precision = 12, scale = 0)
	public double getfEndUsdt() {
		return fEndUsdt;
	}
	public void setfEndUsdt(double fEndUsdt) {
		this.fEndUsdt = fEndUsdt;
	}

	@Column(name = "fEndUsdtzen", precision = 12, scale = 0)
	public double getfEndUsdtzen() {
		return fEndUsdtzen;
	}
	public void setfEndUsdtzen(double fEndUsdtzen) {
		this.fEndUsdtzen = fEndUsdtzen;
	}
 
	@Column(name = "fEndAt", precision = 12, scale = 0)
	public double getfEndAt() {
		return fEndAt;
	}
	public void setfEndAt(double fEndAt) {
		this.fEndAt = fEndAt;
	}

	@Column(name = "fEndAtzen", precision = 12, scale = 0)
	public double getfEndAtzen() {
		return fEndAtzen;
	}
	public void setfEndAtzen(double fEndAtzen) {
		this.fEndAtzen = fEndAtzen;
	}

	@Column(name = "fEndPc", precision = 12, scale = 0)
	public double getfEndPc() {
		return fEndPc;
	}
	public void setfEndPc(double fEndPc) {
		this.fEndPc = fEndPc;
	}

	@Column(name = "fEndPczen", precision = 12, scale = 0)
	public double getfEndPczen() {
		return fEndPczen;
	}
	public void setfEndPczen(double fEndPczen) {
		this.fEndPczen = fEndPczen;
	}

	@Column(name = "fDifferenceUsdt", precision = 12, scale = 0)
	public double getfDifferenceUsdt() {
		return fDifferenceUsdt;
	}
	public void setfDifferenceUsdt(double fDifferenceUsdt) {
		this.fDifferenceUsdt = fDifferenceUsdt;
	}

	@Column(name = "fDifferenceUsdtzen", precision = 12, scale = 0)
	public double getfDifferenceUsdtzen() {
		return fDifferenceUsdtzen;
	}
	public void setfDifferenceUsdtzen(double fDifferenceUsdtzen) {
		this.fDifferenceUsdtzen = fDifferenceUsdtzen;
	}

	@Column(name = "fDifferenceAt", precision = 12, scale = 0)
	public double getfDifferenceAt() {
		return fDifferenceAt;
	}
	public void setfDifferenceAt(double fDifferenceAt) {
		this.fDifferenceAt = fDifferenceAt;
	}

	@Column(name = "fDifferenceAtzen", precision = 12, scale = 0)
	public double getfDifferenceAtzen() {
		return fDifferenceAtzen;
	}
	public void setfDifferenceAtzen(double fDifferenceAtzen) {
		this.fDifferenceAtzen = fDifferenceAtzen;
	}

	@Column(name = "fDifferencePc", precision = 12, scale = 0)
	public double getfDifferencePc() {
		return fDifferencePc;
	}
	public void setfDifferencePc(double fDifferencePc) {
		this.fDifferencePc = fDifferencePc;
	}

	@Column(name = "fDifferencePczen", precision = 12, scale = 0)
	public double getfDifferencePczen() {
		return fDifferencePczen;
	}
	public void setfDifferencePczen(double fDifferencePczen) {
		this.fDifferencePczen = fDifferencePczen;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}