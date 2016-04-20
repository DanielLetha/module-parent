package com.simpletour.service.product;


import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.product.Product;

import java.util.List;
import java.util.Optional;

/**
 * 产品行程模块业务处理接口
 * <p>
 * Created by songfujie on 15/10/22.
 */
public interface IProductService {

    //Product

//    /**
//     * 添加一个Product， product.productPackages需要已赋值
//     * productPackages需要严格按照显示顺序(时间顺序传入)
//     *
//     * @param product
//     * @return
//     */
//    Optional<Product> addProduct(Product product);
//
//    /**
//     * 根据id删除Product，同时会删除Product关联的ProductPackage
//     *
//     * @param id
//     */
//    void deleteProductById(Long id);
//
//    /**
//     * 更新Product
//     *
//     * @param product
//     * @return
//     */
//    Optional<Product> updateProduct(Product product);

    /**
     * 根据id获取Product
     * 返回的Product包含ProductPackage
     *
     * @param id
     * @return
     */
    Optional<Product> getProductById(Long id);

    /**
     * 按条件分页查询行程
     *
     * @param conditions 支持的字段      类型      说明
     *                   name           String  行程名
     *                   depart         String  出发地点
     *                   arrive         String  到达地点
     *                   days           num     行程包含的天数
     *                   isOnline       bool    是否上线
     *                   type           Product.Type 类型
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<Product> getProductByConditionPage(Condition conditions, int page, int pageSize);

    List<Product> getProductByCondition(Condition conditions);



    // stock

//    /**
//     * 查询一个Tourism 在day这一天的剩余库存
//     *
//     * @param tourismRoutes
//     * @param day
//     * @return
//     */
//    Integer findTourismBusNoPlanCapacity(List<TourismRoute> tourismRoutes, Date day);
//
//    /**
//     * 查询一个Tourism 在start-end时间段内每一天的剩余库存
//     *
//     * @param tourismRoutes
//     * @param start
//     * @param end
//     * @return
//     */
//    Map<Date, Integer> findTourismBusNoPlanCapacity(List<TourismRoute> tourismRoutes, Date start, Date end);
//
//    /**
//     * 查询一个Tourism 在day这一天的剩余库存
//     *
//     * @param product
//     * @param day
//     * @return
//     */
//    Integer findTourismBusNoPlanCapacity(Product product, Date day);
//
//    /**
//     * 查询一个Tourism 在start-end时间段内每一天的剩余库存
//     *
//     * @param product
//     * @param start
//     * @param end
//     * @return
//     */
//    Map<Date, Integer> findTourismBusNoPlanCapacity(Product product, Date start, Date end);
//
//
//    /**
//     * 查询一个Tourism 在day这一天的剩余库存
//     *
//     * @param productId
//     * @param day
//     * @return
//     */
//    Integer findTourismBusNoPlanCapacity(Long productId, Date day);
//
//    /**
//     * 查询一个Tourism 在start-end时间段内每一天的剩余库存
//     *
//     * @param productId
//     * @param start
//     * @param end
//     * @return
//     */
//    Map<Date, Integer> findTourismBusNoPlanCapacity(Long productId, Date start, Date end);
//
//    /**
//     * 获取产品的库存
//     * 当产品包含车次时，会根据卖出的凭证数和车次排班情况纠正库存数
//     *
//     * @param product
//     * @param day
//     * @param isOnline null 上下线都包含, true 上线, false 请不要传false！！！
//     * @return
//     */
//    Optional<Stock> getStock(Product product, Date day, Boolean isOnline);
//
//    /**
//     * 获取产品的库存
//     *
//     * @param product
//     * @param startDay
//     * @param endDay
//     * @param isOnline null 上下线都包含, true 上线, false 请不要传false！！！
//     * @return
//     */
//    List<Stock> getStocks(Product product, Date startDay, Date endDay, Boolean isOnline);
}



