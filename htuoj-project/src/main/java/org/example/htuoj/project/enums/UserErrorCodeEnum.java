/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.htuoj.project.enums;


import org.example.htuoj.common.convention.errorcode.IErrorCode;

/**
 * 用户错误码
 */
public enum UserErrorCodeEnum implements IErrorCode {


    USER_ADD_ERROR("A000200", "用户注册失败,可能是 用户名/学号/邮箱 已被注册"),
    USER_UPDATE_ERROR("A000201", "用户记录修改失败"),
    USER_DELETE_ERROR("A000202", "用户记录删除失败"),
    USER_NOT_EXISTS("A000203", "用户尚未注册"),
    USER_LOGIN_ERROR("A000204", "学号或者密码输入错误！"),
    USER_LOGOUT_ERROR("A000205", "用户登出失败"),
    USER_REGISTER_ERROR("A000206", "用户注册失败"),
    USER_UPDATE_AVATAR_ERROR("A000207", "用户头像更新失败"),
    USER_LOGIN_ERROR_PASSWORD("A000208", "用户登录失败,密码错误"),
    LOGIN_CODE_NOT_EXPIRED("A000209", "验证码仍在有效期内,请使用此前获取的验证码"),
    LOGIN_CODE_EXPIRED("A000210", "验证码已过期,请重新获取验证码"),
    LOGIN_CODE_ERROR("A000211", "验证码错误!!!"),
    USER_EXISTS("A000212", "该学号已存在"),
    USER_MAIL_EXISTS("A000213", "该邮箱已经被注册过了!!!"),
    USER_LOGIN_ERROR_STATE("A000214", "用户未审核,请联系管理员"),
    USER_LOGIN_ERROR_STATE_NOT_PASS("A000215", "用户审核未通过,请联系管理员"),
    USER_UPDATE_USER_INFO_ERROR("A000216", "用户信息修改失败"),
    USER_MAIL_ERROR("A000217", "邮箱发送失败"),
    USER_NAME_EXISTS("A000218", "用户名已存在"),
    USER_NAME_NOT_NULL("A000219", "用户名不能为空");

    private final String code;

    private final String message;

    UserErrorCodeEnum(String code, String message) {
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
