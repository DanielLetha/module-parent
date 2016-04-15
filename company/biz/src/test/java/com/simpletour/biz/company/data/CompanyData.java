package com.simpletour.biz.company.data;

import com.simpletour.commons.test.generator.AbstractDataGenerator;
import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.domain.company.Company;

/**
 * 公司数据生成器
 * User: Hawk
 * Date: 2016/4/9 - 11:50
 */
public class CompanyData extends AbstractDataGenerator {

    private ICompanyDao companyDao;

    public CompanyData(ICompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public void generator() {

        Company company1 = new Company(generateName("companyName1")
                , generateName("companyAddress1"), generateName("companyRemark1"));

        Company company2 = new Company(generateName("companyName2")
                , generateName("companyAddress2"), generateName("companyRemark2"));
        domains.add(companyDao.save(company1));
        domains.add(companyDao.save(company2));
    }
}
