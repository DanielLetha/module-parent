package com.simpletour.service.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.SaleApp;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Brief :  销售端服务接口
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/7 16:55
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public interface ISaleAppService {

    /**
     * 添加销售端对象
     *
     * @param saleApp
     * @return
     * @throws IllegalArgumentException
     */
    Optional<SaleApp> addSaleApp(SaleApp saleApp);


    /**
     * @param id
     * @throws BaseSystemException 要删除的不存在时抛出异常SaleApp_NOT_EXIST
     */
    void deleteSaleApp(Long id) throws BaseSystemException;

    /**
     * 修改一个分户相关数据
     *
     * @param saleApp
     * @return
     */
    Optional<SaleApp> updateSaleApp(SaleApp saleApp);


    /**
     * 分页模糊查询
     * fixed by xuhui: 更改方法名queryCateringsPagesByConditions->querySaleAppsPagesByConditions;
     *
     * @param conditions       组合查询条件(name：餐饮点类型, address：名称, destination.name: 目的地)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     * @return
     */
    DomainPage<SaleApp> querySaleAppPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 查询销售端列表
     *
     * @param query
     * @return
     */
    List<SaleApp> querySaleAppList(ConditionOrderByQuery query);

    /**
     * 根据id查询分户
     *
     * @param id
     * @return
     */
    Optional<SaleApp> getSaleAppById(long id);

}
