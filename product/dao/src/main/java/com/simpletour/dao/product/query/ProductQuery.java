package com.simpletour.dao.product.query;


import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.FieldMatcher;

/**
 * Created by yangdongfeng on 2016/3/23.
 */
public class ProductQuery extends ConditionOrderByQuery {
    public static FieldMatcher fieldMatcher = new FieldMatcher();
    static {
        fieldMatcher.put("name", "c.name")
                .put("depart", "c.depart")
                .put("arrive", "c.arrive")
                .put("shuttle", "c.shuttle")
                .put("days", "c.days")
                .put("isDel", "c.del")
                .put("isOnline", "c.online")
                .put("type", "c.type")
                .put("tenantId", "c.tenant_id");
    }

    @Override
    public FieldMatcher getFieldMatcher() {
        return fieldMatcher;
    }
}
