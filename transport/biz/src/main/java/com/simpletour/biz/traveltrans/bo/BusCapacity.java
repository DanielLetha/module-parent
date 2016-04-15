package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.traveltrans.Bus;

/**
 * Author:  igotti
 * Mail  :  igotti@simpletour.com
 * Date  :  2016/3/25
 *
 * 车次排班中一个排班车的容量大小及已经安排在这个车上的人数
 *
 * since :  2.0.0-SNAPSHOT
 */
public class BusCapacity {


    private Bus bus;             // 车辆

    private Integer capacity;       // 车辆容量

    private Integer usedCapacity;   // 车辆已消耗容量

    public BusCapacity() {
        this(null, 0, 0);
    }

    public BusCapacity(Bus bus, Integer capacity, Integer usedCapacity) {
        this.bus = bus;
        this.capacity = capacity;
        this.usedCapacity = usedCapacity;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Integer usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

}
