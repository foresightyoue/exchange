package com.huagu.vcoin.main.controller.front.data;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class Depth {
    private int date = 1520417717;
    private List<List<String>> bids = new ArrayList<>();
    private List<List<String>> asks = new ArrayList<>();

    public List<List<String>> getBids() {
        return bids;
    }

    public void setBids(List<List<String>> bids) {
        this.bids = bids;
    }

    public void addBidsItem(String val1, String val2) {
        List<String> list = new ArrayList<>();
        list.add(val1);
        list.add(val2);
        bids.add(list);
    }

    @Deprecated
    public void addBidsSampleData() {
        addBidsItem("10093.00000000", "0.2539");
        addBidsItem("10092.99000000", "1.2078");
        addBidsItem("10090.46000000", "6.6623");
    }

    public void addAsksItem(String val1, String val2) {
        List<String> list = new ArrayList<>();
        list.add(val1);
        list.add(val2);
        asks.add(list);
    }

    @Deprecated
    public void addAsksSampleData() {
        addAsksItem("10093.00000000", "0.2539");
        addAsksItem("10092.99000000", "1.2078");
        addAsksItem("10090.46000000", "6.6623");
    }

    public List<List<String>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<String>> asks) {
        this.asks = asks;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public static void main(String[] args) {
        Depth obj = new Depth();
        obj.addBidsSampleData();
        System.out.println(JSONObject.fromObject(obj));
    }

}
