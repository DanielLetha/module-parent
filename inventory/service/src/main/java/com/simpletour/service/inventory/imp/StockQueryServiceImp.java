package com.simpletour.service.inventory.imp;

import com.simpletour.biz.inventory.IStockQueryBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.query.*;
import com.simpletour.service.inventory.IStockQueryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：库存关联信息查询服务层实现类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/21 16:03
 * 备注说明：null
 *
 * @since 2.0-SNAPSHOT
 */
public class StockQueryServiceImp implements IStockQueryService {
    @Autowired
    private IStockQueryBiz stockQueryBiz;

    @Override
    public Optional<Stock> getStockById(long id) {
        return stockQueryBiz.getStockById(id);
    }

    @Override
    public boolean stockIsExisted(Stock stock) throws BaseSystemException {
        return stockQueryBiz.stockIsExisted(stock);
    }

//    @Override
//    public BaseDomain getStockWithDependencies(StockKey stockKey, Date day) {
//        return stockBiz.getStockWithDependencies(stockKey, day);
//    }
//
//    @Override
//    public List<StockKey> getStockDependencies(StockKey stockKey, Date day) {
//        return stockBiz.getStockDependencies(stockKey, day);
//    }

    @Override
    public boolean hasOnlineStock(final StockKey stockKey, Date day) {
        return stockQueryBiz.hasOnlineStock(stockKey, day);
    }

//    @Override
//    public DomainPage getStocksPagesByConditions(StockKey stockKey, Date startDate, Date endDate, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize) {
//        return stockBiz.getStocksPagesByConditions(stockKey, startDate, endDate, orderByFiledName, orderBy, pageIndex, pageSize);
//    }

    @Override
    public DomainPage getStocksQuantitiesPagesByRelations(final StockQuery stockQuery) throws BaseSystemException {
        return stockQueryBiz.getStocksQuantitiesPagesByRelations(stockQuery);
    }

    @Override
    public Optional<BigDecimal> getStocksPriceInTimeframe(StockQuery stockQuery, boolean isLowest) {
        BigDecimal lowestPrice = null;

        try {
            lowestPrice = stockQueryBiz.getStocksPriceInTimeframe(stockQuery, isLowest);
        } catch (BaseSystemException bse) {
            String errorMsg = bse.getMessage();
            if (InventoryBizError.GET_STOCK_PRICE_FAILED.getErrorMessage().equals(errorMsg)) {
                return Optional.empty();
            } else {
                bse.printStackTrace();
            }
        }

        return Optional.of(lowestPrice);
    }

    @Override
    public Optional<Stock> getStock(IStockTraceable trace, Date day, Boolean online) throws BaseSystemException {
        return stockQueryBiz.getStock(trace, day, online);
    }

    @Override
    public List<Stock> getStocks(IStockTraceable trace, Date startDate, Date endDate, Boolean online) throws BaseSystemException {
        return stockQueryBiz.getStocks(trace, startDate, endDate, online);
    }

    @Override
    public void checkDemandQuantity(IStockAvailable stockCheckable) throws BaseSystemException {
        stockQueryBiz.checkDemandQuantity(stockCheckable);
    }

    @Override
    public Optional<SoldEntry> getSoldEntryById(Long id) {
        return stockQueryBiz.getSoldEntryById(id);
    }

    @Override
    public List<SoldEntry> getSoldEntriesByUnionKeys(SoldEntryKey soldEntryKey) {
        return stockQueryBiz.getSoldEntriesByUnionKeys(soldEntryKey);
    }

    @Override
    public int getAvailableSoldQuantity(SoldEntryKey soldEntryKey, Date day) {
        return stockQueryBiz.getAvailableSoldQuantity(soldEntryKey, day);
    }
}
