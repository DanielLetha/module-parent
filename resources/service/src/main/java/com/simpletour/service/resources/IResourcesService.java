package com.simpletour.service.resources;


import com.simpletour.biz.resources.vo.ProcurementVo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.Price;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.StockPrice;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.resources.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 文件描述：资源模块业务处理接口
 * 创建人员：石广路
 * 创建日期：2015/11/25 14:27
 * 备注说明：将资源包名从travelresources改为resources
 * 修改记录：添加增删改查元素库存的接口
 */
public interface IResourcesService {
    int DEFAULT_PAGE_SIZE = 10;

    /**
     * 添加osp
     *
     * @param offlineServiceProvider
     * @return
     * @throws IllegalArgumentException
     */
    Optional<OfflineServiceProvider> addOfflineServiceProvider(OfflineServiceProvider offlineServiceProvider);

    /**
     * @param id
     * @throws BaseSystemException 要删除的不存在时抛出异常OSP_NOT_EXIST
     */
    void deleteOfflineServiceProvider(long id) throws BaseSystemException;

    /**
     * 修改一个osp相关数据
     *
     * @param offlineServiceProvider
     * @return
     */
    Optional<OfflineServiceProvider> updateOfflineServiceProvider(OfflineServiceProvider offlineServiceProvider);

    /**
     * 分页查询osp
     *
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<OfflineServiceProvider> getOfflineServiceProvidersByPage(Boolean isDel, int page, int pageSize);

    /**
     * 根据名字模糊查询
     *
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<OfflineServiceProvider> getOfflineServiceProvidersByConditionPage(String name, Boolean isDel, int page, int pageSize);

    /**
     * 根据id查询osp
     *
     * @param id
     * @return
     */
    Optional<OfflineServiceProvider> getOfflineServiceProviderById(long id);


    /**
     * 功能：新增景点
     * 作者：石广路
     * 新增：2015-11-20 20:51
     * 修改：null
     *
     * @param scenic 景点实体
     *               <p>
     *               return scenic   景点实体
     */
    Optional<Scenic> addScenic(Scenic scenic) throws BaseSystemException;

    /**
     * 功能：更新景点
     * 作者：石广路
     * 新增：2015-11-20 20:52
     * 修改：null
     *
     * @param scenic 景点实体
     *               <p>
     *               return scenic   景点实体
     */
    Optional<Scenic> updateScenic(Scenic scenic) throws BaseSystemException;

    /**
    * 功能：删除景点
    * 作者：石广路
    * 新增：2015-11-20 20:52
    * 修改：Hawk:
    *      现在 pseudo 参数已经没用了，全部都用伪删除
    *
    * @param id     主键ID
    */
    void deleteScenic(long id) throws BaseSystemException;

    /**
     * 功能：根据ID获取景点
     * 作者：石广路
     * 新增：2015-11-21 10:45
     * 修改：null
     *
     * @param id 主键ID
     *           <p>
     *           return scenic   景点实体
     */
    Optional<Scenic> getScenicById(long id);

    /**
     * 功能：获取景点ID
     * 作者：石广路
     * 新增：2015-12-05 10:10
     * 修改：Hawk：
     *      该函数的作用为，通过destinationId与scenicName与查询所对应的惟一的一个景点
     *
     * @param scenic 景点实体
     *               <p>
     *               return 景点ID
     */
    long getScenicId(final Scenic scenic);

    /**
     * 功能：根据景点名称、所属景点、目的地等查询条件来获取景点分页列表
     * 作者：石广路
     * 新增：2015-11-21 9:37
     * 修改：null
     *
     * @param conditions       组合查询条件(name：景点名称, address：所属景点, destination.name: 目的地)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     *                         <p>
     *                         return 景点分页列表
     */
    DomainPage<Scenic> queryScenicsPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 功能：新增餐饮点
     * 作者：石广路
     * 新增：2015-11-21 11:48
     * 修改：null
     *
     * @param catering 餐饮点实体
     *                 <p>
     *                 return 餐饮点实体
     */
    Optional<Catering> addCatering(Catering catering) throws BaseSystemException;

    /**
     * 功能：更新餐饮点
     * 作者：石广路
     * 新增：2015-11-21 11:48
     * 修改：null
     *
     * @param catering 餐饮点实体
     *                 return 餐饮点实体
     */
    Optional<Catering> updateCatering(Catering catering) throws BaseSystemException;

    /**
     * 功能：删除餐饮点
     * 作者：石广路
     * 新增：2015-11-21 11:52
     * 修改：null
     *
     * @param id     主键ID
     * @param pseudo true：伪删除，false：真删除
     *               void
     */
    void deleteCatering(long id, boolean pseudo);

    /**
     * 功能：根据ID获取餐饮点
     * 作者：石广路
     * 新增：2015-11-21 13:41
     * 修改：null
     *
     * @param id 主键ID
     *           <p>
     *           return catering  餐饮点实体
     */
    Optional<Catering> getCateringById(long id);

    /**
     * 功能：获取餐饮点ID
     * 作者：石广路
     * 新增：2015-12-05 10:10
     * 修改：null
     *
     * @param catering 餐饮点实体
     *                 <p>
     *                 return 餐饮点ID
     */
    long getCateringId(final Catering catering);

    /**
     * 功能：根据餐饮点类型、名称、目的地等查询条件来获取餐饮点分页列表
     * 作者：石广路
     * 新增：2015-11-21 11:55
     * 修改：null
     *
     * @param conditions       组合查询条件(name：餐饮点类型, address：名称, destination.name: 目的地)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     *                         <p>
     *                         return 餐饮点分页列表
     */
    DomainPage<Catering> queryCateringsPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);


    //region -------------------------------------hotel------------------------------------

    /**
     * 添加住宿点
     *
     * @param hotel 待添加的住宿点
     * @return 添加完成后的住宿点
     * @throws BaseSystemException 住宿点为空、目的地为空（EMPTY_ENTITY）；
     *                             目的地不存在（DESTINATION_NOT_EXIST）
     * @author XuHui/xuhui@simpletour.com
     */
    Optional<Hotel> addHotel(Hotel hotel) throws BaseSystemException;

    /**
     * 更新住宿点
     *
     * @param hotel 待更新住宿点
     * @return  更新完成后的住宿点
     * @throws BaseSystemException
     * @author Xuhui/xuhui@simpletour.com
     */
    Optional<Hotel> updateHotel(Hotel hotel) throws BaseSystemException;

    /**
     * 功能：删除住宿点
     * 作者：石广路
     * 新增：2015-11-21 11:52
     * 修改：null
     *
     * @param id 主键ID
     *           <p>
     *           void
     */
    void deleteHotel(long id);

    /**
     * 功能：根据ID获取住宿点
     * 作者：石广路
     * 新增：2015-11-21 13:41
     * 修改：null
     *
     * @param id 主键ID
     *           <p>
     *           return hotel   住宿点实体
     */
    Optional<Hotel> getHotelById(long id);

    /**
     * 功能：根据住宿点酒店类型、名称、目的地等查询条件来获取住宿点分页列表
     * 作者：石广路
     * 新增：2015-11-21 11:55
     * 修改：null
     *
     * @param conditions       组合查询条件(type：酒店类型（如all，hotel，inn，folk_house，others）, name：名称, destination.name: 目的地)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     *                         <p>
     *                         return 住宿点分页列表
     */
    DomainPage<Hotel> queryHotelsPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    //endregion

    /**
     * 根据id获取area
     *
     * @param id
     * @return
     */
    Optional<Area> getAreaById(Long id);

    //region ----------------------------------destination----------------------------
    /**
     * 添加目的地
     *
     * @param dest
     * @return
     * @throws BaseSystemException
     */
    Optional<Destination> addDestination(Destination dest) throws BaseSystemException;

    /**
     * @param dest
     * @return
     * @throws BaseSystemException
     */
    Optional<Destination> updateDestination(Destination dest) throws BaseSystemException;

    /**
     * @param id
     */
    void deleteDestination(long id);

    /**
     * @param page
     * @return
     */
    DomainPage<Destination> getDestinationsByPage(int page);

    /**
     * 分页查询Destination
     *
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<Destination> getDestinationsByPage(int page, int pageSize);

    /**
     * 按照目的地名字，完整区域名字模糊查询目的地
     *
     * @param name
     * @param area
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<Destination> getDestionationsByConditonPage(String name, String area, int page, int pageSize);

    /**
     * 根据id获取一个Destination
     *
     * @param destinationid
     * @return
     */
    Optional<Destination> getDestinationById(long destinationid);

    //endregion


    //region ------------------------------------entertainment--------------------------------
    /**
     * 添加娱乐
     *
     * @param entertainment 待添加的娱乐
     * @return
     * @throws BaseSystemException
     */
    Optional<Entertainment> addEntertainment(Entertainment entertainment) throws BaseSystemException;

    /**
     * 更新娱乐
     *
     * @param entertainment 待更新的娱乐
     * @return
     */
    Optional<Entertainment> updateEntertainment(Entertainment entertainment) throws BaseSystemException;


    /**
     * 删除住宿点
     *
     * @param id     待删除娱乐id
     */
    void deleteEntertainment(long id);

    /**
     * 根据id获取娱乐
     *
     * @param id 查询id；
     * @return
     */
    Optional<Entertainment> getEntertainmentById(long id);

    /**
     * 按分页获取Entertainment,使用默认页面大小
     * @param page
     * @return
     */
    DomainPage<Entertainment> getEntertainmentsByPage(int page);


    /**
     * 按分页获取Entertainment
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<Entertainment> getEntertainmentsByPage(int page, int pageSize);

    /**
     * 根据娱乐类型，名字，目的地名字 模糊查找Entertainment
     * @param type 为空 则不加入查询条件
     * @param name
     * @param destinationName
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<Entertainment> getEntertainmentsByConditionPage(String type, String name, String destinationName, int page, int pageSize);
    //endregion

    //region ------------------------------------procurement--------------------------------
    /**
     * 功能：新增元素
     * 作者：石广路
     * 新增：2015-11-23 10:58
     * 修改：null
     *
     * @param procurement 元素实体（必须要提供有效的元素供应商信息，即osp）
     *                    <p>
     *                    return 元素实体
     */
    Optional<Procurement> addProcurement(Procurement procurement) throws BaseSystemException;

    /**
     * 功能：更新元素
     * 作者：石广路
     * 新增：2015-11-23 10:59
     * 修改：null
     *
     * @param procurement 元素实体
     *                    <p>
     *                    return 元素实体
     */
    Optional<Procurement> updateProcurement(Procurement procurement) throws BaseSystemException;

    /**
     * 功能：删除元素
     * 作者：石广路
     * 新增：2015-11-23 11:01
     * 修改：null
     *
     * @param id     主键ID
     */
    void deleteProcurement(Long id) throws BaseSystemException;

    /**
     * 功能：根据ID获取元素
     * 作者：石广路
     * 新增：2015-11-23 11:02
     * 修改：null
     *
     * @param id 主键ID
     *           <p>
     *           return procurement   元素实体
     */
    Optional<Procurement> getProcurementById(Long id);

    /**
     * 功能：根据资源类型、状态、名称、所在地等查询条件来获取元素分页列表
     * 作者：石广路
     * 新增：2015-11-23 11:05
     * 修改：null
     *
     * @param conditions       组合查询条件(resourceType：资源类型（如all，hotel，scenic，catering，entertainment）, online：上线状态（true表示上线，false表示下线），name：名称, destination.name: 所在地)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     *                         <p>
     *                         return 元素分页列表
     */
    DomainPage<Procurement> queryProcurementsPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 功能：根据资源类型、状态、名称、所在地、所属资源等查询条件来模糊查询元素分页列表
     *
     * @param conditions       组合查询条件(resourceType：资源类型（如all，hotel，scenic，catering，entertainment）, online：上线状态（true表示上线，false表示下线），name：名称, destination.name: 所在地,resourceName：所属资源名称)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @return 元素分页列表
     */
    DomainPage<ProcurementVo> queryProcurementVoPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize);
    //endregion

    //region ------------------------------------procurement stock--------------------------------
    /**
     * 功能：添加元素库存
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：库存信息中必须要包含有效的库存元素和具体某一天的日期，如果添加库存失败则会抛出BaseSystemException异常
     *
     * @param stockPrice   元素库存及价格信息
     *
     * return 库存实体对象
     */
    Optional<Stock> addProcurementStock(StockPrice stockPrice) throws BaseSystemException;

    /**
     * 功能：批量添加元素库存
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：库存信息中必须要包含有效的库存元素和具体某一时间段的日期，如果添加库存失败则会抛出BaseSystemException异常
     *
     * @param stockPrice   元素库存及价格信息
     *
     * return 库存实体对象
     */
    List<Stock> addProcurementStocks(StockPrice stockPrice) throws BaseSystemException;

    /**
     * 功能：更新元素库存
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：库存信息中必须要包含有效的库存元素和具体某一天的日期，如果添加库存失败则会抛出BaseSystemException异常
     *
     * @param stockPrice   元素库存及价格信息
     *
     * return 库存实体对象
     */
    Optional<Stock> updateProcurementStock(StockPrice stockPrice) throws BaseSystemException;

    /**
     * 功能：批量更新元素库存
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：库存信息中必须要包含有效的库存元素和具体某一时间段的日期，如果添加库存失败则会抛出BaseSystemException异常
     *
     * @param stockPrice   元素库存及价格信息
     *
     * return 库存实体对象
     */
    List<Stock> updateProcurementStocks(StockPrice stockPrice) throws BaseSystemException;

    /**
     * 功能：根据库存组合键来查找某一天上特定类型的元素库存价格信息
     * 作者：石广路
     * 新增：2016-4-22
     * 备注：库存价格信息列表通常包含了成人和儿童的库存价格信息，如果指定了库存价格类型，则只查询单个类型的库存价格信息，否则，进行不分区类型查找
     *
     * @param stockKey  库存联合主键
     * @param day       库存日期
     * @param type      库存价格类型，null - 不区分库存价格类型，其他值则表示是成人价还是儿童价
     *
     * return 元素库存价格信息列表
     */
    List<Price> getProcurementPrices(StockKey stockKey, Date day, Price.Type type);

    //endregion
}
