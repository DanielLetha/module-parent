package com.simpletour.dao.sale.imp;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.jpa.DependencyHandleDAO;
import com.simpletour.dao.sale.IRefundPolicyDao;
import com.simpletour.dao.sale.IRefundRuleDao;
import org.springframework.stereotype.Component;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 17:44
 */
@Component
public class RefundRuleDaoImp extends DependencyHandleDAO implements IRefundRuleDao, IBaseDao {
}
