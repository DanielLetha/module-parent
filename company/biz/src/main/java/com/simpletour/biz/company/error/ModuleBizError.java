package com.simpletour.biz.company.error;

import com.simpletour.common.core.error.IError;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/9
 * Time: 10:13
 */
public enum ModuleBizError implements IError{
    MODULE_NULL("0001","模块为空"),
    MODULE_PERMISSION_NULL("0002","功能列表为空"),
    MODULE_PERMISSION_REPEAT("0003","功能列表项重复，功能名称和代码不能重复"),
    MODULE_PERMISSION_NOT_AVAILABLE("0004","功能不可用，存在重复代码"),
    MODULE_NOT_EXIST("0005","模块不存在"),
    MODULE_NAME_EXIST("0006","模块名称已存在"),
    MODULE_PERMISSION_NOT_EXIST("0007","功能不存在"),
    MODULE_REPEAT("0008","模块重复"),
    ;


    String errorCode;
    String errorMessage;

    private static final String ns = "BIZ.MODULE";

    ModuleBizError(String errorCode, String errorMessage) {
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
