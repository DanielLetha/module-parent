package com.simpletour.service.company.imp;

import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.IPermissionDao;
import com.simpletour.domain.company.Permission;
import com.simpletour.service.company.IPermissionService;
import com.simpletour.service.company.error.PermissionServiceError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by liangfei on 2015/11/21.
 * Modified by XuHui on 2015/12/03.
 */
@Service
public class PermissionServiceImp implements IPermissionService {

    static final int DEFAULT_DESTINATION_PAGE_SIZE = 10;

    @Resource
    private IPermissionDao permissionDao;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Permission> addPermission(Permission permission) {
        verifyPermission4Save(permission, Boolean.FALSE);
        try {
            return Optional.ofNullable(permissionDao.save(permission));
        } catch (Exception e) {
            throw new BaseSystemException(e.getMessage(), PermissionServiceError.ADD_FAILD);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deletePermission(Long id) throws BaseSystemException {
        if (id == null || id <= 0L)
            throw new BaseSystemException(PermissionServiceError.ID_NULL);
        //fixed by xuhui:判断是否存在依赖关系，并使用逻辑删除；
        Optional<Permission> permission = Optional.ofNullable(permissionDao.getEntityById(Permission.class, id));
        if (!permission.isPresent())
            throw new BaseSystemException(PermissionServiceError.DATA_NULL);
        if (permission.get().getRoleList() != null && !permission.get().getRoleList().isEmpty()
                && permission.get().getRoleList().stream().anyMatch(temp ->!temp.getDel()))
            throw new BaseSystemException(PermissionServiceError.CANNOT_DEL_DEPENDENT_ROLE);

        try {
            permissionDao.removeEntityById(Permission.class, id, true);
        } catch (Exception e) {
            throw new BaseSystemException(e.getMessage(), PermissionServiceError.DELETE_FAILD);
        }
    }

    @Override
    public Optional<Permission> getById(Long id) {
        return Optional.ofNullable(permissionDao.getEntityById(Permission.class, id));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Permission> updatePermissione(Permission permission) {
        verifyPermission4Save(permission, Boolean.TRUE);
        try {
            return Optional.ofNullable(permissionDao.save(permission));
        } catch (Exception e) {
            throw new BaseSystemException(e.getMessage(), PermissionServiceError.UPDATE_FAILD);
        }
    }

    @Override
    public Optional<Permission> getByCode(String permissionCode) {
        List<Permission> permissionList = permissionDao.getEntitiesByField(Permission.class, "code", permissionCode);
        return Optional.ofNullable(permissionList != null && permissionList.size() == 1 ? permissionList.get(0) : null);
    }

    @Override
    public Boolean isCodeExist(String permissionCode) {
        Optional<Permission> permission = getByCode(permissionCode);
        return permission.isPresent();
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionDao.getAllEntities(Permission.class);
    }

    @Override
    public List<Permission> getPermissionsByName(String name) {
        return permissionDao.getEntitiesByField(Permission.class, "name", name);
    }

    @Override
    public DomainPage<Permission> getPermissionsByPage(int page) {
        return getPermissionsByPage(page, DEFAULT_DESTINATION_PAGE_SIZE);
    }

    @Override
    public DomainPage<Permission> getPermissionsByPage(int page, int pageSize) {
        return queryPermissionsPages(null, null, page, pageSize);
    }

    @Override
    public DomainPage<Permission> queryPermissionsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return permissionDao.queryEntitiesPagesByFieldList(Permission.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public DomainPage<Permission> queryPermissionsPages(String name, String code, int pageIndex) {
        return queryPermissionsPages(name, code, pageIndex, DEFAULT_DESTINATION_PAGE_SIZE);
    }

    @Override
    public DomainPage<Permission> queryPermissionsPages(String name, String code, int pageIndex, int pageSize) {
        return queryPermissionsPages(name, code, "id", IBaseDao.SortBy.DESC, pageIndex, pageSize);
    }

    @Override
    public DomainPage<Permission> queryPermissionsPages(String name, String code, String orderByFiledName, IBaseDao.SortBy sortBy, int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (name != null && !name.equals("")) map.put("name", name);
        if (code != null && !code.equals("")) map.put("code", code);
        //fixed by:xuhui 使用逻辑删除
        map.put("del", false);
        return queryPermissionsPagesByConditions(map, orderByFiledName, sortBy, pageIndex, pageSize, true);
    }

    /**
     * 角色权限项对象的数据合法性检查（保存前）
     * @param permission
     * @param isUpdate true-更新  false-添加
     * Created by zt on 16/03/23.
     */
    private void verifyPermission4Save(Permission permission, boolean isUpdate) {
        if (permission == null)
            throw new BaseSystemException(PermissionServiceError.EMPTY_ENTITY);
        if (isUpdate) {
            if (permission.getId() == null && permission.getId() <= 0L)
                throw new BaseSystemException(PermissionServiceError.ID_NULL);
        }
        if (permission.getName() == null || "".equals(permission.getName().trim()))
            throw new BaseSystemException(PermissionServiceError.NAME_NULL);

        if (permission.getCode() == null || "".equals(permission.getCode().trim()))
            throw new BaseSystemException(PermissionServiceError.CODE_NULL);
        Optional<Permission> perm = getByCode(permission.getCode());
        if (!isUpdate){
            if (perm.isPresent()){
                throw new BaseSystemException(PermissionServiceError.CODE_EXIST);
            }
        }else {
            if (perm.isPresent() && !permission.getId().equals(perm.get().getId())){
                throw new BaseSystemException(PermissionServiceError.CODE_EXIST);
            }
        }
    }

    @Override
    public boolean isExisted(Long functionId) {
        return getById(functionId).isPresent();
    }

    @Override
    public boolean isAvailable(Permission permission) {
        if(permission==null)
            throw new BaseSystemException(PermissionServiceError.DATA_NULL);
        if(permission.getId()!=null){
            if(!isExisted(permission.getId()))
                throw new BaseSystemException(PermissionServiceError.NOT_EXIST);
        }
        Optional<Permission> permissionSameCode=getByCode(permission.getCode());
        if(!permissionSameCode.isPresent())
            return true;
        if(permission.getId()==null)
            return false;
        if(permissionSameCode.get().getId().equals(permission.getId()))
            return true;
        return false;
    }
}
