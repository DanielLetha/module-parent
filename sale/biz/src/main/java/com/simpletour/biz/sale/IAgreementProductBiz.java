package com.simpletour.biz.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.sale.AgreementProduct;

import java.util.List;
import java.util.Map;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
public interface IAgreementProductBiz {

    /**
     * 增加一个产品退改规则
     * @param agreementProduct 产品退改规则实体类
     * @return
     */
    AgreementProduct addAgreementProduct(AgreementProduct agreementProduct);

    /**
     * 更新一个产品退改规则
     * @param agreementProduct 产品退改规则实体类
     * @return
     */
    AgreementProduct updateAgreementProduct(AgreementProduct agreementProduct);


    /**
     * 删除产品退改规则
     * @param id 主键id
     */
    void deleteAgreementProduct(Long id);

    /**
     * 根据id查询产品退改规则
     * @param id
     * @return
     */
    AgreementProduct getAgreementProductById(Long id);

    /**
     * 根据条件查询产品退改规则列表
     * @param query 条件集
     * @return
     */
    List<AgreementProduct> findAgreementProductListByConditions(ConditionOrderByQuery query);

    /**
     * 根据条件查询产品退改规则分页
     * @param query
     * @param orderByFiledName
     * @param orderBy
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<AgreementProduct> queryAgreementProductPagesByConditions(ConditionOrderByQuery query
            , String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize);

    /**
     * 根据id判断产品退改规则是否存在
     * @param id
     * @return
     */
    boolean isAgreementProductExisted(Long id);

    /**
     * 根据id判断产品退款细则是否存在
     * @param id
     * @return
     */
    boolean isProductRefundRuleExisted(Long id);
}
