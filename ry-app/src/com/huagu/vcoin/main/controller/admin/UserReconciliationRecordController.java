package com.huagu.vcoin.main.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.huagu.vcoin.main.model.FuserReconciliationRecordss;
import com.huagu.vcoin.main.service.admin.UserReconciliationRecordService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.UpdateMode;


@Controller
public class UserReconciliationRecordController extends BaseController {
	private int numPerPage = Utils.getNumPerPage();
    private static final Logger log = Logger.getLogger(UserReconciliationRecordController.class);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserReconciliationRecordService userReconciliationRecordService;

	@RequestMapping("/ssadmin/UserReconciliationRecordBykeywords")
	public ModelAndView UserReconciliationRecordBykeywords() throws Exception {
		JspPage modelAndView = new JspPage(request);
		modelAndView.setViewName("ssadmin/userReconciliationRecordlogList");
		 // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
		String keyWord = request.getParameter("keywords").toString();
		//当前页
		int currentPage = 1;
		int totalCount = 0;
		if (request.getParameter("pageNum") != null) {
	            currentPage = Integer.parseInt(request.getParameter("pageNum"));
	    }	
		if (keyWord != "") 
		{
			 modelAndView.addObject("keywords", keyWord);
			 CommonMethods(keyWord);
			//查询对账记录表
			Mysql handle = new Mysql();
			MyQuery dsselect = new MyQuery(handle);
			dsselect.add("SELECT * from %s ORDER BY fid ", AppDB.fuserReconciliationRecord);
			dsselect.setOffset((currentPage -1) * numPerPage);
			dsselect.setMaximum(numPerPage);
			dsselect.open();
			totalCount = dsselect.size();
			List<Record> lists = dsselect.getRecords();
			modelAndView.addObject("userReconciliationRecord", lists);
			modelAndView.addObject("numPerPage",numPerPage);
			modelAndView.addObject("currentPage", currentPage);
			//总数量R
			modelAndView.addObject("totalCount", totalCount);
			modelAndView.addObject("keyWords", keyWord);
			return modelAndView;
		} 
		else
		{
			return modelAndView;
		}
	}
	/*首次进入*/
	@RequestMapping("/ssadmin/UserReconciliationRecordlists")
	public ModelAndView UserReconciliationRecordlists() throws Exception {
		JspPage modelAndView = new JspPage(request);
		modelAndView.setViewName("ssadmin/userReconciliationRecordlogList");
	    return modelAndView;
	}
	
	public void CommonMethods(String keyWord)
	{
		Mysql handle = new Mysql();
		// 清除指定用户id表
		MyQuery ds0 = new MyQuery(handle);
		ds0.add("select fId,fuserid from %s ", AppDB.fcheckbalance_fusid);
		ds0.open();
		if(ds0.size()>0)
		{
			List<Record> listdelete = ds0.getRecords();
			for(int i=0;i<listdelete.size();i++)
			{
				MyQuery dsdetele = new MyQuery(handle);
				dsdetele.add("select fId,fuserid from %s ", AppDB.fcheckbalance_fusid);
				dsdetele.add(" where fId ="+listdelete.get(i).getInt("fId"));
				dsdetele.open();
				dsdetele.delete();
				dsdetele.clear();
			}
		}
		
		// 清除指定用户对账信息表
		MyQuery ds1 = new MyQuery(handle);
		ds1.add("select fId,fusid,fusdtDifference,fatDifference,fpcDifference,fusdtftotal,fusdtffrozen,fusdtTotal,fatftotal");
		ds1.add("fatffrozen,fatTotal,fpcftotal,fpcffrozen,fpcTotal,faccountsUsdtTotal,faccountsAtTotal,faccountsPcTotal,fusdtCZ");
		ds1.add("fatCZ,fbuyATSellUsdtCount,fbuyATCount,fbuyUsdtSellATCount,fsellATCount,fbuyPCSellUsdtCount,fbuyPCCount,fbuyUsdtSellPCCount,fsellPCCount");
		ds1.add("fatJiangLi,fusdtHTTJ,fatHTTJ,fpcHTTJ,fusdtCTCBuy,fusdtCTCSell,fAtftransfer");
		ds1.add(" from %s ", AppDB.fuserReconciliationRecord);	
		ds1.open();
		if(ds1.size()>0)
		{
			List<Record> listdelete1 = ds1.getRecords();
			for(int i=0;i<listdelete1.size();i++)
			{
				MyQuery dsdetele1 = new MyQuery(handle);
				dsdetele1.add("select fId,fusid,fusdtDifference,fatDifference,fpcDifference,fusdtftotal,fusdtffrozen,fusdtTotal,fatftotal");
				dsdetele1.add("fatffrozen,fatTotal,fpcftotal,fpcffrozen,fpcTotal,faccountsUsdtTotal,faccountsAtTotal,faccountsPcTotal,fusdtCZ");
				dsdetele1.add("fatCZ,fbuyATSellUsdtCount,fbuyATCount,fbuyUsdtSellATCount,fsellATCount,fbuyPCSellUsdtCount,fbuyPCCount,fbuyUsdtSellPCCount,fsellPCCount");
				dsdetele1.add("fatJiangLi,fusdtHTTJ,fatHTTJ,fpcHTTJ,fusdtCTCBuy,fusdtCTCSell,fAtftransfer");
				dsdetele1.add(" from %s ", AppDB.fuserReconciliationRecord);	
				dsdetele1.add(" where fId ="+listdelete1.get(i).getInt("fId"));
				dsdetele1.open();
				dsdetele1.delete();
				dsdetele1.clear();
			}
		}
		
		/*临时表添加信息*/
		StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            boolean flag = true;
            int fid = 0;
            try {
                fid = Integer.parseInt(keyWord);
            } catch (Exception e) {
                flag = false;
            }
            filter.append("and(");
            if (flag) {
                filter.append("fid =" + fid + " or \n");
            }
            filter.append("floginName like '%" + keyWord + "%' or \n");
            filter.append("fnickName like '%" + keyWord + "%'  or \n");
            filter.append("frealName like '%" + keyWord + "%'  or \n");
            filter.append("ftelephone like '%" + keyWord + "%'  or \n");
            filter.append("femail like '%" + keyWord + "%'  or \n");
            filter.append("fidentityNo like '%" + keyWord + "%' )\n");
        }
        
        MyQuery dslist = new MyQuery(handle);
        dslist.add("select DISTINCT fId from %s ", AppDB.Fuser);
        dslist.add(filter.toString());
        dslist.open();
        List<Record> Lists = dslist.getRecords();
		MyQuery dss = new MyQuery(handle);
		for (int i = 0; i < Lists.size(); i++) {
			dss.add("select fId,fuserid from %s", AppDB.fcheckbalance_fusid);
			dss.open();
			dss.append();
			dss.setField("fuserid", Lists.get(i).getInt("fId"));
			dss.post();
			dss.clear();
		}
		

		/* 钱包usdt */
		MyQuery ds2 = new MyQuery(handle);
		ds2.add("SELECT a.fuid fid,a.ftotal Usdtftotal,a.ffrozen Usdtffrozen,a.ftotal+a.ffrozen UsdtTotal FROM ");
		ds2.add(" %s a,%s b WHERE a.fuid=b.fuserid AND a.fvi_fid=6 ORDER BY b.fid", AppDB.fvirtualwallet,
				AppDB.fcheckbalance_fusid);
		ds2.open();

		MyQuery ds22 = new MyQuery(handle);
		for (Record ds222 : ds2) {
			ds22.add("select fId,fusid,fusdtftotal,fusdtffrozen,fusdtTotal from %s",
					AppDB.fuserReconciliationRecord);
			ds22.open();
			ds22.append();
			ds22.setField("fusid", ds222.getField("fid"));
			ds22.setField("fusdtftotal", ds222.getField("Usdtftotal"));
			ds22.setField("fusdtffrozen", ds222.getField("Usdtffrozen"));
			ds22.setField("fusdtTotal", ds222.getField("UsdtTotal"));
			ds22.post();
			ds22.clear();
		}

		/* 钱包at */
		MyQuery ds3 = new MyQuery(handle);
		ds3.add("SELECT a.fuid fid,a.ftotal Atftotal,a.ffrozen Atffrozen,a.ftotal+a.ffrozen AtTotal FROM ");
		ds3.add(" %s a,%s b WHERE a.fuid=b.fuserid AND a.fvi_fid=7 ORDER BY b.fid", AppDB.fvirtualwallet,
				AppDB.fcheckbalance_fusid);
		ds3.open();

		List<Record> list33 = ds3.getRecords();
		for (int i = 0; i < list33.size(); i++) {
			MyQuery ds33 = new MyQuery(handle);
			ds33.add("select fId,fusid,fatftotal ,fatffrozen ,fatTotal from %s",
					AppDB.fuserReconciliationRecord);
			ds33.add("where fusid = '%s'", list33.get(i).getInt("fid"));
			ds33.open();
			if(!ds33.eof()){
				ds33.edit();
				ds33.setField("fatftotal", list33.get(i).getDouble("Atftotal"));
				ds33.setField("fatffrozen", list33.get(i).getDouble("Atffrozen"));
				ds33.setField("fatTotal", list33.get(i).getDouble("AtTotal"));
				ds33.post();
			}
		}

		/* 钱包pc */
		MyQuery ds4 = new MyQuery(handle);
		ds4.add("SELECT a.fuid fid,a.ftotal Pcftotal,a.ffrozen Pcffrozen,a.ftotal+a.ffrozen PcTotal FROM ");
		ds4.add(" %s a,%s b WHERE a.fuid=b.fuserid AND a.fvi_fid=39 ORDER BY b.fid", AppDB.fvirtualwallet,
				AppDB.fcheckbalance_fusid);
		ds4.open();

		List<Record> list44 = ds4.getRecords();
		for (int i = 0; i < list44.size(); i++) {
			MyQuery ds44 = new MyQuery(handle);
			ds44.add("select fId,fusid,fpcftotal ,fpcffrozen ,fpcTotal from %s",
					AppDB.fuserReconciliationRecord);
			ds44.add("where fusid = '%s'", list44.get(i).getInt("fid"));
			ds44.open();
			if(!ds44.eof()){
				ds44.edit();
				ds44.setField("fpcftotal", list44.get(i).getDouble("Pcftotal"));
				ds44.setField("fpcffrozen", list44.get(i).getDouble("Pcffrozen"));
				ds44.setField("fpcTotal", list44.get(i).getDouble("PcTotal"));
				ds44.post();
			}
		}

		/* usdt充值 */
		MyQuery ds5 = new MyQuery(handle);
		ds5.add("SELECT b.fuserid fid,sum(a.fAmount) UsdtCZ FROM %s b LEFT JOIN  %s a ",
				AppDB.fcheckbalance_fusid, AppDB.fvirtualcaptualoperation);
		ds5.add("ON a.fUs_fid2=b.fuserid AND a.fvi_fid2=6 AND a.fType = 1 AND a.fStatus = 3 GROUP BY b.fuserid ORDER BY b.fid ");
		ds5.open();

		List<Record> list55 = ds5.getRecords();
		for (int i = 0; i < list55.size(); i++) {
			MyQuery ds55 = new MyQuery(handle);

			ds55.add("select fId,fusid,fusdtCZ from %s", AppDB.fuserReconciliationRecord);
			ds55.add("where fusid = '%s'", list55.get(i).getInt("fid"));
			ds55.getDefaultOperator().setUpdateMode(UpdateMode.loose);
			ds55.open();
			if(!ds55.eof()){
				ds55.edit();
				ds55.setField("fusdtCZ",
						list55.get(i).getString("UsdtCZ") == null ? 0 : list55.get(i).getDouble("UsdtCZ"));
				ds55.post();
			}
		}

		/* at充值 */
		MyQuery ds6 = new MyQuery(handle);
		ds6.add("SELECT b.fuserid fid,sum(a.fAmount) AtCZ FROM %s b LEFT JOIN  %s a ",
				AppDB.fcheckbalance_fusid, AppDB.fvirtualcaptualoperation);
		ds6.add("ON a.fUs_fid2=b.fuserid AND a.fvi_fid2=7 AND a.fType = 1 AND a.fStatus = 3 GROUP BY b.fuserid ORDER BY b.fid ");
		ds6.open();

		List<Record> list66 = ds6.getRecords();
		for (int i = 0; i < list66.size(); i++) {
			MyQuery ds66 = new MyQuery(handle);

			ds66.add("select fId,fusid,fatCZ from %s", AppDB.fuserReconciliationRecord);
			ds66.add("where fusid = '%s'", list66.get(i).getInt("fid"));
			ds66.open();
			if(!ds66.eof()){
				ds66.edit();
				ds66.setField("fatCZ",
						list66.get(i).getString("AtCZ") == null ? 0 : list66.get(i).getDouble("AtCZ"));
				ds66.post();
			}
		}

		/* 币币交易57买 */
		MyQuery ds7 = new MyQuery(handle);
		ds7.add("SELECT a.fuserid fid,sum(c.fAmount) BuyATSellUsdtCount,sum(c.fCount) BuyATCount FROM ");
		ds7.add(" %s a LEFT JOIN %s b ON a.fuserid = b.fUs_fid LEFT JOIN %s c ON  b.fid= c.FEn_fId",
				AppDB.fcheckbalance_fusid, AppDB.fentrust, AppDB.fentrustlog);
		ds7.add(" AND b.ftrademapping=57 AND b.fstatus in (2,3,4) AND b.fEntrustType = 0 AND b.fsuccessAmount >0 GROUP BY a.fuserid ORDER BY a.fid ");
		ds7.open();

		List<Record> list77 = ds7.getRecords();
		for (int i = 0; i < list77.size(); i++) {
			MyQuery ds77 = new MyQuery(handle);

			ds77.add("select fId,fusid,fbuyATSellUsdtCount,fbuyATCount from %s",
					AppDB.fuserReconciliationRecord);
			ds77.add("where fusid = '%s'", list77.get(i).getInt("fid"));
			ds77.open();
			if(!ds77.eof()){
				ds77.edit();
				ds77.setField("fbuyATSellUsdtCount", list77.get(i).getString("BuyATSellUsdtCount") == null ? 0
						: list77.get(i).getDouble("BuyATSellUsdtCount"));
				ds77.setField("fbuyATCount",
						list77.get(i).getString("BuyATCount") == null ? 0 : list77.get(i).getDouble("BuyATCount"));
				ds77.post();
			}
		}

		/* 币币交易57卖 */
		MyQuery ds8 = new MyQuery(handle);
		ds8.add("SELECT a.fuserid fid,sum(c.fAmount) BuyUsdtSellAtCount,sum(c.fCount) SellATCount FROM ");
		ds8.add(" %s a LEFT JOIN %s b ON a.fuserid = b.fUs_fid LEFT JOIN %s c ON  b.fid= c.FEn_fId",
				AppDB.fcheckbalance_fusid, AppDB.fentrust, AppDB.fentrustlog);
		ds8.add(" AND b.ftrademapping=57 AND b.fstatus in (2,3,4) AND b.fEntrustType = 1 AND b.fsuccessAmount >0 GROUP BY a.fuserid ORDER BY a.fid ");
		ds8.open();

		List<Record> list88 = ds8.getRecords();
		for (int i = 0; i < list88.size(); i++) {
			MyQuery ds88 = new MyQuery(handle);

			ds88.add("select fId,fusid,fbuyUsdtSellATCount,fsellATCount from %s",
					AppDB.fuserReconciliationRecord);
			ds88.add("where fusid = '%s'", list88.get(i).getInt("fid"));
			ds88.open();
			if(!ds88.eof()){
				ds88.edit();
				ds88.setField("fbuyUsdtSellATCount", list88.get(i).getString("BuyUsdtSellAtCount") == null ? 0
						: list88.get(i).getDouble("BuyUsdtSellAtCount"));
				ds88.setField("fsellATCount", list88.get(i).getString("SellATCount") == null ? 0
						: list88.get(i).getDouble("SellATCount"));
				ds88.post();
			}
		}

		/* 币币交易60买 */
		MyQuery ds9 = new MyQuery(handle);
		ds9.add("SELECT a.fuserid fid,sum(c.fAmount) BuyPCSellUsdtCount,sum(c.fCount) BuyPCCount FROM ");
		ds9.add(" %s a LEFT JOIN %s b ON a.fuserid = b.fUs_fid LEFT JOIN %s c ON  b.fid= c.FEn_fId",
				AppDB.fcheckbalance_fusid, AppDB.fentrust, AppDB.fentrustlog);
		ds9.add(" AND b.ftrademapping=60 AND b.fstatus in (2,3,4) AND b.fEntrustType = 0 AND b.fsuccessAmount >0 GROUP BY a.fuserid ORDER BY a.fid ");
		ds9.open();

		List<Record> list99 = ds9.getRecords();
		for (int i = 0; i < list99.size(); i++) {
			MyQuery ds99 = new MyQuery(handle);

			ds99.add("select fId,fusid,fbuyPCSellUsdtCount,fbuyPCCount from %s",
					AppDB.fuserReconciliationRecord);
			ds99.add("where fusid = '%s'", list99.get(i).getInt("fid"));
			ds99.open();
			if(!ds99.eof()){
				ds99.edit();
				ds99.setField("fbuyPCSellUsdtCount", list99.get(i).getString("BuyPCSellUsdtCount") == null ? 0
						: list99.get(i).getDouble("BuyPCSellUsdtCount"));
				ds99.setField("fbuyPCCount",
						list99.get(i).getString("BuyPCCount") == null ? 0 : list99.get(i).getDouble("BuyPCCount"));
				ds99.post();
			}
		}

		/* 币币交易60卖 */
		MyQuery ds10 = new MyQuery(handle);
		ds10.add("SELECT a.fuserid fid,sum(c.fAmount) BuyUsdtSellPCCount,sum(c.fCount) SellPCCount FROM ");
		ds10.add(" %s a LEFT JOIN %s b ON a.fuserid = b.fUs_fid LEFT JOIN %s c ON  b.fid= c.FEn_fId",
				AppDB.fcheckbalance_fusid, AppDB.fentrust, AppDB.fentrustlog);
		ds10.add(
				" AND b.ftrademapping=60 AND b.fstatus in (2,3,4) AND b.fEntrustType = 1 AND b.fsuccessAmount >0 GROUP BY a.fuserid ORDER BY a.fid ");
		ds10.open();

		List<Record> list1010 = ds10.getRecords();
		for (int i = 0; i < list1010.size(); i++) {
			MyQuery ds1010 = new MyQuery(handle);

			ds1010.add("select fId,fusid,fbuyUsdtSellPCCount,fsellPCCount from %s",
					AppDB.fuserReconciliationRecord);
			ds1010.add("where fusid = '%s'", list1010.get(i).getInt("fid"));
			ds1010.open();
			if(!ds1010.eof()){
				ds1010.edit();
				ds1010.setField("fbuyUsdtSellPCCount", list1010.get(i).getString("BuyUsdtSellPCCount") == null ? 0
						: list1010.get(i).getDouble("BuyUsdtSellPCCount"));
				ds1010.setField("fsellPCCount", list1010.get(i).getString("SellPCCount") == null ? 0
						: list1010.get(i).getDouble("SellPCCount"));
				ds1010.post();
			}
		}

		/* 奖励AT */
		MyQuery ds11 = new MyQuery(handle);
		ds11.add("SELECT b.fuserid fid,sum(a.fqty) ATJiangLi FROM %s b LEFT JOIN %s a ",
				AppDB.fcheckbalance_fusid, AppDB.fintrolinfo);
		ds11.add("ON a.fuserid=b.fuserid AND a.fname='AT链' GROUP BY b.fuserid ORDER BY b.fid");
		ds11.open();

		List<Record> list1111 = ds11.getRecords();
		for (int i = 0; i < list1111.size(); i++) {
			MyQuery ds1111 = new MyQuery(handle);

			ds1111.add("select fId,fusid,fatJiangLi from %s", AppDB.fuserReconciliationRecord);
			ds1111.add("where fusid = '%s'", list1111.get(i).getInt("fid"));
			ds1111.open();
			if(!ds1111.eof()){
				ds1111.edit();
				ds1111.setField("fatJiangLi", list1111.get(i).getString("ATJiangLi") == null ? 0
						: list1111.get(i).getDouble("ATJiangLi"));
				ds1111.post();
			}
		}

		/* 后台添加usdt */
		MyQuery ds12 = new MyQuery(handle);
		ds12.add("SELECT b.fuserid fid,sum(a.fqty) UsdtHTTJ FROM %s b LEFT JOIN %s a ",
				AppDB.fcheckbalance_fusid, AppDB.fvirtualoperationlog);
		ds12.add(
				"ON a.fuserid=b.fuserid AND a.fvirtualcointypeid=6 AND fstatus=2 GROUP BY b.fuserid ORDER BY b.fid ");
		ds12.open();

		List<Record> list1212 = ds12.getRecords();
		for (int i = 0; i < list1212.size(); i++) {
			MyQuery ds1212 = new MyQuery(handle);

			ds1212.add("select fId,fusid,fusdtHTTJ from %s", AppDB.fuserReconciliationRecord);
			ds1212.add("where fusid = '%s'", list1212.get(i).getInt("fid"));
			ds1212.open();
			if(!ds1212.eof()){
				ds1212.edit();
				ds1212.setField("fusdtHTTJ",
						list1212.get(i).getString("UsdtHTTJ") == null ? 0 : list1212.get(i).getDouble("UsdtHTTJ"));
				ds1212.post();
			}
		}

		/* 后台添加at */
		MyQuery ds13 = new MyQuery(handle);
		ds13.add("SELECT b.fuserid fid,sum(a.fqty) AtHTTJ FROM %s b LEFT JOIN %s a ", AppDB.fcheckbalance_fusid,
				AppDB.fvirtualoperationlog);
		ds13.add(
				"ON a.fuserid=b.fuserid AND a.fvirtualcointypeid=7 AND fstatus=2 GROUP BY b.fuserid ORDER BY b.fid ");
		ds13.open();

		List<Record> list1313 = ds13.getRecords();
		for (int i = 0; i < list1313.size(); i++) {
			MyQuery ds1313 = new MyQuery(handle);

			ds1313.add("select fId,fusid,fatHTTJ from %s", AppDB.fuserReconciliationRecord);
			ds1313.add("where fusid = '%s'", list1313.get(i).getInt("fid"));
			ds1313.open();
			if(!ds1313.eof()){
				ds1313.edit();
				ds1313.setField("fatHTTJ",
						list1313.get(i).getString("AtHTTJ") == null ? 0 : list1313.get(i).getDouble("AtHTTJ"));
				ds1313.post();
			}
		}

		/* 后台添加pc */
		MyQuery ds14 = new MyQuery(handle);
		ds14.add("SELECT b.fuserid fid,sum(a.fqty) PcHTTJ FROM %s b LEFT JOIN %s a ", AppDB.fcheckbalance_fusid,
				AppDB.fvirtualoperationlog);
		ds14.add(
				"ON a.fuserid=b.fuserid AND a.fvirtualcointypeid=39 AND fstatus=2 GROUP BY b.fuserid ORDER BY b.fid ");
		ds14.open();

		List<Record> list1414 = ds14.getRecords();
		for (int i = 0; i < list1414.size(); i++) {
			MyQuery ds1414 = new MyQuery(handle);

			ds1414.add("select fId,fusid,fpcHTTJ from %s", AppDB.fuserReconciliationRecord);
			ds1414.add("where fusid = '%s'", list1414.get(i).getInt("fid"));
			ds1414.open();
			if(!ds1414.eof()){
				ds1414.edit();
				ds1414.setField("fpcHTTJ",
						list1414.get(i).getString("PcHTTJ") == null ? 0 : list1414.get(i).getDouble("PcHTTJ"));
				ds1414.post();
			}
			
		}

		/* ctc 买 */
		MyQuery ds15 = new MyQuery(handle);
		ds15.add("SELECT b.fuserid fid,sum(fNum) UsdtCTCBuy FROM %s b LEFT JOIN %s a ON a.fbuyerid=b.fuserid",
				AppDB.fcheckbalance_fusid, AppDB.fctcorder);
		ds15.add(" AND a.fstatus=2 AND a.ftype=0 GROUP BY b.fuserid ORDER BY b.fid ");
		ds15.open();

		List<Record> list1515 = ds15.getRecords();
		for (int i = 0; i < list1515.size(); i++) {
			MyQuery ds1515 = new MyQuery(handle);

			ds1515.add("select fId,fusid,fusdtCTCBuy from %s", AppDB.fuserReconciliationRecord);
			ds1515.add("where fusid = '%s'", list1515.get(i).getInt("fid"));
			ds1515.open();
			if(!ds1515.eof()){
				ds1515.edit();
				ds1515.setField("fusdtCTCBuy", list1515.get(i).getString("UsdtCTCBuy") == null ? 0
						: list1515.get(i).getDouble("UsdtCTCBuy"));
				ds1515.post();
			}
		}

		/* ctc 卖 */
		MyQuery ds16 = new MyQuery(handle);
		ds16.add("SELECT b.fuserid fid,sum(fNum) UsdtCTCSell FROM %s b LEFT JOIN %s a ON a.fSellerId=b.fuserid",
				AppDB.fcheckbalance_fusid, AppDB.fctcorder);
		ds16.add(" AND a.fstatus=2 AND a.ftype=1 GROUP BY b.fuserid ORDER BY b.fid ");
		ds16.open();

		List<Record> list1616 = ds16.getRecords();
		for (int i = 0; i < list1616.size(); i++) {
			MyQuery ds1616 = new MyQuery(handle);

			ds1616.add("select fId,fusid,fusdtCTCSell from %s", AppDB.fuserReconciliationRecord);
			ds1616.add("where fusid = '%s'", list1616.get(i).getInt("fid"));
			ds1616.open();
			if(!ds1616.eof()){
				ds1616.edit();
				ds1616.setField("fusdtCTCSell", list1616.get(i).getString("UsdtCTCSell") == null ? 0
						: list1616.get(i).getDouble("UsdtCTCSell"));
				ds1616.post();
			}
		}

		/* 划转 At */
		MyQuery ds17 = new MyQuery(handle); 
		ds17.add("SELECT b.fuserid fid,sum(famount) Atftransfer FROM %s b LEFT JOIN %s a ON a.fus_fid2=b.fuserid",
				AppDB.fcheckbalance_fusid, AppDB.ftransfer);
		ds17.add(" AND a.fstatus=1 GROUP BY b.fuserid ORDER BY b.fid ");
		ds17.open();

		List<Record> list1717 = ds17.getRecords();
		for (int i = 0; i < list1717.size(); i++) {
			MyQuery ds1717 = new MyQuery(handle);

			ds1717.add("select fId,fusid,fAtftransfer from %s", AppDB.fuserReconciliationRecord);
			ds1717.add("where fusid = '%s'", list1717.get(i).getInt("fid"));
			ds1717.open();
			if(!ds1717.eof()){
				ds1717.edit();
				ds1717.setField("fAtftransfer", list1717.get(i).getString("Atftransfer") == null ? 0
						: list1717.get(i).getDouble("Atftransfer"));
				ds1717.post();
			}
		}
		
		
		/* OTC Usdt 买入*/
		MyQuery ds18 = new MyQuery(handle); 
		ds18.add("SELECT d.fuserid fid,c.sumCount fotcBuyUsdt FROM %s d LEFT JOIN",AppDB.fcheckbalance_fusid);
		ds18.add("(SELECT b.fuserid fuid,SUM(a.fTrade_Count) as sumCount from %s b,%s a ",AppDB.fcheckbalance_fusid, AppDB.t_userotcorder);
		ds18.add("WHERE (a.fusr_id = b.fuserid AND a.ftype = 1  AND a.ftrade_Status in(3,6)) OR ");
		ds18.add("(a.ftrade_object = b.fuserid AND a.ftype = 2 AND a.ftrade_Status in(3,6)) GROUP BY b.fuserid) c");
		ds18.add("ON c.fuid=d.fuserid  GROUP BY d.fuserid ORDER BY d.fid");
		ds18.open();

		List<Record> list1818 = ds18.getRecords();
		for (int i = 0; i < list1818.size(); i++) {
			MyQuery ds1818 = new MyQuery(handle);

			ds1818.add("select fId,fusid,fotcBuyUsdt from %s", AppDB.fuserReconciliationRecord);
			ds1818.add("where fusid = '%s'", list1818.get(i).getInt("fid"));
			ds1818.open();
			if(!ds1818.eof()){
				ds1818.edit();
				ds1818.setField("fotcBuyUsdt", list1818.get(i).getString("fotcBuyUsdt") == null ? 0
						: list1818.get(i).getDouble("fotcBuyUsdt"));
				ds1818.post();
			}
		}
		
		/* OTC Usdt 卖出*/
		MyQuery ds19 = new MyQuery(handle); 
		ds19.add("SELECT d.fuserid fid,c.sumCount fotcSellUsdt FROM %s d LEFT JOIN",AppDB.fcheckbalance_fusid);
		ds19.add("(SELECT b.fuserid fuid,SUM(a.fTrade_Count) as sumCount from %s b,%s a ",AppDB.fcheckbalance_fusid, AppDB.t_userotcorder);
		ds19.add("WHERE (a.fusr_id = b.fuserid AND a.ftype = 2  AND a.ftrade_Status in(3,6)) OR ");
		ds19.add("(a.ftrade_object = b.fuserid AND a.ftype = 1 AND a.ftrade_Status in(3,6)) GROUP BY b.fuserid) c");
		ds19.add("ON c.fuid=d.fuserid  GROUP BY d.fuserid ORDER BY d.fid");
		ds19.open();

		List<Record> list1919 = ds19.getRecords();
		for (int i = 0; i < list1919.size(); i++) {
			MyQuery ds1919 = new MyQuery(handle);

			ds1919.add("select fId,fusid,fotcSellUsdt from %s", AppDB.fuserReconciliationRecord);
			ds1919.add("where fusid = '%s'", list1919.get(i).getInt("fid"));
			ds1919.open();
			if(!ds1919.eof()){
				ds1919.edit();
				ds1919.setField("fotcSellUsdt", list1919.get(i).getString("fotcSellUsdt") == null ? 0
						: list1919.get(i).getDouble("fotcSellUsdt"));
				ds1919.post();
			}
		}
		
		/* 币币交易61买 */
		MyQuery ds2020 = new MyQuery(handle);
		ds2020.add("SELECT a.fuserid fid,sum(c.fAmount) BuySccSellUsdtCount,sum(c.fCount) BuySccCount FROM ");
		ds2020.add(" %s a LEFT JOIN %s b ON a.fuserid = b.fUs_fid LEFT JOIN %s c ON  b.fid= c.FEn_fId",
				AppDB.fcheckbalance_fusid, AppDB.fentrust, AppDB.fentrustlog);
		ds2020.add(" AND b.ftrademapping=61 AND b.fstatus in (2,3,4) AND b.fEntrustType = 0 AND b.fsuccessAmount >0 GROUP BY a.fuserid ORDER BY a.fid ");
		ds2020.open();

		List<Record> list2020 = ds2020.getRecords();
		for (int i = 0; i < list99.size(); i++) {
			MyQuery ds = new MyQuery(handle);
			ds.add("select fId,fusid,fbuySccSellUsdtCount,fbuySccCount from %s",
					AppDB.fuserReconciliationRecord);
			ds.add("where fusid = '%s'", list2020.get(i).getInt("fid"));
			ds.open();
			if(!ds.eof()){
				ds.edit();
				ds.setField("fbuySccSellUsdtCount", list2020.get(i).getString("BuySccSellUsdtCount") == null ? 0
						: list2020.get(i).getDouble("BuySccSellUsdtCount"));
				ds.setField("fbuySccCount",
						list2020.get(i).getString("BuySccCount") == null ? 0 : list2020.get(i).getDouble("BuySccCount"));
				ds.post();
			}
		}

		/* 币币交易61卖 */
		MyQuery ds2121 = new MyQuery(handle);
		ds2121.add("SELECT a.fuserid fid,sum(c.fAmount) BuyUsdtSellSccCount,sum(c.fCount) SellSccCount FROM ");
		ds2121.add(" %s a LEFT JOIN %s b ON a.fuserid = b.fUs_fid LEFT JOIN %s c ON  b.fid= c.FEn_fId",
				AppDB.fcheckbalance_fusid, AppDB.fentrust, AppDB.fentrustlog);
		ds2121.add(
				" AND b.ftrademapping=61 AND b.fstatus in (2,3,4) AND b.fEntrustType = 1 AND b.fsuccessAmount >0 GROUP BY a.fuserid ORDER BY a.fid ");
		ds2121.open();

		List<Record> list2121 = ds2121.getRecords();
		for (int i = 0; i < ds2121.size(); i++) {
			MyQuery ds = new MyQuery(handle);

			ds.add("select fId,fusid,fbuyUsdtSellSccCount,fsellSccCount from %s",
					AppDB.fuserReconciliationRecord);
			ds.add("where fusid = '%s'", list2121.get(i).getInt("fid"));
			ds.open();
			if(!ds.eof()){
				ds.edit();
				ds.setField("fbuyUsdtSellSccCount", list2121.get(i).getString("BuyUsdtSellSccCount") == null ? 0
						: list2121.get(i).getDouble("BuyUsdtSellSccCount"));
				ds.setField("fsellSccCount", list2121.get(i).getString("SellSccCount") == null ? 0
						: list2121.get(i).getDouble("SellSccCount"));
				ds.post();
			}
		}
		
		/* 钱包SCC */
		MyQuery ds2222 = new MyQuery(handle);
		ds2222.add("SELECT a.fuid fid,a.ftotal Sccftotal,a.ffrozen Sccffrozen,a.ftotal+a.ffrozen SccTotal FROM ");
		ds2222.add(" %s a,%s b WHERE a.fuid=b.fuserid AND a.fvi_fid=40 ORDER BY b.fid", AppDB.fvirtualwallet,
				AppDB.fcheckbalance_fusid);
		ds2222.open();

		List<Record> list2222 = ds2222.getRecords();
		for (int i = 0; i < list2222.size(); i++) {
			MyQuery ds = new MyQuery(handle);
			ds.add("select fId,fusid,fsccftotal ,fsccffrozen ,fsccTotal from %s",
					AppDB.fuserReconciliationRecord);
			ds.add("where fusid = '%s'", list2222.get(i).getInt("fid"));
			ds.open();
			if(!ds.eof()){
				ds.edit();
				ds.setField("fsccftotal", list2222.get(i).getDouble("Sccftotal"));
				ds.setField("fsccffrozen", list2222.get(i).getDouble("Sccffrozen"));
				ds.setField("fsccTotal", list2222.get(i).getDouble("SccTotal"));
				ds.post();
			}
		}
		
		/*计算总额差额*/
		MyQuery dsRecord = new MyQuery(handle);
		dsRecord.add("SELECT * from %s ORDER BY fid ", AppDB.fuserReconciliationRecord);
		dsRecord.open();

		List<Record> listRecord = dsRecord.getRecords();
		for (int i = 0; i < listRecord.size(); i++) {
			MyQuery dsUpdate = new MyQuery(handle);
			dsUpdate.add(
					"select fId,fusdtDifference,fatDifference,fpcDifference,fsccDifference,faccountsUsdtTotal,faccountsAtTotal,faccountsPcTotal,faccountsSccTotal  from %s",
					AppDB.fuserReconciliationRecord);
			dsUpdate.add("where fId = '%s'", listRecord.get(i).getInt("fId"));
			dsUpdate.open();
			if(!dsUpdate.eof()){
				dsUpdate.edit();
				double faccountsUsdtTotal = listRecord.get(i).getDouble("fusdtCZ")
						- listRecord.get(i).getDouble("fbuyATSellUsdtCount")
						+ listRecord.get(i).getDouble("fbuyUsdtSellATCount") + listRecord.get(i).getDouble("fusdtHTTJ")
						+ listRecord.get(i).getDouble("fusdtCTCBuy") - listRecord.get(i).getDouble("fusdtCTCSell")
						- listRecord.get(i).getDouble("fbuyPCSellUsdtCount")
						+ listRecord.get(i).getDouble("fbuyUsdtSellPCCount")
						- listRecord.get(i).getDouble("fbuySccSellUsdtCount")
						+ listRecord.get(i).getDouble("fbuyUsdtSellSccCount")
						+ listRecord.get(i).getDouble("fotcBuyUsdt")
						- listRecord.get(i).getDouble("fotcSellUsdt");
				double faccountsAtTotal = listRecord.get(i).getDouble("fatCZ")
						+ listRecord.get(i).getDouble("fbuyATCount") - listRecord.get(i).getDouble("fsellATCount")
						+ listRecord.get(i).getDouble("fatJiangLi") + listRecord.get(i).getDouble("fatHTTJ")-listRecord.get(i).getDouble("fAtftransfer");
				double faccountsPcTotal = listRecord.get(i).getDouble("fbuyPCCount")
						- listRecord.get(i).getDouble("fsellPCCount") + listRecord.get(i).getDouble("fpcHTTJ");
				double faccountsSccTotal = listRecord.get(i).getDouble("fbuySccCount")- listRecord.get(i).getDouble("fsellSccCount");
				double fusdtDifference = listRecord.get(i).getDouble("fusdtTotal") - faccountsUsdtTotal;
				double fatDifference = listRecord.get(i).getDouble("fatTotal") - faccountsAtTotal;
				double fpcDifference = listRecord.get(i).getDouble("fpcTotal") - faccountsPcTotal;
				double fsccDifference = listRecord.get(i).getDouble("fsccTotal") - faccountsSccTotal;
				dsUpdate.setField("fusdtDifference", fusdtDifference);
				dsUpdate.setField("fatDifference", fatDifference);
				dsUpdate.setField("fpcDifference", fpcDifference);
				dsUpdate.setField("fsccDifference", fsccDifference);
				dsUpdate.setField("faccountsUsdtTotal", faccountsUsdtTotal);
				dsUpdate.setField("faccountsAtTotal", faccountsAtTotal);
				dsUpdate.setField("faccountsPcTotal", faccountsPcTotal);
				dsUpdate.setField("faccountsSccTotal", faccountsSccTotal);
				dsUpdate.post();
			}
		}
	}
	
	@RequestMapping("ssadmin/UserReconciliationRecordExport.html")
    public ModelAndView UserReconciliationRecordExport(HttpServletResponse response) throws Exception {
        JspPage modelAndView = new JspPage(request);
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=UserReconciliationRecordList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;
        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        String keyWord = request.getParameter("keywords").toString();
		if (keyWord != "") 
		{
			List<FuserReconciliationRecordss> alls = getInList(keyWord);
	        for (FuserReconciliationRecordss fuserReconciliationRecord : alls) {
	            e.createRow(rowIndex++);
	            for (ExportFiled filed : ExportFiled.values()) {
	                switch (filed) {
	                case 会员ID:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusid());
	                    break;
	                case USDT差额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusdtDifference());
	                    break;
	                case AT差额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFatDifference());
	                    break;
	                case PC差额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFpcDifference());
	                    break;
	                case SCC差额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFsccDifference());
	                    break;
	                case 当前USDT可用:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusdtftotal());
	                    break;
	                case 当前USDT冻结:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusdtffrozen());
	                    break;
	                case 当前USDT总额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusdtTotal());
	                    break;
	                case 当前AT可用:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFatftotal());
	                    break;
	                case 当前AT冻结:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFatffrozen());
	                    break;
	                case 当前AT总额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFatTotal());
	                    break;
	                case 当前PC可用:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFpcftotal());
	                    break;
	                case 当前PC冻结:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFpcffrozen());
	                    break;
	                case 当前PC总额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFpcTotal());
	                    break;
	                case 当前SCC可用:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFsccftotal());
	                    break;
	                case 当前SCC冻结:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFsccffrozen());
	                    break;
	                case 当前SCC总额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFsccTotal());
	                    break;
	                case 账面USDT总额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFaccountsUsdtTotal());
	                    break;
	                case 账面AT总额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFaccountsAtTotal());
	                    break;
	                case 账面PC总额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFaccountsPcTotal());
	                    break;
	                case 账面SCC总额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFaccountsSccTotal());
	                    break;
	                case USDT充值额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusdtCZ());
	                    break;
	                case AT充值额:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFatCZ());
	                    break;
	                case 币币交易买入AT花费USDT:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuyATSellUsdtCount());
	                    break;
	                case 币币交易成功买入AT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuyATCount());
	                    break;
	                case 币币交易卖出AT得到USDT:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuyUsdtSellATCount());
	                    break;
	                case 币币交易成功卖出AT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFsellATCount());
	                    break;
	                case 币币交易买入PC花费USDT:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuyPCSellUsdtCount());
	                    break;
	                case 币币交易成功买入PC数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuyPCCount());
	                    break;
	                case 币币交易卖出PC得到USDT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuyUsdtSellPCCount());
	                    break;
	                case 币币交易成功卖出PC数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFsellPCCount());
	                    break;
	                case 币币交易买入SCC花费USDT:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuySccSellUsdtCount());
	                    break;
	                case 币币交易成功买入SCC数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuySccCount());
	                    break;
	                case 币币交易卖出SCC得到USDT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFbuyUsdtSellSccCount());
	                    break;
	                case 币币交易成功卖出SCC数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFsellSccCount());
	                    break;
	                case 推广奖励AT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFatJiangLi());
	                    break;
	                case 后台手工添加USDT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusdtHTTJ());
	                    break;
	                case 后台手工添加AT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFatHTTJ());
	                    break;
	                case 后台手工添加PC数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFpcHTTJ());
	                    break;
	                case CTC成功买入USDT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusdtCTCBuy());
	                    break;
	                case CTC成功卖出USDT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFusdtCTCSell());
	                    break;
	                case 划转AT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getfAtftransfer1());
	                    break;
	                case OTC买入USDT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFotcBuyUsdt());
	                    break;
	                case OTC卖出USDT数量:
	                    e.setCell(filed.ordinal(), fuserReconciliationRecord.getFotcSellUsdt());
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
	        return modelAndView;
		}
		else
		{
			JspPage modelAndView1 = new JspPage(request);
			modelAndView1.setViewName("ssadmin/userReconciliationRecordlogList");
			return modelAndView1;
		}
    }
	
	
	
	 public List<FuserReconciliationRecordss> getInList(String keyWord) throws Exception {		
				CommonMethods(keyWord);
				List<FuserReconciliationRecordss> list = this.userReconciliationRecordService.findAll();
	        return list;
	 }
	
	 private static enum ExportFiled {
	        会员ID,USDT差额,AT差额,PC差额,SCC差额,当前USDT可用,当前USDT冻结,当前USDT总额,当前AT可用,当前AT冻结,当前AT总额,当前PC可用,当前PC冻结,当前PC总额,
	        当前SCC可用,当前SCC冻结,当前SCC总额,账面USDT总额,账面AT总额,账面PC总额,账面SCC总额,USDT充值额,AT充值额,币币交易买入AT花费USDT,币币交易成功买入AT数量,
	        币币交易卖出AT得到USDT,币币交易成功卖出AT数量,币币交易买入PC花费USDT,币币交易成功买入PC数量,币币交易卖出PC得到USDT数量,币币交易成功卖出PC数量,
	        币币交易买入SCC花费USDT,币币交易成功买入SCC数量,币币交易卖出SCC得到USDT数量,币币交易成功卖出SCC数量,推广奖励AT数量,后台手工添加USDT数量,
	        后台手工添加AT数量,后台手工添加PC数量,CTC成功买入USDT数量,CTC成功卖出USDT数量,划转AT数量,OTC买入USDT数量,OTC卖出USDT数量
	    }
}