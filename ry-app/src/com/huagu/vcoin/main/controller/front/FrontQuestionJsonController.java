package com.huagu.vcoin.main.controller.front;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.main.Enum.QuestionStatusEnum;
import com.huagu.vcoin.main.Enum.QuestionTypeEnum;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.front.data.MarketData;
import com.huagu.vcoin.main.controller.front.data.QuestionData;
import com.huagu.vcoin.main.model.Fquestion;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.front.FrontQuestionService;
import com.huagu.vcoin.main.service.front.FrontUserService;
import com.huagu.vcoin.util.KeyUtil;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.mysql.Transaction;
import net.sf.json.JSONObject;

@Controller
public class FrontQuestionJsonController extends BaseController {

    @Autowired
    private FrontQuestionService frontQuestionService;
    @Autowired
    private FrontUserService frontUserService;

    /*
     * var param={questionType:questionType,desc:desc,name:name,phone:phone};
     */
    @ResponseBody
    @RequestMapping("/question/submitQuestion")
    public String submitQuestion(HttpServletRequest request, @RequestParam(required = true) int questiontype,
            @RequestParam(required = true) String questiondesc) throws Exception {
        JSONObject js = new JSONObject();
        String type = QuestionTypeEnum.getEnumString(questiontype);
        if (type == null || type.trim().length() == 0) {
            js.accumulate("code", -1);
            js.accumulate("msg", "非法提交");
            return js.toString();
        }

        questiondesc = HtmlUtils.htmlEscape(questiondesc);
        if (questiondesc.length() > 500) {
            js.accumulate("code", -1);
            js.accumulate("msg", "提交内容太长");
            return js.toString();
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        if (!fuser.isFisTelephoneBind()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "请先绑定手机");
            return js.toString();
        }

        Fquestion fquestion = new Fquestion();
        fquestion.setFuser(GetCurrentUser(request));
        fquestion.setFcreateTime(Utils.getTimestamp());
        fquestion.setFdesc(questiondesc);
        // fquestion.setFanswer(questiondesc);
        fquestion.setFstatus(QuestionStatusEnum.NOT_SOLVED);
        fquestion.setFtype(questiontype);
        fquestion.setIsalluser(1);
        this.frontQuestionService.save(fquestion);
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            Fuser user = GetCurrentUser(request);
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fId,fqid,fuid from %s", AppDB.fquestion);
            ds.add(" where fuid = '%s' order by fcreateTime desc", user.getFid());
            ds.setMaximum(1);
            ds.open();
            ds.edit();
            ds.setField("fqid", ds.getInt("fId"));
            ds.post();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        js.accumulate("code", 0);
        js.accumulate("msg", "问题提交成功，请耐心等待管理员回复");
        return js.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/user/api/question", "/m/api/question" }, produces = { JsonEncode })
    public String question(HttpServletRequest request, @RequestParam(required = true) int questiontype,
            @RequestParam(required = true) String questiondesc, @RequestParam(required = true) String sign)
            throws Exception {
        JSONObject js = new JSONObject();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("questiontype", questiontype);
        map.put("questiondesc", questiondesc);
        String sort = KeyUtil.sort(map);
        if (!sort.equals(sign)) {
            js.accumulate("code", -1);
            js.accumulate("msg", "接口认证失败！");
            return js.toString();
        }

        String type = QuestionTypeEnum.getEnumString(questiontype);
        if (type == null || type.trim().length() == 0) {
            js.accumulate("code", -1);
            js.accumulate("msg", "非法提交");
            return js.toString();
        }

        questiondesc = HtmlUtils.htmlEscape(questiondesc);
        if (questiondesc.length() > 500) {
            js.accumulate("code", -1);
            js.accumulate("msg", "提交内容太长");
            return js.toString();
        }

        Fuser fuser = this.frontUserService.findById(GetCurrentUser(request).getFid());
        if (!fuser.isFisTelephoneBind()) {
            js.accumulate("code", -1);
            js.accumulate("msg", "请先绑定手机");
            return js.toString();
        }
        Fquestion fquestion = new Fquestion();
        fquestion.setFuser(GetCurrentUser(request));
        fquestion.setFcreateTime(Utils.getTimestamp());
        fquestion.setFdesc(questiondesc);
        fquestion.setFstatus(QuestionStatusEnum.NOT_SOLVED);
        fquestion.setFtype(questiontype);
        fquestion.setIsalluser(1);
        this.frontQuestionService.save(fquestion);
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            Fuser user = GetCurrentUser(request);
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fId,fqid,fuid from %s", AppDB.fquestion);
            ds.add(" where fuid = '%s' order by fcreateTime desc", user.getFid());
            ds.setMaximum(1);
            ds.open();
            ds.edit();
            ds.setField("fqid", ds.getInt("fId"));
            ds.post();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        js.accumulate("code", 0);
        js.accumulate("msg", "问题提交成功，请耐心等待管理员回复");
        return js.toString();
    }

    /*
     * * /question/cancelQuestion.html?qid="+id+"&currentPage type=
     */
    @ResponseBody
    @RequestMapping("/question/delquestion")
    public String delquestion(HttpServletRequest request, @RequestParam(required = true) int fid) throws Exception {
        JSONObject js = new JSONObject();
        try {
            Fquestion fquestion = this.frontQuestionService.findById(fid);
            if (fquestion != null && fquestion.getFuser().getFid() == GetCurrentUser(request).getFid()) {
                try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
                    MyQuery ds = new MyQuery(mysql);
                    ds.add("select fId,fuid,fqid from %s", AppDB.fquestion);
                    ds.add(" where fqid = '%s'", fid);
                    ds.open();
                    while (!ds.eof()) {
                        ds.delete();
                    }
                    tx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                js.accumulate("code", -1);
                js.accumulate("msg", "非法操作");
                return js.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            js.accumulate("code", -1);
            js.accumulate("msg", "网络异常");
            return js.toString();
        }

        js.accumulate("code", 0);
        js.accumulate("msg", "删除成功");
        return js.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/question/allAnwer", produces = { JsonEncode })
    public String allAnwer(HttpServletRequest request, @RequestParam(required = true) int questionid) {
        JSONObject jsonObject = new JSONObject();
        MarketData data = new MarketData();
        try (Mysql mysql = new Mysql(); Transaction tx = new Transaction(mysql)) {
            // 查询当前问题的所有的回复
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fq.*,fu.floginName from %s fq", AppDB.fquestion);
            ds.add(" left join %s fu", AppDB.Fuser);
            ds.add(" on fq.fuid = fu.fid");
            ds.add(" where fqid = '%s'", questionid);
            ds.open();
            while (ds.fetch()) {
                QuestionData item = new QuestionData(ds.getString("fanswer"), ds.getInt("userOfsys"),
                        ds.getInt("isask"), ds.getInt("ftype"), ds.getInt("fqid"), ds.getString("floginName"),
                        ds.getInt("issee"), ds.getString("fdesc"));
                data.getQuests().add(item);
            }
            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select fqid,fId,isallsys from %s", AppDB.fquestion);
            ds2.add(" where fId = '%s'", questionid);
            ds2.open();
            ds2.edit();
            ds2.setField("isallsys", "0");
            ds2.post();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.fromObject(data).toString();

    }

    @ResponseBody
    @RequestMapping(value = "/question/saveAnwer", produces = { JsonEncode })
    public String saveAnwer(HttpServletRequest request, @RequestParam(required = true) int fqid,
            @RequestParam(required = true) String content) {
        JSONObject jsonObject = new JSONObject();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fqid,fanswer,fid,fdesc,userOfsys,fcreateTime,fuid,isask,ftype from %s", AppDB.fquestion);
            ds.add("where fid = '%s'", fqid);
            ds.setMaximum(1);
            ds.open();
            if (ds.eof()) {
                jsonObject.accumulate("code", -1);
                jsonObject.accumulate("msg", "非法操作");
                return jsonObject.toString();
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select fqid,fanswer,fId,fdesc,userOfsys,fcreateTime,fuid,isask,fstatus from %s", AppDB.fquestion);
            ds1.setMaximum(1);
            ds1.open();
            ds1.append();
            ds1.setField("fqid", fqid);
            // ds1.setField("fanswer", content);
            ds1.setField("ftype", ds.getInt("ftype"));
            ds1.setField("fdesc", content);
            ds1.setField("userOfsys", ds.getString("userOfsys"));
            ds1.setField("fstatus", "1");
            ds1.setField("fcreateTime", new Date());
            ds1.setField("fuid", ds.getString("fuid"));
            ds1.setField("isask", (ds.getInt("isask") + 1));
            ds1.setField("version", "1");
            ds1.post();
            MyQuery ds2 = new MyQuery(mysql);
            ds2.add("select fqid,fId,isalluser,fcreateTime from %s", AppDB.fquestion);
            ds2.add(" where fId = '%s'", fqid);
            ds2.open();
            ds2.edit();
            ds2.setField("isalluser", "1");
            ds2.setField("fcreateTime", new Date());
            ds2.post();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.accumulate("code", -1);
            jsonObject.accumulate("msg", "网络异常");
            return jsonObject.toString();
        }
        jsonObject.accumulate("code", 0);
        jsonObject.accumulate("msg", "发表成功");
        return jsonObject.toString();

    }

    @ResponseBody
    @RequestMapping(value = "/question/findAnswer", produces = { JsonEncode })
    public String findAnswer(HttpServletRequest request, @RequestParam(required = true) int questionid) {
        JSONObject jsonObject = new JSONObject();
        MarketData data = new MarketData();
        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("select fq.*,fu.floginName from %s fq", AppDB.fquestion);
            ds.add(" left join %s fu", AppDB.Fuser);
            ds.add(" on fq.fuid = fu.fid");
            ds.add(" where fqid = '%s'", questionid);
            ds.open();
            while (ds.fetch()) {
                QuestionData item = new QuestionData(ds.getString("fanswer"), ds.getInt("userOfsys"),
                        ds.getInt("isask"), ds.getInt("ftype"), ds.getInt("fqid"), ds.getString("floginName"),
                        ds.getInt("issee"), ds.getString("fdesc"));
                data.getQuests().add(item);
            }
            MyQuery ds1 = new MyQuery(mysql);
            ds1.add("select fqid,fId,isallsys from %s", AppDB.fquestion);
            ds1.add(" where fId='%s'", questionid);
            ds1.open();
            if (!ds1.eof()) {
                ds1.edit();
                ds1.setField("isallsys", "0");
                ds1.post();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.fromObject(data).toString();
    }
}
