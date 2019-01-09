package com.huagu.vcoin.main.controller.front.data;
public class QuestionData {
    private String fanswer;
    private int userOfsys;
    private int isask;
    private int ftype;
    private int fqid;
    private String floginName;
    private int issee;
    private String fdesc;

    public String getFdesc() {
        return fdesc;
    }

    public void setFdesc(String fdesc) {
        this.fdesc = fdesc;
    }

    public String getFloginName() {
        return floginName;
    }

    public void setFloginName(String floginName) {
        this.floginName = floginName;
    }

    public QuestionData(String fanswer, int userOfsys, int isask, int ftype, int fqid, String floginName, int issee,
            String fdesc) {
        super();
        this.fanswer = fanswer;
        this.userOfsys = userOfsys;
        this.isask = isask;
        this.ftype = ftype;
        this.fqid = fqid;
        this.floginName = floginName;
        this.issee = issee;
        this.fdesc = fdesc;
    }

    public QuestionData(String fanswer, int userOfsys, int fqid, String fdesc) {
        this.fanswer = fanswer;
        this.userOfsys = userOfsys;
        this.fqid = fqid;
        this.fdesc = fdesc;
    }

    public QuestionData(int userOfsys) {
        super();
        this.userOfsys = userOfsys;
    }
    public String getFanswer() {
        return fanswer;
    }

    public void setFanswer(String fanswer) {
        this.fanswer = fanswer;
    }

    public int getIssee() {
        return issee;
    }

    public void setIssee(int issee) {
        this.issee = issee;
    }

    public int getUserOfsys() {
        return userOfsys;
    }

    public void setUserOfsys(int userOfsys) {
        this.userOfsys = userOfsys;
    }

    public int getIsask() {
        return isask;
    }

    public void setIsask(int isask) {
        this.isask = isask;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public int getFqid() {
        return fqid;
    }

    public void setFqid(int fqid) {
        this.fqid = fqid;
    }

}
