package com.simpletour.service.company;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;

import java.util.List;
import java.util.Optional;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/7
 * Time: 14:29
 */
public interface IModuleService {
    /**
     * 添加模块
     * throws
     *  ModuleServiceError.MODULE_NULL 模块为空
     *  ModuleServiceError.MODULE_PERMISSION_NULL 模块权限列表为空
     *  ModuleServiceError.MODULE_PERMISSION_REPEAT 模块中权限列表中存在重复名称、或代码的权限
     *  ModuleServiceError.MODULE_PERMISSION_NOT_AVAILABLE 模块中权限列表中存在权限不可用
     * @param module
     * @return
     */
    Optional<Module> addModule(Module module);

    /**
     * 更新模块
     * throws
     *  ModuleServiceError.MODULE_NULL 模块为空，或者id为空
     *  ModuleServiceError.MODULE_NOT_EXIST 模块不存在
     *  ModuleServiceError.MODULE_PERMISSION_NULL 模块权限列表为空
     *  ModuleServiceError.MODULE_PERMISSION_REPEAT 模块中权限列表中存在重复名称、或代码的权限
     *  ModuleServiceError.MODULE_PERMISSION_NOT_AVAILABLE 模块中权限列表中存在权限不可用
     * @param module
     * @return
     */
    Optional<Module> updateModule(Module module);

    /**
     * 根据模块id获取模块
     * @param moduleId
     * @return
     */
    Optional<Module> getModuleById(Long moduleId);

    /**
     * 根据id删除模块
     * throws
     *  ModuleServiceError.MODULE_NOT_EXIST 模块不存在
     * @param id
     */
    void deleteModule(long id);

    /**
     * 根据query获取模块分页
     * @param query
     * @return
     */
    DomainPage findModulePage(ModuleDaoQuery query);

    /**
     * 根据query获取模块列表
     * @param query
     * @return
     */
    List<Module> findModuleList(ModuleDaoQuery query);

    /**
     * 获取所有权限
     * @return
     */
    List<Permission> getAllPermissions();

}
