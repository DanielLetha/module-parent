package com.simpletour.dao.product.imp.imp;

import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.dao.jpa.JPABaseDAO;
import com.simpletour.common.core.dao.query.condition.*;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.dao.product.IProductDao;
import com.simpletour.dao.product.query.ProductQuery;
import com.simpletour.domain.product.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by songfujie on 15/10/22.
 */
@Repository
public class ProductDaoImp extends JPABaseDAO implements IProductDao, IBaseDao {

    public DomainPage<Product> findProductByConditionPage(Condition conditions, int page, int pageSize) {
        conditions.setFieldMatcher(ProductQuery.fieldMatcher);
        return getEntitiesPagesByCondition(Product.class, conditions, page, pageSize);
    }

    public List<Product> findProductByCondition(Condition conditions) {
        conditions.setFieldMatcher(ProductQuery.fieldMatcher);
        return getEntitiesByCondition(Product.class, conditions);
    }
}

















