package com.simpletour.domain.inventory;

import com.simpletour.domain.inventory.query.StockKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 文件描述：库存及价格信息封装类
 * 创建人员：石广路（shiguanglu@simpletour.com）
 * 创建日期：2016/4/21 20:04
 * 备注说明：null
 *
 * @since 2.0-SNAPSHOT
 */
public class StockPrice {
    /**
     * 库存ID，仅在更新库存信息时会用到
     */
    //private Long id;

    /**
     * 库存组合键
     */
    private StockKey stockKey;

    /**
     * 库存日期列表，用于单次或批量添加或更新库存信息
     */
    private List<Date> days;

    /**
     * 库存数量
     */
    private Integer quantity;

    /**
     * 库存价格列表，可同时包含一个成人和儿童的价格
     */
    private List<Price> prices;

    /**
     * 库存状态，默认为上线
     */
    private boolean online = true;

    public StockPrice() {
    }

    /**
     * 添加单条或多条库存及价格信息
     * @param stockKey
     * @param days
     * @param quantity
     * @param prices
     * @param online
     */
    public StockPrice(StockKey stockKey, List<Date> days, int quantity, List<Price> prices, boolean online) {
        //this.id = id;
        this.stockKey = stockKey;
        this.days = days;
        this.quantity = quantity;
        this.prices = prices;
        this.online = online;
    }

//    public StockPrice(StockKey stockKey, List<LocalDate> days, int quantity, List<Price> prices, boolean online) {
//        this.stockKey = stockKey;
//        this.quantity = quantity;
//        this.prices = prices;
//        this.online = online;
//    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public StockKey getStockKey() {
        return stockKey;
    }

    public void setStockKey(StockKey stockKey) {
        this.stockKey = stockKey;
    }

    public List<Date> getDays() {
        return days;
    }

    public void setDays(List<Date> days) {
        this.days = days;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Stock toStock() {
        if (null != days && !days.isEmpty()) {
            return new Stock(stockKey.getInventoryType(), stockKey.getInventoryId(), days.get(0), quantity, online);
        }
        return null;
    }

    public List<Stock> toStocks() {
        if (null != days && !days.isEmpty()) {
            InventoryType inventoryType = stockKey.getInventoryType();
            Long inventoryId = stockKey.getInventoryId();
            List<Stock> stocks = new ArrayList<>(days.size());
            days.stream().filter(day -> null != day).forEach(day -> stocks.add(new Stock(inventoryType, inventoryId, day, quantity, online)));
            return stocks;
        }
        return Collections.emptyList();
    }
}
