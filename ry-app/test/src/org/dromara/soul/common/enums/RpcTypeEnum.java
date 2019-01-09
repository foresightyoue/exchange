package org.dromara.soul.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.soul.common.exception.SoulException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum RpcTypeEnum {
    /**
     * Http rpc type enum.
     */
    HTTP("http", true),

    /**
     * Dubbo rpc type enum.
     */
    DUBBO("dubbo", true),


    /**
     * springCloud rpc type enum.
     */
    SPRING_CLOUD("springCloud", true),

    /**
     * motan.
     */
    MOTAN("motan", false),

    /**
     * grpc.
     */
    GRPC("grpc", false);


    private final String name;

    private final Boolean support;


    /**
     * acquire operator supports.
     *
     * @return operator support.
     */
    public static List<RpcTypeEnum> acquireSupports() {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(e -> e.support).collect(Collectors.toList());
    }

    /**
     * acquireByName.
     *
     * @param name this is rpc type
     * @return RpcTypeEnum
     */
    public static RpcTypeEnum acquireByName(final String name) {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(e -> e.support && e.name.equals(name)).findFirst()
                .orElseThrow(() -> new SoulException(" this rpc type can not support!"));
    }
}
