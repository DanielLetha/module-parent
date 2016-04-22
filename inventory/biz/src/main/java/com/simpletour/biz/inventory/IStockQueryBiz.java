package com.simpletour.biz.inventory;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.*;
import com.simpletour.domain.inventory.query.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文件描述：库存关联信息查询业务层接口
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/21 11:07
 * 备注说明：专门负责对库存信息、库存价格信息和库存销售信息进行关联查询
 *
 * @since 2.0-SNAPSHOT
 */
public interface IStockQueryBiz {
    /**
     * 功能：检查库存必填字段有效性
     * 作者：石广路
     * 新增：2016-4-21
     * 备注：null
     *
     * @param inventoryType     库存元素类型
     * @param inventoryId       库存元素ID
     * @param day               库存日期
     *
     * return 库存实体
     */
    //void validateStockParam(InventoryType inventoryType, Long inventoryId, Date day) throws BaseSystemException;

    /////////////////////////////////////////////////////////////////////////////////////////////
    // 库存信息相关查询接口
    /////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 功能：根据ID获取库存信息
     * 作者：石广路
     * 新增：2015-11-26 17:07
     * 备注：不计算库存的已销售量和可销售量
     *
     * @param id 主键ID
     *
     * return 库存实体
     */
    //Optional<Stock> getStockById(Long id);

    /**
     * 功能：根据指定的库存类型和库存主体ID来获取某一天的库存依托对象及其自身的库存依赖
     * 作者：石广路
     * 新增：2015-12-18 18:19
     * 备注：获取库存元素实体对象，如果不具备库存依赖，则只返回符合指定条件的库存托管对象
     * 修改：这类接口要放到库存托管对象一侧来实现
     *
     * @param stockKey  库存联合主键
     * @param day       库存日期
     *
     * return 库存托管对象
     */
    //BaseDomain getStockWithDependencies(StockKey stockKey, Date day);

    /**
     * 功能：根据指定的库存类型和库存主体ID来获取某一天的库存依托对象的库存依赖
     * 作者：石广路
     * 新增：2015-12-22 15:07
     * 备注：获取库存元素实体对象，等同于调用库存依托对象的getDependentStockKeys()方法
     * 修改：这类接口要放到库存托管对象一侧来实现
     *
     * @param stockKey  库存联合主键
     * @param day       库存日期
     *
     * return 库存依赖
     */
    //List<StockKey> getStockDependencies(StockKey stockKey, Date day);

    /**
     * 功能：检查库存实体及对应的库存价格信息是否已存在
     * 作者：石广路
     * 新增：2016-4-20
     * 备注：库存实体及对应的库存价格信息必须要同时存在才会返回true，如果库存的基本信息无效则抛出BaseSystemException
     *
     * @param stockBound    库存基本信息
     *
     * return true - 存在，false - 不存在
     */
    //boolean isExisted(StockBound stockBound) throws BaseSystemException;

    /**
     * 功能：检查指定的库存元素在某一天上是否有库存
     * 作者：石广路
     * 新增：2015-12-18 15:02
     * 备注：不会计算库存的已销售量和可销售量，如果不指定日期，则在所有日期上的查找该库存元素是否存在
     *
     * @param stockKey  库存联合主键
     * @param day       库存日期
     *
     * return true：库存元素存在，false：库存元素不存在
     */
    //boolean hasOnlineStock(StockKey stockKey, Date day);


    /**
     * 功能：根据库存组合键来查找某一天上特定类型的库存价格信息
     * 作者：石广路
     * 新增：2016-4-22
     * 备注：库存价格信息列表通常包含了成人和儿童的库存价格信息，如果指定了库存价格类型，则只查询单个类型的库存价格信息，否则，进行不分区类型查找
     *
     * @param stockKey  库存联合主键
     * @param day       库存日期
     * @param type      库存价格类型，null - 不区分库存价格类型，其他值则表示是成人价还是儿童价
     *
     * return 库存价格信息列表
     */
    List<Price> getPrices(StockKey stockKey, Date day, Price.Type type);


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
    DomainPage getStocksQuantitiesPagesByRelations(StockQuery stockQuery) throws BaseSystemException;

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
     * 功能：根据库存组合键来查找某一天上特定状态的库存信息
     * 作者：石广路
     * 新增：2016-4-22
     * 备注：如果查询字段为空，则不使用该字段进行过滤
     *
     * @param stockKey      库存联合主键
     * @param day           库存日期
     * @param online        库存上线状态，true - 获取上线的库存，false - 获取下线的库存，null - 获取所有的库存
     * @param calcQuantity  是否计算可售库存以及已售库存的数量的标志，true - 计算，false - 不计算
     *
     * return 库存实体
     */
    Optional<Stock> getStock(StockKey stockKey, Date day, Boolean online, boolean calcQuantity) throws BaseSystemException;

    /**
     * 功能：根据库存依托对象的组合键字段参数来查找某一天且指定在线状态的库存
     * 作者：石广路
     * 新增：2015-12-02 16:34
     * 备注：如果查询字段为空，则不使用该字段进行过滤，此方法会自动计算库存的已售库存和可售可存
     *
     * @param trace     库存依托对象
     * @param day       库存日期
     * @param online    库存上线状态，true - 获取上线的库存，false - 获取下线的库存，null - 获取所有的库存
     *
     * return 库存实体
     */
    Optional<Stock> getStock(IStockTraceable trace, Date day, Boolean online) throws BaseSystemException;

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
    List<Stock> getStocks(IStockTraceable trace, Date startDate, Date endDate, Boolean online) throws BaseSystemException;

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

    /////////////////////////////////////////////////////////////////////////////////////////////
    // 库存价格信息相关查询接口
    /////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 功能：检查库存价格信息是否存在
     * 作者：石广路
     * 新增：2016-4-20
     * 备注：检查同种类型的库存价格信息在相同日期上的记录是否已存在，如果库存价格信息无效则抛出BaseSystemException
     *
     * @param price 库存价格实体
     *
     * return true - 存在，false - 不存在
     */
    //boolean priceIsExisting(Price price) throws BaseSystemException;

    /**
     * 功能：根据ID获取库存价格信息
     * 作者：石广路（shiguanglu@simpletour.com）
     * 新增：2016-04-20 11:44
     * 备注：null
     *
     * @since 2.0-SNAPSHOT
     *
     * @param id  库存价格实体
     *
     * return 库存价格实体
     */
    Optional<Price> getPriceById(Long id);

    /////////////////////////////////////////////////////////////////////////////////////////////
    // 库存销售记录信息相关查询接口
    /////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 功能：根据ID获取库存销售记录信息
     * 作者：石广路（shiguanglu@simpletour.com）
     * 新增：2016-4-21
     * 备注：null
     *
     * @since 2.0-SNAPSHOT
     *
     * @param id  库存销售记录信息实体
     *
     * return 库存销售记录信息实体
     */
    Optional<SoldEntry> getSoldEntryById(Long id);

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
    List<SoldEntry> getSoldEntriesByUnionKeys(SoldEntryKey soldEntryKey);

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
