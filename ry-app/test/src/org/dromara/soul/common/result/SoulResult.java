package org.dromara.soul.common.result;

import lombok.Data;
import org.dromara.soul.common.exception.CommonErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class SoulResult implements Serializable {
    private static final long serialVersionUID = -2792556188993845048L;

    private Integer code;

    private String message;

    private Object data;

    /**
     * Instantiates a new Soul result.
     */
    public SoulResult() {

    }

    /**
     * Instantiates a new Soul result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    public SoulResult(final Integer code, final String message, final Object data) {

        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * return success.
     *
     * @return {@linkplain SoulResult}
     */
    public static SoulResult success() {
        return success("");
    }

    /**
     * return success.
     *
     * @param msg msg
     * @return {@linkplain SoulResult}
     */
    public static SoulResult success(final String msg) {
        return success(msg, null);
    }

    /**
     * return success.
     *
     * @param data this is result data.
     * @return {@linkplain SoulResult}
     */
    public static SoulResult success(final Object data) {
        return success(null, data);
    }

    /**
     * return success.
     *
     * @param msg  this ext msg.
     * @param data this is result data.
     * @return {@linkplain SoulResult}
     */
    public static SoulResult success(final String msg, final Object data) {
        return get(CommonErrorCode.SUCCESSFUL, msg, data);
    }

    /**
     * return error .
     *
     * @param msg error msg
     * @return {@linkplain SoulResult}
     */
    public static SoulResult error(final String msg) {
        return error(CommonErrorCode.ERROR, msg);
    }

    /**
     * return error .
     *
     * @param code error code
     * @param msg  error msg
     * @return {@linkplain SoulResult}
     */
    public static SoulResult error(final int code, final String msg) {
        return get(code, msg, null);
    }


    /**
     * return timeout .
     *
     * @param msg error msg
     * @return {@linkplain SoulResult}
     */
    public static SoulResult timeout(final String msg) {
        return error(HttpStatus.REQUEST_TIMEOUT.value(), msg);
    }

    private static SoulResult get(final int code, final String msg, final Object data) {
        return new SoulResult(code, msg, data);
    }
}
