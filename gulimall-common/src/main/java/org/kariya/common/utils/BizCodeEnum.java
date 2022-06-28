package org.kariya.common.utils;

import lombok.Getter;

@Getter
public enum BizCodeEnum {
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    UNKNOW_EXCEPTION(10000, "系统位置异常");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
