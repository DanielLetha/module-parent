package com.simpletour.biz.sale.error;

import com.simpletour.commons.data.error.IError;

/**
 * @Brief :  销售端的错误类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/7 9:41
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public enum SaleAppBizError implements IError {
    SALE_APP_EMPTY("0000","对象为空"),
    SAL_APP_DEL("0001","添加已经删除的对象"),
    SALE_APP_NOT_EXIST("0002","对象不存在"),
    SALE_APP_NAME_MUST_BE_UNIQUE("0003","销售端名称必须唯一"),
    SALE_APP_KEY_MUST_NOT_NULL("0004","销售端的key不能为空"),
    SALE_APP_KEY_MUST_BE_UNIQUE("0005","销售端的key必须唯一"),
    SALE_APP_SECRET_MUST_NOT_NULL("0006","销售端的secret不能为空"),
    SALE_APP_SECRET_MUST_BE_UNIQUE("0007","销售端的secret必须唯一"),
    SALE_APP_KEY_SECRET_NOT_SAME("0008","销售端的key、secret和原数据不一致"),

    ;

    String errorCode;

    String errorMessage;

    private static final String ns = "BIZ.SA";

    SaleAppBizError(String errorCode,String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    @Override
    public String getNamespace() {
        return ns;
    }

    @Override
    public String getErrorCode() {
        return ns+"."+errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
