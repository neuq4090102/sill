package com.elv.core.tool.db.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.elv.core.tool.db.model.Authorization;
import com.elv.core.tool.db.model.EmailAccount;
import com.elv.core.tool.db.model.JDBCQueryParam;
import com.elv.core.tool.db.model.SecurityConfig;

/**
 * 基础数据查询-内部使用
 *
 * @author lxh
 * @since 2020-08-13
 */
public class BasicQuery {

    public static EmailAccount emailAccount() {
        return queryOne(BasicConfig.TABLE_EMAIL_ACCOUNT, EmailAccount.class, "alias = 'elv1729' ");
    }

    public static SecurityConfig secretConfig() {
        return queryOne(BasicConfig.TABLE_SECURITY_CONFIG, SecurityConfig.class, "name = 'sill_token' ");
    }

    public static Authorization authorization() {
        return queryOne(BasicConfig.TABLE_AUTHORIZATION, Authorization.class, "app_id = '21672001' ");
    }

    private static <T> T queryOne(String tableName, Class<T> targetClass, String... whereParams) {
        List<String> wheres = new ArrayList<>();
        for (String whereParam : whereParams) {
            wheres.add(whereParam);
        }

        JDBCQueryParam queryParam = new JDBCQueryParam();
        queryParam.setTableName(tableName);
        queryParam.setWheres(wheres);

        T result = null;
        try {
            result = Optional.ofNullable(JDBC.queryOne(queryParam, targetClass)).orElse(targetClass.newInstance());
        } catch (Exception e) {
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(authorization().getApiKey());
    }

}
