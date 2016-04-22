package com.simpletour.order.biz;

import com.simpletour.domain.order.Item;

import java.util.Optional;

/**
 * Created by Mario on 2016/4/20.
 */
public interface IItemBiz {
    /**
     * 添加订单项
     *
     * @param item 订单项实体
     * @return 订单项实体
     */
    Item validateItem(Item item);
}
