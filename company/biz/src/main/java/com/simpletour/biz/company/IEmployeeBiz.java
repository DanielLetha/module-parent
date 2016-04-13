package com.simpletour.biz.company;

import com.simpletour.common.core.dao.query.ConditionOrderByQuery;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.domain.company.Employee;

import java.util.Optional;

/**
 * Created by Mario on 2016/4/8.
 */
public interface IEmployeeBiz {

    /**
     * 存入employee信息
     *
     * @param employee employee实体对象
     * @return employee实体对象
     * @author mth@simpletour.com
     * @remark 只针对业务系统, employee中的isAmin字段必须为false才可以添加
     * @since 2.0
     */
    Optional<Employee> addEmployee(Employee employee);

    /**
     * 根据id删除employee实体对象
     *
     * @param id 主键id
     * @author mth@simpletour.com
     */
    void deleteEmployee(Long id);

    /**
     * 更新employee信息
     *
     * @param employee employee实体对象
     * @return employee 实体对象
     * @author mth@simpletour.com
     * @remark 只针对业务系统, employee中的isAmin字段必须为false才可以添加
     * @since 2.0
     */
    Optional<Employee> updateEmployee(Employee employee);

    /**
     * 修改人员密码
     *
     * @param id       人员实体主键id
     * @param password 密码
     * @param salt     盐
     * @return 更新密码后的人员实体
     * @author mth@simpletour.com
     * @since 2.0
     */
    Optional<Employee> changePwdForEmployee(Long id, String password, String salt);

    /**
     * 根据id查询employee实体
     *
     * @param id 主键id
     * @return employee实体对象
     * @author mth@simpletour.com
     * @since 2.0
     */
    Optional<Employee> queryEmployeeById(Long id);

    /**
     * 根据jobNo查询用户
     *
     * @param jobNo jobNo
     * @return employee实体
     * @author mth@simpletour.com
     * @since 2.0
     */
    Optional<Employee> queryEmployeeByJobNo(Integer jobNo);

    /**
     * 根据查询条件查询employee分页列表
     *
     * @param conditionOrderByQuery 查询条件
     * @return employee分页列表
     * @author mth@simpletour.com
     * @since 2.0
     */
    DomainPage<Employee> queryEmployeesPagesByConditions(ConditionOrderByQuery conditionOrderByQuery);

}
