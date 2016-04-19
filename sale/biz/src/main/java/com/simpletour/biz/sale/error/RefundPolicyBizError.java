package com.simpletour.biz.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 16:10
 */
public enum RefundPolicyBizError implements IError {
    REFUND_POLICY_NULL("0001","退款模板为空"),
    REFUND_POLICY_DATA_ERROR("0002","数据库异常"),
    REFUND_POLICY_NAME_EXIST("0003","退款模板名称已存在"),
    ;



    String errorCode;
    String errorMessage;

    private static final String ns = "BIZ.REFUND_POLICY";

    RefundPolicyBizError(String errorCode, String errorMessage) {
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
