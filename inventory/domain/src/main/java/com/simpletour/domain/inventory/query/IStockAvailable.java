package com.simpletour.domain.inventory.query;

import java.util.List;

/**
 * 文件描述：可用库存检查接口
 * 创建人员：石广路
 * 创建日期：2015/12/8 16:27
 * 备注说明：null
 */
public interface IStockAvailable {
    /**
     * 功能：获取库存检查所需的组合键、库存日期和需要的库存量等字段列表
     * 作者：石广路
     * 新增：2015-12-02 16:36
     * 修改：null
     *
     * return 库存实体列表
     */
    List<StockAvailableKey> getStockAvailableEntries();
}
