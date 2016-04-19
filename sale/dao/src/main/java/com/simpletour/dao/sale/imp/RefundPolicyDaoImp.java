package com.simpletour.dao.sale.imp;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.jpa.DependencyHandleDAO;
import com.simpletour.dao.sale.IRefundPolicyDao;
import org.springframework.stereotype.Component;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 15:05
 */
@Component
public class RefundPolicyDaoImp extends DependencyHandleDAO implements IRefundPolicyDao, IBaseDao {
}
