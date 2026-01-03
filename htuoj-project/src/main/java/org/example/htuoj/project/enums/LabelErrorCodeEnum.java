package org.example.htuoj.project.enums;


import org.example.htuoj.common.convention.errorcode.IErrorCode;

public enum LabelErrorCodeEnum implements IErrorCode {
    LABEL_SAVE_ERROR("C000200", "标签新增失败"),
    ;
    private final String code;

    private final String message;

    LabelErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
