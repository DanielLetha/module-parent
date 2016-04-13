package com.simpletour.service.company.data;

import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.domain.company.Company;
import com.simpletour.test.helper.generator.AbstractDataGenerator;

/**
 * Created by Mario on 2016/4/9.
 */
public class Employee_Company_data extends AbstractDataGenerator {

    private ICompanyDao companyDao;

    public Employee_Company_data(ICompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public void generator() {
        Company company = new Company(generateName("simpletour"), generateName("software address"), generateName("remark"));
        domains.add(companyDao.save(company));
    }
}
