package com.simpletour.biz.company.data;

import com.simpletour.biz.company.ICompanyBiz;
import com.simpletour.biz.company.IModuleBiz;
import com.simpletour.biz.company.error.ModuleBizError;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.error.IError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件描述：角色权限关系数据构造类
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/18 19:40
 * 备注说明：null
 */
public class RoleData {
    private ICompanyBiz companyBiz;

    private IModuleBiz moduleBiz;

    JdbcTemplate jdbcTemplate;

    List<Company> companiesList;

    List<Module> modulesList;

    public RoleData(ICompanyBiz companyBiz, IModuleBiz moduleBiz, JdbcTemplate jdbcTemplate, List<Company> companiesList, List<Module> modulesList) {
        this.companyBiz = companyBiz;
        this.moduleBiz = moduleBiz;
        this.jdbcTemplate = jdbcTemplate;
        this.companiesList = companiesList;
        this.modulesList = modulesList;
    }

    public List<Module> getModulesList(String name) {
        ModuleDaoQuery query = new ModuleDaoQuery();
        AndConditionSet condition = new AndConditionSet();
        condition.addCondition("name", name);
        query.setCondition(condition);
        List<Module> modules = moduleBiz.findModuleList(query);
        Assert.assertFalse(modules.isEmpty());
        return modules;
    }

    public void generateModuleAndPermissions(Company company, String name) {
        final int count = 3;
        Module module = new Module(name + "功能模块名称1", name + "功能模块域名1");
        List<Permission> permissionsList = new ArrayList<>(count);
        List<Company> companies = new ArrayList<>(1);

        companies.add(company);

        for (int index = 0; index < count; index++) {
            Permission permission = new Permission(name + "权限名称-" + index, name + "权限路径-" + index, name + "权限代码-" + index, module);
            permission.setCompanyList(companies);
            permissionsList.add(permission);
        }

        module.setPermissions(permissionsList);

        Module savedModule = null;
        try {
            savedModule = moduleBiz.addModule(module);
            Assert.assertNotNull(savedModule);
            Assert.assertNotNull(savedModule.getId());
        } catch (BaseSystemException bse) {
            IError error = bse.getError();
            System.err.println(bse.getMessage());
            if (ModuleBizError.MODULE_NAME_EXIST.equals(error)) {
                savedModule = getModulesList(module.getName()).get(0);
            } else {
                bse.printStackTrace();
                Assert.fail("excute RoleBizTest.generateModuleAndPermissions failed");
            }
        }

        modulesList.add(savedModule);
    }

    public void generateModulesAndPermissions() {
        generateModuleAndPermissions(companiesList.get(0), "订单");
        generateModuleAndPermissions(companiesList.get(0), "人员管理");
        generateModuleAndPermissions(companiesList.get(1), "资源管理");
    }

    public void generateCompanies(Long id, String name, String address, String contact, String mobile) {
        Company savedCompany = companyBiz.findCompanyById(id);
        boolean ret = null == savedCompany;

        if (ret) {
            String sql = String.format("INSERT INTO sys_company(id, createdtime, del, address, contacts, mobile, name, remark, version) VALUES (%d, current_timestamp, FALSE, '%s', '%s', '%s', '%s', '公司ID:%d', 0)",
                    id, address, contact, mobile, name, id);
            ret = 0 < jdbcTemplate.update(sql);
            if (!ret) {
                String error = String.format("无法创建ID为%d的公司信息", id);
                System.err.println(error);
                Assert.fail(error);
            }

            savedCompany = companyBiz.findCompanyById(id);
        }

        Assert.assertNotNull(savedCompany);
        Assert.assertNotNull(savedCompany.getId());
        companiesList.add(savedCompany);
    }

    public void cleanUpAllData(Long roleId) {
        jdbcTemplate.execute("DELETE FROM SYS_R_ROLE_PERMISSION WHERE rid = " + roleId);
        jdbcTemplate.execute("DELETE FROM SYS_ROLE WHERE id = " + roleId);
        jdbcTemplate.execute("DELETE FROM SYS_R_COMPANY_PERMISSION WHERE cid = 10000 OR cid = 10001");

        modulesList.forEach(module -> {
            Long id = module.getId();
            jdbcTemplate.execute("DELETE FROM SYS_PERMISSION WHERE module_id = " + id);
            jdbcTemplate.execute("DELETE FROM SYS_MODULE WHERE id = " + id);
        });
    }

//    private void setLocalTokenWithCompanyId(Long uid, Long companyId) {
//        ThreadLocalToken.setToken(new Token("1", "1", null == uid ? null : uid.toString(), "1", null == companyId ? null : companyId.toString(), "1", Token.ClientType.BROWSER) {
//            @Override
//            public String toCipherString() {
//                return null;
//            }
//        });
//    }
}
