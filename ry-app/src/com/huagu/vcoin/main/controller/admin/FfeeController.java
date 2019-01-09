package com.huagu.vcoin.main.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.EntrustStatusEnum;
import com.huagu.coa.common.cons.EntrustTypeEnum;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fentrust;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.EntrustService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.admin.VirtualWalletService;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.Transaction;

@Controller
public class FfeeController extends BaseController{
	 @Autowired
	 private HttpServletRequest request;
	 @Autowired
	 private VirtualWalletService virtualWalletService;   
     @Autowired
     private VirtualCoinService virtualCoinService;
	 @Autowired
	 private AdminService adminService;
     @Autowired
     private EntrustService entrustService; 
	// 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage(); 
   
    
    @RequestMapping("/ssadmin/feeList")
    public ModelAndView feeList() throws Exception {
    	JspPage modelAndView = new JspPage(request);
         modelAndView.setViewName("ssadmin/ffeeDetail");
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                filter.append("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                filter.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                filter.append("fuser.frealName like '%" + keyWord + "%' OR \n");
                filter.append("fuser.fnickName like '%" + keyWord + "%' ) \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and ftrademapping.fvirtualcointypeByFvirtualcointype2.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }

        if (request.getParameter("ftype1") != null) {
            int type1 = Integer.parseInt(request.getParameter("ftype1"));
            if (type1 != 0) {
                filter.append("and ftrademapping.fvirtualcointypeByFvirtualcointype1.fid=" + type1 + "\n");
            }
            modelAndView.addObject("ftype1", type1);
        } else {
            modelAndView.addObject("ftype1", 0);
        }

        String status = "3";
        filter.append("and fstatus=" + status + " \n");
       /* if (status != null && status.trim().length() > 0) {
            if (!status.equals("0")) {
                filter.append("and fstatus=" + status + " \n");
            }
            modelAndView.addObject("status", status);
        } else {
            modelAndView.addObject("status", 0);
        }*/

        String category = request.getParameter("category");
        if (category != null && category.trim().length() > 0) {
            if (!category.equals("-1")) {
                filter.append("and isrobotType=" + category + " \n");
            }
            modelAndView.addObject("category", category);
        } else {
            modelAndView.addObject("category", -1);
        }

        String entype = request.getParameter("entype");
        if (entype != null && entype.trim().length() > 0) {
            if (!entype.equals("-1")) {
                filter.append("and fentrustType=" + entype + " \n");
            }
            modelAndView.addObject("entype", entype);
        } else {
            modelAndView.addObject("entype", -1);
        }

       /* try {
            String price = request.getParameter("price");
            if (price != null && price.trim().length() > 0) {
                double p = Double.valueOf(price);
                filter.append("and fprize >=" + p + " \n");
            }
            modelAndView.addObject("price", price);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String price = request.getParameter("price1");
            if (price != null && price.trim().length() > 0) {
                double p = Double.valueOf(price);
                filter.append("and fprize <=" + p + " \n");
            }
            modelAndView.addObject("price1", price);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        String logDate = request.getParameter("logDate");
        if (logDate != null && logDate.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d HH:mm:ss')
            // >= '" +
            // logDate + "' \n");
            filter.append("and  fcreateTime>=  '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }

        String logDate1 = request.getParameter("logDate1");
        if (logDate1 != null && logDate1.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d HH:mm:ss')
            // <= '" +
            // logDate1 + "' \n");
            filter.append("and  fcreateTime <= '" + logDate1 + "' \n");
            modelAndView.addObject("logDate1", logDate1);
        }

        if (orderField != null && orderField.trim().length() > 0) {
            filter.append("order by " + orderField + "\n");
        } else {
            filter.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filter.append(orderDirection + "\n");
        } else {
            filter.append("desc \n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll(CoinTypeEnum.FB_CNY_VALUE, 1);
        Map<Integer, String> typeMap = new HashMap<Integer, String>();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        List<Fvirtualcointype> type1 = this.virtualCoinService.findAll(CoinTypeEnum.COIN_VALUE, 1);
        Map<Integer, String> typeMap1 = new HashMap<Integer, String>();
        for (Fvirtualcointype fvirtualcointype : type1) {
            typeMap1.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap1.put(0, "全部");
        modelAndView.addObject("typeMap1", typeMap1);

        Map<Integer, String> categoryMap = new HashMap<Integer, String>(); // 交易类别
        categoryMap.put(0, "机器人交易");
        categoryMap.put(1, "普通用户交易");
        categoryMap.put(-1, "全部");
        modelAndView.addObject("categoryMap", categoryMap);

        Map<Integer, String> statusMap = new HashMap<Integer, String>();
        statusMap.put(EntrustStatusEnum.AllDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.AllDeal));
        statusMap.put(EntrustStatusEnum.Cancel, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Cancel));
        statusMap.put(EntrustStatusEnum.Going, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Going));
        statusMap.put(EntrustStatusEnum.PartDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.PartDeal));
        statusMap.put(0, "全部");
        modelAndView.addObject("statusMap", statusMap);

        Map<Integer, String> entypeMap = new HashMap<Integer, String>();
        entypeMap.put(EntrustTypeEnum.BUY, EntrustTypeEnum.getEnumString(EntrustTypeEnum.BUY));
        entypeMap.put(EntrustTypeEnum.SELL, EntrustTypeEnum.getEnumString(EntrustTypeEnum.SELL));
        entypeMap.put(-1, "全部");
        modelAndView.addObject("entypeMap", entypeMap);

        double totalAmt = this.adminService.getSQLValue2("select sum(fleftCount) from Fentrust " + filter.toString());
        double totalQty = this.adminService
                .getSQLValue2("select sum(fsuccessAmount) from Fentrust " + filter.toString());
        Mysql mysql = new Mysql();
        MyQuery sx = new MyQuery(mysql);
        sx.add("select sum(linshi.ffees) as sumffees from (");
        // sx.add("SELECT * from %s "+filter.toString(), AppDB.fentrust);
        sx.add(" select f.fid,f.ffees,f.fStatus,f.fcreateTime,f.fentrustType,f.isrobotType from %s f left join %s fuser on ",
                AppDB.fentrust, AppDB.Fuser);
        sx.add(" f.FUs_fId = fuser.fId ");
        sx.add(" left join %s ft", AppDB.Ftrademapping);
        sx.add(" on f.ftrademapping = ft.fid ");
        // sx.add(filter.toString());
        sx.add("where 1=1");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                int fid = Integer.parseInt(keyWord);
                sx.add("and fuser.fid =" + fid + " \n");
            } catch (Exception e) {
                sx.add("and (fuser.floginName like '%" + keyWord + "%' OR \n");
                sx.add("fuser.frealName like '%" + keyWord + "%' OR \n");
                sx.add("fuser.fnickName like '%" + keyWord + "%' ) \n");
            }
            modelAndView.addObject("keywords", keyWord);
        }
        if (request.getParameter("ftype") != null) {
            int type2 = Integer.parseInt(request.getParameter("ftype"));
            if (type2 != 0) {
                sx.add("and ft.fvirtualcointype2=" + type2 + "\n");
            }
            modelAndView.addObject("ftype", type2);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        if (request.getParameter("ftype1") != null) {
            int type3 = Integer.parseInt(request.getParameter("ftype1"));
            if (type3 != 0) {
                sx.add("and ft.fvirtualcointype2=" + type3 + "\n");
            }
            modelAndView.addObject("ftype1", type3);
        } else {
            modelAndView.addObject("ftype1", 0);
        }
        sx.add("and f.fstatus=" + status + " \n");
        if (category != null && category.trim().length() > 0) {
            if (!category.equals("-1")) {
                sx.add("and isrobotType=" + category + " \n");
            }
            modelAndView.addObject("category", category);
        } else {
            modelAndView.addObject("category", -1);
        }
        if (entype != null && entype.trim().length() > 0) {
            if (!entype.equals("-1")) {
                sx.add("and fentrustType=" + entype + " \n");
            }
            modelAndView.addObject("entype", entype);
        } else {
            modelAndView.addObject("entype", -1);
        }
        if (logDate != null && logDate.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d HH:mm:ss')
            // >= '" +
            // logDate + "' \n");
            sx.add("and  f.fcreateTime>=  '" + logDate + "' \n");
            modelAndView.addObject("logDate", logDate);
        }
        if (logDate1 != null && logDate1.trim().length() > 0) {
            // filter.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d HH:mm:ss')
            // <= '" +
            // logDate1 + "' \n");
            sx.add("and  f.fcreateTime <= '" + logDate1 + "' \n");
            modelAndView.addObject("logDate1", logDate1);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            sx.add("order by " + orderField + "\n");
        } else {
            sx.add("order by fid \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            sx.add(orderDirection + "\n");
        } else {
            sx.add("desc \n");
        }
        sx.add("limit "+(currentPage - 1) * numPerPage+","+numPerPage);
        sx.add(") linshi");
        sx.open();
        
        modelAndView.addObject("fees", Utils.getDouble(sx.getCurrent().getDouble("sumffees"), 2));
        modelAndView.addObject("totalAmt", Utils.getDouble(totalAmt, 2));
        modelAndView.addObject("totalQty", Utils.getDouble(totalQty, 2));
        
        

        List<Fentrust> list = this.entrustService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("entrustList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "entrustList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fentrust", filter + ""));
        return modelAndView;
    }
    
    //提现
    @RequestMapping("/ssadmin/withdrawFeeList")
    public ModelAndView withdrawFeeList() throws Exception {
    	JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/fWithdrawFeeDetail");
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
        // 当前页
        int currentPage = 1;
        int totalCount=0;// 搜索关键字
        String keyWord = request.getParameter("keywords");
        String coinId = request.getParameter("coinId");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        List<Map<String, Object>> feeList = new ArrayList<>();
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
        	MyQuery sql = new MyQuery(handle);
        	sql.add("select e.fId,e.fuser_id,e.fcoin_id,e.ffee,e.ffee_time,e.fwithdraw_count,e.ffee_sum,e.ftotal,e.frozen,e.fstatus,"
        			+ "u.floginName,v.fShortName "
        			+ " from %s e left join %s u on e.fuser_id=u.fId left join %s v on e.fcoin_id=v.fId where 1=1","t_withdrawlog","fuser","fvirtualcointype");
        	sql.add(" and e.fstatus = %s",3);
        	if(keyWord != "" && keyWord != null){
            	sql.add("and u.floginName like '%s' ",new String("%"+keyWord+"%"));
        	}
        	if(coinId != "" && coinId != null){
                sql.add("and v.fShortName like '%s' ", new String("%" + coinId + "%"));
        	}
            sql.add("order by ffee_time desc");
            sql.setMaximum(numPerPage);
            sql.setOffset((currentPage - 1) * numPerPage);
        	sql.open();
        	for (Record record : sql) {
                feeList.add(record.getItems());
            }
        	//查询总条数
        	MyQuery sqlTotal = new MyQuery(handle);
        	sqlTotal.add("select e.fId,e.fuser_id,e.fcoin_id,e.ffee,e.ffee_time,e.fwithdraw_count,e.ffee_sum,e.ftotal,e.frozen,e.fstatus,"
        			+ "u.floginName,v.fShortName "
        			+ " from %s e left join %s u on e.fuser_id=u.fId left join %s v on e.fcoin_id=v.fId where 1=1","t_withdrawlog","fuser","fvirtualcointype");
        	sqlTotal.add(" and e.fstatus = %s",3);
        	if(keyWord != "" && keyWord != null){
        		sqlTotal.add("and u.floginName like '%s' ",new String("%"+keyWord+"%"));
        	}
        	if(coinId != "" && coinId != null){
        		sqlTotal.add("and u.fShortName like '%s' ",new String("%"+coinId+"%"));
        	}
        	sqlTotal.add("order by ffee_time desc");
        	sqlTotal.open();
        	totalCount=sqlTotal.getRecords().size();
        	
        	/*StringBuffer filter = new StringBuffer();
    		filter.append("where 1=1 \n");
    		if (StringUtils.isNotEmpty(keyWord)) {
    			filter.append(" and u.floginName like '%" + keyWord+ "%" + "\n");
    		}
    		if (StringUtils.isNotEmpty(coinId)) {
    			filter.append(" and u.fShortName like '%" + coinId + "%') \n");
    		}

    		// 总数量
    		totalCount = (int) this.virtualWalletService.getAllCount1("t_walletlog","fuser","fvirtualcointype", filter + "");
        	*/

        	modelAndView.addObject("feeList", feeList);
        	modelAndView.addObject("totalCount", totalCount);
        	modelAndView.addObject("numPerPage", numPerPage);
        	modelAndView.addObject("currentPage", currentPage);
        	modelAndView.addObject("keyWord", keyWord);
        	modelAndView.addObject("coinId", coinId);
        }catch (Exception e) {
        	modelAndView.addObject("code", 200);
        	modelAndView.addObject("message", "获取失败");
        }
		return modelAndView;
    }
		
    
    @RequestMapping("/ssadmin/otcfeeList")
    public ModelAndView otcfeeList() throws Exception {
    	JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/otcffeeDetail");
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
        // 当前页
        int currentPage = 1;
        int totalCount=0;// 搜索关键字
        String keyWord = request.getParameter("keywords");
        String coinId = request.getParameter("coinId");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        List<Map<String, Object>> otcfeeList = new ArrayList<>();
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
        	MyQuery sql = new MyQuery(handle);
        	sql.add("select e.fId,e.fUser_id,e.fCoin_id,e.ffee,e.fOrder_type,e.ffee_time,e.fOrder_object,e.fTrade_type,e.fTrade_count,"
        			+ "e.ffee_sum,e.fTotal,e.fFrozen,"
        			+ "u.floginName,v.fShortName "
        			+ " from %s e left join %s u on e.fUser_id=u.fId left join %s v on e.fCoin_id=v.fId where 1=1","ffee_detail","fuser","fvirtualcointype");
            /*
             * if(keyWord != "" && keyWord != null){
             * sql.add("and u.floginName like '%s' ",new String("%"+keyWord+"%")); }
             */
            if (keyWord != null && keyWord.trim().length() > 0) {
                try {
                    int fid = Integer.parseInt(keyWord);
                    sql.add("and u.fid =" + fid + " \n");
                } catch (Exception e) {
                    sql.add("and (u.floginName like '%" + keyWord + "%' OR \n");
                    sql.add("u.frealName like '%" + keyWord + "%' OR \n");
                    sql.add("u.fnickName like '%" + keyWord + "%' ) \n");
                }
                modelAndView.addObject("keywords", keyWord);
            }
        	if(coinId != "" && coinId != null){
                sql.add("and v.fShortName like '%s' ", new String("%" + coinId + "%"));
        	}
            sql.add("order by ffee_time desc");
            sql.setMaximum(numPerPage);
            sql.setOffset((currentPage - 1) * numPerPage);
        	sql.open();
        	for (Record record : sql) {
                otcfeeList.add(record.getItems());
            }
        	//查询总条数
        	MyQuery sqlTotal = new MyQuery(handle);
        	sqlTotal.add("select e.fUser_id,e.fCoin_id,e.ffee,e.fOrder_type,e.ffee_time,e.fOrder_object,e.fTrade_type,e.fTrade_count,"
        			+ "e.ffee_sum,e.fTotal,e.fFrozen,"
        			+ "u.floginName,v.fShortName "
        			+ " from %s e left join %s u on e.fUser_id=u.fId left join %s v on e.fCoin_id=v.fId where 1=1","ffee_detail","fuser","fvirtualcointype");
        	if(keyWord != "" && keyWord != null){
            	sql.add("and u.floginName like '%s' ",new String("%"+keyWord+"%"));
        	}
        	if(coinId != "" && coinId != null){
                sql.add("and v.fShortName like '%s' ", new String("%" + coinId + "%"));
        	}
        	sqlTotal.add("order by ffee_time desc");
        	sqlTotal.open();
        	totalCount=sqlTotal.getRecords().size();
        	
        	Mysql mysql = new Mysql();
            MyQuery sx = new MyQuery(mysql);
            StringBuffer filter = new StringBuffer();
            sx.add("select sum(linshi.ffee_sum) as sumffees from (");
            sx.add("SELECT * from %s "+filter.toString(), "ffee_detail");
            sx.add("limit "+(currentPage - 1) * numPerPage+","+numPerPage);
            sx.add(") linshi");
            if(keyWord != "" && keyWord != null){
            	filter.append("and u.floginName like %"+keyWord+"%");
        	}
        	if(coinId != "" && coinId != null){
        		filter.append("and u.fShortName like %"+coinId+"%");
        	}
            sx.open();
            modelAndView.addObject("fees", Utils.getDouble(sx.getCurrent().getDouble("sumffees"), 2));
        	modelAndView.addObject("otcfeeList", otcfeeList);
        	modelAndView.addObject("totalCount", totalCount);
        	modelAndView.addObject("numPerPage", numPerPage);
        	modelAndView.addObject("currentPage", currentPage);
        	modelAndView.addObject("keyWord", keyWord);
        	modelAndView.addObject("coinId", coinId);
        }catch (Exception e) {
        	modelAndView.addObject("code", 200);
        	modelAndView.addObject("message", "获取失败");
        }
		return modelAndView;
    }
}
