package org.dromara.soul.common.dto.zk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectorZkDTO implements Serializable {
    private String id;

    private String pluginId;

    /**
     * plugin name.
     */
    private String pluginName;

    private String name;

    /**
     * matchMode（0 and  1 or).
     */
    private Integer matchMode;

    /**
     * type（false full，true custom).
     */
    private Integer type;

    private Integer sort;

    private Boolean enabled;

    private Boolean loged;

    private Boolean continued;

    private List<ConditionZkDTO> conditionZkDTOList;
}
