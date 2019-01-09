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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.huagu.vcoin.main.Enum.QuestionStatusEnum;
import com.huagu.vcoin.main.Enum.QuestionTypeEnum;

/**
 * Fquestion entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fquestion", catalog = "vcoindb")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fquestion implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 6486896441486514504L;
    private int fid;
    private String fname;
    private Fuser fuser;
    private int ftype;// QuestionTypeEnum
    private String ftype_s;
    private String fdesc;
    private int fstatus;// QuestionStatusEnum
    private String fstatus_s;
    private String ftelephone;
    private Timestamp fcreateTime;
    private Timestamp fSolveTime;
    private String fanswer;
    private Fadmin fadmin;// 解决问题的客服
    private int version;
    private int fqid;
    private int isask;
    private int userOfsys;
    private int issee;
    private int isallsys;
    private int isalluser;
    // Constructors


    /** default constructor */
    public Fquestion() {
    }

    /** full constructor */
    public Fquestion(Fuser fuser, int ftype, String fdesc, String ftelephone) {
        this.fuser = fuser;
        this.ftype = ftype;
        this.fdesc = fdesc;
        this.ftelephone = ftelephone;
    }

    public Fquestion(Fuser fuser, int ftype, String fdesc, int issee, int userOfsys, int fqid, String fanswer) {
        this.fuser = fuser;
        this.ftype = ftype;
        this.fdesc = fdesc;
        this.issee = issee;
        this.userOfsys = userOfsys;
        this.fqid = fqid;
        this.fanswer = fanswer;
    }

    // Property accessors
    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "fid", unique = true, nullable = false)
    public Integer getFid() {
        return this.fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuid")
    public Fuser getFuser() {
        return this.fuser;
    }

    public void setFuser(Fuser fuser) {
        this.fuser = fuser;
    }

    @Column(name = "ftype")
    public int getFtype() {
        return this.ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    @Column(name = "fdesc", length = 1024)
    public String getFdesc() {
        return this.fdesc;
    }

    public void setFdesc(String fdesc) {
        this.fdesc = fdesc;
    }

    @Column(name = "ftelephone", length = 32)
    public String getFtelephone() {
        return this.ftelephone;
    }

    public void setFtelephone(String ftelephone) {
        this.ftelephone = ftelephone;
    }

    @Column(name = "fstatus")
    public int getFstatus() {
        return fstatus;
    }

    public void setFstatus(int fstatus) {
        this.fstatus = fstatus;
    }

    @Transient
    public String getFstatus_s() {
        int status = getFstatus();
        return QuestionStatusEnum.getEnumString(status);
    }

    public void setFstatus_s(String fstatus_s) {
        this.fstatus_s = fstatus_s;
    }

    @Column(name = "fcreateTime")
    public Timestamp getFcreateTime() {
        return fcreateTime;
    }

    public void setFcreateTime(Timestamp fcreateTime) {
        this.fcreateTime = fcreateTime;
    }

    @Column(name = "fSolveTime")
    public Timestamp getfSolveTime() {
        return fSolveTime;
    }

    public void setfSolveTime(Timestamp fSolveTime) {
        this.fSolveTime = fSolveTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faid")
    public Fadmin getFadmin() {
        return fadmin;
    }

    public void setFadmin(Fadmin fadmin) {
        this.fadmin = fadmin;
    }

    @Transient
    public String getFtype_s() {
        int type = getFtype();
        return QuestionTypeEnum.getEnumString(type);
    }

    public void setFtype_s(String ftype_s) {
        this.ftype_s = ftype_s;
    }

    @Column(name = "fname")
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Column(name = "fanswer")
    public String getFanswer() {
        return fanswer;
    }

    public void setFanswer(String fanswer) {
        this.fanswer = fanswer;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Column(name = "fqid")
    public int getFqid() {
        return fqid;
    }

    public void setFqid(int fqid) {
        this.fqid = fqid;
    }

    @Column(name = "isask")
    public int getIsask() {
        return isask;
    }

    public void setIsask(int isask) {
        this.isask = isask;
    }

    @Column(name = "userOfsys")
    public int getUserOfsys() {
        return userOfsys;
    }

    public void setUserOfsys(int userOfsys) {
        this.userOfsys = userOfsys;
    }

    @Column(name = "issee")
    public int getIssee() {
        return issee;
    }

    public void setIssee(int issee) {
        this.issee = issee;
    }

    @Column(name = "isallsys")
    public int getIsallsys() {
        return isallsys;
    }

    public void setIsallsys(int isallsys) {
        this.isallsys = isallsys;
    }

    @Column(name = "isalluser")
    public int getIsalluser() {
        return isalluser;
    }

    public void setIsalluser(int isalluser) {
        this.isalluser = isalluser;
    }
}