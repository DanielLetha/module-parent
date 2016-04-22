package com.simpletour.dao.inventory.imp;

import com.simpletour.commons.data.dao.jpa.DependencyHandleDAO;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.dao.inventory.IStockDao;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.inventory.query.StockQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;

/**
 * 文件描述：库存数据层实现类
 * 创建人员：石广路
 * 创建日期：2015/11/25 20:38
 * 备注说明：null
 */
@Repository
public class StockDaoImp extends DependencyHandleDAO implements IStockDao {
    private static final String SOLD_QUANTITY_SUM = "SUM(CASE WHEN sold.quantity IS NULL THEN 0 ELSE sold.quantity END)";

    //private static final String[] STOCK_FIELDS_ARRAY = {"id", "day", "inventoryType", "inventoryId", "online", "price", "quantity", "version"};
    private static final String[] STOCK_FIELDS_ARRAY = {"id", "day", "row_table", "row_id", "online", "quantity"};

    private static final String STOCK_FIELDS;

    private static final String ASSOCIATED_STOCK_FIELDS;

    static {
        final String LAST_STOCK_FIELD = STOCK_FIELDS_ARRAY[STOCK_FIELDS_ARRAY.length - 1];
        StringBuilder stockFields = new StringBuilder(STOCK_FIELDS_ARRAY.length);

        for (String stockField : STOCK_FIELDS_ARRAY) {
            stockFields.append("stocks." + stockField);
            if (!stockField.equals(LAST_STOCK_FIELD)) {
                stockFields.append(", ");
            }
        }

        STOCK_FIELDS = stockFields.toString();

        StringBuilder allStockFields = new StringBuilder("SELECT ");
        allStockFields.append(STOCK_FIELDS);
        allStockFields.append(", ").append(SOLD_QUANTITY_SUM).append(" AS soldQuantity,");
        allStockFields.append(" CASE WHEN stocks.quantity > ").append(SOLD_QUANTITY_SUM);
        allStockFields.append(" THEN stocks.quantity - ").append(SOLD_QUANTITY_SUM);
        allStockFields.append(" ELSE 0 END AS availableQuantity FROM");
        ASSOCIATED_STOCK_FIELDS = allStockFields.toString();
    }

    @Override
    public <T extends BaseDomain> T getStockDependency(final InventoryType inventoryType, final Long inventoryId) {
        Class<T> clazz = getInventoryClass(inventoryType);
        return null == clazz ? null : getEntityById(clazz, inventoryId);
    }

    @Override
    public <T extends BaseDomain> T getOnlineStockDependency(final InventoryType inventoryType, final Long inventoryId) {
        Class<T> clazz = getInventoryClass(inventoryType);
        if (null == clazz) {
            return null;
        }

        StringBuilder sb = new StringBuilder("SELECT c FROM ");
        sb.append(clazz.getName()).append(" c WHERE 1 = 1 ");

        if (checkCanDel(clazz)) {
            sb.append(" AND ").append(filterDelPattern);
        }

        if (null != inventoryId && 0 < inventoryId) {
            sb.append(" AND c.id = ").append(inventoryId + " ");
        }

        if (InventoryType.bus_no == inventoryType) {
            sb.append(" AND c.status = 'normal'");
        } else {
            sb.append(" AND (c.online IS NULL OR c.online = true)");
        }

        Query query = em.createQuery(sb.toString());
        query.setMaxResults(1);

        Object entity = null;
        try {
            entity = query.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }

        return null == entity ? null : (T)entity;
    }

    @Override
    public Optional<Object> getFieldValueByConditions(final StockQuery stockQuery, String filedName) {
        StringBuilder sb = new StringBuilder(String.format("select %s from %s c where 1 = 1", filedName, Stock.class.getName()));
        StockKey stockKey = stockQuery.getStockKey();
        InventoryType inventoryType = stockKey.getInventoryType();
        Long inventoryId = stockKey.getInventoryId();

        if (null != inventoryType) {
            sb.append(" and c.row_table = '").append(inventoryType).append("'");
        }

        if (null != inventoryId) {
            sb.append(" and c.row_id = ").append(inventoryId);
        }

        Boolean online = stockQuery.getOnline();
        if (null != online) {
            sb.append(" and c.online = ").append(online);
        }

        if (null == buildDateRange(sb, "c.day", "yyyy-MM-dd", stockQuery.getStartDate(), stockQuery.getEndDate())) {
            return Optional.empty();
        }

        Query query = em.createQuery(sb.toString());
        query.setMaxResults(1);

        Object filedValue;
        try {
            filedValue = query.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }

        return null == filedValue ? Optional.empty() : Optional.ofNullable(filedValue);
    }

    @Override
    public Optional<Stock> getStockByConditions(StockKey stockKey, Date day, Boolean online) {
        StringBuilder sb = new StringBuilder("select c from ");
        sb.append(Stock.class.getName()).append(" c where c.inventoryType = ?1 and c.inventoryId = ?2 and c.day = ?3");

        if (null != online) {
            sb.append(" and c.online = ?4");
        }

        Query query = em.createQuery(sb.toString());
        query.setParameter(1, stockKey.getInventoryType());
        query.setParameter(2, stockKey.getInventoryId());
        query.setParameter(3, day);

        if (null != online) {
            query.setParameter(4, online);
        }

        query.setMaxResults(1);

        Object stock;
        try {
            stock = query.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }

        return null == stock ? Optional.empty() : Optional.of((Stock)stock);
    }

//    @Override
//    public List<Stock> getStocksListByConditions(final StockKey stockKey, final Date startDate, final Date endDate, final Boolean online) {
//        String sql = "select c from " + Stock.class.getName() + " c where 1 = 1";
//        Query query = buildQuerySql(sql, stockKey, startDate, endDate, online, "day", SortBy.DESC, 0, 0);
//        List resultsList = query.getResultList();
//        return null == resultsList ? Collections.emptyList() : resultsList;
//    }

//    private Optional<Stock> getStockQuantitiesListByConditions(StockKey stockKey, Date day, Boolean online) {
//
//    }

    @Override
    public Optional<Stock> getStockQuantitiesListByConditions(StockKey stockKey, Date day, Boolean online) {
        List<Stock> stocks = getStocksQuantitiesListByConditions(new StockQuery(stockKey, day, online));
        return stocks.isEmpty() ? Optional.empty() : Optional.ofNullable(stocks.get(0));
    }

    @Override
    public List<Stock> getStocksQuantitiesListByConditions(final StockQuery stockQuery) {
        List<String> stockFieldsList = new ArrayList<>(Arrays.asList(STOCK_FIELDS_ARRAY));
        stockFieldsList.add("soldQuantity");
        stockFieldsList.add("availableQuantity");

        StringBuilder slave = new StringBuilder();
        buildSoldQuerySql(slave);
        slave.append("GROUP BY ").append(STOCK_FIELDS).append(" ORDER BY stocks.").append(stockQuery.getOrderByFiledName()).append(" ").append(stockQuery.getOrderBy().name());

        StockQuery queryParm = (StockQuery)stockQuery.clone();
        queryParm.setPageIndex(0);
        queryParm.setPageSize(0);

        Query selectQuery = buildFilterQuerySql(queryParm, ASSOCIATED_STOCK_FIELDS, slave.toString(), "c.*", 1, null);
        if (null == selectQuery) {
            return Collections.emptyList();
        }

        List<Object[]> resultsList = selectQuery.getResultList();
        List<Stock> stocks = convertFields2Entities(stockFieldsList, resultsList, Stock.class);
        return stocks;
    }

//    @Override
//    public DomainPage getStocksQuantitiesPagesByConditions(final StockQuery stockQuery) {
//        StockQuery queryParm = (StockQuery)stockQuery.clone();
//        queryParm.setPageIndex(0);
//        queryParm.setPageSize(0);
//
//        String count = "SELECT COUNT(DISTINCT(stocks.id)) FROM";
//        StringBuilder slave = new StringBuilder();
//        buildSoldQuerySql(slave);
//
//        DomainPage domainPage = new DomainPage(stockQuery.getPageSize(), stockQuery.getPageIndex(), 0);
//        Query countQuery = buildFilterQuerySql(queryParm, count, slave.toString(), "c.*", 1, null);
//        if (null == countQuery) {
//            return domainPage;
//        }
//
//        Long totalCount = getTotalCount(countQuery);
//        if (0 < totalCount) {
//            List<Stock> stocks = getStocksQuantitiesListByConditions(stockQuery);
//            domainPage.getDomains().addAll(stocks);
//            domainPage.setDomainTotalCount(totalCount);
//        }
//
//        return domainPage;
//    }

    @Override
    public DomainPage getStocksQuantitiesPagesByRelations(final StockQuery stockQuery) {
        List<String> stockFieldsList = new ArrayList<>(Arrays.asList(STOCK_FIELDS_ARRAY));
        stockFieldsList.add("soldQuantity");
        stockFieldsList.add("availableQuantity");
        stockFieldsList.add("inventoryName");
        stockFieldsList.add("inventoryRemark");

        StockQuery queryParm = (StockQuery)stockQuery.clone();
        queryParm.setPageIndex(0);
        queryParm.setPageSize(0);
        List<Long> inventoryTypeIds = getInventoryTypeIds(queryParm);

        StringBuilder master = new StringBuilder();
        int index = buildStockQuerySql(stockQuery, master, null, true, inventoryTypeIds.isEmpty());

        StringBuilder slave = new StringBuilder();
        buildSoldQuerySql(slave);
        slave.append("GROUP BY ").append(STOCK_FIELDS).append(") stocks ON stocks.row_id = inventory.row_id ) stocks ");

        int pageIndex = stockQuery.getPageIndex();
        int pageSize = stockQuery.getPageSize();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, 0);

        // 查询库存记录总数
        Query countQuery = buildFilterQuerySql(stockQuery, master.toString(), slave.toString(), "c.*", index, inventoryTypeIds);
        if (null == countQuery) {
            return domainPage;
        }

        Long totalCount = getTotalCount(countQuery);
        if (0 < totalCount && pageSize > pageIndex * pageSize - totalCount) {
            // 查询库存依托对象所关联的所有库存信息
            master = master.delete(0, master.length());
            inventoryTypeIds = getInventoryTypeIds(stockQuery);
            index = buildStockQuerySql(stockQuery, master, slave, false, inventoryTypeIds.isEmpty());
            Query selectQuery = buildFilterQuerySql(queryParm, master.toString(), slave.toString(), "c.*", index, inventoryTypeIds);
            List<Object[]> resultsList = selectQuery.getResultList();
            List<Stock> stocks = convertFields2Entities(stockFieldsList, resultsList, Stock.class);
            domainPage.getDomains().addAll(stocks);
            domainPage.setDomainTotalCount(totalCount);
        }

        return domainPage;
    }

    /**
     * 获取去重后的库存依托对象的ID列表
     * @param stockQuery
     * @return
     */
    private List<Long> getInventoryTypeIds(final StockQuery stockQuery) {
        StringBuilder master = new StringBuilder("SELECT stocks.row_id FROM (");
        int index = buildInventoryQuerySql(stockQuery, master, true);
        master.append(" ) inventory INNER JOIN");

        StringBuilder slave = new StringBuilder("GROUP BY c.row_id HAVING 0 < COUNT(c.row_id) ) stocks ON stocks.row_id = inventory.row_id ORDER BY stocks.row_id ");
        slave.append(stockQuery.getOrderBy().name());

        Query selectQuery = buildFilterQuerySql(stockQuery, master.toString(), slave.toString(), "c.row_id", index, null);
        if (null == selectQuery) {
            return Collections.emptyList();
        }

        List resultList = selectQuery.getResultList();
        return null == resultList ? Collections.emptyList() : resultList;
    }

    /**
     * 构建库存的查询SQL
     * @param stockQuery
     * @param master
     * @param slave
     * @param isCountSql
     * @return
     */
    private int buildStockQuerySql(final StockQuery stockQuery, StringBuilder master, StringBuilder slave, boolean isCountSql, boolean byLike) {
        master.append("SELECT ");

        if (isCountSql) {
            master.append("COUNT(DISTINCT(stocks.row_id))");
        } else {
            master.append(STOCK_FIELDS).append(", stocks.soldQuantity, stocks.availableQuantity, stocks.inventoryName, stocks.inventoryRemark");
            slave.append("ORDER BY stocks.").append(stockQuery.getOrderByFiledName()).append(" ").append(stockQuery.getOrderBy().name());
        }

        master.append(" FROM ( SELECT stocks.*, inventory.inventoryName, inventory.inventoryRemark FROM ( ");
        int index = buildInventoryQuerySql(stockQuery, master, byLike);
        master.append(" ) inventory INNER JOIN ( ").append(ASSOCIATED_STOCK_FIELDS);

        return index;
    }

    /**
     * 构建库存依托对象的查询SQL
     * @param stockQuery
     * @param master
     * @param byLike
     * @return
     */
    private int buildInventoryQuerySql(final StockQuery stockQuery, StringBuilder master, boolean byLike) {
        int index = 0;  // JPA占位符索引
        InventoryType inventoryType = stockQuery.getStockKey().getInventoryType();
        if (null == inventoryType) {
            return index;
        }

        String nameField = "inventory.";
        String remarkField;
        String statusField = "inventory.";

        if (InventoryType.bus_no == inventoryType) {
            nameField += "no";
            remarkField = "format('[%s - %s]', inventory.depart, inventory.arrive)";
            statusField += "status";
        } else {
            nameField += "name";
            remarkField = "inventory.remark";
            statusField += "online";
        }

        master.append("SELECT inventory.id AS row_id, ").append(nameField).append(" AS inventoryName, ").append(remarkField).append(" AS inventoryRemark FROM ");
        master.append(inventoryType.getTabelName()).append(" inventory WHERE 1 = 1").append(getTenantIdFilterCondition(getInventoryClass(inventoryType)).replaceFirst("c.tenantId", "inventory.tenant_id"));
        if (byLike) {
            String inventoryName = stockQuery.getInventoryName();
            if (null != inventoryName && !inventoryName.isEmpty()) {
                master.append(" AND LOWER(").append(nameField).append(") LIKE LOWER(?").append(++index).append(')');
            }
        } else {
            master.append(" AND inventory.id in (?").append(++index).append(')');
        }

        StockQuery.InventoryStatus inventoryStatus = stockQuery.getInventoryStatus();
        if (null != inventoryStatus) {
            Object statusValue = inventoryStatus.getValue();
            StringBuilder statusPattern = new StringBuilder();
            if (statusValue instanceof Boolean && ((Boolean)statusValue).booleanValue()) {
                statusPattern.append(statusField).append(" IS NULL OR ");
            }
            statusPattern.append(statusField).append(" = ").append(statusValue.toString());
            master.append(" AND (").append(statusPattern.toString()).append(')');
        }

        return index;
    }

    /**
     * 构建与库存关联的销售库存的查询SQL
     * @param slave
     */
    private void buildSoldQuerySql(StringBuilder slave) {
        slave.append(") stocks LEFT JOIN ( SELECT se.row_table, se.row_id, se.day, se.quantity FROM inv_sold_entry se WHERE se.valid = true) sold ");
        slave.append("ON stocks.row_table = sold.row_table AND stocks.row_id = sold.row_id AND stocks.day = sold.day ");
    }

    /**
     * 根据过滤条件来构建查询语句
     * @param stockQuery
     * @param head
     * @param tail
     * @param fields
     * @param begin
     * @param inventoryTypeIds
     * @return
     */
    private Query buildFilterQuerySql(final StockQuery stockQuery, String head, String tail, String fields, int begin, List<Long> inventoryTypeIds) {
        StockKey stockKey = stockQuery.getStockKey();
        InventoryType inventoryType = stockKey.getInventoryType();
        Long inventoryId = stockKey.getInventoryId();
        String inventoryName = stockQuery.getInventoryName();
        Date startDate = stockQuery.getStartDate();
        Date endDate = stockQuery.getEndDate();
        Boolean online = stockQuery.getOnline();
        int index = 0 >= begin ? 1 : begin;
        Map<Integer, Object> values = new LinkedHashMap<>(6);
        StringBuilder sb = new StringBuilder(head);

        sb.append(String.format(" ( SELECT %s FROM inv_stock c WHERE 1 = 1", fields));

        if (null != inventoryTypeIds && !inventoryTypeIds.isEmpty()) {
            values.put(index++, inventoryTypeIds);
            sb.append(" AND 0 < c.quantity");  // 仅用在前端关联查询

            String ids = inventoryTypeIds.toString().replace('[', '(').replace(']', ')');
            values.put(index, inventoryTypeIds);
            sb.append(" AND c.row_id in (?").append(index++).append(')');
        } else {
            if (null != inventoryName && !inventoryName.isEmpty() && -1 != head.indexOf("AS inventoryName")) {
                values.put(index++, '%' + inventoryName + '%');
                sb.append(" AND 0 < c.quantity");  // 仅用在前端关联查询
            }

            if (null != inventoryId) {
                sb.append(" AND c.row_id = ").append(inventoryId);
            }
        }

        if (null != inventoryType) {
            values.put(index, inventoryType.name());
            sb.append(" AND c.row_table = ?").append(index++);
        }

        if (null == buildDateRange(sb, "c.day", "yyyy-MM-dd", startDate, endDate)) {
            return null;
        }

        if (null != online) {
            sb.append(" AND c.online = " + online);
        }

        if (null != tail && !tail.isEmpty()) {
            sb.append(" ").append(tail);
        }

        Query query = em.createNativeQuery(sb.toString());
        for (Map.Entry<Integer, Object> entry : values.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        int pageIndex = 1;
        int pageSize = 1;

        if (!head.startsWith("SELECT COUNT(")) {
            pageIndex = stockQuery.getPageIndex();
            pageSize = stockQuery.getPageSize();
        }

        if (0 < pageIndex && 0 < pageSize) {
            query.setFirstResult((1 >= pageIndex ? 0 : pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        return query;
    }

    private Query buildQuerySql(String sql, StockKey stockKey, Date startDate, Date endDate, Boolean online, String orderByFiledName, SortBy orderBy, int pageIndex, int pageSize) {
        int index = 1;
        Map<Integer, Object> values = new LinkedHashMap<>(5);
        StringBuilder sb = new StringBuilder(sql);
        InventoryType inventoryType = stockKey.getInventoryType();
        Long inventoryId = stockKey.getInventoryId();

        if (null != inventoryType) {
            values.put(index, inventoryType);
            sb.append(" and c.row_table = ?").append(index++);
        }

        if (null != inventoryId) {
            sb.append(" and c.row_id = ").append(inventoryId);
        }

        if (null != startDate) {
            values.put(index, startDate);
            sb.append(" and c.day >= ?").append(index++);
        }

        if (null != endDate) {
            values.put(index, endDate);
            sb.append(" and c.day <= ?").append(index++);
        }

        if (null != online) {
            sb.append(true == online ? " AND (c.online IS NULL OR c.online = TRUE)" : " AND c.online = FALSE");
        }

        if (!sql.startsWith("select count(c)")) {
            if (null == orderByFiledName || orderByFiledName.isEmpty()) {
                orderByFiledName = "day";
            }
            sb.append(" order by c.").append(orderByFiledName).append(" ").append(orderBy.name());
        }

        Query query = em.createQuery(sb.toString());
        for (Map.Entry<Integer, Object> entry : values.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        if (0 < pageIndex && 0 < pageSize) {
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        return query;
    }

    private <T extends BaseDomain> List<T> convertFields2Entities(final List<String> fieldsList, final List<Object[]> resultsList, Class<T> clazz) {
        if (null == resultsList || resultsList.isEmpty()) {
            return Collections.emptyList();
        }

        final int size = resultsList.size();
        List<T> entities = new ArrayList<>(size);

        resultsList.forEach(objs -> {
            int index = 0;
            Object value = null;
            String name = null;

            try {
                T entity = clazz.newInstance();

                for (Object obj : objs) {
                    name = fieldsList.get(index++);

                    if (name.equals("row_table")) {
                        name = "inventoryType";
                    }

                    if (name.equals("row_id")) {
                        name = "inventoryId";
                    }

                    Field field = getDeclaredField(clazz, name);
                    if (null == field) {
                        int MAX_INHERIT_NUM = 0;
                        Class superClazz = clazz;
                        do {
                            superClazz = clazz.getSuperclass();
                            if (Object.class == superClazz || null != (field = getDeclaredField(superClazz, name))) {
                                break;
                            }
                        } while (5 < MAX_INHERIT_NUM++);
                    }

                    if (null == field) {
                        break;
                    }

                    value = getFieldValue(name, obj);
                    field.setAccessible(true);
                    field.set(entity, value);
                }

                Long id = entity.getId();
                if (null != id && 0 < id) {
                    entities.add(entity);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return;
            }
        });

        return size != entities.size() ? Collections.emptyList() : entities;
    }

    private Field getDeclaredField(Class clzz, String name) {
        Field field;
        try {
            field = clzz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
            field = null;
        }
        return field;
    }

    private Object getFieldValue(String name, Object value) {
        if (null != value) {
            if (name.endsWith("Quantity")) {
                return Integer.valueOf((((BigInteger) value).intValue()));
            }
            if (value instanceof BigInteger) {
                return Long.valueOf((((BigInteger) value).longValue()));
            }
            if ("inventorytype".equalsIgnoreCase(name)) {
                return InventoryType.valueOf(value.toString());
            }
        }
        return value;
    }

    private <T extends BaseDomain> Class<T> getInventoryClass(InventoryType inventoryType) {
        Class<T> clazz = null;
        try {
            clazz = (Class<T>)Class.forName(inventoryType.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public <T extends BaseDomain> int removeEntityByConditions(Class<T> clazz, Condition conditions)  {
        String sql;
        sql = "delete from " + clazz.getName() +" c where ";
        sql += conditions.toString();

        Query query = this.em.createQuery(sql);
        conditions.setParameter(query, 1);

        return query.executeUpdate();
    }
}