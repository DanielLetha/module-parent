package com.simpletour.service.order;

import com.simpletour.domain.order.Order;

import java.util.Optional;

/**
 * Created by Mario on 2016/4/21.
 */
public interface IOrderService {
    /**
     * 添加订单
     *
     * @param order 订单实体
     * @return
     */
    Optional<Order> addOrder(Order order);
}
