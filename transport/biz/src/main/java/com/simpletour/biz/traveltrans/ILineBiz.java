package com.simpletour.biz.traveltrans;

import com.simpletour.biz.traveltrans.bo.LineBusNoSerial;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.product.TourismRoute;
import com.simpletour.domain.product.TourismRouteLine;
import com.simpletour.domain.traveltrans.Line;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author:  yangdongfeng
 * Mail  :  yangdongfeng@simpletour.com
 * Date  :  2016/3/25
 *
 * 处理线路相关的添加、删除、更新、查询逻辑
 *
 * 处理线路车次序列和行程路径的分解和重构
 *
 * since :  2.0.0-SNAPSHOT
 */
public interface ILineBiz {

    /**
     * 添加新路线
     *
     * @param line line对象实体
     * @return
     */
    Optional<Line> addLine(Line line);

    /**
     * 更新line以及line级联的busNoSerial
     *
     * @param line
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
     * 判断线路是否存在
     * @param id
     * @return
     */
    boolean isExisted(Long id);

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
     * @param page
     * @param pageSize
     * @return
     */
    DomainPage findLinesPageByConditions(AndConditionSet condition, int page, int pageSize);

    /**
     * 分解行程车次路径到线路
     *
     * @see com.simpletour.domain.product.TourismRoute#decompose(List, List)
     *
     * @param lines 所有线路
     * @param routes 行程路径
     * @return 分解到线路的行程路径，该方法会重新设置tourism route 对应的线路和线路偏移
     */
    List<TourismRoute> decompose(List<Line> lines, List<TourismRoute> routes);


    /**
     * 将行程分解到线路上的线路上的车次序列，从线路车次序列中选出来，并重构各车次之间的换乘关系
     *
     * @see com.simpletour.dao.traveltrans.impl.BusNoPlanCapacityHelper#pick(List, TourismRouteLine)
     *
     * @param tourismRouteLine 行程分解到线路上的车次序列
     * @return 重构分解到线路上的车次序列之间的换乘关系
     */
    List<LineBusNoSerial> pick(TourismRouteLine tourismRouteLine);
}
