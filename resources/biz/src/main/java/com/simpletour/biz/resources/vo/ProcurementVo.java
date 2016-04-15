package com.simpletour.biz.resources.vo;


import com.simpletour.commons.data.domain.BaseDomain;

/**
 * 元素值对象
 * <p>
 * 为前端封装数据，并返回给前端
 * <p>
 * Created by Jeff.Song on 2015/12/1.
 */
public class ProcurementVo extends BaseDomain {


    private Long id;

    /**
     * 元素提供的资源类型，对应着row_table
     */
    private String resourceType;

    /**
     * 资源ID，对应着row_id
     */
    private String resourceName;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 元素名称
     */
    private String name;

    /**
     * 住宿点所在地
     */
    private String destination;

    /**
     * 状态：true：上线，false：下线
     */
    private boolean online = true;

    public ProcurementVo() {

    }

    public ProcurementVo(long id, String resourceType, String resourceName, String remark, String name, String destination, boolean online) {
        this.id = id;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.remark = remark;
        this.name = name;
        this.destination = destination;
        this.online = online;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
