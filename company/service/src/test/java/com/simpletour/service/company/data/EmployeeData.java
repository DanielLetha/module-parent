package com.simpletour.service.company.data;

import com.simpletour.common.core.domain.BaseDomain;
import com.simpletour.dao.company.IEmployeeDao;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Employee;
import com.simpletour.domain.company.Role;
import com.simpletour.test.helper.generator.AbstractDataGenerator;

import java.util.List;

/**
 * Created by Mario on 2016/4/9.
 */
public class EmployeeData extends AbstractDataGenerator {

    private IEmployeeDao employeeDao;

    public EmployeeData(IEmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public void generator() {
        Employee employee = new Employee("remark", 10010, false, "mario", "http://www.handsome.com/mario.jpg", "18608060720", "123456", "salt");
        List<BaseDomain> companys = getDomains(Company.class);
        employee.setCompany((Company) companys.get(0));
        List<BaseDomain> roles = getDomains(Role.class);
        employee.setRole((Role) roles.get(0));
        domains.add(employeeDao.save(employee));
    }
}
