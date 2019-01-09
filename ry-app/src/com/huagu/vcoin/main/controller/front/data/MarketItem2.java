package com.huagu.vcoin.main.controller.front.data;

public class MarketItem2 {
    private int id = 0;
    private double price = 0;
    private double amount = 0;
    private String time = "12:00";

    public int getId() {
        return id;
    }

    public MarketItem2() {
        super();
    }

    public MarketItem2(int id, double price, double amount) {
        super();
        this.id = id;
        this.price = price;
        this.amount = amount;
    }

    public MarketItem2(int id, double price, double amount, String time) {
        super();
        this.id = id;
        this.price = price;
        this.amount = amount;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
