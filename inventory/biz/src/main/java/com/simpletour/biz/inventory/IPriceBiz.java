package com.simpletour.biz.inventory;

import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.Price;

import java.util.Optional;

/**
 * 文件描述：库存价格业务类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/20 21:50
 * 备注说明：提供用以处理与库存各种类型价格数据相关的业务接口
 * @since 2.0-SNAPSHOT
 */
public interface IPriceBiz {
    /**
     * 功能：检查库存价格信息是否存在
     * 作者：石广路
     * 新增：2016-4-20
     * 备注：检查同种类型的库存价格信息在相同的日期上是否已存在，如果库存价格信息无效则抛出BaseSystemException
     *
     * @param price 库存价格实体
     *
     * return true - 存在，false - 不存在
     */
    boolean isExisted(Price price) throws BaseSystemException;

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

//    /**
//     * 功能：删除库存价格信息
//     * 作者：石广路
//     * 新增：2016-4-21
//     * 修改：null
//     * 备注：如果删除失败则抛出BaseSystemException异常
//     *
//     * @param id     主键ID
//     *
//     * return void
//     */
//    //void deletePrice(Long id) throws BaseSystemException;
//    void deletePrice(Price price);

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
    //void deletePrices(List<Long> ids) throws BaseSystemException;

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
    //Optional<Price> getPriceById(Long id);
}
