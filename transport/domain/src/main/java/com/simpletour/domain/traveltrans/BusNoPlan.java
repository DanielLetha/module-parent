package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Mario on 2015/11/21.
 */
@Entity
@Table(name = "TRANS_BUSNOPLAN")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class BusNoPlan extends BaseDomain implements IDependTracable {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;


    /**
     * 车次主键编号
     */
    @ManyToOne
    @JoinColumn(name = "bus_no_id")
    private BusNo no;

    /**
     * 汽车编号
     */
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    /**
     * 哪一天，这个地方用整数表示，方便做车辆运行周期的计算。该值模86400一定是-8*60*60（北京时间）
     */
    @Column
    @Temporal(value = TemporalType.DATE)
    private Date day;

    /**
     *
     */
    @Column
    private Integer capacity;


    /**
     * 行车助理
     */
    @ManyToOne
    @JoinColumn(name = "assistant_id")
    private Assistant assistant;


    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependencies=Arrays.asList(new Dependency(bus.getEntityKey()),new Dependency(no.getEntityKey()));
        if(assistant!=null)
            dependencies.add(new Dependency(assistant.getEntityKey()));
        return dependencies;
    }

//    @Override
//    @JSONField(serialize = false)
//    public List<DependEntity> getDependEntities() {
//        List<DependEntity> dependEntities = new ArrayList<>();
//        dependEntities.add(new DependEntity(QueryUtil.getTableName(BusNoPlan.class), this.id, QueryUtil.getTableName(Bus.class), this.bus.getId()));
//        dependEntities.add(new DependEntity(QueryUtil.getTableName(BusNoPlan.class), this.id, QueryUtil.getTableName(BusNo.class), this.no.getId()));
//        if (assistant != null) {
//            dependEntities.add(new DependEntity(QueryUtil.getTableName(BusNoPlan.class), this.id, QueryUtil.getTableName(Assistant.class), this.assistant.getId()));
//        }
//        return dependEntities;
//    }
}
