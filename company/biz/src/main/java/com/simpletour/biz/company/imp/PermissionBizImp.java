package com.simpletour.biz.company.imp;

import com.simpletour.biz.company.IPermissionBiz;
import com.simpletour.biz.company.error.PermissionBizError;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.IPermissionDao;
import com.simpletour.domain.company.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: afsd
 * Date: 2016/4/9
 * Time: 14:41
 * Remark: Modified by shiguanglu@simpletour.com, add @Component annotation
 */
@Component
public class PermissionBizImp implements IPermissionBiz{
    @Autowired
    private IPermissionDao permissionDao;

    @Override
    public Permission getById(Long id) {
        return permissionDao.getEntityById(Permission.class, id);
    }

    @Override
    public List<Permission> getByCode(String permissionCode) {
        return permissionDao.getEntitiesByField(Permission.class, "code", permissionCode);
    }

    @Override
    public Boolean isCodeExist(String permissionCode) {
        List<Permission> permissions = getByCode(permissionCode);
        if(permissions!=null&&!permissions.isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public boolean isExisted(Long functionId) {
        return getById(functionId)!=null;
    }

    @Override
    public boolean isAvailable(Permission permission) {
        if(permission==null)
            throw new BaseSystemException(PermissionBizError.DATA_NULL);
        if(permission.getId()!=null){
            if(!isExisted(permission.getId()))
                throw new BaseSystemException(PermissionBizError.NOT_EXIST);
        }
        List<Permission> permissionSameCode=getByCode(permission.getCode());
        if(permissionSameCode==null||permissionSameCode.isEmpty())
            return true;
        if(permission.getId()==null)
            return false;
        return !permissionSameCode.stream().anyMatch(tmp->!tmp.getId().equals(permission.getId()));
    }
}
