package com.simpletour.biz.inventory.imp;

import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.domain.inventory.InventoryType;

import java.util.Date;

/**
 * 文件描述：库存参数校验器
 * 创建人员：石广路
 * 创建日期：2015/12/3 17:32
 * 备注说明：仅对库存模块使用，不需要对外提供接口
 */
public class StockParamsValidater {
    public static void validateStockParam(InventoryType inventoryType, Long inventoryTypeId, Date day) throws BaseSystemException {
        if (null == inventoryType || null == inventoryTypeId || 0 >= inventoryTypeId || null == day) {
            throw new BaseSystemException(InventoryBizError.INVALID_STOCK_PARAM);
        }
    }

    public static void validateStockParam(InventoryType inventoryType, Long inventoryTypeId, Date startDate, Date endDate) throws BaseSystemException {
        if (null == inventoryType || null == inventoryTypeId || 0 >= inventoryTypeId) {
            throw new BaseSystemException(InventoryBizError.INVALID_STOCK_PARAM);
        }
        if ((null == startDate && null == endDate) || (null != startDate && null != endDate && startDate.after(endDate))) {
            throw new BaseSystemException(InventoryBizError.INVALID_DATE_SLOT);
        }
    }
}
