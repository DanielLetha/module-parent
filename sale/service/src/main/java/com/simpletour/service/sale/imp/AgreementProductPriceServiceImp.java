package com.simpletour.service.sale.imp;

import com.simpletour.biz.sale.IAgreementProductBiz;
import com.simpletour.biz.sale.IAgreementProductPriceBiz;
import com.simpletour.biz.sale.bo.AgreementPriceBo;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProduct;
import com.simpletour.service.sale.IAgreementProductPriceService;
import com.simpletour.service.sale.error.AgreementProductPriceServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    private IAgreementProductBiz agreementProductBiz;


    @Override
    public Optional<AgreementPriceBo> addAgreementProductPrice(AgreementPriceBo agreementPriceBo) throws BaseSystemException {
        checkNull(agreementPriceBo);
        checkAgreementProductAvailable(agreementPriceBo);
        Optional<AgreementPriceBo> optional =  getAgreementProductPrice(agreementPriceBo.getAgreementProduct(),agreementPriceBo.getDate());
        if(optional.isPresent()){
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_MUST_ONLY);
        }
        return Optional.ofNullable(agreementProductPriceBiz.addAgreementProductPrice(agreementPriceBo));
    }

    @Override
    public Optional<AgreementPriceBo> updateAgreementProductPrice(AgreementPriceBo agreementPriceBo) throws BaseSystemException {
        checkNull(agreementPriceBo);

        AgreementPriceBo agreementPriceBo1 = agreementProductPriceBiz.getAgreementProductPrice(agreementPriceBo.getAgreementProduct(), agreementPriceBo.getDate());

        if (agreementPriceBo1 == null) {
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_AGREEMENT_NOT_EXIST_NOT_EXIST);
        }

        return Optional.ofNullable(agreementProductPriceBiz.updateAgreementProductPrice(agreementPriceBo));
    }

    @Override
    public Optional<AgreementPriceBo> getAgreementProductPrice(AgreementProduct agreementProduct, Date date) throws BaseSystemException {
        return Optional.ofNullable(agreementProductPriceBiz.getAgreementProductPrice(agreementProduct, date));
    }

    @Override
    public List<AgreementPriceBo> getAgreementProductPriceList(AgreementProduct agreementProduct) throws BaseSystemException {
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("agreementProduct", agreementProduct);
        query.setCondition(conditionSet);
        return agreementProductPriceBiz.getAgreementProductPriceList(query);
    }

    @Override
    public List<AgreementPriceBo> getAgreementProductPriceListByQuery(ConditionOrderByQuery query) {
        return agreementProductPriceBiz.getAgreementProductPriceList(query);
    }

    @Override
    public void batchInsert(List<AgreementPriceBo> agreementPriceBos) throws BaseSystemException {
        agreementPriceBos.stream().forEach(priceBo -> agreementProductPriceBiz.deleteAgreementProductPrice(priceBo.getAgreementProduct(), priceBo.getDate()));
        agreementPriceBos.stream().forEach(priceBo -> agreementProductPriceBiz.addAgreementProductPrice(priceBo));
    }


    private void checkNull(AgreementPriceBo agreementPriceBo) {
        if (agreementPriceBo == null)
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_NULL);
    }


    private void checkAgreementProductAvailable(AgreementPriceBo agreementPriceBo) {
        if (!agreementProductBiz.isExisted(agreementPriceBo.getAgreementProduct().getId()))
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_NOT_EXIST);
    }


}
