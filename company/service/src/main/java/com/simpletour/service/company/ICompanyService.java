package com.simpletour.service.company;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.query.CompanyDaoQuery;
import com.simpletour.domain.company.Company;

import java.util.List;
import java.util.Optional;


/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/7.
 * Remark: companyService接口
 */
public interface ICompanyService {

    /**
     * 添加公司, 不会为该公司增加相应的角色和权限
     *
     * @param company
     * @return
     * @throws
     */
    Optional<Company> addCompany(Company company) throws BaseSystemException;

    /**
     * 添加公司, 并为该公司增加管理员，并且给该公司创建一个对应的Role及相应的权限
     *
     * @param company             新增加的公司实体
     * @param permissionCodeList  权限code列表，即新公司管理员，所对应的所有权限
     * @param password            新公司管理员，密码
     * @param avatar              头像默认的地址
     * @return
     * @throws
     */
    Optional<Company> addCompany(Company company, List<String> permissionCodeList, String password, String avatar) throws BaseSystemException;

    /**
     * 删除公司
     * @param id
     * @throws BaseSystemException
     */
    void deleteCompany(Long id) throws BaseSystemException;

    /**
     * 修改一个公司
     * @param company
     * @return
     */
    Optional<Company> updateCompany(Company company) throws BaseSystemException;

    /**
     * 对公司的分页查询
     * @param query
     * @return
     */
    public DomainPage<Company> getCompanyPagesByQuery(CompanyDaoQuery query);

 /**
     * 对公司的列表查询
     * @param query
     * @return
     */
    public List<Company> getCompanyListByQuery(CompanyDaoQuery query);

    /**
     * 根据id查询公司,并且没有被删除
     * @param id
     * @return
     */
    Optional<Company> getCompanyById(Long id);

    /**
     * 根据name获取公司
     */
    Optional<Company> getCompanyByName(String name);

    /**
     * 根据id判断公司是否存在
     * @param id
     * @return
     */
    Boolean isExist(Long id);
}
