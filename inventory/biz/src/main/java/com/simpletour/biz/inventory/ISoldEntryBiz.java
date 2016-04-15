package com.simpletour.biz.inventory;

import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.query.SoldEntryKey;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：销售库存模块业务层接口
 * 创建人员：石广路
 * 创建日期：2015/12/3 17:07
 * 备注说明：null
 */
public interface ISoldEntryBiz {
    /**
     * 功能：添加销售库存
     * 作者：石广路
     * 新增：2015-12-03 17:12
     * 修改：null
     *
     * @param soldEntry 销售库存
     *
     * return 销售库存
     */
    Optional<SoldEntry> addSoldEntry(final SoldEntry soldEntry) throws BaseSystemException;

    /**
     * 功能：批量添加销售库存
     * 作者：石广路
     * 新增：2015-12-03 17:12
     * 修改：null
     *
     * @param soldEntries 销售库存列表
     *
     * return 销售库存
     */
    List<SoldEntry> addSoldEntries(final List<SoldEntry> soldEntries) throws BaseSystemException;

    /**
     * 功能：使指定订单号的所有销售库存的状态都变为无效
     * 作者：石广路
     * 新增：2015-12-08 10:19:08
     * 修改：null
     *
     * @param oid   订单号
     *
     * return void
     */
    void invalidateSoldEntriesByOrderId(Long oid);

    /**
     * 功能：根据订单编号、订单项编号和父条目编号等联合主键ID来查询销售库存
     * 作者：石广路
     * 新增：2015-12-08 10:18:08
     * 修改：null
     *
     * @param soldEntryKey   销售库存的联合主键ID
     *
     * return void
     */
    List<SoldEntry> getSoldEntriesByUnionIds(SoldEntryKey soldEntryKey);

    /**
     * 功能：根据库存类型、库存主体ID和库存日期等查询条件来获取有效的销售库存总量
     * 作者：石广路
     * 新增：2015-11-27 10:47
     * 备注：只查询有效状态的销售库存
     *
     * @param soldEntryKey  销售库存实体
     * @param day           销售库存日期
     *
     * return 库存销售量
     */
    int getAvailableSoldQuantity(SoldEntryKey soldEntryKey, Date day);
}
