package com.huagu.vcoin.main.controller.front.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

public class Fullperiod {
    private JSONArray json = new JSONArray();

    public void addItem(Date date, double v1, double v2, double v3, double v4, double v5) {
        List<Object> list = new ArrayList<>();
        list.add(date.getTime());
        list.add(v1);
        list.add(v2);
        list.add(v3);
        list.add(v4);
        list.add(v5);
        json.add(list);
    }

    @Override
    public String toString() {
        return json.toString();
    }

    @Deprecated
    public void addSampleData() {
        addItem(new Date(1519833600000L), 10230.00000000, 10230.00000000, 10097.00000000, 10137.70000000, 110.43090000);
        addItem(new Date(1519834500000L), 10099.00000000, 10223.00000000, 10089.75000000, 10182.20000000, 72.05440000);
        addItem(new Date(1519835400000L), 10185.00000000, 10185.00000000, 9950.51000000, 9951.04000000, 284.67440000);
    }

    public static void showTime(long va) {
        Date date = new Date(va);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(sdf.format(date));
    }

    public static void main(String[] args) {
        // showTime(1519833600000L);
        // showTime(1519834500000L);
        // showTime(1519835400000L);
        Fullperiod obj = new Fullperiod();
        obj.addSampleData();
        System.out.println(obj.toString());
    }
}
