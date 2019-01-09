package org.dromara.soul.common.dto.zk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleZkDTO implements Serializable {
    private String id;

    private String name;

    private String selectorId;

    /**
     * match way（0 and  1 or).
     */
    private Integer matchMode;

    private Integer sort;

    private Boolean enabled;

    private Boolean loged;

    /**
     * handle message（different plugin have different handle to mark ,json style）.
     */
    private String handle;

    private List<ConditionZkDTO> conditionZkDTOList;
}
