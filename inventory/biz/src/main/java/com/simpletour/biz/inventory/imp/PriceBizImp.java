package com.simpletour.biz.inventory.imp;

import com.simpletour.biz.inventory.IPriceBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.inventory.IStockDao;
import com.simpletour.domain.inventory.Price;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 文件描述：库存价格业务实现类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/20 21:59
 * 备注说明：null
 *
 * @since 2.0-SNAPSHOT
 */
public class PriceBizImp implements IPriceBiz {
    @Resource
    private IStockDao stockDao;

//    @Override
//    public boolean isExisting(Price price) {
//        StockParamsValidater.validateBoundParams(price);
//
//        Long id = price.getId();
//        if (null != id && 0 < id) {
//            return null != stockDao.getEntityById(Price.class, id);
//        }
//
//        Map<String, Object> fieldNameValueMap = new HashMap<>(4);
//        fieldNameValueMap.put("inventoryType", price.getInventoryType());
//        fieldNameValueMap.put("inventoryId", price.getInventoryId());
//        fieldNameValueMap.put("day", price.getDay());
//        fieldNameValueMap.put("type", price.getType());
//
//        return 0 < stockDao.getEntityTotalCount(Price.class, fieldNameValueMap);
//    }

    @Override
    public Optional<Price> addPrice(Price price) throws BaseSystemException {
        StockParamsValidater.validateBoundParams(price);

        if (null != price.getId()) {
            throw new BaseSystemException(InventoryBizError.INVALID_ID);
        }

        return Optional.ofNullable(stockDao.save(price));
    }

    @Override
    public Optional<Price> updatePrice(Price price) throws BaseSystemException {
        StockParamsValidater.validateBoundParams(price);

        if (null == price.getId()) {
            throw new BaseSystemException(InventoryBizError.INVALID_ID);
        }

        return Optional.ofNullable(stockDao.save(price));
    }

    @Override
    public void deletePrice(Price price) {
        stockDao.remove(price);
    }

//    @Override
//    public void deletePrice(Long id) throws BaseSystemException {
//        Optional<Price> optional = getPriceById(id);
//        if (!optional.isPresent()) {
//            throw new BaseSystemException(InventoryBizError.PRICE_NOT_EXIST);
//        }
//        stockDao.remove(optional.get());
//    }

//    @Override
//    public void deletePrices(List<Long> ids) throws BaseSystemException {
//        if (null != ids) {
//            ids.stream().filter(id -> null != id).forEach(id -> deletePrice(id));
//        }
//    }

//    @Override
//    public Optional<Price> getPriceById(Long id) {
//        if (null == id || 0 >= id) {
//            throw new BaseSystemException(InventoryBizError.INVALID_ID);
//        }
//        return Optional.ofNullable(stockDao.getEntityById(Price.class, id));
//    }
}
