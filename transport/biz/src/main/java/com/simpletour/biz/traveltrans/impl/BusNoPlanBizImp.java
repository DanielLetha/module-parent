package com.simpletour.biz.traveltrans.impl;

import com.simpletour.biz.traveltrans.IBusNoPlanBiz;
import com.simpletour.biz.traveltrans.bo.BusNoPlanBo;
import com.simpletour.biz.traveltrans.bo.Tuple;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.BusNoPlan;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mario on 2015/12/2.
 * <p>
 * Update bu Xuhui/XuHui@simpletour on 2016/3/24
 * 进行重构
 */
@Component
public class BusNoPlanBizImp implements IBusNoPlanBiz {

    @Resource
    private ITransportDao iTransportDao;

    @Resource
    private ICertBiz iCertBiz;

    //这段代码我自己都看晕了。。。。。
//    private void validateBusNoPlan(BusNoPlan busNoPlan) {
//        //查找关联的车辆基本信息
//        Bus bus = iTransportDao.getEntityById(Bus.class, busNoPlan.getBus().getId());
//        //查找关联的车次基本信息
//        BusNo busNo = iTransportDao.getEntityById(BusNo.class, busNoPlan.getNo().getId());
//
//        if(busNo.getDepartTime()>busNo.getArriveTime())
//            throw new BaseSystemException(TravelTransportBizError.BUS_NO_SEARCH_EXCEPTION);
//        //计算该busNo的开始时间和结束时间
//        //这里加需要在原来的时间上加上八个小时,GMT和UTC之间的关系
//        Integer start = (int) busNoPlan.getDay().toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNo.getDepartTime();
//        Integer end = (int) busNoPlan.getDay().toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNo.getArriveTime();
//
//        //针对更新的情况
//        if (busNoPlan.getId() != null) {
//            BusNoPlan busNoPlanOriginal = iTransportDao.getEntityById(BusNoPlan.class, busNoPlan.getId());
//            if (busNoPlan.getAssistant() != null && busNoPlan.getAssistant().getId() > 0L) {
//                //查询关联车次的行车助理基本信息
//                Map<String, Object> conditions = new HashMap<>();
//                conditions.put("id", busNoPlan.getAssistant().getId());
//                conditions.put("status", Assistant.Status.normal);
//                List<Assistant> assistantOriginal = iTransportDao.getEntitiesByFieldList(Assistant.class, conditions);
//                if (assistantOriginal.isEmpty() || assistantOriginal == null || assistantOriginal.size() > 1)
//                    throw new BaseSystemException(TravelTransportBizError.BUS_ASSISTANT_NOT_EXISTED);
//                if ((busNoPlanOriginal.getAssistant() != null && busNoPlan.getAssistant().getId() != busNoPlanOriginal.getAssistant().getId()) || busNoPlanOriginal.getAssistant() == null) {
//                    if (!iTransportDao.isAssistantAvailable(assistantOriginal.get(0).getName(), start, end))
//                        throw new BaseSystemException(TravelTransportBizError.BUS_ASSISTANT_HAS_BEEN_DISTRIBUTE);
//                }
//            }
//        } else {
//            //判断插入的车次计划下的行车助理是否可用
//            if (busNoPlan.getAssistant() != null && busNoPlan.getAssistant().getId() > 0L) {
//                //查询关联车次的行车助理基本信息
//                Map<String, Object> conditions = new HashMap<>();
//                conditions.put("id", busNoPlan.getAssistant().getId());
//                conditions.put("status", Assistant.Status.normal);
//                List<Assistant> assistantOriginal = iTransportDao.getEntitiesByFieldList(Assistant.class, conditions);
//                if (assistantOriginal.isEmpty() || assistantOriginal == null || assistantOriginal.size() > 1)
//                    throw new BaseSystemException(TravelTransportBizError.BUS_ASSISTANT_NOT_EXISTED);
//                if (!iTransportDao.isAssistantAvailable(assistantOriginal.get(0).getName(), start, end))
//                    throw new BaseSystemException(TravelTransportBizError.BUS_ASSISTANT_HAS_BEEN_DISTRIBUTE);
//            }
//        }
//    }

    @Override
    public BusNoPlan addBusNoPlan(BusNoPlan busNoPlan) {
        //   this.validateBusNoPlan(busNoPlan);

        return iTransportDao.save(busNoPlan);
    }


    @Override
    public BusNoPlan updateBusNoPlan(BusNoPlan busNoPlan) {
        //    validateBusNoPlan(busNoPlan);

        return iTransportDao.save(busNoPlan);
    }

    @Override
    public boolean deleteBusNoPlanById(Long id) {
        //删除busNoPlan
        iTransportDao.removeEntityById(BusNoPlan.class, id, false);
        return true;
    }

    @Override
    public BusNoPlan getBusNoPlanById(Long id) {
        if (id == null) throw new BaseSystemException(TravelTransportBizError.BUS_NO_PLAN_NULL);
        return iTransportDao.getEntityById(BusNoPlan.class, id);
    }

    @Override
    public List<BusNoPlan> findBusNoPlanByConditions(Map<String, Object> conditions, String orderByFieldName, IBaseDao.SortBy sortBy) {
        List<BusNoPlan> busNoPlanList = iTransportDao.getEntitiesByFieldList(BusNoPlan.class, conditions, orderByFieldName, sortBy);
        return busNoPlanList;
    }

    @Override
    public List<BusNoPlan> findBusNoPlanByConditionsAndDate(Map<String, Object> conditions, Date startDate, Date endDate, String orderFyFieldName, IBaseDao.SortBy sortBy) {
        List<BusNoPlan> busNoPlanList = iTransportDao.findBusNoPlanByConditionsAndDate(conditions, startDate, endDate, orderFyFieldName, sortBy);
        return busNoPlanList;
    }

    @Override
    public DomainPage<BusNoPlan> findBusNoPlanPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        DomainPage<BusNoPlan> domainPage = iTransportDao.queryEntitiesPagesByFieldList(BusNoPlan.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
        return domainPage;
    }

    @Override
    public int findAvailableBusSeat(Map<String, Object> conditions) {
        List<BusNoPlan> busNoPlanList = iTransportDao.getEntitiesByFieldList(BusNoPlan.class, conditions);
        List<BusNoPlanBo> busNoPlanBoList = new ArrayList<>();
        busNoPlanList.stream().forEach(n -> {
            BusNoPlanBo busNoPlanBo = new BusNoPlanBo(n);
            busNoPlanBoList.add(busNoPlanBo);
        });
        List<Tuple<Long, Integer>> tupleList = new ArrayList<>();
        if (!busNoPlanBoList.isEmpty()) {
            tupleList = busNoPlanBoList.stream().map(n -> {
                Map<String, Object> map = new HashMap<>();
                map.put("busNo.id", n.getNo().getId());
                map.put("bus.id", n.getBus().getId());
                map.put("date", n.getDay());
                //查询相关车次计划下的电子凭证售卖情况,以及还剩了多少座位
                List<CertIdentity> certIdentities = iCertBiz.findCertIdentityByConditions(map);
                n.setSoldQuantity(certIdentities.size());
                return new Tuple<>(n.getBus().getId(), n.getCapacity() - n.getSoldQuantity());
            }).collect(Collectors.toList());
        }
        return tupleList.get(0).getSecond();
    }

    @Override
    public boolean isExisted(Long id) {
        if (id == null)
            throw new BaseSystemException(TravelTransportBizError.BUS_NO_PLAN_NULL);
        BusNoPlan busNoPlan = getBusNoPlanById(id);
        return busNoPlan != null;
    }

    // TODO: 2016/3/25 Available校验
    public boolean isBusAvailable(String license, Integer start, Integer end){
        return iTransportDao.isBusAvailable(license, start, end);
    }

    // TODO: 2016/3/25 Available校验
    @Override
    public boolean isAssistantAvailable(String name,Integer start,Integer end) {
        return iTransportDao.isAssistantAvailable(name,start,end);
    }
}
