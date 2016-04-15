package com.simpletour.domain.traveltrans;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.dao.query.QueryUtil;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 车次和线路对应表
 * Created by Mario on 2015/11/21.
 */
@Entity
@Table(name = "TRANS_BUSNOSERIAL")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class BusNoSerial extends BaseDomain implements IDependTracable {


    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;


    /**
     * 线路id,关联线路表
     */
    @ManyToOne
    @JoinColumn(name = "line_id")
    @JSONField(serialize = false)
    private Line line;

    /**
     * 对应车次信息
     */
    @ManyToOne
    @JoinColumn(name = "bus_no_id")
    @JSONField(serialize = false)
    private BusNo busNo;

    /**
     * 天数（表示当前车次序列属于该线路的第几天）
     * 0代表第一天，1代表第二天，2代表第三天，以此类推
     */
    @Column
    private Integer day;

    /**
     * 排序
     */
    @Column
    private Integer sort;

    public BusNoSerial() {

    }

    /**
     * constructor
     *
     * @param line
     * @param busNo
     * @param day
     * @param sort
     */
    public BusNoSerial(Line line, BusNo busNo, Integer day, Integer sort) {
        this.line = line;
        this.busNo = busNo;
        this.day = day;
        this.sort = sort;
    }

    public BusNoSerial(BusNo busNo, Integer day, Integer sort) {
        this.busNo = busNo;
        this.day = day;
        this.sort = sort;
    }

    /**
     * setter & getter
     */

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public BusNo getBusNo() {
        return busNo;
    }

    public void setBusNo(BusNo busNo) {
        this.busNo = busNo;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

//    @Override
//    @JSONField(serialize = false)
//    public List<DependEntity> getDependEntities() {
//        List<DependEntity> dependEntities = new ArrayList<>();
//        dependEntities.add(new DependEntity(QueryUtil.getTableName(BusNoSerial.class), this.id, QueryUtil.getTableName(BusNo.class), this.busNo.getId()));
//        dependEntities.add(new DependEntity(QueryUtil.getTableName(BusNoSerial.class), this.id, QueryUtil.getTableName(Line.class), this.line.getId()));
//        return dependEntities;
//    }

    @Override
    public List<Dependency> getDependencies() {
        return Arrays.asList(new Dependency(busNo.getEntityKey()),new Dependency(line.getEntityKey()));
    }
}
