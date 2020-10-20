package com.elv.core.tool.db.redis;

import com.elv.core.util.StrUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author lxh
 * @since 2020-06-16
 */
public class RedisAccess {

    private JedisPool jedisPool;

    private JedisPoolConfig jedisPoolConfig;
    private String host;
    private String password;
    private int port;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void init() {
        if (StrUtil.isBlank(this.getPassword())) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, 3000);
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, 3000, password);
        }
    }

    // public Jedis conn() {
    //     return jedisPool.getResource();
    // }
    //
    // public void close(Jedis jedis) {
    //     if (jedis != null) {
    //         jedis.close();
    //     }
    // }

}
