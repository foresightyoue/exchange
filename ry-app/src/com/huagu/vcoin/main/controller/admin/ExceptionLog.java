package com.huagu.vcoin.main.controller.admin;

import org.apache.log4j.Logger;

import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;

import cn.cerc.jdb.core.TDateTime;

public class ExceptionLog {
    private static final Logger log = Logger.getLogger(ExceptionLog.class);

    /**
     * 
     * @param furl
     *            访问地址
     * @param fsql
     *            sql/查询语句
     * @param fprompt
     *            提示
     * @param fmsg
     *            异常信息
     */
    public static void add(String furl, String fsql, String fprompt, String fmsg) {
        try {
            Mysql handle = new Mysql();
            MyQuery ds = new MyQuery(handle);
            ds.add("select * from %s", "fexceptionlog");
            ds.setMaximum(1);
            ds.open();
            ds.append();
            ds.setField("furl", furl);
            ds.setField("fsql", fsql);
            ds.setField("fprompt", fprompt);
            ds.setField("fmsg", fmsg);
            ds.setField("fCreateTime", TDateTime.Now());
            ds.post();
        } catch (Exception e) {
            log.warn("异常数据保存失败");
        }
    }

    public static void main(String[] args) {
        add("/ssadmin/goTrafer.html", "select * from fuser", "", null);
    }
}
