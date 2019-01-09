package org.dromara.soul.common.dto.zk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionZkDTO implements Serializable {
    /**
     * param type（post  query  uri..）.
     */
    private String paramType;

    /**
     * （=  > <  like match）.
     */
    private String operator;

    /**
     * param name.
     */
    private String paramName;

    /**
     * param value.
     */
    private String paramValue;
}
