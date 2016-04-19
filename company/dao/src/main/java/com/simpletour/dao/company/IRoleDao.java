package com.simpletour.dao.company;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Role;

/**
 * 文件描述：角色权限关系DAO层接口
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/8 16:56
 * 备注说明：从simpletour-system项目迁移到com.simpletour.module项目的company模块当中，其中DAO底层的removeEntityById接口发生了变更。
 */
public interface IRoleDao extends IBaseDao {
    /**
     * 删除指定的角色权限关系实体
     *
     * @param role  角色权限关系实体
     * @return
     * @throws BaseSystemException
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-18 14:11
     * @remark      执行的是逻辑删除
     */
    void deleteRole(Role role) throws BaseSystemException;

    /**
     * 根据条件参数查询角色权限关系分页列表
     *
     * @param query 条件参数
     * @return
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-09 09:43
     * @remark      模糊查询，区分多租户
     */
    DomainPage<Role> getRolesPages(RoleQuery query);
}
