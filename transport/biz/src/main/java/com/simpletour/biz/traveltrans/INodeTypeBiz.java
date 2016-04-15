package com.simpletour.biz.traveltrans;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.traveltrans.NodeType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author:  WangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/3/23
 * since :  2.0.0-SNAPSHOT
 */
public interface INodeTypeBiz {
    /**
     * 根据id获取行程节点类型
     *
     * @param id 主键id
     * @return
     */
    Optional<NodeType> findNodeTypeById(Long id);

    /**
     * 根据查询条件查询nodetype
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC:降序，ASC:升序
     * @param pageIndex        页码
     * @param pageSize         分页大小
     * @param byLike           true:支持模糊查询，false:支持精确查询
     * @return
     */
    DomainPage<NodeType> findNodeTypePageByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike);

    /**
     * 根据查询条件精确查询NodeType列表
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param sortBy           DESC：降序，ASC:升序
     * @return
     */
    List<NodeType> findNodeTypeByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy);

    /**
     * 根据Id判断nodeType是否存在，这里没有软删除
     * @param id
     * @return
     * @throws BaseSystemException id为空时抛出该异常
     */
    boolean isExisted(Long id) throws BaseSystemException;

    /**
     *  根据NodeType判断NodeType是否存在
     *
     *  @see #isExisted(Long)
     *
     * @param nodeType 必须包含有效的主键
     * @return 存在则返回true，否则返回false
     * @throws BaseSystemException NodeType或者NodeType.getId()为空时抛出该异常
     */
    boolean isExisted(NodeType nodeType) throws BaseSystemException;
}
