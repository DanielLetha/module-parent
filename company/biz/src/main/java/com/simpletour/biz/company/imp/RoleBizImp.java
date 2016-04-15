package com.simpletour.biz.company.imp;

import com.simpletour.biz.company.IRoleBiz;
import com.simpletour.biz.company.error.RoleBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.error.DefaultError;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IRoleDao;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 文件描述：角色权限关系业务层实现类
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/8 16:56
 * 备注说明：null
 */
@Component
public class RoleBizImp implements IRoleBiz {
    @Autowired
    private IRoleDao roleDao;

    @Override
    public Long getTenantId() {
        return 1l;//roleDao.getTenantId();
    }

    @Override
    public void validateRole(Role role) throws BaseSystemException {
        if (null == role) {
            throw new BaseSystemException(RoleBizError.EMPTY_ENTITY);
        }

        String name = role.getName();
        if (null == name || name.isEmpty()) {
            throw new BaseSystemException(RoleBizError.ROLE_NAME_IS_EMPTY);
        }

        int len = name.length();
        if (MIN_NAME_LEN > len || MAX_NAME_LEN < len) {
            throw new BaseSystemException(RoleBizError.ROLE_NAME_LEN_INVALID);
        }

        String remark = role.getRemark();
        if (null != remark && MAX_REMARK_LEN < remark.length()) {
            throw new BaseSystemException(RoleBizError.ROLE_REMARK_LEN_INVALID);
        }

        validateTenantId(role);

        Long roleId = role.getId();
        Optional<Role> roleOptional = getRoleByName(name);
        if (roleOptional.isPresent() && (null == roleId || !roleId.equals(roleOptional.get().getId()))) {
            throw new BaseSystemException(RoleBizError.UNIQUE_ROLE_NAME_IN_SAME_COMPANY);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Role> saveRole(Role role) throws BaseSystemException {
        try {
            return Optional.ofNullable(roleDao.save(role));
        } catch (Exception exc) {
            exc.printStackTrace();
            throw new BaseSystemException(exc.getMessage(), null == role.getId() ? RoleBizError.ADD_FAILD : RoleBizError.UPDATE_FAILD);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteRoleById(Long id) throws BaseSystemException {
        try {
            roleDao.removeEntityById(Role.class, id);
        } catch (Exception e) {
            throw new BaseSystemException(e.getMessage(), RoleBizError.DELETE_FAILD);
        }
    }

    @Override
    public List<Role> getRolesList(RoleQuery query) {
        return roleDao.getEntitiesByFieldList(Role.class, fillQueryConditions(query));
    }

    @Override
    public DomainPage<Role> getRolesPages(RoleQuery query) {
        return roleDao.getRolesPages(query);
    }

    @Override
    public Optional<Role> getRoleById(Long id) throws BaseSystemException {
        if (null == id || 0L >= id) {
            throw new BaseSystemException(RoleBizError.ID_NULL);
        }

        Role role = roleDao.getEntityById(Role.class, id);
        if (null != role) {
            validateTenantId(role);
        }

        return Optional.ofNullable(role);
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        Map<String, Object> conditions = new HashMap<>(2);
        conditions.put("name", name);

        Long tenantId = getTenantId();
        if (null != tenantId) {
            conditions.put("company.id", tenantId);
        }

        List<Role> roles = roleDao.getEntitiesByFieldList(Role.class, conditions);
        return null == roles || roles.isEmpty() ? Optional.empty() : Optional.of(roles.get(0));
    }

    private void validateTenantId(Role role) {
        Long tenantId = getTenantId();
        if (null == tenantId) {
            throw new BaseSystemException(RoleBizError.INVALID_TENANT_ID);
        }

        Long companyId;
        Company company = role.getCompany();
        if (null == company || null == (companyId = company.getId()) || null == roleDao.getEntityById(Company.class, companyId)) {
            throw new BaseSystemException(RoleBizError.INVALID_COMPANY);
        }

        if (!tenantId.equals(companyId)) {
            throw new BaseSystemException(DefaultError.NOT_SAME_TENANT_ID);
        }
    }

    private Map<String, Object> fillQueryConditions(RoleQuery query) {
        if (null == query) {
            query = new RoleQuery();
        }

        Map<String, Object> conditions = new HashMap<>(3);

        String name = query.getName();
        if (null != name && !name.isEmpty()) {
            conditions.put("name", name);
        }

        conditions.put("type", query.getType());

        Long tenantId = getTenantId();
        if (null != tenantId) {
            conditions.put("company.id", tenantId);
        }

        return conditions;
    }
}
