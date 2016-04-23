package com.simpletour.biz.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * @Brief :  ${用途}
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/20 17:43
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public enum AgreementProductPriceBizError implements IError {

    AGREEMENT_PRODUCT_PRICE_EMPTY("0000", "对象为空"),
    AGREEMENT_PRODUCT_PRICE_EXIST("0001", "该价格已经存在"),
    AGREEMENT_PRODUCT_NULL("0002","销售协议产品为空"),
    AGREEMENT_PRODUCT_PRICE_MULTITERM("0003","销售协议产品价格有多条"),

    ;

    String errorCode;

    String errorMessage;

    private static final String ns = "BIZ.AGREEMENT_PRODUCT_PRICE";

    AgreementProductPriceBizError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

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
