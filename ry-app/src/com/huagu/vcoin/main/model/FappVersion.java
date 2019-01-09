package com.huagu.vcoin.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * @Description
 * @author Peng
 * @date 2018年3月20日
 */
@Entity
@Table(name = "fappversion", catalog = "vcoindb")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FappVersion implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int fid;
    private int type;
    private String version;
    private String url;
    private Date createTime;
    private Date updateTime;
    private String updateReadme;
    private boolean updateReset;
    private String name;
    private Integer updateStatus;
    private Integer versionCode;
    private Double apkSize;
    private String apkMd5;

    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "fid", unique = true, nullable = false)
    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "update_readme")
    public String getUpdateReadme() {
        return updateReadme;
    }

    public void setUpdateReadme(String updateReadme) {
        this.updateReadme = updateReadme;
    }

    @Column(name = "update_reset")
    public boolean isUpdateReset() {
        return updateReset;
    }

    public void setUpdateReset(boolean updateReset) {
        this.updateReset = updateReset;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "UpdateStatus")
    public Integer getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(Integer updateStatus) {
        this.updateStatus = updateStatus;
    }
    @Column(name = "VersionCode")
    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }
    @Column(name = "ApkSize")
    public Double getApkSize() {
        return apkSize;
    }

    public void setApkSize(Double apkSize) {
        this.apkSize = apkSize;
    }
    @Column(name = "ApkMd5")
    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

}
