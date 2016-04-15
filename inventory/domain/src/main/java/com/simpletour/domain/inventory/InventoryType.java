package com.simpletour.domain.inventory;

/**
 * 库存类型
 * <p>
 * 1：行程;
 * 2：车次;
 * 3：元素;
 * 4：产品;
 * <p>
 * Created by Jeff.Song on 2015/11/23.
 */
public enum InventoryType {

    bus_no("com.simpletour.domain.traveltrans.BusNo", "trans_busno", "车次"),
    procurement("com.simpletour.domain.resources.Procurement", "tr_procurement", "元素"),
    product("com.simpletour.domain.product.Product", "prod_product", "产品");

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
