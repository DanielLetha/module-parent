package com.simpletour.domain.product;

import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.domain.traveltrans.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yangdongfeng on 2015/12/8.
 */

/**
 * 某段行程分布在某个线路上的车次的集合
 */
public class TourismRouteLine {

    private Line line;      // 线路

    private List<BusNo> busNoSeries;    // 行程 在 线路 上的 车次序列

    private Integer offset; // 行程相对线路上的车次序列的偏移

    TourismRouteLine(){
        this(null, 0);
    }

    TourismRouteLine(Line line, Integer offset){
        this(line, offset, new ArrayList<>());
    }

    TourismRouteLine(Line line, Integer offset, List<BusNo> busNoSeries){
        this.line = line;
        this.offset = offset;
        this.busNoSeries = busNoSeries;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<BusNo> getBusNoSeries() {
        return busNoSeries;
    }

    public void setBusNoSeries(List<BusNo> busNoSeries) {
        this.busNoSeries = busNoSeries;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * 根据一个行程的tourismRoutes 获取一个TourismRouteLine的列表
     * 即将一个行程的TourismRoutes 按照线路 聚合到TourismRouteLine列表中
     * @param tourismRoutes
     * @return
     */
    public static List<TourismRouteLine> getTourismRouteLineFromTourismRoutes(List<TourismRoute> tourismRoutes){
        if (tourismRoutes == null || tourismRoutes.isEmpty())
            return Collections.emptyList();

        List<TourismRouteLine> tourismRouteLines = new ArrayList<>();
        for (TourismRoute tourismRoute : tourismRoutes) {
            TourismRouteLine tourismRouteLine = null;

            for (int i=tourismRouteLines.size()-1; i>=0; --i){  //正常情况下就应该是最后一个，所以倒着查
                TourismRouteLine tmp = tourismRouteLines.get(i);
                if (tmp.getLine().getId().equals(tourismRoute.getLine().getId())
                        && tmp.getOffset().equals(tourismRoute.getLineOffset())){
                    tourismRouteLine = tmp;
                    break;
                }
            }

            if (tourismRouteLine == null) {
                tourismRouteLine = new TourismRouteLine(tourismRoute.getLine(), tourismRoute.getLineOffset());
                tourismRouteLines.add(tourismRouteLine);
            }
            tourismRouteLine.getBusNoSeries().add(tourismRoute.getBusNo());
        }

        return tourismRouteLines;
    }
}


