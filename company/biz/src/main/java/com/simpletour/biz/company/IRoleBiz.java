package com.simpletour.biz.company;

import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Role;

import java.util.List;
import java.util.Optional;

/**
 * 文件描述：角色权限关系业务层接口
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/8 16:56
 * 备注说明：null
 */
public interface IRoleBiz {
    int MIN_NAME_LEN = 2;

    int MAX_NAME_LEN = 20;

    int MAX_REMARK_LEN = 10000;

    /**
     * 获取当前角色权限关系的租户ID
     *
     * @return      租户ID
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-08 11:43
     * @remark      null
     */
    Long getTenantId();

    /**
     * 校验角色权限关系信息是否有效
     *
     * @param role   角色权限关系实体对象
     * @return       角色权限关系实体对象
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-08 11:43
     * @remark      区分多租户，仅检查角色自身字段数据的有效性，对于其包含的模块和功能等数据的有效性则通过调用它们对应模块的biz层接口来进行检查
     */
    void validateRole(Role role) throws BaseSystemException;

    /**
     * 保存角色权限关系
     *
     * @param role   角色权限关系实体对象
     * @return       角色权限关系实体对象
     *
     * @author      shiguanglu@simpletour.com
     * @since       2.0-SNAPSHOT
     * @date        2016-04-08 11:43
     * @remark      区分多租户
     */
    Optional<Role> saveRole(Role role) throws BaseSystemException;

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
    Optional<Role> getRoleById(Long id) throws BaseSystemException;

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
