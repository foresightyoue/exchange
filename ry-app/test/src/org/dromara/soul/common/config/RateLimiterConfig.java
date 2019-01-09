package org.dromara.soul.common.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class RateLimiterConfig implements Serializable {
    private Boolean cluster = false;

    private Boolean sentinel = false;

    /**
     * cluster url example:ip:port;ip:port.
     */
    private String clusterUrl;

    /**
     * sentinel url example:ip:port;ip:port.
     */
    private String sentinelUrl;

    private String masterName;

    private String hostName;

    private int port;

    private String password;
}
