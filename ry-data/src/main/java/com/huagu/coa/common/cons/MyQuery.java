package com.huagu.coa.common.cons;

import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.mysql.SqlOperator;
import cn.cerc.jdb.mysql.SqlQuery;

public class MyQuery extends SqlQuery {
    private static final long serialVersionUID = 4893380505757273569L;

    public MyQuery(IHandle handle) {
        super(handle);
        this.getDefaultOperator().setPrimaryKey("fId");
    }

    @Override
    public SqlOperator getDefaultOperator() {
        return (SqlOperator) super.getDefaultOperator();
    }

}
