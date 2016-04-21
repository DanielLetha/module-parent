package com.simpletour.biz.sale.imp;

import com.simpletour.biz.sale.IAgreementProductBiz;
import com.simpletour.biz.sale.error.AgreementProductBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IAgreementProductDao;
import com.simpletour.dao.sale.IAgreementProductRefundRuleDao;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.domain.sale.AgreementProduct;
import com.simpletour.domain.sale.AgreementProductRefundRule;
import com.simpletour.domain.sale.RefundRule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
@Component
public class AgreementProductBizImp implements IAgreementProductBiz {

    @Resource
    private IAgreementProductDao agreementProductDao;

    @Resource
    private IAgreementProductRefundRuleDao refundRuleDao;

    @Override
    public AgreementProduct addAgreementProduct(AgreementProduct agreementProduct) {
        checkNull(agreementProduct);
        checkAddIdNotRepate(agreementProduct);
        return agreementProductDao.save(agreementProduct);
    }

    @Override
    public AgreementProduct updateAgreementProduct(AgreementProduct agreementProduct,Boolean isAgreementUpdate) {
        checkNull(agreementProduct);
        checkUpdateIdNotRepate(agreementProduct);
        if (!isAgreementUpdate){
            checkProductRefundRules(agreementProduct);
        }
        return agreementProductDao.save(agreementProduct);
    }

    @Override
    public void deleteAgreementProduct(AgreementProduct agreementProduct) {
        checkNull(agreementProduct);
        //验证是否存在
        if (!this.isExisted(agreementProduct.getId())){
            throw new BaseSystemException(AgreementProductBizError.AGREEMENT_PRODUCT_NOT_EXIST);
        }
        agreementProductDao.remove(agreementProduct);

    }

    @Override
    public AgreementProduct getAgreementProductById(Long id) {
        checkId(id);
        return agreementProductDao.getEntityById(AgreementProduct.class,id);
    }

    @Override
    public List<AgreementProduct> getAgreementProductByAgreementId(Long id) {
        checkId(id);
        return agreementProductDao.getEntitiesByField(AgreementProduct.class,"agreement.id",id);
    }

    @Override
    public List<AgreementProduct> findAgreementProductListByConditions(ConditionOrderByQuery query) {
        if (query == null) throw new BaseSystemException(AgreementProductBizError.CONDITION_QUERY_IS_NULL);
        return agreementProductDao.getEntitiesByQuery(AgreementProduct.class,query);
    }

    @Override
    public DomainPage<AgreementProduct> queryAgreementProductPagesByConditions(ConditionOrderByQuery query) {
        if (query == null) throw new BaseSystemException(AgreementProductBizError.CONDITION_QUERY_IS_NULL);
        return agreementProductDao.getEntitiesPagesByQuery(AgreementProduct.class,query);
    }

    @Override
    public boolean isExisted(Long id) {
        checkId(id);
        AgreementProduct agreementProduct = this.getAgreementProductById(id);
        return agreementProduct != null;
    }

    @Override
    public boolean isProductRefundRuleExisted(Long id) {
        checkId(id);
        AgreementProductRefundRule refundRule = refundRuleDao.getEntityById(AgreementProductRefundRule.class, id);
        return refundRule != null;
    }

    /**
     * 检查id不能为空和小于1
     * @param id
     */
    private void checkId(Long id){
        if (id == null || id.longValue() < 1) {
            throw new BaseSystemException(AgreementProductBizError.ID_IS_ERROR);
        }
    }
    /**
     * 检查对象是否为空
     * @param agreementProduct
     */
    private void checkNull(AgreementProduct agreementProduct){
        if (agreementProduct == null){
            throw new BaseSystemException(AgreementProductBizError.AGREEMENT_PRODUCT_IS_NULL);
        }
    }

    /**
     * 添加时检查产品Id和协议id同时不重复(即一个销售协议下产品必须唯一)
     * @param agreementProduct 产品退改规则
     */
    private void checkAddIdNotRepate(AgreementProduct agreementProduct){
        Product product = agreementProduct.getProduct();
        Agreement agreement = agreementProduct.getAgreement();
        if (product == null|| agreement == null){
            throw new BaseSystemException(AgreementProductBizError.PRODUCT_OR_AGREEMENT_IS_NULL);
        }
        if (product.getId() == null || agreement.getId() == null){
            throw new BaseSystemException(AgreementProductBizError.PRODUCT_ID_OR_AGREEMENT_ID_IS_NULL);
        }
        //通过产品id和协议id联合查询产品退改规则
        HashMap<String,Object> conditionMap = new HashMap<>(2);
        conditionMap.put("product.id",product.getId());
        conditionMap.put("agreement.id",agreement.getId());
        List<AgreementProduct> agreementProducts = agreementProductDao.getEntitiesByFieldList(AgreementProduct.class, conditionMap);
        if (agreementProducts != null && !agreementProducts.isEmpty()){
            throw new BaseSystemException(AgreementProductBizError.PRODUCT_MUST_BE_UNIQUE_IN_ONE_AGREEMENT);
        }
    }
    /**
     * 编辑时时检查产品Id和协议id同时不重复(即一个销售协议下产品必须唯一)
     * @param agreementProduct 产品的退改规则
     */
    private void checkUpdateIdNotRepate(AgreementProduct agreementProduct){
        Product product = agreementProduct.getProduct();
        Agreement agreement = agreementProduct.getAgreement();
        if (product == null|| agreement == null){
            throw new BaseSystemException(AgreementProductBizError.PRODUCT_OR_AGREEMENT_IS_NULL);
        }
        if (product.getId() == null || agreement.getId() == null){
            throw new BaseSystemException(AgreementProductBizError.PRODUCT_ID_OR_AGREEMENT_ID_IS_NULL);
        }
        //通过产品id和协议id联合查询产品退改规则
        HashMap<String,Object> conditionMap = new HashMap<>(2);
        conditionMap.put("product.id",product.getId());
        conditionMap.put("agreement.id",agreement.getId());
        List<AgreementProduct> agreementProducts = agreementProductDao.getEntitiesByFieldList(AgreementProduct.class, conditionMap);
        if (agreementProducts != null && !agreementProducts.isEmpty()){
           if (agreementProducts.size() > 1 || (!agreementProducts.get(0).getId().equals(agreementProduct.getId()))){
                throw new BaseSystemException(AgreementProductBizError.PRODUCT_MUST_BE_UNIQUE_IN_ONE_AGREEMENT);
            }
        }
    }

    /**
     * 对产品退改规则细则进行验证
     * @param agreementProduct
     */
    private void checkProductRefundRules(AgreementProduct agreementProduct){
        List<AgreementProductRefundRule> productRefundRules = agreementProduct.getProductRefundRules();
        if (productRefundRules == null || productRefundRules.isEmpty()){
            throw new BaseSystemException(AgreementProductBizError.PRODUCT_REFUND_RULE_IS_EMPTY);
        }
        //判断退款细则是否可用
        if (!isRuleAvaliable(productRefundRules)){
            throw new BaseSystemException(AgreementProductBizError.REFUND_POLICY_REFUND_RULE_ERROR);
        }

        //如果是被编辑的退款细则，要判断是否存在
        productRefundRules.forEach(productRefundRule->{
            if (productRefundRule.getId()!=null && !this.isProductRefundRuleExisted(productRefundRule.getId())){
                throw new BaseSystemException(AgreementProductBizError.REFUND_POLICY_REFUND_RULE_NOT_EXIST);
            }
            if (productRefundRule.getAgreementProduct() == null) {
                productRefundRule.setAgreementProduct(agreementProduct);
            }
        });

    }

    /**
     * 判断退款细则是否重复
     * @param refundRules
     * @return
     */
    private boolean isRuleAvaliable(List<AgreementProductRefundRule> refundRules){
        for(int i=1;i<refundRules.size();i++){
            if (refundRules.get(i-1).getTiming()>=refundRules.get(i).getTiming())
                return false;
        }
        return true;
    }
}
