package com.simpletour.domain.inventory.query;

import java.util.List;

/**
 * 文件描述：依托库存对象必须要实现的接口
 * 创建人员：石广路
 * 创建日期：2015/12/1 14:16
 * 备注说明：依托库存对象必须要重写equals()和hashCode()方法。
 */
public interface IStockTraceable {
    /**
     * 功能：获取查询库存所依赖的组合键参数
     * 作者：石广路
     * 新增：2015-12-01 14:20
     * 修改：null
     *
     * return StockKey
     */
    StockKey getStockKey();

    /**
     * 功能：获取库存依托对象所依赖的库存组合键参数列表
     * 作者：石广路
     * 新增：2015-12-02 10:54
     * 修改：null
     *
     * return 依赖的库存组合键参数列表
     */
    List<StockKey> getDependentStockKeys();
}
