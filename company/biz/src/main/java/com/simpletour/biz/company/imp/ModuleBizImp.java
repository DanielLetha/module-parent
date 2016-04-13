package com.simpletour.biz.company.imp;

import com.simpletour.biz.company.IModuleBiz;
import com.simpletour.biz.company.error.ModuleBizError;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.IModuleDao;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private boolean moduleValid(Module module) {
        List<Module> modules = findModuleByName(module.getName());
        if (modules==null||modules.isEmpty())
            return true;
        if (module.getId() == null)
            return false;
        return !modules.stream().anyMatch(tmp->!tmp.getId().equals(module.getId()));
    }

    @Override
    public Module addModule(Module module) {
        if(!moduleValid(module))
            throw new BaseSystemException(ModuleBizError.MODULE_NAME_EXIST);
        return moduleDao.save(module);
    }

    @Override
    public Module updateModule(Module module) {
        if(!moduleValid(module))
            throw new BaseSystemException(ModuleBizError.MODULE_NAME_EXIST);
        return moduleDao.save(module);
    }

    @Override
    public Module getModuleById(Long id) {
        return moduleDao.getEntityById(Module.class, id);
    }

    @Override
    public void deleteModule(long id) {
        moduleDao.removeEntityById(Module.class, id, false);
    }

    @Override
    public DomainPage findModulePage(ModuleDaoQuery query) {
        return moduleDao.getEntitiesPagesByQuery(Module.class, query);
    }

    @Override
    public List<Module> findModuleList(ModuleDaoQuery query) {
        return moduleDao.getEntitiesByQuery(Module.class, query);
    }

    @Override
    public List<Module> findModuleByName(String name) {
        return moduleDao.getEntitiesByField(Module.class, "name", name);
    }

    @Override
    public boolean isExisted(Long moduleId) {
        if(moduleId==null)
            throw new BaseSystemException(ModuleBizError.MODULE_NULL);
        return getModuleById(moduleId)!=null;
    }

    @Override
    public boolean isAvailable(Module module) {
        if (module==null||module.getId()==null)
            throw new BaseSystemException(ModuleBizError.MODULE_NULL);
        if(module.getPermissions()==null||module.getPermissions().isEmpty())
            throw new BaseSystemException(ModuleBizError.MODULE_PERMISSION_NULL);
        if(!isExisted(module.getId()))
            throw new BaseSystemException(ModuleBizError.MODULE_NOT_EXIST);
        Module module1=getModuleById(module.getId());
        List<Permission> permissionsExisted=module1.getPermissions();
        List<Permission> permissionsNow=module.getPermissions();
        permissionsNow.forEach(tmp->{
            if(tmp.getId()==null)
                throw new BaseSystemException(ModuleBizError.MODULE_PERMISSION_NULL);
            if(!permissionsExisted.stream().anyMatch(tmpExist-> tmpExist.getId().equals(tmp.getId()))){
                throw new BaseSystemException(ModuleBizError.MODULE_PERMISSION_NOT_EXIST);
            }
        });
        return true;
    }

    @Override
    public boolean isAvailable(List<Module> modules) {
        if(modules==null||modules.isEmpty())
            throw new BaseSystemException(ModuleBizError.MODULE_NULL);
        for(int i=0;i<modules.size();i++)
            for(int j=i+1;j<modules.size();j++){
                if (modules.get(i).getId().equals(modules.get(j).getId()))
                    throw new BaseSystemException(ModuleBizError.MODULE_REPEAT);
            }
        if(modules.stream().anyMatch(tmp->!isAvailable(tmp))){
           return false;
        }
        return true;
    }
}
