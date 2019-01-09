package com.huagu.vcoin.main.VO;


import com.huagu.vcoin.main.model.FuserReference;

public class FuserReferenceVO {
    private int rid;
    private String freferenceId;
    private Boolean hasActive;
    private Long activeTime;

    public FuserReferenceVO(FuserReference fuserReference) {
        if(null != fuserReference) {
            this.rid = fuserReference.getId();
            this.freferenceId = fuserReference.getFuid();
            this.hasActive = fuserReference.getHasActive();
            this.activeTime = fuserReference.getActiveTime() == null ? 0 : fuserReference.getActiveTime().getTime();
        }
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getFreferenceId() {
        return freferenceId;
    }

    public void setFreferenceId(String freferenceId) {
        this.freferenceId = freferenceId;
    }

    public Boolean getHasActive() {
        return hasActive;
    }

    public void setHasActive(Boolean hasActive) {
        this.hasActive = hasActive;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }
}
