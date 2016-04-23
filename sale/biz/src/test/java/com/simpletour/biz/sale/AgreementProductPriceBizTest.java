package com.simpletour.biz.sale;

import com.simpletour.biz.sale.bo.AgreementPriceBo;
import com.simpletour.biz.sale.bo.Price;
import com.simpletour.biz.sale.error.AgreementProductPriceBizError;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IAgreementProductPriceDao;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.domain.sale.AgreementProduct;
import com.simpletour.domain.sale.AgreementProductPrice;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Brief :  ${用途}
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/21 11:40
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class AgreementProductPriceBizTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    IAgreementProductPriceBiz agreementProductPriceBiz;

    @Autowired
    IAgreementProductBiz agreementProductBiz;

    @Autowired
    IAgreementProductPriceDao agreementProductPriceDao;

    @BeforeClass
    public void init() {
        AgreementProduct agreementProduct = agreementProductBiz.getAgreementProductById(1L);
        Map<AgreementProductPrice.Type, Price> map = new HashMap<>();
        Price adultPrice = new Price(100, 150, 200);
        Price childPrice = new Price(100, 150, 180);
        map.put(AgreementProductPrice.Type.ADULT, adultPrice);
        map.put(AgreementProductPrice.Type.CHILD, childPrice);
        String now = "2016-04-23";

        AgreementPriceBo agreementPriceBo = new AgreementPriceBo();
        agreementPriceBo.setDate(formateDate(now));
        agreementPriceBo.setAgreementProduct(agreementProduct);
        agreementPriceBo.setPriceMap(map);

        agreementProductPriceBiz.addAgreementProductPrice(agreementPriceBo);
    }

    @AfterTest
    public void clearDate() {
        String now = "2016-04-23";
        String now1 = "2016-04-24";

        AgreementProduct agreementProduct = agreementProductBiz.getAgreementProductById(1L);
        agreementProductPriceBiz.deleteAgreementProductPrice(agreementProduct, formateDate(now));
        agreementProductPriceBiz.deleteAgreementProductPrice(agreementProduct, formateDate(now1));

    }

    @Test(priority = 1)
    public void testAddAPPWithNull() {
        try {
            agreementProductPriceBiz.addAgreementProductPrice(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), AgreementProductPriceBizError.AGREEMENT_PRODUCT_PRICE_EMPTY);
        }


    }

    @Test(priority = 2)
    public void testAPPWithProductNull() {
        Map<AgreementProductPrice.Type, Price> map = new HashMap<>();
        Price adultPrice = new Price(80, 120, 180);
        Price childPrice = new Price(80, 120, 160);
        map.put(AgreementProductPrice.Type.ADULT, adultPrice);
        map.put(AgreementProductPrice.Type.CHILD, childPrice);
        Date now = new Date();
        AgreementPriceBo agreementPriceBo = new AgreementPriceBo();
        agreementPriceBo.setDate(now);
        agreementPriceBo.setAgreementProduct(null);
        agreementPriceBo.setPriceMap(map);

        try {
            agreementProductPriceBiz.addAgreementProductPrice(agreementPriceBo);

        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), AgreementProductPriceBizError.AGREEMENT_PRODUCT_NULL);
        }

    }

    @Test(priority = 3)
    @Rollback(value = false)
    public void testAddAPP(){
        AgreementProduct agreementProduct = agreementProductBiz.getAgreementProductById(1L);
        Map<AgreementProductPrice.Type, Price> map = new HashMap<>();
        Price adultPrice = new Price(50, 80, 150);
        Price childPrice = new Price(50, 80, 120);
        map.put(AgreementProductPrice.Type.ADULT, adultPrice);
        map.put(AgreementProductPrice.Type.CHILD, childPrice);
        String now = "2016-04-24";

        AgreementPriceBo agreementPriceBo = new AgreementPriceBo();
        agreementPriceBo.setDate(formateDate(now));
        agreementPriceBo.setAgreementProduct(agreementProduct);
        agreementPriceBo.setPriceMap(map);

        AgreementPriceBo agreementPriceBo1 =  agreementProductPriceBiz.addAgreementProductPrice(agreementPriceBo);
        Assert.assertNotNull(agreementPriceBo1);
    }

    @Test(priority = 4)
    @Rollback(value = false)
    public void testUpdateAPP() {
        String now = "2016-04-23";
        AgreementProduct agreementProduct = agreementProductBiz.getAgreementProductById(1L);
        AgreementPriceBo agreementPriceBo =  agreementProductPriceBiz.getAgreementProductPrice(agreementProduct, formateDate(now));
        agreementPriceBo.getPriceMap().get(AgreementProductPrice.Type.CHILD).setRetail(300);
        AgreementPriceBo agreementPriceBo1 =  agreementProductPriceBiz.updateAgreementProductPrice(agreementPriceBo);
        System.out.println(agreementPriceBo1.getPriceMap().get(AgreementProductPrice.Type.CHILD).getRetail());
        Assert.assertEquals(300,(int)agreementPriceBo1.getPriceMap().get(AgreementProductPrice.Type.CHILD).getRetail());

    }

    @Test(priority = 5)
    public void testGetAPP(){
        String now = "2016-04-23";
        AgreementProduct agreementProduct = agreementProductBiz.getAgreementProductById(1L);
        AgreementPriceBo agreementPriceBo =  agreementProductPriceBiz.getAgreementProductPrice(agreementProduct,formateDate(now));
        Assert.assertNotNull(agreementPriceBo);
    }

    @Test(priority = 6)
    public void testGetAPPList(){
        AgreementProduct agreementProduct = agreementProductBiz.getAgreementProductById(1L);
        ConditionOrderByQuery query = new ConditionOrderByQuery();
        AndConditionSet conditionSet = new AndConditionSet();
        String now = "2016-04-23";
        String now2 = "2016-04-23";

        conditionSet.addCondition("agreementProduct",agreementProduct);
        conditionSet.addCondition("date",formateDate(now), Condition.MatchType.greaterOrEqual);
        conditionSet.addCondition("date",formateDate(now2), Condition.MatchType.lessOrEqual);
        query.setCondition(conditionSet);
        List<AgreementPriceBo> agreementPriceBoList =  agreementProductPriceBiz.getAgreementProductPriceList(query);
        Assert.assertEquals(2,agreementPriceBoList.size());
    }

    private Date formateDate(String now) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

}
