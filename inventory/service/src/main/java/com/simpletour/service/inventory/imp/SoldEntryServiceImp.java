package com.simpletour.service.inventory.imp;

import com.simpletour.biz.inventory.ISoldEntryBiz;
import com.simpletour.biz.inventory.IStockQueryBiz;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.service.inventory.ISoldEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：销售库存模块服务层处理实现类
 * 创建人员：石广路
 * 创建日期：2015/12/3 18:03
 * 备注说明：null
 */
@Service
public class SoldEntryServiceImp implements ISoldEntryService {
    @Autowired
    private IStockQueryBiz stockQueryBiz;

    @Autowired
    private ISoldEntryBiz soldEntryBiz;

    @Override
    public Optional<SoldEntry> addSoldEntry(SoldEntry soldEntry) throws BaseSystemException {
        return soldEntryBiz.addSoldEntry(soldEntry);
    }

    @Override
    public List<SoldEntry> addSoldEntries(List<SoldEntry> soldEntries) throws BaseSystemException {
        if (null != soldEntries && !soldEntries.isEmpty()) {
            List<SoldEntry> soldEntriesList = new ArrayList<>(soldEntries.size());
            soldEntries.forEach(soldEntry -> soldEntriesList.add(soldEntryBiz.addSoldEntry(soldEntry).get()));
            return soldEntriesList;
        }
        return soldEntries;
    }

    @Override
    public Optional<SoldEntry> updateSoldEntry(SoldEntry soldEntry) throws BaseSystemException {
        return soldEntryBiz.updateSoldEntry(soldEntry);
    }

    @Override
    public List<SoldEntry> updateSoldEntries(List<SoldEntry> soldEntries) throws BaseSystemException {
        if (null != soldEntries && !soldEntries.isEmpty()) {
            List<SoldEntry> soldEntriesList = new ArrayList<>(soldEntries.size());
            soldEntries.forEach(soldEntry -> soldEntriesList.add(updateSoldEntry(soldEntry).get()));
            return soldEntriesList;
        }
        return soldEntries;
    }

//    @Override
//    public void deleteSoldEntry(Long id) throws BaseSystemException {
//        //soldEntryBiz.deleteSoldEntry(id);
//
//        Optional<SoldEntry> optional = stockQueryBiz.getSoldEntryById(id);
//        if (!optional.isPresent()) {
//            throw new BaseSystemException(InventoryBizError.SOLD_ENTRY_NOT_EXIST);
//        }
//        soldEntryBiz.deleteSoldEntry(optional.get());
//    }
//
//    @Override
//    public void deleteSoldEntries(List<Long> ids) {
//        if (null != ids) {
//            ids.stream().filter(id -> null != id && 0 < id).forEach(id -> deleteSoldEntry(id));
//        }
//    }

    @Override
    public void invalidateSoldEntriesByOrderId(Long oid) {
        soldEntryBiz.invalidateSoldEntriesByOrderId(oid);
    }

//    @Override
//    public List<SoldEntry> getSoldEntriesByUnionIds(SoldEntryKey soldEntryKey) {
//        return stockQueryBiz.getSoldEntriesByUnionIds(soldEntryKey);
//    }
//
//    @Override
//    public int getAvailableSoldQuantity(SoldEntryKey soldEntryKey, Date day) {
//        return stockQueryBiz.getAvailableSoldQuantity(soldEntryKey, day);
//    }
}
