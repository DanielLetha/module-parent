package com.simpletour.service.company.imp;

import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IAdministratorDao;
import com.simpletour.domain.company.Administrator;
import com.simpletour.service.company.IAdministratorService;
import com.simpletour.service.company.error.AdministratorServiceError;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 测试管理员Service
 * User: Hawk
 * Date: 2016/4/7 - 10:32
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AdminServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private IAdministratorService adminService;

    @Resource
    private IAdministratorDao adminDao;

    private Administrator administrator;

    @BeforeClass
    public void setUp() {
        Administrator admin = generateNewAdmin();
        administrator = adminDao.save(admin);
        Assert.assertNotNull(administrator);
    }

    @AfterClass
    public void tearDown() {
        if (administrator != null) {
            adminDao.remove(administrator);
        }
    }

    @Test
    public void testFindAdminByJobNo() {
        Assert.assertTrue(adminService.findAdminByJobNo(administrator.getJobNo()).isPresent());
    }

    @Test
    public void testFindAdminByJobNoWithNull() {
        Administrator admin = generateNewAdmin();
        admin.setJobNo(null);
        Administrator dbAdmin = adminDao.save(admin);
        Assert.assertEquals(dbAdmin.getId(), adminService.findAdminByJobNo(null).get().getId());
    }

    @Test
    public void testFindAdminByJobNoWithEmpty() {
        Administrator admin = generateNewAdmin();
        admin.setJobNo("");
        Administrator dbAdmin = adminDao.save(admin);
        Assert.assertEquals(dbAdmin.getId(), adminService.findAdminByJobNo("").get().getId());
    }

    @Test
    public void testUpdateAdminPassword() {
        String newPassword = administrator.getPassword() + "7";
        Optional<Administrator> administratorOpt = adminService.updateAdminPassword(administrator.getJobNo(), newPassword);
        Assert.assertTrue(administratorOpt.isPresent());
        Assert.assertEquals(administrator.getId(), administratorOpt.get().getId());
        Assert.assertEquals(newPassword, administratorOpt.get().getPassword());
    }

    @Test
    public void testUpdateAdminPasswordWithJobNoNull() {
        try {
            adminService.updateAdminPassword(null, "");
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(AdministratorServiceError.EMPTY_ENTITY, e.getError());
        }
    }

    @Test
    public void testUpdateAdminPasswordWithJobNoBlank() {
        try {
            adminService.updateAdminPassword("", "");
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(AdministratorServiceError.EMPTY_ENTITY, e.getError());
        }
    }

    @Test
    public void testUpdateAdminPasswordWithPasswordNull() {
        try {
            adminService.updateAdminPassword("10000", null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(AdministratorServiceError.EMPTY_ENTITY, e.getError());
        }
    }

    @Test
    public void testUpdateAdminPasswordWithPasswordBlank() {
        try {
            adminService.updateAdminPassword("10000", "");
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(AdministratorServiceError.EMPTY_ENTITY, e.getError());
        }
    }


    private Administrator generateNewAdmin() {
        Administrator administrator = new Administrator();
        administrator.setPassword("123456");
        administrator.setMobile("18981926692");
        administrator.setJobNo(generateString("100000"));
        administrator.setStatus(Administrator.Status.inservice);
        administrator.setName("admin");
        administrator.setSalt("12398123");
        return administrator;
    }

    private String generateString(String data) {
        return System.currentTimeMillis() + data;
    }

}
