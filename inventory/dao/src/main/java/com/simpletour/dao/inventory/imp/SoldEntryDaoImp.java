package com.simpletour.dao.inventory.imp;

import com.simpletour.commons.data.dao.jpa.JPABaseDAO;
import com.simpletour.dao.inventory.ISoldEntryDao;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.query.SoldEntryKey;
import com.simpletour.domain.inventory.query.StockKey;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 文件描述：销售库存实现类
 * 创建人员：石广路
 * 创建日期：2015/11/27 16:15
 * 备注说明：null
 */
@Repository
public class SoldEntryDaoImp extends JPABaseDAO implements ISoldEntryDao {
    @Override
    @Transactional
    public void updateSoldEntriesStatusByOrderId(Long itemId, Boolean valid) {
        String sql = "update " + SoldEntry.class.getName() + " c set c.valid = ?1 where c.itemId = ?2 and c.valid != ?3";
        em.createQuery(sql).setParameter(1, valid).setParameter(2, itemId).setParameter(3, valid).executeUpdate();
    }

    @Override
    public List<SoldEntry> getSoldEntriesByUnionKeys(SoldEntryKey soldEntryKey, Date day) {
        int index = 1;
        Map<Integer, Object> values = new HashMap<>(2);
        StringBuilder sb = new StringBuilder("select c from " + SoldEntry.class.getName() + " c where 1 = 1");
        InventoryType inventoryType = soldEntryKey.getInventoryType();
        Long inventoryId = soldEntryKey.getInventoryId();
        Long itemId = soldEntryKey.getItemId();
        Long parentId = soldEntryKey.getParentId();

        if (null != inventoryType) {
            values.put(index, inventoryType);
            sb.append(" and c.inventoryType = ?" + (index++));
        }

        if (null != inventoryId && 0 < inventoryId) {
            sb.append(" and c.inventoryId = " + inventoryId);
        }

        if (null != day) {
            Date date = (Date)day.clone();
            int offset = soldEntryKey.getOffset();
            if (0 < offset) {
                date = Date.from(day.toInstant().plus(offset, ChronoUnit.DAYS));
            }
            values.put(index, date);
            sb.append(" and c.day <= ?").append(index++);
        }

        if (null != itemId && 0 < itemId) {
            sb.append(" and c.itemId = " + itemId);
        }

        if (null != parentId && 0 <= parentId) {
            sb.append(" and c.parentId = ?" + parentId);
        }

        sb.append(" and c.valid = " + soldEntryKey.isValid());

        Query query = em.createQuery(sb.toString());
        for (Map.Entry<Integer, Object> entry : values.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List resultsList = query.getResultList();
        return null == resultsList ? Collections.emptyList() : resultsList;
    }

    @Override
    public int getSoldQuantity(StockKey stockKey, Date day, Boolean valid) {
        StringBuilder sb = new StringBuilder("select sum(c.quantity) from " + SoldEntry.class.getName() + " c where c.inventoryType = ?1 and c.inventoryId = ?2 and c.day = ?3");

        if (null != valid) {
            sb.append(" and c.status = ?4 ");
        }

        Query query = em.createQuery(sb.toString());
        query.setParameter(1, stockKey.getInventoryType());
        query.setParameter(2, stockKey.getInventoryId());
        query.setParameter(3, day);

        if (null != valid) {
            query.setParameter(4, valid);
        }

        return getTotalCount(query).intValue();
    }
}
