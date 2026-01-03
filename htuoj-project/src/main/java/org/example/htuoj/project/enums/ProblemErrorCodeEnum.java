package org.example.htuoj.project.enums;


import org.example.htuoj.common.convention.errorcode.IErrorCode;

public enum ProblemErrorCodeEnum implements IErrorCode {

    Problem_SAVE_ERROR("B000201", "题目新增失败"),
    Problem_UPDATE_ERROR("B000202", "题目修改失败"),
    Problem_DELETE_ERROR("B000203", "题目删除失败"),
    Problem_NOT_FOUND( "B000204", "题目不存在");
    private final String code;

    private final String message;

    ProblemErrorCodeEnum(String code, String message) {
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
