package com.simpletour.biz.traveltrans;


import com.simpletour.biz.traveltrans.bo.BusNoBo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.traveltrans.BusNo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author:  WangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/3/23
 * since :  2.0.0-SNAPSHOT
 */
public interface IBusNoBiz {

    /**
     * 增加车次以及车次行程节点
     *
     * @param busNoBo 业务对象实体
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
     * 更新busNo以及busNo下的node信息
     *
     * @param busNoBo busNoBo业务实体类
     * @return
     */
    Optional<BusNo> updateBusNo(BusNoBo busNoBo);

    /**
     * 根据no获取车次及行程信息
     *
     * @param no     车次编号
     * @param eagger 是否需要级联查出关联的node
     * @return
     */
    Optional<BusNo> findBusNoByNo(String no, Boolean eagger);

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

    /**
     * 判断车次是否存在
     * @param id
     * @return
     * @throws BaseSystemException
     */
    boolean isExisted(Long id);

    /**
     * 判断车次是否可用，包括 status.equal(normal)
     * @param id
     * @return
     */
    boolean isAvailable(Long id);
}



