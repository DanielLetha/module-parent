package com.simpletour.order.biz.error;

import com.simpletour.commons.data.error.IError;

/**
 * Created by Mario on 2016/4/20.
 */
public enum OrderBizError implements IError {

    ORDER_ITEM_NULL("0001", "订单项不能为空"),
    ORDER_ITEM_BASIC_NOT_ENOUGH("0002", "订单项基本信息不完整"),
    ORDER_ITEM_SALEAPP_NULL("0003", "订单项关联的销售端为空或者销售端ID为空"),
    ORDER_ITEM_SALEAPP_NOT_EXISTED("0004", "订单项关联的销售端不存在"),
    ORDER_ITEM_SALEAPP_PRODUCT_NOT_EXISTED("0005", "订单项关联产品不在关联销售端的销售范围内"),
    ORDER_ITEM_SALEAPP_EXCEED_PRODUCT_DEADLINE("0006", "订单下单时间超过产品销售时间"),
    ORDER_ITEM_PRODUCT_NOT_EXISTED("0007","订单项关联产品不存在或者已经下线"),
    ORDER_ITEM_PRODUCT_BUS_NOT_EXSITED("0008","订单项关联的车次不存在或已下线"),
    ORDER_ITEM_PRODUCT_PROCUREMENT_NOT_EXISTED("0009","订单项关联的元素不存在或已下线"),

    ORDER_SYSTEM_EXCEPTION("10000", "程序异常");

    private String errorCode;
    private String errorMessage;

    private static final String ns = "BIZ.ORDER";

    OrderBizError(String errorCode, String errorMessage) {
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
        return this.errorMessage = errorMessage;
    }
}
