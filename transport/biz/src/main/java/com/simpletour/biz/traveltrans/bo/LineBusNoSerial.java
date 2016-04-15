package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.domain.traveltrans.BusNoSerial;
import com.simpletour.domain.traveltrans.Line;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author:  igotti
 * Mail  :  igotti@simpletour.com
 * Date  :  2016/3/25
 *
 * 线路车次序列中的元素，包含每个车次在线路中的偏移、是否可换乘等
 *
 * since :  2.0.0-SNAPSHOT
 */
public class LineBusNoSerial {


    private BusNo busNo;

    private Integer offset;

    private boolean transferable;

    public LineBusNoSerial(BusNoSerial busNoSerial){
        this(busNoSerial.getBusNo(), busNoSerial.getDay(), busNoSerial.getBusNo().isTransferable());
    }

    public LineBusNoSerial(BusNo busNo, boolean transferable) {
        this(busNo, 0, transferable);
    }

    public LineBusNoSerial(BusNo busNo, Integer offset, boolean transferable) {
        this.busNo = busNo;
        this.offset = offset;
        this.transferable = transferable;
    }

    public BusNo getBusNo() {
        return busNo;
    }

    public void setBusNo(BusNo busNo) {
        this.busNo = busNo;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public boolean isTransferable() {
        return transferable;
    }

    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
    }

    public static List<LineBusNoSerial> from(Line line) {
        if(!(line==null||line.getBusNoSeries()==null||line.getBusNoSeries().stream().anyMatch(busNoSerial -> busNoSerial.getBusNo()==null))){
            return line.getBusNoSeries().stream().map(LineBusNoSerial::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
