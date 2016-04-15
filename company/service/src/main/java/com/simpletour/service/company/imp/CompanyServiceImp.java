package com.simpletour.service.company.imp;

import com.simpletour.biz.company.*;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.query.CompanyDaoQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Employee;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.Role;
import com.simpletour.service.company.ICompanyService;
import com.simpletour.service.company.error.CompanyServiceError;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/7.
 * Remark: companyService实现
 */
@Service
public class CompanyServiceImp implements ICompanyService {

    @Resource
    private IModuleBiz moduleBiz;

    @Resource
    private ICompanyBiz companyBiz;

    @Resource
    private IRoleBiz roleBiz;

    @Resource
    private IEmployeeBiz employeeBiz;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Company> addCompany(Company company) throws BaseSystemException {
        verifyCompany(company, Boolean.FALSE);
        return Optional.ofNullable(companyBiz.addCompany(company));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Company> addCompany(Company company, List<String> permissionCodeList,String avatar) throws BaseSystemException {
        verifyCompany(company, Boolean.FALSE);
        Company companySaved = companyBiz.addCompany(company);
        if (companySaved == null) throw new BaseSystemException(CompanyServiceError.COMPANY_SAVE_FAILURE);
        //保存公司后，初始化角色
        Optional<Role> role = initRole(companySaved, permissionCodeList);
        if (!role.isPresent()) throw new BaseSystemException(CompanyServiceError.COMPANY_ROLE_INIT_FAILURE);
        //初始化公司管理员
        Optional<Employee> employee = initEmployee(companySaved, role.get(),avatar);
        if (!employee.isPresent()) throw new  BaseSystemException(CompanyServiceError.COMPANY_EMPLOYEE_INIT_FAILURE);
        return Optional.ofNullable(companySaved);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteCompany(Long id) throws BaseSystemException {
        if (!isExist(id)) {
            throw new BaseSystemException(CompanyServiceError.COMPANY_NOT_EXIST);
        }
        companyBiz.deleteCompany(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Company> updateCompany(Company company) throws BaseSystemException {
        verifyCompany(company, Boolean.TRUE);
        return Optional.ofNullable(companyBiz.updateCompany(company));
    }

    @Override
    public DomainPage<Company> getCompanyPagesByQuery(CompanyDaoQuery query) {
        return companyBiz.queryCompanyPagesByQuery(query);
    }

    @Override
    public List<Company> getCompanyListByQuery(CompanyDaoQuery query) {
        return companyBiz.findCompanyPageByQuery(query);
    }

    @Override
    public Optional<Company> getCompanyById(Long id) {
        if (id == null) throw new BaseSystemException(CompanyServiceError.ID_NULL);
        Company company = companyBiz.findCompanyById(id);
        return Optional.ofNullable(company);
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        if (name == null) {
            throw new BaseSystemException(CompanyServiceError.NAME_NULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        List<Company> companyList = companyBiz.findCompanyPageByConditions(params);
        if (!(companyList == null || companyList.isEmpty())) {
            return Optional.ofNullable(companyList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Boolean isExist(Long id) {
        if (id == null) {
            throw new BaseSystemException(CompanyServiceError.ID_NULL);
        }
        return companyBiz.isExist(id);
    }

    /**
     * 功能：公司一致性的验证
     * @param company 公司对象
     * @param isUpdate 是不是编辑
     */
    private void verifyCompany(Company company,Boolean isUpdate){
        if (company == null){
            throw new BaseSystemException(CompanyServiceError.COMPANY_NULL);
        }
        if (isUpdate){
            //判断公司存在且为非删除
            if (!isExist(company.getId())) throw new BaseSystemException(CompanyServiceError.COMPANY_NOT_EXIST);
        }

        //添加公司时必须要有功能
        if (company.getPermissions() == null || company.getPermissions().isEmpty()){
            throw new BaseSystemException(CompanyServiceError.COMPANY_MUST_CONTAIN_PERMISSION);
        }
        //对公司包含的功能以及模块是否存在进行验证
        ArrayList<Permission> permissions = new ArrayList<>();
        for (Permission permission:company.getPermissions()){
            boolean permissionExisted = moduleBiz.isPermissionExisted(permission.getId());
            if (!permissionExisted){
                throw  new BaseSystemException(CompanyServiceError.PERMISSION_NOT_EXIST);
            }
            boolean moduleExisted = moduleBiz.isModuleExisted(permission.getModule().getId());
            if (!moduleExisted){
                throw new BaseSystemException(CompanyServiceError.MODULE_NOT_EXIST);
            }
            //从数据库查询出permisssion
            Permission permisssionFormDb = moduleBiz.getPermissionById(permission.getId());
            if (permisssionFormDb != null){
                permissions.add(permisssionFormDb);
            }
            company.setPermissions(permissions);
        }
    }

    /**
     * 添加公司后初始化角色
     * @param company 不需要对公司判断空了
     * @param permissionCodeList 权限code的集合
     */
    private Optional<Role> initRole(Company company, List<String> permissionCodeList){
        ArrayList<Permission> permissions = new ArrayList<>();
        if (permissionCodeList != null && !permissionCodeList.isEmpty()){
            for (String code: permissionCodeList){
                Permission permission = moduleBiz.getPermissionByCode(code);
                if (permission == null){
                    throw new BaseSystemException(CompanyServiceError.COMPANY_ROLE_PERMISSION_NOT_IS_EXIST);
                }
                permissions.add(permission);
            }
        }
        Role role = new Role("公司管理员", 0, company, "", permissions);
        return roleBiz.saveRole(role);
    }

    /**
     * 初始化公司管理员
     * @param company 已经保存的company，不需要判空
     * @param role 初始化后的角色，不需要判空
     */
    private Optional<Employee> initEmployee(Company company,Role role,String avatar){
        Employee employee = new Employee(Boolean.TRUE,"admin",company.getMobile(),avatar,company, role);
        return employeeBiz.addEmployee(employee);
    }
}
