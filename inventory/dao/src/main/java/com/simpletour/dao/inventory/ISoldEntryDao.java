package com.simpletour.dao.inventory;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.query.SoldEntryKey;

import java.util.Date;
import java.util.List;

/**
 * 文件描述：销售库存数据层接口
 * 创建人员：石广路
 * 创建日期：2015/11/27 16:11
 * 备注说明：null
 */
public interface ISoldEntryDao extends IBaseDao {
    /**
     * 功能：设置指定订单号的销售库存的状态
     * 作者：石广路
     * 新增：2015-12-08 10:19:08
     * 修改：null
     *
     * @param oid       订单编号
     * @param status    库存状态
     *
     * return void
     */
    void updateSoldEntriesStatusByOrderId(long oid, boolean status);

    /**
     * 功能：根据库存类型、库存ID、订单编号、订单项编号和父条目编号等联合主键来查询销售库存
     * 作者：石广路
     * 新增：2015-12-08 10:18:08
     * 修改：主键字段值无效时，则不会将其作为过滤条件
     *
     * @param soldEntryKey   销售库存的联合主键
     *
     * return 销售库存列表
     */
    List<SoldEntry> getSoldEntriesByUnionKeys(SoldEntryKey soldEntryKey);

    /**
     * 功能：根据库存类型、库存主体ID、库存日期和有效状态等查询条件来获取销售库存总量
     * 作者：石广路
     * 新增：2015-11-27 0:47
     * 修改：null
     *
     * @param inventoryType     库存类型
     * @param inventoryTypeId   库存主体ID
     * @param day               库存日期
     * @param status            库存有效状态（null：查找所有状态的库存，true：查找有效状态的库存，false：查找无效状态的库存）
     *
     * return 库存实体
     */
    int getSoldQuantity(InventoryType inventoryType, Long inventoryTypeId, Date day, Boolean status);
}
