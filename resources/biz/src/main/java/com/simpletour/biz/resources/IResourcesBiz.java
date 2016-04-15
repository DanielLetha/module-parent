package com.simpletour.biz.resources;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.commons.data.exception.BaseSystemException;
import resources.Area;
import resources.Hotel;
import resources.IUnionEntityKey;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 文件描述：资源模块业务层处理接口
 * 创建人员：石广路
 * 创建日期：2015/12/4 14:51
 * 备注说明：null
 */
public interface IResourcesBiz {
    /**
     * 功能：根据资源类型、资源名称和目的地ID来获取资源ID
     * 作者：石广路
     * 新增：2015-12-05 09:51
     * 修改：null
     *
     * @param unionEntityKey 包含有资源和目的地的联合主键
     *
     * @return 资源ID
     */
    long getResourceId(IUnionEntityKey unionEntityKey);

    /**
     * 功能：根据主键ID获取指定类型的资源实体
     * 作者：石广路
     * 新增：2015-12-05 9:56
     * 修改：null
     *
     * @param tClass    可支持逻辑删除的资源类型
     * @param id        资源ID
     *
     * @return 资源实体
     */
    <T extends LogicalDeletableDomain> Optional<T> getResourceById(Class<T> tClass, long id);

    /**
     * 功能：根据目的地ID获取指定类型的资源列表
     * 作者：石广路
     * 新增：2015-12-05 9:56
     * 修改：null
     *
     * @param tClass    可支持逻辑删除的资源类型
     * @param destId    目的地ID
     *
     * @return 资源实体
     */
    <T extends LogicalDeletableDomain> List<T> getResourcesListByDestId(Class<T> tClass, long destId);

    /**
     * 功能：根据主键ID删除指定类型的资源
     * 作者：石广路
     * 新增：2015-12-05 9:55
     * 修改：null
     *
     * @param tClass    可支持逻辑删除的资源类型
     * @param id        资源ID
     * @param pseudo    true：伪删除，false：真删除
     *
     * @return void
     */
    <T extends LogicalDeletableDomain> void deleteResource(Class<T> tClass, long id, boolean pseudo);

    /**
     * 功能：新增住宿点
     * 作者：石广路
     * 新增：2015-11-21 13:54
     * 修改：null
     *
     * @param hotel 住宿点实体
     *              <p>
     *              return 住宿点实体
     */
    Optional<Hotel> addHotel(Hotel hotel) throws BaseSystemException;

    /**
     * 功能：更新住宿点
     * 作者：石广路
     * 新增：2015-11-21 11:48
     * 修改：null
     *
     * @param hotel 住宿点实体
     *              <p>
     *              return 住宿点实体
     */
    Optional<Hotel> updateHotel(Hotel hotel) throws BaseSystemException;

    /**
     * 功能：根据住宿点酒店类型、名称、目的地等查询条件来获取住宿点分页列表
     * 作者：石广路
     * 新增：2015-11-21 11:55
     * 修改：注意，conditions中包含的字段名称必须为住宿点实体类中的属性名，如果涉及到关联实体，则字段名必须为{关联实体的属性名}.{实体类中的属性名}格式
     *
     * @param conditions       组合查询条件(type：酒店类型（如all，hotel，inn，folk_house，others）, name：名称, destination.name: 目的地名称)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     *                         <p>
     *                         return 住宿点分页列表
     */
    DomainPage<Hotel> queryHotelsPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 根据id获取area
     * @param id
     * @return
     */
    Area getAreaById(Long id);
}
