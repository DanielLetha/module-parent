package com.simpletour.dao.sale.imp;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.jpa.DependencyHandleDAO;
import com.simpletour.dao.sale.ISaleAppDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zt on 15/10/19.
 */
@Repository
@Transactional
public class SaleAppDaoImp extends DependencyHandleDAO implements ISaleAppDao, IBaseDao {

}
