package com.simpletour.service.company.imp;

import com.simpletour.biz.company.IModuleBiz;
import com.simpletour.biz.company.IRoleBiz;
import com.simpletour.biz.company.error.ModuleBizError;
import com.simpletour.biz.company.error.RoleBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.Role;
import com.simpletour.service.company.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 文件描述：角色权限关系服务层实现类
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/8 16:56
 * 备注说明：null
 */
@Service
public class RoleServiceImp implements IRoleService {
    @Autowired
    IRoleBiz roleBiz;

    @Autowired
    IModuleBiz moduleBiz;

    private void validateModules(Role role) {
        List<Permission> permissionsList = role.getPermissionList();
        if (null == permissionsList || permissionsList.isEmpty()) {
            throw new BaseSystemException(ModuleBizError.PERMISSION_NULL);
        }

        permissionsList.forEach(item -> {
            if (!moduleBiz.isPermissionExisted(item.getId())) {
                throw new BaseSystemException(ModuleBizError.PERMISSION_NOT_EXIST);
            }
        });
    }

    @Override
    public Optional<Role> addRole(Role role) throws BaseSystemException {
        roleBiz.validateRole(role);

        validateModules(role);

        return roleBiz.saveRole(role);
    }

    @Override
    public Optional<Role> updateRole(Role role) {
        if (null != role && null == role.getId()) {
            throw new BaseSystemException(RoleBizError.ID_NULL);
        }
        return addRole(role);
    }

    @Override
    public void deleteRoleById(Long id) throws BaseSystemException {
        if (!roleBiz.getRoleById(id).isPresent()) {
            throw new BaseSystemException(RoleBizError.NOT_EXISTING);
        }
        roleBiz.deleteRoleById(id);
    }

    @Override
    public List<Role> getRolesList(RoleQuery query) {
        return roleBiz.getRolesList(query);
    }

    @Override
    public DomainPage<Role> getRolesPages(RoleQuery query) {
        return roleBiz.getRolesPages(query);
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleBiz.getRoleById(id);
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        return roleBiz.getRoleByName(name);
    }
}
