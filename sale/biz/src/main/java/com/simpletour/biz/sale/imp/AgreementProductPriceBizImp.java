package com.simpletour.biz.sale.imp;

import com.simpletour.biz.sale.IAgreementProductPriceBiz;
import com.simpletour.biz.sale.error.AgreementProductPriceBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IAgreementDao;
import com.simpletour.domain.sale.AgreementProductPrice;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Brief :  产品价格的实现类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/20 17:00
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public class AgreementProductPriceBizImp implements IAgreementProductPriceBiz {

    @Autowired
    private IAgreementDao agreementDao;

    @Override
    public AgreementProductPrice addAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException {
        checkNull(agreementProductPrice);
        return agreementDao.save(agreementProductPrice);
    }

    @Override
    public AgreementProductPrice updateAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException {
        return null;
    }

    @Override
    public void delAgreementProductPrice(Long id) throws BaseSystemException {

    }

    @Override
    public AgreementProductPrice findAgreementProductPriceById(Long id) {
        return null;
    }

    @Override
    public DomainPage<AgreementProductPrice> queryAgreementProductPricePageByCondition(ConditionOrderByQuery query, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public boolean isAgreementExist(Long id) {
        return false;
    }

    @Override
    public boolean isProductExist(Long id) {
        return false;
    }

    private void checkNull(AgreementProductPrice agreementProductPrice) {
        if (agreementProductPrice == null)
            throw new BaseSystemException(AgreementProductPriceBizError.AGREEMENT_PRODUCT_PRICE_EMPTY);
    }

}
