package com.simpletour.biz.company;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;

import java.util.List;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：Module Biz
 * Date: 2016/4/9
 * Time: 10:08
 */
public interface IModuleBiz {

    /**
     * 添加模块
     * 校验是否存在重名：throw ModuleBizError.MODULE_NAME_EXIST
     * @param module
     * @return
     */
    Module addModule(Module module);

    /**
     * 更新模块
     * 校验是否存在重名：throw ModuleBizError.MODULE_NAME_EXIST
     * @param module
     * @return
     */
    Module updateModule(Module module);

    /**
     * 根据id查询模块
     * @param id
     * @return
     */
    Module getModuleById(Long id);

    /**
     * 根据id删除模块
     * @param id
     */
    void deleteModule(long id);

    /**
     * 根据query对象查询模块分页
     * @param query
     * @return
     */
    DomainPage findModulePage(ModuleDaoQuery query);

    /**
     * 根据query对象查询模块列表
     * @param query
     * @return
     */
    List<Module> findModuleList(ModuleDaoQuery query);

    /**
     * 根据id判断模块是否存在
     * @param moduleId
     * @return
     */
    boolean isModuleExisted(Long moduleId);

    /**
     * 判断模块是否可用
     * throws：
     *  ModuleBizError.MODULE_NULL 模块为空、模块id为空
     *  ModuleBizError.MODULE_PERMISSION_NULL 模块权限列表为空、模块权限列表大小为0
     *  ModuleBizError.MODULE_NOT_EXIST 模块不存在
     *  ModuleBizError.MODULE_PERMISSION_NULL 存在权限列表中的权限为空
     *  ModuleBizError.MODULE_PERMISSION_NOT_EXIST 存在权限不属于该权限即认为不存在。
     * @param module
     * @return
     */
    boolean isModuleAvailable(Module module);

    /**
     * 判断模块是否可用
     * throws：
     *  ModuleBizError.MODULE_NULL 模块列表为空
     *  ModuleBizError.MODULE_REPEAT 存在重复的模块id；
     *  ModuleBizError.MODULE_NULL 模块为空、模块id为空
     *  ModuleBizError.MODULE_PERMISSION_NULL 模块权限列表为空、模块权限列表大小为0
     *  ModuleBizError.MODULE_NOT_EXIST 模块不存在
     *  ModuleBizError.MODULE_PERMISSION_NULL 存在权限列表中的权限为空
     *  ModuleBizError.MODULE_PERMISSION_NOT_EXIST 存在权限不属于该权限即认为不存在。
     * @param modules
     * @return
     */
    boolean isModuleAvailable(List<Module> modules);

    /**
     * 获取所有权限
     * @return
     */
    List<Permission> getAllPermissions();

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    Permission getPermissionById(Long id);

    /**
     * 根据code获得权限（code是不重复的）
     */
    Permission getPermissionByCode(String permissionCode);

    /**
     * 根据code判断code是否存在。
     * @param permissionCode
     * @return
     */
    Boolean isPermissionCodeExist(String permissionCode);

    /**
     * 判断权限是否可用
     * @param permission
     * @return
     */
    boolean isPermissionAvailable(Permission permission);

    /**
     * 判断id是否存在
     * @param functionId
     * @return
     */
    boolean isPermissionExisted(Long functionId);
}
