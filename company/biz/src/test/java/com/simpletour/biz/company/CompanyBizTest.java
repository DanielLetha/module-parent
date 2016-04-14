package com.simpletour.biz.company;

import com.simpletour.biz.company.error.CompanyBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.dao.company.query.CompanyDaoQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Employee;
import com.simpletour.domain.company.Permission;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司Biz层的测试
 * User: Hawk
 * Date: 2016/4/9 - 11:36
 * AbstractTransactionalTestNGSpringContextTests
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class CompanyBizTest extends AbstractTransactionalTestNGSpringContextTests {

    @Resource
    private ICompanyDao companyDao;

    @Resource
    private ICompanyBiz companyBiz;

    /**
     * 测试正常的增加公司
     */
    @Test
    public void testAddCompany() {
        String tempName = generateStr("companyName");
        String tempAddress = generateStr("companyAddress");
        String tempRemark = generateStr("companyRemark");
        Company company = new Company(tempName, tempAddress, tempRemark);
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertNotNull(dbCompany);
        Assert.assertEquals(tempName, dbCompany.getName());
        Assert.assertEquals(tempAddress, dbCompany.getAddress());
        Assert.assertEquals(tempRemark, dbCompany.getRemark());
    }

    /**
     * 测试增加公司时，两次增加的公司ID差为10000
     */
    @Test
    public void testAddCompanyAndIdSeq() {
        Company company1 = generateNewCompany();
        Company dbCompany1 = companyBiz.addCompany(company1);
        Assert.assertNotNull(dbCompany1);

        Company company2 = generateNewCompany();
        Company dbCompany2 = companyBiz.addCompany(company2);
        Assert.assertNotNull(dbCompany2);

        Assert.assertEquals(10000L, dbCompany2.getId() - dbCompany1.getId());
    }

    /**
     * 测试增加公司, ID为null
     */
    @Test
    public void testAddCompanyAndIdNull() {
        try {
            companyBiz.addCompany(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_NULL, e.getError());
        }
    }

    /**
     * 测试增加公司, del为true
     */
    @Test
    public void testAddCompanyAndDeleted() {
        try {
            Company company = generateNewCompany();
            company.setDel(Boolean.TRUE);
            companyBiz.addCompany(company);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_DEL, e.getError());
        }
    }

    /**
     * 测试增加公司, 名字重复
     */
    @Test
    public void testAddCompanyAndNameRepeat() {
        String tempName = generateStr("company-name");
        Company company1 = generateNewCompany();
        company1.setName(tempName);
        Company dbCompany1 = companyBiz.addCompany(company1);
        Assert.assertNotNull(dbCompany1);

        Company company2 = generateNewCompany();
        company2.setName(tempName);
        try {
            companyBiz.addCompany(company2);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_NAME_REPEAT, e.getError());
        }
    }

    /**
     * 测试正常的删除公司
     */
    @Test
    public void testDeleteCompany() {
        Company company = generateNewCompany();
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertNotNull(dbCompany);

        companyBiz.deleteCompany(dbCompany.getId());
        Company deletedCompany = companyDao.getEntityById(Company.class, dbCompany.getId());
        Assert.assertTrue(deletedCompany.getDel());
    }

    /**
     * 测试删除公司, id为null
     */
    @Test
    public void testDeleteCompanyAndIdNull() {
        try {
            companyBiz.deleteCompany(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_ID_ERROR, e.getError());
        }
    }

    /**
     * 测试删除公司, id错误
     */
    @Test
    public void testDeleteCompanyAndIdError() {
        try {
            companyBiz.deleteCompany(-1L);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_ID_ERROR, e.getError());
        }
    }

    /**
     * 测试删除公司, 存在员工依赖的情况
     */
    @Test
    public void testDeleteCompanyAndEmployeeDepended() {
        Company company = generateNewCompany();
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertNotNull(dbCompany);

        Employee employee = generateNewEmployee();
        Employee dbEmployee = companyDao.save(employee);
        Assert.assertNotNull(dbEmployee);

        dbEmployee.setCompany(dbCompany);
        Employee newDbEmployee = companyDao.save(dbEmployee);
        Assert.assertNotNull(newDbEmployee);

        try {
            companyBiz.deleteCompany(dbCompany.getId());
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.EMPLOYEE_DEPEND_ON_COMPANY, e.getError());
        }
    }

    /**
     * 测试正常的更新公司
     */
    @Test
    public void testUpdateCompany() {
        String tempName = generateStr("companyName");
        String tempAddress = generateStr("companyAddress");
        String tempRemark = generateStr("companyRemark");
        Company company = new Company(tempName, tempAddress, tempRemark);
        Company dbCompany = companyBiz.addCompany(company);

        Company newCompany = new Company(tempName + "1", tempAddress + "2", tempRemark + "3");
        newCompany.setId(dbCompany.getId());
        newCompany.setVersion(dbCompany.getVersion());
        Company newDbCompany = companyBiz.updateCompany(newCompany);
        Assert.assertEquals(tempName + "1", newDbCompany.getName());
        Assert.assertEquals(tempAddress + "2", newDbCompany.getAddress());
        Assert.assertEquals(tempRemark + "3", newDbCompany.getRemark());
    }

    /**
     * 测试正常的更新公司, id为null
     */
    @Test
    public void testUpdateCompanyAndEntityNull() {
        try {
            companyBiz.updateCompany(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_NULL, e.getError());
        }
    }

    /**
     * 测试正常的更新公司, id为null
     */
    @Test
    public void testUpdateCompanyAndIdNull() {
        try {
            Company company = generateNewCompany();
            companyBiz.updateCompany(company);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_ID_ERROR, e.getError());
        }
    }

    /**
     * 测试正常的更新公司, version为null
     */
    @Test
    public void testUpdateCompanyAndVersionNull() {

        Company company = generateNewCompany();
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertNotNull(dbCompany);

        try {
            company.setId(dbCompany.getId());
            companyBiz.updateCompany(company);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_UPDATE_VERSION_NULL, e.getError());
        }
    }

    /**
     * 测试正常的更新公司, 重名情况
     */
    @Test
    public void testUpdateCompanyAndNameRepeat() {

        String companyName = generateStr("companyName");

        Company company1 = generateNewCompany();
        company1.setName(companyName);
        Company dbCompany1 = companyBiz.addCompany(company1);
        Assert.assertNotNull(dbCompany1);

        Company company2 = generateNewCompany();
        Company dbCompany2 = companyBiz.addCompany(company2);
        Assert.assertNotNull(dbCompany2);

        try {
            dbCompany2.setName(companyName);
            companyBiz.updateCompany(dbCompany2);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_NAME_REPEAT, e.getError());
        }
    }

    /**
     * 测试正常的更新公司, 什么都不改，直接更新对象
     */
    @Test
    public void testUpdateCompanyAndNameRepeatAndObject() {

        Company company1 = generateNewCompany();
        Company dbCompany1 = companyBiz.addCompany(company1);
        Assert.assertNotNull(dbCompany1);
        Assert.assertNotNull(companyBiz.updateCompany(dbCompany1));
    }


    /**
     * 测试正常的findCompanyById
     */
    @Test
    public void testFindCompanyById() {
        Company company = generateNewCompany();
        Company dbCompany = companyBiz.addCompany(company);
        Company queryCompany = companyBiz.findCompanyById(dbCompany.getId());
        Assert.assertEquals(company.getName(), queryCompany.getName());
        Assert.assertEquals(company.getAddress(), queryCompany.getAddress());
        Assert.assertEquals(company.getRemark(), queryCompany.getRemark());
    }

    /**
     * 测试正常的findCompanyById, id为null
     */
    @Test
    public void testFindCompanyByIdAndIdNull() {
        try {
            companyBiz.findCompanyById(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_ID_ERROR, e.getError());
        }
    }

    /**
     * 测试正常的findCompanyById, id错误
     */
    @Test
    public void testFindCompanyByIdAndIdError() {
        try {
            companyBiz.findCompanyById(-1L);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_ID_ERROR, e.getError());
        }
    }

    /**
     * 测试正常的findCompanyPageByQuery
     */
    @Test
    public void testFindCompanyPageByQuery() {
        String tempName = generateStr("companyName");
        String tempAddress = generateStr("companyAddress");
        String tempRemark = generateStr("companyRemark");
        Company company = new Company(tempName, tempAddress, tempRemark);
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertNotNull(dbCompany);

        CompanyDaoQuery query = new CompanyDaoQuery();
        AndConditionSet condition = new AndConditionSet();
        condition.addCondition("name", tempName, Condition.MatchType.like);
        condition.addCondition("address", tempAddress, Condition.MatchType.like);
        condition.addCondition("remark", tempRemark, Condition.MatchType.like);
        query.setCondition(condition);

        List<Company> companyList = companyBiz.findCompanyPageByQuery(query);
        Assert.assertNotNull(companyList);
        Assert.assertEquals(1, companyList.size());
        Assert.assertEquals(tempName, companyList.get(0).getName());
        Assert.assertEquals(tempAddress, companyList.get(0).getAddress());
        Assert.assertEquals(tempRemark, companyList.get(0).getRemark());
    }

    /**
     * 测试findCompanyPageByQuery, query为null
     */
    @Test
    public void testFindCompanyPageByQueryAndQueryNull() {
        try {
            companyBiz.findCompanyPageByQuery(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_QUERY_CONDITION_NULL, e.getError());
        }
    }

    /**
     * 测试正常的findCompanyPageByConditions
     */
    @Test
    public void testFindCompanyPageByConditions() {
        String tempName = generateStr("companyName");
        String tempAddress = generateStr("companyAddress");
        String tempRemark = generateStr("companyRemark");
        Company company = new Company(tempName, tempAddress, tempRemark);
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertNotNull(dbCompany);

        Map<String, Object> params = new HashMap<>();
        params.put("name", tempName);
        params.put("address", tempAddress);
        params.put("remark", tempRemark);
        List<Company> companyList = companyBiz.findCompanyPageByConditions(params);
        Assert.assertNotNull(companyList);
        Assert.assertEquals(1, companyList.size());
        Assert.assertEquals(tempName, companyList.get(0).getName());
        Assert.assertEquals(tempAddress, companyList.get(0).getAddress());
        Assert.assertEquals(tempRemark, companyList.get(0).getRemark());
    }

    /**
     * 测试findCompanyPageByConditions, conditions为null
     */
    @Test
    public void testFindCompanyPageByConditionsAndConditionsNull() {
        try {
            companyBiz.findCompanyPageByConditions(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_QUERY_CONDITION_NULL, e.getError());
        }
    }

    /**
     * 测试正常的queryCompanyPagesByQuery
     */
    @Test
    public void testQueryCompanyPagesByQuery() {
        String tempName = generateStr("companyName");
        String tempAddress = generateStr("companyAddress");
        String tempRemark = generateStr("companyRemark");
        Company company = new Company(tempName, tempAddress, tempRemark);
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertNotNull(dbCompany);

        CompanyDaoQuery query = new CompanyDaoQuery();
        AndConditionSet condition = new AndConditionSet();
        condition.addCondition("name", tempName, Condition.MatchType.like);
        condition.addCondition("address", tempAddress, Condition.MatchType.like);
        condition.addCondition("remark", tempRemark, Condition.MatchType.like);
        query.setCondition(condition);

        DomainPage<Company> page = companyBiz.queryCompanyPagesByQuery(query);
        Assert.assertNotNull(page);
        Assert.assertEquals(1, page.getDomains().size());
        Assert.assertEquals(tempName, page.getDomains().get(0).getName());
        Assert.assertEquals(tempAddress, page.getDomains().get(0).getAddress());
        Assert.assertEquals(tempRemark, page.getDomains().get(0).getRemark());
    }

    /**
     * 测试queryCompanyPagesByQuery, query为null
     */
    @Test
    public void testQueryCompanyPagesByQueryAndQueryNull() {
        try {
            companyBiz.queryCompanyPagesByQuery(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_QUERY_CONDITION_NULL, e.getError());
        }
    }

    /**
     * 测试正常的queryCompanyPagesByConditions
     */
    @Test
    public void testQueryCompanyPagesByConditions() {
        String tempName = generateStr("companyName");
        String tempAddress = generateStr("companyAddress");
        String tempRemark = generateStr("companyRemark");
        Company company = new Company(tempName, tempAddress, tempRemark);
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertNotNull(dbCompany);

        Map<String, Object> params = new HashMap<>();
        params.put("name", tempName);
        params.put("address", tempAddress);
        params.put("remark", tempRemark);
        DomainPage<Company> page = companyBiz.queryCompanyPagesByConditions(params, "id"
                , IBaseDao.SortBy.DESC, 1, 10, false);
        Assert.assertNotNull(page);
        Assert.assertEquals(1, page.getDomains().size());
        Assert.assertEquals(tempName, page.getDomains().get(0).getName());
        Assert.assertEquals(tempAddress, page.getDomains().get(0).getAddress());
        Assert.assertEquals(tempRemark, page.getDomains().get(0).getRemark());
    }

    /**
     * 测试queryCompanyPagesByConditions, Conditiosn为null
     */
    @Test
    public void testQueryCompanyPagesByConditionsAndConditionsNull() {
        try {
            companyBiz.queryCompanyPagesByConditions(null, "id", IBaseDao.SortBy.DESC, 1, 10, false);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_QUERY_CONDITION_NULL, e.getError());
        }
    }

    /**
     * 测试正常的isExist
     */
    @Test
    public void testIsExist() {
        String tempName = generateStr("companyName");
        String tempAddress = generateStr("companyAddress");
        String tempRemark = generateStr("companyRemark");
        Company company = new Company(tempName, tempAddress, tempRemark);
        Company dbCompany = companyBiz.addCompany(company);
        Assert.assertTrue(companyBiz.isExist(dbCompany.getId()));
    }

    /**
     * 测试isExist, id为null
     */
    @Test
    public void testIsExistAndIDNull() {
        try {
            companyBiz.isExist(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_ID_ERROR, e.getError());
        }
    }

    /**
     * 测试isExist, id所对应的实体不存在
     */
    @Test
    public void testIsExistAndIDError() {
        try {
            companyBiz.isExist(-1L);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(CompanyBizError.COMPANY_ID_ERROR, e.getError());
        }
    }

    private Company generateNewCompany() {
        Company company = new Company(generateStr("companyName")
                , generateStr("companyAddress"), generateStr("companyRemark"));
        return company;
    }

    private Permission generateNewPermession() {
        Permission permission = new Permission(generateStr("permission-name")
                , generateStr("www.baidu.com"), generateStr("permission-scope"), null);
        return permission;
    }

    private Employee generateNewEmployee() {
        Employee employee = new Employee();
        employee.setName(generateStr("employee-name"));
        employee.setMobile(generateStr("employee-mobile"));
        employee.setSalt(generateStr("123"));
        employee.setPasswd(generateStr("password"));
        return employee;
    }

    private String generateStr(String str) {
        return System.currentTimeMillis() + str;
    }
}
