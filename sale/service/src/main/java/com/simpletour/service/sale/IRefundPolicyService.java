package com.simpletour.service.sale;

import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.sale.RefundPolicy;

import java.util.List;
import java.util.Optional;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/19
 * Time: 17:20
 */
public interface IRefundPolicyService {

    /**
     * 添加退款规则
     * @param refundPolicy
     * @return
     */
    Optional<RefundPolicy> addRefundPolicy(RefundPolicy refundPolicy);

    /**
     * 更新退款规则
     * @param refundPolicy
     * @return
     */
    Optional<RefundPolicy> updateRefundPolicy(RefundPolicy refundPolicy);

    /**
     * 根据id获取退款规则
     * @param id
     * @return
     */
    Optional<RefundPolicy> getRefundPolicyById(Long id);

    /**
     * 根据id删除退款规则
     * @param id
     */
    void deleteRefundPolicy(Long id);

    /**
     * 根据Query查询退款规则分页
     * @param query
     * @return
     */
    DomainPage findRefundPolicyPage(ConditionOrderByQuery query);

    /**
     * 根据Query查询退款规则List
     * @param query
     * @return
     */
    List<RefundPolicy> findRefundPolicyList(ConditionOrderByQuery query);
}
