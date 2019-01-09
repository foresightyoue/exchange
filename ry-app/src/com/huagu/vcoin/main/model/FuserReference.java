package com.huagu.vcoin.main.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "fuser_reference_relation", catalog = "vcoindb")
public class FuserReference {
    private int id;
    private String fuid;
    private String freferenceId;
    private Boolean hasActive;
    private Date activeTime;

    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "fuid")
    public String getFuid() {
        return fuid;
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }

    @Column(name = "freferenceId")
    public String getFreferenceId() {
        return freferenceId;
    }

    public void setFreferenceId(String freferenceId) {
        this.freferenceId = freferenceId;
    }

    @Column(name = "hasActive")
    public Boolean getHasActive() {
        return hasActive;
    }

    public void setHasActive(Boolean hasActive) {
        this.hasActive = hasActive;
    }

    @Column(name = "activeTime")
    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }
}
