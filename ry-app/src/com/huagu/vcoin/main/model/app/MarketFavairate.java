package com.huagu.vcoin.main.model.app;

public class MarketFavairate {
    private String mid;
    private String coin_coin;
    private boolean favairate;

    public boolean isFavairate() {
        return favairate;
    }

    public void setFavairate(boolean favairate) {
        this.favairate = favairate;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCoin_coin() {
        return coin_coin;
    }

    public void setCoin_coin(String coin_coin) {
        this.coin_coin = coin_coin;
    }
}
