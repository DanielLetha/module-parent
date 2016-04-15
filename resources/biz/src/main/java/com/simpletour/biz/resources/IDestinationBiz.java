package com.simpletour.biz.resources;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Destination;

/**
 * @author XuHui/xuhui@simpletour.com
 *         Date: 2016/3/23
 *         Time: 10:00
 */
public interface IDestinationBiz {
    /**
     * 添加目的地，dest.area不存在时，抛出异常 AREA_NOT_EXIST
     *
     * @param dest
     * @return
     */
    Destination addDestination(Destination dest) throws BaseSystemException;

    /**
     * 更新Destination，同一地区存在同名目的地（DESTINATION_NAME_UNDER_SAME_AREA_MUST_BE_UNIQUE）
     *
     * @param dest
     * @return
     */
    Destination updateDestination(Destination dest) throws BaseSystemException;

    /**
     * @param id
     */
    void deleteDestination(long id) throws BaseSystemException;

    /**
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage<Destination> getDestinationsByPage(int page, int pageSize);

    /**
     * 根据目的地名字，所在地区 分页查找目的地
     *
     * @param name     目的地名称
     * @param area     所在地区
     * @param page     页码
     * @param pageSize 页面大小
     * @return
     */
    DomainPage<Destination> getDestionationsByConditonPage(String name, String area, int page, int pageSize) throws BaseSystemException;

    /**
     * @param id
     * @return
     */
    Destination getDestinationById(long id);

    /**
     * 根据id判断目的地是否存在
     * @param id
     * @return
     * @throws BaseSystemException  目的地不存在（EMPTY_ENTITY）
     */
    boolean isExisted(Long id) throws BaseSystemException;

}
