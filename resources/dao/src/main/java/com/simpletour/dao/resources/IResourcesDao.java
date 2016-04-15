package com.simpletour.dao.resources;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.domain.resources.Procurement;
import com.simpletour.domain.resources.UnionEntityKey;

import java.util.Map;

/**
 * 旅行资源Dao接口
 * <p>
 * Created by songfujie on 15/10/22.
 */
public interface IResourcesDao extends IBaseDao {
    /**
     * 功能：根据资源类型、资源名称和目的地ID来获取资源ID
     * 作者：石广路
     * 新增：2015-12-05 09:51
     * 修改：null
     *
     * @param unionEntityKey 包含有资源类型、资源名称和目的地的联合主键
     *
     * @return 资源ID
     */
    long getResourceId(UnionEntityKey unionEntityKey);

    /**
     * 功能：根据资源类型、资源名称和目的地ID来获取资源
     * 作者：石广路
     * 新增：2015-12-05 09:51
     * 修改：null
     *
     * @param unionEntityKey 包含有资源类型、资源名称和目的地的联合主键
     *
     * @return 资源ID
     */
    <T extends LogicalDeletableDomain> T getResourceById(UnionEntityKey unionEntityKey);

    /**
     * 功能：检查元素所依赖的资源是否存在
     * 作者：石广路
     * 新增：2015-12-05 15:42
     * 修改：null
     *
     * @param resourceType  元素所依赖的资源类型
     * @param resourceId    元素所依赖的资源ID
     *
     * @return true：资源存在，false：资源不存在
     */
    boolean hasDependableResource(Procurement.ResourceType resourceType, Long resourceId);

    /**
     * 功能：检查含有目的地的指定类型的资源在元素中是否存在依赖
     * 作者：石广路
     * 新增：2015-12-05 11:46
     * 修改：null
     *
     * @param tClass    可支持逻辑删除的资源类型
     * @param id        资源ID
     *
     * @return true：存在依赖，false：不存在依赖
     */
    <T extends LogicalDeletableDomain> boolean hasDependedByProcurement(Class<T> tClass, long id);

    /**
     * 功能：检查元素是否被其他库存托管对象所依赖
     * 作者：石广路
     * 新增：2015-12-05 11:46
     * 修改：null
     *
     * @param id        元素ID
     *
     * @return true：存在依赖，false：不存在依赖
     */
    boolean hasDependedByStockEntity(long id);

    /**
     * 功能：添加资源对应的依赖
     * 作者：石广路
     * 新增：2015-12-04 17:16
     * 修改：暂不实现
     *
     * @param domain 当前实体
     *
     * @return void
     */
    void addDependency(BaseDomain domain);

    /**
     * 功能：更新资源对应的依赖
     * 作者：石广路
     * 新增：2015-12-04 16:21
     * 修改：暂不实现
     *
     * @param domain 当前实体
     *
     * @return void
     */
    void updateDependency(BaseDomain domain);

    /**
     * 功能：删除资源对应的依赖
     * 作者：石广路
     * 新增：2015-12-04 16:21
     * 修改：暂不实现
     *
     * @param domain 当前实体
     *
     * @return void
     */
    void deleteDependency(BaseDomain domain);

    /**
     * 根据条件模糊查询元素列表
     *
     * @param fieldNameValueMap service层给出的查询条件（查询条件详见service接口）
     * @param orderByFiledName  排序字段
     * @param orderBy           升序还是降序
     * @param pageIndex         页面序号
     * @param pageSize          一页显示条数
     * @return 分页列表
     */
    <T extends BaseDomain> DomainPage queryEntitiesPagesByFieldList(Map<String, Object> fieldNameValueMap, String orderByFiledName, IBaseDao.SortBy orderBy, long pageIndex, long pageSize);

    /**
     * 模糊查询元素
     *
     * @param fieldNameValueMap
     * @param orderByFiledName
     * @param orderBy
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage queryProcurement(Map<String, Object> fieldNameValueMap, String orderByFiledName, SortBy orderBy, long pageIndex, long pageSize);
}
