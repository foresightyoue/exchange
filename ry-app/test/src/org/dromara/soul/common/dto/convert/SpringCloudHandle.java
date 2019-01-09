package org.dromara.soul.common.dto.convert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SpringCloudHandle extends HystrixHandle implements Serializable {
    /**
     * this is register eureka serviceId.
     */
    private String serviceId;

    /**
     * this remote uri path.
     */
    private String path;
}
