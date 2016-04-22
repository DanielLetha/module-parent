package com.simpletour.service.order.impl;

import com.simpletour.biz.sale.ISaleAppBiz;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.order.Order;
import com.simpletour.domain.sale.SaleApp;
import com.simpletour.order.biz.IItemBiz;
import com.simpletour.service.order.IOrderService;
import com.simpletour.service.order.error.OrderServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Mario on 2016/4/21.
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ISaleAppBiz saleAppBiz;

    @Autowired
    private IItemBiz itemBiz;

    /**
     * 校验订单关联的销售端是否存在,并设置订单关联的销售端
     *
     * @param order
     */
    private void validateSaleApp(Order order) {
        if (order.getSaleApp() == null || order.getSaleApp().getId() == null)
            throw new BaseSystemException(OrderServiceError.ORDER_SALEAPP_NULL);
        SaleApp saleAppOrigin = saleAppBiz.findSaleAppById(order.getSaleApp().getId());
        if (saleAppOrigin == null)
            throw new BaseSystemException(OrderServiceError.ORDER_SALEAPP_NOT_EXISTED);
        order.setSaleApp(saleAppOrigin);
    }

    /**
     * 校验订单项
     * 1.检查订单是否至少包含一个订单项
     * 2.将订单项和订单进行关联,防止前台传进来的数据没有进行关联造成jpa插入数据时异常
     *
     * @param order 订单实体
     * @return
     */
    private void validateItem(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty())
            throw new BaseSystemException(OrderServiceError.ORDER_ITEM_NULL);
        order.getItems().stream().forEach(item -> {
            item.setSaleApp(order.getSaleApp());
            item.setOrder(order);
            itemBiz.validateItem(item);
        });
    }


    @Override
    public Optional<Order> addOrder(Order order) {
        if (order == null)
            throw new BaseSystemException(OrderServiceError.ORDER_NULL);
        validateSaleApp(order);
        validateItem(order);
        //检查库存
        //设置订单价格
//        order.setAmount(order.getItems().stream().);
        //设置订单状态
        return null;
    }
}
