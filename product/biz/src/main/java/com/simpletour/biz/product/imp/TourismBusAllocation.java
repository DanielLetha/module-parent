//package com.simpletour.biz.product.imp;
//
//import com.simpletour.dao.traveltrans.impl.BusNoPlanCapacityHelper;
//import com.simpletour.domain.product.TourismRouteLine;
//import com.simpletour.domain.traveltrans.BusNo;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
///**
// * Created by yangdongfeng on 2015/12/8.
// */
//public class TourismBusAllocation {
//    public static class BusQuantity {
//
//        private Long busId;
//        private Integer quantity;
//
//        public BusQuantity() {
//            this(0L, 0);
//        }
//
//        public BusQuantity(Long busId, Integer quantity) {
//            this.busId = busId;
//            this.quantity = quantity;
//        }
//
//        public Long getBusId() {
//            return busId;
//        }
//
//        public void setBusId(Long busId) {
//            this.busId = busId;
//        }
//
//        public Integer getQuantity() {
//            return quantity;
//        }
//
//        public void setQuantity(Integer quantity) {
//            this.quantity = quantity;
//        }
//
//        @Override
//        public String toString() {
//            return "busId:" + busId + " quantity:" + quantity;
//        }
//    }
//
//    private static List<BusNo.BusCapacity> reduceTransferableMatrix(List<BusNoPlanCapacityHelper.MergeBusNoCapacity> transferableMatrix) {
//        List<BusNo.BusCapacity> overallResult = transferableMatrix.get(0).getBusNoCapacities();
//        if (transferableMatrix.size() > 1) {
//            for (BusNoPlanCapacityHelper  .MergeBusNoCapacity plans : transferableMatrix.subList(1, transferableMatrix.size())) {
//                overallResult = overallResult.stream().map(busCapacity -> {
//                    BusNoPlanCapacityHelper.MergeBusCapacity plan = plans.getMergeBusCapacityByBusId(busCapacity.getBusId().getId());
//                    return new BusNo.BusCapacity(busCapacity.getBusId(),
//                            plan != null ? Integer.min(busCapacity.getCapacity(), plan.getCapacity()) : 0,
//                            plan != null ? Integer.max(busCapacity.getUsedCapacity(), plan.getUsedCapacity()) : 0);
//                }).collect(Collectors.toList());
//            }
//        }
//        return overallResult;
//    }
//
//    /**
//     * 优选方式
//     */
//    public static AllocationStrategy NOTRANSFER_FIRST = (diagrams, tourismRouteLine, quantity) -> {
//        if (diagrams == null || diagrams.isEmpty())
//            return Collections.emptyList();
//        //按照线路内车次之间的换乘关系，两两合并diagrams直到某一个diagram是可换乘的，则产生一个新的digram列，最终得到一个可换乘的矩阵
//        //这里Diagram包含的车辆座位数据表中的三元组为<bus_id, capacity, sold_quantity>
//        List<BusNoPlanCapacityHelper.MergeBusNoCapacity> initResult = new ArrayList<>();
//        initResult.add(new BusNoPlanCapacityHelper.MergeBusNoCapacity(diagrams.get(0)));
//
//        //得到的可换乘向量中，四元组表示<bus_id, capacity, sold_quantity, reduction_times>，reduction_times标识该可换乘列是经过几次合并得到
//        List<BusNoPlanCapacityHelper.MergeBusNoCapacity> transferableMatrix = BusNoPlanCapacityHelper.recursiveMatrix(initResult
//                , diagrams.get(0).isTransferable()
//                , diagrams.subList(1, diagrams.size()));
//        //将可换乘矩阵合并成一个可换乘向量，那么在该向量里取任意一个能够容纳quantity个人的车辆，都是整条车次线路内使用该车
//        List<BusNo.BusCapacity> overallResult = reduceTransferableMatrix(transferableMatrix);
//
//        //将已分配车辆和未分配车辆分开，避免直接分在空车上，摊高成本
//        List<BusNo.BusCapacity> allocated = overallResult.stream()
//                .filter(busCapacity -> busCapacity.getUsedCapacity() > 0).collect(Collectors.toList());
//        List<BusNo.BusCapacity> brandNew = overallResult.stream()
//                .filter(busCapacity -> busCapacity.getCapacity() > 0 && busCapacity.getUsedCapacity() == 0).collect(Collectors.toList());
//
//        //从已经分配的车辆里面挑选
//        List<BusQuantity> firstArrangement = arrange(allocated, quantity, 1);
//        List<BusQuantity> overallArrangement = firstArrangement.isEmpty() ? new ArrayList<>() : firstArrangement;
//
//        //将分车优选方案的优化范围从线路退化到行程内，并应用上述过程
//        if (!overallArrangement.isEmpty())
//            return tourismRouteLine.getBusNoSeries().stream().map(diagram -> overallArrangement).collect(Collectors.toList());
//
//        // 挑选出行程的所有车次的数据
//        List<BusNo.BusNoCapacity> tourismDiagrams = BusNoPlanCapacityHelper.pick(diagrams, tourismRouteLine);
//        if (tourismDiagrams.isEmpty())
//            return tourismRouteLine.getBusNoSeries().stream().map(diagram -> overallArrangement).collect(Collectors.toList());
//        List<BusNoPlanCapacityHelper.MergeBusNoCapacity> initTourismResult = new ArrayList<>();
//        initTourismResult.add(new BusNoPlanCapacityHelper.MergeBusNoCapacity(tourismDiagrams.get(0)));
//
//        // 得到可换乘矩阵
//        List<BusNoPlanCapacityHelper.MergeBusNoCapacity> tourismTransferableMatrix = null;
//        if (tourismDiagrams.size() > 1)
//            tourismTransferableMatrix = BusNoPlanCapacityHelper.recursiveMatrix(initTourismResult, tourismDiagrams.get(0).isTransferable(),
//                    tourismDiagrams.subList(1, tourismDiagrams.size()));
//        else
//            tourismTransferableMatrix = initTourismResult;
//
//        // 可换乘矩阵转换为只有一列的矩阵
//        List<BusNo.BusCapacity> overallTourismResult = reduceTransferableMatrix(tourismTransferableMatrix);
//
//        // 已分配车辆
//        List<BusNo.BusCapacity> tourismAllocated = overallTourismResult.stream()
//                .filter(busCapacity -> !brandNew.stream().map(busCapacity1 -> busCapacity1.getBusId().getId()).collect(Collectors.toList()).contains(busCapacity.getBusId().getId()))
//                .collect(Collectors.toList());
//        // 未分配车辆
//        List<BusNo.BusCapacity> tourismBrandNew = overallTourismResult.stream()
//                .filter(busCapacity ->
//                        brandNew.stream().map(busCapacity1 -> busCapacity1.getBusId().getId()).collect(Collectors.toList()).contains(busCapacity.getBusId().getId()))
//                .collect(Collectors.toList());
//
//        // 排车
//        List<BusQuantity> tourismFirstArrangement = arrange(tourismAllocated, quantity, 1);
//        List<BusQuantity> tourismOverallArrangement = tourismFirstArrangement.isEmpty() ? new ArrayList<>() : tourismFirstArrangement;
//        Optional<BusNo.BusCapacity> alternative = tourismBrandNew.stream().filter(busCapacity -> busCapacity.getCapacity() >= quantity).findFirst();    // 选一辆没用的车
//
//        if (tourismFirstArrangement.isEmpty() && alternative.isPresent()) { // 如果行程在已分配车辆中分配失败，则使用新车
//            tourismOverallArrangement.add(new BusQuantity(alternative.get().getBusId().getId(), quantity));
//        }
//        if (!tourismOverallArrangement.isEmpty())
//            overallArrangement.addAll(tourismOverallArrangement);
//
//        // 这里：因为将 矩阵降为一列(reduceTransferableMatrix)，所有结果中同一行的每一个车次都使用相同的结果
//        return tourismRouteLine.getBusNoSeries().stream().map(diagram -> overallArrangement).collect(Collectors.toList());
//    };
//
//    /**
//     * 低保证方式
//     */
//    public static AllocationStrategy TOGETHER_FIRST = (diagrams, tourismRouteLine, quantity) -> {
//        if (!(diagrams == null || diagrams.isEmpty())) {
//            List<List<BusNo.BusNoCapacity>> sectionPlans = new ArrayList<>();
//            for (int i = 0, j = 0; i < diagrams.size() && j < tourismRouteLine.getBusNoSeries().size(); ++i) {
//                if (sectionPlans.isEmpty()) sectionPlans.add(new ArrayList<>());
//                if (tourismRouteLine.getBusNoSeries().get(j).equals(diagrams.get(i).getBusNoId())) {
//                    sectionPlans.get(sectionPlans.size() - 1).add(diagrams.get(i));
//                    j += 1;
//                }
//                if (diagrams.get(i).isTransferable()
//                        && !sectionPlans.get(sectionPlans.size() - 1).isEmpty()
//                        && j < tourismRouteLine.getBusNoSeries().size()) sectionPlans.add(new ArrayList<>());
//            }
//            List<List<BusQuantity>> lowGuaranteeResult = new ArrayList<>();
//            sectionPlans.stream().filter(sectionPlan -> !sectionPlan.isEmpty()).forEach(sectionPlan -> {
//                List<BusNoPlanCapacityHelper.MergeBusNoCapacity> initResult = new ArrayList<>();
//                initResult.add(new BusNoPlanCapacityHelper.MergeBusNoCapacity(sectionPlan.get(0)));
//                List<BusNoPlanCapacityHelper.MergeBusNoCapacity> interVector = BusNoPlanCapacityHelper.recursiveMatrix(initResult
//                        , sectionPlan.get(0).isTransferable()
//                        , sectionPlan.size() > 1 ? sectionPlan.subList(1, sectionPlan.size()) : Collections.emptyList());
//                List<BusNo.BusCapacity> localResult = interVector.get(0).getBusNoCapacities();
//                List<BusNo.BusCapacity> sortedPlan = localResult.stream()
//                        .sorted((busCapacity1, busCapacity2) ->
//                                (busCapacity2.getCapacity() - busCapacity2.getUsedCapacity()) - ((busCapacity1.getCapacity() - busCapacity1.getUsedCapacity())))
//                        .collect(Collectors.toList());
//                List<BusQuantity> interResult = Collections.emptyList();
//                List<BusQuantity> comboAlternative = new ArrayList<>();
//                int i = 0, j = quantity;
//                for (; i < sortedPlan.size() && j >= 0; ++i) {
//                    int capacity = sortedPlan.get(i).getCapacity() - sortedPlan.get(i).getUsedCapacity();
//                    comboAlternative.add(new BusQuantity(sortedPlan.get(i).getBusId().getId(), capacity > j ? j : capacity));
//                    j -= capacity;
//                }
//                if (j <= 0) interResult = comboAlternative;//能装下，则方案可用
//                if (!interResult.isEmpty()) {
//                    for (int it = 0; it < sectionPlan.size(); ++it) lowGuaranteeResult.add(interResult);
//                }
//            });
//            return lowGuaranteeResult;
//        }
//        return Collections.emptyList();
//    };
//
//
//    /**
//     * 自适应方式，当优选方案失败时，直接退化到低保证方案
//     */
//    public static AllocationStrategy ADAPTIVE = (diagrams, tourismRouteLine, quantity) -> {
//        if (!(diagrams == null || diagrams.isEmpty()
//                || tourismRouteLine == null || tourismRouteLine.getBusNoSeries() == null || tourismRouteLine.getBusNoSeries().isEmpty())) {
//            List<List<BusQuantity>> result = NOTRANSFER_FIRST.allocate(diagrams, tourismRouteLine, quantity);
//            if (result.isEmpty() || result.stream().anyMatch(tuples -> tuples.isEmpty())) {
//                result = TOGETHER_FIRST.allocate(diagrams, tourismRouteLine, quantity);
//            }
//            return result;
//        }
//        return Collections.emptyList();
//    };
//
//    private static Integer compareBusCapacity(BusNo.BusCapacity busCapacity1, BusNo.BusCapacity busCapacity2, int average) {
//        return Math.abs(busCapacity1.getCapacity() - busCapacity1.getUsedCapacity() - average) -
//                Math.abs(busCapacity2.getCapacity() - busCapacity2.getUsedCapacity() - average);    // 剩余容量和平均值的差排序
//    }
//
//    private static List<BusNo.BusCapacity> sortPlans(List<BusNo.BusCapacity> plans, int average) {
//        return plans.stream()
//                .sorted((busCapacity1, busCapacity2) -> compareBusCapacity(busCapacity1, busCapacity2, average))
//                .collect(Collectors.toList());
//    }
//
//    public static List<BusQuantity> arrange(List<BusNo.BusCapacity> plans, int quantity, int busNum) {
//        if (busNum > 0 && plans.size() > 0) {//递归终结条件
//            int average = quantity / busNum;
//            if (average >= 1) {//保证没有人在单独的一辆车
//                List<BusNo.BusCapacity> sortedPlans = sortPlans(plans, average);
//                int i = 0;
//                do {
//                    List<BusQuantity> busQuantities = new ArrayList<>();
//
//                    int capacity = sortedPlans.get(i).getCapacity() - sortedPlans.get(i).getUsedCapacity();
//                    BusQuantity current = new BusQuantity(sortedPlans.get(i).getBusId().getId(), capacity > quantity ? quantity : capacity);
//                    busQuantities.add(current);
//                    busQuantities.addAll(arrange(sortedPlans.subList(i + 1, sortedPlans.size()), quantity - current.getQuantity(), busNum - 1));
//
//                    int arrangedQuantity = busQuantities.stream().map(busQuantity -> busQuantity.getQuantity()).reduce(0, Integer::sum);
//                    if (arrangedQuantity >= quantity)   // 已经全部安排完成，则返回
//                        return busQuantities;
//
//                    i += 1;
//                } while (i < sortedPlans.size());
//                return arrange(plans, quantity, busNum + 1);
//            }
//        }
//        return Collections.emptyList();
//    }
//
//    @FunctionalInterface
//    public interface AllocationStrategy {
//
//        List<List<BusQuantity>> allocate(List<BusNo.BusNoCapacity> diagrams, TourismRouteLine tourismRouteLine, int quantity);
//
//    }
//}
