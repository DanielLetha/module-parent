package com.simpletour.service.company;

import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.domain.company.Permission;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by liangfei on 2015/11/21.
 * Modified by XuHui on 2015/12/03.
 */
public interface IPermissionService {

    /**
     * 添加后台权限项对象
     *
     * @param permission
     * @return
     * @throws IllegalArgumentException
     */
    @Deprecated
    Optional<Permission> addPermission(Permission permission);


    /**
     * @param id
     * @throws BaseSystemException 要删除的不存在时抛出异常NOT_EXIST
     */
    @Deprecated
    void deletePermission(Long id) throws BaseSystemException;

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    @Deprecated
    Optional<Permission> getById(Long id);

    /**
     * 修改一个后台权限项相关数据
     *
     * @param permission
     * @return
     */
    @Deprecated
    Optional<Permission> updatePermissione(Permission permission);

    /**
     * 根据code获得权限（code是不重复的）
     */
    @Deprecated
    Optional<Permission> getByCode(String permissionCode);

    /**
     * 根据code判断code是否存在。
     * @param permissionCode
     * @return
     */
    @Deprecated
    Boolean isCodeExist(String permissionCode);

    /**
     * 获取所有权限
     * @return
     */
    List<Permission> getAllPermissions();

    /**
     * 获取所有权限
     * @return
     */
    @Deprecated
    List<Permission> getPermissionsByName(String name);

    /**
     * 分页查询后台权限项
     *
     * @param page
     * @return
     */
    @Deprecated
    DomainPage<Permission> getPermissionsByPage(int page);

    /**
     * 分页查询后台角色
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Deprecated
    DomainPage<Permission> getPermissionsByPage(int page, int pageSize);

    /**
     * 分页模糊查询
     *
     * @param conditions       组合查询条件(name：餐饮点类型, address：名称, destination.name: 目的地)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     * @return
     */
    @Deprecated
    DomainPage<Permission> queryPermissionsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 根据name，code查询,使用默认大小。
     */
    @Deprecated
    DomainPage<Permission> queryPermissionsPages(String name,String code,int pageIndex);

    /**
     * 模糊查询，根据name、code，默认根据id排序
     */
    @Deprecated
    DomainPage<Permission> queryPermissionsPages(String name, String code, int pageIndex, int pageSize);

    /**
     * 模糊查询，根据name、code。根据条件进行排序。
     */
    @Deprecated
    DomainPage<Permission> queryPermissionsPages(String name, String code, String orderByFiledName, IBaseDao.SortBy sortBy, int pageIndex, int pageSize);

    /**
     * 判断权限是否可用
     * @param permission
     * @return
     */
    @Deprecated
    boolean isAvailable(Permission permission);

    /**
     * 判断id是否存在
     * @param functionId
     * @return
     */
    @Deprecated
    boolean isExisted(Long functionId);

}
