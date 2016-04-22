package com.simpletour.biz.inventory.imp;

import com.simpletour.biz.inventory.ISoldEntryBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.inventory.ISoldEntryDao;
import com.simpletour.domain.inventory.SoldEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 文件描述：库存销售记录模块业务层实现类
 * 创建人员：石广路
 * 创建日期：2015/12/3 17:29
 * 备注说明：null
 */
@Component
public class SoldEntryBizImp implements ISoldEntryBiz {
    @Autowired
    private ISoldEntryDao soldEntryDao;

//    @Override
//    public void deleteSoldEntry(SoldEntry soldEntry) {
//        soldEntryDao.remove(soldEntry);
//    }

    @Override
    public Optional<SoldEntry> addSoldEntry(SoldEntry soldEntry) throws BaseSystemException {
        StockParamsValidater.validateBoundParams(soldEntry);

        if (null != soldEntry.getId()) {
            throw new BaseSystemException(InventoryBizError.INVALID_ID);
        }

        return Optional.ofNullable(soldEntryDao.save(soldEntry));
    }

    @Override
    public Optional<SoldEntry> updateSoldEntry(SoldEntry soldEntry) throws BaseSystemException {
        StockParamsValidater.validateBoundParams(soldEntry);

        Long id = soldEntry.getId();
        if (null == id) {
            throw new BaseSystemException(InventoryBizError.INVALID_ID);
        }

        if (null == soldEntryDao.getEntityById(SoldEntry.class, id)) {
            throw new BaseSystemException(InventoryBizError.SOLD_ENTRY_NOT_EXIST);
        }

        return Optional.ofNullable(soldEntryDao.save(soldEntry));
    }

//    @Override
//    public void deleteSoldEntry(SoldEntry soldEntry) {
//        soldEntryDao.remove(soldEntry);
//    }

//    @Override
//    public void deleteSoldEntry(Long id) throws BaseSystemException {
//        Optional<SoldEntry> optional = getSoldEntryById(id);
//        if (!optional.isPresent()) {
//            throw new BaseSystemException(InventoryBizError.SOLD_ENTRY_NOT_EXIST);
//        }
//        soldEntryDao.remove(optional.get());
//    }

//    @Override
//    public void deleteSoldEntries(List<Long> ids) {
//        if (null != ids) {
//            ids.stream().filter(id -> null != id).forEach(id -> deleteSoldEntry(id));
//        }
//    }

//    @Override
//    public Optional<SoldEntry> getSoldEntryById(Long id) {
//        if (null == id || 0 >= id) {
//            throw new BaseSystemException(InventoryBizError.INVALID_ID);
//        }
//        return Optional.ofNullable(soldEntryDao.getEntityById(SoldEntry.class, id));
//    }

    @Override
    public void invalidateSoldEntriesByOrderId(Long oid) {
        if (null != oid && 0 < oid) {
            soldEntryDao.updateSoldEntriesStatusByOrderId(oid, false);
        }
    }

//    @Override
//    public List<SoldEntry> getSoldEntriesByUnionIds(SoldEntryKey soldEntryKey) {
//        return soldEntryDao.getSoldEntriesByUnionKeys(soldEntryKey);
//    }
//
//    @Override
//    public int getAvailableSoldQuantity(SoldEntryKey soldEntryKey, Date day) {
//        if (null == soldEntryKey) {
//            throw new BaseSystemException(InventoryBizError.EMPTY_ENTITY);
//        }
//
//        if (null == day) {
//            throw new BaseSystemException(InventoryBizError.INVALID_DATE_PARAM);
//        }
//
//        InventoryType inventoryType = soldEntryKey.getInventoryType();
//        Long inventoryTypeId = soldEntryKey.getInventoryId();
//        StockParamsValidater.validateStockParam(inventoryType, inventoryTypeId, day);
//
//        // 只查询有效状态的销售库存
//        return soldEntryDao.getSoldQuantity(inventoryType, inventoryTypeId, day, true);
//    }
}
