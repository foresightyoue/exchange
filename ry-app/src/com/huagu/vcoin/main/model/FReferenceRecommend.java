package com.huagu.vcoin.main.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "fuser_reference_recommend", catalog = "vcoindb")
public class FReferenceRecommend {
    private int id;
    private String freferenceId;
    private int type;
    private Date createTime;

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

    @Column(name = "freferenceId")
    public String getFreferenceId() {
        return freferenceId;
    }

    public void setFreferenceId(String freferenceId) {
        this.freferenceId = freferenceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
