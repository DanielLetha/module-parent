package com.simpletour.service.resources.error;


import com.simpletour.commons.data.error.IError;

/**
 * Created by songfujie on 15/10/24.
 */
public enum ResourcesServiceError implements IError {

    EMPTY_ENTITY("0000", "空对象"),
    NAME_EXIST("0001", "名字相同"),
    ILLEGAL_LONGITUDE("0002", "非法经度"),
    ILLEGAL_LATITUDE("0003", "非法纬度"),
    DESTINATION_NOT_EXIST("0004", "目的地不存在"),
    ILLEGAL_ID("0005", "非法id"),
    DESTINATION_NULL("0006", "目的地为空"),
    DESTINATION_DELETED("0007", "目的地已经被删除"),
    SCENIC_NOT_EXIST("0008", "景点不存在"),
    CANNOT_DEL_DEPENDENT_RESOURCE("0009", "存在元素依赖该资源，不能删除"),
    SCENIC_ID_ERROR("0010", "景点的ID异常"),
    SCENIC_PARENT_ID_NULL("0011", "父景点ID为NULL"),
    SCENIC_PARENT_NOT_EXIST("0012", "父景点不存在"),
    AREA_NOT_EXIST("0013", "Area not exist"),

    PROCUREMENT_RESOURCE_NOT_EXIST("0014", "元素所对应的资源不存在"),
    PROCUREMENT_NOT_EXIST("0015", "元素不存在"),
    PROCUREMENT_OSP_NOT_EXIST("0016", "供应商不存在"),
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "SER.TR";

    ResourcesServiceError(String errorCode, String errorMessage) {
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
