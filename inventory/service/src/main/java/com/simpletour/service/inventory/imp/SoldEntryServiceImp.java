package com.simpletour.service.inventory.imp;

import com.simpletour.biz.inventory.ISoldEntryBiz;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.query.SoldEntryKey;
import com.simpletour.service.inventory.ISoldEntryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
    @Resource
    private ISoldEntryBiz soldEntryBiz;

    @Override
    public Optional<SoldEntry> addSoldEntry(SoldEntry soldEntry) throws BaseSystemException {
        return soldEntryBiz.addSoldEntry(soldEntry);
    }

    @Override
    public List<SoldEntry> addSoldEntries(List<SoldEntry> soldEntries) throws BaseSystemException {
        return soldEntryBiz.addSoldEntries(soldEntries);
    }

    @Override
    public void invalidateSoldEntriesByOrderId(Long oid) {
        soldEntryBiz.invalidateSoldEntriesByOrderId(oid);
    }

    @Override
    public List<SoldEntry> getSoldEntriesByUnionIds(SoldEntryKey soldEntryKey) {
        return soldEntryBiz.getSoldEntriesByUnionIds(soldEntryKey);
    }

    @Override
    public int getAvailableSoldQuantity(SoldEntryKey soldEntryKey, Date day) {
        return soldEntryBiz.getAvailableSoldQuantity(soldEntryKey, day);
    }
}
