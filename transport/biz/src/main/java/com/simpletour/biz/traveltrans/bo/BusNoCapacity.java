package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.traveltrans.BusNo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  igotti
 * Mail  :  igotti@simpletour.com
 * Date  :  2016/3/25
 *
 * 车次特定某一天排班数据包含该车次某一天所有班次的容量数据
 *
 * since :  2.0.0-SNAPSHOT
 */
public class BusNoCapacity extends LineBusNoSerial {

    private List<BusCapacity> busCapacities;    // 该车次对应的某一天的所有班次的容量信息

    public BusNoCapacity(LineBusNoSerial lineBusNoSerial){
        this(lineBusNoSerial.getBusNo(), lineBusNoSerial.getOffset(), lineBusNoSerial.isTransferable());
    }

    public BusNoCapacity() {
        this(null, 0, false, new ArrayList<>());
    }

    public BusNoCapacity(BusNo busNo, Integer offset, boolean transferable) {
        this(busNo, offset, transferable, new ArrayList<>());
    }

    public BusNoCapacity(BusNo busNo, boolean transferable, List<BusCapacity> busCapacities) {
        this(busNo, 0, transferable, busCapacities);
    }

    public BusNoCapacity(BusNo busNo, Integer offset, boolean transferable, List<BusCapacity> busCapacities) {
        super(busNo, offset, transferable);
        this.busCapacities = busCapacities;
    }

    public List<BusCapacity> getBusCapacities() {
        return busCapacities;
    }

    public void setBusCapacities(List<BusCapacity> busCapacities) {
        this.busCapacities = busCapacities;
    }
}
