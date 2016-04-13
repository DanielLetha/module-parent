package com.simpletour.service.company;

import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Role;

import java.util.List;
import java.util.Optional;

/**
 * 文件描述：角色权限关系服务层接口
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/8 16:56
 * 备注说明：null
 */
public interface IRoleService {
    /**
     * 添加角色权限关系
     *
     * @param role   角色权限关系实体对象
     * @return       角色权限关系实体对象
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-08 11:43
     * @remark      区分多租户
     */
    Optional<Role> addRole(Role role) throws BaseSystemException;

    /**
     * 编辑角色权限关系
     *
     * @param role   角色权限关系实体对象
     * @return       角色权限关系实体对象
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-08 11:43
     * @remark      区分多租户
     */
    Optional<Role> updateRole(Role role) throws BaseSystemException;

    /**
     * 根据ID删除角色权限关系
     *
     * @param id   角色ID
     * @return
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-08 11:43
     * @remark      区分多租户
     * @throws      BaseSystemException
     */
    void deleteRoleById(Long id) throws BaseSystemException;

    /**
     * 根据条件参数查询角色权限关系列表
     *
     * @param query 条件参数
     * @return
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-09 09:43
     * @remark      精确查询，区分多租户
     * @throws      BaseSystemException
     */
    List<Role> getRolesList(RoleQuery query);

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
     * @throws      BaseSystemException
     */
    DomainPage<Role> getRolesPages(RoleQuery query);

    /**
     * 根据ID获取角色权限关系实体对象
     *
     * @param id    角色ID
     * @return      角色权限关系实体对象
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-08 11:43
     * @remark      区分多租户
     */
    Optional<Role> getRoleById(Long id);

    /**
     * 根据角色名称获取角色权限关系实体对象
     *
     * @param name   角色名称
     * @return       角色权限关系实体对象
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-08 11:43
     * @remark      区分多租户
     */
    Optional<Role> getRoleByName(String name);
}
