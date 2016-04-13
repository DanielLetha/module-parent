package com.simpletour.service.company.imp;

import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.IAdministratorDao;
import com.simpletour.domain.company.Administrator;
import com.simpletour.service.company.IAdministratorService;
import com.simpletour.service.company.error.AdministratorServiceError;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 超级管理员的对外提供的接口
 * User: Hawk
 * Date: 2016/4/7 - 9:43
 */
@Service
public class AdministratorServiceImp implements IAdministratorService {

    @Resource
    private IAdministratorDao administratorDao;

    @Override
    public Optional<Administrator> findAdminByJobNo(String jobNo) throws BaseSystemException {
        return Optional.ofNullable(administratorDao.getEntityByField(Administrator.class, "jobNo", jobNo));
    }

    @Override
    public Optional<Administrator> updateAdminPassword(String jobNo, String password) throws BaseSystemException {
        if (jobNo == null || jobNo.isEmpty() || password == null || password.isEmpty()) {
            throw new BaseSystemException(AdministratorServiceError.EMPTY_ENTITY);
        }
        Optional<Administrator> oriOpt = findAdminByJobNo(jobNo);
        if (!oriOpt.isPresent()) {
            throw new BaseSystemException(AdministratorServiceError.ADMIN_NOT_EXIST);
        }
        if (password.equals(oriOpt.get().getPassword())) {
            throw new BaseSystemException(AdministratorServiceError.PASSWORD_SAME_OLD);
        }
        Administrator newAdmin = oriOpt.get();
        newAdmin.setPassword(password);
        return Optional.ofNullable(administratorDao.save(newAdmin));
    }
}
