package com.simpletour.biz.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
public enum  AgreementProductBizError implements IError {
   AGREEMENT_PRODUCT_EMPTY("0000", "对象为空"),
   AGREEMENT_PRODUCT_NOT_EXIST("0001", "对象不存在");

    String errorCode;

    String errorMessage;

    private static final String ns = "BIZ.AGREEMENT_PRODUCT";

    AgreementProductBizError(String errorMessage, String errorCode) {
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
