package com.smpletour.order.dao.impl;

import com.simpletour.commons.data.dao.jpa.JPABaseDAO;
import com.simpletour.domain.order.Cert;
import com.simpletour.domain.traveltrans.Bus;
import com.simpletour.domain.traveltrans.BusNo;
import com.smpletour.order.dao.IOrderDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mario on 2016/4/20.
 */
@Repository
public class IOrderDaoImpl extends JPABaseDAO implements IOrderDao {
    //TODO...添加依赖关系

    @Override
    public List<Cert> queryCertByBusNoAndBusAndDate(BusNo busNo, Bus bus, Date date) {
        HashMap<String, Object> queryConditions = new HashMap<>(6);
//        Long tenantId = getTenantId();
//        if (null == tenantId) {
//            queryConditions.put("busNo", busNo);
//            queryConditions.put("bus", bus);
//        } else {
//            queryConditions.put("busNo.id", busNo.getId());
//            queryConditions.put("busNo.tenantId", tenantId);
//            queryConditions.put("bus.id", bus.getId());
//            queryConditions.put("bus.tenantId", tenantId);
//        }
//        queryConditions.put("date", date);
//        queryConditions.put("cert.valid", true);

        queryConditions.put("bus.id", bus.getId());
        queryConditions.put("busNo.id", busNo.getId());
        queryConditions.put("date", date);
        queryConditions.put("cert.valid", true);
        return getEntitiesByFieldList(Cert.class, queryConditions);
    }
}
