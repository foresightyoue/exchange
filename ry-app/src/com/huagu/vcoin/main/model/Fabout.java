package com.huagu.vcoin.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.huagu.vcoin.util.HTMLSpirit;

/**
 * Fabout entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fabout", catalog = "vcoindb")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fabout implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 2670491400032517840L;
    private int fid;
    private String ftitle;
    private String ftitleEn;
    private String fcontent;
    private String fcontentEn;
    private String fcontent_s;
    private String ftype;

    // Constructors

    /** default constructor */
    public Fabout() {
    }

    /** full constructor */
    public Fabout(String ftitle, String fcontent) {
        this.ftitle = ftitle;
        this.fcontent = fcontent;
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

    @Column(name = "ftitle", length = 128)
    public String getFtitle() {
        return this.ftitle;
    }

    public void setFtitle(String ftitle) {
        this.ftitle = ftitle;
    }

    @Column(name = "ftitleEn", length = 128)
    public String getFtitleEn() {
        return ftitleEn;
    }

    public void setFtitleEn(String ftitleEn) {
        this.ftitleEn = ftitleEn;
    }

    @Column(name = "fcontentEn", length = 65535)
    public String getFcontentEn() {
        return fcontentEn;
    }

    public void setFcontentEn(String fcontentEn) {
        this.fcontentEn = fcontentEn;
    }

    @Column(name = "fcontent", length = 65535)
    public String getFcontent() {
        return this.fcontent;
    }

    public void setFcontent(String fcontent) {
        this.fcontent = fcontent;
    }

    @Transient
    public String getFcontent_s() {
        return HTMLSpirit.delHTMLTag(getFcontent());
    }

    public void setFcontent_s(String fcontent_s) {
        this.fcontent_s = fcontent_s;
    }

    @Column(name = "ftype")
    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

}