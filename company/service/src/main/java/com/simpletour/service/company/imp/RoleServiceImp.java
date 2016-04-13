package com.simpletour.service.company.imp;

import com.simpletour.biz.company.IModuleBiz;
import com.simpletour.biz.company.IPermissionBiz;
import com.simpletour.biz.company.IRoleBiz;
import com.simpletour.biz.company.error.RoleBizError;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.Role;
import com.simpletour.service.company.ICompanyService;
import com.simpletour.service.company.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    ICompanyService companyService;

    @Autowired
    IModuleBiz moduleBiz;

    @Autowired
    IPermissionBiz permissionBiz;

    private void validateModules(Role role) {
        List<Permission> permissionsList = role.getPermissionList();
        if (null == permissionsList || permissionsList.isEmpty()) {
            throw new BaseSystemException(RoleBizError.INVALID_PERMISSION);
        }

        permissionsList.forEach(item -> {
            if (!permissionBiz.isAvailable(item)) {
                throw new BaseSystemException(RoleBizError.INVALID_PERMISSION);
            }
            if (!moduleBiz.isAvailable(item.getModule())) {
                throw new BaseSystemException(RoleBizError.INVALID_MODULE);
            }
        });
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
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

        roleBiz.validateRole(role);

        validateModules(role);

        return roleBiz.saveRole(role);
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
