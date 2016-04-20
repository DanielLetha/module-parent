package com.simpletour.service.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
public enum  AgreementProductServiceError implements IError {

    AGREEMENT_PRODUCT_EMPTY("0000", "对象为空"),
    AGREEMENT_PRODUCT_NOT_EXIST("0001", "对象不存在");

    String errorCode;

    String errorMessage;

    private static final String ns = "SER.AGREEMENT_PRODUCT";

    AgreementProductServiceError(String errorMessage, String errorCode) {
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
