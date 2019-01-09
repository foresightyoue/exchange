package org.dromara.soul.common.dto.zk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginZkDTO implements Serializable {
    private String id;

    private String name;

    private Boolean enabled;
}
