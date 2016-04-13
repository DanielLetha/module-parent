package com.simpletour.service.company;

import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.domain.company.Administrator;

import java.util.Optional;

/**
 * 超级管理员服务层接口
 * User: Hawk
 * Date: 2016/4/7 - 9:43
 */
public interface IAdministratorService {

    /**
     * 根据工号查询管理员
     * @param jobNo 工号
     * @return
     */
    Optional<Administrator> findAdminByJobNo(String jobNo) throws BaseSystemException;

    /**
     * 更新管理员，仅仅只更新密码
     * @param jobNo    工号
     * @param password 密码
     * @return
     */
    Optional<Administrator> updateAdminPassword(String jobNo, String password) throws BaseSystemException;

}
