package com.simpletour.biz.resources;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Entertainment;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：娱乐接口
 * Date: 2016/3/23
 * Time: 15:23
 */
public interface IEntertainmentBiz {

    /**
     * 添加娱乐
     *
     * @param entertainment 待添加的娱乐
     * @return
     * @throws BaseSystemException 所在目的地有同名娱乐（SAME_NAME_RESOURCE_IS_EXISTING）
     */
    Entertainment addEntertainment(Entertainment entertainment) throws BaseSystemException;

    /**
     * 更新娱乐
     *
     * @param entertainment 待更新的娱乐
     * @return
     * @throws BaseSystemException 娱乐所在目的地为空或不存在（DESTINATION_NOT_EXIST）、
     *                             所在目的地有同名娱乐（SAME_NAME_RESOURCE_IS_EXISTING）
     */
    Entertainment updateEntertainment(Entertainment entertainment) throws BaseSystemException;

    /**
     * 删除住宿点
     *
     * @param id     待删除娱乐id
     * @throws BaseSystemException 存在元素依赖于该娱乐（CANNOT_DEL_DEPENDENT_RESOURCE）；
     */
    void deleteEntertainment(long id) throws BaseSystemException;

    /**
     * 根据id获取娱乐
     *
     * @param id 查询id；
     * @return
     */
    Entertainment getEntertainmentById(long id);

    /**
     * 根据id判断娱乐是否存在
     * @param id
     * @return
     * @throws BaseSystemException id为空（EMPTY_ENTITY）
     */
    boolean isExisted(Long id);

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
    DomainPage<Entertainment> getEntertainmentsByConditionPage(String type, String name, String destinationName, int page, int pageSize) throws BaseSystemException;
}
