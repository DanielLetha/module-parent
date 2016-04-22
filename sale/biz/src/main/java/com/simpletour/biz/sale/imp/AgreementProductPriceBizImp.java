package com.simpletour.biz.sale.imp;

import com.simpletour.biz.sale.IAgreementProductPriceBiz;
import com.simpletour.biz.sale.bo.AgreementPriceBo;
import com.simpletour.biz.sale.error.AgreementProductPriceBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IAgreementProductPriceDao;
import com.simpletour.domain.sale.AgreementProduct;
import com.simpletour.domain.sale.AgreementProductPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Brief :  产品价格的实现类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/20 17:00
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@Component
public class AgreementProductPriceBizImp implements IAgreementProductPriceBiz {

    @Autowired
    private IAgreementProductPriceDao agreementProductPriceDao;


    @Override
    public AgreementPriceBo addAgreementProductPrice(AgreementPriceBo agreementPriceBo) throws BaseSystemException {
        checkNull(agreementPriceBo);
        List<AgreementProductPrice> prices = agreementPriceBo.asList().stream().map(
                agreementPrice -> agreementProductPriceDao.save(agreementPrice)).collect(Collectors.toList());
        return AgreementPriceBo.from(prices).get(0);
    }

    @Override
    public AgreementPriceBo updateAgreementProductPrice(AgreementPriceBo agreementPriceBo) throws BaseSystemException {
        checkNull(agreementPriceBo);
        List<AgreementProductPrice> prices = agreementPriceBo.asList().stream().map(
                agreementPrice -> agreementProductPriceDao.save(agreementPrice)).collect(Collectors.toList());
        return AgreementPriceBo.from(prices).get(0);
    }

    @Override
    public void deleteAgreementProductPrice(Long agreementId, Date date) throws BaseSystemException {
        ConditionOrderByQuery conditionOrderByQuery = new ConditionOrderByQuery();
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("agreementId", agreementId);
        conditionSet.addCondition("date", date);
        conditionOrderByQuery.setCondition(conditionSet);
        List<AgreementPriceBo> agreementPriceBos = getAgreementProductPriceList(conditionOrderByQuery);
        if (agreementPriceBos.size() == 1) {
            agreementPriceBos.get(0).asList().stream().forEach(agrementPrice -> agreementProductPriceDao.remove(agrementPrice));
        }

    }


    @Override
    public List<AgreementPriceBo> getAgreementProductPriceList(ConditionOrderByQuery query) {
        List<AgreementProductPrice> prices = agreementProductPriceDao.getEntitiesByQuery(AgreementProductPrice.class, query);
        return AgreementPriceBo.from(prices);
    }


    private void checkNull(AgreementPriceBo agreementProductPrice) {
        if (agreementProductPrice == null)
            throw new BaseSystemException(AgreementProductPriceBizError.AGREEMENT_PRODUCT_PRICE_EMPTY);
        if (agreementProductPrice == null || agreementProductPrice.getAgreementProduct().getId() == null)
            throw new BaseSystemException(AgreementProductPriceBizError.AGREEMENT_PRODUCT_NULL);


    }
}
