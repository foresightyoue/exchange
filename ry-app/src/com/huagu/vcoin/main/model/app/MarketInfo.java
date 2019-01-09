package com.huagu.vcoin.main.model.app;

public class MarketInfo {
    private Integer id;
    private String coin_coin;
    private Double deal_amount;
    private Double price;
    private Double amount;
    private Double amount_increase;
    private String coinType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoin_coin() {
        return coin_coin;
    }

    public void setCoin_coin(String coin_coin) {
        this.coin_coin = coin_coin;
    }

    public Double getDeal_amount() {
        return deal_amount;
    }

    public void setDeal_amount(Double deal_amount) {
        this.deal_amount = deal_amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount_increase() {
        return amount_increase;
    }

    public void setAmount_increase(Double amount_increase) {
        this.amount_increase = amount_increase;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }
}
