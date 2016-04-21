package com.simpletour.biz.sale;

import com.simpletour.dao.sale.IAgreementProductPriceDao;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.domain.sale.AgreementProductPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
    IAgreementProductPriceDao agreementProductPriceDao;
    @BeforeClass
    public void init(){
        Agreement agreement = new Agreement();

    }
    @AfterTest
    public void clearDate(){

    }

    @Test(priority = 1)
    public void testAddAPPWithNull(){

    }
    @Test(priority = 2)
    public void testAPP(){

    }

}
