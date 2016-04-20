package com.simpletour.biz.sale.imp;

import com.simpletour.biz.sale.IAgreementProductBiz;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.dao.sale.IAgreementProductDao;
import com.simpletour.dao.sale.IAgreementProductRefundRuleDao;
import com.simpletour.domain.sale.AgreementProduct;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
@Component
public class AgreementProductBizImp implements IAgreementProductBiz {

    @Resource
    private IAgreementProductDao agreementProductDao;

    @Resource
    private IAgreementProductRefundRuleDao refundRuleDao;

    @Override
    public AgreementProduct addAgreementProduct(AgreementProduct agreementProduct) {
        return null;
    }

    @Override
    public AgreementProduct updateAgreementProduct(AgreementProduct agreementProduct) {
        return null;
    }

    @Override
    public void deleteAgreementProduct(Long id) {

    }

    @Override
    public AgreementProduct getAgreementProductById(Long id) {
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

    @Override
    public boolean isAgreementProductExisted(Long id) {
        return false;
    }

    @Override
    public boolean isProductRefundRuleExisted(Long id) {
        return false;
    }
}
