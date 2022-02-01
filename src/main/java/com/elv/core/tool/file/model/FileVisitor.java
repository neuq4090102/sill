package com.elv.core.tool.file.model;

import com.elv.core.constant.Const;

/**
 * 文件系统
 *
 * @author lxh
 * @since 2021-11-09
 */
public class FileVisitor {

    private String host;
    private int port = Const.FTP_PORT;
    private String userName;
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static FileVisitor of() {
        return new FileVisitor();
    }

    public FileVisitor host(String host) {
        this.host = host;
        return this;
    }

    public FileVisitor port(int port) {
        this.port = port;
        return this;
    }

    public FileVisitor userName(String userName) {
        this.userName = userName;
        return this;
    }

    public FileVisitor password(String password) {
        this.password = password;
        return this;
    }

}
