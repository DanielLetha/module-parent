package com.simpletour;

import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.commons.test.TestClassWithLazyLoadAndTenantId;
import com.simpletour.commons.test.Utils;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.product.ProductPackage;
import com.simpletour.domain.product.TourismRoute;
import com.simpletour.domain.resources.Procurement;
import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.service.product.IProductService;
import com.simpletour.service.product.error.ProductServiceError;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by songfujie on 15/10/22.
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ProductTest extends TestClassWithLazyLoadAndTenantId {

    @Resource
    private IProductService productService;

    private Long id;

    private Long pid;

    @Resource
    ITransportDao transportDao;

    @Resource
    IResourcesDao resourcesDao;

    ResourceDataGenerator resourceDataGenerator;

    ProductDataGenerator productDataGenerator;

    @BeforeClass
    public void beforeClass() throws Exception {
        super.beforeClass();
        resourceDataGenerator = new ResourceDataGenerator(transportDao);
        productDataGenerator = new ProductDataGenerator(transportDao, transportDao, transportDao);
    }

    @AfterClass
    public void afterClass() {
        removeEntities(resourcesDao);
        super.afterClass();
    }

    @Test
    public void addProduct() {
        List<ProductPackage> productPackages = productDataGenerator.createProductPackages();
        List<TourismRoute> tourismRoutes = productDataGenerator.generateTourismRouteList();

        //do the correct thing
        String sameName = Utils.generateName();
        //tourism
        {
            Product product = new Product(sameName, true, "depart place", "arrive place", "00:00", "99:99", tourismRoutes, true, "remark");
            product.setProductPackages(productPackages);
            Optional<Product> savedProduct = productService.addProduct(product);
            Assert.assertTrue(savedProduct.isPresent());
            id = savedProduct.get().getId();
        }
        //product
        {
            Product product = new Product(Utils.generateName(), true, "remark", productPackages);
            Optional<Product> savedProduct = productService.addProduct(product);
            Assert.assertTrue(savedProduct.isPresent());
            pid = savedProduct.get().getId();
        }
        {
            //null Bus No
            try {
                tourismRoutes.get(0).setBusNo(null);
                Product tourism = new Product(Utils.generateName(), true, "depart place", "arrive place", "99:90", "99:99", tourismRoutes, true, "remark");
                tourism.setProductPackages(productPackages);
                productService.addProduct(tourism);
                Assert.assertFalse(true);
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), ProductServiceError.EMPTY_ENTITY);
            }
            //Bus No not exist
            try {
                tourismRoutes.get(0).setBusNo(new BusNo());
                tourismRoutes.get(0).getBusNo().setId(0L);
                Product tourism = new Product(Utils.generateName(), true, "depart place", "arrive place", "99:90", "99:99", tourismRoutes, true, "remark");
                tourism.setProductPackages(productPackages);
                productService.addProduct(tourism);
                Assert.assertFalse(true);
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), TravelTransportBizError.BUS_NO_NOT_EXIST);
            }
        }
        {
            Product product = new Product();
            product.setName(Utils.generateName());
            for (ProductPackage productPackage : productPackages) {
                Procurement procurement = new Procurement();
                procurement.setId(0L);
                productPackage.setProcurement(procurement);
            }
            product.setProductPackages(productPackages);
            try {
                product.setProductPackages(productPackages);
                productService.addProduct(product);
                Assert.assertFalse(true);
            } catch (BaseSystemException e) {
                Assert.assertEquals(e.getError(), ResourcesBizError.PROCUREMENT_NOT_EXIST);
            }
        }
    }

}
