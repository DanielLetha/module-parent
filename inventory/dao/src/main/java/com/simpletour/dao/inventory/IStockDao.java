package com.simpletour.dao.inventory;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.inventory.query.StockQuery;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：库存数据层接口
 * 创建人员：石广路
 * 创建日期：2015/11/25 20:37
 * 备注说明：@Deprecated注解的接口没有计算已售库存和可售可存，现已废弃，请使用新的接口
 */
public interface IStockDao extends IBaseDao {
    /**
     * 功能：根据库存类型和库存主体ID来获取库存依托对象
     * 作者：石广路
     * 新增：2015-12-14 14:27
     * 备注：不区分库存依托对象的状态
     *
     * @param inventoryType     库存类型
     * @param inventoryTypeId   库存主体ID
     *
     * return 库存依托对象
     */
    <T extends BaseDomain> T getStockDependency(final InventoryType inventoryType, final Long inventoryTypeId);

    /**
     * 功能：根据库存类型和库存主体ID来获取上架中的库存托管对象
     * 作者：石广路
     * 新增：2015-12-01 11:47
     * 备注：只获取上架中的库存托管对象
     *
     * @param inventoryType     库存类型
     * @param inventoryTypeId   库存主体ID
     *
     * return true：存在库存依赖，false：不存在库存依赖
     */
    <T extends BaseDomain> T getOnlineStockDependency(final InventoryType inventoryType, final Long inventoryTypeId);

    /**
     * 功能：根据库存类型、库存主体ID、库存日期和上线状态等查询条件来获取指定库存字段的值
     * 作者：石广路
     * 新增：2015-12-09 10:37
     * 备注：不会计算库存的可用库存量和已售库存量，指定字段必须与库存对象属性同名，可为SQL内置函数
     *
     * @param stockQuery    库存查询条件参数封装类
     * @param filedName     库存字段
     *
     * return 库存字段值
     */
    Optional<Object> getFieldValueByConditions(final StockQuery stockQuery, String filedName);

    /**
     * 功能：根据库存类型、库存主体ID、库存日期和上线状态等查询条件来获取库存信息
     * 作者：石广路
     * 新增：2015-12-09 10:37
     * 备注：不会计算库存的可用库存量和已售库存量
     *
     * @param stockKey          库存组合键
     * @param day               库存日期
     * @param online            库存上线状态
     *
     * return 库存实体
     */
    Optional<Stock> getStockByConditions(final StockKey stockKey, final Date day, final Boolean online);

    /**
     * 功能：根据库存字段条件来获取库存列表
     * 作者：石广路
     * 新增：2015-12-09 10:27
     * 修改：通过StockKey对象来传递查询字段，默认以库存日期进行降序排序，不会计算库存的可用库存量和已售库存量
     *
     * @param stockKey          库存字段
     * @param startDate         库存起始日期
     * @param endDate           库存截止日期
     * @param online            库存上线状态
     *
     * return 库存列表
     */
    @Deprecated
    List<Stock> getStocksListByConditions(final StockKey stockKey, final Date startDate, final Date endDate, final Boolean online);

    /**
     * 功能：根据库存类型、库存主体ID、库存日期和上线状态等查询条件来获取库存信息
     * 作者：石广路
     * 新增：2015-12-11 17:11
     * 备注：通过StockKey对象来传递查询字段，默认以库存日期进行降序排序，并自动计算库存的已售库存和可售可存
     *
     * @param stockKey          库存组合键
     * @param day               库存日期
     * @param online            库存上线状态
     *
     * return 库存实体
     */
    Optional<Stock> getStockQuantitiesListByConditions(final StockKey stockKey, final Date day, final Boolean online);

    /**
     * 功能：根据库存字段条件来获取指定时间段内的库存列表
     * 作者：石广路
     * 新增：2015-12-11 14:21
     * 备注：空字段将不会作为过滤条件，会自动计算每条库存的已售库存和可售可存
     *
     * @param stockQuery    库存查询条件参数封装类
     *
     * return 库存列表
     */
    List<Stock> getStocksQuantitiesListByConditions(final StockQuery stockQuery);

    /**
     * 功能：根据库存字段条件来查询指定时间段内的库存及其关联的库存销售记录
     * 作者：石广路
     * 新增：2015-12-09 10:27
     * 备注：空字段将不会作为过滤条件，会自动计算每条库存的已售库存和可售可存
     *
     * @param stockQuery    库存查询条件参数封装类
     *
     * return 库存分页列表
     */
    @Deprecated
    DomainPage getStocksQuantitiesPagesByConditions(final StockQuery stockQuery);

    /**
     * 功能：根据库存关联关系（如依托的具体库存对象）来查询指定时间段内的库存及其关联的库存销售记录
     * 作者：石广路
     * 新增：2015-12-15 20:27
     * 备注：空字段将不会作为过滤条件，会自动计算每条库存的已售库存和可售可存，会携带关联的库存托管对象的名称，如元素名称“青城山门票1张”
     *
     * @param stockQuery    库存查询条件参数封装类
     *
     * return 库存分页列表
     */
    DomainPage getStocksQuantitiesPagesByRelations(final StockQuery stockQuery);

    /**
     * 根据条件删除
     * add by: yangdongfeng
     * @param clazz
     * @param conditions
     * @param <T>
     * @return 删除的对象的个数
     */
    <T extends BaseDomain> int removeEntityByConditions(Class<T> clazz, Condition conditions);
}



