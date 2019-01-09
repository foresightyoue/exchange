package org.dromara.soul.common.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpringCloudConfig implements Serializable {
    private String serviceUrl;

    private Integer leaseRenewalIntervalInSeconds;

    private Integer leaseExpirationDurationInSeconds;
}
