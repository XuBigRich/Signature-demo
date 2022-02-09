package com.sign.signaturedemo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 刘雪
 * @since 2021/12/27 15:15:52
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS(200, "处理成功"),

    VALIDATION_FAILS(401, "签名验证失败"),
    //
    FAIL(100, "处理失败");



    private final Integer code;

    private final String msg;
}
