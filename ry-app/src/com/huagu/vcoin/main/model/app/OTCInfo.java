package com.huagu.vcoin.main.model.app;

public class OTCInfo {
    private String businessName;
    private int dealNumInMonth;
    private String payMethod;
    private double mount;
    private double price;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public int getDealNumInMonth() {
        return dealNumInMonth;
    }

    public void setDealNumInMonth(int dealNumInMonth) {
        this.dealNumInMonth = dealNumInMonth;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public double getMount() {
        return mount;
    }

    public void setMount(double mount) {
        this.mount = mount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
