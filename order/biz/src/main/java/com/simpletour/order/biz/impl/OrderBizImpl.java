package com.simpletour.order.biz.impl;

import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.order.Item;
import com.simpletour.domain.order.Order;
import com.simpletour.order.biz.IOrderBiz;
import com.simpletour.order.biz.error.OrderBizError;
import com.smpletour.order.dao.IOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Mario on 2016/4/22.
 */
@Component
public class OrderBizImpl implements IOrderBiz {

    @Autowired
    private IOrderDao orderDao;

    @Override
    public Order addOrder(Order order) {
        if (order == null)
            throw new BaseSystemException(OrderBizError.ORDER_NULL);
        //设置订单价格
        order.setAmount(order.getItems().stream().mapToInt(Item::getTotalSettlement).sum());
        //设置订单状态
        order.setStatus(Order.Status.PENDING);
        return orderDao.save(order);
    }
}
