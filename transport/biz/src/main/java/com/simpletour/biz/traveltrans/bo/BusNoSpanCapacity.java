package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.traveltrans.BusNo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:  igotti
 * Mail  :  igotti@simpletour.com
 * Date  :  2016/3/25
 *
 * 车次特定一段时间排班数据，包含该这次一段时间内所有班次的容量数据
 *
 * since :  2.0.0-SNAPSHOT
 */
public class BusNoSpanCapacity extends LineBusNoSerial {

    private Map<Date,List<BusCapacity>> busCapacities;    // 该车次对应的某一天的所有班次的容量信息

    public BusNoSpanCapacity(LineBusNoSerial lineBusNoSerial) {
        this(lineBusNoSerial.getBusNo(), lineBusNoSerial.getOffset(), lineBusNoSerial.isTransferable());
    }

    public BusNoSpanCapacity() {
        this(null, 0, false, new HashMap<>());
    }

    public BusNoSpanCapacity(BusNo busNo, Integer offset, boolean transferable) {
        this(busNo, offset, transferable, new HashMap<>());
    }

    public BusNoSpanCapacity(BusNo busNo, boolean transferable, Map<Date,List<BusCapacity>> busCapacities) {
        this(busNo, 0, transferable, busCapacities);
    }

    public BusNoSpanCapacity(BusNo busNo, Integer offset, boolean transferable, Map<Date,List<BusCapacity>> busCapacities) {
        super(busNo, offset, transferable);
        this.busCapacities = busCapacities;
    }

    public Map<Date,List<BusCapacity>> getBusCapacities() {
        return busCapacities;
    }

    public void setBusCapacities(Map<Date,List<BusCapacity>> busCapacities) {
        this.busCapacities = busCapacities;
    }

}
