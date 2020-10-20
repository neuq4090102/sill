package com.elv.core.tool.db.model;

import java.util.List;

/**
 * @author lxh
 * @since 2020-08-13
 */
public final class JDBCQueryParam {

    private String tableName;
    private List<String> wheres;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getWheres() {
        return wheres;
    }

    public void setWheres(List<String> wheres) {
        this.wheres = wheres;
    }
}
