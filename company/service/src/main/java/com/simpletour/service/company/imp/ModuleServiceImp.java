package com.simpletour.service.company.imp;

import com.simpletour.biz.company.IModuleBiz;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.service.company.IModuleService;
import com.simpletour.service.company.error.ModuleServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/7
 * Time: 14:30
 */
@Service
public class ModuleServiceImp implements IModuleService {
    @Autowired
    private IModuleBiz moduleBiz;

    private boolean isPermissionRepeat(List<Permission> permissions,Module module){
        int permissionSize = permissions.size();
        for (int i = 0; i < permissionSize; i++) {
            if (permissions.get(i).getModule() == null)
                permissions.get(i).setModule(module);
            String name1 = permissions.get(i).getName();
            String code1 = permissions.get(i).getCode();
            for (int j = i + 1; j < permissionSize; j++) {
                String name2 = permissions.get(j).getName();
                String code2 = permissions.get(j).getCode();
                if (name1.equals(name2) || code1.equals(code2))
                    return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Module> addModule(Module module) {
        if (module == null)
            throw new BaseSystemException(ModuleServiceError.MODULE_NULL);
        if (module.getPermissions() == null || module.getPermissions().isEmpty())
            throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_NULL);
        if(isPermissionRepeat(module.getPermissions(),module))
            throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_REPEAT);
        if (module.getPermissions().stream().anyMatch(tmp -> !moduleBiz.isPermissionAvailable(tmp)))
            throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_NOT_AVAILABLE);
        return Optional.ofNullable(moduleBiz.addModule(module));
    }

    @Override
    public Optional<Module> updateModule(Module module) {
        if (module == null || module.getId() == null)
            throw new BaseSystemException(ModuleServiceError.MODULE_NULL);
        if(!moduleBiz.isModuleExisted(module.getId()))
            throw new BaseSystemException(ModuleServiceError.MODULE_NOT_EXIST);
        if (module.getPermissions() == null || module.getPermissions().isEmpty())
            throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_NULL);
        if(isPermissionRepeat(module.getPermissions(),module))
            throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_REPEAT);
        if (module.getPermissions().stream().anyMatch(tmp -> !moduleBiz.isPermissionAvailable(tmp)))
            throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_NOT_AVAILABLE);
        module.getPermissions().forEach(tmp->{
            if(tmp.getId()!=null){
                Permission permissionExist=moduleBiz.getPermissionById(tmp.getId());
                tmp.setRoleList(permissionExist.getRoleList());
                tmp.setCompanyList(permissionExist.getCompanyList());
                tmp.setScopeTemplateList(permissionExist.getScopeTemplateList());
            }
        });
        return Optional.ofNullable(moduleBiz.updateModule(module));
    }

    @Override
    public Optional<Module> getModuleById(Long moduleId) {
        return Optional.ofNullable(moduleBiz.getModuleById(moduleId));
    }

    @Override
    @Transactional
    public void deleteModule(long id) {
        if (!moduleBiz.isModuleExisted(id))
            throw new BaseSystemException(ModuleServiceError.MODULE_NOT_EXIST);
        moduleBiz.deleteModule(id);
    }

    @Override
    public DomainPage findModulePage(ModuleDaoQuery query) {
        return moduleBiz.findModulePage(query);
    }

    @Override
    public List<Module> findModuleList(ModuleDaoQuery query) {
        return moduleBiz.findModuleList(query);
    }

    @Override
    public List<Permission> getAllPermissions(){
        return moduleBiz.getAllPermissions();
    }
}
