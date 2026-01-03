package org.example.htuoj.project.enums;


import org.example.htuoj.common.convention.errorcode.IErrorCode;

public enum ArticleErrorCodeEnum implements IErrorCode {
    ARTICLE_ADD_ERROR("D000201", "文章发布失败"),
    ARTICLE_DELETE_ERROR("D000202", "文章删除失败"),
    ;
    private final String code;

    private final String message;

    ArticleErrorCodeEnum(String code, String message) {
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
