package com.simpletour.biz.sale.imp;

import com.simpletour.biz.sale.IAgreementProductPriceBiz;
import com.simpletour.biz.sale.error.AgreementProductPriceBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IAgreementProductPriceDao;
import com.simpletour.domain.sale.AgreementProductPrice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Brief :  产品价格的实现类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/20 17:00
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public class AgreementProductPriceBizImp implements IAgreementProductPriceBiz {

    @Autowired
    private IAgreementProductPriceDao agreementProductPriceDao;


    @Override
    public AgreementProductPrice addAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException {
        checkNull(agreementProductPrice);
        return agreementProductPriceDao.save(agreementProductPrice);
    }

    @Override
    public AgreementProductPrice updateAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException {
        checkNull(agreementProductPrice);
        return agreementProductPriceDao.save(agreementProductPrice);
    }

    @Override
    public AgreementProductPrice findAgreementProductPriceById(Long id) {
        return agreementProductPriceDao.getEntityById(AgreementProductPrice.class, id);
    }

    @Override
    public DomainPage<AgreementProductPrice> queryAgreementProductPricePageByCondition(ConditionOrderByQuery query) {
        return agreementProductPriceDao.getEntitiesPagesByQuery(AgreementProductPrice.class, query);
    }

    @Override
    public boolean isAgreementProductPriceExist(Long agreementId, Long productId, Date date, String type) {
        ConditionOrderByQuery conditionOrderByQuery = new ConditionOrderByQuery();
        AndConditionSet condition = new AndConditionSet();
        condition.addCondition("productId", productId);
        condition.addCondition("agreementId", agreementId);
        condition.addCondition("date", date);
        condition.addCondition("type", type);
        conditionOrderByQuery.setCondition(condition);
        List<AgreementProductPrice> agreementProductPriceList = getAgreementProductList(conditionOrderByQuery);
        if (agreementProductPriceList.isEmpty() || agreementProductPriceList.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<AgreementProductPrice> getAgreementProductList(ConditionOrderByQuery query) {
        return agreementProductPriceDao.getEntitiesByQuery(AgreementProductPrice.class, query);
    }

    @Override
    public DomainPage<AgreementProductPrice> queryAgreementProductPricePageByCondition(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return agreementProductPriceDao.queryEntitiesPagesByFieldList(AgreementProductPrice.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    private void checkNull(AgreementProductPrice agreementProductPrice) {
        if (agreementProductPrice == null)
            throw new BaseSystemException(AgreementProductPriceBizError.AGREEMENT_PRODUCT_PRICE_EMPTY);
        if (agreementProductPrice.getAgreement() == null || agreementProductPrice.getAgreement().getId() == null)
            throw new BaseSystemException(AgreementProductPriceBizError.AGREEMENT_NULL);
        if (agreementProductPrice.getProduct() == null || agreementProductPrice.getProduct().getId() == null)
            throw new BaseSystemException(AgreementProductPriceBizError.AGREEMENT_PRODUCT_NULL);

    }

}
