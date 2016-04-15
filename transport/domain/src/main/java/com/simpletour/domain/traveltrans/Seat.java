package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;

/**
 * 座位
 * Created by Mario on 2015/11/20.
 */
@Entity
@Table(name = "TRANS_SEAT")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class Seat extends BaseDomain {
    //枚举默认顺序为0,1,2......
    public enum Type {
        EMPTY("空"), SEAT("座位");
        private String remark;

        Type(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }


    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;


    /**
     * 座位布局信息
     */
    @ManyToOne
    @JoinColumn(name = "seatlayout_id")
    @JSONField(serialize = false)
    private SeatLayout seatLayout;

    /**
     * 座位类型编号,l:座位，0:空
     * 枚举默认顺序为0,1,2
     * 默认为空
     */
    @Column
    private Type type = Type.EMPTY;

    /**
     * 座位行号
     */
    @Column(name = "row_no")
    private Integer rowNo;

    /**
     * 座位列号
     */
    @Column(name = "col_no")
    private String colNo;

    /**
     * constructor
     */
    public Seat() {
    }

    /**
     * constructor
     *
     * @param type
     * @param rowNo
     * @param colNo
     */
    public Seat(Type type, Integer rowNo, String colNo) {
        this.type = type;
        this.rowNo = rowNo;
        this.colNo = colNo;
    }

    /**
     * constructor
     *
     * @param seatLayout
     * @param type
     * @param rowNo
     * @param colNo
     */
    public Seat(SeatLayout seatLayout, Type type, Integer rowNo, String colNo) {
        this.seatLayout = seatLayout;
        this.type = type;
        this.rowNo = rowNo;
        this.colNo = colNo;
    }

    /**
     * constructor
     *
     * @param id
     * @param seatLayout
     * @param type
     * @param rowNo
     * @param colNo
     */
    public Seat(Long id, SeatLayout seatLayout, Type type, Integer rowNo, String colNo) {
        this.id = id;
        this.seatLayout = seatLayout;
        this.type = type;
        this.rowNo = rowNo;
        this.colNo = colNo;
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

    public SeatLayout getSeatLayout() {
        return seatLayout;
    }

    public void setSeatLayout(SeatLayout seatLayout) {
        this.seatLayout = seatLayout;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    public String getColNo() {
        return colNo;
    }

    public void setColNo(String colNo) {
        this.colNo = colNo;
    }

}
