package com.simpletour.biz.resources;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.resources.Hotel;

import java.util.Map;

/**
 * @author XuHui/xuhui@simpletour.com
 *         Date: 2016/3/23
 *         Time: 9:55
 */
public interface IHotelBiz {
    /**
     * 添加住宿点
     *
     * @param hotel 待添加的住宿点
     * @return
     * @throws BaseSystemException 所在目的地有同名住宿地（SAME_NAME_RESOURCE_IS_EXISTING）
     */
    Hotel addHotel(Hotel hotel) throws BaseSystemException;

    /**
     * 更新住宿点
     *
     * @param hotel 待更新的住宿点
     * @return
     * @throws BaseSystemException 住宿点所在目的地为空或不存在（DESTINATION_NOT_EXIST）、
     *                             所在目的地有同名住宿地（SAME_NAME_RESOURCE_IS_EXISTING）
     */
    Hotel updateHotel(Hotel hotel) throws BaseSystemException;

    /**
     * 依据条件查询住宿点
     *
     * @param conditions       查询条件
     * @param orderByFiledName 排序字段
     * @param orderBy          排序方向
     * @param pageIndex        分页页码
     * @param pageSize         分页大小
     * @param byLike           是否模糊查询
     * @return
     */
    DomainPage<Hotel> queryHotelsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike);

    /**
     * 删除住宿点
     *
     * @param id     待删除住宿点id
     * @throws BaseSystemException 存在元素依赖于该住宿点（CANNOT_DEL_DEPENDENT_RESOURCE）；
     */
    void deleteHotel(long id) throws BaseSystemException;

    /**
     * 根据id获取住宿点
     *
     * @param id 查询id；
     * @return
     */
    Hotel getHotelById(long id);

    /**
     * 根据id判断住宿点是否存在
     * @param id
     * @return
     * @throws BaseSystemException id为空（EMPTY_ENTITY）
     */
    boolean isExisted(Long id) throws BaseSystemException;
}
