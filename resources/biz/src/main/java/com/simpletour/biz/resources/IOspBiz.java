package com.simpletour.biz.resources;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import resources.OfflineServiceProvider;

/**
 * @Brief :  供应商的biz接口
 * @Author:  liangfei/liangfei@simpletour.com
 * @Date  :  2016/3/23 20:25
 * @Since :  2.0.0-SNAPSHOT
 * @Remark:  无
 */
public interface IOspBiz {

    /**
     * 添加供应商
     *
     * @param offlineServiceProvider
     * @return
     * @throws BaseSystemException
     */
    OfflineServiceProvider addOfflineServiceProvider(OfflineServiceProvider offlineServiceProvider) throws BaseSystemException;

    /**
     * 删除供应商
     *
     * @param id
     * @throws BaseSystemException
     */
    void deleteOfflineServiceProvider(long id) throws BaseSystemException;

    /**
     * 更新供应商
     *
     * @param offlineServiceProvider
     * @return
     * @throws BaseSystemException
     */
    OfflineServiceProvider updateOfflineServiceProvider(OfflineServiceProvider offlineServiceProvider) throws BaseSystemException;

    /**
     * 分页查询供应商
     *
     * @param isDel
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<OfflineServiceProvider> getOfflineServiceProvidersByPage(Boolean isDel, int page, int pageSize);

    /**
     * 按照套件分页查询供应商
     *
     * @param name
     * @param isDel
     * @param page
     * @param pageSize
     * @return
     * @throws BaseSystemException
     */
    DomainPage<OfflineServiceProvider> getOfflineServiceProvidersByConditionPage(String name, Boolean isDel, int page, int pageSize) throws BaseSystemException;

    /**
     * 根据id查询供应商
     *
     * @param id
     * @return
     */
    OfflineServiceProvider getOfflineServiceProviderById(long id);

    /**
     * 根据osp id判断osp是否存在
     * @param id
     * @return
     */
    boolean isExisted(long id);


}
