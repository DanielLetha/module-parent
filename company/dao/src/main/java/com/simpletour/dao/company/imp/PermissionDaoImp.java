package com.simpletour.dao.company.imp;

import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.dao.jpa.JPABaseDAO;
import com.simpletour.dao.company.IPermissionDao;
import org.springframework.stereotype.Repository;

/**
 * Created by zt on 2015/11/19.
 */
@Repository
public class PermissionDaoImp extends JPABaseDAO implements IPermissionDao, IBaseDao {
}
