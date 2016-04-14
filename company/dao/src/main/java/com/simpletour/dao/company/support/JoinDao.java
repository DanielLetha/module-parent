package com.simpletour.dao.company.support;


import com.simpletour.commons.data.dao.jpa.JPABaseDAO;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.QueryUtil;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/8
 * Time: 10:29
 */
public class JoinDao extends JPABaseDAO {
    public <T extends BaseDomain> DomainPage getEntitiesPagesByQuery(Class<T> clazz, ConditionOrderByQuery query) {
        if (query == null)
            query = new ConditionOrderByQuery();
        if (JoinConditionOrderByQuery.class.isInstance(query)) {
            return getEntitiesPagesByJoinCondition(clazz, ((JoinConditionOrderByQuery) query).getJoinMap()
                    , query.getCondition(), query.getOrderByString(), query.getPageIndex(), query.getPageSize());
        } else {
            return getEntitiesPagesByCondition(clazz, query.getCondition(), query.getOrderByString(), query.getPageIndex(), query.getPageSize());
        }
    }

    public <T extends BaseDomain> DomainPage getEntitiesPagesByJoinCondition(Class<T> clazz, Map<String, String> joinMap, Condition condition, String orderBy, int pageIndex, int pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuilder buffer = new StringBuilder("select distinct c from ").append(clazz.getName()).append(" c ");
        if (joinMap != null && !joinMap.isEmpty()) {
            joinMap.entrySet().forEach(tmp -> {
                buffer.append(" left join ").append(tmp.getKey()).append(" ").append(tmp.getValue());
            });
        }
        buffer.append(" where 1=1 ");
        //where
        if (condition != null && !condition.isEmpty())
            buffer.append(" and ").append(condition.toString());
        if (checkCanDel(clazz))
            buffer.append(" and ").append(filterDelPattern);
        buffer.append(getTenantIdFilterCondition(clazz));

        //order by
        if (orderBy == null || orderBy.isEmpty())
            orderBy = " order by c.id desc ";
        buffer.append(orderBy);

        Query query = em.createQuery(buffer.toString());
        if (condition != null)
            condition.setParameter(query, 1);
        query.setFirstResult((pageIndex - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<T> resultList = query.getResultList();

        //count
        String sql = buffer.toString();
        String sqlCount = "select count(distinct c) " + QueryUtil.removeSelect(QueryUtil.removeOrders(sql));
        query = em.createQuery(sqlCount);

        if (condition != null)
            condition.setParameter(query, 1);

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, getTotalCount(query));
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    public <T extends BaseDomain> List<T> getEntitiesByQuery(Class<T> clazz, ConditionOrderByQuery query){
        if (query == null)
            query = new ConditionOrderByQuery();
        if (JoinConditionOrderByQuery.class.isInstance(query)) {
            return getEntitiesByConditionJoin(clazz,((JoinConditionOrderByQuery)query).getJoinMap(),query.getCondition(),query.getOrderByString());
        }else {
            return getEntitiesByCondition(clazz, query.getCondition(), query.getOrderByString());
        }
    }

    public <T extends BaseDomain> List<T> getEntitiesByConditionJoin(Class<T> clazz,Map<String, String> joinMap, Condition condition, String orderBy){
        StringBuilder buffer = new StringBuilder("select distinct c from ").append(clazz.getName()).append(" c ");
        if (joinMap != null && !joinMap.isEmpty()) {
            joinMap.entrySet().forEach(tmp -> {
                buffer.append("left join ").append(tmp.getKey()).append(" ").append(tmp.getValue());
            });
        }
        buffer.append(" where 1=1 ");

        // where
        if (condition != null && !condition.isEmpty())
            buffer.append(" and ").append(condition.toString());
        if (checkCanDel(clazz)) {
            buffer.append(" and ").append(filterDelPattern);
        }
        buffer.append(getTenantIdFilterCondition(clazz));

        // order by
        if (orderBy == null || orderBy.isEmpty())
            orderBy = " order by c.id desc ";
        buffer.append(orderBy);

        Query query = em.createQuery(buffer.toString());

        if (condition != null)
            condition.setParameter(query, 1);

        return query.getResultList();
    }
}
