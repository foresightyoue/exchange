//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.cerc.jdb.mysql;

import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.other.utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BuildStatement implements AutoCloseable {
    private Connection conn;
    private StringBuffer sb = new StringBuffer();
    private PreparedStatement ps = null;
    private List<Object> items = new ArrayList();
    private SimpleDateFormat sdf;

    public BuildStatement(Connection conn) {
        this.conn = conn;
    }

    public BuildStatement append(String sql) {
        this.sb.append(sql);
        return this;
    }

    public void append(String sql, Object data) {
        this.sb.append(sql);
        Object result = data;
        if (data instanceof TDateTime) {
            result = data.toString();
        } else if (data instanceof Double) {
            result = utils.roundTo((Double)data, -8);
        } else if (data instanceof Date) {
            if (this.sdf == null) {
                this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }

            result = this.sdf.format(data);
        }

        this.items.add(result);
    }

    public PreparedStatement build() throws SQLException {
        if (this.ps != null) {
            throw new RuntimeException("ps not is null");
        } else {
            this.ps = this.conn.prepareStatement(this.sb.toString());
            int i = 0;
            Iterator var2 = this.items.iterator();

            while(var2.hasNext()) {
                Object value = var2.next();
                ++i;
                this.ps.setObject(i, value);
            }

            return this.ps;
        }
    }

    public String getCommand() {
        if (this.ps == null) {
            return null;
        } else {
            String Result = this.ps.toString();
            return Result.substring(Result.indexOf(58) + 2);
        }
    }

    public void close() {
        try {
            if (this.ps != null) {
                this.ps.close();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
