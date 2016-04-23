package com.simpletour.service.inventory.imp;

import com.simpletour.biz.inventory.IPriceBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.Price;
import com.simpletour.service.inventory.IPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：xx类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/21 9:44
 * 备注说明：null
 *
 * @since 2.0-SNAPSHOT
 */
@Service
public class PriceServiceImp implements IPriceService {
//    @Autowired
//    private IStockQueryBiz stockQueryBiz;

    @Autowired
    IPriceBiz priceBiz;

    @Override
    public Optional<Price> addPrice(Price price) throws BaseSystemException {
        if (priceBiz.isExisted(price)) {
            throw new BaseSystemException(InventoryBizError.PRICE_IS_EXISTING);
        }
        return priceBiz.addPrice(price);
    }

    @Override
    public List<Price> addPrices(List<Price> prices) throws BaseSystemException {
        if (null != prices && !prices.isEmpty()) {
            List<Price> pricesList = new ArrayList<>(prices.size());
            prices.forEach(price -> pricesList.add(addPrice(price).get()));
            return pricesList;
        }
        return prices;
    }

    @Override
    public Optional<Price> updatePrice(Price price) throws BaseSystemException {
        if (!priceBiz.isExisted(price)) {
            throw new BaseSystemException(InventoryBizError.PRICE_NOT_EXIST);
        }
        return priceBiz.updatePrice(price);
    }

    @Override
    public List<Price> updatePrices(List<Price> prices) throws BaseSystemException {
        if (null != prices && !prices.isEmpty()) {
            List<Price> pricesList = new ArrayList<>(prices.size());
            prices.forEach(price -> pricesList.add(updatePrice(price).get()));
            return pricesList;
        }
        return prices;
    }

//    @Override
//    public void deletePrice(Long id) throws BaseSystemException {
//        Optional<Price> optional = stockQueryBiz.getPriceById(id);
//        if (!optional.isPresent()) {
//            throw new BaseSystemException(InventoryBizError.PRICE_NOT_EXIST);
//        }
//        priceBiz.deletePrice(optional.get());
//    }
//
//    @Override
//    public void deletePrices(List<Long> ids) {
//        if (null != ids) {
//            ids.stream().filter(id -> null != id && 0 < id).forEach(id -> deletePrice(id));
//        }
//    }
}
