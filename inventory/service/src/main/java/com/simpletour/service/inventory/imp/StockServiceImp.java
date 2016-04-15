package com.simpletour.service.inventory.imp;

import com.simpletour.biz.inventory.IStockBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.domain.BaseDomain;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.query.IStockAvailable;
import com.simpletour.domain.inventory.query.IStockTraceable;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.inventory.query.StockQuery;
import com.simpletour.service.inventory.IStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：库存模块服务接口实现类
 * 创建人员：石广路
 * 创建日期：2015/11/25 20:15
 * 备注说明：调用biz层实现数据校验与增删改查操作
 */
@Service
public class StockServiceImp implements IStockService {
    @Resource
    private IStockBiz stockBiz;

    @Override
    public Optional<Stock> addStock(Stock stock) throws BaseSystemException {
        return stockBiz.addStock(stock);
    }

    @Override
    public List<Stock> addStocks(List<Stock> stocks) throws BaseSystemException {
        return stockBiz.addStocks(stocks);
    }

    @Override
    public Optional<Stock> updateStock(Stock stock) throws BaseSystemException {
        return stockBiz.updateStock(stock);
    }

    @Override
    public List<Stock> updateStocks(List<Stock> stocks) throws BaseSystemException {
        return stockBiz.updateStocks(stocks);
    }

    @Override
    public void deleteStock(long id) {
        stockBiz.deleteStock(id);
    }

    @Override
    public void deleteStocks(List<Long> ids) {
        stockBiz.deleteStocks(ids);
    }

    @Override
    public Optional<Stock> getStockRecordById(long id) {
        return stockBiz.getStockRecordById(id);
    }

    @Override
    public BaseDomain getStockWithDependencies(StockKey stockKey, Date day) {
        return stockBiz.getStockWithDependencies(stockKey, day);
    }

    @Override
    public List<StockKey> getStockDependencies(StockKey stockKey, Date day) {
        return stockBiz.getStockDependencies(stockKey, day);
    }

    @Override
    public boolean hasOnlineStock(final StockKey stockKey, Date day) {
        return stockBiz.hasOnlineStock(stockKey, day);
    }

    @Override
    public DomainPage getStocksPagesByConditions(StockKey stockKey, Date startDate, Date endDate, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize) {
        return stockBiz.getStocksPagesByConditions(stockKey, startDate, endDate, orderByFiledName, orderBy, pageIndex, pageSize);
    }

    @Override
    public DomainPage getStocksQuantitiesPagesByRelations(final StockQuery stockQuery) throws BaseSystemException {
        return stockBiz.getStocksQuantitiesPagesByRelations(stockQuery);
    }

    @Override
    public Optional<BigDecimal> getStocksPriceInTimeframe(StockQuery stockQuery, boolean isLowest) {
        BigDecimal lowestPrice = null;

        try {
            lowestPrice = stockBiz.getStocksPriceInTimeframe(stockQuery, isLowest);
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
        return stockBiz.getStock(trace, day, online);
    }

    @Override
    public Optional<Stock> getOnlineStock(IStockTraceable trace, Date day) throws BaseSystemException {
        return stockBiz.getOnlineStock(trace, day);
    }

    @Override
    public Optional<Stock> getRelationalStock(IStockTraceable trace, Date day) throws BaseSystemException {
        return stockBiz.getRelationalStock(trace, day);
    }

    @Override
    public List<Stock> getStocks(IStockTraceable trace, Date startDate, Date endDate, Boolean online) throws BaseSystemException {
        return stockBiz.getStocks(trace, startDate, endDate, online);
    }

    @Override
    public List<Stock> getOnlineStocks(IStockTraceable trace, Date startDate, Date endDate) throws BaseSystemException {
        return stockBiz.getOnlineStocks(trace, startDate, endDate);
    }

    @Override
    public List<Stock> getRelationalStocks(IStockTraceable trace, Date startDate, Date endDate) throws BaseSystemException {
        return stockBiz.getRelationalStocks(trace, startDate, endDate);
    }

    @Override
    public void checkDemandQuantity(IStockAvailable stockCheckable) throws BaseSystemException {
        stockBiz.checkDemandQuantity(stockCheckable);
    }
}
