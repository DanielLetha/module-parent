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
    AGREEMENT_PRODUCT_NOT_EXIST("0001", "对象不存在"),
    PRODUCT_NOT_EXIST("0002", "产品不存在"),
    PRODUCT_NAME_NOT_CORRECT("0003", "产品名称不正确"),
    AGREEMENT_NOT_EXIST("0004", "销售协议不存在"),
    AGREEMENT_APP_IS_NULL("0005", "销售协议中客户端的为空"),
    AGREEMENT_NAME_NOT_CORRECT("0006", "销售协议名称不正确"),
    MUST_CONTAIN_PRODUCT("0007", "必须包含产品"),
    AGREEMENT_IS_NULL("0008", "销售协议为空");


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
