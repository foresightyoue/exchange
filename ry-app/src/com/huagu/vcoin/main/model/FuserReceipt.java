package com.huagu.vcoin.main.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author xinchang
 *
 */
@Entity
@Table(name = "t_userreceipt", catalog = "vcoindb")
public class FuserReceipt implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8002499076454575667L;
	
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)//加这句会报fid没有默认值错误！
	private Integer fid = 1;
	private Timestamp fcreateTime;
	private String fusr_id;//用户账户
	private String ftype;//收款类型（0银行转账、1支付宝、2微信）
	private String fname;
	private String faccount;//收款账户
	private String fimgurl;//收款账户二维码
	private String fbankname;//银行名称
	private String fbanknamez;//银行支行名称
	
	@Column(name = "fid")
	public Integer getFid() {
		if(fid == null || fid.equals("")){
			fid = 0;
		}
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	@Column(name = "FCreateTime")
	public Timestamp getFcreateTime() {
		return fcreateTime;
	}
	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}
	
	@Column(name = "fUsr_id")
	public String getFusr_id() {
		return fusr_id;
	}
	public void setFusr_id(String fusr_id) {
		this.fusr_id = fusr_id;
	}
	@Column(name = "fType")
	public String getFtype() {
		return ftype;
	}
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
	@Column(name = "fName")
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	@Column(name = "fAccount")
	public String getFaccount() {
		return faccount;
	}
	public void setFaccout(String faccount) {
		this.faccount = faccount;
	}
	@Column(name = "fImgUrl")
	public String getFimgurl() {
		return fimgurl;
	}
	public void setFimgurl(String fimgurl) {
		this.fimgurl = fimgurl;
	}
	@Column(name = "fBankname")
	public String getFbankname() {
		return fbankname;
	}
	public void setFbankname(String fbankname) {
		this.fbankname = fbankname;
	}
	@Column(name = "fBanknamez")
	public String getFbanknamez() {
		return fbanknamez;
	}
	public void setFbanknamez(String fbanknamez) {
		this.fbanknamez = fbanknamez;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "FuserReceipt [fid=" + fid + ", fcreateTime=" + fcreateTime + ", fusr_id=" + fusr_id + ", ftype=" + ftype
				+ ", fname=" + fname + ", faccount=" + faccount + ", fimgurl=" + fimgurl + ", fbankname=" + fbankname
				+ ", fbanknamez=" + fbanknamez + "]";
	}
	public FuserReceipt(Integer fid, Timestamp fcreateTime, String fusr_id, String ftype, String fname, String faccount,
			String fimgurl, String fbankname, String fbanknamez) {
		super();
		this.fid = fid;
		this.fcreateTime = fcreateTime;
		this.fusr_id = fusr_id;
		this.ftype = ftype;
		this.fname = fname;
		this.faccount = faccount;
		this.fimgurl = fimgurl;
		this.fbankname = fbankname;
		this.fbanknamez = fbanknamez;
	}
	public FuserReceipt(Timestamp fcreateTime, String fusr_id, String ftype, String fname, String faccount,
			String fimgurl, String fbankname, String fbanknamez) {
		super();
		this.fcreateTime = fcreateTime;
		this.fusr_id = fusr_id;
		this.ftype = ftype;
		this.fname = fname;
		this.faccount = faccount;
		this.fimgurl = fimgurl;
		this.fbankname = fbankname;
		this.fbanknamez = fbanknamez;
	}
	public FuserReceipt() {
		
	}
	
}