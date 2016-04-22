package com.simpletour.biz.sale;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.SaleApp;

import java.util.List;
import java.util.Map;

/**
 * @Brief :  销售端biz接口
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/6 19:47
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public interface ISaleAppBiz {
    /**
     * 添加销售端
     * @param saleApp
     * @return
     * @throws BaseSystemException
     */
    SaleApp addSaleApp(SaleApp saleApp) throws BaseSystemException;

    /**
     * 更新销售端
     * @param saleApp
     * @return
     * @throws BaseSystemException
     */
    SaleApp updateSaleApp(SaleApp saleApp) throws BaseSystemException;

    /**
     * 删除销售端
     * @param id
     * @param pseudo（true:逻辑删除，false:物理删除）
     * @throws BaseSystemException
     */
    void deleteSaleApp(long id, boolean pseudo) throws BaseSystemException;

    /**
     * 根据id查询销售端
     * @param id
     * @return
     */
    SaleApp findSaleAppById(long id);

    /**
     * 根据条件查询销售端
     * @param conditions
     * @param orderByFiledName
     * @param orderBy
     * @param pageIndex
     * @param pageSize
     * @param byLike
     * @return
     */
    DomainPage<SaleApp> querySaleAppByCondition(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 根据条件查询销售端的list
     * @param query
     * @return
     */
    List<SaleApp> querySaleAppList(ConditionOrderByQuery query);
    /**
     * 判断销售端是否存在
     * @param id
     * @return
     */
    boolean isExist(long id);
}
