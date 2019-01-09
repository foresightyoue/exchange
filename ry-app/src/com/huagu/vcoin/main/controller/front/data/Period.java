package com.huagu.vcoin.main.controller.front.data;

import java.util.ArrayList;
import java.util.List;

public class Period {
    private int marketFrom = 12;
    private int coinVol = 7;
    private int type = 900;
    private List<String> data = new ArrayList<>();

    public int getMarketFrom() {
        return marketFrom;
    }

    public void setMarketFrom(int marketFrom) {
        this.marketFrom = marketFrom;
    }

    public int getCoinVol() {
        return coinVol;
    }

    public void setCoinVol(int coinVol) {
        this.coinVol = coinVol;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
