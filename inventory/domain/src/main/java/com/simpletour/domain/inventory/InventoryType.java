package com.simpletour.domain.inventory;

/**
 * 文件描述：库存类型枚举类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016-4-19
 * 备注说明：仅保留车次和元素两种库存类型
 * @since 2.0-SNAPSHOT
 */
public enum InventoryType {
    bus_no("com.simpletour.domain.traveltrans.BusNo", "trans_busno", "车次"),
    procurement("com.simpletour.domain.resources.Procurement", "tr_procurement", "元素");

    private String className;

    private String tabelName;

    private String remark;

    InventoryType(String className, String tabelName, String remark) {
        this.className = className;
        this.tabelName = tabelName;
        this.remark = remark;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTabelName() {
        return tabelName;
    }

    public void setTabelName(String tabelName) {
        this.tabelName = tabelName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
