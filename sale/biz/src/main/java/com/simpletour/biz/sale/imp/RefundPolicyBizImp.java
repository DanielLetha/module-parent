package com.simpletour.biz.sale.imp;

import com.simpletour.biz.sale.IRefundPolicyBiz;
import com.simpletour.biz.sale.error.RefundPolicyBizError;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.IRefundPolicyDao;
import com.simpletour.dao.sale.IRefundRuleDao;
import com.simpletour.domain.sale.RefundPolicy;
import com.simpletour.domain.sale.RefundRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 16:07
 */
@Component
public class RefundPolicyBizImp implements IRefundPolicyBiz {
    @Autowired
    private IRefundPolicyDao refundPolicyDao;
    @Autowired
    private IRefundRuleDao refundRuleDao;


    private RefundPolicy findRefundPolicyByName(String name){
        List<RefundPolicy> refundPolicies=refundPolicyDao.getEntitiesByField(RefundPolicy.class,"name",name);
        if(refundPolicies==null||refundPolicies.isEmpty())
            return null;
        if(refundPolicies.size()!=1)
            throw new BaseSystemException(RefundPolicyBizError.REFUND_POLICY_DATA_ERROR);
        return refundPolicies.get(0);
    }

    @Override
    public RefundPolicy addRefundPolicy(RefundPolicy refundPolicy) {
        RefundPolicy refundPolicyExisted=findRefundPolicyByName(refundPolicy.getName());
        if(refundPolicyExisted!=null)
            throw new BaseSystemException(RefundPolicyBizError.REFUND_POLICY_NAME_EXIST);
        if(!isRefundRuleListAvailable(refundPolicy.getRefundRules()))
            throw new BaseSystemException(RefundPolicyBizError.REFUND_POLICY_REFUND_RULE_ERROR);
        refundPolicy.getRefundRules().forEach(tmp->{
            if (tmp.getRefundPolicy()==null)
                tmp.setRefundPolicy(refundPolicy);
        });
        return refundPolicyDao.save(refundPolicy);
    }

    @Override
    public RefundPolicy updateRefundPolicy(RefundPolicy refundPolicy) {
        RefundPolicy refundPolicyExisted=findRefundPolicyByName(refundPolicy.getName());
        if(refundPolicyExisted!=null&&refundPolicyExisted.getId()!=refundPolicy.getId())
            throw new BaseSystemException(RefundPolicyBizError.REFUND_POLICY_NAME_EXIST);
        if(!isRefundRuleListAvailable(refundPolicy.getRefundRules()))
            throw new BaseSystemException(RefundPolicyBizError.REFUND_POLICY_REFUND_RULE_ERROR);
        return refundPolicyDao.save(refundPolicy);
    }

    @Override
    public RefundPolicy getRefundPolicyById(long id) {
        return refundPolicyDao.getEntityById(RefundPolicy.class,id);
    }

    @Override
    public void deleteRefundPolicy(long id) {
        RefundPolicy refundPolicy=getRefundPolicyById(id);
        refundPolicyDao.remove(refundPolicy);
    }

    @Override
    public DomainPage findRefundPolicyPage(ConditionOrderByQuery query) {
        return refundPolicyDao.getEntitiesPagesByQuery(RefundPolicy.class,query);
    }

    @Override
    public List<RefundPolicy> findRefundPolicyList(ConditionOrderByQuery query) {
        return refundPolicyDao.getEntitiesByQuery(RefundPolicy.class,query);
    }

    @Override
    public boolean isRefundPolicyExisted(long id) {
        RefundPolicy refundPolicy=getRefundPolicyById(id);
        return refundPolicy!=null;
    }

    private boolean isRefundRuleListAvailable(List<RefundRule> refundRules) {
        for(int i=1;i<refundRules.size();i++){
            if (refundRules.get(i-1).getTiming()>=refundRules.get(i).getTiming())
                return false;
        }
        return true;
    }
}
