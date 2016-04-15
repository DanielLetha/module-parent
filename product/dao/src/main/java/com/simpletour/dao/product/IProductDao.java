package com.simpletour.dao.product;



import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.product.Product;

import java.util.List;

/**
 * 旅行资源Dao接口
 * <p>
 * Created by songfujie on 15/10/22.
 */
public interface IProductDao extends IBaseDao {

    /**
     * 根据条件查询Tourism
     *
     * @param conditions 支持的字段
     *                   name
     *                   depart
     *                   arrive
     *                   days
     *                   isOnline
     * @param page
     * @param pageSize
     * @return
     */
    // 推荐使用此接口
    DomainPage<Product> findProductByConditionPage(Condition conditions, int page, int pageSize);

    /**
     * 不分页，根据条件查询Tourism
     *
     * @param conditions
     * @return
     */
    List<Product> findProductByCondition(Condition conditions);
}

