package com.simpletour.biz.inventory;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.query.IStockAvailable;
import com.simpletour.domain.inventory.query.IStockTraceable;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.inventory.query.StockQuery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：库存模块业务层接口
 * 创建人员：石广路
 * 创建日期：2015/11/30 15:20
 * 备注说明：null
 */
public interface IStockBiz {
    /**
     * 功能：添加库存
     * 作者：石广路
     * 新增：2015-11-26 16:57
     * 备注：null
     *
     * @param stock     库存实体
     *
     * return 库存实体
     */
    Optional<Stock> addStock(final Stock stock) throws BaseSystemException;

    /**
     * 功能：批量添加库存
     * 作者：石广路
     * 新增：2015-11-26 16:57
     * 备注：null
     *
     * @param stocks        批量库存列表
     *
     * return 库存列表
     */
    List<Stock> addStocks(final List<Stock> stocks) throws BaseSystemException;

    /**
     * 功能：更新库存
     * 作者：石广路
     * 新增：2015-11-26 17:01
     * 备注：null
     *
     * @param stock     库存实体
     *
     * return 库存实体
     */
    Optional<Stock> updateStock(final Stock stock) throws BaseSystemException;

    /**
     * 功能：批量更新库存
     * 作者：石广路
     * 新增：2015-11-26 17:01
     * 备注：null
     *
     * @param stocks        批量库存列表
     *
     * return 库存列表
     */
    List<Stock> updateStocks(final List<Stock> stocks) throws BaseSystemException;

    /**
     * 功能：删除库存
     * 作者：石广路
     * 新增：2015-11-26 17:03
     * 备注：null
     *
     * @param id     主键ID
     *
     * return void
     */
    void deleteStock(long id);

    /**
     * 功能：批量删除库存
     * 作者：石广路
     * 新增：2015-11-26 17:03
     * 备注：null
     *
     * @param ids    库存ID列表
     *
     * return void
     */
    void deleteStocks(final List<Long> ids);

    /**
     * 根据库存对象类型及id删除库存
     * add by:yangdongfeng
     * @param type
     * @param id
     */
    void deleteStocksByInventoryTypeId(InventoryType type, Long id);

    /**
     * 功能：根据库存ID获取库存记录
     * 作者：石广路
     * 新增：2015-11-26 17:07
     * 备注：不计算库存的已销售量和可销售量
     *
     * @param id 主键ID
     *
     * return 库存实体
     */
    Optional<Stock> getStockRecordById(long id);

    /**
     * 功能：根据指定的库存类型和库存主体ID来获取某一天的库存依托对象及其自身的库存依赖
     * 作者：石广路
     * 新增：2015-12-18 18:19
     * 备注：如果不具备库存依赖，则只返回符合指定条件的库存托管对象
     *
     * @param stockKey  库存联合主键
     * @param day       库存日期
     *
     * return 库存托管对象
     */
    BaseDomain getStockWithDependencies(final StockKey stockKey, final Date day);

    /**
     * 功能：根据指定的库存类型和库存主体ID来获取某一天的库存依托对象的库存依赖
     * 作者：石广路
     * 新增：2015-12-22 15:07
     * 备注：等同于调用库存依托对象的getDependentStockKeys()方法
     *
     * @param stockKey  库存联合主键
     * @param day       库存日期
     *
     * return 库存依赖
     */
    List<StockKey> getStockDependencies(final StockKey stockKey, final Date day);

    /**
     * 功能：检查指定库存类型和库存主体ID的库存在指定的日期里是否还在售
     * 作者：石广路
     * 新增：2015-12-18 15:02
     * 备注：不计算库存的已销售量和可销售量，如果不指定日期，则查找所有指定类型和主体ID的库存
     *
     * @param stockKey  库存联合主键
     * @param day       库存日期
     *
     * return true：存在该库存记录，false：不存在该库存记录
     */
    boolean hasOnlineStock(final StockKey stockKey, final Date day);

    /**
     * 功能：根据库存类型、库存主体ID等字段信息来查找某一时间段范围内的有效库存记录
     * 作者：石广路
     * 新增：2015-12-9 10:46
     * 备注：已废弃，不对查询字段有效性做检查，查询字段为空时则不使用该过滤条件
     *
     * @param stockKey          库存主键字段
     * @param startDate         库存起始日期
     * @param endDate           库存截止日期
     * @param orderByFiledName  库存列表排序字段
     * @param orderBy           列表排序方式
     * @param pageIndex         分页索引（从1开始）
     * @param pageSize          分页大小
     *
     * return 库存分页列表
     */
    @Deprecated
    DomainPage getStocksPagesByConditions(final StockKey stockKey, final Date startDate, final Date endDate, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize);

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
    DomainPage getStocksQuantitiesPagesByRelations(final StockQuery stockQuery) throws BaseSystemException;

    /**
     * 功能：根据库存字段条件来获取指定时间段内的库存最低或最高价格
     * 作者：石广路
     * 新增：2016-03-11 10:19
     * 备注：空字段将不会作为过滤条件，如果获取价格失败，将会抛出InventoryBizError.GET_STOCK_PRICE_FAILED异常错误信息
     *
     * @param stockQuery    库存查询条件参数封装类
     * @param isLowest      true表示获取指定时间段内库存的最低价格，反之则获取最高价格
     *
     * return 库存最低或最高价格
     */
    BigDecimal getStocksPriceInTimeframe(StockQuery stockQuery, boolean isLowest) throws BaseSystemException;

    /**
     * 功能：根据库存依托对象的组合键字段参数来查找某一天且指定在线状态的库存
     * 作者：石广路
     * 新增：2015-12-02 16:34
     * 备注：如果查询字段为空，则不使用该字段进行过滤，此方法会自动计算库存的已售库存和可售可存
     *
     * @param trace     库存依托对象
     * @param day       库存日期
     * @param online    库存在线状态
     *
     * return 库存实体
     */
    Optional<Stock> getStock(IStockTraceable trace, final Date day, Boolean online) throws BaseSystemException;

    /**
     * 功能：根据指定类型的库存字段信息来查找某一天的上线库存量
     * 作者：石广路
     * 新增：2015-12-10 16:05
     * 备注：面向服务端的库存查询，仅查询上线状态，且不存在依赖关系的库存，自动计算库存的已售库存和可售可存
     *
     * @param trace 指定类型的库存
     * @param day   库存日期
     *
     * return 库存实体
     */
    @Deprecated
    Optional<Stock> getOnlineStock(IStockTraceable trace, final Date day) throws BaseSystemException;

    /**
     * 功能：根据关联的库存字段信息来查找某一天的库存量
     * 作者：石广路
     * 新增：2015-12-02 16:34
     * 备注：查询存在库存依赖关系或不存在库存依赖关系的上线库存，自动计算库存的已售库存和可售可存
     *
     * @param trace 指定类型的库存
     * @param day   库存日期
     *
     * return 库存实体
     */
    @Deprecated
    Optional<Stock> getRelationalStock(IStockTraceable trace, final Date day) throws BaseSystemException;

    /**
     * 功能：根据库存依托对象的组合键字段参数来查找某一时间段范围内且指定在线状态的库存列表
     * 作者：石广路
     * 新增：2015-12-02 16:36
     * 备注：如果查询字段为空，则不使用该字段进行过滤，此方法会自动计算库存的已售库存和可售可存
     *
     * @param trace     库存依托对象
     * @param startDate 库存起始日期
     * @param endDate   库存截止日期
     * @param online    库存在线状态
     *
     * return 库存列表
     */
    List<Stock> getStocks(IStockTraceable trace, final Date startDate, final Date endDate, Boolean online) throws BaseSystemException;

    /**
     * 功能：根据指定类型的库存字段信息来查找某一时间段范围内的库存量
     * 作者：石广路
     * 新增：2015-12-02 16:36
     * 备注：面向服务端的库存查询，仅查询上线状态，且不存在依赖关系的库存，自动计算库存的已售库存和可售可存
     *
     * @param trace     指定类型的库存
     * @param startDate 库存起始日期
     * @param endDate   库存截止日期
     *
     * return 库存实体列表
     */
    @Deprecated
    List<Stock> getOnlineStocks(IStockTraceable trace, final Date startDate, final Date endDate) throws BaseSystemException;

    /**
     * 功能：根据关联的库存字段信息来查找某一时间段范围内的库存量
     * 作者：石广路
     * 新增：2015-12-02 16:36
     * 备注：查询存在库存依赖关系或不存在库存依赖关系的上线库存，自动计算库存的已售库存和可售可存
     *
     * @param trace     指定类型的库存
     * @param startDate 库存起始日期
     * @param endDate   库存截止日期
     *
     * return 库存实体列表
     */
    @Deprecated
    List<Stock> getRelationalStocks(IStockTraceable trace, final Date startDate, final Date endDate) throws BaseSystemException;

    /**
     * 功能：检查下单的库存数量是否充足
     * 作者：石广路
     * 新增：2015-12-08 16:50
     * 修改：null
     * 备注：如果下单的库存量存在问题，则抛出InventoryBizError.STOCK_SOLD_OUT或InventoryBizError.DEPENDENT_STOCK_SHORTAGE异常给上层，否则，正常返回
     *
     * @param stockCheckable    库存检查参数
     *
     * return void
     */
    void checkDemandQuantity(IStockAvailable stockCheckable) throws BaseSystemException;
}
