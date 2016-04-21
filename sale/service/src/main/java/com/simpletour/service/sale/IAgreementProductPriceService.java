package com.simpletour.service.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProductPrice;

import java.util.Map;
import java.util.Optional;

/**
 * @Brief :  ${用途}
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/21 10:34
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public interface IAgreementProductPriceService {

    Optional<AgreementProductPrice> addAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException;

    Optional<AgreementProductPrice> updateAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException;

    DomainPage<AgreementProductPrice> queryAgreementProductPrice(ConditionOrderByQuery query);

    DomainPage<AgreementProductPrice> queryAgreementProductPrice(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

}
