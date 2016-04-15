package com.simpletour.domain.inventory.query;

import java.util.Date;

/**
 * 文件描述：检查可用库存所需条件字段的封装类
 * 创建人员：石广路
 * 创建日期：2015/12/8 16:24
 * 备注说明：null
 */
public class StockAvailableKey {
    // 订购库存对象
    private IStockTraceable stockTraceable;

    // 订购日期
    private Date day;

    // 订购数量
    private int demandQuantity;

    public StockAvailableKey(IStockTraceable stockTraceable, Date day, int demandQuantity) {
        this.stockTraceable = stockTraceable;
        this.day = day;
        this.demandQuantity = demandQuantity;
    }

    public IStockTraceable getStockTraceable() {
        return stockTraceable;
    }

    public void setStockTraceable(IStockTraceable stockTraceable) {
        this.stockTraceable = stockTraceable;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public int getDemandQuantity() {
        return demandQuantity;
    }

    public void setDemandQuantity(int demandQuantity) {
        this.demandQuantity = demandQuantity;
    }

    @Override
    public String toString() {
        StockKey stockKey = stockTraceable.getStockKey();
        if (null == stockKey) {
            return null;
        }
        return "StockAvailableKey{" +
                "stockTraceable=" + stockKey.toString() +
                ", stockDependencies=" + stockTraceable.getDependentStockKeys().toString() +
                ", day=" + day +
                ", demandQuantity=" + demandQuantity +
                '}';
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockAvailableKey)) return false;

        StockAvailableKey that = (StockAvailableKey) o;

        if (!stockTraceable.getStockKey().equals(that.getStockTraceable().getStockKey())) return false;
        //if (demandQuantity != that.getDemandQuantity()) return false;

        // 库存日期精度为天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String thisDay = sdf.format(day);
        String thatDay = sdf.format(that.getDay());
        return thisDay.equals(thatDay);
    }

    @Override
    public int hashCode() {
        int result = getStockTraceable().hashCode();
        result = 31 * result + getDay().hashCode();
        //result = 31 * result + 0 < demandQuantity ? Integer.hashCode(demandQuantity) : 0;
        return result;
    }

    @Override
    public Object clone() {
        StockAvailableKey stockAvailableKey = null;
        try {
            stockAvailableKey = (StockAvailableKey)super.clone();
        } catch (CloneNotSupportedException exc) {
            exc.printStackTrace();
            return null;
        }
        stockAvailableKey.stockTraceable = stockTraceable;  // 保持相同引用即可
        stockAvailableKey.day = null == day ? null : (Date)day.clone();
        stockAvailableKey.demandQuantity = demandQuantity;
        return stockAvailableKey;
    }
    */
}
