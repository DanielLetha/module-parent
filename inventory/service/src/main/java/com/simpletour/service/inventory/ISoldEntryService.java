package com.simpletour.service.inventory;

import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.SoldEntry;

import java.util.List;
import java.util.Optional;

/**
 * 文件描述：销售库存模块服务层接口
 * 创建人员：石广路
 * 创建日期：2015/12/3 18:03
 * 备注说明：null
 */
public interface ISoldEntryService {
    /**
     * 功能：添加库存销售记录信息
     * 作者：石广路
     * 新增：2015-12-03 17:12
     * 修改：null
     *
     * @param soldEntry 库存销售记录信息实体
     *
     * return 库存销售记录信息实体
     */
    Optional<SoldEntry> addSoldEntry(SoldEntry soldEntry) throws BaseSystemException;

    /**
     * 功能：批量添加库存销售记录信息
     * 作者：石广路
     * 新增：2015-12-03 17:12
     * 修改：null
     *
     * @param soldEntries 库存销售记录信息实体列表
     *
     * return 库存销售记录信息实体列表
     */
    List<SoldEntry> addSoldEntries(List<SoldEntry> soldEntries) throws BaseSystemException;

    /**
     * 功能：更新库存销售记录信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     *
     * @param soldEntry 库存销售记录信息实体
     *
     * return 库存销售记录信息实体
     */
    Optional<SoldEntry> updateSoldEntry(SoldEntry soldEntry) throws BaseSystemException;

    /**
     * 功能：批量更新库存销售记录信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     *
     * @param soldEntries 库存销售记录信息实体列表
     *
     * return 库存销售记录信息实体列表
     */
    List<SoldEntry> updateSoldEntries(List<SoldEntry> soldEntries) throws BaseSystemException;

    /**
     * 功能：删除库存销售记录信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：如果删除失败则抛出BaseSystemException异常
     *
     * @param id     主键ID
     *
     * return void
     */
    void deleteSoldEntry(Long id) throws BaseSystemException;

    /**
     * 功能：批量删除库存销售记录信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：如果删除失败则抛出BaseSystemException异常
     *
     * @param ids    库存ID列表
     *
     * return void
     */
    void deleteSoldEntries(List<Long> ids);

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
    //List<SoldEntry> getSoldEntriesByUnionIds(SoldEntryKey soldEntryKey);

    /**
     * 功能：根据库存类型、库存主体ID和库存日期等查询条件来获取有效的销售库存总量
     * 作者：石广路
     * 新增：2015-11-27 0:47
     * 修改：2015-12-08 14:05
     *
     * @param soldEntryKey  销售库存实体
     * @param day           销售库存日期
     *
     * return 库存销售量
     */
    //int getAvailableSoldQuantity(SoldEntryKey soldEntryKey, Date day);
}
