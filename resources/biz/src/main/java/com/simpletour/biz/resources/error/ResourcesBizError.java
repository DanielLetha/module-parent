package com.simpletour.biz.resources.error;


import com.simpletour.commons.data.error.IError;

/**
 * Created by yangdongfeng on 2015/11/21.
 */
public enum ResourcesBizError implements IError {

    EMPTY_ENTITY("0000", "对象为空"),
    DESTINATION_NOT_EXIST("0001", "目的地不存在"),
    AREA_NOT_EXIST("0002", "区域不存在"),
    ENTERTAINMENT_NOT_EXIST("0003", "娱乐不存在"),
    OSP_NOT_EXIST("0004", "供应商不存在"),
    UNDEFINE_ENUM_VALUE("0005", "未定义的枚举类型"),
    DESTINATION_NAME_UNDER_SAME_AREA_MUST_BE_UNIQUE("0006", "同一区域下的目的地不能重名"),
    ENTERTAINMENT_NAME_UNDER_SAME_DESTINATION_MUST_BE_UNIQUE("0007", "同一目的地下的娱乐不能重名"),
    DATA_ERROR_IN_DB("0008", "数据库中数据有错误"),
    PROCUREMENT_DEPEND_ON_ENTERTAINMENT("0009", "元素依赖于娱乐"),

    ILLEGAL_ID("0010", "非法ID"),
    OSP_NAME_MUST_BE_NUIQUE("0011", "供应商名字必须唯一"),
    SAME_NAME_RESOURCE_IS_EXISTING("0012", "资源名已存在"),
    ADD_OSP_DEL("0013","添加供应商时，del为ture"),

    CANNOT_DEL_DEPENDENT_PROCUREMENT("1000", "有行程或产品依赖此元素，不能删除"),
    CANNOT_DEL_DEPENDENT_RESOURCE("1000", "存在元素依赖该资源，不能删除"),
    INVALID_RESOURCE_AND_DESTINATION("1001", "无效的资源信息和目的地"),
    CANNOT_DEPEND_SAME_RESOURCE("1000", "不能依赖自己"),
    RESOURCE_IS_EXISTING("1002", "资源已经存在"),
    NOT_EXIST_RESOURCE("1003", "不存在的资源"),
    INVALID_COORDINATE("1004", "无效的经纬度坐标"),
    SCENIC_NULL("1005", "景点实体为NULL"),
    ADD_SCENIC_DEL("1006", "增加景点时，del为true"),
    SCENIC_NAME_REPAET_ON_DESTINATION("1007", "同一个目的地下的景点名不能重复"),


    CATERING_NOT_EXIST("1008","餐饮点不存在"),
    CATERING_NAME_MUST_BE_NUIQUE("1009","同一个目的地下的餐饮点名不能重复"),
    ADD_CATERING_DEL("1010","添加餐饮点时，del为ture"),

    PROCUREMENT_NULL("1011", "元素为空"),
    PROCUREMENT_ADD_AND_DEL("1012", "元素增加时，del为true"),
    PROCUREMENT_RESOURCE_ERROR("1013", "元素所对应的资源出现异常"),
    PROCUREMENT_RESOURCE_DESTINATION_NOMATCH("1014", "元素与资源所对应的目的地不一致"),
    PROCUREMENT_NAME_REPEAT_ON_ONE_DESTINATION("1015", "同一个目的地下元素名不能重名"),
    PROCUREMENT_NOT_EXIST("1016", "元素不存在"),
    PROCUREMENT_IS_OFFLINE("1017", "元素已下线"),

    SCENIC_UPDATE_VERSION_NULL("1018", "更新景点时，Version为空"),
    PROCUREMENT_UPDATE_VERSION_NULL("1019", "更新元素时，Version为空"),
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "BIZ.TR";

    ResourcesBizError(String errorCode, String errorMessage) {
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
