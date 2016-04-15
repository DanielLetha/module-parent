package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.traveltrans.Bus;

/**
 * Author:  igotti
 * Mail  :  igotti@simpletour.com
 * Date  :  2016/3/25
 *
 * 带有合并次数的BusCapacity
 *
 * since :  2.0.0-SNAPSHOT
 */
public class MergedBusCapacity extends BusCapacity {


    private Integer mergeTimes; //合并次数

    public MergedBusCapacity(){
        this(null, 0, 0, 0);
    }

    public MergedBusCapacity(BusCapacity busCapacity){
        this(busCapacity.getBus(), busCapacity.getCapacity(), busCapacity.getUsedCapacity(), 1);
    }

    public MergedBusCapacity(Bus bus, Integer capacity, Integer usedCapacity, Integer mergeTimes){
        super(bus, capacity, usedCapacity);
        this.mergeTimes = mergeTimes;
    }


    public Integer getMergeTimes() {
        return mergeTimes;
    }

    public void setMergeTimes(Integer mergeTimes) {
        this.mergeTimes = mergeTimes;
    }

    public BusCapacity getBusCapacity(){
        return new BusCapacity(getBus(), getCapacity(), getUsedCapacity());
    }

}
