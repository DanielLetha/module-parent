package com.simpletour.service.traveltrans;


import com.simpletour.biz.traveltrans.bo.BusNoBo;
import com.simpletour.biz.traveltrans.bo.BusNoPlanBo;
import com.simpletour.biz.traveltrans.bo.DomainPageBo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.traveltrans.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 车次模块业务处理接口
 * Created by Mario on 2015/11/20.
 */
public interface ITravelTransportService {

    /**===================================BUS====================================**/
    /**
     * 增加车辆：非空、座位布局可用、车牌号可用校验
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
    boolean deleteBus(Bus bus);

    /**
     * 更新车辆信息
     *
     * @param bus bus实体类
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
     * 根据时间查询未被排班的可用的车辆
     *
     * @param license 车牌号
     * @param busNoId 车次id 必填非空
     * @param day     添加车次计划的当天
     * @return
     */
    DomainPage<Bus> findAvailableBus(String license, Long busNoId, Date day, int pageIndex, int pageSize);

    /**
     * 根据车辆id查询车辆信息
     *
     * @param id 主键id
     * @return
     */
    Optional<Bus> findBusById(Long id);

    /**===================================BUS_NO====================================**/
    /**
     * 增加车次以及车次行程节点
     *
     * @param busNoBo busNoBo业务实体
     * @return
     */
    Optional<BusNo> addBusNo(BusNoBo busNoBo);

    /**
     * 删除车次
     *
     * @param busNo busNo对象实体
     * @return
     */
    boolean deleteBusNo(BusNo busNo);

    /**
     * 更新车次及车次行程
     *
     * @param busNoBo busNo对象实体
     * @return
     */
    Optional<BusNo> updateBusNo(BusNoBo busNoBo);

    /**
     * 根据no获取车次及行程信息
     *
     * @param no    车次编号
     * @param eager 是否需要级联查询关联的node，true为需要
     * @return
     */
    Optional<BusNo> findBusNoByNo(String no, Boolean eager);

    /**
     * 根据车次id获取车次信息
     *
     * @param id 主键id
     * @return
     */
    Optional<BusNo> findBusNoById(Long id);

    /**
     * 根据查询条件查询busNo列表，支持模糊查询、分页
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param orderBy          DESC：降序，ASC：升序
     * @param pageIndex        页码
     * @param pageSize         分页大小
     * @param byLike           true:支持模糊查询，false:支持精确查询
     * @return
     */
    DomainPage<BusNo> findBusNoPageByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike);


    /**
     * 根据查询条件查询busNo列表
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param sortBy           DESC：降序，ASC：升序
     * @return
     */
    List<BusNo> findBusNoByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy);


    /**
     * 查询线路上可用的车次
     *
     * @param no        车次号
     * @param pageIndex 分页索引
     * @param pageSize  分页大小
     * @return
     */
    DomainPage<BusNo> findAvailableBusNo(String no, int pageIndex, int pageSize);

    /**===================================BUS_NO_PLAN====================================**/

    /**
     * 增加busNoPlan信息
     *
     * @param busNoPlan busNoPlan对象实体
     * @return
     */
    Optional<BusNoPlan> addBusNoPlan(BusNoPlan busNoPlan);

    /**
     * 更新busNoPlan
     *
     * @param busNoPlan
     * @return
     */
    Optional<BusNoPlan> updateBusNoPlan(BusNoPlan busNoPlan);

    /**
     * 根据id删除busNoPlan
     *
     * @param id 主键id
     * @return
     */
    boolean deleteBusNoPlanById(Long id);

    /**
     * 根据id获取BusNoPlan
     *
     * @param id 主键id
     * @return
     */
    Optional<BusNoPlanBo> findBusNoPlanById(Long id);

    /**
     * 根据查询条件精确查询busNoPlan
     *
     * @param conditions       查询条件
     * @param orderByFieldName 根据哪个字段排序
     * @param sortBy           DESC:降序，ASC：升序
     * @return
     */
    List<BusNoPlanBo> findBusNoPlanByConditions(final Map<String, Object> conditions, String orderByFieldName, IBaseDao.SortBy sortBy);

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
    List<BusNoPlanBo> findBusNoPlanByConditionsAndDate(final Map<String, Object> conditions, Date startDate, Date endDate, String orderFyFieldName, IBaseDao.SortBy sortBy);

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
    DomainPageBo<BusNoPlanBo> findBusNoPlanPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike);

    /**
     * 获取车辆剩余座位
     *
     * @param conditions 查询条件
     * @return
     */
    int findAvailableBusSeat(final Map<String, Object> conditions);
    /**===================================LINE====================================**/
    /**
     * 添加新路线
     *
     * @param line line对象实体
     * @return
     */
    Optional<Line> addLine(Line line);

    /**
     * 更新线路信息
     *
     * @param line line对象实体
     * @return
     */
    Optional<Line> updateLine(Line line);

    /**
     * 删除线路
     *
     * @param line line对象实体
     * @return
     */
    boolean deleteLineById(Line line);

    /**
     * 根据id获取线路信息
     *
     * @param id
     * @return
     */
    Optional<Line> findLineById(Long id);

    /**
     * 根据查询条件查询line列表，精确查询
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param sortBy           DESC:降序，ASC:升序
     * @return
     */
    List<Line> findLineByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy);

    /**
     * 根据查询条件查询line列表信息，支持分页、模糊查询
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param sortBy           DESC:降序，ASC:升序
     * @param pageIndex        页码
     * @param pageSize         分页大小
     * @param byLike           true:支持模糊查询，false:支持精确查询
     * @return
     */
    DomainPage<Line> findLinePagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike);

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

    /**===================================NODE_STATUS====================================**/
    /**
     * 增加节点状态信息
     *
     * @param nodeStatus
     * @return
     */
    Optional<NodeStatus> addNodeStatus(NodeStatus nodeStatus);

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
    /**===================================NODE_TYPE====================================**/
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
     * ===================================SEAT_LAYOUT====================================
     **/

    /**
     * 增加座位布局
     *
     * @param seatLayout seatLayout对象实体
     * @return
     */
    Optional<SeatLayout> addSeatLayout(SeatLayout seatLayout);

    /**
     * 删除座位布局，该操作会级联删除该布局包含的座位
     *
     * @param seatLayout seatLayout对象实体
     * @return
     */
    Boolean deleteSeatLayout(SeatLayout seatLayout);

    /**
     * 更新座位布局
     *
     * @param seatLayout
     * @return
     */
    Optional<SeatLayout> updateSeatLayout(SeatLayout seatLayout);

    /**
     * 根据座位id查询座位布局信息,包括座位及每个座位包含的类型
     *
     * @param id 主键id
     * @return
     */
    Optional<SeatLayout> findLayoutById(Long id);

    /**
     * 根据查询条件获取seatLayout列表，支持分页、模糊查询
     *
     * @param conditions       查询条件
     * @param orderByFiledName 根据哪个字段进行排序
     * @param sortBy           DESC：降序，ASC：升序
     * @param pageIndex        页码
     * @param pageSize         分页大小
     * @param byLike           true：使用模糊查询，false：使用精确查询
     * @return
     */
    DomainPage<SeatLayout> findLayOutPagesByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike);

    /**
     * ===================================ASSISTANT====================================
     **/

    /**
     * 查询可用的行车助理
     *
     * @param assistantName 行车助理的名字
     * @param busNoId       车次id
     * @param date          具体哪一天
     * @param pageIndex     页码
     * @param pageSize      分页大小
     * @return
     */
    DomainPage<Assistant> findAvailableAssistant(String assistantName, Long busNoId, Date date, int pageIndex, int pageSize);

}
