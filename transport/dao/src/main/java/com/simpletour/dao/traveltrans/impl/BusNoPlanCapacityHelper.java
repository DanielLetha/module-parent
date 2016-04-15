package com.simpletour.dao.traveltrans.impl;

import com.simpletour.domain.product.TourismRouteLine;
import com.simpletour.domain.traveltrans.Bus;
import com.simpletour.domain.traveltrans.BusNo;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yangdongfeng on 2015/12/8.
 */
public class BusNoPlanCapacityHelper {

    /**
     * 带有合并次数的BusCapacity
     */
    public static class MergeBusCapacity extends BusNo.BusCapacity{

        private Integer mergeTimes; //合并次数

        public MergeBusCapacity(){
            this(null, 0, 0, 0);
        }

        public MergeBusCapacity(BusNo.BusCapacity busCapacity){
            this(busCapacity.getBusId(), busCapacity.getCapacity(), busCapacity.getUsedCapacity(), 1);
        }

        public MergeBusCapacity(Bus bus, Integer capacity, Integer usedCapacity, Integer mergeTimes){
            super(bus, capacity, usedCapacity);
            this.mergeTimes = mergeTimes;
        }


        public Integer getMergeTimes() {
            return mergeTimes;
        }

        public void setMergeTimes(Integer mergeTimes) {
            this.mergeTimes = mergeTimes;
        }

        public BusNo.BusCapacity getBusCapacity(){
            return new BusNo.BusCapacity(getBusId(), getCapacity(), getUsedCapacity());
        }
    }

    /**
     * 一个车次包含的所有班次的容量信息(该车次在某一天的所有班次)
     * 结构和BusNo.BusNoCapacity基本一样的，只是这里的busNoCapacities包含的是MergeBusCapacity, 多了一个记录合并次数的字段
     */
    public static class MergeBusNoCapacity {

        private List<MergeBusCapacity> mergeBusNoCapacities;

        public MergeBusNoCapacity(){
            mergeBusNoCapacities = new ArrayList<>();
        }

        public MergeBusNoCapacity(BusNo.BusNoCapacity busNoCapacity){
            this(busNoCapacity.getBusCapacities());
        }

        // 这里有坑 小心!!!
        // 如果用List<MergeBusCapacity> 结构来初始化，就会出问题(编译能通过，但是逻辑错误)
        public MergeBusNoCapacity(List<BusNo.BusCapacity> busCapacities){
            mergeBusNoCapacities = busCapacities.stream()
                    .map(busCapacity -> new MergeBusCapacity(busCapacity))
                    .collect(Collectors.toList());
        }

        public List<MergeBusCapacity> getMergeBusNoCapacities() {
            return mergeBusNoCapacities;
        }

        public void setMergeBusNoCapacities(List<MergeBusCapacity> mergeBusNoCapacities) {
            this.mergeBusNoCapacities = mergeBusNoCapacities;
        }

        public boolean add(MergeBusCapacity mergeBusCapacity){
            return mergeBusNoCapacities.add(mergeBusCapacity);
        }

        public List<BusNo.BusCapacity> getBusNoCapacities() {
            return mergeBusNoCapacities.stream().map(mergeBusCapacity -> mergeBusCapacity.getBusCapacity()).collect(Collectors.toList());
        }

        public MergeBusCapacity getMergeBusCapacityByBusId(Long busId){
            Optional<MergeBusCapacity> result = mergeBusNoCapacities.stream()
                    .filter(mergeBusCapacity -> busId.equals(mergeBusCapacity.getBusId().getId()))
                    .findFirst();
            return result.isPresent()? result.get() : null;
        }

    }

    public static class BusNoSeriesSpanCapacity {

        private TourismRouteLine tourismRouteLine;

        /**
         * 车次序列内
         */
        private List<BusNo.BusNoSpanCapacity> busNoSpanCapacities;

        public BusNoSeriesSpanCapacity(List<BusNo.BusNoSpanCapacity> busNoSpanCapacities) {
            this.busNoSpanCapacities = busNoSpanCapacities;
        }

        public BusNoSeriesSpanCapacity(TourismRouteLine tourismRouteLine, List<BusNo.BusNoSpanCapacity> busNoSpanCapacities) {
            this.tourismRouteLine = tourismRouteLine;
            this.busNoSpanCapacities = busNoSpanCapacities;
        }

        public TourismRouteLine getTourismRouteLine() {
            return tourismRouteLine;
        }

        public void setTourismRouteLine(TourismRouteLine tourismRouteLine) {
            this.tourismRouteLine = tourismRouteLine;
        }

        public List<BusNo.BusNoSpanCapacity> getBusNoSpanCapacities() {
            return busNoSpanCapacities;
        }

        public void setBusNoSpanCapacities(List<BusNo.BusNoSpanCapacity> busNoSpanCapacities) {
            this.busNoSpanCapacities = busNoSpanCapacities;
        }

        public Map<Date, List<BusNo.BusNoCapacity>> getBusNoSeriesCapacitySlice(Date day){
            //如果以下任意条件不满足，则返回空map
            if(!(day==null||tourismRouteLine==null||busNoSpanCapacities==null||tourismRouteLine.getBusNoSeries().size()!=busNoSpanCapacities.size())){
                Date lineOff = Date.from(day.toInstant().plus(tourismRouteLine.getOffset(), ChronoUnit.DAYS));
                Map<Date, List<BusNo.BusNoCapacity>> busNoCapacitiesTuple = new HashMap<>(1);
                List<BusNo.BusNoCapacity> busNoCapacities = busNoSpanCapacities.stream().map(busNoSpanCapacity -> {
                    Date busNoOff = Date.from(lineOff.toInstant().plus(busNoSpanCapacity.getOffset(), ChronoUnit.DAYS));
                    List<BusNo.BusCapacity> busCapacities = busNoSpanCapacity.getBusCapacities().get(busNoOff);
                    return !(busCapacities==null||busCapacities.isEmpty())?
                            new BusNo.BusNoCapacity(busNoSpanCapacity.getBusNoId(), busNoSpanCapacity.isTransferable(), busCapacities):null;
                }).collect(Collectors.toList());
                if(busNoCapacities.stream().anyMatch(busNoCapacity -> busNoCapacity==null)) return Collections.emptyMap();

                busNoCapacitiesTuple.put(day, busNoCapacities);
                return Collections.unmodifiableMap(busNoCapacitiesTuple);
            }
            return Collections.emptyMap();
        }
    }

    /**
     * 从BusNoSeries中挑选车次排班表
     * @param busNoSpanCapacities
     * @param tourismRouteLine
     * @return
     */
    public static List<BusNo.BusNoSpanCapacity> pick1(List<BusNo.BusNoSpanCapacity> busNoSpanCapacities, TourismRouteLine tourismRouteLine){
        if(!(busNoSpanCapacities ==null|| busNoSpanCapacities.isEmpty()|| tourismRouteLine==null || tourismRouteLine.getBusNoSeries()==null||tourismRouteLine.getBusNoSeries().isEmpty())){
            List<BusNo.BusNoSpanCapacity> selectedBusNoCapacities = new ArrayList<>();
            for(int i = 0, j = 0; i < busNoSpanCapacities.size() && j < tourismRouteLine.getBusNoSeries().size(); ++i){
                if(busNoSpanCapacities.get(i).getBusNoId().equals(tourismRouteLine.getBusNoSeries().get(j))){//线路内的第i个车次包含在行程分解线路序列内
                    selectedBusNoCapacities.add(busNoSpanCapacities.get(i));
                    j += 1;
                }else if(!(selectedBusNoCapacities.isEmpty()|| selectedBusNoCapacities.get(selectedBusNoCapacities.size()-1).isTransferable())){  // 虎哥定理：可换乘的传递性
                    selectedBusNoCapacities.get(selectedBusNoCapacities.size()-1).setTransferable(busNoSpanCapacities.get(i).isTransferable());
                }
            }
            return selectedBusNoCapacities;
        }
        return Collections.emptyList();
    }

    /**
     * 从BusNoSeries中挑选车次排班表
     * @param busNoCapacities
     * @param tourismRouteLine
     * @return
     */
    public static List<BusNo.BusNoCapacity> pick(List<BusNo.BusNoCapacity> busNoCapacities, TourismRouteLine tourismRouteLine){
        if(!(busNoCapacities ==null|| busNoCapacities.isEmpty()|| tourismRouteLine==null || tourismRouteLine.getBusNoSeries()==null||tourismRouteLine.getBusNoSeries().isEmpty())){
            List<BusNo.BusNoCapacity> selectedBusNoCapacities = new ArrayList<>();
            for(int i = 0, j = 0; i < busNoCapacities.size() && j < tourismRouteLine.getBusNoSeries().size(); ++i){
                if(busNoCapacities.get(i).getBusNoId().equals(tourismRouteLine.getBusNoSeries().get(j))){//线路内的第i个车次包含在行程分解线路序列内
                    selectedBusNoCapacities.add(busNoCapacities.get(i));
                    j += 1;
                }else if(!(selectedBusNoCapacities.isEmpty()|| selectedBusNoCapacities.get(selectedBusNoCapacities.size()-1).isTransferable())){  // 虎哥定理：可换乘的传递性
                    selectedBusNoCapacities.get(selectedBusNoCapacities.size()-1).setTransferable(busNoCapacities.get(i).isTransferable());
                }
            }
            return selectedBusNoCapacities;
        }
        return Collections.emptyList();
    }

    public static Integer recursive(List<BusNo.BusCapacity> result, boolean transferable, List<BusNo.BusNoCapacity> busNoCapacities){
        if(!(result==null||result.isEmpty())){
            List<MergeBusNoCapacity> initResult = new ArrayList<>();
            MergeBusNoCapacity mergeBusNoCapacity = new MergeBusNoCapacity(result);
            initResult.add(mergeBusNoCapacity);

            //
            List<MergeBusNoCapacity> transferableMatrix = recursiveMatrix(initResult, transferable, busNoCapacities);

            // 计算每一个车次的剩余容量
            List<Integer> overallCapacities = transferableMatrix.stream()
                    .map(innerBusNoCapicity -> innerBusNoCapicity.getMergeBusNoCapacities().stream()
                            .map(busCapacity -> busCapacity.getCapacity() - busCapacity.getUsedCapacity())) // 计算每一辆车的剩余容量
                    .map(innerBusNoCapicity -> innerBusNoCapicity.reduce(0, Integer::sum))    //数组求和，每一辆车的剩余容量加在一起作为这一个车次的容量
                    .collect(Collectors.toList());
            Optional<Integer> finalCapacity = overallCapacities.stream().min(Integer::compare); // 所有车次的剩余容量，取最小值作为最终容量
            return finalCapacity.isPresent()?finalCapacity.get():0;
        }
        return 0;
    }

    /**
     * 在busCapacities中查找BusNo.id 与id相等的并返回， 没有则返回空
     * @param busCapacities
     * @param busNoId
     * @return
     */
    private static BusNo.BusCapacity getBusCapacityWithBusIdFromBusCapacities(List<BusNo.BusCapacity> busCapacities, Long busNoId){
        for (BusNo.BusCapacity it_busCapacity : busCapacities) {
            if (it_busCapacity.getBusId().getId().equals(busNoId))
                return it_busCapacity;
        }
        return null;
    }

    public static List<MergeBusNoCapacity> recursiveMatrix(List<MergeBusNoCapacity> result, boolean transferable, List<BusNo.BusNoCapacity> busNoCapacities){
        if(busNoCapacities.isEmpty()) return result;
        BusNo.BusNoCapacity current = busNoCapacities.get(0);   // 剩余车次容量中的第一个
        MergeBusNoCapacity prev = result.get(result.size()-1);  //当前结果中的最后一个
        List<BusNo.BusCapacity> busCapacities = current.getBusCapacities();
        if(transferable){   // 如果可换乘，则加入到结果集，
            List<MergeBusNoCapacity> interResult = new ArrayList<MergeBusNoCapacity>();
            MergeBusNoCapacity mergeBusNoCapacity = new MergeBusNoCapacity(busCapacities);
            interResult.add(mergeBusNoCapacity);

            List<MergeBusNoCapacity> columnResult = recursiveMatrix(interResult, current.isTransferable(), busNoCapacities.subList(1, busNoCapacities.size()));
            result.addAll(columnResult);
            return result;
        }else{
            // 如果不可换乘，则合并当前结果中最后一个和busCapacities中的数据
            List<MergeBusCapacity> mergerdBusCapacities = new ArrayList<>(prev.getMergeBusNoCapacities().size());
            for (MergeBusCapacity it_mergeBusCapacity : prev.getMergeBusNoCapacities()) {
                BusNo.BusCapacity busCapacity = getBusCapacityWithBusIdFromBusCapacities(busCapacities, it_mergeBusCapacity.getBusId().getId());
                MergeBusCapacity conbinedMergedBusCapacities = new MergeBusCapacity(it_mergeBusCapacity.getBusId(),
                        busCapacity == null ? 0 : it_mergeBusCapacity.getCapacity(),
                        busCapacity == null ? 0 : Integer.max(it_mergeBusCapacity.getUsedCapacity(), busCapacity.getUsedCapacity()),
                        it_mergeBusCapacity.getMergeTimes() + 1);
                mergerdBusCapacities.add(conbinedMergedBusCapacities);
            }
            MergeBusNoCapacity mergeBusNoCapacity = new MergeBusNoCapacity();
            mergeBusNoCapacity.setMergeBusNoCapacities(mergerdBusCapacities);

            result.set(result.size() - 1, mergeBusNoCapacity);
        }
        return recursiveMatrix(result, busNoCapacities.get(0).isTransferable(), busNoCapacities.subList(1, busNoCapacities.size()));
    }
}


