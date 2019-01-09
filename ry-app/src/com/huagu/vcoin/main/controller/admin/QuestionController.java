package com.huagu.vcoin.main.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.QuestionStatusEnum;
import com.huagu.vcoin.main.Enum.QuestionTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.front.FrontIndexController;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Fquestion;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.QuestionService;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.Transaction;
import cn.jpush.api.utils.StringUtils;

@Controller
public class QuestionController extends BaseController {
    private static Logger log = Logger.getLogger(FrontIndexController.class);
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private HttpServletRequest request;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/questionList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/questionList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String ftype = request.getParameter("ftype");
        String timeSor = request.getParameter("timeSor");
        String fstatus = request.getParameter("fstatus");
        String fqid = request.getParameter("fqid");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where 1 = 1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filter.append("and fuser.ftelephone like '%" + keyWord + "%' \n");
            modelAndView.addObject("keywords", keyWord);
        }
        if (!"".equals(ftype) && ftype != null) {
            filter.append(String.format(" and ftype=%d \n", Integer.parseInt(ftype)));
        }
        if (!"".equals(fstatus) && fstatus != null) {
            filter.append(String.format(" and fstatus=%d \n", Integer.parseInt(fstatus)));
        }
        if (!"".equals(fqid) && fqid != null) {
            filter.append(String.format(" and fqid=%d \n", Integer.parseInt(fqid)));
        }
        filter.append(" order by fcreateTime \n");
        if (timeSor != null) {
            filter.append(timeSor);
        }
        List<Fquestion> list = this.questionService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("ftype", ftype);
        modelAndView.addObject("timeSor", timeSor);
        modelAndView.addObject("fstatus", fstatus);
        modelAndView.addObject("questionList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "questionList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fquestion", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/questionForAnswerList")
    public ModelAndView questionForAnswerList() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/questionForAnswerList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String ftype = request.getParameter("ftype");
        String timeSor = request.getParameter("timeSor");

        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where fstatus =1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filter.append("and fuser.ftelephone like '%" + keyWord + "%' \n");
            modelAndView.addObject("keywords", keyWord);
        }
        if (!"".equals(ftype) && ftype != null) {
            filter.append(String.format(" and ftype=%d \n", Integer.parseInt(ftype)));
        }

        filter.append(" group by fqid  \n");
        filter.append(" order by fcreateTime \n");
        if (timeSor != null) {
            filter.append(timeSor);
        }

        List<Fquestion> list = this.questionService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("questionList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "questionList");
        // 总数量
        modelAndView.addObject("totalCount", this.questionService.list(0, 0, filter + "", false).size());
        return modelAndView;
    }

    @RequestMapping("ssadmin/goQuestionJSP")
    public ModelAndView goQuestionJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            String fid1 = request.getParameter("uid");
            if (StringUtils.isEmpty(fid1)) {
                log.error("fid is null");
            }
            int fid = Integer.parseInt(fid1);

            Fquestion question = null;
            try {
                question = this.questionService.findById_user(fid);
            } catch (Exception e) {
                ExceptionLog.add("ssadmin/goQuestionJSP", String.format("questionService.findById_user(%s)", fid),
                        String.format("请检查fquestion表字段fqid为:%s的信息", fid), e.getMessage());
            }
            try (Mysql mysql = new Mysql()) {
                MyQuery ds = new MyQuery(mysql);
                ds.add("select fq.fqid,fq.fanswer,fu.fid,fq.fdesc,fq.userOfsys,fq.fcreateTime,fq.fuid,fq.isask,fq.ftype,fu.frealName,fq.issee from %s fq",
                        AppDB.fquestion);
                ds.add("left join %s fu on fq.fuid = fu.fid ", AppDB.Fuser);
                ds.add(" where fqid = '%s' and userOfsys = '0' and issee ='0' ", fid);
                ds.open();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                while (ds.fetch()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("fanswer", ds.getString("fanswer"));
                    map.put("fdesc", ds.getString("fdesc"));
                    map.put("fqid", ds.getString("fqid"));
                    list.add(map);
                }
                MyQuery ds1 = new MyQuery(mysql);
                ds1.add("select fId,fqid,isalluser from %s", AppDB.fquestion);
                ds1.add(" where fId = '%s'", fid);
                ds1.setMaximum(1);
                ds1.open();
                ds1.edit();
                ds1.setField("isalluser", "0");
                ds1.post();
                modelAndView.addObject("list", list);
            } catch (Exception e) {
                e.printStackTrace();
            }
            modelAndView.addObject("fquestion", question);
        }
        Map<Integer, String> map = new HashMap<Integer, String>();
        /*
         * map.put(QuestionTypeEnum.CNY_RECHARGE,
         * QuestionTypeEnum.getEnumString(QuestionTypeEnum.CNY_RECHARGE));
         * map.put(QuestionTypeEnum.CNY_WITHDRAW,
         * QuestionTypeEnum.getEnumString(QuestionTypeEnum.CNY_WITHDRAW));
         */ map.put(QuestionTypeEnum.COIN_RECHARGE, QuestionTypeEnum.getEnumString(QuestionTypeEnum.COIN_RECHARGE));
        map.put(QuestionTypeEnum.COIN_WITHDRAW, QuestionTypeEnum.getEnumString(QuestionTypeEnum.COIN_WITHDRAW));
        map.put(QuestionTypeEnum.OTHERS, QuestionTypeEnum.getEnumString(QuestionTypeEnum.OTHERS));
        modelAndView.addObject("typeMap", map);
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateQuestion")
    public ModelAndView updateQuestion() throws Exception {
        int fid = Integer.parseInt(request.getParameter("fid"));
        String fanswer = request.getParameter("fanswer");
        Fquestion question = this.questionService.findById(fid);
        question.setFanswer(fanswer);
        this.questionService.updateObj(question);

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "更新成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/answerQuestion")
    public ModelAndView answerQuestion() throws Exception {
        Fadmin admin = (Fadmin) request.getSession().getAttribute("login_admin");
        String fname = admin.getFname();
        int fqid = Integer.parseInt(request.getParameter("fid"));
        String fanswer = request.getParameter("fanswer");
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fid,fqid issee,fuid,ftype,fdesc from %s fq", AppDB.fquestion);
            ds.add(" where fqid = '%s' and issee ='0'", fqid);
            ds.open();
            for (Record ds1 : ds) {
                MyQuery ds2 = new MyQuery(mysql);
                ds2.add("select fId,issee,fstatus from %s fq", AppDB.fquestion);
                ds2.add(" where fId = '%s'", ds1.getInt("fid"));
                ds2.open();
                if (!ds2.eof()) {
                    ds2.edit();
                    ds2.setField("issee", "1");
                    ds2.setField("fstatus", "2");
                    ds2.post();
                }
            }

            MyQuery ds3 = new MyQuery(mysql);
            ds3.add("select fId,fanswer,fqid,issee,fuid,ftype,fuid,userOfsys,fsolveTime,fcreateTime,");
            ds3.add("fdesc,fstatus,fstatus,version from %s", AppDB.fquestion);
            ds3.setMaximum(1);
            ds3.open();
            ds3.append();
            ds3.setField("fanswer", fanswer);
            ds3.setField("fqid", fqid);
            ds3.setField("issee", "0");
            ds3.setField("ftype", ds.getInt("ftype"));
            ds3.setField("fuid", ds.getInt("fuid"));
            ds3.setField("userOfsys", "1");
            ds3.setField("fsolveTime", Utils.getTimestamp());
            ds3.setField("fcreateTime", Utils.getTimestamp());
            ds3.setField("fdesc", ds.getString("fdesc"));
            ds3.setField("fstatus", QuestionStatusEnum.SOLVED);
            ds3.setField("fname", fname);
            ds3.setField("version", "1");
            ds3.post();
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select fId,fqid,isallsys from %s", AppDB.fquestion);
            ds1.add(" where fId = '%s'", fqid);
            ds1.setMaximum(1);
            ds1.open();
            if (!ds1.eof()) {
                ds1.edit();
                ds1.setField("isallsys", "1");
                ds1.post();
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "回复成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

}
