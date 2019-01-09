package com.huagu.vcoin.main.service.wallet;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TradingService {

    public double getCoin(String uId, String coinType) {
        double coins = 0;
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("SELECT * from fvirtualwallet WHERE fuid = %s ", uId);
        ds.add("and fVi_fId = %s", coinType);
        ds.open();
        if (ds.size() > 0) {
            while (ds.fetch()) {
                coins = ds.getDouble("fTotal");
            }
        }
        return coins;
    }

    public void updateCoin(String uId,String coinType,String coins) {
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("SELECT * from fvirtualwallet WHERE fuid = %s ", uId);
        ds.add("and fVi_fId = %s", coinType);
        ds.open();
        if (ds.size() > 0) {
            ds.edit();
            ds.setField("fTotal", coins);
            ds.post();
        }
    }

    public Map<String, Object> getTotalCoin(int uId, int ryhCoin, int rybCoin) throws Exception{
        Map<String, Object> coinMap = null;
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT * from fvirtualwallet WHERE fuid = %s ", uId);
            ds.add("and fVi_fId in ( %s , %s)", ryhCoin, rybCoin);
            ds.open();
            if (ds.size() > 0) {
                coinMap = new HashMap<>();
                while (ds.fetch()) {
                    if (ryhCoin == ds.getInt("fVi_fId")) {
                        double ryhTotal = ds.getDouble("fTotal");
                        coinMap.put("ryhFlow", ryhTotal);
                    }
                    if (rybCoin == ds.getInt("fVi_fId")) {
                        double ryhTotal = ds.getDouble("fTotal");
                        coinMap.put("rybFlow", ryhTotal);
                    }
                }

            }
        } catch (Exception e) {
            new Exception();
        }
        return coinMap;
    }

    public JSONObject transferCoin(String toCoinType,String userId,String transferType,String coins) {
        JSONObject json = new JSONObject();
        Mysql mysql = new Mysql();
        MyQuery ds2 = new MyQuery(mysql);
        ds2.add("SELECT fId from fvirtualcointype WHERE fName_en = '%s'", toCoinType + "");
        ds2.open();
        if (ds2.size() < 1) {
            json.accumulate("status", 203);
            json.accumulate("msg", "该币不存在");
            return json;
        }
        MyQuery ds3 = new MyQuery(mysql);
        ds3.add("SELECT fId from fuser WHERE floginName = '%s' or fTelephone = '%s' ", userId + "",
                userId + "");
        ds3.open();
        if (ds3.size() < 1) {
            json.accumulate("status", 300);
            json.accumulate("msg", "未查找到用户");
            return json;
        }
        int coinType = ds2.getInt("fId");
        int uId = ds3.getInt("fId");
        MyQuery ds = new MyQuery(mysql);
        ds.add("SELECT * from fvirtualwallet WHERE fuid = %s ", uId);
        ds.add("and fVi_fId = %s", coinType);
        ds.open();
        if (ds.size() > 0) {
            ds.edit();
            if (transferType.equals("RTT")) {
                ds.setField("fTotal", ds.getDouble("fTotal") + Double.parseDouble(coins + ""));
            } else if (transferType.equals("TTR")) {
                double ftotal = ds.getDouble("fTotal");
                double coin = Double.parseDouble(coins + "");
                if (ftotal >= coin) {
                    ds.setField("fTotal", ds.getDouble("fTotal") - Double.parseDouble(coins + ""));
                } else {
                    json.accumulate("status", 202);
                    json.accumulate("msg", "余额不足");
                    return json;
                }
            }
            ds.post();
            json.accumulate("status", 200);
            json.accumulate("msg", "请求成功");
            } else {
                json.accumulate("status", 204);
                json.accumulate("msg", "钱包异常，请稍后再试");
                return json;
            }
        return json;
    }
}
