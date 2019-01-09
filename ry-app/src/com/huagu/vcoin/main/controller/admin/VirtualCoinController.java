package com.huagu.vcoin.main.controller.admin;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huagu.vcoin.util.DeCodeUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;
import com.huagu.coa.common.tool.TokenClient;
import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.Enum.CoinTypeEnum;
import com.huagu.vcoin.main.Enum.VirtualCoinTypeStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fpool;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.model.Fwithdrawfees;
import com.huagu.vcoin.main.service.admin.AboutService;
import com.huagu.vcoin.main.service.admin.AdminService;
import com.huagu.vcoin.main.service.admin.PoolService;
import com.huagu.vcoin.main.service.admin.TradeMappingService;
import com.huagu.vcoin.main.service.admin.VirtualCoinService;
import com.huagu.vcoin.main.service.admin.WithdrawFeesService;
import com.huagu.vcoin.main.service.comm.listener.ChannelConstant;
import com.huagu.vcoin.main.service.comm.listener.MessageSender;
import com.huagu.vcoin.main.service.front.FrontVirtualCoinService;
import com.huagu.vcoin.util.Constant;
import com.huagu.vcoin.util.Utils;

import cn.cerc.jdb.core.DataSetState;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.ServerConfig;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.oss.OssConnection;
import cn.cerc.jdb.oss.OssSession;
import net.sf.json.JSONObject;
import site.jayun.vcoin.wallet.BTCUtils;
import site.jayun.vcoin.wallet.ETHUtils;
import site.jayun.vcoin.wallet.OmniUtils;
import site.jayun.vcoin.wallet.WalletConfig;
import site.jayun.vcoin.wallet.WalletFactory;
import site.jayun.vcoin.wallet.WalletUtil;

@Controller
public class VirtualCoinController extends BaseController {
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private WithdrawFeesService withdrawFeesService;
    @Autowired
    private PoolService poolService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private ConstantMap map;
    @Autowired
    private AboutService aboutService;
    @Autowired
    private TradeMappingService tradeMappingService;
    @Autowired
    private MessageSender messageSender;

    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/virtualCoinTypeList")
    public ModelAndView Index() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/virtualCoinTypeList");
        // 环境安全检测
        /*
         * String salt = Utils.MD5(Constant.AppLevel,
         * "0bca36ef25364cdbaf72133d59c47aad"); if
         * ("c1c9402a47b23f8d31728cfcd6ecbb04".equals(salt)) { if
         * (!SecurityEnvironment.check(modelAndView)) { return modelAndView; } }
         */
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
            filter.append("and (fname like '%" + keyWord + "%' OR \n");
            filter.append("fShortName like '%" + keyWord + "%' OR \n");
            filter.append("fdescription like '%" + keyWord + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }

//        filter.append(" and ftype <>" + CoinTypeEnum.FB_CNY_VALUE + " \n");

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
        List<Fvirtualcointype> list = this.virtualCoinService.list((currentPage - 1) * numPerPage, numPerPage,
                filter + "", true);
        modelAndView.addObject("virtualCoinTypeList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "virtualCoinTypeList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount("Fvirtualcointype", filter + ""));
        modelAndView.addObject("walletConfigEnable", Constant.walletConfigEnable);
        return modelAndView;
    }

    @RequestMapping("/ssadmin/walletAddressList")
    public ModelAndView walletAddressList() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/walletAddressList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");

        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filter.append("and b.fname like '%" + keyWord + "%'\n");
            modelAndView.addObject("keywords", keyWord);
        }
        filter.append("and (a.fstatus=0 or a.fstatus is null)\n");

        List list = this.poolService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
        modelAndView.addObject("walletAddressList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("rel", "walletAddressList");
        modelAndView.addObject("currentPage", currentPage);
        // 总数量
        modelAndView.addObject("totalCount",
                this.poolService.list((currentPage - 1) * numPerPage, numPerPage, filter + "", false).size());
        return modelAndView;
    }

    @RequestMapping("/ssadmin/transferCoinList")
    public ModelAndView transferCoinList() throws Exception {

        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/transferCoinList");
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
                filterSQL.append(String.format(" and a.fuserid = '%s'", keyWord));
            } catch (Exception e) {
                e.printStackTrace();
            }
            modelAndView.addObject("keywords", keyWord);
        }

        List<Map<String, Object>> list = new ArrayList<>();
        Mysql mysql = new Mysql();
        MyQuery ds = new MyQuery(mysql);
        ds.add("select a.*,b.fName from %s a left join %s b ", "fvirtualcaptualoperationtoyylog", "fvirtualcointype");
        ds.add("on a.finput_coinFid = b.fId ");
        ds.add(filterSQL.toString());
        ds.add(" order by a.fCreateTime desc ");
        ds.setOffset((currentPage - 1) * numPerPage);
        ds.setMaximum(numPerPage);
        ds.open();
        if (!ds.eof()) {
            for (Record record : ds) {
                list.add(record.getItems());
            }
        }
        MyQuery ds1 = new MyQuery(mysql);
        ds1.add("select a.fId from %s a left join %s b ", "fvirtualcaptualoperationtoyylog", "fvirtualcointype");
        ds1.add("on a.finput_coinFid = b.fId ");
        ds1.add(filterSQL.toString());
        ds1.open();

        modelAndView.addObject("lists", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalOutList");
        // 总数量
        modelAndView.addObject("totalCount", ds1.size());
        return modelAndView;
    }

    //修改体现手续费
    @RequestMapping("ssadmin/goVirtualCoinTypeJSP")
    public ModelAndView goVirtualCoinTypeJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(fid);
            modelAndView.addObject("virtualCoinType", virtualCoinType);

            String filter = "where fvirtualcointype.fid=" + fid + " order by flevel asc";
            List<Fwithdrawfees> allFees = this.withdrawFeesService.list(0, 0, filter, false);

            DecimalFormat df = new DecimalFormat("#,##0.0000");
            List<Map<String, Object>> list = new ArrayList<>();
            for (Fwithdrawfees Ffee : allFees) {
                Map<String, Object> ma = new HashMap<>();
                ma.put("fid", Ffee.getFid());
                String ffee = df.format(Ffee.getFfee());
                ma.put("ffee", ffee);
                ma.put("flevel", Ffee.getFlevel());
                String buyfee = df.format(Ffee.getFlevel());
                // ma.put("fbuyfee", buyfee);
                list.add(ma);
            }
            modelAndView.add("list", list);
        }
        modelAndView.addObject("walletConfigEnable", Constant.walletConfigEnable);

        return modelAndView;
    }
    
  //修改OTC手续费
    @RequestMapping("ssadmin/goOTCCoinTypeJSP")
    public ModelAndView goOTCCoinTypeJSP() throws Exception {
        String url = request.getParameter("url");
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(fid);
            modelAndView.addObject("virtualCoinType", virtualCoinType);

            List<Map<String, Object>> list = new ArrayList<>();
            try(Mysql mysql = new Mysql()){
                MyQuery otc = new MyQuery(mysql);
                otc.add("select * from %s where 1=1 ","fotcfees");
                otc.add("and fvid =%s",fid);
                otc.open();
                DecimalFormat df = new DecimalFormat("#,##0.00%");
                List<Record> otclist = otc.getRecords();
                for(Record li:otclist){
                	 Map<String, Object> ma = new HashMap<>();
                     ma.put("fid", li.getString("fId"));
                     String ffee = df.format(li.getDouble("ffee"));
                     ma.put("ffee", ffee);
                     ma.put("flevel", li.getString("flevel"));
                     //String buyfee = df.format(li.getFlevel());
                     // ma.put("fbuyfee", buyfee);
                     list.add(ma);
                }
            }

            modelAndView.addObject("list", list);
        }  
        return modelAndView;
    }

    //修改OTC的手续费
    @RequestMapping("ssadmin/updateOTCFee")
    public ModelAndView updateOTCFee() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fid"));
        /*List<Fwithdrawfees> all = this.withdrawFeesService.findByProperty("fvirtualcointype.fid", fid);

        // add by hank
        for (Fwithdrawfees ffees : all) {
            String feeKey = "fee" + ffees.getFid();
            double fee = Double.valueOf(request.getParameter(feeKey));

            if (fee >= 1 || fee < 0) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "手续费率只能是小于1的小数！");
                return modelAndView;
            }
        }*/
        try(Mysql mysql = new Mysql()){
        	 MyQuery otcUpdate = new MyQuery(mysql);
        	 otcUpdate.add("select * from %s where 1=1 ","fotcfees");
        	 otcUpdate.add("and fvid =%s",fid);
        	 otcUpdate.open();
        	 if(!otcUpdate.eof()){
        		 List<Record> otcList = otcUpdate.getRecords();
        		 otcUpdate.edit();
        		 for(int j=0;j<otcList.size();j++){
            		 otcUpdate.setBatchSave(true);
        			 int flevel = otcList.get(j).getInt("flevel");
        			 for(int i=0;i<otcList.size();i++){
        				 String feeKey = "fee" + (i+1);
        				 String fee =request.getParameter(feeKey);
        				 if(flevel == (i+1)){
                			 otcList.get(i).setState(DataSetState.dsEdit);
                			 otcList.get(i).setField("ffee", fee);
            			 }
        			 }
        			 
        		 }
        		 otcUpdate.save();
        	 }
        }catch(Exception e){
        	e.printStackTrace();
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "更新成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }
    
    @RequestMapping("ssadmin/saveVirtualCoinType")
    public ModelAndView saveVirtualCoinType(@RequestParam(required = false) MultipartFile filedata,
            @RequestParam(required = false) String fdescription, @RequestParam(required = false) String fname,
            @RequestParam(required = false) String fShortName, @RequestParam(required = false) String faccess_key,
            @RequestParam(required = false) String fsecrt_key, @RequestParam(required = false) String fip,
            @RequestParam(required = false) String fport, @RequestParam(required = false) String fSymbol,
            @RequestParam(required = false) String faddress, @RequestParam(required = false) String fisother,
            @RequestParam(required = false) String FIsWithDraw, @RequestParam(required = false) String fweburl,
            @RequestParam(required = false) String fisEth, @RequestParam(required = false) String fisToken,
            @RequestParam(required = false, defaultValue = "") String mainAddr,
            @RequestParam(required = false) String fisauto, @RequestParam(required = false) String fisautosend,
            @RequestParam(required = false) String fpassword, @RequestParam(required = false) String fisrecharge,
            @RequestParam(required = false) String fregDesc, @RequestParam(required = false) String fwidDesc,
            @RequestParam(required = false) double ftotalqty, @RequestParam(required = false) boolean fisCtcCoin)
            throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        Fvirtualcointype virtualCoinType = new Fvirtualcointype();
        String fpictureUrl = "";
        boolean isTrue = false;
        if (filedata != null && !filedata.isEmpty()) {
            CommonsMultipartFile cf = (CommonsMultipartFile) filedata;
            FileItem fileItem = cf.getFileItem();

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
                    fileUName = TDateTime.FormatDateTime("yyMMddHH", new Date());
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp")) { // 图片上传
                        if (fileName.endsWith(".jpg"))
                            fileUName += ".jpg";
                        if (fileName.endsWith("png"))
                            fileUName += ".png";
                        if (fileName.endsWith(".bmp"))
                            fileUName += ".bmp";
                        oss.upload("vcoin" + "/ssadmin/CoinType/" + fileUName, fileItem.getInputStream());
                        fpictureUrl = config.getProperty("oss.site") + "/" + "vcoin" + "/ssadmin/CoinType/" + fileUName;
                    }
                    isTrue = true;
                }
            }
        }
        if (isTrue) {
            virtualCoinType.setFurl(fpictureUrl);
        }
        virtualCoinType.setFtotalqty(ftotalqty);
        virtualCoinType.setFaddTime(Utils.getTimestamp());
        virtualCoinType.setFdescription(fdescription);
        virtualCoinType.setFname(fname);
        virtualCoinType.setfShortName(fShortName);
        virtualCoinType.setFstatus(VirtualCoinTypeStatusEnum.Abnormal);
        virtualCoinType.setFaccess_key(faccess_key);
        virtualCoinType.setFsecrt_key(fsecrt_key);
        virtualCoinType.setFip(fip);
        virtualCoinType.setFtype(CoinTypeEnum.COIN_VALUE);
        virtualCoinType.setFport(fport);
        virtualCoinType.setfSymbol(fSymbol);
        virtualCoinType.setFisCtcCoin(fisCtcCoin);
        if (fisToken != null && fisToken.trim().length() > 0) {
            virtualCoinType.setFisToken(true);
            
            if ("".equals(faddress.trim())) {
                virtualCoinType.setFaddress("");
            } else {
                virtualCoinType.setFaddress(faddress);
            }
        } else {
            virtualCoinType.setFisToken(false);
        }
        if (fisEth != null && fisEth.trim().length() > 0) {
            virtualCoinType.setFisEth(true);

            if ("".equals(mainAddr.trim())) {
                virtualCoinType.setMainAddr("");
            } else {
                ETHUtils eth = new ETHUtils(null);
                boolean valid = eth.validateAddress(mainAddr.trim());
                if (valid == false) {
                    modelAndView.addObject("statusCode", 500);
                    modelAndView.addObject("message", "以太坊汇总地址错误");
                    return modelAndView;
                }
                virtualCoinType.setMainAddr(mainAddr);
            }
        } else {
            virtualCoinType.setFisEth(false);
            virtualCoinType.setMainAddr(mainAddr);
        }
        if (FIsWithDraw != null && FIsWithDraw.trim().length() > 0) {
            virtualCoinType.setFIsWithDraw(true);
        } else {
            virtualCoinType.setFIsWithDraw(false);
        }
        if (fisautosend != null && fisautosend.trim().length() > 0) {
            virtualCoinType.setFisautosend(true);
        } else {
            virtualCoinType.setFisautosend(false);
        }
        virtualCoinType.setFpassword(fpassword);
        if (fisauto != null && fisauto.trim().length() > 0) {
            virtualCoinType.setFisauto(true);
        } else {
            virtualCoinType.setFisauto(false);
        }
        if (fisrecharge != null && fisrecharge.trim().length() > 0) {
            virtualCoinType.setFisrecharge(true);
        } else {
            virtualCoinType.setFisrecharge(false);
        }
        virtualCoinType.setFregDesc(fregDesc);
        virtualCoinType.setFwidDesc(fwidDesc);
        // if(fistransport != null && fistransport.trim().length() >0){
        // virtualCoinType.setFistransport(true);
        // }else{
        // virtualCoinType.setFistransport(false);
        // }
        // Fabout about = new Fabout();
        // about.setFcontent(".");
        // about.setFtitle(virtualCoinType.getFname());
        // about.setFtype("帮助分类");
        // this.aboutService.saveObj(about);
        // virtualCoinType.setFweburl(String.valueOf(about.getFid()));
        this.virtualCoinService.saveObj(virtualCoinType);

        for (int i = 1; i <= Constant.VIP; i++) {
            Fwithdrawfees fees = new Fwithdrawfees();
            fees.setFlevel(i);
            fees.setFvirtualcointype(virtualCoinType);
            fees.setFfee(0d);
            this.withdrawFeesService.saveObj(fees);
        }

        this.messageSender.publish(ChannelConstant.constantmap, "virtualCoinType");
        this.messageSender.publish(ChannelConstant.constantmap, "allWithdrawCoins");
        this.messageSender.publish(ChannelConstant.constantmap, "allRechargeCoins");
        this.messageSender.publish(ChannelConstant.constantmap, "tradeMappings");

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "新增成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateVirtualCoinType")
    public ModelAndView updateVirtualCoinType(@RequestParam(required = false) MultipartFile filedata,
            @RequestParam(required = false) String fdescription, @RequestParam(required = false) String fname,
            @RequestParam(required = false) String fShortName, @RequestParam(required = false) String faccess_key,
            @RequestParam(required = false) String fsecrt_key, @RequestParam(required = false) String fip,
            @RequestParam(required = false) String fport, @RequestParam(required = false) String fSymbol,
            @RequestParam(required = false) String FIsWithDraw, @RequestParam(required = false) String faddress,
            @RequestParam(required = false) String fisautosend, @RequestParam(required = false) String fpassword,
            @RequestParam(required = false) String fisother, @RequestParam(required = false) int fid,
            @RequestParam(required = false) String fisEth, @RequestParam(required = false) String fisToken,
            @RequestParam(required = false, defaultValue = "") String mainAddr,
            @RequestParam(required = false) String fweburl, @RequestParam(required = false) String fisauto,
            @RequestParam(required = false) String fisrecharge,@RequestParam(required = false) Boolean fisActive,
            @RequestParam(required = false) String fregDesc,@RequestParam(required = false) int fWithDrawTime,
            @RequestParam(required = false) double fWithDrawLimit,
            @RequestParam(required = false) String fwidDesc, @RequestParam(required = false) double ftotalqty,
            @RequestParam(required = false) double fctcSellPrice, @RequestParam(required = false) double fctcBuyPrice,
            @RequestParam(required = false) boolean fisCtcCoin, @RequestParam(required = false) boolean fisOtcCoin)
            throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        if(fWithDrawTime < 0) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "每天允许提现次数不能为负数");
            return modelAndView;
        }
        if(fWithDrawLimit < 0 || fWithDrawLimit > 100) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "提现百分比不能为负数或者不能超过100");
            return modelAndView;
        }
        Fvirtualcointype virtualCoinType = this.virtualCoinService.findById(fid);
        String fpictureUrl = "";
        boolean isTrue = false;
        if (filedata != null && !filedata.isEmpty()) {
            CommonsMultipartFile cf = (CommonsMultipartFile) filedata;
            FileItem fileItem = cf.getFileItem();

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
                    fileUName = TDateTime.FormatDateTime("yyMMddHH", new Date());
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp")) { // 图片上传
                        if (fileName.endsWith(".jpg"))
                            fileUName += ".jpg";
                        if (fileName.endsWith("png"))
                            fileUName += ".png";
                        if (fileName.endsWith(".bmp"))
                            fileUName += ".bmp";
                        oss.upload("vcoin" + "/ssadmin/CoinType/" + fileUName, fileItem.getInputStream());
                        fpictureUrl = config.getProperty("oss.site") + "/" + "vcoin" + "/ssadmin/CoinType/" + fileUName;
                    }
                    isTrue = true;
                }
            }
        }
        if (isTrue) {
            virtualCoinType.setFurl(fpictureUrl);
        }
        if (fisautosend != null && fisautosend.trim().length() > 0) {
            virtualCoinType.setFisautosend(true);
        } else {
            virtualCoinType.setFisautosend(false);
        }
        if (fisToken != null && fisToken.trim().length() > 0) {
            virtualCoinType.setFisToken(true);

            if(faddress != null) {
                if ("".equals(faddress.trim())) {
                    virtualCoinType.setFaddress("");
                } else {
                    virtualCoinType.setFaddress(faddress);
                }
            }
        } else {
            virtualCoinType.setFisToken(false);
        }
        if (fisEth != null && fisEth.trim().length() > 0) {
            virtualCoinType.setFisEth(true);
            if(null != mainAddr) {
                if (!"".equals(mainAddr.trim())) {
                    ETHUtils eth = new ETHUtils(null);
                    boolean valid = eth.validateAddress(mainAddr.trim());
                    if (valid == false) {
                        modelAndView.addObject("statusCode", 500);
                        modelAndView.addObject("message", "以太坊汇总地址错误");
                        return modelAndView;
                    }
                    virtualCoinType.setMainAddr(mainAddr);
                }
            }
        } else {
            virtualCoinType.setFisEth(false);
            if(null != mainAddr) {
                virtualCoinType.setMainAddr(mainAddr);
            }
        }
        virtualCoinType.setFregDesc(fregDesc);
        virtualCoinType.setFwidDesc(fwidDesc);
        if(null != fpassword) {
            virtualCoinType.setFpassword(fpassword);
        }
        if(null != fweburl) {
            virtualCoinType.setFweburl(fweburl);
        }
        virtualCoinType.setFtotalqty(ftotalqty);
        virtualCoinType.setFaddTime(Utils.getTimestamp());
        virtualCoinType.setFdescription(fdescription);
        if(null != fname) {
            virtualCoinType.setFname(fname);
        }
        if(null != fShortName) {
            virtualCoinType.setfShortName(fShortName);
        }
        if(null != faccess_key) {
            virtualCoinType.setFaccess_key(faccess_key);
        }
        if(null != fsecrt_key) {
            virtualCoinType.setFsecrt_key(fsecrt_key);
        }
        if(null != fip) {
            virtualCoinType.setFip(fip);
        }
        if(null != fport) {
            virtualCoinType.setFport(fport);
        }
        virtualCoinType.setFctcBuyPrice(fctcBuyPrice);
        virtualCoinType.setFctcSellPrice(fctcSellPrice);
        virtualCoinType.setfSymbol(fSymbol);
        virtualCoinType.setFisCtcCoin(fisCtcCoin);
        virtualCoinType.setFisOtcCoin(fisOtcCoin);
        virtualCoinType.setFisActive(fisActive);
        virtualCoinType.setfWithdrawTimes(fWithDrawTime);
        virtualCoinType.setfWithdrawLimit(fWithDrawLimit);
        if (FIsWithDraw != null && FIsWithDraw.trim().length() > 0) {
            virtualCoinType.setFIsWithDraw(true);
        } else {
            virtualCoinType.setFIsWithDraw(false);
        }
        if (fisauto != null && fisauto.trim().length() > 0) {
            virtualCoinType.setFisauto(true);
        } else {
            virtualCoinType.setFisauto(false);
        }
        if (fisrecharge != null && fisrecharge.trim().length() > 0) {
            virtualCoinType.setFisrecharge(true);
        } else {
            virtualCoinType.setFisrecharge(false);
        }
        // if(fistransport != null && fistransport.trim().length() >0){
        // virtualCoinType.setFistransport(true);
        // }else{
        // virtualCoinType.setFistransport(false);
        // }
//        if (virtualCoinType.getFtype() == CoinTypeEnum.FB_CNY_VALUE) {
//            modelAndView.addObject("statusCode", 300);
//            modelAndView.addObject("message", "人民币不允许修改");
//            return modelAndView;
//        }
        this.virtualCoinService.updateObj(virtualCoinType);

        // Fabout about =
        // this.aboutService.findById(Integer.parseInt(virtualCoinType.getFweburl()));
        // if(about != null){
        // about.setFtitle(virtualCoinType.getFname());
        // this.aboutService.updateObj(about);
        // }

        this.messageSender.publish(ChannelConstant.constantmap, "virtualCoinType");
        this.messageSender.publish(ChannelConstant.constantmap, "allWithdrawCoins");
        this.messageSender.publish(ChannelConstant.constantmap, "allRechargeCoins");
        this.messageSender.publish(ChannelConstant.constantmap, "tradeMappings");

        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "更新成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/goWallet")
    public ModelAndView goWallet() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        String password = request.getParameter("passWord");
        Fvirtualcointype virtualcointype = this.virtualCoinService.findById(fid);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        boolean flag = false;
        virtualcointype.setFstatus(VirtualCoinTypeStatusEnum.Normal);
        String msg = "";
        try {
            flag = this.virtualCoinService.updateCoinType(virtualcointype, password);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        }

        this.messageSender.publish(ChannelConstant.constantmap, "virtualCoinType");
        this.messageSender.publish(ChannelConstant.constantmap, "allWithdrawCoins");
        this.messageSender.publish(ChannelConstant.constantmap, "allRechargeCoins");
        this.messageSender.publish(ChannelConstant.constantmap, "tradeMappings");

        if (!flag) {
            modelAndView.addObject("message", msg);
            modelAndView.addObject("statusCode", 300);
        } else {
            modelAndView.addObject("message", "启用成功");
            modelAndView.addObject("statusCode", 200);
            modelAndView.addObject("callbackType", "closeCurrent");
        }
        return modelAndView;
    }

    @RequestMapping("ssadmin/deleteVirtualCoinType")
    public ModelAndView deleteVirtualCoinType() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fvirtualcointype virtualcointype = this.virtualCoinService.findById(fid);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        if (virtualcointype.getFtype() == CoinTypeEnum.FB_CNY_VALUE) {
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "人民币不允许禁用");
            return modelAndView;
        }

        virtualcointype.setFstatus(VirtualCoinTypeStatusEnum.Abnormal);
        this.virtualCoinService.updateObj(virtualcointype);

        this.messageSender.publish(ChannelConstant.constantmap, "virtualCoinType");
        this.messageSender.publish(ChannelConstant.constantmap, "allWithdrawCoins");
        this.messageSender.publish(ChannelConstant.constantmap, "allRechargeCoins");
        this.messageSender.publish(ChannelConstant.constantmap, "tradeMappings");

        modelAndView.addObject("message", "禁用成功");
        modelAndView.addObject("statusCode", 200);
        return modelAndView;
    }

    /**
     * 测试钱包服务器
     */
    @RequestMapping("ssadmin/testWallet")
    public ModelAndView testWallet() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fvirtualcointype coinInfo = this.virtualCoinService.findById(fid);
        DeCodeUtil.deCode(coinInfo);
        WalletConfig config = new WalletConfig();
        config.setAccessKey(coinInfo.getFaccess_key());
        config.setIP(coinInfo.getFip());
        config.setPort(coinInfo.getFport());
        config.setSecretKey(coinInfo.getFsecrt_key());

        if (config.getAccessKey() == null || config.getIP() == null || config.getPort() == null
                || config.getSecretKey() == null) {
            modelAndView.addObject("message", "钱包连接失败，请检查配置信息");
            modelAndView.addObject("statusCode", 300);
        }

        try {
            if (coinInfo.isFisEth() == true) {
            	// 以太坊类型
                ETHUtils util = new ETHUtils(config);
                if (coinInfo.getMainAddr() != null && coinInfo.isFisToken() != true) {
                    long blocks = util.getBlockNumberValue();
                    double balance = util.getBalance(coinInfo.getMainAddr());
                    modelAndView.addObject("message", "测试成功，主地址余额:" + balance + ",区块高度：" + blocks);
                    modelAndView.addObject("statusCode", 200);
                }else if(coinInfo.isFisToken() == true){
                	long blocks = util.getBlockNumberValue();
                	double balancepre = util.getBalance(coinInfo.getMainAddr());
            		String ipandport = "http://" + coinInfo.getFip() + ":" + coinInfo.getFport() + "/";
            		Web3j web3j = Web3j.build(new HttpService(ipandport));
            		BigInteger balance = TokenClient.getTokenBalance(web3j, coinInfo.getMainAddr(), coinInfo.getFaddress());
            		balance = balance.divide(BigInteger.valueOf(Math.round(Math.pow(10, TokenClient.getTokenDecimals(web3j, coinInfo.getFaddress())))));
            		modelAndView.addObject("message", "测试成功，主地址代币余额:" + balance + ",区块高度：" + blocks +",手续费剩余：" + balancepre);
                    modelAndView.addObject("statusCode", 200);
                } 
                else {
                    modelAndView.addObject("message", "主地址没有设置或者网络连接不通");
                    modelAndView.addObject("statusCode", 500);
                }
            } else {
                String coinType = coinInfo.getfShortName();
                if ("USDT".equals(coinType)) {
                    OmniUtils util = new OmniUtils(config);
                    double balance = util.getOmniWalletBalance(OmniUtils.propertyId);

                    JSONObject json = util.getBlockChaininfo();
                    int blocks = json.getJSONObject("result").getInt("blocks");
                    modelAndView.addObject("message", "测试成功，钱包余额:" + balance + ",区块高度：" + blocks);
                    modelAndView.addObject("statusCode", 200);
                } else {
                    // 比特币类型
                    BTCUtils util = new BTCUtils(config);
                    double balance = util.getWalletBalance();
                    JSONObject json = util.getBlockChaininfo();
                    int blocks = json.getJSONObject("result").getInt("blocks");
                    modelAndView.addObject("message", "测试成功，钱包余额:" + balance + ",区块高度：" + blocks);
                    modelAndView.addObject("statusCode", 200);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("message", "钱包连接失败，请检查配置信息");
            modelAndView.addObject("statusCode", 300);
        }
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateCoinFee")
    public ModelAndView updateCoinFee() throws Exception {
        JspPage modelAndView = new JspPage(request);
        int fid = Integer.parseInt(request.getParameter("fid"));
        List<Fwithdrawfees> all = this.withdrawFeesService.findByProperty("fvirtualcointype.fid", fid);

        // add by hank
        for (Fwithdrawfees ffees : all) {
            String feeKey = "fee" + ffees.getFid();
            double fee = Double.valueOf(request.getParameter(feeKey));

            if (fee >= 1 || fee < 0) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "手续费率只能是小于1的小数！");
                return modelAndView;
            }
        }

        for (Fwithdrawfees ffees : all) {
            String feeKey = "fee" + ffees.getFid();
            double fee = Double.valueOf(request.getParameter(feeKey));
            ffees.setFfee(fee);
            this.withdrawFeesService.updateObj(ffees);
        }

        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "更新成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    /**
     * 生成钱包地址
     */
    @RequestMapping("ssadmin/createWalletAddress")
    public ModelAndView createWalletAddress() throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        int fid = Integer.parseInt(request.getParameter("uid"));
        type = this.virtualCoinService.findById(fid);
        if (!type.isFIsWithDraw() && !type.isFisrecharge()) {
            modelAndView.addObject("message", "不允许充值和提现的虚拟币类型不能生成虚拟地址!");
            modelAndView.addObject("statusCode", 300);
            return modelAndView;
        }

        config = new WalletConfig();
        DeCodeUtil.deCode(type);
        config.setAccessKey(type.getFaccess_key());
        config.setIP(type.getFip());
        config.setPort(type.getFport());
        config.setSecretKey(type.getFsecrt_key());
//        config.setPassword(request.getParameter("passWord"));
        config.setPassword(type.getFpassword());

        if (config.getAccessKey() == null || config.getIP() == null || config.getPort() == null
                || config.getSecretKey() == null) {
            modelAndView.addObject("message", "钱包连接失败，请检查配置信息");
            modelAndView.addObject("statusCode", 300);
            return modelAndView;
        }

        try {
            Fvirtualcointype fvirtualcointype = this.virtualCoinService.findById(fid);
            if (fvirtualcointype.isFisEth()) {
                ETHUtils ethUtils = new ETHUtils(config);
                if (fvirtualcointype.getStartBlockId() == 0) {
                    fvirtualcointype.setStartBlockId(ethUtils.getBlockNumberValue());
                    this.virtualCoinService.updateObj(fvirtualcointype);
                }
            }
        } catch (Exception e1) {
            modelAndView.addObject("message", "钱包异常!");
            modelAndView.addObject("statusCode", 300);
            return modelAndView;
        }

        try {
            new Thread(new Work()).start();
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("message", "钱包异常!");
            modelAndView.addObject("statusCode", 300);
            return modelAndView;
        }

        modelAndView.addObject("message", "后台执行中!");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("rel", "createWalletAddress");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    private WalletConfig config;
    private Fvirtualcointype type;
    private static String walletnum = new ServerConfig().getProperty("walletnum");

    class Work implements Runnable {
        @Override
        public void run() {
            createAddress(config, type);
        }
    }

    private void createAddress(WalletConfig config, Fvirtualcointype type) {
        try {
            WalletUtil util = WalletFactory.build(config, type.isFisEth() ? "ETH" : "BTC");
            for (int i = 0; i < Integer.valueOf(walletnum); i++) {
                String address = util.createAddress();
                if (address == null || address.trim().length() == 0) {
                    break;
                }
                Fpool poolInfo = new Fpool();
                poolInfo.setFaddress(address);
                poolInfo.setFvirtualcointype(type);
                poolService.saveObj(poolInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以太坊钱包汇总
     * 
     * @param uid
     *            币种id
     * @param password
     *            确认密码
     */
    @RequestMapping("ssadmin/etcMainAddr")
    public ModelAndView etcMainAddr(@RequestParam(required = true) int uid,
            @RequestParam(required = true) final String password) throws Exception {
        JspPage modelAndView = new JspPage(request);
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        final Fvirtualcointype fvirtualcointype = this.virtualCoinService.findById(uid);
        DeCodeUtil.deCode(fvirtualcointype);
        if (fvirtualcointype == null || fvirtualcointype.isFisEth() == false) {
            modelAndView.addObject("message", "非以太坊钱包不可汇总");
            modelAndView.addObject("statusCode", 500);
            modelAndView.addObject("rel", "etcMainAddr");
            return modelAndView;
        }

        if (fvirtualcointype.getMainAddr() == null || "".equals(fvirtualcointype.getMainAddr().trim())) {
            modelAndView.addObject("message", "未设置主钱包地址");
            modelAndView.addObject("statusCode", 500);
            modelAndView.addObject("rel", "etcMainAddr");
            return modelAndView;
        }

        WalletConfig config = new WalletConfig();
        config.setAccessKey(fvirtualcointype.getFaccess_key());
        config.setIP(fvirtualcointype.getFip());
        config.setPort(fvirtualcointype.getFport());
        config.setSecretKey(fvirtualcointype.getFsecrt_key());
        config.setPassword(password);

        final ETHUtils ethUtils = new ETHUtils(config);
        boolean flag = false;

        flag = ethUtils.unlockAccount(fvirtualcointype.getMainAddr().trim());
        ethUtils.lockAccount(fvirtualcointype.getMainAddr().trim());

        if (flag == false) {
            modelAndView.addObject("message", "钱包链接错误，或密码错误");
            modelAndView.addObject("statusCode", 500);
            modelAndView.addObject("rel", "etcMainAddr");
            return modelAndView;
        }

        // FIXME 汇总钱包余额已经由队列来执行
        // sendMain(ethUtils, fvirtualcointype.getMainAddr().trim());
        modelAndView.addObject("message", "后台执行中,执行时间由钱包中地址数量决定，短时间内请不要重复执行该功能!");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("rel", "etcMainAddr");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

}
