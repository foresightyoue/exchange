package com.huagu.vcoin.main.controller.front.data;

import java.util.List;
import java.util.Map;

public class ZiCanData {
    private double fny;
    private List<Map<String, Double>> ziCans;

    public ZiCanData() {
        super();
    }

    public ZiCanData(double fny, List<Map<String, Double>> ziCans) {
        super();
        this.fny = fny;
        this.ziCans = ziCans;
    }
    public double getFny() {
        return fny;
    }

    public void setFny(double fny) {
        this.fny = fny;
    }

    public List<Map<String, Double>> getZiCans() {
        return ziCans;
    }

    public void setZiCans(List<Map<String, Double>> ziCans) {
        this.ziCans = ziCans;
    }

    @Override
    public String toString() {
        return "ZiCanData [fny=" + fny + ", ziCans=" + ziCans + "]";
    }
}
