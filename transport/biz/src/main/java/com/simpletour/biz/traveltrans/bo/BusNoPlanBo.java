package com.simpletour.biz.traveltrans.bo;

import com.simpletour.domain.traveltrans.Assistant;
import com.simpletour.domain.traveltrans.Bus;
import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.domain.traveltrans.BusNoPlan;

import java.util.Date;

/**
 * Created by Mario on 2015/12/2.
 */
public class BusNoPlanBo {
    private Long id;
    /**
     * 车次主键编号
     */
    private BusNo no;

    /**
     * 汽车编号
     */
    private Bus bus;

    /**
     * 哪一天，这个地方用整数表示，方便做车辆运行周期的计算。该值模86400一定是-8*60*60（北京时间）
     */
    private Date day;

    /**
     *
     */
    private Integer capacity;

    private Assistant assistant;

    private Integer soldQuantity;

    public BusNoPlanBo() {
    }

    public BusNoPlanBo(BusNoPlan busNoPlan) {
        this.id = busNoPlan.getId();
        this.no = busNoPlan.getNo();
        this.bus = busNoPlan.getBus();
        this.day = busNoPlan.getDay();
        this.capacity = busNoPlan.getCapacity();
        this.assistant = busNoPlan.getAssistant();
    }

    public BusNoPlanBo(BusNoPlan busNoPlan,Integer soldQuantity){
        this(busNoPlan);
        this.soldQuantity=soldQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusNo getNo() {
        return no;
    }

    public void setNo(BusNo no) {
        this.no = no;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
