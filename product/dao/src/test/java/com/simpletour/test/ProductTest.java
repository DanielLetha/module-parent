package com.simpletour.test;

import com.simpletour.dao.product.IProductDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import javax.annotation.Resource;

/**
 * Created by songfujie on 15/10/22.
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ProductTest extends AbstractTestNGSpringContextTests {

    @Resource
    private IProductDao productDao;

/*    @Test
    public void add() {

    }*/

}
