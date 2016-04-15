package com.simpletour.test;

import com.simpletour.dao.product.IProductDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import javax.annotation.Resource;

/**
 * Created by Jeff.Song on 2015/11/21.
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class TourismDaoTest extends AbstractTestNGSpringContextTests {

    @Resource
    private IProductDao productDao;

/*    @Test
    public void add() {
        TourismRoute tourismRoute = new TourismRoute();
        Long[] ids = {1L, 2L, 3L};
        tourismRoute.setRoutes(ids);
        productDao.save(tourismRoute);
    }

    @Test
    public void getAndUpdate() {
        TourismRoute tourismRoute = productDao.getEntityById(TourismRoute.class, 1L);
        Assert.assertNotNull(tourismRoute);
        Long[] ids = {2L, 24L, 3L};
        tourismRoute.setRoutes(ids);
        productDao.save(tourismRoute);
    }

    @Test
    public void delete(){
        productDao.removeEntityById(TourismRoute.class,1L, true);
    }*/

}
