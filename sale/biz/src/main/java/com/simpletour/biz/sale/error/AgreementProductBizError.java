package com.simpletour.biz.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
public enum  AgreementProductBizError implements IError {
    AGREEMENT_PRODUCT_IS_NULL("0000", "对象为空"),
    PRODUCT_OR_AGREEMENT_IS_NULL("0001", "产品或者销售协议为空"),
    PRODUCT_ID_OR_AGREEMENT_ID_IS_NULL("0002", "产品id或者销售协议id为空"),
    PRODUCT_MUST_BE_UNIQUE_IN_ONE_AGREEMENT("0003", "一个销售协议下产品必须唯一"),
    AGREEMENT_PRODUCT_NOT_EXIST("0004", "对象不存在"),
    ID_IS_ERROR("0005", "id不合法"),
    PRODUCT_REFUND_RULE_IS_EMPTY("0006", "产品退款细则为空"),
    REFUND_POLICY_REFUND_RULE_ERROR("0005","退款规则时间范围应完全闭合且不重叠"),
    REFUND_POLICY_REFUND_RULE_NOT_EXIST("0007","退款模板退款规则不存在"),
    CONDITION_QUERY_IS_NULL("0008","查询条件query为空");


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
