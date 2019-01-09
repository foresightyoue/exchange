package com.huagu.vcoin.main.controller.front.data;

import net.sf.json.JSONObject;

public class KlineData {
    private Depth depth = new Depth();
    private Period period = new Period();

    public Depth getDepth() {
        return depth;
    }

    public void setDepth(Depth depth) {
        this.depth = depth;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
    
    public static void main(String[] args) {
        KlineData obj = new KlineData();
        obj.getDepth().addBidsSampleData();
        obj.getDepth().addAsksSampleData();
        System.out.println(JSONObject.fromObject(obj));
    }
}
