package com.simpletour.biz.traveltrans;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.traveltrans.NodeStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mario on 2015/12/3.
 */
public interface INodeStatusBiz {
    /**
     * 增加节点信息
     *
     * @param nodeStatuss
     * @return
     */
    Optional<NodeStatus> addNodeStatus(NodeStatus nodeStatuss);

    /**
     * 根据查询条件查询nodeStatus信息,支持模糊查询、分页
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param sortBy           DESC：降序，ASC：升序
     * @param pageIndex        页码
     * @param pageSize         分页大小
     * @param byLike           true：支持模糊查询，false：支持精确查询
     * @return
     */
    DomainPage<NodeStatus> findNodeStatusPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike);

    /**
     * 根据查询条件精确查询nodeStatus信息
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param sortBy           DESC:降序，ASC：升序
     * @return
     */
    List<NodeStatus> findNodeStatusByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy);
}
