package com.simpletour.service.company;

import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.dao.company.query.CompanyDaoQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.service.company.error.CompanyServiceError;
import junit.framework.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Brief :  ${用途}
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/9 9:52
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CompanyServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IModuleService moduleService;
    @Autowired
    private ICompanyDao companyDao;

    private Long companyId;

    private Long moudleId;

    private Long permissionId;


    /**
     * 初始化数据，先构造permission,再持久化module,最后持久化company
     */
    @BeforeClass
    public void setUp() {

        Company company = new Company();
        company.setName("简途AAA");
        company.setAddress("软件园");
        company.setPermissions(generateGetPermissionList());
        Optional<Company> optional = companyService.addCompany(company);
        companyId = optional.get().getId();
        Assert.assertTrue(optional.isPresent());
    }

    @AfterClass
    public void tearDown() {
        if (companyId != null) {
            companyDao.removeEntityById(Company.class, companyId);
        }
        if (permissionId != null) {
            companyDao.removeEntityById(Permission.class, permissionId);
        }
        if (moudleId != null) {
            companyDao.removeEntityById(Module.class, moudleId);
        }

    }

    /**
     * 测试添加空的公司
     */
    @Test(priority = 1)
    public void testAddCompanyWithNull() {
        try {
            Optional<Company> optional = companyService.addCompany(null);
            Assert.assertTrue(optional.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), CompanyServiceError.COMPANY_NULL);
        }
    }


    /**
     * 测试添加公司没有功能
     */
    @Test(priority = 2)
    public void testAddCompanyWithNoPermission() {
        try {
            Company company = new Company();
            company.setName("简途BBB");
            Optional<Company> op = companyService.addCompany(company);
            Assert.assertTrue(op.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyServiceError.COMPANY_MUST_CONTAIN_PERMISSION, e.getError());
        }
    }


    /**
     * 测试更新公司使用不存在的实体
     */
    @Test(priority = 3)
    public void testUpdateCompanyWithNotExist() {
        try {
            Company company = new Company();
            company.setName("简途CCC");
            Optional<Company> op = companyService.updateCompany(company);
            Assert.assertTrue(op.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyServiceError.ID_NULL, e.getError());
        }
    }

    /**
     * 测试添加公司没有功能
     */
    @Test(priority = 4)
    public void testUpdateCompanyWithNoPermission() {
        try {
            Optional<Company> companyOptional = companyService.getCompanyById(companyId);
            Assert.assertTrue(companyOptional.isPresent());
            companyOptional.get().setPermissions(null);
            Optional<Company> op = companyService.updateCompany(companyOptional.get());
            Assert.assertTrue(op.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyServiceError.COMPANY_MUST_CONTAIN_PERMISSION, e.getError());
        }
    }

    /**
     * 测试更新公司
     */
    @Test(priority = 5)
    public void testUpdateCompany() {
        Optional<Company> companyOptional = companyService.getCompanyById(companyId);
        companyOptional.get().setAddress("天府软件园C区11");
        Optional<Company> op = companyService.updateCompany(companyOptional.get());
        Assert.assertTrue(op.isPresent());

    }

    /**
     * 测试根据id获取公司
     */
    @Test(priority = 6)
    public void testGetCompanyById() {
        Optional<Company> companyOptional = companyService.getCompanyById(companyId);
        Assert.assertTrue(companyOptional.isPresent());
    }

    /**
     * 测试分页获取公司
     */
    @Test(priority = 7)
    public void testGetCompanyDomainPage() {
        CompanyDaoQuery query = new CompanyDaoQuery();
        AndConditionSet condition = new AndConditionSet();
        condition.addCondition("name", "简途", Condition.MatchType.like);
        condition.addCondition("address", "软件园", Condition.MatchType.like);
        query.setCondition(condition);
        DomainPage<Company> domainPage = companyService.getCompanyPagesByQuery(query);
        Assert.assertTrue(domainPage.getDomains().size() > 0);
    }

    @Test(priority = 8)
    public void testDeleteCompanyWithNullId() {
        try {
            companyService.deleteCompany(null);
            Assert.assertFalse(true);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyServiceError.ID_NULL, e.getError());
        }
    }
    @Test(priority = 9)
    public void testDeleteCompany(){
           companyService.deleteCompany(companyId);
        Optional<Company> optional = companyService.getCompanyById(companyId);
        Assert.assertFalse(optional.isPresent());
    }

    private List<Permission> generateGetPermissionList() {
        double i =  Math.random();

        Permission permission = new Permission();
        permission.setCode("TEST_PERMISSION" + i);
        permission.setName("测试权限" + i);
        permission.setPath("测试路径"+i);
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(permission);

        Module module = new Module();
        module.setName("测试模块" + i);
        module.setDomain("测试domain" + i);
        module.setPermissions(permissionList);
        Optional<Module> moduleOptional = moduleService.addModule(module);
        moudleId = moduleOptional.get().getId();
        permissionId = moduleOptional.get().getPermissions().get(0).getId();

        return moduleOptional.get().getPermissions();
    }

}
