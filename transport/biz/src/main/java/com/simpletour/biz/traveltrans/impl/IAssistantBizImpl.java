package com.simpletour.biz.traveltrans.impl;

import com.simpletour.biz.traveltrans.IAssistantBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.Assistant;
import com.simpletour.domain.traveltrans.BusNo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Created by Mario on 2015/12/25.
 */
@Component
public class IAssistantBizImpl implements IAssistantBiz {
    @Resource
    private ITransportDao iTransportDao;

    @Override
    public DomainPage<Assistant> findAvailableAssistant(String assistantName, Long busNoId, Date date, int pageIndex, int pageSize) {
        if (busNoId == null || date == null)
            throw new BaseSystemException(TravelTransportBizError.BUS_BUS_NO_ID_AND_DAY_NULL);
        //先查询出busNoId对应的离开时间和到达时间
        BusNo busNo = iTransportDao.getEntityById(BusNo.class, busNoId);
        if (busNo == null) throw new BaseSystemException(TravelTransportBizError.BUS_NO_SEARCH_EXCEPTION);
        //计算该busNo的开始时间和结束时间
        Integer start = (int) date.toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNo.getDepartTime();
        Integer end = (int) date.toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNo.getArriveTime();
        if (start > end) throw new BaseSystemException(TravelTransportBizError.BUS_NO_SEARCH_EXCEPTION);
        return iTransportDao.findAvailableAssistant(assistantName, start, end, pageIndex, pageSize);
    }

    @Override
    public Assistant getAssistantById(Long id) {
        if(id==null)
            throw new BaseSystemException(TravelTransportBizError.BUS_ASSISTANT_NULL);
        return iTransportDao.getEntityById(Assistant.class,id);
    }

    @Override
    public boolean isExisted(Long id) {
        return getAssistantById(id)!=null;
    }

    @Override
    public boolean isAvailable(Long id) {
        Assistant assistant=getAssistantById(id);
        return assistant.getStatus().equals(Assistant.Status.normal);
    }


}
