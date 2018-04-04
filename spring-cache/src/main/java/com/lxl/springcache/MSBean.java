package com.lxl.springcache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author lxl lukas
 * @description
 * @create 2018/1/31
 */
//@ConfigurationProperties(prefix = "ms.db")
@Configuration
@PropertySource("classpath:config/db.properties")
@ConfigurationProperties(prefix = "ms.db")
public class MSBean {
    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String maxActive;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive;
    }

    @Override
    public String toString() {
        return driverClassName+" "+url+ " "+username+" "+maxActive;
    }
}
