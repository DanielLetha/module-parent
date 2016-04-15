package com.simpletour.test;


import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.product.error.ProductBizError;
import com.simpletour.common.core.dao.query.condition.AndConditionSet;
import com.simpletour.common.core.dao.query.condition.Condition;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.product.IProductDao;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.product.ProductPackage;
import com.simpletour.domain.product.TourismRoute;
import com.simpletour.test.helper.*;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangdongfeng on 2015/11/26.
 */

@ContextConfiguration({"classpath:applicationContext.xml"})
public class ProductTest extends TestClassWithLazyLoadAndTenantId { //AbstractTestNGSpringContextTests {

    private Long id;

    private Long pid;

    @Resource
    IResourcesDao travelResourcesDao;

    @Resource
    IProductDao productDaoImp;

    @Resource
    ITransportDao transportDao;

    @Resource
    IProductBiz productBiz;

    ResourceDataGenerator resourceDataGenerator;
    ProductDataGenerator productDataGenerator;

    @BeforeClass
    public void beforeClass() throws Exception {
        super.beforeClass();
        resourceDataGenerator = new ResourceDataGenerator(transportDao);
        productDataGenerator = new ProductDataGenerator(transportDao, transportDao, transportDao);
    }

    @Resource
    IProductDao productDao;

    @AfterClass
    public void afterClass() {
        removeEntities(transportDao);
        super.afterClass();
    }

    @Test
    public void addProduct() {
        //TODO  行程分解有待进一步测试
        List<ProductPackage> productPackages = productDataGenerator.createProductPackages();
        List<TourismRoute> tourismRoutes = productDataGenerator.generateTourismRouteList();

        //do the correct thing
        String sameName = Utils.generateName();
        //tourism
        {
            Product product = new Product(sameName, true, "depart place", "arrive place", "00:00", "99:99", tourismRoutes, true, "remark");
            product.setProductPackages(productPackages);
            Product savedProduct = productBiz.addProduct(product);
            Assert.assertNotEquals(savedProduct, null);
            id = savedProduct.getId();
        }
        //product
        {
            Product product = new Product(Utils.generateName(), true, "remark", productPackages);
            Product savedProduct = productBiz.addProduct(product);
            Assert.assertNotEquals(savedProduct, null);
            pid = savedProduct.getId();
        }
        //same name
        {
            try {
                Product tourism = new Product(sameName, true, "depart place", "arrive place", "00:00", "99:99", tourismRoutes, true, "remark");
                tourism.setProductPackages(productPackages);
                productBiz.addProduct(tourism);
                Assert.fail();
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), ProductBizError.PRODUCT_NAME_EXIST);
            }
        }
        //wrong time format
        {
            try {
                Product tourism = new Product(Utils.generateName(), true, "depart place", "arrive place", "0000", "99-99", tourismRoutes, true, "remark");
                tourism.setProductPackages(productPackages);
                Product savedTrourism = productBiz.addProduct(tourism);
                Assert.assertFalse(true);
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), ProductBizError.TIME_FORMAT_ERROR);
            }
        }
        //arrive time early than depart time
        {
            try {
                Product tourism = new Product(Utils.generateName(), true, "depart place", "arrive place", "99:99", "99:98", tourismRoutes, true, "remark");
                tourism.setProductPackages(productPackages);
                Product savedTrourism = productBiz.addProduct(tourism);
                Assert.assertFalse(true);
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), ProductBizError.DEPART_TIME_IS_AFTER_ARRIVE_TIME);
            }
        }

        //TODO move to service test
        {
            // tourism routes bus no duplicate
            try {
                tourismRoutes = productDataGenerator.generateTourismRouteList();
                tourismRoutes.get(1).setBusNo(tourismRoutes.get(2).getBusNo());
                Product tourism = new Product(Utils.generateName(), true, "depart place", "arrive place", "99:90", "99:99", tourismRoutes, true, "remark");
                productBiz.addProduct(tourism);
                Assert.assertFalse(true);
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), ProductBizError.TOURISM_ROUTES_BUS_NO_DUPLICATE);
            }
        }
        {
            // bus no time conflict
            try {
                tourismRoutes = productDataGenerator.generateTourismRouteList(true);
                Product tourism = new Product(Utils.generateName(), true, "depart place", "arrive place", "99:90", "99:99", tourismRoutes, true, "remark");
                productBiz.addProduct(tourism);
                Assert.assertFalse(true);
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), ProductBizError.TOURISM_ROUTES_BUS_NO_TIME_CONFLICT);
            }
        }
        {
            //package offset is decreasing
            try {
                tourismRoutes = productDataGenerator.generateTourismRouteList();
                Product tourism = new Product(Utils.generateName(), true, "depart place", "arrive place", "99:90", "99:99", tourismRoutes, true, "remark");
                tourism.setProductPackages(productPackages);
                productPackages.get(0).setOffset(2);
                productBiz.addProduct(tourism);
                Assert.assertFalse(true);
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), ProductBizError.PRODUCT_PACKAGES_DAY_MUST_BE_NON_DECREASING);
            }
        }
    }

    @Test(dependsOnMethods = {"addProduct", "getProductById", "getProductByConditions", "updateTourismWithProductPackage", "getTourismById", "getProductByPage"})
    public void deleteProduct() {
        productBiz.deleteProductById(id);

        Product tourism = productBiz.getProductById(id);
        Assert.assertTrue(tourism.getDel());
        productBiz.deleteProductById(0L);

        productBiz.deleteProductById(pid);
    }

    @Test(dependsOnMethods = "addProduct")
    public void getTourismById() {
        Product tourism = productBiz.getProductById(id);
        Assert.assertNotEquals(tourism, null);

        tourism = productBiz.getProductById(0L);
        Assert.assertEquals(tourism, null);
    }

    @Test(dependsOnMethods = "addProduct")
    public void updateTourismWithProductPackage() {
        Product product = productBiz.getProductById(id);
        List<ProductPackage> savedProductPackages = product.getProductPackages();
        Assert.assertNotEquals(savedProductPackages.size(), 0);

        List<ProductPackage> updateProductPackages = productDataGenerator.createProductPackages();
        product.setProductPackages(updateProductPackages);
        product = productBiz.updateProduct(product);

        product.setRemark("test remark updated");
        product.getTourismRouteList().remove(2);
        product.getTourismRouteList().remove(1);
        product.setProductPackages(updateProductPackages);
        productBiz.updateProduct(product);
    }

    @Test(dependsOnMethods = "addProduct")
    public void getProductByPage() {
        DomainPage<Product> tourims = productBiz.getProductByConditionPage(null, 1, 10);
        Assert.assertNotEquals(tourims.getDomains().size(), 0);
        AndConditionSet conditions = new AndConditionSet();
        conditions.addCondition("name", "", Condition.MatchType.like);
        conditions.addCondition("shuttle", true);
        conditions.addCondition("isOnline", true);
        tourims = productBiz.getProductByConditionPage(conditions, 1, 10);
        Assert.assertNotEquals(tourims.getDomains().size(), 0);
    }

    @Test(dependsOnMethods = "addProduct")
    public void getProductById() {
        Product product = productBiz.getProductById(pid);
        Assert.assertNotEquals(product, null);
        Assert.assertEquals(product.getId(), pid);

        product = productBiz.getProductById(0L);
        Assert.assertEquals(product, null);
    }

    @Test
    public void getProductByConditions() {
        Map<String, Object> conditions = new HashMap<>(2);
        conditions.put("online", true);
        conditions.put("name", "");
        DomainPage<Product> productDomainPage = productBiz.getProductByConditionsPage(conditions, 1, 10);
        Assert.assertNotEquals(productDomainPage.getDomains().size(), 0);

        conditions.replace("name", "not exist name");
        productDomainPage = productBiz.getProductByConditionsPage(conditions, 1, 10);
        Assert.assertEquals(productDomainPage.getDomains().size(), 0);
    }
}

