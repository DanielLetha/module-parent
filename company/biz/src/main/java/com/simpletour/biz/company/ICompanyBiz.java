package com.simpletour.biz.company;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.dao.company.query.CompanyDaoQuery;
import com.simpletour.domain.company.Company;

import java.util.List;
import java.util.Map;

/**
 * 公司的业务层
 * User: Hawk
 * Date: 2016/4/8 - 16:51
 */
public interface ICompanyBiz {

    /**
     * 增加一个公司
     * @param company 公司实体类
     * @return
     */
    Company addCompany(Company company);

    /**
     * 删除公司
     * @param id 公司主键ID
     */
    void deleteCompany(Long id);

    /**
     * 更新公司
     * @param company 公司实体
     * @return
     */
    Company updateCompany(Company company);

    /**
     * 根据ID查询公司
     * @param id 公司ID
     * @return
     */
    Company findCompanyById(Long id);

    /**
     * 根据条件查询公司数据
     * @param query 条件集
     * @return
     */
    List<Company> findCompanyPageByQuery(CompanyDaoQuery query);

    /**
     * 根据条件查询公司数据
     * @param conditions 条件集
     * @return
     */
    List<Company> findCompanyPageByConditions(final Map<String, Object> conditions);

    /**
     * 多条件查询，返回分页对象
     * @param query 查询对象
     * @return
     */
    DomainPage<Company> queryCompanyPagesByQuery(CompanyDaoQuery query);

    /**
     * 多条件查询，返回分页对象
     * @author Hawk
     * @Date   2016-03-21 17:32
     *
     * @param conditions       组合查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     *
     * @return       最新的公司实体
     */
    DomainPage<Company> queryCompanyPagesByConditions(final Map<String, Object> conditions
            , String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 检查是否存在
     * @param id
     * @return
     */
    boolean isExist(Long id);
}
