package org.dromara.soul.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WafEnum {
    /**
     * Reject waf enum.
     */
    REJECT(0, "reject"),

    /**
     * Allow waf enum.
     */
    ALLOW(1, "allow");

    private final int code;

    private final String name;
}
