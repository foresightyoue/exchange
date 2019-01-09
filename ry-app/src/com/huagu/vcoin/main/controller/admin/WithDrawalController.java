package com.huagu.vcoin.main.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;
import cn.jiguang.common.utils.StringUtils;

@Controller
public class WithDrawalController extends BaseController {

    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/withDrawalList")
    public ModelAndView userWithDrawal() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/withDrawalList");

        // 当前页
        int currentPage = 1;
        int totalCount = 0;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        String userphone = request.getParameter("userPhone");
        String potype_ = request.getParameter("potype_");
        String bztype = request.getParameter("bztype");
        String status = request.getParameter("status");

        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> tradedescList = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select e.FUs_fId,e.fId,e.fCreateTime,t.ftradedesc,e.ftrademapping,e.fEntrustType,e.fPrize,");
            ds.add("e.fAmount,e.fsuccessAmount,e.fCount,e.fleftCount,e.fStatus,e.tradingStatus,");
            ds.add("u.fNickName,u.fRealName,u.fTelephone from %s e", AppDB.fentrust);
            ds.add("LEFT JOIN %s u on e.FUs_fId = u.fId", AppDB.Fuser);
            ds.add("LEFT JOIN %s t ON t.fid = e.ftrademapping", AppDB.Ftrademapping);
            ds.add("where 1 = 1");
            if (StringUtils.isNotEmpty(userphone)) {
                ds.add(" AND u.fTelephone LIKE '%s'", "%" + userphone + "%");
            }
            if (StringUtils.isNotEmpty(potype_)) {
                if ("0".equals(potype_))
                    ds.add(" AND e.fEntrustType = 0");
                if ("1".equals(potype_))
                    ds.add(" AND e.fEntrustType = 1");
            }
            if (StringUtils.isNotEmpty(bztype)) {
                ds.add(" AND t.ftradedesc LIKE '%s'", "%" + bztype + "%");
            }
            if (StringUtils.isNotEmpty(status)) {
                if ("0".equals(status))
                    ds.add(" AND e.fStatus = 1");
                // FIXME 先只做未"成交"的，保留下列"部分成交"的筛选条件代码
                // if ("1".equals(status))
                // ds.add(" AND e.fStatus = 2");
                // if ("2".equals(status))
                // ds.add(" AND e.fStatus = 1 or e.fStatus = 2");
            } else {
                ds.add(" AND e.fStatus = 1 ");
            }
            ds.add(" ORDER BY e.fCreateTime DESC");
            ds.setOffset((currentPage - 1) * numPerPage);
            ds.setMaximum(numPerPage);
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select distinct ftradedesc from %s", AppDB.Ftrademapping);
            ds1.open();
            for (Record record : ds1) {
                tradedescList.add(record.getItems());
            }
        }
        // 查询总条目
        try (Mysql mysql = new Mysql()) {
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select count(1) totalCount from %s e", AppDB.fentrust);
            ds1.add("LEFT JOIN %s u on e.FUs_fId = u.fId", AppDB.Fuser);
            ds1.add("LEFT JOIN %s t ON t.fid = e.ftrademapping", AppDB.Ftrademapping);
            ds1.add("where 1 = 1");
            if (StringUtils.isNotEmpty(userphone)) {
                ds1.add(" AND u.fTelephone LIKE '%s'", "%" + userphone + "%");
            }
            if (StringUtils.isNotEmpty(potype_)) {
                if ("0".equals(potype_))
                    ds1.add(" AND e.fEntrustType = 0");
                if ("1".equals(potype_))
                    ds1.add(" AND e.fEntrustType = 1");
            }
            if (StringUtils.isNotEmpty(bztype)) {
                ds1.add(" AND t.ftradedesc LIKE '%s'", "%" + bztype + "%");
            }
            if (StringUtils.isNotEmpty(status)) {
                if ("0".equals(status))
                    ds1.add(" AND e.fStatus = 1");
                // FIXME 先只做未"成交"的，保留下列"部分成交"的筛选条件代码
                // if ("1".equals(status))
                // ds1.add(" AND e.fStatus = 2");
                // if ("2".equals(status))
                // ds1.add(" AND e.fStatus = 1 or e.fStatus = 2");
            } else {
                ds1.add(" AND e.fStatus = 1 ");
            }

            ds1.setMaximum(1);
            ds1.open();
            totalCount = ds1.getInt("totalCount");
        }
        modelAndView.addObject("ctctradeList", list);
        modelAndView.addObject("tradedescList", tradedescList);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "ctctradeList");
        modelAndView.addObject("currentPage", currentPage);

        modelAndView.addObject("userphone", userphone);
        modelAndView.addObject("potype_", potype_);
        modelAndView.addObject("bztype", bztype);
        modelAndView.addObject("status", status);
        modelAndView.addObject("totalCount", totalCount);
        return modelAndView;
    }

    @RequestMapping("/ssadmin/withDrawalCancelList")
    public ModelAndView userWithDrawalCancel() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/withDrawalCancelList");

        // 当前页
        int currentPage = 1;
        int totalCount = 0;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        String userphone = request.getParameter("userPhone");
        String potype_ = request.getParameter("potype_");
        String bztype = request.getParameter("bztype");

        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> tradedescList = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select e.FUs_fId,e.fCreateTime,t.ftradedesc,e.ftrademapping,e.fEntrustType,e.fPrize,");
            ds.add("e.fAmount,e.fsuccessAmount,e.fCount,e.fleftCount,e.fStatus,e.tradingStatus,");
            ds.add("u.fNickName,u.fTelephone,u.fRealName from %s e", AppDB.fentrust);
            ds.add("LEFT JOIN %s u on e.FUs_fId = u.fId", AppDB.Fuser);
            ds.add("LEFT JOIN %s t ON t.fid = e.ftrademapping", AppDB.Ftrademapping);
            ds.add("where e.fStatus = 4");
            if (StringUtils.isNotEmpty(userphone)) {
                ds.add(" AND u.fTelephone LIKE '%s'", "%" + userphone + "%");
            }
            if (StringUtils.isNotEmpty(potype_)) {
                if ("0".equals(potype_))
                    ds.add(" AND e.fEntrustType = 0");
                if ("1".equals(potype_))
                    ds.add(" AND e.fEntrustType = 1");
            }
            if (StringUtils.isNotEmpty(bztype)) {
                ds.add(" AND t.ftradedesc LIKE '%s'", "%" + bztype + "%");
            }
            ds.add(" ORDER BY e.fCreateTime DESC");
            ds.setOffset((currentPage - 1) * numPerPage);
            ds.setMaximum(numPerPage);
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select distinct ftradedesc from %s", AppDB.Ftrademapping);
            ds1.open();
            for (Record record : ds1) {
                tradedescList.add(record.getItems());
            }
        }
        Mysql mysql = new Mysql();
        MyQuery ds1 = new MyQuery(mysql);
        ds1.add("select count(1) totalCount from %s e", AppDB.fentrust);
        ds1.add("LEFT JOIN %s u on e.FUs_fId = u.fId", AppDB.Fuser);
        ds1.add("LEFT JOIN %s t ON t.fid = e.ftrademapping", AppDB.Ftrademapping);
        ds1.add("where e.fStatus = 4");
        if (StringUtils.isNotEmpty(userphone)) {
            ds1.add(" AND u.fTelephone LIKE '%s'", "%" + userphone + "%");
        }
        if (StringUtils.isNotEmpty(potype_)) {
            if ("0".equals(potype_))
                ds1.add(" AND e.fEntrustType = 0");
            if ("1".equals(potype_))
                ds1.add(" AND e.fEntrustType = 1");
        }
        if (StringUtils.isNotEmpty(bztype)) {
            ds1.add(" AND t.ftradedesc LIKE '%s'", "%" + bztype + "%");
        }
        ds1.setMaximum(1);
        ds1.open();
        totalCount = ds1.getInt("totalCount");
        modelAndView.addObject("ctctradeList", list);
        modelAndView.addObject("tradedescList", tradedescList);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "ctctradeList");
        modelAndView.addObject("currentPage", currentPage);

        modelAndView.addObject("userphone", userphone);
        modelAndView.addObject("potype_", potype_);
        modelAndView.addObject("bztype", bztype);
        modelAndView.addObject("totalCount", totalCount);
        return modelAndView;

    }

    @RequestMapping("ssadmin/withDrawalCancel")
    public ModelAndView sendVirtualOperationLog() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));

        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery queryStatus = new MyQuery(mysql);
            queryStatus.add("select fId,fStatus from %s where 1=1", AppDB.fentrust);
            queryStatus.add("and fId=%s for update", fid);
            queryStatus.setMaximum(-1);
            queryStatus.getDefaultOperator().setUpdateMode(UpdateMode.loose);
            queryStatus.open();
            if (!queryStatus.eof()) {
                String fStatus = queryStatus.getString("fStatus");
                if (fStatus.equals("3")) {
                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                    modelAndView.addObject("statusCode", 300);
                    modelAndView.addObject("message", "此单已完全成交，不能再撤销");
                    return modelAndView;
                } else if (fStatus.equals("1") || fStatus.equals("2")) {
                    queryStatus.edit();
                    queryStatus.setField("fStatus", 4);
                    queryStatus.open();
                    MyQuery ds = new MyQuery(mysql);
                    ds.add("select e.fAmount,e.fCount,e.fId eFid,e.fEntrustType,t.fvirtualcointype1,t.fvirtualcointype2,u.fId uid,u.fTelephone phone from %s e",
                            AppDB.fentrust);
                    ds.add("left join %s u on e.FUs_fId = u.fId", AppDB.Fuser);
                    ds.add("left join %s t on t.fid = e.ftrademapping", AppDB.Ftrademapping);
                    ds.add("where e.fId = %d", fid);
                    ds.setMaximum(1);
                    ds.open();
                    if (!ds.eof()) {
                        double fAmount = ds.getDouble("fAmount");
                        int uid = ds.getInt("uid");
                        String phone = ds.getString("phone");
                        int fvirtualcointype1 = ds.getInt("fvirtualcointype1");
                        int fvirtualcointype2 = ds.getInt("fvirtualcointype2");
                        int fEntrustType = ds.getInt("fEntrustType");
                        double fCount = ds.getDouble("fCount");
                        int eFid = ds.getInt("eFid");
                        MyQuery ds2 = new MyQuery(mysql);
                        ds2.add("select * from %s ", AppDB.fvirtualwallet);
                        ds2.add("where 1=1");
                        if (fEntrustType == 0) {// 买单
                            ds2.add("and fVi_fId = %d ", fvirtualcointype1);
                        } else {
                            ds2.add("and fVi_fId = %d ", fvirtualcointype2);
                        }
                        ds2.add("and fuid = %d", uid);
                        ds2.setMaximum(1);
                        ds2.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                        ds2.open();
                        if (!ds2.eof()) {
                            double fFrozen = ds2.getDouble("fFrozen");
                            double fTotal = ds2.getDouble("fTotal");
                            if (fEntrustType == 0) {
                                if (fFrozen - fAmount < 0) {
                                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                                    modelAndView.addObject("statusCode", 300);
                                    modelAndView.addObject("message", "冻结金额少于撤销金额,撤销失败");
                                    return modelAndView;
                                }
                                ds2.edit();
                                ds2.setField("fFrozen", fFrozen - fAmount);
                                ds2.setField("fTotal", fTotal + fAmount);
                                ds2.post();
                            } else {
                                if (fFrozen - fCount < 0) {
                                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                                    modelAndView.addObject("statusCode", 300);
                                    modelAndView.addObject("message", "冻结金额少于撤销金额,撤销失败");
                                    return modelAndView;
                                }
                                ds2.edit();
                                ds2.setField("fFrozen", fFrozen - fCount);
                                ds2.setField("fTotal", fTotal + fCount);
                                ds2.post();
                            }
                            String updateUser = ((Fadmin) getSession(request).getAttribute("login_admin")).getFname();
                            MyQuery ds3 = new MyQuery(mysql);
                            ds3.add("select * from %s ", "t_withdrawallog");
                            ds3.setMaximum(1);
                            ds3.open();
                            ds3.append();
                            ds3.setField("userId_", uid);
                            ds3.setField("userPhone_", phone);
                            ds3.setField("fen_id_", eFid);
                            ds3.setField("riginalOrdersMoney_", fAmount);
                            ds3.setField("withdrawalMoney_", fAmount);
                            ds3.setField("operator_", updateUser);
                            ds3.setField("time_", TDateTime.Now());
                            ds3.post();

                            MyQuery ds4 = new MyQuery(mysql);
                            ds4.add("select * from %s ", AppDB.fentrust);
                            ds4.add("where fId = %d ", eFid);
                            ds4.setMaximum(1);
                            ds4.open();
                            ds4.edit();
                            ds4.setField("fStatus", 4);
                            ds4.post();
                        }
                    }
                }
            }
            tx.commit();
        } catch (Exception e) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "网络出错,撤销失败");
            return modelAndView;
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "撤销成功");
        return modelAndView;
    }
}
