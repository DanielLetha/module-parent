package com.simpletour.service.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * 销售协议Service错误类
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/19.
 *
 * @since 2.0-SNAPSHOT
 */
public enum AgreementServiceError implements IError {
    AGREEMENT_EMPTY("0000", "销售协议不能为空"),
    AGREEMENT_NOT_EXIST("0001", "销售协议不存在"),
    AGREEMENT_APP_NOT_EXIST("0002", "销售端不存在"),
    AGREEMENT_STATUS_ENABLED("0003", "销售端已启用"),
    AGREEMENT_STATUS_DISABLED("0004", "销售端已禁用"),;

    String errorCode;

    String errorMessage;

    private static final String ns = "SER.AGREEMENT";

    AgreementServiceError(String errorCode, String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    @Override
    public String getNamespace() {
        return ns;
    }

    @Override
    public String getErrorCode() {
        return ns + "." + errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
