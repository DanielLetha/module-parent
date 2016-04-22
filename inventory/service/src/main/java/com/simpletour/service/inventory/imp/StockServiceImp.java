package com.simpletour.service.inventory.imp;

import com.simpletour.biz.inventory.IStockBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.service.inventory.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
//    @Autowired
//    private IStockQueryBiz stockQueryBiz;

    @Autowired
    private IStockBiz stockBiz;

    @Override
    public Optional<Stock> addStock(Stock stock) throws BaseSystemException {
        if (stockBiz.isExisted(stock)) {
            throw new BaseSystemException(InventoryBizError.STOCK_IS_EXISTING);
        }
        return stockBiz.addStock(stock);
    }

    @Override
    public List<Stock> addStocks(List<Stock> stocks) throws BaseSystemException {
        if (null != stocks && !stocks.isEmpty()) {
            List<Stock> stocksList = new ArrayList<>(stocks.size());
            stocks.forEach(stock -> stocksList.add(addStock(stock).get()));
            return stocksList;
        }
        return stocks;
    }

    @Override
    public Optional<Stock> updateStock(Stock stock) throws BaseSystemException {
        if (!stockBiz.isExisted(stock)) {
            throw new BaseSystemException(InventoryBizError.STOCK_NOT_EXIST);
        }
        return stockBiz.updateStock(stock);
    }

    @Override
    public List<Stock> updateStocks(List<Stock> stocks) throws BaseSystemException {
        if (null != stocks && !stocks.isEmpty()) {
            List<Stock> stocksList = new ArrayList<>(stocks.size());
            stocks.forEach(stock -> stocksList.add(updateStock(stock).get()));
            return stocksList;
        }
        return stocks;
    }

//    @Override
//    public void deleteStock(Long id) throws BaseSystemException {
//        Optional<Stock> optional = stockQueryBiz.getStockById(id);
//        if (!optional.isPresent()) {
//            throw new BaseSystemException(InventoryBizError.STOCK_NOT_EXIST);
//        }
//        stockBiz.deleteStock(optional.get());
//    }
//
//    @Override
//    public void deleteStocks(List<Long> ids) {
//        if (null != ids && !ids.isEmpty()) {
//            ids.stream().filter(id -> null != id && 0 < id).forEach(id -> deleteStock(id));
//        }
//    }

//    @Override
//    public Optional<Stock> getStockById(long id) {
//        return stockQueryBiz.getStockById(id);
//    }
//
//    @Override
//    public BaseDomain getStockWithDependencies(StockKey stockKey, Date day) {
//        return stockBiz.getStockWithDependencies(stockKey, day);
//    }
//
//    @Override
//    public List<StockKey> getStockDependencies(StockKey stockKey, Date day) {
//        return stockBiz.getStockDependencies(stockKey, day);
//    }
//
//    @Override
//    public boolean hasOnlineStock(final StockKey stockKey, Date day) {
//        return stockBiz.hasOnlineStock(stockKey, day);
//    }
//
////    @Override
////    public DomainPage getStocksPagesByConditions(StockKey stockKey, Date startDate, Date endDate, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize) {
////        return stockBiz.getStocksPagesByConditions(stockKey, startDate, endDate, orderByFiledName, orderBy, pageIndex, pageSize);
////    }
//
//    @Override
//    public DomainPage getStocksQuantitiesPagesByRelations(final StockQuery stockQuery) throws BaseSystemException {
//        return stockBiz.getStocksQuantitiesPagesByRelations(stockQuery);
//    }
//
//    @Override
//    public Optional<BigDecimal> getStocksPriceInTimeframe(StockQuery stockQuery, boolean isLowest) {
//        BigDecimal lowestPrice = null;
//
//        try {
//            lowestPrice = stockBiz.getStocksPriceInTimeframe(stockQuery, isLowest);
//        } catch (BaseSystemException bse) {
//            String errorMsg = bse.getMessage();
//            if (InventoryBizError.GET_STOCK_PRICE_FAILED.getErrorMessage().equals(errorMsg)) {
//                return Optional.empty();
//            } else {
//                bse.printStackTrace();
//            }
//        }
//
//        return Optional.of(lowestPrice);
//    }
//
//    @Override
//    public Optional<Stock> getStock(IStockTraceable trace, Date day, Boolean online) throws BaseSystemException {
//        return stockBiz.getStock(trace, day, online);
//    }
//
////    @Override
////    public Optional<Stock> getOnlineStock(IStockTraceable trace, Date day) throws BaseSystemException {
////        return stockBiz.getOnlineStock(trace, day);
////    }
////
////    @Override
////    public Optional<Stock> getRelationalStock(IStockTraceable trace, Date day) throws BaseSystemException {
////        return stockBiz.getRelationalStock(trace, day);
////    }
//
//    @Override
//    public List<Stock> getStocks(IStockTraceable trace, Date startDate, Date endDate, Boolean online) throws BaseSystemException {
//        return stockBiz.getStocks(trace, startDate, endDate, online);
//    }
//
////    @Override
////    public List<Stock> getOnlineStocks(IStockTraceable trace, Date startDate, Date endDate) throws BaseSystemException {
////        return stockBiz.getOnlineStocks(trace, startDate, endDate);
////    }
////
////    @Override
////    public List<Stock> getRelationalStocks(IStockTraceable trace, Date startDate, Date endDate) throws BaseSystemException {
////        return stockBiz.getRelationalStocks(trace, startDate, endDate);
////    }
//
//    @Override
//    public void checkDemandQuantity(IStockAvailable stockCheckable) throws BaseSystemException {
//        stockBiz.checkDemandQuantity(stockCheckable);
//    }
}
