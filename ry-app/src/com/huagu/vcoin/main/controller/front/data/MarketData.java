package com.huagu.vcoin.main.controller.front.data;

import java.util.ArrayList;
import java.util.List;

public class MarketData {
    private double p_new;
    private int high;
    private int low;
    private int vol;
    private double buy1;
    private double sell1;
    private double rose;
    private List<MarketItem> sells;
    private List<MarketItem> buys;
    private List<MarketItem2> trades;
    private List<MarketItem> pricenews;
    private List<MarketItem> sells1;
    private List<MarketItem> buys1;
    private List<QuestionData> quests;

    public MarketData() {
        super();
        sells = new ArrayList<>();
        buys = new ArrayList<>();
        trades = new ArrayList<>();
        pricenews = new ArrayList<>();
        sells1 = new ArrayList<>();
        buys1 = new ArrayList<>();
        quests = new ArrayList<>();
    }

    public List<QuestionData> getQuests() {
        return quests;
    }

    public void setQuests(List<QuestionData> quests) {
        this.quests = quests;
    }

    public double getP_new() {
        return p_new;
    }

    public void setP_new(double p_new) {
        this.p_new = p_new;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getVol() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public double getBuy1() {
        return buy1;
    }

    public void setBuy1(double buy1) {
        this.buy1 = buy1;
    }

    public double getSell1() {
        return sell1;
    }

    public void setSell1(double sell1) {
        this.sell1 = sell1;
    }

    public double getRose() {
        return rose;
    }

    public void setRose(double rose) {
        this.rose = rose;
    }

    public List<MarketItem> getSells() {
        return sells;
    }

    public void setSells(List<MarketItem> sells) {
        this.sells = sells;
    }

    public List<MarketItem> getBuys() {
        return buys;
    }

    public void setBuys(List<MarketItem> buys) {
        this.buys = buys;
    }

    public List<MarketItem2> getTrades() {
        return trades;
    }

    public void setTrades(List<MarketItem2> trades) {
        this.trades = trades;
    }

    public List<MarketItem> getPricenews() {
        return pricenews;
    }

    public void setPricenews(List<MarketItem> pricenews) {
        this.pricenews = pricenews;
    }

    public void setSells1(List<MarketItem> sells1) {
        this.sells1 = sells1;
    }

    public List<MarketItem> getSells1() {
        return sells1;
    }

    public void setBuys1(List<MarketItem> buys1) {
        this.buys1 = buys1;
    }

    public List<MarketItem> getBuys1() {
        return buys1;
    }
}
