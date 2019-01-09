package com.huagu.vcoin.main.controller.admin;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.comm.ParamArray;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Farticle;
import com.huagu.vcoin.main.model.Farticletype;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.ArticleService;
import com.huagu.vcoin.main.service.admin.ArticleTypeService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSender;
import com.huagu.vcoin.main.service.front.FrontOthersService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.OSSPostObject;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.ServerConfig;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.oss.OssConnection;
import cn.cerc.jdb.oss.OssSession;
import net.sf.json.JSONObject;

@Controller
public class ArticleController extends BaseController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FrontOthersService frontOtherService;
    @Autowired
    private ConstantMap map;
    @Autowired
    private MessageSender messageSender;
    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/articleList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/articleList");
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
            filter.append("and (fTitle like '%" + keyWord + "%' OR \n");
            filter.append("fkeyword like '%" + keyWord + "%' ) \n");
            modelAndView.addObject("keywords", keyWord);
        }
        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filter.append("and farticletype.fid=" + request.getParameter("ftype") + "\n");
            }
            modelAndView.addObject("ftype", request.getParameter("ftype"));
        }

        if (orderField != null && orderField.trim().length() > 0) {
            filter.append("order by " + orderField + "\n");
        } else {
            filter.append("order by fcreateDate \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filter.append(orderDirection + "\n");
        } else {
            filter.append("desc \n");
        }

        Map typeMap = new HashMap();
        typeMap.put(0, "全部");
        List<Farticletype> all = this.articleTypeService.findAll();
        for (Farticletype farticletype : all) {
            typeMap.put(farticletype.getFid(), farticletype.getFname());
        }
        modelAndView.addObject("typeMap", typeMap);

        List<Farticle> list = this.articleService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("articleList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "articleList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Farticle", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goArticleJSP")
    public ModelAndView goArticleJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Farticle article = this.articleService.findById(fid);
            modelAndView.addObject("farticle", article);
        }
        return modelAndView;
    }

    @RequestMapping(value = "ssadmin/upload")
    @ResponseBody
    public String upload(ParamArray param) throws Exception {
        MultipartFile multipartFile = param.getFiledata();
        CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
        FileItem fileItem = cf.getFileItem();

        String address = "";
        boolean flag = false;
        // 处理文件上传
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置最大缓存
        factory.setSizeThreshold(5 * 1024);
        ServerConfig config = ServerConfig.getInstance();

        OssConnection ossCon = new OssConnection();
        ossCon.setConfig(config);

        OssSession oss = ossCon.getSession();
        String fileUName = null;
        if (!fileItem.isFormField()) {// 文件名
            if (fileItem.getSize() > 0) {
                String fileName = fileItem.getName().toLowerCase();
                fileUName = TDateTime.FormatDateTime("yyMMddHH", new Date()) + UUID.randomUUID();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif")
                        || fileName.endsWith(".jpeg") || fileName.endsWith(".bmp")) { // 图片上传
                    if (fileName.endsWith(".jpg"))
                        fileUName += ".jpg";
                    if (fileName.endsWith("png"))
                        fileUName += ".png";
                    if (fileName.endsWith(".gif"))
                        fileUName += ".gif";
                    if (fileName.endsWith(".jpeg"))
                        fileUName += ".jpeg";
                    oss.upload("vcoin" + "/ssadmin/upload/" + fileUName, fileItem.getInputStream());
                    address = config.getProperty("oss.site") + "/" + "vcoin" + "/ssadmin/upload/" + fileUName;
                    flag = true;
                }
            }
        }

        String result = "";
        if (!flag) {
            result = "上传失败";
        }
        JSONObject resultJson = new JSONObject();
        resultJson.accumulate("err", result);
        resultJson.accumulate("msg", address);

        return resultJson.toString();
    }

    @RequestMapping("ssadmin/saveArticle")
    public ModelAndView saveArticle(@RequestParam(required = false) MultipartFile filedata,
            @RequestParam(required = false) String ftitle, @RequestParam(required = false) String ftitleEn,
            @RequestParam(required = false) String fKeyword, @RequestParam("articleLookup.id") int articleLookupid,
            @RequestParam(required = false) String fcontent, @RequestParam(required = false) String fcontentEn,
            @RequestParam(required = false) String fisout, @RequestParam(required = false) String foutUrl)
            throws Exception {
        Farticle article = new Farticle();
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        Farticletype articletype = this.articleTypeService.findById(articleLookupid);
        article.setFarticletype(articletype);
        article.setFtitle(ftitle);
        article.setFtitleEn(ftitleEn);
        article.setFkeyword(fKeyword);
        article.setFoutUrl(foutUrl);
        if (fisout == null || fisout.trim().length() == 0) {
            article.setFisout(false);
        } else {
            article.setFisout(true);
        }
        article.setFcontent(fcontent);
        article.setFcontentEn(fcontentEn);
        article.setFlastModifyDate(Utils.getTimestamp());
        article.setFcreateDate(Utils.getTimestamp());
        Fadmin admin = (Fadmin) request.getSession().getAttribute("login_admin");
        article.setFadminByFcreateAdmin(admin);
        article.setFadminByFmodifyAdmin(admin);
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
            article.setFurl(fpictureUrl);
        }
        this.articleService.saveObj(article);

        this.messageSender.publish(ChannelConstant.constantmap, "news");

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "保存成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteArticle")
    public ModelAndView deleteArticle() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        this.articleService.deleteObj(fid);

        this.messageSender.publish(ChannelConstant.constantmap, "news");

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "删除成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/dingArticle")
    public ModelAndView dingArticle() throws Exception {
        int fid = Integer.parseInt(request.getParameter("uid"));
        Farticle a = this.articleService.findById(fid);
        if (a.isFisding()) {
            a.setFisding(false);
        } else {
            a.setFisding(true);
        }

        this.articleService.updateObj(a);

        this.messageSender.publish(ChannelConstant.constantmap, "news");

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "操作成功");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateArticle")
    public ModelAndView updateArticle(@RequestParam(required = false) MultipartFile filedata,
            @RequestParam(required = false) String ftitle, @RequestParam(required = false) String ftitleEn,
            @RequestParam(required = false) String fKeyword, @RequestParam("articleLookup.id") int articleLookupid,
            @RequestParam(required = false) int fid, @RequestParam(required = false) String fcontent,
            @RequestParam(required = false) String fcontentEn, @RequestParam(required = false) String fisout,
            @RequestParam(required = false) String foutUrl) throws Exception {
        Farticle article = this.articleService.findById(fid);
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        Farticletype articletype = this.articleTypeService.findById(articleLookupid);
        article.setFarticletype(articletype);
        article.setFtitle(ftitle);
        article.setFtitleEn(ftitleEn);
        article.setFoutUrl(foutUrl);
        if (fisout == null || fisout.trim().length() == 0) {
            article.setFisout(false);
        } else {
            article.setFisout(true);
        }
        article.setFkeyword(fKeyword);
        article.setFcontent(fcontent);
        article.setFcontentEn(fcontentEn);
        article.setFlastModifyDate(Utils.getTimestamp());
        article.setFcreateDate(Utils.getTimestamp());
        Fadmin admin = (Fadmin) request.getSession().getAttribute("login_admin");
        article.setFadminByFcreateAdmin(admin);
        article.setFadminByFmodifyAdmin(admin);
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
            article.setFurl(fpictureUrl);
        }
        this.articleService.updateObj(article);

        this.messageSender.publish(ChannelConstant.constantmap, "news");

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }
}
