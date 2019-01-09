package com.huagu.vcoin.main.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.FcheckOverflowList;
import com.huagu.vcoin.main.service.admin.UserOverflowTracingService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;


@Controller
public class UserOverflowTracingController extends BaseController {
    private static final Logger log = Logger.getLogger(UserOverflowTracingController.class);
    
    private static String startTime;
    
    private static String endTime;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserOverflowTracingService userOverflowTracingService;
//    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("/ssadmin/UserOverflowTracing")
	public ModelAndView UserOverflowTracing() throws Exception {
		JspPage modelAndView = new JspPage(request);
		modelAndView.setViewName("ssadmin/userOverflowTracingList");
		 // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
		
// 		当前页
		int currentPage = 1;
		int totalCount = 0;
		if (request.getParameter("pageNum") != null) {
	            currentPage = Integer.parseInt(request.getParameter("pageNum"));
	    }		
		startTime = request.getParameter("startTime");
		if(startTime == "" || startTime == null)
		{
			modelAndView.addObject("startTime",startTime);
			modelAndView.addObject("USDTSum","0.0");
			modelAndView.addObject("USDTZenSum","0.0");
			modelAndView.addObject("ATSum","0.0");
			modelAndView.addObject("ATZenSum","0.0");
			modelAndView.addObject("PCSum","0.0");
			modelAndView.addObject("PCZenSum","0.0");
			return modelAndView;
		}
		endTime =  TDateTime.Now().toString();
	    String result1 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkInsertfusid");
	    String result2 = userOverflowTracingService.UserOverflowTracingInsert("checkFuserInfo");
	    String result3 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFentrust57");
	    String result4 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFentrust60");
	    String result5 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFctcorder");
	    String result6 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFvirtualoperationlog");
	    String result7 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFtransfer");
	    String result8 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkUpdatefcheckoverflowList");
	    String result9 = userOverflowTracingService.UserOverflowTracingInsert("checkUpdatefcheckoverflowList2");
		if("1".equals(result1)&&"1".equals(result2)&&"1".equals(result3)&&"1".equals(result4)&&"1".equals(result5)
				&&"1".equals(result6)&&"1".equals(result7)&&"1".equals(result8)&&"1".equals(result9))
		{
			Mysql handle = new Mysql();
			//查询记录表
			MyQuery dsselect = new MyQuery(handle);
			dsselect.add("SELECT * from %s ORDER BY fid ", AppDB.fcheckoverflowlist);
			dsselect.setOffset((currentPage -1) * numPerPage);
			dsselect.setMaximum(numPerPage);
			dsselect.open();
			
			//查询记录表
			MyQuery dsselectCount = new MyQuery(handle);
			dsselectCount.add("SELECT * from %s ORDER BY fid ", AppDB.fcheckoverflowlist);
			dsselectCount.open();
			totalCount =dsselectCount.size();
			
			//查询记录表
			MyQuery dssum = new MyQuery(handle);
			dssum.add("SELECT sum(fDifferenceUsdt) USDTSum,sum(fDifferenceUsdtzen) USDTZenSum,sum(fDifferenceAt) ATSum,sum(fDifferenceAtzen) ATZenSum,");
			dssum.add("sum(fDifferencePc) PCSum,sum(fDifferencePczen) PCZenSum from %s ORDER BY fid ", AppDB.fcheckoverflowlist);
			dssum.open();
			
			
			List<Record> lists = dsselect.getRecords();
			modelAndView.addObject("UserOverflowTracing", lists);
			modelAndView.addObject("numPerPage",numPerPage);
			modelAndView.addObject("currentPage", currentPage);
			//总数量
			modelAndView.addObject("totalCount", totalCount);
			//开始时间
			modelAndView.addObject("startTime",startTime);
			
			modelAndView.addObject("USDTSum",dssum.getCurrent().getDouble("USDTSum"));
			modelAndView.addObject("USDTZenSum",dssum.getCurrent().getDouble("USDTZenSum"));
			modelAndView.addObject("ATSum",dssum.getCurrent().getDouble("ATSum"));
			modelAndView.addObject("ATZenSum",dssum.getCurrent().getDouble("ATZenSum"));
			modelAndView.addObject("PCSum",dssum.getCurrent().getDouble("PCSum"));
			modelAndView.addObject("PCZenSum",dssum.getCurrent().getDouble("PCZenSum"));
			return modelAndView;
		}
		return modelAndView;
	}
	
	@RequestMapping("/ssadmin/UserOverflowTracingDetails")
	public ModelAndView UserOverflowTracingDetails() throws Exception {
        JspPage modelAndView = new JspPage(request);
            //当前页
      		int currentPage = 1;
      		if (request.getParameter("pageNum") != null) {
      	            currentPage = Integer.parseInt(request.getParameter("pageNum"));
      	    }
            if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            //查询记录表
    		Mysql handle = new Mysql();
    		MyQuery dsselect = new MyQuery(handle);
    		dsselect.add("SELECT * from %s where userId_ ="+fid, AppDB.t_walletlog);
    		dsselect.add(" And changeDate_ > '%s' And  changeDate_ < '%s'",startTime,endTime);
    		dsselect.add("ORDER BY changeDate_");
    		dsselect.setOffset((currentPage -1) * numPerPage);
    		dsselect.setMaximum(numPerPage);
    		dsselect.open();
    		
    		StringBuffer filter = new StringBuffer();
    		filter.append("  where 1=1 and userId_ ="+fid);
    		filter.append("  And changeDate_ > '" + startTime+"'");
    		filter.append("  And changeDate_ < '" + endTime+"'");
    		filter.append("  ORDER BY changeDate_");
    		
    		List<Record> lists = dsselect.getRecords();
    		modelAndView.addObject("uid", fid);
    		modelAndView.addObject("UserOverflowTracingDetailslog", lists);
    		modelAndView.addObject("numPerPage",numPerPage);
    		modelAndView.addObject("currentPage", currentPage);
    		
    		//总数量
    		modelAndView.addObject("totalCount", this.userOverflowTracingService.getAllCount("t_walletlog", filter + ""));
        }
        modelAndView.setViewName("ssadmin/userOverflowTracingDetailslog");
        return modelAndView;
	}
	
    @RequestMapping("ssadmin/OpenUserOverflowTracingFlatAccount")
    public ModelAndView OpenUserOverflowTracingFlatAccount() throws Exception {
        String url = request.getParameter("url");
        String uid = request.getParameter("uid");
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName(url);
        if (uid != null && !"".equals(uid)) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            //查询记录表
    		Mysql handle = new Mysql();
    		MyQuery dsselect = new MyQuery(handle);
    		dsselect.add("SELECT a.fStartUsdt,a.fStartUsdtzen,a.fStartAt,a.fStartAtzen,a.fStartPc,a.fStartPczen,");
    		dsselect.add("a.fEndUsdt,a.fEndUsdtzen,a.fEndAt,a.fEndAtzen,a.fEndPc,a.fEndPczen,");
    		dsselect.add("a.fUsdtChangeSum,a.fUsdtzenChangeSum,a.fAtChangeSum,a.fAtzenChangeSum,a.fPcChangeSum,a.fPczenChangeSum,");
    		dsselect.add("a.fDifferenceUsdt,a.fDifferenceUsdtzen,a.fDifferenceAt,a.fDifferenceAtzen,a.fDifferencePc,a.fDifferencePczen,");
    		dsselect.add("b.floginName,b.fId,b.fnickName,b.frealName from %s a, %s b  WHERE a.fuserid = b.fId AND a.fuserid = %s ",AppDB.fcheckoverflowlist,AppDB.Fuser,fid);
    		dsselect.open();
    		jspPage.addObject("fnickName", dsselect.getCurrent().getString("fnickName"));
    		List<Record> lists = dsselect.getRecords();
            jspPage.addObject("UserOverflowTracingFlatAccount", lists); 
        }
        return jspPage;
    }
	
	@RequestMapping("/ssadmin/UserOverflowTracingFlatAccount")
	public ModelAndView UserOverflowTracingFlatAccount() throws Exception {  
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        String uid = request.getParameter("uid");
        Double fDifferenceUsdts =Double.parseDouble(request.getParameter("fDifferenceUsdt"));
        Double fDifferenceUsdtzens =Double.parseDouble(request.getParameter("fDifferenceUsdtzen"));
        Double fDifferenceAts =Double.parseDouble(request.getParameter("fDifferenceAt"));
        Double fDifferenceAtzens =Double.parseDouble(request.getParameter("fDifferenceAtzen"));
        Double fDifferencePcs =Double.parseDouble(request.getParameter("fDifferencePc"));
        Double fDifferencePczens =Double.parseDouble(request.getParameter("fDifferencePczen"));
        if (uid == null || "".equals(uid)) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "用户fid不正确");
            return jspPage;
        }
        int fid = Integer.parseInt(uid);
        try (Mysql handle = new Mysql();Transaction tx=new Transaction(handle)) {
            	
            	if(fDifferenceUsdts !=0)
            	{
            		MyQuery dsSelect = new MyQuery(handle);
            		dsSelect.add("select fId,fTotal,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsSelect.add(" where fVi_fId =6 and fuid ="+fid);
            		dsSelect.open();
            		Double fTotals = dsSelect.getCurrent().getDouble("fTotal");
            		Double fFrozens = dsSelect.getCurrent().getDouble("fFrozen");
            		Double fTotalNew = fTotals -fDifferenceUsdts;
            		MyQuery dsUpdate = new MyQuery(handle);
            		dsUpdate.add("select fId,fTotal from %s set ",AppDB.fvirtualwallet);
            		dsUpdate.add(" where fVi_fId =6 and fuid ="+fid);
            		dsUpdate.open();
                    if(!dsUpdate.eof()) {
                    	dsUpdate.edit();
                    	dsUpdate.setField("fTotal",fTotalNew);
                    	dsUpdate.post();
                    }
                    
                    MyQuery dsInsert = new MyQuery(handle);
                    dsInsert.add("select fId,fsuerid,fTotal,fTotalChange,fFrozen,fFrozenChange,fVi_fId,fCreateTime from %s", AppDB.fcheckbalance_fusid);
                    dsInsert.open();
                    dsInsert.append();
                    dsInsert.setField("fsuerid", fid);
                    dsInsert.setField("fTotal", fTotals);
                    dsInsert.setField("fTotalChange",fDifferenceUsdts );
                    dsInsert.setField("fFrozen", fFrozens);
                    dsInsert.setField("fFrozenChange", 0);
                    dsInsert.setField("fVi_fId", 6);
                    dsInsert.setField("fCreateTime", TDateTime.Now());
                    dsInsert.post();
            	}
            	if(fDifferenceUsdtzens !=0)
            	{
            		MyQuery dsSelect = new MyQuery(handle);
            		dsSelect.add("select fId,fTotal,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsSelect.add(" where fVi_fId =6 and fuid ="+fid);
            		dsSelect.open();
            		Double fTotals = dsSelect.getCurrent().getDouble("fTotal");
            		Double fFrozens = dsSelect.getCurrent().getDouble("fFrozen");
            		Double fFrozenNew = fFrozens -fDifferenceUsdtzens;
            		MyQuery dsUpdate = new MyQuery(handle);
            		dsUpdate.add("select fId,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsUpdate.add(" where fVi_fId =6 and fuid ="+fid);
            		dsUpdate.open();
                    if(!dsUpdate.eof()) {
                    	dsUpdate.edit();
                    	dsUpdate.setField("fFrozen",fFrozenNew);
                    	dsUpdate.post();
                    }
                    
                    MyQuery dsInsert = new MyQuery(handle);
                    dsInsert.add("select fId,fsuerid,fTotal,fTotalChange,fFrozen,fFrozenChange,fVi_fId,fCreateTime from %s", AppDB.fcheckbalance_fusid);
                    dsInsert.open();
                    dsInsert.append();
                    dsInsert.setField("fsuerid", fid);
                    dsInsert.setField("fTotal", fTotals);
                    dsInsert.setField("fTotalChange",0 );
                    dsInsert.setField("fFrozen", fFrozens);
                    dsInsert.setField("fFrozenChange", fDifferenceUsdtzens);
                    dsInsert.setField("fVi_fId", 6);
                    dsInsert.setField("fCreateTime", TDateTime.Now());
                    dsInsert.post();
            	}
            	if(fDifferenceAts !=0)
            	{
            		MyQuery dsSelect = new MyQuery(handle);
            		dsSelect.add("select fId,fTotal,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsSelect.add(" where fVi_fId =7 and fuid ="+fid);
            		dsSelect.open();
            		Double fTotals = dsSelect.getCurrent().getDouble("fTotal");
            		Double fFrozens = dsSelect.getCurrent().getDouble("fFrozen");
            		Double fTotalNew = fTotals -fDifferenceAts;
            		MyQuery dsUpdate = new MyQuery(handle);
            		dsUpdate.add("select fId,fTotal from %s set ",AppDB.fvirtualwallet);
            		dsUpdate.add(" where fVi_fId =7 and fuid ="+fid);
            		dsUpdate.open();
                    if(!dsUpdate.eof()) {
                    	dsUpdate.edit();
                    	dsUpdate.setField("fTotal",fTotalNew);
                    	dsUpdate.post();
                    }
                    
                    MyQuery dsInsert = new MyQuery(handle);
                    dsInsert.add("select fId,fsuerid,fTotal,fTotalChange,fFrozen,fFrozenChange,fVi_fId,fCreateTime from %s", AppDB.fcheckbalance_fusid);
                    dsInsert.open();
                    dsInsert.append();
                    dsInsert.setField("fsuerid", fid);
                    dsInsert.setField("fTotal", fTotals);
                    dsInsert.setField("fTotalChange",fDifferenceAts );
                    dsInsert.setField("fFrozen", fFrozens);
                    dsInsert.setField("fFrozenChange", 0);
                    dsInsert.setField("fVi_fId", 7);
                    dsInsert.setField("fCreateTime", TDateTime.Now());
                    dsInsert.post();
            	}
            	if(fDifferenceAtzens !=0)
            	{
            		MyQuery dsSelect = new MyQuery(handle);
            		dsSelect.add("select fId,fTotal,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsSelect.add(" where fVi_fId =7 and fuid ="+fid);
            		dsSelect.open();
            		Double fTotals = dsSelect.getCurrent().getDouble("fTotal");
            		Double fFrozens = dsSelect.getCurrent().getDouble("fFrozen");
            		Double fFrozenNew = fFrozens -fDifferenceAtzens;
            		MyQuery dsUpdate = new MyQuery(handle);
            		dsUpdate.add("select fId,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsUpdate.add(" where fVi_fId =7 and fuid ="+fid);
            		dsUpdate.open();
                    if(!dsUpdate.eof()) {
                    	dsUpdate.edit();
                    	dsUpdate.setField("fFrozen",fFrozenNew);
                    	dsUpdate.post();
                    }
                    
                    MyQuery dsInsert = new MyQuery(handle);
                    dsInsert.add("select fId,fsuerid,fTotal,fTotalChange,fFrozen,fFrozenChange,fVi_fId,fCreateTime from %s", AppDB.fcheckbalance_fusid);
                    dsInsert.open();
                    dsInsert.append();
                    dsInsert.setField("fsuerid", fid);
                    dsInsert.setField("fTotal", fTotals);
                    dsInsert.setField("fTotalChange",0 );
                    dsInsert.setField("fFrozen", fFrozens);
                    dsInsert.setField("fFrozenChange", fDifferenceAtzens);
                    dsInsert.setField("fVi_fId", 7);
                    dsInsert.setField("fCreateTime", TDateTime.Now());
                    dsInsert.post();
            	}
            	if(fDifferencePcs !=0)
            	{
            		MyQuery dsSelect = new MyQuery(handle);
            		dsSelect.add("select fId,fTotal,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsSelect.add(" where fVi_fId =39 and fuid ="+fid);
            		dsSelect.open();
            		Double fTotals = dsSelect.getCurrent().getDouble("fTotal");
            		Double fFrozens = dsSelect.getCurrent().getDouble("fFrozen");
            		Double fTotalNew = fTotals -fDifferencePcs;
            		MyQuery dsUpdate = new MyQuery(handle);
            		dsUpdate.add("select fId,fTotal from %s set ",AppDB.fvirtualwallet);
            		dsUpdate.add(" where fVi_fId =39 and fuid ="+fid);
            		dsUpdate.open();
                    if(!dsUpdate.eof()) {
                    	dsUpdate.edit();
                    	dsUpdate.setField("fTotal",fTotalNew);
                    	dsUpdate.post();
                    }
                    
                    MyQuery dsInsert = new MyQuery(handle);
                    dsInsert.add("select fId,fsuerid,fTotal,fTotalChange,fFrozen,fFrozenChange,fVi_fId,fCreateTime from %s", AppDB.fcheckbalance_fusid);
                    dsInsert.open();
                    dsInsert.append();
                    dsInsert.setField("fsuerid", fid);
                    dsInsert.setField("fTotal", fTotals);
                    dsInsert.setField("fTotalChange",fDifferencePcs );
                    dsInsert.setField("fFrozen", fFrozens);
                    dsInsert.setField("fFrozenChange", 0);
                    dsInsert.setField("fVi_fId", 39);
                    dsInsert.setField("fCreateTime", TDateTime.Now());
                    dsInsert.post();
            	}
            	if(fDifferencePczens !=0)
            	{
            		MyQuery dsSelect = new MyQuery(handle);
            		dsSelect.add("select fId,fTotal,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsSelect.add(" where fVi_fId =39 and fuid ="+fid);
            		dsSelect.open();
            		Double fTotals = dsSelect.getCurrent().getDouble("fTotal");
            		Double fFrozens = dsSelect.getCurrent().getDouble("fFrozen");
            		Double fFrozenNew = fFrozens -fDifferencePczens;
            		MyQuery dsUpdate = new MyQuery(handle);
            		dsUpdate.add("select fId,fFrozen from %s set ",AppDB.fvirtualwallet);
            		dsUpdate.add(" where fVi_fId =39 and fuid ="+fid);
            		dsUpdate.open();
                    if(!dsUpdate.eof()) {
                    	dsUpdate.edit();
                    	dsUpdate.setField("fFrozen",fFrozenNew);
                    	dsUpdate.post();
                    }
                    
                    MyQuery dsInsert = new MyQuery(handle);
                    dsInsert.add("select fId,fsuerid,fTotal,fTotalChange,fFrozen,fFrozenChange,fVi_fId,fCreateTime from %s", AppDB.fcheckbalance_fusid);
                    dsInsert.open();
                    dsInsert.append();
                    dsInsert.setField("fsuerid", fid);
                    dsInsert.setField("fTotal", fTotals);
                    dsInsert.setField("fTotalChange",0 );
                    dsInsert.setField("fFrozen", fFrozens);
                    dsInsert.setField("fFrozenChange", fDifferencePczens);
                    dsInsert.setField("fVi_fId", 39);
                    dsInsert.setField("fCreateTime", TDateTime.Now());
                    dsInsert.post();
            	}
            	tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "当前用户平账失败");
            return jspPage;
        }
        jspPage.addObject("statusCode", 200);
        jspPage.addObject("callbackType", "closeCurrent");
        jspPage.addObject("message", "当前用户平账成功");
        return jspPage;
	}
	
	
	@RequestMapping("ssadmin/UserOverflowTracingExport.html")
    public ModelAndView UserOverflowTracingExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=UserOverflowTracingList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        startTime = request.getParameter("startTime");
        endTime =  TDateTime.Now().toString();
		if(startTime == "" || startTime == null)
		{
			modelAndView.addObject("startTime",startTime);
			return modelAndView;
		}
		String result1 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkInsertfusid");
		String result2 = userOverflowTracingService.UserOverflowTracingInsert("checkFuserInfo");
	    String result3 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFentrust57");
	    String result4 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFentrust60");
	    String result5 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFctcorder");
		String result6 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFvirtualoperationlog");
		String result7 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkFtransfer");
		String result8 = userOverflowTracingService.UserOverflowTracingInsert(startTime,endTime,"checkUpdatefcheckoverflowList");
		String result9 = userOverflowTracingService.UserOverflowTracingInsert("checkUpdatefcheckoverflowList2");
	    if("1".equals(result1)&&"1".equals(result2)&&"1".equals(result3)&&"1".equals(result4)&&"1".equals(result5)
					&&"1".equals(result6)&&"1".equals(result7)&&"1".equals(result8)&&"1".equals(result9))
	    {
		List<FcheckOverflowList> alls = this.userOverflowTracingService.findAll();
	    for (FcheckOverflowList FcheckOverflowData : alls) {
	            e.createRow(rowIndex++);
	            for (ExportFiled filed : ExportFiled.values()) {
	                switch (filed) {
	                case 会员ID:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getFuserid());
	                    break;
	                case 登录名:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getFloginName());
	                    break;
	                case 姓名:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfRealName());
	                    break;
	                case 电话:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfTelephone());
	                    break;
	                
	                case 初始USDT可用:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfStartUsdt());
	                    break;
	                case 当前USDT可用:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfEndUsdt());
	                    break;
	                case 区间USDT可用变动金额:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfUsdtChangeSum());
	                    break;
	                case USDT可用实际与理论差值:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfDifferenceUsdt());
	                    break;
	                    
	                case 初始USDT冻结:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfStartUsdtzen());
	                    break;
	                case 当前USDT冻结:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfEndUsdtzen());
	                    break;
	                case 区间USDT冻结变动金额:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfUsdtzenChangeSum());
	                    break;
	                case USDT冻结实际与理论差值:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfDifferenceUsdtzen());
	                    break;
	                    
	                case 初始AT可用:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfStartAt());
	                    break;
	                case 当前AT可用:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfEndAt());
	                    break;
	                case 区间AT可用变动金额:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfAtChangeSum());
	                    break;
	                case AT可用实际与理论差值:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfDifferenceAt());
	                    break;
	                    
	                case 初始AT冻结:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfStartAtzen());
	                    break;
	                case 当前AT冻结:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfEndAtzen());
	                    break;
	                case 区间AT冻结变动金额:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfAtzenChangeSum());
	                    break;
	                case AT冻结实际与理论差值:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfDifferenceAtzen());
	                    break;
	                    
	                case 初始PC可用:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfStartPc());
	                    break;
	                case 当前PC可用:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfEndPc());
	                    break;
	                case 区间PC可用变动金额:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfPcChangeSum());
	                    break;
	                case PC可用实际与理论差值:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfDifferencePc());
	                    break;
	                    
	                case 初始PC冻结:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfStartPczen());
	                    break;
	                case 当前PC冻结:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfEndPczen());
	                    break;
	                case 区间PC冻结变动金额:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfPczenChangeSum());
	                    break;
	                case PC冻结实际与理论差值:
	                    e.setCell(filed.ordinal(), FcheckOverflowData.getfDifferencePczen());
	                    break;
	                default:
	                    break;
	                }
	            }
	        }
	        e.exportXls(response);
	        response.getOutputStream().close();

	        modelAndView.setViewName("ssadmin/comm/ajaxDone");
	        modelAndView.addObject("statusCode", 200);
	        modelAndView.addObject("message", "导出成功");
	        modelAndView.addObject("startTime",startTime);
	        return modelAndView;
	    }
	    else
	    {
	    	modelAndView.addObject("startTime",startTime);
	    	return modelAndView;
	    }
    }

	 private static enum ExportFiled {
	        会员ID,登录名,姓名,电话,
	        初始USDT可用,当前USDT可用,区间USDT可用变动金额,USDT可用实际与理论差值,
	        初始USDT冻结,当前USDT冻结,区间USDT冻结变动金额,USDT冻结实际与理论差值,
	        初始AT可用,当前AT可用,区间AT可用变动金额,AT可用实际与理论差值,
	        初始AT冻结,当前AT冻结,区间AT冻结变动金额,AT冻结实际与理论差值,
	        初始PC可用,当前PC可用,区间PC可用变动金额,PC可用实际与理论差值,
	        初始PC冻结,当前PC冻结,区间PC冻结变动金额,PC冻结实际与理论差值
	 }
}