package com.simpletour.biz.traveltrans.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author:  igotti
 * Mail  :  igotti@simpletour.com
 * Date  :  2016/3/25
 *
 * 一个车次包含的所有班次的容量信息(该车次在某一天的所有班次)
 * 结构和BusNoCapacity基本一样的，只是这里的busNoCapacities包含的是MergeBusCapacity, 多了一个记录合并次数的字段
 *
 * since :  2.0.0-SNAPSHOT
 */
public class MergedBusNoCapacity {


    private List<MergedBusCapacity> mergeBusNoCapacities;

    public MergedBusNoCapacity(){
        mergeBusNoCapacities = new ArrayList<>();
    }

    public MergedBusNoCapacity(BusNoCapacity busNoCapacity){
        this(busNoCapacity.getBusCapacities());
    }

    // 这里有坑 小心!!!
    // 如果用List<MergeBusCapacity> 结构来初始化，就会出问题(编译能通过，但是逻辑错误)
    public MergedBusNoCapacity(List<BusCapacity> busCapacities){
        mergeBusNoCapacities = busCapacities.stream()
                .map(busCapacity -> new MergedBusCapacity(busCapacity))
                .collect(Collectors.toList());
    }

    public List<MergedBusCapacity> getMergeBusNoCapacities() {
        return mergeBusNoCapacities;
    }

    public void setMergeBusNoCapacities(List<MergedBusCapacity> mergeBusNoCapacities) {
        this.mergeBusNoCapacities = mergeBusNoCapacities;
    }

    public boolean add(MergedBusCapacity mergeBusCapacity){
        return mergeBusNoCapacities.add(mergeBusCapacity);
    }

    public List<BusCapacity> getBusNoCapacities() {
        return mergeBusNoCapacities.stream().map(mergeBusCapacity -> mergeBusCapacity.getBusCapacity()).collect(Collectors.toList());
    }

    public MergedBusCapacity getMergeBusCapacityByBusId(Long busId){
        Optional<MergedBusCapacity> result = mergeBusNoCapacities.stream()
                .filter(mergeBusCapacity -> busId.equals(mergeBusCapacity.getBus().getId()))
                .findFirst();
        return result.isPresent()? result.get() : null;
    }

}
