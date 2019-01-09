package org.dromara.soul.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.soul.common.exception.SoulException;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum  HttpMethodEnum {
    /**
     * Get http method enum.
     */
    GET("get", true),

    /**
     * Post http method enum.
     */
    POST("post", true),

    /**
     * Put http method enum.
     */
    PUT("put", false),

    /**
     * Delete http method enum.
     */
    DELETE("delete", false);

    private final String name;

    private final Boolean support;

    /**
     *  convert by name.
     *
     * @param name name
     * @return {@link HttpMethodEnum }
     */
    public static HttpMethodEnum acquireByName(final String name) {
        return Arrays.stream(HttpMethodEnum.values())
                .filter(e -> e.support && e.name.equals(name)).findFirst()
                .orElseThrow(() -> new SoulException(" this http method can not support!"));
    }
}
