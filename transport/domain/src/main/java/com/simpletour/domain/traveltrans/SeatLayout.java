package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 座位布局信息
 * Created by Mario on 2015/11/20.
 */
@Entity
@Table(name = "TRANS_SEATLAYOUT")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class SeatLayout extends BaseDomain {

    public enum Type {
        large("大型"), medium("中型"), mini("微型"), others("其他");
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
     * 座位布局名称
     */
    @Column
    private String name;

    /**
     * 类型:大型|中型|微型|其他
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    /**
     * 列数
     */
    @Column
    private Integer row;

    /**
     * 行数
     */
    @Column
    private Integer col;

    /**
     * 座位总数
     */
    @Column
    private Integer capacity;

    @OneToMany(mappedBy = "seatLayout", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seatList = new ArrayList<>();

    /**
     * constructor
     */
    public SeatLayout() {

    }

    /**
     * constructor
     *
     * @param name
     * @param type
     * @param row
     * @param col
     * @param capacity
     * @param seatList
     */
    public SeatLayout(String name, Type type, Integer row, Integer col, Integer capacity, List<Seat> seatList) {
        this.name = name;
        this.type = type;
        this.row = row;
        this.col = col;
        this.capacity = capacity;
        this.seatList = seatList;
    }

    /**
     * constructor
     *
     * @param id
     * @param name
     * @param type
     * @param row
     * @param col
     * @param capacity
     * @param seatList
     */
    public SeatLayout(Long id, String name, Type type, Integer row, Integer col, Integer capacity, List<Seat> seatList) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.row = row;
        this.col = col;
        this.capacity = capacity;
        this.seatList = seatList;
    }

    /**
     * getter & setter
     */


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Seat> getSeatList() {
        if (seatList == null) {
            seatList = new ArrayList<Seat>();
        }
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList.clear();
        this.seatList.addAll(seatList);
    }

    /**
     * 判断容量是否正确
     *
     * @return true/false
     */
    public Boolean legalCapacity() {
        if (row == null || col == null || seatList == null || seatList.size() != row * col) {
            return false;
        }
        int tmpCap = seatList.size();
        for (Seat seat : seatList) {
            if (Seat.Type.EMPTY.equals(seat.getType())) {
                tmpCap--;
            }
        }
        return tmpCap == capacity;
    }

}
