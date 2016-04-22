package com.smpletour.order.dao;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.domain.order.Cert;
import com.simpletour.domain.traveltrans.Bus;
import com.simpletour.domain.traveltrans.BusNo;

import java.util.Date;
import java.util.List;

/**
 * Created by Mario on 2016/4/20.
 */
public interface IOrderDao extends IBaseDao {
    /**
     * 根据车次、车辆、日期查询相关的电子凭证
     *
     * @param busNo 车次
     * @param bus   车辆
     * @param date  相关的日期
     * @return cert的列表
     */
    List<Cert> queryCertByBusNoAndBusAndDate(BusNo busNo, Bus bus, Date date);
}
