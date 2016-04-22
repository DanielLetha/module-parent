package com.simpletour.dao.inventory;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.query.SoldEntryKey;
import com.simpletour.domain.inventory.query.StockKey;

import java.util.Date;
import java.util.List;

/**
 * 文件描述：销售库存数据层接口
 * 创建人员：石广路
 * 创建日期：2015/11/27 16:11
 * 备注说明：null
 * @since 2.0-SNAPSHOT
 */
public interface ISoldEntryDao extends IBaseDao {
    /**
     * 功能：设置指定订单项编号的销售库存的状态
     * 作者：石广路
     * 新增：2015-12-08 10:19:08
     * 修改：2016-4-22
     *
     * @param itemId    订单项编号
     * @param valid     销售库存状态，true - 有效，false - 无效
     *
     * return void
     * @since 2.0-SNAPSHOT
     */
    void updateSoldEntriesStatusByOrderId(Long itemId, Boolean valid);

    /**
     * 功能：根据库存组合键、库存日期、库存销售记录状态、订单项编号和父条目编号等条件来查询销售库存
     * 作者：石广路
     * 新增：2015-12-08 10:18:08
     * 修改：主键字段值无效时，则不会将其作为过滤条件
     *      于2016-4-22
     *      1、新增了库存日期和库存销售记录状态两个查询条件参数；
     *      2、移除了之前版本中的订单ID查询条件。
     *
     * @param soldEntryKey  销售库存组合键
     * @param day           库存日期
     *
     * return 销售库存列表
     * @since 2.0-SNAPSHOT
     */
    List<SoldEntry> getSoldEntriesByUnionKeys(SoldEntryKey soldEntryKey, Date day);

    /**
     * 功能：根据库存类型、库存主体ID、库存日期和有效状态等查询条件来获取销售库存总量
     * 作者：石广路
     * 新增：2015-11-27 0:47
     * 修改：null
     *
     * @param stockKey  库存组合键
     * @param day       库存日期
     * @param valid     销售库存状态，true - 有效，false - 无效，null - 不区分状态
     *
     * return 库存实体
     */
    //int getSoldQuantity(InventoryType inventoryType, Long inventoryTypeId, Date day, Boolean status);
    int getSoldQuantity(StockKey stockKey, Date day, Boolean valid);
}
