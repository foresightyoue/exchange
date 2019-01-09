package com.huagu.vcoin.main.controller.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.controller.admin.UserController;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.FuserReceipt;
import com.huagu.vcoin.main.service.front.FrontUserReceiptService;
import com.huagu.vcoin.util.KeyUtil;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.ServerConfig;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.oss.OssConnection;
import cn.cerc.jdb.oss.OssSession;
import net.sf.json.JSONObject;
@Controller
public class FrontUserReceiptController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
	public FrontUserReceiptService frontUserReceiptService;
    
    @Autowired
    private HttpServletRequest request;
    
    /*
     * 所有账号
     */
    @RequestMapping("/otc/userReceiptAll")
    public ModelAndView findAll(){
        System.out.println("==进来查询全部中1==");
    	JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("front/user/userReceiptShow");
        
    	List<FuserReceipt> frs = frontUserReceiptService.findAll();
    	modelAndView.addObject("frs", frs);
        System.out.println("==结束了==");
    	return modelAndView;
    }
	
    /*
     * 当前用户自己的账号
     */
    @RequestMapping("/otc/findAllOnly")
    public ModelAndView findAllOnly(HttpServletRequest request){
    	JspPage modelAndView = new JspPage(request);
		List<Map<String, Object>> List = new ArrayList<>();
        Fuser fuser = GetCurrentUser(request);
        int fuserId = fuser.getFid();
        try (Mysql mysql = new Mysql()){
			MyQuery ds = new MyQuery(mysql);
			ds.add("select * from %s where 1=1" ,"t_userreceipt");
			ds.add(" and fUsr_id = '%s'",fuserId);
			ds.open();
			for (Record record : ds) {
				List.add(record.getItems());
			}
			
		} catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
		modelAndView.addObject("frList", List);
        modelAndView.setViewName("front/user/userReceiptShow");
    	return modelAndView;
    	/*StringBuffer fa = new StringBuffer();
    	fa.append("where 1 = 1");
    	fa.append("and fUsr_id = '" + fuserId + "'");
    	List<FuserReceipt> frs = frontUserReceiptService.list(0, 0, fa + "", false);
        
    	modelAndView.addObject("frs", frs);
        modelAndView.setViewName("front/user/userReceiptShow");
    	return modelAndView;*/
    }
    
    /*
     * 选择页面
     */
    @RequestMapping(value = { "/otc/userReceiptOption", "/m/otc/userReceiptOption" })
    public ModelAndView goOption(HttpServletRequest req){
        JspPage modelAndView = new JspPage(request);
        // modelAndView.setViewName("front/user/userReceiptOption");
        modelAndView.setJspFile("front/user/userReceiptOption");
        String coin_id = request.getParameter("coinType");
        modelAndView.add("coin_id", coin_id);
        Fuser fuser = GetCurrentUser(req);
    	int fuserId = fuser.getFid();
    	System.out.println("fuserId="+fuserId);
    	try (Mysql mysql = new Mysql()){
			MyQuery ds0 = new MyQuery(mysql);
			ds0.add("select * from %s where 1=1" ,"t_userreceipt");
			ds0.add(" and fUsr_id = '%s'",fuserId);
			ds0.add(" and fType = '%s'",0);
			ds0.open();
			if(!ds0.eof()){
				modelAndView.addObject("fr0", ds0.getCurrent());
			}
			MyQuery ds1 = new MyQuery(mysql);
			ds1.add("select * from %s where 1=1" ,"t_userreceipt");
			ds1.add(" and fUsr_id = '%s'",fuserId);
			ds1.add(" and fType = '%s'",1);
			ds1.open();
			if(!ds1.eof()){
				modelAndView.addObject("fr1", ds1.getCurrent());
			}
			MyQuery ds2 = new MyQuery(mysql);
			ds2.add("select * from %s where 1=1" ,"t_userreceipt");
			ds2.add(" and fUsr_id = '%s'",fuserId);
			ds2.add(" and fType = '%s'",2);
			ds2.open();
			if(!ds2.eof()){
				modelAndView.addObject("fr2", ds2.getCurrent());
			}
			MyQuery dsBank= new MyQuery(mysql);
			dsBank.add("select * from %s where 1=1" ,"fbankinfo_withdraw");
			dsBank.add(" and FUs_fId = '%s'",fuserId);
			dsBank.setMaximum(1);
			dsBank.open();
			if(!dsBank.eof()){
				String fBankNumber = dsBank.getCurrent().getString("fBankNumber");
				if(fBankNumber != "" || fBankNumber != null){
					modelAndView.addObject("bankStatus", 0);
				}else{
					modelAndView.addObject("bankStatus", 1);
				}
			}else{
				modelAndView.addObject("bankStatus", 1);
			}
			
		} catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    	return modelAndView;
    }
    
    /*
     * 个人收款账户页面
     */
    @RequestMapping(value = { "/otc/userReceiptgo", "/m/otc/userReceiptgo" })
    public ModelAndView goSave(String fusr_id,String ftype,HttpServletRequest req){
        System.out.println("==进来收款账户页面==");
    	String ying = req.getParameter("ying");
        String coin_id = req.getParameter("coinType");
    	System.out.println("ftype="+ftype);
		Fuser fuser = GetCurrentUser(req);
        JspPage modelAndView = new JspPage(request);
    	int fuserId = fuser.getFid();
    	try (Mysql mysql = new Mysql()){
			MyQuery ds0 = new MyQuery(mysql);
			ds0.add("select * from %s where 1=1" ,"t_userreceipt");
			ds0.add(" and fUsr_id = '%s'",fuserId);
			ds0.add(" and fType = '%s'",ftype);
			ds0.open();
			if(!ds0.eof()){
				modelAndView.addObject("frNew", ds0.getCurrent());
			}
		} catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    	if(ying.equals("0")){
    		modelAndView.addObject("ying",0);
    	}
		modelAndView.addObject("fuserId",fuserId);
		modelAndView.addObject("ftype",ftype);
        modelAndView.addObject("coin_id", coin_id);
        // modelAndView.setViewName("front/user/userReceipt");
        modelAndView.setJspFile("front/user/userReceipt");
        System.out.println("==没有账户走完了==");
		return modelAndView;
//    	}
    }
    
    @RequestMapping("/otc/findOnly")
    public ModelAndView findOnly(String fuserId,String ftype){
        System.out.println("==进来findOnly中了==");
    	
    	System.out.println("fuserId123="+fuserId);
    	System.out.println("ftype123="+ftype);
    	
    	JspPage modelAndView = new JspPage(request);
    	modelAndView.setViewName("front/user/userReceipt");
    	
    	try (Mysql mysql = new Mysql()){
			MyQuery m = new MyQuery(mysql);
			m.add("select * from %s" ,"t_userreceipt");
			m.add(" where fUsr_id = '"+ fuserId.toString()+"'");
			m.add(" and ftype = '" + ftype + "'");
			m.open();
			modelAndView.addObject("fr", m.getCurrent());
			
		} catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
        System.out.println("==走完了findOnly了==");
		modelAndView.addObject("fuserId", fuserId);
    	return modelAndView;
    }
    
    // 添加个人收款账户
    @ResponseBody
    @RequestMapping(value = { "/otc/userReceipt", "/m/otc/userReceipt" }, produces = JsonEncode)
	public String saveOrUpdate(HttpServletRequest request){
        System.out.println("==开始保存==");
	    JSONObject resultJson = new JSONObject();
    	String fname = request.getParameter("fname");
    	String ftype = request.getParameter("ftype");
    	String fuser_id = request.getParameter("fuser_id");
        String faccount = request.getParameter("faccount");
        String pic1Url = request.getParameter("pic1Url");
        String fbankname = request.getParameter("fbankname");
        String fbanknamez = request.getParameter("fbanknamez");
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery ds = new MyQuery(handle);
            ds.add("select * from %s where 1=1","t_userreceipt");
            ds.add("and fUsr_id = '%s'",fuser_id);
            ds.add(" and fType = '%s'",ftype);
            ds.open();
            if(!ds.eof()){
        	    ds.edit();
                ds.setField("fName", fname);
                ds.setField("fCreateTime", TDateTime.Now());
                ds.setField("fUsr_id", fuser_id);
                ds.setField("fAccount", faccount);
                ds.setField("fImgUrl", pic1Url);
                ds.setField("fBankname", fbankname);
                ds.setField("fBanknamez", fbanknamez);
                ds.setField("fType", ftype);
                ds.post();
            }else{
                ds.append();
                ds.setField("fName", fname);
                ds.setField("fCreateTime", TDateTime.Now());
                ds.setField("fUsr_id", fuser_id);
                ds.setField("fAccount", faccount);
                ds.setField("fImgUrl", pic1Url);
                ds.setField("fBankname", fbankname);
                ds.setField("fBanknamez", fbanknamez);
                ds.setField("fType", ftype);
                ds.post();
            }
            tx.commit();
            resultJson.accumulate("code", 0);
            resultJson.accumulate("msg", "添加成功！");
        }catch (Exception e) {
     	   resultJson.accumulate("code", 1);
            resultJson.accumulate("msg", "添加失败！");
        }
        System.out.println("==保存成功结束==");
        return resultJson.toString();
        
	}

    @ResponseBody
    @RequestMapping(value = { "api/otc/userReceipt", "/m/api/otc/userReceipt" }, produces = JsonEncode)
    public String apisaveOrUpdate(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        Fuser fuser = GetCurrentUser(request);
        String fname = request.getParameter("fname");// 收款名
        String ftype = request.getParameter("ftype");// 收款方式
        int fuser_id = fuser.getFid();
        String faccount = request.getParameter("faccount");// 收款账号
        String piclUrl = request.getParameter("piclUrl");// 二维码图片
        String sign = request.getParameter("sign");// 接口签名
        // 判断接口签名是否匹配
        Map<String, Object> map = new HashMap<>();
        map.put("fname", fname);
        map.put("ftype", ftype);
        map.put("faccount", faccount);
        map.put("piclUrl", piclUrl);
        String sort = KeyUtil.sort(map);
        if (!sort.equals(sign)) {
            resultJson.accumulate("code", -1);
            resultJson.accumulate("msg", "接口认证失败！");
            return resultJson.toString();
        }
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            MyQuery ds = new MyQuery(handle);
            ds.add("select * from %s where 1=1", "t_userreceipt");
            ds.add("and fUsr_id = '%s'", fuser_id);
            ds.add(" and fType = '%s'", ftype);
            ds.open();
            if (!ds.eof()) {
                ds.edit();
                ds.setField("fName", fname);
                ds.setField("fCreateTime", TDateTime.Now());
                ds.setField("fUsr_id", fuser_id);
                ds.setField("fAccount", faccount);
                ds.setField("fImgUrl", piclUrl);
                ds.setField("fType", ftype);
                ds.post();
            } else {
                ds.append();
                ds.setField("fName", fname);
                ds.setField("fCreateTime", TDateTime.Now());
                ds.setField("fUsr_id", fuser_id);
                ds.setField("fAccount", faccount);
                ds.setField("fImgUrl", piclUrl);
                ds.setField("fType", ftype);
                ds.post();
            }
            tx.commit();
            resultJson.accumulate("code", 0);
            resultJson.accumulate("msg", "添加成功！");
        } catch (Exception e) {
            resultJson.accumulate("code", -1);
            resultJson.accumulate("msg", "添加失败！");
        }
        return resultJson.toString();

    }
	
    // 上传图片
    @ResponseBody
    @RequestMapping(value = "/otc/upload", produces = { "text/html;charset=UTF-8" })
    public String upload(HttpServletRequest request) throws Exception {

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = mRequest.getFile("file");
        CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
        FileItem fileItem = cf.getFileItem();

        JSONObject resultJson = new JSONObject();

        String address = "";
        // 处理文件上传
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置最大缓存
        factory.setSizeThreshold(3 * 1024);
        ServerConfig config = ServerConfig.getInstance();

        OssConnection ossCon = new OssConnection();
        ossCon.setConfig(config);

        OssSession oss = ossCon.getSession();
        String fileUName = null;
        if (!fileItem.isFormField()) {// 文件名
            if (fileItem.getSize() > 0) {
                String fileName = fileItem.getName().toLowerCase();
                fileUName = TDateTime.FormatDateTime("yyMMddHH", new Date()) + UUID.randomUUID();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp")) { // 图片上传
                    if (fileName.endsWith(".jpg"))
                        fileUName += ".jpg";
                    if (fileName.endsWith(".png"))
                        fileUName += ".png";
                    if (fileName.endsWith(".bmp"))
                        fileUName += ".bmp";
                    oss.upload("test-jx-20180928/" + fileUName, fileItem.getInputStream());
                    address = config.getProperty("oss.site") + "/test-jx-20180928/" + fileUName;
                }
            }
        }

        resultJson.accumulate("code", 0);
        resultJson.accumulate("resultUrl", address);
        return resultJson.toString();
    }
    
    // 上传图片
    @ResponseBody
    @RequestMapping(value = { "user/api/upload", "m/user/api/upload" }, produces = JsonEncode)
    public String uploadFile(HttpServletRequest request) throws Exception {

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = mRequest.getFile("file");
        CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
        FileItem fileItem = cf.getFileItem();

        JSONObject resultJson = new JSONObject();

        String address = "";
        // 处理文件上传
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置最大缓存
        factory.setSizeThreshold(3 * 1024);
        ServerConfig config = ServerConfig.getInstance();

        OssConnection ossCon = new OssConnection();
        ossCon.setConfig(config);

        OssSession oss = ossCon.getSession();
        String fileUName = null;
        if (!fileItem.isFormField()) {// 文件名
            if (fileItem.getSize() > 0) {
                String fileName = fileItem.getName().toLowerCase();
                fileUName = TDateTime.FormatDateTime("yyMMddHH", new Date()) + UUID.randomUUID();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp")) { // 图片上传
                    if (fileName.endsWith(".jpg"))
                        fileUName += ".jpg";
                    if (fileName.endsWith(".png"))
                        fileUName += ".png";
                    if (fileName.endsWith(".bmp"))
                        fileUName += ".bmp";
                    oss.upload("test-jx-20180928/" + fileUName, fileItem.getInputStream());
                    address = config.getProperty("oss.site") + "/test-jx-20180928/" + fileUName;
                }
            }
        }

        resultJson.accumulate("code", 0);
        resultJson.accumulate("resultUrl", address);
        return resultJson.toString();
    }

}