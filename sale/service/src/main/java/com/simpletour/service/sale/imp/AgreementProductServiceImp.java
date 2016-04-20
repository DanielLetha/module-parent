package com.simpletour.service.sale.imp;

import com.simpletour.biz.sale.IAgreementProductBiz;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.sale.AgreementProduct;
import com.simpletour.service.sale.IAgreementProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
@Service
public class AgreementProductServiceImp implements IAgreementProductService{

    @Resource
    private IAgreementProductBiz agreementProductBiz;


    @Override
    public Optional<AgreementProduct> addAgreementProduct(AgreementProduct agreementProduct) {
        return null;
    }

    @Override
    public Optional<AgreementProduct> updateAgreementProduct(AgreementProduct agreementProduct) {
        return null;
    }

    @Override
    public void deleteAgreementProduct(Long id) {

    }

    @Override
    public Optional<AgreementProduct> getAgreementProductById(Long id) {
        return null;
    }

    @Override
    public List<AgreementProduct> findAgreementProductListByConditions(ConditionOrderByQuery query) {
        return null;
    }

    @Override
    public DomainPage<AgreementProduct> queryAgreementProductPagesByConditions(ConditionOrderByQuery query, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize) {
        return null;
    }
}
