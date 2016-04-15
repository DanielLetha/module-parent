package com.simpletour.service.product.error;


import com.simpletour.commons.data.error.IError;

/**
 * Created by songfujie on 15/10/24.
 */
public enum ProductServiceError implements IError {

    EMPTY_ENTITY("0000", "对象为空"),
    NAME_EXIST("0001", "名字已经存在"),
    PRODUCT_NOT_COTAIN_BUS("0002", "产品中没有行程"),
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "SER.PRODUCT";

    ProductServiceError(String errorCode, String errorMessage) {
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
