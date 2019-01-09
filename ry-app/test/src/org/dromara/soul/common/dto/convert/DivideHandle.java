package org.dromara.soul.common.dto.convert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class DivideHandle extends HystrixHandle implements Serializable {
    /**
     * loadBalance.
     * {@linkplain org.dromara.soul.common.enums.LoadBalanceEnum}
     */
    private String loadBalance;

    /**
     * upstream list.
     */
    private List<DivideUpstream> upstreamList;
}
