package org.dromara.soul.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.soul.common.exception.SoulException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum OperatorEnum {
    /**
     * Match operator enum.
     */
    MATCH("match", true),

    /**
     * Eq operator enum.
     */
    EQ("=", true),

    /**
     * Gt operator enum.
     */
    GT(">", false),

    /**
     * Lt operator enum.
     */
    LT("<", false),

    /**
     * Like operator enum.
     */
    LIKE("like", true);

    private final String alias;

    private final Boolean support;

    /**
     * acquire operator supports.
     *
     * @return operator support.
     */
    public static List<OperatorEnum> acquireSupport() {
        return Arrays.stream(OperatorEnum.values())
                .filter(e -> e.support).collect(Collectors.toList());
    }

    /**
     * get operator enum by alias.
     *
     * @param alias operator alias.
     * @return operator enum.
     */
    public static OperatorEnum getOperatorEnumByAlias(final String alias) {
        return Arrays.stream(OperatorEnum.values())
                .filter(e -> e.getAlias().equals(alias) && e.support).findFirst()
                .orElseThrow(() -> new SoulException(" this  operator can not support!"));

    }
}
