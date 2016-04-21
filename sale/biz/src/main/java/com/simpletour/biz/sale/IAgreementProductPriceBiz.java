package com.simpletour.biz.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProductPrice;
import org.springframework.stereotype.Repository;

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
@Repository
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

    /**
     * 依据协议id,产品id,日期以及类型判断销售协议产品价格是否存在
     * @param agreementId
     * @param productId
     * @param date
     * @param type
     * @return
     */
    boolean isAgreementProductPriceExist(Long agreementId,Long productId,Date date,String type);


}
