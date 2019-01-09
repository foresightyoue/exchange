package org.dromara.soul.common.dto.zk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppAuthZkDTO implements Serializable {
    private String appKey;

    private String appSecret;

    private Boolean enabled;
}
