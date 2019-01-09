package com.huagu.vcoin.main.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.cerc.jdb.core.ServerConfig;
import cn.cerc.jdb.oss.OssConnection;
import cn.cerc.jdb.oss.OssSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.AdminStatusEnum;
import com.huagu.vcoin.main.Enum.LogTypeEnum;
import com.huagu.vcoin.main.comm.ParamArray;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.security.SecurityEnvironment;
import com.huagu.vcoin.main.model.Fadmin;
import com.huagu.vcoin.main.model.Frole;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.RoleService;
import com.huagu.vcoin.main.service.admin.UserService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.OSSPostObject;
import com.huagu.vcoin.util.Utils;

import net.sf.json.JSONObject;

@Controller
public class AdminController extends BaseController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;

    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/adminList")
    public ModelAndView Index() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/adminList");
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
            filter.append("and fname like '%" + keyWord + "%' \n");
            modelAndView.addObject("keywords", keyWord);
        }

        if (orderField != null && orderField.trim().length() > 0) {
            filter.append("order by " + orderField + "\n");
        } else {
            filter.append("order by fid \n");
        }

        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filter.append(orderDirection + "\n");
        } else {
            filter.append("asc \n");
        }
        List<Fadmin> list = this.adminService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("adminList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "adminList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fadmin", filter + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goAdminJSP")
    public ModelAndView goAdminJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fadmin admin = this.adminService.findById(fid);
            modelAndView.addObject("fadmin", admin);
        }
        if (request.getSession().getAttribute("login_admin") != null) {
            Fadmin admin = (Fadmin) request.getSession().getAttribute("login_admin");
            modelAndView.addObject("login_admin", admin);
        }

        List<Frole> roleList = this.roleService.findAll();
        Map map = new HashMap();
        for (Frole frole : roleList) {
            map.put(frole.getFid(), frole.getFname());
        }
        modelAndView.addObject("roleMap", map);
        return modelAndView;
    }

    @RequestMapping("ssadmin/saveAdmin")
    public ModelAndView saveAdmin(ParamArray param) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        Fadmin fadmin = param.getFadmin();
        String fname = fadmin.getFname();
        List<Fadmin> all = this.adminService.findByProperty("fname", fname);
        if (all != null && all.size() > 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "管理员登录名已存在！");
            return modelAndView;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        String dateString = sdf.format(new java.util.Date());
        fadmin.setFcreateTime(Timestamp.valueOf(dateString));
        String passWord = fadmin.getFpassword();
        fadmin.setSalt(Utils.getUUID());
        fadmin.setFpassword(Utils.MD5(passWord, fadmin.getSalt()));
        fadmin.setFstatus(AdminStatusEnum.NORMAL_VALUE);
        fadmin.setFuserid(0);

        int roleId = Integer.parseInt(request.getParameter("frole.fid"));
        Frole role = this.roleService.findById(roleId);
        fadmin.setFrole(role);

        this.adminService.saveObj(fadmin);
        Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
        this.adminService.updateAdminlog(sessionAdmin, getIpAddr(request), LogTypeEnum.Admin_ADD);

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "新增成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/forbbinAdmin")
    public ModelAndView forbbinAdmin() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
        if (fid == sessionAdmin.getFid()) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "不允许禁用当前登录的管理员！");
            return modelAndView;
        }

        List<Fadmin> fadmin = this.adminService.findByProperty("fname", "admin");
        if (fid == fadmin.get(0).getFid()) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "不允许禁用admin管理员！");
            return modelAndView;
        }
        Fadmin admin = this.adminService.findById(fid);
        int status = Integer.parseInt(request.getParameter("status"));
        if (status == 1) {
            admin.setFstatus(AdminStatusEnum.FORBBIN_VALUE);
        } else if (status == 2) {
            admin.setFstatus(AdminStatusEnum.NORMAL_VALUE);
        }
        this.adminService.updateObj(admin);
        this.adminService.updateAdminlog(sessionAdmin, getIpAddr(request), LogTypeEnum.Admin_UPDATE);

        modelAndView.addObject("statusCode", 200);
        if (status == 1) {
            modelAndView.addObject("message", "禁用成功");
        } else if (status == 1) {
            modelAndView.addObject("message", "解除禁用成功");
        }
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateAdmin")
    public ModelAndView updateAdmin() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        int fid = Integer.parseInt(request.getParameter("fadmin.fid"));
        Fadmin fadmin = this.adminService.findById(fid);
        Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
        if (fid == sessionAdmin.getFid()) {
            // modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "不允许修改当前登录的管理员密码！");
            return modelAndView;
        }
        if ("admin".equals(fadmin.getFname())) {
            // modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "不允许修改admin管理员密码！");
            return modelAndView;
        }
        String passWord = request.getParameter("fadmin.fpassword");
        fadmin.setFpassword(Utils.MD5(passWord, fadmin.getSalt()));

        this.adminService.updateObj(fadmin);
        this.adminService.updateAdminlog(sessionAdmin, getIpAddr(request), LogTypeEnum.Admin_UPDATE);
        // modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改密码成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateAdminPassword")
    public ModelAndView updateAdminPassword() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fadmin.fid"));
        Fadmin fadmin = this.adminService.findById(fid);
        String truePassword = fadmin.getFpassword();
        String newPassWord = request.getParameter("fadmin.fpassword");
        String oldPassword = request.getParameter("oldPassword");
        String newPasswordMD5 = Utils.MD5(newPassWord, fadmin.getSalt());
        String oldPasswordMD5 = Utils.MD5(oldPassword, fadmin.getSalt());
        if (!truePassword.equals(oldPasswordMD5)) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "原密码输入有误，请重新输入");
            return modelAndView;
        }
        fadmin.setFpassword(newPasswordMD5);
        this.adminService.updateObj(fadmin);
        Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
        this.adminService.updateAdminlog(sessionAdmin, getIpAddr(request), LogTypeEnum.Admin_UPDATE);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改密码成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/loginOut")
    public ModelAndView loginOut() throws Exception {
        JspPage modelAndView = new JspPage(request);
        Subject admin = SecurityUtils.getSubject();
        admin.getSession().removeAttribute("permissions");
        admin.getSession().removeAttribute("fuser");
        admin.logout();
        modelAndView.setViewName("ssadmin/login");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateAdminRole")
    public ModelAndView updateAdminRole() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        int fid = Integer.parseInt(request.getParameter("fadmin.fid"));
        Fadmin fadmin = this.adminService.findById(fid);
        if (fadmin.getFname().equals("admin")) {
            // modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "超级管理员角色不允许修改！");
            return modelAndView;
        }

        int roleId = Integer.parseInt(request.getParameter("frole.fid"));
        Frole role = this.roleService.findById(roleId);
        fadmin.setFrole(role);
        this.adminService.updateObj(fadmin);

        Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
        this.adminService.updateAdminlog(sessionAdmin, getIpAddr(request), LogTypeEnum.Admin_ROLE);

        // modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "修改成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateAdminUser")
    public ModelAndView updateAdminUser() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        try {
            int fid = Integer.parseInt(request.getParameter("fadmin.fid"));
            Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
            if (fid != sessionAdmin.getFid()) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "不允许操作admin账户！");
                return modelAndView;
            }
            Fadmin fadmin = this.adminService.findById(fid);
            if (fadmin.getFuserid() > 0) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "已关联用户ID，不允许重复操作");
                return modelAndView;
            }

            int roleId = Integer.parseInt(request.getParameter("fuserid"));
            Fuser fuser = this.userService.findById(roleId);
            if (fuser == null) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "会员UID不存在");
                return modelAndView;
            }
            if (!fuser.getFgoogleBind()) {
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "该会员未绑定GOOGLE认证");
                return modelAndView;
            }
            fadmin.setFuserid(fuser.getFid());
            this.adminService.updateObj(fadmin);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "会员UID不存在");
            return modelAndView;
        }

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "关联成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/ssadmin/upload1", produces = { "text/html;charset=UTF-8" })
    public String upload(HttpServletRequest request) throws Exception {
        String fileKeyApk = null;
        String plist = "";
        String version = "";
        String appCode = "";
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = mRequest.getFile("file");
        InputStream inputStream = multipartFile.getInputStream();
        String realName = multipartFile.getOriginalFilename();
        String uploadName = appCode;

        ServerConfig config = ServerConfig.getInstance();
        OssConnection ossCon = new OssConnection();
        ossCon.setConfig(config);

        OssSession oss = ossCon.getSession();
        String[] nameSplit = realName.split("\\.");
        String ext = nameSplit[nameSplit.length - 1];
        if (ext != null && !ext.trim().toLowerCase().endsWith("jpg") && !ext.trim().toLowerCase().endsWith("bmp")
                && !ext.trim().toLowerCase().endsWith("png") && !ext.trim().toLowerCase().endsWith("apk")
                && !ext.trim().toLowerCase().endsWith("ipa")) {
            return "";
        }

        String realPath = request.getSession().getServletContext().getRealPath("/") + Constant.uploadPicDirectory;

        String fileName = Utils.getRandomImageName() + "." + ext;
        // String fileName = appCode + "." + ext;
        if (fileName.endsWith(".apk")) {
            fileName = "android";
            version = "1.0";
            oss.upload("vcoin" + "/common/upload/" + realName, inputStream);
            fileKeyApk = config.getProperty("oss.site") + "/" + "vcoin" + "/common/upload/" + realName;
            /*fileKeyApk = String.format(realPath + "/%s-%s.apk", fileName, version);
            uploadFile(inputStream, fileKeyApk);*/
        } else if (fileName.endsWith(".ipa")) {
            fileName = "ios";
            version = "1.0.2";
            oss.upload("vcoin" + "/common/upload/" + realName, inputStream);
            fileKeyApk = config.getProperty("oss.site") + "/" + "vcoin" + "/common/upload/" + realName;
            /*fileKeyApk = String.format(realPath + "/%s-%s.ipa", fileName, version);
            uploadFile(inputStream, fileKeyApk);*/
            String pathApk = fileKeyApk;
            plist = createPlist(appCode, pathApk, version, fileName);
        } else if (fileName.endsWith(".exe")) {
            fileKeyApk = String.format("bbc/client/%s/%s-%s.exe", uploadName, uploadName, version);
            uploadFile(inputStream, realPath);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.accumulate("code", 0);
        resultJson.accumulate("resultUrl", fileKeyApk);
        /*if (Constant.IS_OPEN_OSS.equals("false")) {
            resultJson.accumulate("resultUrl", "/" + Constant.uploadPicDirectory + "/" + fileName);
        } else {
            resultJson.accumulate("resultUrl", OSSPostObject.URL + "/" + Constant.uploadPicDirectory + "/" + fileName);
        }*/
        return resultJson.toString();
    }

    private static void uploadFile(InputStream inputStream, String realPath) {
        // 获取文件
        File file = new File(realPath);
        OutputStream out = null;
        try {
            int len = 0;
            byte[] buf = new byte[1024];
            // 获取输出流
            out = new FileOutputStream(file);
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e2) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/ssadmin/uptversion", produces = { "text/html;charset=UTF-8" })
    public String uptversion(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        String type = request.getParameter("type");
        String version = request.getParameter("version");
        String context = request.getParameter("context");

        // 文件转化
        MultipartFile iosMultipartFileCardFront = mRequest.getFile("appName");
        CommonsMultipartFile iosCfFront = (CommonsMultipartFile) iosMultipartFileCardFront;
        FileItem iosFileItemFront = iosCfFront.getFileItem();

        // 文件上传添加
        String downFilePath = null;
        if(type.equals("1")){
            downFilePath = upLoadOss(iosFileItemFront,"ios/ios.ipa");
        }else{
            downFilePath = upLoadOss(iosFileItemFront,"android/android.apk");
        }


        System.out.println(downFilePath);


        JSONObject json = new JSONObject();

        try (Mysql mysql = new Mysql()) {
            MyQuery ds = new MyQuery(mysql);
            ds.add("SELECT * from fappversion WHERE type = %s", type);
            ds.open();
            if (ds.size() > 0) {
                ds.edit();
            } else {
                ds.append();
                if ("0".equals(type)) {
                    ds.setField("name", "瑞银钱包安卓版");
                } else if ("1".equals(type)) {
                    ds.setField("name", "瑞银钱包IOS版");
                }
                ds.setField("type", type);
            }
            ds.setField("update_readme", context);
            ds.setField("url", downFilePath);
            ds.setField("version", version);
            ds.setField("fid",ds.getField("fid"));
            ds.post();
            json.accumulate("Status", "200");
            json.accumulate("msg", "提交成功！");
        } catch (Exception e) {
            e.printStackTrace();
            json.accumulate("Status", "500");
            json.accumulate("msg", "网络错误");
        }
        return json.toString();
    }

    public String upLoadOss(FileItem fileItem,String fileName) throws IOException {
        ServerConfig config = ServerConfig.getInstance();
        OssConnection ossCon = new OssConnection();
        ossCon.setConfig(config);
        OssSession oss = ossCon.getSession();
        oss.upload(fileName, fileItem.getInputStream());
        return config.getProperty("oss.site")+"/" + fileName;
    }

    private String createPlist(String appCode, String url, String version, String name) {
        // IOSConfig iosConfig = new IOSConfig();
        // log.info("==========开始创建html文件");
        // 这个地址应该是生成的服务器地址，在这里用生成到本地磁盘地址

        /*
         * final String path = urlPath+"/WEB-INF/forms/"; File file = new File(path); if
         * (!file.exists()) { file.mkdirs(); }
         */
        // String installImage = iosConfig.getInstallDisplayImage();
        String installImage = "";
        String installName = "ETH International";
        String bundleIdentifier = "com.ETHbzw.vcoin";
        if (appCode.equals("ufamily-iPhone-merchant")) {
            installName = "μ Merchant";
            bundleIdentifier = "com.FL.miufamily";
        }
        String fileKey = "";
        // String plistFile = name + "-" + version + ".plist";
        String plistFile = "E:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp8\\wtpwebapps\\vcoin-app\\WEB-INF\\classes\\"
                + name + "-" + version + ".plist";
        final String PLIST_PATH = plistFile;
        File file = new File(PLIST_PATH);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("文件写入错误:" + plistFile);
            return "false";
        }
        // ipa安装文件必须为https，这里进行处理,只替换第一个
        if (url.startsWith("http://")) {
            url = url.replaceFirst("http://", "https://");
        }
        StringBuffer plist = new StringBuffer();
        plist.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        plist.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" ");
        plist.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
        plist.append("<plist version=\"1.0\">\n");
        plist.append("<dict>\n");
        plist.append("<key>items</key>\n");
        plist.append("<array>\n");
        plist.append("<dict>\n");
        plist.append("<key>assets</key>\n");
        plist.append("<array>\n");

        // 设置下载的ipa文件地址
        plist.append("<dict>\n");
        plist.append("<key>kind</key>\n");
        plist.append("<string>software-package</string>\n");
        plist.append("<key>url</key>\n");
        plist.append(String.format("<string>%s</string>\n", url));
        plist.append("</dict>\n");

        // 设置全尺寸图片路径
        plist.append("<dict>\n");
        plist.append("<key>kind</key>\n");
        plist.append("<string>full-size-image</string>\n");
        plist.append("<key>needs-shine</key>\n");
        plist.append("<true/>\n");
        plist.append("<key>url</key>\n");
        plist.append(String.format("<string>%s</string>\n", installImage));
        plist.append("</dict>\n");

        // 设置安装时图片路径
        plist.append("<dict>\n");
        plist.append("<key>kind</key>\n");
        plist.append("<string>display-image</string>\n");
        plist.append("<key>needs-shine</key>\n");
        plist.append("<true/>\n");
        plist.append("<key>url</key>\n");
        plist.append(String.format("<string>%s</string>\n", installImage));
        plist.append("</dict>\n");

        plist.append("</array>\n");
        plist.append("<key>metadata</key>\n");
        plist.append("<dict>\n");
        plist.append("<key>bundle-identifier</key>\n");
        // 这个是开发者账号用户名，也可以为空，为空安装时看不到图标，完成之后可以看到
        plist.append(String.format("<string>%s</string>\n", bundleIdentifier));
        plist.append("<key>bundle-version</key>\n");
        plist.append(String.format("<string>%s</string>\n", version));
        plist.append("<key>kind</key>\n");
        plist.append("<string>software</string>\n");
        plist.append("<key>title</key>\n");
        plist.append(String.format("<string>%s</string>\n", installName));
        plist.append("</dict>\n");
        plist.append("</dict>\n");
        plist.append("</array>\n");
        plist.append("</dict>\n");
        plist.append("</plist>");
        try {
            FileOutputStream output = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
            writer.write(plist.toString());
            writer.close();
            output.close();
            try (InputStream in = new FileInputStream(plistFile)) {
                // OssSession oss = (OssSession) frmProject.getProperty(OssSession.sessionId);
                // String fileKeyApk = String.format("bbc/client/%s/plist/%s", name, plistFile);
                String fileKeyApk = String.format("%s", plistFile);
                // uploadFile(in, fileKeyApk);
                // oss.upload(fileKeyApk, in);
                // fileKey = oss.getSite() + "/" + fileKeyApk;
                // plist文件必须为https，这里进行处理,只替换第一个
                if (fileKey.startsWith("http://")) {
                    fileKey = fileKey.replaceFirst("http://", "https://");
                }
                file.delete();
            }
        } catch (IOException e) {
            // log.info("==========创建html文件异常：" + e.getMessage());
        }
        // log.info("==========成功创建html文件");
        return fileKey;
    }

    @RequestMapping("ssadmin/getAdminInfo")
    public ModelAndView getUserInfo() throws Exception {
        String url = request.getParameter("url");
        String uid = request.getParameter("uid");
        JspPage jspPage = new JspPage(request);
        jspPage.setViewName(url);
        if (uid != null && !"".equals(uid)) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fadmin fadmin = this.adminService.findById(fid);
            jspPage.addObject("fadmin", fadmin);
        }
        return jspPage;
    }

    // 修改手机号
    @RequestMapping("/ssadmin/updatePhone")
    public ModelAndView updatePhone() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        // 环境安全检测
        String salt = Utils.MD5(Constant.AppLevel, "0bca36ef25364cdbaf72133d59c47aad");
        if ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) {
            if (!SecurityEnvironment.check(modelAndView)) {
                return modelAndView;
            }
        }
        int uid = Integer.parseInt(request.getParameter("uid"));
        String fNewPhone = request.getParameter("fNewPhone");

        Fadmin fadmin = this.adminService.findById(uid);
        Fadmin sessionAdmin = (Fadmin) request.getSession().getAttribute("login_admin");
        Fadmin admin = this.adminService.findByProperty("fname", "admin").get(0);
        if (!"admin".equals(sessionAdmin.getFname())) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "当前账户暂无权限修改手机号码！");
            return modelAndView;
        }

        if (fadmin.getFid() == admin.getFid()) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "不允许重置admin账户手机号！");
            return modelAndView;
        }

        fadmin.setFphone(fNewPhone);
        this.adminService.updateObj(fadmin);
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "重置手机号码成功");
        return modelAndView;
    }

}
