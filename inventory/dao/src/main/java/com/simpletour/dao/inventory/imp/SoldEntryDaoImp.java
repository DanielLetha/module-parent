package com.simpletour.dao.inventory.imp;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.jpa.JPABaseDAO;
import com.simpletour.dao.inventory.ISoldEntryDao;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.query.SoldEntryKey;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

/**
 * 文件描述：销售库存实现类
 * 创建人员：石广路
 * 创建日期：2015/11/27 16:15
 * 备注说明：null
 */
@Repository
public class SoldEntryDaoImp extends JPABaseDAO implements ISoldEntryDao, IBaseDao {
    @Override
    @Transactional
    public void updateSoldEntriesStatusByOrderId(long oid, boolean status) {
        String sql = "update " + SoldEntry.class.getName() + " c set c.status = ?1 where c.orderId = ?2";
        em.createQuery(sql).setParameter(1, status).setParameter(2, oid).executeUpdate();
    }

    @Override
    public List<SoldEntry> getSoldEntriesByUnionKeys(SoldEntryKey soldEntryKey) {
        int index = 1;
        Map<Integer, Object> values = new HashMap<>(5);
        StringBuilder sb = new StringBuilder("select c from " + SoldEntry.class.getName() + " c where 1 = 1");

        InventoryType inventoryType = soldEntryKey.getInventoryType();
        Long inventoryTypeId = soldEntryKey.getInventoryTypeId();
        Long orderId = soldEntryKey.getOrderId();
        Long itemId = soldEntryKey.getItemId();
        Long parentId = soldEntryKey.getParentId();

        if (null != inventoryType) {
            values.put(index, inventoryType);
            sb.append(" and c.inventoryType = ?" + (index++));
        }

        if (null != inventoryTypeId && 0 < inventoryTypeId) {
            values.put(index, inventoryTypeId);
            sb.append(" and c.inventoryTypeId = ?" + (index++));
        }

        if (null != orderId && 0 < orderId) {
            values.put(index, orderId);
            sb.append(" and c.orderId = ?" + (index++));
        }

        if (null != itemId && 0 < itemId) {
            values.put(index, itemId);
            sb.append(" and c.itemId = ?" + (index++));
        }

        if (null != parentId && 0 <= parentId) {
            values.put(index, parentId);
            sb.append(" and c.parentId = ?" + (index++));
        }

        Query query = em.createQuery(sb.toString());
        for (Map.Entry<Integer, Object> entry : values.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List resultsList = query.getResultList();
        return null == resultsList ? Collections.emptyList() : resultsList;
    }

    @Override
    public int getSoldQuantity(InventoryType inventoryType, Long inventoryTypeId, Date day, Boolean status) {
        StringBuilder sb = new StringBuilder("select sum(c.quantity) from " + SoldEntry.class.getName() + " c where c.inventoryType = ?1 and c.inventoryTypeId = ?2 and c.day = ?3");

        if (null != status) {
            sb.append(" and c.status = ?4 ");
        }

        Query query = em.createQuery(sb.toString());
        query.setParameter(1, inventoryType);
        query.setParameter(2, inventoryTypeId);
        query.setParameter(3, day);

        if (null != status) {
            query.setParameter(4, status);
        }

        Object result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }

        return null == result ? 0 : ((Long)result).intValue();
    }
}
