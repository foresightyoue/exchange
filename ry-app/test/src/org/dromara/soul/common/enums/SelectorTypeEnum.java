package org.dromara.soul.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SelectorTypeEnum {
    /**
     * full selector type enum.
     */
    FULL_FLOW(0, "全流量"),

    /**
     * Or match mode enum.
     */
    CUSTOM_FLOW(1, "自定义流量");

    private final int code;

    private final String name;

    /**
     * get selector type name by code.
     *
     * @param code selector type code.
     * @return selector type name.
     */
    public static String getSelectorTypeByCode(final int code) {
        for (SelectorTypeEnum selectorTypeEnum : SelectorTypeEnum.values()) {
            if (selectorTypeEnum.getCode() == code) {
                return selectorTypeEnum.getName();
            }
        }
        return null;
    }
}
