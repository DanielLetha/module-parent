package com.simpletour.service.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProductPrice;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Brief : 销售产品价格的服务接口
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/21 10:34
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public interface IAgreementProductPriceService {


    /**
     * 添加销售协议价格
     *
     * @param agreementProductPrice
     * @return
     * @throws BaseSystemException
     */
    Optional<AgreementProductPrice> addAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException;

    /**
     * 更新销售协议价格
     *
     * @param agreementProductPrice
     * @return
     * @throws BaseSystemException
     */
    Optional<AgreementProductPrice> updateAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException;

    /**
     * 分页查询销售协议价格
     *
     * @param query
     * @return
     */
    DomainPage<AgreementProductPrice> queryAgreementProductPrice(ConditionOrderByQuery query);

    /**
     * 根据条件获取销售协议价格的list
     * @param query
     * @return
     */
    List<AgreementProductPrice> getAgreementProductPriceList(ConditionOrderByQuery query);

    /**
     * 分页查询销售协议价格
     *
     * @param conditions
     * @param orderByFiledName
     * @param orderBy
     * @param pageIndex
     * @param pageSize
     * @param byLike
     * @return
     */
    DomainPage<AgreementProductPrice> queryAgreementProductPriceByCondtion(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 根据id查询销售产品价格
     *
     * @param id
     * @return
     */
    Optional<AgreementProductPrice> findAgreementProductPriceById(Long id);
}
