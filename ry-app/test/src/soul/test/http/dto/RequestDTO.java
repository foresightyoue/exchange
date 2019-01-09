package soul.test.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO implements Serializable {
    private String module;

    private String method;

    private String content;

    private String extInfo;
}
