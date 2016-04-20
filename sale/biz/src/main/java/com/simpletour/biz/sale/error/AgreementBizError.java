package com.simpletour.biz.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * 销售协议Biz错误类
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/19.
 *
 * @since 2.0-SNAPSHOT
 */
public enum AgreementBizError implements IError {
    AGREEMENT_EMPTY("0000", "对象为空"),
    AGREEMENT_NOT_EXIST("0001", "对象不存在"),
    AGREEMENT_APP_NULL("0002", "销售协议销售端为空"),
    AGREEMENT_APP_EXISTED("0003", "销售协议销售端已存在"),;

    String errorCode;

    String errorMessage;

    private static final String ns = "BIZ.AGREEMENT";

    AgreementBizError(String errorMessage, String errorCode) {
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
