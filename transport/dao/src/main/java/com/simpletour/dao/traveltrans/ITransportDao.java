package com.simpletour.dao.traveltrans;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.product.TourismRouteLine;
import com.simpletour.domain.traveltrans.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Mario on 2015/12/2.
 */
public interface ITransportDao extends IBaseDao {

    /**
     * 根据条件查询分页查询Line
     * @param condition
     *          depart          String          出发地
     *          arrive          String          目的地
     *          days            int             天数
     *          name            String          名字
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage findLinesPageByConditions(AndConditionSet condition, int page, int pageSize);

    /**
     * 根据lineId删除busNoSerial
     *
     * @param id
     */
    void deleteBusNoSerialByLineId(Long id);

    /**
     * 根据busNo查询nodeList
     *
     * @param busNo
     * @return
     */
    List<Node> findNodeListByBusNo(BusNo busNo);

    /**
     * @param conditions
     * @param startDate
     * @param endDate
     * @param orderFyFieldName
     * @param sortBy
     * @return
     */
    List<BusNoPlan> findBusNoPlanByConditionsAndDate(Map<String, Object> conditions, Date startDate, Date endDate, String orderFyFieldName, IBaseDao.SortBy sortBy);

    /**
     * 查询车次在offday-uptoday时间段内的所有班次信息
     * @param busNo
     * @param offday
     * @param uptoDay
     * @return
     */
    List<BusNoPlan> getBusNoPlanByBusNoAndSpan(BusNo busNo, Date offday, Date uptoDay);

    /**
     * 查询车次在day这一天的所有班次信息
     *
     * @param busNo
     * @param day
     * @return
     */
    List<BusNoPlan> getBusNoPlanByBusNoAndDate(BusNo busNo, Date day);

    /**
     * 获取一个TourismRoute再start-end这段时间内每天的声誉座位总量
     * @param tourismRouteLine
     * @param start
     * @param end
     * @return
     */
    Map<Date, Integer> findTourismRouteBusNoPlanCapacity(TourismRouteLine tourismRouteLine, Date start, Date end);

    /**
     * 查询一个TourismRoute对应的BusNo在 指定天的所有的班次的剩余总容量
     *
     * @param tourismRouteLine
     * @param day
     * @return
     */
    Integer findTourismRouteBusNoPlanCapacity(TourismRouteLine tourismRouteLine, Date day);

    /**
     * 查询可用的车辆
     *
     * @param license   车牌号
     * @param start     开始时间
     * @param end       结束时间
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    DomainPage<Bus> findAvailableBus(String license, Integer start, Integer end, int pageIndex, int pageSize);

    /**
     * 判断当前车辆是否可用
     *
     * @param license 车牌号
     * @param start   开始时间
     * @param end     结束时间
     * @return
     */
    boolean isBusAvailable(String license, Integer start, Integer end);


    /**
     * 查询可用的行车助理
     *
     * @param assistantName 行车助理的名字
     * @param start         开始时间
     * @param end           结束时间
     * @param pageIndex     页码
     * @param pageSize      分页大小
     * @return
     */
    DomainPage<Assistant> findAvailableAssistant(String assistantName, Integer start, Integer end, int pageIndex, int pageSize);

    /**
     * 判断该行车助理在车次计划下是否可用
     *
     * @param assistantName 姓名
     * @param start         开始时间
     * @param end           结束时间
     * @return
     */
    boolean isAssistantAvailable(String assistantName, Integer start, Integer end);

    /**
     * 查询可用的busNo
     *
     * @param no        车次号
     * @param pageIndex 分页索引
     * @param pageSize  分页大小
     * @return
     */
    DomainPage<BusNo> findAvailableBusNo(String no, int pageIndex, int pageSize);

    /**
     * 根据busNoIds查询节点信息
     *
     * @param busNoIds         busNoId列表
     * @param orderByFieldName 根据哪个字段进行排序
     * @param sortBy           ASC:升序,DESC:降序
     * @return
     */
    DomainPage<Node> findNodePageByBusNoId(final List<Long> busNoIds, String orderByFieldName, IBaseDao.SortBy sortBy, int pageIndex, int pageSize);
}
