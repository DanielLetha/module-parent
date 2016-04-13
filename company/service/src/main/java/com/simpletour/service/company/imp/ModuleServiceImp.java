package com.simpletour.service.company.imp;

import com.simpletour.biz.company.IModuleBiz;
import com.simpletour.biz.company.IPermissionBiz;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.service.company.IModuleService;
import com.simpletour.service.company.error.ModuleServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private IPermissionBiz permissionBiz;

    private void verifyModule(Module module) {
        if (module == null)
            throw new BaseSystemException(ModuleServiceError.MODULE_NULL);
        if (module.getPermissions() == null || module.getPermissions().isEmpty())
            throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_NULL);
        int permissionSize = module.getPermissions().size();
        for (int i = 0; i < permissionSize; i++) {
            if (module.getPermissions().get(i).getModule() == null)
                module.getPermissions().get(i).setModule(module);
            String name1 = module.getPermissions().get(i).getName();
            String code1 = module.getPermissions().get(i).getCode();
            for (int j = i + 1; j < permissionSize; j++) {
                String name2 = module.getPermissions().get(j).getName();
                String code2 = module.getPermissions().get(j).getCode();
                if (name1.equals(name2) || code1.equals(code2))
                    throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_REPEAT);
            }
        }
        if (module.getPermissions().stream().anyMatch(tmp -> !permissionBiz.isAvailable(tmp)))
            throw new BaseSystemException(ModuleServiceError.MODULE_PERMISSION_NOT_AVAILABLE);

        module.getPermissions().forEach(tmp->{
            if(tmp.getId()!=null){
                Permission permissionExist=permissionBiz.getById(tmp.getId());
                tmp.setRoleList(permissionExist.getRoleList());
                tmp.setCompanyList(permissionExist.getCompanyList());
                tmp.setScopeTemplateList(permissionExist.getScopeTemplateList());
            }
        });
    }

    @Override
    public Optional<Module> addModule(Module module) {
        verifyModule(module);
        return Optional.ofNullable(moduleBiz.addModule(module));
    }

    @Override
    public Optional<Module> updateModule(Module module) {
        if (module == null || module.getId() == null)
            throw new BaseSystemException(ModuleServiceError.MODULE_NULL);
        if(!moduleBiz.isExisted(module.getId()))
            throw new BaseSystemException(ModuleServiceError.MODULE_NOT_EXIST);
        verifyModule(module);
        return Optional.ofNullable(moduleBiz.updateModule(module));
    }

    @Override
    public Optional<Module> getModuleById(Long moduleId) {
        return Optional.ofNullable(moduleBiz.getModuleById(moduleId));
    }

    @Override
    public void deleteModule(long id) {
        if (!moduleBiz.isExisted(id))
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
    public List<Module> findByName(String name) {
        return moduleBiz.findModuleByName(name);
    }
}
