package com.simpletour.service.sale;

import com.simpletour.biz.sale.bo.AgreementPriceBo;
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
     * @param agreementPriceBo
     * @return
     * @throws BaseSystemException
     */
    Optional<AgreementPriceBo> addAgreementProductPrice(AgreementPriceBo agreementPriceBo) throws BaseSystemException;

    /**
     * 更新销售协议价格
     *
     * @param agreementPriceBo
     * @return
     * @throws BaseSystemException
     */
    Optional<AgreementPriceBo> updateAgreementProductPrice(AgreementPriceBo agreementPriceBo) throws BaseSystemException;
    /**
     * 批量更新产品价格
     * @param agreementPriceBos
     * @throws BaseSystemException
     */
    void batchInsert(List<AgreementPriceBo> agreementPriceBos) throws BaseSystemException;


    /**
     * 根据条件获取销售协议价格的list
     * @param query
     * @return
     */
    List<AgreementPriceBo> getAgreementProductPriceList(ConditionOrderByQuery query);

}
