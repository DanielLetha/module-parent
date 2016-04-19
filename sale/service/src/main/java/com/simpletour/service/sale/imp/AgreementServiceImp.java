package com.simpletour.service.sale.imp;

import com.simpletour.biz.sale.IAgreementBiz;
import com.simpletour.biz.sale.ISaleAppBiz;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.service.sale.IAgreementService;
import com.simpletour.service.sale.error.AgreementServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 销售协议Service实现类
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/19.
 *
 * @since 2.0-SNAPSHOT
 */
@Service
public class AgreementServiceImp implements IAgreementService {
    @Autowired
    IAgreementBiz agreementBiz;

    @Autowired
    ISaleAppBiz saleAppBiz;

    @Override
    public DomainPage<Agreement> findAgreementByCondition(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return agreementBiz.findAgreementByCondition(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public DomainPage<Agreement> findAgreementByQuery(ConditionOrderByQuery query) {
        return agreementBiz.findAgreementByQuery(query);
    }

    @Override
    public List<Agreement> getAgreementListByQuery(ConditionOrderByQuery query) {
        return agreementBiz.getAgreementListByQuery(query);
    }

    @Override
    public Optional<Agreement> getAgreementById(Long id) {
        if (id == null)
            throw new BaseSystemException(AgreementServiceError.AGREEMENT_EMPTY);
        return Optional.ofNullable(agreementBiz.getAgreementById(id));
    }

    @Override
    public Optional<Agreement> addAgreement(Agreement agreement) {
        verifyData(agreement, false);
        return Optional.ofNullable(agreementBiz.addAgreement(agreement));
    }

    @Override
    public Optional<Agreement> updateAgreement(Agreement agreement) {
        verifyData(agreement, true);
        return Optional.ofNullable(agreementBiz.updateAgreement(agreement));
    }

    @Override
    public Optional<Agreement> updateStatus(Agreement agreement) {
        if (agreement == null)
            throw new BaseSystemException(AgreementServiceError.AGREEMENT_EMPTY);
        Agreement original = agreementBiz.getAgreementById(agreement.getId());
        if (original == null)
            throw new BaseSystemException(AgreementServiceError.AGREEMENT_NOT_EXIST);
        if (original.getStatus().equals(agreement.getStatus())) {
            if (original.getStatus().equals(Agreement.Status.enable))
                throw new BaseSystemException(AgreementServiceError.AGREEMENT_STATUS_ENABLED);
            if (original.getStatus().equals(Agreement.Status.disable))
                throw new BaseSystemException(AgreementServiceError.AGREEMENT_STATUS_DISABLED);
        }
        return Optional.ofNullable(agreementBiz.updateStatus(agreement));
    }

    /**
     * 验证数据
     *
     * @param agreement 销售协议对象
     * @param isUpdate  是否为更新操作
     */
    private void verifyData(Agreement agreement, boolean isUpdate) {
        if (agreement == null)
            throw new BaseSystemException(AgreementServiceError.AGREEMENT_EMPTY);
        if (isUpdate) {
            if (!agreementBiz.isExist(agreement.getId()))
                throw new BaseSystemException(AgreementServiceError.AGREEMENT_NOT_EXIST);
        }
        if (!saleAppBiz.isExist(agreement.getSaleApp().getId()))
            throw new BaseSystemException(AgreementServiceError.AGREEMENT_APP_NOT_EXIST);
    }
}
