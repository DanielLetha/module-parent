package com.simpletour.biz.inventory.error;


import com.simpletour.commons.data.error.IError;

/**
 * 文件描述：库存业务层处理失败错误信息
 * 创建人员：石广路
 * 创建日期：2015/11/25 10:57
 * 备注说明：null
 */
public enum InventoryBizError implements IError {
    EMPTY_ENTITY("0000", "对象为空"),
    INVALID_ID("0001", "无效的ID"),

    /**
     * note: add some error descriptions for the procurement
     * author: shiguanglu
     * date: 2015-11-26 11:28
     */
    STOCK_IS_EXISTING("0002", "库存已存在"),
    STOCK_NOT_EXIST("0003", "库存不存在"),
    STOCK_DEPENDENCY_NOT_EXIST("0004", "库存依赖不存在"),
    INVALID_STOCK_PARAM("0005", "无效的库存参数信息"),
    INVALID_DATE_SLOT("0006", "无效的库存日期时间段"),
    INVALID_DATE_PARAM("0007", "无效的库存日期参数"),
    INVALID_STOCK_QUANTITY("0008", "设置的库存数量不能小于有效状态的已销售库存数量"),
    STOCK_SOLD_OUT("0009", "库存已售罄"),
    DEPENDENT_STOCK_SHORTAGE("0010", "依赖库存量已不足"),
    GET_STOCK_PRICE_FAILED("0011", "获取库存价格失败，请检查查询参数是否正确，以及库存是否还存在"),

    STOCK_ADD_FAILED("0100", "库存基本信息添加失败"),
    STOCK_UPDATE_FAILED("0100", "库存基本信息更新失败"),

    PRICE_IS_EMPTY("1000", "库存价格信息不能为空"),
    PRICE_IS_EXISTING("1001", "库存价格信息已存在"),
    PRICE_NOT_EXIST("1002", "库存价格信息不存在"),
    PRICE_ADD_FAILED("1003", "库存价格信息添加失败"),
    PRICE_UPDATE_FAILED("1003", "库存价格信息更新失败"),

    SOLD_ENTRY_IS_EXISTING("2001", "库存销售记录信息已存在"),
    SOLD_ENTRY_NOT_EXIST("2002", "库存销售记录信息不存在"),
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "BIZ.INVENTORY";

    InventoryBizError(String errorCode, String errorMessage) {
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
