package com.simpletour.service.sale.imp;

import com.simpletour.biz.sale.IAgreementProductPriceBiz;
import com.simpletour.biz.sale.error.AgreementProductPriceBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProductPrice;
import com.simpletour.service.sale.IAgreementProductPriceService;
import com.simpletour.service.sale.error.AgreementProductPriceServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @Brief :  ${用途}
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/21 10:56
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@Service
public class AgreementProductPriceServiceImp implements IAgreementProductPriceService {

    @Autowired
    private IAgreementProductPriceBiz agreementProductPriceBiz;


    @Override
    public Optional<AgreementProductPrice> addAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException {
        checkNull(agreementProductPrice);

        return null;
    }

    @Override
    public Optional<AgreementProductPrice> updateAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException {
        return null;
    }

    @Override
    public DomainPage<AgreementProductPrice> queryAgreementProductPrice(ConditionOrderByQuery query) {
        return null;
    }

    @Override
    public DomainPage<AgreementProductPrice> queryAgreementProductPrice(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return null;
    }

    private void checkNull(AgreementProductPrice agreementProductPrice) {
        if (agreementProductPrice == null)
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_NULL);
    }

    private void checkExist(AgreementProductPrice agreementProductPrice) {
        boolean bool = agreementProductPriceBiz.isAgreementProductPriceExist(agreementProductPrice.getAgreement().getId(), agreementProductPrice.getProduct().getId(), agreementProductPrice.getDate());
        if (bool)
            throw new BaseSystemException(AgreementProductPriceServiceError.AGREEMENT_PRODUCT_PRICE_EXIST);
    }
}
