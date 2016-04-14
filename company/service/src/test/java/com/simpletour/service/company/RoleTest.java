package com.simpletour.service.company;

import com.simpletour.biz.company.IRoleBiz;
import com.simpletour.biz.company.error.ModuleBizError;
import com.simpletour.biz.company.error.RoleBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.error.DefaultError;
import com.simpletour.commons.data.error.IError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.common.security.token.ThreadLocalToken;
import com.simpletour.common.security.token.Token;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：角色权限关系功能模块测试用例
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/8 16:57
 * 备注说明：null
 */
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class RoleTest extends AbstractTestNGSpringContextTests {
    private static final Logger LOGGER = Logger.getLogger(RoleTest.class);

    static final String PERMISSION_REMARK;

    @Autowired
    IRoleService roleService;

    @Autowired
    ICompanyService companyService;

    @Autowired
    IEmployeeService employeeService;

    @Autowired
    IModuleService moduleService;

    @Autowired
    IPermissionService permissionService;

    List<Permission> permissionsList;

//    List<Employee> employeesList;

    List<Company> companiesList;

    List<Long> modulesId;

    List<Long> rolesId;

    Role savedRole;

    static {
        String remark = "";
        do {
            remark += "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMN";
            if (IRoleBiz.MAX_REMARK_LEN < remark.length()) {
                break;
            }
        } while (true);
        PERMISSION_REMARK = remark;
    }

    private void setLocalTokenWithCompanyId(Long uid, Long companyId) {
        ThreadLocalToken.setToken(new Token("1", "1", null == uid ? null : uid.toString(), "1", null == companyId ? null : companyId.toString(), "1", Token.ClientType.BROWSER) {
            @Override
            public String toCipherString() {
                return null;
            }
        });
    }

    private Module generateModule(String name){
        Module module = new Module(name, String.format("这是‘%s’的域名", name));
        Permission permission = new Permission(String.format("这是‘%s’的角色权限名称", name), String.format("这是‘%s’的角色权限路径", name), String.format("这是‘%s’的角色权限功能代码", name), module);
        List<Permission> permissions = new ArrayList<>(1);
        permissions.add(permission);
        module.setPermissions(permissions);
        return module;
    }

    private Module getModule(String name) {
        Module module;
        List<Module> modules = moduleService.findByName(name);

        if (modules.isEmpty()) {
            module = generateModule(name);
            Optional<Module> moduleOptional = moduleService.addModule(module);
            Assert.assertTrue(moduleOptional.isPresent());
            module = moduleOptional.get();
        } else {
            module = modules.get(0);
        }

        modulesId.add(module.getId());

        return module;
    }

    private void addCompanyAndEmployee() {
        Permission permission = new Permission("订单管理权限", "订单管理权限路径", "订单管理权限功能代码", getModule("订单功能模块"));
        Optional<Permission> permissionOptional = permissionService.addPermission(permission);
        Assert.assertTrue(permissionOptional.isPresent());
        permission = permissionOptional.get();
        permissionsList.add(permission);

        List<Permission> permissionsList1 = new ArrayList<>(1);
        permissionsList1.add(permission);

        Company company = new Company();
        company.setName("测试公司");
        company.setAddress("成都市天府软件园");
        company.setContacts("张三");
        company.setMobile("13312345678");
//        company.setEmployees(employeesList);
        company.setPermissions(permissionsList1);

        Optional<Company> companyOptional1 = companyService.addCompany(company);
        Assert.assertTrue(companyOptional1.isPresent());
        company = companyOptional1.get();
        companiesList.add(company);

//        Employee adminEmployee = new Employee();
//        adminEmployee.setAdmin(true);
//        adminEmployee.setJobNo(10000);
//        adminEmployee.setName("Admin");
//        adminEmployee.setMobile("13187654321");
//        adminEmployee.setCompany(company);
//
//        Optional<Employee> adminEmployeeOptional = employeeService.addEmployee(adminEmployee);
//        Assert.assertTrue(adminEmployeeOptional.isPresent());
//        adminEmployee = adminEmployeeOptional.get();
//        employeesList.add(adminEmployee);

        setLocalTokenWithCompanyId(0L, company.getId());

//        Employee generalEmployee = new Employee();
//        generalEmployee.setAdmin(false);
//        generalEmployee.setJobNo(10001);
//        generalEmployee.setName("General");
//        generalEmployee.setMobile("13812344321");
//        generalEmployee.setCompany(company);
//
//        Optional<Employee> generalEmployeeOptional = employeeService.addEmployee(generalEmployee);
//        Assert.assertTrue(generalEmployeeOptional.isPresent());
//        generalEmployee = generalEmployeeOptional.get();
//        employeesList.add(generalEmployee);

        Permission innerPermission = new Permission("内部人员管理权限", "内部人员管理权限路径", "内部人员管理权限功能代码", getModule("内部人员管理功能模块"));
        Optional<Permission> innerPermissionOptional = permissionService.addPermission(innerPermission);
        Assert.assertTrue(innerPermissionOptional.isPresent());
        innerPermission = innerPermissionOptional.get();
        permissionsList.add(innerPermission);

        List<Permission> permissionsList2 = new ArrayList<>(1);
        permissionsList2.add(innerPermission);

        Company softwareInc = new Company();
        softwareInc.setName("软件有限责任公司");
        softwareInc.setAddress("成都市天府新谷");
        softwareInc.setContacts("李四");
        softwareInc.setMobile("13312347444");
//        softwareInc.setEmployees(employeesList);
        softwareInc.setPermissions(permissionsList2);

        Optional<Company> companyOptional2 = companyService.addCompany(softwareInc);
        Assert.assertTrue(companyOptional2.isPresent());
        companiesList.add(companyOptional2.get());

//        Employee lisiEmployee = new Employee();
//        lisiEmployee.setAdmin(false);
//        lisiEmployee.setJobNo(14444);
//        lisiEmployee.setName("Boss");
//        lisiEmployee.setMobile("13812399333");
//        lisiEmployee.setCompany(softwareInc);
//
//        Optional<Employee> lisiEmployeeOptional = employeeService.addEmployee(lisiEmployee);
//        Assert.assertTrue(lisiEmployeeOptional.isPresent());
//        lisiEmployee = lisiEmployeeOptional.get();
//        employeesList.add(lisiEmployee);
    }

    private void deleteCompanyAndEmployee() {
        permissionsList.forEach(permission -> permissionService.deletePermission(permission.getId()));
        modulesId.forEach(id -> moduleService.deleteModule(id));
//        employeesList.forEach(employee -> employeeService.deleteEmployee(employee.getId()));
        companiesList.forEach(company -> companyService.deleteCompany(company.getId()));
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        LOGGER.info("enter beforeClass");

        modulesId = new ArrayList<>(2);
//        employeesList = new ArrayList<>(2);
        companiesList = new ArrayList<>(2);
        permissionsList = new ArrayList<>(2);
        rolesId = new ArrayList<>(1);

        addCompanyAndEmployee();

        LOGGER.info("end beforeClass");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        LOGGER.info("enter afterClass");

        deleteCompanyAndEmployee();

        rolesId.clear();
        permissionsList.clear();
        modulesId.clear();
//        employeesList.clear();
        companiesList.clear();

        LOGGER.info("end afterClass");
    }

    private Role saveRole(Long id, String name, String remark, Company company, List<Permission> permissionsList) {
        Role role = null;

        if (null != name || null != company) {
            if (null != id && null != savedRole) {
                role = savedRole;
            } else {
                role = new Role();
            }

            role.setName(name);
            role.setRemark(remark);
            role.setType(0);
            role.setCompany(company);
            role.setPermissionList(permissionsList);
        }

        Optional<Role> roleOptional = Optional.empty();

        try {
            roleOptional = null == id ? roleService.addRole(role) : roleService.updateRole(role);
        } catch (BaseSystemException bse) {
            IError error = bse.getError();
            if (RoleBizError.EMPTY_ENTITY.equals(error) || RoleBizError.ROLE_NAME_IS_EMPTY.equals(error) ||
                RoleBizError.ROLE_NAME_LEN_INVALID.equals(error) || RoleBizError.ROLE_REMARK_LEN_INVALID.equals(error) ||
                RoleBizError.INVALID_TENANT_ID.equals(error) || RoleBizError.UNIQUE_ROLE_NAME_IN_SAME_COMPANY.equals(error) ||
                RoleBizError.INVALID_COMPANY.equals(error) || DefaultError.NOT_SAME_TENANT_ID.equals(error) ||
                RoleBizError.INVALID_PERMISSION.equals(error) || RoleBizError.INVALID_MODULE.equals(error) ||
                PermissionBizError.EMPTY_ENTITY.equals(error) || ModuleBizError.MODULE_NULL.equals(error)) {
                LOGGER.error(bse.getMessage());
                return null;
            } else {
                bse.printStackTrace();
                Assert.fail("excute saveRole failed");
            }
        }

        Assert.assertTrue(roleOptional.isPresent());
        LOGGER.info("excute saveRole successfully");
        return roleOptional.get();
    }

    @Test(priority = 1)
    public void addRole() {
        LOGGER.info("enter addRole");

        List<Permission> permissions = permissionService.getPermissionsByName("订单管理权限");
        Company company = companiesList.get(0);
        savedRole = saveRole(null, "公司管理员角色权限", "公司管理员角色权限备注说明信息", company, permissions);
        Assert.assertNotNull(savedRole);
        rolesId.add(savedRole.getId());

        Assert.assertNull(saveRole(null, savedRole.getName(), "相同的角色名称", company, permissions));
        Assert.assertNull(saveRole(null, null, "空的角色名称", company, null));
        Assert.assertNull(saveRole(null, "1", "无效的角色名称", company, null));
        Assert.assertNull(saveRole(null, "备注过长的角色权限", PERMISSION_REMARK, company, null));
        Assert.assertNull(saveRole(null, "test_role", "无效公司的角色权限", null, null));

        Permission permission = new Permission("公司管理权限", "公司管理权限路径", "公司管理权限功能代码", null);
        permissions.clear();
        permissions.add(permission);
        Assert.assertNull(saveRole(null, "公司管理用户角色权限", "权限范围无效", company, permissions));

        Optional<Company> companyOptional = companyService.getCompanyByName("测试公司");
        Assert.assertTrue(companyOptional.isPresent());

        setLocalTokenWithCompanyId(0L, 0L);
        Assert.assertNull(saveRole(null, "公司管理用户角色权限", "不同的租户ID", companyOptional.get(), permissions));

        setLocalTokenWithCompanyId(0L, null);
        Assert.assertNull(saveRole(null, "公司管理用户角色权限", "无效的租户ID", null, null));

        //Long employeeId = employeesList.get(0).getId();
        Company softwareInc = companiesList.get(1);
        setLocalTokenWithCompanyId(0L, softwareInc.getId());

        permissions.clear();
        permissions = permissionService.getPermissionsByName("内部人员管理权限");
        Role role = saveRole(null, "内部管理员角色权限", "内部管理员角色权限备注说明信息", softwareInc, permissions);
        Assert.assertNotNull(role);
        rolesId.add(role.getId());

        setLocalTokenWithCompanyId(0L, company.getId());

        LOGGER.info("enter addRole");
    }

    @Test(priority = 2) //, dependsOnMethods = "addRole"
    public void updateRole() {
        LOGGER.info("enter updateRole");

        savedRole.setName("普通用户角色权限");
        savedRole.setRemark("普通用户角色权限说明");

        Company company = companiesList.get(0);
        List<Permission> permissions = permissionService.getPermissionsByName("订单管理权限");
        Long roleId = savedRole.getId();
        savedRole = saveRole(roleId, "公司管理员角色权限", "公司管理员角色权限备注说明信息", company, permissions);
        Assert.assertNotNull(savedRole);
        Assert.assertEquals(roleId, savedRole.getId());
        rolesId.add(savedRole.getId());

        Assert.assertNull(saveRole(roleId, savedRole.getName(), "相同的角色名称", company, null));

        LOGGER.info("enter updateRole");
    }

    @Test(priority = 3)
    public void getRolesList() {
        LOGGER.info("enter getRolesList");

        RoleQuery query = new RoleQuery("公司管理员角色权限", null, null, null);
        List<Role> rolesList = roleService.getRolesList(query);
        Assert.assertNotNull(rolesList);
        Assert.assertEquals(1, rolesList.size());

        rolesList.clear();
        query.setName("内部管理员角色权限");
        rolesList = roleService.getRolesList(query);
        Assert.assertNotNull(rolesList);
        Assert.assertTrue(rolesList.isEmpty());

        query.setName("公司管理员角色权限");
        query.setPermission("订单管理权限");
        query.setModule("订单功能模块");
        rolesList = roleService.getRolesList(query);
        Assert.assertNotNull(rolesList);
        Assert.assertEquals(1, rolesList.size());

        LOGGER.info("end getRolesList");
    }

    @Test(priority = 4)
    public void getRolesPages() {
        LOGGER.info("enter getRolesPages");

        RoleQuery query = new RoleQuery("公司管理员角色权限", "功能模块", "权限", null);
        DomainPage<Role> rolesPage = roleService.getRolesPages(query);
        Assert.assertNotNull(rolesPage);
        Assert.assertEquals(1, rolesPage.getDomainTotalCount());

        query.setName("内部管理员角色权限");
        rolesPage = roleService.getRolesPages(query);
        Assert.assertNotNull(rolesPage);
        Assert.assertTrue(rolesPage.getDomains().isEmpty());

        query.setName("管理员角色权限");
        query.setPermission("权限");
        query.setModule("功能模块");
        rolesPage = roleService.getRolesPages(query);
        Assert.assertNotNull(rolesPage);
        Assert.assertEquals(1, rolesPage.getDomainTotalCount());

        setLocalTokenWithCompanyId(0L, null);

        rolesPage = roleService.getRolesPages(query);
        Assert.assertNotNull(rolesPage);
        Assert.assertEquals(2, rolesPage.getDomainTotalCount());

        setLocalTokenWithCompanyId(0L, companiesList.get(0).getId());

        LOGGER.info("end getRolesPages");
    }

    @Test(priority = 5)
    public void deleteRoles() {
        LOGGER.info("enter getRolesPages");

        roleService.deleteRoleById(rolesId.get(0));

        try {
            roleService.deleteRoleById(rolesId.get(1));
        } catch (BaseSystemException bse) {
            IError error = bse.getError();
            if (RoleBizError.INVALID_TENANT_ID.equals(error) || RoleBizError.INVALID_COMPANY.equals(error) || DefaultError.NOT_SAME_TENANT_ID.equals(error)) {
                LOGGER.error(bse.getMessage());
            } else {
                bse.printStackTrace();
                Assert.fail("excute deleteRoles failed");
            }
        }

        setLocalTokenWithCompanyId(0L, companiesList.get(1).getId());
        roleService.deleteRoleById(rolesId.get(1));

        LOGGER.info("enter deleteRoles");
    }
}
