package com.simpletour.biz.traveltrans;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.traveltrans.BusNoPlan;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Mario on 2015/12/2.
 */
public interface IBusNoPlanBiz {

    /**
     * 增加busNoPlan信息
     *
     * @param busNoPlan
     * @return
     */
    BusNoPlan addBusNoPlan(BusNoPlan busNoPlan);

    /**
     * 根据id删除busNoPlan
     *
     * @param id 主键id
     * @return
     */
    //TODO.....存在级联关系，暂时不做
    boolean deleteBusNoPlanById(Long id);

    /**
     * 更新busNoPlan
     *
     * @param busNoPlan
     * @return
     */
    BusNoPlan updateBusNoPlan(BusNoPlan busNoPlan);

    /**
     * 更具id获取BusNoPlan
     * @param id
     * @return
     */
    BusNoPlan getBusNoPlanById(Long id);


    /**
     * 根据查询条件精确查询busNoPlan
     *
     * @param conditions       查询条件
     * @param orderByFieldName 根据哪个字段排序
     * @param sortBy           DESC:降序，ASC：升序
     * @return
     */
    List<BusNoPlan> findBusNoPlanByConditions(Map<String, Object> conditions, String orderByFieldName, IBaseDao.SortBy sortBy);

    /**
     * 根据查询条件精确查询busNoPlan，查询条件包括开始时间和结束时间
     *
     * @param conditions       查询条件
     * @param startDate        开始时间
     * @param endDate          结束时间
     * @param orderFyFieldName 根据哪个字段进行排序
     * @param sortBy           DESC:降序，ASC:升序
     * @return
     */
    List<BusNoPlan> findBusNoPlanByConditionsAndDate(final Map<String, Object> conditions, Date startDate, Date endDate, String orderFyFieldName, IBaseDao.SortBy sortBy);


    /**
     * 根据查询条件查询busNoPlan，支持模糊查询、分页
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段排序
     * @param orderBy          DESC：降序，ASC:升序
     * @param pageIndex        页码
     * @param pageSize         分页大小
     * @param byLike           true:支持模糊查询，false:支持精确查询
     * @return
     */
    DomainPage<BusNoPlan> findBusNoPlanPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike);

    /**
     * 获取车辆剩余座位
     *
     * @param conditions       查询条件
     * @return
     */
    int findAvailableBusSeat(final Map<String, Object> conditions);

    /**
     * 依据车次排班id判断排班是否存在
     * @param id
     * @return
     */
    boolean isExisted(Long id);

    /**
     * 判断车辆是否可用
     * @param license
     * @param start
     * @param end
     * @return
     */
    boolean isBusAvailable(String license, Integer start, Integer end);

    /**
     * 判断行车助理是否存在，并在start-end时间段内可用
     * @param name
     * @param start
     * @param end
     * @return
     */
    boolean isAssistantAvailable(String name, Integer start, Integer end);
}
