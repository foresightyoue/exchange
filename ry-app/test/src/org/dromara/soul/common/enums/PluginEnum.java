package org.dromara.soul.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum PluginEnum {
    /**
     * Global plugin enum.
     */
    GLOBAL(1, "global"),

    /**
     * Sign plugin enum.
     */
    SIGN(2, "sign"),

    /**
     * Waf plugin enum.
     */
    WAF(10, "waf"),

    /**
     * Rate limiter plugin enum.
     */
    RATE_LIMITER(20, "rate_limiter"),

    /**
     * Rewrite plugin enum.
     */
    REWRITE(30, "rewrite"),

    /**
     * Redirect plugin enum.
     */
    REDIRECT(40, "redirect"),

    /**
     * Divide plugin enum.
     */
    DIVIDE(50, "divide"),

    /**
     * Dubbo plugin enum.
     */
    DUBBO(60, "dubbo"),

    /**
     * springCloud plugin enum.
     */
    SPRING_CLOUD(70, "springCloud"),

    /**
     * Monitor plugin enum.
     */
    MONITOR(80, "monitor");

    private final int code;

    private final String name;

    /**
     * get plugin enum by code.
     *
     * @param code plugin code.
     * @return plugin enum.
     */
    public static PluginEnum getPluginEnumByCode(final int code) {
        return Arrays.stream(PluginEnum.values())
                .filter(pluginEnum -> pluginEnum.getCode() == code)
                .findFirst().orElse(PluginEnum.GLOBAL);
    }

    /**
     * get plugin enum by name.
     *
     * @param name plugin name.
     * @return plugin enum.
     */
    public static PluginEnum getPluginEnumByName(final String name) {
        return Arrays.stream(PluginEnum.values())
                .filter(pluginEnum -> pluginEnum.getName().equals(name))
                .findFirst().orElse(PluginEnum.GLOBAL);
    }
}
