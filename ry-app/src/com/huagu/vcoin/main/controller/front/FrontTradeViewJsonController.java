package com.huagu.vcoin.main.controller.front;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fperiod;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.service.front.FtradeMappingService;
import com.huagu.vcoin.main.service.front.UtilsService;

import net.sf.json.JSONObject;

/**
 * @Description
 * @author Peng
 * @date 2018年4月19日
 */
@Controller
@RequestMapping("/tradeView/")
public class FrontTradeViewJsonController extends BaseController {
    @Autowired
    private FtradeMappingService ftradeMappingService;
    @Autowired
    private UtilsService utilsService;

    private String[] resolutions = new String[] { "1", "5", "15", "30", "60", "240", "D", "7D" };

    @RequestMapping("time")
    @ResponseBody
    public long tradeViewTime() {
        return new Date().getTime() / 1000;
    }

    @RequestMapping("history")
    @ResponseBody
    public String tradeViewHistory(@RequestParam("symbol") int symbol,
            @RequestParam(value = "resolution", required = true) String resolution,
            @RequestParam(value = "from", required = true) long from,
            @RequestParam(value = "to", required = true) long to) throws ParseException {
        Map<String, Integer> items = new HashMap<>();
        items.put("1", 1); // 1分钟
        items.put("5", 5); // 5分钟
        items.put("15", 15); // 15分钟
        items.put("30", 30); // 30分钟
        items.put("60", 60); // 1小时
        items.put("240", 60 * 4); // 4小时
        items.put("D", 60 * 24); // 3天
        items.put("7D", 60 * 24 * 7); // 7天
        List<Object> tList = new ArrayList<>();
        List<Object> oList = new ArrayList<>();
        List<Object> hList = new ArrayList<>();
        List<Object> lList = new ArrayList<>();
        List<Object> cList = new ArrayList<>();
        List<Object> vList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        String status = "ok";
        from = 1000*from;
        to = 1000 * to;
        SimpleDateFormat dsf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = new Date();
        date2.setTime(from); 
        String fromTime = dsf.format(date2);
        date2.setTime(to);
        String toTime = dsf.format(date2);
        Date datefrom = dsf.parse(fromTime);
        Date dateto = dsf.parse(toTime);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("symbol", symbol);
        map.put("resolution", items.get(resolution));
        List<Fperiod> fperiodList = this.utilsService.findKlineQuery(map, datefrom, dateto);
        if (fperiodList.size() == 0) {
                status = "no_data";
        } else {
                for (Fperiod fperiod : fperiodList) {
                    tList.add(fperiod.getFtime().getTime() / 1000);
                    oList.add(fperiod.getFkai());
                    hList.add(fperiod.getFgao());
                    lList.add(fperiod.getFdi());
                    cList.add(fperiod.getFshou());
                    vList.add(fperiod.getFliang());
                }
        }
        jsonObject.accumulate("t", tList);
        jsonObject.accumulate("o", oList);
        jsonObject.accumulate("h", hList);
        jsonObject.accumulate("l", lList);
        jsonObject.accumulate("c", cList);
        jsonObject.accumulate("v", vList);
        jsonObject.accumulate("s", status);
        return jsonObject.toString();
    }

    @RequestMapping("search")
    @ResponseBody
    public List<Map<String, Object>> tradeViewSearch(@RequestParam("query") String query) {
        List<Map<String, Object>> tradeMappingList = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select concat(name1,'/',name2) name, fid from ");
            ds.add("(select m.fid,");
            ds.add("( select fShortName from fvirtualcointype f where f.fId = m.fvirtualcointype1 )  name1,");
            ds.add("( select fShortName from fvirtualcointype f where f.fId = m.fvirtualcointype2 )  name2");
            ds.add("from %s m) t", "ftrademapping");
            ds.add("where name1 like '%" + query + "%'");
            ds.add("or name2 like '%" + query + "%'");
            ds.open();
            while (ds.fetch()) {
                Map<String, Object> tradeMapping = new HashMap<>();
                tradeMapping.put("symbol", ds.getString("name"));
                tradeMapping.put("full_name", ds.getString("name"));
                tradeMapping.put("ticker", ds.getString("fid"));
                tradeMapping.put("description", ds.getString("name"));
                tradeMapping.put("exchange", "eth");
                tradeMapping.put("type", "stock");
                tradeMappingList.add(tradeMapping);
            }
        }
        return tradeMappingList;
    }

    @RequestMapping("symbols")
    @ResponseBody
    public String tradeViewSymbols(@RequestParam("symbol") int symbol) {
        JSONObject jsonObject = new JSONObject();
        Ftrademapping trademapping = ftradeMappingService.findFtrademapping2(symbol);
        // jsonObject.accumulate("name",
        // trademapping.getFvirtualcointypeByFvirtualcointype2().getfShortName()
        // + "/"
        // +
        // trademapping.getFvirtualcointypeByFvirtualcointype1().getfShortName());
        jsonObject.accumulate("ticker", trademapping.getFid().toString());
        jsonObject.accumulate("name", trademapping.getFid().toString());
        jsonObject.accumulate("currency_code", trademapping.getFid().toString());
        jsonObject.accumulate("description", trademapping.getFvirtualcointypeByFvirtualcointype2().getfShortName() + "/"
                + trademapping.getFvirtualcointypeByFvirtualcointype1().getfShortName());
        jsonObject.accumulate("type", "stock");
        jsonObject.accumulate("session", "0000-2400");
        // jsonObject.accumulate("listed_exchange", "eth");
        // jsonObject.accumulate("exchange", "eth");
        jsonObject.accumulate("timezone", "Asia/Shanghai");
        jsonObject.accumulate("minmov", 1);
        jsonObject.accumulate("pricescale", 1000000);
        jsonObject.accumulate("minmov2", 0);
        jsonObject.accumulate("has_intraday", true);
        jsonObject.accumulate("supported_resolutions", resolutions);
        jsonObject.accumulate("has_no_volume", false);
        jsonObject.accumulate("pointvalue", "1");
        return jsonObject.toString();
    }

    @RequestMapping("config")
    @ResponseBody
    public String tradeViewConfig() {
        JSONObject jsonObject = new JSONObject();
        List<Map<String, String>> exchanges = new ArrayList<>();
        List<Map<String, String>> symbols_types = new ArrayList<>();
        Map<String, String> exchangeMap = new HashMap<>();
        Map<String, String> symbolType = new HashMap<>();
        exchangeMap.put("value", "");
        exchangeMap.put("name", "");
        exchangeMap.put("desc", "");
        symbolType.put("name", "");
        symbolType.put("value", "");
        exchanges.add(exchangeMap);
        symbols_types.add(symbolType);
        jsonObject.accumulate("supports_search", true);
        jsonObject.accumulate("supports_group_request", false);
        jsonObject.accumulate("exchanges", exchanges);
        jsonObject.accumulate("symbols_types", symbols_types);
        jsonObject.accumulate("supported_resolutions", resolutions);
        jsonObject.accumulate("supports_marks", false); // 是否支持在K线上显示标记
        jsonObject.accumulate("supports_timescale_marks", false);// 是否支持时间刻度标记
        jsonObject.accumulate("supports_time", false);// 是否支持时间刻度标记

        return jsonObject.toString();
    }

}
