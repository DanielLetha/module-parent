package com.simpletour.biz.resources;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Catering;

import java.util.Map;

/**
 * @Brief :  餐饮点的操作
 * @Author:  liangfei/liangfei@simpletour.com
 * @Date  :  2016/3/23 11:06
 * @Since :  2.0.0-SNAPSHOT
 * @Remark:  无
 */
public interface ICateringBiz {
    /**
     * 添加餐饮点
     * @param catering
     * @return
     * @throws BaseSystemException
     */
    Catering addCatering(Catering catering) throws BaseSystemException;

    /**
     * 更新餐饮点
     * @param catering
     * @return
     * @throws BaseSystemException
     */
    Catering updateCatering(Catering catering) throws BaseSystemException;

    /**
     * 根据id删除餐饮点
     * @param id
     * @param pseudo
     * @throws BaseSystemException
     */
    void deleteCatering(long id, boolean pseudo) throws BaseSystemException;

    /**
     * 按照条件分页查询餐饮点
     * @param conditions
     * @param orderByFiledName
     * @param orderBy
     * @param pageIndex
     * @param pageSize
     * @param byLike
     * @return
     */
    DomainPage<Catering> queryCateringsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 根据id查询餐饮点
     * @param id
     * @return
     */
    Catering getCateringById(long id);

    /**
     * 判断餐饮点是否存在
     * @param id
     * @return
     */
    boolean isExisted(long id);



}
