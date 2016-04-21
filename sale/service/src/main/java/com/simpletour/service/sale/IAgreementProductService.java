package com.simpletour.service.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.domain.sale.AgreementProduct;

import java.util.List;
import java.util.Optional;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
public interface IAgreementProductService {

    /**
     * 增加一个产品退改规则
     * @param agreementProduct 产品退改规则实体类
     * @return
     */
    Optional<AgreementProduct> addAgreementProduct(AgreementProduct agreementProduct);

    /**
     * 向一个销售协议批量增加产品退改规则
     * @param agreement 产品退改规则实体类集合
     * @return
     */
    void addAllAgreementProduct(Agreement agreement);

    /**
     * 批量更新产品退改规则
     * @param agreementProducts 产品退改规则实体类集合
     * @return
     */
    void updateAllAgreementProduct(List<AgreementProduct> agreementProducts);

    /**
     * 更新一个产品退改规则
     * @param agreementProduct 产品退改规则实体类
     * @param isAgreementUpdate 是不是销售协议中的修改，如果是在销售协议的产品列表中修改 是不检查产品退改规则的
     * @return
     */
    Optional<AgreementProduct> updateAgreementProduct(AgreementProduct agreementProduct,Boolean isAgreementUpdate);


    /**
     * 根据id查询产品退改规则
     * @param id
     * @return
     */
    Optional<AgreementProduct> getAgreementProductById(Long id);

    /**
     * 根据条件查询产品退改规则列表
     * @param query 条件集
     * @return
     */
    List<AgreementProduct> findAgreementProductListByConditions(ConditionOrderByQuery query);

    /**
     * 根据条件查询产品退改规则分页
     * @param query
     * @return
     */
    DomainPage<AgreementProduct> queryAgreementProductPagesByConditions(ConditionOrderByQuery query);

}
