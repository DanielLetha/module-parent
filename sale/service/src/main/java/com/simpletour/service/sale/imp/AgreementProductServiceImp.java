package com.simpletour.service.sale.imp;

import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.sale.IAgreementBiz;
import com.simpletour.biz.sale.IAgreementProductBiz;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.domain.sale.AgreementProduct;
import com.simpletour.service.sale.IAgreementProductService;
import com.simpletour.service.sale.error.AgreementProductServiceError;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/20.
 * Remark:
 */
@Service
public class AgreementProductServiceImp implements IAgreementProductService{

    @Resource
    private IAgreementProductBiz agreementProductBiz;

    @Resource
    private IProductBiz productBiz;

    @Resource
    private IAgreementBiz agreementBiz;


    @Override
    public Optional<AgreementProduct> addAgreementProduct(AgreementProduct agreementProduct) {
        //对产品退改规则的存在性验证
        verifyAgreementProduct(agreementProduct,Boolean.FALSE);
        return Optional.ofNullable(agreementProductBiz.addAgreementProduct(agreementProduct));
    }

    @Override
    public void addAllAgreementProduct(Agreement agreement) {
        if (agreement == null) throw new BaseSystemException(AgreementProductServiceError.AGREEMENT_IS_NULL);
        List<AgreementProduct> agreementProducts = agreement.getAgreementProducts();
        if (agreementProducts == null || agreementProducts.isEmpty()){
            throw new BaseSystemException(AgreementProductServiceError.MUST_CONTAIN_PRODUCT);
        }
        //根据销售协议id查询出相关产品退改规则
        List<AgreementProduct> agreementProductsDb = agreementProductBiz.getAgreementProductByAgreementId(agreement.getId());
        //删除掉已经不存在的产品退改规则
        List<AgreementProduct> toBeDeleteProducts = agreementProductsDb.stream().filter(agreementProduct -> !agreementProducts.contains(agreementProduct)).collect(Collectors.toList());
        toBeDeleteProducts.stream().forEach(agreementProduct -> agreementProductBiz.deleteAgreementProduct(agreementProduct));
        //把所有退改规则插入数据库
        for (AgreementProduct agreementProduct : agreementProducts){
            if (agreementProduct.getId() == null){
                this.addAgreementProduct(agreementProduct);
            } else{
                this.updateAgreementProduct(agreementProduct,Boolean.TRUE);
            }
        }
    }

    @Override
    public Optional<AgreementProduct> updateAgreementProduct(AgreementProduct agreementProduct,Boolean isAgreementUpdate ) {
        //对产品退改规则的存在性验证
        verifyAgreementProduct(agreementProduct,Boolean.TRUE);
        return Optional.ofNullable(agreementProductBiz.updateAgreementProduct(agreementProduct,isAgreementUpdate));
    }

    @Override
    public void updateAllAgreementProduct(List<AgreementProduct> agreementProducts) {
        if (agreementProducts == null || agreementProducts.isEmpty()){
            throw new BaseSystemException(AgreementProductServiceError.MUST_CONTAIN_PRODUCT);
        }
        //更新所有产品退改规则
        for (AgreementProduct agreementProduct : agreementProducts){
            this.updateAgreementProduct(agreementProduct,Boolean.FALSE);
        }
    }

    @Override
    public Optional<AgreementProduct> getAgreementProductById(Long id) {
        return Optional.ofNullable(agreementProductBiz.getAgreementProductById(id));
    }

    @Override
    public List<AgreementProduct> findAgreementProductListByConditions(ConditionOrderByQuery query) {
        return agreementProductBiz.findAgreementProductListByConditions(query);
    }

    @Override
    public DomainPage<AgreementProduct> queryAgreementProductPagesByConditions(ConditionOrderByQuery query) {
        return agreementProductBiz.queryAgreementProductPagesByConditions(query);
    }

    /**
     * 对产品退改规则模板进行验证,主要是相关产品和协议存在性的验证
     * @param agreementProduct
     * @param isUpdate 是不是更新
     */
    private void verifyAgreementProduct(AgreementProduct agreementProduct,Boolean isUpdate){
        if (agreementProduct == null) throw new BaseSystemException(AgreementProductServiceError.AGREEMENT_PRODUCT_EMPTY);
        if (isUpdate){
            if (!agreementProductBiz.isExisted(agreementProduct.getId())){
                throw new BaseSystemException(AgreementProductServiceError.AGREEMENT_PRODUCT_NOT_EXIST);
            }
        }
        //判断产品的存在性及名字正确性
        Product productDb = null; //从数据库查询出来的product
        Product product = agreementProduct.getProduct(); //页面传来的product
        if (product != null){
            productDb = productBiz.getProductById(product.getId());
        }
        if (productDb == null){
            throw new BaseSystemException(AgreementProductServiceError.PRODUCT_NOT_EXIST);
        }
        if (!product.getName().equals(productDb.getName())){
            throw new BaseSystemException(AgreementProductServiceError.PRODUCT_NAME_NOT_CORRECT);
        }
        //判断销售协议的存在性及名字正确性
        Agreement agreementDb = null;//从数据库查出来的销售协议
        Agreement agreement = agreementProduct.getAgreement();//页面传来的销售协议
        if (agreement != null ){
            agreementDb = agreementBiz.getAgreementById(agreement.getId());
        }
        if (agreementDb == null){
            throw new BaseSystemException(AgreementProductServiceError.AGREEMENT_NOT_EXIST);
        }
        if (agreement.getSaleApp() == null || agreementDb.getSaleApp() == null){
            throw new BaseSystemException(AgreementProductServiceError.AGREEMENT_APP_IS_NULL);
        }
        if (!agreement.getSaleApp().getName().equals(agreementDb.getSaleApp().getName())){
            throw new BaseSystemException(AgreementProductServiceError.AGREEMENT_NAME_NOT_CORRECT);
        }
        agreementProduct.setProduct(productDb);
        agreementProduct.setAgreement(agreementDb);
    }
}
