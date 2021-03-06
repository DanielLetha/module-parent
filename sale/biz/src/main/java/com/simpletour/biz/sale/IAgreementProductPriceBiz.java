package com.simpletour.biz.sale;

import com.simpletour.biz.sale.bo.AgreementPriceBo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.AgreementProduct;
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
public interface IAgreementProductPriceBiz {
    /**
     * 添加协议产品价格
     * @param agreementPricebo
     * @return
     * @throws BaseSystemException
     */
    AgreementPriceBo addAgreementProductPrice(AgreementPriceBo agreementPricebo) throws BaseSystemException;

    /**
     * 更新协议产品价格
     * @param agreementPricebo
     * @return
     * @throws BaseSystemException
     */
    AgreementPriceBo updateAgreementProductPrice(AgreementPriceBo agreementPricebo) throws BaseSystemException;

    /**
     * 根据协议产品和日期获取协议产品价格
     * @param agreementProduct
     * @param date (format:yyyy-MM-dd, eg:2016-04-23)
     * @return
     */
    AgreementPriceBo getAgreementProductPrice(AgreementProduct agreementProduct,Date date) throws BaseSystemException;


    /**
     * 删除价格
     * @param agreementProduct
     * @param date (format:yyyy-MM-dd, eg:2016-04-23)
     * @throws BaseSystemException
     */
     void deleteAgreementProductPrice(AgreementProduct agreementProduct, Date date) throws BaseSystemException;

    /**
     * 根据条件查询产品价格的list
     * @param query
     * @return
     */
    List<AgreementPriceBo> getAgreementProductPriceList(ConditionOrderByQuery query);


}
