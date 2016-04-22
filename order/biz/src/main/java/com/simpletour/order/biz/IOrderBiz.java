package com.simpletour.order.biz;

import com.simpletour.domain.order.Order;

/**
 * Created by Mario on 2016/4/22.
 */
public interface IOrderBiz {
    /**
     * 添加订单
     * @param order
     * @return
     */
    Order addOrder(Order order);
}
