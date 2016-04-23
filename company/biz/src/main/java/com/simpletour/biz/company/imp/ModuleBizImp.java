package com.simpletour.biz.company.imp;

import com.simpletour.biz.company.IModuleBiz;
import com.simpletour.biz.company.error.ModuleBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IModuleDao;
import com.simpletour.dao.company.IPermissionDao;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/9
 * Time: 10:15
 */
@Component
public class ModuleBizImp implements IModuleBiz {
    @Autowired
    private IModuleDao moduleDao;

    @Autowired
    private IPermissionDao permissionDao;

    /**
     * 判断Module name是否可用
     * @param module
     * @return
     */
    private boolean isModuleNameValid(Module module) {
        Module moduleExist = findModuleByName(module.getName());
        if (moduleExist == null)
            return true;
        if (module.getId() == null)
            return false;
        return moduleExist.getId().equals(module.getId());
    }

    @Override
    public Module addModule(Module module) {
        if (!isModuleNameValid(module))
            throw new BaseSystemException(ModuleBizError.MODULE_NAME_EXIST);
        return moduleDao.save(module);
    }

    @Override
    public Module updateModule(Module module) {
        if (!isModuleNameValid(module))
            throw new BaseSystemException(ModuleBizError.MODULE_NAME_EXIST);
        return moduleDao.save(module);
    }

    @Override
    public Module getModuleById(Long id) {
        Module module=moduleDao.getEntityById(Module.class, id);
        if(module==null||module.getDel())
            return null;
        else
            return module;
    }

    @Override
    public void deleteModule(long id) {
        Module module=getModuleById(id);
        module.getPermissions().clear();
        updateModule(module);
        moduleDao.removeEntityById(Module.class, id);
    }

    @Override
    public DomainPage findModulePage(ModuleDaoQuery query) {
        return moduleDao.getEntitiesPagesByQuery(Module.class, query);
    }

    @Override
    public List<Module> findModuleList(ModuleDaoQuery query) {
        return moduleDao.getEntitiesByQuery(Module.class, query);
    }

    /**
     * 根据name获取模块
     * @param name
     * @return
     */
    private Module findModuleByName(String name) {
        List<Module> modules=moduleDao.getEntitiesByField(Module.class, "name", name);
        if(modules==null||modules.isEmpty())
            return null;
        if(modules.size()!=1)
            throw new BaseSystemException(ModuleBizError.MODULE_DATA_ERROR);
        return modules.get(0);
    }

    @Override
    public boolean isModuleExisted(Long moduleId) {
        if (moduleId == null)
            throw new BaseSystemException(ModuleBizError.MODULE_NULL);
        return getModuleById(moduleId) != null;
    }

    @Override
    public boolean isModuleAvailable(Module module) {
        if (module == null || module.getId() == null)
            throw new BaseSystemException(ModuleBizError.MODULE_NULL);
        if (module.getPermissions() == null || module.getPermissions().isEmpty())
            throw new BaseSystemException(ModuleBizError.MODULE_PERMISSION_NULL);
        if (!isModuleExisted(module.getId()))
            throw new BaseSystemException(ModuleBizError.MODULE_NOT_EXIST);
        Module module1 = getModuleById(module.getId());
        List<Permission> permissionsExisted = module1.getPermissions();
        List<Permission> permissionsNow = module.getPermissions();
        permissionsNow.forEach(tmp -> {
            if (tmp.getId() == null)
                throw new BaseSystemException(ModuleBizError.MODULE_PERMISSION_NULL);
            if (!permissionsExisted.stream().anyMatch(tmpExist -> tmpExist.getId().equals(tmp.getId()))) {
                throw new BaseSystemException(ModuleBizError.MODULE_PERMISSION_NOT_EXIST);
            }
        });
        return true;
    }

    @Override
    public boolean isModuleAvailable(List<Module> modules) {
        if (modules == null || modules.isEmpty())
            throw new BaseSystemException(ModuleBizError.MODULE_NULL);
        for (int i = 0; i < modules.size(); i++)
            for (int j = i + 1; j < modules.size(); j++) {
                if (modules.get(i).getId().equals(modules.get(j).getId()))
                    throw new BaseSystemException(ModuleBizError.MODULE_REPEAT);
            }
        if (modules.stream().anyMatch(tmp -> !isModuleAvailable(tmp))) {
            return false;
        }
        return true;
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionDao.getAllEntities(Permission.class);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionDao.getEntityById(Permission.class, id);
    }

    @Override
    public Permission getPermissionByCode(String permissionCode) {
        List<Permission> permissions = permissionDao.getEntitiesByField(Permission.class, "code", permissionCode);
        if (permissions == null || permissions.isEmpty())
            return null;
        if (permissions.size() != 1)
            throw new BaseSystemException(ModuleBizError.MODULE_DATA_ERROR);
        return permissions.get(0);
    }

    @Override
    public Boolean isPermissionCodeExist(String permissionCode) {
        return getPermissionByCode(permissionCode) != null;
    }

    @Override
    public boolean isPermissionExisted(Long functionId) {
        return getPermissionById(functionId) != null;
    }

    @Override
    public boolean isPermissionAvailable(Permission permission) {
        if (permission == null)
            throw new BaseSystemException(ModuleBizError.PERMISSION_NULL);
        if (permission.getId() != null) {
            if (!isPermissionExisted(permission.getId()))
                throw new BaseSystemException(ModuleBizError.PERMISSION_NOT_EXIST);
        }
        Permission permissionSameCode = getPermissionByCode(permission.getCode());
        if (permissionSameCode == null)
            return true;
        if (permission.getId() == null)
            return false;
        return permissionSameCode.getId().equals(permission.getId());
    }
}
