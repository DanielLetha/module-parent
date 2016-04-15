package com.simpletour.biz.traveltrans;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.traveltrans.SeatLayout;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Mario on 2015/12/4.
 */
public interface ISeatLayoutBiz {
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
    //TODO...级联删除，暂时不做
    Boolean deleteSeatLayout(SeatLayout seatLayout);

    /**
     * 更新座位布局
     *
     * @param seatLayout
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
     * 检查座位布局是否可用
     * @param seatLayoutId 座位布局ID 非空必填
     * @return true/false
     * @author baifeilong@simpletour.com
     *
     */
    Boolean isExisted(Long seatLayoutId);

}
