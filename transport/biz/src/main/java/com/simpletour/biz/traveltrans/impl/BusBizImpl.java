package com.simpletour.biz.traveltrans.impl;

import com.simpletour.biz.traveltrans.IBusBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.Bus;
import com.simpletour.domain.traveltrans.BusNo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mario on 2015/11/21.
 */
@Component
public class BusBizImpl implements IBusBiz {

    @Resource
    ITransportDao iTransportDao;

    @Override
    public Optional<Bus> addBus(Bus bus) {
        Bus original = iTransportDao.getEntityByField(Bus.class, "license", bus.getLicense());
        if (original != null) {//车牌号已被占用
            throw new BaseSystemException(TravelTransportBizError.BUS_LICENSE_EXISTED);
        }
        return Optional.ofNullable(iTransportDao.save(bus));
    }

    @Override
    public Boolean deleteBus(Bus bus) {
        iTransportDao.removeEntityById(Bus.class, bus.getId(), false);
        return true;
    }

    @Override
    public Optional<Bus> updateBus(Bus bus) {
        Bus original = iTransportDao.getEntityById(Bus.class, bus.getId());
        if (original == null) {//待更新对象不存在
            throw new BaseSystemException(TravelTransportBizError.BUS_UPDATE_ABNORMALLY);
        }
        if (!original.getLicense().equals(bus.getLicense())) {
            Bus existed = iTransportDao.getEntityByField(Bus.class, "license", bus.getLicense());
            if (existed != null) {//新的车牌号已被占用
                throw new BaseSystemException(TravelTransportBizError.BUS_LICENSE_EXISTED);
            }
        }
        return Optional.ofNullable(iTransportDao.save(bus));
    }

    @Override
    public DomainPage<Bus> findBusPageByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iTransportDao.queryEntitiesPagesByFieldList(Bus.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public List<Bus> findBusByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy) {
        return iTransportDao.getEntitiesByFieldList(Bus.class, conditions, orderByFiledName, orderBy);
    }

    @Override
    public Optional<Bus> findBusById(Long id) {
        return Optional.ofNullable(iTransportDao.getEntityById(Bus.class, id));
    }

    @Override
    public DomainPage<Bus> findAvailableBus(String license, Long busNoId, Date day, int pageIndex, int pageSize) {
        BusNo busNo = iTransportDao.getEntityById(BusNo.class, busNoId);
        if (busNo == null) {//车次不存在
            throw new BaseSystemException(TravelTransportBizError.BUS_NO_SEARCH_EXCEPTION);
        }
        //计算该busNo的开始时间和结束时间
        //这里需要在原来的时间上加上八个小时,GMT和UTC之间的关系
        Integer start = (int) day.toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNo.getDepartTime();
        Integer end = (int) day.toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNo.getArriveTime();
        if (start > end) {
            throw new BaseSystemException(TravelTransportBizError.BUS_NO_SEARCH_EXCEPTION);
        }
        return iTransportDao.findAvailableBus(license, start, end, pageIndex, pageSize);
    }

    @Override
    public Boolean isExisted(Long busId) {
        return iTransportDao.getEntityById(Bus.class, busId) != null ? true : false;
    }

    @Override
    public Boolean isAvailable(Long busId) {
        Bus bus = iTransportDao.getEntityById(Bus.class, busId);
        return bus != null && bus.isOnline() ? true : false;
    }
}
