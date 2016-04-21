package com.simpletour.service.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * @Brief :  ${用途}
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/21 10:52
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public enum AgreementProductPriceServiceError implements IError {

    AGREEMENT_PRODUCT_PRICE_NULL("0000", "销售价格为空"),
    AGREEMENT_PRODUCT_PRICE_EXIST("0001", "销售价格已经存在"),
   ;
    String errorCode;
    String errorMessage;

    AgreementProductPriceServiceError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    private static final String ns = "SER.AGREEMENT_PRODUCT_PRICE";

    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    public String getErrorCode() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
