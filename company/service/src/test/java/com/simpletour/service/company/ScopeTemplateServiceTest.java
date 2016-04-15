package com.simpletour.service.company;

import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IScopeTemplateDao;
import com.simpletour.dao.company.query.ScopeTemplateDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.ScopeTemplate;
import com.simpletour.service.company.error.ScopeTemplateServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Brief :  权限范围服务的测试类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/11 15:51
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ScopeTemplateServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private IScopeTemplateService scopeTemplateService;

    @Autowired
    private IScopeTemplateDao scopeTemplateDao;

    private Long moudleId;

    private Long permissionId;

    private Long scopeTemplateId;

    /**
     * 初始化数据
     */
    @BeforeClass
    public void init() {
        ScopeTemplate scopeTemplate = new ScopeTemplate("测试权限范围", "测试权限范围");
        scopeTemplate.setPermissions(generateGetPermissionList());
        Optional<ScopeTemplate> scopeTemplate1 = scopeTemplateService.addScopeTemplate(scopeTemplate);
        scopeTemplateId = scopeTemplate1.get().getId();

    }

    /**
     * 清除数据
     */
    @AfterClass
    public void clearData() {
        if (scopeTemplateId != null) {
            scopeTemplateDao.removeEntityById(ScopeTemplate.class, scopeTemplateId);
        }

        if (moudleId != null) {
            scopeTemplateDao.removeEntityById(Module.class, moudleId);
        }
    }

    /**
     * 测试添加空值添加权限范围
     */
    @Test(priority = 1)
    public void addScopeTemplateWithNull() {
        try {
            Optional<ScopeTemplate> optional = scopeTemplateService.addScopeTemplate(null);
            Assert.assertTrue(optional.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL, e.getError());
        }
    }

    /**
     * 测试添加id不为空的权限对象
     */
    @Test(priority = 2)
    public void addScopeTemplateWithIDNotNull() {
        Optional<ScopeTemplate> stOptional = scopeTemplateService.getScopeTemplateById(scopeTemplateId);
        Assert.assertTrue(stOptional.isPresent());
        try {
            Optional<ScopeTemplate> scopeTemplate1 = scopeTemplateService.addScopeTemplate(stOptional.get());
            Assert.assertTrue(scopeTemplate1.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_DATA_ERROR, e.getError());
        }

    }

    /**
     * 测试添加不含权限集合的权限范围
     */
    @Test(priority = 3)
    public void addScopetTemplateWithNoPermission() {
        ScopeTemplate scopeTemplate = new ScopeTemplate("测试权限范围33", "测试权限范围33");
        try {
            Optional<ScopeTemplate> scopeTemplate1 = scopeTemplateService.addScopeTemplate(scopeTemplate);
            Assert.assertTrue(scopeTemplate1.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NULL, e.getError());
        }
    }

    /**
     * 测试添加不存在的权限集合
     */
    @Test(priority = 4)
    public void addScopeTemplateWithNoExistPermission() {
        ScopeTemplate scopeTemplate = new ScopeTemplate("测试权限范围44", "测试权限范围44");
        List<Permission> permissionList = new ArrayList<>();
        scopeTemplate.setPermissions(permissionList);
        try {
            Optional<ScopeTemplate> scopeTemplate1 = scopeTemplateService.addScopeTemplate(scopeTemplate);
            Assert.assertTrue(scopeTemplate1.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NULL, e.getError());
        }

    }

    /**
     * 更新空的权限范围
     */
    @Test(priority = 5)
    public void updateScopeTemplateNull() {
        try {
            Optional<ScopeTemplate> scopeTemplateOptional = scopeTemplateService.updateScopeTemplate(null);
            Assert.assertTrue(scopeTemplateOptional.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL, e.getError());
        }

    }

    /**
     * 测试更新id不为空的权限对象
     */
    @Test(priority = 6)
    public void updateScopeTemplateWithIDNull() {
        ScopeTemplate scopeTemplate = new ScopeTemplate("测试权限范围55", "测试权限范围55");
        try {
            Optional<ScopeTemplate> scopeTemplateOptional = scopeTemplateService.updateScopeTemplate(scopeTemplate);
            Assert.assertTrue(scopeTemplateOptional.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL, e.getError());
        }

    }

    /**
     * 测试更新权限为空的权限范围对象
     */
    @Test(priority = 7)
    public void updateScopeTemplateWithNoPermission() {
        Optional<ScopeTemplate> stOptional = scopeTemplateService.getScopeTemplateById(scopeTemplateId);
        Assert.assertTrue(stOptional.isPresent());
        stOptional.get().setPermissions(null);
        try {
            Optional<ScopeTemplate> scopeTemplateOptional = scopeTemplateService.updateScopeTemplate(stOptional.get());
            Assert.assertTrue(scopeTemplateOptional.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NULL, e.getError());
        }

    }

    /**
     * 测试更新不存在的权限范围
     */
    @Test(priority = 8)
    public void updateScopeTemplateWithIDNotExist() {
        Optional<ScopeTemplate> stOptional = scopeTemplateService.getScopeTemplateById(scopeTemplateId);
        Assert.assertTrue(stOptional.isPresent());
        List<Permission> permissionList = new ArrayList<>();
        stOptional.get().setPermissions(permissionList);
        try {
            Optional<ScopeTemplate> scopeTemplateOptional = scopeTemplateService.updateScopeTemplate(stOptional.get());
            Assert.assertTrue(scopeTemplateOptional.isPresent());
        } catch (BaseSystemException e) {
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NULL, e.getError());
        }
    }

    /**
     * 测试根据id获取权限范围
     */
    @Test(priority = 9)
    public void testGetScopeTemplateById() {

        Optional<ScopeTemplate> stOptional = scopeTemplateService.getScopeTemplateById(scopeTemplateId);
        Assert.assertTrue(stOptional.isPresent());
    }

    /**
     * 测试分页获取权限范围
     */
    @Test(priority = 10)
    public void testQueryScopeTemplateDomainPage() {
        ScopeTemplateDaoQuery query = new ScopeTemplateDaoQuery();
        AndConditionSet condition = new AndConditionSet();
        condition.addCondition("name", "测试", Condition.MatchType.like);
        query.setCondition(condition);
        DomainPage<ScopeTemplate> domainPage = scopeTemplateService.findScopeTemplatePage(query);
        Assert.assertTrue(domainPage.getDomains().size() > 0);
    }

    /**
     * 测试删除不存在权限范围
     */
    @Test(priority = 11)
    public void testDelScopeTemplateNoExist() {
        try {
            scopeTemplateService.deleteScopeTemplate(10112L);
            Optional<ScopeTemplate> stOptional =  scopeTemplateService.getScopeTemplateById(10112L);
            Assert.assertTrue(!stOptional.isPresent());
        }catch (BaseSystemException e){
            Assert.assertEquals(ScopeTemplateServiceError.SCOPE_TEMPLATE_NOT_EXIST,e.getError());
        }

    }

    /**
     * 测试删除权限范围
     */
    @Test(priority = 12)
    public void testDelScopeTemplate() {
        scopeTemplateService.deleteScopeTemplate(scopeTemplateId);
        Optional<ScopeTemplate> stOptional = scopeTemplateService.getScopeTemplateById(scopeTemplateId);
        Assert.assertFalse(stOptional.isPresent());
    }

    private List<Permission> generateGetPermissionList() {
        int i = (int) Math.random();

        Permission permission = new Permission();
        permission.setCode("TEST_PERMISSION" + i);
        permission.setName("测试添加权限" + i);
        permission.setPath("TEST");
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(permission);

        Module module = new Module();
        module.setName("测试添加模块" + i);
        module.setDomain("测试domain" + i);
        module.setPermissions(permissionList);
        Optional<Module> moduleOptional = moduleService.addModule(module);
        moudleId = moduleOptional.get().getId();
        permissionId = moduleOptional.get().getPermissions().get(0).getId();

        return moduleOptional.get().getPermissions();
    }
}
