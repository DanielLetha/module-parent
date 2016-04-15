package com.simpletour.biz.company.imp;

import com.simpletour.biz.company.IEmployeeBiz;
import com.simpletour.biz.company.error.EmployeeBizError;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IEmployeeDao;
import com.simpletour.domain.company.Employee;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Mario on 2016/4/8.
 */
@Component
public class EmployeeBizImp implements IEmployeeBiz {

    @Resource
    private IEmployeeDao employeeDao;

    @Override
    public Optional<Employee> addEmployee(Employee employee) {
        if (employee == null) throw new BaseSystemException(EmployeeBizError.EMPLOYEE_NULL);
        //校验基本信息
        validateEmployeeBasicInfo(employee);
        //电话号码是否唯一
        if (isExistEmployeeByMobile(employee.getMobile()))
            throw new BaseSystemException(EmployeeBizError.MOBILE_EXISTED);
        //设置jobNo
        setJobNoForEmployee(employee);
        //设置初始密码
      //  employee.setPasswd(new String(Base64.encode(RandomString.getRandomNumberString(64).getBytes())));
        employee.setSalt(UUID.randomUUID().toString());
        return Optional.ofNullable(employeeDao.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        if (id == null || id.longValue() <= 0) throw new BaseSystemException(EmployeeBizError.EMPLOYEE_ID_NULL);
        if (!this.queryEmployeeById(id).isPresent())
            throw new BaseSystemException(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED);
        employeeDao.removeEntityById(Employee.class, id);
    }

    @Override
    public Optional<Employee> updateEmployee(Employee employee) {
        if (employee == null || employee.getId() == null)
            throw new BaseSystemException(EmployeeBizError.EMPLOYEE_NULL_OR_ID_NULL);
        //检验原有人员是否存在
        Optional<Employee> origin = this.queryEmployeeById(employee.getId());
        if (!origin.isPresent())
            throw new BaseSystemException(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED);
        //设置密码和salt
        employee.setPasswd(origin.get().getPasswd());
        employee.setSalt(origin.get().getSalt());
        //校验基本信息
        validateEmployeeBasicInfo(employee);
        //验证账号是否和原有一样
        if (!origin.get().getJobNo().equals(employee.getJobNo()))
            throw new BaseSystemException(EmployeeBizError.JOB_NO_NOT_SAME);
        //验证电话号码是否唯一
        if (isExsitEmployeeByMobile(employee.getId(), employee.getMobile()))
            throw new BaseSystemException(EmployeeBizError.MOBILE_EXISTED);
        return Optional.ofNullable(employeeDao.save(employee));
    }

    @Override
    public Optional<Employee> changePwdForEmployee(Long id, String password, String salt) {
        if (id == null || password == null || password.isEmpty() || salt == null || salt.isEmpty())
            throw new BaseSystemException(EmployeeBizError.EMPLOYEE_ID_PWD_SALT);
        Optional<Employee> employeeOptional = this.queryEmployeeById(id);
        if (!employeeOptional.isPresent()) throw new BaseSystemException(EmployeeBizError.EMPLOYEE_ORIGIN_NOT_EXISTED);
        employeeOptional.get().setPasswd(password);
        employeeOptional.get().setSalt(salt);
        return Optional.ofNullable(employeeDao.save(employeeOptional.get()));
    }

    @Override
    public Optional<Employee> queryEmployeeById(Long id) {
        if (id == null) throw new BaseSystemException(EmployeeBizError.EMPLOYEE_ID_NULL);
        Employee employee = employeeDao.getEntityById(Employee.class, id);
        if (employee != null && !employee.getDel()) return Optional.ofNullable(employee);
        return Optional.empty();
    }

    @Override
    public Optional<Employee> queryEmployeeByJobNo(Integer jobNo) {
        if (jobNo == null || jobNo <= 0) throw new BaseSystemException(EmployeeBizError.JOBNO_NULL);
        return Optional.ofNullable(employeeDao.getEntityByField(Employee.class, "jobNo", jobNo));
    }

    @Override
    public DomainPage<Employee> queryEmployeesPagesByConditions(ConditionOrderByQuery conditionOrderByQuery) {
        return employeeDao.getEntitiesPagesByQuery(Employee.class, conditionOrderByQuery);
    }

    private void validateEmployeeBasicInfo(Employee employee) {
        //校验基本信息
        if (employee.getName() == null || employee.getName().isEmpty() || employee.getAdmin() == null ||
                employee.getAvater() == null || employee.getAvater().isEmpty() || employee.getMobile() == null || employee.getMobile().isEmpty())
            throw new BaseSystemException(EmployeeBizError.EMPLOYEE_BASIC_INFO_NULL);
    }

    private boolean isExsitEmployeeByMobile(Long id, String mobile) {
        if (id == null || mobile == null || mobile.isEmpty())
            throw new BaseSystemException(EmployeeBizError.MOBILE_NULL);
        Employee origin = employeeDao.getEntityById(Employee.class, id);
        if (origin == null) throw new BaseSystemException(EmployeeBizError.SYSTEM_ERROR);
        if (!mobile.equals(origin.getMobile()) && isExistEmployeeByMobile(mobile)) return true;
        return false;
    }

    private boolean isExistEmployeeByMobile(String mobile) {
        if (mobile == null || mobile.isEmpty())
            throw new BaseSystemException(EmployeeBizError.MOBILE_NULL);
        List<Employee> employees = employeeDao.getEntitiesByField(Employee.class, "mobile", mobile);
        if (!(employees == null || employees.isEmpty())) return true;
        return false;
    }

    private void setJobNoForEmployee(Employee employee) {
        if (employee == null || employee.getCompany() == null || employee.getCompany().getId() == null)
            throw new BaseSystemException(EmployeeBizError.EMPLOYEE_SET_JOB_NO_BASIC_INFO_WRONG);
        Integer jobNoMax = employeeDao.queryEmployeeByComanyWithJobNoMax(employee.getCompany().getId());
        if (jobNoMax == null) {
            try {
                employee.setJobNo(Integer.parseInt(employee.getCompany().getId().toString()));
            } catch (NumberFormatException e) {
                throw new BaseSystemException(EmployeeBizError.SYSTEM_ERROR);
            }
        } else {
            if (jobNoMax <= 0)
                throw new BaseSystemException(EmployeeBizError.EMPLOYEE_SET_JOB_NO_EXCEPTION);
            employee.setJobNo(jobNoMax + 1);
        }
    }
}
