package org.dromara.soul.common.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class MonitorConfig implements Serializable {
    private String influxdbUrl;

    private String userName;

    private String password;

    private String database;

    private String retentionPolicy;

    private Integer connectTimeout;

    private Integer readTimeout;

    private Integer writeTimeout;
}
