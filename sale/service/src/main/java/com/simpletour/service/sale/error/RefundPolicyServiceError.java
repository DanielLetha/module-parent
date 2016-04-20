package com.simpletour.service.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 17:25
 */
public enum RefundPolicyServiceError implements IError {
    REFUND_POLICY_NULL("0001","退款模板为空"),
    REFUND_POLICY_NOT_EXIST("0002","退款模板不存在"),
    REFUND_POLICY_REFUND_RULE_NULL("0003","退款模板退款规则为空"),
    REFUND_POLICY_REFUND_RULE_NOT_EXIST("0004","退款模板退款规则不存在"),
    REFUND_POLICY_REFUND_RULE_ERROR("0005","退款规则时间范围应完全闭合且不重叠"),
    REFUND_POLICY_DATA_ERROR("0006","退款模板数据异常"),
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "SYS.REFUND_POLICY";

    RefundPolicyServiceError(String errorCode, String errorMessage) {
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
