package com.simpletour.biz.sale;

import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.sale.RefundPolicy;

import java.util.List;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 15:07
 */
public interface IRefundPolicyBiz {

    /**
     * 添加退款规则模板
     * 校验是否存在重名：throw RefundPolicyBizError.REFUND_POLICY_NAME_EXIST
     * @param refundPolicy
     * @return
     */
    RefundPolicy addRefundPolicy(RefundPolicy refundPolicy);

    /**
     * 更新退款规则模板
     * 校验是否存在重名：throw RefundPolicyBizError.REFUND_POLICY_NAME_EXIST
     * @param refundPolicy
     * @return
     */
    RefundPolicy updateRefundPolicy(RefundPolicy refundPolicy);

    /**
     * 根据id查询退款规则模板
     * @param id
     * @return
     */
    RefundPolicy getRefundPolicyById(long id);

    /**
     * 根据id删除退款规则模板
     * @param id
     */
    void deleteRefundPolicy(long id);

    /**
     * 根据query对象查询退款规则模板分页
     * @return
     */
    DomainPage findRefundPolicyPage(ConditionOrderByQuery query);

    /**
     * 根据query对象查询退款规则模板列表
     * @return
     */
    List<RefundPolicy> findRefundPolicyList(ConditionOrderByQuery query);

    /**
     * 根据id判断退款模板规则模板是否存在
     * @param id
     * @return
     */
    boolean isRefundPolicyExisted(long id);

    /**
     * 根据id判断退款细则是否存在
     * @param id
     * @return
     */
    boolean isRefundRuleExisted(long id);
}
