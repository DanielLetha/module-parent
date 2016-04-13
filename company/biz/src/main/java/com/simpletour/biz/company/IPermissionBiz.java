package com.simpletour.biz.company;

import com.simpletour.domain.company.Permission;

import java.util.List;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/9
 * Time: 14:38
 */
public interface IPermissionBiz {

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    Permission getById(Long id);

    /**
     * 根据code获得权限（code是不重复的）
     */
    List<Permission> getByCode(String permissionCode);

    /**
     * 根据code判断code是否存在。
     * @param permissionCode
     * @return
     */
    Boolean isCodeExist(String permissionCode);

    /**
     * 判断权限是否可用
     * @param permission
     * @return
     */
    boolean isAvailable(Permission permission);

    /**
     * 判断id是否存在
     * @param functionId
     * @return
     */
    boolean isExisted(Long functionId);
}
