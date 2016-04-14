package com.simpletour.biz.company;

import com.simpletour.biz.company.data.EmployeeData;
import com.simpletour.biz.company.data.Employee_Company_data;
import com.simpletour.biz.company.data.Employee_Role_data;
import com.simpletour.biz.company.error.EmployeeBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.dao.company.IEmployeeDao;
import com.simpletour.dao.company.IRoleDao;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Employee;
import com.simpletour.domain.company.Role;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ListIterator;
import java.util.Optional;

/**
 * Created by Mario on 2016/4/9.
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class EmployeeBizTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger logger = Logger.getLogger(EmployeeBizTest.class);

    @Autowired
    private IEmployeeBiz employeeBiz;

    @Autowired
    private ICompanyDao companyDao;

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private IEmployeeDao employeeDao;

    private EmployeeData employeeData;

    private static String generateName(String name) {
        return System.currentTimeMillis() + name;
    }

    @BeforeClass
    public void setUp() {
        logger.error("setup.....................");
        Employee_Company_data employee_company_data = new Employee_Company_data(companyDao);
        Employee_Role_data employee_role_data = new Employee_Role_data(roleDao);
        employeeData = new EmployeeData(employeeDao);
        employeeData.addDataGenerators(employee_company_data);
        employeeData.addDataGenerators(employee_role_data);
        employeeData.createData();
    }

    @AfterClass
    public void tearDown() {
        logger.error("teardown..................");
        ListIterator<BaseDomain> iterator = employeeData.getDomains().listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        while (iterator.hasPrevious()) {
            employeeDao.removeEntity(iterator.previous());
        }
    }

    private Employee generateEmployee() {
        Employee employee = new Employee(generateName("remark1"), 10000, false, generateName("mary"), generateName("avatar"), "mobile", generateName("pwd"), generateName("salt"));
        employee.setCompany((Company) employeeData.getDomains(Company.class).get(0));
        employee.setRole((Role) employeeData.getDomains(Role.class).get(0));
        return employee;
    }

    private Employee generateEmployeeForMobile() {
        Employee employee = new Employee(generateName("remark2"), 10020, false, generateName("mary"), generateName("avatar"), "mobile", generateName("pwd"), generateName("salt"));
        employee.setCompany((Company) employeeData.getDomains(Company.class).get(0));
        employee.setRole((Role) employeeData.getDomains(Role.class).get(0));
        return employee;
    }

    private Employee generateEmployeeForNew() {
        Employee employee = new Employee(generateName("remark3"), 10030, false, generateName("mary"), generateName("avatar"), "mobile1", generateName("pwd"), generateName("salt"));
        employee.setCompany((Company) employeeData.getDomains(Company.class).get(0));
        employee.setRole((Role) employeeData.getDomains(Role.class).get(0));
        return employee;
    }

    private Employee generateEmployeeForJobNoValidate() {
        Employee employee = new Employee(generateName("remark4"), 10030, false, generateName("larry"), generateName("avatar"), "mobile3", generateName("pwd"), generateName("salt"));
        employee.setCompany((Company) employeeData.getDomains(Company.class).get(1));
        employee.setRole((Role) employeeData.getDomains(Role.class).get(0));
        return employee;
    }

    /**
     * 添加:正常添加
     */
    @Test
    public void addEmployee() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        Assert.assertNotNull(employeeOptional.get().getCompany());
        Assert.assertNotNull(employeeOptional.get().getRole());
    }

    /**
     * 添加:关联电话号码已经存在的情况
     */
    @Test
    public void addEmployeeMobileExist() {
        try {
            employeeBiz.addEmployee(generateEmployee());
            employeeBiz.addEmployee(generateEmployeeForMobile());
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.MOBILE_EXISTED, e.getError());
        }
    }

    /**
     * 添加:添加employee实体对象为空
     */
    @Test
    public void addNullEmployee() {
        try {
            employeeBiz.addEmployee(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_NULL, e.getError());
        }
    }

    /**
     * 添加:添加employee实体对象中的company实体为空
     */
    @Test
    public void addEmployeeWithoutCompany() {
        Employee employee = generateEmployee();
        employee.setCompany(null);
        try {
            employeeBiz.addEmployee(employee);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_SET_JOB_NO_BASIC_INFO_WRONG, e.getError());
        }
    }

    /**
     * 添加:添加employee,初始化公司admin
     */
    @Test
    public void addEmployForAdminEmployee() {
        Employee employee = generateEmployeeForJobNoValidate();
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(employee);
        Assert.assertTrue(employeeOptional.isPresent());
    }

    /**
     * 添加:添加异常,公司id转化出错
     */
    @Test
    public void addEmployeeForAdminIdFormatException() {
        Employee employee = generateEmployeeForJobNoValidate();
        Company newCompany = new Company(Long.MAX_VALUE, employee.getCompany().getName(), employee.getCompany().getAddress(),
                employee.getCompany().getContacts(), employee.getCompany().getMobile(), employee.getCompany().getFax(), employee.getCompany().getEmail(),
                employee.getCompany().getLink(), employee.getCompany().getRemark(), employee.getCompany().getEmployees(), employee.getCompany().getRoles(),
                employee.getCompany().getPermissions(), employee.getCompany().getVersion());
        employee.setCompany(newCompany);
        try {
            employeeBiz.addEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.SYSTEM_ERROR, e.getError());
        }
    }

    /**
     * 删除:正常删除情况
     */
    @Test
    public void deleteEmployeeById() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        employeeBiz.deleteEmployee(employeeOptional.get().getId());
        Employee employee = employeeDao.getEntityById(Employee.class, employeeOptional.get().getId());
        Assert.assertTrue(employee.getDel());
    }

    /**
     * 删除:id为null
     */
    @Test
    public void deleteEmployeeWithoutId() {
        try {
            employeeBiz.deleteEmployee(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ID_NULL, e.getError());
        }
    }

    /**
     * 删除:id<=0的情况
     */
    @Test
    public void deleteEmployeeWithIdLessZero() {
        try {
            employeeBiz.deleteEmployee(Long.MIN_VALUE);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ID_NULL, e.getError());
        }
    }

    /**
     * 删除:原有employee不存在的情况
     */
    @Test
    public void deleteNotExistedEmployee() {
        try {
            employeeBiz.deleteEmployee(Long.MAX_VALUE);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED, e.getError());
        }
    }

    /**
     * 更新:正常更新employee
     */
    @Test
    public void updateEmployee() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        employeeOptional.get().setName("jack");
        employeeOptional.get().setAvater("avatar1412");

        Optional<Employee> employeeOptional1 = employeeBiz.updateEmployee(employeeOptional.get());
        Assert.assertTrue(employeeOptional1.isPresent());
    }

    /**
     * 更新:测试基本条件不满足
     */
    @Test
    public void updateEmployeeForBasic() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        Employee employeeNew = new Employee(employeeOptional.get().getId(), employeeOptional.get().getAdmin(), employeeOptional.get().getJobNo(), "", employeeOptional.get().getAvater(),
                employeeOptional.get().getMobile(), employeeOptional.get().getPasswd(), employeeOptional.get().getSalt(), employeeOptional.get().getCompany(), employeeOptional.get().getRole(),
                employeeOptional.get().getRemark(), employeeOptional.get().getVersion());

        try {
            employeeBiz.updateEmployee(employeeNew);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_BASIC_INFO_NULL, e.getError());
        }
    }

    /**
     * 更新:更新时employee没有id
     */
    @Test
    public void updateEmployeeWithoutIds() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        Employee employeeNew = new Employee(null, employeeOptional.get().getAdmin(), employeeOptional.get().getJobNo(), employeeOptional.get().getName(), employeeOptional.get().getAvater(),
                employeeOptional.get().getMobile(), employeeOptional.get().getPasswd(), employeeOptional.get().getSalt(), employeeOptional.get().getCompany(), employeeOptional.get().getRole(),
                employeeOptional.get().getRemark(), employeeOptional.get().getVersion());
        employeeOptional.get().setId(null);

        try {
            employeeBiz.updateEmployee(employeeNew);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_NULL_OR_ID_NULL, e.getError());
        }
    }

    /**
     * 更新:原有employee不存在或已被删除
     */
    @Test
    public void updateEmployeeNotExisted() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        Employee employeeNew = new Employee(Long.MAX_VALUE, employeeOptional.get().getAdmin(), employeeOptional.get().getJobNo(), employeeOptional.get().getName(), employeeOptional.get().getAvater(),
                employeeOptional.get().getMobile(), employeeOptional.get().getPasswd(), employeeOptional.get().getSalt(), employeeOptional.get().getCompany(), employeeOptional.get().getRole(),
                employeeOptional.get().getRemark(), employeeOptional.get().getVersion());
        try {
            employeeBiz.updateEmployee(employeeNew);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED, e.getError());
        }
    }

    /**
     * 更新:JobNo和原有jobNo不相同
     */
    @Test
    public void updateEmployeeJobNoNotSame() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        Employee employeeNew = new Employee(employeeOptional.get().getId(), employeeOptional.get().getAdmin(), employeeOptional.get().getJobNo() + 1, employeeOptional.get().getName(), employeeOptional.get().getAvater(),
                employeeOptional.get().getMobile(), employeeOptional.get().getPasswd(), employeeOptional.get().getSalt(), employeeOptional.get().getCompany(), employeeOptional.get().getRole(),
                employeeOptional.get().getRemark(), employeeOptional.get().getVersion());

        try {
            employeeBiz.updateEmployee(employeeNew);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.JOB_NO_NOT_SAME, e.getError());
        }
    }

    /**
     * 更新:Mobile已经存在
     */
    @Test
    public void updateEmployeeMobileExisted() {
        Optional<Employee> employeeOptional1 = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional1.isPresent());

        Optional<Employee> employeeOptional2 = employeeBiz.addEmployee(generateEmployeeForNew());
        Assert.assertTrue(employeeOptional2.isPresent());

        Employee employeeNew = new Employee(employeeOptional2.get().getId(), employeeOptional2.get().getAdmin(), employeeOptional2.get().getJobNo(), employeeOptional2.get().getName(), employeeOptional2.get().getAvater(),
                employeeOptional1.get().getMobile(), employeeOptional2.get().getPasswd(), employeeOptional2.get().getSalt(), employeeOptional2.get().getCompany(), employeeOptional2.get().getRole(),
                employeeOptional2.get().getRemark(), employeeOptional2.get().getVersion());
        try {
            employeeBiz.updateEmployee(employeeNew);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.MOBILE_EXISTED, e.getError());
        }
    }

    /**
     * 修改密码:正常修改
     */
    @Test
    public void changePwd() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        Optional<Employee> employeeOptional1 = employeeBiz.changePwdForEmployee(employeeOptional.get().getId(), employeeOptional.get().getPasswd() + "ROCK", employeeOptional.get().getSalt() + "YOU");
        Assert.assertTrue(employeeOptional1.isPresent());
    }

    /**
     * 修改密码:基本信息不完整
     */
    @Test
    public void changePwdForBasic() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        try {
            employeeBiz.changePwdForEmployee(employeeOptional.get().getId(), "", null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ID_PWD_SALT, e.getError());
        }
    }

    /**
     * 修改密码:原有账号信息不存在
     */
    @Test
    public void changePwdOriginNotExisted() {
        Optional<Employee> employeeOptional = employeeBiz.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        try {
            employeeBiz.changePwdForEmployee(Long.MAX_VALUE, employeeOptional.get().getPasswd(), employeeOptional.get().getSalt());
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED, e.getError());
        }
    }

    /**
     * id查询:正常查询
     */
    @Test
    public void queryEmployeeById() {
        Optional<Employee> employeeOptionl = employeeBiz.queryEmployeeById(employeeData.getDomains(Employee.class).get(0).getId());
        Assert.assertTrue(employeeOptionl.isPresent());
    }

    /**
     * id查询:缺少id
     */
    @Test
    public void queryEmployeeWithoutId() {
        try {
            employeeBiz.queryEmployeeById(null);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ID_NULL, e.getError());
        }
    }

    /**
     * jobNo查询:正常查询
     */
    @Test
    public void queryEmployeeByJobNo() {
        Optional<Employee> employeeOptinal = employeeBiz.queryEmployeeByJobNo(((Employee) employeeData.getDomains(Employee.class).get(0)).getJobNo());
        Assert.assertTrue(employeeOptinal.isPresent());
    }

    /**
     * jobNo查询:jobNo为空
     */
    @Test
    public void queryEmployeeByJobNoNull() {
        try {
            employeeBiz.queryEmployeeByJobNo(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.JOBNO_NULL, e.getError());
        }
    }

    /**
     * jobNo查询:jobNo小于0
     */
    @Test
    public void queryEmployeeByJobNoLessZero() {
        try {
            employeeBiz.queryEmployeeByJobNo(Integer.MIN_VALUE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.JOBNO_NULL, e.getError());
        }
    }

    /**
     * id查询:原有数据已被删除或不存在
     */
    @Test
    public void queryEmployeeOriginNotExisted() {
        employeeDao.removeEntityById(Employee.class, employeeData.getDomains(Employee.class).get(0).getId(), true);
        Assert.assertTrue(!employeeBiz.queryEmployeeById(employeeData.getDomains(Employee.class).get(0).getId()).isPresent());
    }

    /**
     * 条件查询:正常查询
     */
    @Test
    public void queryEmployeeByCondition() {
        AndConditionSet andConditionSet = new AndConditionSet();
        andConditionSet.addCondition("c.name", "mario", Condition.MatchType.like);
        ConditionOrderByQuery conditionQuery = new ConditionOrderByQuery();
        conditionQuery.setCondition(andConditionSet);
        conditionQuery.setPageIndex(1);
        conditionQuery.setPageSize(10);
        conditionQuery.addSortByField("c.id", IBaseDao.SortBy.ASC);
        DomainPage<Employee> employeeDomainPage = employeeBiz.queryEmployeesPagesByConditions(conditionQuery);
        Assert.assertTrue(employeeDomainPage.getDomains() != null);
        Assert.assertTrue(employeeDomainPage.getDomains().size() >= 1);
    }
}
