package com.simpletour.service.company;

import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.company.Employee;

import java.util.Optional;

/**
 * Created by zt on 2015/11/20.
 */
public interface IEmployeeService {
    /**
     * 添加后台用户对象
     *
     * @param employee
     * @return employee实体类
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
     * 修改一个后台用户相关数据
     *
     * @param employee employee实体类
     * @return employee实体类
     * @author mth@simpletourcom
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
     * 根据id查询后台用户
     *
     * @param id 人员id
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
     * 分页模糊查询
     *
     * @param conditionOrderByQuery 查询条件
     * @return employee实体对象的分页列表
     * @author mth@simpletour.com
     * @since 2.0
     */
    DomainPage<Employee> queryEmployeesPagesByConditions(ConditionOrderByQuery conditionOrderByQuery);


}
