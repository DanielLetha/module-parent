package com.simpletour.biz.resources;

import com.simpletour.biz.resources.vo.ProcurementVo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Procurement;

import java.util.List;
import java.util.Map;

/**
 * Brief : 元素的业务检查接口
 * Author: Hawk
 * E-mail: wangcan@simpletour.com
 * Date  : 2016/3/24
 */
public interface IProcurementBiz {

    /**
     * 功能：新增元素
     * 作者：Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @param procurement 元素实体（必须要提供有效的元素供应商信息，即osp）
     * @return 元素实体
     */
    Procurement addProcurement(Procurement procurement) throws BaseSystemException;

    /**
     * 功能：更新元素
     * 作者：Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @param procurement 元素实体
     * @return 元素实体
     */
    Procurement updateProcurement(Procurement procurement) throws BaseSystemException;

    /**
     * 功能：删除元素(伪删除)
     * 作者：Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @param id     主键ID
     * @resturn void
     */
    void deleteProcurement(long id) throws BaseSystemException;

    /**
     * 功能：根据ID获取元素
     * 作者：Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @param id 主键ID
     * @return procurement 元素实体
     */
    Procurement getProcurementById(long id);

    /**
     * 功能：根据资源类型、状态、名称、所在地等查询条件来获取元素列表(精确查找)
     * 作者：Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @param conditions   组合查询条件(resourceType：资源类型（如all，hotel，scenic，catering，entertainment）, online：上线状态（true表示上线，false表示下线），name：名称, destination.name: 所在地)
     * @return 元素列表
     */
    List<Procurement> findProcurementsByConditions(final Map<String, Object> conditions);

    /**
     * 功能：根据资源类型、状态、名称、所在地、所属资源等查询条件来模糊查询元素分页列表
     * 作者: Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @param conditions       组合查询条件(resourceType：资源类型（如all，hotel，scenic，catering，entertainment）, online：上线状态（true表示上线，false表示下线），name：名称, destination.name: 所在地,resourceName：所属资源名称)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @return 元素分页数据
     */
    DomainPage<Procurement> queryProcurementsPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 功能：根据资源类型、状态、名称、所在地、所属资源等查询条件来模糊查询元素分页列表
     * 作者: Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @param conditions       组合查询条件(resourceType：资源类型（如all，hotel，scenic，catering，entertainment）, online：上线状态（true表示上线，false表示下线），name：名称, destination.name: 所在地,resourceName：所属资源名称)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @return 元素分页数据
     */
    DomainPage<ProcurementVo> queryProcurementVoPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize);


    /**
     * 功能：检查元素是否存在
     * 作者: Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @see #isAvailable(long)
     *
     * @param id 元素ID
     * @return
     */
    boolean isExisted(long id);

    /**
     * 功能：检查元素是否可用
     * 作者: Hawk
     * 新增：2016-03-24
     * 修改：null
     *
     * @see #isExisted(long)
     *
     * @param id
     * @return
     */
    boolean isAvailable(long id);

}
