package com.simpletour.biz.resources;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Scenic;

import java.util.List;
import java.util.Map;

/**
 * Brief : 对景区的数据校验
 * Author: Hawk
 * Date  : 2016/3/22
 */
public interface IScenicBiz {

    /**
     * 新增景点
     * @author Hawk
     * @email  wangcan@simpletour.com
     * @Date   2016-03-21 17:32
     * @param scenic 景点实体
     * @return       景点实体
     * @throws BaseSystemException 表明Scenic为空
     */
    Scenic addScenic(Scenic scenic) throws BaseSystemException;

    /**
     * 删除景点
     * @author Hawk
     * @email  wangcan@simpletour.com
     * @date   2016-03-22 14:17
     * @param id 景点实体ID
     * @return
     * @throws BaseSystemException
     */
    void deleteScenic(long id) throws BaseSystemException;

    /**
     * 更新景点
     * @author Hawk
     * @Date   2016-03-21 17:32
     *
     * @param scenic 最新的景点实体
     * @return       最新的景点实体
     * @throws BaseSystemException 表明Scenic为空
     */
    Scenic updateScenic(Scenic scenic) throws BaseSystemException;

    /**
     * 根据ID查询景点
     * @author Hawk
     * @Date   2016-03-22 15：37
     * @param id 景点实体所对应的ID
     * @return  景点实体的Optional对象
     */
    Scenic findScenicById(long id);

    /**
     * 根据景点名称、所属景点、目的地等查询条件来获取景点列表, 精确查询
     * conditions中包含的字段名称必须为景点实体类中的属性名，如果涉及到关联实体，则字段名必须为{关联实体的属性名}.{实体类中的属性名}格式
     * @author Hawk
     * @Date   2016-03-21 17:32
     *
     * @param conditions       组合查询条件(name：景点名称, address：所属景点, destination.name: 目的地名称)
     *
     * @return       最新的景点实体
     * @throws BaseSystemException 表明Scenic为空
     */
    List<Scenic> findScenicsPagesByConditions(final Map<String, Object> conditions);

    /**
     * 根据景点名称、所属景点、目的地等查询条件来获取景点分页列表
     * conditions中包含的字段名称必须为景点实体类中的属性名，如果涉及到关联实体，则字段名必须为{关联实体的属性名}.{实体类中的属性名}格式
     * @author Hawk
     * @Date   2016-03-21 17:32
     *
     * @param conditions       组合查询条件(name：景点名称, address：所属景点, destination.name: 目的地名称)
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页面索引
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     *
     * @return       最新的景点实体
     * @throws BaseSystemException 表明Scenic为空
     */
    DomainPage<Scenic> queryScenicsPagesByConditions(final Map<String, Object> conditions
            , String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 检查某一个景点是否存在
     * @author Hawk
     * @email  wangcan@simpletour.com
     * @date   2016-03-22 14:21
     * @param id 景点实体ID
     * @return
     */
    boolean isExisted(long id) throws BaseSystemException;

}
