package com.simpletour.dao.company.imp;

import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.dao.company.ICompanyDao;
import com.simpletour.dao.company.support.JoinDao;
import org.springframework.stereotype.Repository;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/6.
 * Remark: companyDao实现
 */
@Repository
public class CompanyDaoImp extends JoinDao implements ICompanyDao, IBaseDao {

}
