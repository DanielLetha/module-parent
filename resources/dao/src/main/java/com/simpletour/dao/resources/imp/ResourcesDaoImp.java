package com.simpletour.dao.resources.imp;

import com.simpletour.commons.data.dao.jpa.JPABaseDAO;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.dao.resources.IResourcesDao;
import org.springframework.stereotype.Repository;
import resources.Destination;
import resources.Procurement;
import resources.UnionEntityKey;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songfujie on 15/10/22.
 */
@Repository
public class ResourcesDaoImp extends JPABaseDAO implements IResourcesDao {

    public <T extends BaseDomain> DomainPage queryProcurement(Map<String, Object> fieldNameValueMap, String orderByFiledName, SortBy orderBy, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuffer select = new StringBuffer("select t.id as t_id,t.name as t_name,t.resourceType as t_rt,t.resourceId as t_rid,t.online as t_online,d.name as d_name ");
        StringBuffer from = new StringBuffer(" from TR_PROCUREMENT t ");
        StringBuffer join = new StringBuffer();
        join.append(" left join TR_DESTINATION d on d.id = t.destination_id ");

        StringBuffer where = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<>();
        buildJoinResourceSql(join, select, fieldNameValueMap);
        buildWhereSql(where, fieldNameValueMap, paramMap);

        if (where.length() > 4)
            where = new StringBuffer("where " + where.substring(4));

        select.append(from).append(join).append(where).append(" order by t." + orderByFiledName + " " + orderBy.name());
        Query query = em.createNativeQuery(select.toString());

        for (String key : paramMap.keySet()) {
            query.setParameter(key, paramMap.get(key));
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Object[]> resultList = query.getResultList();

        String sql = select.toString();
        String sqlCount = "select count(*) " + sql.substring(sql.indexOf("from"), sql.indexOf("order"));
        query = em.createNativeQuery(sqlCount);
        for (String key : paramMap.keySet()) {
            query.setParameter(key, paramMap.get(key));
        }

        Object count = query.getSingleResult();
        Long totalCount = 0L;
        if (count != null) {
            totalCount = Long.parseLong(count.toString());
        }

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);

        return domainPage;
    }

    @Override
    public long getResourceId(UnionEntityKey unionEntityKey) {
        Object entity = queryResource(unionEntityKey, "c.id");
        return null == entity ? 0 : (Long)entity;
    }

    @Override
    public <T extends LogicalDeletableDomain> T getResourceById(UnionEntityKey unionEntityKey) {
        Object entity = queryResource(unionEntityKey, "c");
        return null == entity ? null : (T)entity;
    }

    private Object queryResource(UnionEntityKey unionEntityKey, String fieldName) {
        if (null == unionEntityKey) {
            return null;
        }

        StringBuilder sb = new StringBuilder("select ");
        sb.append(String.format(" %s from %s c where ", fieldName, unionEntityKey.getClazz().getName()));
        sb.append(filterDelPattern);
        sb.append(getTenantIdFilterCondition(unionEntityKey.getClazz()));
        sb.append(" and c.name = ?1 and ");
        sb.append(unionEntityKey.getClazz() == Destination.class ? "id = ?2" : "c.destination.id = ?2");

        Query query = em.createQuery(sb.toString());
        query.setParameter(1, unionEntityKey.getName());
        query.setParameter(2, unionEntityKey.getDestination().getId());
        query.setMaxResults(1);

        Object entity;
        try {
            entity = query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return entity;
    }

    @Override
    public boolean hasDependableResource(Procurement.ResourceType resourceType, Long resourceId) {
        Class<? extends BaseDomain> clazz = null;
        String typeName = resourceType.name();
        typeName = typeName.replace(typeName, typeName.substring(0, 1).toUpperCase() + typeName.substring(1));

        try {
            clazz = (Class<? extends BaseDomain>)Class.forName("com.simpletour.domain.resources." + typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return !(null == clazz || 0 == getEntityTotalCount(clazz, "id", resourceId));
    }

    @Override
    public <T extends LogicalDeletableDomain> boolean hasDependedByProcurement(Class<T> tClass, long id) {
        StringBuilder sb = new StringBuilder("select count(c.id) from " + Procurement.class.getName() + " c where ");
        sb.append(filterDelPattern);
        sb.append(getTenantIdFilterCondition(Procurement.class));
        sb.append(" and c.resourceType = ?1 and c.resourceId = ?2");

        String className = tClass.getName();
        String resourceType = className.substring(className.lastIndexOf('.') + 1).toLowerCase();

        Query query = em.createQuery(sb.toString());
        query.setParameter(1, Procurement.ResourceType.valueOf(resourceType));
        query.setParameter(2, id);

        return 0 < getTotalCount(query);
    }

    @Override
    public boolean hasDependedByStockEntity(long id) {
        String sql = "select count(c.id) from prod_package c where procurement_id = " + id;
        Query query = em.createNativeQuery(sql);
        return 0 < getTotalCount(query);
    }

    @Override
    public void addDependency(BaseDomain domain) {
        // TODO：暂不实现
    }

    @Override
    public void updateDependency(BaseDomain domain) {
        // TODO：暂不实现
    }

    @Override
    public void deleteDependency(BaseDomain domain) {
        // TODO：暂不实现
    }

    @Override
    public <T extends BaseDomain> DomainPage queryEntitiesPagesByFieldList(Map<String, Object> fieldNameValueMap, String orderByFiledName, SortBy orderBy, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuffer select = new StringBuffer("select t.id as t_id,t.name as t_name,t.resourceType as t_rt,t.online as t_online ");
        StringBuffer from = new StringBuffer(" from TR_PROCUREMENT t ");
        StringBuffer joinBuffer = new StringBuffer();
        StringBuffer where = new StringBuffer();
//        buildJoinDestinationSql(joinBuffer, select);
        buildJoinResourceSql(joinBuffer, select, fieldNameValueMap);
        Map<String, Object> paramMap = new HashMap<>();
        buildWhereSql(where, fieldNameValueMap, paramMap);
        if (where.length() > 4)
            where = new StringBuffer("where " + where.substring(4));
        select.append(from).append(joinBuffer).append(where).append(" order by t." + orderByFiledName + " " + orderBy.name());
        Query query = em.createNativeQuery(select.toString());
        for (String key : paramMap.keySet()) {
            query.setParameter(key, paramMap.get(key));
        }
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Object[]> resultList = query.getResultList();

        String sql = select.toString();
        String sqlCount = "select count(*) " + sql.substring(sql.indexOf("from"), sql.indexOf("order"));
        query = em.createNativeQuery(sqlCount);
        for (String key : paramMap.keySet()) {
            query.setParameter(key, paramMap.get(key));
        }
        Object count = query.getSingleResult();
        Long totalCount = 0L;
        if (count != null) {
            totalCount = Long.parseLong(count.toString());
        }
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
//        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    private void buildWhereSql(StringBuffer where, Map<String, Object> fieldNameValueMap, Map<String, Object> paramMap) {
        if (fieldNameValueMap.containsKey("resourceType") && fieldNameValueMap.containsKey("resourceName")) {
            where.append("and r.name like :r_name ");
            paramMap.put("r_name", "%" + fieldNameValueMap.get("resourceName") + "%");
        }
        if (fieldNameValueMap.containsKey("name")) {
            where.append("and t.name like :t_name ");
            paramMap.put("t_name", "%" + fieldNameValueMap.get("name") + "%");
        }
        if (fieldNameValueMap.containsKey("online")) {
            where.append("and t.online = :online ");
            paramMap.put("online", fieldNameValueMap.get("online"));
        }
        if (fieldNameValueMap.containsKey("destination.name")) {
            where.append("and d.name like :d_name ");
            paramMap.put("d_name", "%" + fieldNameValueMap.get("destination.name") + "%");
        }
        if (fieldNameValueMap.containsKey("resourceType")) {
            where.append("and t.resourceType = :resourceType ");
            paramMap.put("resourceType", fieldNameValueMap.get("resourceType"));
        }
        Long tenantId = getTenantId();
        where.append(" and (t.del is null or t.del = false)").append(null == tenantId ? "" : " and t.tenant_id = " + tenantId);
    }

    private void buildJoinResourceSql(StringBuffer buffer, StringBuffer select, Map<String, Object> fieldNameValueMap) {
        if (fieldNameValueMap.containsKey("resourceType")) {
            String resourceType = (String) fieldNameValueMap.get("resourceType");
            if (resourceType.equals("hotel")) {
                buffer.append(" left join (select trh.* from TR_HOTEL trh where trh.del = 'f') r on t.resourceId = r.id ");
            } else if (resourceType.equals("scenic")) {
                buffer.append(" left join (select trh.* from TR_SCENIC trh where trh.del = 'f') r on t.resourceId = r.id ");
            } else if (resourceType.equals("catering")) {
                buffer.append(" left join (select trh.* from TR_CATERING trh where trh.del = 'f') r on t.resourceId = r.id ");
            } else if (resourceType.equals("entertainment")) {
                buffer.append(" left join (select trh.* from TR_ENTERTAINMENT trh where trh.del = 'f') r on t.resourceId = r.id ");
            }
            select.append(",r.name as r_name ");
        }
    }
}
