package com.huagu.vcoin.main.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.service.admin.OtcOrdersService;
import com.huagu.vcoin.util.Utils;
import com.huagu.vcoin.util.XlsExport;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;

@Controller
public class OtcOrdersController extends BaseController {
    @Autowired
    private OtcOrdersService otcOrdersService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/otcOrdersList.html")
    public ModelAndView otcOrdersList() {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/otcOrdersList");
        // 当前页
        int currentPage = 1;
        int totalCount = 0;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        String keywords = request.getParameter("keywords");
        String userlogin = request.getParameter("userlogin");
        String fStatus = request.getParameter("fStatus");
        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select b.*,c.fName from t_otcorders b left join fuser a on b.fusr_id = a.fid left join fvirtualcointype c on c.fId = b.fAm_fId where b.isCheck = 1 ");
        if (StringUtils.isNotEmpty(keywords)) {
            ds.add("and b.fId = '%s' ", keywords);
            modelAndView.addObject("keywords", keywords);
        }
        if (StringUtils.isNotEmpty(userlogin)) {
            ds.add(" and ( b.fUsr_id = '%s'", userlogin);
            ds.add(" or a.floginName = '%s'", userlogin);
            ds.add(" or a.fTelephone = '%s' )", userlogin);
            modelAndView.addObject("userlogin", userlogin);
        }
        if(StringUtils.isNotEmpty(fStatus)){
        	ds.add(" and b.fStatus = '%s' ", fStatus);
        }
        ds.add("order by fId desc");
        ds.setOffset((currentPage - 1) * numPerPage);
        ds.setMaximum(numPerPage);
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        MyQuery cds = new MyQuery(mysql);
        cds.add("select b.*,c.fName from t_otcorders b left join fuser a on b.fusr_id = a.fid left join fvirtualcointype c on c.fId = b.fAm_fId where b.isCheck = 1 ");
        if (StringUtils.isNotEmpty(keywords)) {
            cds.add("and b.fId = '%s' ", keywords);
            modelAndView.addObject("keywords", keywords);
        }
        if (StringUtils.isNotEmpty(userlogin)) {
            cds.add(" and ( b.fUsr_id = '%s'", userlogin);
            cds.add(" or a.floginName = '%s'", userlogin);
            cds.add(" or a.fTelephone = '%s' )", userlogin);
            modelAndView.addObject("userlogin", userlogin);
        }
        if (StringUtils.isNotEmpty(fStatus)) {
            cds.add(" and b.fStatus = '%s' ", fStatus);
        }
        cds.add("order by fId desc");
        cds.open();
        totalCount = cds.size();
        modelAndView.addObject("otcOrdersList", list);
        modelAndView.addObject("fStatus", fStatus);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalCount", totalCount);
        return modelAndView;
    }

    @RequestMapping("/ssadmin/otcOrdersCheckList.html")
    public ModelAndView otcOrdersCheckList() {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/otcOrdersCheckList");
        // 当前页
        int currentPage = 1;
        int totalCount = 0;
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        String keywords = request.getParameter("keywords");
        String userlogin = request.getParameter("userlogin");
        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select b.*,c.fName from t_otcorders b left join fuser a on b.fusr_id = a.fid left join fvirtualcointype c on c.fId = b.fAm_fId where b.fStatus = 0 and b.isCheck = 0 ");
        if (StringUtils.isNotEmpty(keywords)) {
            ds.add("and b.fId = '%s'", keywords);
            modelAndView.addObject("keywords", keywords);
        }
        if (StringUtils.isNotEmpty(userlogin)) {
            ds.add(" and ( b.fUsr_id = '%s'", userlogin);
            ds.add(" or a.floginName = '%s'", userlogin);
            ds.add(" or a.fTelephone = '%s' )", userlogin);
            modelAndView.addObject("userlogin", userlogin);
        }
        ds.add("order by fId desc");
        ds.setOffset((currentPage - 1) * numPerPage);
        ds.setMaximum(numPerPage);
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        MyQuery cds = new MyQuery(mysql);
        cds.add("select b.*,c.fName from t_otcorders b left join fuser a on b.fusr_id = a.fid left join fvirtualcointype c on c.fId = b.fAm_fId where b.fStatus = 0 and b.isCheck = 0 ");
        if (StringUtils.isNotEmpty(keywords)) {
            cds.add("and b.fId = '%s'", keywords);
            modelAndView.addObject("keywords", keywords);
        }
        if (StringUtils.isNotEmpty(userlogin)) {
            cds.add(" and ( b.fUsr_id = '%s'", userlogin);
            cds.add(" or a.floginName = '%s'", userlogin);
            cds.add(" or a.fTelephone = '%s' )", userlogin);
            modelAndView.addObject("userlogin", userlogin);
        }
        cds.add("order by fId desc");
        cds.open();
        totalCount = cds.size();
        modelAndView.addObject("otcOrdersCheckList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalCount", totalCount);
        return modelAndView;
    }

    private static enum ExportFiled {
        订单号, 下单时间, 币种id, 用户账户, 单价, 操作数量, 操作币种产生人民币金额, 订单类型, 交易所在地, 最小限额, 最大限额, 最低价, 收款类型, 广告留言, 是否开启实名认证购买, 是否撤销, 订单状态
    }

    @SuppressWarnings("unused")
    private List<Map<String, Object>> otcList() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from t_otcOrders where isUndo = 1");
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        return list;
    }

    @SuppressWarnings("unused")
    private List<Map<String, Object>> otcCheckList() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from t_otcOrders where isUndo = 0");
            ds.open();
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        return list;
    }

    // 广告信息表execl导出
    @RequestMapping("ssadmin/otctradeExport")
    public ModelAndView otctradeExport(HttpServletResponse response) throws IOException {
        JspPage modelAndView = new JspPage(request);
        String[] status = new String[] { "挂单中", "售卖中", "已售卖" };
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=otcList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> o2cList = otcList();
        for (Map<String, Object> map : o2cList) {
            e.createRow(rowIndex++);
            for (ExportFiled filed : ExportFiled.values()) {
                switch (filed) {
                case 订单号:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fId")));
                    break;
                case 下单时间:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCreateTime")));
                    break;
                case 币种id:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAm_fId")));
                    break;
                case 用户账户:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fUsr_id")));

                    break;
                case 单价:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fUnitprice")));
                    break;
                case 操作数量:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCount")));
                    break;
                case 操作币种产生人民币金额:
                    e.setCell(filed.ordinal(), String.valueOf(((double) map.get("fMoney"))));
                    break;
                case 订单类型:
                    if ("0".equals(String.valueOf(map.get("fOrdertype")))) {
                        e.setCell(filed.ordinal(), "买入");
                    } else {
                        e.setCell(filed.ordinal(), "卖出");
                    }
                    break;
                case 交易所在地:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAdr")));
                    break;
                case 最小限额:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fSmallprice")));

                    break;
                case 最大限额:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fBigprice")));
                    break;
                case 最低价:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fLowprice")));
                    break;
                case 收款类型:
                    if ("0".equals(map.get("fReceipttype"))) {
                        e.setCell(filed.ordinal(), String.valueOf("银行"));
                    } else if ("2".equals(map.get("fReceipttype"))) {
                        e.setCell(filed.ordinal(), String.valueOf("支付宝"));
                    } else if ("1".equals(map.get("fReceipttype"))) {
                        e.setCell(filed.ordinal(), String.valueOf("微信"));
                    }
                    break;
                case 广告留言:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fMsg")));
                    break;
                case 是否开启实名认证购买:
                    if ("0".equals(String.valueOf(map.get("isCertified")))) {
                        e.setCell(filed.ordinal(), "是");
                    } else {
                        e.setCell(filed.ordinal(), "否");
                    }
                    break;
                case 是否撤销:
                    if ("0".equals(String.valueOf(map.get("isUndo")))) {
                        e.setCell(filed.ordinal(), "是");
                    } else {
                        e.setCell(filed.ordinal(), "否");
                    }
                    break;
                case 订单状态:
                    if ("0".equals(map.get("fStatus"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(0))]);
                    } else if ("2".equals(map.get("fStatus"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(1))]);
                    } else if ("1".equals(map.get("fStatus"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(2))]);
                    }
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

    // 审核广告信息表execl导出
    @RequestMapping("ssadmin/otcChecktradeExport")
    public ModelAndView otcChecktradeExport(HttpServletResponse response) throws IOException {
        JspPage modelAndView = new JspPage(request);
        String[] status = new String[] { "挂单中", "售卖中", "已售卖" };
        response.setContentType("Application/excel");
        response.addHeader("Content-Disposition", "attachment;filename=otcCheckList.xls");
        XlsExport e = new XlsExport();
        int rowIndex = 0;

        // header
        e.createRow(rowIndex++);
        for (ExportFiled filed : ExportFiled.values()) {
            e.setCell(filed.ordinal(), filed.toString());
        }
        List<Map<String, Object>> o2cCheckList = otcCheckList();
        for (Map<String, Object> map : o2cCheckList) {
            e.createRow(rowIndex++);
            for (ExportFiled filed : ExportFiled.values()) {
                switch (filed) {
                case 订单号:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fId")));
                    break;
                case 下单时间:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCreateTime")));
                    break;
                case 币种id:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAm_fId")));
                    break;
                case 用户账户:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fUsr_id")));

                    break;
                case 单价:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fUnitprice")));
                    break;
                case 操作数量:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fCount")));
                    break;
                case 操作币种产生人民币金额:
                    e.setCell(filed.ordinal(), String.valueOf(((double) map.get("fMoney"))));
                    break;
                case 订单类型:
                    if ("0".equals(String.valueOf(map.get("fOrdertype")))) {
                        e.setCell(filed.ordinal(), "买入");
                    } else {
                        e.setCell(filed.ordinal(), "卖出");
                    }
                    break;
                case 交易所在地:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fAdr")));
                    break;
                case 最小限额:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fSmallprice")));

                    break;
                case 最大限额:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fBigprice")));
                    break;
                case 最低价:
                    e.setCell(filed.ordinal(), String.valueOf((double) map.get("fLowprice")));
                    break;
                case 收款类型:
                    if ("0".equals(map.get("fReceipttype"))) {
                        e.setCell(filed.ordinal(), String.valueOf("银行"));
                    } else if ("2".equals(map.get("fReceipttype"))) {
                        e.setCell(filed.ordinal(), String.valueOf("支付宝"));
                    } else if ("1".equals(map.get("fReceipttype"))) {
                        e.setCell(filed.ordinal(), String.valueOf("微信"));
                    }
                    break;
                case 广告留言:
                    e.setCell(filed.ordinal(), String.valueOf(map.get("fMsg")));
                    break;
                case 是否开启实名认证购买:
                    if ("0".equals(String.valueOf(map.get("isCertified")))) {
                        e.setCell(filed.ordinal(), "是");
                    } else {
                        e.setCell(filed.ordinal(), "否");
                    }
                    break;
                case 是否撤销:
                    if ("0".equals(String.valueOf(map.get("isUndo")))) {
                        e.setCell(filed.ordinal(), "是");
                    } else {
                        e.setCell(filed.ordinal(), "否");
                    }
                    break;
                case 订单状态:
                    if ("0".equals(map.get("fStatus"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(0))]);
                    } else if ("2".equals(map.get("fStatus"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(1))]);
                    } else if ("1".equals(map.get("fStatus"))) {
                        e.setCell(filed.ordinal(), status[Integer.valueOf(String.valueOf(2))]);
                    }
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

    @RequestMapping("ssadmin/otcOrdersCheck")
    public ModelAndView otcOrdersCheck(@RequestParam(required = true) String uid) throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        if (uid == "" || uid == null) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "请选择信息!");
            return jspPage;
        } else {
            String[] fids = uid.split(",");
            // int fid = Integer.parseInt(request.getParameter("fid"));
            int success = 0;
            int fail = 0;
            try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
                for (int i = 0; i < fids.length; i++) {
                    int fid1 = Integer.parseInt(fids[i]);
                    MyQuery queryStatus = new MyQuery(mysql);
                    List<Map<String, Object>> list = new ArrayList<>();
                    queryStatus.add("select isUndo,fId,isCheck from %s where 1=1", "t_otcOrders");
                    queryStatus.add("and fId=%s for update", fid1);
                    queryStatus.setMaximum(-1);
                    queryStatus.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                    queryStatus.open();
                    queryStatus.edit();
                    queryStatus.setField("isCheck", 1);
                    queryStatus.post();
                    success++;
                }
                tx.commit();
            } catch (Exception e) {

                jspPage.addObject("statusCode", 300);
                jspPage.addObject("message", "审核失败" + e.getMessage());
                return jspPage;
            }
            jspPage.addObject("statusCode", 200);
            jspPage.addObject("message", "审核成功,审核成功数量" + success + "，失败数量" + fail);
            return jspPage;
        }
    }

    @RequestMapping("ssadmin/otcOrdersNoCheck")
    public ModelAndView otcOrdersNoCheck(@RequestParam(required = true) String uid) throws Exception {
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName("ssadmin/comm/ajaxDone");
        if (uid == "" || uid == null) {
            jspPage.addObject("statusCode", 300);
            jspPage.addObject("message", "请选择信息!");
            return jspPage;
        } else {
            String[] fids = uid.split(",");
            // int fid = Integer.parseInt(request.getParameter("fid"));
            int success = 0;
            int fail = 0;
            try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
                for (int i = 0; i < fids.length; i++) {
                    int fid1 = Integer.parseInt(fids[i]);
                    MyQuery queryStatus = new MyQuery(mysql);
                    List<Map<String, Object>> list = new ArrayList<>();
                    queryStatus.add("select isUndo,fId,isCheck from %s where 1=1", "t_otcOrders");
                    queryStatus.add("and fId=%s for update", fid1);
                    queryStatus.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                    queryStatus.setMaximum(-1);
                    queryStatus.open();
                    queryStatus.edit();
                    queryStatus.setField("isCheck", 2);
                    queryStatus.post();
                    success++;
                }
                tx.commit();
            } catch (Exception e) {

                jspPage.addObject("statusCode", 300);
                jspPage.addObject("message", "取消审核失败" + e.getMessage());
                return jspPage;
            }
            jspPage.addObject("statusCode", 200);
            jspPage.addObject("message", "取消审核成功,取消审核成功数量" + success + "，失败数量" + fail);
            return jspPage;
        }
    }

}
