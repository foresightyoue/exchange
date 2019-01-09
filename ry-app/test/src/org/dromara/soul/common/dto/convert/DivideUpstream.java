package org.dromara.soul.common.dto.convert;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DivideUpstream {
    /**
     * host.
     */
    private String upstreamHost;

    /**
     * this is http protocol.
     */
    private String protocol;

    /**
     * url.
     */
    private String upstreamUrl;

    /**
     * timeout.
     */
    private long timeout;


    /**
     * http retry.
     */
    private int retry;

    /**
     * weight.
     */
    private int weight;
}
