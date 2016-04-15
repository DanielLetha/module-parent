package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

/**
 * 节点状态表
 * Created by Mario on 2015/11/21.
 */
@Entity
@Table(name = "TRANS_NODESTATUS")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class NodeStatus extends BaseDomain {

    public enum Status {
        arrived("到达", "arrived"), finished("完成", "finished");

        private String remark;

        private String value;

        Status(String remark, String value) {
            this.remark = remark;
            this.value = value;
        }

        public String getRemark() {
            return remark;
        }

        public String getValue() {
            return value;
        }
    }



    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;


    /**
     * 对应车次
     */
    @ManyToOne
    @JoinColumn(name = "bus_no_id")
    private BusNo busNo;

    /**
     * 对应汽车信息
     */
    @ManyToOne
    @JoinColumn(name = "bus_id")
    @JSONField(serialize = false)
    private Bus bus;

    /**
     * 具体哪一天
     */
    @Column
    @Temporal(TemporalType.DATE)
    private Date day;

    /**
     * 节点编号
     */
    @OneToOne
    @JoinColumn(name = "node_id")
    @JSONField(serialize = false)
    private Node node;

    /**
     * 时间
     */
    @Column
    private Integer timing;
    /**
     * 节点状态
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Status status = Status.arrived;

    /**
     * constructor
     */
    public NodeStatus() {
    }

    /**
     * constructor
     *
     * @param busNo
     * @param bus
     * @param day
     * @param node
     * @param timing
     * @param status
     */
    public NodeStatus(BusNo busNo, Bus bus, Date day, Node node, Integer timing, Status status) {
        this.busNo = busNo;
        this.bus = bus;
        this.day = day;
        this.node = node;
        this.timing = timing;
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    public BusNo getBusNo() {
        return busNo;
    }

    public void setBusNo(BusNo busNo) {
        this.busNo = busNo;
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

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Integer getTiming() {
        return timing;
    }

    public void setTiming(Integer timing) {
        this.timing = timing;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
