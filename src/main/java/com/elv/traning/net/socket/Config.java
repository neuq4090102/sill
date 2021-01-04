package com.elv.traning.net.socket;

/**
 * @author lxh
 * @since 2020-12-22
 */
public class Config {

    private String host;
    private int port;

    public Config() {
    }

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

    public static Config of() {
        return new Config();
    }

    public Config host(String host) {
        this.host = host;
        return this;
    }

    public Config port(int port) {
        this.port = port;
        return this;
    }
}
