package com.simpletour.biz.product.error;


import com.simpletour.commons.data.error.IError;

/**
 * Created by yangdongfeng on 2015/11/25.
 */
public enum ProductBizError implements IError {
    EMPTY_ENTITY("0000", "对象为空"),
    ADD_ENTITY_TO_DB_FAIL("0001", "对象添加到数据库失败"),
    TIME_FORMAT_ERROR("0002", "时间格式错误"),
    DEPART_TIME_IS_AFTER_ARRIVE_TIME("0003", "出发时间晚于到达时间"),
    PROCUREMENT_NOT_EXIST("0004", "元素不存在"),
    PRODUCT_NOT_EXIST("0005", "产品不存在"),
    PRODUCTPACKAGE_NOT_EXIST("0006", "产品包不存在"),
    CALCULAT_TOURISM_MAX_DAY_ERROR("0007", "计算行程天数错误"),
    BUS_NO_NOT_EXIST("0008", "车次不存在"),
    NODE_NOT_EXIST("0009", "Node节点不存在"),
    TOURISM_NOT_EXIST("0010", "行程不存在"),
    DECOMPOSE_ROUTE_TO_LINE_FAIL("0011", "行程分解到线路失败"),
    PRODUCT_NOT_ENOUGH("0012", "产品不足"),
    PROCUREMENT_NOT_ENOUGH("0013", "元素不足"),
    PRODUCT_NAME_EXIST("0014", "产品名字已存在"),
    TOURISM_NAME_EXIST("0015", "行程名字已存在"),
    DATA_ERROR_IN_DB("0016", "数据库中数据有错"),
    TOURISM_ROUTES_DAY_MUST_BE_NON_DECREASING("0017", "行程中天数必须为非递减"),
    TOURISM_ROUTES_BUS_NO_DUPLICATE("0018", "行程中车次冲突"),
    TOURISM_ROUTES_BUS_NO_TIME_CONFLICT("0019", "行程中车次时间冲突"),
    PRODUCT_PACKAGES_DAY_MUST_BE_NON_DECREASING("0020", "产品包中天数必须为非递减"),
    PROCUREMENT_STOCK_NOT_EXIST("0021", "元素库存不存在"),
    TOURISM_ROUTES_IS_EMPTY("0022", "行程线路为空"),
    TOURISM_ROUTES_BUS_NO_PLAN_NOT_EXIST("0023", "行程线路车次排班不存在"),
    STOCK_NOT_EXIST("0024", "库存不存在"),
    PROCUREMENT_HAS_BEEN_DELETED("0025", "元素已经被删除"),
    PROCUREMENT_IS_OFFLINE("0026", "元素已经下线"),
    BUS_NO_HAS_BEEN_STOPED("0027", "车次已经停运"),
    PRODUCT_TYPE_DIS_MATCH("0028", "产品类型不匹配"),
    PRODUCT_MUST_CONTAINS_ATLEAST_ONE_BUS_OR_PROCUREMENT("0029", "产品至少包含一个车次或者元素")
    ;


    String errorCode;
    String errorMessage;
    private static final String ns = "BIZ.PRODUCT";

    private ProductBizError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getNamespace() {
        return ns;
    }

    public String getErrorCode() {
        return ns + "." + this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
