package com.simpletour.biz.company.imp;

import com.simpletour.biz.company.ICompanyBiz;
import com.simpletour.biz.company.error.CompanyBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.dao.company.IEmployeeDao;
import com.simpletour.dao.company.query.CompanyDaoQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Employee;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司业务层的实现
 * User: Hawk
 * Date: 2016/4/8 - 16:52
 */
@Component
public class CompanyBizImp implements ICompanyBiz {

    @Resource
    private ICompanyDao companyDao;

    @Resource
    private IEmployeeDao employeeDao;

    @Override
    public Company addCompany(Company company) {
        checkNull(company);
        checkNotDel(company);
        checkAddNameUnique(company);
        return companyDao.save(company);
    }

    @Override
    public void deleteCompany(Long id) {
        checkId(id);
        //是否被人员引用
        //todo, 虎哥说暂时不用，但是为了符合xmind上的需求，还是加上，以后有依赖检查的biz时，直接就干掉
        List<Employee> employees = employeeDao.getEntitiesByField(Employee.class, "company.id", id);
        if (!(employees == null || employees.isEmpty())) {
            throw new BaseSystemException(CompanyBizError.EMPLOYEE_DEPEND_ON_COMPANY);
        }
        companyDao.removeEntityById(Company.class, id);
    }

    @Override
    public Company updateCompany(Company company) {
        checkNull(company);
        checkId(company.getId());
        checkVersion(company);
        checkUpdateNameUnique(company);
        return companyDao.save(company);
    }

    @Override
    public Company findCompanyById(Long id) {
        checkId(id);
        Company company = companyDao.getEntityById(Company.class, id);
        if (!(company == null || company.getDel())) {
            return company;
        }
        return null;
    }

    @Override
    public List<Company> findCompanyPageByQuery(CompanyDaoQuery query) {
        if (query == null) {
            throw new BaseSystemException(CompanyBizError.COMPANY_QUERY_CONDITION_NULL);
        }
        return companyDao.getEntitiesByQuery(Company.class, query);
    }

    @Override
    public List<Company> findCompanyPageByConditions(Map<String, Object> conditions) {
        if (conditions == null) {
            throw new BaseSystemException(CompanyBizError.COMPANY_QUERY_CONDITION_NULL);
        }
        conditions.put("del", Boolean.FALSE);
        return companyDao.getEntitiesByFieldList(Company.class, conditions);
    }

    @Override
    public DomainPage<Company> queryCompanyPagesByQuery(CompanyDaoQuery query) {
        if (query == null) {
            throw new BaseSystemException(CompanyBizError.COMPANY_QUERY_CONDITION_NULL);
        }
        return companyDao.getEntitiesPagesByQuery(Company.class, query);
    }

    @Override
    public DomainPage<Company> queryCompanyPagesByConditions(Map<String, Object> conditions, String orderByFiledName
            , IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        if (conditions == null) {
            throw new BaseSystemException(CompanyBizError.COMPANY_QUERY_CONDITION_NULL);
        }
        conditions.put("del", Boolean.FALSE);
        return companyDao.queryEntitiesPagesByFieldList(Company.class, conditions, orderByFiledName
                , orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public boolean isExist(Long id) {
        checkId(id);
        Company company = findCompanyById(id);
        return company != null;
    }

    /**
     * 检查对象是否为null
     * @param company
     */
    private void checkNull(Company company) {
        if (company == null) {
            throw new BaseSystemException(CompanyBizError.COMPANY_NULL);
        }
    }

    /**
     * 检查对象是否已经被删除
     * @param company
     */
    private void checkNotDel(Company company) {
        if (company.getDel()) {
            throw new BaseSystemException(CompanyBizError.COMPANY_DEL);
        }
    }

    /**
     * 更新时，检查ID不为空
     * @param id
     */
    private void checkId(Long id) {
        if (id == null || id.longValue() < 1L) {
            throw new BaseSystemException(CompanyBizError.COMPANY_ID_ERROR);
        }
    }

    /**
     * 当增加时，检查公司名字的唯一性
     * @param company
     */
    private void checkAddNameUnique(Company company) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", company.getName());
        params.put("del", Boolean.FALSE);
        List<Company> companyList = companyDao.getEntitiesByFieldList(Company.class, params);
        if (!(companyList == null || companyList.isEmpty())) {
            throw new BaseSystemException(CompanyBizError.COMPANY_NAME_REPEAT);
        }
    }

    /**
     * 当更新时，检查公司名字的唯一性
     * @param company
     */
    private void checkUpdateNameUnique(Company company) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", company.getName());
        params.put("del", Boolean.FALSE);
        List<Company> companyList = companyDao.getEntitiesByFieldList(Company.class, params);
        if (!(companyList == null || companyList.isEmpty())) {
            if (companyList.size() > 1 || !companyList.get(0).getId().equals(company.getId())) {
                throw new BaseSystemException(CompanyBizError.COMPANY_NAME_REPEAT);
            }
        }
    }

    /**
     * 当更新时，检查是否设置了版本号
     */
    private void checkVersion(Company company) {
        if (company.getVersion() == null) {
            throw new BaseSystemException(CompanyBizError.COMPANY_UPDATE_VERSION_NULL);
        }
    }
}
