package com.simpletour.dao.company.query;

import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.dao.query.BaseQuery;

/**
 * 文件描述：角色查询条件封装类
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/7 12:03
 * 备注说明：null
 */
public class RoleQuery extends BaseQuery {
    /**
     * 角色名称
     */
    String name;

    /**
     * 模块名称
     */
    String module;

    /**
     * 功能名称
     */
    String permission;

    /**
     * 系统类型
     */
    Integer type = 0;

    public RoleQuery() {
    }

    public RoleQuery(String name, String module, String permission, Integer type) {
        this.name = name;
        this.module = module;
        this.permission = permission;
        this.type = null == type ? 0 : type;
    }

    public RoleQuery(String orderByFiledName, IBaseDao.SortBy orderBy, int pageSize, int pageIndex, String name, String module, String permission, Integer type) {
        super(orderByFiledName, orderBy, pageSize, pageIndex);
        this.name = name;
        this.module = module;
        this.permission = permission;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
