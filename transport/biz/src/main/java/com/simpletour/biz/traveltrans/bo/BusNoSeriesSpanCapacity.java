package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.product.TourismRouteLine;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author:  igotti
 * Mail  :  igotti@simpletour.com
 * Date  :  2016/3/25
 *
 * 车次特定某一天排班数据包含该车次某一天所有班次的容量数据
 *
 * since :  2.0.0-SNAPSHOT
 */
public class BusNoSeriesSpanCapacity {

    private TourismRouteLine tourismRouteLine;

    /**
     * 车次序列内
     */
    private List<BusNoSpanCapacity> busNoSpanCapacities;

    public BusNoSeriesSpanCapacity(List<BusNoSpanCapacity> busNoSpanCapacities) {
        this.busNoSpanCapacities = busNoSpanCapacities;
    }

    public BusNoSeriesSpanCapacity(TourismRouteLine tourismRouteLine, List<BusNoSpanCapacity> busNoSpanCapacities) {
        this.tourismRouteLine = tourismRouteLine;
        this.busNoSpanCapacities = busNoSpanCapacities;
    }

    public TourismRouteLine getTourismRouteLine() {
        return tourismRouteLine;
    }

    public void setTourismRouteLine(TourismRouteLine tourismRouteLine) {
        this.tourismRouteLine = tourismRouteLine;
    }

    public List<BusNoSpanCapacity> getBusNoSpanCapacities() {
        return busNoSpanCapacities;
    }

    public void setBusNoSpanCapacities(List<BusNoSpanCapacity> busNoSpanCapacities) {
        this.busNoSpanCapacities = busNoSpanCapacities;
    }

    public Map<Date, List<BusNoCapacity>> getBusNoSeriesCapacitySlice(Date day){
        //如果以下任意条件不满足，则返回空map
        if(!(day==null||tourismRouteLine==null||busNoSpanCapacities==null||tourismRouteLine.getBusNoSeries().size()!=busNoSpanCapacities.size())){
            Date lineOff = Date.from(day.toInstant().plus(tourismRouteLine.getOffset(), ChronoUnit.DAYS));
            Map<Date, List<BusNoCapacity>> busNoCapacitiesTuple = new HashMap<>(1);
            List<BusNoCapacity> busNoCapacities = busNoSpanCapacities.stream().map(busNoSpanCapacity -> {
                Date busNoOff = Date.from(lineOff.toInstant().plus(busNoSpanCapacity.getOffset(), ChronoUnit.DAYS));
                List<BusCapacity> busCapacities = busNoSpanCapacity.getBusCapacities().get(busNoOff);
                return !(busCapacities==null||busCapacities.isEmpty())?
                        new BusNoCapacity(busNoSpanCapacity.getBusNo(), busNoSpanCapacity.isTransferable(), busCapacities):null;
            }).collect(Collectors.toList());
            if(busNoCapacities.stream().anyMatch(busNoCapacity -> busNoCapacity==null)) return Collections.emptyMap();

            busNoCapacitiesTuple.put(day, busNoCapacities);
            return Collections.unmodifiableMap(busNoCapacitiesTuple);
        }
        return Collections.emptyMap();
    }

}
