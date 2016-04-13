package com.simpletour.service.company.error;

import com.simpletour.common.core.error.IError;

/**
 * 超级管理员服务层的错误
 * User: Hawk
 * Date: 2016/4/7 - 10:02
 */
public enum  AdministratorServiceError implements IError {

    EMPTY_ENTITY("0000", "实体为空"),
    ADMIN_NOT_EXIST("0001", "管理员不存在"),
    PASSWORD_SAME_OLD("0002", "更新后的密码与之前的密码一致"),
    ;

    private String errorCode;
    private String errorMessage;
    private static final String ns = "SER.ADMINISTRATOR";

    AdministratorServiceError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getNamespace() {
        return ns;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
