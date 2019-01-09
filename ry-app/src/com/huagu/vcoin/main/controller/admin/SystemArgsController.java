package com.huagu.vcoin.main.controller.admin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fsystemargs;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.SystemArgsService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSender;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.OSSPostObject;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.Record;

@Controller
public class SystemArgsController extends BaseController {
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MessageSender messageSender;
    // 每页显示多少条数据
    private int numPerPage = 50;

    @RequestMapping("/ssadmin/systemArgsList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/systemArgsList");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
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
            filter.append("and fkey like '%" + keyWord + "%' \n");
            modelAndView.addObject("keywords", keyWord);
        }
        // if(orderField != null && orderField.trim().length() >0){
        // filter.append("order by "+orderField+"\n");
        // }else{
        // filter.append("order by fid \n");
        // }
        //
        // if(orderDirection != null && orderDirection.trim().length() >0){
        // filter.append(orderDirection+"\n");
        // }else{
        // filter.append("desc \n");
        // }
        List<Fsystemargs> list = this.systemArgsService.list((currentPage - 1) * numPerPage, numPerPage, filter + "",
                true);
        modelAndView.addObject("systemArgsList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "systemArgsList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fsystemargs", filter + ""));
        return modelAndView;
    }

    @RequestMapping("/ssadmin/candlestickChart")
    public ModelAndView candlestickChart() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/candlestickChart");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 ");
        if (keyWord != null && keyWord.trim().length() > 0) {
            try {
                filterSQL.append(String.format(" and fid = '%s'", keyWord));
            } catch (Exception e) {
                e.printStackTrace();
            }
            modelAndView.addObject("keywords", keyWord);
        }

        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select * from %s ", AppDB.Ftrademapping);
        ds.add(filterSQL.toString());
        ds.setOffset((currentPage - 1) * numPerPage);
        ds.setMaximum(numPerPage);
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }

        modelAndView.addObject("lists", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalOutList");
        // 总数量
        modelAndView.addObject("totalCount", ds.size());
        return modelAndView;
    }

    @RequestMapping("ssadmin/setCandlestickChart")
    public ModelAndView setCandlestickChart(@RequestParam(required = true) int fid,
            @RequestParam(required = true) int state) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        try {
            Mysql mysql = new Mysql();
            MyQuery ds = new MyQuery(mysql);
            ds.add("select * from %s", AppDB.Ftrademapping);
            ds.add(" where fid = %d ", fid);
            ds.setMaximum(1);
            ds.getDefaultOperator().setPrimaryKey("fid");
            ds.open();
            if (!ds.eof()) {
                ds.edit();
                ds.setField("state", state);
                ds.post();
            } else {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "数据源切换失败，请刷新页面再试！");
                return modelAndView;
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", e.getMessage());
            return modelAndView;
        }

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "更新成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/goSystemArgsJSP")
    public ModelAndView goSystemArgsJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fsystemargs systemargs = this.systemArgsService.findById(fid);
            modelAndView.addObject("systemargs", systemargs);
        }
        if (request.getParameter("fileType") != null) {
            modelAndView.addObject("fileType", request.getParameter("fileType"));
        }
        if (request.getParameter("maxSize") != null) {
            modelAndView.addObject("maxSize", request.getParameter("maxSize"));
        }
        return modelAndView;
    }

    @RequestMapping("ssadmin/saveSystemArgs")
    public ModelAndView saveSystemArgs(@RequestParam(required = false) MultipartFile filedata,
            @RequestParam(required = true) String key, @RequestParam(required = false) String value,
            @RequestParam(required = false) String desc) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        Fsystemargs systemArgs = new Fsystemargs();
        systemArgs.setFkey(key);
        systemArgs.setFvalue(value);
        systemArgs.setFdescription(desc);
        String fpictureUrl = "";
        boolean isTrue = false;
        if (filedata != null && !filedata.isEmpty()) {
            InputStream inputStream = filedata.getInputStream();
            String fileRealName = filedata.getOriginalFilename();
            if (fileRealName != null && fileRealName.trim().length() > 0) {
                String[] nameSplit = fileRealName.split("\\.");
                String ext = nameSplit[nameSplit.length - 1];
                if (ext != null && !ext.trim().toLowerCase().endsWith("jpg")
                        && !ext.trim().toLowerCase().endsWith("png")) {
                    modelAndView.addObject("statusCode", 300);
                    modelAndView.addObject("message", "非jpg、png文件格式");
                    return modelAndView;
                }
                String realPath = request.getSession().getServletContext().getRealPath("/")
                        + Constant.uploadPicDirectory;
                String fileName = Utils.getRandomImageName() + "." + ext;
                boolean flag = Utils.saveFile(realPath, fileName, inputStream, Constant.uploadPicDirectory);
                if (flag) {
                    if (Constant.IS_OPEN_OSS.equals("false")) {
                        fpictureUrl = "/" + Constant.uploadPicDirectory + "/" + fileName;
                    } else {
                        fpictureUrl = OSSPostObject.URL + "/" + Constant.uploadPicDirectory + "/" + fileName;
                    }
                    isTrue = true;
                }
            }
        }
        if (isTrue) {
            systemArgs.setFvalue(fpictureUrl);
        }
        if (!isTrue && (value == null || value.trim().length() == 0)) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "参数值或图片链接不能全为空");
            return modelAndView;
        }

        this.systemArgsService.saveObj(systemArgs);

        this.messageSender.publish(ChannelConstant.constantmap, "SystemArgs");

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "新增成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateSystemArgs")
    public ModelAndView updateSystemArgs(@RequestParam(required = false) MultipartFile filedata,
            @RequestParam(required = true) String key, @RequestParam(required = true) String value,
            @RequestParam(required = true) int fid, @RequestParam(required = false) String desc) throws Exception {
        JspPage modelAndView = new JspPage(request);
        Fsystemargs systemArgs = this.systemArgsService.findById(fid);
        // systemArgs.setFkey(key);
        systemArgs.setFvalue(value);
        systemArgs.setFdescription(desc);
        String fpictureUrl = "";
        boolean isTrue = false;
        if (filedata != null && !filedata.isEmpty()) {
            InputStream inputStream = filedata.getInputStream();
            String fileRealName = filedata.getOriginalFilename();
            if (fileRealName != null && fileRealName.trim().length() > 0) {
                String[] nameSplit = fileRealName.split("\\.");
                String ext = nameSplit[nameSplit.length - 1];
                if (ext != null && !ext.trim().toLowerCase().endsWith("jpg")
                        && !ext.trim().toLowerCase().endsWith("png")) {
                    modelAndView.addObject("statusCode", 300);
                    modelAndView.addObject("message", "非jpg、png文件格式");
                    return modelAndView;
                }
                String realPath = request.getSession().getServletContext().getRealPath("/")
                        + Constant.uploadPicDirectory;
                String fileName = Utils.getRandomImageName() + "." + ext;
                boolean flag = Utils.saveFile(realPath, fileName, inputStream, Constant.uploadPicDirectory);
                if (flag) {
                    if (Constant.IS_OPEN_OSS.equals("false")) {
                        fpictureUrl = "/" + Constant.uploadPicDirectory + "/" + fileName;
                    } else {
                        fpictureUrl = OSSPostObject.URL + "/" + Constant.uploadPicDirectory + "/" + fileName;
                    }
                    isTrue = true;
                }
            }
        }
        if (isTrue) {
            systemArgs.setFvalue(fpictureUrl);
        }

        if (!isTrue && (value == null || value.trim().length() == 0)) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "参数值或图片链接不能全为空");
            return modelAndView;
        }

        this.systemArgsService.updateObj(systemArgs);
        this.messageSender.publish(ChannelConstant.constantmap, "SystemArgs");

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "更新成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

}
