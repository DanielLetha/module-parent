package com.simpletour.service.order.error;

import com.simpletour.commons.data.error.IError;

/**
 * Created by Mario on 2016/4/21.
 */
public enum OrderServiceError implements IError {

    ORDER_NULL("0001", "订单对象实体不能为空"),
    ORDER_SALEAPP_NULL("0002", "订单关联销售端对象实体或者实体ID不能为空"),
    ORDER_SALEAPP_NOT_EXISTED("0003", "订单关联销售端不存在或已被删除"),
    ORDER_ITEM_NULL("0004", "订单应至少包含一个订单项"),


    ORDER_SYSTEM_EXCEPTION("10000", "程序异常");

    private String errorCode;
    private String errorMessage;

    private static final String ns = "SERVICE.ORDER";

    OrderServiceError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
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
        return this.errorMessage;
    }
}
