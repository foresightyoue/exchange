package org.dromara.soul.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PluginTypeEnum {
    /**
     * before plugin type enum.
     */
    BEFORE("before"),

    /**
     * function plugin type enum.
     */
    FUNCTION("function"),

    /**
     * last plugin type enum.
     */
    LAST("last");

    private final String name;
}
