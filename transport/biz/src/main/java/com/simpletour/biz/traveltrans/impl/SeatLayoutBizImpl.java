package com.simpletour.biz.traveltrans.impl;

import com.simpletour.biz.traveltrans.ISeatLayoutBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.Seat;
import com.simpletour.domain.traveltrans.SeatLayout;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mario on 2015/12/4.
 */
@Component
public class SeatLayoutBizImpl implements ISeatLayoutBiz {

    @Resource
    private ITransportDao iTransportDao;

    @Override
    public Optional<SeatLayout> addSeatLayout(SeatLayout seatLayout) {
        if (!seatLayout.legalCapacity()) {//容量错误
            throw new BaseSystemException(TravelTransportBizError.BUS_SEAT_LATOUT_CAPACITY_ERROR);
        }
        SeatLayout original = iTransportDao.getEntityByField(SeatLayout.class, "name", seatLayout.getName());
        if (original != null) {//重名
            throw new BaseSystemException(TravelTransportBizError.BUS_SEAT_LAYOUT_NAME_EXISTED);
        }
        return Optional.ofNullable(iTransportDao.save(seatLayout));
    }

    @Override
    public Boolean deleteSeatLayout(SeatLayout seatLayout) {
        iTransportDao.removeEntity(seatLayout);
        return true;
    }

    @Override
    public Optional<SeatLayout> updateSeatLayout(SeatLayout seatLayout) {
        //判断数据库中是否有同名的数据
        SeatLayout original = iTransportDao.getEntityById(SeatLayout.class, seatLayout.getId());
        if (original == null) {//待更新对象不存在
            throw new BaseSystemException(TravelTransportBizError.BUS_SEAT_LATOUT_UPDATE_FAILD);
        }
        if (!original.getName().equals(seatLayout.getName())) {
            SeatLayout existed = iTransportDao.getEntityByField(SeatLayout.class, "name", seatLayout.getName());
            if (existed != null) {//重名异常
                throw new BaseSystemException(TravelTransportBizError.BUS_SEAT_LAYOUT_NAME_EXISTED);
            }
        }
        //如果座位布局对应的座位信息为空，则删除掉座位布局关联的座位信息
        List<Seat> oldSeat = iTransportDao.getEntitiesByField(Seat.class, "seatLayout", seatLayout);
        oldSeat.forEach(seat -> iTransportDao.removeEntityById(Seat.class, seat.getId(), true));
        if (!seatLayout.legalCapacity()) {//容量不正确异常
            throw new BaseSystemException(TravelTransportBizError.BUS_SEAT_LATOUT_CAPACITY_ERROR);
        }
        return Optional.ofNullable(iTransportDao.save(seatLayout));
    }

    @Override
    public Optional<SeatLayout> findLayoutById(Long id) {
        return Optional.ofNullable(iTransportDao.getEntityById(SeatLayout.class, id));
    }

    @Override
    public DomainPage<SeatLayout> findLayOutPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iTransportDao.queryEntitiesPagesByFieldList(SeatLayout.class, conditions, orderByFiledName, sortBy, pageIndex, pageSize, byLike);
    }

    @Override
    public Boolean isExisted(Long seatLayoutId) {
        return iTransportDao.getEntityById(SeatLayout.class, seatLayoutId) != null ? true : false;
    }
}
