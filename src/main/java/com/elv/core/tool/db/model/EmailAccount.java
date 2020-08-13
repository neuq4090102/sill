package com.elv.core.tool.db.model;

/**
 * 邮件账户
 * @author lxh
 * @since 2020-08-13
 */
public class EmailAccount {

    private long id;
    private String account; // 账户
    private String alias; // 账户别名
    private String password; // 密码
    private int type; // 账户类型，1=个人，2=公司
    private int status; // 状态，1=启用，2=禁用

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
