package com.simpletour.service.sale.imp;

import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.sale.IAgreementBiz;
import com.simpletour.biz.sale.IAgreementProductBiz;
import com.simpletour.biz.sale.IAgreementProductPriceBiz;
import com.simpletour.biz.sale.bo.AgreementPriceBo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProductPrice;
import com.simpletour.service.sale.IAgreementProductPriceService;
import com.simpletour.service.sale.IAgreementProductService;
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
    private IAgreementProductBiz agreementProductBiz;



    @Override
    public List<AgreementPriceBo> getAgreementProductPriceList(ConditionOrderByQuery query) {
        return agreementProductPriceBiz.getAgreementProductPriceList(query);
    }

    @Override
    public Optional<AgreementPriceBo> addAgreementProductPrice(AgreementPriceBo agreementPriceBo) throws BaseSystemException {
        checkNull(agreementPriceBo);
        checkAgreementProductAvailable(agreementPriceBo);
        return Optional.ofNullable(agreementProductPriceBiz.addAgreementProductPrice(agreementPriceBo));
    }

    @Override
    public Optional<AgreementPriceBo> updateAgreementProductPrice(AgreementPriceBo agreementPriceBo) throws BaseSystemException {
        checkNull(agreementPriceBo);

        ConditionOrderByQuery query = new ConditionOrderByQuery();
        AndConditionSet andConditionSet = new AndConditionSet();
        andConditionSet.addCondition("agreementProductId",agreementPriceBo.getAgreementProduct().getId());
        andConditionSet.addCondition("date",agreementPriceBo.getDate());
        query.setCondition(andConditionSet);
        List<AgreementPriceBo> list = getAgreementProductPriceList(query);
        if(list.size() >2){
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_MUST_ONLY);
        }
        list.stream().filter(p -> !p.getAgreementProduct().equals(agreementPriceBo.getAgreementProduct())||!p.getDate().equals(agreementPriceBo.getDate()));
        return Optional.ofNullable(agreementProductPriceBiz.addAgreementProductPrice(agreementPriceBo));
    }

    @Override
    public void batchInsert(List<AgreementPriceBo> agreementPriceBos) throws BaseSystemException {
        agreementPriceBos.stream().forEach(price -> agreementProductPriceBiz.deleteAgreementProductPrice(price.getAgreementProduct().getId(),price.getDate()));
        agreementPriceBos.stream().forEach(price -> agreementProductPriceBiz.addAgreementProductPrice(price));
    }


    private void checkNull(AgreementPriceBo agreementPriceBo) {
        if (agreementPriceBo == null)
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_NULL);
    }



    private void checkAgreementProductAvailable(AgreementPriceBo agreementPriceBo) {
        if (!agreementProductBiz.isExisted(agreementPriceBo.getAgreementProduct().getId()))
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_NOT_EXIST);
    }





}
