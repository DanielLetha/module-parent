package com.simpletour.biz.company.error;

import com.simpletour.common.core.error.IError;

/**
 * User: Hawk
 * Date: 2016/4/8 - 17:19
 */
public enum CompanyBizError implements IError {
    COMPANY_NULL("0000", "公司对象为空"),
    COMPANY_DEL("0001", "公司对象已经被删除"),
    COMPANY_NAME_REPEAT("0002", "公司名字重复"),
    COMPANY_ID_ERROR("0003", "公司ID为空"),
    EMPLOYEE_DEPEND_ON_COMPANY("0004", "员工依赖公司"),
    COMPANY_UPDATE_VERSION_NULL("0005", "更新公司时，公司Version为空"),
    COMPANY_QUERY_CONDITION_NULL("0006", "公司查询时条件为NULL"),
    ;

    String errorCode;
    String errorMessage;
    private static final String ns = "SER.COMPANY";

    CompanyBizError(String errorCode, String errorMessage) {
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
