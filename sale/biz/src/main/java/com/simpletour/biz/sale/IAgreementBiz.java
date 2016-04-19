package com.simpletour.biz.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.sale.Agreement;

import java.util.List;
import java.util.Map;

/**
 * 销售协议Biz接口
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/19.
 *
 * @since 2.0-SNAPSHOT
 */
public interface IAgreementBiz {
    /**
     * 查询销售协议分页
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 16:27
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        当前页数
     * @param pageSize         一页栏目数量
     * @param byLike           是否模糊查询
     * @return 销售协议分页
     * @since 2.0-SNAPSHOT
     */
    DomainPage<Agreement> findAgreementByCondition(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 根据query对象查询销售协议分页
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 17:46
     *
     * @param query 查询对象
     * @return 销售协议分页
     * @since 2.0-SNAPSHOT
     */
    DomainPage<Agreement> findAgreementByQuery(ConditionOrderByQuery query);

    /**
     * 根据query对象获取销售协议列表
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 17:47
     *
     * @param query 查询对象
     * @return 销售协议列表
     * @since 2.0-SNAPSHOT
     */
    List<Agreement> getAgreementListByQuery(ConditionOrderByQuery query);

    /**
     * 根据ID查询销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 16:27
     *
     * @param id 销售协议ID
     * @return 销售协议实体
     * @since 2.0-SNAPSHOT
     */
    Agreement getAgreementById(Long id);

    /**
     * 新增销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 16:27
     *
     * @param agreement 待新增销售协议对象
     * @return 销售协议实体
     * @since 2.0-SNAPSHOT
     */
    Agreement addAgreement(Agreement agreement);

    /**
     * 更新销售协议
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 16:27
     *
     * @param agreement 待更新销售协议对象
     * @return 销售协议实体
     * @since 2.0-SNAPSHOT
     */
    Agreement updateAgreement(Agreement agreement);

    /**
     * 更新销售协议状态
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 16:27
     *
     * @param agreement 待更新销售协议对象
     * @return 销售协议实体
     * @since 2.0-SNAPSHOT
     */
    Agreement updateStatus(Agreement agreement);

    /**
     * 根据ID判断销售协议是否存在
     * <p>
     * Author: YY/yuanyuan@simpletour.com
     * Time:   2016-04-19 16:27
     *
     * @param id 销售协议ID
     * @return true/false
     * @since 2.0-SNAPSHOT
     */
    Boolean isExist(Long id);
}
