package org.dromara.soul.common.utils;

import org.dromara.soul.common.constant.Constants;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class SignUtils {
    private static final SignUtils SIGN_UTILS = new SignUtils();

    private SignUtils() {

    }

    /**
     * getInstance.
     *
     * @return {@linkplain SignUtils}
     */
    public static SignUtils getInstance() {
        return SIGN_UTILS;
    }

    /**
     * acquired sign.
     *
     * @param signKey sign key
     * @param params params
     * @return sign
     */
    private static String generateSign(final String signKey, final Map<String, String> params) {
        List<String> storedKeys = Arrays.stream(params.keySet()
                .toArray(new String[]{}))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        final String sign = storedKeys.stream()
                .filter(key -> !Objects.equals(key, Constants.SIGN))
                .map(key -> String.join("", key, params.get(key)))
                .collect(Collectors.joining()).trim()
                .concat(signKey);
        return DigestUtils.md5DigestAsHex(sign.getBytes()).toUpperCase();
    }

    /**
     * isValid.
     *
     * @param sign sign
     * @param params params
     * @param signKey signKey
     * @return boolean
     */
    public boolean isValid(final String sign, final Map<String, String> params, final String signKey) {
        return Objects.equals(sign, generateSign(signKey, params));
    }
}
