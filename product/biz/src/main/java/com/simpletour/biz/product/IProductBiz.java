package com.simpletour.biz.product;


import com.simpletour.biz.product.imp.TourismBusAllocation;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.product.TourismRoute;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Product业务模块
 * <p>
 * Created by Jeff.Song on 2015/11/21.
 */
public interface IProductBiz {
    //Tourism

    /**
     * 判断产品是否存在
     * @param id
     * @return
     */
    boolean isExist(Long id);

    /**
     * 判断产品是否上线
     */
    boolean isOnline(Long id);

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
    DomainPage<Product> getProductByConditionPage(Condition conditions, int page, int pageSize);

    List<Product> getProductByCondition(Condition conditions);

    //Product

    /**
     * 添加一个Product
     *
     * @param product
     * @return
     */
    Product addProduct(Product product);

    /**
     * 根据id删除Product，同时会删除Product关联的ProductPackage
     *
     * @param id
     */
    void deleteProductById(Long id);

    /**
     * 更新Product, 会先删除原有的ProductPackage，再保存传入的productPackages
     *
     * @param product
     * @return
     */
    Product updateProduct(Product product);

    /**
     * 根据id获取Product
     * 返回的Product不包含ProductPackage
     *
     * @param id
     * @return
     */
    Product getProductById(Long id);

    /**
     * 根据条件分页查询Product
     *
     * @param conditions
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<Product> getProductByConditionsPage(Map<String, Object> conditions, int page, int pageSize);


    /**
     * 根据条件分页查询Product
     *
     * @param isOnline null，查询所有， true，查询上线产品， false，查询下线产品
     * @param name
     * @param isDel    null, 查询所有， true，查询已删除产品， false，查询未删除产品
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<Product> getProductByConditionsPage(Boolean isOnline, String name, Boolean isDel, int page, int pageSize);


    //stock

    /**
     * 查询一个tourism在start-end每一天的车位剩余有效库存（必须能够排车排进去）
     *
     * @param tourismRoutes
     * @param start
     * @param end
     * @return
     */
    Map<Date, Integer> findTourismBusNoPlanCapacity(List<TourismRoute> tourismRoutes, Date start, Date end);

    /**
     * 查询一个Tourism 在day这一天的剩余库存
     *
     * @param tourismRoutes
     * @param day
     * @return
     */
    Integer findTourismBusNoPlanCapacity(List<TourismRoute> tourismRoutes, Date day);

    /**
     * 查询一个Tourism 在day这一天的剩余库存
     *
     * @param tourism
     * @param day
     * @return
     */
    Integer findTourismBusNoPlanCapacity(Product tourism, Date day);


    /**
     * 查询一个Tourism 在day这一天的剩余库存
     *
     * @param tourismId
     * @param day
     * @return
     */
    Integer findTourismBusNoPlanCapacity(Long tourismId, Date day);

    /**
     * 根据行程，日期，订购数量，以及排车算法安排车辆
     *
     * @param tourism
     * @param day
     * @param quantity
     * @param allocateStrategy
     * @return
     */
    List<List<TourismBusAllocation.BusQuantity>> allocate(Product tourism, Date day, Integer quantity, TourismBusAllocation.AllocationStrategy allocateStrategy);
}




