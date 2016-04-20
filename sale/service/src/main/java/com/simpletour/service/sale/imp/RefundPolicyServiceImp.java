package com.simpletour.service.sale.imp;

import com.simpletour.biz.sale.IRefundPolicyBiz;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.RefundPolicy;
import com.simpletour.domain.sale.RefundRule;
import com.simpletour.service.sale.IRefundPolicyService;
import com.simpletour.service.sale.error.RefundPolicyServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 17:29
 */
@Service
public class RefundPolicyServiceImp implements IRefundPolicyService {
    @Autowired
    private IRefundPolicyBiz refundPolicyBiz;

    //判断退款细则是否重复
    private boolean isRuleAvaliable(List<RefundRule> refundRules){
        for(int i=1;i<refundRules.size();i++){
            if (refundRules.get(i-1).getTiming()>=refundRules.get(i).getTiming())
                return false;
        }
        return true;
    }

    @Override
    public Optional<RefundPolicy> addRefundPolicy(RefundPolicy refundPolicy) {
        if(refundPolicy==null)
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_NULL);
        if(refundPolicy.getId()!=null)
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_DATA_ERROR);
        if(refundPolicy.getRefundRules()==null||refundPolicy.getRefundRules().isEmpty())
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_NULL);
        if(refundPolicy.getRefundRules().stream().anyMatch(tmp->tmp.getId()!=null))
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_DATA_ERROR);
        if(!isRuleAvaliable(refundPolicy.getRefundRules()))
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_ERROR);

        refundPolicy.getRefundRules().forEach(tmp->{
            if (tmp.getRefundPolicy()==null)
                tmp.setRefundPolicy(refundPolicy);
        });

        return Optional.ofNullable(refundPolicyBiz.addRefundPolicy(refundPolicy));
    }

    @Override
    public Optional<RefundPolicy> updateRefundPolicy(RefundPolicy refundPolicy) {
        if(refundPolicy==null)
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_NULL);
        if(refundPolicy.getId()==null)
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_DATA_ERROR);
        if(!refundPolicyBiz.isRefundPolicyExisted(refundPolicy.getId()))
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_NOT_EXIST);
        if(refundPolicy.getRefundRules()==null||refundPolicy.getRefundRules().isEmpty())
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_NULL);
        if(!isRuleAvaliable(refundPolicy.getRefundRules()))
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_ERROR);

        refundPolicy.getRefundRules().forEach(tmp->{
            if (tmp.getId()!=null&&!refundPolicyBiz.isRefundRuleExisted(tmp.getId())){
                throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_REFUND_RULE_NOT_EXIST);
            }
        });
        refundPolicy.getRefundRules().forEach(tmp->{
            if (tmp.getRefundPolicy()==null)
                tmp.setRefundPolicy(refundPolicy);
        });
        return Optional.ofNullable(refundPolicyBiz.updateRefundPolicy(refundPolicy));
    }

    @Override
    public Optional<RefundPolicy> getRefundPolicyById(Long id) {
        if(id==null)
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_NULL);
        return Optional.ofNullable(refundPolicyBiz.getRefundPolicyById(id));
    }

    @Override
    public void deleteRefundPolicy(Long id) {
        if(id==null)
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_NULL);
        if(!refundPolicyBiz.isRefundPolicyExisted(id))
            throw new BaseSystemException(RefundPolicyServiceError.REFUND_POLICY_NOT_EXIST);
        refundPolicyBiz.deleteRefundPolicy(id);
    }

    @Override
    public DomainPage findRefundPolicyPage(ConditionOrderByQuery query) {
        return refundPolicyBiz.findRefundPolicyPage(query);
    }

    @Override
    public List<RefundPolicy> findRefundPolicyList(ConditionOrderByQuery query) {
        return refundPolicyBiz.findRefundPolicyList(query);
    }
}
