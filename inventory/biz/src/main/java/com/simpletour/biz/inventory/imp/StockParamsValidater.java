package com.simpletour.biz.inventory.imp;

import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.StockBound;

import java.util.Date;

/**
 * 文件描述：库存参数校验器
 * 创建人员：石广路
 * 创建日期：2015/12/3 17:32
 * 备注说明：仅对库存模块使用，不需要对外提供接口
 */
final class StockParamsValidater {
    /**
     * 功能：检查库存基本边界信息是否有效
     * 作者：石广路
     * 新增：2016-4-20
     * 备注：如果库存价格信息无效，则抛出BaseSystemException异常
     *
     * @param stockBound 库存价格实体
     *
     * return void
     */
    static void validateBoundParams(StockBound stockBound) throws BaseSystemException {
        if (null == stockBound) {
            throw new BaseSystemException(InventoryBizError.EMPTY_ENTITY);
        }

        InventoryType inventoryType = stockBound.getInventoryType();
        Long inventoryId = stockBound.getInventoryId();
        Date day = stockBound.getDay();
        if (null == inventoryType || null == inventoryId || 0 >= inventoryId || null == day) {
            throw new BaseSystemException(InventoryBizError.INVALID_STOCK_PARAM);
        }
    }

    static void validateStockParam(InventoryType inventoryType, Long inventoryTypeId, Date day) throws BaseSystemException {
        if (null == inventoryType || null == inventoryTypeId || 0 >= inventoryTypeId || null == day) {
            throw new BaseSystemException(InventoryBizError.INVALID_STOCK_PARAM);
        }
    }

    static void validateStockParam(InventoryType inventoryType, Long inventoryTypeId, Date startDate, Date endDate) throws BaseSystemException {
        if (null == inventoryType || null == inventoryTypeId || 0 >= inventoryTypeId) {
            throw new BaseSystemException(InventoryBizError.INVALID_STOCK_PARAM);
        }
        if ((null == startDate && null == endDate) || (null != startDate && null != endDate && startDate.after(endDate))) {
            throw new BaseSystemException(InventoryBizError.INVALID_DATE_SLOT);
        }
    }
}
