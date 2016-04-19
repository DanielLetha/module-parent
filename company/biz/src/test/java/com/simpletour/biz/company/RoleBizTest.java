package com.simpletour.biz.company;

import com.simpletour.biz.company.data.RoleData;
import com.simpletour.biz.company.error.RoleBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.error.DefaultError;
import com.simpletour.commons.data.error.IError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：角色权限关系业务层测试类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016-4-14
 * 备注说明：null
 * @since 2.0-SNAPSHOT
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RoleBizTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private IRoleBiz roleBiz;

    @Autowired
    private ICompanyBiz companyBiz;

    @Autowired
    private IModuleBiz moduleBiz;

    @Autowired
    JdbcTemplate jdbcTemplate;

    List<Company> companiesList;

    List<Module> modulesList;

    List<Role> rolesList;

    Role savedRole;

    RoleData roleData;

    boolean deleteAll = true;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        System.out.println("enter RoleBizTest beforeClass");

        companiesList = new ArrayList<>();
        modulesList = new ArrayList<>();
        rolesList = new ArrayList<>();

        roleData = new RoleData(companyBiz, moduleBiz, jdbcTemplate, companiesList, modulesList);
        roleData.generateCompanies(10000L, "成都简途", "成都天府软件园C区", "杨老板", "13353329876");
        roleData.generateCompanies(10001L, "成都放贷", "成都天府广场锦江区", "张哥", "13353398765");
        roleData.generateModulesAndPermissions();

        System.out.println("end RoleBizTest beforeClass");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        System.out.println("enter RoleBizTest afterClass");

        //modulesList.forEach(module -> moduleBiz.deleteModule(module.getId()));
        //companiesList.forEach(company -> companyBiz.deleteCompany(company.getId()));

        if (deleteAll && null != savedRole) {
            roleData.cleanUpAllData(savedRole.getId());
        }

        modulesList.clear();
        companiesList.clear();
        rolesList.clear();

        System.out.println("end RoleBizTest afterClass");
    }

    private Role saveRole(Long id, String name, String remark, Company company, List<Permission> permissionsList) {
        Role role = null;

        if (null != name || null != company) {
            if (null != id && null != savedRole) {
                role = roleBiz.getRoleById(id).get();
                Assert.assertNotNull(role);
            } else {
                role = new Role();
                role.setType(0);
            }

            role.setName(name);
            role.setRemark(remark);
            role.setCompany(company);
            role.setPermissionList(permissionsList);
        }

        Optional<Role> roleOptional = Optional.empty();

        try {
            roleBiz.validateRole(role);
            roleOptional = roleBiz.saveRole(role);
        } catch (BaseSystemException bse) {
            IError error = bse.getError();
            System.err.println("\n" + bse.getMessage());
            if (RoleBizError.EMPTY_ENTITY.equals(error) || RoleBizError.ROLE_NAME_IS_EMPTY.equals(error) ||
                    RoleBizError.INVALID_TENANT_ID.equals(error) || RoleBizError.UNIQUE_ROLE_NAME_IN_SAME_COMPANY.equals(error) ||
                    RoleBizError.INVALID_COMPANY.equals(error) || DefaultError.NOT_SAME_TENANT_ID.equals(error)) {
                return null;
            } else {
                bse.printStackTrace();
                Assert.fail("excute RoleBizTest.saveRole failed");
            }
        }

        Assert.assertTrue(roleOptional.isPresent());
        System.out.println("excute RoleBizTest.saveRole successfully");

        return roleOptional.get();
    }

    @Test(priority = 1)
    public void addRole() {
        System.out.println("enter RoleBizTest addRole");

        Company savedCompany = companiesList.get(0);
        List<Permission> permissions = modulesList.get(0).getPermissions();

        savedRole = saveRole(null, "订单角色", "正常添加角色权限关系", savedCompany, permissions);
        Assert.assertNotNull(savedRole);
        Assert.assertNotNull(savedRole.getId());
        rolesList.add(savedRole);

        Role role1 = saveRole(null, "订单角色", "跨公司添加角色权限关系", companiesList.get(1), permissions);
        Assert.assertNull(role1);

        Role role2 = saveRole(null, "", "添加空名称的角色权限关系", savedCompany, permissions);
        Assert.assertNull(role2);

        Role role3 = saveRole(null, "订单角色", "添加同名的角色权限关系", savedCompany, permissions);
        Assert.assertNull(role3);

        System.out.println("end RoleBizTest addRole");
    }

    @Test(priority = 2)
    public void updateRole() {
        System.out.println("enter RoleBizTest updateRole");

        Company savedCompany = companiesList.get(0);
        List<Permission> permissions = modulesList.get(0).getPermissions();
        List<Permission> permissionsList = new ArrayList<>(1);
        permissionsList.add(permissions.get(0));

        Long roleId = savedRole.getId();
        Role role = saveRole(roleId, "订单角色", "更新角色的功能列表", savedCompany, permissionsList);
        Assert.assertNotNull(role);
        Assert.assertEquals(1, role.getPermissionList().size());

        permissionsList.clear();
        permissionsList.addAll(roleData.getModulesList("订单功能模块名称1").get(0).getPermissions());

        savedRole = saveRole(roleId, "新的订单角色", "更新订单角色名称及功能列表", savedCompany, permissionsList);
        Assert.assertNotNull(savedRole);
        Assert.assertEquals(permissions.size(), savedRole.getPermissionList().size());

        role = saveRole(roleId, "新的订单角色", "跨公司更新订单角色名称", companiesList.get(1), permissions);
        Assert.assertNull(role);

        System.out.println("enter RoleBizTest updateRole");
    }

    @Test(priority = 3)
    public void getRolesList() {
        System.out.println("enter RoleBizTest getRolesList");

        RoleQuery query = new RoleQuery("新的订单角色", null, null, null);
        List<Role> rolesList = roleBiz.getRolesList(query);
        Assert.assertNotNull(rolesList);
        Assert.assertEquals(1, rolesList.size());

        rolesList.clear();
        query.setName("订单角色");
        rolesList = roleBiz.getRolesList(query);
        Assert.assertNotNull(rolesList);
        Assert.assertTrue(rolesList.isEmpty());

        System.out.println("end RoleBizTest getRolesList");
    }

    @Test(priority = 4)
    public void getRolesPages() {
        System.out.println("enter RoleBizTest getRolesPages");

        RoleQuery query = new RoleQuery("新的订单角色", null, null, null);
        DomainPage<Role> rolesPage = roleBiz.getRolesPages(query);
        Assert.assertNotNull(rolesPage);

        long count = rolesPage.getDomainTotalCount();
        Assert.assertEquals(1, count);
        Assert.assertEquals(count, rolesPage.getDomains().size());

        query.setName("新的订单角色");
        query.setModule("订单管理模块");
        rolesPage = roleBiz.getRolesPages(query);
        Assert.assertNotNull(rolesPage);
        Assert.assertEquals(0, rolesPage.getDomainTotalCount());
        Assert.assertTrue(rolesPage.getDomains().isEmpty());

        query.setName("新的订单角色");
        query.setModule("订单功能模块");
        query.setPermission("订单权限");
        rolesPage = roleBiz.getRolesPages(query);
        Assert.assertNotNull(rolesPage);
        Assert.assertEquals(1, rolesPage.getDomainTotalCount());
        Assert.assertEquals(3, rolesPage.getDomains().get(0).getPermissionList().size());

        count = rolesPage.getDomainTotalCount();
        Assert.assertEquals(1, count);
        Assert.assertEquals(count, rolesPage.getDomains().size());

        System.out.println("end RoleBizTest getRolesPages");
    }

    @Test(priority = 5)
    public void deleteRoles() {
        System.out.println("enter RoleBizTest deleteRoles");

        Role role = new Role("临时角色", 0, null, "非真实存在", null);
        role.setId(10099213L);
        rolesList.add(role);

        rolesList.forEach(item -> {
            try {
                roleBiz.deleteRoleById(item.getId());
                System.out.println("excute RoleBizTest.deleteRoles successfully");
            } catch (BaseSystemException bse) {
                IError error = bse.getError();
                System.err.println(bse.getMessage());
                if (RoleBizError.INVALID_TENANT_ID.equals(error) || RoleBizError.INVALID_COMPANY.equals(error) ||
                    RoleBizError.DELETE_FAILD.equals(error) || DefaultError.NOT_SAME_TENANT_ID.equals(error)) {
                    System.out.println("catch an error: " + error.getErrorCode());
                } else {
                    System.err.println("cause: " + item.getRemark());
                    bse.printStackTrace();
                    Assert.fail("excute RoleBizTest.deleteRoles failed");
                }
            }
        });

        rolesList.clear();

        System.out.println("enter RoleBizTest deleteRoles");
    }
}
