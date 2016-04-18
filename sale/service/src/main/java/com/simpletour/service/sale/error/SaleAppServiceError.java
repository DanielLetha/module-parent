package com.simpletour.service.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * @Brief : 销售端服务的错误类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/8 10:56
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */

public enum SaleAppServiceError implements IError {

    SALE_APP_EMPTY("0000", "销售端实体为空"),
    SALE_APP_DEL("0001", "销售端已经被删除"),
    SALE_APP_ID_NULL("0002", "主键为空"),
    SALE_APP_NOT_EXIST("0003", "销售端不已存在"),
    SALE_APP_NAME_MUST_BE_UNIQUE("0004", "销售端名称必须唯一")
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "SER.SALEAPP";

    SaleAppServiceError(String errorCode, String errorMessage) {
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
        return errorMessage;
    }
}
