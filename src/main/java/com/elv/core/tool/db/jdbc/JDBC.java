package com.elv.core.tool.db.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.elv.core.tool.db.model.EmailAccount;
import com.elv.core.tool.db.model.JDBCQueryParam;
import com.elv.core.util.BeanUtil;
import com.elv.core.util.JsonUtil;

/**
 * JDBC工具
 *
 * @author lxh
 * @since 2020-08-13
 */
public class JDBC {

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
        } catch (Exception e) {
            throw new RuntimeException("JDBC#init error.", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static <T> T queryOne(JDBCQueryParam queryParam, Class<T> targetClass) {
        List<T> results = query(queryParam, targetClass);
        if (results != null && results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    public static <T> List<T> query(JDBCQueryParam queryParam, Class<T> targetClass) {
        List<Field> fields = BeanUtil.getAllFields(targetClass);
        Map<String, Class<?>> fieldTypeMap = fields.stream()
                .collect(Collectors.toMap(item -> item.getName(), item -> item.getType()));
        Map<String, String> sqlFieldMap = fields.stream()
                .collect(Collectors.toMap(item -> item.getName(), item -> humpToUnderline(item.getName())));
        Map<String, Method> setterMap = BeanUtil.getSetterMap(targetClass);

        List<T> results = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(getSql(queryParam));
            while (rs.next()) {
                T result = targetClass.newInstance();
                results.add(result);

                for (Entry<String, Class<?>> entry : fieldTypeMap.entrySet()) {
                    Class<?> fieldType = entry.getValue();
                    String fieldName = entry.getKey();
                    Object fieldValue = null;
                    if (String.class.isAssignableFrom(fieldType)) {
                        fieldValue = rs.getString(sqlFieldMap.get(fieldName));
                    } else if (Integer.class.isAssignableFrom(fieldType) || int.class.isAssignableFrom(fieldType)) {
                        fieldValue = rs.getInt(sqlFieldMap.get(fieldName));
                    } else if (Long.class.isAssignableFrom(fieldType) || long.class.isAssignableFrom(fieldType)) {
                        fieldValue = rs.getLong(sqlFieldMap.get(fieldName));
                    }

                    setterMap.get(fieldName).invoke(result, fieldValue);
                }
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("JDBC#query error.", e);
        }
    }

    private static String getSql(JDBCQueryParam queryParam) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ");
        sql.append(queryParam.getTableName().trim()).append(" ");
        List<String> wheres = queryParam.getWheres();
        if (wheres != null && wheres.size() > 0) {
            sql.append("where ");
            wheres.forEach(item -> sql.append(item.trim()).append(" "));
        }
        return sql.toString();
    }

    // TODO 读取文件配置
    private static String getUrl() {
        return "jdbc:mysql://localhost:3306/sill";
    }

    private static String getUser() {
        return "root";
    }

    private static String getPassword() {
        return "mariadb";
    }

    /**
     * 驼峰转下划线
     *
     * @param param 参数
     * @return java.lang.String
     */
    private static String humpToUnderline(String param) {
        Matcher matcher = Pattern.compile("[A-Z]").matcher(param);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param param 参数
     * @return java.lang.String
     */
    private static String underlineToHump(String param) {
        Matcher matcher = Pattern.compile("_(\\w)").matcher(param.toLowerCase());
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        List<String> wheres = new ArrayList<>();
        // wheres.add("alias = '1' ");

        JDBCQueryParam queryParam = new JDBCQueryParam();
        queryParam.setTableName("t_sill_email_account");
        queryParam.setWheres(wheres);

        List<EmailAccount> emailAccounts = query(queryParam, EmailAccount.class);
        emailAccounts.stream().forEach(item -> System.out.println(JsonUtil.toJson(item)));

        EmailAccount emailAccount = queryOne(queryParam, EmailAccount.class);
        System.out.println(emailAccount.getPassword());

        System.out.println(underlineToHump("table_name_ali"));
    }

}
