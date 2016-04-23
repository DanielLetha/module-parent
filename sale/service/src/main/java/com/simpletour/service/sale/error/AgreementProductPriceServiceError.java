package com.simpletour.service.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * @Brief :  销售产品价格服务的错误类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/21 10:52
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public enum AgreementProductPriceServiceError implements IError {

    AGREEMENT_PRODUCT_PRICE_NULL("0000", "销售价格为空"),
    AGREEMENT_PRODUCT_PRICE_EXIST("0001", "销售价格已经存在"),
    AGREEMENT_PRODUCT_PRICE_MUST_ONLY("0002", "同一天的销售价格唯一"),
    AGREEMENT_AGREEMENT_NOT_EXIST_NOT_EXIST("0003", "销售协议价格不存在"),
    AGREEMENT_PRODUCT_NOT_EXIST("0004", "销售协议产品不存在"),
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
