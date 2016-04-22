package com.simpletour.service.sale.imp;

import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.sale.IAgreementBiz;
import com.simpletour.biz.sale.IAgreementProductPriceBiz;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProductPrice;
import com.simpletour.service.sale.IAgreementProductPriceService;
import com.simpletour.service.sale.error.AgreementProductPriceServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Brief :  销售产品价格的实现类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/21 10:56
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@Service
public class AgreementProductPriceServiceImp implements IAgreementProductPriceService {

    @Autowired
    private IAgreementProductPriceBiz agreementProductPriceBiz;
    @Autowired
    private IAgreementBiz agreementBiz;
    @Autowired
    private IProductBiz productBiz;


    @Override
    public Optional<AgreementProductPrice> addAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException {
        checkNull(agreementProductPrice);
        checkExist(agreementProductPrice);
        checkAgreementAvailable(agreementProductPrice);
        checkProductAvailable(agreementProductPrice);
        return Optional.ofNullable(agreementProductPriceBiz.addAgreementProductPrice(agreementProductPrice));
    }

    @Override
    public Optional<AgreementProductPrice> updateAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException {
        checkNull(agreementProductPrice);
        List<AgreementProductPrice> list = getAgreementProductPriceList(agreementProductPrice.getAgreement().getId(), agreementProductPrice.getProduct().getId(), agreementProductPrice.getDate(), agreementProductPrice.getType().name());
        if (list.size() > 0 && !list.get(0).getId().equals(agreementProductPrice.getId())) {
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_MUST_ONLY);
        }
        checkAgreementAvailable(agreementProductPrice);
        checkAgreementAvailable(agreementProductPrice);
        checkProductAvailable(agreementProductPrice);
        return Optional.ofNullable(agreementProductPriceBiz.addAgreementProductPrice(agreementProductPrice));
    }

    @Override
    public DomainPage<AgreementProductPrice> queryAgreementProductPrice(ConditionOrderByQuery query) {
        return agreementProductPriceBiz.queryAgreementProductPricePageByCondition(query);
    }

    @Override
    public DomainPage<AgreementProductPrice> queryAgreementProductPriceByCondtion(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return agreementProductPriceBiz.queryAgreementProductPricePageByCondition(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public Optional<AgreementProductPrice> findAgreementProductPriceById(Long id) {
        return Optional.ofNullable(agreementProductPriceBiz.findAgreementProductPriceById(id));
    }

    private void checkNull(AgreementProductPrice agreementProductPrice) {
        if (agreementProductPrice == null)
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_NULL);
    }

    private void checkExist(AgreementProductPrice agreementProductPrice) {
        boolean bool = agreementProductPriceBiz.isExisted(agreementProductPrice.getAgreement().getId(), agreementProductPrice.getProduct().getId(), agreementProductPrice.getDate(), agreementProductPrice.getType().name());
        if (bool)
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_EXIST);
    }

    private void checkAgreementAvailable(AgreementProductPrice agreementProductPrice) {
        if (!agreementBiz.isAvailable(agreementProductPrice.getId()))
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_NOT_EXIST);
    }

    private void checkProductAvailable(AgreementProductPrice agreementProductPrice) {
        if (!productBiz.isExist(agreementProductPrice.getId()))
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_NOT_EXIST);
    }


    private List<AgreementProductPrice> getAgreementProductPriceList(Long agreementId, Long productId, Date date, String type) {
        ConditionOrderByQuery conditionOrderByQuery = new ConditionOrderByQuery();
        AndConditionSet condition = new AndConditionSet();
        condition.addCondition("productId", productId);
        condition.addCondition("agreementId", agreementId);
        condition.addCondition("date", date);
        condition.addCondition("type", type);
        conditionOrderByQuery.setCondition(condition);
        return agreementProductPriceBiz.getAgreementProductPriceList(conditionOrderByQuery);
    }


}
