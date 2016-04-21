package com.simpletour.biz.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProductPrice;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Brief :  销售产品价格biz
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/20 16:14
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public interface IAgreementProductPriceBiz {
    /**
     * 添加协议产品价格
     * @param agreementProductPrice
     * @return
     * @throws BaseSystemException
     */
    AgreementProductPrice addAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException;

    /**
     * 更新协议产品价格
     * @param agreementProductPrice
     * @return
     * @throws BaseSystemException
     */
    AgreementProductPrice updateAgreementProductPrice(AgreementProductPrice agreementProductPrice) throws BaseSystemException;

    /**
     * 根据id查询协议产品价格
     * @param id
     * @return
     */
    AgreementProductPrice findAgreementProductPriceById(Long id);



    List<AgreementProductPrice> getAgreementProductList(ConditionOrderByQuery query);

    /**
     * 分页查询产品价格分页
     * @param query
     * @return
     */
    DomainPage<AgreementProductPrice> queryAgreementProductPricePageByCondition(ConditionOrderByQuery query);

    /**
     * 分页查询产品价格分页
     * @param conditions
     * @param orderByFiledName
     * @param orderBy
     * @param pageIndex
     * @param pageSize
     * @param byLike
     * @return
     */
    DomainPage<AgreementProductPrice> queryAgreementProductPricePageByCondition(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    boolean isAgreementProductPriceExist(Long agreeId,Long productId,Date date,String type);


}
