package com.simpletour.biz.inventory.imp;

import com.simpletour.biz.inventory.ISoldEntryBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.inventory.ISoldEntryDao;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.query.SoldEntryKey;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：资源模块业务处理实现类
 * 创建人员：石广路
 * 创建日期：2015/12/3 17:29
 * 备注说明：null
 */
@Component
public class SoldEntryBizImp implements ISoldEntryBiz {
    @Resource
    private ISoldEntryDao soldEntryDao;

    @Override
    public Optional<SoldEntry> addSoldEntry(final SoldEntry soldEntry) throws BaseSystemException {
        if (null == soldEntry) {
            throw new BaseSystemException(InventoryBizError.EMPTY_ENTITY);
        }

        StockParamsValidater.validateStockParam(soldEntry.getInventoryType(), soldEntry.getInventoryTypeId(), soldEntry.getDay());

        Optional<SoldEntry> soldEntryOptional = Optional.ofNullable(soldEntryDao.save(soldEntry));
        return soldEntryOptional;
    }

    @Override
    public List<SoldEntry> addSoldEntries(final List<SoldEntry> soldEntries) throws BaseSystemException {
        if (null != soldEntries) {
            List<SoldEntry> soldsList = new ArrayList<>(soldEntries.size());
            soldEntries.forEach(soldEntry -> soldsList.add(addSoldEntry(soldEntry).get()));
            return soldsList;
        }
        return soldEntries;
    }

    @Override
    public void invalidateSoldEntriesByOrderId(Long oid) {
        if (null != oid && 0 < oid) {
            soldEntryDao.updateSoldEntriesStatusByOrderId(oid, false);
        }
    }

    @Override
    public List<SoldEntry> getSoldEntriesByUnionIds(SoldEntryKey soldEntryKey) {
        return soldEntryDao.getSoldEntriesByUnionKeys(soldEntryKey);
    }

    @Override
    public int getAvailableSoldQuantity(SoldEntryKey soldEntryKey, Date day) {
        if (null == soldEntryKey) {
            throw new BaseSystemException(InventoryBizError.EMPTY_ENTITY);
        }

        if (null == day) {
            throw new BaseSystemException(InventoryBizError.INVALID_DATE_PARAM);
        }

        InventoryType inventoryType = soldEntryKey.getInventoryType();
        Long inventoryTypeId = soldEntryKey.getInventoryTypeId();
        StockParamsValidater.validateStockParam(inventoryType, inventoryTypeId, day);

        // 只查询有效状态的销售库存
        return soldEntryDao.getSoldQuantity(inventoryType, inventoryTypeId, day, true);
    }
}
