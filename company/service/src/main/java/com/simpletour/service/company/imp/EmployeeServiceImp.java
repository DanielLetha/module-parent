package com.simpletour.service.company.imp;

import com.simpletour.biz.company.ICompanyBiz;
import com.simpletour.biz.company.IEmployeeBiz;
import com.simpletour.biz.company.IRoleBiz;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.domain.company.Employee;
import com.simpletour.domain.company.Role;
import com.simpletour.service.company.IEmployeeService;
import com.simpletour.service.company.error.EmployeeServiceError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by zt on 2015/11/20.
 */
@Service
public class EmployeeServiceImp implements IEmployeeService {

    @Resource
    private IEmployeeBiz employeeBiz;

    @Resource
    private IRoleBiz roleBiz;

    @Resource
    private ICompanyBiz companyBiz;

    @Resource
    private ICompanyDao companyDao;

    /**
     * 校验employee基本信息，包括对关联公司、关联角色是否存在，jobNo和mobile是否唯一
     *
     * @param employee employee对象实体
     */
    private void validateForEmployeeBasic(Employee employee) {
        if (employee == null) throw new BaseSystemException(EmployeeServiceError.EMPTY_ENTITY);
        if (employee.getCompany() == null || employee.getCompany().getId() == null)
            throw new BaseSystemException(EmployeeServiceError.COMPANY_NULL);
        if (!companyBiz.isExist(employee.getCompany().getId()))
            throw new BaseSystemException(EmployeeServiceError.COMPANY_HAS_BEEN_DELETE);
        //验证角色
        if (employee.getRole() == null || employee.getRole().getId() == null)
            throw new BaseSystemException(EmployeeServiceError.ROLE_NULL);
        Optional<Role> roleOrigin = roleBiz.getRoleById(employee.getRole().getId());
        if (!roleOrigin.isPresent() || roleOrigin.get() == null || roleOrigin.get().getDel())
            throw new BaseSystemException(EmployeeServiceError.ROLE_HAS_BEEN_DELETE);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Employee> addEmployee(Employee employee) {
        validateForEmployeeBasic(employee);
        return employeeBiz.addEmployee(employee);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteEmployee(Long id) {
        employeeBiz.deleteEmployee(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Employee> updateEmployee(Employee employee) {
        validateForEmployeeBasic(employee);
        return employeeBiz.updateEmployee(employee);
    }

    @Override
    public Optional<Employee> changePwdForEmployee(Long id, String password, String salt) {
        if (id == null || password == null || password.isEmpty() || salt == null || salt.isEmpty())
            throw new BaseSystemException(EmployeeServiceError.EMPLOYEE_ID_PWD_SALT_NULL);
        return employeeBiz.changePwdForEmployee(id, password, salt);
    }

    @Override
    public Optional<Employee> queryEmployeeById(Long id) {
        if (id == null) throw new BaseSystemException(EmployeeServiceError.ID_NULL);
        return employeeBiz.queryEmployeeById(id);
    }

    @Override
    public Optional<Employee> queryEmployeeByJobNo(Integer jobNo) {
        if (jobNo == null || jobNo <= 0) throw new BaseSystemException(EmployeeServiceError.JOBNO_NULL);
        return employeeBiz.queryEmployeeByJobNo(jobNo);
    }

    @Override
    public DomainPage<Employee> queryEmployeesPagesByConditions(ConditionOrderByQuery conditionOrderByQuery) {
        return employeeBiz.queryEmployeesPagesByConditions(conditionOrderByQuery);
    }

}
