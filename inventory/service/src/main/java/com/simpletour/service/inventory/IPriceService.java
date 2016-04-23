package com.simpletour.service.inventory;

import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.Price;

import java.util.List;
import java.util.Optional;

/**
 * 文件描述：库存价格服务层接口
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/21 9:44
 * 备注说明：仅提供针对库存价格信息的增删改等功能接口，涉及到关联查询相关的功能接口放在IStockQueryBiz接口。
 *
 * @since 2.0-SNAPSHOT
 */
public interface IPriceService {
    /**
     * 功能：添加库存价格信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：null
     *
     * @param price 库存价格实体
     *
     * return 库存价格实体
     */
    Optional<Price> addPrice(Price price) throws BaseSystemException;

    /**
     * 功能：批量添加库存价格信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：null
     *
     * @param prices 库存价格实体列表
     *
     * return 库存价格实体列表
     */
    List<Price> addPrices(List<Price> prices) throws BaseSystemException;

    /**
     * 功能：更新库存价格信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：null
     *
     * @param price 库存价格实体
     *
     * return 库存实体
     */
    Optional<Price> updatePrice(Price price) throws BaseSystemException;

    /**
     * 功能：批量更新库存价格信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：null
     *
     * @param prices 库存价格实体列表
     *
     * return 库存价格实体列表
     */
    List<Price> updatePrices(List<Price> prices) throws BaseSystemException;

    /**
     * 功能：删除库存价格信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：如果删除失败则抛出BaseSystemException异常
     *
     * @param id     主键ID
     *
     * return void
     */
    //void deletePrice(Long id) throws BaseSystemException;

    /**
     * 功能：批量删除库存价格信息
     * 作者：石广路
     * 新增：2016-4-21
     * 修改：null
     * 备注：如果删除失败则抛出BaseSystemException异常
     *
     * @param ids    库存ID列表
     *
     * return void
     */
    //void deletePrices(List<Long> ids);
}
