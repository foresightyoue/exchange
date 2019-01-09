package org.dromara.soul.common.dto.convert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class DubboHandle extends HystrixHandle implements Serializable {
    /**
     * zookeeper url is required.
     */
    private String registry;

    /**
     * dubbo application name is required.
     */
    private String appName;

    /**
     * dubbo protocol.
     */
    private String protocol;

    /**
     * port.
     */
    private int port;

    /**
     * version.
     */
    private String version;

    /**
     *  group.
     */
    private String group;

    /**
     * retries.
     */
    private Integer retries;

    /**
     * {@linkplain org.dromara.soul.common.enums.LoadBalanceEnum}
     */
    private String loadBalance;
}
