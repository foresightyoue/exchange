package org.dromara.soul.common.dto.convert;

import lombok.Data;

@Data
public class RateLimiterHandle {
    /**
     * replenish rate.
     */
    private double replenishRate;

    /**
     * burst capacity.
     */
    private double burstCapacity;

    /**
     * loged.
     */
    private boolean loged;
}
