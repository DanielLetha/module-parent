package com.simpletour.service.company.imp;

import com.simpletour.biz.company.error.EmployeeBizError;
import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.dao.query.ConditionOrderByQuery;
import com.simpletour.common.core.dao.query.condition.AndConditionSet;
import com.simpletour.common.core.dao.query.condition.Condition;
import com.simpletour.common.core.domain.BaseDomain;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.common.security.token.ThreadLocalToken;
import com.simpletour.common.security.token.Token;
import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.dao.company.IEmployeeDao;
import com.simpletour.dao.company.IRoleDao;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Employee;
import com.simpletour.domain.company.Role;
import com.simpletour.service.company.IEmployeeService;
import com.simpletour.service.company.data.EmployeeData;
import com.simpletour.service.company.data.Employee_Company_data;
import com.simpletour.service.company.data.Employee_Role_data;
import com.simpletour.service.company.error.EmployeeServiceError;
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
public class EmployeeServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger logger = Logger.getLogger(EmployeeServiceTest.class);

    @Autowired
    private IEmployeeDao employeeDao;

    @Autowired
    private ICompanyDao companyDao;

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private IEmployeeService employeeService;

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
        ThreadLocalToken.setToken(new Token("1", "1", "1", "1", ((Company) employeeData.getDomains().get(0)).getId().toString(), "1", Token.ClientType.BROWSER) {
            @Override
            public String toCipherString() {
                return null;
            }
        });
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

    /**
     * 添加:正常添加
     */
    @Test
    public void addEmployee() {
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
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
            employeeService.addEmployee(generateEmployee());
            employeeService.addEmployee(generateEmployeeForMobile());
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
            employeeService.addEmployee(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.EMPTY_ENTITY, e.getError());
        }
    }

    /**
     * 添加:关联公司实体不存在
     */
    @Test
    public void addEmployeeWithoutCompany() {
        Employee employee = generateEmployee();
        employee.setCompany(null);
        try {
            employeeService.addEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.COMPANY_NULL, e.getError());
        }
    }

    /**
     * 添加:关联公司对象id不存在
     */
    @Test
    public void addEmployeeWithoutCompanyId() {
        Employee employee = generateEmployee();
        Company company = new Company(null, employee.getCompany().getName(), employee.getCompany().getAddress(), employee.getCompany().getContacts(), employee.getCompany().getMobile(),
                employee.getCompany().getFax(), employee.getCompany().getEmail(), employee.getCompany().getLink(), employee.getCompany().getRemark(), employee.getCompany().getEmployees(),
                employee.getCompany().getRoles(), employee.getCompany().getPermissions(), employee.getCompany().getVersion());
        employee.setCompany(company);
        try {
            employeeService.addEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.COMPANY_NULL, e.getError());
        }
    }

    /**
     * 添加:原有公司实体对象不存在
     */
    @Test
    public void addEmployeeCompanyNotExisted() {
        Employee employee = generateEmployee();
        Company company = new Company(Long.MAX_VALUE, employee.getCompany().getName(), employee.getCompany().getAddress(), employee.getCompany().getContacts(), employee.getCompany().getMobile(),
                employee.getCompany().getFax(), employee.getCompany().getEmail(), employee.getCompany().getLink(), employee.getCompany().getRemark(), employee.getCompany().getEmployees(),
                employee.getCompany().getRoles(), employee.getCompany().getPermissions(), employee.getCompany().getVersion());
        employee.setCompany(company);
        try {
            employeeService.addEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.COMPANY_HAS_BEEN_DELETE, e.getError());
        }
    }

    /**
     * 添加:关联角色实体不存在
     */
    @Test
    public void addEmployeeWithoutRole() {
        Employee employee = generateEmployee();
        employee.setRole(null);
        try {
            employeeService.addEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.ROLE_NULL, e.getError());
        }
    }

    /**
     * 添加:关联角色对象id不存在
     */
    @Test
    public void addEmployeeWithoutRoleId() {
        Employee employee = generateEmployee();
        Role role = new Role(null, employee.getRole().getName(), employee.getRole().getType(), employee.getRole().getCompany(), employee.getRole().getRemark(), employee.getRole().getPermissionList(), employee.getRole().getVersion());
        employee.setRole(role);
        try {
            employeeService.addEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.ROLE_NULL, e.getError());
        }
    }

    /**
     * 添加:原有角色实体对象不存在
     */
    @Test
    public void addEmployeeRoleNotExisted() {
        Employee employee = generateEmployee();
        Role role = new Role(Long.MAX_VALUE, employee.getRole().getName(), employee.getRole().getType(), employee.getRole().getCompany(), employee.getRole().getRemark(), employee.getRole().getPermissionList(), employee.getRole().getVersion());
        employee.setRole(role);
        try {
            employeeService.addEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.ROLE_HAS_BEEN_DELETE, e.getError());
        }
    }

    /**
     * 删除:正常删除情况
     */
    @Test
    public void deleteEmployeeById() {
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        employeeService.deleteEmployee(employeeOptional.get().getId());
        Employee employee = employeeDao.getEntityById(Employee.class, employeeOptional.get().getId());
        Assert.assertTrue(employee.getDel());
    }

    /**
     * 删除:id为null
     */
    @Test
    public void deleteEmployeeWithoutId() {
        try {
            employeeService.deleteEmployee(null);
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
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        try {
            employeeService.deleteEmployee(Long.MIN_VALUE);
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
            employeeService.deleteEmployee(Long.MAX_VALUE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED, e.getError());
        }
    }

    /**
     * 更新:正常更新employee
     */
    @Test
    public void updateEmployee() {
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        employeeOptional.get().setName("jack");
        employeeOptional.get().setAvater("avatar1412");

        Optional<Employee> employeeOptional1 = employeeService.updateEmployee(employeeOptional.get());
        Assert.assertTrue(employeeOptional1.isPresent());
    }


    /**
     * 更新:关联公司实体不存在
     */
    @Test
    public void updateEmployeeWithoutCompany() {
        Employee employee = generateEmployee();
        employee.setCompany(null);
        try {
            employeeService.updateEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.COMPANY_NULL, e.getError());
        }
    }

    /**
     * 更新:关联公司对象id不存在
     */
    @Test
    public void updateEmployeeWithoutCompanyId() {
        Employee employee = generateEmployee();
        Company company = new Company(null, employee.getCompany().getName(), employee.getCompany().getAddress(), employee.getCompany().getContacts(), employee.getCompany().getMobile(),
                employee.getCompany().getFax(), employee.getCompany().getEmail(), employee.getCompany().getLink(), employee.getCompany().getRemark(), employee.getCompany().getEmployees(),
                employee.getCompany().getRoles(), employee.getCompany().getPermissions(), employee.getCompany().getVersion());
        employee.setCompany(company);
        try {
            employeeService.updateEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.COMPANY_NULL, e.getError());
        }
    }

    /**
     * 更新:原有公司实体对象不存在
     */
    @Test
    public void updateEmployeeCompanyNotExisted() {
        Employee employee = generateEmployee();
        Company company = new Company(Long.MAX_VALUE, employee.getCompany().getName(), employee.getCompany().getAddress(), employee.getCompany().getContacts(), employee.getCompany().getMobile(),
                employee.getCompany().getFax(), employee.getCompany().getEmail(), employee.getCompany().getLink(), employee.getCompany().getRemark(), employee.getCompany().getEmployees(),
                employee.getCompany().getRoles(), employee.getCompany().getPermissions(), employee.getCompany().getVersion());
        employee.setCompany(company);
        try {
            employeeService.updateEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.COMPANY_HAS_BEEN_DELETE, e.getError());
        }
    }

    /**
     * 更新:关联角色实体不存在
     */
    @Test
    public void updateEmployeeWithoutRole() {
        Employee employee = generateEmployee();
        employee.setRole(null);
        try {
            employeeService.updateEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.ROLE_NULL, e.getError());
        }
    }

    /**
     * 更新:关联角色对象id不存在
     */
    @Test
    public void updateEmployeeWithoutRoleId() {
        Employee employee = generateEmployee();
        Role role = new Role(null, employee.getRole().getName(), employee.getRole().getType(), employee.getRole().getCompany(), employee.getRole().getRemark(), employee.getRole().getPermissionList(), employee.getRole().getVersion());
        employee.setRole(role);
        try {
            employeeService.updateEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.ROLE_NULL, e.getError());
        }
    }

    /**
     * 更新:原有角色实体对象不存在
     */
    @Test
    public void updateEmployeeRoleNotExisted() {
        Employee employee = generateEmployee();
        Role role = new Role(Long.MAX_VALUE, employee.getRole().getName(), employee.getRole().getType(), employee.getRole().getCompany(), employee.getRole().getRemark(), employee.getRole().getPermissionList(), employee.getRole().getVersion());
        employee.setRole(role);
        try {
            employeeService.updateEmployee(employee);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.ROLE_HAS_BEEN_DELETE, e.getError());
        }
    }


    /**
     * 更新:测试基本条件不满足
     */
    @Test
    public void updateEmployeeForBasic() {
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        Employee employeeNew = new Employee(employeeOptional.get().getId(), employeeOptional.get().getAdmin(), employeeOptional.get().getJobNo(), "", employeeOptional.get().getAvater(),
                employeeOptional.get().getMobile(), employeeOptional.get().getPasswd(), employeeOptional.get().getSalt(), employeeOptional.get().getCompany(), employeeOptional.get().getRole(),
                employeeOptional.get().getRemark(), employeeOptional.get().getVersion());

        try {
            employeeService.updateEmployee(employeeNew);
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
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        Employee employeeNew = new Employee(null, employeeOptional.get().getAdmin(), employeeOptional.get().getJobNo(), employeeOptional.get().getName(), employeeOptional.get().getAvater(),
                employeeOptional.get().getMobile(), employeeOptional.get().getPasswd(), employeeOptional.get().getSalt(), employeeOptional.get().getCompany(), employeeOptional.get().getRole(),
                employeeOptional.get().getRemark(), employeeOptional.get().getVersion());

        try {
            employeeService.updateEmployee(employeeNew);
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
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        Employee employeeNew = new Employee(Long.MAX_VALUE, employeeOptional.get().getAdmin(), employeeOptional.get().getJobNo(), employeeOptional.get().getName(), employeeOptional.get().getAvater(),
                employeeOptional.get().getMobile(), employeeOptional.get().getPasswd(), employeeOptional.get().getSalt(), employeeOptional.get().getCompany(), employeeOptional.get().getRole(),
                employeeOptional.get().getRemark(), employeeOptional.get().getVersion());
        try {
            employeeService.updateEmployee(employeeNew);
            Assert.assertNotNull(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED, e.getError());
        }
    }

    /**
     * 更新:JobNo已经存在
     */
    @Test
    public void updateEmployeeJobNoNotSame() {
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());

        Employee employeeNew = new Employee(employeeOptional.get().getId(), employeeOptional.get().getAdmin(), employeeOptional.get().getJobNo() + 1, employeeOptional.get().getName(), employeeOptional.get().getAvater(),
                employeeOptional.get().getMobile(), employeeOptional.get().getPasswd(), employeeOptional.get().getSalt(), employeeOptional.get().getCompany(), employeeOptional.get().getRole(),
                employeeOptional.get().getRemark(), employeeOptional.get().getVersion());

        try {
            employeeService.updateEmployee(employeeNew);
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
        Optional<Employee> employeeOptional1 = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional1.isPresent());

        Optional<Employee> employeeOptional2 = employeeService.addEmployee(generateEmployeeForNew());
        Assert.assertTrue(employeeOptional2.isPresent());

        Employee employeeNew = new Employee(employeeOptional2.get().getId(), employeeOptional2.get().getAdmin(), employeeOptional2.get().getJobNo(), employeeOptional2.get().getName(), employeeOptional2.get().getAvater(),
                employeeOptional1.get().getMobile(), employeeOptional2.get().getPasswd(), employeeOptional2.get().getSalt(), employeeOptional2.get().getCompany(), employeeOptional2.get().getRole(),
                employeeOptional2.get().getRemark(), employeeOptional2.get().getVersion());
        try {
            employeeService.updateEmployee(employeeNew);
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
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        Optional<Employee> employeeOptional1 = employeeService.changePwdForEmployee(employeeOptional.get().getId(), employeeOptional.get().getPasswd() + "ROCK", employeeOptional.get().getSalt() + "YOU");
        Assert.assertTrue(employeeOptional1.isPresent());
    }

    /**
     * 修改密码:基本信息不完整
     */
    @Test
    public void changePwdForBasic() {
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        try {
            employeeService.changePwdForEmployee(employeeOptional.get().getId(), "", null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.EMPLOYEE_ID_PWD_SALT_NULL, e.getError());
        }
    }

    /**
     * 修改密码:原有账号信息不存在
     */
    @Test
    public void changePwdOriginNotExisted() {
        Optional<Employee> employeeOptional = employeeService.addEmployee(generateEmployee());
        Assert.assertTrue(employeeOptional.isPresent());
        try {
            employeeService.changePwdForEmployee(Long.MAX_VALUE, employeeOptional.get().getPasswd(), employeeOptional.get().getSalt());
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED, e.getError());
        }
    }

    /**
     * id查询:正常查询
     */
    @Test
    public void queryEmployeeById() {
        Optional<Employee> employeeOptionl = employeeService.queryEmployeeById(employeeData.getDomains(Employee.class).get(0).getId());
        Assert.assertTrue(employeeOptionl.isPresent());
    }

    /**
     * id查询:缺少id
     */
    @Test
    public void queryEmployeeWithoutId() {
        try {
            employeeService.queryEmployeeById(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.ID_NULL, e.getError());
        }
    }

    /**
     * jobNo查询:正常查询
     */
    @Test
    public void queryEmployeeByJobNo() {
        Optional<Employee> employeeOptinal = employeeService.queryEmployeeByJobNo(((Employee) employeeData.getDomains(Employee.class).get(0)).getJobNo());
        Assert.assertTrue(employeeOptinal.isPresent());
    }

    /**
     * jobNo查询:jobNo为空
     */
    @Test
    public void queryEmployeeByJobNoNull(){
        try {
            employeeService.queryEmployeeByJobNo(null);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.JOBNO_NULL, e.getError());
        }
    }

    /**
     * jobNo查询:jobNo小于0
     */
    @Test
    public void queryEmployeeByJobNoLessZero(){
        try {
            employeeService.queryEmployeeByJobNo(Integer.MIN_VALUE);
        } catch (BaseSystemException e) {
            Assert.assertEquals(EmployeeServiceError.JOBNO_NULL, e.getError());
        }
    }

    /**
     * id查询:原有数据已被删除或不存在
     */
    @Test
    public void queryEmployeeOriginNotExisted() {
        employeeDao.removeEntityById(Employee.class, employeeData.getDomains(Employee.class).get(0).getId(), true);
        Assert.assertTrue(!employeeService.queryEmployeeById(employeeData.getDomains(Employee.class).get(0).getId()).isPresent());
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
        DomainPage<Employee> employeeDomainPage = employeeService.queryEmployeesPagesByConditions(conditionQuery);
        Assert.assertTrue(employeeDomainPage.getDomains() != null);
        Assert.assertTrue(employeeDomainPage.getDomains().size() >= 1);
    }


}
