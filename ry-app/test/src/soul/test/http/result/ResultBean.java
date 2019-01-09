package soul.test.http.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultBean implements Serializable {
    private Integer code;

    private String msg;

    private Object data;
}
