package com.simpletour.service.company.data;

import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.dao.company.IRoleDao;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Role;
import com.simpletour.commons.test.generator.AbstractDataGenerator;

import java.util.List;

/**
 * Created by Mario on 2016/4/9.
 */
public class Employee_Role_data extends AbstractDataGenerator {

    private IRoleDao roleDao;

    public Employee_Role_data(IRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void generator() {
        List<BaseDomain> companys = getDomains(Company.class);
        Role role = new Role(generateName("role"), 0, (Company) companys.get(0), "remark", null);
        domains.add(roleDao.save(role));
    }
}
