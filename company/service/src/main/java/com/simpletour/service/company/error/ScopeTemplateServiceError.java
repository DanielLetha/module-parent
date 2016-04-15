package com.simpletour.service.company.error;

import com.simpletour.commons.data.error.IError;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/8
 * Time: 14:09
 */
public enum ScopeTemplateServiceError implements IError {
    SCOPE_TEMPLATE_NULL("0001","权限范围为空"),
    SCOPE_TEMPLATE_PERMISSION_NULL("0002","功能列表为空"),
    SCOPE_TEMPLATE_NOT_EXIST("0003","权限范围不存在"),
    SCOPE_TEMPLATE_NAME_EXIST("0004","权限范围名称已存在"),
    SCOPE_TEMPLATE_DATA_ERROR("0005","权限范围数据异常"),
    SCOPE_TEMPLATE_PERMISSION_NOT_EXIST("0006","功能不存在");
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "SERVICE.SCOPE";

    ScopeTemplateServiceError(String errorCode, String errorMessage) {
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
