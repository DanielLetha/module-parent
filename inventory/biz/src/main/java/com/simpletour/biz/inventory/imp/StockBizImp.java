package com.simpletour.biz.inventory.imp;

import com.simpletour.biz.inventory.IStockBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.inventory.ISoldEntryDao;
import com.simpletour.dao.inventory.IStockDao;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.query.IStockTraceable;
import com.simpletour.domain.inventory.query.StockAvailableKey;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.inventory.query.StockQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 文件描述：库存模块业务处理实现类
 * 创建人员：石广路
 * 创建日期：2015/11/30 15:21
 * 备注说明：null
 */
@Component
public class StockBizImp implements IStockBiz {
//    @Autowired
//    private IStockQueryBiz stockQueryBiz;

    @Autowired
    private IStockDao stockDao;

    @Resource
    private ISoldEntryDao soldEntryDao;

//    @Override
//    public boolean isExisted(Stock stock) throws BaseSystemException {
//        StockParamsValidater.validateBoundParams(stock);
//
//        Long id = stock.getId();
//        if (null != id && 0 < id) {
//            return null != stockDao.getEntityById(Stock.class, id);
//        }
//
//        return stockQueryBiz.hasOnlineStock(new StockKey(stock.getInventoryType(), stock.getInventoryId()), stock.getDay());
//    }

    @Override
    public Optional<Stock> addStock(Stock stock) throws BaseSystemException {
        StockParamsValidater.validateBoundParams(stock);

        if (null != stock.getId()) {
            throw new BaseSystemException(InventoryBizError.INVALID_ID);
        }

        // TODO: 放在库存的服务层来处理
        // 检查是否存在同样的库存记录
//        if (isExisted(stock)) {
//            throw new BaseSystemException(InventoryBizError.STOCK_IS_EXISTING);
//        }

        // TODO: 放在库存托管对象的服务层来处理
        // 检查库存依赖是否存在
//        List<StockKey> dependencies = getStockDependencies(new StockKey(inventoryType, inventoryTypeId), day);
//        dependencies.forEach(dependency -> {
//            Optional<Stock> stockOptional = getStock(dependency, day, true, false);
//            if (!stockOptional.isPresent() || null == stockDao.getOnlineStockDependency(dependency.getInventoryType(), dependency.getInventoryId())) {
//                throw new BaseSystemException(InventoryBizError.STOCK_DEPENDENCY_NOT_EXIST);
//            }
//        });

        return Optional.ofNullable(stockDao.save(stock));
    }

//    @Override
//    public List<Stock> addStocks(final List<Stock> stocks) throws BaseSystemException {
//        if (null != stocks && !stocks.isEmpty()) {
//            List<Stock> stocksList = new ArrayList<>(stocks.size());
//            stocks.forEach(stock -> stocksList.add(addStock(stock).get()));
//            return stocksList;
//        }
//        return stocks;
//    }

    @Override
    public Optional<Stock> updateStock(final Stock stock) throws BaseSystemException {
        StockParamsValidater.validateBoundParams(stock);

        if (null == stock.getId()) {
            throw new BaseSystemException(InventoryBizError.INVALID_ID);
        }

        // TODO: 放在库存的服务层来处理
        // 检查是否存在同样的库存记录
//        if (!isExisted(stock)) {
//            throw new BaseSystemException(InventoryBizError.STOCK_NOT_EXIST);
//        }

        // TODO: 放在库存托管对象的服务层来处理
        // 检查库存托管对象是否下架
//        if (null == stockDao.getOnlineStockDependency(inventoryType, inventoryTypeId)) {
//            throw new BaseSystemException(InventoryBizError.STOCK_SOLD_OUT);
//        }

        // TODO: 放在库存托管对象的服务层来处理
        // 检查库存数量的有效性
//        int soldQuantity = soldEntryDao.getSoldQuantity(inventoryType, inventoryTypeId, day, true);
//        if (stock.getQuantity() < soldQuantity) {
//            throw new BaseSystemException(InventoryBizError.INVALID_STOCK_QUANTITY);
//        }

        return Optional.ofNullable(stockDao.save(stock));
    }

//    @Override
//    public List<Stock> updateStocks(final List<Stock> stocks) throws BaseSystemException {
//        if (null != stocks && !stocks.isEmpty()) {
//            List<Stock> stocksList = new ArrayList<>(stocks.size());
//            stocks.forEach(stock -> stocksList.add(updateStock(stock).get()));
//            return stocksList;
//        }
//        return stocks;
//    }


    @Override
    public void deleteStock(Stock stock) {
        stockDao.remove(stock);
    }

//    @Override
//    public void deleteStock(Long id) throws BaseSystemException {
//        Optional<Stock> optional = getStockById(id);
//        if (!optional.isPresent()) {
//            throw new BaseSystemException(InventoryBizError.STOCK_NOT_EXIST);
//        }
//        stockDao.remove(optional.get());
//    }

//    @Override
//    public void deleteStocks(List<Long> ids) {
//        if (null != ids && !ids.isEmpty()) {
//            ids.stream().filter(id -> null != id).forEach(id -> deleteStock(id));
//        }
//    }

//    @Override
//    public void deleteStocks(List<Stock> stocks) {
//        if (null != stocks) {
//            stocks.forEach(stock -> stockDao.remove(stock));
//        }
//    }

//    @Override
//    public Optional<Stock> getStockById(Long id) {
//        if (null == id || 0 >= id) {
//            throw new BaseSystemException(InventoryBizError.INVALID_ID);
//        }
//        return Optional.ofNullable(stockDao.getEntityById(Stock.class, id));
//    }

    @Transactional
    public void deleteStocksByInventoryId(InventoryType type, Long id) {
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("inventoryType", type);
        conditionSet.addCondition("inventoryTypeId", id);
        stockDao.removeEntityByConditions(Stock.class, conditionSet);
    }

//    @Override
//    public BaseDomain getStockWithDependencies(final StockKey stockKey, final Date day) throws BaseSystemException {
//        InventoryType inventoryType = stockKey.getInventoryType();
//        Long inventoryTypeId = stockKey.getInventoryId();
//        BaseDomain baseDomain = stockDao.getStockDependency(inventoryType, inventoryTypeId);
//        if (null == baseDomain) {
//            throw new BaseSystemException(InventoryBizError.STOCK_DEPENDENCY_NOT_EXIST);
//        }
//
//        return baseDomain;
//    }
//
//    @Override
//    public List<StockKey> getStockDependencies(final StockKey stockKey, final Date day) throws BaseSystemException {
//        List<StockKey> dependencies = Collections.emptyList();
//        BaseDomain baseDomain = getStockWithDependencies(stockKey, day);
//        if (null != baseDomain) {
//            if (baseDomain instanceof Product) {
//                dependencies = ((Product) baseDomain).getDependentStockKeys();
//            }
//        }
//        return dependencies;
//    }
//
//    @Override
//    public boolean hasOnlineStock(final StockKey stockKey, Date day) {
//        if (null == stockKey) {
//            return false;
//        }
//
//        Map fieldNameValueMap = new HashMap<String, Object>(4);
//        fieldNameValueMap.put("online", true);
//        fieldNameValueMap.put("inventoryType", stockKey.getInventoryType());
//        fieldNameValueMap.put("inventoryTypeId", stockKey.getInventoryId());
//
//        if (null != day) {
//            Date date = (Date)day.clone();
//            int offset = stockKey.getOffset();
//            if (0 < offset) {
//                date = Date.from(day.toInstant().plus(offset, ChronoUnit.DAYS));
//            }
//            fieldNameValueMap.put("day", date);
//        }
//
//        return 0 < stockDao.getEntityTotalCount(Stock.class, fieldNameValueMap);
//    }

//    @Override
//    public DomainPage getStocksPagesByConditions(final StockKey stockKey, final Date startDate, final Date endDate, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize) {
//        if (null == stockKey) {
//            return new DomainPage();
//        }
//
//        // 计算某一时间段范围内的库存量
//        // 方式1：通过嵌套调用SQL来查询
////        DomainPage domainPage = stockDao.getStocksPagesByConditions(stockKey, startDate, endDate, true, orderByFiledName, orderBy, pageIndex, pageSize);
////        List<Stock> stocksList = domainPage.getDomains();
////        stocksList.forEach(stock -> calculateStockQuantity(stock));
//
//        // 方式2：通过关联SQL来查询
//        StockQuery stockQuery = new StockQuery(stockKey, startDate, endDate, true, null, null, orderByFiledName, orderBy, pageSize, pageIndex);
//        //DomainPage domainPage = stockDao.getStocksQuantitiesPagesByConditions(stockQuery);
//        return new DomainPage();
//    }

//    @Override
//    public DomainPage getStocksQuantitiesPagesByRelations(final StockQuery stockQuery) throws BaseSystemException {
//        DomainPage domainPage = stockDao.getStocksQuantitiesPagesByRelations(stockQuery);
//        List<Stock> stocksList = domainPage.getDomains();
//        // 根据依赖库存目前的最小库存量来设置当前库存数量
//        stocksList.forEach(stock -> {
//            List<StockKey> dependencies = getStockDependencies(new StockKey(stock.getInventoryType(), stock.getInventoryId()), stock.getDay());
//            setStockAvailableQuantity(dependencies, stock);
//        });
//        return domainPage;
//    }
//
//    @Override
//    public BigDecimal getStocksPriceInTimeframe(StockQuery stockQuery, boolean isLowest) throws BaseSystemException {
//        Optional<Object> optional = stockDao.getFieldValueByConditions(stockQuery, isLowest ? "MIN(price)" : "MAX(price)");
//        if (!optional.isPresent()) {
//            throw new BaseSystemException(InventoryBizError.GET_STOCK_PRICE_FAILED);
//        }
//        return (BigDecimal) optional.get();
//    }
//
//    @Override
//    public Optional<Stock> getStock(IStockTraceable trace, Date day, Boolean online) throws BaseSystemException {
//        Optional<Stock> stockOptional = getStock(trace.getStockKey(), day, online, true);
//        if (stockOptional.isPresent()) {
//            // 根据库存依赖来计算可用库存量
//            setStockAvailableQuantity(trace.getDependentStockKeys(), stockOptional.get());
//        }
//        return stockOptional;
//    }
//
//    @Override
//    public Optional<Stock> getOnlineStock(IStockTraceable trace, Date day) throws BaseSystemException {
//        return getStock(trace, day, true);
//    }
//
//    @Override
//    public Optional<Stock> getRelationalStock(IStockTraceable trace, Date day) throws BaseSystemException {
//        return getStock(trace, day, true);
//    }
//
//    @Override
//    public List<Stock> getStocks(IStockTraceable trace, Date startDate, Date endDate, Boolean online) throws BaseSystemException {
//        List<Stock> stocksList = getStocksList(trace, startDate, endDate, online);
//        if (!stocksList.isEmpty()) {
//            // 根据依赖库存的可用库存量来设置当前的库存数量
//            List<StockKey> dependencies = trace.getDependentStockKeys();
//            stocksList.forEach(stock -> setStockAvailableQuantity(dependencies, stock));
//        }
//        return stocksList;
//    }
//
//    @Override
//    public List<Stock> getOnlineStocks(IStockTraceable trace, Date startDate, Date endDate) throws BaseSystemException {
//        return getStocksList(trace, startDate, endDate, true);
//    }
//
//    @Override
//    public List<Stock> getRelationalStocks(IStockTraceable trace, Date startDate, Date endDate) throws BaseSystemException {
//        return getStocks(trace, startDate, endDate, true);
//    }
//
//    @Override
//    public void checkDemandQuantity(IStockAvailable stockCheckable) throws BaseSystemException {
//        //logger.info("enter checkDemandQuantity...");
//
//        if (null == stockCheckable) {
//            throw new BaseSystemException(InventoryBizError.EMPTY_ENTITY);
//        }
//
//        List<StockAvailableKey> stockAvailableEntries = stockCheckable.getStockAvailableEntries();
//        if (null == stockAvailableEntries || stockAvailableEntries.isEmpty()) {
//            throw new BaseSystemException(InventoryBizError.INVALID_STOCK_PARAM);
//        }
//
//        // 预先统计每个订单项在下单后剩余的库存量
//        Stream<Integer> stockAvailableQuantities = stockAvailableEntries.stream().map(item -> {
//            Optional<Stock> stockOptional = getStock(item.getStockTraceable(), item.getDay(), true);
//            if (stockOptional.isPresent()) {
//                Stock stock = stockOptional.get();
//                //logger.info("prepare to check the order item: " + item.toString() + ", stock data: " + stock.toString());
//                return stock.getAvailableQuantity() - item.getDemandQuantity();
//            }
//            return 0;
//            //return stockOptional.isPresent() ? stockOptional.get().getAvailableQuantity() - item.getDemandQuantity() : 0;
//        });
//        if (stockAvailableQuantities.anyMatch(left -> left < 0)) {   // 库存已售罄，无法完成本次订单
//            throw new BaseSystemException(InventoryBizError.STOCK_SOLD_OUT);
//        }
//
//        stockAvailableEntries = stockAvailableEntries.stream().filter(stockAvailableKey -> null != stockAvailableKey && null != stockAvailableKey.getStockTraceable()).collect(Collectors.toList());
//        List<StockAvailableKey> mergedStockAvailableKeys = new ArrayList<>(stockAvailableEntries.size());
//        stockAvailableEntries.forEach(stockAvailableKey -> {
//            StockAvailableKey existence = getSameStockDependency(mergedStockAvailableKeys, stockAvailableKey);
//            if (null != existence) {
//                //logger.info("the existing order item: " + existence.toString());
//                existence.setDemandQuantity(existence.getDemandQuantity() + stockAvailableKey.getDemandQuantity());
//            } else {
//                //logger.info("the current order item: " + stockAvailableKey.toString());
//                mergedStockAvailableKeys.add(stockAvailableKey);
//            }
//        });
//
//        // 检查合并后的下单库存量是否够用
//        Stream<Integer> dependStockAvailables = mergedStockAvailableKeys.stream().map(item -> {
//            Optional<Stock> stockOptional = getStock(item.getStockTraceable(), item.getDay(), true);
//            if (stockOptional.isPresent()) {
//                Stock stock = stockOptional.get();
//                //logger.info("merged stock data: " + stock.toString());
//                return stock.getAvailableQuantity() - item.getDemandQuantity();
//            }
//            return 0;
//        });
//        if (dependStockAvailables.anyMatch(left -> left < 0)) {   // 库存量不足，无法完成本次订单
//            throw new BaseSystemException(InventoryBizError.DEPENDENT_STOCK_SHORTAGE);
//        }
//
//        //logger.info("exit checkDemandQuantity.");
//    }

    private StockAvailableKey getSameStockDependency(final List<StockAvailableKey> stockAvailableKeys, final StockAvailableKey stockAvailableKey) {
        IStockTraceable orderStockTraceable = stockAvailableKey.getStockTraceable();
        StockKey orderStockKey = orderStockTraceable.getStockKey();
        List<StockKey> orderStockKeys = orderStockTraceable.getDependentStockKeys();
        Date orderDate = stockAvailableKey.getDay();

        for (StockAvailableKey item : stockAvailableKeys) {
            IStockTraceable stockTraceable = item.getStockTraceable();
            StockKey stockKey = stockTraceable.getStockKey();
            Date date = item.getDay();

            // 检查两个顶层的订单项是否相同
            if (isSameStockDependency(orderStockKey, orderDate, stockKey, date)) {
                return item;
            }

            // 检查当前订单项是否存在于另一个订单项的库存依赖当中
            for (StockKey order : orderStockKeys) {
                if (isSameStockDependency(order, orderDate, stockKey, date)) {
                    return item;
                }
            }

            // 检查订单项依赖的库存是否存在重复
            List<StockKey> stockKeys = stockTraceable.getDependentStockKeys();
            for (StockKey key : stockKeys) {
                if (isSameStockDependency(orderStockKey, orderDate, key, date)) {
                    return item;
                }

                // 嵌套检查彼此间的库存依赖是否存在重复
                for (StockKey order : orderStockKeys) {
                    if (isSameStockDependency(order, orderDate, key, date)) {
                        return item;
                    }
                }
            }
        }

        return null;
    }

    private boolean isSameStockDependency(StockKey stockKey1, Date date1, StockKey stockKey2, Date date2) {
        if (null == stockKey1 || null == stockKey2 || null == date1 || null == date2) {
            return false;
        }

        // 由于Date的时间精度为毫秒，因此不能使用Date的equals方法来比较，订单中指定的库存日期以天为粒度，因此需要转换
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (stockKey1.equals(stockKey2)) {
            return df.format(date1).equals(df.format(date2));
        }

        Date toDate1 = Date.from(date1.toInstant().plus(stockKey1.getOffset(), ChronoUnit.DAYS));
        Date toDate2 = Date.from(date2.toInstant().plus(stockKey2.getOffset(), ChronoUnit.DAYS));
        return df.format(toDate1).equals(df.format(toDate2));
    }

    /**
     * 根据库存组合字段来获取某天的库存信息
     *
     * @param stockKey            库存组合字段
     * @param day                 库存日期
     * @param online              库存上线状态
     * @param calculateQuantities 计算已售库存和可售库存
     */
    private Optional<Stock> getStock(final StockKey stockKey, final Date day, Boolean online, boolean calculateQuantities) throws BaseSystemException {
        if (null != stockKey) {
            StockParamsValidater.validateStockParam(stockKey.getInventoryType(), stockKey.getInventoryId(), day);
            int offset = stockKey.getOffset();
            Date date = 0 >= offset ? day : Date.from(day.toInstant().plus(offset, ChronoUnit.DAYS));
            return calculateQuantities ? stockDao.getStockQuantitiesListByConditions(stockKey, date, online) : stockDao.getStockByConditions(stockKey, date, online);
        }
        return Optional.empty();
    }

    /**
     * 根据库存组合字段来获取某个时间段内的指定在线状态的库存列表
     *
     * @param trace     库存依托对象
     * @param startDate 库存起始日期
     * @param endDate   库存截止日期
     * @param online    库存上线状态
     */
    private List<Stock> getStocksList(IStockTraceable trace, Date startDate, Date endDate, Boolean online) throws BaseSystemException {
        StockKey stockKey = trace.getStockKey();
        if (null != stockKey) {
            StockParamsValidater.validateStockParam(stockKey.getInventoryType(), stockKey.getInventoryId(), startDate, endDate);
            return stockDao.getStocksQuantitiesListByConditions(new StockQuery(stockKey, startDate, endDate, online));
        }
        return Collections.emptyList();
    }

    /**
     * 根据库存依赖的可销售量来设定当前库存的最大可销售量
     *
     * @param dependencies 库存依赖
     * @param stock        当前库存
     */
    private void setStockAvailableQuantity(final List<StockKey> dependencies, Stock stock) {
        if (null == stock || null == stock.getDay()) {
            return;
        }

        Boolean online = stock.isOnline();
        if (null != online && !online) {
            stock.setAvailableQuantity(0);
            return;
        }

        if (null != dependencies && !dependencies.isEmpty()) {
            final Date DAY = stock.getDay();
            final int SIZE = dependencies.size();
            List<Integer> availableQuantities = new ArrayList<>(SIZE);
            Map<StockKey, Map<Integer, Integer>> availableStocks = new HashMap<>(SIZE);

            dependencies.forEach(dependency -> {
                if (availableStocks.containsKey(dependency)) {
                    Map<Integer, Integer> availableStock = availableStocks.get(dependency);
                    Map.Entry<Integer, Integer> entry = availableStock.entrySet().iterator().next();
                    availableStock.put(entry.getKey(), entry.getValue() + 1);
                    return;
                } else {
                    Optional<Stock> stockOptional = getStock(dependency, DAY, null, true);
                    Integer availableQuantity = !stockOptional.isPresent() || !stockOptional.get().isOnline() ? 0 : stockOptional.get().getAvailableQuantity();
                    availableStocks.put(dependency, new HashMap<Integer, Integer>() {{
                        put(availableQuantity, 1);
                    }});
                }
            });

            Iterator<Map<Integer, Integer>> iter = availableStocks.values().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, Integer> entry = iter.next().entrySet().iterator().next();
                availableQuantities.add(entry.getKey() / entry.getValue());
            }

            Integer min = availableQuantities.stream().filter(item -> null != item && 0 <= item).reduce(Integer.MAX_VALUE, Integer::min);
            stock.setAvailableQuantity(Integer.min(stock.getAvailableQuantity(), min));
        }
    }
}
