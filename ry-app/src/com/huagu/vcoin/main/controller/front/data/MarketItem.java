package com.huagu.vcoin.main.controller.front.data;

public class MarketItem {
    private int id = 0;
    private double price = 0;
    private double amount = 0;

    public int getId() {
        return id;
    }

    public MarketItem() {
        super();
    }

    public MarketItem(int id, double price, double amount) {
        super();
        this.id = id;
        this.price = price;
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
