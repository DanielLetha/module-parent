package com.simpletour.biz.sale.imp;

import com.simpletour.biz.sale.IAgreementBiz;
import com.simpletour.biz.sale.error.AgreementBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IAgreementDao;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.domain.sale.SaleApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 销售协议Biz实现类
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/19.
 *
 * @since 2.0-SNAPSHOT
 */
@Component
public class AgreementBizImp implements IAgreementBiz {
    @Autowired
    IAgreementDao agreementDao;

    @Override
    public DomainPage<Agreement> findAgreementByCondition(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return agreementDao.queryEntitiesPagesByFieldList(Agreement.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public DomainPage<Agreement> findAgreementByQuery(ConditionOrderByQuery query) {
        return agreementDao.getEntitiesPagesByQuery(Agreement.class, query);
    }

    @Override
    public List<Agreement> getAgreementListByQuery(ConditionOrderByQuery query) {
        return agreementDao.getEntitiesByQuery(Agreement.class, query);
    }

    @Override
    public Agreement getAgreementById(Long id) {
        return agreementDao.getEntityById(Agreement.class, id);
    }

    @Override
    public Agreement addAgreement(Agreement agreement) {
        checkAgreement(agreement);
        checkSaleApp(agreement.getSaleApp());
        checkUnique(agreement, false);
        checkStatus(agreement.getStatus());
        return agreementDao.save(agreement);
    }

    @Override
    public Agreement updateAgreement(Agreement agreement) {
        checkAgreement(agreement);
        checkId(agreement.getId());
        checkSaleApp(agreement.getSaleApp());
        checkUnique(agreement, true);
        checkStatus(agreement.getStatus());
        if (!isExist(agreement.getId()))
            throw new BaseSystemException(AgreementBizError.AGREEMENT_NOT_EXIST);
        return agreementDao.save(agreement);
    }

    @Override
    public Agreement updateStatus(Agreement agreement) {
        checkAgreement(agreement);
        checkId(agreement.getId());
        checkSaleApp(agreement.getSaleApp());
        checkStatus(agreement.getStatus());
        if (!isExist(agreement.getId()))
            throw new BaseSystemException(AgreementBizError.AGREEMENT_NOT_EXIST);
        return agreementDao.save(agreement);
    }

    @Override
    public Boolean isExist(Long id) {
        return Optional.ofNullable(getAgreementById(id)).isPresent();
    }

    /**
     * 检查销售协议对象是否为空
     *
     * @param agreement 销售协议对象
     */
    private void checkAgreement(Agreement agreement) {
        if (agreement == null)
            throw new BaseSystemException(AgreementBizError.AGREEMENT_EMPTY);
    }

    /**
     * 检查销售协议ID是否为空
     *
     * @param id 销售协议ID
     */
    private void checkId(Long id) {
        if (id == null)
            throw new BaseSystemException(AgreementBizError.AGREEMENT_EMPTY);
    }

    /**
     * 检查销售端是否为空
     *
     * @param saleApp 销售端
     */
    private void checkSaleApp(SaleApp saleApp) {
        if (saleApp == null || saleApp.getId() == null)
            throw new BaseSystemException(AgreementBizError.AGREEMENT_APP_NULL);
    }

    /**
     * 检查销售协议的销售端ID是否唯一
     *
     * @param agreement 销售协议对象
     * @param isUpdate  是否为更新操作
     */
    private void checkUnique(Agreement agreement, Boolean isUpdate) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("app_id", agreement.getSaleApp().getId());
        List<Agreement> list = agreementDao.getEntitiesByFieldList(Agreement.class, conditions);
        if (isUpdate) {
            if (list != null && list.size() > 0 && !list.get(0).getId().equals(agreement.getId()))
                throw new BaseSystemException(AgreementBizError.AGREEMENT_APP_EXISTED);
        } else {
            if (list != null && list.size() > 0)
                throw new BaseSystemException(AgreementBizError.AGREEMENT_APP_EXISTED);
        }
    }

    /**
     * 检查销售协议状态是否为空
     *
     * @param status 状态
     */
    private void checkStatus(Agreement.Status status) {
        if (status == null)
            throw new BaseSystemException(AgreementBizError.AGREEMENT_STATUS);
    }
}
