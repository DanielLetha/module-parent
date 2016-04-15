package com.simpletour.biz.traveltrans;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.traveltrans.Bus;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mario on 2015/11/21.
 */
public interface IBusBiz {

    /**
     * 增加车辆
     *
     * @param bus bus实体类
     * @return
     */
    Optional<Bus> addBus(Bus bus);

    /**
     * 删除车辆
     *
     * @param bus bus实体类
     * @return
     */
    Boolean deleteBus(Bus bus);

    /**
     * 更新bus信息
     *
     * @param bus
     * @return
     */
    Optional<Bus> updateBus(Bus bus);

    /**
     * 根据查询条件查询bus列表信息，支持模糊查询、分页
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页码
     * @param pageSize         分页大小
     * @param byLike           true：支持模糊查询，false：不支持模糊查询
     * @return
     */
    DomainPage<Bus> findBusPageByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike);

    /**
     * 根据查询条件精确查询bus列表信息
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @return
     */
    List<Bus> findBusByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy);

    /**
     * 根据车辆id查询车辆信息
     *
     * @param id 主键id
     * @return
     */
    Optional<Bus> findBusById(Long id);

    /**
     * 根据时间查询未被排班的可用的车辆
     *
     * @param license 车牌号
     * @param busNoId 车次id 必填非空
     * @param day     添加车次计划的当天
     * @return
     */
    DomainPage<Bus> findAvailableBus(String license, Long busNoId, Date day, int pageIndex, int pageSize);

    /**
     * 判断车辆是否存在
     * @param busId 必填非空
     * @return true/false
     */
    Boolean isExisted(Long busId);

    /**
     * 判断车辆是否可用
     * @param busId 必填非空
     * @return true/false
     */
    Boolean isAvailable(Long busId);
}
