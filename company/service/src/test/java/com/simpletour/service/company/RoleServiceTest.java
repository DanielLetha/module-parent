package com.simpletour.service.company;

import com.simpletour.biz.company.error.ModuleBizError;
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
import com.simpletour.service.company.data.RoleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：角色权限关系功能模块测试用例
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/8 16:57
 * 备注说明：null
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RoleServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    IRoleService roleService;

    @Autowired
    ICompanyService companyService;

    @Autowired
    IModuleService moduleService;

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
        System.out.println("enter RoleServiceTest beforeClass");

        companiesList = new ArrayList<>();
        modulesList = new ArrayList<>();
        rolesList = new ArrayList<>();

        roleData = new RoleData(companyService, moduleService, jdbcTemplate, companiesList, modulesList);
        roleData.generateCompanies(10000L, "成都简途", "成都天府软件园C区", "杨老板", "13353329876");
        roleData.generateCompanies(10001L, "成都放贷", "成都天府广场锦江区", "张哥", "13353398765");
        roleData.generateModulesAndPermissions();

        System.out.println("end RoleServiceTest beforeClass");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        System.out.println("enter RoleServiceTest afterClass");

        if (deleteAll && null != savedRole) {
            roleData.cleanUpAllData(savedRole.getId());
        }

        modulesList.clear();
        companiesList.clear();
        rolesList.clear();

        System.out.println("end RoleServiceTest afterClass");
    }

    private Role saveRole(Long id, String name, String remark, Company company, List<Permission> permissionsList) {
        Role role = null;

        if (null != name || null != company) {
            if (null != id && null != savedRole) {
                role = roleService.getRoleById(id).get();
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
            roleOptional = null == id ? roleService.addRole(role) : roleService.updateRole(role);
        } catch (BaseSystemException bse) {
            IError error = bse.getError();
            System.err.println("\n" + bse.getMessage());
            if (RoleBizError.EMPTY_ENTITY.equals(error) || RoleBizError.ROLE_NAME_IS_EMPTY.equals(error) ||
                ModuleBizError.PERMISSION_NULL.equals(error) || ModuleBizError.PERMISSION_NOT_EXIST.equals(error) ||
                RoleBizError.INVALID_TENANT_ID.equals(error) || RoleBizError.UNIQUE_ROLE_NAME_IN_SAME_COMPANY.equals(error) ||
                RoleBizError.INVALID_COMPANY.equals(error) || DefaultError.NOT_SAME_TENANT_ID.equals(error)) {
                return null;
            } else {
                bse.printStackTrace();
                Assert.fail("excute RoleServiceTest.saveRole failed");
            }
        }

        Assert.assertTrue(roleOptional.isPresent());
        System.out.println("excute RoleServiceTest.saveRole successfully");

        return roleOptional.get();
    }

    @Test(priority = 1)
    public void addRole() {
        System.out.println("enter RoleServiceTest addRole");

        Company savedCompany = companiesList.get(0);
        List<Permission> permissions = modulesList.get(1).getPermissions();

        savedRole = saveRole(null, "人员角色", "正常添加人员角色权限关系", savedCompany, permissions);
        Assert.assertNotNull(savedRole);
        Assert.assertNotNull(savedRole.getId());
        rolesList.add(savedRole);

        Role role1 = saveRole(null, "人员角色1", "添加模块功能不存在的角色权限关系", savedCompany, Collections.emptyList());
        Assert.assertNull(role1);

        Role role2 = saveRole(null, "人员角色2", "添加模块功能不存在的角色权限关系", savedCompany, modulesList.get(3).getPermissions());
        Assert.assertNull(role2);

        System.out.println("end RoleServiceTest addRole");
    }

    @Test(priority = 2)
    public void updateRole() {
        System.out.println("enter RoleServiceTest updateRole");

        Company savedCompany = companiesList.get(0);
        List<Permission> permissions = modulesList.get(1).getPermissions();

        Long roleId = savedRole.getId();
        savedRole = saveRole(roleId, "新的人员角色", "更新人员角色名称", savedCompany, permissions);
        Assert.assertNotNull(savedRole);
        Assert.assertEquals(roleId, savedRole.getId());
        Assert.assertNotEquals("人员角色", savedRole.getName());

        Role role = new Role();
        try {
            roleService.updateRole(role);
        } catch (BaseSystemException bse) {
            IError error = bse.getError();
            if (RoleBizError.ID_NULL.equals(error)) {
                System.err.println("\n" + bse.getMessage());
            } else {
                bse.printStackTrace();
                Assert.fail("excute RoleServiceTest.updateRole failed");
            }
        }

        System.out.println("enter RoleServiceTest updateRole");
    }

    @Test(priority = 3)
    public void getRolesList() {
        System.out.println("enter RoleServiceTest getRolesList");

        RoleQuery query = new RoleQuery("新的人员角色", null, null, null);
        List<Role> rolesList = roleService.getRolesList(query);
        Assert.assertNotNull(rolesList);
        Assert.assertEquals(1, rolesList.size());

        rolesList.clear();
        query.setName("订单角色1");
        rolesList = roleService.getRolesList(query);
        Assert.assertNotNull(rolesList);
        Assert.assertTrue(rolesList.isEmpty());

        System.out.println("end RoleServiceTest getRolesList");
    }

    @Test(priority = 4)
    public void getRolesPages() {
        System.out.println("enter RoleServiceTest getRolesPages");

        RoleQuery query = new RoleQuery("新的人员角色", null, null, null);
        DomainPage<Role> rolesPage = roleService.getRolesPages(query);
        Assert.assertNotNull(rolesPage);

        long count = rolesPage.getDomainTotalCount();
        Assert.assertEquals(1, count);
        Assert.assertEquals(count, rolesPage.getDomains().size());

        query.setName("新的人员角色");
        query.setModule("人员角色管理模块");
        rolesPage = roleService.getRolesPages(query);
        Assert.assertNotNull(rolesPage);
        Assert.assertEquals(0, rolesPage.getDomainTotalCount());
        Assert.assertTrue(rolesPage.getDomains().isEmpty());

        query.setName("人员角色");
        query.setModule("人员管理功能");
        query.setPermission("人员管理权限");
        rolesPage = roleService.getRolesPages(query);
        Assert.assertNotNull(rolesPage);
        Assert.assertEquals(1, rolesPage.getDomainTotalCount());
        Assert.assertEquals(3, rolesPage.getDomains().get(0).getPermissionList().size());

        count = rolesPage.getDomainTotalCount();
        Assert.assertEquals(1, count);
        Assert.assertEquals(count, rolesPage.getDomains().size());

        System.out.println("end RoleServiceTest getRolesPages");
    }

    @Test(priority = 5)
    public void deleteRoles() {
        System.out.println("enter RoleServiceTest getRolesPages");

        rolesList.forEach(item -> {
            try {
                roleService.deleteRoleById(item.getId());
                System.out.println("excute RoleServiceTest.deleteRoles successfully");
            } catch (BaseSystemException bse) {
                IError error = bse.getError();
                System.err.println(bse.getMessage());
                if (RoleBizError.INVALID_TENANT_ID.equals(error) || RoleBizError.INVALID_COMPANY.equals(error) ||
                        RoleBizError.DELETE_FAILD.equals(error) || DefaultError.NOT_SAME_TENANT_ID.equals(error)) {
                    System.out.println("catch an error: " + error.getErrorCode());
                } else {
                    System.err.println("cause: " + item.getRemark());
                    bse.printStackTrace();
                    Assert.fail("excute RoleServiceTest.deleteRoles failed");
                }
            }
        });

        rolesList.clear();

        System.out.println("enter RoleServiceTest deleteRoles");
    }
}
